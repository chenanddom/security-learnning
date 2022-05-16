package com.itdom.edge.configuration;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.itdom.commen.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ResourceManager implements ReactiveAuthorizationManager<AuthorizationContext> {


    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        //匹配url
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        //从Redis中获取当前路径可访问角色列表
        URI uri = authorizationContext.getExchange().getRequest().getURI();
        //请求方法 POST,GET
        String method = authorizationContext.getExchange().getRequest().getMethodValue();
        /**
         * TODO 为了适配restful接口，比如 GET:/api/.... POST:/api/....  *:/api/.....  星号匹配所有
         */
        String restFulPath = method + ":" + uri.getPath();
        //获取所有的uri->角色对应关系
        Map<String, List<String>> entries = new HashMap<String, List<String>>(){{
            put("GET:/product/list",new ArrayList<String>(){{
                add("ROLE_admin");
                add("ROLE_normal");
                add("ROLE_user");
            }});
            put("GET:/product-management/product/list",new ArrayList<String>(){{
                add("ROLE_admin");
                add("ROLE_normal");
                add("ROLE_user");
            }});
        }};
        //角色集合
        List<String> authorities = new ArrayList<>();
        entries.forEach((path, roles) -> {
            //路径匹配则添加到角色集合中
            if (antPathMatcher.match(path, restFulPath)) {
                authorities.addAll(roles);
            }
        });
        //认证通过且角色匹配的用户可访问当前路径
        return mono
                //判断是否认证成功
                .filter(Authentication::isAuthenticated)
                //获取认证后的全部权限
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                //如果权限包含则判断为true
                .any(authority->{
                    //超级管理员直接放行
                    if (StrUtil.equals("ROLE_admin",authority))
                        return true;
                    //其他必须要判断角色是否存在交集
                    return CollectionUtil.isNotEmpty(authorities) && authorities.contains(authority);
                })
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }
}

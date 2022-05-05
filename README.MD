# SpringSecurity






## 校验获取资源需要某些权限的校验
```java
@Service
public class UserServiveDetailImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//            如果查找不到就会抛出UsernameNotFoundException
        if (!"admin".equalsIgnoreCase(username)){
            throw new UsernameNotFoundException("用户名不存在");
        }
        String encode = passwordEncoder.encode("test123");
        //表名当前用户是由admin和normol角色的
        return new User(username,encode, AuthorityUtils.commaSeparatedStringToAuthorityList("admin,normol"));
    }
}

```
此时如果需要校验当前的登录的用户是否由某个权限才能跳转到某个资源，需要如下的配置
```java
   .successForwardUrl("/toMain")

  .antMatchers("/Home.html").hasAuthority("admin2")
```
如果没有某个权限就访问需要具体某个权限的资源就会有如下的返回提示：

![](./files\访问资源需要某个具体的权限.PNG)

如果需要校验某个用户是否有某个角色才能访问某个资源时，可以使用以下的配置
```java
@Service
public class UserServiveDetailImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//            如果查找不到就会抛出UsernameNotFoundException

        if (!"admin".equalsIgnoreCase(username)){
            throw new UsernameNotFoundException("用户名不存在");
        }
        String encode = passwordEncoder.encode("test123");
        //角色的配置需要使用ROLE_xx的方式，否则就是权限
        return new User(username,encode, AuthorityUtils.commaSeparatedStringToAuthorityList("admin,normol,ROLE_user"));
    }
}

```
访问路径的配置如下：
```java
//校验访问需要某个具体角色的资源需要的角色
.antMatchers("/Home.html").hasRole("user2")
//用户有多个角色，只要有其中一个匹配就可以了.
.antMatchers("/Home.html").hasAnyRole("user2","user")
```




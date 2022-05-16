package com.itdom.edge.exception;

import com.itdom.commen.ResultCode;
import com.itdom.commen.ReturnResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关全局异常处理
 * 优先级一定要比ResponseStatusExceptionHandler低
 */
@Slf4j
@Order(-1)
@Component
@RequiredArgsConstructor
public class GlobalErrorHandler implements ErrorWebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        ReturnResult<Object> result = ReturnResult.failed(ResultCode.ACCESS_UNAUTHORIZED);
        // JOSN格式返回
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }
        //处理token异常
        if (ex instanceof InvalidTokenException) {
            result = ReturnResult.failed(ResultCode.TOKEN_INVALID_OR_EXPIRED);
        }

        ReturnResult finalResult = result;
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory dataBufferFactory = response.bufferFactory();
            try {
                //todo 返回响应结果，根据业务需求，自己定制
                return dataBufferFactory.wrap(new ObjectMapper().writeValueAsBytes(finalResult));
            } catch (Exception e) {
                log.error("Error writing response", ex);
                return dataBufferFactory.wrap(new byte[0]);
            }
        }));
    }
}

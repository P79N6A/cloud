package com.springframework.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

/**
 * @author summer
 * 2018/7/2
 */
@Configuration
public class RouteConfig {

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route(GET("/time"), timeFunction)
                .andRoute(GET("/date"), dateFunction);
    }

    /**
     * 返回包含时间字符串的ServerResponse
     */
    private HandlerFunction<ServerResponse> timeFunction =
            request -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(
                    Mono.just("Now is " + new SimpleDateFormat("HH:mm:ss").format(new Date())), String.class);

    /**
     * 返回包含日期字符串的ServerResponse
     */
    private HandlerFunction<ServerResponse> dateFunction =
            request -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(
                    Mono.just("Today is " + new SimpleDateFormat("yyyy-MM-dd").format(new Date())), String.class);
}

package com.zeny.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName GatewayConfig
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/6 0006 0:20
 */
@Configuration
public class GatewayConfig {

   /* @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("path_route_gateway",
                r-> r.path("/**")
                        .uri("https://news.baidu.com/")).build();
        return routes.build();
    }*/
}

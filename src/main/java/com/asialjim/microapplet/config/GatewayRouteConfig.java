/*
 *    Copyright 2014-2025 <a href="mailto:asialjim@qq.com">Asial Jim</a>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.asialjim.microapplet.config;

import com.asialjim.microapplet.filter.Global404Filter;
import com.asialjim.microapplet.filter.auth.AuthFilter;
import com.asialjim.microapplet.filter.log.GlobalTraceFilter;
import com.asialjim.microapplet.route.RouteConfigProperty;
import com.asialjim.microapplet.route.RouteNode;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Signal;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 网关路由配置
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/9/24, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Slf4j
@Configuration
public class GatewayRouteConfig {

    @Resource
    private RouteConfigProperty routeConfigProperty;

    @Bean
    @RefreshScope
    public RouteLocator dynamicRouteLocator(RouteLocatorBuilder builder, AuthFilter authFilter,
                                            GlobalTraceFilter globalTraceFilter, Global404Filter global404Filter) {

        RouteLocatorBuilder.Builder routeBuilder = builder.routes();
        List<RouteNode> routes = this.routeConfigProperty.getRoutes();
        if (CollectionUtils.isNotEmpty(routes)) {
            for (RouteNode route : routes) {
                boolean enableAuth = route.enableAuth();
                String path = (enableAuth ? "/api/rest/" : "/api/open/") + route.getPath() + "/**";
                log.info("创建路由：{},path: {}",route,path);
                Function<GatewayFilterSpec, UriSpec> fn = filter -> {
                    filter.stripPrefix(2);
                    filter.rewritePath("/" + route.getPath(), StringUtils.EMPTY);
                    filter.filters(global404Filter,globalTraceFilter);
                    if (enableAuth)
                        filter.filter(authFilter);
                    return filter;
                };

                routeBuilder.route(route.getName(), r -> r.path(path).filters(fn).uri("lb://" + route.getService()));
            }
        }

        return routeBuilder.build();
    }

}
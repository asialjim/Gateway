/*
 * Copyright 2014-2025 <a href="mailto:asialjim@qq.com">Asial Jim</a>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.asialjim.microapplet.filter;

import com.asialjim.microapplet.common.cons.HttpHeaderCons;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * 全局链路追踪组件
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/5/6, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Component
public class GlobalTraceFilter implements GatewayFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 生成唯一的Trace ID（去除连字符）
        String traceId = UUID.randomUUID().toString().substring(0, 8);

        // 获取会话并处理请求头
        //noinspection ReactorTransformationOnMonoVoid
        return exchange.getSession()
                .flatMap(webSession -> {
                    // 存在会话时添加两个头部
                    ServerHttpRequest request = exchange.getRequest()
                            .mutate()
                            .header(HttpHeaderCons.CloudTraceId, traceId)
                            .header(HttpHeaderCons.CloudSessionId, webSession.getId())
                            .build();
                    return chain.filter(exchange.mutate().request(request).build());
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // 无会话时仅添加Trace ID
                    ServerHttpRequest request = exchange.getRequest()
                            .mutate()
                            .header(HttpHeaderCons.CloudTraceId, traceId)
                            .header(HttpHeaderCons.CloudSessionId, traceId)
                            .build();
                    return chain.filter(exchange.mutate().request(request).build());
                }));
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
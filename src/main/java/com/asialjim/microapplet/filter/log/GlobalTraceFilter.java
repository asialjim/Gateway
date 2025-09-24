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

package com.asialjim.microapplet.filter.log;

import com.asialjim.microapplet.common.cons.Headers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
        ServerHttpResponse response = exchange.getResponse();

        // 获取会话并处理请求头
        //noinspection ReactorTransformationOnMonoVoid
        return exchange.getSession()
                .flatMap(webSession -> {
                    // 存在会话时添加两个头部
                    ServerHttpRequest request = exchange.getRequest()
                            .mutate()
                            .header(Headers.SessionId, webSession.getId())
                            .headers(httpHeaders -> {
                                String trace = StringUtils.EMPTY;
                                for (String name : Headers.TRACE_HEADERS) {
                                    trace = httpHeaders.getFirst(name);
                                    if (StringUtils.isNotBlank(trace))
                                        break;
                                }
                                if (StringUtils.isBlank(trace))
                                    trace = UUID.randomUUID().toString().substring(0, 8);
                                httpHeaders.set(Headers.TraceId, trace);
                                httpHeaders.set(Headers.TRACE_ID, trace);
                                response.getHeaders().set(Headers.TraceId,trace);
                            })
                            .build();


                    return chain.filter(exchange.mutate().request(request).response(response).build());
                })
                .switchIfEmpty(Mono.defer(() -> {
                    String traceId = UUID.randomUUID().toString().substring(0, 8);
                    // 无会话时仅添加Trace ID
                    ServerHttpRequest request = exchange.getRequest()
                            .mutate()
                            .header(Headers.TraceId, traceId)
                            .header(Headers.SessionId, traceId)
                            .build();
                    response.getHeaders().set(Headers.TraceId,traceId);
                    return chain.filter(exchange.mutate().request(request).response(response).build());
                }));
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
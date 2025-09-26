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

package com.asialjim.microapplet.route;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.asialjim.microapplet.common.utils.JacksonUtil;
import com.asialjim.microapplet.config.NacosRouteDefinitionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 路由配置变更监听器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/9/25, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Slf4j
@Configuration
public class RouteChangedListener implements Listener {
    private static final JacksonUtil yamlUtil = JacksonUtil.instance(new ObjectMapper(new YAMLFactory()));
    private static final String dataId = "route.yaml";

    private final NacosRouteDefinitionRepository nacosRouteDefinitionRepository;
    private final NacosConfigManager nacosConfigManager;
    private final Executor executor;

    @Value("${spring.cloud.nacos.discovery.group}")
    private String group;

    public RouteChangedListener(List<Executor> executors,
                                NacosConfigManager nacosConfigManager,
                                NacosRouteDefinitionRepository nacosRouteDefinitionRepository) {
        this.executor = Optional.ofNullable(executors)
                .stream()
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .findAny()
                .orElseGet(Executors::newSingleThreadExecutor);
        this.nacosConfigManager = nacosConfigManager;
        this.nacosRouteDefinitionRepository = nacosRouteDefinitionRepository;
    }

    @PostConstruct
    public void init() {
        ConfigService configService = this.nacosConfigManager.getConfigService();
        try {
            configService.addListener(dataId, group, this);
        } catch (NacosException e) {
            log.error("添加路由配置变更监听器异常：{}", e.getMessage(), e);
        }
    }

    @Override
    public Executor getExecutor() {
        return this.executor;
    }

    @Override
    public void receiveConfigInfo(String configInfo) {
        GatewayRouteWrapper bean = yamlUtil.toBean(configInfo, GatewayRouteWrapper.class);
        Optional.ofNullable(bean)
                .map(GatewayRouteWrapper::getGateway)
                .ifPresent(nacosRouteDefinitionRepository::refreshRoutes);
    }

    @Data
    private static class GatewayRouteWrapper {
        private RouteConfigProperty gateway;
    }
}
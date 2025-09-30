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

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户认证服务器信息
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/9/24, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "feign.auth.user")
public class AuthServerProperty implements Serializable {
    @Serial
    private static final long serialVersionUID = -899675679406861833L;
//    @Value("${feign.auth.user.auth}")
    private String auth;
//    @Value("${feign.auth.user.auth-path}")
    private String authPath;

    public String authUrl(String token) {
        String path = (StringUtils.startsWith(authPath, "/") ? StringUtils.EMPTY : "/") + authPath;
        return String.format("lb://%s%s?token=%s", auth, path, token);
    }
}
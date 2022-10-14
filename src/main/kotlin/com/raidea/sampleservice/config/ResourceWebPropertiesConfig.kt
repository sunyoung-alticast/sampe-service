package com.raidea.sampleservice.config

import ch.qos.logback.core.util.Loader.getResources
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.boot.autoconfigure.web.WebProperties


@Configuration
class ResourceWebPropertiesConfig {
    @Bean
    fun resources(): WebProperties.Resources {
        return WebProperties.Resources()
    }
}
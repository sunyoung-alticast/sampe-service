package com.raidea.sampleservice.config
import org.springframework.web.reactive.function.client.WebClient
//import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean

@Configuration
class WebClientConfiguration {
    @Bean
    //@LoadBalanced
    fun loadBalancedWebClientBuilder(): WebClient.Builder {
        return WebClient.builder()
    }
}
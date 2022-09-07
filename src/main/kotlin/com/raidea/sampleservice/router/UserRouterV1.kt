package com.raidea.sampleservice.router

import com.raidea.sampleservice.handler.MemberHandler
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.router
import java.net.URI


@Component
class UserRouterV1 {

    @Bean
    fun swaggerRouterV1() = router {
        accept(MediaType.TEXT_HTML).nest {
            GET("/") { permanentRedirect(URI("index.html")).build() }
        }
        resources("/**", ClassPathResource("/static"))
    }

    @Bean
    fun memberRouterV1(memberHandler: MemberHandler) = router {
        accept(MediaType.APPLICATION_JSON).nest {
            "sample-service/user/v1.1".nest {
                GET("/members-async", memberHandler::findAllAsync)
                GET("/members-sync", memberHandler::findAllSync)
                GET("/members-tr", memberHandler::transactionTest)
                GET("/members-nontr", memberHandler::nonTransactionTest)
            }
        }
    }
}




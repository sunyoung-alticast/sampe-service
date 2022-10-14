package com.raidea.sampleservice.router

import com.raidea.sampleservice.handler.ExceptionHandlerV2
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.router
import java.net.URI

@Component
class ExceptionRouter(private val exceptionHandler: ExceptionHandlerV2) {

    @Bean
    fun exceptionRoutes() = router {
        accept(MediaType.TEXT_HTML).nest {
            GET("/") { permanentRedirect(URI("index.html")).build() }
        }

        accept(MediaType.APPLICATION_JSON).nest {
            "sample-service/v2".nest {
                GET("/exception-global", exceptionHandler::exceptionGlobal)
                GET("/exception-resume", exceptionHandler::exceptionResume)
                POST("/exception-duplication", exceptionHandler::makeDuplicationException)
            }
        }
    }
}




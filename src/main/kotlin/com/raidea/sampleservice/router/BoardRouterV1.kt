package com.raidea.sampleservice.router

import com.raidea.sampleservice.handler.BoardHandler
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.router
import java.net.URI

@Component
class BoardRouter(private val boardHandler: BoardHandler) {
    @Bean
    fun boardRoutes() = router {
        accept(MediaType.TEXT_HTML).nest {
            GET("/") { permanentRedirect(URI("index.html")).build() }
        }

        accept(MediaType.APPLICATION_JSON).nest {
            "sample-service/board/v1.1".nest {
                POST("/board-tr", boardHandler::transactionTest)
                POST("/board-nontr", boardHandler::nonTransactionTest)
                GET("/boards", boardHandler::getBoards)
            }
        }
    }
}




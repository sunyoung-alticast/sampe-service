package com.raidea.sampleservice.handler

import com.raidea.sampleservice.common.aop.LogExecution
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import com.raidea.sampleservice.config.Code
import com.raidea.sampleservice.domain.Response
import com.raidea.sampleservice.service.BoardServiceIn
import com.raidea.sampleservice.service.MemberService
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.server.body
import java.net.InetAddress

@Component
class MemberHandler(val mbService: MemberService, val board: BoardServiceIn){

    private val logger = LoggerFactory.getLogger(this::class.java)

    @LogExecution
    fun findAllAsync(request: ServerRequest): Mono<ServerResponse> {
        try {
            logger.info("called Async: {} ", InetAddress.getLocalHost().hostName)
            return Mono.just("test-string").flatMap {
                mbService.findAllAsync(it)
            }.flatMap {
                ServerResponse.ok().body(Mono.just(Response(Code.OK_CODE, Code.OK_MESSAGE, it)))
            }
        } catch (e: Exception) {
            logger.error(e.message)
            return ServerResponse.ok()
                .body(Mono.just(Response(Code.INTERNAL_SERVER_ERROR_CODE, e.message.toString(), null)))
        }
    }

    @LogExecution
    fun findAllSync(request: ServerRequest): Mono<ServerResponse> {
        try {
            logger.info("called findAllSync: {} ", InetAddress.getLocalHost().hostName)
            return Mono.just("test-string").flatMap {
                mbService.findAllSync(it)

            }.flatMap {
                ServerResponse.ok().body(Mono.just(Response(Code.OK_CODE, Code.OK_MESSAGE, it)))
            }
        } catch (e: Exception) {
            logger.error(e.message)
            return ServerResponse.ok()
                .body(Mono.just(Response(Code.INTERNAL_SERVER_ERROR_CODE, e.message.toString(), null)))
        }
    }

    @LogExecution
    fun transactionTest(request: ServerRequest): Mono<ServerResponse> {
        try {
            logger.info("called hostName : ", InetAddress.getLocalHost().hostName)
            return Mono.just("test-string").flatMap {
                board.insertBoard("test", "transactional", "test123")
            }.flatMap {
                ServerResponse.ok().body(Mono.just(Response(Code.OK_CODE, Code.OK_MESSAGE, it)))
            }
        } catch (e: Exception) {
            logger.error(e.message)
            return ServerResponse.ok()
                .body(Mono.just(Response(Code.INTERNAL_SERVER_ERROR_CODE, e.message.toString(), null)))
        }
    }

    @LogExecution
    fun nonTransactionTest(request: ServerRequest): Mono<ServerResponse> {
        try {
            logger.info("called hostName : ", InetAddress.getLocalHost().hostName)
            return Mono.just("test-string").flatMap {
                board.insertBoardNonTrans("test", "non-transactional", "test123")
            }.flatMap {
                ServerResponse.ok().body(Mono.just(Response(Code.OK_CODE, Code.OK_MESSAGE, it)))
            }
        } catch (e: Exception) {
            logger.error(e.message)
            return ServerResponse.ok()
                .body(Mono.just(Response(Code.INTERNAL_SERVER_ERROR_CODE, e.message.toString(), null)))
        }
    }


}

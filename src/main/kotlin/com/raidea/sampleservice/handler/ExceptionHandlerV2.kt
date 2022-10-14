package com.raidea.sampleservice.handler

import com.raidea.sampleservice.common.aop.LogExecution
import com.raidea.sampleservice.exception.exceptions.CommonErrorCode
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import com.raidea.sampleservice.domain.Response
import com.raidea.sampleservice.service.BoardServiceIn
import com.raidea.sampleservice.service.MemberService
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.server.body

@Component
class ExceptionHandlerV2(val mbService: MemberService, val board: BoardServiceIn){

    private val logger = LoggerFactory.getLogger(this::class.java)

    @LogExecution(message = "exceptionGlobal")
    fun exceptionGlobal(request: ServerRequest): Mono<ServerResponse> {
        logger.info("Exception : Global Test")

        return Mono.just("test-string").flatMap {
            board.makeServiceException("test", "transactional", "test123")
        }.flatMap {
            ServerResponse.ok().body(Mono.just(Response(CommonErrorCode.OK_CODE.code, CommonErrorCode.OK_CODE.message, it)))
        }
    }

    fun exceptionResume(request: ServerRequest): Mono<ServerResponse> {
        logger.info("Exception : Local onErrorResume1 Test")

        var result = Response(CommonErrorCode.OK_CODE.code, CommonErrorCode.OK_CODE.message, null)

        return Mono.just("test-string").flatMap {
            board.insertBoard("test", "transactional", "test123")
        }.flatMap {
            ServerResponse.ok().body(Mono.just(Response(CommonErrorCode.OK_CODE.code, CommonErrorCode.OK_CODE.message, it)))
        }.onErrorResume {
                when (it) {
                    is UninitializedPropertyAccessException -> {
                        result = Response(500, it.message.toString(), null)
                    }
                    is NoSuchElementException -> {
                        result = Response(500, it.message.toString(), null)
                    }
                    is Exception -> {
                        result = Response(500, it.message.toString(), null)
                    }
                }
                return@onErrorResume ok().body(Mono.just(result))
            }
    }

    fun makeDuplicationException(req: ServerRequest) : Mono<ServerResponse> {
        logger.info("Exception : Duplication Exception")
        val nickName = req.queryParam("nickName").get()
        val title = req.queryParam("title").get()
        val contents = req.queryParam("contents").get()
        return Mono.just("test-string").flatMap {
            board.makeDuplicationException(nickName, contents, title)
        }.flatMap {
            ServerResponse.ok().body(Mono.just(Response(CommonErrorCode.OK_CODE.code, CommonErrorCode.OK_CODE.message, it)))
        }
    }
}

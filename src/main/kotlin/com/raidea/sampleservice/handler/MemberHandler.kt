package com.raidea.sampleservice.handler

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import com.raidea.sampleservice.domain.Response
import com.raidea.sampleservice.exception.exceptions.CommonErrorCode
import com.raidea.sampleservice.service.BoardServiceIn
import com.raidea.sampleservice.service.MemberService
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.server.body
import java.net.InetAddress

@Component
class MemberHandler(val mbService: MemberService, val board: BoardServiceIn){

    private val logger = LoggerFactory.getLogger(this::class.java)

    /* webClient Proxy 예제 */
    fun findAllAsync(request: ServerRequest): Mono<ServerResponse> {
        logger.info("called Async: {} ", InetAddress.getLocalHost().hostName)
        return Mono.just("test-string").flatMap {
            mbService.findAllAsync(it)
        }.flatMap {
            ServerResponse.ok().body(Mono.just(Response(CommonErrorCode.OK_CODE.code, CommonErrorCode.OK_CODE.message, it)))
        }
    }

    fun findAllSync(request: ServerRequest): Mono<ServerResponse> {
        logger.info("called findAllSync: {} ", InetAddress.getLocalHost().hostName)
        return Mono.just("test-string").flatMap {
            mbService.findAllSync(it)
        }.flatMap {
            ServerResponse.ok().body(Mono.just(Response(CommonErrorCode.OK_CODE.code, CommonErrorCode.OK_CODE.message, it)))
        }
    }
}

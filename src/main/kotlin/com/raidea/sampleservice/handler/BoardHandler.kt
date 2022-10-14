package com.raidea.sampleservice.handler

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import com.raidea.sampleservice.domain.Board
import com.raidea.sampleservice.domain.Response
import com.raidea.sampleservice.exception.exceptions.CommonErrorCode
import com.raidea.sampleservice.service.BoardServiceIn
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.server.body
import java.net.InetAddress

@Component
class BoardHandler(val board: BoardServiceIn) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun transactionTest(req: ServerRequest): Mono<ServerResponse> {
        logger.info("called hostName : ", InetAddress.getLocalHost().hostName)
        return req.bodyToMono(Board::class.java).flatMap {
            board.insertBoard(it.nickName!!, it.contents!!, it.contents!!)
        }.flatMap {
            ServerResponse.ok().body(Mono.just(Response(CommonErrorCode.OK_CODE.code, CommonErrorCode.OK_CODE.message, it)))
        }
    }

    fun nonTransactionTest(req: ServerRequest): Mono<ServerResponse> {
        logger.info("called hostName : ", InetAddress.getLocalHost().hostName)
        return req.bodyToMono(Board::class.java).flatMap {
            board.insertBoardNonTrans(it.nickName!!, it.contents!!, it.contents!!)
        }.flatMap {
            ServerResponse.ok().body(Mono.just(Response(CommonErrorCode.OK_CODE.code, CommonErrorCode.OK_CODE.message, it)))
        }
    }

    fun getBoards(req : ServerRequest) : Mono<ServerResponse> {
        val nickName = req.queryParam("nickName").orElse("")
        return Mono.just(nickName).flatMap {
            board.getBoards(it)
        }.flatMap {
            ServerResponse.ok().body(Mono.just(Response(CommonErrorCode.OK_CODE.code, CommonErrorCode.OK_CODE.message, it)))
        }
    }
}

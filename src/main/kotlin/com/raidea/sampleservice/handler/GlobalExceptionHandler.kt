package com.raidea.sampleservice.handler

import com.raidea.sampleservice.exception.exceptions.CustomException
import com.raidea.sampleservice.domain.Response
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.server.ResponseStatusException
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.BodyInserters

@Component
@Order(-2)
class GlobalExceptionHandler(
    errorAttributes: ErrorAttributes,
    resources: WebProperties.Resources,
    applicationContext: ApplicationContext,
    serverCodecConfigurer: ServerCodecConfigurer,
) : AbstractErrorWebExceptionHandler(
    errorAttributes, resources, applicationContext
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        super.setMessageReaders(serverCodecConfigurer.readers)
        super.setMessageWriters(serverCodecConfigurer.writers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes): RouterFunction<ServerResponse> {
       return RouterFunctions.route(RequestPredicates.all()) {
           logger.info("getRoutingFunction error $it")
           when (val throwable: Throwable =  errorAttributes.getError(it)) {
               is CustomException -> {
                   ServerResponse.status(HttpStatus.OK).body(
                       BodyInserters.fromValue(
                           Response(throwable.code, throwable.message.toString(), null)
                       ))
               }
               else -> {
                   badResponse(throwable)
               }
           }
       }
    }


    private fun badResponse(throwable: Throwable): Mono<ServerResponse> {
        logger.info("[GlobalException] badResponse {}", throwable.message)
        return when (throwable) {
           is ResponseStatusException -> {
               logger.info("getRoutingFunction error {} {} ", throwable.status, throwable.rawStatusCode)
                ServerResponse.status(throwable.rawStatusCode)
                        .body(
                       BodyInserters.fromValue(
                       Response(throwable.rawStatusCode, throwable.message.toString(), null)))
           }
            else -> {
                ServerResponse.status(HttpStatus.OK)
                    .body(
                        BodyInserters.fromValue(
                            Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), throwable.message.toString(), null)))
            }
        }
    }

}
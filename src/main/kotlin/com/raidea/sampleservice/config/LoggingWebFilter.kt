package com.raidea.sampleservice.config


import org.apache.commons.io.IOUtils
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.channels.Channels

@Component
class LoggingWebFilter : WebFilter {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val httpRequest = exchange.request
        val httpUrl = httpRequest.uri.toString()
        val loggingServerHttpRequestDecorator: ServerHttpRequestDecorator =
            object : ServerHttpRequestDecorator(exchange.request) {
                var requestBody = ""
                override fun getBody(): Flux<DataBuffer> {

                    return super.getBody().doOnNext { dataBuffer: DataBuffer ->
                        try {
                            ByteArrayOutputStream().use { byteArrayOutputStream ->
                                Channels.newChannel(byteArrayOutputStream)
                                    .write(dataBuffer.asByteBuffer().asReadOnlyBuffer())
                                requestBody =
                                    IOUtils.toString(
                                        byteArrayOutputStream.toByteArray(),
                                        "UTF-8"
                                    )
                                log.info(
                                    "Logging Request Filter: {} {}",
                                    httpUrl,
                                    requestBody
                                )
                            }
                        } catch (e: IOException) {
                            log.error(
                                "Logging Request Filter Error: {} {}",
                                httpUrl,
                                requestBody,
                                e
                            )
                        }
                    }
                }
            }

        val loggingServerHttpResponseDecorator: ServerHttpResponseDecorator =
            object : ServerHttpResponseDecorator(exchange.response) {
                var responseBody = ""
                override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
                    val buffer: Mono<DataBuffer> = Mono.from(body)
                    return super.writeWith(
                        buffer.doOnNext { dataBuffer: DataBuffer ->
                            try {
                                ByteArrayOutputStream().use { byteArrayOutputStream ->
                                    Channels.newChannel(byteArrayOutputStream)
                                        .write(
                                            dataBuffer
                                                .asByteBuffer()
                                                .asReadOnlyBuffer()
                                        )
                                    responseBody = IOUtils.toString(
                                        byteArrayOutputStream.toByteArray(),
                                        "UTF-8"
                                    )
                                    log.info(
                                        "Logging Response Filter: {} {}",
                                        httpUrl,
                                        responseBody
                                    )
                                }
                            } catch (e: Exception) {
                                log.error(
                                    "Logging Response Filter Error: {} {}",
                                    httpUrl,
                                    responseBody,
                                    e
                                )
                            }
                        }
                    )
                }
            }
        return chain.filter(
            exchange.mutate().request(loggingServerHttpRequestDecorator)
                .response(loggingServerHttpResponseDecorator)
                .build()
        )
    }
}
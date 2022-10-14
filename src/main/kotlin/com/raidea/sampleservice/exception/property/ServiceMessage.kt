package com.raidea.sampleservice.exception.property

import com.raidea.sampleservice.exception.exceptions.ServiceException
import reactor.core.publisher.Mono
import org.springframework.http.HttpStatus


enum class ServiceMessage(
    val status: HttpStatus,
    val message: String,
) {
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "Board not found."),
    ;

    fun exception(): ServiceException {
        return ServiceException(this)
    }

    fun <T> error(): Mono<T> {
        return Mono.error(this.exception())
    }
}
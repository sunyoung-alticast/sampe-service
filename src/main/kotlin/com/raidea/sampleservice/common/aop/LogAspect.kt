package com.raidea.sampleservice.common.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.lang.System.currentTimeMillis
import java.net.InetAddress

@Aspect
@Component
class LogAspect {
    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    @Around("@annotation(logExecution)")
    @Throws(Throwable::class)
    fun log(joinPoint: ProceedingJoinPoint, logExecution: LogExecution): Any? =
        joinPoint.run {
            val startTime = currentTimeMillis()

            joinPoint.proceed()
                .also {
                    // if need returned use $it
                    logMessage(startTime = startTime, signature = joinPoint.signature.toShortString(), message = logExecution.message, level = logExecution.level)
                }
        }

    @Around("@annotation(proxyLogExecution)")
    @Throws(Throwable::class)
    fun log(joinPoint: ProceedingJoinPoint, proxyLogExecution: ProxyLogExecution): Any? =
        joinPoint.run {
            val startTime = currentTimeMillis()
            joinPoint.proceed()
                .also {
                    // if need returned use $it
                    logMessage(startTime, joinPoint.signature.toShortString(), proxyLogExecution.api, proxyLogExecution.message, proxyLogExecution.level)
                }
        }

    fun logMessage(startTime: Long, signature : String, api : String? = "", message : String? = "", level : LoggerLevel) {

        val logMessage = with(StringBuffer()) {
            append("[${InetAddress.getLocalHost().hostName}] $signature call : ${currentTimeMillis() - startTime} ms")
            if (api != null && api.isNotBlank()) append(", api : $api ")
            if (message != null && message.isNotBlank()) append(", message : $message ")
            toString()
        }
        log.info(logMessage)
        when (level) {
            LoggerLevel.INFO -> logger.info(logMessage)
            LoggerLevel.WARN -> logger.warn(logMessage)
            LoggerLevel.ERROR -> logger.error(logMessage)
            LoggerLevel.DEBUG -> logger.debug(logMessage)
            LoggerLevel.TRACE -> logger.trace(logMessage)
        }
    }
}
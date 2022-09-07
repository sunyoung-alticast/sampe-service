package com.raidea.sampleservice.common.aop

/**
 * 메소드 실행 시 로그 기록을 위한 annotation class
 *
 * EX. @ProxyLogExecution(level = LoggerLevel.INFO, api = "/v1.1/objs/search", message = "example")
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ProxyLogExecution(
    val level: LoggerLevel = LoggerLevel.INFO,
    val api: String = "",
    val message: String = ""
)
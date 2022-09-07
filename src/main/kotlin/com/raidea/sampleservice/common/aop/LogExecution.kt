package com.raidea.sampleservice.common.aop

/**
 * 메소드 실행 시 로그 기록을 위한 annotation class
 *
 * EX. @LogExecution(level = LoggerLevel.INFO, message = "메소드 호출")
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class LogExecution(
    val level: LoggerLevel = LoggerLevel.INFO,
    val message: String = ""
)
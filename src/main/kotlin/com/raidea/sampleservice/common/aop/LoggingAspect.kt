package com.raidea.sampleservice.common.aop

import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Around
import kotlin.Throws
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.util.*

/**
 * Aspect for logging execution of service and repository Spring components.
 * @author Ramesh Fadatare
 */
@Aspect
@Component
class LoggingAspect {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut(
        "within(@org.springframework.stereotype.Repository *)" +
                " || within(@org.springframework.stereotype.Service *)"
                // " || within(@org.springframework.web.reactive.function.server.router *)"
    )
    fun springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut(
        //"within(com.raidea.sampleservice.handler..*)" +
                //"within(com.raidea.sampleservice.handler..*)" +
                "within(com.raidea.sampleservice.service..*)" +
                //" || within(com.raidea.sampleservice.service..*)" +
                " || within(com.raidea.sampleservice.repository..*)"
    )
    fun applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    fun logAfterThrowing(joinPoint: JoinPoint, e: Throwable) {
        log.error(
            "Exception in {}.{}() with cause = {}", joinPoint.signature.declaringTypeName,
            joinPoint.signature.name, if (e.cause != null) e.cause else "NULL"
        )
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    @Throws(Throwable::class)
    fun logAround(joinPoint: ProceedingJoinPoint): Any? =
        joinPoint.run{
            log.info(
                "Enter: {}.{}() with argument[s] = {}", joinPoint.signature.declaringTypeName,
                joinPoint.signature.name, Arrays.toString(joinPoint.args)
            )
            joinPoint.proceed().also {
                log.info(
                    "Exit: {}.{}() with result = {}", joinPoint.signature.declaringTypeName,
                    joinPoint.signature.name)
            }
        }
}

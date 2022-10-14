package com.raidea.sampleservice.common.aop

import org.aspectj.lang.JoinPoint
import kotlin.Throws
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.util.StringUtils
import java.util.*
import org.aspectj.lang.reflect.MethodSignature
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import reactor.core.publisher.Flux

import reactor.core.publisher.Mono
import java.lang.System.currentTimeMillis

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
    )
    fun springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }


    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut(
        "within(com.raidea.sampleservice.service..*)" +
                " || within(com.raidea.sampleservice.repository..*)"
    )
    fun applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
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
    fun logAround(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature as MethodSignature
        val method: Method = signature.method

        val parameterizedType: ParameterizedType = method.getGenericReturnType() as ParameterizedType
        val rawType: Type = parameterizedType.getRawType()
        if (rawType != Mono::class.java && rawType != Flux::class.java){
            throw IllegalArgumentException("The return type is not Mono/Flux. Use Mono/Flux for return type. method: " + method.getName())
        }
        val startTime = currentTimeMillis()
        log.info(
            "Enter: {}.{}() with argument[s] = {} {}", joinPoint.signature.declaringTypeName,
            joinPoint.signature.name, Arrays.toString(joinPoint.args), rawType.toString()
        )
        try {
            var result = joinPoint.proceed().also {
                var endTime = currentTimeMillis() - startTime
                log.info(
                    "Exit: {}.{}()  Elapsed Time:{} ms", joinPoint.signature.declaringTypeName,
                    joinPoint.signature.name, endTime.toString()
                )
            }
            return result
        } catch (e: Exception) {
            // check for custom exception
            val rs = e.javaClass.getAnnotation(ResponseStatus::class.java)
            if (rs != null) {
                log.error("ERROR accessing resource, reason: '{}', status: {}.",
                    if (!StringUtils.hasText(e.message)) rs.reason else e.message,
                    rs.value)
            } else {
                log.error("ERROR accessing resource")
            }
            throw e
        }
    }


    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "e")
    fun logAfterThrowing(joinPoint: JoinPoint, e: Throwable) {
        log.error(
            "Exception in {}.{}() with cause = {}", joinPoint.signature.declaringTypeName,
            joinPoint.signature.name, if (e.cause != null) e.cause else "NULL"
        )
    }

}

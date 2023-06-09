package com.addressbook.aop

import com.fasterxml.jackson.databind.ObjectMapper
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Aspect
@Component
class LoggingAspect {

    private val logger = LoggerFactory.getLogger(LoggingAspect::class.java)

    @Around("@annotation(com.addressbook.annotations.LoggedGetRequest) " +
            "|| @annotation(com.addressbook.annotations.LoggedPostRequest)")
    @Throws(Throwable::class)
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val start = System.currentTimeMillis()
        val proceed = joinPoint.proceed()
        val executionTime = System.currentTimeMillis() - start
        logger.info(joinPoint.signature.toString() + " executed in " + executionTime + "ms")
        if(logger.isDebugEnabled) logger.debug(ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(proceed))
        return proceed
    }
}
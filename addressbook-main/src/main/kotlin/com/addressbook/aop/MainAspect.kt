package com.addressbook.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Aspect
@Component
class MainAspect {

    private val logger = LoggerFactory.getLogger(MainAspect::class.java)

    @Around("@annotation(com.addressbook.annotations.LoggedGetRequest) " +
               "|| @annotation(com.addressbook.annotations.LoggedPostRequest)")
    @Throws(Throwable::class)
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val start = System.nanoTime()
        val proceed = joinPoint.proceed()
        val executionTime = System.nanoTime() - start
        logger.info(joinPoint.signature.toString() + " executed in " + executionTime + "ns")
        return proceed
    }
}
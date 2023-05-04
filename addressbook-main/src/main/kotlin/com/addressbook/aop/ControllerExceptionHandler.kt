package com.addressbook.aop

import com.addressbook.dto.AlertDto
import com.addressbook.exceptions.LockRecordException
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.multipart.MaxUploadSizeExceededException

@RestControllerAdvice
class ControllerExceptionHandler {

    private val logger = LoggerFactory.getLogger(ControllerExceptionHandler::class.java)

    @ExceptionHandler(value = [MaxUploadSizeExceededException::class, SizeLimitExceededException::class])
    protected fun handleMaxUploadSizeExceededException(ex: Exception, request: WebRequest): ResponseEntity<Any?>? {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build()
    }

    @ExceptionHandler(Throwable::class)
    fun handleError(ex: Throwable): ResponseEntity<Any?>? {
        logger.error("Exception occurred:", ex)
        val message = if (ex.javaClass == IllegalArgumentException::class.java || ex.javaClass == LockRecordException::class.java) {
            ex.message
        } else {
            ExceptionUtils.getStackTrace(ex)
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AlertDto("Error occurred!", AlertDto.DANGER, message))
    }
}
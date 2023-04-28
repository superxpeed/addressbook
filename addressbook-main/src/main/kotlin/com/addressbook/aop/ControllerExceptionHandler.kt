package com.addressbook.aop

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.multipart.MaxUploadSizeExceededException

@RestControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(value = [MaxUploadSizeExceededException::class, SizeLimitExceededException::class])
    protected fun handleMaxUploadSizeExceededException(ex: Exception, request: WebRequest): ResponseEntity<Any?>? {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build()
    }

}
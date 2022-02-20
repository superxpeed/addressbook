package com.addressbook.annotations

import org.springframework.core.annotation.AliasFor
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@RequestMapping(method = [RequestMethod.GET])
annotation class LoggedGetRequest(
        @get:AliasFor(annotation = RequestMapping::class, attribute = "path") val path: String
)

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@RequestMapping(method = [RequestMethod.POST])
annotation class LoggedPostRequest(
        @get:AliasFor(annotation = RequestMapping::class, attribute = "path") val path: String
)
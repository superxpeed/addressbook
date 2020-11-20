package com.addressbook.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@kotlin.annotation.Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@RequestMapping(method = [RequestMethod.GET])
annotation class LoggedGetRequest(
        @get:AliasFor(annotation = RequestMapping::class, attribute = "path") val path: String
)

@kotlin.annotation.Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@RequestMapping(method = [RequestMethod.POST])
annotation class LoggedPostRequest(
        @get:AliasFor(annotation = RequestMapping::class, attribute = "path") val path: String
)
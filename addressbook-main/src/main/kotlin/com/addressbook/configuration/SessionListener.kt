package com.addressbook.configuration

import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger
import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener

class SessionListener : HttpSessionListener {

    private val logger = LoggerFactory.getLogger(SessionListener::class.java)
    private val activeSessions: AtomicInteger = AtomicInteger()

    override fun sessionCreated(event: HttpSessionEvent) {
        activeSessions.incrementAndGet()
        logger.info("HttpSession created: {}", event.session.id)
        logger.info("Total sessions: {}", activeSessions.get())
    }

    override fun sessionDestroyed(event: HttpSessionEvent) {
        activeSessions.decrementAndGet()
        logger.info("HttpSession destroyed: {}", event.session.id)
        logger.info("Total sessions: {}", activeSessions.get())
    }
}


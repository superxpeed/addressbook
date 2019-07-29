package com.addressbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionListener implements HttpSessionListener {

    private static Logger logger = LoggerFactory.getLogger(SessionListener.class);

    private final AtomicInteger activeSessions;

    public SessionListener() {
        activeSessions = new AtomicInteger();
    }

    public void sessionCreated(final HttpSessionEvent event) {
        activeSessions.incrementAndGet();
        logger.info("HttpSession created: " + event.getSession().getId());
        logger.info("Total sessions: " + activeSessions.get());
    }

    public void sessionDestroyed(final HttpSessionEvent event) {
        activeSessions.decrementAndGet();
        logger.info("HttpSession destroyed: " + event.getSession().getId());
        logger.info("Total sessions: " + activeSessions.get());
    }
}


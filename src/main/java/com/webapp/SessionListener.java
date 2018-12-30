package com.webapp;

import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

    private final AtomicInteger activeSessions;

    public SessionListener() {
        activeSessions = new AtomicInteger();
    }

    public void sessionCreated(final HttpSessionEvent event) {
        activeSessions.incrementAndGet();
        System.out.println("HttpSession created: " + event.getSession().getId());
        System.out.println("Total sessions: " + activeSessions.get());
    }
    public void sessionDestroyed(final HttpSessionEvent event) {
        activeSessions.decrementAndGet();
        System.out.println("HttpSession destroyed: " + event.getSession().getId());
        System.out.println("Total sessions: " + activeSessions.get());
    }
}


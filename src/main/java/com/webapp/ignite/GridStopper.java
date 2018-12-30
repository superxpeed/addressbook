package com.webapp.ignite;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

public class GridStopper implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("--------------- Context Closed -----------------");
        GridDAO.stopClient();
    }
}

package com.webapp.ignite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

public class GridStopper implements ApplicationListener<ContextClosedEvent> {

    private static Logger logger = LoggerFactory.getLogger(GridStopper.class);

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("--------------- Context Closed -----------------");
        GridDAO.stopClient();
    }
}

package com.webapp.ignite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class GridStarter implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(GridStarter.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("--------------- Context Refreshed -----------------");
        GridDAO.startClient();
    }
}

package com.addressbook.ignite;

import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

class GridStarter: ApplicationListener<ContextRefreshedEvent> {

    private val logger = LoggerFactory.getLogger(GridStarter::class.java)

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        logger.info("--------------- Ignite client node startup -----------------");
        GridDAO.startClient();
        Runtime.getRuntime().addShutdownHook( Thread {
            logger.info("--------------- Ignite client node shutdown -----------------");
            Thread.sleep(5_000);
            GridDAO.stopClient();
        })
    }
}

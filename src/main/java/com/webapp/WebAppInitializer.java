package com.webapp;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class WebAppInitializer implements WebApplicationInitializer {

    public void onStartup(ServletContext container) {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootConfiguration.class);

        container.addListener(new ContextLoaderListener(rootContext));

        AnnotationConfigWebApplicationContext indexDispatcherContext = new AnnotationConfigWebApplicationContext();
        indexDispatcherContext.register(IndexConfiguration.class);
        ServletRegistration.Dynamic indexDispatcher = container.addServlet("index", new DispatcherServlet(indexDispatcherContext));
        indexDispatcher.setLoadOnStartup(1);
        indexDispatcher.addMapping("/*");

        AnnotationConfigWebApplicationContext restDispatcherContext = new AnnotationConfigWebApplicationContext();
        restDispatcherContext.register(RestConfiguration.class);
        ServletRegistration.Dynamic restDispatcher = container.addServlet("rest", new DispatcherServlet(restDispatcherContext));
        restDispatcher.setLoadOnStartup(1);
        restDispatcher.addMapping("/rest/*");
    }

}

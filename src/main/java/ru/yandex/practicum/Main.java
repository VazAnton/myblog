package ru.yandex.practicum;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class Main {

    private static final int PORT = 8080;

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.getConnector().setPort(PORT);
        Context servletContext = tomcat.addContext("", null);
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.scan("ru.yandex.practicum");
        applicationContext.setServletContext(servletContext.getServletContext());
        applicationContext.refresh();

        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
        Wrapper dispatcherServletWrapper = Tomcat.addServlet(servletContext, "dispatcherServlet",
                dispatcherServlet);
        dispatcherServletWrapper.addMapping("/");
        dispatcherServletWrapper.setLoadOnStartup(1);
        tomcat.start();
    }
}

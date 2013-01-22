package lv.k2611a;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);
    public static ClassPathXmlApplicationContext springContext;
    public static AutowireCapableBeanFactory autowireCapableBeanFactory;
    public static Map<String, String> properties = new HashMap<String, String>();
    private static Server server;

    public static void main(String[] arg) throws Exception {

        log.info("Starting server..");

        loadProperties();

        springContext = new ClassPathXmlApplicationContext("application-context.xml");

        log.info("Spring context created");

        try {
            springContext.registerShutdownHook();
            springContext.start();
            log.info("Spring context started");
            Runtime.getRuntime().addShutdownHook(shutdownHook);
            autowireCapableBeanFactory = springContext.getAutowireCapableBeanFactory();
            start(arg);
        } catch (Exception e) {
            stop();
            throw e;
        }
    }

    private static Thread shutdownHook = new Thread() {
        @Override
        public void run() {
            log.info("Running shutdown hook");
            App.stop();
        }
    };

    private static void stop() {
        try {
            log.info("Stopping jetty server..");
            server.stop();
            log.info("Server jetty stopped");
        } catch (Exception e) {
            log.error("Exception while stopping jetty server", e);
        }
        try {
            log.info("Stopping spring context..");
            springContext.stop();
            log.info("Spring context stopped");
        } catch (Exception e) {
            log.error("Exception while stopping spring context", e);
        }
        try {
            log.info("Closing spring context..");
            springContext.close();
            log.info("Spring context closed");
        } catch (Exception e) {
            log.error("Exception while closing spring context",e);
        }
    }

    private static void start(String[] arg) throws Exception {
        int port = arg.length > 1 ? Integer.parseInt(arg[1]) : 8080;
        server = new Server(port);

        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(GameServlet.class, "/chat/*");

        Handler resHandler = createStaticResourceHandler();

        DefaultHandler defaultHandler = new DefaultHandler();

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{servletHandler, resHandler, defaultHandler});
        server.setHandler(handlers);

        server.start();
        server.join();
        log.info("Jetty started");
    }

    private static Handler createStaticResourceHandler() {
        Handler resHandler = null;

        if (properties.get("loadClientFromClasspath").equals("true")) {
            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setBaseResource(Resource.newClassPathResource("web"));
            resHandler = resourceHandler;
            log.info("Serving static files from classpath");
        } else {
            ResourceHandler resourceHandler = new ResourceHandler();
            String resourceBase = "dune-client/resources/";
            resourceHandler.setResourceBase(resourceBase);
            resHandler = resourceHandler;
            log.info("Serving static files from " + resourceBase);
        }
        return resHandler;
    }

    private static void loadProperties() throws IOException {
        InputStream is = App.class.getClassLoader().getResourceAsStream("app.config");
        for (String s : IOUtils.readLines(is)) {
            String[] parts = s.split("=");
            properties.put(parts[0],parts[1]);
        }
        log.info("Properties loaded");
    }
}

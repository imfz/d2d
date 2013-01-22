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
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static ClassPathXmlApplicationContext springContext;
    public static AutowireCapableBeanFactory autowireCapableBeanFactory;
    public static Map<String, String> properties = new HashMap<String, String>();

    public static void main(String[] arg) throws Exception {

        loadProperties();

        springContext = new ClassPathXmlApplicationContext("application-context.xml");

        try {
            autowireCapableBeanFactory = springContext.getAutowireCapableBeanFactory();
            springContext.registerShutdownHook();
            start(arg);
        } catch (Exception e) {
            stop();
            throw e;
        }
    }

    private static void stop() {
        try {
            springContext.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void start(String[] arg) throws Exception {
        int port = arg.length > 1 ? Integer.parseInt(arg[1]) : 8080;
        Server server = new Server(port);

        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(GameServlet.class, "/chat/*");

        Handler resHandler = createStaticResourceHandler();

        DefaultHandler defaultHandler = new DefaultHandler();

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{servletHandler, resHandler, defaultHandler});
        server.setHandler(handlers);

        server.start();
        server.join();
    }

    private static Handler createStaticResourceHandler() {
        Handler resHandler = null;

        if (properties.get("loadClientFromClasspath").equals("true")) {
            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setBaseResource(Resource.newClassPathResource("web"));
            resHandler = resourceHandler;
        } else {
            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setResourceBase("dune-client/resources/");
            resHandler = resourceHandler;
        }
        return resHandler;
    }

    private static void loadProperties() throws IOException {
        InputStream is = App.class.getClassLoader().getResourceAsStream("app.config");
        for (String s : IOUtils.readLines(is)) {
            String[] parts = s.split("=");
            properties.put(parts[0],parts[1]);
        }
    }
}

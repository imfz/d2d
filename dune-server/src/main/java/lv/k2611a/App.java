package lv.k2611a;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletHandler;

public class App {
    public static void main(String[] arg) throws Exception {
        int port = arg.length > 1 ? Integer.parseInt(arg[1]) : 8080;
        Server server = new Server(port);

        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(GameServlet.class, "/chat/*");

        DefaultHandler defaultHandler = new DefaultHandler();

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{servletHandler, defaultHandler});
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}

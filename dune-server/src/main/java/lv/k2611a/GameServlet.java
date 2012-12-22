package lv.k2611a;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GameServlet extends HttpServlet {
    private WebSocketFactory _wsFactory;
    private ClassPathXmlApplicationContext springContext;
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Override
    public void init() throws ServletException {
        springContext = new ClassPathXmlApplicationContext("application-context.xml");
        autowireCapableBeanFactory = springContext.getAutowireCapableBeanFactory();
        _wsFactory = new WebSocketFactory(new WebSocketFactory.Acceptor() {
            public boolean checkOrigin(HttpServletRequest request, String origin) {
                return true;
            }

            public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
                if ("chat".equals(protocol)) {
                    ClientConnection clientConnection = new ClientConnection();
                    autowireCapableBeanFactory.autowireBean(clientConnection);
                    return clientConnection;
                }
                return null;
            }
        });
        _wsFactory.setBufferSize(4096);
        _wsFactory.setMaxIdleTime(60000);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (_wsFactory.acceptWebSocket(request, response)) {
            return;
        }
        response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Websocket only");
    }

}

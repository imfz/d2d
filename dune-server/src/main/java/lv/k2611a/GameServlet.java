package lv.k2611a;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameServlet extends HttpServlet {
    private WebSocketFactory _wsFactory;

    private static final Logger log = LoggerFactory.getLogger(GameServlet.class);
    private AtomicLong connectionId = new AtomicLong(0);

    @Override
    public void init() throws ServletException {
        _wsFactory = new WebSocketFactory(new WebSocketFactory.Acceptor() {
            public boolean checkOrigin(HttpServletRequest request, String origin) {
                return true;
            }

            public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
                if ("chat".equals(protocol)) {
                    log.info("Connection established");
                    long id = connectionId.incrementAndGet();
                    ClientConnection clientConnection = new ClientConnection(id);
                    App.autowireCapableBeanFactory.autowireBean(clientConnection);
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

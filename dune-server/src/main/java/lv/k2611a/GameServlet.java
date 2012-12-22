package lv.k2611a;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketFactory;

public class GameServlet extends HttpServlet {
    private WebSocketFactory _wsFactory;


    @Override
    public void init() throws ServletException {
        _wsFactory = new WebSocketFactory(new WebSocketFactory.Acceptor() {
            public boolean checkOrigin(HttpServletRequest request, String origin) {
                return true;
            }

            public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
                if ("chat".equals(protocol)) {
                    ClientConnection clientConnection = new ClientConnection();
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

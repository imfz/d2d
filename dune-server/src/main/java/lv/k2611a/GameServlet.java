package lv.k2611a;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.k2611a.service.scope.ContextService;
import lv.k2611a.service.scope.GameKey;

public class GameServlet extends HttpServlet {
    private WebSocketFactory _wsFactory;

    private static final Logger log = LoggerFactory.getLogger(GameServlet.class);


    @Override
    public void init() throws ServletException {
        _wsFactory = new WebSocketFactory(new WebSocketFactory.Acceptor() {
            public boolean checkOrigin(HttpServletRequest request, String origin) {
                return true;
            }

            public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
                if ("chat".equals(protocol)) {
                    ContextService contextService = App.autowireCapableBeanFactory.getBean(ContextService.class);
                    String gameIdAttribute = request.getParameter("gameId");
                    if (gameIdAttribute == null) {
                        log.warn("Game id not found in url attributes");
                        return null;
                    }
                    GameKey value;
                    try {
                        value = new GameKey(Integer.valueOf(gameIdAttribute));
                    } catch (NumberFormatException e) {
                        log.warn("Game id not parsable in url attributes: " + gameIdAttribute);
                        return null;
                    }
                    log.info("Connection established to game " + gameIdAttribute);
                    try {
                        contextService.setSessionKey(value);
                        ClientConnection clientConnection = new ClientConnection(contextService.getCurrentContextKey());
                        App.autowireCapableBeanFactory.autowireBean(clientConnection);
                        return clientConnection;
                    } finally {
                        contextService.clearCurrentSessionKey();
                    }
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

package lv.k2611a;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.alibaba.fastjson.JSON;

import lv.k2611a.network.req.Request;
import lv.k2611a.network.resp.CustomSerializationHeader;
import lv.k2611a.network.resp.Left;
import lv.k2611a.network.resp.Response;
import lv.k2611a.service.game.GameService;
import lv.k2611a.service.global.GlobalSessionService;
import lv.k2611a.service.global.GlobalUsernameService;
import lv.k2611a.service.lobby.LobbyService;
import lv.k2611a.service.scope.ContextService;
import lv.k2611a.service.scope.GameKey;

public class ClientConnection implements WebSocket.OnTextMessage, Runnable {

    private static final Logger log = LoggerFactory.getLogger(ClientConnection.class);

    private volatile Connection _connection;

    private static final ThreadLocal<ClientConnection> localConnection = new ThreadLocal<ClientConnection>();

    public static ClientConnection getCurrentConnection() {
        ClientConnection clientConnection = localConnection.get();
        return clientConnection;
    }

    public static void setCurrentConnection(ClientConnection clientConnection) {
        localConnection.set(clientConnection);
    }

    @Autowired
    private GameService gameService;

    @Autowired
    private GlobalSessionService globalSessionService;

    @Autowired
    private GlobalUsernameService globalUsernameService;

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Autowired
    private ContextService contextService;

    @Autowired
    private LobbyService lobbyService;

    private String username;

    // null if observer
    private Integer playerId;

    private GameKey gameKey;

    private Integer selectedBuildingId;

    private BlockingQueue<Response> queue = new LinkedBlockingQueue<Response>();

    private ExecutorService exec = Executors.newFixedThreadPool(1);

    private volatile boolean closed = false;

    public ClientConnection() {
    }

    public void onOpen(Connection connection) {
        try {
            contextService.setSessionKey(gameKey);
            processOnOpen(connection);
        } finally {
            contextService.clearCurrentSessionKey();
        }
    }

    private void processOnOpen(Connection connection) {
        _connection = connection;
        exec.execute(this);
        globalSessionService.add(this);
        log.info("Connection opened");
    }

    public void onClose(int closeCode, String message) {
        closeInContext();
    }

    private void closeInContext() {
        try {
            contextService.setSessionKey(gameKey);
            processOnClose();
        } finally {
            contextService.clearCurrentSessionKey();
        }
    }

    private void processOnClose() {
        if (!closed) {
            closed = true;
        } else {
            return;
        }
        if (playerId != null) {
            gameService.freePlayer(playerId);
        }
        exec.shutdown();
        globalSessionService.remove(this);
        if (username != null) {
            globalUsernameService.freeUsername(username);
            Left left = new Left();
            left.setNickname(username);
            globalSessionService.sendUpdate(left);
            lobbyService.removeUserFromAllGames(username);
        }

        log.info("Connection closed");
    }

    public void onMessage(String data) {
        try {
            contextService.setSessionKey(gameKey);
            processMessage(data);
        } finally {
            contextService.clearCurrentSessionKey();
        }

    }

    private void processMessage(String data) {
        NetworkPacket networkPacket = JSON.parseObject(data, NetworkPacket.class);
        Request request = null;
        try {
            localConnection.set(this);
            request = (Request) JSON.parseObject(networkPacket.getPayload(), Class.forName("lv.k2611a.network.req." + networkPacket.getMessageName()));
            autowireCapableBeanFactory.autowireBean(request);
            request.process();
        } catch (Exception e) {
            log.error("Exception while processing message",e);
        } finally {
            localConnection.remove();
        }
    }

    @Override
    public void run() {
        long byteCount = 0;
        long timeStart = System.currentTimeMillis();
        while (!Thread.interrupted()) {
            try {
                if (!_connection.isOpen()) {
                    break;
                }
                Response response = queue.poll(2, TimeUnit.SECONDS);
                if (response == null) {
                    continue;
                }
                if (response instanceof CustomSerializationHeader) {
                    CustomSerializationHeader responseCasted = (CustomSerializationHeader) response;
                    byte[] data = responseCasted.toBytes();
                    byte[] payload = new byte[data.length + 1];
                    payload[0] = responseCasted.serializerId();
                    System.arraycopy(data,0,payload,1,data.length);
                    _connection.sendMessage(payload, 0, payload.length);
                    byteCount += data.length;
                } else {
                    NetworkPacket networkPacket = new NetworkPacket();
                    networkPacket.setMessageName(response.getClass().getSimpleName());
                    networkPacket.setPayload(JSON.toJSONString(response));
                    String toSend = JSON.toJSONString(networkPacket);
                    byteCount += toSend.length() * 2;
                    _connection.sendMessage(toSend);
                }
            } catch (IOException e) {
                log.error("Exception happened while working with websocket", e);
                break;
            } catch (InterruptedException e) {
                break;
            } catch (RuntimeException e) {
                log.error("Exception happened while working with websocket", e);
                break;
            }
        }
        if (_connection.isOpen()) {
            _connection.close();
        }
        long timeEnd = System.currentTimeMillis();
        long timePassed = timeEnd - timeStart;
        double speed = 0;
        if (timePassed / 1000 != 0) {
            speed = byteCount / (timePassed / 1000);
        }
        log.info("Message writer closed. Total bytes sent : " + byteCount + " . Time passed : " + timePassed + " . Speed : " + speed + " bytes / sec");
        closeInContext();
    }

    public void sendMessage(Response response) {
        queue.add(response);
    }

    public String getUsername() {
        if (username == null) {
            throw new AssertionError("Empty username");
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getSelectedBuildingId() {
        return selectedBuildingId;
    }

    public void setSelectedBuildingId(Integer selectedBuildingId) {
        this.selectedBuildingId = selectedBuildingId;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public void setGameKey(GameKey gameKey) {
        this.gameKey = gameKey;
    }
}

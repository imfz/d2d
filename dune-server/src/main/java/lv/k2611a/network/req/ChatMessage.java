package lv.k2611a.network.req;

import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.ClientConnection;
import lv.k2611a.network.resp.IncomingChatMessage;
import lv.k2611a.service.connection.ConnectionState;
import lv.k2611a.service.global.GlobalSessionService;

public class ChatMessage implements Request {

    private String message;

    @Autowired
    private GlobalSessionService sessionService;

    @Autowired
    private ConnectionState connectionState;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void process() {
        IncomingChatMessage incomingChatMessage = new IncomingChatMessage();
        incomingChatMessage.setFrom(connectionState.getUsername());
        incomingChatMessage.setMessage(message);
        for (ClientConnection clientConnection : sessionService.getMembers()) {
            clientConnection.sendMessage(incomingChatMessage);
        }
    }
}

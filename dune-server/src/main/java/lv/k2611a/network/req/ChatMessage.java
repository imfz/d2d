package lv.k2611a.network.req;

import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.ClientConnection;
import lv.k2611a.network.resp.IncomingChatMessage;
import lv.k2611a.service.SessionServiceImpl;

public class ChatMessage implements Request {

    private String message;

    @Autowired
    private SessionServiceImpl sessionService;

    @Override
    public void process() {
        IncomingChatMessage incomingChatMessage = new IncomingChatMessage();
        incomingChatMessage.setFrom(ClientConnection.getCurrentConnection().getUsername());
        incomingChatMessage.setMessage(message);
        for (ClientConnection clientConnection : sessionService.getMembers()) {
            clientConnection.sendMessage(incomingChatMessage);
        }
    }
}

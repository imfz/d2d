package lv.k2611a.network.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.k2611a.ClientConnection;

public class Join implements Request {

    private static final Logger log = LoggerFactory.getLogger(Join.class);

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void process() {
        if (nickname == null) {
            throw new IllegalArgumentException("Nickname cannot be null");
        }
        ClientConnection.getCurrentConnection().setUsername(nickname);
        ClientConnection.getCurrentConnection().setGameKey(null);

        log.info("Joined player with nickname " + nickname);
    }
}

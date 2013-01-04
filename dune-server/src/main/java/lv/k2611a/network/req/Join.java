package lv.k2611a.network.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.ClientConnection;
import lv.k2611a.network.resp.JoinOk;
import lv.k2611a.network.resp.UsernameAlreadyUsed;
import lv.k2611a.service.global.GlobalSessionService;
import lv.k2611a.service.global.GlobalUsernameService;

public class Join implements Request {

    private static final Logger log = LoggerFactory.getLogger(Join.class);

    @Autowired
    private GlobalSessionService globalSessionService;

    @Autowired
    private GlobalUsernameService globalUsernameService;

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
        if (globalUsernameService.loginAsUser(nickname)) {
            ClientConnection.getCurrentConnection().sendMessage(new JoinOk());
            ClientConnection.getCurrentConnection().setUsername(nickname);
            ClientConnection.getCurrentConnection().setGameKey(null);

            log.info("Joined player with nickname " + nickname);
        } else {
            ClientConnection.getCurrentConnection().sendMessage(new UsernameAlreadyUsed());
        }

    }
}

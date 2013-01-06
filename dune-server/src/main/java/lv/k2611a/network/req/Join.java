package lv.k2611a.network.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.network.resp.JoinOk;
import lv.k2611a.network.resp.UsernameAlreadyUsed;
import lv.k2611a.service.connection.ConnectionState;
import lv.k2611a.service.global.GlobalUsernameService;

public class Join implements Request {

    private static final Logger log = LoggerFactory.getLogger(Join.class);


    @Autowired
    private GlobalUsernameService globalUsernameService;

    @Autowired
    private ConnectionState connectionState;

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
            connectionState.getConnection().sendMessage(new JoinOk());
            connectionState.setUsername(nickname);
            connectionState.setGameKey(null);

            log.info("Joined player with nickname " + nickname);
        } else {
            connectionState.getConnection().sendMessage(new UsernameAlreadyUsed());
        }

    }
}

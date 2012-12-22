package lv.k2611a.network.req;

import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.service.UserActionService;

public abstract class AbstractGameStateChanger implements GameStateChanger, Request {

    @Autowired
    private UserActionService userActionService;


    @Override
    public void process() {
        userActionService.registerAction(this);
    }
}

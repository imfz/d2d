package lv.k2611a.testContext;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lv.k2611a.network.req.GameStateChanger;
import lv.k2611a.service.UserActionService;

@Service
public class UserActionServiceTestImpl implements UserActionService {
    @Override
    public void registerAction(GameStateChanger gameStateChanger) {

    }

    @Override
    public List<GameStateChanger> drainActions() {
        return new ArrayList<GameStateChanger>();
    }
}

package lv.k2611a.service.lobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lv.k2611a.domain.lobby.Game;
import lv.k2611a.network.GameDTO;
import lv.k2611a.network.resp.UpdateGameList;
import lv.k2611a.service.game.GameService;
import lv.k2611a.service.global.GlobalSessionService;
import lv.k2611a.service.scope.ContextService;
import lv.k2611a.service.scope.GameKey;
import lv.k2611a.util.MapGenerator;

@Service
public class LobbyServiceImpl implements LobbyService {

    @Autowired
    private GlobalSessionService globalSessionService;

    @Autowired
    private ContextService contextService;

    @Autowired
    private GameService gameService;

    private List<Game> games = new ArrayList<Game>();

    private int id;

    @Override
    public synchronized List<Game> getGames() {
        return Collections.unmodifiableList(games);
    }

    @Override
    public synchronized void addGame(Game game) {
        id++;
        game.setId(id);
        games.add(game);
        contextService.setSessionKey(new GameKey(id));
        gameService.init(MapGenerator.generateMap(64, 64, 8));
    }

    @Scheduled(fixedRate = 1 * 1000)
    public void updateGameList() {
        List<GameDTO> gameDTOList = new ArrayList<GameDTO>();
        for (Game game : games) {
            gameDTOList.add(GameDTO.fromGame(game));
        }
        UpdateGameList updateGameList = new UpdateGameList();
        updateGameList.setGames(gameDTOList.toArray(new GameDTO[gameDTOList.size()]));

        globalSessionService.sendUpdate(updateGameList);

    }
}

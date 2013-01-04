package lv.k2611a.service.lobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lv.k2611a.domain.Player;
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

    private static final Logger log = LoggerFactory.getLogger(LobbyServiceImpl.class);

    @Autowired
    private GlobalSessionService globalSessionService;

    @Autowired
    private ContextService contextService;

    @Autowired
    private GameService gameService;

    private List<Game> games = new ArrayList<Game>();

    private int id;
    private static final int TICKS_TO_KEEP_GAME = 100;

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
        updateGameList();
    }

    @Scheduled(fixedRate = 1 * 1000)
    public synchronized void updateGameList() {
        List<GameDTO> gameDTOList = new ArrayList<GameDTO>();
        for (Game game : games) {
            contextService.setSessionKey(new GameKey(game.getId()));
            GameDTO gameDTO = GameDTO.fromGame(game);

            int totalPlayerCount = 0;
            int usedPlayerCount = 0;
            for (Player player : gameService.getPlayers()) {
                totalPlayerCount++;
                if (player.isUsed()) {
                    usedPlayerCount++;
                }
            }
            gameDTO.setUsedSlotCount(usedPlayerCount);
            gameDTO.setTotalSlotCount(totalPlayerCount);
            gameDTOList.add(gameDTO);

            if (usedPlayerCount == 0) {
                game.setTicksWithoutPlayers(game.getTicksWithoutPlayers()+1);
            } else {
                game.setTicksWithoutPlayers(0);
            }

        }
        UpdateGameList updateGameList = new UpdateGameList();
        updateGameList.setGames(gameDTOList.toArray(new GameDTO[gameDTOList.size()]));

        globalSessionService.sendUpdate(updateGameList);

    }

    @Scheduled(fixedRate = 10 * 1000)
    public synchronized void clearOldGames() {
        List<Game> gamesToRemoveList = new ArrayList<Game>();
        for (Game game : games) {
            if (game.getTicksWithoutPlayers() > TICKS_TO_KEEP_GAME) {
                gamesToRemoveList.add(game);
            }
        }
        for (Game game : gamesToRemoveList) {
            log.info("Removing expired game " + game.getId());
            games.remove(game);
            contextService.clearContext(new GameKey(game.getId()));
            log.info("Removed expired game " + game.getId());
        }
        if (!gamesToRemoveList.isEmpty()) {
            updateGameList();
        }
    }
}

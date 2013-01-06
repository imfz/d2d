package lv.k2611a.service.global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lv.k2611a.domain.lobby.Game;
import lv.k2611a.network.GameDTO;
import lv.k2611a.network.resp.UpdateGameList;
import lv.k2611a.service.scope.ContextService;
import lv.k2611a.service.scope.GameKey;

@Service
public class LobbyServiceImpl implements LobbyService {

    private static final Logger log = LoggerFactory.getLogger(LobbyServiceImpl.class);

    @Autowired
    private GlobalSessionService globalSessionService;

    @Autowired
    private ContextService contextService;


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
        updateGameList();
    }

    @Override
    public synchronized Game getCurrentGame() {
        int id = contextService.getCurrentGameKey().getId();
        for (Game game : games) {
            if (game.getId() == id) {
                return game;
            }
        }
        return null;
    }

    @Override
    public synchronized void movePlayerToObservers(String username) {
        Game currentGame = getCurrentGame();

        if (currentGame.getPlayers().remove(username)) {
            currentGame.getObservers().add(username);
        }
    }

    @Override
    public synchronized void movePlayerToPlayers(String username) {
        Game currentGame = getCurrentGame();

        if (currentGame.getObservers().remove(username)) {
            currentGame.getPlayers().add(username);
        }
    }

    @Override
    public synchronized void addUserToCurrentGame(String username) {
        getCurrentGame().getObservers().add(username);
    }

    @Override
    public synchronized void removeUserFromCurrentGame(String username) {
        getCurrentGame().getObservers().remove(username);
        getCurrentGame().getPlayers().remove(username);
    }

    @Scheduled(fixedRate = 1 * 1000)
    public synchronized void updateGameList() {
        List<GameDTO> gameDTOList = new ArrayList<GameDTO>();
        for (Game game : games) {
            GameDTO gameDTO = GameDTO.fromGame(game);
            gameDTOList.add(gameDTO);

            if (game.getPlayers().isEmpty()) {
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
            destroy(game);
        }
        if (!gamesToRemoveList.isEmpty()) {
            updateGameList();
        }
    }

    @Scheduled(fixedRate = 10 * 1000)
    public synchronized void clearOldContexts() {
        for (GameKey gameKey : contextService.getGameKeys()) {
            boolean found = false;
            for (Game game : games) {
                if (game.getId() == gameKey.getId()) {
                    found = true;
                }
            }
            if (!found) {
                log.info("Removing orhpan game context " + gameKey.getId());
                contextService.clearGameKey(gameKey);
            }
        }
    }

    @Override
    public synchronized boolean isCurrentGameStarted() {
        return getCurrentGame().isStarted();
    }

    @Override
    public synchronized void setCurrentGameStarted() {
        getCurrentGame().setStarted(true);
    }

    @Override
    public synchronized void destroy(Game game) {
        games.remove(game);
        contextService.clearGameKey(new GameKey(game.getId()));
    }

    @Override
    public synchronized void removeUserFromAllGames(String username) {
        for (Game game : games) {
            game.getObservers().remove(username);
            game.getPlayers().remove(username);
        }
    }
}

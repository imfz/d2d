package lv.k2611a.network;

import java.util.List;

import lv.k2611a.domain.lobby.Game;

public class GameDTO {

    private int id;
    private int usedSlotCount;
    private int observersCount;
    private int width;
    private int height;
    private String creator;

    private String[] players;
    private String[] observers;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getUsedSlotCount() {
        return usedSlotCount;
    }

    public void setUsedSlotCount(int usedSlotCount) {
        this.usedSlotCount = usedSlotCount;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getObserversCount() {
        return observersCount;
    }

    public void setObserversCount(int observersCount) {
        this.observersCount = observersCount;
    }

    public String[] getPlayers() {
        return players;
    }

    public void setPlayers(String[] players) {
        this.players = players;
    }

    public String[] getObservers() {
        return observers;
    }

    public void setObservers(String[] observers) {
        this.observers = observers;
    }

    public static GameDTO fromGame(Game game) {
        GameDTO dto = new GameDTO();
        dto.setId(game.getId());
        dto.setWidth(game.getWidth());
        dto.setHeight(game.getHeight());
        dto.setCreator(game.getCreator());
        dto.setUsedSlotCount(game.getPlayers().size());
        List<String> observers = game.getObservers();
        List<String> players = game.getPlayers();
        dto.setPlayers(players.toArray(new String[players.size()]));
        dto.setObservers(observers.toArray(new String[observers.size()]));
        dto.setObserversCount(observers.size());
        return dto;
    }
}

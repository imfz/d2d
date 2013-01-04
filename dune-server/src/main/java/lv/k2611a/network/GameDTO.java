package lv.k2611a.network;

import lv.k2611a.domain.lobby.Game;

public class GameDTO {

    private int id;
    private int totalSlotCount;
    private int freeSlotCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalSlotCount() {
        return totalSlotCount;
    }

    public void setTotalSlotCount(int totalSlotCount) {
        this.totalSlotCount = totalSlotCount;
    }

    public int getFreeSlotCount() {
        return freeSlotCount;
    }

    public void setFreeSlotCount(int freeSlotCount) {
        this.freeSlotCount = freeSlotCount;
    }

    public static GameDTO fromGame(Game game) {
        GameDTO dto = new GameDTO();
        dto.setId(game.getId());
        return dto;
    }
}

package lv.k2611a.network;

import lv.k2611a.domain.lobby.Game;

public class GameDTO {

    private int id;
    private int totalSlotCount;
    private int usedSlotCount;
    private int width;
    private int height;
    private String creator;

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

    public static GameDTO fromGame(Game game) {
        GameDTO dto = new GameDTO();
        dto.setId(game.getId());
        dto.setWidth(game.getWidth());
        dto.setHeight(game.getHeight());
        dto.setCreator(game.getCreator());
        return dto;
    }
}

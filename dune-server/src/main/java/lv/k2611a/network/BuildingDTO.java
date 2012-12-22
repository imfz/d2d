package lv.k2611a.network;

import lv.k2611a.domain.Building;

public class BuildingDTO {
    private long id;
    private int x;
    private int y;
    private int buildingType;
    private int hp;
    private int maxHp;
    private int width;
    private int height;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(int buildingType) {
        this.buildingType = buildingType;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
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

    public static BuildingDTO fromBuilding(Building building) {
        BuildingDTO dto = new BuildingDTO();
        dto.setBuildingType(building.getType().getIdOnJS());
        dto.setHp(building.getHp());
        dto.setMaxHp(building.getType().getHp());
        dto.setId(building.getId());
        dto.setX(building.getX());
        dto.setY(building.getY());
        dto.setWidth(building.getType().getWidth());
        dto.setHeight(building.getType().getHeight());
        return dto;
    }
}

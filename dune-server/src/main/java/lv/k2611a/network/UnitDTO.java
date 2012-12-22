package lv.k2611a.network;

import lv.k2611a.domain.Unit;

public class UnitDTO {
    private long id;
    private int x;
    private int y;
    private int unitType;
    private int viewDirection;
    private double travelled;
    private int hp;
    private int maxHp;

    public int getUnitType() {
        return unitType;
    }

    public void setUnitType(int unitType) {
        this.unitType = unitType;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getViewDirection() {
        return viewDirection;
    }

    public void setViewDirection(int viewDirection) {
        this.viewDirection = viewDirection;
    }

    public double getTravelled() {
        return travelled;
    }

    public void setTravelled(double travelled) {
        this.travelled = travelled;
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

    public static UnitDTO fromUnit(Unit unit) {
        UnitDTO dto = new UnitDTO();
        dto.setUnitType(unit.getUnitType().getIdOnJS());
        dto.setX(unit.getX());
        dto.setY(unit.getY());
        dto.setHp(unit.getHp());
        dto.setMaxHp(unit.getUnitType().getHp());
        dto.setId(unit.getId());
        dto.setViewDirection(unit.getViewDirection().getIdOnJS());
        dto.setTravelled((double)unit.getTicksMovingToNextCell() / unit.getUnitType().getSpeed());
        return dto;
    }
}

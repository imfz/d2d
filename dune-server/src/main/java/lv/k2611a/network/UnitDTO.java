package lv.k2611a.network;

import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.domain.unitgoals.Harvest;
import lv.k2611a.domain.unitgoals.UnitGoal;

public class UnitDTO {
    private int id;
    private int x;
    private int y;
    private int unitType;
    private int viewDirection;
    private double travelled;
    private int hp;
    private int maxHp;
    private int spicePercents;
    private int ownerId;
    private int harvesting;

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

    public void setId(int id) {
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

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getSpicePercents() {
        return spicePercents;
    }

    public void setSpicePercents(int spicePercents) {
        this.spicePercents = spicePercents;
    }

    public int getHarvesting() {
        return harvesting;
    }

    public void setHarvesting(int harvesting) {
        this.harvesting = harvesting;
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
        dto.setOwnerId(unit.getOwnerId());
        if (unit.getUnitType() == UnitType.HARVESTER) {
            int spicePercents = (int) ((double) unit.getTicksCollectingSpice() / Harvest.TICKS_FOR_FULL * 100);
            dto.setSpicePercents(spicePercents);
            UnitGoal uncasted = unit.getCurrentGoal();
            if (uncasted instanceof Harvest) {
                Harvest casted = (Harvest)uncasted;
                dto.setHarvesting(casted.getCollectingSpice());
            }
        }
        return dto;
    }
}

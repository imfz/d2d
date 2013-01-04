package lv.k2611a.domain;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private int id;
    private long money = 10000;
    private long electricity;
    private boolean lost = false;
    private Set<BuildingType> buildingTypes = new HashSet<BuildingType>();
    private boolean used;

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getElectricity() {
        return electricity;
    }

    public void setElectricity(long electricity) {
        this.electricity = electricity;
    }

    public Set<BuildingType> getBuildingTypes() {
        return buildingTypes;
    }

    public void setBuildingTypes(Set<BuildingType> buildingTypes) {
        this.buildingTypes = buildingTypes;
    }

    public boolean hasLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}

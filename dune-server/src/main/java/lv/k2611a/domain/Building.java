package lv.k2611a.domain;

import java.util.ArrayList;
import java.util.List;

import lv.k2611a.domain.buildinggoals.BuildingGoal;
import lv.k2611a.util.Point;

public class Building {
    private int id;
    private BuildingType type;
    private int x;
    private int y;
    private int hp;
    private List<BuildingGoal> goals = new ArrayList<BuildingGoal>();
    private int ticksAccumulated;
    private int ownerId;

    // constr. yard only
    private boolean awaitingClick;
    private BuildingType buildingTypeBuilt;

    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
        this.type = type;
        this.hp = type.getHp();
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

    public int getId() {
        return id;
    }

    public int getHp() {
        return hp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }


    public void addGoal(BuildingGoal goal) {
        goals.add(goal);
    }

    public BuildingGoal getCurrentGoal() {
        if (!goals.isEmpty()) {
            return goals.get(0);
        }
        return null;

    }

    public void removeGoal(BuildingGoal goal) {
        goals.remove(goal);
    }

    public int getTicksAccumulated() {
        return ticksAccumulated;
    }

    public boolean isAwaitingClick() {
        return awaitingClick;
    }

    public void setTicksAccumulated(int ticksAccumulated) {
        this.ticksAccumulated = ticksAccumulated;
    }

    public void setAwaitingClick(boolean awaitingClick) {
        this.awaitingClick = awaitingClick;
    }

    public BuildingType getBuildingTypeBuilt() {
        return buildingTypeBuilt;
    }

    public void setBuildingTypeBuilt(BuildingType buildingTypeBuilt) {
        this.buildingTypeBuilt = buildingTypeBuilt;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Point getPoint() {
        return new Point(x,y);
    }

    public Point getPoint2() {
        return new Point(x+getType().getWidth()-1,y);
    }

    public Point getPoint3() {
        return new Point(x+getType().getWidth()-1,y+getType().getHeight()-1);
    }

    public Point getPoint4() {
        return new Point(x,y+getType().getHeight()-1);
    }
}

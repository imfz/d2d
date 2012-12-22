package lv.k2611a.domain;

import java.util.ArrayList;
import java.util.List;

import lv.k2611a.domain.unitgoals.UnitGoal;
import lv.k2611a.domain.unitgoals.Move;
import lv.k2611a.util.Point;

public class Unit {
    private long id;
    private int x;
    private int y;
    private UnitType unitType;
    private ViewDirection viewDirection = ViewDirection.TOP;
    private List<UnitGoal> goals = new ArrayList<UnitGoal>();
    private int ticksMovingToNextCell;
    private int hp;

    public UnitType getUnitType() {
        return unitType;
    }


    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
        this.hp = unitType.getHp();
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

    public ViewDirection getViewDirection() {
        return viewDirection;
    }

    public void setViewDirection(ViewDirection viewDirection) {
        this.viewDirection = viewDirection;
    }

    public UnitGoal getCurrentGoal() {
        if (goals.isEmpty()) {
            return null;
        }
        return goals.get(0);
    }

    public void setGoal(UnitGoal goal) {
        this.goals = new ArrayList<UnitGoal>();
        if (ticksMovingToNextCell > 0) {
            Move move = new Move(viewDirection.apply(new Point(x,y)));
            goals.add(move);
        }
        goals.add(goal);
    }

    public int getTicksMovingToNextCell() {
        return ticksMovingToNextCell;
    }

    public void setTicksMovingToNextCell(int ticksMovingToNextCell) {
        this.ticksMovingToNextCell = ticksMovingToNextCell;
    }

    public void removeGoal(UnitGoal goal) {
        this.goals.remove(goal);
    }

    public int getHp() {
        return hp;
    }
}

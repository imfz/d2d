package lv.k2611a.domain;

import java.util.ArrayList;
import java.util.List;

import lv.k2611a.domain.goals.Goal;
import lv.k2611a.domain.goals.Move;
import lv.k2611a.util.Point;

public class Unit {
    private long id;
    private int x;
    private int y;
    private UnitType unitType;
    private ViewDirection viewDirection = ViewDirection.TOP;
    private List<Goal> goals = new ArrayList<Goal>();
    private int ticksMovingToNextCell;
    private int hp;

    public Unit copy() {
        Unit copy = new Unit();
        copy.unitType = unitType;
        copy.x = x;
        copy.y = y;
        copy.id = id;
        copy.viewDirection = viewDirection;
        copy.ticksMovingToNextCell = ticksMovingToNextCell;
        copy.goals = new ArrayList<Goal>();
        copy.hp = hp;
        for (Goal goal : goals) {
            copy.goals.add(goal.copy());
        }
        return copy;
    }

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

    public Goal getCurrentGoal() {
        if (goals.isEmpty()) {
            return null;
        }
        return goals.get(0);
    }

    public void setGoal(Goal goal) {
        this.goals = new ArrayList<Goal>();
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

    public void removeGoal(Goal goal) {
        this.goals.remove(goal);
    }

    public int getHp() {
        return hp;
    }
}

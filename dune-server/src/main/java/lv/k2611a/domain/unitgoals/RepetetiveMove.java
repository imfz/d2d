package lv.k2611a.domain.unitgoals;

import java.util.Random;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.Unit;
import lv.k2611a.service.GameServiceImpl;

public class RepetetiveMove implements UnitGoal {

    private int goalX;
    private int goalY;
    private boolean weTried = false;

    public RepetetiveMove(int goalX, int goalY) {
        this.goalX = goalX;
        this.goalY = goalY;
    }

    private int ticksToWait;

    @Override
    public void process(Unit unit, Map map, GameServiceImpl gameService) {
        if (ticksToWait > 0) {
            ticksToWait--;
            return;
        }
        if ((unit.getX() != goalX) || (unit.getY() != goalY)) {
            if (weTried) {
                // we tried and failed, lets wait some ticks
                ticksToWait = new Random().nextInt(10);
                weTried = false;
                return;
            } else {
                unit.insertGoalBeforeCurrent(new Move(goalX, goalY));
                weTried = true;
                return;
            }
        }
        return;
    }
}

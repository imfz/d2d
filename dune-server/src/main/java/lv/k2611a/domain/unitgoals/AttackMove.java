package lv.k2611a.domain.unitgoals;

import lv.k2611a.domain.*;
import lv.k2611a.network.UnitDTO;
import lv.k2611a.service.game.GameServiceImpl;
import lv.k2611a.util.Node;
import lv.k2611a.util.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttackMove implements UnitGoal {

    private static final Logger log = LoggerFactory.getLogger(AttackMove.class);

    private Guard guardGoal;
    private Move moveGoal;
    private boolean moved = false;

    public AttackMove(int goalX, int goalY) {
        moveGoal = new Move(new Point(goalX, goalY));
        guardGoal = new Guard();
    }

    public AttackMove(Point point) {
        moveGoal = new Move(point);
        guardGoal = new Guard();
    }

    public boolean hasMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    @Override
    public void reserveTiles(Unit unit, Map map) {
        moveGoal.reserveTiles(unit, map);
    }

    @Override
    public void process(Unit unit, Map map, GameServiceImpl gameService) {
        // If we are currently moving, keep moving
        if (unit.getTicksSpentOnCurrentGoal() > 0) {
            moveGoal.process(unit, map, gameService);
            return;
        }
        guardGoal.process(unit, map, gameService);
        // If we have not fired and there are no enemies in range, we can start moving.
        if (!guardGoal.getAttemptedToFire() && !map.enemiesPresentInAttackRange(unit, map)) {
            moveGoal.process(unit, map, gameService);
            moved = true;
        }
    }

    @Override
    public void saveAdditionalInfoIntoDTO(Unit unit, UnitDTO dto) {
        if (moved) {
            moveGoal.saveAdditionalInfoIntoDTO(unit, dto);
        } else {
            guardGoal.saveAdditionalInfoIntoDTO(unit, dto);
        }
    }
}

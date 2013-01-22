package lv.k2611a.domain.unitgoals;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.ViewDirection;
import lv.k2611a.network.UnitDTO;
import lv.k2611a.service.game.GameServiceImpl;
import lv.k2611a.util.Point;

public class Turn implements UnitGoal {

    private ViewDirection goalDirection;

    public Turn(ViewDirection goalDirection) {
        this.goalDirection = goalDirection;
    }

    public ViewDirection getGoalDirection() {
        return goalDirection;
    }

    @Override
    public void reserveTiles(Unit unit, Map map) {
        map.setUsedByUnit(unit.getX(), unit.getY(), unit.getId());
    }

    @Override
    public void process(Unit unit, Map map, GameServiceImpl gameService) {
        if (unit.getViewDirection() != goalDirection) {
            int ticksRequiredToTurn = unit.getUnitType().getTurnSpeed();
            unit.setTicksSpentOnCurrentGoal(unit.getTicksSpentOnCurrentGoal() + 1);
            if (unit.getTicksSpentOnCurrentGoal() >= ticksRequiredToTurn) {
                unit.setTicksSpentOnCurrentGoal(0);
                unit.setViewDirection(unit.getViewDirection().turnInDirection(goalDirection));
                unit.removeGoal(this);
            }
        } else {
            unit.removeGoal(this);
        }
    }
    @Override
    public void saveAdditionalInfoIntoDTO(Unit unit, UnitDTO dto) {

    }
}

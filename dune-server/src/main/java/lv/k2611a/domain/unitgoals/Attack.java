package lv.k2611a.domain.unitgoals;

import java.util.ArrayList;
import java.util.List;

import lv.k2611a.domain.*;
import lv.k2611a.network.UnitDTO;
import lv.k2611a.service.game.GameServiceImpl;
import lv.k2611a.util.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Attack extends FireGoal implements UnitGoal {

    private static final Logger log = LoggerFactory.getLogger(Move.class);

    public Attack(Target target) {
        this.target = target;
    }

    @Override
    public void reserveTiles(Unit unit, Map map) {
        map.setUsedByUnit(unit.getX(), unit.getY(), unit.getId());
    }

    @Override
    public void process(Unit unit, Map map, GameServiceImpl gameService) {
        if (target.getEntity() == Entity.BUILDING) {
            if (map.getBuilding(target.getId()) == null) {
                unit.removeGoal(this);
                return;
            }
            target.setPoint(map.getClosestPoint(map.getBuilding(target.getId()), unit));
            attackTarget(unit, map, gameService);
        } else if (target.getEntity() == Entity.UNIT) {
            if (map.getUnit(target.getId()) == null) {
                unit.removeGoal(this);
                return;
            }
            target.setPoint(map.getUnit(target.getId()).getPoint());
            attackTarget(unit, map, gameService);
        } else {
            log.warn("An alien is under attack!");
            unit.removeGoal(this);
        }
    }

    private void attackTarget(Unit unit, Map map, GameServiceImpl gameService) {
        if (map.targetInAttackRange(unit, target.getPoint())) {
            if (!needToTurnToTarget(unit, map, gameService)) {
                if (unit.getTicksReloading() == 0) {
                    fire(unit, map);
                }
            }
        } else {
            unit.insertGoalBeforeCurrent(new Chase(target));
            unit.getCurrentGoal().process(unit, map, gameService);
        }
    }

    @Override
    public void saveAdditionalInfoIntoDTO(Unit unit, UnitDTO dto) {

    }
}

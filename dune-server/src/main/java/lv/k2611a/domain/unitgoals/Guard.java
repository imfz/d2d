package lv.k2611a.domain.unitgoals;

import lv.k2611a.domain.*;
import lv.k2611a.domain.Map;
import lv.k2611a.network.UnitDTO;
import lv.k2611a.service.game.GameServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Guard extends FireGoal implements UnitGoal  {

    private static final Logger log = LoggerFactory.getLogger(Guard.class);

    public Guard() {
    }

    @Override
    public void reserveTiles(Unit unit, Map map) {
        map.setUsedByUnit(unit.getX(), unit.getY(), unit.getId());
    }

    @Override
    public void process(Unit unit, Map map, GameServiceImpl gameService) {
        if (unit.getUnitType() == UnitType.HARVESTER) {
            return;
        }
        setAttemptedToFire(false);
        if (unit.getTicksReloading() == 0) {
            if (target != null) {
                // Try attacking last target
                if (attackLastTarget(unit, map, gameService)) {
                    return;
                }
                // If it failed(target dead or outside of attack range), forget about it
                target = null;
            }
            List<Target> targetList = map.getTargetsInRange(unit.getPoint(), unit.getUnitType().getAttackRange(), map);
            // If we have found at least one target in range, check if there are any enemies among these and pick a target
            if (!targetList.isEmpty()) {
                target = findBestTarget(unit, map, targetList);
                if (target != null) {
                    setAttemptedToFire(true);
                    if (!needToTurnToTarget(unit, map, gameService)) {
                        fire(unit, map);
                    }
                }
            }
            if (map.enemiesPresentInAttackRange(unit, map) != getAttemptedToFire()) {
                if (getAttemptedToFire()) {
                    log.error("GUARD fired at a target outside of attack range! UnitId:" + unit.getId()
                            + " Coords:"+ unit.getX() + "-" + unit.getY() + " Range:" + unit.getUnitType().getAttackRange()
                            + " TargetId:" + target.getId() + " TargetCoords:" + target.getPoint().getX() + "-" + target.getPoint().getY());
                } else {
                    log.error("GUARD did not fire at a target in attack range! UnitId:" + unit.getId()
                            + " Coords:"+ unit.getX() + "-" + unit.getY());
                }
            }
        }
    }

    private boolean attackLastTarget(Unit unit, Map map, GameServiceImpl gameService) {
        if (target.getEntity() == Entity.BUILDING) {
            Building targetBuilding = map.getBuilding(target.getId());
            if (targetBuilding != null) {
                target.setPoint(map.getClosestPoint(targetBuilding, unit));
                if (!DiplomacyUtil.isAlly(unit.getOwnerId(), targetBuilding.getOwnerId())
                        && map.targetInAttackRange(unit, target.getPoint())) {
                    setAttemptedToFire(true);
                    if (!needToTurnToTarget(unit, map, gameService)) {
                        fire(unit, map);
                    }
                    return true;
                }
            }
        } else if (target.getEntity() == Entity.UNIT) {
            Unit targetUnit = map.getUnit(target.getId());
            if (targetUnit != null) {
                target.setPoint(targetUnit.getPoint());
                if (!DiplomacyUtil.isAlly(unit.getOwnerId(), targetUnit.getOwnerId())
                        && map.targetInAttackRange(unit, target.getPoint())) {
                    setAttemptedToFire(true);
                    if (!needToTurnToTarget(unit, map, gameService)) {
                        fire(unit, map);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private Target findBestTarget(Unit unit, Map map, List<Target> targetList) {
        List<Target> bestTargets = new ArrayList<Target>();
        int bestTargetCostEffectiveness = 0;
        for (Target target : targetList) {
            if (target.getEntity() == Entity.UNIT) {
                Unit targetUnit = map.getUnit(target.getId());
                if (!DiplomacyUtil.isAlly(unit.getOwnerId(), targetUnit.getOwnerId())) {
                    int targetUnitCostEffectiveness = targetUnit.getUnitType().getCostEffectiveness();
                    if (targetUnitCostEffectiveness > bestTargetCostEffectiveness) {
                        bestTargets.clear();
                        bestTargets.add(target);
                        bestTargetCostEffectiveness = targetUnitCostEffectiveness;
                    } else if (targetUnitCostEffectiveness == bestTargetCostEffectiveness) {
                        bestTargets.add(target);
                    }
                }
            } else if (target.getEntity() == Entity.BUILDING) {
                Building targetBuilding = map.getBuilding(target.getId());
                if (!DiplomacyUtil.isAlly(unit.getOwnerId(), targetBuilding.getOwnerId())) {
                    int targetBuildingCostEffectiveness = targetBuilding.getType().getCostEffectiveness();
                    if (targetBuildingCostEffectiveness > bestTargetCostEffectiveness) {
                        bestTargets.clear();
                        bestTargets.add(target);
                        bestTargetCostEffectiveness = targetBuildingCostEffectiveness;
                    } else if (targetBuildingCostEffectiveness == bestTargetCostEffectiveness) {
                        bestTargets.add(target);
                    }
                }
            }
        }
        if (!bestTargets.isEmpty()) {
            Collections.shuffle(bestTargets);
            return bestTargets.get(0);
        }
        return null;
    }

    @Override
    public void saveAdditionalInfoIntoDTO(Unit unit, UnitDTO dto) {

    }
}

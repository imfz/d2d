package lv.k2611a.domain.unitgoals;

import java.util.ArrayList;
import java.util.List;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.Bullet;
import lv.k2611a.domain.BulletType;
import lv.k2611a.domain.Entity;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.ViewDirection;
import lv.k2611a.network.UnitDTO;
import lv.k2611a.service.game.GameServiceImpl;
import lv.k2611a.util.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Attack implements UnitGoal {

    private static final Logger log = LoggerFactory.getLogger(Move.class);
    private int targetId;
    private Entity targetEntity;

    public Attack(Entity targetEntity, int targetId) {
        this.targetId = targetId;
        this.targetEntity = targetEntity;
    }

    @Override
    public void reserveTiles(Unit unit, Map map) {
        map.setUsed(unit.getX(), unit.getY(), unit.getId());
    }

    @Override
    public void process(Unit unit, Map map, GameServiceImpl gameService) {
        if (targetEntity == Entity.BUILDING) {
            if (map.getBuilding(targetId) == null) {
                unit.removeGoal(this);
                return;
            }
            Point targetBuildingPoint = getClosestPoint(map.getBuilding(targetId), unit);
            attackTarget(unit, targetBuildingPoint, map, gameService);
        } else if (targetEntity == Entity.UNIT) {
            if (map.getUnit(targetId) == null) {
                unit.removeGoal(this);
                return;
            }
            Point targetUnitPoint = map.getUnit(targetId).getPoint();
            attackTarget(unit, targetUnitPoint, map, gameService);
        } else {
            log.warn("An alien is under attack!");
            unit.removeGoal(this);
        }
    }
    private void attackTarget(Unit unit, Point targetPoint, Map map, GameServiceImpl gameService) {
        if (targetInAttackRange(unit, targetPoint)) {
            // unit.setViewDirection(ViewDirection.getDirection(unit.getPoint(), closestBuildingPoint));
            if (unit.getTicksReloading() == 0) {
                fire(unit, map, targetPoint);
            }
        } else {
            unit.insertGoalBeforeCurrent(new Chase(targetEntity, targetId, targetPoint));
            unit.getCurrentGoal().process(unit, map, gameService);
        }
    }

    private boolean targetInAttackRange(Unit unit, Point targetPoint) {
        return Map.getDistanceBetween(unit.getPoint(), targetPoint) <= unit.getUnitType().getAttackRange();
    }

    private void fire(Unit unit, Map map, Point target) {
        Bullet bullet = new Bullet();
        bullet.setDamageToDeal(unit.getUnitType().getAttackDamage());
        bullet.setStartX(unit.getX());
        bullet.setStartY(unit.getY());
        bullet.setGoalX(target.getX());
        bullet.setGoalY(target.getY());
        int bulletTicksToMove = (int) Math.round(unit.getUnitType().getBulletSpeed() * Map.getDistanceBetween(unit.getPoint(), target));

        bullet.setTicksToMove(bulletTicksToMove);
        bullet.setTicksToMoveTotal(bulletTicksToMove);
        bullet.setBulletType(BulletType.TANK_SHOT);
        map.addBullet(bullet);
        unit.setTicksReloading(unit.getUnitType().getTicksToAttack());
    }

    private Point getClosestPoint(Building building, Unit unit) {
        List<Point> points = new ArrayList<Point>();
        points.add(building.getPoint());
        points.add(building.getPoint2());
        points.add(building.getPoint3());
        points.add(building.getPoint4());

        return Map.getClosestNode(unit.getPoint(), points);
    }

    @Override
    public void saveAdditionalInfoIntoDTO(Unit unit, UnitDTO dto) {

    }
}

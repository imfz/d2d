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
import lv.k2611a.service.game.GameServiceImpl;
import lv.k2611a.util.Point;

public class Attack implements UnitGoal {

    private Entity entity;
    private int entityId;

    public Attack(Entity entity, int entityId) {
        this.entity = entity;
        this.entityId = entityId;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public void process(Unit unit, Map map, GameServiceImpl gameService) {
        if (entity == Entity.BUILDING) {
            if (map.getBuilding(entityId) == null) {
                return;
            }
            attackBuilding(unit,map,gameService);
        }
        if (entity == Entity.UNIT) {
            if (map.getUnit(entityId) == null) {
                return;
            }
            attackUnit(unit, map, gameService);
        }
    }

    private void attackUnit(Unit unit, Map map, GameServiceImpl gameService) {
        Unit target = map.getUnit(entityId);
        if (inRange(target,unit,map)) {
            fireUnit(target, unit, map, gameService);
        } else {
            move(target,unit,map);
        }
    }

    private void attackBuilding(Unit unit, Map map, GameServiceImpl gameService) {
        Building building = map.getBuilding(entityId);
        if (inRange(building,unit,map)) {
            fireBuilding(building, unit, map, gameService);
        } else {
            move(building,unit,map);
        }
    }

    private void move(Building building, Unit unit, Map map) {
        Point bestPoint = getClosestPoint(building, unit);
        final int buildingId = building.getId();
        final int unitOwnerId = unit.getOwnerId();
        unit.insertGoalBeforeCurrent(new Move(bestPoint.getX(), bestPoint.getY(),unit.getUnitType().getAttackRange(), new MoveExpired() {
            @Override
            public boolean isExpired(Move move, Map map) {
                Building building = map.getBuilding(buildingId);
                if (building == null) {
                    return true;
                }
                if (building.getOwnerId() == unitOwnerId) {
                    return true;
                }
                return false;
            }
        }));
    }

    private void move(Unit target, Unit unit, Map map) {
        Point bestPoint = target.getPoint();
        final int unitId = target.getId();
        final int ownerId = target.getOwnerId();
        final Point unitPoint = target.getPoint();
        unit.insertGoalBeforeCurrent(new Move(bestPoint.getX(), bestPoint.getY(),unit.getUnitType().getAttackRange(), new MoveExpired() {
            @Override
            public boolean isExpired(Move move, Map map) {
                Unit unit = map.getUnit(unitId);
                if (unit == null) {
                    return true;
                }
                if (unit.getOwnerId() == ownerId) {
                    return true;
                }
                if (!unitPoint.equals(unit.getPoint())) {
                    return true;
                }
                return false;
            }
        }));
    }

    private Point getClosestPoint(Building building, Unit unit) {
        List<Point> points = new ArrayList<Point>();
        points.add(building.getPoint());
        points.add(building.getPoint2());
        points.add(building.getPoint3());
        points.add(building.getPoint4());

        return Map.getClosestNode(unit.getPoint(), points);
    }

    private void fireBuilding(Building building, Unit unit, Map map, GameServiceImpl gameService) {
        Point closestPoint = getClosestPoint(building, unit);
        unit.setViewDirection(ViewDirection.getDirection(unit.getPoint(), closestPoint));
        if (unit.getTicksReloading() == 0) {
            fire(unit, map, closestPoint);
        }
    }

    private void fireUnit(Unit target, Unit unit, Map map, GameServiceImpl gameService) {
        Point closestPoint = target.getPoint();
        unit.setViewDirection(ViewDirection.getDirection(unit.getPoint(), closestPoint));
        if (unit.getTicksReloading() == 0) {
            fire(unit, map, closestPoint);
        }
    }

    private void fire(Unit unit, Map map, Point closestPoint) {
        Bullet bullet = new Bullet();
        bullet.setDamageToDeal(unit.getUnitType().getAttackDamage());
        bullet.setStartX(unit.getX());
        bullet.setStartY(unit.getY());
        bullet.setGoalX(closestPoint.getX());
        bullet.setGoalY(closestPoint.getY());
        int bulletTicksToMove = (int) Math.round(unit.getUnitType().getBulletSpeed() * Map.getDistanceBetween(unit.getPoint(), closestPoint));

        bullet.setTicksToMove(bulletTicksToMove);
        bullet.setTicksToMoveTotal(bulletTicksToMove);
        bullet.setBulletType(BulletType.TANK_SHOT);
        map.addBullet(bullet);
        unit.setTicksReloading(unit.getUnitType().getTicksToAttack());
    }

    private boolean inRange(Building building, Unit unit, Map map) {
        Point bestPoint = getClosestPoint(building, unit);
        return Map.getDistanceBetween(bestPoint, unit.getPoint()) <= unit.getUnitType().getAttackRange();
    }

    private boolean inRange(Unit target, Unit unit, Map map) {
        Point bestPoint = target.getPoint();
        return Map.getDistanceBetween(bestPoint, unit.getPoint()) <= unit.getUnitType().getAttackRange();
    }
}

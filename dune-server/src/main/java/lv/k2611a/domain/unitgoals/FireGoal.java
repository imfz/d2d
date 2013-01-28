package lv.k2611a.domain.unitgoals;

import lv.k2611a.domain.*;
import lv.k2611a.service.game.GameServiceImpl;
import lv.k2611a.util.Point;

import java.util.ArrayList;
import java.util.List;

public abstract class FireGoal implements UnitGoal {

    protected Target target;

    private boolean attemptedToFire = false;

    protected boolean getAttemptedToFire() {
        return attemptedToFire;
    }

    protected void setAttemptedToFire(boolean attemptedToFire) {
        this.attemptedToFire = attemptedToFire;
    }

    protected void fire(Unit unit, Map map) {
        Bullet bullet = new Bullet();
        bullet.setDamageToDeal(unit.getUnitType().getAttackDamage());
        bullet.setStartX(unit.getX());
        bullet.setStartY(unit.getY());
        bullet.setGoalX(target.getPoint().getX());
        bullet.setGoalY(target.getPoint().getY());
        int bulletTicksToMove = (int) Math.round(unit.getUnitType().getBulletSpeed() * Map.getDistanceBetween(unit.getPoint(), target.getPoint()));

        bullet.setTicksToMove(bulletTicksToMove);
        bullet.setTicksToMoveTotal(bulletTicksToMove);
        bullet.setBulletType(BulletType.TANK_SHOT);
        map.addBullet(bullet);
        unit.setTicksReloading(unit.getUnitType().getTicksToAttack());
    }

    // Handle the turning of the unit. If our view direction does not match target direction, we turn
    protected boolean needToTurnToTarget(Unit unit, Map map, GameServiceImpl gameService) {
        ViewDirection goalDirection = ViewDirection.getDirection(unit.getPoint(), target.getPoint());
        if (unit.getViewDirection() != goalDirection) {
            unit.insertGoalBeforeCurrent(new Turn(goalDirection));
            unit.getCurrentGoal().process(unit, map, gameService);
            return true;
        }
        return false;
    }
}

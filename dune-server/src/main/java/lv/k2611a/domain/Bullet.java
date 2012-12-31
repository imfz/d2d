package lv.k2611a.domain;

public class Bullet {
    private int id;
    private BulletType bulletType;
    private int startX;
    private int startY;
    private int goalX;
    private int goalY;
    private int ticksToMove;
    private int ticksToMoveTotal;
    private int damageToDeal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getGoalX() {
        return goalX;
    }

    public void setGoalX(int goalX) {
        this.goalX = goalX;
    }

    public int getGoalY() {
        return goalY;
    }

    public void setGoalY(int goalY) {
        this.goalY = goalY;
    }

    public int getTicksToMove() {
        return ticksToMove;
    }

    public void setTicksToMove(int ticksToMove) {
        this.ticksToMove = ticksToMove;
    }

    public int getDamageToDeal() {
        return damageToDeal;
    }

    public void setDamageToDeal(int damageToDeal) {
        this.damageToDeal = damageToDeal;
    }

    public int getTicksToMoveTotal() {
        return ticksToMoveTotal;
    }

    public void setTicksToMoveTotal(int ticksToMoveTotal) {
        this.ticksToMoveTotal = ticksToMoveTotal;
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public void setBulletType(BulletType bulletType) {
        this.bulletType = bulletType;
    }
}

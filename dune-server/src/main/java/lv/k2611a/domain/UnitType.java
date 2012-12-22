package lv.k2611a.domain;

public enum UnitType {
    BATTLE_TANK(1, 40, 100),
    SIEGE_TANK(2, 60, 120),
    LAUNCHER(3, 30, 50),
    DEVASTATOR(4, 100, 200)
    ;

    private final int idOnJS;
    private final int speed; // ticks for cell
    private final int hp;

    private UnitType(int idOnJS, int speed, int hp) {
        this.idOnJS = idOnJS;
        this.speed = speed;
        this.hp = hp;
    }

    public int getIdOnJS() {
        return idOnJS;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHp() {
        return hp;
    }
}

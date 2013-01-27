package lv.k2611a.domain;

public class Explosion {

    private ExplosionType explosionType = ExplosionType.BIG;
    private int x;
    private int y;

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ExplosionType getExplosionType() {
        return explosionType;
    }
}

package lv.k2611a.domain;

public enum TileType {
    SAND(1),ROCK(2),SPICE(3),RICH_SPICE(4);

    private int idOnJS;

    private TileType(int idOnJS) {
        this.idOnJS = idOnJS;
    }

    public int getIdOnJS() {
        return idOnJS;
    }
}

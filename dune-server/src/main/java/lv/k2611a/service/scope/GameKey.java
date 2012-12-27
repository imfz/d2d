package lv.k2611a.service.scope;

public class GameKey {
    private final int id;

    public GameKey(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GameKey gameKey = (GameKey) o;

        if (id != gameKey.id) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

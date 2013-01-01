package lv.k2611a.domain.unitgoals;

import lv.k2611a.domain.Map;

public interface MoveExpired {
    boolean isExpired(Move move, Map map);
}

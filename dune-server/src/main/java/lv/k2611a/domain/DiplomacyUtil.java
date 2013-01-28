package lv.k2611a.domain;

public class DiplomacyUtil {
    public static boolean isAlly (int ownerId1, int ownerId2) {
        return ownerId1 == ownerId2;
    }
}

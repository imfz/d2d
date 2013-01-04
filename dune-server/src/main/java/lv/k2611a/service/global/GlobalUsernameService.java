package lv.k2611a.service.global;

public interface GlobalUsernameService {
    boolean loginAsUser(String username);
    void freeUsername(String username);
}

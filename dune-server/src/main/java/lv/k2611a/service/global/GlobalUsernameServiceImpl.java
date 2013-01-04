package lv.k2611a.service.global;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class GlobalUsernameServiceImpl implements GlobalUsernameService {

    private Set<String> usernameSet = new HashSet<String>();

    @Override
    public synchronized boolean loginAsUser(String username) {
        return usernameSet.add(username);
    }

    @Override
    public synchronized void freeUsername(String username) {
        usernameSet.remove(username);
    }
}

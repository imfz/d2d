package lv.k2611a.service.scope;

import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class GameScope implements Scope  {

    private ContextService contextService;

    public void setContextService(ContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    public synchronized Object get(String name, ObjectFactory<?> objectFactory) {
        Object result = null;
        Map<String, Object> hBeans = contextService.getCurrentGameContext().getBeanMap();

        if (!hBeans.containsKey(name)) {
            result = objectFactory.getObject();
            hBeans.put(name, result);
        } else {
            result = hBeans.get(name);
        }

        return result;
    }

    @Override
    public synchronized Object remove(String name) {
        Object result = null;
        Map<String, Object> hBeans = contextService.getCurrentGameContext().getBeanMap();
        if (hBeans.containsKey(name)) {
            result = hBeans.get(name);
            hBeans.remove(name);
        }

        return result;
    }

    @Override
    public synchronized void registerDestructionCallback(String name, Runnable callback) {
        contextService.getCurrentGameContext().registerRequestDestructionCallback(name, callback);
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}

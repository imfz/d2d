package lv.k2611a.service.scope;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public abstract class AbstractScope implements Scope {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractScope.class);

    @Override
    public synchronized Object get(String name, ObjectFactory<?> objectFactory) {
        Object result = null;
        Context currentContext = getContext();
        if (currentContext == null) {
            LOG.warn("No scope");
            return null;
        }
        Map<String, Object> hBeans = currentContext.getBeanMap();

        if (!hBeans.containsKey(name)) {
            result = objectFactory.getObject();
            hBeans.put(name, result);
        } else {
            result = hBeans.get(name);
        }

        return result;
    }

    protected abstract Context getContext();

    @Override
    public synchronized Object remove(String name) {
        Object result = null;
        Context currentContext = getContext();
        if (currentContext == null) {
            return null;
        }
        Map<String, Object> hBeans = currentContext.getBeanMap();
        if (hBeans.containsKey(name)) {
            result = hBeans.get(name);
            hBeans.remove(name);
        }

        return result;
    }

    @Override
    public synchronized void registerDestructionCallback(String name, Runnable callback) {
        Context currentContext = getContext();
        if (currentContext == null) {
            return;
        }
        currentContext.registerRequestDestructionCallback(name, callback);
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

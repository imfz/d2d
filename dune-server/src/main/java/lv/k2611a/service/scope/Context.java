package lv.k2611a.service.scope;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class Context {
    final Logger logger = LoggerFactory.getLogger(Context.class);

    protected final Map<String, Object> hBeans = new HashMap<String, Object>();
    protected final Map<String, Runnable> hRequestDestructionCallbacks = new LinkedHashMap<String, Runnable>();

    protected final Map<String, Object> getBeanMap() {
        return hBeans;
    }

    /**
     * Register the given callback as to be executed after request completion.
     *
     * @param name     The name of the bean.
     * @param callback The callback of the bean to be executed for destruction.
     */
    protected synchronized final void registerRequestDestructionCallback(String name, Runnable callback) {
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(callback, "Callback must not be null");

        hRequestDestructionCallbacks.put(name, callback);
    }

    /**
     * Clears beans and processes all bean destruction callbacks.
     */
    public synchronized final void clear() {
        processDestructionCallbacks();

        hBeans.clear();
    }

    /**
     * Processes all bean destruction callbacks.
     */
    private synchronized final void processDestructionCallbacks() {
        for (String name : hRequestDestructionCallbacks.keySet()) {
            Runnable callback = hRequestDestructionCallbacks.get(name);

            logger.debug("Performing destruction callback for '" + name + "' bean" +
                    " on thread '" + Thread.currentThread().getName() + "'.");

            callback.run();
        }

        hRequestDestructionCallbacks.clear();
    }
}

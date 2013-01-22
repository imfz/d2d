package lv.k2611a.service.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

@Service
class ContextClosedHandler implements ApplicationListener<ContextClosedEvent> {

    private static final Logger log = LoggerFactory.getLogger(ContextClosedHandler.class);

    @Autowired
    private ThreadPoolTaskExecutor executor;
    @Autowired
    private ThreadPoolTaskScheduler scheduler;

    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("Context closing event received");
        try {
            log.info("Shutting down scheduler..");
            scheduler.shutdown();
            log.info("Scheduler shut down");
        } catch (Exception e) {
            log.error("Exception while shutting down scheduler",e);
        }
        try {
            log.info("Shutting down executor..");
            executor.shutdown();
            log.info("Executor shut down");
        } catch (Exception e) {
            log.error("Exception while shutting down executor",e);
        }
    }
}
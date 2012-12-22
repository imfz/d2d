package lv.k2611a.testContext;

import org.springframework.stereotype.Service;

import lv.k2611a.jmx.ServerMonitor;

@Service
public class ServerMonitorTestImpl implements ServerMonitor {
    @Override
    public void reportTickTime(long tickTime) {

    }
}

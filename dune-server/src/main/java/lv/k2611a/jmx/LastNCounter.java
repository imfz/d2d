package lv.k2611a.jmx;

public class LastNCounter {
    private final long[] lastTicks;
    private int tickToWriteInto;

    public LastNCounter(int tickCountToSave) {
        lastTicks = new long[tickCountToSave];
        for (int i = 0; i < lastTicks.length; i++) {
            lastTicks[i] = -1;
        }
    }

    public Double averageTickTime() {
        int tickCount = 0;
        long totalTime = 0;
        for (long tickTime : lastTicks) {
            if (tickTime != -1) {
                tickCount++;
                totalTime += tickTime;
            }
        }
        if (tickCount != 0) {
            return (double)totalTime / tickCount;
        }
        return null;
    }

    public synchronized void reportTickTime(long tickTime) {
        tickToWriteInto++;
        if (tickToWriteInto >= lastTicks.length) {
            tickToWriteInto = 0;
        }
        this.lastTicks[tickToWriteInto] = tickTime;
    }
}

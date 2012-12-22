package lv.k2611a.jmx;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class LastNCounterTest {

    @Test
    public void noTicksNoValue() {
        LastNCounter counter = new LastNCounter(10);
        assertEquals(null, counter.averageTickTime());
    }

    @Test
    public void sameTicksSameAverage() {
        LastNCounter counter = new LastNCounter(10);
        for (int i = 0; i < 50; i++) {
            counter.reportTickTime(50);
            assertEquals(50d, counter.averageTickTime(), 0.01);
        }
    }
}

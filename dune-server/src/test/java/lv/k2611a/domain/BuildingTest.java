package lv.k2611a.domain;

import org.junit.Test;

import lv.k2611a.domain.buildinggoals.BuildingGoal;
import lv.k2611a.domain.buildinggoals.CreateBuilding;

import static junit.framework.Assert.assertEquals;

public class BuildingTest {
    @Test
    public void testGoals() {
        Building building = new Building();
        BuildingGoal goal = new CreateBuilding();
        building.addGoal(goal);
        assertEquals(goal, building.getCurrentGoal());
        building.removeGoal(goal);
        assertEquals(null, building.getCurrentGoal());
    }
}

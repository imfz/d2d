package lv.k2611a.domain.unitgoals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.BuildingType;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.Player;
import lv.k2611a.domain.RefineryEntrance;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.domain.ViewDirection;
import lv.k2611a.service.GameServiceImpl;
import lv.k2611a.util.Point;

public class ReturnToBase implements UnitGoal {

    public static int TICKS_COLLECTING_UNLOADED_PER_TICK = 4;
    public static final int MONEY_PER_TICK = 40;

    private Point targetRefinery;
    private long targetRefineryId;

    public ReturnToBase() {
    }

    public ReturnToBase(Point targetRefinery, long targetRefineryId) {
        this.targetRefinery = targetRefinery;
        this.targetRefineryId = targetRefineryId;
    }

    public ReturnToBase(RefineryEntrance refineryEntrance) {
        this.targetRefinery = refineryEntrance.getPoint();
        this.targetRefineryId = refineryEntrance.getRefineryId();
    }

    @Override
    public void process(Unit unit, Map map, GameServiceImpl gameService) {
        if (unit.getUnitType() != UnitType.HARVESTER) {
            unit.removeGoal(this);
            return;
        }
        if (unit.getTicksCollectingSpice() <= 0) {
            unit.setTicksCollectingSpice(0);
            unit.removeGoal(this);
            unit.setGoal(new Harvest());
        }
        if (targetRefinery == null) {
            searchBase(unit, map);
        }
        if (targetRefinery == null) {
            // no refinery found.
            return;
        }
        Building building = map.getBuilding(targetRefineryId);
        if (building == null) {
            // refinery dissappeared
            targetRefinery = null;
            targetRefineryId = 0;
            return;
        }

        if (building.getOwnerId() != unit.getOwnerId()) {
            // refinery is no longer ours
            targetRefinery = null;
            targetRefineryId = 0;
            return;
        }

        if (unit.getPoint().equals(targetRefinery)) {
            unloadSpice(unit, map, gameService);
        } else {
            moveToRefinery(map, unit);
        }

    }

    private void moveToRefinery(Map map, Unit unit) {
        if (map.getTile(targetRefinery).isUsedByUnit()) {
            // refinery already occupied, go search for another
            targetRefinery = null;
            targetRefineryId = 0;
        } else {
            unit.insertGoalBeforeCurrent(new Move(targetRefinery));
        }
    }

    private void unloadSpice(Unit unit, Map map, GameServiceImpl gameService) {
        unit.setViewDirection(ViewDirection.TOP);
        unit.setTicksCollectingSpice(unit.getTicksCollectingSpice() - TICKS_COLLECTING_UNLOADED_PER_TICK);
        Player player = map.getPlayerById(unit.getOwnerId());
        player.setMoney(player.getMoney() + MONEY_PER_TICK);
        if (unit.getTicksCollectingSpice() <= 0) {
            unit.setTicksCollectingSpice(0);
            unit.removeGoal(this);
            if (unit.getY() > 2) {
                if (!map.getTile(unit.getX(), unit.getY()-1).isUsedByUnit()) {
                   unit.setX(unit.getX());
                   unit.setY(unit.getY()-1);
                }
            }
            unit.setGoal(new Harvest());
        }
    }


    private void searchBase(Unit unit, Map map) {
        Point unitCoordinates = unit.getPoint();
        List<Pair> targets = new ArrayList<Pair>();

        for (java.util.Map.Entry<Point, RefineryEntrance> pointRefineryEntranceEntry : map.getRefineryEntranceList().entrySet()) {
            RefineryEntrance refineryEntrance = pointRefineryEntranceEntry.getValue();
            if (refineryEntrance.getOwnerId() == unit.getOwnerId()) {
                Point point = refineryEntrance.getPoint();
                if (!map.getTile(point).isUsedByUnit()) {
                    double distanceBetween = Map.getDistanceBetween(point, unitCoordinates);
                    Pair pair = new Pair(point, distanceBetween, refineryEntrance.getRefineryId());
                    targets.add(pair);
                }
            }
        }

        Collections.sort(targets, new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                return Double.compare(o1.getDistance(), o2.getDistance());
            }
        });
        if (targets.isEmpty()) {
            // no refinery is found
            return;
        }
        targetRefinery = targets.get(0).getPoint();
        targetRefineryId = targets.get(0).getRefineryId();
    }

    private static class Pair {
        private Point point;
        private double distance;
        private long refineryId;

        private Pair(Point point, double distance, long refineryId) {
            this.point = point;
            this.distance = distance;
            this.refineryId = refineryId;
        }

        public Point getPoint() {
            return point;
        }

        public void setPoint(Point point) {
            this.point = point;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public long getRefineryId() {
            return refineryId;
        }

        public void setRefineryId(long refineryId) {
            this.refineryId = refineryId;
        }
    }
}

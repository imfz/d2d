package lv.k2611a.domain.unitgoals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.Player;
import lv.k2611a.domain.RefineryEntrance;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.domain.ViewDirection;
import lv.k2611a.network.UnitDTO;
import lv.k2611a.service.game.GameServiceImpl;
import lv.k2611a.util.AStar;
import lv.k2611a.util.Point;

public class ReturnToBase implements UnitGoal {

    public static int TICKS_COLLECTING_UNLOADED_PER_TICK = 4;
    public static final int MONEY_PER_TICK = 40;

    private Point targetRefinery;
    private int targetRefineryId;
    private int ticksToWait;

    public ReturnToBase() {
    }

    public ReturnToBase(Point targetRefinery, int targetRefineryId) {
        this.targetRefinery = targetRefinery;
        this.targetRefineryId = targetRefineryId;
    }

    public ReturnToBase(RefineryEntrance refineryEntrance) {
        this.targetRefinery = refineryEntrance.getPoint();
        this.targetRefineryId = refineryEntrance.getRefineryId();
    }

    @Override
    public void reserveTiles(Unit unit, Map map) {
        map.setUsed(unit.getX(), unit.getY(), unit.getId());
    }

    @Override
    public void process(Unit unit, Map map, GameServiceImpl gameService) {
        if (ticksToWait > 0) {
            ticksToWait--;
            return;
        }
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
            ticksToWait = new Random().nextInt(10);
            return;
        }
        Building building = map.getBuilding(targetRefineryId);
        if (building == null) {
            // refinery disappeared
            targetRefinery = null;
            targetRefineryId = 0;
            ticksToWait = new Random().nextInt(10);
            return;
        }

        if (building.getOwnerId() != unit.getOwnerId()) {
            // refinery is no longer ours
            targetRefinery = null;
            targetRefineryId = 0;
            ticksToWait = new Random().nextInt(10);
            return;
        }

        if (unit.getPoint().equals(targetRefinery)) {
            unloadSpice(unit, map, gameService);
        } else {
            moveToRefinery(map, unit, gameService);
        }

    }

    private void moveToRefinery(Map map, Unit unit, GameServiceImpl gameService) {
        if (map.getTile(targetRefinery).isUsedByUnit()) {
            // refinery already occupied, go search for another
            targetRefinery = null;
            targetRefineryId = 0;
        } else {
            unit.insertGoalBeforeCurrent(new Move(targetRefinery));
            unit.getCurrentGoal().process(unit, map, gameService);
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
            unit.setGoal(new Harvest());
        }
    }


    private void searchBase(Unit unit, Map map) {
        Point unitCoordinates = unit.getPoint();
        List<Pair> targets = new ArrayList<Pair>();

        for (java.util.Map.Entry<Point, RefineryEntrance> pointRefineryEntranceEntry : map.getRefineryEntranceList().entrySet()) {
            RefineryEntrance refineryEntrance = pointRefineryEntranceEntry.getValue();
            if (refineryEntrance.getOwnerId() == unit.getOwnerId()) {
                if (refineryEntrance.getPoint().equals(unit.getPoint())) {
                    targetRefinery = refineryEntrance.getPoint();
                    targetRefineryId = refineryEntrance.getRefineryId();
                    return;
                }
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
        for (Pair target : targets) {
            Point targetPoint = target.getPoint();
            if (targetPoint.equals(unitCoordinates)) {
                targetRefinery = targetPoint;
                targetRefineryId = target.getRefineryId();
                return;
            }
            boolean pathExists = AStar.pathExists(unit, map, targetPoint);
            if (pathExists) {
                targetRefinery = targetPoint;
                targetRefineryId = target.getRefineryId();
                return;
            }
        }
        targetRefinery = null;
        targetRefineryId = 0;
        ticksToWait = 10 + new Random().nextInt(40);
    }



    private static class Pair {
        private Point point;
        private double distance;
        private int refineryId;

        private Pair(Point point, double distance, int refineryId) {
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

        public int getRefineryId() {
            return refineryId;
        }

        public void setRefineryId(int refineryId) {
            this.refineryId = refineryId;
        }
    }

    @Override
    public String toString() {
        return "ReturnToBase{" +
                "targetRefinery=" + targetRefinery +
                ", targetRefineryId=" + targetRefineryId +
                ", ticksToWait=" + ticksToWait +
                '}';
    }
    @Override
    public void saveAdditionalInfoIntoDTO(Unit unit, UnitDTO dto) {

    }
}

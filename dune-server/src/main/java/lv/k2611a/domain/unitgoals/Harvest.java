package lv.k2611a.domain.unitgoals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.Tile;
import lv.k2611a.domain.TileType;
import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.service.GameService;
import lv.k2611a.service.GameServiceImpl;
import lv.k2611a.util.Point;

public class Harvest implements UnitGoal {

    public static final int TICKS_FOR_FULL = 200;

    private Point targetSpice;

    public Harvest() {
    }

    public Harvest(Point targetSpice) {
        this.targetSpice = targetSpice;
    }

    @Override
    public void process(Unit unit, Map map, GameServiceImpl gameService) {
        if (unit.getUnitType() != UnitType.HARVESTER) {
            unit.removeGoal(this);
            return;
        }
        if (unit.getTicksCollectingSpice() < TICKS_FOR_FULL) {
            lookForSpice(unit, map, gameService);
        } else {
            unit.setTicksCollectingSpice(TICKS_FOR_FULL);
            unit.removeGoal(this);
            unit.setGoal(new ReturnToBase());
        }

    }


    private void lookForSpice(Unit unit, Map map, GameService gameService) {
        if (targetSpice == null) {
            searchTargetSpice(unit, map);
        }
        if (targetSpice == null) {
            // still null, seems no spice left on the map
            unit.removeGoal(this);
            return;
        }
        // spice already harvested
        if (map.getTile(targetSpice).getTileType() != TileType.SPICE) {
            targetSpice = null;
            return;
        }
        if (targetSpice.equals(unit.getPoint())) {
            harvestSpice(unit, map, gameService);
        } else {
            moveToSpice(unit);
        }
    }

    private void moveToSpice(Unit unit) {
        unit.insertGoalBeforeCurrent(new Move(targetSpice));
    }

    private void harvestSpice(Unit unit, Map map, GameService gameService) {
        // increment unit spice
        unit.setTicksCollectingSpice(unit.getTicksCollectingSpice() + 1);
        // decrement tile spice
        Tile tile = map.getTile(unit.getPoint());
        tile.setSpiceRemainingTicks(tile.getSpiceRemainingTicks() - 1);
        if (tile.getSpiceRemainingTicks() <= 0) {
            tile.setTileType(TileType.SAND);
            targetSpice = null;
            gameService.registerChangedTile(tile.getPoint());
        }
    }

    private void searchTargetSpice(Unit unit, Map map) {
        Point unitCoordinates = unit.getPoint();
        Tile currentTile = map.getTile(unit.getX(), unit.getY());
        if (currentTile.getTileType() == TileType.SPICE) {
            targetSpice = unitCoordinates;
            return;
        }
        List<Pair> targets = new ArrayList<Pair>();
        for (Tile tile : map.getTilesByType(TileType.SPICE)) {
            double distanceBetween = Map.getDistanceBetween(tile.getPoint(), unitCoordinates);
            Pair pair = new Pair(tile.getPoint(), distanceBetween);
            targets.add(pair);
        }
        Collections.sort(targets, new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                return Double.compare(o1.getDistance(),o2.getDistance());
            }
        });
        if (targets.isEmpty()) {
            // no spice is found
            return;
        }
        targetSpice = targets.get(0).getPoint();
    }

    private static class Pair {
        private Point point;
        private double distance;

        private Pair(Point point, double distance) {
            this.point = point;
            this.distance = distance;
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
    }
}

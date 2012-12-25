package lv.k2611a.domain;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lv.k2611a.util.Node;
import lv.k2611a.util.Point;

public class Map {

    private static final int MAX_PLAYER_COUNT = 16;

    private Tile[][] tiles;
    private Player[] players;
    private int width;
    private int height;
    private List<Unit> units;
    private List<Building> buildings;

    private HashMap<Point, RefineryEntrance> refineryEntranceList = new HashMap<Point, RefineryEntrance>();
    private Set<Long> harvesters = new HashSet<Long>();

    public Map(int width, int height) {
        this(width, height, TileType.SAND);
    }


    public Map(int width, int height, TileType tileType) {
        this.width = width;
        this.height = height;
        tiles = new Tile[height][];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (tiles[y] == null) {
                    tiles[y] = new Tile[width];
                }
                tiles[y][x] = new Tile(x, y);
                tiles[y][x].setTileType(tileType);
            }
        }
        units = new ArrayList<Unit>();
        buildings = new ArrayList<Building>();
        players = new Player[MAX_PLAYER_COUNT];
        for (int i = 0; i < MAX_PLAYER_COUNT; i++) {
            players[i] = new Player();
        }
    }

    public Tile getTile(Point point) {
        return getTile(point.getX(), point.getY());
    }

    public Tile getTile(int x, int y) {
        if (x < 0) {
            return null;
        }
        if (y < 0) {
            return null;
        }
        if (x >= width) {
            return null;
        }
        if (y >= height) {
            return null;
        }
        return tiles[y][x];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public List<Unit> getUnitsByType(UnitType unitType) {
        List<Unit> result = new ArrayList<Unit>();
        for (Unit unit : units) {
            if (unit.getUnitType() == UnitType.HARVESTER) {
                result.add(unit);
            }
        }
        return result;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public List<Tile> getTilesByType(TileType tileType) {
        List<Tile> result = new ArrayList<Tile>();
        for (Tile[] tile : tiles) {
            for (Tile tile1 : tile) {
                if (tile1.getTileType() == tileType) {
                    result.add(tile1);
                }
            }
        }
        return result;
    }

    public List<Building> getBuildingsByType(BuildingType buildingType) {
        List<Building> result = new ArrayList<Building>();
        for (Building building : buildings) {
            if (building.getType() == buildingType) {
                result.add(building);
            }
        }
        return result;
    }

    public List<Building> getBuildingsByTypeAndOwner(BuildingType buildingType, int ownerId) {
        List<Building> result = new ArrayList<Building>();
        for (Building building : buildings) {
            if (building.getType() == buildingType) {
                if (building.getOwnerId() == ownerId) {
                    result.add(building);
                }
            }
        }
        return result;
    }

    public List<Tile> getTileNeighbours(int x, int y) {
        List<Tile> tileList = new ArrayList<Tile>();
        if (x > 0) {
            tileList.add(getTile(x - 1, y));
        }
        if (y > 0) {
            tileList.add(getTile(x, y - 1));
        }
        if ((x > 0) && (y > 0)) {
            tileList.add(getTile(x - 1, y - 1));
        }
        if (x < width - 1) {
            tileList.add(getTile(x + 1, y));
        }
        if (y < height - 1) {
            tileList.add(getTile(x, y + 1));
        }
        if ((x < width - 1) && (y < height - 1)) {
            tileList.add(getTile(x + 1, y + 1));
        }

        if ((x > 0) && (y < height - 1)) {
            tileList.add(getTile(x - 1, y + 1));
        }
        if ((x < width - 1) && (y > 0)) {
            tileList.add(getTile(x + 1, y - 1));
        }
        return tileList;
    }

    public List<Tile> getTileDirectNeighbours(int x, int y) {
        List<Tile> tileList = new ArrayList<Tile>();
        if (x > 0) {
            tileList.add(getTile(x - 1, y));
        }
        if (y > 0) {
            tileList.add(getTile(x, y - 1));
        }
        if (x < width - 1) {
            tileList.add(getTile(x + 1, y));
        }
        if (y < height - 1) {
            tileList.add(getTile(x, y + 1));
        }
        return tileList;
    }

    public List<Tile> getTileVerticalNeighbours(int x, int y) {
        List<Tile> tileList = new ArrayList<Tile>();
        if (y > 0) {
            tileList.add(getTile(x, y - 1));
        }
        if (y < height - 1) {
            tileList.add(getTile(x, y + 1));
        }
        return tileList;
    }

    public List<Tile> getTileHorizontalNeighbours(int x, int y) {
        List<Tile> tileList = new ArrayList<Tile>();
        if (x > 0) {
            tileList.add(getTile(x - 1, y));
        }
        if (x < width - 1) {
            tileList.add(getTile(x + 1, y));
        }
        return tileList;
    }

    public boolean hasTileInSquare(int x, int y, int radius, EnumSet<TileType> tileTypes) {
        for (int currentX = x - radius; currentX < x + radius; currentX++) {
            for (int currentY = y - radius; currentY < y + radius; currentY++) {
                if (currentX > 0) {
                    if (currentY > 0) {
                        if (currentX < width) {
                            if (currentY < height) {
                                if (tileTypes.contains(getTile(currentX, currentY).getTileType())) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean hasBuildingInSquare(int x, int y, int radius) {
        for (int currentX = x - radius; currentX < x + radius; currentX++) {
            for (int currentY = y - radius; currentY < y + radius; currentY++) {
                if (currentX > 0) {
                    if (currentY > 0) {
                        if (currentX < width) {
                            if (currentY < height) {
                                for (Building building : buildings) {
                                    if (building.getX() == currentX) {
                                        if (building.getY() == currentY) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static double getDistanceBetween(Point node1, Point node2) {
        //if the nodes are on top or next to each other, return 1
        if (node1.getX() == node2.getX()) {
            return Math.abs(node1.getY() - node2.getY());
        }
        if (node1.getY() == node2.getY()) {
            return Math.abs(node1.getX() - node2.getX());
        }
        int deltaX = Math.abs(node1.getX() - node2.getX());
        int deltaY = Math.abs(node1.getY() - node2.getY());
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public boolean isObstacle(Node neighbor, long unitId, int ownerid, boolean isHarvester) {
        return isObstacle(neighbor.getX(), neighbor.getY(), unitId, ownerid, isHarvester);
    }

    private boolean harvesterCheck(Point point, long unitId, int ownerid, boolean isHarvester) {
        if (isHarvester) {
            if (getTile(point).isUsedByUnit()) {
                return false;
            }
            if (this.harvesters.contains(unitId)) {
                RefineryEntrance refineryEntrance = this.refineryEntranceList.get(point);
                if (refineryEntrance != null) {
                    if (refineryEntrance.getOwnerId() == ownerid) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isObstacle(int x, int y) {
        Tile tile = getTile(x, y);
        if (tile == null) {
            return true;
        }
        return !tile.isPassable();
    }

    public boolean isObstacle(int x, int y, long unitId, int ownerid, boolean isHarvester) {
        if (harvesterCheck(new Point(x, y), unitId, ownerid, isHarvester)) {
            return false;
        }
        return !getTile(x, y).isPassable(unitId);
    }

    public void setUsed(int x, int y, long unitId) {
        getTile(x, y).setUsed(unitId);
    }

    public void clearUsageFlag() {
        for (Tile[] tile : tiles) {
            for (Tile tile1 : tile) {
                tile1.setUsed(-1);
                tile1.setPassableSegmentNumber(-1);
            }
        }
    }

    public Unit getUnit(int id) {
        for (Unit unit : units) {
            if (unit.getId() == id) {
                return unit;
            }
        }
        return null;
    }

    public Building getBuilding(long id) {
        for (Building building : buildings) {
            if (building.getId() == id) {
                return building;
            }
        }
        return null;
    }

    public Player getPlayerById(int id) {
        return this.players[id];
    }

    public Tile getNearestFreeTile(int x, int y) {
        if (!isObstacle(x, y)) {
            getTile(x, y);
        }
        for (int radius = 0; radius < 10; radius++) {
            for (int currentX = x - radius; currentX < x + radius; currentX++) {
                for (int currentY = y - radius; currentY < y + radius; currentY++) {
                    if (!isObstacle(currentX, currentY)) {
                        return getTile(currentX, currentY);
                    }
                }
            }
        }
        return null;

    }

    public HashMap<Point, RefineryEntrance> getRefineryEntranceList() {
        return refineryEntranceList;
    }

    public Set<Long> getHarvesters() {
        return harvesters;
    }

    // precache simultaneously impassable segments, to avoid AStars worse-case scenario calculations. Like moving 100 units to impassable area.
    public void buildPassableSegmentCache() {
        int segmentNumber = 0;
        boolean found;
        do {
            found = false;
            for (Tile[] tile : tiles) {
                for (Tile tile1 : tile) {
                    if (tile1.isPassable()) {
                        if (tile1.getPassableSegmentNumber() == -1) {
                            segmentNumber++;
                            spreadSegment(segmentNumber, tile1);
                            found = true;
                        }
                    }
                }
            }
        } while (found);
    }

    private void spreadSegment(int segmentNumber, Tile tile) {
        tile.setPassableSegmentNumber(segmentNumber);
        for (Tile neighbour : getTileNeighbours(tile.getX(), tile.getY())) {
            if (neighbour.isPassable()) {
                if (neighbour.getPassableSegmentNumber() == -1) {
                    spreadSegment(segmentNumber, neighbour);
                }
            }
        }
    }
}

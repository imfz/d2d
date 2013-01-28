package lv.k2611a.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.k2611a.util.Node;
import lv.k2611a.util.Point;

public class Map {

    private static final Logger log = LoggerFactory.getLogger(Map.class);

    private Tile[][] tiles;
    private Player[] players;
    private int width;
    private int height;
    private List<Unit> units;
    private List<Building> buildings;
    private List<Bullet> bullets;
    private List<Explosion> explosions;
    private int lastBulletId = 0;

    private HashMap<Point, RefineryEntrance> refineryEntranceList = new HashMap<Point, RefineryEntrance>();

    public Map(int width, int height) {
        this(width, height, TileType.SAND);
        this.clearUsageFlag();
    }


    public Map(int width, int height, TileType tileType) {
        this(width,height,tileType,8);
    }

    public Map(int width, int height, TileType tileType, int playerCount) {
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
        bullets = new ArrayList<Bullet>();
        explosions = new ArrayList<Explosion>();
        players = new Player[playerCount];
        for (int i = 0; i < playerCount; i++) {
            Player player = new Player();
            player.setId(i);
            players[i] = player;
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
        List<Unit> result = new ArrayList<Unit>();
        for (Unit unit : units) {
            if (unit != null) {
                result.add(unit);
            }
        }
        return result;
    }

    public List<Unit> getUnitsByIds(Set<Integer> ids) {
        List<Unit> unitList = new ArrayList<Unit>();
        for (Integer id : ids) {
            if (id != null) {
                Unit unit = getUnit(id);
                if (unit != null) {
                    unitList.add(unit);
                }
            }
        }
        return unitList;
    }

    public int addUnit(Unit unit) {
        units.add(unit);
        unit.setId(units.size() - 1);
        setUsedByUnit(unit.getX(), unit.getY(), unit.getId());
        return unit.getId();
    }

    public int addBuilding(Building building) {
        buildings.add(building);
        building.setId(buildings.size() - 1);
        return building.getId();
    }

    public List<Unit> getUnitsByType(UnitType unitType) {
        List<Unit> result = new ArrayList<Unit>();
        for (Unit unit : units) {
            if (unit != null) {
                if (unit.getUnitType() == unitType) {
                    result.add(unit);
                }
            }
        }
        return result;
    }

    public List<Building> getBuildings() {
        return filterNonNulls(buildings);
    }

    private List<Building> filterNonNulls(List<Building> buildings) {
        List<Building> result = new ArrayList<Building>();
        for (Building building : buildings) {
            if (building != null) {
                result.add(building);
            }
        }
        return result;
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
        for (Building building : filterNonNulls(buildings)) {
            if (building.getType() == buildingType) {
                result.add(building);
            }
        }
        return result;
    }

    public List<Building> getBuildingsByTypeAndOwner(BuildingType buildingType, int ownerId) {
        List<Building> result = new ArrayList<Building>();
        for (Building building : filterNonNulls(buildings)) {
            if (building.getType() == buildingType) {
                if (DiplomacyUtil.isAlly(building.getOwnerId(), ownerId)) {
                    result.add(building);
                }
            }
        }
        return result;
    }


    public List<Building> getBuildingsByOwner(int ownerId) {
        List<Building> result = new ArrayList<Building>();
        for (Building building : filterNonNulls(buildings)) {
            if (DiplomacyUtil.isAlly(building.getOwnerId(), ownerId)) {
                result.add(building);
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


    public List<Node> getTileNeighbourNodes(int x, int y) {
        List<Node> nodeList = new ArrayList<Node>();
        if (x > 0) {
            nodeList.add(new Node(x - 1, y));
        }
        if (y > 0) {
            nodeList.add(new Node(x, y - 1));
        }
        if (x < width - 1) {
            nodeList.add(new Node(x + 1, y));
        }
        if (y < height - 1) {
            nodeList.add(new Node(x, y + 1));
        }

        if ((x > 0) && (y > 0)) {
            nodeList.add(new Node(x - 1, y - 1));
        }
        if ((x < width - 1) && (y < height - 1)) {
            nodeList.add(new Node(x + 1, y + 1));
        }

        if ((x > 0) && (y < height - 1)) {
            nodeList.add(new Node(x - 1, y + 1));
        }
        if ((x < width - 1) && (y > 0)) {
            nodeList.add(new Node(x + 1, y - 1));
        }
        return nodeList;
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

    public Unit getUnitAt(int x, int y) {
        Tile tile = getTile(x, y);
        if (tile != null) {
            if (tile.isUsedByUnit()) {
                return getUnit(tile.getUsedByUnit());
            }
        }
        return null;
    }

    public Building getBuildingAt(int x, int y) {
        Tile tile = getTile(x, y);
        if (tile != null) {
            if (tile.isUsedByBuilding()) {
                return getBuilding(tile.getUsedByBuilding());
            }
        }
        return null;
    }

    public boolean hasBuildingInSquare(int x, int y, int radius) {
        for (int currentX = x - radius; currentX < x + radius; currentX++) {
            for (int currentY = y - radius; currentY < y + radius; currentY++) {
                if (currentX > 0) {
                    if (currentY > 0) {
                        if (currentX < width) {
                            if (currentY < height) {
                                for (Building building : filterNonNulls(buildings)) {
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

    public static Point getClosestNode(Point current, List<Point> points) {
        Point bestNode = null;
        double bestDistance = Double.MAX_VALUE;
        for (Point point : points) {
            double currentDistance = Map.getDistanceBetween(current, point);
            if (bestDistance > currentDistance) {
                bestNode = point;
                bestDistance = currentDistance;
            }
        }
        return bestNode;
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

    public static double getDistanceBetween(Node node1, Node node2) {
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

    // Check if the tile contains no building. Units are not considered an obstacle in this case, as they might move.
    public boolean isPassable(Node neighbor) {
        return getTile(neighbor.getX(), neighbor.getY()).isPassable();
    }

    // Check if the tile contains no buildings or units. Also used for Harvester move.
    public boolean isUnoccupied(Node neighbor, Unit unit) {
        if (unit.getUnitType() == UnitType.HARVESTER && refineryTileCheck(neighbor.getPoint(), unit)) {
            return true;
        }
        return getTile(neighbor.getX(), neighbor.getY()).isUnoccupied(unit.getId());
    }

    private boolean refineryTileCheck(Point tile, Unit harvester) {
        if (getTile(tile).isUsedByUnit()) {
            return false;
        }
        int spiceAmount = harvester.getTicksCollectingSpice();
        RefineryEntrance refineryEntrance = this.refineryEntranceList.get(tile);
        // Refinery entrance should be passable for non-empty harvesters
        if (refineryEntrance != null && spiceAmount > 0) {
             if (refineryEntrance.getOwnerId() == harvester.getOwnerId()) {
                return true;
            }
        }
        // This is a reverse-lookup for 3 upper tiles of refinery when we are leaving refinery.
        if (spiceAmount <= 0) {
            for (int entranceX = tile.getX() - 1; entranceX < tile.getX()+2; entranceX++) {
                Point entracePoint = new Point(entranceX, tile.getY() + 1);
                refineryEntrance = this.refineryEntranceList.get(entracePoint);
                if (refineryEntrance != null) {
                    if (refineryEntrance.getOwnerId() == harvester.getOwnerId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    // Check if the tile contains no buildings or units. Used only for Harvester pathfinding in AStar.
    public boolean isUnoccupiedAStar(RefineryEntrance startIsRefineryEntrance, Node neighbor, Unit unit) {
        if (unit.getUnitType() == UnitType.HARVESTER && refineryEntranceCheck(neighbor.getPoint(), startIsRefineryEntrance, unit)) {
            return true;
        }
        return getTile(neighbor.getX(), neighbor.getY()).isUnoccupied(unit.getId());
    }

    private boolean refineryEntranceCheck(Point tile, RefineryEntrance startIsRefineryEntrance, Unit harvester) {
        if (getTile(tile).isUsedByUnit()) {
            return false;
        }
        int spiceAmount = harvester.getTicksCollectingSpice();
        RefineryEntrance refineryEntrance = this.refineryEntranceList.get(tile);
        if (refineryEntrance != null && spiceAmount > 0) {
            if (refineryEntrance.getOwnerId() == harvester.getOwnerId()) {
                return true;
            }
        }
        // This is a lookup for 3 upper tiles of refinery when we are planning to leave the refinery.
        if (startIsRefineryEntrance != null && spiceAmount <= 0) {
            if (startIsRefineryEntrance.getOwnerId() == harvester.getOwnerId()) {
                if (tile.getY() == startIsRefineryEntrance.getPoint().getY() - 1) {
                    for (int pointX = tile.getX() - 1; pointX < (tile.getX() + 2); pointX++) {
                        if (pointX == startIsRefineryEntrance.getPoint().getX()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isUnoccupied(int x, int y) {
        Tile tile = getTile(x, y);
        if (tile != null) {
            return tile.isUnoccupied();
        }
        return false;
    }

    public boolean isPassable(int x, int y) {
        Tile tile = getTile(x, y);
        if (tile != null) {
            return tile.isPassable();
        }
        return false;
    }


    public void setUsedByUnit(int x, int y, int id) {
        Tile tile = getTile(x, y);
        if (tile != null) {
            tile.setUsedByUnit(id);
        }
    }

    public void setUsedByBuilding(int x, int y, int id) {
        Tile tile = getTile(x, y);
        if (tile != null) {
            tile.setUsedByBuilding(id);
        }
    }

    public void clearUsageFlag() {
        for (Tile[] tile : tiles) {
            for (Tile tile1 : tile) {
                tile1.setUsedClear();
            }
        }
    }

    public Unit getUnit(int id) {
        if (id >= units.size()) {
            return null;
        }
        return units.get(id);
    }

    public Building getBuilding(int id) {
        if (id >= buildings.size()) {
            return null;
        }
        return buildings.get(id);
    }

    public Player getPlayerById(int id) {
        return this.players[id];
    }

    public Player[] getPlayers() {
        return players;
    }

    public Tile getNearestFreeTile(int x, int y) {
        if (isUnoccupied(x, y)) {
            getTile(x, y);
        }
        for (int radius = 0; radius < 10; radius++) {
            for (int currentX = x - radius; currentX < x + radius; currentX++) {
                for (int currentY = y - radius; currentY < y + radius; currentY++) {
                    if (isUnoccupied(currentX, currentY)) {
                        return getTile(currentX, currentY);
                    }
                }
            }
        }
        return null;
    }

    public Tile getNearestFreeTileForUnitPlacement(int minX, int maxX, int minY, int maxY) {
        for (int currentX = minX; currentX <= maxX; currentX++) {
            for (int currentY = maxY; currentY >= minY; currentY--) {
                if (isUnoccupied(currentX, currentY)) {
                    return getTile(currentX, currentY);
                }
            }
        }
        return null;
    }

    public List<Target> getTargetsInRange(Point startPoint, int radius, Map map) {
        List<Target> targetList = new ArrayList<Target>();
        Tile targetTile;

        for (int targetX = startPoint.getX() - radius; targetX <= startPoint.getX() + radius; targetX++) {
            for (int targetY = startPoint.getY() - radius; targetY <= startPoint.getY() + radius; targetY++) {
                if (getDistanceBetween(startPoint, new Point(targetX,targetY)) <= radius) {
                    targetTile = getTile(targetX, targetY);
                    if (targetTile != null) {
                        if (targetTile.isUsedByUnit()) {
                            Unit targetUnit = map.getUnitAt(targetX,targetY);
                            // Check if the unit is standing in that tile(to avoid ghosts for units that are moving)
                            if (targetTile.getPoint().getX() == targetUnit.getPoint().getX()
                             && targetTile.getPoint().getY() == targetUnit.getPoint().getY()) {
                                targetList.add(new Target(Entity.UNIT, targetUnit.getId(), targetUnit.getPoint()));
                            }
                        } else if (targetTile.isUsedByBuilding()) {
                            targetList.add(new Target(Entity.BUILDING, targetTile.getUsedByBuilding(), targetTile.getPoint()));
                        }
                    }
                }
            }
        }
        return targetList;
    }

    public Point getClosestPoint(Building building, Unit unit) {
        List<Point> points = new ArrayList<Point>();
        points.add(building.getPoint());
        points.add(building.getPoint2());
        points.add(building.getPoint3());
        points.add(building.getPoint4());

        return Map.getClosestNode(unit.getPoint(), points);
    }

    public boolean targetInAttackRange(Unit unit, Point targetPoint) {
        return getDistanceBetween(unit.getPoint(), targetPoint) <= unit.getUnitType().getAttackRange();
    }

    public boolean enemiesPresentInAttackRange(Unit unit, Map map) {
        int radius = unit.getUnitType().getAttackRange();
        Tile targetTile;
        for (int targetX = unit.getX() - radius; targetX <= unit.getX() + radius; targetX++) {
            for (int targetY = unit.getY() - radius; targetY <= unit.getY() + radius; targetY++) {
                if (getDistanceBetween(unit.getPoint(), new Point(targetX,targetY)) <= radius) {
                    targetTile = getTile(targetX, targetY);
                    if (targetTile != null) {
                        if (targetTile.isUsedByUnit()) {
                            Unit targetUnit = map.getUnitAt(targetX,targetY);
                            if (targetTile.getPoint().getX() == targetUnit.getPoint().getX()
                                    && targetTile.getPoint().getY() == targetUnit.getPoint().getY()) {
                                if (!DiplomacyUtil.isAlly(unit.getOwnerId(), targetUnit.getOwnerId())) {
                                    return true;
                                }
                            }
                        } else if (targetTile.isUsedByBuilding()) {
                            Building targetBuilding = map.getBuildingAt(targetX,targetY);
                            if (!DiplomacyUtil.isAlly(unit.getOwnerId(), targetBuilding.getOwnerId())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public Point getRandomFreeTile(Point near, int distanceFrom, int distanceTo) {
        Random r = new Random();
        int iterationCount = 0;
        while (iterationCount < 100) {
            int x = r.nextInt(width);
            int y = r.nextInt(height);
            Point candidate = new Point(x, y);
            double distanceBetween = Map.getDistanceBetween(near, candidate);
            if ((distanceBetween > distanceFrom) && (distanceBetween < distanceTo)) {
                return candidate;
            }
            iterationCount++;
        }
        log.warn("Cannot find free tile spot in " + iterationCount + " iterations");
        return null;
    }

    public HashMap<Point, RefineryEntrance> getRefineryEntranceList() {
        return refineryEntranceList;
    }

    public void removeUnit(Unit unit) {
        this.units.set(unit.getId(), null);
    }

    public void addExplosion(Explosion explosion) {
        this.explosions.add(explosion);
    }

    public void removeBuilding(Building building) {
        this.buildings.set(building.getId(), null);
    }

    public void removeBullet(Bullet bullet) {
        this.bullets.remove(bullet);
    }

    public void removeBullets(List<Bullet> bullets) {
        this.bullets.removeAll(bullets);
    }

    public int addBullet(Bullet bullet) {
        lastBulletId++;
        bullet.setId(lastBulletId);
        this.bullets.add(bullet);
        return lastBulletId;
    }

    public List<Bullet> getBullets() {
        return Collections.unmodifiableList(bullets);
    }

    public List<Explosion> getExplosions() {
        return explosions;
    }
}

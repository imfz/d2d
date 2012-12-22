package lv.k2611a.domain;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;

import lv.k2611a.util.Node;

public class Map {
    private Tile[][] tiles;
    private int width;
    private int height;
    private List<Unit> units;
    private List<Building> buildings;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Tile[height][];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (tiles[y] == null) {
                    tiles[y] = new Tile[width];
                }
                tiles[y][x] = new Tile(x, y);
                tiles[y][x].setTileType(TileType.SAND);
            }
        }
        units = new ArrayList<Unit>();
        buildings = new ArrayList<Building>();
    }

    public Map(Map map) {
        this.width = map.width;
        this.height = map.height;
        this.tiles = new Tile[map.tiles.length][];
        for (int i = 0; i < map.tiles.length; i++) {
            this.tiles[i] = new Tile[map.tiles[i].length];
            for (int j = 0; j < map.tiles[i].length; j++) {
                this.tiles[i][j] = map.tiles[i][j].copy();
            }
        }
        this.units = new ArrayList<Unit>();
        for (Unit unit : map.units) {
            this.units.add(unit.copy());
        }
        this.buildings = new ArrayList<Building>();
        for (Building building : map.buildings) {
            this.buildings.add(building.copy());
        }
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

    public List<Building> getBuildings() {
        return buildings;
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

    public boolean isObstacle(Node neighbor, long unitId) {
        Tile tile = getTile(neighbor.getX(), neighbor.getY());
        if (tile == null) {
            return true;
        }
        return !tile.isPassable(unitId);
    }

    public boolean isObstacle(int x, int y, long unitId) {
        return !getTile(x,y).isPassable(unitId);
    }

    public void setUsed(int x, int y, long unitId) {
        if (isObstacle(x,y,unitId)) {
            return;
        }
        getTile(x,y).setUsed(unitId);
    }

    public void clearUsageFlag() {
        for (Tile[] tile : tiles) {
            for (Tile tile1 : tile) {
                tile1.setUsed(-1);
            }
        }
    }
}

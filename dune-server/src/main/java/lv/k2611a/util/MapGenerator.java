package lv.k2611a.util;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.k2611a.domain.Building;
import lv.k2611a.domain.BuildingType;
import lv.k2611a.domain.Map;
import lv.k2611a.domain.Tile;
import lv.k2611a.domain.TileType;
import lv.k2611a.service.IdGeneratorService;

public class MapGenerator {

    private static final Logger log = LoggerFactory.getLogger(MapGenerator.class);
    private static final int BASE_RADIUS = 6;
    private static final int SPICE_RADIUS = 5;
    private static final int SPICE_COUNT = 7;

    private MapGenerator() {

    }

    public static Map generateMap(int width, int height, int baseCount, IdGeneratorService idGeneratorService) {
        log.info("Generating map");
        Map map = new Map(width, height);
        Random random = new Random();
        log.info("Placing bases");
        for (int i = 0; i < baseCount; i++) {
            int x;
            int y;
            int iterationCount = 0;
            do {
                x = random.nextInt(map.getWidth() - BASE_RADIUS * 2) + BASE_RADIUS;
                y = random.nextInt(map.getHeight() - BASE_RADIUS * 2) + BASE_RADIUS;
                iterationCount++;
            } while (
                    map.hasTileInSquare(x, y, BASE_RADIUS + 1, EnumSet.noneOf(TileType.class))
                    || map.hasBuildingInSquare(x, y, BASE_RADIUS + 1)
                    && iterationCount < 1000);
            if (iterationCount > 100) {
                log.warn("Could not find a spot for base in 1000 iterations");
            }
            placeTiles(map, x, y, TileType.ROCK, BASE_RADIUS, random);
            placeConYard(map, x,y,i, idGeneratorService.generateBuildingId());
        }


        log.info("Bases placed");



        // fix for missing graphics. Sandy tile cannot be surrounded by rocks, etc.
        fixSandyTiles(map);

        log.info("Sandy tiles fixed");

        log.info("Placing spice");

        for (int i = 0; i < SPICE_COUNT; i++) {
            int x;
            int y;
            int iterationCount = 0;
            do {
                x = random.nextInt(map.getWidth() - SPICE_RADIUS * 2) + SPICE_RADIUS;
                y = random.nextInt(map.getHeight() - SPICE_RADIUS * 2) + SPICE_RADIUS;
                iterationCount++;
            } while (map.hasTileInSquare(x, y, SPICE_RADIUS + 1, EnumSet.of(TileType.ROCK)) && iterationCount < 1000);
            if (iterationCount > 100) {
                log.warn("Could not find a spot for spice in 1000 iterations");
            }
            placeTiles(map, x, y, TileType.SPICE, SPICE_RADIUS, random);
        }


        fixSandyTiles(map);

        log.info("Spice placed");

        log.info("Map generated");

        return map;
    }

    private static void placeConYard(Map map, int x, int y, int playerId, int id) {
        Building building = new Building();
        building.setOwnerId(playerId);
        building.setType(BuildingType.CONSTRUCTIONYARD);
        building.setX(x);
        building.setY(y);
        building.setId(id);
        map.getBuildings().add(building);
    }

    private static void fixSandyTiles(Map map) {
        boolean found;
        do {
            found = false;
            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getHeight(); y++) {
                    if (map.getTile(x, y).getTileType() == TileType.SAND) {
                        if (
                                (countTileType(map.getTileHorizontalNeighbours(x, y), TileType.ROCK) > 1) ||
                                        (countTileType(map.getTileVerticalNeighbours(x, y), TileType.ROCK) > 1)
                                ) {
                            map.getTile(x, y).setTileType(TileType.ROCK);
                            found = true;
                        } else {
                            if (
                                    (countTileType(map.getTileHorizontalNeighbours(x, y), TileType.SPICE) > 1) ||
                                            (countTileType(map.getTileVerticalNeighbours(x, y), TileType.SPICE) > 1)
                                    ) {
                                map.getTile(x, y).setTileType(TileType.SPICE);
                                found = true;
                            }
                        }
                    }

                }
            }
        } while (found);
    }

    private static int countTileType(List<Tile> tileList, TileType tileType) {
        int count = 0;
        for (Tile tile : tileList) {
            if (tile.getTileType() == tileType) {
                count++;
            }
        }
        return count;
    }

    private static void placeTiles(Map map, int x, int y, TileType tileType, int radius, Random r) {
        double maxDistance = radius;
        for (int currentX = x - radius; currentX < x + radius; currentX++) {
            for (int currentY = y - radius; currentY < y + radius; currentY++) {
                int xDelta = Math.abs(x - currentX);
                int yDelta = Math.abs(y - currentY);
                double distance = Math.sqrt(xDelta * xDelta + yDelta * yDelta);
                double chance = 1;
                if (distance < maxDistance / 5) {
                    chance = 1;
                } else if (distance < maxDistance / 5 * 2) {
                    chance = 0.9;
                } else if (distance < maxDistance / 5 * 3) {
                    chance = 0.7;
                } else if (distance < maxDistance / 5 * 4) {
                    chance = 0.4;
                } else {
                    chance = 0.3;
                }
                if (r.nextDouble() < chance) {
                    map.getTile(currentX, currentY).setTileType(tileType);
                }
            }
        }
    }
}

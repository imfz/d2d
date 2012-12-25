package lv.k2611a.util;

import java.util.EnumSet;

import org.junit.Test;

import lv.k2611a.domain.Map;
import lv.k2611a.domain.Tile;
import lv.k2611a.domain.TileType;
import lv.k2611a.service.IdGeneratorServiceImpl;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class MapGeneratorTest {
    @Test
    public void testGenerateMap() throws Exception {
        Map map = MapGenerator.generateMap(256,256, 8, new IdGeneratorServiceImpl());

        EnumSet<TileType> tileTypesFound = EnumSet.noneOf(TileType.class);

        for (int x = 0; x < 256; x++) {
            for (int y = 0; y < 256; y++) {
                Tile tile = map.getTile(x,y);
                tileTypesFound.add(tile.getTileType());
                assertNotNull(tile);
            }
        }

        assertTrue("Sand found", tileTypesFound.contains(TileType.SAND));
        assertTrue("Rock found", tileTypesFound.contains(TileType.ROCK));
        assertTrue("Spice found", tileTypesFound.contains(TileType.SPICE));


    }
}

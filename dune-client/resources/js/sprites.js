function Sprites() {

}
;

Sprites.prototype.getTileConfig = function (targetTileX, targetTileY, map) {
    if (targetTileX >= map.getWidth()) {
        return null;
    }
    if (targetTileY >= map.getHeight()) {
        return null;
    }

    var tileType = map.getTileType(targetTileX, targetTileY);
    var tileTypeUp = map.getTileType(targetTileX, targetTileY - 1);
    var tileTypeDown = map.getTileType(targetTileX, targetTileY + 1);
    var tileTypeLeft = map.getTileType(targetTileX - 1, targetTileY);
    var tileTypeRight = map.getTileType(targetTileX + 1, targetTileY);
    var tileTypeUpLeft = map.getTileType(targetTileX - 1, targetTileY - 1);
    var tileTypeUpRight = map.getTileType(targetTileX + 1, targetTileY - 1);
    var tileTypeDownLeft = map.getTileType(targetTileX - 1, targetTileY + 1);
    var tileTypeDownRight = map.getTileType(targetTileX + 1, targetTileY + 1);

    var x;
    var y;

    if (tileType == TILE_TYPE_SAND) {
        x = 0;
        y = 0;


        if ((tileTypeUp == TILE_TYPE_ROCK) && (tileTypeLeft == TILE_TYPE_ROCK)) {
            x = 4;
            y = 3;
        }


        else if ((tileTypeDown == TILE_TYPE_ROCK) && (tileTypeRight == TILE_TYPE_ROCK)) {
            x = 3;
            y = 2;
        }
        else if ((tileTypeDown == TILE_TYPE_ROCK) && (tileTypeLeft == TILE_TYPE_ROCK)) {
            x = 4;
            y = 2;
        }
        else if ((tileTypeUp == TILE_TYPE_ROCK) && (tileTypeRight == TILE_TYPE_ROCK)) {
            x = 3;
            y = 3;
        }
        else if ((tileTypeUp == TILE_TYPE_ROCK) && (tileTypeLeft == TILE_TYPE_ROCK)) {
            x = 4;
            y = 3;
        }
        else if (tileTypeUp == TILE_TYPE_ROCK) {
            x = 5;
            y = 3;
        }

        else if (tileTypeDown == TILE_TYPE_ROCK) {
            x = 5;
            y = 2;
        }

        else if (tileTypeLeft == TILE_TYPE_ROCK) {
            x = 4;
            y = 1
        }
        else if (tileTypeRight == TILE_TYPE_ROCK) {
            x = 3;
            y = 1;
        }

        if ((tileTypeUp == TILE_TYPE_SPICE) && (tileTypeLeft == TILE_TYPE_SPICE)) {
            x = 25;
            y = 3;
        }


        else if ((tileTypeDown == TILE_TYPE_SPICE) && (tileTypeRight == TILE_TYPE_SPICE)) {
            x = 24;
            y = 2;
        }
        else if ((tileTypeDown == TILE_TYPE_SPICE) && (tileTypeLeft == TILE_TYPE_SPICE)) {
            x = 25;
            y = 2;
        }
        else if ((tileTypeUp == TILE_TYPE_SPICE) && (tileTypeRight == TILE_TYPE_SPICE)) {
            x = 24;
            y = 3;
        }
        else if ((tileTypeUp == TILE_TYPE_SPICE) && (tileTypeLeft == TILE_TYPE_SPICE)) {
            x = 25;
            y = 3;
        }
        else if (tileTypeUp == TILE_TYPE_SPICE) {
            x = 26;
            y = 3;
        }

        else if (tileTypeDown == TILE_TYPE_SPICE) {
            x = 26;
            y = 2;
        }

        else if (tileTypeLeft == TILE_TYPE_SPICE) {
            x = 25;
            y = 1
        }
        else if (tileTypeRight == TILE_TYPE_SPICE) {
            x = 24;
            y = 1;
        }

        else if ((tileTypeDown == TILE_TYPE_RICH_SPICE) && (tileTypeRight == TILE_TYPE_RICH_SPICE)) {
            x = 24;
            y = 2;
        }
        else if ((tileTypeDown == TILE_TYPE_RICH_SPICE) && (tileTypeLeft == TILE_TYPE_RICH_SPICE)) {
            x = 25;
            y = 2;
        }
        else if ((tileTypeUp == TILE_TYPE_RICH_SPICE) && (tileTypeRight == TILE_TYPE_RICH_SPICE)) {
            x = 24;
            y = 3;
        }
        else if ((tileTypeUp == TILE_TYPE_RICH_SPICE) && (tileTypeLeft == TILE_TYPE_RICH_SPICE)) {
            x = 25;
            y = 3;
        }
        else if (tileTypeUp == TILE_TYPE_RICH_SPICE) {
            x = 26;
            y = 3;
        }

        else if (tileTypeDown == TILE_TYPE_RICH_SPICE) {
            x = 26;
            y = 2;
        }

        else if (tileTypeLeft == TILE_TYPE_RICH_SPICE) {
            x = 25;
            y = 1
        }
        else if (tileTypeRight == TILE_TYPE_RICH_SPICE) {
            x = 24;
            y = 1;
        }
    }

    if (tileType == TILE_TYPE_SPICE) {
        x = 30;
        y = 0;
        if ((tileTypeUp == TILE_TYPE_RICH_SPICE) && (tileTypeLeft == TILE_TYPE_RICH_SPICE)) {
            x = 25;
            y = 3;
        }


        else if ((tileTypeDown == TILE_TYPE_RICH_SPICE) && (tileTypeRight == TILE_TYPE_RICH_SPICE)) {
            x = 24;
            y = 2;
        }
        else if ((tileTypeDown == TILE_TYPE_RICH_SPICE) && (tileTypeLeft == TILE_TYPE_RICH_SPICE)) {
            x = 25;
            y = 2;
        }
        else if ((tileTypeUp == TILE_TYPE_RICH_SPICE) && (tileTypeRight == TILE_TYPE_SPICE)) {
            x = 24;
            y = 3;
        }
        else if ((tileTypeUp == TILE_TYPE_RICH_SPICE) && (tileTypeLeft == TILE_TYPE_RICH_SPICE)) {
            x = 25;
            y = 3;
        }
        else if (tileTypeUp == TILE_TYPE_RICH_SPICE) {
            x = 26;
            y = 3;
        }

        else if (tileTypeDown == TILE_TYPE_RICH_SPICE) {
            x = 26;
            y = 2;
        }

        else if (tileTypeLeft == TILE_TYPE_RICH_SPICE) {
            x = 25;
            y = 1
        }
        else if (tileTypeRight == TILE_TYPE_RICH_SPICE) {
            x = 24;
            y = 1;
        }
    }

    if (tileType == TILE_TYPE_RICH_SPICE) {
        x = 40;
        y = 0;
    }

    if (tileType == TILE_TYPE_ROCK) {
        x = 7;
        y = 2;
    }


    x *= TILE_WIDTH;
    y *= TILE_HEIGHT;
    var result = new Object();
    result.x = x;
    result.y = y;
    return result;
};

Sprites.prototype.getBuildingConfig = function (building) {
    var x = 0;
    var y = 0;
    var width = 2;
    var height = 2;
    if (building.type == BUILDING_TYPE_SILO) {
        y = 0;
    }
    if (building.type == BUILDING_TYPE_POWERPLANT) {
        y = 1;
    }
    if (building.type == BUILDING_TYPE_REPAIRSHOP) {
        y = 2;
        width = 3;
    }
    if (building.type == BUILDING_TYPE_BARRACKS) {
        y = 3;
    }
    if (building.type == BUILDING_TYPE_RADAR) {
        y = 4;
    }
    if (building.type == BUILDING_TYPE_LIGHTFACTORY) {
        y = 5;
    }
    if (building.type == BUILDING_TYPE_CONSTRUCTION_YARD) {
        y = 6;
    }
    if (building.type == BUILDING_TYPE_FACTORY) {
        y = 7;
        width = 3;
    }
    if (building.type == BUILDING_TYPE_AIRBASE) {
        y = 8;
    }

    var result = new Object();
    result.x = x;
    result.y = y * (63 * 2);
    result.width = width * 63;
    result.height = height * 63;
    return result;

};

Sprites.prototype.getUnitConfig = function (unit) {
    var x = 0;
    if (unit.viewDirection == VIEW_DIRECTION_TOP) {
        x = 0;
    }
    if (unit.viewDirection == VIEW_DIRECTION_TOPLEFT) {
        x = 1;
    }
    if (unit.viewDirection == VIEW_DIRECTION_TOPRIGHT) {
        x = 7;
    }
    if (unit.viewDirection == VIEW_DIRECTION_LEFT) {
        x = 2;
    }
    if (unit.viewDirection == VIEW_DIRECTION_RIGHT) {
        x = 6;
    }
    if (unit.viewDirection == VIEW_DIRECTION_BOTTOMLEFT) {
        x = 3;
    }
    if (unit.viewDirection == VIEW_DIRECTION_BOTTOMRIGHT) {
        x = 5;
    }
    if (unit.viewDirection == VIEW_DIRECTION_BOTTOM) {
        x = 4;
    }

    var y = 0;
    if (unit.unitType == UNIT_TYPE_BATTLE_TANK) {
        y = 16;
    }
    if (unit.unitType == UNIT_TYPE_SIEGE_TANK) {
        y = 1;
    }
    if (unit.unitType == UNIT_TYPE_DEVASTATOR) {
        y = 10;
    }
    if (unit.unitType == UNIT_TYPE_LAUNCHER) {
        y = 21;
    }

    result = new Object();
    result.x = x * 63;
    result.y = y * 63;
    return result;
};

Sprites.prototype.getPlayerColor = function (playerId) {
    var result = new Object();
    if (playerId == 1) {
        result.r = 214;
        result.g = 0;
        result.b = 0;
    }
    if (playerId == 2) {
        result.r = 0;
        result.g = 214;
        result.b = 0;
    }
    if (playerId == 3) {
        result.r = 0;
        result.g = 0;
        result.b = 214;
    }
    return result;
};

var sprites = new Sprites();
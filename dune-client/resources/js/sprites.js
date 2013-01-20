function Sprites() {
}

Sprites.prototype.init = function() {
    this.colorPlayerIdSelector();
    this.createBuildingPlacementSprites();
    this.buildUnitSprites();
    this.buildBuildingMarkers();
};

Sprites.prototype.createBuildingPlacementSprites = function() {
    var canvas = document.createElement("canvas");
    var canvas2 = document.createElement("canvas");
    var canvas3 = document.createElement("canvas");

    this.fillWithLines(canvas, "rgba(255, 0, 0, 1)");
    this.fillWithLines(canvas2, "rgba(255, 255, 0, 1)");
    this.fillWithLines(canvas3, "rgba(0, 255, 0, 1)");

    this.bgYellowSprite = canvas2;
    this.bgGreenSprite = canvas3;
    this.bgRedSprite = canvas;
};

Sprites.prototype.fillWithLines = function (canvasEl, fillStyle) {
    fillStyle = fillStyle || "rgba(255, 0, 0, 1)";
    canvasEl.width = 64;
    canvasEl.height = 64;
    // use getContext to use the canvas for drawing
    var ctx = canvasEl.getContext('2d');
    var width = canvasEl.width;
    var height = canvasEl.height;
    for (var i = 0; i < 10; i++) {
        var x = -width + i * 16;
        var x2 = x + 3;
        var x3 = x + width;
        var x4 = x2 + width;
        var y = height;
        var y2 = 0;
        // Filled triangle
        ctx.fillStyle = fillStyle;
        ctx.beginPath();
        ctx.moveTo(x, y);
        ctx.lineTo(x2, y);
        ctx.lineTo(x4, y2);
        ctx.lineTo(x3, y2);
        ctx.lineTo(x, y);
        ctx.fill();
    }
};

Sprites.prototype.getTileConfig = function (targetTileX, targetTileY, map) {
    if (targetTileX >= map.getWidth()) {
        return null;
    }
    if (targetTileY >= map.getHeight()) {
        return null;
    }
    if (targetTileX < 0) {
        return null;
    }
    if (targetTileY < 0) {
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
        x = 3;
        y = 3;


        if ((tileTypeUp == TILE_TYPE_ROCK) && (tileTypeLeft == TILE_TYPE_ROCK)) {
            x = 0;
            y = 2;
        }


        else if ((tileTypeDown == TILE_TYPE_ROCK) && (tileTypeRight == TILE_TYPE_ROCK)) {
            x = 0;
            y = 1;
        }
        else if ((tileTypeDown == TILE_TYPE_ROCK) && (tileTypeLeft == TILE_TYPE_ROCK)) {
            x = 0;
            y = 3;
        }
        else if ((tileTypeUp == TILE_TYPE_ROCK) && (tileTypeRight == TILE_TYPE_ROCK)) {
            x = 0;
            y = 0;
        }
        else if (tileTypeUp == TILE_TYPE_ROCK) {
            x = 4;
            y = 1;
        }

        else if (tileTypeDown == TILE_TYPE_ROCK) {
            x = 5;
            y = 0;
        }

        else if (tileTypeLeft == TILE_TYPE_ROCK) {
            x = 6;
            y = 0
        }
        else if (tileTypeRight == TILE_TYPE_ROCK) {
            x = 4;
            y = 2;
        }


        if ((tileTypeUp == TILE_TYPE_SPICE) && (tileTypeLeft == TILE_TYPE_SPICE)) {
            x = 1;
            y = 2;
        }


        else if ((tileTypeDown == TILE_TYPE_SPICE) && (tileTypeRight == TILE_TYPE_SPICE)) {
            x = 1;
            y = 1;
        }
        else if ((tileTypeDown == TILE_TYPE_SPICE) && (tileTypeLeft == TILE_TYPE_SPICE)) {
            x = 1;
            y = 3;
        }
        else if ((tileTypeUp == TILE_TYPE_SPICE) && (tileTypeRight == TILE_TYPE_SPICE)) {
            x = 1;
            y = 0;
        }
        else if (tileTypeUp == TILE_TYPE_SPICE) {
            x = 8;
            y = 1;
        }

        else if (tileTypeDown == TILE_TYPE_SPICE) {
            x = 9;
            y = 0;
        }

        else if (tileTypeLeft == TILE_TYPE_SPICE) {
            x = 10;
            y = 0
        }
        else if (tileTypeRight == TILE_TYPE_SPICE) {
            x = 8;
            y = 2;
        }

    }

    if (tileType == TILE_TYPE_SPICE) {
        x = 12;
        y = 0;
    }

    if (tileType == TILE_TYPE_ROCK) {
        x = 3;
        y = 0;
    }


    x *= TILE_WIDTH;
    y *= TILE_HEIGHT;
    var result = {};
    result.x = x;
    result.y = y;
    result.sprite = this.mainSprite;
    return result;
};

Sprites.prototype.setMainSprite = function (mainSprite) {
    this.mainSprite = mainSprite;
};

Sprites.prototype.setBuildingsSprite = function (buildingsSprite) {
    this.buildingsSprite = buildingsSprite;
};

Sprites.prototype.setOkButton = function (okButtonSprite) {
    this.okButtonSprite = okButtonSprite;
};

Sprites.prototype.setUnitSprite = function (unitSprite) {
    this.unitSprite = unitSprite;
};

Sprites.prototype.setBuildingMarkerSprite = function (buildingMarkerSprite) {
    this.buildingMarkerSprite = buildingMarkerSprite;
};

Sprites.prototype.buildUnitSprites = function () {

    var canvasTemp = document.createElement("canvas");

    canvasTemp.width = 32;
    canvasTemp.height = 32;

    var that = this;

    // create colorings
    for (var playerNum = 0; playerNum < 8; playerNum++) {
        var newSprite = document.createElement("canvas");
        colorize(this.unitSprite, newSprite, playerNum);
        this["unitSprite" + playerNum + "1"] = newSprite;
    }

    console.log("Units colored");

    // create rotations
    for (playerNum = 0; playerNum < 8; playerNum++) {
        for (var i = 1; i <= 8; i++) {
            newSprite = document.createElement("canvas");
            rotateUnits(this["unitSprite"+playerNum+"1"], newSprite, i - 1, canvasTemp);
            this["unitSprite"+playerNum+i] = newSprite;
        }
    }

    console.log("Units rotated");

    function colorize(canvasFrom, canvasTo, toPlayer) {
        canvasTo.width = canvasFrom.width;
        canvasTo.height = canvasFrom.height;

        var context = canvasTo.getContext("2d");

        context.drawImage(canvasFrom, 0, 0, canvasFrom.width, canvasFrom.height, 0, 0, canvasFrom.width, canvasFrom.height);

        var width = canvasTo.width;
        var height = canvasTo.height;
        var imgd = context.getImageData(0, 0, canvasTo.width, canvasTo.height);
        var pix = imgd.data;

        var playerColor = that.getPlayerColor(toPlayer);
        var playerColorLight = that.getPlayerLightColor(toPlayer);

        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                var pixelColor = getpixel(x, y);

                if ((pixelColor.r >= 155) && ((pixelColor.r <= 180))) { // red
                    putpixel(x, y, playerColor.r, playerColor.g, playerColor.b, 255);
                } else if ((pixelColor.r >= 215) && (pixelColor.g >= 160) && (pixelColor.g <= 220)) {   // orange
                    putpixel(x, y, playerColorLight.r, playerColorLight.g, playerColorLight.b, 255);
                } else if ((pixelColor.r >= 80) && ((pixelColor.r <= 130))) { // dark red
                    putpixel(x, y, playerColor.r, playerColor.g, playerColor.b, 255);
                } else if ((pixelColor.r >= 120) && (pixelColor.g >= 80) && (pixelColor.g <= 120)) {   // dark orange
                    putpixel(x, y, playerColorLight.r, playerColorLight.g, playerColorLight.b, 255);
                } else {
                    putpixel(x, y, pixelColor.r, pixelColor.g, pixelColor.b, pixelColor.a);
                }
            }
        }

        function putpixel(ix, iy, rd, gr, bl, alpha) {
            var p = (width * iy + ix) * 4;
            pix[p] = rd % 256; // red
            pix[p + 1] = gr % 256; // green
            pix[p + 2] = bl % 256; // blue
            pix[p + 3] = alpha; // alpha
        }

        function getpixel(ix, iy) {
            var p = (width * iy + ix) * 4;
            var result = {};
            result.r = pix[p];
            result.g = pix[p + 1];
            result.b = pix[p + 2];
            result.a = pix[p + 3];
            return result;
        }

        context.putImageData(imgd, 0, 0);
    }

    function rotateUnits(canvasFrom, canvasTo, number, canvasTemp) {
        canvasTo.width = canvasFrom.width;
        canvasTo.height = canvasFrom.height;

        var fromContext = canvasFrom;
        var toContext = canvasTo.getContext('2d');
        var tmpContext = canvasTemp.getContext('2d');
        for (var x = 0; x < canvasFrom.width; x += TILE_WIDTH) {
            for (var y = 0; y < canvasFrom.height; y += TILE_HEIGHT) {

                tmpContext.setTransform(1, 0, 0, 1, 0, 0);
                tmpContext.clearRect(0, 0, canvasTemp.width, canvasTemp.height);


                tmpContext.save();

                // translate context to center of canvas
                tmpContext.translate(canvasTemp.width / 2, canvasTemp.height / 2);

                // rotate 45 degrees clockwise
                tmpContext.rotate(Math.PI / 4 * number);

                tmpContext.drawImage(fromContext, x, y, TILE_WIDTH, TILE_HEIGHT, -canvasTemp.width / 2, -canvasTemp.width / 2, TILE_WIDTH, TILE_HEIGHT);

                tmpContext.restore();

                toContext.drawImage(canvasTemp, 0, 0, TILE_WIDTH, TILE_HEIGHT, x, y, TILE_WIDTH, TILE_HEIGHT);
            }
        }
    }
};

Sprites.prototype.buildBuildingMarkers = function() {

    var canvasTemp = document.createElement("canvas");

    canvasTemp.width = this.buildingMarkerSprite.width;
    canvasTemp.height = this.buildingMarkerSprite.height;

    var that = this;

    // create colorings
    for (var playerNum = 0; playerNum < 8; playerNum++) {
        var newSprite = document.createElement("canvas");
        colorize(this.buildingMarkerSprite, newSprite, playerNum);
        this["buildingMarkerSprite" + playerNum] = newSprite;
        document.body.appendChild(newSprite);
    }

    console.log("Building markers colored");

    function colorize(canvasFrom, canvasTo, toPlayer) {
        canvasTo.width = canvasFrom.width;
        canvasTo.height = canvasFrom.height;

        var context = canvasTo.getContext("2d");

        context.drawImage(canvasFrom, 0, 0, canvasFrom.width, canvasFrom.height, 0, 0, canvasFrom.width, canvasFrom.height);

        var width = canvasTo.width;
        var height = canvasTo.height;
        var imgd = context.getImageData(0, 0, canvasTo.width, canvasTo.height);
        var pix = imgd.data;

        var playerColor = that.getPlayerColor(toPlayer);
        var playerColorLight = that.getPlayerLightColor(toPlayer);
        var playerColorDark = that.getPlayerDarkColor(toPlayer);

        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                var pixelColor = getpixel(x, y);

                if ((pixelColor.r >= 230) && ((pixelColor.b <= 100))) { // orange
                    putpixel(x, y, playerColorLight.r, playerColorLight.g, playerColorLight.b, 255);
                } else if ((pixelColor.r >= 140) && (pixelColor.b <= 100)) {   // red
                    putpixel(x, y, playerColor.r, playerColor.g, playerColor.b, 255);
                }  else if ((pixelColor.r <= 60) && (pixelColor.a > 100)) {   // very dark red
                    putpixel(x, y, playerColorDark.r, playerColorDark.g, playerColorDark.b, 255);
                } else {
                    putpixel(x, y, pixelColor.r, pixelColor.g, pixelColor.b, pixelColor.a);
                }
            }
        }

        function putpixel(ix, iy, rd, gr, bl, alpha) {
            var p = (width * iy + ix) * 4;
            pix[p] = rd % 256; // red
            pix[p + 1] = gr % 256; // green
            pix[p + 2] = bl % 256; // blue
            pix[p + 3] = alpha; // alpha
        }

        function getpixel(ix, iy) {
            var p = (width * iy + ix) * 4;
            var result = {};
            result.r = pix[p];
            result.g = pix[p + 1];
            result.b = pix[p + 2];
            result.a = pix[p + 3];
            return result;
        }

        context.putImageData(imgd, 0, 0);
    }
};

Sprites.prototype.setBulletSprite = function (bulletSprite) {
    this.bulletSprite = bulletSprite;
};

Sprites.prototype.colorPlayerIdSelector = function () {
    var that = this;

    var setOptionColors = function () {
        var id = $(this).text();
        var playerColor = that.getPlayerColor(id);
        $(this).css("color", colorToHex(playerColor.r, playerColor.g, playerColor.b));
    };

    $('#playerId > option').each(setOptionColors);

};

function colorToHex(red, green, blue) {
    var rgb = blue | (green << 8) | (red << 16);
    return '#' + rgb.toString(16);
};

Sprites.prototype.getBuildingMarkerSprite = function(ownerId) {
    return this["buildingMarkerSprite" + ownerId];
};

Sprites.prototype.getBulletConfig = function (bullet) {
    var sprite = this.bulletSprite;

    var result = {};

    result.sprite = sprite;
    result.x = 0;
    result.y = 0;
    result.width = 10;
    result.height = 10;
    return result;

};

Sprites.prototype.getBuildingConfig = function (building) {
    var x = 0;
    var y = 0;
    var width = 2;
    var height = 2;

    var sprite = this.buildingsSprite;


    if (building.type == BUILDING_TYPE_SILO) {
        x = 4;
        y = 0;
    }
    if (building.type == BUILDING_TYPE_POWERPLANT) {
        x = 2;
        y = 0;
    }
    if (building.type == BUILDING_TYPE_BARRACKS) {
        x = 2;
        y = 2;
    }
    if (building.type == BUILDING_TYPE_RADAR) {
        x = 2;
        y = 4;
    }
    if (building.type == BUILDING_TYPE_LIGHTFACTORY) {
        x = 0;
        y = 6;
        width = 3;
    }
    if (building.type == BUILDING_TYPE_CONSTRUCTION_YARD) {
        x = 0;
        y = 0;
    }
    if (building.type == BUILDING_TYPE_FACTORY) {
        x = 3;
        y = 6;
        width = 3;
    }
    if (building.type == BUILDING_TYPE_AIRBASE) {
        x = 2;
        y = 4;
    }
    if (building.type == BUILDING_TYPE_REFINERY) {
        x = 0;
        y = 8;
        width = 3;
    }
    if (building.type == BUILDING_TYPE_REPAIRSHOP) {
        x = 3;
        y = 8;
        width = 3;
    }

    var result = {};

    result.sprite = sprite;
    result.x = x * TILE_WIDTH;
    result.y = y * (TILE_HEIGHT);
    result.width = width * TILE_WIDTH;
    result.height = height * TILE_HEIGHT;
    return result;

};

Sprites.prototype.getSpriteNumberByViewDirection = function (viewDirection) {
    switch (viewDirection) {
    case VIEW_DIRECTION_TOP:
        return 1;

    case VIEW_DIRECTION_TOPRIGHT:
        return 2;

    case VIEW_DIRECTION_RIGHT:
        return 3;

    case VIEW_DIRECTION_BOTTOMRIGHT:
        return 4;

    case VIEW_DIRECTION_BOTTOM:
        return 5;

    case VIEW_DIRECTION_BOTTOMLEFT:
        return 6;

    case VIEW_DIRECTION_LEFT:
        return 7;

    case VIEW_DIRECTION_TOPLEFT:
        return 8;
    }
};

Sprites.prototype.getUnitConfig = function (unit) {

    var viewDirectionNumber = this.getSpriteNumberByViewDirection(unit.viewDirection);
    var sprite = this["unitSprite"+unit.ownerId+viewDirectionNumber];
    var xOffset = 0;
    var yOffset = 0;

    var x = 0;
    var y = 0;

    if (unit.unitType == UNIT_TYPE_BATTLE_TANK) {
        x = 2;
        y = 1;
    }
    if (unit.unitType == UNIT_TYPE_HARVESTER) {
        y = 0;

        if (unit.harvesting > 0) {
            y = ((Math.floor((unit.harvesting / 3)) % 3) + 1);
        }


        x = 4;
        y = 0;
    }
    if (unit.unitType == UNIT_TYPE_SIEGE_TANK) {
        x = 2;
        y = 2;
    }
    if (unit.unitType == UNIT_TYPE_DEVASTATOR) {
        x = 2;
        y = 3;
    }
    if (unit.unitType == UNIT_TYPE_LAUNCHER) {
        x = 1;
        y = 3;
    }

    var result = {};
    result.sprite = sprite;
    result.x = x * TILE_WIDTH;
    result.y = y * TILE_HEIGHT;
    result.width = TILE_WIDTH;
    result.height = TILE_HEIGHT;
    result.xOffset = xOffset;
    result.yOffset = yOffset;
    return result;
};

Sprites.prototype.getPlayerColor = function (playerId) {
    var result = {};
    if (playerId == 0) {
        result.r = 176;
        result.g = 0;
        result.b = 0;
        return result;
    }
    if (playerId == 1) {
        result.r = 0;
        result.g = 176;
        result.b = 0;
        return result;
    }
    if (playerId == 2) {
        result.r = 0;
        result.g = 0;
        result.b = 176;
        return result;
    }
    if (playerId == 3) {
        result.r = 176;
        result.g = 0;
        result.b = 176;
        return result;
    }
    if (playerId == 4) {
        result.r = 0;
        result.g = 176;
        result.b = 176;
        return result;
    }
    if (playerId == 5) {
        result.r = 176;
        result.g = 176;
        result.b = 0;
        return result;
    }
    if (playerId == 6) {
        result.r = 176;
        result.g = 176;
        result.b = 176;
        return result;
    }
    if (playerId == 7) {
        result.r = 40;
        result.g = 40;
        result.b = 40;
        return result;
    }
    console.log("Unknown player id " + playerId);
    return result;
};

Sprites.prototype.getPlayerLightColor = function (playerId) {
    var result = {};
    if (playerId == 0) {
        result.r = 248;
        result.g = 180;
        result.b = 0;
        return result;
    }
    if (playerId == 1) {
        result.r = 0;
        result.g = 248;
        result.b = 180;
        return result;
    }
    if (playerId == 2) {
        result.r = 0;
        result.g = 180;
        result.b = 248;
        return result;
    }
    if (playerId == 3) {
        result.r = 248;
        result.g = 0;
        result.b = 248;
        return result;
    }
    if (playerId == 4) {
        result.r = 0;
        result.g = 248;
        result.b = 248;
        return result;
    }
    if (playerId == 5) {
        result.r = 248;
        result.g = 248;
        result.b = 0;
        return result;
    }
    if (playerId == 6) {
        result.r = 248;
        result.g = 248;
        result.b = 248;
        return result;
    }
    if (playerId == 7) {
        result.r = 90;
        result.g = 90;
        result.b = 90;
        return result;
    }
    console.log("Unknown player id " + playerId);
    return result;
};

Sprites.prototype.getPlayerDarkColor = function (playerId) {
    var result = {};
    if (playerId == 0) {
        result.r = 55;
        result.g = 15;
        result.b = 15;
        return result;
    }
    if (playerId == 1) {
        result.r = 15;
        result.g = 55;
        result.b = 15;
        return result;
    }
    if (playerId == 2) {
        result.r = 15;
        result.g = 15;
        result.b = 55;
        return result;
    }
    if (playerId == 3) {
        result.r = 55;
        result.g = 15;
        result.b = 55;
        return result;
    }
    if (playerId == 4) {
        result.r = 15;
        result.g = 55;
        result.b = 55;
        return result;
    }
    if (playerId == 5) {
        result.r = 55;
        result.g = 55;
        result.b = 15;
        return result;
    }
    if (playerId == 6) {
        result.r = 55;
        result.g = 55;
        result.b = 55;
        return result;
    }
    if (playerId == 7) {
        result.r = 15;
        result.g = 15;
        result.b = 15;
        return result;
    }
    console.log("Unknown player id " + playerId);
    return result;
};







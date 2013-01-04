var Serializers = new Array();

Serializers[1] = function (payload) {
    var result = new Array();
    result.money = getIntAt(payload, 0);
    result.electricity = getIntAt(payload, 4);
    return ["UpdateMoney", result];
};

Serializers[2] = function (payload) {
    var result = new Array();
    var position = 0;

    var units = new Array();
    var unitsLength = getIntAt(payload, position);
    position += 4;
    for (var i = 0; i < unitsLength; i++) {
        var unitDTO = new Array();
        position = readUnitDTO(unitDTO, payload, position);
        units.push(unitDTO);
    }
    result["units"] = units;


    var buildings = new Array();
    var buildingsLength = getIntAt(payload, position);
    position += 4;
    for (i = 0; i < buildingsLength; i++) {
        var buildingDTO = new Array();
        position = readBuildingDTO(buildingDTO, payload, position);
        buildings.push(buildingDTO);
    }
    result["buildings"] = buildings;

    var bullets = new Array();
    var bulletsLength = getIntAt(payload, position);
    position += 4;
    for (i = 0; i < bulletsLength; i++) {
        var bulletDTO = new Array();
        position = readBulletDTO(bulletDTO, payload, position);
        bullets.push(bulletDTO);
    }
    result["bullets"] = bullets;

    var changedTiles = new Array();
    var changedTilesLength = getIntAt(payload, position);
    position += 4;
    for (i = 0; i < changedTilesLength; i++) {
        var changedTileDTO = new Array();
        position = readChangedTileDTO(changedTileDTO, payload, position);
        changedTiles.push(changedTileDTO);
    }
    result["changedTiles"] = changedTiles;

    result["tickCount"] = getLongAt(payload,position);
    position+=8;


    if (position != payload.length) {
        console.log("Read " + position + " bytes from payloads " + payload.length);
    }

    return ["UpdateMapIncremental", result];
};


Serializers[3] = function (payload) {
    var result = new Array();
    var position = 0;

    var mapDTO = new Array();
    result["map"] = mapDTO;

    position = readMapDTO(mapDTO, payload, position);

    result["tickCount"] = getLongAt(payload,position);
    position+=8;

    result["playerId"] = getIntAt(payload,position);
    position+=4;


    if (position != payload.length) {
        console.log("Read " + position + " bytes from payloads " + payload.length);
    }

    return ["UpdateMap", result];
};


Serializers[4] = function (payload) {
    var result = new Array();
    var position = 0;

    var options = new Array();
    var optionsDTOLength = getIntAt(payload, position);
    position += 4;
    for (i = 0; i < optionsDTOLength; i++) {
        var optionDTO = new Array();
        position = readOptionDTO(optionDTO, payload, position);
        options.push(optionDTO);
    }
    result["options"] = options;

    result["builderId"] = getIntAt(payload,position);
    position+=4;

    result["currentlyBuildingId"] = getIntAt(payload,position);
    position+=4;

    result["readyToBuild"] = getBooleanAt(payload,position);
    position+=1;

    result["percentsDone"] = payload[position];
    position+=1;

    result["currentlyBuildingOptionType"] = payload[position];
    position+=1;


    if (position != payload.length) {
        console.log("Read " + position + " bytes from payloads " + payload.length);
    }

    return ["UpdateConstructionOptions", result];
};

function readMapDTO(dto, payload, position) {
    var units = new Array();
    var unitsLength = getIntAt(payload, position);
    position += 4;
    for (var i = 0; i < unitsLength; i++) {
        var unitDTO = new Array();
        position = readUnitDTO(unitDTO, payload, position);
        units.push(unitDTO);
    }
    dto["units"] = units;


    var buildings = new Array();
    var buildingsLength = getIntAt(payload, position);
    position += 4;
    for (i = 0; i < buildingsLength; i++) {
        var buildingDTO = new Array();
        position = readBuildingDTO(buildingDTO, payload, position);
        buildings.push(buildingDTO);
    }
    dto["buildings"] = buildings;

    var tiles = new Array();
    var tileLength = getIntAt(payload, position);
    position += 4;
    for (i = 0; i < tileLength; i++) {
        var tileDTO = new Array();
        position = readTileDTO(tileDTO, payload, position);
        tiles.push(tileDTO);
    }
    dto["tiles"] = tiles;

    var bullets = new Array();
    var bulletsLength = getIntAt(payload, position);
    position += 4;
    for (i = 0; i < bulletsLength; i++) {
        var bulletDTO = new Array();
        position = readBulletDTO(bulletDTO, payload, position);
        bullets.push(bulletDTO);
    }
    dto["bullets"] = bullets;


    dto["width"] = getShortAt(payload, position);
    position += 2;

    dto["height"] = getShortAt(payload, position);
    position += 2;

    return position;
}

function readUnitDTO(dto, payload, position) {
    dto["id"] = getIntAt(payload,position);
    position+=4;
    dto["x"] = getShortAt(payload,position);
    position+=2;
    dto["y"] = getShortAt(payload,position);
    position+=2;
    dto["hp"] = getShortAt(payload,position);
    position+=2;
    dto["maxHp"] = getShortAt(payload,position);
    position+=2;

    dto["unitType"] = payload[position];
    position++;
    dto["viewDirection"] = payload[position];
    position++;
    dto["travelledPercents"] = payload[position];
    position++;
    dto["spicePercents"] = payload[position];
    position++;
    dto["ownerId"] = payload[position];
    position++;
    dto["harvesting"] = payload[position];
    position++;
    return position;
}

function readBuildingDTO(dto, payload, position) {
    dto["id"] = getIntAt(payload,position);
    position+=4;
    dto["x"] = getShortAt(payload,position);
    position+=2;
    dto["y"] = getShortAt(payload,position);
    position+=2;


    dto["hp"] = getShortAt(payload,position);
    position+=2;
    dto["maxHp"] = getShortAt(payload,position);
    position+=2;

    dto["type"] = payload[position];
    position++;
    dto["width"] = payload[position];
    position++;
    dto["height"] = payload[position];
    position++;
    dto["constructionComplete"] = getBooleanAt(payload,position);
    position++;
    dto["entityBuiltId"] = payload[position];
    position++;
    dto["ownerId"] = payload[position];
    position++;
    return position;
}

function readChangedTileDTO(dto, payload, position) {
    dto["tileType"] = payload[position];
    position++;
    dto["x"] = getShortAt(payload,position);
    position+=2;
    dto["y"] = getShortAt(payload,position);
    position+=2;
    return position;
}


function readBulletDTO(dto, payload, position) {
    dto["x"] = getShortAt(payload,position);
    position+=2;
    dto["y"] = getShortAt(payload,position);
    position+=2;
    dto["goalX"] = getShortAt(payload,position);
    position+=2;
    dto["goalY"] = getShortAt(payload,position);
    position+=2;
    dto["progress"] = payload[position];
    position++;
    dto["type"] = payload[position];
    position++;

    return position;
}


function readOptionDTO(dto, payload, position) {
    dto["cost"] = getShortAt(payload,position);
    position+=2;
    dto["type"] = payload[position];
    position++;
    dto["entityToBuildType"] = payload[position];
    position++;
    return position;
}

function readTileDTO(dto, payload, position) {
    dto["tileType"] = payload[position];
    position++;
    return position;
}

function getIntAt(arr, offs) {
    return (arr[offs + 0] << 24) +
            (arr[offs + 1] << 16) +
            (arr[offs + 2] << 8) +
            arr[offs + 3];
}

function getShortAt(arr, offs) {
    return (arr[offs + 0] << 8) +
            (arr[offs + 1] << 0);
}

function getBooleanAt(arr, offs) {
    if (arr[offs] == 0) {
        return false;
    } else {
        return true;
    }
}

function getLongAt(arr, offs) {
    return (arr[offs + 0] << 56) +
            (arr[offs + 1] << 48) +
            (arr[offs + 2] << 40) +
            (arr[offs + 3] << 32) +
            (arr[offs + 4] << 24) +
            (arr[offs + 5] << 16) +
            (arr[offs + 6] << 8) +
            arr[offs + 7];
}


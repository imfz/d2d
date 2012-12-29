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


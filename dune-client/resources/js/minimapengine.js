function MinimapGameEngine() {
    this.frameCount = 0;
    this.startTime = 0;
    console.log("Created minimap engine");
}

MinimapGameEngine.prototype.setCanvas = function (canvas) {
    this.canvas = canvas;
};

MinimapGameEngine.prototype.setMap = function (map) {
    this.map = map;
};

MinimapGameEngine.prototype.setEngine = function (engine) {
    this.engine = engine;
};

MinimapGameEngine.prototype.bindEvents = function() {
    var that = this;
    $(this.canvas).mousedown(function(e){
        var x = Math.floor((e.pageX-$(that.canvas).offset().left));
        var y = Math.floor((e.pageY-$(that.canvas).offset().top));
        var mapX = Math.round(x / that.canvas.width * that.map.getWidth());
        var mapY = Math.round(y / that.canvas.height * that.map.getHeight());

        switch (event.which) {
        case 1:
            that.engine.centerOnCoordinates(mapX,mapY);
            break;
        case 3:
            if (mapX < 0) {
                mapX = 0;
            }
            if (mapY < 0) {
                mapY = 0;
            }
            if (mapX >= that.map.width) {
                mapX = that.map.width-1;
            }
            if (mapY >= that.map.height) {
                mapY = that.map.height-1;
            }
            connection.sendUnitAction(that.engine.selectedUnitId,Math.round(mapX),Math.round(mapY));
            break;
        }
        return false ;
    });
    $(this.canvas).bind("contextmenu",function(e){
        return false;
    });
};


MinimapGameEngine.prototype.render = function () {
    if (this.startTime == 0) {
        this.startTime = new Date().getTime();
    }
    this.frameCount++;
    var canvas = this.canvas;
    var context = canvas.getContext("2d");
    var map = this.map;
    var engine = this.engine;

    context.clearRect(0, 0, canvas.width, canvas.height);
    var imgd = context.createImageData(canvas.width, canvas.height);
    var pix = imgd.data;

    drawTerrain();
    drawUnits();
    drawBuildings();
    drawRectangle();
    var that = this;
    setTimeout(function () {
        that.render()
    }, 1000 / 60);

    function putpixel(imgd, ix, iy, rd, gr, bl) {
        var p = (canvas.height * iy + ix) * 4;
        pix[p]   = rd % 256; // red
        pix[p+1] = gr % 256; // green
        pix[p+2] = bl % 256; // blue
        pix[p+3] = 255; // alpha
    }

    function drawTerrain() {
        for (var x = 0; x < canvas.width; x++) {
            for (var y = 0; y < canvas.height; y++) {
                var mapX = Math.round(x / canvas.width * map.getWidth());
                var mapY = Math.round(y / canvas.height * map.getHeight());
                var tileType = map.getTileType(mapX, mapY);
                if (tileType == TILE_TYPE_SAND) {
                    putpixel(imgd, x, y, 214, 179, 103);
                }
                else if (tileType == TILE_TYPE_ROCK) {
                    putpixel(imgd, x, y, 124, 89, 23);
                }
                else if (tileType == TILE_TYPE_SPICE) {
                    putpixel(imgd, x, y, 200, 134, 61);
                }
                else if (tileType == TILE_TYPE_RICH_SPICE) {
                    putpixel(imgd, x, y, 200, 134, 61);
                } else {
                    putpixel(imgd, x, y, 0, 0, 0);
                }
            }
        }
    }

    function drawUnits() {
        for (var i = 0; i < map.units.length; i++) {
            var unit = map.units[i];
            var x = Math.round(unit.x / map.getWidth() * canvas.width);
            var y = Math.round(unit.y / map.getHeight() * canvas.height);
            var x2 = Math.round((unit.x + 1) / map.getWidth() * canvas.width);
            var y2 = Math.round((unit.y + 1) / map.getHeight() * canvas.height);
            var color = sprites.getPlayerColor(unit.ownerId);
            putPixels(x,y,x2,y2,color);
        }
    }

    function drawBuildings() {
        for (var i = 0; i < map.buildings.length; i++) {
            var building = map.buildings[i];
            var x = Math.round(building.x / map.getWidth() * canvas.width);
            var y = Math.round(building.y / map.getHeight() * canvas.height);
            var x2 = Math.round((building.x + building.width) / map.getWidth() * canvas.width);
            var y2 = Math.round((building.y + building.height)/ map.getHeight() * canvas.height);
            var color = sprites.getPlayerColor(building.ownerId);
            putPixels(x,y,x2,y2,color);
        }
    }

    function putPixels(x, y, x2, y2, color) {
        for (var w = x; w <= x2; w++) {
            for (var h = y; h <= y2; h++) {
                putpixel(imgd, w, h, color.r, color.g, color.b);
            }
        }
    }

    function drawRectangle() {
        var x1 = engine.x * 1.0 / map.getWidth() * canvas.width;
        var y1 = engine.y * 1.0 / map.getHeight() * canvas.height;
        var x2 = (engine.x * 1.0 + engine.widthInTiles) / map.getWidth() * canvas.width;
        var y2 = (engine.y * 1.0 + engine.heightInTiles) / map.getHeight() * canvas.height;
        context.putImageData(imgd, 0, 0);
        context.strokeStyle = "#000000";
        context.lineWidth = 2;
        context.strokeRect(x1, y1, x2 - x1, y2 - y1);
    }

};





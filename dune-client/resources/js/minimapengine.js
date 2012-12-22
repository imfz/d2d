function MinimapGameEngine() {
    this.frameCount = 0;
    this.startTime = 0;
    console.log("Created minimap engine");
}

MinimapGameEngine.prototype.setCanvas = function (canvas) {
    this.canvas = canvas;
}

MinimapGameEngine.prototype.setMap = function (map) {
    this.map = map;
}

MinimapGameEngine.prototype.setEngine = function (engine) {
    this.engine = engine;
}

MinimapGameEngine.prototype.bindEvents = function() {
    var that = this;
    $(this.canvas).mousedown(function(e){
        switch (event.which) {
        case 1:
            var x = Math.floor((e.pageX-$(that.canvas).offset().left));
            var y = Math.floor((e.pageY-$(that.canvas).offset().top));
            var mapX = x / that.canvas.width * that.map.getWidth() - that.engine.widthInTiles / 2;
            var mapY = y / that.canvas.height * that.map.getHeight() - that.engine.heightInTiles / 2;
            that.engine.x = Math.round(mapX);
            that.engine.y = Math.round(mapY);
            break;
        case 3:
            var x = Math.floor((e.pageX-$(that.canvas).offset().left));
            var y = Math.floor((e.pageY-$(that.canvas).offset().top));
            var mapX = x / that.canvas.width * that.map.getWidth() - that.engine.widthInTiles / 2;
            var mapY = y / that.canvas.height * that.map.getHeight() - that.engine.heightInTiles / 2;
            connection.sendUnitAction(that.engine.selectedUnitId,Math.round(mapX),Math.round(mapY));
            break;
        }
        return false ;
    });
    $(this.canvas).bind("contextmenu",function(e){
        return false;
    });
}

function putpixel(imgd, ix, iy, rd, gr, bl, al, height, pix)
{
    var p = (height * iy + ix) * 4;
    pix[p]   = rd % 256; // red
    pix[p+1] = gr % 256; // green
    pix[p+2] = bl % 256; // blue
    pix[p+3] = al % 256; // alpha
}

MinimapGameEngine.prototype.render = function () {
    if (this.startTime == 0) {
        this.startTime = new Date().getTime();
    }
    var currentTime = new Date().getTime();
    this.frameCount++;
    var fps = this.frameCount / (currentTime - this.startTime) * 1000;
    context = this.canvas.getContext("2d");
    context.clearRect(0, 0, this.canvas.width, canvas.height);

    var imgd = context.createImageData(this.canvas.width, this.canvas.height);
    var pix = imgd.data;
    for (var x = 0; x < this.canvas.width; x++) {
        for (var y = 0; y < this.canvas.height; y++) {
            var mapX = Math.round(x / this.canvas.width * this.map.getWidth());
            var mapY = Math.round(y / this.canvas.height * this.map.getHeight());
            var tileType = this.map.getTileType(mapX, mapY);
            if (tileType == TILE_TYPE_SAND) {
                putpixel(imgd, x,y, 214, 179, 103, 255, this.canvas.height, pix);
            }
            else if (tileType == TILE_TYPE_ROCK) {
                putpixel(imgd, x,y, 124, 89, 23, 255, this.canvas.height, pix);
            }
            else if (tileType == TILE_TYPE_SPICE) {
                putpixel(imgd, x,y, 200, 134, 61, 255, this.canvas.height, pix);
            }
            else if (tileType == TILE_TYPE_RICH_SPICE) {
                putpixel(imgd, x,y, 200, 134, 61, 255, this.canvas.height, pix);
            } else {
                putpixel(imgd, x,y, 0,0,0, 255, this.canvas.height, pix);
            }

        }
    }

    for (var i = 0; i < this.map.units.length; i++) {
        var unit = this.map.units[i];
        var x = Math.round(unit.x / this.map.getWidth() * this.canvas.width );
        var y = Math.round(unit.y / this.map.getHeight() * this.canvas.height);
        putpixel(imgd, x,y, 214, 0, 0, 255, this.canvas.height, pix);
    }

    for (var i = 0; i < this.map.buildings.length; i++) {
        var building = this.map.buildings[i];
        var x = Math.round(building.x / this.map.getWidth() * this.canvas.width );
        var y = Math.round(building.y / this.map.getHeight() * this.canvas.height);
        for (var w = x; w < x + building.width; w++) {
            for (var h = y; h < y + building.height; h++) {
                putpixel(imgd, w,h, 214, 0, 0, 255, this.canvas.height, pix);
            }
        }

    }

    var x1 = this.engine.x * 1.0 / this.map.getWidth() * this.canvas.width;
    var y1 = this.engine.y * 1.0 / this.map.getHeight() * this.canvas.height;

    var x2 = (this.engine.x * 1.0 + this.engine.widthInTiles) / this.map.getWidth() * this.canvas.width;
    var y2 = (this.engine.y * 1.0 + this.engine.heightInTiles) / this.map.getHeight() * this.canvas.height;

    context.putImageData(imgd, 0,0);

    context.strokeStyle = "#000000";
    context.lineWidth = 2;
    context.strokeRect(x1,y1,x2-x1,y2-y1);

    var that = this;
    setTimeout(function () {
        that.render()
    }, 1000 / 60);
}





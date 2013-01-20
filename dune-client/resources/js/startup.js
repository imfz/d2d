var gameLog;
var engine;
var minimapEngine;
var map;
var chat;
var networking;
var handler;
var rightmenu;
var moneyTab;
var sprites;

$(function () {
    var thing = { plugin: 'jquery-json', version: 2.4 };

    console.log("Initialized");

    $("#host").val("ws://" + window.location.hostname + ":" + window.location.port +  "/chat");

    gameLog = new GameLog();
    sprites = new Sprites();
    engine = new GameEngine();
    minimapEngine = new MinimapGameEngine();
    map = new GameMap();
    networking = new NetworkConnection();
    lobby = new Lobby();
    chat = new Chat();
    rightmenu = new RightMenu();
    moneyTab = new MoneyTab();
    handler = new Handler(map, gameLog, rightmenu, engine, moneyTab, lobby);

    var canvas = $("#canvas").get(0);
    engine.setCanvas(canvas);
    engine.setMap(map);

    var minimapCanvas = $("#minimap").get(0);
    minimapEngine.setCanvas(minimapCanvas);
    minimapEngine.setMap(map);
    minimapEngine.setEngine(engine);

    rightmenu.setEngine(engine);

    rightmenu.setMainSprite(Utils.getImageElement("images/buys.jpg"));

    sprites.setMainSprite(Utils.getImageElement("images/main_sprite.png"));
    sprites.setUnitSprite(Utils.getImageElement("images/units.png"));
    sprites.setBulletSprite(Utils.getImageElement("images/bullet.png"));
    sprites.setBuildingsSprite(Utils.getImageElement("images/buildings.png"));
    sprites.setHarvesterSprite(Utils.getImageElement("images/harvester.png"));

    sprites.setOkButton(Utils.getImageElement("images/okbutton.png"));

    Utils.afterImagesLoaded(function() {

        sprites.buildUnitSprites();

        minimapEngine.bindEvents();

        rightmenu.bindEvents();

        gameLog.redraw();

        networking.init();

        lobby.setNetwork(connection);
        lobby.init();

        chat.setEngine(engine);
        chat.init();

        rightmenu.setOptions([]);
    });

});

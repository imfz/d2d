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

    sprites.setMainSprite(Utils.getImageElement("images/main_sprite.jpg"));
    sprites.setUnitSprite(Utils.getImageElement("images/units.png"));
    sprites.setBulletSprite(Utils.getImageElement("images/bullet.png"));
    sprites.setBuildingsSprite(Utils.getImageElement("images/buildings.jpg"));
    sprites.setRefinerySprite(Utils.getImageElement("images/refinery.jpg"));
    sprites.setHarvesterSprite(Utils.getImageElement("images/harvester.png"));

    sprites.setOkButton(Utils.getImageElement("images/okbutton.png"));
    sprites.setBuildBgGreen(Utils.getImageElement("images/build_bg_green.png"));
    sprites.setBuildBgRed(Utils.getImageElement("images/build_bg_red.png"));
    sprites.setBuildBgYellow(Utils.getImageElement("images/build_bg_yellow.png"));

    var canvas = $("#canvas").get(0);
    engine.setCanvas(canvas);
    engine.setMap(map);

    var minimapCanvas = $("#minimap").get(0);
    minimapEngine.setCanvas(minimapCanvas);
    minimapEngine.setBufferCanvas($("#minimap_buffer").get(0));
    minimapEngine.setMap(map);
    minimapEngine.setEngine(engine);
    minimapEngine.bindEvents();

    rightmenu.setMainSprite(Utils.getImageElement("images/buys.jpg"));
    rightmenu.bindEvents();
    rightmenu.setEngine(engine);

    gameLog.redraw();

    networking.init();

    lobby.setNetwork(connection);
    lobby.init();

    chat.setEngine(engine);
    chat.init();

    rightmenu.setOptions([
        {type: BUY_OPTION_SILO},
        {type: BUY_OPTION_REFINERY},
        {type: BUY_OPTION_REPAIR_DEPO},
        {type: BUY_OPTION_AIRBASE},
        {type: BUY_OPTION_FACTORY}
    ]);

    connection.start("uzerok");
});

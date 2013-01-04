function Windows() {
}

Windows.prototype.openLobby = function() {
    $("#gamelisttable > tbody").empty();
    $("#login").css({display: "none"});
    $("#lobby").css({display: "block"});
    $("#game").css({display: "none"});
};

Windows.prototype.openLogin = function() {
    $("#login").css({display: "block"});
    $("#lobby").css({display: "none"});
    $("#game").css({display: "none"});
};

Windows.prototype.openGame = function() {
    $("#login").css({display: "none"});
    $("#lobby").css({display: "none"});
    $("#game").css({display: "block"});


};

var windows = new Windows();
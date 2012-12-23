function $F() {
    return document.getElementById(arguments[0]).value;
}

function getKeyCode(ev) {
    if (window.event) {
        return window.event.keyCode;
    }
    return ev.keyCode;
}

function Chat() {
    console.log("Chat created");
}

Chat.prototype.sendMessage = function (message) {
    if (message != null && message.length > 0)
        connection.sendChatMessage(message);
}

Chat.prototype.init = function () {
    var that = this;
    document.getElementById('username').setAttribute('autocomplete', 'OFF');
    document.getElementById('username').onkeyup = function (ev) {
        var keyc = getKeyCode(ev);
        if (keyc == 13 || keyc == 10) {
            connection.start($F('username'), $F('playerId'));
            return false;
        }
        return true;
    };
    document.getElementById('joinB').onclick = function (event) {
        connection.start($F('username'), $F('playerId'));
        return false;
    };
    document.getElementById('phrase').setAttribute('autocomplete', 'OFF');
    document.getElementById('phrase').onkeyup = function (ev) {
        var keyc = getKeyCode(ev);
        if (keyc == 13 || keyc == 10) {
            that.sendMessage($F('phrase'));
            document.getElementById('phrase').value = '';
            return false;
        }
        return true;
    };
    document.getElementById('sendB').onclick = function (event) {
        that.sendMessage($F('phrase'));
        document.getElementById('phrase').value = '';
        return false;
    };
    console.log("Chat initialized");
}
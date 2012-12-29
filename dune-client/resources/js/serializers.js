var Serializers = new Array();

Serializers[1] = function (payload) {
    var result = new Array();
    result.money = getIntAt(payload, 1);
    result.electricity = getIntAt(payload, 5);
    return ["UpdateMoney", result];
};

function getIntAt(arr, offs) {
    return (arr[offs + 0] << 24) +
            (arr[offs + 1] << 16) +
            (arr[offs + 2] << 8) +
            arr[offs + 3];
}


({
    appDir: "../web/",
    baseUrl: "js",
    dir: "web/",
    paths: {
        "jquery": "require-jquery"
    },
    modules: [
        {
            name: "main",
            exclude: ["jquery"]
        }
    ]
})

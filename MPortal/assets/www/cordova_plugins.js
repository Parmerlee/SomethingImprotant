cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/cordova-hot-code-push-plugin/www/chcp.js",
        "id": "cordova-hot-code-push-plugin.chcp",
        "clobbers": [
            "chcp"
        ]
    },
    {
        "file": "plugins/cordova-hot-code-push-local-dev-addon/www/chcpLocalDev.js",
        "id": "cordova-hot-code-push-local-dev-addon.chcpLocalDev",
        "clobbers": [
            "chcpLocalDev"
        ]
    },
    {
        "file": "plugins/cordova-plugin-statusbar/www/statusbar.js",
        "id": "cordova-plugin-statusbar.statusbar",
        "clobbers": [
            "window.StatusBar"
        ]
    },
     {
            "file": "plugins/cordova-plugin-splashscreen/www/splashscreen.js",
            "id": "cordova-plugin-splashscreen.SplashScreen",
            "clobbers": [
                "navigator.splashscreen"
            ]
        },
    {
            "file": "plugins/com.gldjc.guangcaiclient/www/httpsinterface.js",
            "id": "com.gldjc.guangcaiclient.httpsinterface",
            "clobbers": [
                "httpsinterface"
            ]
        }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "cordova-plugin-whitelist": "1.3.0",
     "cordova-hot-code-push-plugin": "1.5.2",
     "cordova-hot-code-push-local-dev-addon": "0.4.2",
     "com.gldjc.guangcaiclient": "0.0.1",
     "cordova-plugin-console": "1.0.4",
     "cordova-plugin-splashscreen": "4.0.0",
     "cordova-plugin-statusbar": "2.2.0"
}
// BOTTOM OF METADATA
});
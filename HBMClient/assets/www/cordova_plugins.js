cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/cordova-plugin-device/www/device.js",
        "id": "cordova-plugin-device.device",
        "clobbers": [
            "device"
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
        "file": "plugins/cordova-plugin-statusbar/www/statusbar.js",
        "id": "cordova-plugin-statusbar.statusbar",
        "clobbers": [
            "window.StatusBar"
        ]
    },
    {
        "file": "plugins/ionic-plugin-keyboard/www/android/keyboard.js",
        "id": "ionic-plugin-keyboard.keyboard",
        "clobbers": [
            "cordova.plugins.Keyboard"
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
    "cordova-plugin-whitelist": "1.2.2",
    "cordova-plugin-console": "1.0.4-dev",
    "cordova-plugin-device": "1.1.3-dev",
    "cordova-plugin-splashscreen": "4.0.0",
    "cordova-plugin-statusbar": "2.1.4-dev",
    "ionic-plugin-keyboard": "2.2.1",
    "com.gldjc.guangcaiclient": "0.0.1"
};
// BOTTOM OF METADATA
});
cordova.define('com.gldjc.guangcaiclient.httpsinterface', function(require, exports, module){
               
               var exec = require("cordova/exec");
               
               function Httpsinterface() {};
               
               Httpsinterface.prototype.version = function (getversion) {
               
               exec(getversion, null, 'Httpsinterface', 'version', []);
               
               };
               
               Httpsinterface.prototype.GetLocalData = function ( getLocalData ){
               
               exec(getLocalData, null, 'Httpsinterface', 'GetLocalData', []);
               
               }
               
               //返回
               Httpsinterface.prototype.NativeGoBack = function ( NativeGoBack ){
               
               exec(NativeGoBack, null, 'Httpsinterface', 'NativeGoBack', []);
               
               }
               
               
               //分享
               Httpsinterface.prototype.Html5Share = function ( Html5Share ){
               
               exec(Html5Share, null, 'Httpsinterface', 'Html5Share', []);
               
               }
               
        
               var httpsinterface = new Httpsinterface();
               
               module.exports = httpsinterface;
               
               });

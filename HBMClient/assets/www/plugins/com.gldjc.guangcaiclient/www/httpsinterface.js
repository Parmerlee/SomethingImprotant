cordova.define("com.gldjc.guangcaiclient.httpsinterface", function(require, exports, module) {
var exec = require('cordova/exec');
  
var httpsinterface = {};
//分享
httpsinterface.Html5Share = function(successCallback, errorCallback, args) {
  exec(successCallback, errorCallback, "httpsinterface", "Html5Share", [args]);
};

//家宽第一个界面
httpsinterface.GetJiakuanData = function(args,successCallback) {
  exec(successCallback, null, "httpsinterface", "GetJiakuanData", [args]);
};

//返回
httpsinterface.NativeGoBack = function(successCallback) {
  exec(successCallback, null, "httpsinterface", "NativeGoBack", []);
};

 //获得地域
httpsinterface.GetJiakuanArea = function ( param,getJiakuanArea ){
   exec(getJiakuanArea, null, 'httpsinterface', 'GetJiakuanArea', [ param ]);
}

 //家宽第二个界面 详情
httpsinterface.GetJiakuanData2 = function ( param,getJiakuanData2 ){
	exec(getJiakuanData2, null, 'httpsinterface', 'GetJiakuanData2', [ param ]);
 }

  //家宽第三个界面 排名
httpsinterface.GetJiakuanData3 = function ( param,getJiakuanData2 ){
	exec(getJiakuanData2, null, 'httpsinterface', 'GetJiakuanData3', [ param ]);
 }

  //政企第一个界面 
httpsinterface.GetZhenqiData = function ( param,successCallback ){
	exec(successCallback, null, 'httpsinterface', 'GetGeMarketData', [ param ]);
 }

  //政企详情界面 
httpsinterface.GetZhenqiData2 = function ( param,successCallback ){
	exec(successCallback, null, 'httpsinterface', 'GetGeMarketData2', [ param ]);
 }

  //政企第三个界面 排名 
httpsinterface.GetZhenqiData3 = function ( param,successCallback ){
	exec(successCallback, null, 'httpsinterface', 'GetGeMarketData3', [ param ]);
 }

module.exports = httpsinterface;
});

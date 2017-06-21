cordova.define("com.gldjc.guangcaiclient.httpsinterface", function(require, exports, module) {
var exec = require('cordova/exec');
  
var httpsinterface = {};
//����
httpsinterface.Html5Share = function(successCallback, errorCallback, args) {
  exec(successCallback, errorCallback, "httpsinterface", "Html5Share", [args]);
};

//�ҿ��һ������
httpsinterface.GetJiakuanData = function(args,successCallback) {
  exec(successCallback, null, "httpsinterface", "GetJiakuanData", [args]);
};

//����
httpsinterface.NativeGoBack = function(successCallback) {
  exec(successCallback, null, "httpsinterface", "NativeGoBack", []);
};

 //��õ���
httpsinterface.GetJiakuanArea = function ( param,getJiakuanArea ){
   exec(getJiakuanArea, null, 'httpsinterface', 'GetJiakuanArea', [ param ]);
}

 //�ҿ�ڶ������� ����
httpsinterface.GetJiakuanData2 = function ( param,getJiakuanData2 ){
	exec(getJiakuanData2, null, 'httpsinterface', 'GetJiakuanData2', [ param ]);
 }

  //�ҿ���������� ����
httpsinterface.GetJiakuanData3 = function ( param,getJiakuanData2 ){
	exec(getJiakuanData2, null, 'httpsinterface', 'GetJiakuanData3', [ param ]);
 }

  //�����һ������ 
httpsinterface.GetZhenqiData = function ( param,successCallback ){
	exec(successCallback, null, 'httpsinterface', 'GetGeMarketData', [ param ]);
 }

  //����������� 
httpsinterface.GetZhenqiData2 = function ( param,successCallback ){
	exec(successCallback, null, 'httpsinterface', 'GetGeMarketData2', [ param ]);
 }

  //������������� ���� 
httpsinterface.GetZhenqiData3 = function ( param,successCallback ){
	exec(successCallback, null, 'httpsinterface', 'GetGeMarketData3', [ param ]);
 }

module.exports = httpsinterface;
});

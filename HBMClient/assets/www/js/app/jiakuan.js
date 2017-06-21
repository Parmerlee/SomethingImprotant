/******  
   zl 创建2016.8.22
  《家庭宽带》 专题 
******/

var jiakuan = function() {

  var  areafunc = function(){ }();
  var _this = this;

  var stime='';
  var sarea='';

	var testdata = 10;
  var tdate = '时间选择器';
  var tdatedata = '';
  
  var tarea = '地域';
  var tareacode='';

  var ttype = 'D';

	function testfunc(){
		   console.log( '测试数据' );
	}

    function tdata(){
        var jsonobj;
        J.showMask();

       httpsinterface.GetLocalData(function(version) {
    		jsonobj = eval(version);
        //J.Cache.save('key',jsonobj);
    		//console.log(jsonobj);
    		shandler( jsonobj );
	   });
    }

    function _setdate( sdate ){
        tdate  =  sdate;

        var timestr = tdate;
         if( timestr.length >=10 ){
           timestr = timestr.substring(0,4) + timestr.substring(5,7) + timestr.substring(8,10);
         }else{
           timestr = timestr.substring(0,4) +  timestr.substring(5,7);
         }
         console.log( 'tdatedata:' + timestr );

         tdatedata =  timestr;
    }

    function _setdatedata( sdatedata ){
         tdatedata = sdatedata ;
    }

    function _getdatedata(){
      return tdatedata;
    }

    function _setarea( sarea ){
        tarea  =  sarea;
    }

    function _getareacode(){
      return tareacode;
    }

    function _setareacode( sareacode ){
       tareacode =  sareacode;
    }

    var gareadata = function( sareaCode ,options ){
        console.log( sareaCode );
        _this.areafunc = $.extend({},options);
        var testdat = { areaName: sareaCode };
        var testdatastr = JSON.stringify( testdat );
        //console.log( testdatastr );
        //_this.testfunc.onSelect.call(_this,'测试adfasdfasdfasdfadfadfadfasdf');

       //J.showMask();
      //J.hideMask();
        J.showToast('正在加载数据...','loading',0);

     httpsinterface.GetJiakuanArea(testdatastr,function(dataobj) {
        jsonobj =  $.parseJSON( dataobj );
        //console.log( jsonobj );
        //J.Cache.save('areaCodelist',jsonobj );
        //J.hideMask();
        J.hideToast();
        _this.areafunc.onSelect.call(_this,jsonobj );
        //_this.settings.onSelect.call(_this,'测试');
     });
    }

    /*获取运端数据*/
    function gdata(){
       
        //J.showMask();
        J.showToast('正在加载数据...','loading',0);
        //J.showToast('数据正在加载...','success',5000);
        var jsonobj;

        //var testdat = "{\"a\":\"1\",\"b\":\"2\",\"c\":\"3\"}";
        var testdat = { paramtime:tdatedata,  paramarea:tareacode, pareamtype:ttype };
        var testdatastr = JSON.stringify( testdat );
        console.log( testdatastr );

        //var testdat = @"{\"name\":\"hanqing\",\"age\":\"23\"}"
     httpsinterface.GetJiakuanData(testdatastr,function(dataobj) {
        jsonobj =  $.parseJSON( dataobj );
        shandler( jsonobj );
     });

    }

    function tback(){
      httpsinterface.NativeGoBack();
    }

    function tshare(){
      httpsinterface.Html5Share();
    }

    function footershowz(){
         var html = '';   
         html += '<footer>\
                  <a  id ="ztime" href="#" data-target="popup" data-ids="1" data-area='
                  +tdate+ 
                  '><span class="fix_label">'
                  +tdate+ 
                  '</span>\
                  </a>\
                  <a  id ="zarea" href="#" data-target="popup" data-ids="2" data-area='
                  +tarea+ 
                  '><span class="fix_label">'
                  +tarea+
                  '</span>\
                  </a>\
                  </footer>'
         $('#jiakuan_article').append(html);

         J.Scroll('#jiakuan_article',{hScroll:true,hScrollbar : false});
    }

    function footerhidez(){
        //J.popup.close();
         $('#jiakuan_article footer').remove();
          J.Scroll('#jiakuan_article',{hScroll:true,hScrollbar : false});
    }

    /*新增list 
    */
    function listadd( dataobj, strname ){
     
     if( dataobj.length > 0 ){
         var html = '';
         html += '<li class="zdivider">\
                  <div class="grid right">\
                  <div class="col-1">'
                  +strname+
                  '</div>\
                  <div class="grid-fixwhitelabel">用户数</div>\
                  <div class="grid-fixwhitelabel">日涨幅</div>\
                  </div>\
                  </li>';
         $('#jiakuan_article ul.list').append(html);

      for(var i =0; i < dataobj.length; ++i  ){
                
            var colstr= '';
            var colstrcol = 'grid-fixredlabel';
            var colvalue =  dataobj[i].CD_COL;
            
            console.log( colvalue );

            if( colvalue == 'null'  || colvalue == null ){
                   colstr += '--';
                   colstrcol = 'grid-fixtnulredlabel';
            }else if(colvalue >= 0){
                   colstr +='+' + colvalue + '%';
                   colstrcol = 'grid-fixredlabel';
            }else{
               colstr += colvalue + '%';
               colstrcol = 'grid-fixgreenlabel';
            }   

            var curdayv='';
            var curunit='';
            var strfixlable = '';

            if( dataobj[i].CURDAY_VALUE =='null' || dataobj[i].CURDAY_VALUE == null ){
                curdayv += '--';
                strfixlable += 'grid-fixtnullabel';
            }else{
                //curdayv +=  dataobj[i].CURDAY_VALUE;

                curunit += dataunit( dataobj[i].CURDAY_VALUE, '户' );
                curdayv += dataform( dataobj[i].CURDAY_VALUE );
                
                strfixlable += 'grid-fixtwolabel';

            }
                var html='';
                html += '<li data-selected="selected" data-kpicode='+dataobj[i].KPI_CODE+' \
                data-kpiname='+dataobj[i].KPI_NAME+' \
                data-time='+tdate+' \
                data-tda='+tdatedata+' \
                data-area='+tarea+' \
                data-aarea='+tareacode+'> \
                 <a href="#" data-target="popup">\
                 <div class="grid ">\
                 <div class="col-z1" >' + dataobj[i].KPI_NAME + '</div>\
                 <div class="secondtag">'+ curunit +'</div>\
                 <div class="' + strfixlable + '" >' + curdayv + '</div>\
                 <div class="'+colstrcol+'">'+colstr+'</div></div></a></li>';
                $('#jiakuan_article ul.list').append(html);
          }//for

          J.Scroll('#jiakuan_article',{hScroll:true,hScrollbar : false});
        }//if
    }

    function shandler( dataobj ){

    if( dataobj.flag == undefined || dataobj.flag == "false" ){
        $('#jiakuan_article ul.list').empty();
          var html = '';
                html += '<li><div class="grid middle" style="height: 100%;border:0px solid #ee3207">\
                        <div style="margin: auto;border: 0px solid #00364d;padding: 0px;">今日数据暂未发布</div>\
                        </div></li>'
              $('#jiakuan_article ul.list').append(html);
              J.Scroll('#jiakuan_article', { hScroll: true,hScrollbar: false });
              console.log( dataobj.msg );
              //J.hideMask();
              J.hideToast();
              return ;
    }

    	console.log( dataobj );
                  
       //J.hideMask();

      //时间 地域  
      if( dataobj.time !=undefined ){

         var timestr = dataobj.time;
         if( timestr.length >=8 ){
           timestr = timestr.substring(0,4) + '-' + timestr.substring(4,6) +'-'+ timestr.substring(6,8);
         }else{
           timestr = timestr.substring(0,4) + '-' + timestr.substring(4,6);
         }
         console.log( timestr );

         tdate = timestr ;
         tdatedata = dataobj.time;

         //J.Cache.save('stime', timestr);
      }  

      if( dataobj.areaCode !=undefined ){
         var areastr = dataobj.areaCode;
         //console.log( areastr );

         tarea = areastr[0].AREA_NAME;
         tareacode = areastr[0].AREA_CODE;
      }  

       if( dataobj.data1.length <=0 && dataobj.data2.length <=0 && dataobj.data3.length <=0   ){

          $('#jiakuan_article ul.list').empty();

          var html = '';
          html += '<li><div class="grid middle" style="height: 100%;border:0px solid #ee3207">\
                   <div style="margin: auto;border: 0px solid #00364d;padding: 0px;">今日数据暂未发布</div>\
                   </div></li>'
          $('#jiakuan_article ul.list').append(html);
       }

       //var CurPageValue = [];

      if( dataobj.data1 !=undefined && dataobj.data1.length > 0 ){
          var objdata =  dataobj.data1;

          // for( var i in objdata){
          //    CurPageValue.push(objdata[i]);
          // }

          listadd(objdata,'到达用户数');
        }//if

      if( dataobj.data2 !=undefined && dataobj.data2.length > 0 ){
          var objdata =  dataobj.data2;

          // for( var i in objdata){
          //    CurPageValue.push(objdata[i]);
          // }

          listadd(objdata,'新装用户数');
        }//if

      if( dataobj.data3 !=undefined && dataobj.data3.length > 0 ){
          var objdata =  dataobj.data3;

          // for( var i in objdata){
          //    CurPageValue.push(objdata[i]);
          // }

          listadd(objdata,'净增用户数');
        }//if

      if( dataobj.data4 !=undefined && dataobj.data4.length > 0 ){
          var objdata =  dataobj.data4;

          // for( var i in objdata){
          //    CurPageValue.push(objdata[i]);
          // }

          listadd(objdata,'和TV');
        }//if

        //console.log( CurPageValue );
        //J.Cache.save('firstPageValue', CurPageValue );
        //J.hideMask();
        J.hideToast();
    }

    function timedata(){
       
        $('#jiakuan_article ul.list').empty();

         gdata();
    }

    function areadata(){

     $('#jiakuan_article ul.list').empty();

      gdata();

    }

    //数据格式化,保留2位小数
    function dataform( dataj ){

      var fuhao ='';
      if( dataj < 0 ){
        fuhao +='-';
      }

        var datan = Math.abs(Number( dataj ));
        var datastr='';

        if( datan > 100000000){
          //亿
            datan = datan/100000000;
            datan = datan.toFixed(2);
            datastr +=fuhao;
            datastr += String(datan);
            return datastr;
        }else if( datan > 10000 ){
          //万
            datan = datan/10000;
            datan = datan.toFixed(2);
            datastr +=fuhao;
            datastr += String(datan);
            return datastr;
        }else{
             datan = datan.toFixed(2);
             datastr +=fuhao;
             datastr += String(datan);
            return datastr;
        }
    }

    function dataunit( dataj,unit ){

        var datan = Math.abs(Number( dataj ));

        var datastr='';

        if( datan > 100000000){
          //亿
            datastr += '亿' +unit;
            return datastr;
        }else if( datan > 10000 ){
          //万
            datastr += '万'+unit;
            //console.log(datastr);
            return datastr;
        }else{
             datastr += unit;
            return datastr;
        }
    }

    function showtest(){

        console.log( ' 回调函数测试 ');
    }

	return {
		test: showtest,
    testdata: tdata,

    setdate: _setdate,
    setdatedata: _setdatedata,
    getdatedata: _getdatedata,
    setarea: _setarea,
    getareacode: _getareacode,
    setareacode: _setareacode,
    //showtimearea: timeareashow,
    //hidetimearea: timeareahide,

    showfooter: footershowz,
    hidefooter: footerhidez,
    getdatabytime:timedata,
    getdatabyarea:areadata,

    btnshare:tshare,
    btnback: tback,
    getdata: gdata,
    getareadata: gareadata 
	};

}();

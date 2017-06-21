/*
  地域排名
*/
var jiakuan3 = function(){

	var paramSettings = { sareaCode:'',sarea:'',sdataType:'D',stime:'',stimedata:'' };
  
  var jiantouflag1 = true;
  var jiantouflag2 = true;

	function _testdata(){

        var jsonobj;

        var testdat = { param: '1' };
        var testdatastr = JSON.stringify( testdat );

        httpsinterface.GetLocalData(testdatastr, function(dataobj) {
        jsonobj =  eval( dataobj );;
       
        //console.log(jsonobj);

        handler( jsonobj );
     });
	}

	function _getdata(){
		console.log( '数据调用' );
        jiantouflag1 = true;
		    var testdat = {   
                        areaCode: paramSettings.sareaCode, 
                        dataType: paramSettings.sdataType,
                        time: paramSettings.stimedata
                      };

        var testdatastr = JSON.stringify( testdat );
        console.log( testdatastr );

        var jsonobj;
        //J.showMask();
        J.showToast('正在加载数据...','loading',0);

        httpsinterface.GetJiakuanData3(testdatastr,function(dataobj) {
        jsonobj =  $.parseJSON( dataobj );;
        
        //console.log( jsonobj );

        handler( jsonobj );
     });
	}

	function handler( dataobj ){

      $('#jiakuan3page1').empty();
      $('#jiakuan3page2').empty();
      _hidefooter();

		if( dataobj == undefined || dataobj == null || dataobj == 'null'
          || dataobj.flag == 'false' || dataobj.sort == undefined || dataobj.sort == null || dataobj.sort == 'null' 
          || dataobj.sort.length <=0  ){

				var html = ' <ul class="list" >';
          			html += '<li data-ids="0" ><div class="grid middle" style="height: 100%;border:0px solid #ee3207">\
                   			<div style="margin: auto;border: 0px solid #00364d;padding: 0px;">今日数据暂未发布</div>\
                   			</div></li></ul>';

       				$('#jiakuan3_article').append(html);
       				J.Scroll('#jiakuan3_article', { hScroll: true,hScrollbar: false });
       				console.log( dataobj.msg );
       				//J.hideMask();
                    J.hideToast();
       				return ;
		}

		//时间+地域  解析
    if( dataobj.maxTime != undefined && dataobj.maxTime != null && dataobj.maxTime != 'null'){

      var timestr = dataobj.maxTime;

      if( paramSettings.sdataType  == 'D'){

        if (timestr.length >= 8) {
          timestr = timestr.substring(0, 4) + '-' + timestr.substring(4, 6) + '-' + timestr.substring(6, 8);
        } else {
          timestr = timestr.substring(0, 4) + '-' + timestr.substring(4, 6);
        }
          paramSettings.stime = timestr;
          paramSettings.stimedata = dataobj.maxTime;

      }else{

        if (timestr.length >= 6) {
          timestr = timestr.substring(0, 4) + '-' + timestr.substring(4, 6);
        }

          paramSettings.stime = timestr;
          paramSettings.stimedata = dataobj.maxTime;
      }

    }

		listadd( dataobj.sort );

		//console.log( $('#jiakuan3page1').html() );

		J.Scroll('#jiakuan3_article', { hScroll: true,hScrollbar: false });
		//J.hideMask();
        J.hideToast();
	}

  function myarr( dataobj ){
   
    for(var i =0; i < dataobj.length; ++i){
       if( dataobj[i].AREA_NAME == '湖北' ){
            var tdata = dataobj[i].orders;
            console.log( tdata );

           return tdata;
       }
    }
  }

	function listadd( dataobj ){

		console.log( dataobj );

    var tdata = dataobj[0].orders;

		if( tdata.length <= 6 ){

			var html='';
			html += '<li style="padding: 0px 0px 0px 0px;">\
                <div class="grid ztitle-grid">\
                   <div class="col-ztitle-left" style="width:10%;"></div>\
                <div class="col-ztitle-left" style="width:30%;" >宽带用户到达数</div>\
                <div class="col-ztitle-middle" style="width:30%;" >有线宽带到达数</div>\
                <div class="col-ztitle-right" style="width:30%;">广电宽带到达数</div>\
                </div></li>';
			$('#jiakuan3page1').append(html);

			var html2='';
			html2 += '<li style="padding: 0px 0px 0px 0px;">\
                <div class="grid ztitle-grid">\
                    <div class="col-ztitle-left" style="width:10%;">\
                        <i class="icon arrow-left-zl" style="color:#fff;height:15px;top:0px"></i></div>\
                <div class="col-ztitle-left" style="width:45%;" >CPE到达数</div>\
                <div class="col-ztitle-right" style="width:45%;" >集宽用户到达数</div>\
                </div></li>';
			$('#jiakuan3page2').append(html2);	
		}

		for (var i = 0; i < dataobj.length; ++i) {

			var tareaName = dataobj[i].AREA_NAME;
			
			if( i == 0 ){
				ordersadd(dataobj[i].orders,tareaName,'1' );
			}else{
				ordersadd(dataobj[i].orders,tareaName,'1' );
			}
		}//for 
		
	}

function ordersadd( dataorders,tareaName,flag ){
	for(var j=0,html='',html2=''; j < dataorders.length;++j ){

		  if( flag == '0' && j==0 ){
			 html +='<li><div class="areatag" >'+ tareaName +'</div>\
			 		<div class="tag" style="font-size:0.5em;background-color:#9f00a7">\
					<span>向左滑动查看更多内容</span>\
					<i class="icon arrow-right-2 right"></i>\
					</div>\
                    <div class="grid three-grid">';

              html2 +='<li><div class="areatag" >'+ tareaName +'</div>\
			 		<div class="tag" style="font-size:0.5em;background-color:#9f00a7">\
					<span>向右滑动查看更多内容</span>\
					<i class="icon arrow-left-2 left"></i>\
					</div>\
                    <div class="grid three-grid">';
		  }else if(flag == '1' && j==0 ){
			 html +='<li style="padding: 0px 0px 0px 0px;" >\
                    <div class="grid three-grid"><div class="col-1" style="width:10%;" >\
                    <div class="areatag" >'+ tareaName +'</div>\
                    </div>';
       html2 +='<li style="padding: 0px 0px 0px 0px;" >\
                    <div class="grid three-grid"><div class="col-1" style="width:10%;">\
                    <div class="areatag" >'+ tareaName +'</div>\
                    </div>';		      		
		 }

			//console.log( dataorders[j] );

				var dataob = dataorders[j];

				var sicon = '';
				var scolor ='';
				var svalue = '';
				var skpiname ='';
				var srank = '';
        var sunit ='';
				if( dataob.isRise == '-'){
					sicon += 'arrow-down-2 right';
					scolor += '#78ba00';
				}else if( dataob.isRise == '+' ){
					sicon += ' arrow-up-3 right';
					scolor += '#e74c3c';
				}else if( dataob.isRise == '=' ){
					sicon += ' minus right';
					scolor += '#E67E22';		
				}else{
          sicon += '';
          scolor += '#ECF0F1';
        }	
					skpiname += dataob.kpiName;

          if( dataob.ranking != undefined && dataob.ranking != null && dataob.ranking != 'null' ){
            srank += dataob.ranking;
          }else{
            srank += '--';
          }
					
          if( dataob.vlaue != undefined && dataob.vlaue != null && dataob.vlaue != 'null'  ){
  
                sunit +=dataunit(dataob.vlaue,'户');
                svalue += dataform(dataob.vlaue);
          }else{
                svalue += '--';
                sunit ='';
          }


			if( j < 3 ){

          var h = '';

        if( jiantouflag1 && j ==2  ){
            h += '<p class="icon arrow-right-zl" style="color:#2573fe"></p>'
            jiantouflag1 = false;
        }else{
            h='';
        }

				html +='<div class="col-1" style="width:30%">\
                        <div class="grid">\
                            <div class="col-1 left" ></div>\
                                  <div class="col-1 right" >\
                                      <i class="icon '+sicon+' right"  style="color: '+ scolor +'" ></i>\
                                      <span style="font-size: 0.5em;color:#2573fe">排名:</span>\
                                      <span style= "color:#000;" >'+srank+'</span>\
                                  </div>\
                            <div class="col-1 right" >'+h+'</div>\
                        </div>\
                            <p>\
                                <span style="color:#000">'+svalue+'</span>\
                                <span style="font-size: 0.5em;color:#2573fe">'+sunit+'</span>\
                            </p>\
                  </div>';


            }else {

				html2 +='<div class="col-1" style="width:45%">\
                        <div class="grid">\
                            <div class="col-1 left" ></div>\
                                  <div class="col-1 right" >\
                                      <i class="icon '+sicon+' right"  style="color: '+ scolor +'" ></i>\
                                      <span style="font-size: 0.5em;color:#2573fe">排名:</span>\
                                      <span style= "color:#000;" >'+srank+'</span>\
                                  </div>\
                            <div class="col-1 right" ></div>\
                        </div>\
                            <p>\
                                <span style="color:#000">'+svalue+'</span>\
                                <span style="font-size: 0.5em;color:#2573fe">'+sunit+'</span>\
                            </p>\
                        </div>';
            }    

             if( j == dataorders.length -1 ){
             	$('#jiakuan3page1').append(html);
             	$('#jiakuan3page2').append(html2);
             }

		}//for 
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

	function _btnshare(){
		httpsinterface.Html5Share();
	}

	function _btnback(){
		httpsinterface.NativeGoBack();
	}

	function _showfooter(){
         var html = ''; 
        	html += '<footer>\
                  <a  id ="jiakuan3time" href="#" data-target="popup" data-ids="1" >\
                  <span class="fix_label">'
                  + paramSettings.stime + 
                  '</span>\
                  </a>\
                  </footer>'
                  // <a  id ="jiakuan3area" href="#" data-target="popup" data-ids="2" >\
                  // <span class="fix_label">'
                  // + paramSettings.sarea +
                  // '</span>\
                  // </a>\
         $('#jiakuan3_article').append(html);
         J.Scroll('#jiakuan3_article',{hScroll:true,hScrollbar : false});
	}

	function _hidefooter(){
         $('#jiakuan3_article footer').remove();
          J.Scroll('#jiakuan3_article',{hScroll:true,hScrollbar : false});
	}


	 function _setdate( sdate ){

        var timestr = sdate;
         if( timestr.length >=10 ){
           timestr = timestr.substring(0,4) + timestr.substring(5,7) + timestr.substring(8,10);
         }else{
           timestr = timestr.substring(0,4) +  timestr.substring(5,7);
         }
         
         paramSettings.stime =  sdate;
         paramSettings.stimedata =  timestr;
    }

	function _getDflag(){
		return paramSettings.sdataType;
	}

	function _getdatedata(){
		return paramSettings.stimedata;
	}

	function _setarea( sarea ){
        paramSettings.sarea  =  sarea;
    }

    function _getareacode(){
      	return paramSettings.sareaCode;
    }

    function _setareacode( sareacode ){
       paramSettings.sareaCode =  sareacode;
    }

    function _setriyuedata( riyuedata ){
       paramSettings.sdataType =  riyuedata;
    }

  return {
  	getdata:_getdata,
  	testdata:_testdata,

  	btnback:_btnback,
  	btnshare:_btnshare,
  	showfooter:_showfooter,
  	hidefooter:_hidefooter,

  	getDflag: _getDflag,
    setdate: _setdate,
    getdatedata: _getdatedata,
    setarea: _setarea,
    getareacode: _getareacode,
    setareacode: _setareacode,
	  setriyuedata:_setriyuedata
  };

}();

/*
  地域排名
*/
var zhenqi3 = function(){

	var paramSettings = { sareaCode:'',sarea:'',sdataType:'D',stime:'',stimedata:'20160901' };

	function _testdata(){

        var jsonobj;

        var testdat = { param: '2' };
        var testdatastr = JSON.stringify( testdat );

        httpsinterface.GetLocalData(testdatastr, function(dataobj) {
        jsonobj =  eval( dataobj );;
       
        //console.log(jsonobj);

        handler( jsonobj );
     });
	}

	function _getdata(){
		console.log( '数据调用' );

		var testdat = {   
                        areaCode: paramSettings.sareaCode, 
                        dataType: paramSettings.sdataType,
                        time: paramSettings.stimedata
                      };

        var testdatastr = JSON.stringify( testdat );
        console.log( testdatastr );

        var jsonobj;
        J.showMask();

        httpsinterface.GetZhenqiData3(testdatastr,function(dataobj) {
        jsonobj =  $.parseJSON( dataobj );;
        
        //console.log( jsonobj );

        handler( jsonobj );
     });
	}

	function handler( dataobj ){

    console.log( dataobj );

    $('#zhenqi3page1').empty();
    $('#zhenqi3page2').empty();
    $('#zhenqi3page3').empty();
    $('#zhenqi3page4').empty();

		if( dataobj == undefined || dataobj.flag == 'false' || dataobj.sort == undefined || dataobj.sort.length <=0 ){

				var html = ' <ul class="list" >';
          			html += '<li><div class="grid middle" style="height: 100%;border:0px solid #ee3207">\
                   			<div style="margin: auto;border: 0px solid #00364d;padding: 0px;">今日数据暂未发布</div>\
                   			</div></li></ul>';

       				$('#zhenqi3_article').append(html);
       				J.Scroll('#zhenqi3_article', { hScroll: true,hScrollbar: false });
       				console.log( dataobj.msg );
       				J.hideMask();
       				return ;
		}

		//时间+地域  解析
    if( dataobj.maxTime != undefined ){

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

		//console.log( $('#zhenqi3page1').html() );

		J.Scroll('#zhenqi3_article', { hScroll: true,hScrollbar: false });
		J.hideMask();
	}

	function listadd( dataobj ){

		//console.log( dataobj );
		var tdata = dataobj[0].orders;
		if( tdata.length < 13 ){

			var html='';
			html += '<li class="ztitledivider">\
                <div class="grid ztitle-grid">\
                <div class="col-ztitle-left">'+ tdata[0].kpiName +'</div>\
                <div class="col-ztitle-middle">'+ tdata[1].kpiName +'</div>\
                <div class="col-ztitle-right">'+ tdata[2].kpiName +'</div>\
                </div></li>'
			$('#zhenqi3page1').append(html);

      var html2='';
      html2 += '<li class="ztitledivider">\
                <div class="grid ztitle-grid">\
                <div class="col-ztitle-left">'+ tdata[3].kpiName +'</div>\
                <div class="col-ztitle-middle">'+ tdata[4].kpiName +'</div>\
                <div class="col-ztitle-right">'+ tdata[5].kpiName +'</div>\
                </div></li>'
      $('#zhenqi3page2').append(html2);

      var html3='';
      html3 += '<li class="ztitledivider">\
                <div class="grid ztitle-grid">\
                <div class="col-ztitle-left">'+ tdata[6].kpiName +'</div>\
                <div class="col-ztitle-middle">'+ tdata[7].kpiName +'</div>\
                <div class="col-ztitle-right">'+ tdata[8].kpiName +'</div>\
                </div></li>'
      $('#zhenqi3page3').append(html3);

			var html4='';
			html4 += '<li class="ztitledivider">\
                <div class="grid ztitle-grid">\
                 <div class="col-ztitle-middle">'+ tdata[9].kpiName +'</div>\
                </div></li>'
			$('#zhenqi3page4').append(html4);	
		}

		for (var i = 0; i < dataobj.length; ++i) {

			var tareaName = dataobj[i].AREA_NAME;
			
			if( i == 0 ){
				ordersadd(dataobj[i].orders,tareaName,'0' );
			}else{
				ordersadd(dataobj[i].orders,tareaName,'1' );
			}
		}//for 
		
	}

function ordersadd( dataorders,tareaName,flag ){
	for(var j=0,html='',html2='',html3='',html4=''; j < dataorders.length;++j ){

		  if( flag == '0' && j==0 ){
			 html +='<li><div class="areatag" >'+ tareaName +'</div>\
			 		<div class="tag" style="font-size:0.5em;background-color:#9f00a7">\
					<span>向左滑动查看更多内容</span>\
					<i class="icon arrow-right-2 right"></i>\
					</div>\
          <div class="grid three-grid">';

       html2 +='<li><div class="areatag" >'+ tareaName +'</div>\
          <div class="tag" style="font-size:0.5em;background-color:#9f00a7">\
          <i class="icon arrow-left-2 left"></i>\
          <span>滑动查看更多内容</span>\
          <i class="icon arrow-right-2 right"></i>\
          </div>\
          <div class="grid three-grid">';

       html3 +='<li><div class="areatag" >'+ tareaName +'</div>\
          <div class="tag" style="font-size:0.5em;background-color:#9f00a7">\
          <i class="icon arrow-left-2 left"></i>\
          <span>滑动查看更多内容</span>\
          <i class="icon arrow-right-2 right"></i>\
          </div>\
          <div class="grid three-grid">';

       html4 +='<li><div class="areatag" >'+ tareaName +'</div>\
			 		<div class="tag" style="font-size:0.5em;background-color:#9f00a7">\
					<span>向右滑动查看更多内容</span>\
					<i class="icon arrow-left-2 left"></i>\
					</div>\
                    <div class="grid three-grid">';
		  }else if(flag == '1' && j==0 ){
			       html +='<li><div class="areatag" >'+ tareaName +'</div>\
                    <div class="grid three-grid">';
             html2 +='<li><div class="areatag" >'+ tareaName +'</div>\
                    <div class="grid three-grid">';
             html3 +='<li><div class="areatag" >'+ tareaName +'</div>\
                    <div class="grid three-grid">'; 
             html4 +='<li><div class="areatag" >'+ tareaName +'</div>\
                    <div class="grid three-grid">';         		      		
		 }

			//console.log( dataorders[j] );

				var dataob = dataorders[j];

				var sicon = '';
				var scolor ='';
				var svalue = '';
				var skpiname ='';
				var srank = '';
				if( dataob.isRise == '-'){
					sicon += 'arrow-down-2 right';
					scolor += '#78ba00';
				}else if( dataob.isRise == '+' ){
					sicon += ' arrow-up-3 right';
					scolor += '#e74c3c';
				}else{
					sicon += ' minus right';
					scolor += '#E67E22';		
				}	
					skpiname += dataob.kpiName;
					srank += dataob.ranking;

					var sunit ='';
          svalue += dataform(dataob.vlaue);

          if(j==0 || j==2){
            sunit +=dataunit(dataob.vlaue,'户');
          }else if( 1 == j || 3==j || 5 == j || 6 ==j || 7 ==j || 8 ==j || 9 ==j){
            sunit +=dataunit(dataob.vlaue,'元');
          }else if( 4 == j){
            sunit +=dataunit(dataob.vlaue,'%');
            svalue = dataform(dataob.vlaue*100);
          }

			if( j < 3 ){
				html +='<div class="col-1" style="width:33%">\
                     		<p style="color: '+ scolor +'">\
                     			<span class="zlabel">排名:</span>\
                     			<span class="label" style="background-color: '+ scolor +';">'+srank+'</span>\
                     			<i class="icon '+sicon+'"></i>\
                     		</p>\
                            <p>\
                                <span style="color:#000">'+svalue+'</span>\
                                <span style="font-size: 0.5em;color:#2573fe">'+sunit+'</span>\
                            </p>\
                        </div>';
            }else if( j< 6 ){
        html2 +='<div class="col-1" style="width:33%">\
                        <p style="color: '+ scolor +'">\
                          <span class="zlabel">排名:</span>\
                          <span class="label" style="background-color: '+ scolor +';">'+srank+'</span>\
                          <i class="icon '+sicon+'"></i>\
                        </p>\
                            <p>\
                                <span style="color:#000">'+svalue+'</span>\
                                <span style="font-size: 0.5em;color:#2573fe">'+sunit+'</span>\
                            </p>\
                        </div>';
            }else if( j< 9){
        html3 +='<div class="col-1" style="width:33%">\
                        <p style="color: '+ scolor +'">\
                          <span class="zlabel">排名:</span>\
                          <span class="label" style="background-color: '+ scolor +';">'+srank+'</span>\
                          <i class="icon '+sicon+'"></i>\
                        </p>\
                            <p>\
                                <span style="color:#000">'+svalue+'</span>\
                                <span style="font-size: 0.5em;color:#2573fe">'+sunit+'</span>\
                            </p>\
                        </div>';
            }else{
				        html4 +='<div class="col-1" style="width:33%">\
                     		<p style="color: '+ scolor +'">\
                     			<span class="zlabel">排名:</span>\
                     			<span class="label" style="background-color: '+ scolor +';">'+srank+'</span>\
                     			<i class="icon '+sicon+'"></i>\
                     		</p>\
                            <p>\
                                <span style="color:#000">'+svalue+'</span>\
                                <span style="font-size: 0.5em;color:#2573fe">'+sunit+'</span>\
                            </p>\
                        </div>';
            }    

             if( j == dataorders.length -1 ){
             	$('#zhenqi3page1').append(html);
             	$('#zhenqi3page2').append(html2);
              $('#zhenqi3page3').append(html3);
              $('#zhenqi3page4').append(html4);
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
                  <a  id ="zhenqi3time" href="#" data-target="popup" data-ids="1" >\
                  <span class="fix_label">'
                  + paramSettings.stime + 
                  '</span>\
                  </a>\
                  </footer>'
                  // <a  id ="zhenqi3area" href="#" data-target="popup" data-ids="2" >\
                  // <span class="fix_label">'
                  // + paramSettings.sarea +
                  // '</span>\
                  // </a>\
         $('#zhenqi3_article').append(html);
         J.Scroll('#zhenqi3_article',{hScroll:true,hScrollbar : false});
	}

	function _hidefooter(){
         $('#zhenqi3_article footer').remove();
          J.Scroll('#zhenqi3_article',{hScroll:true,hScrollbar : false});
	}


	 function _setdate( sdate ){

        var timestr = sdate;
         if( timestr.length >=10 ){
           timestr = timestr.substring(0,4) + timestr.substring(5,7) + timestr.substring(8,10);
         }else{
           timestr = timestr.substring(0,4) +  timestr.substring(5,7);
         }
         
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
	setriyuedata:_setriyuedata,
  };

}();
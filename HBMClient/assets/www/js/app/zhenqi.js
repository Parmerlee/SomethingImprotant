
/*zl create*/

var zhenqi = function(){

  var paramSettings = { 
  		tdate:'时间选择器',
  		tdatedata:'',
  		tMDFlag:'D',
  		tarea:'地域选择器',
  		tareacode:'',
  };

	function tdata(){
		var jsonobj;
		//J.showMask();
        J.showToast('正在加载数据...','loading',0);
        
		httpsinterface.GetLocalData( function(dataobj){

			jsonobj =  eval(dataobj);//$.parseJSON( dataobj );
			//console.log( jsonobj );
			handler( jsonobj );
		});
	}

	function _rgetdata(){
/*
        if( paramSettings.tMDFlag == 'D' ){
                $('#btn_zhenqiriyue').attr('class','icon icon-btn_ri');
        }else{
                $('#btn_zhenqiriyue').attr('class','icon icon-btn_yue');              
        }
        */
		//gdata();
		//J.Scroll('#zhenqi_section',{hScroll:true,hScrollbar : false});
	}

	function gdata(){
		// J.showMask();
        J.showToast('正在加载数据...','loading',0);
        var jsonobj;

        //var testdat = "{\"a\":\"1\",\"b\":\"2\",\"c\":\"3\"}";
        var testdat = { paramtime:paramSettings.tdatedata,  paramarea:paramSettings.tareacode, pareamtype:paramSettings.tMDFlag };
        var testdatastr = JSON.stringify( testdat );
        console.log( testdatastr );

        //var testdat = @"{\"name\":\"hanqing\",\"age\":\"23\"}"
     	httpsinterface.GetZhenqiData(testdatastr,function(dataobj) {
        	jsonobj =  $.parseJSON( dataobj );
        	handler( jsonobj );
     	});
	}

	function _initdaydata(){
		paramSettings.tdatedata = '';
		paramSettings.tareacode = '';
		paramSettings.tMDFlag = 'D';
		paramSettings.tdate = '--';
		paramSettings.tarea = '--';
	}

	function _inityuedata(){
		paramSettings.tdatedata = '';
		paramSettings.tareacode = '';
		paramSettings.tMDFlag = 'M';
		paramSettings.tdate = '--';
		paramSettings.tarea = '--';

	}

    function _setdate( sdate ){

        var timestr = sdate;
         if( timestr.length >=10 ){
           timestr = timestr.substring(0,4) + timestr.substring(5,7) + timestr.substring(8,10);
         }else{
           timestr = timestr.substring(0,4) +  timestr.substring(5,7);
         }
         console.log( 'tdatedata:' + timestr );

         paramSettings.tdate =  sdate;
         paramSettings.tdatedata =  timestr;
    }

	function _setdatedata( sdatedate ){
		 paramSettings.tdatedata = sdatedate;
	}

	function _getDflag(){
		return paramSettings.tMDFlag;
	}

	function _setDflag( flag ){
		 paramSettings.tMDFlag = flag;
	}

	function _getdatedata(){
		return paramSettings.tdatedata;
	}

	function _setarea( sarea ){
        paramSettings.tarea  =  sarea;
    }

    function _getareacode(){
      	return paramSettings.tareacode;
    }

    function _setareacode( sareacode ){
       paramSettings.tareacode =  sareacode;
    }

	function tshare(){
		httpsinterface.Html5Share();
	}

	function tback(){
		httpsinterface.NativeGoBack();
	}

	function footershowz(){
         var html = ''; 
        	html += '<footer>\
                  <a  id ="zhenqitime" href="#" data-target="popup" data-ids="1" data-area='
                  + paramSettings.tdate + 
                  '><span class="fix_label">'
                  + paramSettings.tdate + 
                  '</span>\
                  </a>\
                  <a  id ="zhenqiarea" href="#" data-target="popup" data-ids="2" data-area='
                  + paramSettings.tarea + 
                  '><span class="fix_label">'
                  + paramSettings.tarea +
                  '</span>\
                  </a>\
                  </footer>'
             
         $('#zhenqi_article').append(html);

         J.Scroll('#zhenqi_article',{hScroll:true,hScrollbar : false});
	}

	function footerhidez(){
         $('#zhenqi_article footer').remove();
          J.Scroll('#zhenqi_article',{hScroll:true,hScrollbar : false});
	}

	function handler( dataobj ){
		console.log( dataobj );
		$('#zhenqi_article ul.list').empty();
		
		if( paramSettings.tMDFlag == 'D' ){
              $('#btn_zhenqiriyue').attr('class','icon icon-btn_ri');
          }else{
               $('#btn_zhenqiriyue').attr('class','icon icon-btn_yue');
                    
         }
			J.Scroll('#zhenqi3_section',{hScroll:true,hScrollbar : false});

		if( dataobj.flag == undefined || dataobj.flag == "false" ){

				$('#zhenqi_article ul.list').empty();

			    var html = '';
          			html += '<li><div class="grid middle" style="height: 100%;border:0px solid #ee3207">\
                   			<div style="margin: auto;border: 0px solid #00364d;padding: 0px;">今日数据暂未发布</div>\
                   			</div></li>'
       				$('#zhenqi_article ul.list').append(html);
       				J.Scroll('#zhenqi_article', { hScroll: true,hScrollbar: false });
       				console.log( dataobj.msg );

       				//J.hideMask();
                    J.hideToast();
       				return ;
		}
		//时间
		if( dataobj.time != undefined ){
			var timestr = dataobj.time;

			if( paramSettings.tMDFlag  == 'D'){

				if (timestr.length >= 8) {
					timestr = timestr.substring(0, 4) + '-' + timestr.substring(4, 6) + '-' + timestr.substring(6, 8);
				} else {
					timestr = timestr.substring(0, 4) + '-' + timestr.substring(4, 6);
				}
					paramSettings.tdate = timestr;
					paramSettings.tdatedata = dataobj.time;

				console.log( paramSettings.tdate );
				console.log( paramSettings.tdatedata );
			}else{
				if (timestr.length >= 6) {
					timestr = timestr.substring(0, 4) + '-' + timestr.substring(4, 6);
				}

					paramSettings.tdate = timestr;
					paramSettings.tdatedata = dataobj.time;
			}

		}
		//地域
		if( dataobj.areaCode !=undefined ){

			var areastr = dataobj.areaCode;
         	paramSettings.tarea = areastr[0].AREA_NAME;
         	paramSettings.tareacode = areastr[0].AREA_CODE;
		}

		//无数据
       if( dataobj.data1.length <=0 && dataobj.data2.length <=0 && dataobj.data3.length <=0 && dataobj.data4.length <=0  ){

       		$('#zhenqi_article ul.list').empty();
       		
          var html = '';
          html += '<li><div class="grid middle" style="height: 100%;border:0px solid #ee3207">\
                   <div style="margin: auto;border: 0px solid #00364d;padding: 0px;">今日数据暂未发布</div>\
                   </div></li>'
          $('#zhenqi_article ul.list').append(html);
          //J.hideMask();
          J.hideToast();
          return;
       }

		//到达数
		if( dataobj.data1 !=undefined && dataobj.data1.length > 0 ){
				var objdata =  dataobj.data1;
				listadd(objdata, '到达数','用户数','户');
		}

		//总体收入类
		if( dataobj.data2 !=undefined && dataobj.data2.length > 0 ){
				var objdata =  dataobj.data2;
				listadd(objdata, '总体收入类','收入','元');
		}

		//使用情况类
		if( dataobj.data3 !=undefined && dataobj.data3.length > 0 ){
				var objdata =  dataobj.data3;
				listadd(objdata, '使用情况类','百分比','%');

		}

		//产品分类（只展示收入）
		if( dataobj.data4 !=undefined && dataobj.data4.length > 0 ){
				var objdata =  dataobj.data4;
				listadd(objdata, '产品分类（只展示收入）','收入','元');
				// if( paramSettings.tMDFlag == 'D' ){
				// 	listadd(objdata, '产品分类（只展示收入）','收入');
				// }else{
				// 	listaddmonth(objdata, '产品分类（只展示收入）','收入');
				// }
		}

			//J.hideMask();
            J.hideToast();
		}//handler	

		function listadd( dataobj, strname, strname2,unit ){
     
			if (dataobj.length > 0) {
				var html = '';

				var Dfstr = '涨幅';
				if( paramSettings.tMDFlag == 'D'){
					Dfstr = '日涨幅';
				}else{
					Dfstr = '月涨幅';
				}

				html += '<li class="zdivider"><div class="grid right">\
                  <div class="col-1">' + strname + '</div>\
                  <div class="grid-fixwhitelabel">' +strname2 + '</div>\
                  <div class="grid-fixwhitelabel">' +Dfstr+ '</div>\
                  </div></li>';
				$('#zhenqi_article ul.list').append(html);

				for (var i = 0; i < dataobj.length; ++i) {

					var colstr = '';
					var colstrcol = 'grid-fixredlabel';
					//var colvalue = dataobj[i].CM_COL;
					var colvalue;

				if( paramSettings.tMDFlag == 'M'){
					colvalue = dataobj[i].CM_COL;
				}else{
					colvalue = dataobj[i].CD_COL;
				}

					//console.log( '--' );
					//console.log( colvalue );
					//console.log( '--' );

					if( colvalue == 'null' || colvalue == undefined  || colvalue == null){
						colstr += '--';
						colstrcol = 'grid-fixtnulredlabel';
					}else if (colvalue >= 0) {
						colstr += '+' + colvalue + '%';
						colstrcol = 'grid-fixredlabel';
					} else {
						colstr += colvalue + '%';
						colstrcol = 'grid-fixgreenlabel';
					}

					var strcurmonva = '' ;
					var strfixlable = '';
					var strunit ='';

					if( dataobj[i].CURRENT_VAL ==undefined || dataobj[i].CURRENT_VAL =='null' || dataobj[i].CURRENT_VAL ==null){
						strcurmonva += '--';
						strfixlable += 'grid-fixtnullabel';
					}else{

						if( unit == '%'){
								strunit +=dataunit(dataobj[i].CURRENT_VAL,unit);
								var dal = 100*dataobj[i].CURRENT_VAL;
								strcurmonva += dataform( dal );
						}else{
							strunit +=dataunit(dataobj[i].CURRENT_VAL,unit);
							strcurmonva += dataform(dataobj[i].CURRENT_VAL);
						}

						strfixlable += 'grid-fixtwolabel';
					}

					// console.log( '--' );
					// console.log( strcurmonva );
					// console.log( '--' );

					var html = '';
					html += '<li data-selected="selected" \
							data-kpicode=' + dataobj[i].KPI_CODE + ' \
                			data-kpiname=' + dataobj[i].KPI_NAME + ' \
                			data-time=' + paramSettings.tdate  + ' \
                			data-tda=' + paramSettings.tdatedata + ' \
                			data-area=' + paramSettings.tarea + ' \
                			data-unit=' + unit +'\
                			data-aarea=' + paramSettings.tareacode + '> \
                			<a href="#" data-target="popup">\
                 			<div class="grid ">\
                 			<div class="col-z1" >' + dataobj[i].KPI_NAME +'</div>\
                 			<div class="secondtag">'+ strunit +'</div>\
                 			<div class="' + strfixlable + '">' + strcurmonva +'</div>\
                 			<div class="' + colstrcol + '">' + colstr +'</div></div></a></li>';
					$('#zhenqi_article ul.list').append(html);
				} //for
				J.Scroll('#zhenqi_article', { hScroll: true,hScrollbar: false });
			} //if
		}

	function timeareadata(){
        $('#zhenqi_article ul.list').empty();
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

        //console.log(datan);

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

	return{
		 testdata:tdata,

		 btnshare:tshare,
     	 btnback: tback,
     	 getdata: gdata,
     	 rgetdata: _rgetdata,

     	 getDflag: _getDflag,
     	 setDflag: _setDflag,
    	 setdate: _setdate,
    	 setdatedata: _setdatedata,
    	 getdatedata: _getdatedata,
    	 setarea: _setarea,
    	 getareacode: _getareacode,
    	 setareacode: _setareacode,

		 initdaydata: _initdaydata,
		 inityuedata: _inityuedata,
    	 gettimeareadata: timeareadata,

     	 showfooter: footershowz,
     	 hidefooter: footerhidez
	};
}();

























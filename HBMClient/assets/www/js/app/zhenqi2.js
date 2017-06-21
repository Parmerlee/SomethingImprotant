
/*zl create*/

var zhenqi2 = function(){

	var paramSettings = { skpicode:'',skpiname:'',stime:'',stimedata:'',sarea:'',sareaname:'',dateType:'',sshowType:'',areashowType:'',srowNumber:'',sunit:''};

  var paramchartlabel ='';
  var paramchart2label ='';

  var minvalue = 0;
  var maxvalue = 0;

  function _zinit( objdata ){
    console.log( objdata );

    paramSettings.skpicode = objdata.kpicode;
    paramSettings.skpiname = objdata.kpiname;
    paramSettings.stime = objdata.time;
    paramSettings.stimedata = objdata.timeform;

    paramSettings.sarea = objdata.areacode;
    paramSettings.sareaname = objdata.areaname;

    paramSettings.sdateType = objdata.dateType;
    paramSettings.srowNumber = objdata.number;
    paramSettings.sunit = objdata.unit;

    paramchartlabel = paramSettings.skpiname  + '(' + '单位:' + paramSettings.sunit +')';

   }
  function _setdate( sdate ){

        paramSettings.stimedata  =  sdate;

        var timestr = sdate;
         if( timestr.length >=10 ){
           timestr = timestr.substring(0,4) + timestr.substring(5,7) + timestr.substring(8,10);
         }else{
           timestr = timestr.substring(0,4) +  timestr.substring(5,7);
         }
         //console.log( 'tdatedata:' + timestr );

         paramSettings.stime =  timestr;
  }

	function _getDflag(){
		return paramSettings.sdateType;
	}

    function _getdatedata(){
        return paramSettings.stime;
    }

    function _setarea( sarea ){

        paramSettings.sareaname = sarea;
    }

    function _getareacode(){
      return paramSettings.sarea;
    }

    function _setareacode( sareacode ){
  
       paramSettings.sarea = sareacode;
    }

  function _setchart1label( tshowType){
    paramchartlabel = tshowType;
  }

  function _settitle(){
    $('#zhenqi2_section header h3.title').text( paramSettings.skpiname );
  }

  function _setchart2label( tshowType){
    paramchart2label = tshowType;
  }

  function _setchart1( tshowType){
    paramSettings.sshowType = tshowType;
  }

  function _setchart2( tareashowType){
    paramSettings.sareashowType = tareashowType;
  }

  function tdata(){
       var jsonobj;
       //J.showMask();
       J.showToast('正在加载数据...','loading',0);
      
       httpsinterface.GetLocalData(function(version) {
    		jsonobj = eval(version);
    		//console.log(jsonobj);
    		shandler( jsonobj );
	   });
  }

  function gdata(){
        console.log( '数据调用' );
        // var stime ='';

        // if(paramSettings.sdateType == 'M'){
        //    stime += paramSettings.stime +'01';
        // }else{
        //   stime += paramSettings.stime;
        // }

        var testdat = { kpiCode:  paramSettings.skpicode,  
                        areaCode: paramSettings.sarea, 
                        dateType: paramSettings.sdateType,
                        time: paramSettings.stime,
                        rowNumber:  paramSettings.srowNumber
                      };

        var testdatastr = JSON.stringify( testdat );
        console.log( testdatastr );

        var jsonobj;
        //J.showMask();
        J.showToast('正在加载数据...','loading',0);

        httpsinterface.GetZhenqiData2(testdatastr,function(dataobj) {
        jsonobj =  $.parseJSON( dataobj );;
        shandler( jsonobj );
     });
  }

    function shandler( dataobj ){

      console.log( dataobj );
      //J.hideMask();

      if(dataobj ==undefined || dataobj ==null || dataobj =='null'   || dataobj.flag == 'false'  ){
           $('#zhenqiname1').text( '--' );
           //$('#zhenqiname2').text( '--' );
           //$('#zhenqiname3').text( '--' );
           //$('#zhenqiname4').text( '--' );
           $('#zhenqiname5').text( '--' );
           $('#zhenqiname6').text( '--' );
           $('#zhenqiname7').text( '--' );

           $('#zhenqivalue0').text( '--'  );
           $('#zhenqivalue1').text( '--' );
           //$('#zhenqivalue2').text( '--' );
           //$('#zhenqivalue3').text( '--' );
           //$('#zhenqivalue4').text( '--' );
           $('#zhenqivalue5').text('--');
           $('#zhenqivalue6').text('--');
           $('#zhenqivalue7').text('--');

          $('#zhenqirank5').text( '--' );
          $('#zhenqirank6').text( '--' );
          $('#zhenqirank7').text( '--' );


           $('#zhenqiname8').text( '--' );
           $('#zhenqiname9').text( '--' );
           $('#zhenqiname10').text( '--' );

           $('#zhenqivalue8').text('--');
           $('#zhenqivalue9').text('--');
           $('#zhenqivalue10').text('--');

          $('#zhenqirank8').text( '--' );
          $('#zhenqirank9').text( '--' );
          $('#zhenqirank10').text( '--' );
          $('#zhenqiico').addClass('icon ');
          
           $('#zhenqidiv1').empty();
           $('#zhenqidiv2').empty();

           _settitle();
           J.Scroll('#zhenqi2_article',{hScroll:true,hScrollbar : false});

           $('#zhenqizbf').empty();
           //J.hideMask();
           J.hideToast();
           return;
      }

      if( dataobj != undefined && dataobj != 'null' && dataobj != null ){
		//排名
        var zarry2 = dataobj.ranking
        if( zarry2 != undefined ){

            if( zarry2[0] != undefined && zarry2[0] != 'null' && zarry2[0] != null ){
                $('#zhenqivalue1').text( zarry2[0].NUM );
            }else{
                $('#zhenqivalue1').text( '--' );
            }

            if( zarry2[1] != undefined && zarry2[1] != null && zarry2[1] != 'null' ){

              console.log( zarry2[1]  );

              if( zarry2[1].upOrDown == 'EQUAL' ){
                 $('#zhenqiico').removeClass('icon arrow-up-3');
                 $('#zhenqiico').removeClass('icon arrow-down-2');
                  $('#zhenqiico').addClass('icon minus');
                  $('#zhenqiico').attr('style','color: #3498db');
              }else if( zarry2[1].upOrDown == 'UP' ){
                 $('#zhenqiico').removeClass('icon minus');
                 $('#zhenqiico').removeClass('icon arrow-down-2');
                  $('#zhenqiico').addClass('icon arrow-up-3');
                  $('#zhenqiico').attr('style','color: #e74c3c');
              }else if( zarry2[1].upOrDown == 'DOWN' ){
                 $('#zhenqiico').removeClass('icon arrow-up-3');
                 $('#zhenqiico').removeClass('icon minus');
                  $('#zhenqiico').addClass('icon arrow-down-2');
                  $('#zhenqiico').attr('style','color: #78ba00');
              }
            }
        }else{
          $('#zhenqivalue1').text( '--' );
        }

        //指标分析
        var zarry = dataobj.detailValue
        if( zarry[0] != undefined && zarry[0] != 'null' && zarry[0] != null ){

          //当日值	
          $('#zhenqiname1').text( paramSettings.skpiname );

          var lenstr = String(zarry[0].CURRENT_VAL);

          if (lenstr == 'null') {
            $('#zhenqivalue0').text('--');
          } else{
          
            var dotl = lenstr.indexOf('.');

             if(dotl <0 ){

          if (lenstr.length > 6) {
                var len = lenstr.length;
                var stru0 = lenstr.substring(len - 3, len);
                var stru1 = lenstr.substring(len - 6, len - 3);
                var stru2 = lenstr.substring(0, len - 6);
            var str = stru2 + ',' + stru1 + ',' + stru0;
            $('#zhenqivalue0').text(str);

          } else if (lenstr.length > 3) {
            var len = lenstr.length;
            var stru0 = lenstr.substring(len - 3, len);
            var stru2 = lenstr.substring(0, len - 3);
            var str = stru2 + ',' + stru0;
            $('#zhenqivalue0').text(str);

          } else {
            $('#zhenqivalue0').text(zarry[0].CURRENT_VAL);
          }

             }else{
                $('#zhenqivalue0').text(zarry[0].CURRENT_VAL);
             }
          }//else end 


		   //环比值，同比值
		   $('#zhenqizbf').empty();

//CM_MYOY  月同比   CM_YYOY  年同比  CURRENT_VAL  当日值  CURMONTH_VALUE  日累计值   CM_COL日环比   CD_MYOY 月同比

       //指标排名
       if( paramSettings.sdateType =='D' ){
            var strname;
            var colvalue; 
            var html = '';
            var colstr= '';

            colvalue = zarry[0].CM_COL;
            strname = '日环比';
            
              if( colvalue == 'null' || colvalue == undefined || colvalue == null ){
                  colstr +='--';
                     html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#7f8c8d">' + colstr + '</div>\
                    </div>';

              }else if(colvalue >= 0){
                    colstr +='+' + colvalue + '%';
                     html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#e74c3c">' + colstr + '</div>\
                    </div>';

              }else{
                  colstr += colvalue + '%';
                   html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#78ba00">' + colstr + '</div>\
                    </div>';
              } 

            colvalue = zarry[0].CD_MYOY;
            strname = '月同比';
            colstr='';
              if( colvalue == 'null' || colvalue == undefined || colvalue == null ){
                  colstr +='--';
                     html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#7f8c8d">' + colstr + '</div>\
                    </div>';

              }else if(colvalue >= 0){
                    colstr +='+' + colvalue + '%';
                     html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#e74c3c">' + colstr + '</div>\
                    </div>';

              }else{
                  colstr += colvalue + '%';
                   html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#78ba00">' + colstr + '</div>\
                    </div>';
              } 

            colvalue = zarry[0].CURMONTH_VALUE;
            strname = '日累计';
            colstr='';
              if( colvalue == 'null' || colvalue == undefined || colvalue == null ){
                  colstr +='--';
                     html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#7f8c8d">' + colstr + '</div>\
                    </div>';

              }else{

                var lenstr = String( colvalue );
                var colvaluestr=''; 
              
                if( lenstr == 'null'){
                    //$('#zhenqivalue0').text( '--' );
                    colvaluestr +='--';
                }else{

                    var dotl = lenstr.indexOf('.');

                if(dotl <0 ){

                    if( lenstr.length > 6){
                      var len = lenstr.length;
                      var stru0 = lenstr.substring(len-3,len);
                      var stru1 = lenstr.substring(len-6,len-3);
                      var stru2 = lenstr.substring(0,len-6);
                      var str = stru2 + ',' + stru1 + ',' + stru0;
                      colvaluestr += str;
                      //$('#zhenqivalue0').text( str );

                    }else if( lenstr.length > 3){
                      var len = lenstr.length;
                      var stru0 = lenstr.substring(len-3,len);
                      var stru2 = lenstr.substring(0,len-3);
                      var str = stru2  + ',' + stru0;
                     //$('#zhenqivalue0').text( str );
                      colvaluestr += str;

                    }else{
                   // $('#zhenqivalue0').text( zarry[0].CURRENT_VAL );
                     colvaluestr += colvalue;
                    }

                }else{
                        colvaluestr += colvalue;
                }

                }
                    colstr += colvaluestr ;
                     html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#e74c3c">' + colstr + '</div>\
                    </div>';
            }

            colvalue = zarry[0].CM_MYOY;
            strname = '日累计月同比';
            colstr='';
              if( colvalue == 'null' || colvalue == undefined || colvalue == null ){
                  colstr +='--';
                     html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#7f8c8d">' + colstr + '</div>\
                    </div>';

              }else if(colvalue >= 0){
                  colvalue =  Number(colvalue)*100;
                  colvalue = Number(colvalue).toFixed(2);

                    colstr +='+' + colvalue + '%';
                     html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#e74c3c">' + colstr + '</div>\
                    </div>';

              }else{
                  colvalue =  Number(colvalue)*100;
                  colvalue = Number(colvalue).toFixed(2);

                  colstr += colvalue + '%';
                   html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#78ba00">' + colstr + '</div>\
                    </div>';
              } 

            colvalue = zarry[0].CM_YYOY;
            strname = '日累计年同比';
            colstr='';
              if( colvalue == 'null' || colvalue == undefined || colvalue == null ){
                  colstr +='--';
                     html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#7f8c8d">' + colstr + '</div>\
                    </div>';

              }else if(colvalue >= 0){
                   colvalue =  Number(colvalue)*100;
                  colvalue = Number(colvalue).toFixed(2);

                    colstr +='+' + colvalue + '%';
                     html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#e74c3c">' + colstr + '</div>\
                    </div>';

              }else{
                  colvalue =  Number(colvalue)*100;
                  colvalue = Number(colvalue).toFixed(2);

                  colstr += colvalue + '%';
                   html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#78ba00">' + colstr + '</div>\
                    </div>';
              } 
              $('#zhenqizbf').append(html);

       }else{ //月
            var strname;
            var colvalue; 
            var html = '';
            var colstr= '';

            colvalue = zarry[0].CM_COL;
            strname = '月环比';
            
              if( colvalue == 'null' || colvalue == undefined || colvalue == null ){
                  colstr +='--';
                     html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#7f8c8d">' + colstr + '</div>\
                    </div>';

              }else if(colvalue >= 0){
                    colstr +='+' + colvalue + '%';
                     html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#e74c3c">' + colstr + '</div>\
                    </div>';

              }else{
                  colstr += colvalue + '%';
                   html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#78ba00">' + colstr + '</div>\
                    </div>';
              } 

            colvalue = zarry[0].CM_YOY;
            strname = '年同比';
            colstr='';

              if( colvalue == 'null' || colvalue == undefined || colvalue == null ){
                  colstr +='--';
                     html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#7f8c8d">' + colstr + '</div>\
                    </div>';

              }else if(colvalue >= 0){
                    colstr +='+' + colvalue + '%';
                     html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#e74c3c">' + colstr + '</div>\
                    </div>';

              }else{
                  colstr += colvalue + '%';
                   html +='<div class="grid">\
                        <div class="col-1 left" >'+strname+'</div>\
                      <div class="col-1 right" style="color:#78ba00">' + colstr + '</div>\
                    </div>';
              } 
              $('#zhenqizbf').append(html);

          }

      }else{
           $('#zhenqizbf').empty();
           $('#zhenqivalue0').text( '--'  );
      }
 
        //TOP3
        var zarry1 = dataobj.topThree

        console.log('--' + zarry1 );

        if( zarry1 != undefined && zarry1 != null && zarry1 != 'null'){

            if( zarry1[0] != undefined && zarry1[0] != 'null' ){

                if( zarry1[0].AREA_RANK != undefined && zarry1[0].AREA_RANK != 'null' && zarry1[0].AREA_RANK != null ){
                 $('#zhenqirank5').text( zarry1[0].AREA_RANK );
               }else{
                 $('#zhenqirank5').text( '--' );
               }

               if( zarry1[0].AREA_NAME != undefined && zarry1[0].AREA_NAME != 'null' && zarry1[0].AREA_NAME != null ){
                 $('#zhenqiname5').text( zarry1[0].AREA_NAME );
               }else{
                 $('#zhenqiname5').text( '--' );
               }

               if( zarry1[0].PERSENT_RANKING != undefined && zarry1[0].PERSENT_RANKING != 'null' && zarry1[0].PERSENT_RANKING != null ){
                 $('#zhenqivalue5').text( zarry1[0].PERSENT_RANKING + '%');
               }else{
                 $('#zhenqivalue5').text( '--');
               }

            }else{
              $('#zhenqirank5').text( '--' );
               $('#zhenqivalue5').text( '--' );
               $('#zhenqiname5').text( '--' );
            }

            if( zarry1[1] != undefined && zarry1[1] != 'null' && zarry1[1] != null ){

              if( zarry1[1].AREA_RANK != undefined && zarry1[1].AREA_RANK != 'null' ){
                 $('#zhenqirank6').text( zarry1[1].AREA_RANK );
               }else{
                 $('#zhenqirank6').text( '--' );
               }

               if( zarry1[1].AREA_NAME != undefined && zarry1[1].AREA_NAME != 'null' && zarry1[1].AREA_NAME != null ){
                 $('#zhenqiname6').text( zarry1[1].AREA_NAME );
               }else{
                 $('#zhenqiname6').text( '--' );
               }

               if( zarry1[1].PERSENT_RANKING != undefined && zarry1[1].PERSENT_RANKING != 'null' && zarry1[1].PERSENT_RANKING != null  ){
                 $('#zhenqivalue6').text( zarry1[1].PERSENT_RANKING + '%');
               }else{
                 $('#zhenqivalue6').text( '--');
               }

            }else{
               $('#zhenqirank6').text( '--' );
               $('#zhenqivalue6').text( '--' );
               $('#zhenqiname6').text( '--' );
            }

            if( zarry1[2] != undefined && zarry1[2] != 'null' && zarry1[2] != null ){

              if( zarry1[2].AREA_RANK != undefined && zarry1[2].AREA_RANK != 'null' ){
                 $('#zhenqirank7').text( zarry1[2].AREA_RANK );
               }else{
                 $('#zhenqirank7').text( '--' );
               }

              if( zarry1[2].AREA_NAME != undefined && zarry1[2].AREA_NAME != 'null' && zarry1[2].AREA_NAME != null ){
                 $('#zhenqiname7').text( zarry1[2].AREA_NAME );
               }else{
                 $('#zhenqiname7').text( '--' );
               }

               if( zarry1[2].PERSENT_RANKING != undefined && zarry1[2].PERSENT_RANKING != 'null' && zarry1[2].PERSENT_RANKING != null ){
                 $('#zhenqivalue7').text( zarry1[2].PERSENT_RANKING + '%');
               }else{
                 $('#zhenqivalue7').text( '--');
               }

            }else{
               $('#zhenqirank7').text( '--' );
               $('#zhenqivalue7').text( '--' );
               $('#zhenqiname7').text( '--' );
            }
        }else{
          $('#zhenqivalue7').text( '--' );
          $('#zhenqiname7').text( '--' );
          $('#zhenqivalue5').text( '--' );
          $('#zhenqiname5').text( '--' );
          $('#zhenqivalue6').text( '--' );
          $('#zhenqiname6').text( '--' );

          $('#zhenqirank5').text( '--' );
          $('#zhenqirank6').text( '--' );
          $('#zhenqirank7').text( '--' );
        }

//末3名
  var zarry2 = dataobj.endThree

        if( zarry2 != undefined && zarry2 != null && zarry2 != 'null' ){

            if( zarry2[0] != undefined && zarry2[0] != 'null' ){

              if( zarry2[0].AREA_RANK != undefined && zarry2[0].AREA_RANK != 'null' ){
                 $('#zhenqirank8').text( zarry2[0].AREA_RANK );
               }else{
                 $('#zhenqirank8').text( '--' );
               }
         
               if( zarry2[0].AREA_NAME != undefined && zarry2[0].AREA_NAME != 'null' ){
                 $('#zhenqiname8').text( zarry2[0].AREA_NAME );
               }else{
                 $('#zhenqiname8').text( '--' );
               }

               if( zarry2[0].PERSENT_RANKING != undefined && zarry2[0].PERSENT_RANKING != 'null' ){
                 $('#zhenqivalue8').text( zarry2[0].PERSENT_RANKING + '%');
               }else{
                 $('#zhenqivalue8').text( '--');
               }

            }else{
               $('#zhenqirank8').text( '--' );
               $('#zhenqivalue8').text( '--' );
               $('#zhenqiname8').text( '--' );
            }

            if( zarry2[1] != undefined && zarry2[1] != 'null'){

               if( zarry2[1].AREA_RANK != undefined && zarry2[1].AREA_RANK != 'null' ){
                 $('#zhenqirank9').text( zarry2[1].AREA_RANK );
               }else{
                 $('#zhenqirank9').text( '--' );
               }
         
               if( zarry2[1].AREA_NAME != undefined && zarry2[1].AREA_NAME != 'null' ){
                 $('#zhenqiname9').text( zarry2[1].AREA_NAME );
               }else{
                 $('#zhenqiname9').text( '--' );
               }

               if( zarry2[1].PERSENT_RANKING != undefined && zarry2[1].PERSENT_RANKING != 'null' ){
                 $('#zhenqivalue9').text( zarry2[1].PERSENT_RANKING + '%');
               }else{
                 $('#zhenqivalue9').text( '--');
               }

            }else{
               $('#zhenqirank9').text( '--' );
               $('#zhenqiname9').text( '--' );
               $('#zhenqivalue9').text( '--' );
            }

            if( zarry2[2] != undefined && zarry2[2] != 'null'){

                if( zarry2[2].AREA_RANK != undefined && zarry2[2].AREA_RANK != 'null' ){
                 $('#zhenqirank10').text( zarry2[2].AREA_RANK );
               }else{
                 $('#zhenqirank10').text( '--' );
               }
         
              if( zarry2[2].AREA_NAME != undefined && zarry2[2].AREA_NAME != 'null' ){
                 $('#zhenqiname10').text( zarry2[2].AREA_NAME );
               }else{
                 $('#zhenqiname10').text( '--' );
               }

               if( zarry2[2].PERSENT_RANKING != undefined && zarry2[2].PERSENT_RANKING != 'null' ){
                 $('#zhenqivalue10').text( zarry2[2].PERSENT_RANKING + '%');
               }else{
                 $('#zhenqivalue10').text( '--');
               }

            }else{
               $('#zhenqirank10').text( '--' );
               $('#zhenqivalue10').text( '--' );
               $('#zhenqiname10').text( '--' );
            }
        }else{
          $('#zhenqivalue10').text( '--' );
          $('#zhenqiname10').text( '--' );
          $('#zhenqivalue8').text( '--' );
          $('#zhenqiname8').text( '--' );
          $('#zhenqivalue9').text( '--' );
          $('#zhenqiname9').text( '--' );
      
          $('#zhenqirank8').text( '--' );
          $('#zhenqirank9').text( '--' );
          $('#zhenqirank10').text( '--' );
      }

        //图表展现
        var zarry3 = dataobj.trendByDayOrMonth
        if( zarry3 != undefined ){
           var xdata =[];  
           var ydata =[];  
           var ysortdata =[];

           for( var i=0;i < zarry3.length; ++i ){
                 //console.log( zarry3[i] );
                var value = zarry3[i];
                if( value != 'null' && value != undefined ){
                    if( value.Y != undefined && value.Y != 'null'){
                        if (paramSettings.sdateType == 'D') {
                            xdata.push( value.X + '日');
                        }else{
                            xdata.push( value.X + '月');
                        }
                            ydata.push( value.Y );
                            ysortdata.push( value.Y );
                    }
                }
           }
            hchart1(xdata,ydata);
   /*
           ysortdata.sort( arrsort );
           var len = ysortdata.length;
            minvalue = Number(ysortdata[0])/2;
            minvalue = minvalue.toFixed(2);
            maxvalue = Number(ysortdata[ len -1 ]) + Number(minvalue);
            maxvalue = maxvalue.toFixed(2);

            if( (maxvalue-minvalue)%5 != 0 ){

            }

             console.log( minvalue );
             console.log( maxvalue );
             console.log( ydata);

           if(xdata.length >0 ){
              $('#zhenqidiv1').attr('style','width:100%;height:400px');
              hchart1(xdata,ydata);
           }else{
              $('#zhenqidiv1').attr('style','width:100%;height:0px');
              hchart1(xdata,ydata);
           }
    */
        }

        var zarry4 = dataobj.trendByArea
        if( zarry4 != undefined ){
           var xdata =[];
           var ydata =[];

           for( var i=0;i < zarry4.length; ++i ){
                var value = zarry4[i];
                if( value != 'null' && value != undefined ){
                  if( value.Y != undefined && value.Y != 'null'){
                    xdata.push( value.X );
                    ydata.push( value.Y );
                  }
                }
           }
        hchart2(xdata,ydata);
/*
           if(xdata.length >0 ){
              var height = xdata.length * 30;
              if( height < 150 )height = 150;
              //console.log( height); 
              $('#zhenqidiv2').attr('style','width:100%;height:'+ height + 'px');
              hchart2(xdata,ydata);
           }else{
               $('#zhenqidiv2').attr('style','width:100%;height:0px');
               hchart2(xdata,ydata);
           }
*/
        }
      }

      _settitle();

      J.Scroll('#zhenqi2_article',{hScroll:true,hScrollbar : false});
      //J.hideMask();
      J.hideToast();
    }

    function arrsort(a,b){
      return a-b;
    }

    function tshare(){
      httpsinterface.Html5Share();
    }

    function hchart1(xdata,ydata){

//        console.log(xdata)
//        console.log(ydata)
        jchart_vbar.init('zhenqidiv1',paramchartlabel,xdata,ydata);
   /*
    var mChart = echarts.init(document.getElementById('zhenqidiv1'));
    var option1 = {
      title: {
        x: 'left',
        y: 'top',
        textStyle: {
          fontSize: 10,
          fontWeight: 'bolder',
          color: '#333'
        },
        text: paramchartlabel
      },
      grid: {
        'x': 5,
        'y': 20,
        'x2': 20,
        'y2': 40
      },
      calculable: false,
      xAxis: [{
        type: 'category',
        data: xdata,
        axisLabel:{ textStyle:{ fontSize:8 } }
      }],
      yAxis: [{
        type: 'value',
        min: minvalue,
        max: maxvalue,
        splitNumber:5,
        axisLabel:{ margin : -50,formatter:function(v){ return Number(v).toFixed(2); },
        textStyle:{ fontSize:8 } }
      }],
      series: [{
        name: '用户数',
        type: 'bar',
        data: ydata
      }]
    };
    mChart.setOption(option1, true);
  */
}

function hchart2(xdata,ydata){

    //console.log(xdata)
    //console.log(ydata)
    
    jchart_hbar.init('zhenqidiv2',paramchartlabel,ydata,xdata);
/*
  var myChart = echarts.init(document.getElementById('zhenqidiv2'));
  var option = {
    title : {
        x: 'left',
        y: 'top',
        textStyle:{fontSize: 10,fontWeight: 'bolder',color: '#333' },
        text: paramchartlabel
    },
    grid: {'x':5,'y':25,'x2':20,'y2':40},
    calculable : false,
    xAxis : [
        {
            type : 'value',
            boundaryGap : [0, 0.01],
            axisLabel:{ textStyle:{ fontSize:8 } }
        }
    ],
    yAxis : [
        {
            type : 'category',
            data : xdata,
            axisLabel:{margin : -50, textStyle:{  fontSize:8} }
        }
    ],
    series : [
        {
            name:'2011年',
            type:'bar',
            data:ydata
        }
    ]
};
    myChart.setOption( option,true );
 */
}

    function footershowz(){
         var html = '';   
         html += '<footer>\
                  <a  id ="zhen2time" href="#" data-target="popup" data-ids="1" data-area='
                  +paramSettings.stimedata+ 
                  '><span class="fix_label">'
                  +paramSettings.stimedata+ 
                  '</span>\
                  </a>\
                  <a  id ="zhen2area" href="#" data-target="popup" data-ids="2" data-area='
                  +paramSettings.sareaname+ 
                  '><span class="fix_label">'
                  +paramSettings.sareaname+
                  '</span>\
                  </a>\
                  </footer>'
         $('#zhenqi2_article').append(html);

         J.Scroll('#zhenqi2_article',{hScroll:true,hScrollbar : false});
    }

    function footerhidez(){
        //J.popup.close();
         $('#zhenqi2_article footer').remove();
          J.Scroll('#zhenqi2_article',{hScroll:true,hScrollbar : false});
    }

    //数据格式化,保留2位小数
    function dataform( dataj,unit ){

        var datan = Number( dataj );
        var datastr='';

        if( datan > 100000000){
          //亿
            datan = datan/100000000;
            datan = datan.toFixed(2);
            datastr += String(datan) +'亿' +unit;
            return datastr;
        }else if( datan > 10000 ){
          //万
            datan = datan/10000;
            datan = datan.toFixed(2);
            datastr += String(datan) +'万'+unit;
            return datastr;
        }else{
             datan = datan.toFixed(2);
             datastr += String(datan) +unit;
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

	return {
    zinit: _zinit,
    testdata: tdata,
    setchart1label: _setchart1label,
    setchart2label: _setchart2label,

    showfooter:footershowz,
    hidefooter:footerhidez,

    setdate: _setdate,
    getdatedata: _getdatedata,
    setarea: _setarea,
    getareacode: _getareacode,
    setareacode: _setareacode,

    btnshare:tshare,
    getdata:  gdata,
    getDflag: _getDflag,


    setchart1: _setchart1,
    setchart2: _setchart2
	};
  
}();

























/******  
   zl 创建2016.8.22
  《家庭宽带》  指标明细
******/
var homenetwork = function() {

  var paramSettings = { skpicode:'',skpiname:'',stime:'',stimedata:'',sarea:'',sareaname:'',dateType:'',sshowType:'',areashowType:'',srowNumber:''};

  //var paramlables = { char1name1:'用户数',char1name2:'环比',char2name1:'用户数',char2name2:'日环比',char2name3:'日累计',char2name4:'日同比'  };
  var paramchartlabel ='用户数(单位:户)';
  var paramchart2label ='用户数(单位:户)';

  var minvalue = 0;
  var maxvalue = 0;

  function _zinit( objdata ){
    console.log( objdata );

    paramSettings.skpicode = objdata.kpicode;
    paramSettings.skpiname = objdata.kpiname;
    paramSettings.stime = objdata.time;;
    paramSettings.stimedata = objdata.timeform;;

    paramSettings.sarea = objdata.areacode;;
    paramSettings.sareaname = objdata.areaname;;

    paramSettings.sdateType = 'D';
    paramSettings.sshowType = objdata.showtype;;
    paramSettings.sareashowType = objdata.areashowtype;;
    paramSettings.srowNumber = objdata.number;;

   }
  function _setdate( sdate ){

        paramSettings.stimedata  =  sdate;

        var timestr = sdate;
         if( timestr.length >=10 ){
           timestr = timestr.substring(0,4) + timestr.substring(5,7) + timestr.substring(8,10);
         }else{
           timestr = timestr.substring(0,4) +  timestr.substring(5,7);
         }
         console.log( 'tdatedata:' + timestr );

         paramSettings.stime =  timestr;
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
    $('#home_network_section header h3.title').text( paramSettings.skpiname );
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
        var testdat = { kpiCode:  paramSettings.skpicode,  
                        areaCode: paramSettings.sarea, 
                        dateType: paramSettings.sdateType,
                        time: paramSettings.stime,
                        showType: paramSettings.sshowType,
                        areaShowType:  paramSettings.sareashowType,
                        rowNumber:  paramSettings.srowNumber
                      };

        var testdatastr = JSON.stringify( testdat );
        console.log( testdatastr );

        var jsonobj;
        //J.showMask();
        J.showToast('正在加载数据...','loading',0);

        httpsinterface.GetJiakuanData2(testdatastr,function(dataobj) {
        jsonobj =  $.parseJSON( dataobj );;
        console.log(jsonobj);
        shandler( jsonobj );
     });
  }

    function shandler( dataobj ){

      console.log( dataobj );
      //J.hideMask();

      if(dataobj ==undefined || dataobj.flag == 'false'){
           $('#homename1').text( '--' );
           $('#homename2').text( '--' );
           $('#homename3').text( '--' );
           $('#homename4').text( '--' );
           $('#homename5').text( '--' );
           $('#homename6').text( '--' );
           $('#homename7').text( '--' );

           $('#homevalue0').text( '--'  );
           $('#homevalue1').text( '--' );
           $('#homevalue2').text( '--' );
           $('#homevalue3').text( '--' );
           $('#homevalue4').text( '--' );
           $('#homevalue5').text('--');
           $('#homevalue6').text('--');
           $('#homevalue7').text('--');

           $('#homevalue8').text('--');
           $('#homevalue9').text('--');
           $('#homevalue10').text('--');

           $('#homename8').text( '--' );
           $('#homename9').text( '--' );
           $('#homename10').text( '--' );

      $('#homerank5').text( '--' );
      $('#homerank6').text( '--' );
      $('#homerank7').text( '--' );

      $('#homerank8').text( '--' );
      $('#homerank9').text( '--' );
      $('#homerank10').text( '--' );

           $('#zdiv1').empty();
           $('#zdiv2').empty();

           _settitle();
           J.Scroll('#home_network_article',{hScroll:true,hScrollbar : false});
           //J.hideMask();
           J.hideToast();
           return;
      }

      if( dataobj != undefined ){

        //指标分析
        var zarry = dataobj.detailValue
        if( zarry[0] != undefined ){
          $('#homename1').text( zarry[0].COLUMN_CHINESE_NAME );

          var lenstr = zarry[0].COLUMN_VALUE;
          if( lenstr.length > 6){
            var len = lenstr.length;
            var stru0 = lenstr.substring(len-3,len);
            var stru1 = lenstr.substring(len-6,len-3);
            var stru2 = lenstr.substring(0,len-6);
            // console.log( stru0 );
            // console.log( stru1 );
            // console.log( stru2 );
            var str = stru2 + ',' + stru1 + ',' + stru0;
            $('#homevalue0').text( str );

          }else if( lenstr.length > 3){
            var len = lenstr.length;
            var stru0 = lenstr.substring(len-3,len);
            var stru2 = lenstr.substring(0,len-3);
            // console.log( stru0 );
            // console.log( stru2 );

            var str = stru2  + ',' + stru0;
            $('#homevalue0').text( str );

          }else{
            $('#homevalue0').text( zarry[0].COLUMN_VALUE );
          }


        }else{
           $('#homename1').text( '--' );
           $('#homevalue0').text( '--'  );
        }
        
        if( zarry[1] != undefined ){
          $('#homename2').text( zarry[1].COLUMN_CHINESE_NAME );

            var colvalue =  zarry[1].COLUMN_VALUE;
            var colstr= '';
            if( colvalue == 'null' || colvalue == null ){
                 colstr +='--';
                   $('#homevalue2').attr('style','color: #7f8c8d');
                   $('#homevalue2').text( colstr );
            }else if(colvalue >= 0){
                   colstr +='+' + colvalue + '%';
                   $('#homevalue2').attr('style','color: #e74c3c');
                   $('#homevalue2').text( colstr );
            }else{
               colstr += colvalue + '%';
               $('#homevalue2').attr('style','color: #78ba00');
               $('#homevalue2').text( colstr );
            } 
        }else{
          $('#homevalue2').text( '--' );
        }

        if( zarry[2] != undefined ){
          $('#homename3').text( zarry[2].COLUMN_CHINESE_NAME );

          var colvalue =  zarry[2].COLUMN_VALUE;
            var colstr= '';
            if( colvalue == 'null' || colvalue == null ){
                 colstr +='--';
                   $('#homevalue3').attr('style','color: #7f8c8d');
                   $('#homevalue3').text( colstr );
            }else if(colvalue >= 0){
                   colstr +='+' + colvalue + '%';
                   $('#homevalue3').attr('style','color: #e74c3c');
                   $('#homevalue3').text( colstr );
            }else{
               colstr += colvalue + '%';
                   $('#homevalue3').attr('style','color: #78ba00');
                   $('#homevalue3').text( colstr );
            } 
        }else{
          $('#homename3').text( '--' );
          $('#homevalue3').text( '--' );
        }

        if( zarry[3] != undefined ){
          $('#homename4').text( zarry[3].COLUMN_CHINESE_NAME );

            var colvalue =  zarry[3].COLUMN_VALUE;
            var colstr= '';
            
            if( colvalue == 'null' || colvalue == null ){
                 colstr +='--';
                  $('#homevalue4').attr('style','color: #7f8c8d');
                   $('#homevalue4').text( colstr );
            }else if(colvalue >= 0){
                   colstr +='+' + colvalue + '%';
                    $('#homevalue4').attr('style','color: #e74c3c');
                   $('#homevalue4').text( colstr );
            }else{
               colstr += colvalue + '%';
                  $('#homevalue4').attr('style','color: #78ba00');
                   $('#homevalue4').text( colstr );
            } 
        }else{
          $('#homevalue4').text( '--' );
          $('#homename4').text( '--' );
        }

        //TOP3
        var zarry1 = dataobj.topThree
        if( zarry1 != undefined ){

             if( zarry1[0] != undefined && zarry1[0] != 'null' && zarry1[0] != null ){

              if( zarry1[0].AREA_RANK != undefined && zarry1[0].AREA_RANK != 'null' && zarry1[0].AREA_RANK != null ){
                 $('#homerank5').text( zarry1[0].AREA_RANK );
               }else{
                 $('#homerank5').text( '--' );
               }

               if( zarry1[0].AREA_NAME != undefined && zarry1[0].AREA_NAME != 'null' && zarry1[0].AREA_NAME != null ){
                 $('#homename5').text( zarry1[0].AREA_NAME );
               }else{
                 $('#homename5').text( '--' );
               }

               if( zarry1[0].PERSENT_RANKING != undefined && zarry1[0].PERSENT_RANKING != 'null' && zarry1[0].PERSENT_RANKING != null ){
                 $('#homevalue5').text( zarry1[0].PERSENT_RANKING + '%');
               }else{
                 $('#homevalue5').text( '--');
               }

                // $('#homerank5').text( zarry1[0].AREA_RANK );
                // $('#homename5').text( zarry1[0].AREA_NAME );
                // $('#homevalue5').text( zarry1[0].PERSENT_RANKING + '%');
            }else{
               $('#homerank5').text( '--' );
               $('#homevalue5').text( '--' );
               $('#homename5').text( '--' );
            }

           if( zarry1[1] != undefined && zarry1[1] != 'null' && zarry1[1] != null ){

               if( zarry1[1].AREA_RANK != undefined && zarry1[1].AREA_RANK != 'null'  && zarry1[1].AREA_RANK != null ){
                 $('#homerank6').text( zarry1[1].AREA_RANK );
               }else{
                 $('#homerank6').text( '--' );
               }

               if( zarry1[1].AREA_NAME != undefined && zarry1[1].AREA_NAME != 'null' && zarry1[1].AREA_NAME != null ){
                 $('#homename6').text( zarry1[1].AREA_NAME );
               }else{
                 $('#homename6').text( '--' );
               }

               if( zarry1[1].PERSENT_RANKING != undefined && zarry1[1].PERSENT_RANKING != 'null' && zarry1[1].PERSENT_RANKING != null ){
                 $('#homevalue6').text( zarry1[1].PERSENT_RANKING + '%');
               }else{
                 $('#homevalue6').text( '--');
               }

                // $('#homerank6').text( zarry1[1].AREA_RANK );
                // $('#homename6').text( zarry1[1].AREA_NAME );
                // $('#homevalue6').text( zarry1[1].PERSENT_RANKING + '%');
            }else{
               $('#homerank6').text( '--' );
               $('#homevalue6').text( '--' );
               $('#homename6').text( '--' );
            }

            if( zarry1[2] != undefined && zarry1[2] != 'null'  && zarry1[2] != null ){

              if( zarry1[2].AREA_RANK != undefined && zarry1[2].AREA_RANK != 'null' && zarry1[2].AREA_RANK != null ){
                 $('#homerank7').text( zarry1[2].AREA_RANK );
               }else{
                 $('#homerank7').text( '--' );
               }

              if( zarry1[2].AREA_NAME != undefined && zarry1[2].AREA_NAME != 'null' && zarry1[2].AREA_NAME != null ){
                 $('#homename7').text( zarry1[2].AREA_NAME );
               }else{
                 $('#homename7').text( '--' );
               }

               if( zarry1[2].PERSENT_RANKING != undefined && zarry1[2].PERSENT_RANKING != 'null' && zarry1[2].PERSENT_RANKING != null ){
                 $('#homevalue7').text( zarry1[2].PERSENT_RANKING + '%');
               }else{
                 $('#homevalue7').text( '--');
               }

               // $('#homerank7').text( zarry1[2].AREA_RANK );
               // $('#homename7').text( zarry1[2].AREA_NAME );
               // $('#homevalue7').text( zarry1[2].PERSENT_RANKING + '%');
            }else{
               $('#homerank7').text( '--' );
               $('#homevalue7').text( '--' );
               $('#homename7').text( '--' );
            }
        }else{
          $('#homevalue7').text( '--' );
          $('#homename7').text( '--' );
          $('#homevalue5').text( '--' );
          $('#homename5').text( '--' );
          $('#homevalue6').text( '--' );
          $('#homename6').text( '--' );
          
          $('#homerank5').text( '--' );
          $('#homerank6').text( '--' );
          $('#homerank7').text( '--' );
        }

        //末三位
        var zarry2 = dataobj.endThree
        if( zarry2 != undefined ){

            if( zarry2[0] != undefined && zarry2[0] != 'null' && zarry2[0] != null){

              if( zarry2[0].AREA_RANK != undefined && zarry2[0].AREA_RANK != 'null' && zarry2[0].AREA_RANK != null ){
                 $('#homerank8').text( zarry2[0].AREA_RANK );
               }else{
                 $('#homerank8').text( '--' );
               }
         
               if( zarry2[0].AREA_NAME != undefined && zarry2[0].AREA_NAME != 'null' && zarry2[0].AREA_NAME != null ){
                 $('#homename8').text( zarry2[0].AREA_NAME );
               }else{
                 $('#homename8').text( '--' );
               }

               if( zarry2[0].PERSENT_RANKING != undefined && zarry2[0].PERSENT_RANKING != 'null' && zarry2[0].PERSENT_RANKING != null ){
                 $('#homevalue8').text( zarry2[0].PERSENT_RANKING + '%');
               }else{
                 $('#homevalue8').text( '--');
               }

                // $('#homerank8').text( zarry2[0].AREA_RANK );
                // $('#homename8').text( zarry2[0].AREA_NAME );
                // $('#homevalue8').text( zarry2[0].PERSENT_RANKING + '%');
            }else{
               $('#homerank8').text( '--' );
               $('#homevalue8').text( '--' );
               $('#homename8').text( '--' );
            }

            if( zarry2[1] != undefined && zarry2[1] != 'null'&& zarry2[1] != null){

               if( zarry2[1].AREA_RANK != undefined && zarry2[1].AREA_RANK != 'null' && zarry2[1].AREA_RANK != null){
                 $('#homerank9').text( zarry2[1].AREA_RANK );
               }else{
                 $('#homerank9').text( '--' );
               }
         
               if( zarry2[1].AREA_NAME != undefined && zarry2[1].AREA_NAME != 'null' && zarry2[1].AREA_NAME != null){
                 $('#homename9').text( zarry2[1].AREA_NAME );
               }else{
                 $('#homename9').text( '--' );
               }

               if( zarry2[1].PERSENT_RANKING != undefined && zarry2[1].PERSENT_RANKING != 'null' && zarry2[1].PERSENT_RANKING != null){
                 $('#homevalue9').text( zarry2[1].PERSENT_RANKING + '%');
               }else{
                 $('#homevalue9').text( '--');
               }

                //$('#homerank9').text( zarry2[1].AREA_RANK );
                //$('#homename9').text( zarry2[1].AREA_NAME );
                //$('#homevalue9').text( zarry2[1].PERSENT_RANKING + '%');
            }else{
                $('#homerank9').text( '--' );
               $('#homevalue9').text( '--' );
               $('#homename9').text( '--' );
            }

           if( zarry2[2] != undefined && zarry2[2] != 'null'){
              
               if( zarry2[2].AREA_RANK != undefined && zarry2[2].AREA_RANK != 'null' && zarry2[2].AREA_RANK != null){
                 $('#homerank10').text( zarry2[2].AREA_RANK );
               }else{
                 $('#homerank10').text( '--' );
               }
         
              if( zarry2[2].AREA_NAME != undefined && zarry2[2].AREA_NAME != 'null' && zarry2[2].AREA_NAME != null){
                 $('#homename10').text( zarry2[2].AREA_NAME );
               }else{
                 $('#homename10').text( '--' );
               }

               if( zarry2[2].PERSENT_RANKING != undefined && zarry2[2].PERSENT_RANKING != 'null' && zarry2[2].PERSENT_RANKING != null){
                 $('#homevalue10').text( zarry2[2].PERSENT_RANKING + '%');
               }else{
                 $('#homevalue10').text( '--');
               }

               //$('#homerank10').text( zarry2[2].AREA_RANK );
               //$('#homename10').text( zarry2[2].AREA_NAME );
               //$('#homevalue10').text( zarry2[2].PERSENT_RANKING + '%');
            }else{
               $('#homerank10').text( '--' );
               $('#homevalue10').text( '--' );
               $('#homename10').text( '--' );
            }
        }else{
          $('#homevalue10').text( '--' );
          $('#homename10').text( '--' );
          $('#homevalue8').text( '--' );
          $('#homename8').text( '--' );
          $('#homevalue9').text( '--' );
          $('#homename9').text( '--' );
          
          $('#homerank8').text( '--' );
          $('#homerank9').text( '--' );
          $('#homerank10').text( '--' );
        }


        //排名
        var zarry2 = dataobj.ranking
        if( zarry2 != undefined ){

            if( zarry2[0] != undefined ){
                $('#homevalue1').text( zarry2[0].NUM );
            }else{
                $('#homevalue1').text( '--' );
            }

            if( zarry2[1] != undefined ){

              console.log( zarry2[1]  );

              if( zarry2[1].upOrDown == 'EQUAL' ){
                 $('#homename0divi').removeClass('icon arrow-up-3');
                 $('#homename0divi').removeClass('icon arrow-down-2');
                  $('#homename0divi').addClass('icon minus');
                  $('#homename0divi').attr('style','color: #3498db');
              }else if( zarry2[1].upOrDown == 'UP' ){
                 $('#homename0divi').removeClass('icon minus');
                 $('#homename0divi').removeClass('icon arrow-down-2');
                  $('#homename0divi').addClass('icon arrow-up-3');
                  $('#homename0divi').attr('style','color: #e74c3c');
              }else if( zarry2[1].upOrDown == 'DOWN' ){
                 $('#homename0divi').removeClass('icon arrow-up-3');
                 $('#homename0divi').removeClass('icon minus');
                  $('#homename0divi').addClass('icon arrow-down-2');
                  $('#homename0divi').attr('style','color: #78ba00');
              }
            }
        }else{
          $('#homevalue1').text( '--' );
        }

        //图表展现
        var zarry3 = dataobj.trendByDayOrMonth
        if( zarry3 != undefined ){
           var xdata =[];  
           var ydata =[];  
           var ysortdata = [];

           for( var i=0;i < zarry3.length; ++i ){
                 //console.log( zarry3[i] );
                var value = zarry3[i];
                if( value != 'null' && value != undefined  && value != null ){
                    xdata.push( value.X + '日');
                    ydata.push( value.Y );
                    ysortdata.push( value.Y );
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

           if(xdata.length >0 ){
              $('#zdiv1').attr('style','width:100%;height:300px');
              hchart1(xdata,ydata);
           }else{
             $('#zdiv1').attr('style','width:100%;height:0px');
             hchart1(xdata,ydata);
           }
      */
        }

        var zarry4 = dataobj.trendByArea
        if( zarry4 != undefined ){
           var xdata =[];//['武汉', '黄石', '十堰', '宜昌', '襄阳', '鄂州', '荆门', '荆州', '孝感', '黄冈', '咸宁','随州', '仙桃', '潜江', '天门', '恩施'];
           var ydata =[];//[18203, 23489, 29034, 104970, 131744, 630230, 18203, 23489, 29034, 104970, 131744, 630230,18203, 23489, 29034, 104970];

           for( var i=0;i < zarry4.length; ++i ){
                var value = zarry4[i];
                if( value != 'null' && value != undefined ){
                    xdata.push( value.X );
                    ydata.push( value.Y );
                }
           }
            hchart2(ydata,xdata);
 /*
           if(xdata.length >0 ){
              var height = xdata.length * 30;
              if( height < 150 )height = 150;
              console.log( height); 
              $('#zdiv2').attr('style','width:100%;height:'+ height + 'px');

              // var html ='';
              //     html +='<li class="active"><a href="#" id="homechar2li1">用户数</a></li>\
              //             <li><a href="#" id="homechar2li2">日环比</a></li>\
              //             <li><a href="#" id="homechar2li3">日累计</a></li>\
              //             <li><a href="#" id="homechar2li4">日同比</a></li>';
              //     $('#homechar2').append(html);
              hchart2(xdata,ydata);
           }else{
             $('#zdiv2').attr('style','width:100%;height:0px');
             hchart2(xdata,ydata);
           }
*/
        }

      }

      _settitle();

      J.Scroll('#home_network_article',{hScroll:true,hScrollbar : false});
       //J.hideMask();
      J.hideToast();
    }

    function arrsort(a,b){
      return a-b;
    }

    function tshare(){
      httpsinterface.Html5Share();
    }

    function timedata( date ){
       //$('#home_network_article ul.list').empty();
       // console.log( '时间选择器:'  + date );

       // var timestr = date;
       //   if( timestr.length >=10 ){
       //     timestr = timestr.substring(0,4) + timestr.substring(5,7) + timestr.substring(8,10);
       //   }else{
       //     timestr = timestr.substring(0,4) +  timestr.substring(5,7);
       //   }
       //   console.log( timestr );
       //   paramSettings.stime = timestr;

       //   //J.Cache.save('stime', date);

         gdata();
    }

    function areadata( areaCode ){
      //$('#home_network_article ul.list').empty();
      //  console.log( '地域选择器:'  +  areaCode );

      // paramSettings.sarea = areaCode;
      gdata();
    }

    function hchart1(xdata,ydata){

        jchart_vbar.init('zdiv1',paramchartlabel,xdata,ydata);
/*
    var mChart = echarts.init(document.getElementById('zdiv1'));
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
        axisLabel:{ margin : -50,formatter:function(v){ return Number(v).toFixed(2); },  textStyle:{ fontSize:8 } }
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

    jchart_hbar.init('zdiv2',paramchart2label,xdata,ydata);
/*
  var myChart = echarts.init(document.getElementById('zdiv2'));
  var option = {
    title : {
        x: 'left',
        y: 'top',
        textStyle:{fontSize: 10,fontWeight: 'bolder',color: '#333' },
        text: paramchart2label
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
                  <a  id ="htime" href="#" data-target="popup" data-ids="1" data-area='
                  +paramSettings.stimedata+ 
                  '><span class="fix_label">'
                  +paramSettings.stimedata+ 
                  '</span>\
                  </a>\
                  <a  id ="harea" href="#" data-target="popup" data-ids="2" data-area='
                  +paramSettings.sareaname+ 
                  '><span class="fix_label">'
                  +paramSettings.sareaname+
                  '</span>\
                  </a>\
                  </footer>'
         $('#home_network_article').append(html);

         J.Scroll('#home_network_article',{hScroll:true,hScrollbar : false});
    }

    function footerhidez(){
        //J.popup.close();
         $('#home_network_article footer').remove();
          J.Scroll('#home_network_article',{hScroll:true,hScrollbar : false});
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

    getdatabytime:timedata,
    getdatabyarea:areadata,
    btnshare:tshare,
    getdata:  gdata,


    setchart1: _setchart1,
    setchart2: _setchart2
	};

}();

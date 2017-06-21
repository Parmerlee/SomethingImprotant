document.addEventListener('deviceready', onDeviceReady, false);
function onDeviceReady(){

    console.log('插件测试');
    console.log( $(window).width() +" " +  $(window).height() );

    navigator.splashscreen.hide();

    //注册后退按钮
    document.addEventListener("backbutton", function (e) {
        if(J.hasMenuOpen){
            J.Menu.hide();
        }else if(J.hasPopupOpen){
            J.closePopup();
        }else{
            var sectionId = $('section.active').attr('id');
            if(sectionId == 'jiakuan3_section' || sectionId == 'zhenqi3_section'){
                 httpsinterface.NativeGoBack();
                // J.confirm('提示','是否退出程序？',function(){
                //     navigator.app.exitApp();
                // });
            }else if( sectionId == 'index_section' || sectionId == 'index1_section' ){
                httpsinterface.NativeGoBack();
            }else{
                //jiakuan.hidefooter();
                //homenetwork.hidefooter();
                //zhenqi2.hidefooter();
				//alert("AAAA:"+document.referrer);
				//window.history.reload();
                window.history.go(-1);
				
			//	window.location.href = document.referrer;

            }
        }
    }, false);
}



var App = (function(){
    var pages = {};
    var run = function(){
        $.each(pages,function(k,v){
            var sectionId = '#'+k+'_section';
            $('body').delegate(sectionId,'pageinit',function(){
                v.init && v.init.call(v);
                               
            });
            $('body').delegate(sectionId,'pageshow',function(e,isBack){
                //页面加载的时候都会执行
                v.show && v.show.call(v);
                //后退时不执行
                if(!isBack && v.load){
                    v.load.call(v);
                }
            });
        });
		J.Transition.add('flip','slideLeftOut','flipOut','slideRightOut','flipIn');
        Jingle.launch({
            showWelcome : false,
            welcomeSlideChange : function(i){
                switch(i){
                    case 0 :
                        J.anim('#welcome_jingle','welcome_jinlge',1000);
                        break;
                    case 1 :
                        $('#r_head,#r_body,#r_hand_left,#r_hand_right,#r_foot_left,#r_foot_right').hide()
                        J.anim($('#r_head').show(),'r_head',500,function(){
                            J.anim($('#r_body').show(),'r_body',1200,function(){
                                J.anim($('#r_hand_left').show(),'r_hand_l',500);
                                J.anim($('#r_hand_right').show(),'r_hand_r',500,function(){
                                    J.anim($('#r_foot_left').show(),'r_foot_l',500);
                                    J.anim($('#r_foot_right').show(),'r_foot_r',500,function(){
                                        J.anim('#welcome_robot','welcome_robot',2000);
                                    });
                                });
                            });
                        });
                        break;
                    case 2 :
                        $('#w_box_1,#w_box_2,#w_box_3,#w_box_4').hide()
                        J.anim($('#w_box_1').show(),'box_l',500,function(){
                            J.anim($('#w_box_2').show(),'box_r',500,function(){
                                J.anim($('#w_box_3').show(),'box_l',500,function(){
                                    J.anim($('#w_box_4').show(),'box_r',500);
                                });
                            });
                        });
                        break;
                }
            },
            showPageLoading : true,
            remotePage : {
                '#about_section' : 'remote/about_section.html'
            }
        });
       
    };
    var page = function(id,factory){
        return ((id && factory)?_addPage:_getPage).call(this,id,factory);
    }
    var _addPage = function(id,factory){
        pages[id] = new factory();
    };
    var _getPage = function(id){
        return pages[id];
    }
    //动态计算chart canvas的高度，宽度，以适配终端界面
    var calcChartOffset = function(){
        return {
            height : $(document).height() - 44 - 30 -60,
            width : $(document).width()
        }

    }
    return {
        run : run,
        page : page,
        calcChartOffset : calcChartOffset
    }
}());

/*zl modify index*/
App.page('index',function(){

    this.init = function(){
        //$('#btn_show_welcome').on('tap', J.Welcome.show)
        //$('ul.list').attr('background','url(../../image/watermark3.png)');
        //$('ul.list').css('background','url("..\/..\/image/watermark3.png")');
        //J.Router.goTo('#jiakuan_section');
        J.Router.goTo('#jiakuan3_section');
        //J.Router.goTo('#jiakuanfirst_section');
    }
})

App.page('index1',function(){

    this.init = function(){
        J.Router.goTo('#zhenqi3_section');
    }
})


App.page('index2',function(){

    this.init = function(){
       
       var $target,$ctarget,$btarget,$dtarget;
            $('#index2_article').on('tap',function(e){
                $target = $(e.target);
                $btarget = $(e.target);

                $ctarget = $target.closest('li');
                if(!$target.is('li') && $ctarget.length > 0){
                         $target = $ctarget;
                }

                if($target.is('li')){

                }

                
            });//on

    }
})

App.page('ribao1',function(){

    this.init = function(){
       
       $('#ribao1_section article').on('articleshow',function(){
            slider = new J.Slider({
                selector : '#ribao1_slider',
                showDots : false,
                onBeforeSlide : function(){
                    return true;
                },
                onAfterSlide : function(i){
                    //alert(i);
                }
            });
        });
        $('#slider_prev').tap(function(){slider.prev()});
        $('#slider_next').tap(function(){slider.next()});

        $('#btn_ribao1_back').tap(function(){

                window.history.go(-1);
        });

    }
})


App.page('ribao2',function(){
         
         this.init = function(){
         
         $('#ribao2_section article').on('articleshow',function(){
                                         slider = new J.Slider({
                                                               selector : '#ribao2_slider',
                                                               showDots : false,
                                                               onBeforeSlide : function(){
                                                               return true;
                                                               },
                                                               onAfterSlide : function(i){
                                                               //alert(i);
                                                               }
                                                               });
                                         });
         $('#slider_prev').tap(function(){slider.prev()});
         $('#slider_next').tap(function(){slider.next()});
         
         $('#btn_ribao2_back').tap(function(){
                                   
                                   window.history.go(-1);
                                   });
         
         }
})
App.page('ribao21',function(){
         
         this.init = function(){
         
         $('#ribao21_section article').on('articleshow',function(){
                                         slider = new J.Slider({
                                                               selector : '#ribao21_slider',
                                                               showDots : false,
                                                               onBeforeSlide : function(){
                                                               return true;
                                                               },
                                                               onAfterSlide : function(i){
                                                               //alert(i);
                                                               }
                                                               });
                                         });
         $('#slider_prev').tap(function(){slider.prev()});
         $('#slider_next').tap(function(){slider.next()});
         
         $('#btn_ribao21_back').tap(function(){
                                   
                                   window.history.go(-1);
                                   });
         
         }
 })
App.page('ribao22',function(){
         
         this.init = function(){
         
         $('#ribao22_section article').on('articleshow',function(){
                                         slider = new J.Slider({
                                                               selector : '#ribao22_slider',
                                                               showDots : false,
                                                               onBeforeSlide : function(){
                                                               return true;
                                                               },
                                                               onAfterSlide : function(i){
                                                               //alert(i);
                                                               }
                                                               });
                                         });
         $('#slider_prev').tap(function(){slider.prev()});
         $('#slider_next').tap(function(){slider.next()});
         
         $('#btn_ribao22_back').tap(function(){
                                   
                                   window.history.go(-1);
                                   });
         
         }
 })
App.page('ribao3',function(){
         
         this.init = function(){
         
         $('#ribao3_section article').on('articleshow',function(){
                                          slider = new J.Slider({
                                                                selector : '#ribao3_slider',
                                                                showDots : false,
                                                                onBeforeSlide : function(){
                                                                return true;
                                                                },
                                                                onAfterSlide : function(i){
                                                                //alert(i);
                                                                }
                                                                });
                                          });
         $('#slider_prev').tap(function(){slider.prev()});
         $('#slider_next').tap(function(){slider.next()});
         
         $('#btn_ribao3_back').tap(function(){
                                    
                                    window.history.go(-1);
                                    });
         
         }
})
App.page('ribao31',function(){
         
         this.init = function(){
         
         $('#ribao31_section article').on('articleshow',function(){
                                         slider = new J.Slider({
                                                               selector : '#ribao31_slider',
                                                               showDots : false,
                                                               onBeforeSlide : function(){
                                                               return true;
                                                               },
                                                               onAfterSlide : function(i){
                                                               //alert(i);
                                                               }
                                                               });
                                         });
         $('#slider_prev').tap(function(){slider.prev()});
         $('#slider_next').tap(function(){slider.next()});
         
         $('#btn_ribao31_back').tap(function(){
                                   
                                   window.history.go(-1);
                                   });
         
         }
})

App.page('ribao4',function(){

    this.init = function(){
       
       $('#ribao4_section article').on('articleshow',function(){
            slider = new J.Slider({
                selector : '#ribao4_slider',
                showDots : false,
                onBeforeSlide : function(){
                    return true;
                },
                onAfterSlide : function(i){
                    //alert(i);
                }
            });
        });
        $('#slider_prev').tap(function(){slider.prev()});
        $('#slider_next').tap(function(){slider.next()});

        $('#btn_ribao4_back').tap(function(){

                window.history.go(-1);
        });

    }
})

App.page('ribao41',function(){

    this.init = function(){
       
       $('#ribao41_section article').on('articleshow',function(){
            slider = new J.Slider({
                selector : '#ribao41_slider',
                showDots : false,
                onBeforeSlide : function(){
                    return true;
                },
                onAfterSlide : function(i){
                    //alert(i);
                }
            });
        });
        $('#slider_prev').tap(function(){slider.prev()});
        $('#slider_next').tap(function(){slider.next()});

        $('#btn_ribao41_back').tap(function(){

                window.history.go(-1);
        });

    }
})

App.page('ribao5',function(){

    this.init = function(){
       
       $('#ribao5_section article').on('articleshow',function(){
            slider = new J.Slider({
                selector : '#ribao5_slider',
                showDots : false,
                onBeforeSlide : function(){
                    return true;
                },
                onAfterSlide : function(i){
                    //alert(i);
                }
            });
        });
        $('#slider_prev').tap(function(){slider.prev()});
        $('#slider_next').tap(function(){slider.next()});

        $('#btn_ribao5_back').tap(function(){

                window.history.go(-1);
        });

    }
})

App.page('jifeiliuliang',function(){

    this.init = function(){
       
       $('#jifeiliuliang_section article').on('articleshow',function(){
            slider = new J.Slider({
                selector : '#jifeiliuliang_slider',
                showDots : false,
                onBeforeSlide : function(){
                    return true;
                },
                onAfterSlide : function(i){
                    //alert(i);
                }
            });
        });
        $('#slider_prev').tap(function(){slider.prev()});
        $('#slider_next').tap(function(){slider.next()});

        $('#btn_jifeiliuliang_back').tap(function(){

                window.history.go(-1);
        });

    }
})

App.page('flowManager',function(){

    this.init = function(){
       
       $('#flowManager_section article').on('articleshow',function(){
            slider = new J.Slider({
                selector : '#flowManager_slider',
                showDots : false,
                onBeforeSlide : function(){
                    return true;
                },
                onAfterSlide : function(i){
                    //alert(i);
                }
            });
        });
        $('#slider_prev').tap(function(){slider.prev()});
        $('#slider_next').tap(function(){slider.next()});

        $('#btn_flowManager_back').tap(function(){

                window.history.go(-1);
        });

    }
})

App.page('realNameMakeup',function(){

    this.init = function(){
       
       $('#realNameMakeup_section article').on('articleshow',function(){
            slider = new J.Slider({
                selector : '#realNameMakeup_slider',
                showDots : false,
                onBeforeSlide : function(){
                    return true;
                },
                onAfterSlide : function(i){
                    //alert(i);
                }
            });
        });
        $('#slider_prev').tap(function(){slider.prev()});
        $('#slider_next').tap(function(){slider.next()});

        $('#btn_realNameMakeup_back').tap(function(){

                window.history.go(-1);
        });

    }
})

App.page('groupProduction',function(){

    this.init = function(){
       
       $('#groupProduction_section article').on('articleshow',function(){
            slider = new J.Slider({
                selector : '#groupproduction_slider',
                showDots : false,
                onBeforeSlide : function(){
                    return true;
                },
                onAfterSlide : function(i){
                    //alert(i);
                }
            });
        });
        $('#slider_prev').tap(function(){slider.prev()});
        $('#slider_next').tap(function(){slider.next()});

        $('#btn_groupProduction_back').tap(function(){

                window.history.go(-1);
        });

    }
})

/***
  zl add 
***/
App.page('jiakuan',function(){

    this.init = function(){

            //jiakuan.showtimearea('#jiakuan_article');

            jiakuan.getdata();

           $('#btn_menuz1').tap(function(){
            J.popover('<ul class="list">\
                <li style="color:#000;">\
                <button class="block"  id="btn_alert">时间</button>\
                </li>\
                <li style="color:#000;">\
                <button class="block"  data-target="test">地域</button>\
                </li></ul>',
                {top:'64px',left:'50%',right:'0%'},
                'top');
           });

           $('#jiankuan_back').tap(function(){
                console.log('返回'); 
                //jiakuan.btnback();
                jiakuan.hidefooter();
                //J.Router.goTo('#jiakuan3_section');
                window.history.go(-1);
           });

           $('#btn_zshare').tap(function(){
                console.log('分享');
                jiakuan.btnshare();
           });

           $('#btn_zrefresh').tap(function(){
                console.log('刷新页面');
                $('#jiakuan_article ul.list').empty();
                jiakuan.getdata();
           });

           
            $('#jiakuan_article').swipeDown(function(){
                console.log('向下滑动事件');
                jiakuan.showfooter();
                //jiakuan.showtimearea('#jiakuan_article');
            });

            $('#jiakuan_article').swipeUp(function(){

                console.log('向上滑动事件');
                jiakuan.hidefooter();
                //jiakuan.hidetimearea('#jiakuan_article');
            });    

            var $target,$ctarget,$btarget,$dtarget;
            $('#jiakuan_article').on('tap',function(e){
                $target = $(e.target);
                $btarget = $(e.target);

                $ctarget = $target.closest('li');
                if(!$target.is('li') && $ctarget.length > 0){
                         $target = $ctarget;
                }

                if($target.is('li')){
                        var kpicodeStr = $target.data('kpicode');
                        var kpinameStr = $target.data('kpiname');

                        var areaStr = $target.data('area');
                        var areacodeStr = $target.data('aarea');
                        var timeStr = $target.data('time');
                        var timedataStr = $target.data('tda');
                        //var areaName = $target.data('code');
                        // console.log( '指标选择');
                        // console.log( kpicodeStr);
                        // console.log( kpinameStr);
                        // console.log( areaStr);
                        // console.log( areacodeStr);
                        // console.log( timeStr);
                        // console.log( timedataStr);

                        if( kpicodeStr != undefined && kpicodeStr != "" ){
                            var homedat = { 
                            kpicode:String( kpicodeStr ),  
                            kpiname:String( kpinameStr ),
                            areacode:String( areacodeStr ),
                            areaname:String( areaStr ),
                            time:String( timedataStr ),
                            timeform:String( timeStr ),
                            showtype:"1",
                            areashowtype:"1",
                            number:"7"
                        };

                        homenetwork.zinit( homedat );
                        homenetwork.getdata();
                        J.closePopup();
                        J.Router.goTo('#home_network_section');

                        }//endif
                }

                $dtarget = $btarget.closest('a');
                if(!$btarget.is('a') && $dtarget.length > 0){
                         $btarget = $dtarget;
                }
                if($btarget.is('a')){
                        var idstr =  $btarget.data('ids');
                        var areaStr2 = $btarget.data('area');

                    if( idstr == '1'){
                        console.log( '时间选择器');
                        console.log( areaStr2);

                        var d = new Date();
                        var year = d.getFullYear();
                        var month = d.getMonth();
                        var day = d.getDate();

                        var datestr = jiakuan.getdatedata();
                        if( datestr.length >=8 ){
                            year = Number(datestr.substring(0,4) );
                            month =  Number( datestr.substring(4,6) );
                           if( month >0 ){ month = month -1;}
                            day = Number( datestr.substring(6,8) );
                        }

                        //console.log( '=='+ year + month + day );

                        J.popup({
                            html: '<div id="popup_calendar"></div>',
                            pos: 'center',
                            backgroundOpacity: 0.4,
                            showCloseBtn: false,
                            onShow: function() {
                                 new J.Calendar('#popup_calendar', {
                                    date: new Date(year,month,day),
                                    onSelect: function(date) {
                                    console.log('第一页时间选择' + date);
                                    $('#ztime span').text(date);
                                    jiakuan.setdate(date);
                                    jiakuan.getdatabytime();
                                    J.closePopup(); }
                                    });
                            }
                        });
                    }else if( idstr == '2' ){
                            console.log( '地域选择器');
                            console.log( areaStr2);
                            //J.showMask();
                            var areacode = jiakuan.getareacode();
                            jiakuan.getareadata( areacode , { onSelect: function( param ){
                            J.popup({
                                html: '<div id="popup_area"></div>',
                                pos : 'center',
                                showCloseBtn : false,
                                onShow: function(){ 
                                    new J.Area('#popup_area',{
                                                areadata: param,
                                                onSelect:function( areaCode ,areaName ){
                                                console.log('地域选择 ' + areaCode + areaName);
                                                $('#zarea span').text(areaName);
                                                var areadat = { AREA_CODE:areaCode,  AREA_NAME:areaName };
                                                jiakuan.setarea(areaName);
                                                jiakuan.setareacode( areaCode );
                                                jiakuan.getdatabyarea();
                                                J.closePopup();}
                                         })
                                }
                            })
                        }});
                    }//end else if


                }//if
            });//on
    }
});

/*zl add */
App.page('home_network',function(){

    this.init = function(){
        
        //console.log('adfadf');

           $('#btn_home_time').tap(function(){
              console.log('日、月控件');
              homenetwork.showD3();
           })

           /*分享按钮*/
           $('#btn_home_share').tap(function(){
                console.log('分享');
                homenetwork.btnshare();
           });

           /* 刷新按钮*/
           $('#btn_home_refresh').tap(function(){
                console.log('刷新页面');
                //$('#home_network_article ul.list').empty();
                homenetwork.getdata();
                //homenetwork.getdata( 'DBN150011','HB.WH','D','20160825','2','2','7' );
           });

            $('#home_network_article').swipeDown(function(){
                console.log('向下滑动事件');
                homenetwork.showfooter();
                //jiakuan.showtimearea('#home_network_article');
            });

            $('#home_network_article').swipeUp(function(){
                console.log('向上滑动事件');
                homenetwork.hidefooter();
                //jiakuan.hidetimearea('#home_network_article');
            });    

            $('#btn_home_back').tap(function(){
                 homenetwork.hidefooter();
                 //J.Router.goTo('#jiakuan_section');
                 window.history.go(-1);
            })

            var $btarget,$dtarget;
            $('#home_network_article').on('tap',function(e){
                //$target = $(e.target);
                $btarget = $(e.target);

                $dtarget = $btarget.closest('a');
                if(!$btarget.is('a') && $dtarget.length > 0){
                         $btarget = $dtarget;
                }
                if($btarget.is('a')){
                        var idstr =  $btarget.data('ids');
                        var areaStr2 = $btarget.data('area');

                    if( idstr == '1'){
                        console.log( '时间选择器');
                        console.log( areaStr2);

                        var d = new Date();
                        var year = d.getFullYear();
                        var month = d.getMonth();
                        var day = d.getDate();

                        var datestr = homenetwork.getdatedata();
                        if( datestr.length >=8 ){
                            year = Number(datestr.substring(0,4) );
                            month =  Number( datestr.substring(4,6) );
                           if( month >0 ){ month = month -1;}
                            day = Number( datestr.substring(6,8) );
                        }

                        J.popup({
                            html: '<div id="popup_calendar_home"></div>',
                            pos: 'center',
                            backgroundOpacity: 0.4,
                            showCloseBtn: false,
                            onShow: function() {
                                 new J.Calendar('#popup_calendar_home', {
                                    date: new Date(year, month, day),
                                    onSelect: function(date) {
                                    console.log('第一页时间选择' + date);
                                    $('#htime span').text(date);
                                    homenetwork.setdate(date);
                                    homenetwork.getdatabytime();
                                    J.closePopup(); }
                                    });
                            }
                        });
                    }else if( idstr == '2' ){
                            console.log( '地域选择器');
                            console.log( areaStr2);

                            var areacode = homenetwork.getareacode();
                            jiakuan.getareadata( areacode , { onSelect: function( param ){
                            J.popup({
                                html: '<div id="popup_area_home"></div>',
                                pos : 'center',
                                showCloseBtn : false,
                                onShow: function(){ 
                                    new J.Area('#popup_area_home',{
                                                areadata: param,
                                                onSelect:function( areaCode ,areaName ){
                                                console.log('地域选择 ' + areaCode + areaName);
                                                $('#harea span').text(areaName);
                                                var areadat = { AREA_CODE:areaCode,  AREA_NAME:areaName };
                                                homenetwork.setarea(areaName);
                                                homenetwork.setareacode( areaCode );
                                                homenetwork.getdatabyarea();
                                                J.closePopup();}
                                         })
                                }
                            })
                        }});
                    }//end else if


                }//if
            });//on

    }
});

App.page('zhenqi',function(){

    this.init = function(){
        //$('#btn_zhenqiriyue').addClass('icon icon-paragraph-right');
        //zhenqi.testdata();
        var dataflag = zhenqi.getDflag();
        if( dataflag == 'D' ){
                $('#btn_zhenqiriyue').attr('class','icon icon-btn_ri');
        }else{
                $('#btn_zhenqiriyue').attr('class','icon icon-btn_yue');              
        }
        zhenqi.getdata();
        J.Scroll('#zhenqi_section',{hScroll:true,hScrollbar : false});

        /*返回*/
        $('#zhenqi_back').tap(function(){
                 //zhenqi.btnback();
             zhenqi.hidefooter();
            //J.Router.goTo('#zhenqi3_section');
             window.history.go(-1);
        })

         /*分享按钮*/
        $('#btn_zhenqishare').tap(function(){
                console.log('分享');
                zhenqi.btnshare();
        });

         /*日月按钮*/
        $('#btn_zhenqiriyue').tap(function(){
                console.log('日月按钮切换');
                $('#zhenqi_article ul.list').empty();

                    var dataflag = zhenqi.getDflag();

                if( dataflag == 'D' ){
                    zhenqi.hidefooter();
                    $('#btn_zhenqiriyue').attr('class','icon icon-btn_yue');
                    zhenqi.inityuedata();
                }else{
                    zhenqi.hidefooter();
                     $('#btn_zhenqiriyue').attr('class','icon icon-btn_ri');
                     zhenqi.initdaydata();
                    
                }
                
                zhenqi.getdata();
                J.Scroll('#zhenqi_section',{hScroll:true,hScrollbar : false});
        });

           /* 刷新按钮*/
        $('#btn_zhenqirefresh').tap(function(){
                console.log('刷新页面');
                $('#zhenqi_article ul.list').empty();
                zhenqi.getdata();
        });

        $('#zhenqi_article').swipeDown(function(){
                console.log('向下滑动事件');
                zhenqi.showfooter();
        });

        $('#zhenqi_article').swipeUp(function(){
                console.log('向上滑动事件');
                zhenqi.hidefooter();
        });   

        var $target,$ctarget,$btarget,$dtarget;
        $('#zhenqi_article').on('tap',function(e){
            $target = $(e.target);
            $btarget = $(e.target);

            $ctarget = $target.closest('li');
            if(!$target.is('li') && $ctarget.length > 0){
                         $target = $ctarget;
            }

            if($target.is('li')){
                var kpicodeStr = $target.data('kpicode');
                var kpinameStr = $target.data('kpiname');

                var areaStr = $target.data('area');
                var areacodeStr = $target.data('aarea');
                var timeStr = $target.data('time');
                var timedataStr = $target.data('tda');
                var unitstr = $target.data('unit');
                var Dflag = zhenqi.getDflag();

                var homedat = { 
                        kpicode:String( kpicodeStr ),  
                        kpiname:String( kpinameStr ),
                        areacode:String( areacodeStr ),
                        areaname:String( areaStr ),
                        time:String( timedataStr ),
                        timeform:String( timeStr ),
                        dateType:String(Dflag),
                        unit:String(unitstr),
                        number:"7"
                };

                //console.log( homedat );

                if( kpicodeStr != undefined && kpicodeStr != "" ){

                    //跳转第二页
                    J.closePopup();
                    zhenqi2.zinit( homedat );
                    zhenqi2.getdata();
                    J.Router.goTo('#zhenqi2_section');
                }

            }//if

            $dtarget = $btarget.closest('a');
            if(!$btarget.is('a') && $dtarget.length > 0){
                         $btarget = $dtarget;
            }
            if($btarget.is('a')){
                var idstr =  $btarget.data('ids');
                var areaStr2 = $btarget.data('area');

            if( idstr == '1'){
                console.log( '时间选择器');
                console.log( areaStr2);

                var d = new Date();
                var year = d.getFullYear();
                var month = d.getMonth();
                var day = d.getDate();

                var datestr = zhenqi.getdatedata();

                console.log( '--' );
                console.log( datestr );
                console.log( '--' );

                    if( datestr.length >=8 ){
                        year = Number(datestr.substring(0,4) );
                         month =  Number( datestr.substring(4,6) );
                        if( month >0 ){ month = month -1;}
                        day = Number( datestr.substring(6,8) );
                    }else if(datestr.length >=6 ){ //月
                            year = Number(datestr.substring(0,4) );
                            month =  Number( datestr.substring(4,6) );
                           day = Number( '0' );
                    }

                    var Dflag = zhenqi.getDflag();
                    if(Dflag == 'D'){
                        J.popup({
                            html: '<div id="popup_calendar_zhenqi"></div>',
                            pos: 'center',
                            backgroundOpacity: 0.4,
                            showCloseBtn: false,
                            onShow: function() {
                                 new J.Calendar('#popup_calendar_zhenqi', {
                                    date: new Date(year, month, day),
                                    onSelect: function(date) {
                                    console.log('政企时间选择' + date);
                                    $('#zhenqitime span').text(date);
                                     zhenqi.setdate(date);
                                     zhenqi.gettimeareadata();
                                     J.closePopup(); }
                                    });
                            }
                        });
                    }else{
                        J.popup({
                            html: '<div id="popup_zhenqiMonth"></div>',
                            pos: 'center',
                            backgroundOpacity: 0.4,
                            showCloseBtn: false,
                            onShow: function() {
                            new J.CalendarMonth('#popup_zhenqiMonth', {
                                date: new Date(year, month, day),
                                onSelect: function(date) {
                                console.log('政企时间选择' + date);
                                $('#zhenqitime span').text(date);
                                zhenqi.setdate(date);
                                zhenqi.gettimeareadata();
                                J.closePopup(); }
                                });
                            }
                        });
                    }

            }else if( idstr == '2' ){
                    console.log( '地域选择器');
                    console.log( areaStr2);

                    var areacode = zhenqi.getareacode();
                    jiakuan.getareadata( areacode , { onSelect: function( param ){
                        J.popup({
                            html: '<div id="popup_area"></div>',
                            pos : 'center',
                            showCloseBtn : false,
                            onShow: function(){ 
                                 new J.Area('#popup_area',{
                                     areadata: param,
                                    onSelect:function( areaCode ,areaName ){
                                    console.log('政企地域选择 ' + areaCode + areaName);
                                    $('#zhenqiarea span').text(areaName);
                                    var areadat = { AREA_CODE:areaCode,  AREA_NAME:areaName };
                                    zhenqi.setarea(areaName);
                                    zhenqi.setareacode( areaCode );
                                    zhenqi.gettimeareadata();
                                    J.closePopup();}
                                    })
                                }
                            })
                        }});
            }//end else if
            }//if

        });//on

    }
});

//政企第二个页面
App.page('zhenqi2',function(){

    this.init = function(){

        $('#btn_zhenqi2_back').tap(function(){
            zhenqi2.hidefooter();
            //J.Router.goTo('#zhenqi_section');
            window.history.go(-1);
        })

        /*分享按钮*/
        $('#btn_zhenqi2_share').tap(function(){
            zhenqi2.btnshare();
        });

        /* 刷新按钮*/
        $('#btn_zhenqi2_refresh').tap(function(){
            zhenqi2.getdata();
        });

        $('#zhenqi2_article').swipeDown(function(){
            zhenqi2.showfooter();
        });

        $('#zhenqi2_article').swipeUp(function(){
            zhenqi2.hidefooter();
        });   

        var $btarget,$dtarget;
        $('#zhenqi2_article').on('tap',function(e){

                $btarget = $(e.target);
                $dtarget = $btarget.closest('a');
                if(!$btarget.is('a') && $dtarget.length > 0){
                         $btarget = $dtarget;
                }
                if($btarget.is('a')){
                        var idstr =  $btarget.data('ids');
                        var areaStr2 = $btarget.data('area');

                    if( idstr == '1'){
                        console.log( '时间选择器');
                        console.log( areaStr2);

                        var d = new Date();
                        var year = d.getFullYear();
                        var month = d.getMonth();
                        var day = d.getDate();

                        var datestr = zhenqi2.getdatedata();
                        if( datestr.length >=8 ){
                            year = Number(datestr.substring(0,4) );
                            month =  Number( datestr.substring(4,6) );
                           if( month >0 ){ month = month -1;}
                            day = Number( datestr.substring(6,8) );
                        }else if( datestr.length >=6  ){ //月
                            year = Number(datestr.substring(0,4) );
                            month =  Number( datestr.substring(4,6) );
                            day = Number('0');
                        }

                    var Dflag = zhenqi2.getDflag();
                    if(Dflag == 'D'){
                        J.popup({
                            html: '<div id="popup_calendar_zhen"></div>',
                            pos: 'center',
                            backgroundOpacity: 0.4,
                            showCloseBtn: false,
                            onShow: function() {
                                 new J.Calendar('#popup_calendar_zhen', {
                                    date: new Date(year, month, day),
                                    onSelect: function(date) {
                                    console.log('政企2时间选择' + date);
                                    $('#zhen2time span').text(date);
                                     zhenqi2.setdate(date);
                                     zhenqi2.getdata();
                                     J.closePopup(); }
                                    });
                            }
                        });
                    }else{
                        J.popup({
                            html: '<div id="popup_zhenqi2Month"></div>',
                            pos: 'center',
                            backgroundOpacity: 0.4,
                            showCloseBtn: false,
                            onShow: function() {
                            new J.CalendarMonth('#popup_zhenqi2Month', {
                                date: new Date(year, month, day),
                                onSelect: function(date) {
                                console.log('政企时间选择' + date);
                                $('#zhen2time span').text(date);
                                zhenqi2.setdate(date);
                                zhenqi2.getdata();
                                J.closePopup(); }
                                });
                            }
                        });
                        }
                    }else if( idstr == '2' ){
                            console.log( '地域选择器');
                            console.log( areaStr2);
                            var areacode = zhenqi2.getareacode();
                            jiakuan.getareadata( areacode , { onSelect: function( param ){
                            J.popup({
                                html: '<div id="popup_area_zheqi2"></div>',
                                pos : 'center',
                                showCloseBtn : false,
                                onShow: function(){ 
                                    new J.Area('#popup_area_zheqi2',{
                                                areadata: param,
                                                onSelect:function( areaCode ,areaName ){
                                                console.log('地域选择 ' + areaCode + areaName);
                                                $('#zhen2area span').text(areaName);
                                                var areadat = { AREA_CODE:areaCode,  AREA_NAME:areaName };
                                                zhenqi2.setarea(areaName);
                                                zhenqi2.setareacode( areaCode );
                                                zhenqi2.getdata();
                                                J.closePopup();}
                                         })
                                }
                            })
                        }});
                    }//end else if
                }//if
            });//on
    }
});

App.page('zhenqi3',function(){
    this.init = function(){

        var flag = true;
        var slider;
        $('#zhenqi3_section article').on('articleshow',function(){
            slider = new J.Slider({
                selector : '#zhenqi3_slider',
                showDots : false,
                onBeforeSlide : function(){
                    return true;
                },
                onAfterSlide : function(i){
                    //alert(i);
                }
            });
        });
        $('#slider_prev').tap(function(){slider.prev()});
        $('#slider_next').tap(function(){slider.next()});

        zhenqi3.getdata();
        //zhenqi3.testdata();
        /*返回*/
        $('#btn_zhenqi3_back').tap(function(){
            zhenqi3.btnback();
        })

         /*分享按钮*/
        $('#btn_zhenqi3_share').tap(function(){
             zhenqi3.btnshare();
        });
           /* 刷新按钮*/
        $('#btn_zhenqi3_refresh').tap(function(){
                 $('#zhenqi3page1').empty();
                 $('#zhenqi3page2').empty();
                 $('#zhenqi3page3').empty();
                 $('#zhenqi3page4').empty();
                zhenqi3.getdata();
                //zhenqi3.testdata();
        });

         /*日月按钮*/
        $('#btn_zhenqi3riyue').tap(function(){
                console.log('日月按钮切换');
                $('#zhenqi3_article ul.list').empty();

                if( flag ){
                      zhenqi3.hidefooter();
                    $('#btn_zhenqi3riyue').attr('class','icon icon-btn_yue');
                     zhenqi3.setriyuedata('M');
                }else{
                     zhenqi3.hidefooter();
                     $('#btn_zhenqi3riyue').attr('class','icon icon-btn_ri');
                     zhenqi3.setriyuedata('D');
                    
                }
                flag = !flag;

                zhenqi3.getdata();
                J.Scroll('#zhenqi3_section',{hScroll:true,hScrollbar : false});
        });


        $('#zhenqi3_article').swipeDown(function(){
            console.log('上滑');
            zhenqi3.showfooter();
        });

        $('#zhenqi3_article').swipeUp(function(){
            console.log('下滑');
            zhenqi3.hidefooter();
        }); 

        var $target,$ctarget, $btarget,$dtarget;
        $('#zhenqi3_article').on('tap',function(e){
                $target = $(e.target);
                $btarget = $(e.target);

            $ctarget = $target.closest('li');
            if(!$target.is('li') && $ctarget.length > 0){
                         $target = $ctarget;
            }

            if($target.is('li')){

                var idstr =  $target.data('ids');

                console.log( idstr );

                if( idstr != '0' ){
                    //跳转第二页;
                    J.closePopup();

                    var datatime = zhenqi3.getdatedata();
                    zhenqi.setdatedata(datatime);

                    var dataflag = zhenqi3.getDflag();
                    zhenqi.setDflag(dataflag);

                    zhenqi.getdata();
                    J.Router.goTo('#zhenqi_section');
                }

            }//if

                $dtarget = $btarget.closest('a');
                if(!$btarget.is('a') && $dtarget.length > 0){
                         $btarget = $dtarget;
                }
                if($btarget.is('a')){
                        var idstr =  $btarget.data('ids');

                    if( idstr == '1'){

                        var d = new Date();
                        var year = d.getFullYear();
                        var month = d.getMonth();
                        var day = d.getDate();

                        var datestr = zhenqi3.getdatedata();

                        console.log( datestr );

                        if( datestr.length >=8 ){
                            year = Number(datestr.substring(0,4) );
                            month =  Number( datestr.substring(4,6) );
                           if( month >0 ){ month = month -1;}
                            day = Number( datestr.substring(6,8) );
                        }else if(datestr.length >=6 ){ //月
                            year = Number(datestr.substring(0,4) );
                            month =  Number( datestr.substring(4,6) );
                           day = Number( '0' );
                        }

                    var Dflag = zhenqi3.getDflag();

                    if(Dflag == 'D'){
                        J.popup({
                            html: '<div id="popup_calendar_zhen3"></div>',
                            pos: 'center',
                            backgroundOpacity: 0.4,
                            showCloseBtn: false,
                            onShow: function() {
                                 new J.Calendar('#popup_calendar_zhen3', {
                                    date: new Date(year, month, day),
                                    onSelect: function(date) {
                                    console.log('政企3时间选择' + date);
                                    $('#zhenqi3time span').text(date);

                                     zhenqi3.setdate(date);
                                     zhenqi3.getdata();

                                     J.closePopup(); }
                                    });
                            }
                        });
                    }else{
                        J.popup({
                            html: '<div id="popup_zhenqi3Monthz"></div>',
                            pos: 'center',
                            backgroundOpacity: 0.4,
                            showCloseBtn: false,
                            onShow: function() {
                            new J.CalendarMonth('#popup_zhenqi3Monthz', {
                                date: new Date(year, month, day),
                                onSelect: function(date) {
                                console.log('政企3时间选择' + date);
                                $('#zhenqi3time span').text(date);

                                zhenqi3.setdate(date);
                                zhenqi3.getdata();

                                J.closePopup(); }
                                });
                            }
                        });
                        }
                    }else if( idstr == '2' ){
                            var areacode = zhenqi3.getareacode();
                            jiakuan.getareadata( areacode , { onSelect: function( param ){
                            J.popup({
                                html: '<div id="popup_area_zheqi3"></div>',
                                pos : 'center',
                                showCloseBtn : false,
                                onShow: function(){ 
                                    new J.Area('#popup_area_zheqi3',{
                                                areadata: param,
                                                onSelect:function( areaCode ,areaName ){
                                                console.log('地域选择 ' + areaCode + areaName);
                                                $('#zhenqi3area span').text(areaName);

                                                zhenqi3.setarea(areaName);
                                                zhenqi3.setareacode( areaCode );
                                                zhenqi3.getdata();

                                                J.closePopup();}
                                         })
                                }
                            })
                        }});
                    }//end else if
                }//if
            });//on
    }
});

App.page('jiakuan3',function(){
    this.init = function(){
        var flag = true;
        var slider;
        $('#jiakuan3_section article').on('articleshow',function(){
            slider = new J.Slider({
                selector : '#jiakuan3_slider',
                showDots : false,
                onBeforeSlide : function(){
                    return true;
                },
                onAfterSlide : function(i){
                    //alert(i);
                }
            });
        });
        $('#slider_prev').tap(function(){slider.prev()});
        $('#slider_next').tap(function(){slider.next()});

        jiakuan3.getdata();
        //jiakuan3.testdata();
        /*返回*/
        $('#btn_jiakuan3_back').tap(function(){
            jiakuan3.btnback();
        })

         /*分享按钮*/
        $('#btn_jiakuan3_share').tap(function(){
             jiakuan3.btnshare();
        });
           /* 刷新按钮*/
        $('#btn_jiakuan3_refresh').tap(function(){
                 $('#jiakuan3page1').empty();
                 $('#jiakuan3page2').empty();
                jiakuan3.getdata();
                //jiakuan3.testdata();
        });

         /*日月按钮*/
        $('#btn_jiakuan3riyue').tap(function(){
                console.log('日月按钮切换');
                $('#jiakuan3_article ul.list').empty();

                if( flag ){
                      jiakuan3.hidefooter();
                    $('#btn_jiakuan3riyue').attr('class','icon icon-btn_yue');
                     jiakuan3.setriyuedata('M');
                }else{
                     jiakuan3.hidefooter();
                     $('#btn_jiakuan3riyue').attr('class','icon icon-btn_ri');
                     jiakuan3.setriyuedata('D');
                    
                }
                flag = !flag;

                jiakuan3.getdata();
                J.Scroll('#jiakuan3_section',{hScroll:true,hScrollbar : false});
        });


        $('#jiakuan3_article').swipeDown(function(){
            console.log('上滑');
            jiakuan3.showfooter();
        });

        $('#jiakuan3_article').swipeUp(function(){
            console.log('下滑');
            jiakuan3.hidefooter();
        }); 

        var $target,$ctarget, $btarget,$dtarget;
        $('#jiakuan3_article').on('tap',function(e){
                $target = $(e.target);
                $btarget = $(e.target);

            $ctarget = $target.closest('li');
            if(!$target.is('li') && $ctarget.length > 0){
                         $target = $ctarget;
            }

            if($target.is('li')){

                var idstr =  $target.data('ids');

                console.log( idstr );

                if( idstr != '0' ){

                    //跳转第二页;
                    J.closePopup();

                    var datatime = jiakuan3.getdatedata();
                    jiakuan.setdatedata(datatime);

                    J.Router.goTo('#jiakuan_section');
                }
            }//if

                $dtarget = $btarget.closest('a');
                if(!$btarget.is('a') && $dtarget.length > 0){
                         $btarget = $dtarget;
                }
                if($btarget.is('a')){
                        var idstr =  $btarget.data('ids');

                    if( idstr == '1'){

                        var d = new Date();
                        var year = d.getFullYear();
                        var month = d.getMonth();
                        var day = d.getDate();

                        var datestr = jiakuan3.getdatedata();
                        if( datestr.length >=8 ){
                            year = Number(datestr.substring(0,4) );
                            month =  Number( datestr.substring(4,6) );
                           if( month >0 ){ month = month -1;}
                            day = Number( datestr.substring(6,8) );
                        }else if(datestr.length >=6 ){ //月
                            year = Number(datestr.substring(0,4) );
                            month =  Number( datestr.substring(4,6) );
                           day = Number( '0' );
                        }

                    var Dflag = jiakuan3.getDflag();

                    if(Dflag == 'D'){
                        J.popup({
                            html: '<div id="popup_calendar_zhen3"></div>',
                            pos: 'center',
                            backgroundOpacity: 0.4,
                            showCloseBtn: false,
                            onShow: function() {
                                 new J.Calendar('#popup_calendar_zhen3', {
                                    date: new Date(year, month, day),
                                    onSelect: function(date) {
                                    console.log('政企3时间选择' + date);
                                    $('#jiakuan3time span').text(date);

                                     jiakuan3.setdate(date);
                                     jiakuan3.getdata();

                                     J.closePopup(); }
                                    });
                            }
                        });
                    }else{
                        J.popup({
                            html: '<div id="popup_calendarMonthz3"></div>',
                            pos: 'center',
                            backgroundOpacity: 0.4,
                            showCloseBtn: false,
                            onShow: function() {
                            new J.CalendarMonth('#popup_calendarMonthz3', {
                                date: new Date(year, month, day),
                                onSelect: function(date) {
                                console.log('政企3时间选择' + date);
                                $('#jiakuan3time span').text(date);

                                jiakuan3.setdate(date);
                                jiakuan3.getdata();

                                J.closePopup(); }
                                });
                            }
                        });
                        }
                    }else if( idstr == '2' ){
                            var areacode = jiakuan3.getareacode();
                            jiakuan.getareadata( areacode , { onSelect: function( param ){
                            J.popup({
                                html: '<div id="popup_area_jiakuan3"></div>',
                                pos : 'center',
                                showCloseBtn : false,
                                onShow: function(){ 
                                    new J.Area('#popup_area_jiakuan3',{
                                                areadata: param,
                                                onSelect:function( areaCode ,areaName ){
                                                console.log('地域选择 ' + areaCode + areaName);
                                                $('#jiakuan3area span').text(areaName);

                                                jiakuan3.setarea(areaName);
                                                jiakuan3.setareacode( areaCode );
                                                jiakuan3.getdata();

                                                J.closePopup();}
                                         })
                                }
                            })
                        }});
                    }//end else if
                }//if
            });//on
    }
});

App.page('jiakuanfirst',function(){
    this.init = function(){
        var flag = true;
        var slider;
        $('#jiakuanfirst_section article').on('articleshow',function(){
            slider = new J.Slider({
                selector : '#slider_test',
                onBeforeSlide : function(){
                    return true;
                },
                onAfterSlide : function(i){
                    //alert(i);
                }
            });
        });
        $('#slider_prev').tap(function(){slider.prev()});
        $('#slider_next').tap(function(){slider.next()});
    }
});


App.page('calendar',function(){
    this.init = function(){
        new J.Calendar('#calendar_demo',{
            onRenderDay : function(day,date){
                if(day == 5){
                    return '<div>'+day+'</div><div style="font-size: .8em;color: red">威武</div>'
                }
                return day;
            },
            onSelect:function(date){
                alert(date);
            }
        });
        $('#btn_popup_calendar').tap(function(){
            J.popup({
                html : '<div id="popup_calendar"></div>',
                pos : 'center',
                backgroundOpacity : 0.4,
                showCloseBtn : false,
                onShow : function(){
                    new J.Calendar('#popup_calendar',{
                        date : new Date(2013,7,1),
                        onSelect:function(date){
                            $('#btn_popup_calendar').text(date);
                            J.closePopup();
                        }
                    });
                }
            });
        });
    }
});
App.page('refresh',function(){
    this.init = function(){
        J.Refresh({
            selector : '#down_refresh_article',
            type : 'pullDown',
            pullText : '你敢往下拉么...',
            releaseText : '什么时候你才愿意放手？',
            refreshTip : '最后一次拉的人：<span style="color:#e222a5">骚年</span>',
            callback : function(){
                var scroll = this;
                setTimeout(function () {
                    $('#down_refresh_article ul.list li').text('嗯哈，长大后我就成了你~');
                    scroll.refresh();
                    J.showToast('更新成功','success');
                }, 2000);
            }
        });
//    最简约的调用方式
        J.Refresh( '#up_refresh_article','pullUp', function(){
            var scroll = this;
            setTimeout(function () {
                var html = '';
                for(var i=0;i<10;i++){
                    html += '<li style="color:#E74C3C">我是被拉出来的...</li>'
                }
                $('#up_refresh_article ul.list').append(html);
                scroll.refresh();
                J.showToast('加载成功','success');
            }, 2000);
        })
    }
});
App.page('scroll',function(){
    this.init = function(){
        $('#h_scroll_article').on('articleshow',function(){
            J.Scroll('#h_scroll_demo',{hScroll:true,hScrollbar : false});
        })
    }
});
App.page('menu',function(){
    this.init = function(){
        $.get('html/custom_aside.html',function(aside){
            $('#aside_container').append(aside);
        })
    }
});
App.page('layout',function(){
    this.init = function(){
        $('#layout_header_ctrl').on('change',function(event,el){
            J.alert('提示','你点了'+$(el).text());
        })
        $('#layout-btn-getmore').tap(function(){
            J.popup({
                html: '<div style="height: 100px;line-height: 100px;font-size: 20px;font-weight: 600;text-align: center;">这里展示更多功能</div>',
                pos : 'bottom-second',
                showCloseBtn : false
            });
        })
    }
});
App.page('popup',function(){
    this.init = function(){
        $('#btn_alert').tap(function(){
            J.alert('提示','这是一个Alert');
        })
        $('#btn_confirm').tap(function(){
            J.confirm('提示','这是一个Confirm！',function(){J.showToast('你选择了“确定”')},function(){J.showToast('你选择了“取消”')});
        })
        $('#btn_loading').tap(function(){
            J.showMask();
        })
        $('#btn_center').tap(function(){
            J.popup({
                html: '<div style="height: 100px;text-align: center;font-size: 20px;font-weight: 600;margin-top: 10px;color:#E74C3C ">随意设计你的弹出框吧</div>',
                pos : 'center'
            })
        })
        $('#btn_from_tpl').tap(function(){
            J.popup({
                tplId : 'tpl_popup_login',
                pos : 'center'
            })
        })
        $('#btn_t_top').tap(function(){
            J.popup({
                html: '这是一个来自顶部的弹出框',
                pos : 'top',
                showCloseBtn : false
            })
        })
        $('#btn_t_ts').tap(function(){
            J.popup({
                html: '这是一个在header之下的弹出框',
                pos : 'top-second',
                showCloseBtn : false
            })
        })
        $('#btn_t_bottom').tap(function(){
            J.popup({
                html: '这是一个来自底部弹出框',
                pos : 'bottom',
                showCloseBtn : true
            })
        })
        $('#btn_t_bs').tap(function(){
            J.popup({
                html: '这是一个在footer之上的弹出框',
                pos : 'bottom-second',
                showCloseBtn : false
            })
        })
        $('#btn_popover').tap(function(){
            J.popover('<ul class="list"><li style="color:#000;">Hello Jingle</li><li style="color:#000;">你好，Jingle</li></ul>',{top:'64px',left:'10%',right:'10%'},'top');
        });

        $('#btn_actionsheet').tap(function(){
            J.Popup.actionsheet([{
                text : '告诉QQ好友',
                handler : function(){
                    J.showToast('告诉QQ好友！');
                }
            },{
                text : '告诉MSN好友',
                handler : function(){
                    J.showToast('告诉MSN好友！');
                }
            }
            ]);
        });
    }
});
App.page('slider',function(){
    this.init = function(){
        var slider;
        $('#slider_section article').on('articleshow',function(){
            slider = new J.Slider({
                selector : '#slider_test',
                onBeforeSlide : function(){
                    return true;
                },
                onAfterSlide : function(i){
                    //alert(i);
                }
            });
        });
        $('#slider_prev').tap(function(){slider.prev()});
        $('#slider_next').tap(function(){slider.next()});
    }
});
App.page('toast',function(){
    this.init = function(){
        $('#btn_t_default').tap(function(){
      
            J.showToast('这是默认的Toast,默认3s后小时');
        })
        $('#btn_t_success').tap(function(){
            J.showToast('恭喜，success,5s后消失','success',5000);
        })
        $('#btn_t_error').tap(function(){
            J.showToast('抱歉，error','error');
        })
        $('#btn_t_info').tap(function(){
            J.showToast('提示，info','info');
        })
        $('#btn_t_top').tap(function(){
            J.showToast('更新了50条数据','toast top');
        })
    }
});
App.page('chart_line',function(){
    var line,$chart;
    this.init = function(){
        //重新设置canvas大小
        $chart = $('#line_canvas');
        var wh = App.calcChartOffset();
        $chart.attr('width',wh.width).attr('height',wh.height-30);

        renderLine();
        $('#changeLineType').on('change',function(e,el){
            updateChart(el.data('type'));
        })
    }

    function renderLine(){
        var data = {
            labels : ["一月","二月","三月","四月","五月","六月","七月",'八月','九月','十月','十一月','十二月'],
            datasets : [
                {
                    name : 'A产品',
                    color : "#72caed",
                    pointColor : "#95A5A6",
                    pointBorderColor : "#fff",
                    data : [65,59,90,81,56,55,40,20,13,20,11,60]
                },
                {
                    name : 'B产品',
                    color : "#a6d854",
                    pointColor : "#95A5A6",
                    pointBorderColor : "#fff",
                    data : [28,48,40,19,96,27,100,40,40,70,11,89]
                }
            ]
        }
        line = new JChart.Line(data,{
            id : 'line_canvas',
            smooth : false,
            fill : false
        });
        line.on('tap.point',function(d,i,j){
            J.alert(data.labels[i],d);
        });
        line.draw();
    }
    function updateChart(type){
        if(type == 'smooth'){
            line.refresh({
                smooth : true,
                fill : false
            });
        }else if(type == 'area'){
            line.refresh({
                smooth : true,
                fill : true
            });
        }else{
            line.refresh({
                smooth : false,
                fill : false
            });
        }

    }
});
App.page('chart_bar',function(){
    var $chart;
    this.init = function(){
        //重新设置canvas大小
        $chart = $('#bar_canvas');
        var wh = App.calcChartOffset();
        $chart.attr('width',wh.width).attr('height',wh.height);

        var data = {
            labels : ["一月","二月","三月","四月","五月","六月","七月",'八月','九月','十月','十一月','十二月'],
            datasets : [
                {
                    name : 'A产品',
                    color : "#72caed",
                    pointColor : "#95A5A6",
                    pointBorderColor : "#fff",
                    data : [65,59,90,81,56,55,40,20,13,20,11,60]
                },
                {
                    name : 'B产品',
                    color : "#a6d854",
                    pointColor : "#95A5A6",
                    pointBorderColor : "#fff",
                    data : [28,48,40,19,96,27,100,40,40,70,11,89]
                }
            ]
        }
        var bar = new JChart.Bar(data,{
            id : 'bar_canvas'
        });
        bar.on('tap.bar',function(d,i,j){
            J.alert(data.labels[i],d);
        });
        bar.on('longTap.bar',function(d,i,j){
            J.alert('提示',d+' = 按住750ms会出现此提示');
        });
        bar.draw();
    }
});
App.page('chart_pie',function(){
    var pie,$chart;
    this.init = function(){
        //重新设置canvas大小
        $chart = $('#pie_canvas');
        var wh = App.calcChartOffset();
        $chart.attr('width',wh.width).attr('height',wh.height-100);
        renderPie();
        $('#changePieType').on('change',function(e,el){
            updateChart(el.data('type'));
        })
    }

    function renderPie(){
        var pie_data = [
            {
                name : '直接访问',
                value: 335,
                color:"#F38630"
            },{
                name : '联盟广告',
                value : 234,
                color : "#E0E4CC"
            },{
                name : '视频广告',
                value : 135,
                color : "#72caed"
            },{
                name : '搜索引擎',
                value : 1400,
                color : "#a6d854"
            }
        ];
        pie = new JChart.Pie(pie_data,{
            id : 'pie_canvas',
            clickType : 'rotate'
        });
        pie.on('rotate',function(d,i,j){
            $('#pie_segment_info').html(d.name + '    '+ d.value).show();
        });
        pie.draw();
    }
    function updateChart(type){
        $('#pie_segment_info').hide();
        if(type == 'pie'){
            pie.refresh({
                isDount : false
            });
        }else if(type == 'dount'){
            pie.refresh({
                isDount : true,
                dountText : '访问来源'
            });
        }

    }
});
App.page('chart_drag',function(){
    var $lineChart,$barChart;
    this.init = function(){
        //重新设置canvas大小
        $lineChart = $('#chart_drag_line_canvas');
        $barChart = $('#chart_drag_bar_canvas');
        var wh = App.calcChartOffset();
        $lineChart.attr('width',wh.width).attr('height',wh.height-30);
        $barChart.attr('width',wh.width).attr('height',wh.height-30);
        var data = {
            labels : ["2012","二月","三月","四月","五月","六月","七月",'八月','九月','十月','十一月','十二月','2013',"二月","三月","四月","五月","六月","七月",'八月','九月','十月','十一月','十二月','2014','一月','二月'],
            datasets : [
                {
                    name : 'A产品',
                    color : "#72caed",
                    pointColor : "#95A5A6",
                    pointBorderColor : "#fff",
                    data : [65,59,90,81,56,55,40,20,13,20,11,60,65,59,90,81,56,55,40,20,11,20,10,60,11,60,65]
                },
                {
                    name : 'B产品',
                    color : "#a6d854",
                    pointColor : "#95A5A6",
                    pointBorderColor : "#fff",
                    data : [28,48,40,19,96,27,100,40,40,70,11,89,28,48,40,19,96,27,100,40,40,70,10,89,28,48,40]
                }
            ]
        }
        $('#changeDragChartType').on('change',function(e,el){
            renderChart(el.data('type'),data);
        })
        renderChart('line',data);
    }
    var renderChart = function(type,data){
        if(type == 'line'){
            $lineChart.show();
            $barChart.hide();
            new JChart.Line(data,{
                id : 'chart_drag_line_canvas',
                datasetGesture : true,
                datasetOffsetNumber : 10
            }).draw(true);
        }else{
            $lineChart.hide();
            $barChart.show();
            new JChart.Bar(data,{
                id : 'chart_drag_bar_canvas',
                datasetGesture : true,
                datasetOffsetNumber : 10
            }).draw(true);
        }
    }
});
App.page('chart_dynamic',function(){
    var pause = false,$chart;
    var datasets = [[65,59,90,81,56,55,40,20,3,20,10,60],[28,48,40,19,96,27,100,40,40,70,10,89]];
    var data = {
        labels : ["一月","二月","三月","四月","五月","六月","七月",'八月','九月','十月','十一月','十二月'],
        datasets : [
            {
                name : 'A产品',
                color : "#72caed",
                pointColor : "#95A5A6",
                pointBorderColor : "#fff",
                data : datasets[0]
            },
            {
                name : 'B产品',
                color : "#a6d854",
                pointColor : "#95A5A6",
                pointBorderColor : "#fff",
                data : datasets[1]
            }
        ]
    }

    this.init = function(){
        //重新设置canvas大小
        $chart = $('#dynamic_line_canvas');
        var wh = App.calcChartOffset();
        $chart.attr('width',wh.width).attr('height',wh.height-30);
        var line = new JChart.Line(data,{
            id : 'dynamic_line_canvas'
        });
        line.draw();
        refreshChart(line);
        $('#pause_dynamic_chart').on('tap',function(){
            pause = !pause;
            $(this).text(pause?'继续':'暂停');
        })
    }

    function refreshChart(chart){
        setTimeout(function(){
            if(!pause){
                datasets[0].shift();
                datasets[0].push(Math.floor(Math.random()*100));
                datasets[1].shift();
                datasets[1].push(Math.floor(Math.random()*100));
                chart.load(data);
            }
            refreshChart(chart);
        },1000);
    }
});
App.page('form',function(){
    this.init = function(){
        alert('init');
        $('#checkbox_1').on('change',function(){
            alert($(this).data('checkbox'));
        })
    }
})
$(function(){
    App.run();
})

document.addEventListener('deviceready', onDeviceReady, false);

function onDeviceReady(){
    navigator.splashscreen.hide();
    console.log('-----');
    //注册后退按钮
    document.addEventListener("backbutton", function (e) {
        if(J.hasMenuOpen){
            J.Menu.hide();
        }else if(J.hasPopupOpen){
            J.closePopup();
        }else{
            var sectionId = $('section.active').attr('id');
            if(sectionId == 'index_section'){
                J.confirm('提示','是否退出程序？',function(){
                    navigator.app.exitApp();
                });
            }else{
                window.history.go(-1);
            }
        }
    }, false);
};

var App = (function(){
    var pages = {};
    var run = function(){
        //console.log('---3---');
        $.each(pages,function(k,v){
            //console.log('----'+ k);
            var sectionId = '#'+k+'_section';
            
            $('body').delegate(sectionId,'pageinit',function(){
                //console.log('---4---');
                v.init && v.init.call(v);
            });
            
            $('body').delegate(sectionId,'pageshow',function(e,isBack){
                //console.log('---5---');
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
            showWelcome : true,
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
            showPageLoading : false,
            remotePage : {
                '#about_section' : 'remote/about_section.html'
            }
        });
       
    };
    var page = function(id,factory){
        //console.log('---1---'+id +'--'+ factory );
        return ((id && factory)?_addPage:_getPage).call(this,id,factory);
    }
    var _addPage = function(id,factory){
        //console.log('---2---'+id +'--'+ factory);
        
        pages[id] = new factory();
    };
    var _getPage = function(id){
       // console.log('---11---');
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

App.page('index',function(){
    this.init = function(){
        J.Footer.ban( false);
        /*返回*/
        $('#btn_back').tap(function(){
            httpsinterface.NativeGoBack();
        })
        
        $('#btn_share').tap(function(){
            httpsinterface.Html5Share();
        })
    }
});

$(function(){
    App.run();
})
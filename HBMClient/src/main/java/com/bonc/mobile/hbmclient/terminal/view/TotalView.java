package com.bonc.mobile.hbmclient.terminal.view;

/*package com.bonc.anhuimobile.terminal.baseview;

 import java.text.DecimalFormat;
 import java.util.List;
 import java.util.Map;

 import android.content.Context;
 import android.os.Handler;
 import android.util.AttributeSet;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.widget.LinearLayout;
 import android.widget.TextView;

 import com.bonc.mobile.hbmclient.R;
 import com.bonc.anhuimobile.terminal.common.Constant;
 import com.bonc.anhuimobile.terminal.config.TerminalConfig;
 import com.bonc.anhuimobile.terminal.data.TerminalHomePageDataLoad;
 import com.bonc.anhuimobile.terminal.refresh.TerminalHomeRefreshInterface;
 import com.bonc.anhuimobile.terminal.refresh.TerminalRefreshListener;
 import com.bonc.mobile.util.NumberUtil;
 *//**
 * 
 * @author ZZZ
 *
 */
/*
 * public class TotalView extends LinearLayout implements
 * TerminalHomeRefreshInterface{ private TextView tv1,tv2,count1,count2; private
 * double num1,num2;
 * 
 * private String[] tvs1; private String[] tvs2; private MyProgressBar4Total
 * progressBar; private LinearLayout ll_total_item; private TextView
 * ll_total_title ; private Handler handler = new Handler(); public
 * TotalView(Context context, AttributeSet attrs) { super(context, attrs);
 * initView(); TerminalRefreshListener.register(this); } public void initView(){
 * LayoutInflater inflater = LayoutInflater.from(getContext()); View
 * view=(LinearLayout)inflater.inflate(R.layout.curr_total, this); String
 * s1=TerminalConfig.getValue("teminal.statics.saleanalysis.label1"); String
 * s2=TerminalConfig.getValue("teminal.statics.saleanalysis.label2");
 * if(s1==null||s2==null||TerminalHomePageDataLoad.getBareContact()==null){
 * return; }else{ tvs1= s1.split(Constant.DEFAULT_SPLIT); tvs2=
 * s2.split(Constant.DEFAULT_SPLIT); }
 * ll_total_item=(LinearLayout)view.findViewById(R.id.ll_total_item);
 * ll_total_title = (TextView)view.findViewById(R.id.ll_total_title);
 * ll_total_title
 * .setText(TerminalConfig.getValue("teminal.statics.saleanalysis.title"));
 * for(int i=0;i<4;i++) inflater.inflate(R.layout.curr_total_item,
 * ll_total_item); for(int j=0;j<4;j++){ //获取find by id tv1=(TextView)
 * ll_total_item.getChildAt(j).findViewById(R.id.total_tv1); tv2=(TextView)
 * ll_total_item.getChildAt(j).findViewById(R.id.total_tv2);
 * count1=(TextView)ll_total_item.getChildAt(j).findViewById(R.id.total_count1);
 * count2=(TextView)ll_total_item.getChildAt(j).findViewById(R.id.total_count2);
 * progressBar
 * =(MyProgressBar4Total)ll_total_item.getChildAt(j).findViewById(R.id
 * .progressBar_total); //绑定数值 tv1.setText(tvs1[j]); tv2.setText(tvs2[j]);
 * DecimalFormat digits=new DecimalFormat("0.0"); switch (j) { case 0:
 * List<Map<String,String>> bc = TerminalHomePageDataLoad.getBareContact();
 * 
 * int percent = 0;
 * 
 * 
 * if(bc == null ||bc.get(0).isEmpty()){ progressBar.setProgress(0); }else {
 * 
 * if(bc.get(0).get("num").length()>4){
 * num1=NumberUtil.changeToDouble(bc.get(0).get("num"));
 * count1.setText(digits.format(num1/10000)+"万"); }else {
 * count1.setText(bc.get(0).get("num")); } if(bc.get(1).get("num").length()>4){
 * num2=NumberUtil.changeToDouble(bc.get(1).get("num"));
 * count2.setText(digits.format(num2/10000)+"万"); }else {
 * count2.setText(bc.get(1).get("num")); }
 * 
 * percent = (int)((num1/(num1+num2))*100); progressBar.setProgress(percent);
 * 
 * }
 * 
 * 
 * break; case 1: List<Map<String,String>> sc =
 * TerminalHomePageDataLoad.getIntelCommon();
 * 
 * if(sc!=null && !sc.get(0).isEmpty()) {
 * 
 * if(sc.get(0).get("num").length()>4){
 * num1=NumberUtil.changeToDouble(sc.get(0).get("num"));
 * count1.setText(digits.format(num1/10000)+"万"); }else {
 * count1.setText(sc.get(0).get("num")); }
 * 
 * if(sc.get(1).get("num").length()>4){
 * num2=NumberUtil.changeToDouble(sc.get(1).get("num"));
 * count2.setText(digits.format(num2/10000)+"万"); }else {
 * count2.setText(sc.get(1).get("num")); }
 * 
 * percent = (int)((num1/(num1+num2))*100); progressBar.setProgress(percent); }
 * break; case 2: List<Map<String,String>> us =
 * TerminalHomePageDataLoad.getTD2G(); if(us!=null && !us.get(0).isEmpty()){
 * if(us.get(0).get("num").length()>4){
 * num1=NumberUtil.changeToDouble(us.get(0).get("num"));
 * count1.setText(digits.format(num1/10000)+"万"); }else {
 * count1.setText(us.get(0).get("num")); }
 * 
 * if(us.get(1).get("num").length()>4){
 * num2=NumberUtil.changeToDouble(us.get(1).get("num"));
 * count2.setText(digits.format(num2/10000)+"万"); }else {
 * count2.setText(us.get(1).get("num")); } percent =
 * (int)((num1/(num1+num2))*100); progressBar.setProgress(percent); } break;
 * case 3: List<Map<String,String>> im =
 * TerminalHomePageDataLoad.getSelfProxy(); if(im!=null &&
 * !im.get(0).isEmpty()){ if(im.get(0).get("num").length()>4){
 * num1=NumberUtil.changeToDouble(im.get(0).get("num"));
 * count1.setText(digits.format(num1/10000)+"万"); }else {
 * count1.setText(im.get(0).get("num")); } if(im.get(1).get("num").length()>4){
 * num2=NumberUtil.changeToDouble(im.get(1).get("num"));
 * count2.setText(digits.format(num2/10000)+"万"); }else {
 * count2.setText(im.get(1).get("num")); } percent =
 * (int)((num1/(num1+num2))*100); progressBar.setProgress(percent); } break;
 * default: break; } } }
 * 
 * @Override public void refresh() { handler.post(new Runnable() {
 * 
 * @Override public void run() { TotalView.this.removeAllViews(); initView();
 * invalidate(); } }); } }
 */
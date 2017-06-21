package com.bonc.mobile.common.util;


import android.app.Activity;
import android.view.View;
import android.widget.TextView;

public class TextViewUtils {
    public static void setText(View parent,int id, CharSequence value){
        TextView tv=(TextView)parent.findViewById(id);
        tv.setText(value);
    }
	
    public static CharSequence getText(View parent,int id){
        TextView tv=(TextView)parent.findViewById(id);
        return tv.getText();
    }
    
    public static void setText(Activity parent,int id, CharSequence value){
        TextView tv=(TextView)parent.findViewById(id);
        tv.setText(value);
    }
    
    public static void setTextColor(View parent,int id, int colorid){
        TextView tv=(TextView)parent.findViewById(id);
        tv.setTextColor(tv.getResources().getColor(colorid));
    }
    
    public static void setTextAppearance(View parent,int id, int resid){
        TextView tv=(TextView)parent.findViewById(id);
        tv.setTextAppearance(parent.getContext(), resid);
    }
    
    public static void setVisibility(View parent,int id, int visibility){
        TextView tv=(TextView)parent.findViewById(id);
        tv.setVisibility(visibility);
    }
    
	/**
	 * 在指定文本控件上显示数字
	 * @param tv
	 * @param value
	 */
	public static void setNumberValue(TextView tv, double value){
		
		tv.setText(NumberUtil.format(value));
		
	}
}

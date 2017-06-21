package com.bonc.mobile.hbmclient.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.bonc.mobile.common.util.UIUtil;

public class MyDatePickerDialog extends DatePickerDialog {
    // private OnDateSetListener callBack;

    public MyDatePickerDialog(Context context, OnDateSetListener callBack,
                              int year, int monthOfYear, int dayOfMonth) {
        super(UIUtil.resolveDialogTheme(context), callBack, year, monthOfYear, dayOfMonth);
        // this.callBack = callBack;
    }

//    static Context resolveDialogTheme(Context context) {
//
//        Context themedContext = new ContextThemeWrapper(context,
//                // android.R.style.Widget_Material_NumberPicker
//                android.R.style.Theme_Holo_Light_Dialog);
//
//        return themedContext;
//    }

    @Override
    public void show() {
        super.show();

		/*
         * if(DataMessageControl.getInstance().isNeedHiddenDay()) { DatePicker
		 * dp = findDatePicker((ViewGroup) this.getWindow().getDecorView()); if
		 * (dp != null) {
		 * 
		 * View v1 = null,v2 = null; try{
		 * if(release.startsWith("2.")||release.startsWith("3.")) { v1 =
		 * ((ViewGroup) dp.getChildAt(0)).getChildAt(0); v2 = ((ViewGroup)
		 * dp.getChildAt(0)).getChildAt(0); //((ViewGroup)
		 * dp.getChildAt(0)).getChildAt(2).setVisibility(View.GONE); } else {
		 * if(dp.getChildAt(0) != null && ((ViewGroup)
		 * dp.getChildAt(0)).getChildAt(0)!=null && ((ViewGroup)((ViewGroup)
		 * dp.getChildAt(0)).getChildAt(0)).getChildAt(2) !=null) { v1 =
		 * ((ViewGroup)((ViewGroup)
		 * dp.getChildAt(0)).getChildAt(0)).getChildAt(0); v2 =
		 * ((ViewGroup)((ViewGroup)
		 * dp.getChildAt(0)).getChildAt(0)).getChildAt(2); }
		 * //((ViewGroup)((ViewGroup)
		 * dp.getChildAt(0)).getChildAt(0)).getChildAt
		 * (2).setVisibility(View.GONE);
		 * 
		 * } if(v1!=null && v2!=null){
		 * if(v1.getLayoutParams().width>v2.getLayoutParams().width){
		 * v2.setVisibility(View.GONE); }else { v1.setVisibility(View.GONE); }
		 * 
		 * }
		 * 
		 * }catch (Exception e) { e.printStackTrace(); } } }
		 */

        setCanceledOnTouchOutside(true);

    }

	/*
	 * 
	 * 该方法用来一次性解决api>=11时的两次回调问题
	 * 
	 * @Override protected void onStop() { // TODO Auto-generated method stub if
	 * (callBack != null) { int api_level = android.os.Build.VERSION.SDK_INT;
	 * if(api_level < 11) {
	 * 
	 * } else { try { // Field field =
	 * DatePickerDialog.class.getDeclaredField("mDatePicker"); Field field =
	 * this.getClass().getSuperclass().getDeclaredField("mDatePicker");
	 * field.setAccessible(true); if(field == null) {
	 * 
	 * } else { Object o = field.get(this); DatePicker dp = (DatePicker)(o);
	 * dp.clearFocus(); } } catch (Exception e) { e.printStackTrace(); } } }
	 */

    /**
     * 解决日期的两次回调时，必须注释掉super.onStop();
     */
	/*
	 * //super.onStop(); }
	 */
    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }

}

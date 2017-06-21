
package com.bonc.mobile.common.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.UIUtil;

public class DataChooseButton extends Button {
    protected List<Map<String, String>> mData;
    protected int which;
    protected OnDataChooseListener mDataChooseListener;
    String nameKey, valueKey;

    public interface OnDataChooseListener {
        public void onChoose(Map<String, String> item);
    }

    public DataChooseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DataChooseButton(Context context) {
        super(context);
        init();
    }

    protected void init() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.size() == 0)
                    return;
                Context c = UIUtil.getSafeContext(getContext());
                CharSequence[] items = DataUtil.extractList(mData, nameKey).toArray(new String[0]);
                UIUtil.showAlertDialog(c, c.getString(R.string.hint), items,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setChoice(which);
                                if (mDataChooseListener != null)
                                    mDataChooseListener.onChoose(mData.get(which));
                            }
                        });
            }
        });
    }

    public void setData(List<Map<String, String>> data, String nameKey, String valueKey) {
        if (data == null)
            this.mData = new ArrayList<Map<String, String>>();
        else
            this.mData = data;
        this.nameKey = nameKey;
        this.valueKey = valueKey;
        setChoice(0);
    }

    public void setChoice(int which) {
        this.which = which;
        setText(getChoiceName());
    }

    public void setChoice(String value) {
        int choice = DataUtil.getRowNum(mData, valueKey, value);
        if (choice == -1)
            choice = 0;
        setChoice(choice);
    }

    public String getChoiceName() {
        if (which < 0 || which >= mData.size())
            return null;
        return mData.get(which).get(nameKey);
    }

    public String getChoiceValue() {
        if (which < 0 || which >= mData.size())
            return null;
        return mData.get(which).get(valueKey);
    }

    public void setOnDataChooseListener(OnDataChooseListener listener) {
        this.mDataChooseListener = listener;
    }
}

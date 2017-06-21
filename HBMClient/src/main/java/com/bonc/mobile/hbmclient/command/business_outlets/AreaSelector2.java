/**
 * AreaSelector2
 */
package com.bonc.mobile.hbmclient.command.business_outlets;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.LogUtil;

/**
 * @author liweigao
 */
public class AreaSelector2 {
    private Context context;
    private List<Map<String, String>> areaInfo;
    private Map<String, String> selectArea;
    private OnAreaSelectListener listener;
    private String key_areaName;
    private String key_areaCode;
    private String key_areaLevel;

    public AreaSelector2(Context context, String keyAreaName,
                         String keyAreaCode, String keyAreaLevel) {
        this.context = context;
        this.key_areaName = keyAreaName;
        this.key_areaCode = keyAreaCode;
        this.key_areaLevel = keyAreaLevel;
    }

    public void setOnAreaSelectListener(OnAreaSelectListener oasl) {
        this.listener = oasl;
    }

    public void parseAreaInfo(JSONArray ja_area) {
        if (ja_area == null || "".equals(ja_area) || "null".equals(ja_area)
                || ja_area.length() <= 0) {

        } else if (ja_area.length() > 0) {
            this.areaInfo = JsonUtil.toDataList(ja_area);
        }
    }

    public void setAreaInfo(List<Map<String, String>> areaInfo) {
        this.areaInfo = areaInfo;
    }

    public void chooseArea() {
        if (this.selectArea != null
                && this.selectArea.get(key_areaName) != null) {
            final String[] areaName = new String[this.areaInfo.size()];
            for (int i = 0; i < this.areaInfo.size(); i++) {
                areaName[i] = this.areaInfo.get(i).get(key_areaName);
            }
            Context c  = UIUtil.resolveDialogTheme(this.context);
            new AlertDialog.Builder(c).setTitle(R.string.hint)
                    .setItems(areaName, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setSelectArea(which);
                            listener.onAreaSelect(AreaSelector2.this);
                        }
                    }).show();
        } else {

        }

    }

    public void setSelectArea(int index) {
//        LogUtils.logBySys("index：" + index + ";Info:" + areaInfo.toString());
//        if (areaInfo != null && areaInfo.size() > 0)
//            return;
        try {
            if (areaInfo.size()==0){
                return;
            }
            this.selectArea = areaInfo.get(index);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getAreaName() {
        return this.selectArea.get(key_areaName);
    }

    public String getAreaLevel() {
        return this.selectArea.get(key_areaLevel);
    }

    public String getAreaCode() {
        if (this.selectArea != null) {
            return this.selectArea.get(key_areaCode);
        } else {
            return null;
        }

    }

    public interface OnAreaSelectListener {
        /**
         * @param areaSelector2
         */
        void onAreaSelect(AreaSelector2 areaSelector2);
    }
}

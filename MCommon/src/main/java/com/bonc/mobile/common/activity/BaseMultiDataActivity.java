package com.bonc.mobile.common.activity;

import org.json.JSONObject;

import android.content.Context;

public class BaseMultiDataActivity extends BaseDataActivity {
    protected boolean firstQuery = true;
    
    protected void bindData(JSONObject result, int requestCode) {
    }

    protected class LoadDataTask2 extends LoadDataTask {
        int requestCode;

        public LoadDataTask2(Context context, String basePath, int requestCode) {
            super(context, basePath);
            this.requestCode = requestCode;
        }

        @Override
        protected void handleResult(JSONObject result) {
            firstQuery=false;
            bindData(result, requestCode);
        }
    }

}

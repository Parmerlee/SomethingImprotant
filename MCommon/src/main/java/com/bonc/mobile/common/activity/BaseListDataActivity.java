
package com.bonc.mobile.common.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bonc.mobile.common.R;

/**
 * @author sunwei
 */
public class BaseListDataActivity extends BaseDataActivity implements OnItemClickListener {
    protected ListView mList;

    @Override
    protected void initView() {
        super.initView();
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(this);
    }

    @Override
    protected void resetView() {
        super.resetView();
        mList.setAdapter(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}

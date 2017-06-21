
package com.bonc.mobile.common.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.view.SlideHolder;

public class SlideHolderDataActivity extends BaseDataActivity {
    protected SlideHolder slideHolder;
    protected ExpandableListView side_menu;
    protected FrameLayout main_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_holder);
        initSlideHolder();
    }

    @Override
    public void onBackPressed() {
        if(slideHolder.isOpened()){
            slideHolder.close();
            return;
        }
        super.onBackPressed();
    };
    
    protected void initSlideHolder() {
        slideHolder = (SlideHolder) findViewById(R.id.slideHolder);
        slideHolder.setEnabled(false);
        side_menu = (ExpandableListView) findViewById(R.id.side_menu);
        this.side_menu.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition,
                    long id) {
                return true;
            }
        });
        main_content = (FrameLayout) findViewById(R.id.main_content);
    }

    public void setMainContent(View v) {
        main_content.removeAllViews();
        main_content.addView(v);
    }

    public void setMainContent(int id) {
        setMainContent(View.inflate(this, id, null));
    }

    public void setSideMenu(ExpandableListAdapter adapter) {
        side_menu.setAdapter(adapter);
    }
    
    public void setToggleButton(int id){
        findViewById(id).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                slideHolder.toggle();
            }
        });
    }
}

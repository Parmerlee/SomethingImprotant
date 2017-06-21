
package com.bonc.mobile.common.activity;

import java.util.List;

import android.os.Handler;
import android.widget.TextView;

import com.bonc.mobile.common.view.PatternView;
import com.bonc.mobile.common.view.PatternView.Cell;

/**
 * 
 * @author sunwei
 */
public class PatternViewActivity extends BaseActivity {
    protected PatternView patternView;
    protected TextView hint;
    protected Handler handler = new Handler();

    protected String getPassword(List<Cell> pattern) {
        StringBuffer sb = new StringBuffer();
        for (Cell cell : pattern) {
            sb.append(cell.getRow() * 3 + cell.getColumn() + 1);
        }
        return sb.toString();
    }

    /**
     * 重置绘图界面
     */
    protected void resetPattern() {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                patternView.clearPattern();
                patternView.enableInput();
            }
        }, 1000);
    }
}

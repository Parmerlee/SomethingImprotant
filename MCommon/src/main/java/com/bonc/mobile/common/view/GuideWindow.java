
package com.bonc.mobile.common.view;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.DataUtil;

/**
 * 引导动画popwindow
 * @author Lenevo
 *
 */
public class GuideWindow extends PopupWindow {
    ImageView image_view;
    int[] guides;
    int guideStep;

    public GuideWindow(Context context) {
        super(context);
        Display d = ((Activity) context).getWindowManager().getDefaultDisplay();
        setWidth(d.getWidth() + 10);
        setHeight(d.getHeight() + 10);
        setClippingEnabled(false);
        View view = View.inflate(context, R.layout.guide_view, null);
        setContentView(view);
        image_view = (ImageView) view.findViewById(R.id.image);
        image_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                guideStep++;
                if (guideStep < guides.length) {
                    image_view.setImageResource(guides[guideStep]);
                } else
                    dismiss();
            }
        });
        final Context c = context;
        view.findViewById(R.id.skip_guide).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtil.saveSetting(PreferenceManager.getDefaultSharedPreferences(c),
                        "skip_guide", true);
                dismiss();
            }
        });
    }

    public void show(View parent, int[] guides) {
        this.guides = guides;
        guideStep = 0;
        image_view.setImageResource(guides[guideStep]);
        showAtLocation(parent, Gravity.NO_GRAVITY, -5, -5);
    }
}

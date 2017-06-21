package com.bonc.mobile.common.activity;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.onekeyshare.theme.classic.PlatformListPage;
import cn.sharesdk.system.email.Email;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.view.BrushView;
import com.bonc.mobile.common.view.ColorPickerView;

public class PaintActivity extends BaseActivity {
    protected BrushView brush;
    long lastClickTime = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        brush = (BrushView) findViewById(R.id.brush);
        File bg = (File) getIntent().getSerializableExtra("bg");
        brush.setBackgroundDrawable(new BitmapDrawable(bg.getAbsolutePath()));
    }

    @Override
    public void onClick(final View v) {
        super.onClick(v);
        if (v.getId() == R.id.choose_color) {
            chooseColor();
        } else if (v.getId() == R.id.clear) {
            brush.clear();
        } else if (v.getId() == R.id.save) {

            v.setClickable(false);

            CountDownTimer timer = new CountDownTimer(2*1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    v.setClickable(true);
                }
            };

            timer.start();
            try {


                File f = File.createTempFile("share", ".jpg", new File(
                        Environment.getExternalStorageDirectory(), "tmp"));
                brush.save(f);
                ShareSDK.initSDK(this);
                OnekeyShare oks = new OnekeyShare();
                oks.setShareContentCustomizeCallback(new ShareContentCustomize());
                oks.disableSSOWhenAuthorize();
                oks.setImagePath(f.getAbsolutePath());

                oks.setSilent(true);

                oks.show(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ShareContentCustomize implements ShareContentCustomizeCallback {
        @Override
        public void onShare(Platform platform, ShareParams paramsToShare) {
            if (Email.NAME.equals(platform.getName())) {
                paramsToShare.setText("分享");
            }
        }
    }

    void chooseColor() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final ColorPickerView colorPick = new ColorPickerView(this,
                Color.parseColor("#FFFFFF"), 1.6);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        layout.addView(colorPick, lp);
        new AlertDialog.Builder(this)
                .setTitle("选择颜色")
                .setView(layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        brush.setColor(Color.parseColor(colorPick.getStrColor()));
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }
}

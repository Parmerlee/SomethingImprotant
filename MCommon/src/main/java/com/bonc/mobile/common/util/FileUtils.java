package com.bonc.mobile.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.R;
import com.bonc.mobile.common.activity.PaintActivity;

public class FileUtils {
    public static void writeText(File file, String s) throws IOException {
        FileOutputStream os = new FileOutputStream(file);
        os.write(s.getBytes());
        os.close();
    }

    public static void viewWord(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/ms-word");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "未安装Office程序", Toast.LENGTH_LONG).show();
        }
    }

    public static Object loadObject(Context context, String name) {
        Object o = null;
        try {
            InputStream ins = context.openFileInput(name);
            ObjectInputStream ois = new ObjectInputStream(ins);
            o = ois.readObject();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    public static void saveObject(Context context, String name, Object o) {
        if (o == null)
            return;
        try {
            OutputStream out = context.openFileOutput(name, 0);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(o);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeBitmap(Context context, String name, Bitmap bitmap) {
        try {
            OutputStream out = context.openFileOutput(name, 0);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeBitmap(File name, Bitmap bitmap) {
        try {
            OutputStream out = new FileOutputStream(name);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap fromViewToBitmap(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap b = view.getDrawingCache();
        return Bitmap.createBitmap(b);
    }

    public static void captureScreen(Activity activity, File name, WebView view1) {
        View view = activity.getWindow().getDecorView();
//        view1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        if (view1 != null) {
            view1.destroyDrawingCache();
            view1.setDrawingCacheEnabled(true);
            view1.buildDrawingCache();
        }
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();

        int contentTop = activity.getWindow()
                .findViewById(Window.ID_ANDROID_CONTENT).getTop();

        int titleBarHeight = contentTop - statusBarHeight;

        int cutHeight = titleBarHeight + statusBarHeight;

        // cutHeight = cutHeight + NumberUtil.DpToPx(activity, 38);
        cutHeight = cutHeight + NumberUtil.DpToPx(activity, 62);

        int bHeight = bmp.getHeight();
        int nHeight = height - cutHeight;

        if (hasSmartBar()) {
            nHeight -= 48;
        }

        Bitmap b = Bitmap.createBitmap(bmp, 0, cutHeight, width,
                Math.min(nHeight, bHeight - cutHeight));

        view.destroyDrawingCache();

        writeBitmap(name, b);

    }

    public static void captureScreen(Activity activity, File name, boolean hor) {
        activity.getWindow().getDecorView().destroyDrawingCache();
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();

        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();

        int contentTop = activity.getWindow()
                .findViewById(Window.ID_ANDROID_CONTENT).getTop();

        int titleBarHeight = contentTop - statusBarHeight;

        int cutHeight = titleBarHeight + statusBarHeight;
        if (!hor)
            cutHeight = cutHeight + NumberUtil.DpToPx(activity, 45);

        int bHeight = bmp.getHeight();
        int nHeight = height - cutHeight;

        if (hasSmartBar()) {
            nHeight -= 48;
        }

        Bitmap b = Bitmap.createBitmap(bmp, 0, cutHeight, width,
                Math.min(nHeight, bHeight - cutHeight));

        if (hor)
            b = remodelBitmap(b, 90);
        activity.getWindow().getDecorView().destroyDrawingCache();
        Canvas bitCanvas = new Canvas(b);
        bitCanvas.drawBitmap(b, 0, 0, null);

        writeBitmap(name, b);

    }

    private static boolean hasSmartBar() {
        try {

            Method method = Class.forName("android.os.Build").getMethod(
                    "hasSmartBar");
            return ((Boolean) method.invoke(null)).booleanValue();
        } catch (Exception e) {
        }

        if (Build.DEVICE.equals("mx2")) {
            return true;
        } else if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
            return false;
        }
        return false;
    }

    static CountDownTimer timer = null;

    public static void shareScreen(final Activity activity) {

        if (timer != null) {
            return;
        }


        timer = new CountDownTimer(3 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                activity.findViewById(R.id.id_share).setClickable(false);
            }

            @Override
            public void onFinish() {
                activity.findViewById(R.id.id_share).setClickable(true);
                timer = null;
            }
        };


        timer.start();
        shareScreen(activity, false);

    }

    public static void shareScreen(Activity activity, boolean hor) {
        try {
            File d = new File(Environment.getExternalStorageDirectory(), "tmp");
            if (!AppConstant.SEC_ENH) {
                System.out.println(d.getPath());
            }

            removeTheRest(d);

            d.mkdirs();
            File f = File.createTempFile("cap", ".png", d);
            FileUtils.captureScreen(activity, f, hor);
            Intent intent = new Intent(activity, PaintActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("bg", f);
            activity.startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void removeTheRest(File d) {
        if (d.exists()) {
            File[] files = d.listFiles();
            for (int i = 0; i < files.length; i++) {
                File temp = files[i];
                String name = temp.getName().toString();
                if (name.endsWith("png") || name.endsWith("jpg")) {
                    if (temp.canWrite()) {
                        temp.delete();
                    }
                }
            }

        }

    }

    /**
     * Cordova 类截屏专用
     *
     * @param activity
     * @param view     Cordova的WebView
     */
    public static void shareScreen(final Activity activity, WebView view) {

        if (timer != null) {
            return;
        }


        timer = new CountDownTimer(3 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                activity.findViewById(R.id.id_share).setClickable(false);
            }

            @Override
            public void onFinish() {
//                activity.findViewById(R.id.id_share).setClickable(true);
                timer = null;
            }
        };


        timer.start();

        try {
            File d = new File(Environment.getExternalStorageDirectory(), "tmp");

            removeTheRest(d);

            d.mkdirs();
            File f = File.createTempFile("cap", ".png", d);
            FileUtils.captureScreen(activity, f, view);
            Intent intent = new Intent(activity, PaintActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("bg", f);
            activity.startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap remodelBitmap(Bitmap b, float degree) {
        Matrix m = new Matrix();
        m.reset();
        m.setRotate(degree);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m,
                true);
    }

    public static Bitmap createWatermark(Context context, String s) {
        Bitmap src = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.watermark_background);
        TextView tv = new TextView(context);
        tv.setTextSize(15);
        tv.setText(s);
        Bitmap watermark = remodelBitmap(fromViewToBitmap(tv), -45);
        int src_w = src.getWidth();
        int src_h = src.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(src_w, src_h, Config.RGB_565);
        Canvas c = new Canvas(newBitmap);
        c.drawBitmap(src, 0, 0, null);
        c.drawBitmap(watermark, 0, 100, null);
        c.drawBitmap(watermark, 90, 250, null);
        c.drawBitmap(watermark, 180, 400, null);
        c.drawBitmap(watermark, 270, 550, null);
        c.drawBitmap(watermark, 360, 700, null);
        c.drawBitmap(watermark, 450, 850, null);
        c.drawBitmap(watermark, 540, 1000, null);
        c.drawBitmap(watermark, 630, 1150, null);
        c.save(Canvas.ALL_SAVE_FLAG);
        c.restore();
        return newBitmap;
    }

    public static Drawable getWatermark(Context context, String name) {
        Drawable drawable = null;
        try {
            FileInputStream is = context.openFileInput(name);
            drawable = Drawable.createFromStream(context.openFileInput(name),
                    "");
            is.close();
        } catch (IOException e) {
        }
        return drawable;
    }

    public static String readLocalJson(Context context, String fileName) {
        String resultString = "";
        try {
            InputStream inputStream = context.getResources().getAssets()
                    .open(fileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            resultString = new String(buffer, "UTF-8");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return resultString;
    }

    static CountDownTimer countDownTimer = null;

    public static CountDownTimer postDelayWithTime(int time) {

        countDownTimer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                countDownTimer = null;
            }
        };

        return countDownTimer;
    }

}

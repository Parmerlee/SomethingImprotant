package com.bonc.mobile.common.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.bonc.mobile.common.util.FileUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/***
 * 截图画笔类 默认红色
 *
 */
public class BrushView extends View {
    List<Paint> paint_list = new ArrayList<Paint>();
    List<Path> path_list = new ArrayList<Path>();
    Path last_path;
    
    public BrushView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(Color.RED);
    }

    public BrushView(Context context) {
        super(context);
        init(Color.RED);
    }

    void init(int color){
        Paint brush=new Paint();
        brush.setAntiAlias(true);
        brush.setColor(color);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeWidth(10f);
        paint_list.add(brush);
        last_path=new Path();
        path_list.add(last_path);
    }
    
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            last_path.moveTo(pointX, pointY);
            return true;
        case MotionEvent.ACTION_MOVE:
            last_path.lineTo(pointX, pointY);
            break;
        default:
            return false;
        }     
        invalidate();
        return false;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        for(int i=0;i<path_list.size();i++)
        canvas.drawPath(path_list.get(i), paint_list.get(i));
    }
    
    public void clear(){
        for(Path path:path_list)
        path.reset();
        invalidate();
    }
    
    public void setColor(int color){
        init(color);
    }
    
    public void save(File file){
        setDrawingCacheEnabled(true);
        FileUtils.writeBitmap(file, getDrawingCache());
        destroyDrawingCache();
    }
}

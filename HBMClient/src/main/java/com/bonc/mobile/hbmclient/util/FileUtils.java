package com.bonc.mobile.hbmclient.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件操作类
 *
 * @author liulu
 */
public class FileUtils extends com.bonc.mobile.common.util.FileUtils {

    private static final int BUFFER_SIZE = 102400;

    /**
     * 复制文件
     *
     * @param in   文件流
     * @param file 保存的文件
     * @throws Exception
     */
    public static void copyFile(InputStream in, File file) throws Exception {
        if (in == null || file == null) {
            return;
        }
        if (!file.exists()) {
            File mParent = file.getParentFile();
            mParent.mkdirs();
            file.createNewFile();
        }
        OutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[BUFFER_SIZE];
        int off = 0;
        int size = 0;
        while ((off = in.read(buffer)) != -1) {
            size += off;
            out.write(buffer, 0, off);
        }
        out.flush();
        out.close();
    }

    //从assets 文件夹中获取文件并读取数据
    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[]  buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

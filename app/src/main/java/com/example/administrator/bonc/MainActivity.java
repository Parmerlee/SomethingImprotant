package com.example.administrator.bonc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String mimeType = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), mimeType);
        context.startActivity(intent);
    }

    public String getMIMEType(File file) {
        String type = "";
        String name = file.getName();
        String temp = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(temp);
        return type;
    }

//    if(Build.VERSION.SDK_INT >= 24) {
//        Uri uri = null;
//        try {
//            uri = FileProvider.getUriForFile(Welcome.this, "com.bonc.mobile.hbmclient.fileprovider", file);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.setDataAndType(uri, mimeType);
//    } else {
//        intent.setDataAndType(Uri.fromFile(file), mimeType);
//    }
}


package com.bonc.mobile.common.kpi;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeUtility;

import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.activity.BaseListDataActivity;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.net.SendMailTask;
import com.bonc.mobile.common.net.SimpleDownloadManager;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.common.view.DatePickerButton;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;


public abstract class FileReportActivity extends BaseListDataActivity {
    protected int page = 0;

    protected List<Map<String, String>> mData;

    String mobile;

    protected boolean isNeedReflesh = false;

    protected PullToRefreshLayout refreshLayout;

    public boolean isNeedReflesh() {
        return isNeedReflesh;
    }

    public void setIsNeedReflesh(boolean isNeedReflesh) {
        this.isNeedReflesh = isNeedReflesh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = new ArrayList<Map<String, String>>();
        doInit();
    }

    protected void doInit() {
        setContentView(R.layout.activity_report_list);
        initView();
        loadData();
    }

    @Override
    protected void initView() {
        super.initView();


        TextViewUtils.setText(this, R.id.title, getConfigLoader().getMenuName(menuCode));
    }

    @Override
    protected void loadData() {
        Map<String, String> param = new LinkedHashMap<String, String>();
        param.put("page", String.valueOf(page));
        new LoadDataTask(this).execute(getQueryAction(), param);
    }

    @Override
    protected void bindData(JSONObject result) {
        mobile = result.optString("user_mobile");
        List<Map<String, String>> data = JsonUtil.toList(JsonUtil.optJSONArray(result, "data"));
        if (data.size() == 0) {
            showToast(getString(R.string.no_more_data));
            return;
        }
        for (Map<String, String> m : data) {
            m.put("title", m.get("TITLE") + "-" + m.get("TYPE"));
        }
        mData.addAll(data);
        mList.setAdapter(new ReportListAdapter(this, mData, R.layout.report_list_item,
                new String[]{
                        "title"
                }, new int[]{
                R.id.text
        }));
    }

    class ReportListAdapter extends SimpleAdapter {
        public ReportListAdapter(Context context, List<? extends Map<String, ?>> data,
                                 int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Map<String, String> m = (Map<String, String>) mList.getAdapter()
                    .getItem(position);
            View view = super.getView(position, convertView, parent);
            view.findViewById(R.id.icon).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("txt".equals(m.get("TYPE")))
                        sendTextReport(m.get("TITLE"), m.get("CONTENT"));
                    else
                        downloadReport(getBasePath() + m.get("FILE_PATH"), 1, m.get("TITLE"));
                }
            });
            return view;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.button_more) {
            page++;
            loadData();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, String> m = (Map<String, String>) mList.getAdapter().getItem(position);
        if ("txt".equals(m.get("TYPE")))
            showTextReport(m.get("TITLE"), m.get("HTML_CONTENT"));
        else
            downloadReport(getBasePath() + m.get("FILE_PATH"), 0, m.get("TITLE"));
    }

    protected void showTextReport(String title, String content) {
        Intent intent = new Intent(this, HtmlReportActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        startActivity(intent);
    }

    protected void sendTextReport(String name, String content) {
        try {
            File f = new File(getStorageDir(), name + ".txt");
            FileUtils.writeText(f, content);
            sendReportMail(name + "-txt", f.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void downloadReport(String url, int action, String name) {
        System.out.println(url);
        new ReportDownloader(this, action, name).downloadFile(url, new File(getStorageDir(), name
                + ".doc").getAbsolutePath());
    }

    protected void showWordReport(String path) {
        FileUtils.viewWord(this, Uri.fromFile(new File(path)));
    }

    protected void sendReportMail(final String subject, final String path) {
        final String to = TextUtils.isEmpty(mobile) ? null : mobile + "@139.com";
        if (to == null) {
            showToast("未找到注册手机号");
            return;
        }
        UIUtil.showAlertDialog(this, "确定向" + to + "发送邮件?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendReportMail(to, subject, path);
            }
        }, null);
    }

    protected void sendReportMail(String to, String subject, String path) {
        String user = "13507194910";
        String pass = "Hbydjk01";
        String from = user + "@139.com";
        try {
            from = MimeUtility.encodeText("网络千里眼") + " <" + from + ">";
        } catch (UnsupportedEncodingException e) {
        }
        new SendMailTask(this).execute(user, pass, from, to, subject, path);
    }

    protected void showSearchDialog(Date date) {
        Context c = UIUtil.getSafeContext(this);
        final View dlg_view = View.inflate(c, R.layout.file_report_search, null);
        final DatePickerButton start_date = (DatePickerButton) dlg_view
                .findViewById(R.id.start_date);
        start_date.setDate(date);
        start_date.setText("日报开始时间");
        final DatePickerButton end_date = (DatePickerButton) dlg_view.findViewById(R.id.end_date);
        end_date.setDate(date);
        end_date.setText("日报结束时间");
        UIUtil.showAlertDialog(c, "搜索", dlg_view, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                onSearch(TextViewUtils.getText(dlg_view, R.id.text).toString(), start_date
                                .isDateSet() ? start_date.getDate() : null,
                        end_date.isDateSet() ? end_date.getDate() : null);
            }
        }, null);
    }

    protected void onSearch(String text, Date start, Date end) {
    }

    File getStorageDir() {
        File d = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        d.mkdirs();
        return d;
    }

    class ReportDownloader extends SimpleDownloadManager {
        int a;
        String name;

        public ReportDownloader(Context context, int action, String name) {
            super(context);
            a = action;
            this.name = name;
        }

        @Override
        protected void onDownloadFinished(String path) {
            if (a == 0)
                showWordReport(path);
            else
                sendReportMail(name + "-word", path);
        }
    }

    protected abstract BaseConfigLoader getConfigLoader();

    protected abstract String getQueryAction();

    protected abstract String getBasePath();
}

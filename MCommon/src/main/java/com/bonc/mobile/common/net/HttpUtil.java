package com.bonc.mobile.common.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.SSLCertificateSocketFactory;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.R;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.net.Download.OnDownloadListener;
import com.bonc.mobile.common.util.LogUtils;

/**
 * http请求的工具类.
 *
 * @author sunwei
 */
public class HttpUtil {
    private static final int REQUEST_TIMEOUT = 60 * 1000;// 设置请求超时60秒钟
    private static final int SO_TIMEOUT = 2 * 60 * 1000; // 设置等待数据超时时间120秒钟
    static Map<String, String> map = new HashMap<String, String>();

    public static String sendRequest(String basePath, String action,
                                     String params) throws IOException {

        String url = basePath + action + "?"
                + getUrlParams(User.getInstance().getUserMap()) + params;
        map.clear();
        map.put(url, null);

        HttpPost post = new HttpPost(basePath + action + "?"
                + getUrlParams(User.getInstance().getUserMap()));
        HttpEntity entity = new StringEntity(params);
        post.setEntity(entity);
        DefaultHttpClient hc = getHttpClient();
        HttpResponse response = hc.execute(post);
        String result = EntityUtils.toString(response.getEntity());
//        url +=params;
        LogUtils.logBySys("params:" + (params == null) + ";" + url);
//        LogUtils.debug(HttpUtil.class, url);

        map.put(url, result);
        return result;
    }

    /**
     * get 请求
     *
     * @param action 请求的action
     * @param params 参数
     * @return 请求结果字符串.
     * @throws IOException
     */
    public static String sendRequest(String basePath, String action,
                                     Map<String, String> params) throws IOException {
        Map<String, String> f_params = new LinkedHashMap<String, String>();
        if (params.get("nouser") == null)
            f_params.putAll(User.getInstance().getUserMap());
        else
            params.remove("nouser");
        f_params.putAll(params);
        String url = basePath + action + "?" + getUrlParams(f_params);
        if (!AppConstant.SEC_ENH) {
            System.out.println("http get" + "action:" + action + "url:" + url);
        }
        map.clear();
        map.put(url, null);

        HttpGet post = new HttpGet(url);
        DefaultHttpClient hc = getHttpClient();
        HttpResponse response = hc.execute(post);
        String result = null;
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

            result = EntityUtils.toString(response.getEntity());
        }
        // map.clear();
        map.put(url, result);

        return result;
    }

    /**
     * post请求
     *
     * @param action 请求的action
     * @param params 参数
     * @return 请求结果字符串.
     * @throws IOException
     */
    public static String sendRequest2(String basePath, String action,
                                      Map<String, String> params) throws IOException {
        Map<String, String> f_params = new LinkedHashMap<String, String>();
        if (params.get("nouser") == null)
            f_params.putAll(User.getInstance().getUserMap());
        else
            params.remove("nouser");
        f_params.putAll(params);

        String url = basePath + action + "?" + getUrlParams(f_params);
        if (!AppConstant.SEC_ENH) {
            System.out.println("http post" + "action:" + action + "url:" + url);
        }
        map.clear();
        map.put(url, null);

        HttpPost post = new HttpPost(basePath + action + "?");
        HttpEntity entity = getFormEntity(f_params);
        post.setEntity(entity);

        DefaultHttpClient hc = getHttpClient();
        HttpResponse response = hc.execute(post);
        String result = null;
        // if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

        result = EntityUtils.toString(response.getEntity());
        // }

        // map.clear();
        map.put(url, result);

        return result;
    }

    /**
     * 获取请求的URL及返回值
     *
     * @return
     */
    public static Map<String, String> getMap() {
        return map;
    }

    // public static Map<String,String>setMap(String value){
    // String key = map.
    // map.clear();
    // map.put(key, value)
    // }

    public static DefaultHttpClient getHttpClient() {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        DefaultHttpClient hc = new DefaultHttpClient(httpParams);
        try {
            SSLSocketFactory f = SSLCertificateSocketFactory.getInsecure(
                    REQUEST_TIMEOUT, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(f);
            HttpsURLConnection
                    .setDefaultHostnameVerifier(new AllowAllHostnameVerifier());
            // https
            Scheme scheme = new Scheme("https", new MySSLSocketFactory(f), 443);

            // http
            // Scheme scheme = new Scheme("http",
            // PlainSocketFactory.getSocketFactory(),
            // 80);

            hc.getConnectionManager().getSchemeRegistry().register(scheme);
        } catch (Exception e) {
        }
        hc.addResponseInterceptor(new HttpResponseInterceptor() {

            public void process(final HttpResponse response,
                                final HttpContext context) throws HttpException,
                    IOException {
                HttpEntity entity = response.getEntity();
                Header ceheader = entity.getContentEncoding();
                if (ceheader != null) {
                    HeaderElement[] codecs = ceheader.getElements();
                    for (int i = 0; i < codecs.length; i++) {
                        if (codecs[i].getName().contains("gzip")) {
//                            GzipDecompressingEntity entity1 = response.getEntity();
                            response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                            return;
                        }
                    }
                }
            }

        });
        return hc;
    }

    static class MySSLSocketFactory extends
            org.apache.http.conn.ssl.SSLSocketFactory {
        SSLSocketFactory socketfactory;

        final SSLContext sslContext = SSLContext.getInstance("TLSv1");

        public MySSLSocketFactory(SSLSocketFactory socketfactory)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(null);

            TrustManager tm = new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                    // TODO Auto-generated method stub

                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                    // TODO Auto-generated method stub

                }
            };
            sslContext.init(null, new TrustManager[]{tm}, null);
            socketfactory = sslContext.getSocketFactory();

            this.socketfactory = socketfactory;
        }

        public MySSLSocketFactory(KeyStore trustStore) throws Throwable {
            super(trustStore);
            TrustManager tm = new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                    // TODO Auto-generated method stub

                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                    // TODO Auto-generated method stub

                }
            };
            sslContext.init(null, new TrustManager[]{tm}, null);
            socketfactory = sslContext.getSocketFactory();
        }

        @Override
        public Socket createSocket() throws IOException {
            return socketfactory.createSocket();
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                                   boolean autoClose) throws IOException, UnknownHostException {
            return socketfactory.createSocket(socket, host, port, autoClose);
        }

        public static org.apache.http.conn.ssl.SSLSocketFactory getSocketFactory(
                Context context) {
            try {
                InputStream ins = context.getResources().openRawResource(
                        R.raw.cert12306);

                KeyStore trustStore = KeyStore.getInstance(KeyStore
                        .getDefaultType());
                ins.close();
                org.apache.http.conn.ssl.SSLSocketFactory factory = new MySSLSocketFactory(
                        trustStore);
                return factory;
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //压缩文件处理的实体包装类
    static class GzipDecompressingEntity extends HttpEntityWrapper {
        public GzipDecompressingEntity(final HttpEntity entity) {
            super(entity);
        }
        @Override
        public InputStream getContent()
                throws IOException, IllegalStateException {
            // the wrapped entity's getContent() decides about repeatability
            InputStream wrappedin = wrappedEntity.getContent();
            return new GZIPInputStream(wrappedin);
        }
        @Override
        public long getContentLength() {
            // length of ungzipped content is not known
            return -1;
        }

    }
    public static UrlEncodedFormEntity getFormEntity(Map<String, String> map)
            throws UnsupportedEncodingException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            params.add(new BasicNameValuePair(key, map.get(key)));
        }
        return new UrlEncodedFormEntity(params, "utf-8");
    }

    public static String getUrlParams(Map<String, String> map)
            throws IOException {
        return EntityUtils.toString(getFormEntity(map));
    }

    public static void download(String url, String path, OnDownloadListener odl) {
        Downloader downloader = new Downloader();
        downloader.setDownPath(url);
        downloader.setSavePath(path);
        downloader.setOnDownloadListener(odl);
        downloader.download();
    }
}

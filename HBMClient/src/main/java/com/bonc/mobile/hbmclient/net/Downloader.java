package com.bonc.mobile.hbmclient.net;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader extends Thread implements Download {

	public static final int TIME_OUT = 10000;
	private static byte[] cache = new byte[10240];// 缓存为10K

	// public static final String IMAGE_URL =
	// "http://192.168.25.36:8080/Aset2/app/android/AndroidImage!getImageByResId.action?resId=";

	private String urlPath;
	private String savePath;
	private String PID;// 图片的ID

	private int range = 0;// 下载起始位置

	private OnDownloadListener odl;
	private boolean finish = false;
	private boolean work = false;

	public Downloader() {
	}

	@Override
	public void setDownPath(String url) {
		this.urlPath = url;

	}

	@Override
	public void setSavePath(String path) {
		this.savePath = path;

	}

	@Override
	public void setCacheSize(int size) {
		// cache = new byte[size];
	}

	@Override
	public void setRange(int range) {
		this.range = range;
	}

	public void run() {
		try {
			download();
		} catch (Exception e) {
		}
	}

	@Override
	public void download() throws Exception {
		new Thread() {
			public void run() {
				boolean noDownload = false;// 下载失败标志
				work = true;
				try {
					URL url = new URL(urlPath);
					// HttpURLConnection httpHead = (HttpURLConnection)
					// url.openConnection();
					// httpHead.setConnectTimeout(TIME_OUT);
					// httpHead.setRequestMethod("GET");
					// //http.setRequestProperty("Accept",
					// "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
					// //http.setRequestProperty("Accept-Language", "zh-CN");
					// //http.setRequestProperty("Referer", urlPath);
					// //http.setRequestProperty("Charset", "UTF-8");
					// //http.setRequestProperty("User-Agent",
					// "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
					// httpHead.setRequestProperty("Connection", "Keep-Alive");
					// P.p("connect the web");
					// httpHead.connect();
					// P.p("connect end");
					// httpHead.disconnect();

					HttpURLConnection http = (HttpURLConnection) url
							.openConnection();

					http.setConnectTimeout(TIME_OUT);
					http.setRequestMethod("GET");
					http.setRequestProperty("Range", "bytes=" + range + "-");
					http.setRequestProperty("Charset", "UTF-8");
					http.setRequestProperty("Connection", "Keep-Alive");

					http.connect();

					int code = http.getResponseCode();

					if (200 <= code && 300 >= code) {

						InputStream inStream;

						try {
							inStream = http.getInputStream();
						} catch (Exception e) {
							throw new Exception(
									"get the url InputStream an error happend ");
						}

						int off = 0;

						File saveFile = new File(savePath);

						if (!saveFile.exists()) {

							saveFile.getParentFile().mkdirs();
							saveFile.createNewFile();
						}

						RandomAccessFile randomFile = new RandomAccessFile(
								new File(savePath), "rwd");

						int fileSize = http.getContentLength();

						if (fileSize != randomFile.length() && range == 0) {
							randomFile.setLength(fileSize);
						}

						randomFile.seek(range);

						int downSize = range;

						while ((off = inStream.read(cache)) != -1) {

							if (!finish) {

								randomFile.write(cache, 0, off);

								downSize += off;

								if (odl != null) {

									odl.onDownload(downSize, fileSize);
								}
							} else {

								work = false;

								if (odl != null) {

									odl.onStop(downSize, urlPath, savePath);
								}

								break;
							}
						}
						if (work) {

							if (odl != null) {

								odl.onFinished(downSize, urlPath, savePath);
							}
						}

						randomFile.close();
						inStream.close();
						http.disconnect();

					} else {

						noDownload = true;

					}
				} catch (Exception e) {
					noDownload = true;
				}
				work = false;
				finish = false;
				if (noDownload) {
					if (odl != null) {
						odl.OnError("the service has no response");
					}
				}
			}
		}.start();

	}

	public void setOnDownloadListener(OnDownloadListener odl) {
		this.odl = odl;
	}

	@Override
	public void setFinish(boolean finish) {
		this.finish = finish;

	}

	@Override
	public boolean getFinish() {
		// TODO Auto-generated method stub
		return this.finish;
	}

	public boolean isWork() {
		return this.work;
	}

}

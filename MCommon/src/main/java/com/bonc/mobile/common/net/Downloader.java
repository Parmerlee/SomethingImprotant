package com.bonc.mobile.common.net;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader extends Thread implements Download {

	public static final int TIME_OUT = 10000;
	private static byte[] cache = new byte[10240];// 缓存为10K

	private String urlPath;
	private String savePath;
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
	public void download() {
		new Thread() {
			public void run() {
				boolean noDownload = false;// 下载失败标志
				work = true;
				try {
					URL url = new URL(urlPath);
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

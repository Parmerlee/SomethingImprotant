package com.bonc.mobile.hbmclient.util;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

/**
 * Android设备相关工具类
 * 
 * @author Hyy
 */
public class DeviceUtils {

	private static DeviceUtils deviceUtils = null;

	public static DeviceUtils getInstance() {
		if (deviceUtils == null) {
			deviceUtils = new DeviceUtils();
		}
		return deviceUtils;
	}

	/**
	 * 检查SD卡是否已经准备就绪
	 * 
	 * @return true:准备就绪; <br>
	 *         false:没有准备就绪;
	 */
	public boolean isSDCardReady() {
		if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment
				.getExternalStorageState()))
			return true;
		else
			return false;
	}

	/**
	 * @return SD卡剩余空间，单位MB
	 */
	public long getSDCardFreeSize() {
		File file = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(file.getPath());
		long blockSize = sf.getBlockSize();
		long freeBlockNumber = sf.getAvailableBlocks();
		return blockSize * freeBlockNumber / 1024 / 1024;
	}

	/**
	 * @return SD卡整体空间大小，单位MB
	 */
	public long getSDCardTotalSize() {
		// 取得SD卡文件路径
		File file = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(file.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 获取所有数据块数
		long allBlocks = sf.getBlockCount();
		// 返回SD卡大小
		// return allBlocks * blockSize; //单位Byte
		// return (allBlocks * blockSize)/1024; //单位KB
		return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

}

package com.bonc.mobile.hbmclient.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	private static final int EOF = -1;

	public static void main(String[] args) throws IOException {
		File src = new File("D:/temp/data_1018.db");
		File zip = new File("D:/temp/data_1018.zip");
		File dest = new File("D:/temp");
		unzip(zip, dest);

	}

	/**
	 * 将指定目录下的所有文件压缩并生成指定路径的压缩文件. 如果压缩文件的路径或父路径不存在, 将会自动创建.
	 * 
	 * @param src
	 *            将要进行压缩的目录
	 * @param zip
	 *            最终生成的压缩文件的路径
	 */
	public static void zip(File src, File zip) throws IOException {
		zip(src, new FileOutputStream(zip));
	}

	/**
	 * 将指定目录下的所有文件压缩并将流写入指定的输出流中.
	 * 
	 * @param src
	 *            将要进行压缩的目录
	 * @param out
	 *            用于接收压缩产生的文件流的输出流
	 */
	public static void zip(File src, OutputStream out) throws IOException {
		zip(src, new ZipOutputStream(out));
	}

	/**
	 * 将指定目录下的所有文件压缩并将流写入指定的ZIP输出流中.
	 * 
	 * @param src
	 *            将要进行压缩的目录
	 * @param zout
	 *            用于接收压缩产生的文件流的ZIP输出流
	 */
	public static void zip(File src, ZipOutputStream zout) throws IOException {
		try {
			doZip(src, zout, "");
		} finally {
			zout.close();
		}
	}

	/**
	 * 将指定的压缩文件解压到指定的目标目录下. 如果指定的目标目录不存在或其父路径不存在, 将会自动创建.
	 * 
	 * @param zip
	 *            将会解压的压缩文件
	 * @param dest
	 *            解压操作的目录目录
	 */
	public static void unzip(File zip, File dest) throws IOException {
		unzip(new FileInputStream(zip), dest);
	}

	/**
	 * 将指定的输入流解压到指定的目标目录下.
	 * 
	 * @param in
	 *            将要解压的输入流
	 * @param dest
	 *            解压操作的目标目录
	 */
	public static void unzip(InputStream in, File dest) throws IOException {
		unzip(new ZipInputStream(in), dest);
	}

	/**
	 * 将指定的ZIP输入流解压到指定的目标目录下.
	 * 
	 * @param zin
	 *            将要解压的ZIP输入流
	 * @param dest
	 *            解压操作的目标目录
	 */
	public static void unzip(ZipInputStream zin, File dest) throws IOException {
		try {
			doUnzip(zin, dest);
		} finally {
			zin.close();
		}
	}

	/**
	 * @param src
	 * @param zout
	 * @param ns
	 */
	private static void doZip(File src, ZipOutputStream zout, String ns)
			throws IOException {
		for (File file : src.listFiles()) {
			String entryName = ns + file.getName();
			if (file.isDirectory()) {
				zout.putNextEntry(new ZipEntry(entryName + "/"));
				doZip(file, zout, entryName + "/");
			} else {
				zout.putNextEntry(new ZipEntry(entryName));
				fillZip(new FileInputStream(file), zout);
			}
		}
	}

	/**
	 * @param zin
	 * @param dest
	 */
	private static void doUnzip(ZipInputStream zin, File dest)
			throws IOException {
		for (ZipEntry e; (e = zin.getNextEntry()) != null; zin.closeEntry()) {
			File file = new File(dest, e.getName());
			if (e.isDirectory()) {
				forceMkdir(file);
			} else {
				if (!dest.exists()) {
					dest.mkdirs();
				}
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				flushZip(zin, new FileOutputStream(file));
			}
		}
	}

	private static void forceMkdir(File directory) throws IOException {
		if (directory.exists()) {
			if (!directory.isDirectory()) {
				String message = "File " + directory + " exists and is "
						+ "not a directory. Unable to create directory.";
				throw new IOException(message);
			}
		} else {
			if (!directory.mkdirs()) {
				// Double-check that some other thread or process hasn't made
				// the directory in the background
				if (!directory.isDirectory()) {
					String message = "Unable to create directory " + directory;
					throw new IOException(message);
				}
			}
		}
	}

	/**
	 * @param in
	 * @param zout
	 */
	private static void fillZip(InputStream in, ZipOutputStream zout)
			throws IOException {
		try {
			copy(in, zout);
		} finally {
			closeQuietly(in);
		}
	}

	/**
	 * @param zin
	 * @param out
	 */
	private static void flushZip(ZipInputStream zin, OutputStream out)
			throws IOException {
		try {
			copy(zin, out);
		} finally {
			if (out != null) {
				out.close();
			}
			closeQuietly(out);
		}
	}

	private static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	private static int copy(InputStream input, OutputStream output)
			throws IOException {
		long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	private static long copyLarge(InputStream input, OutputStream output)
			throws IOException {
		return copyLarge(input, output, new byte[DEFAULT_BUFFER_SIZE]);
	}

	private static long copyLarge(InputStream input, OutputStream output,
			byte[] buffer) throws IOException {
		long count = 0;
		int n = 0;
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

}

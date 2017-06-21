package com.bonc.mobile.hbmclient.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DefaultEncrypt {

	private static final String defaultKey = "hbmc0730";

	// 默认加密
	public static String encrpyt(String encryptString) {

		return encryptDES(encryptString, defaultKey);

	}

	// 默认解密
	public static String decrypt(String decryptString) {

		return decryptDES(decryptString, defaultKey);

	}

	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	public static String encryptDES(String encryptString, String encryptKey) {

		if (StringUtil.isNull(encryptString)) {
			return "";
		}
		try {
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");

			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
			return Base64.encode(encryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String decryptDES(String decryptString, String decryptKey) {

		if (StringUtil.isNull(decryptString)) {
			return "";
		}
		try {
			byte[] byteMi = Base64.decode(decryptString);
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");

			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			byte decryptedData[] = cipher.doFinal(byteMi);

			return new String(decryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
}

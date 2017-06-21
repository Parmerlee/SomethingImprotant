package com.bonc.mobile.common.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.text.GetChars;
import android.text.TextUtils;
import android.util.Log;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.User;

//import de.greenrobot.event.EventBus;
import org.greenrobot.eventbus.EventBus;

public class DES {
	// 密钥
	private final static String secretKey = "lyslyslyslyslyslyslyslys";
	// private final static String secretKey = "yE9eDqwpdMo3vMtGoBJ5gdX1";
	// 向量
	private final static String iv = "01234567";
	// 加解密统一使用的编码方式
	private final static String encoding = "utf-8";

	/***
	 * Des 加密
	 * 
	 * @param plainText
	 * @return
	 */
	public static String encrypt(String plainText) {
		if (plainText == null)
			return plainText;
		try {
			Key deskey = null;
			DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
			SecretKeyFactory keyfactory = SecretKeyFactory
					.getInstance("desede");
			deskey = keyfactory.generateSecret(spec);

			Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
			IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
			byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
			return Base64.encode(encryptData);
		} catch (Exception e) {
			e.printStackTrace();
			return plainText;
		}
	}

	// //为下面方法的备份
	// public static String decrypt(String encryptText) {
	// String str = null;
	// try {
	// Key deskey = null;
	// DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
	// SecretKeyFactory keyfactory = SecretKeyFactory
	// .getInstance("desede");
	// deskey = keyfactory.generateSecret(spec);
	// Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
	// IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
	// cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
	//
	// byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));
	//
	// str = new String(decryptData, encoding);
	// LogUtils.debug("AAAA", "解密后：" + str);
	// return str;
	// } catch (Exception e) {
	// // EventBus.getDefault().post("数据解密异常：" + e.toString());
	// LogUtils.debug(DES.class, "数据解密异常：" + e.toString());
	// e.printStackTrace();
	// }
	//
	// LogUtils.debug("AAAA",
	// "msg:" + str + ";empty:" + TextUtils.isEmpty(str));
	// return TextUtils.isEmpty(str) ? encryptText : str;
	//
	// }

	/**
	 * des 解密
	 * 
	 * @param encryptText
	 * @return
	 */
	public static String decrypt(String encryptText) {
		Key deskey = null;
		DESedeKeySpec spec;
		try {
			spec = new DESedeKeySpec(secretKey.getBytes());

			SecretKeyFactory keyfactory = SecretKeyFactory
					.getInstance("desede");
			deskey = keyfactory.generateSecret(spec);
			Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
			IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

			byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));

			return new String(decryptData, encoding);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return encryptText;
		}
	}

	/**
	 * DES解密
	 * 
	 * @param encryptText
	 *            加密文本
	 * @return
	 * @throws Exception
	 */
	public static String decode(String encryptText) throws Exception {
		encryptText = encryptText.substring(1, encryptText.length() - 1);
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

		byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));

		return new String(decryptData, encoding);
	} 

	public static String decrypt2(String result) {

		LogUtils.debug(DES.class, "解密之前数据:" + result + ";product:"
				+ User.getInstance().isProduct()+";mode:"+User.getInstance().mode);
		if (result == null || result.length() == 0)
			return result;
		if (User.getInstance().isProduct() && !result.startsWith("{")
				&& !result.startsWith("[")) {
			if (result.startsWith("\""))
				result = decrypt(result.substring(1, result.length() - 1));
			else
				result = decrypt(result);
		}
		return result;
	}

	/***
	 * DES 参数加密
	 * 
	 * @param map
	 */
	public static void encrypt(Map<String, String> map) {
		for (String key : map.keySet()) {
			map.put(key, encrypt(map.get(key)));
		}
	}

	/***
	 * Base64 参数加密
	 * 
	 * @param map
	 */
	public static void encryptByBase64(Map<String, String> map) {
		for (String key : map.keySet()) {
			if (!TextUtils.isEmpty(map.get(key)))
				map.put(key, Base64.encode((map.get(key).getBytes())));
		}
	}

	public static void main(String[] args) {
		System.out
				.println(decrypt("rK2J9ISCJpZKjfSCyE9eDqqTb3fKmXBJZu8nF7E7G8psP05+MXb3Sw=="));
	}
}

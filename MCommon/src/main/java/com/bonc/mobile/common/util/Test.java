package com.bonc.mobile.common.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;


public class Test {  
	// 密钥  	lyslyslyslyslyslyslyslys		yE9eDqwpdMo3vMtGoBJ5gdX1						 
    private final static String secretKey = "lyslyslyslyslyslyslyslys";  
    // 向量  
    private final static String iv = "01234567";  
    // 加解密统一使用的编码方式  
    private final static String encoding = "utf-8";  
  
    /** 
     * 3DES加密 
     *  
     * @param plainText 普通文本 
     * @return 
     * @throws Exception  
     */  
    public static String encode(String plainText) throws Exception {  
        Key deskey = null;  
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());  
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");  
        deskey = keyfactory.generateSecret(spec);  
  
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");  
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());  
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);  
        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));  
        return Base64.encode(encryptData);  
    }  
  
    /** 
     * 3DES解密 
     *  
     * @param encryptText 加密文本 
     * @return 
     * @throws Exception 
     */  
    public static String decode(String encryptText) throws Exception {  
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
    
    public static void main(String[] args) throws Exception{
    	String encode="aHR0cDovLzEyMC4yMDIuMTcuMTI2OjgwNzAvaGJtTG9naW4vNGFMb2dpbj91 c2VyNEFDb2RlPWJYWjFZbmRxTkRWb2VrZHBhVGw0Y0cxU05FVldVVDA5JnVz ZXI0QVB3ZD1SV3BsY25KbEszbHdSMGxDUjFSVVJXOVdiMkp5ZHowOSZwaG9u ZT1NMEZOYUdaSE9FeGxkSGN4TDI1cGIybGpiU3MwWnowOSZzbXNDb2RlPWJI bEpSa2hSZEdGQldFMDkmc2Vzc2lvbklkPVNHUmpOR3c0SzFGcmNGUnlWRmd2 ZDBRME5VWkRibXhvZEdSSGJFczVjSEYxUWpscWJEQnhUWFoxY0ZoRitNakYz Y1ZKYVZtOUJQVDAlM0QmY2hhbm5lbD1kM2M0YzBoNllYZE5hRlU5Jm9zPVUy ZFpiVFZuZFRSWlQxRTkmaW1laT1UaTkzWnpoR2FIZFFiaTlXV0ZsWWN6Vm9l RXhOVVQwOSZp";
    	System.out.println(Base64.encode(encode.getBytes()));
    }
}  
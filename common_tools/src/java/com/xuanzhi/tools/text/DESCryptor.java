package com.xuanzhi.tools.text;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class DESCryptor {

    /**
     * 已知密钥的情况下加密
     */
    public static String encode(String str, String key) throws Exception {
        SecureRandom sr = new SecureRandom();
        byte[] rawKey = Base64.decode(key);

        DESKeySpec dks = new DESKeySpec(rawKey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(dks);

        javax.crypto.Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);

        byte data[] = str.getBytes("UTF8");
        byte encryptedData[] = cipher.doFinal(data);
        return new String(Base64.encode(encryptedData));
    }


    /**
     * 已知密钥的情况下解密
     *
     * @param str 加密串
     * @param key key
     * @return decode
     * @throws Exception Exception
     */
    public static String decode(String str, String key) throws Exception {
        SecureRandom sr = new SecureRandom();
        byte[] rawKey = Base64.decode(key);
        DESKeySpec dks = new DESKeySpec(rawKey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
        byte encryptedData[] = Base64.decode(str);
        byte decryptedData[] = cipher.doFinal(encryptedData);
        return new String(decryptedData, "UTF8");
    }

  public static void main(String args[]) {

      String str1 = "123456789";
      
      try {
		System.out.println("trace:" + encode(str1, "gdfgfdgdfgdgsdfdfdfd"));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
}

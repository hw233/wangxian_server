package com.fy.boss.platform.qq;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.Security;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class DES3Secret {
  private final String TriDes = "DESede/ECB/NoPadding";

  private byte[] keyBytes = null;

  public DES3Secret(String key) {
    Security.addProvider(new com.sun.crypto.provider.SunJCE());
    this.keyBytes = key.getBytes();
  }

  public DES3Secret(String key1, String key2, String key3) {
    Security.addProvider(new com.sun.crypto.provider.SunJCE());
    this.keyBytes = (key1+key2+key3).getBytes();
  }

  public byte[] encrypt(byte[] encryptValue) throws Exception {
    return this.trides_crypt(this.keyBytes, encryptValue);
  }

  public byte[] decrypt(byte[] decryptValue) throws Exception {
    return this.trides_decrypt(this.keyBytes, decryptValue);
  }

  public byte[] trides_crypt(byte key[], byte data[]) {
    try {
      byte[] k = new byte[24];

      int len = data.length;
      if (data.length % 8 != 0) {
        len = data.length - data.length % 8 + 8;
      }
      byte[] needData = null;
      if (len != 0)
        needData = new byte[len];

      for (int i = 0; i < len; i++) {
        needData[i] = 0x00;
      }

      System.arraycopy(data, 0, needData, 0, data.length);

      if (key.length == 16) {
        System.arraycopy(key, 0, k, 0, key.length);
        System.arraycopy(key, 0, k, 16, 8);
      }
      else {
        System.arraycopy(key, 0, k, 0, 24);
      }

      KeySpec ks = new DESedeKeySpec(k);
      SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
      SecretKey ky = kf.generateSecret(ks);

      Cipher c = Cipher.getInstance(TriDes);
      c.init(Cipher.ENCRYPT_MODE, ky);
      return c.doFinal(needData);
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }

  public byte[] trides_decrypt(byte key[], byte data[]) {
    try {
      byte[] k = new byte[24];

      int len = data.length;
      if (data.length % 8 != 0) {
        len = data.length - data.length % 8 + 8;
      }
      byte[] needData = null;
      if (len != 0)
        needData = new byte[len];

      for (int i = 0; i < len; i++) {
        needData[i] = 0x00;
      }

      System.arraycopy(data, 0, needData, 0, data.length);

      if (key.length == 16) {
        System.arraycopy(key, 0, k, 0, key.length);
        System.arraycopy(key, 0, k, 16, 8);
      }
      else {
        System.arraycopy(key, 0, k, 0, 24);
      }
      KeySpec ks = new DESedeKeySpec(k);
      SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
      SecretKey ky = kf.generateSecret(ks);

      Cipher c = Cipher.getInstance(TriDes);
      c.init(Cipher.DECRYPT_MODE, ky);
      return c.doFinal(needData);
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }

  public static byte[] hexToBytes(String str) {
    if (str == null) {
      return null;
    }
    else if (str.length() < 2) {
      return null;
    }
    else {
      int len = str.length() / 2;
      byte[] buffer = new byte[len];
      for (int i = 0; i < len; i++) {
        buffer[i] = (byte) Integer.parseInt(str.substring(i * 2,
            i * 2 + 2), 16);
      }
      return buffer;
    }
  }
}
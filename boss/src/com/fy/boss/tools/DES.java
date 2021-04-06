package com.fy.boss.tools;

import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;


public class DES {
    private String Algorithm="DES";

    private KeyGenerator keygen;

    private SecretKey deskey;

    private Cipher c;

    private byte[] cipherByte;

    /**
     * ��ʼ�� DES ʵ��
     */
    public DES() {
        init();
    }

    public void init() {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        try {
            keygen=KeyGenerator.getInstance(Algorithm);
            deskey=keygen.generateKey();
            c=Cipher.getInstance(Algorithm);
        } catch(NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch(NoSuchPaddingException ex) {
            ex.printStackTrace();
        }
    }


    public byte[] createEncryptor(String str) {
        try {
            c.init(Cipher.ENCRYPT_MODE, deskey);
            cipherByte=c.doFinal(str.getBytes());
        } catch(java.security.InvalidKeyException ex) {
            ex.printStackTrace();
        } catch(javax.crypto.BadPaddingException ex) {
            ex.printStackTrace();
        } catch(javax.crypto.IllegalBlockSizeException ex) {
            ex.printStackTrace();
        }
        return cipherByte;
    }


    public String createDecryptor(byte[] buff) {
        try {
            c.init(Cipher.DECRYPT_MODE, deskey);
            cipherByte=c.doFinal(buff);
        } catch(java.security.InvalidKeyException ex) {
            ex.printStackTrace();
        } catch(javax.crypto.BadPaddingException ex) {
            ex.printStackTrace();
        } catch(javax.crypto.IllegalBlockSizeException ex) {
            ex.printStackTrace();
        }
        return(new String(cipherByte));
    }


//    public static String encode(String str, String key) throws Exception {
//
////        SecureRandom sr=new SecureRandom();
//        byte[] rawKey=(new sun.misc.BASE64Decoder()).decodeBuffer(key); //Base64.decode(key);
//        IvParameterSpec sr=new IvParameterSpec(rawKey);
//        DESKeySpec dks=new DESKeySpec(rawKey);
//        SecretKeyFactory keyFactory=SecretKeyFactory.getInstance("DES");
//        SecretKey secretKey=keyFactory.generateSecret(dks);
//
//        javax.crypto.Cipher cipher=Cipher.getInstance("DES/CBC/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
//
//        byte data[]=str.getBytes("UTF8");
//        byte encryptedData[]=cipher.doFinal(data);
//        return(new sun.misc.BASE64Encoder()).encodeBuffer(encryptedData);
//
//    }


    public static String decode(String str, String key) throws Exception {
//        SecureRandom sr=new SecureRandom();
        byte[] rawKey=(new sun.misc.BASE64Decoder()).decodeBuffer(key); //Base64.decode(key);
        IvParameterSpec sr=new IvParameterSpec(rawKey);
        DESKeySpec dks=new DESKeySpec(rawKey);
        SecretKeyFactory keyFactory=SecretKeyFactory.getInstance("DES");
        SecretKey secretKey=keyFactory.generateSecret(dks);
        Cipher cipher=Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
        byte encryptedData[]=(new sun.misc.BASE64Decoder()).decodeBuffer(str);
        byte decryptedData[]=cipher.doFinal(encryptedData);
        return new String(decryptedData, "UTF8");
    }

    public static void main(String[] args) {
        DES des=new DES();
        System.out.println("Test1...");
        try {
//            String en=des.encode("100@07773194573437965@323548109530138171", "x7VhhchMp0Y=");
//            System.out.print(en);
//            String de=des.decode(en, "x7VhhchMp0Y=");
//            System.out.println(de);
        } catch(Exception e) {
            System.out.println("Test1 Error!");
        }
    }

}

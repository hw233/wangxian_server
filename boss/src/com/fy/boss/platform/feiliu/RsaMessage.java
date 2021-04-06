package com.fy.boss.platform.feiliu;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import com.fy.boss.finance.service.platform.Base64;
import com.fy.boss.platform.feiliu.RsaMessage;


public class RsaMessage {

	 
	
	 /**
	  * 加密,key可以是公钥，也可以是私钥
	  *
	  * @param message
	  * @return
	  * @throws Exception
	  */
	 public String  encrypt(String message, Key key) throws Exception {
		 Cipher cipher = Cipher.getInstance("RSA");
		 cipher.init(Cipher.ENCRYPT_MODE, key);
		 byte[] encryptBytes=cipher.doFinal(message.getBytes());
		 return Base64.encode(encryptBytes);
	 }
	
	 /**
	  * 解密，key可以是公钥，也可以是私钥，如果是公钥加密就用私钥解密，反之亦然
	  *
	  * @param message
	  * @return
	  * @throws Exception
	  */
	 public String decrypt(String message, Key key) throws Exception {
		 Cipher cipher = Cipher.getInstance("RSA");
		 cipher.init(Cipher.DECRYPT_MODE, key);
		 //byte[] decryptBytes = cipher.doFinal(toBytes(message));
		 byte[] decryptBytes = cipher.doFinal(Base64.decode(message));
		 //return Base64.encode(decryptBytes);
		 return new String(decryptBytes);
	 }
	
	 /**
	  * 用私钥签名
	  *
	  * @param message
	  * @param key
	  * @return
	  * @throws Exception
	  */
	 public byte[] sign(String message, PrivateKey key) throws Exception {
		 Signature signetcheck = Signature.getInstance("MD5withRSA");
		 signetcheck.initSign(key);
		 signetcheck.update(message.getBytes("ISO-8859-1"));
		 return signetcheck.sign();
	 }
	
	 /**
	  * 用公钥验证签名的正确性
	  *
	  * @param message
	  * @param signStr
	  * @return
	  * @throws Exception
	  */
	 public boolean verifySign(String message, String signStr, PublicKey key) throws Exception {
		 if (message == null || signStr == null || key == null) {
			 return false;
		 }
		 Signature signetcheck = Signature.getInstance("MD5withRSA");
		 signetcheck.initVerify(key);
		 signetcheck.update(message.getBytes("ISO-8859-1"));
		 return signetcheck.verify(toBytes(signStr));
	 }
	
	 /**
	  * 从文件读取object
	  *
	  * @param fileName
	  * @return
	  * @throws Exception
	  */
	 private Object readFromFile(String fileName) throws Exception {
	  ObjectInputStream input = new ObjectInputStream(new FileInputStream(
	    fileName));
	  Object obj = input.readObject();
	  input.close();
	  return obj;
	 }
	 
	 /**
	  * 从pem文件读取RSA PublicKey
	  * filename 为pem文件访问绝对路径。
	  * @param fileName  
	  * @return
	  * @throws Exception
	  */
	 public static  RSAPublicKey initPublicKey(String keyFilePath)
	 {
		 //String privateFilePath = "F:\\rsa_pub_key.pem";
		 try{
			 	BufferedReader br = new BufferedReader(new FileReader(keyFilePath));   
			      String s = br.readLine();   
			      StringBuffer publicBuff = new StringBuffer();   
			      s = br.readLine();   
			      while (s.charAt(0) != '-') {   
			    	  publicBuff.append(s + "\r");   
			          s = br.readLine();   
			      }   
			            
			      byte[] keybyte = Base64.decode(publicBuff.toString());   
			          
			      KeyFactory kf = KeyFactory.getInstance("RSA");   
			      //PublicKey publicKey =  kf.generatePublic(keySpec);			      
			      //RSAPublicKeySpec	keySpec = new RSAPublicKeySpec();
			      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keybyte); 
		      
			      RSAPublicKey publicKey = (RSAPublicKey) kf.generatePublic(keySpec);
			      
			      return publicKey;
			      
		 }catch(Exception e)
		 {
			 return null;
		 }
	 }
	 
	 
	 /**
	  * 从pem文件读取RSA PrivateKey , 根据文件获取私钥
	  * filename 为pem文件访问绝对路径。
	  * @param fileName  
	  * @return
	  * @throws Exception
	  */
	 public static  RSAPrivateKey initPrivateKey(String keyFilePath)
	 {
		 //String privateFilePath = "F:\\rsa_pub_key.pem";
		 try{
			 	BufferedReader br = new BufferedReader(new FileReader(keyFilePath));   
			      String s = br.readLine();   
			      StringBuffer privateBuff = new StringBuffer();   
			      s = br.readLine();   
			      while (s.charAt(0) != '-') {   
			    	  privateBuff.append(s + "\r");   
			          s = br.readLine();   
			      }   
			            
			      byte[] keybyte = Base64.decode(privateBuff.toString());   
			          
			      KeyFactory kf = KeyFactory.getInstance("RSA");   
			      //PublicKey publicKey =  kf.generatePublic(keySpec);			      
			      //RSAPublicKeySpec	keySpec = new RSAPublicKeySpec();
			      //X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keybyte); 
			      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keybyte);
			      RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(keySpec);
			      
			      return privateKey;
			      
		 }catch(Exception e)
		 {
			 return null;
		 }
	 }
	
	 public static String toHexString(byte[] b) {
	  StringBuilder sb = new StringBuilder(b.length * 2);
	  for (int i = 0; i < b.length; i++) {
	   sb.append(HEXCHAR[(b[i] & 0xf0) >>> 4]);
	   sb.append(HEXCHAR[b[i] & 0x0f]);
	  }
	  return sb.toString();
	 }
	
	 public static final byte[] toBytes(String s) {
		 byte[] bytes;
		 bytes = new byte[s.length() / 2];
		 for (int i = 0; i < bytes.length; i++) {
			 bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),16);
		 }
		 return bytes;
	 }
	
	 private static char[] HEXCHAR = { '0', '1', '2', '3', '4', '5', '6', '7',
		 '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	 
	 
	 
	 
	 /**
	  * 测试函数
	  * filename 为pem文件访问绝对路径。
	  * @param fileName  
	  * @return
	  * @throws Exception
	  */
	 public static void main(String[] args) throws Exception {
		 String publicKeyFilePath = "F:\\100005_SignKey.pub";//"F:\\rsa_pub_key.pem";
		 String privateKeyFilePath = "F:\\100005.pri";

		 String messageStr = "走抽根烟去！";
		 System.out.println("原文：" + messageStr);
		
		 RsaMessage rsa = new RsaMessage ();
		 
		 //根据key文件载入生成 RSAPublicKey，载入公钥
		 RSAPublicKey publickKey = RsaMessage.initPublicKey(publicKeyFilePath);
		 //根据key文件载入生成RSAPrivateKey，载入私钥
		 RSAPrivateKey privateKey = RsaMessage.initPrivateKey(privateKeyFilePath);
		 
		 
		 //公钥加密
		 //根据RSAPublicKey 将 messageStr 生成 加密后字符串 encryptMessage
		 String encryptMessage = rsa.encrypt(messageStr, publickKey);
		 System.out.println("公钥加密后：");
		 System.out.println(encryptMessage);
		 
		 
		 //私钥解密：
		 String decryptMessage = rsa.decrypt(encryptMessage, privateKey);
		 System.out.println("私钥解密后：");
		 System.out.println(decryptMessage);
		 	 
		 
		 //私钥加密
		 encryptMessage = rsa.encrypt(messageStr, privateKey);
		 System.out.println("原文：" + messageStr);
		 System.out.println("私钥加密后：");
		 System.out.println(encryptMessage);
		 
		 
		 //公钥解密
		 decryptMessage = rsa.decrypt(encryptMessage, publickKey);
		 System.out.println("公钥解密后：");
		 System.out.println(decryptMessage);
		 
		 
		 /*byte[] encbyte = rsa.encrypt(str, privateKey);
		 System.out.println("私钥加密后：");
		 String encStrB = Base64.encodeBytes(encbyte);
		 String encStr = toHexString(encbyte);
		 System.out.println(encStrB);*/
		 
		 /*byte[] signBytes = rsa.sign(str, privateKey);
		 System.out.println("签名值：");
		 String signStr = toHexString(signBytes);
		 System.out.println(signStr);*/
		 
		 /*byte[] decByte = rsa.decrypt(encStr, publickKey);
		 System.out.println("公钥解密后：");
		 String decStr = new String(decByte);
		 System.out.println(decStr);*/
		 
		 /*if (rsa.verifySign(str, signStr, publickKey)) {
			 System.out.println("rsa sign check success");
		 } else {
			 System.out.println("rsa sign check failure");
		 }*/
	}
}
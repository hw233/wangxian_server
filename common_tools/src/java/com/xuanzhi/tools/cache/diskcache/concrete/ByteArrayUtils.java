package com.xuanzhi.tools.cache.diskcache.concrete;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ByteArrayUtils {
	/**
	 * 将网络传输的字节数组转化为整数，遵循高位字节在前的规则。
	 * 比如：字节数组为
	 *     {0x00,0x7c,0x12,0x34,0xab,0xcd,0x00,0x88}
	 * 那么对应的整数为为：
	 *     0x007c1234abcd0088     
	 */
	public static long byteArrayToNumber(byte[] bytes, int offset,int numOfBytes) {
		long l = 0L;
		for(int i = 0 ; i < numOfBytes ; i++){
			l = l | ((long)(bytes[offset + i] & 0xFF) << (8 * (numOfBytes - 1 - i)));
		}
		return l;
	}

	/**
	 * 将整数转化为网络传输的字节数组，遵循高位字节在前的规则。
	 * 比如：	整数为
	 *     0x 007c 1234 abcd 0088
	 * 那么对应的字节数组为：
	 *     {0x00,0x7c,0x12,0x34,0xab,0xcd,0x00,0x88}     
	 */
	public static byte[] numberToByteArray(long n, int numOfBytes) {
		byte bytes[] = new byte[numOfBytes];
		for(int i = 0 ; i < numOfBytes ; i++)
			bytes[i] = (byte)((n >> (8 *(numOfBytes - 1 - i))) & 0xFF);
		return bytes;
	}

	/**
	 * 将整数转化为网络传输的字节数组，遵循高位字节在前的规则。
	 * 比如：	整数为
	 *     0x 007c 1234 abcd 0088
	 * 那么对应的字节数组为：
	 *     {0x00,0x7c,0x12,0x34,0xab,0xcd,0x00,0x88}     
	 */
	public static byte[] numberToByteArray(int n, int numOfBytes) {
		byte bytes[] = new byte[numOfBytes];
		for(int i = 0 ; i < numOfBytes ; i++)
			bytes[i] = (byte)((n >> (8*(numOfBytes - 1 - i))) & 0xFF);
		return bytes;
	}
	
	/**
	 * 将对象转化为数组，可能抛出异常
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static byte[] objectToByteArray(Object obj) throws Exception{
		if(obj == null) return new byte[0];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(obj);
		oos.close();
		return out.toByteArray();
	}
	
	/**
	 * 将数组转化为对象，可能抛出异常
	 * @param bytes
	 * @param offset
	 * @param numOfBytes
	 * @return
	 * @throws Exception
	 */
	public static Object byteArrayToObject(byte[] bytes, int offset,int numOfBytes) throws Exception{
		if(numOfBytes == 0) return null;
		ByteArrayInputStream input = new ByteArrayInputStream(bytes,offset,numOfBytes);
		ObjectInputStream o = new ObjectInputStream(input);
		Object obj = o.readObject();
		o.close();
		return obj;
		
	}
	static MessageDigest digest;
	
	public synchronized static byte[] key_value_md5(byte[] data1,byte[] data2){
		if (digest == null) {
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nsae) {
				System.err.println("Failed to load the MD5 MessageDigest. "
						+ "Jive will be unable to function normally.");
				nsae.printStackTrace();
			}
		}
		// Now, compute hash.
		digest.update(data1);
		digest.update(data2);
		return digest.digest();
	}
}

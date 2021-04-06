package com.xuanzhi.tools.filesystem;



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


	
	public static byte[] key_value_md5(byte[] data1,byte[] data2){
		return new byte[16];
	}
}

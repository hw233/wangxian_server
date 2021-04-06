package com.xuanzhi.tools.cache.lateral.concrete.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.xuanzhi.tools.transport.AbstractMessageFactory;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.Message;
import com.xuanzhi.tools.transport.MessageFormatErrorException;

/**
 * 消息工厂类，我们定义了如下消息：
 * 
 * 每种消息的格式分为 HEADER 和 BODY
 * HEADER的格式如下：
 * 		| message length |  type        |sequnce number |
 * 		其中	message_length 为4个字节，sequnce number为4个字节，type为4个字节
 * BODY由各个消息来定义。
 * 
 * 数据的传输格式为：  数据类型 +  数据二进制序列
 * 数据类型我们定义以下几种：
 *      类型        类型值      长度
 * 		byte		1         1个字节
 * 		short       2         2个字节
 * 		char        3         2个字节
 * 		int         4         4个字节
 * 		long        5         8个字节
 * 		float       6         4个字节
 * 		double      7         8个字节
 * 		String      8         2个字节标识长度+n个字节（getBytes）,以unicode编码传输
 * 		Object      9         2个字节标识长度+n个字节（序列化后的byte数组）
 * 		
 */
public class TFWMessageFactory extends AbstractMessageFactory {

	private static long sequnceNum = 0L;
	public synchronized static long nextSequnceNum(){
		sequnceNum ++;
		if(sequnceNum >= 0x7FFFFFFF){
			sequnceNum = 1L;
		}
		return sequnceNum;
	}
	protected static TFWMessageFactory self;
	
	public static TFWMessageFactory getInstance(){
		if(self != null){
			return self;
		}
		synchronized(TFWMessageFactory.class){
			if(self != null) return self;
			self = new TFWMessageFactory();
		}
		return self;
	}
	public Message newMessage(byte[] messageContent)
			throws MessageFormatErrorException, ConnectionException {
		return newMessage(messageContent,0,messageContent.length);
	}
	public Message newMessage(byte[] messageContent,int offset,int size)
			throws MessageFormatErrorException, ConnectionException {
		int len = (int)byteArrayToNumber(messageContent,offset,getNumOfByteForMessageLength());
		if(len != size)
			throw new MessageFormatErrorException("message length not match");
		int end = offset + size;
		offset += getNumOfByteForMessageLength();
		long type = byteArrayToNumber(messageContent,offset,4);
		offset += 4;
		long sn = byteArrayToNumber(messageContent,offset,4);
		offset += 4;
		
		try{
			if(type == 0x00000002){
				return new ACTIVE_TEST_REQ(sn);
			}else if(type == 0x80000002L){
				return new ACTIVE_TEST_RES(sn,messageContent,offset,end-offset);
			}else if(type == 0x00000003L){
				try {
					return new GET_REQ(sn,messageContent,offset,end-offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct GET_REQ error:" + e.getMessage(),e);
				}
			}else if(type == 0x80000003L){
				try {
					return new GET_RES(sn,messageContent,offset,end-offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct GET_REQ error:" + e.getMessage(),e);
				}
			}else if(type == 0x00000004L){
				try {
					return new PUT_REQ(sn,messageContent,offset,end-offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct PUT_REQ error:" + e.getMessage(),e);
				}
			}else if(type == 0x80000004L){
				try {
					return new PUT_RES(sn,messageContent,offset,end-offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct PUT_RES error:" + e.getMessage(),e);
				}
			}else if(type == 0x00000005L){
				try {
					return new MODIFY_REQ(sn,messageContent,offset,end-offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct MODIFY_REQ error:" + e.getMessage(),e);
				}
			}else if(type == 0x80000005L){
				try {
					return new MODIFY_RES(sn,messageContent,offset,end-offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct MODIFY_RES error:" + e.getMessage(),e);
				}
			}else if(type == 0x00000006L){
				try {
					return new REMOVE_REQ(sn,messageContent,offset,end-offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct REMOVE_REQ error:" + e.getMessage(),e);
				}
			}else if(type == 0x80000006L){
				try {
					return new REMOVE_RES(sn,messageContent,offset,end-offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct REMOVE_RES error:" + e.getMessage(),e);
				}
			}else{
				throw new MessageFormatErrorException("unknown message type [0x"+Long.toHexString(type)+"]");
			}
		}catch(IndexOutOfBoundsException e){
			throw new ConnectionException("parse message error",e);
		}
	}
	/*
	 * 数据类型我们定义以下几种：
	 *      类型        类型值      长度
	 * 		byte		1         1个字节
	 * 		short       2         2个字节
	 * 		char        3         2个字节
	 * 		int         4         4个字节
	 * 		long        5         8个字节
	 * 		float       6         4个字节
	 * 		double      7         8个字节
	 * 		String      8         2个字节标识长度+n个字节（getBytes）,以unicode编码传输
	 * 		Object      9         2个字节标识长度+n个字节（序列化后的byte数组）
	 */
	public int getTypeValueOfObject(Object obj){
		if(obj == null) return 0;
		if(obj instanceof String){
			return 8; 
		}else if(obj instanceof Double){
			return 7;
		}else if(obj instanceof Float){
			return 6;
		}else if(obj instanceof Long){
			return 5;
		}else if(obj instanceof Integer){
			return 4;
		}else if(obj instanceof Character){
			return 3;
		}else if(obj instanceof Short){
			return 2;
		}else if(obj instanceof Byte){
			return 1;
		}else{
			return 9;
		}
	}
	
	public byte[] getByteArrayOfObject(Object obj) throws Exception{
		if(obj == null){
			return numberToByteArray(0,2);
		}else if(obj instanceof String){
			String s = (String)obj;
			byte bytes[] = s.getBytes();
			byte lenb[] = numberToByteArray(bytes.length,2);
			return merge(lenb,bytes);
		}else if(obj instanceof Double){
			Double d = (Double)obj;
			return numberToByteArray(Double.doubleToLongBits(d.doubleValue()),8);
		}else if(obj instanceof Float){
			Float d = (Float)obj;
			return numberToByteArray(Float.floatToIntBits(d.floatValue()),4);
		}else if(obj instanceof Long){
			Long d = (Long)obj;
			return numberToByteArray(d.longValue(),8);
		}else if(obj instanceof Integer){
			Integer d = (Integer)obj;
			return numberToByteArray(d.intValue(),8);
		}else if(obj instanceof Character){
			Character d = (Character)obj;
			short s = (short)d.charValue();
			return numberToByteArray(s,2);
		}else if(obj instanceof Short){
			Short s = (Short)obj;
			return numberToByteArray(s.shortValue(),2);
		}else if(obj instanceof Byte){
			Byte s = (Byte)obj;
			return new byte[]{s.byteValue()};
		}else{
			byte bytes[] = objectToByteArray(obj);
			byte lenb[] = numberToByteArray(bytes.length,2);
			return merge(lenb,bytes);
		}
	}
	
	public static byte[] merge(byte bytes1[],byte bytes2[]){
		byte bytes[] = new byte[bytes1.length + bytes2.length];
		System.arraycopy(bytes1,0,bytes,0,bytes1.length);
		System.arraycopy(bytes2,0,bytes,bytes1.length,bytes2.length);
		return bytes;
	}
	
	/**
	 * 当返回-1的时候，表示读取的是Object或者String，需要再读取2个字节
	 * 得到长度，然后再读取数据。
	 * 
	 * @param type
	 * @return
	 */
	public int getLengthByTypeValue(int type){
		if(type == 0){
			return -1;
		}else if(type == 1){
			return 1;
		}else if(type == 2){
			return 2;
		}else if(type == 3){
			return 2;
		}else if(type == 4){
			return 4;
		}else if(type == 5){
			return 8;
		}else if(type == 6){
			return 4;
		}else if(type == 7){
			return 8;
		}else if(type == 8){
			return -1;
		}else if(type == 9){
			return -1;
		}else{
			throw new IllegalArgumentException("invalid type of object ["+type+"]");
		}
	}
	
	/**
	 * 
	 * @param type
	 * @param bytes
	 * @param offset
	 * @param len
	 * @return
	 */
	public Object getObjectFromByteArray(int type, byte bytes[],int offset,int len) throws Exception{
		if(type == 0){
			return null;
		}else if(type == 1){
			return (byte)byteArrayToNumber(bytes,offset,1);
		}else if(type == 2){
			return (short)byteArrayToNumber(bytes,offset,2);
		}else if(type == 3){
			short s = (short)byteArrayToNumber(bytes,offset,2);
			return (char)s;
		}else if(type == 4){
			return (int)byteArrayToNumber(bytes,offset,4);
		}else if(type == 5){
			return (long)byteArrayToNumber(bytes,offset,8);
		}else if(type == 6){
			int s =  (int)byteArrayToNumber(bytes,offset,4);
			return Float.intBitsToFloat(s);
		}else if(type == 7){
			long s =  (long)byteArrayToNumber(bytes,offset,4);
			return Double.longBitsToDouble(s);
		}else if(type == 8){
			return new String(bytes,offset,len);
		}else if(type == 9){
			return byteArrayToObject(bytes,offset,len);
		}else{
			throw new IllegalArgumentException("invalid type of object ["+type+"]");
		}
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
}

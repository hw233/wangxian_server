package com.xuanzhi.tools.transport;

public interface MessageFactory {

	/**
	 * 消息长度用几个字节来标识。我们认为消息是这样的：
	 * |----|------------------------------------------------------|
	 *  长度                  消息内容  
	 */
	public int getNumOfByteForMessageLength();
	
	/**
	 * 将网络上读到的表达一个数的字节数组，翻译成一个数
	 * 默认情况下，我们都是采用高位字节在前的方法。
	 * 也就是bytes[0]对应高位字节，bytes[numOfBytes-1]对应地位字节。
	 * 
	 * @param bytes 网络上读到的数据
	 * @param numOfBytes 多少个字节表达一个数
	 * @return
	 */
	public long byteArrayToNumber(byte bytes[],int offset,int numOfBytes);

	/**
	 * 将一个数的翻译成网络上一个字节数组
	 * 默认情况下，我们都是采用高位字节在前的方法。
	 * 也就是bytes[0]对应高位字节，bytes[numOfBytes-1]对应地位字节。
	 * 
	 * @param bytes 网络上读到的数据
	 * @param numOfBytes 多少个字节表达一个数
	 * @return
	 */
	public byte[] numberToByteArray(long n,int numOfBytes);
	
	/**
	 * 将一个数的翻译成网络上一个字节数组
	 * 默认情况下，我们都是采用高位字节在前的方法。
	 * 也就是bytes[0]对应高位字节，bytes[numOfBytes-1]对应地位字节。
	 * 
	 * @param bytes 网络上读到的数据
	 * @param numOfBytes 多少个字节表达一个数
	 * @return
	 */
	public byte[] numberToByteArray(int n,int numOfBytes);
	
	/**
	 * 根据网络上传输来的内容，创建新的消息
	 * 
	 * @param messageContent
	 * @return
	 * @throws MessageFormatErrorException
	 * @throws ConnectionException 预示着出现网络异常，Connection发现这个异常后会自动关闭
	 */
	public Message newMessage(byte[] messageContent,int offset,int size) throws MessageFormatErrorException,ConnectionException,Exception;
}

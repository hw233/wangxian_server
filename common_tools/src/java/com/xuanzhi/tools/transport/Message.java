package com.xuanzhi.tools.transport;

import java.io.Serializable;
import java.nio.*;

/**
 * 消息体，是接收消息和发送消息的超类
 * 
 *
 */
public interface Message extends Serializable {
	
	/**
	 * 将消息的所有的内容，写入到一个Byte Buffer中，然后返回写入这个buffer的字节数。
	 * 这个方法保证要么所有的消息的内容写入Byte Buffer中，要么一个字节都不写入。
	 * 
	 * 返回0表示，Buffer中没有足够的空间来存放消息内容。
	 * 大于0，表示已经将全部消息内容写入Buffer中。
	 * 
	 * @return 
	 */
	public int writeTo(ByteBuffer buffer);
	
	/**
	 * 得到消息体内容的长度，按字节计算，包括消息所有的内容
	 */
	public int getLength();
	
	/**
	 * 得到消息体的序列号，每发出去的一个RequestMessage的系列号都是不一样的。
	 * 当链接收到一个ResponseMessage时，会比较滑动窗口中的所有RequestMessage，
	 * 根据这个函数的返回值来判断这个ResponseMessage对应的RequestMessage。
	 */
	public String getSequenceNumAsString();
	
	/**
	 * 得到消息体的序列号，每发出去的一个RequestMessage的系列号都是不一样的。
	 * 当链接收到一个ResponseMessage时，会比较滑动窗口中的所有RequestMessage，
	 * 根据这个函数的返回值来判断这个ResponseMessage对应的RequestMessage。
	 * @return
	 */
	public long getSequnceNum();
	
	/**
	 * 获得消息的数字id
	 * @return
	 */
	public int getType();
	
	/**
	 * 获得消息类型的描述，消息类型有具体的实现来定义，
	 * 而对于消息类型的描述，希望是可以方便阅读的字符串。
	 * @return
	 */
	public String getTypeDescription();
	
	
}

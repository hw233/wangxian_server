package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageItem;
import com.fy.engineserver.chat.ChatMessageTask;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端发送聊天消息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chatMessage.groupId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chatMessage.chatTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chatMessage.senderName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chatMessage.senderName</td><td>String</td><td>chatMessage.senderName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chatMessage.receiverId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chatMessage.senderId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chatMessage.campId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chatMessage.receiverName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chatMessage.receiverName</td><td>String</td><td>chatMessage.receiverName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chatMessage.sort</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chatMessage.messageText.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chatMessage.messageText</td><td>String</td><td>chatMessage.messageText.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chatMessage.chatType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chatMessageItem.id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chatMessageItem.color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chatMessageItem.name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chatMessageItem.name</td><td>String</td><td>chatMessageItem.name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chatMessageTask.name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chatMessageTask.name</td><td>String</td><td>chatMessageTask.name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chatMessageTask.id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * </table>
 */
public class CHAT_MESSAGE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	ChatMessage chatMessage;
	ChatMessageItem chatMessageItem;
	ChatMessageTask chatMessageTask;

	public CHAT_MESSAGE_REQ(){
	}

	public CHAT_MESSAGE_REQ(long seqNum,ChatMessage chatMessage,ChatMessageItem chatMessageItem,ChatMessageTask chatMessageTask){
		this.seqNum = seqNum;
		this.chatMessage = chatMessage;
		this.chatMessageItem = chatMessageItem;
		this.chatMessageTask = chatMessageTask;
	}

	public CHAT_MESSAGE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		chatMessage = new ChatMessage();
		chatMessage.setGroupId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		chatMessage.setChatTime((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		chatMessage.setSenderName(new String(content,offset,len,"UTF-8"));
		offset += len;
		chatMessage.setReceiverId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		chatMessage.setSenderId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		chatMessage.setCampId((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		chatMessage.setReceiverName(new String(content,offset,len,"UTF-8"));
		offset += len;
		chatMessage.setSort((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		chatMessage.setMessageText(new String(content,offset,len,"UTF-8"));
		offset += len;
		chatMessage.setChatType((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		chatMessageItem = new ChatMessageItem();
		chatMessageItem.setId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		chatMessageItem.setColor((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		chatMessageItem.setName(new String(content,offset,len,"UTF-8"));
		offset += len;
		chatMessageTask = new ChatMessageTask();
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		chatMessageTask.setName(new String(content,offset,len,"UTF-8"));
		offset += len;
		chatMessageTask.setId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
	}

	public int getType() {
		return 0x00000E02;
	}

	public String getTypeDescription() {
		return "CHAT_MESSAGE_REQ";
	}

	public String getSequenceNumAsString() {
		return String.valueOf(seqNum);
	}

	public long getSequnceNum(){
		return seqNum;
	}

	private int packet_length = 0;

	public int getLength() {
		if(packet_length > 0) return packet_length;
		int len =  mf.getNumOfByteForMessageLength() + 4 + 4;
		len += 8;
		len += 8;
		len += 2;
		if(chatMessage.getSenderName() != null){
			try{
			len += chatMessage.getSenderName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		len += 8;
		len += 4;
		len += 2;
		if(chatMessage.getReceiverName() != null){
			try{
			len += chatMessage.getReceiverName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 2;
		if(chatMessage.getMessageText() != null){
			try{
			len += chatMessage.getMessageText().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 8;
		len += 4;
		len += 2;
		if(chatMessageItem.getName() != null){
			try{
			len += chatMessageItem.getName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(chatMessageTask.getName() != null){
			try{
			len += chatMessageTask.getName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		packet_length = len;
		return len;
	}

	public int writeTo(ByteBuffer buffer) {
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		int oldPos = buffer.position();
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.putLong(chatMessage.getGroupId());
			buffer.putLong(chatMessage.getChatTime());
			byte[] tmpBytes1 ;
				try{
				tmpBytes1 = chatMessage.getSenderName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(chatMessage.getReceiverId());
			buffer.putLong(chatMessage.getSenderId());
			buffer.putInt((int)chatMessage.getCampId());
				try{
				tmpBytes1 = chatMessage.getReceiverName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt((int)chatMessage.getSort());
				try{
				tmpBytes1 = chatMessage.getMessageText().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt((int)chatMessage.getChatType());
			buffer.putLong(chatMessageItem.getId());
			buffer.putInt((int)chatMessageItem.getColor());
				try{
				tmpBytes1 = chatMessageItem.getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = chatMessageTask.getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(chatMessageTask.getId());
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		int newPos = buffer.position();
		buffer.position(oldPos);
		buffer.put(mf.numberToByteArray(newPos-oldPos,mf.getNumOfByteForMessageLength()));
		buffer.position(newPos);
		return newPos-oldPos;
	}

	/**
	 * 获取属性：
	 *	聊天消息
	 */
	public ChatMessage getChatMessage(){
		return chatMessage;
	}

	/**
	 * 设置属性：
	 *	聊天消息
	 */
	public void setChatMessage(ChatMessage chatMessage){
		this.chatMessage = chatMessage;
	}

	/**
	 * 获取属性：
	 *	聊天消息元素
	 */
	public ChatMessageItem getChatMessageItem(){
		return chatMessageItem;
	}

	/**
	 * 设置属性：
	 *	聊天消息元素
	 */
	public void setChatMessageItem(ChatMessageItem chatMessageItem){
		this.chatMessageItem = chatMessageItem;
	}

	/**
	 * 获取属性：
	 *	聊天消息元素
	 */
	public ChatMessageTask getChatMessageTask(){
		return chatMessageTask;
	}

	/**
	 * 设置属性：
	 *	聊天消息元素
	 */
	public void setChatMessageTask(ChatMessageTask chatMessageTask){
		this.chatMessageTask = chatMessageTask;
	}

}
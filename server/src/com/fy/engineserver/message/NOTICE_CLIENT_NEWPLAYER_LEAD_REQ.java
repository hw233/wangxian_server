package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 通知客户端新手引导<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>windowNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>windowNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>windowNames[0]</td><td>String</td><td>windowNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>windowNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>windowNames[1]</td><td>String</td><td>windowNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>windowNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>windowNames[2]</td><td>String</td><td>windowNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activeXs.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activeXs[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activeXs[0]</td><td>String</td><td>activeXs[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activeXs[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activeXs[1]</td><td>String</td><td>activeXs[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activeXs[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activeXs[2]</td><td>String</td><td>activeXs[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showMessages.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showMessages[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showMessages[0]</td><td>String</td><td>showMessages[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showMessages[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showMessages[1]</td><td>String</td><td>showMessages[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showMessages[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showMessages[2]</td><td>String</td><td>showMessages[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>outStackEvents.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>outStackEvents</td><td>int[]</td><td>outStackEvents.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prioritys.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prioritys</td><td>int[]</td><td>prioritys.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>types.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>types</td><td>int[]</td><td>types.length</td><td>*</td></tr>
 * </table>
 */
public class NOTICE_CLIENT_NEWPLAYER_LEAD_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String[] windowNames;
	String[] activeXs;
	String[] showMessages;
	int[] outStackEvents;
	int[] prioritys;
	int[] types;

	public NOTICE_CLIENT_NEWPLAYER_LEAD_REQ(){
	}

	public NOTICE_CLIENT_NEWPLAYER_LEAD_REQ(long seqNum,String[] windowNames,String[] activeXs,String[] showMessages,int[] outStackEvents,int[] prioritys,int[] types){
		this.seqNum = seqNum;
		this.windowNames = windowNames;
		this.activeXs = activeXs;
		this.showMessages = showMessages;
		this.outStackEvents = outStackEvents;
		this.prioritys = prioritys;
		this.types = types;
	}

	public NOTICE_CLIENT_NEWPLAYER_LEAD_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		windowNames = new String[len];
		for(int i = 0 ; i < windowNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			windowNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		activeXs = new String[len];
		for(int i = 0 ; i < activeXs.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activeXs[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		showMessages = new String[len];
		for(int i = 0 ; i < showMessages.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			showMessages[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		outStackEvents = new int[len];
		for(int i = 0 ; i < outStackEvents.length ; i++){
			outStackEvents[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		prioritys = new int[len];
		for(int i = 0 ; i < prioritys.length ; i++){
			prioritys[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		types = new int[len];
		for(int i = 0 ; i < types.length ; i++){
			types[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x0F300029;
	}

	public String getTypeDescription() {
		return "NOTICE_CLIENT_NEWPLAYER_LEAD_REQ";
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
		len += 4;
		for(int i = 0 ; i < windowNames.length; i++){
			len += 2;
			try{
				len += windowNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < activeXs.length; i++){
			len += 2;
			try{
				len += activeXs[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < showMessages.length; i++){
			len += 2;
			try{
				len += showMessages[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += outStackEvents.length * 4;
		len += 4;
		len += prioritys.length * 4;
		len += 4;
		len += types.length * 4;
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

			buffer.putInt(windowNames.length);
			for(int i = 0 ; i < windowNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = windowNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(activeXs.length);
			for(int i = 0 ; i < activeXs.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = activeXs[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(showMessages.length);
			for(int i = 0 ; i < showMessages.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = showMessages[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(outStackEvents.length);
			for(int i = 0 ; i < outStackEvents.length; i++){
				buffer.putInt(outStackEvents[i]);
			}
			buffer.putInt(prioritys.length);
			for(int i = 0 ; i < prioritys.length; i++){
				buffer.putInt(prioritys[i]);
			}
			buffer.putInt(types.length);
			for(int i = 0 ; i < types.length; i++){
				buffer.putInt(types[i]);
			}
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
	 *	窗口名
	 */
	public String[] getWindowNames(){
		return windowNames;
	}

	/**
	 * 设置属性：
	 *	窗口名
	 */
	public void setWindowNames(String[] windowNames){
		this.windowNames = windowNames;
	}

	/**
	 * 获取属性：
	 *	控件名
	 */
	public String[] getActiveXs(){
		return activeXs;
	}

	/**
	 * 设置属性：
	 *	控件名
	 */
	public void setActiveXs(String[] activeXs){
		this.activeXs = activeXs;
	}

	/**
	 * 获取属性：
	 *	显示文字
	 */
	public String[] getShowMessages(){
		return showMessages;
	}

	/**
	 * 设置属性：
	 *	显示文字
	 */
	public void setShowMessages(String[] showMessages){
		this.showMessages = showMessages;
	}

	/**
	 * 获取属性：
	 *	出栈事件
	 */
	public int[] getOutStackEvents(){
		return outStackEvents;
	}

	/**
	 * 设置属性：
	 *	出栈事件
	 */
	public void setOutStackEvents(int[] outStackEvents){
		this.outStackEvents = outStackEvents;
	}

	/**
	 * 获取属性：
	 *	左右优先
	 */
	public int[] getPrioritys(){
		return prioritys;
	}

	/**
	 * 设置属性：
	 *	左右优先
	 */
	public void setPrioritys(int[] prioritys){
		this.prioritys = prioritys;
	}

	/**
	 * 获取属性：
	 *	类型
	 */
	public int[] getTypes(){
		return types;
	}

	/**
	 * 设置属性：
	 *	类型
	 */
	public void setTypes(int[] types){
		this.types = types;
	}

}
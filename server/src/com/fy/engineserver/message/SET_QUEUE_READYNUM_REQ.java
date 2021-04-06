package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端设置网络参数,1 含义是有数据包立即发送  2 含义是有2个数据包立即发送，如果只有一个数据包，等到 0.2 * 2 = 0.4秒   3 含义是有3个数据包立即发送，如果只有一个或者两个数据包，等到 0.2 * 3 = 0.6秒<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>selectedQueueReadyNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>viewWidth</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>viewHeight</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class SET_QUEUE_READYNUM_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int selectedQueueReadyNum;
	int viewWidth;
	int viewHeight;

	public SET_QUEUE_READYNUM_REQ(){
	}

	public SET_QUEUE_READYNUM_REQ(long seqNum,int selectedQueueReadyNum,int viewWidth,int viewHeight){
		this.seqNum = seqNum;
		this.selectedQueueReadyNum = selectedQueueReadyNum;
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;
	}

	public SET_QUEUE_READYNUM_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		selectedQueueReadyNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		viewWidth = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		viewHeight = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70F00000;
	}

	public String getTypeDescription() {
		return "SET_QUEUE_READYNUM_REQ";
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
		len += 4;
		len += 4;
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

			buffer.putInt(selectedQueueReadyNum);
			buffer.putInt(viewWidth);
			buffer.putInt(viewHeight);
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
	 *	玩家设置的网络流量参数
	 */
	public int getSelectedQueueReadyNum(){
		return selectedQueueReadyNum;
	}

	/**
	 * 设置属性：
	 *	玩家设置的网络流量参数
	 */
	public void setSelectedQueueReadyNum(int selectedQueueReadyNum){
		this.selectedQueueReadyNum = selectedQueueReadyNum;
	}

	/**
	 * 获取属性：
	 *	视野的宽度
	 */
	public int getViewWidth(){
		return viewWidth;
	}

	/**
	 * 设置属性：
	 *	视野的宽度
	 */
	public void setViewWidth(int viewWidth){
		this.viewWidth = viewWidth;
	}

	/**
	 * 获取属性：
	 *	视野的高度
	 */
	public int getViewHeight(){
		return viewHeight;
	}

	/**
	 * 设置属性：
	 *	视野的高度
	 */
	public void setViewHeight(int viewHeight){
		this.viewHeight = viewHeight;
	}

}
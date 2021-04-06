package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 地物NPC信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcIds</td><td>long[]</td><td>npcIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showIndex.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showIndex</td><td>int[]</td><td>showIndex.length</td><td>*</td></tr>
 * </table>
 */
public class NOTICE_CLIENT_SURFACENPC_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] npcIds;
	int[] showIndex;

	public NOTICE_CLIENT_SURFACENPC_REQ(){
	}

	public NOTICE_CLIENT_SURFACENPC_REQ(long seqNum,long[] npcIds,int[] showIndex){
		this.seqNum = seqNum;
		this.npcIds = npcIds;
		this.showIndex = showIndex;
	}

	public NOTICE_CLIENT_SURFACENPC_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		npcIds = new long[len];
		for(int i = 0 ; i < npcIds.length ; i++){
			npcIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		showIndex = new int[len];
		for(int i = 0 ; i < showIndex.length ; i++){
			showIndex[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x000EEE0B;
	}

	public String getTypeDescription() {
		return "NOTICE_CLIENT_SURFACENPC_REQ";
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
		len += npcIds.length * 8;
		len += 4;
		len += showIndex.length * 4;
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

			buffer.putInt(npcIds.length);
			for(int i = 0 ; i < npcIds.length; i++){
				buffer.putLong(npcIds[i]);
			}
			buffer.putInt(showIndex.length);
			for(int i = 0 ; i < showIndex.length; i++){
				buffer.putInt(showIndex[i]);
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
	 *	地表类型NPC的ID
	 */
	public long[] getNpcIds(){
		return npcIds;
	}

	/**
	 * 设置属性：
	 *	地表类型NPC的ID
	 */
	public void setNpcIds(long[] npcIds){
		this.npcIds = npcIds;
	}

	/**
	 * 获取属性：
	 *	地表类型NPC的显示层次
	 */
	public int[] getShowIndex(){
		return showIndex;
	}

	/**
	 * 设置属性：
	 *	地表类型NPC的显示层次
	 */
	public void setShowIndex(int[] showIndex){
		this.showIndex = showIndex;
	}

}
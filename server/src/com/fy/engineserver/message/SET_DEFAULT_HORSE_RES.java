package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 玩家设置默认的马<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>defaultHorseId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class SET_DEFAULT_HORSE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	boolean result;
	long defaultHorseId;

	public SET_DEFAULT_HORSE_RES(){
	}

	public SET_DEFAULT_HORSE_RES(long seqNum,boolean result,long defaultHorseId){
		this.seqNum = seqNum;
		this.result = result;
		this.defaultHorseId = defaultHorseId;
	}

	public SET_DEFAULT_HORSE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		result = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		defaultHorseId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x70000126;
	}

	public String getTypeDescription() {
		return "SET_DEFAULT_HORSE_RES";
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
		len += 1;
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

			buffer.put((byte)(result==false?0:1));
			buffer.putLong(defaultHorseId);
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
	 *	设置成功没有
	 */
	public boolean getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	设置成功没有
	 */
	public void setResult(boolean result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	默认马的id
	 */
	public long getDefaultHorseId(){
		return defaultHorseId;
	}

	/**
	 * 设置属性：
	 *	默认马的id
	 */
	public void setDefaultHorseId(long defaultHorseId){
		this.defaultHorseId = defaultHorseId;
	}

}
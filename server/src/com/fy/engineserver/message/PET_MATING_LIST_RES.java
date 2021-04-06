package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.pet.PetMating;
import com.xuanzhi.tools.transport.ResponseMessage;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 宠物繁殖列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings.length</td><td>int</td><td>4个字节</td><td>PetMating数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings[0].petEntityAId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[0].petEntityBId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings[0].playerAId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[0].playerBId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings[0].startMating</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[0].finishTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings[0].expireTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[0].state</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings[0].childForAId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[0].childForBId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[1].petEntityAId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings[1].petEntityBId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[1].playerAId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings[1].playerBId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[1].startMating</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings[1].finishTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[1].expireTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings[1].state</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[1].childForAId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings[1].childForBId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings[2].petEntityAId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[2].petEntityBId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings[2].playerAId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[2].playerBId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings[2].startMating</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[2].finishTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings[2].expireTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[2].state</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petMatings[2].childForAId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petMatings[2].childForBId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class PET_MATING_LIST_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	PetMating[] petMatings;

	public PET_MATING_LIST_RES(long seqNum,PetMating[] petMatings){
		this.seqNum = seqNum;
		this.petMatings = petMatings;
	}

	public PET_MATING_LIST_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		petMatings = new PetMating[len];
		for(int i = 0 ; i < petMatings.length ; i++){
			petMatings[i] = new PetMating();
			petMatings[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			petMatings[i].setPetEntityAId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			petMatings[i].setPetEntityBId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			petMatings[i].setPlayerAId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			petMatings[i].setPlayerBId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			petMatings[i].setStartMating((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			petMatings[i].setFinishTime((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			petMatings[i].setExpireTime((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			petMatings[i].setState((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			petMatings[i].setChildForAId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			petMatings[i].setChildForBId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
		}
	}

	public int getType() {
		return 0x8000FF19;
	}

	public String getTypeDescription() {
		return "PET_MATING_LIST_RES";
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
		for(int i = 0 ; i < petMatings.length ; i++){
			len += 8;
			len += 8;
			len += 8;
			len += 8;
			len += 8;
			len += 8;
			len += 8;
			len += 8;
			len += 4;
			len += 8;
			len += 8;
		}
		packet_length = len;
		return len;
	}

	public int writeTo(ByteBuffer buffer) {
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.putInt(petMatings.length);
			for(int i = 0 ; i < petMatings.length ; i++){
				buffer.putLong(petMatings[i].getId());
				buffer.putLong(petMatings[i].getPetEntityAId());
				buffer.putLong(petMatings[i].getPetEntityBId());
				buffer.putLong(petMatings[i].getPlayerAId());
				buffer.putLong(petMatings[i].getPlayerBId());
				buffer.putLong(petMatings[i].getStartMating());
				buffer.putLong(petMatings[i].getFinishTime());
				buffer.putLong(petMatings[i].getExpireTime());
				buffer.putInt((int)petMatings[i].getState());
				buffer.putLong(petMatings[i].getChildForAId());
				buffer.putLong(petMatings[i].getChildForBId());
			}
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public PetMating[] getPetMatings(){
		return petMatings;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPetMatings(PetMating[] petMatings){
		this.petMatings = petMatings;
	}

}

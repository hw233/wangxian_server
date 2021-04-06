package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.core.MoveTrace4Client;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器通知客户端，精灵沿着按路径移动到目的地，包括到达的时间<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveTrace4Client.id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveTrace4Client.startTimestamp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveTrace4Client.pointsX.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveTrace4Client.pointsX</td><td>short[]</td><td>moveTrace4Client.pointsX.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveTrace4Client.speed</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveTrace4Client.destineTimestamp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveTrace4Client.roadLen.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveTrace4Client.roadLen</td><td>short[]</td><td>moveTrace4Client.roadLen.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveTrace4Client.type</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveTrace4Client.pointsY.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveTrace4Client.pointsY</td><td>short[]</td><td>moveTrace4Client.pointsY.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveTrace4Client.specialSkillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * </table>
 */
public class SPECIAL_SKILL_MOVETRACE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	MoveTrace4Client moveTrace4Client;

	public SPECIAL_SKILL_MOVETRACE_REQ(){
	}

	public SPECIAL_SKILL_MOVETRACE_REQ(long seqNum,MoveTrace4Client moveTrace4Client){
		this.seqNum = seqNum;
		this.moveTrace4Client = moveTrace4Client;
	}

	public SPECIAL_SKILL_MOVETRACE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		moveTrace4Client = new MoveTrace4Client();
		moveTrace4Client.setId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		moveTrace4Client.setStartTimestamp((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		short[] pointsX_0001 = new short[len];
		for(int j = 0 ; j < pointsX_0001.length ; j++){
			pointsX_0001[j] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		moveTrace4Client.setPointsX(pointsX_0001);
		moveTrace4Client.setSpeed((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		moveTrace4Client.setDestineTimestamp((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		short[] roadLen_0002 = new short[len];
		for(int j = 0 ; j < roadLen_0002.length ; j++){
			roadLen_0002[j] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		moveTrace4Client.setRoadLen(roadLen_0002);
		moveTrace4Client.setType((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		short[] pointsY_0003 = new short[len];
		for(int j = 0 ; j < pointsY_0003.length ; j++){
			pointsY_0003[j] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		moveTrace4Client.setPointsY(pointsY_0003);
		moveTrace4Client.setSpecialSkillType((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
	}

	public int getType() {
		return 0x000000CA;
	}

	public String getTypeDescription() {
		return "SPECIAL_SKILL_MOVETRACE_REQ";
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
		len += 4;
		len += moveTrace4Client.getPointsX().length * 2;
		len += 4;
		len += 8;
		len += 4;
		len += moveTrace4Client.getRoadLen().length * 2;
		len += 1;
		len += 4;
		len += moveTrace4Client.getPointsY().length * 2;
		len += 1;
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

			buffer.putLong(moveTrace4Client.getId());
			buffer.putLong(moveTrace4Client.getStartTimestamp());
			buffer.putInt(moveTrace4Client.getPointsX().length);
			short[] pointsX_0004 = moveTrace4Client.getPointsX();
			for(int j = 0 ; j < pointsX_0004.length ; j++){
				buffer.putShort(pointsX_0004[j]);
			}
			buffer.putInt((int)moveTrace4Client.getSpeed());
			buffer.putLong(moveTrace4Client.getDestineTimestamp());
			buffer.putInt(moveTrace4Client.getRoadLen().length);
			short[] roadLen_0005 = moveTrace4Client.getRoadLen();
			for(int j = 0 ; j < roadLen_0005.length ; j++){
				buffer.putShort(roadLen_0005[j]);
			}
			buffer.put((byte)moveTrace4Client.getType());
			buffer.putInt(moveTrace4Client.getPointsY().length);
			short[] pointsY_0006 = moveTrace4Client.getPointsY();
			for(int j = 0 ; j < pointsY_0006.length ; j++){
				buffer.putShort(pointsY_0006[j]);
			}
			buffer.put((byte)moveTrace4Client.getSpecialSkillType());
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
	 *	玩家的移动路径
	 */
	public MoveTrace4Client getMoveTrace4Client(){
		return moveTrace4Client;
	}

	/**
	 * 设置属性：
	 *	玩家的移动路径
	 */
	public void setMoveTrace4Client(MoveTrace4Client moveTrace4Client){
		this.moveTrace4Client = moveTrace4Client;
	}

}
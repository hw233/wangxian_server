package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * npc等的粒子效果，如暴风雪npc的暴风雪效果<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>particle.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>particle[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>particle[0]</td><td>String</td><td>particle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>particle[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>particle[1]</td><td>String</td><td>particle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>particle[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>particle[2]</td><td>String</td><td>particle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>attackRadius</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>startTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>endTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>speed</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>angle</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>height</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxParticleEachTime</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class PARTICLE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long id;
	byte playType;
	String[] particle;
	short attackRadius;
	long startTime;
	long endTime;
	short speed;
	short angle;
	short height;
	short maxParticleEachTime;

	public PARTICLE_REQ(){
	}

	public PARTICLE_REQ(long seqNum,long id,byte playType,String[] particle,short attackRadius,long startTime,long endTime,short speed,short angle,short height,short maxParticleEachTime){
		this.seqNum = seqNum;
		this.id = id;
		this.playType = playType;
		this.particle = particle;
		this.attackRadius = attackRadius;
		this.startTime = startTime;
		this.endTime = endTime;
		this.speed = speed;
		this.angle = angle;
		this.height = height;
		this.maxParticleEachTime = maxParticleEachTime;
	}

	public PARTICLE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		id = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		playType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		particle = new String[len];
		for(int i = 0 ; i < particle.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			particle[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		attackRadius = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		startTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		endTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		speed = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		angle = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		height = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		maxParticleEachTime = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
	}

	public int getType() {
		return 0x000000EE;
	}

	public String getTypeDescription() {
		return "PARTICLE_REQ";
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
		len += 1;
		len += 4;
		for(int i = 0 ; i < particle.length; i++){
			len += 2;
			try{
				len += particle[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		len += 8;
		len += 8;
		len += 2;
		len += 2;
		len += 2;
		len += 2;
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

			buffer.putLong(id);
			buffer.put(playType);
			buffer.putInt(particle.length);
			for(int i = 0 ; i < particle.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = particle[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putShort(attackRadius);
			buffer.putLong(startTime);
			buffer.putLong(endTime);
			buffer.putShort(speed);
			buffer.putShort(angle);
			buffer.putShort(height);
			buffer.putShort(maxParticleEachTime);
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
	 *	npc等的id
	 */
	public long getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	npc等的id
	 */
	public void setId(long id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	播放类型，0普通类型，1暴风雪类型
	 */
	public byte getPlayType(){
		return playType;
	}

	/**
	 * 设置属性：
	 *	播放类型，0普通类型，1暴风雪类型
	 */
	public void setPlayType(byte playType){
		this.playType = playType;
	}

	/**
	 * 获取属性：
	 *	粒子名
	 */
	public String[] getParticle(){
		return particle;
	}

	/**
	 * 设置属性：
	 *	粒子名
	 */
	public void setParticle(String[] particle){
		this.particle = particle;
	}

	/**
	 * 获取属性：
	 *	攻击半径
	 */
	public short getAttackRadius(){
		return attackRadius;
	}

	/**
	 * 设置属性：
	 *	攻击半径
	 */
	public void setAttackRadius(short attackRadius){
		this.attackRadius = attackRadius;
	}

	/**
	 * 获取属性：
	 *	粒子开始时间
	 */
	public long getStartTime(){
		return startTime;
	}

	/**
	 * 设置属性：
	 *	粒子开始时间
	 */
	public void setStartTime(long startTime){
		this.startTime = startTime;
	}

	/**
	 * 获取属性：
	 *	粒子结束时间
	 */
	public long getEndTime(){
		return endTime;
	}

	/**
	 * 设置属性：
	 *	粒子结束时间
	 */
	public void setEndTime(long endTime){
		this.endTime = endTime;
	}

	/**
	 * 获取属性：
	 *	粒子飞行速度
	 */
	public short getSpeed(){
		return speed;
	}

	/**
	 * 设置属性：
	 *	粒子飞行速度
	 */
	public void setSpeed(short speed){
		this.speed = speed;
	}

	/**
	 * 获取属性：
	 *	角度
	 */
	public short getAngle(){
		return angle;
	}

	/**
	 * 设置属性：
	 *	角度
	 */
	public void setAngle(short angle){
		this.angle = angle;
	}

	/**
	 * 获取属性：
	 *	粒子开始高度
	 */
	public short getHeight(){
		return height;
	}

	/**
	 * 设置属性：
	 *	粒子开始高度
	 */
	public void setHeight(short height){
		this.height = height;
	}

	/**
	 * 获取属性：
	 *	高峰时期落下粒子数量
	 */
	public short getMaxParticleEachTime(){
		return maxParticleEachTime;
	}

	/**
	 * 设置属性：
	 *	高峰时期落下粒子数量
	 */
	public void setMaxParticleEachTime(short maxParticleEachTime){
		this.maxParticleEachTime = maxParticleEachTime;
	}

}
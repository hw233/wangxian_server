package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询单个成员的贡献度与工资范围<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerid</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftSalary</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>baseSalary</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxSalary</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class JIAZU_QUERY_MEMBER_SALARY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long playerid;
	long leftSalary;
	long baseSalary;
	long maxSalary;

	public JIAZU_QUERY_MEMBER_SALARY_RES(){
	}

	public JIAZU_QUERY_MEMBER_SALARY_RES(long seqNum,long playerid,long leftSalary,long baseSalary,long maxSalary){
		this.seqNum = seqNum;
		this.playerid = playerid;
		this.leftSalary = leftSalary;
		this.baseSalary = baseSalary;
		this.maxSalary = maxSalary;
	}

	public JIAZU_QUERY_MEMBER_SALARY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerid = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		leftSalary = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		baseSalary = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		maxSalary = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x700AEE19;
	}

	public String getTypeDescription() {
		return "JIAZU_QUERY_MEMBER_SALARY_RES";
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
		len += 8;
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

			buffer.putLong(playerid);
			buffer.putLong(leftSalary);
			buffer.putLong(baseSalary);
			buffer.putLong(maxSalary);
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
	 *	玩家id
	 */
	public long getPlayerid(){
		return playerid;
	}

	/**
	 * 设置属性：
	 *	玩家id
	 */
	public void setPlayerid(long playerid){
		this.playerid = playerid;
	}

	/**
	 * 获取属性：
	 *	家族剩余可发放工资
	 */
	public long getLeftSalary(){
		return leftSalary;
	}

	/**
	 * 设置属性：
	 *	家族剩余可发放工资
	 */
	public void setLeftSalary(long leftSalary){
		this.leftSalary = leftSalary;
	}

	/**
	 * 获取属性：
	 *	基础工资额
	 */
	public long getBaseSalary(){
		return baseSalary;
	}

	/**
	 * 设置属性：
	 *	基础工资额
	 */
	public void setBaseSalary(long baseSalary){
		this.baseSalary = baseSalary;
	}

	/**
	 * 获取属性：
	 *	最多发送的工资额
	 */
	public long getMaxSalary(){
		return maxSalary;
	}

	/**
	 * 设置属性：
	 *	最多发送的工资额
	 */
	public void setMaxSalary(long maxSalary){
		this.maxSalary = maxSalary;
	}

}
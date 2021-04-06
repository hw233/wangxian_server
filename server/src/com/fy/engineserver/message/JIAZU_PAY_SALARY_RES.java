package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 发送工资<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>failreason.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>failreason</td><td>String</td><td>failreason.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerid</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>salary</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuLeftSalary</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class JIAZU_PAY_SALARY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte result;
	String failreason;
	long playerid;
	long salary;
	long jiazuLeftSalary;

	public JIAZU_PAY_SALARY_RES(){
	}

	public JIAZU_PAY_SALARY_RES(long seqNum,byte result,String failreason,long playerid,long salary,long jiazuLeftSalary){
		this.seqNum = seqNum;
		this.result = result;
		this.failreason = failreason;
		this.playerid = playerid;
		this.salary = salary;
		this.jiazuLeftSalary = jiazuLeftSalary;
	}

	public JIAZU_PAY_SALARY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		result = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		failreason = new String(content,offset,len,"UTF-8");
		offset += len;
		playerid = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		salary = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		jiazuLeftSalary = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x700AEE20;
	}

	public String getTypeDescription() {
		return "JIAZU_PAY_SALARY_RES";
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
		len += 2;
		try{
			len +=failreason.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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

			buffer.put(result);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = failreason.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(playerid);
			buffer.putLong(salary);
			buffer.putLong(jiazuLeftSalary);
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
	 *	结果,0-成功，1-失败
	 */
	public byte getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	结果,0-成功，1-失败
	 */
	public void setResult(byte result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	失败的结果描述
	 */
	public String getFailreason(){
		return failreason;
	}

	/**
	 * 设置属性：
	 *	失败的结果描述
	 */
	public void setFailreason(String failreason){
		this.failreason = failreason;
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
	 *	工资数
	 */
	public long getSalary(){
		return salary;
	}

	/**
	 * 设置属性：
	 *	工资数
	 */
	public void setSalary(long salary){
		this.salary = salary;
	}

	/**
	 * 获取属性：
	 *	家族剩余工资
	 */
	public long getJiazuLeftSalary(){
		return jiazuLeftSalary;
	}

	/**
	 * 设置属性：
	 *	家族剩余工资
	 */
	public void setJiazuLeftSalary(long jiazuLeftSalary){
		this.jiazuLeftSalary = jiazuLeftSalary;
	}

}
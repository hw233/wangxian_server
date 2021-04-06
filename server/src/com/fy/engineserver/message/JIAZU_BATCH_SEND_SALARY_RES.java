package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 批量发送工资<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerId</td><td>long[]</td><td>playerId.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>salary.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>salary</td><td>long[]</td><td>salary.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuLeftSalary</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>des</td><td>String</td><td>des.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class JIAZU_BATCH_SEND_SALARY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] playerId;
	long[] salary;
	long jiazuLeftSalary;
	String des;

	public JIAZU_BATCH_SEND_SALARY_RES(){
	}

	public JIAZU_BATCH_SEND_SALARY_RES(long seqNum,long[] playerId,long[] salary,long jiazuLeftSalary,String des){
		this.seqNum = seqNum;
		this.playerId = playerId;
		this.salary = salary;
		this.jiazuLeftSalary = jiazuLeftSalary;
		this.des = des;
	}

	public JIAZU_BATCH_SEND_SALARY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playerId = new long[len];
		for(int i = 0 ; i < playerId.length ; i++){
			playerId[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		salary = new long[len];
		for(int i = 0 ; i < salary.length ; i++){
			salary[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		jiazuLeftSalary = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		des = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x700AEE2A;
	}

	public String getTypeDescription() {
		return "JIAZU_BATCH_SEND_SALARY_RES";
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
		len += playerId.length * 8;
		len += 4;
		len += salary.length * 8;
		len += 8;
		len += 2;
		try{
			len +=des.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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

			buffer.putInt(playerId.length);
			for(int i = 0 ; i < playerId.length; i++){
				buffer.putLong(playerId[i]);
			}
			buffer.putInt(salary.length);
			for(int i = 0 ; i < salary.length; i++){
				buffer.putLong(salary[i]);
			}
			buffer.putLong(jiazuLeftSalary);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = des.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
	 *	发送成功的角色ID
	 */
	public long[] getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	发送成功的角色ID
	 */
	public void setPlayerId(long[] playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	角色获得的工资
	 */
	public long[] getSalary(){
		return salary;
	}

	/**
	 * 设置属性：
	 *	角色获得的工资
	 */
	public void setSalary(long[] salary){
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

	/**
	 * 获取属性：
	 *	结果
	 */
	public String getDes(){
		return des;
	}

	/**
	 * 设置属性：
	 *	结果
	 */
	public void setDes(String des){
		this.des = des;
	}

}
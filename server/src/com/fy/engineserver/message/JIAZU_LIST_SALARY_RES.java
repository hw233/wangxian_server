package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询工资<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>salarySum</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>salaryLeft</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hasPopedom</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>des</td><td>String</td><td>des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>batchDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>batchDes</td><td>String</td><td>batchDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerName[0]</td><td>String</td><td>playerName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerName[1]</td><td>String</td><td>playerName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerName[2]</td><td>String</td><td>playerName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId</td><td>long[]</td><td>playerId.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>salary.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>salary</td><td>long[]</td><td>salary.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>paidSalary.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>paidSalary</td><td>long[]</td><td>paidSalary.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastWeekSalary.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastWeekSalary</td><td>long[]</td><td>lastWeekSalary.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hasPaid.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hasPaid</td><td>boolean[]</td><td>hasPaid.length</td><td>*</td></tr>
 * </table>
 */
public class JIAZU_LIST_SALARY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long salarySum;
	long salaryLeft;
	boolean hasPopedom;
	String des;
	String batchDes;
	String[] playerName;
	long[] playerId;
	long[] salary;
	long[] paidSalary;
	long[] lastWeekSalary;
	boolean[] hasPaid;

	public JIAZU_LIST_SALARY_RES(){
	}

	public JIAZU_LIST_SALARY_RES(long seqNum,long salarySum,long salaryLeft,boolean hasPopedom,String des,String batchDes,String[] playerName,long[] playerId,long[] salary,long[] paidSalary,long[] lastWeekSalary,boolean[] hasPaid){
		this.seqNum = seqNum;
		this.salarySum = salarySum;
		this.salaryLeft = salaryLeft;
		this.hasPopedom = hasPopedom;
		this.des = des;
		this.batchDes = batchDes;
		this.playerName = playerName;
		this.playerId = playerId;
		this.salary = salary;
		this.paidSalary = paidSalary;
		this.lastWeekSalary = lastWeekSalary;
		this.hasPaid = hasPaid;
	}

	public JIAZU_LIST_SALARY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		salarySum = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		salaryLeft = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		hasPopedom = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		des = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		batchDes = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playerName = new String[len];
		for(int i = 0 ; i < playerName.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			playerName[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
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
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		paidSalary = new long[len];
		for(int i = 0 ; i < paidSalary.length ; i++){
			paidSalary[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		lastWeekSalary = new long[len];
		for(int i = 0 ; i < lastWeekSalary.length ; i++){
			lastWeekSalary[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		hasPaid = new boolean[len];
		for(int i = 0 ; i < hasPaid.length ; i++){
			hasPaid[i] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
	}

	public int getType() {
		return 0x700AEE17;
	}

	public String getTypeDescription() {
		return "JIAZU_LIST_SALARY_RES";
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
		len += 1;
		len += 2;
		try{
			len +=des.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=batchDes.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < playerName.length; i++){
			len += 2;
			try{
				len += playerName[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += playerId.length * 8;
		len += 4;
		len += salary.length * 8;
		len += 4;
		len += paidSalary.length * 8;
		len += 4;
		len += lastWeekSalary.length * 8;
		len += 4;
		len += hasPaid.length;
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

			buffer.putLong(salarySum);
			buffer.putLong(salaryLeft);
			buffer.put((byte)(hasPopedom==false?0:1));
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = des.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = batchDes.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(playerName.length);
			for(int i = 0 ; i < playerName.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = playerName[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(playerId.length);
			for(int i = 0 ; i < playerId.length; i++){
				buffer.putLong(playerId[i]);
			}
			buffer.putInt(salary.length);
			for(int i = 0 ; i < salary.length; i++){
				buffer.putLong(salary[i]);
			}
			buffer.putInt(paidSalary.length);
			for(int i = 0 ; i < paidSalary.length; i++){
				buffer.putLong(paidSalary[i]);
			}
			buffer.putInt(lastWeekSalary.length);
			for(int i = 0 ; i < lastWeekSalary.length; i++){
				buffer.putLong(lastWeekSalary[i]);
			}
			buffer.putInt(hasPaid.length);
			for(int i = 0 ; i < hasPaid.length; i++){
				buffer.put((byte)(hasPaid[i]==false?0:1));
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
	 *	本周工资的总量
	 */
	public long getSalarySum(){
		return salarySum;
	}

	/**
	 * 设置属性：
	 *	本周工资的总量
	 */
	public void setSalarySum(long salarySum){
		this.salarySum = salarySum;
	}

	/**
	 * 获取属性：
	 *	本周工资的剩余量
	 */
	public long getSalaryLeft(){
		return salaryLeft;
	}

	/**
	 * 设置属性：
	 *	本周工资的剩余量
	 */
	public void setSalaryLeft(long salaryLeft){
		this.salaryLeft = salaryLeft;
	}

	/**
	 * 获取属性：
	 *	是否有发放权限
	 */
	public boolean getHasPopedom(){
		return hasPopedom;
	}

	/**
	 * 设置属性：
	 *	是否有发放权限
	 */
	public void setHasPopedom(boolean hasPopedom){
		this.hasPopedom = hasPopedom;
	}

	/**
	 * 获取属性：
	 *	描述
	 */
	public String getDes(){
		return des;
	}

	/**
	 * 设置属性：
	 *	描述
	 */
	public void setDes(String des){
		this.des = des;
	}

	/**
	 * 获取属性：
	 *	批量发放描述
	 */
	public String getBatchDes(){
		return batchDes;
	}

	/**
	 * 设置属性：
	 *	批量发放描述
	 */
	public void setBatchDes(String batchDes){
		this.batchDes = batchDes;
	}

	/**
	 * 获取属性：
	 *	角色名
	 */
	public String[] getPlayerName(){
		return playerName;
	}

	/**
	 * 设置属性：
	 *	角色名
	 */
	public void setPlayerName(String[] playerName){
		this.playerName = playerName;
	}

	/**
	 * 获取属性：
	 *	成员ID
	 */
	public long[] getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	成员ID
	 */
	public void setPlayerId(long[] playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	工资
	 */
	public long[] getSalary(){
		return salary;
	}

	/**
	 * 设置属性：
	 *	工资
	 */
	public void setSalary(long[] salary){
		this.salary = salary;
	}

	/**
	 * 获取属性：
	 *	实发工资
	 */
	public long[] getPaidSalary(){
		return paidSalary;
	}

	/**
	 * 设置属性：
	 *	实发工资
	 */
	public void setPaidSalary(long[] paidSalary){
		this.paidSalary = paidSalary;
	}

	/**
	 * 获取属性：
	 *	上周工资
	 */
	public long[] getLastWeekSalary(){
		return lastWeekSalary;
	}

	/**
	 * 设置属性：
	 *	上周工资
	 */
	public void setLastWeekSalary(long[] lastWeekSalary){
		this.lastWeekSalary = lastWeekSalary;
	}

	/**
	 * 获取属性：
	 *	工资是否已发放
	 */
	public boolean[] getHasPaid(){
		return hasPaid;
	}

	/**
	 * 设置属性：
	 *	工资是否已发放
	 */
	public void setHasPaid(boolean[] hasPaid){
		this.hasPaid = hasPaid;
	}

}
package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询工资<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>salarySum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>salaryLeft</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName[0]</td><td>String</td><td>playerName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName[1]</td><td>String</td><td>playerName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName[2]</td><td>String</td><td>playerName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>contribution.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>contribution</td><td>int[]</td><td>contribution.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>salaryPaid.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>salaryPaid</td><td>int[]</td><td>salaryPaid.length</td><td>*</td></tr>
 * </table>
 */
public class JIAZU_QUERY_SALARY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int salarySum;
	int salaryLeft;
	String[] playerName;
	int[] contribution;
	int[] salaryPaid;

	public JIAZU_QUERY_SALARY_RES(long seqNum,int salarySum,int salaryLeft,String[] playerName,int[] contribution,int[] salaryPaid){
		this.seqNum = seqNum;
		this.salarySum = salarySum;
		this.salaryLeft = salaryLeft;
		this.playerName = playerName;
		this.contribution = contribution;
		this.salaryPaid = salaryPaid;
	}

	public JIAZU_QUERY_SALARY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		salarySum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		salaryLeft = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
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
		contribution = new int[len];
		for(int i = 0 ; i < contribution.length ; i++){
			contribution[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		salaryPaid = new int[len];
		for(int i = 0 ; i < salaryPaid.length ; i++){
			salaryPaid[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x800AEE17;
	}

	public String getTypeDescription() {
		return "JIAZU_QUERY_SALARY_RES";
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
		len += contribution.length * 4;
		len += 4;
		len += salaryPaid.length * 4;
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

			buffer.putInt(salarySum);
			buffer.putInt(salaryLeft);
			buffer.putInt(playerName.length);
			for(int i = 0 ; i < playerName.length; i++){
				byte[] tmpBytes2 = playerName[i].getBytes("UTF-8");
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(contribution.length);
			for(int i = 0 ; i < contribution.length; i++){
				buffer.putInt(contribution[i]);
			}
			buffer.putInt(salaryPaid.length);
			for(int i = 0 ; i < salaryPaid.length; i++){
				buffer.putInt(salaryPaid[i]);
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
	 *	本周工资的总量
	 */
	public int getSalarySum(){
		return salarySum;
	}

	/**
	 * 设置属性：
	 *	本周工资的总量
	 */
	public void setSalarySum(int salarySum){
		this.salarySum = salarySum;
	}

	/**
	 * 获取属性：
	 *	本周工资的剩余量
	 */
	public int getSalaryLeft(){
		return salaryLeft;
	}

	/**
	 * 设置属性：
	 *	本周工资的剩余量
	 */
	public void setSalaryLeft(int salaryLeft){
		this.salaryLeft = salaryLeft;
	}

	/**
	 * 获取属性：
	 *	失败的结果描述
	 */
	public String[] getPlayerName(){
		return playerName;
	}

	/**
	 * 设置属性：
	 *	失败的结果描述
	 */
	public void setPlayerName(String[] playerName){
		this.playerName = playerName;
	}

	/**
	 * 获取属性：
	 *	贡献工资
	 */
	public int[] getContribution(){
		return contribution;
	}

	/**
	 * 设置属性：
	 *	贡献工资
	 */
	public void setContribution(int[] contribution){
		this.contribution = contribution;
	}

	/**
	 * 获取属性：
	 *	已发工资
	 */
	public int[] getSalaryPaid(){
		return salaryPaid;
	}

	/**
	 * 设置属性：
	 *	已发工资
	 */
	public void setSalaryPaid(int[] salaryPaid){
		this.salaryPaid = salaryPaid;
	}

}

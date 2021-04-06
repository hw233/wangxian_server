package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查看境界信息 <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lvupBournExp</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bournDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bournDes</td><td>String</td><td>bournDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxBournTaskNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftZazenTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class BOURN_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long lvupBournExp;
	String bournDes;
	int maxBournTaskNum;
	long leftZazenTime;

	public BOURN_INFO_RES(){
	}

	public BOURN_INFO_RES(long seqNum,long lvupBournExp,String bournDes,int maxBournTaskNum,long leftZazenTime){
		this.seqNum = seqNum;
		this.lvupBournExp = lvupBournExp;
		this.bournDes = bournDes;
		this.maxBournTaskNum = maxBournTaskNum;
		this.leftZazenTime = leftZazenTime;
	}

	public BOURN_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		lvupBournExp = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		bournDes = new String(content,offset,len,"UTF-8");
		offset += len;
		maxBournTaskNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		leftZazenTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x8F100004;
	}

	public String getTypeDescription() {
		return "BOURN_INFO_RES";
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
		len += 2;
		try{
			len +=bournDes.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
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

			buffer.putLong(lvupBournExp);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = bournDes.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(maxBournTaskNum);
			buffer.putLong(leftZazenTime);
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
	 *	升级所需境界经验
	 */
	public long getLvupBournExp(){
		return lvupBournExp;
	}

	/**
	 * 设置属性：
	 *	升级所需境界经验
	 */
	public void setLvupBournExp(long lvupBournExp){
		this.lvupBournExp = lvupBournExp;
	}

	/**
	 * 获取属性：
	 *	当前境界的描述
	 */
	public String getBournDes(){
		return bournDes;
	}

	/**
	 * 设置属性：
	 *	当前境界的描述
	 */
	public void setBournDes(String bournDes){
		this.bournDes = bournDes;
	}

	/**
	 * 获取属性：
	 *	当前VIP等级可完成日常任务上限
	 */
	public int getMaxBournTaskNum(){
		return maxBournTaskNum;
	}

	/**
	 * 设置属性：
	 *	当前VIP等级可完成日常任务上限
	 */
	public void setMaxBournTaskNum(int maxBournTaskNum){
		this.maxBournTaskNum = maxBournTaskNum;
	}

	/**
	 * 获取属性：
	 *	剩余打坐时间
	 */
	public long getLeftZazenTime(){
		return leftZazenTime;
	}

	/**
	 * 设置属性：
	 *	剩余打坐时间
	 */
	public void setLeftZazenTime(long leftZazenTime){
		this.leftZazenTime = leftZazenTime;
	}

}
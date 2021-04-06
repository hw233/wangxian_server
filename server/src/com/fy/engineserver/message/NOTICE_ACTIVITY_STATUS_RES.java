package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>isBegin</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityIcon</td><td>String</td><td>activityIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityName</td><td>String</td><td>activityName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>numDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>numDes</td><td>String</td><td>numDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>remainTime</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class NOTICE_ACTIVITY_STATUS_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	boolean isBegin;
	String activityIcon;
	int activityType;
	String activityName;
	String numDes;
	int remainTime;

	public NOTICE_ACTIVITY_STATUS_RES(){
	}

	public NOTICE_ACTIVITY_STATUS_RES(long seqNum,boolean isBegin,String activityIcon,int activityType,String activityName,String numDes,int remainTime){
		this.seqNum = seqNum;
		this.isBegin = isBegin;
		this.activityIcon = activityIcon;
		this.activityType = activityType;
		this.activityName = activityName;
		this.numDes = numDes;
		this.remainTime = remainTime;
	}

	public NOTICE_ACTIVITY_STATUS_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		isBegin = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		activityIcon = new String(content,offset,len,"UTF-8");
		offset += len;
		activityType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		activityName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		numDes = new String(content,offset,len,"UTF-8");
		offset += len;
		remainTime = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70EEEE13;
	}

	public String getTypeDescription() {
		return "NOTICE_ACTIVITY_STATUS_RES";
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
			len +=activityIcon.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 2;
		try{
			len +=activityName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=numDes.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
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

			buffer.put((byte)(isBegin==false?0:1));
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = activityIcon.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(activityType);
				try{
			tmpBytes1 = activityName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = numDes.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(remainTime);
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
	 *	活动开始(true),开始显示图标，结束关闭图标
	 */
	public boolean getIsBegin(){
		return isBegin;
	}

	/**
	 * 设置属性：
	 *	活动开始(true),开始显示图标，结束关闭图标
	 */
	public void setIsBegin(boolean isBegin){
		this.isBegin = isBegin;
	}

	/**
	 * 获取属性：
	 *	活动图标
	 */
	public String getActivityIcon(){
		return activityIcon;
	}

	/**
	 * 设置属性：
	 *	活动图标
	 */
	public void setActivityIcon(String activityIcon){
		this.activityIcon = activityIcon;
	}

	/**
	 * 获取属性：
	 *	活动类型,1采花
	 */
	public int getActivityType(){
		return activityType;
	}

	/**
	 * 设置属性：
	 *	活动类型,1采花
	 */
	public void setActivityType(int activityType){
		this.activityType = activityType;
	}

	/**
	 * 获取属性：
	 *	活动类型
	 */
	public String getActivityName(){
		return activityName;
	}

	/**
	 * 设置属性：
	 *	活动类型
	 */
	public void setActivityName(String activityName){
		this.activityName = activityName;
	}

	/**
	 * 获取属性：
	 *	今天采几:最多采几
	 */
	public String getNumDes(){
		return numDes;
	}

	/**
	 * 设置属性：
	 *	今天采几:最多采几
	 */
	public void setNumDes(String numDes){
		this.numDes = numDes;
	}

	/**
	 * 获取属性：
	 *	剩余时间(秒)-1不显示
	 */
	public int getRemainTime(){
		return remainTime;
	}

	/**
	 * 设置属性：
	 *	剩余时间(秒)-1不显示
	 */
	public void setRemainTime(int remainTime){
		this.remainTime = remainTime;
	}

}
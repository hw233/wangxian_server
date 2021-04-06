package com.fy.boss.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.boss.gm.newfeedback.NewFeedback;
import com.fy.boss.message.BossMessageFactory;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>queueCountMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>queueCountMess</td><td>String</td><td>queueCountMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gmServiceMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gmServiceMess</td><td>String</td><td>gmServiceMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showNumLimit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>feedbacks.length</td><td>int</td><td>4个字节</td><td>NewFeedback数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>feedbacks[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>feedbacks[0].playerState</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>feedbacks[0].waitCount</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>feedbacks[0].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>feedbacks[0].title</td><td>String</td><td>feedbacks[0].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>feedbacks[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>feedbacks[1].playerState</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>feedbacks[1].waitCount</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>feedbacks[1].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>feedbacks[1].title</td><td>String</td><td>feedbacks[1].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>feedbacks[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>feedbacks[2].playerState</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>feedbacks[2].waitCount</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>feedbacks[2].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>feedbacks[2].title</td><td>String</td><td>feedbacks[2].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class FEEDBACK_HOME_PAGE_BOSS_RES implements ResponseMessage{

	static BossMessageFactory mf = BossMessageFactory.getInstance();

	long seqNum;
	String queueCountMess;
	String gmServiceMess;
	int showNumLimit;
	NewFeedback[] feedbacks;

	public FEEDBACK_HOME_PAGE_BOSS_RES(){
	}

	public FEEDBACK_HOME_PAGE_BOSS_RES(long seqNum,String queueCountMess,String gmServiceMess,int showNumLimit,NewFeedback[] feedbacks){
		this.seqNum = seqNum;
		this.queueCountMess = queueCountMess;
		this.gmServiceMess = gmServiceMess;
		this.showNumLimit = showNumLimit;
		this.feedbacks = feedbacks;
	}

	public FEEDBACK_HOME_PAGE_BOSS_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		queueCountMess = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		gmServiceMess = new String(content,offset,len,"UTF-8");
		offset += len;
		showNumLimit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096000) throw new Exception("object array length ["+len+"] big than the max length [4096000]");
		feedbacks = new NewFeedback[len];
		for(int i = 0 ; i < feedbacks.length ; i++){
			feedbacks[i] = new NewFeedback();
			feedbacks[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			feedbacks[i].setPlayerState((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			feedbacks[i].setWaitCount((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
			feedbacks[i].setTitle(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
	}

	public int getType() {
		return 0x8000F014;
	}

	public String getTypeDescription() {
		return "FEEDBACK_HOME_PAGE_BOSS_RES";
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
		len += 2;
		try{
			len +=queueCountMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=gmServiceMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		for(int i = 0 ; i < feedbacks.length ; i++){
			len += 8;
			len += 4;
			len += 4;
			len += 2;
			if(feedbacks[i].getTitle() != null){
				try{
				len += feedbacks[i].getTitle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = queueCountMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = gmServiceMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(showNumLimit);
			buffer.putInt(feedbacks.length);
			for(int i = 0 ; i < feedbacks.length ; i++){
				buffer.putLong(feedbacks[i].getId());
				buffer.putInt((int)feedbacks[i].getPlayerState());
				buffer.putInt((int)feedbacks[i].getWaitCount());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = feedbacks[i].getTitle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
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
	 *	当前排队数量信息
	 */
	public String getQueueCountMess(){
		return queueCountMess;
	}

	/**
	 * 设置属性：
	 *	当前排队数量信息
	 */
	public void setQueueCountMess(String queueCountMess){
		this.queueCountMess = queueCountMess;
	}

	/**
	 * 获取属性：
	 *	客服帮助提示信息
	 */
	public String getGmServiceMess(){
		return gmServiceMess;
	}

	/**
	 * 设置属性：
	 *	客服帮助提示信息
	 */
	public void setGmServiceMess(String gmServiceMess){
		this.gmServiceMess = gmServiceMess;
	}

	/**
	 * 获取属性：
	 *	最大显示数量
	 */
	public int getShowNumLimit(){
		return showNumLimit;
	}

	/**
	 * 设置属性：
	 *	最大显示数量
	 */
	public void setShowNumLimit(int showNumLimit){
		this.showNumLimit = showNumLimit;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public NewFeedback[] getFeedbacks(){
		return feedbacks;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setFeedbacks(NewFeedback[] feedbacks){
		this.feedbacks = feedbacks;
	}

}
package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 活动首界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>reqType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>noticeStat.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>noticeStat</td><td>int[]</td><td>noticeStat.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityNames[0]</td><td>String</td><td>activityNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityNames[1]</td><td>String</td><td>activityNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityNames[2]</td><td>String</td><td>activityNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityColors.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityColors</td><td>int[]</td><td>activityColors.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityStst.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityStst</td><td>int[]</td><td>activityStst.length</td><td>*</td></tr>
 * </table>
 */
public class ACTIVITY_FIRST_PAGE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int reqType;
	int[] noticeStat;
	String[] activityNames;
	int[] activityColors;
	int[] activityStst;

	public ACTIVITY_FIRST_PAGE_RES(){
	}

	public ACTIVITY_FIRST_PAGE_RES(long seqNum,int reqType,int[] noticeStat,String[] activityNames,int[] activityColors,int[] activityStst){
		this.seqNum = seqNum;
		this.reqType = reqType;
		this.noticeStat = noticeStat;
		this.activityNames = activityNames;
		this.activityColors = activityColors;
		this.activityStst = activityStst;
	}

	public ACTIVITY_FIRST_PAGE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		reqType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		noticeStat = new int[len];
		for(int i = 0 ; i < noticeStat.length ; i++){
			noticeStat[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		activityNames = new String[len];
		for(int i = 0 ; i < activityNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activityNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		activityColors = new int[len];
		for(int i = 0 ; i < activityColors.length ; i++){
			activityColors[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		activityStst = new int[len];
		for(int i = 0 ; i < activityStst.length ; i++){
			activityStst[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70F0EF68;
	}

	public String getTypeDescription() {
		return "ACTIVITY_FIRST_PAGE_RES";
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
		len += noticeStat.length * 4;
		len += 4;
		for(int i = 0 ; i < activityNames.length; i++){
			len += 2;
			try{
				len += activityNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += activityColors.length * 4;
		len += 4;
		len += activityStst.length * 4;
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

			buffer.putInt(reqType);
			buffer.putInt(noticeStat.length);
			for(int i = 0 ; i < noticeStat.length; i++){
				buffer.putInt(noticeStat[i]);
			}
			buffer.putInt(activityNames.length);
			for(int i = 0 ; i < activityNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = activityNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(activityColors.length);
			for(int i = 0 ; i < activityColors.length; i++){
				buffer.putInt(activityColors[i]);
			}
			buffer.putInt(activityStst.length);
			for(int i = 0 ; i < activityStst.length; i++){
				buffer.putInt(activityStst[i]);
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
	 *	1:日常,2:活动
	 */
	public int getReqType(){
		return reqType;
	}

	/**
	 * 设置属性：
	 *	1:日常,2:活动
	 */
	public void setReqType(int reqType){
		this.reqType = reqType;
	}

	/**
	 * 获取属性：
	 *	按钮状态
	 */
	public int[] getNoticeStat(){
		return noticeStat;
	}

	/**
	 * 设置属性：
	 *	按钮状态
	 */
	public void setNoticeStat(int[] noticeStat){
		this.noticeStat = noticeStat;
	}

	/**
	 * 获取属性：
	 *	活动名字
	 */
	public String[] getActivityNames(){
		return activityNames;
	}

	/**
	 * 设置属性：
	 *	活动名字
	 */
	public void setActivityNames(String[] activityNames){
		this.activityNames = activityNames;
	}

	/**
	 * 获取属性：
	 *	活动颜色
	 */
	public int[] getActivityColors(){
		return activityColors;
	}

	/**
	 * 设置属性：
	 *	活动颜色
	 */
	public void setActivityColors(int[] activityColors){
		this.activityColors = activityColors;
	}

	/**
	 * 获取属性：
	 *	活动列表状态
	 */
	public int[] getActivityStst(){
		return activityStst;
	}

	/**
	 * 设置属性：
	 *	活动列表状态
	 */
	public void setActivityStst(int[] activityStst){
		this.activityStst = activityStst;
	}

}
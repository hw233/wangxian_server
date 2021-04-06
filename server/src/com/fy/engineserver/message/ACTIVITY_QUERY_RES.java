package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 活动请求<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityNames[0]</td><td>String</td><td>activityNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityNames[1]</td><td>String</td><td>activityNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityNames[2]</td><td>String</td><td>activityNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityids.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityids</td><td>int[]</td><td>activityids.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityContent.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityContent</td><td>String</td><td>activityContent.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>havaBindSivler</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bindSivlerNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class ACTIVITY_QUERY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int activityId;
	String[] activityNames;
	int[] activityids;
	String activityContent;
	boolean havaBindSivler;
	int bindSivlerNum;

	public ACTIVITY_QUERY_RES(){
	}

	public ACTIVITY_QUERY_RES(long seqNum,int activityId,String[] activityNames,int[] activityids,String activityContent,boolean havaBindSivler,int bindSivlerNum){
		this.seqNum = seqNum;
		this.activityId = activityId;
		this.activityNames = activityNames;
		this.activityids = activityids;
		this.activityContent = activityContent;
		this.havaBindSivler = havaBindSivler;
		this.bindSivlerNum = bindSivlerNum;
	}

	public ACTIVITY_QUERY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		activityId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
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
		activityids = new int[len];
		for(int i = 0 ; i < activityids.length ; i++){
			activityids[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		activityContent = new String(content,offset,len,"UTF-8");
		offset += len;
		havaBindSivler = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		bindSivlerNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70FFF139;
	}

	public String getTypeDescription() {
		return "ACTIVITY_QUERY_RES";
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
		len += activityids.length * 4;
		len += 2;
		try{
			len +=activityContent.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
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

			buffer.putInt(activityId);
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
			buffer.putInt(activityids.length);
			for(int i = 0 ; i < activityids.length; i++){
				buffer.putInt(activityids[i]);
			}
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = activityContent.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put((byte)(havaBindSivler==false?0:1));
			buffer.putInt(bindSivlerNum);
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
	 *	活动id
	 */
	public int getActivityId(){
		return activityId;
	}

	/**
	 * 设置属性：
	 *	活动id
	 */
	public void setActivityId(int activityId){
		this.activityId = activityId;
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
	 *	活动ids
	 */
	public int[] getActivityids(){
		return activityids;
	}

	/**
	 * 设置属性：
	 *	活动ids
	 */
	public void setActivityids(int[] activityids){
		this.activityids = activityids;
	}

	/**
	 * 获取属性：
	 *	内容
	 */
	public String getActivityContent(){
		return activityContent;
	}

	/**
	 * 设置属性：
	 *	内容
	 */
	public void setActivityContent(String activityContent){
		this.activityContent = activityContent;
	}

	/**
	 * 获取属性：
	 *	havaBindSivler
	 */
	public boolean getHavaBindSivler(){
		return havaBindSivler;
	}

	/**
	 * 设置属性：
	 *	havaBindSivler
	 */
	public void setHavaBindSivler(boolean havaBindSivler){
		this.havaBindSivler = havaBindSivler;
	}

	/**
	 * 获取属性：
	 *	绑银数量
	 */
	public int getBindSivlerNum(){
		return bindSivlerNum;
	}

	/**
	 * 设置属性：
	 *	绑银数量
	 */
	public void setBindSivlerNum(int bindSivlerNum){
		this.bindSivlerNum = bindSivlerNum;
	}

}
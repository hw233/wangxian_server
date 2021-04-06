package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 杂念<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>taskId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>des</td><td>String</td><td>des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>star</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>refreshExpCost</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>refreshCoinCost</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>starDes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>starDes[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>starDes[0]</td><td>String</td><td>starDes[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>starDes[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>starDes[1]</td><td>String</td><td>starDes[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>starDes[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>starDes[2]</td><td>String</td><td>starDes[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class BOURN_OF_PURIFY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long taskId;
	String des;
	int star;
	long refreshExpCost;
	long refreshCoinCost;
	String[] starDes;

	public BOURN_OF_PURIFY_RES(){
	}

	public BOURN_OF_PURIFY_RES(long seqNum,long taskId,String des,int star,long refreshExpCost,long refreshCoinCost,String[] starDes){
		this.seqNum = seqNum;
		this.taskId = taskId;
		this.des = des;
		this.star = star;
		this.refreshExpCost = refreshExpCost;
		this.refreshCoinCost = refreshCoinCost;
		this.starDes = starDes;
	}

	public BOURN_OF_PURIFY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		taskId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		des = new String(content,offset,len,"UTF-8");
		offset += len;
		star = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		refreshExpCost = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		refreshCoinCost = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		starDes = new String[len];
		for(int i = 0 ; i < starDes.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			starDes[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x8F100006;
	}

	public String getTypeDescription() {
		return "BOURN_OF_PURIFY_RES";
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
			len +=des.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 8;
		len += 8;
		len += 4;
		for(int i = 0 ; i < starDes.length; i++){
			len += 2;
			try{
				len += starDes[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
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

			buffer.putLong(taskId);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = des.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(star);
			buffer.putLong(refreshExpCost);
			buffer.putLong(refreshCoinCost);
			buffer.putInt(starDes.length);
			for(int i = 0 ; i < starDes.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = starDes[i].getBytes("UTF-8");
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
	 *	当前任务的ID
	 */
	public long getTaskId(){
		return taskId;
	}

	/**
	 * 设置属性：
	 *	当前任务的ID
	 */
	public void setTaskId(long taskId){
		this.taskId = taskId;
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
	 *	当前任务的星级
	 */
	public int getStar(){
		return star;
	}

	/**
	 * 设置属性：
	 *	当前任务的星级
	 */
	public void setStar(int star){
		this.star = star;
	}

	/**
	 * 获取属性：
	 *	刷新任务经验消耗
	 */
	public long getRefreshExpCost(){
		return refreshExpCost;
	}

	/**
	 * 设置属性：
	 *	刷新任务经验消耗
	 */
	public void setRefreshExpCost(long refreshExpCost){
		this.refreshExpCost = refreshExpCost;
	}

	/**
	 * 获取属性：
	 *	刷新任务元宝消耗
	 */
	public long getRefreshCoinCost(){
		return refreshCoinCost;
	}

	/**
	 * 设置属性：
	 *	刷新任务元宝消耗
	 */
	public void setRefreshCoinCost(long refreshCoinCost){
		this.refreshCoinCost = refreshCoinCost;
	}

	/**
	 * 获取属性：
	 *	各个星级任务的描述
	 */
	public String[] getStarDes(){
		return starDes;
	}

	/**
	 * 设置属性：
	 *	各个星级任务的描述
	 */
	public void setStarDes(String[] starDes){
		this.starDes = starDes;
	}

}
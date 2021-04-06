package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求排行榜<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>titles.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>titles[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>titles[0]</td><td>String</td><td>titles[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>titles[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>titles[1]</td><td>String</td><td>titles[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>titles[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>titles[2]</td><td>String</td><td>titles[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rankObject.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rankObject[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rankObject[0]</td><td>String</td><td>rankObject[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rankObject[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rankObject[1]</td><td>String</td><td>rankObject[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rankObject[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rankObject[2]</td><td>String</td><td>rankObject[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>value.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>value</td><td>long[]</td><td>value.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description[0]</td><td>String</td><td>description[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description[1]</td><td>String</td><td>description[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description[2]</td><td>String</td><td>description[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ids.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ids</td><td>long[]</td><td>ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>additionalInfo.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>additionalInfo</td><td>int[]</td><td>additionalInfo.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>selfRank</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class GET_BILLBOARD_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String[] titles;
	String[] rankObject;
	long[] value;
	String[] description;
	long[] ids;
	int[] additionalInfo;
	int selfRank;

	public GET_BILLBOARD_RES(){
	}

	public GET_BILLBOARD_RES(long seqNum,String[] titles,String[] rankObject,long[] value,String[] description,long[] ids,int[] additionalInfo,int selfRank){
		this.seqNum = seqNum;
		this.titles = titles;
		this.rankObject = rankObject;
		this.value = value;
		this.description = description;
		this.ids = ids;
		this.additionalInfo = additionalInfo;
		this.selfRank = selfRank;
	}

	public GET_BILLBOARD_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		titles = new String[len];
		for(int i = 0 ; i < titles.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			titles[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		rankObject = new String[len];
		for(int i = 0 ; i < rankObject.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			rankObject[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		value = new long[len];
		for(int i = 0 ; i < value.length ; i++){
			value[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		description = new String[len];
		for(int i = 0 ; i < description.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			description[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ids = new long[len];
		for(int i = 0 ; i < ids.length ; i++){
			ids[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		additionalInfo = new int[len];
		for(int i = 0 ; i < additionalInfo.length ; i++){
			additionalInfo[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		selfRank = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x7000EF11;
	}

	public String getTypeDescription() {
		return "GET_BILLBOARD_RES";
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
		for(int i = 0 ; i < titles.length; i++){
			len += 2;
			try{
				len += titles[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < rankObject.length; i++){
			len += 2;
			try{
				len += rankObject[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += value.length * 8;
		len += 4;
		for(int i = 0 ; i < description.length; i++){
			len += 2;
			try{
				len += description[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += ids.length * 8;
		len += 4;
		len += additionalInfo.length * 4;
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

			buffer.putInt(titles.length);
			for(int i = 0 ; i < titles.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = titles[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(rankObject.length);
			for(int i = 0 ; i < rankObject.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = rankObject[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(value.length);
			for(int i = 0 ; i < value.length; i++){
				buffer.putLong(value[i]);
			}
			buffer.putInt(description.length);
			for(int i = 0 ; i < description.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = description[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(ids.length);
			for(int i = 0 ; i < ids.length; i++){
				buffer.putLong(ids[i]);
			}
			buffer.putInt(additionalInfo.length);
			for(int i = 0 ; i < additionalInfo.length; i++){
				buffer.putInt(additionalInfo[i]);
			}
			buffer.putInt(selfRank);
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
	 *	表头
	 */
	public String[] getTitles(){
		return titles;
	}

	/**
	 * 设置属性：
	 *	表头
	 */
	public void setTitles(String[] titles){
		this.titles = titles;
	}

	/**
	 * 获取属性：
	 *	排行内容
	 */
	public String[] getRankObject(){
		return rankObject;
	}

	/**
	 * 设置属性：
	 *	排行内容
	 */
	public void setRankObject(String[] rankObject){
		this.rankObject = rankObject;
	}

	/**
	 * 获取属性：
	 *	数据
	 */
	public long[] getValue(){
		return value;
	}

	/**
	 * 设置属性：
	 *	数据
	 */
	public void setValue(long[] value){
		this.value = value;
	}

	/**
	 * 获取属性：
	 *	描述
	 */
	public String[] getDescription(){
		return description;
	}

	/**
	 * 设置属性：
	 *	描述
	 */
	public void setDescription(String[] description){
		this.description = description;
	}

	/**
	 * 获取属性：
	 *	ID
	 */
	public long[] getIds(){
		return ids;
	}

	/**
	 * 设置属性：
	 *	ID
	 */
	public void setIds(long[] ids){
		this.ids = ids;
	}

	/**
	 * 获取属性：
	 *	附加信息
	 */
	public int[] getAdditionalInfo(){
		return additionalInfo;
	}

	/**
	 * 设置属性：
	 *	附加信息
	 */
	public void setAdditionalInfo(int[] additionalInfo){
		this.additionalInfo = additionalInfo;
	}

	/**
	 * 获取属性：
	 *	玩家自己的排名
	 */
	public int getSelfRank(){
		return selfRank;
	}

	/**
	 * 设置属性：
	 *	玩家自己的排名
	 */
	public void setSelfRank(int selfRank){
		this.selfRank = selfRank;
	}

}
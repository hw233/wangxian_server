package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.newBillboard.BillboardDate;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 得到指定排行榜数据<br>
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
 * <tr bgcolor="#FAFAFA" align="center"><td>titleNum.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>titleNum</td><td>int[]</td><td>titleNum.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardDates.length</td><td>int</td><td>4个字节</td><td>BillboardDate数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardDates[0].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardDates[0].dateId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardDates[0].dateValues.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardDates[0].dateValues[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardDates[0].dateValues[0]</td><td>String</td><td>billboardDates[0].dateValues[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardDates[0].dateValues[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardDates[0].dateValues[1]</td><td>String</td><td>billboardDates[0].dateValues[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardDates[0].dateValues[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardDates[0].dateValues[2]</td><td>String</td><td>billboardDates[0].dateValues[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardDates[1].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardDates[1].dateId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardDates[1].dateValues.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardDates[1].dateValues[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardDates[1].dateValues[0]</td><td>String</td><td>billboardDates[1].dateValues[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardDates[1].dateValues[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardDates[1].dateValues[1]</td><td>String</td><td>billboardDates[1].dateValues[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardDates[1].dateValues[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardDates[1].dateValues[2]</td><td>String</td><td>billboardDates[1].dateValues[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardDates[2].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardDates[2].dateId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardDates[2].dateValues.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardDates[2].dateValues[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardDates[2].dateValues[0]</td><td>String</td><td>billboardDates[2].dateValues[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardDates[2].dateValues[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardDates[2].dateValues[1]</td><td>String</td><td>billboardDates[2].dateValues[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardDates[2].dateValues[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardDates[2].dateValues[2]</td><td>String</td><td>billboardDates[2].dateValues[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description</td><td>String</td><td>description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>selfRank.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>selfRank</td><td>String</td><td>selfRank.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class GET_ONE_BILLBOARD_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String[] titles;
	int[] titleNum;
	BillboardDate[] billboardDates;
	String description;
	String selfRank;

	public GET_ONE_BILLBOARD_RES(){
	}

	public GET_ONE_BILLBOARD_RES(long seqNum,String[] titles,int[] titleNum,BillboardDate[] billboardDates,String description,String selfRank){
		this.seqNum = seqNum;
		this.titles = titles;
		this.titleNum = titleNum;
		this.billboardDates = billboardDates;
		this.description = description;
		this.selfRank = selfRank;
	}

	public GET_ONE_BILLBOARD_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
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
		titleNum = new int[len];
		for(int i = 0 ; i < titleNum.length ; i++){
			titleNum[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		billboardDates = new BillboardDate[len];
		for(int i = 0 ; i < billboardDates.length ; i++){
			billboardDates[i] = new BillboardDate();
			billboardDates[i].setType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			billboardDates[i].setDateId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] dateValues_0001 = new String[len];
			for(int j = 0 ; j < dateValues_0001.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				dateValues_0001[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			billboardDates[i].setDateValues(dateValues_0001);
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		description = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		selfRank = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x0F70002A;
	}

	public String getTypeDescription() {
		return "GET_ONE_BILLBOARD_RES";
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
		len += titleNum.length * 4;
		len += 4;
		for(int i = 0 ; i < billboardDates.length ; i++){
			len += 4;
			len += 8;
			len += 4;
			String[] dateValues = billboardDates[i].getDateValues();
			for(int j = 0 ; j < dateValues.length; j++){
				len += 2;
				try{
					len += dateValues[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 2;
		try{
			len +=description.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=selfRank.getBytes("UTF-8").length;
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
			buffer.putInt(titleNum.length);
			for(int i = 0 ; i < titleNum.length; i++){
				buffer.putInt(titleNum[i]);
			}
			buffer.putInt(billboardDates.length);
			for(int i = 0 ; i < billboardDates.length ; i++){
				buffer.putInt((int)billboardDates[i].getType());
				buffer.putLong(billboardDates[i].getDateId());
				buffer.putInt(billboardDates[i].getDateValues().length);
				String[] dateValues_0002 = billboardDates[i].getDateValues();
				for(int j = 0 ; j < dateValues_0002.length ; j++){
				try{
					buffer.putShort((short)dateValues_0002[j].getBytes("UTF-8").length);
					buffer.put(dateValues_0002[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
			}
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = description.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = selfRank.getBytes("UTF-8");
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
	 *	表头每个最大长度
	 */
	public int[] getTitleNum(){
		return titleNum;
	}

	/**
	 * 设置属性：
	 *	表头每个最大长度
	 */
	public void setTitleNum(int[] titleNum){
		this.titleNum = titleNum;
	}

	/**
	 * 获取属性：
	 *	榜单数据
	 */
	public BillboardDate[] getBillboardDates(){
		return billboardDates;
	}

	/**
	 * 设置属性：
	 *	榜单数据
	 */
	public void setBillboardDates(BillboardDate[] billboardDates){
		this.billboardDates = billboardDates;
	}

	/**
	 * 获取属性：
	 *	描述
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * 设置属性：
	 *	描述
	 */
	public void setDescription(String description){
		this.description = description;
	}

	/**
	 * 获取属性：
	 *	自己排名
	 */
	public String getSelfRank(){
		return selfRank;
	}

	/**
	 * 设置属性：
	 *	自己排名
	 */
	public void setSelfRank(String selfRank){
		this.selfRank = selfRank;
	}

}
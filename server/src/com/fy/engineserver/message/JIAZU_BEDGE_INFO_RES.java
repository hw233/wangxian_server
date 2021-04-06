package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.fy.engineserver.septbuilding.entity.JiazuBedge;
import com.xuanzhi.tools.transport.ResponseMessage;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询家族徽章信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges.length</td><td>int</td><td>4个字节</td><td>JiazuBedge数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges[0].color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[0].price</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges[0].levelLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[0].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[0].name</td><td>String</td><td>jiazuBedges[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges[0].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[0].des</td><td>String</td><td>jiazuBedges[0].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges[0].resName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[0].resName</td><td>String</td><td>jiazuBedges[0].resName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[1].color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges[1].price</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[1].levelLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges[1].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges[1].name</td><td>String</td><td>jiazuBedges[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[1].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges[1].des</td><td>String</td><td>jiazuBedges[1].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[1].resName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges[1].resName</td><td>String</td><td>jiazuBedges[1].resName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges[2].color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[2].price</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges[2].levelLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[2].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[2].name</td><td>String</td><td>jiazuBedges[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges[2].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[2].des</td><td>String</td><td>jiazuBedges[2].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuBedges[2].resName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuBedges[2].resName</td><td>String</td><td>jiazuBedges[2].resName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hasDedgIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hasDedgIds</td><td>int[]</td><td>hasDedgIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>switchCost</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class JIAZU_BEDGE_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	JiazuBedge[] jiazuBedges;
	int[] hasDedgIds;
	int switchCost;

	public JIAZU_BEDGE_INFO_RES(long seqNum,JiazuBedge[] jiazuBedges,int[] hasDedgIds,int switchCost){
		this.seqNum = seqNum;
		this.jiazuBedges = jiazuBedges;
		this.hasDedgIds = hasDedgIds;
		this.switchCost = switchCost;
	}

	public JIAZU_BEDGE_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		jiazuBedges = new JiazuBedge[len];
		for(int i = 0 ; i < jiazuBedges.length ; i++){
			jiazuBedges[i] = new JiazuBedge();
			jiazuBedges[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			jiazuBedges[i].setColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			jiazuBedges[i].setPrice((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			jiazuBedges[i].setLevelLimit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			jiazuBedges[i].setType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			jiazuBedges[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			jiazuBedges[i].setDes(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			jiazuBedges[i].setResName(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		hasDedgIds = new int[len];
		for(int i = 0 ; i < hasDedgIds.length ; i++){
			hasDedgIds[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		switchCost = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x800AEE22;
	}

	public String getTypeDescription() {
		return "JIAZU_BEDGE_INFO_RES";
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
		for(int i = 0 ; i < jiazuBedges.length ; i++){
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(jiazuBedges[i].getName() != null){
				try{
				len += jiazuBedges[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(jiazuBedges[i].getDes() != null){
				try{
				len += jiazuBedges[i].getDes().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(jiazuBedges[i].getResName() != null){
				try{
				len += jiazuBedges[i].getResName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 4;
		len += hasDedgIds.length * 4;
		len += 4;
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

			buffer.putInt(jiazuBedges.length);
			for(int i = 0 ; i < jiazuBedges.length ; i++){
				buffer.putInt((int)jiazuBedges[i].getId());
				buffer.putInt((int)jiazuBedges[i].getColor());
				buffer.putInt((int)jiazuBedges[i].getPrice());
				buffer.putInt((int)jiazuBedges[i].getLevelLimit());
				buffer.putInt((int)jiazuBedges[i].getType());
				byte[] tmpBytes2 = jiazuBedges[i].getName().getBytes("UTF-8");
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				tmpBytes2 = jiazuBedges[i].getDes().getBytes("UTF-8");
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				tmpBytes2 = jiazuBedges[i].getResName().getBytes("UTF-8");
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(hasDedgIds.length);
			for(int i = 0 ; i < hasDedgIds.length; i++){
				buffer.putInt(hasDedgIds[i]);
			}
			buffer.putInt(switchCost);
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	系统提供的所有的徽章
	 */
	public JiazuBedge[] getJiazuBedges(){
		return jiazuBedges;
	}

	/**
	 * 设置属性：
	 *	系统提供的所有的徽章
	 */
	public void setJiazuBedges(JiazuBedge[] jiazuBedges){
		this.jiazuBedges = jiazuBedges;
	}

	/**
	 * 获取属性：
	 *	已经获得的徽章列表
	 */
	public int[] getHasDedgIds(){
		return hasDedgIds;
	}

	/**
	 * 设置属性：
	 *	已经获得的徽章列表
	 */
	public void setHasDedgIds(int[] hasDedgIds){
		this.hasDedgIds = hasDedgIds;
	}

	/**
	 * 获取属性：
	 *	切换徽章消耗
	 */
	public int getSwitchCost(){
		return switchCost;
	}

	/**
	 * 设置属性：
	 *	切换徽章消耗
	 */
	public void setSwitchCost(int switchCost){
		this.switchCost = switchCost;
	}

}

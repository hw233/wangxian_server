package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.datasource.article.entity.client.BagInfo4Client;
import com.fy.engineserver.datasource.article.data.props.PropsCategory;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询玩家的背包，整理背包也使用这个指令<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bagInfo4Client.length</td><td>int</td><td>4个字节</td><td>BagInfo4Client数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bagInfo4Client[0].bagtype</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bagInfo4Client[0].entityId.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bagInfo4Client[0].entityId</td><td>long[]</td><td>bagInfo4Client[0].entityId.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bagInfo4Client[0].counts.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bagInfo4Client[0].counts</td><td>short[]</td><td>bagInfo4Client[0].counts.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bagInfo4Client[1].bagtype</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bagInfo4Client[1].entityId.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bagInfo4Client[1].entityId</td><td>long[]</td><td>bagInfo4Client[1].entityId.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bagInfo4Client[1].counts.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bagInfo4Client[1].counts</td><td>short[]</td><td>bagInfo4Client[1].counts.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bagInfo4Client[2].bagtype</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bagInfo4Client[2].entityId.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bagInfo4Client[2].entityId</td><td>long[]</td><td>bagInfo4Client[2].entityId.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bagInfo4Client[2].counts.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bagInfo4Client[2].counts</td><td>short[]</td><td>bagInfo4Client[2].counts.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>categories.length</td><td>int</td><td>4个字节</td><td>PropsCategory数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>categories[0].cooldownLimit</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>categories[0].stalemateType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>categories[0].categoryName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>categories[0].categoryName</td><td>String</td><td>categories[0].categoryName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>categories[0].stalemateTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>categories[1].cooldownLimit</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>categories[1].stalemateType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>categories[1].categoryName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>categories[1].categoryName</td><td>String</td><td>categories[1].categoryName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>categories[1].stalemateTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>categories[2].cooldownLimit</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>categories[2].stalemateType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>categories[2].categoryName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>categories[2].categoryName</td><td>String</td><td>categories[2].categoryName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>categories[2].stalemateTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_KNAPSACK_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	BagInfo4Client[] bagInfo4Client;
	PropsCategory[] categories;

	public QUERY_KNAPSACK_RES(){
	}

	public QUERY_KNAPSACK_RES(long seqNum,BagInfo4Client[] bagInfo4Client,PropsCategory[] categories){
		this.seqNum = seqNum;
		this.bagInfo4Client = bagInfo4Client;
		this.categories = categories;
	}

	public QUERY_KNAPSACK_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		bagInfo4Client = new BagInfo4Client[len];
		for(int i = 0 ; i < bagInfo4Client.length ; i++){
			bagInfo4Client[i] = new BagInfo4Client();
			bagInfo4Client[i].setBagtype((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] entityId_0001 = new long[len];
			for(int j = 0 ; j < entityId_0001.length ; j++){
				entityId_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			bagInfo4Client[i].setEntityId(entityId_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] counts_0002 = new short[len];
			for(int j = 0 ; j < counts_0002.length ; j++){
				counts_0002[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			bagInfo4Client[i].setCounts(counts_0002);
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		categories = new PropsCategory[len];
		for(int i = 0 ; i < categories.length ; i++){
			categories[i] = new PropsCategory();
			categories[i].setCooldownLimit((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			categories[i].setStalemateType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			categories[i].setCategoryName(new String(content,offset,len,"UTF-8"));
			offset += len;
			categories[i].setStalemateTime((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
		}
	}

	public int getType() {
		return 0x700000F5;
	}

	public String getTypeDescription() {
		return "QUERY_KNAPSACK_RES";
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
		for(int i = 0 ; i < bagInfo4Client.length ; i++){
			len += 1;
			len += 4;
			len += bagInfo4Client[i].getEntityId().length * 8;
			len += 4;
			len += bagInfo4Client[i].getCounts().length * 2;
		}
		len += 4;
		for(int i = 0 ; i < categories.length ; i++){
			len += 8;
			len += 1;
			len += 2;
			if(categories[i].getCategoryName() != null){
				try{
				len += categories[i].getCategoryName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 8;
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

			buffer.putInt(bagInfo4Client.length);
			for(int i = 0 ; i < bagInfo4Client.length ; i++){
				buffer.put((byte)bagInfo4Client[i].getBagtype());
				buffer.putInt(bagInfo4Client[i].getEntityId().length);
				long[] entityId_0003 = bagInfo4Client[i].getEntityId();
				for(int j = 0 ; j < entityId_0003.length ; j++){
					buffer.putLong(entityId_0003[j]);
				}
				buffer.putInt(bagInfo4Client[i].getCounts().length);
				short[] counts_0004 = bagInfo4Client[i].getCounts();
				for(int j = 0 ; j < counts_0004.length ; j++){
					buffer.putShort(counts_0004[j]);
				}
			}
			buffer.putInt(categories.length);
			for(int i = 0 ; i < categories.length ; i++){
				buffer.putLong(categories[i].getCooldownLimit());
				buffer.put((byte)categories[i].getStalemateType());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = categories[i].getCategoryName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putLong(categories[i].getStalemateTime());
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
	 *	背包内容
	 */
	public BagInfo4Client[] getBagInfo4Client(){
		return bagInfo4Client;
	}

	/**
	 * 设置属性：
	 *	背包内容
	 */
	public void setBagInfo4Client(BagInfo4Client[] bagInfo4Client){
		this.bagInfo4Client = bagInfo4Client;
	}

	/**
	 * 获取属性：
	 *	道具分类信息
	 */
	public PropsCategory[] getCategories(){
		return categories;
	}

	/**
	 * 设置属性：
	 *	道具分类信息
	 */
	public void setCategories(PropsCategory[] categories){
		this.categories = categories;
	}

}
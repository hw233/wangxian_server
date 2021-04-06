package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 仙气葫芦临时道具id请求<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleIds</td><td>long[]</td><td>articleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>addExps.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>addExps</td><td>long[]</td><td>addExps.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soulOpenlevel.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soulOpenlevel</td><td>int[]</td><td>soulOpenlevel.length</td><td>*</td></tr>
 * </table>
 */
public class GOURD_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] articleIds;
	long[] addExps;
	int[] soulOpenlevel;

	public GOURD_INFO_RES(){
	}

	public GOURD_INFO_RES(long seqNum,long[] articleIds,long[] addExps,int[] soulOpenlevel){
		this.seqNum = seqNum;
		this.articleIds = articleIds;
		this.addExps = addExps;
		this.soulOpenlevel = soulOpenlevel;
	}

	public GOURD_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		articleIds = new long[len];
		for(int i = 0 ; i < articleIds.length ; i++){
			articleIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		addExps = new long[len];
		for(int i = 0 ; i < addExps.length ; i++){
			addExps[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		soulOpenlevel = new int[len];
		for(int i = 0 ; i < soulOpenlevel.length ; i++){
			soulOpenlevel[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FF0172;
	}

	public String getTypeDescription() {
		return "GOURD_INFO_RES";
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
		len += articleIds.length * 8;
		len += 4;
		len += addExps.length * 8;
		len += 4;
		len += soulOpenlevel.length * 4;
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

			buffer.putInt(articleIds.length);
			for(int i = 0 ; i < articleIds.length; i++){
				buffer.putLong(articleIds[i]);
			}
			buffer.putInt(addExps.length);
			for(int i = 0 ; i < addExps.length; i++){
				buffer.putLong(addExps[i]);
			}
			buffer.putInt(soulOpenlevel.length);
			for(int i = 0 ; i < soulOpenlevel.length; i++){
				buffer.putInt(soulOpenlevel[i]);
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
	 *	仙气葫芦物品id
	 */
	public long[] getArticleIds(){
		return articleIds;
	}

	/**
	 * 设置属性：
	 *	仙气葫芦物品id
	 */
	public void setArticleIds(long[] articleIds){
		this.articleIds = articleIds;
	}

	/**
	 * 获取属性：
	 *	对应增加经验值
	 */
	public long[] getAddExps(){
		return addExps;
	}

	/**
	 * 设置属性：
	 *	对应增加经验值
	 */
	public void setAddExps(long[] addExps){
		this.addExps = addExps;
	}

	/**
	 * 获取属性：
	 *	灵髓镶嵌位置开启等级
	 */
	public int[] getSoulOpenlevel(){
		return soulOpenlevel;
	}

	/**
	 * 设置属性：
	 *	灵髓镶嵌位置开启等级
	 */
	public void setSoulOpenlevel(int[] soulOpenlevel){
		this.soulOpenlevel = soulOpenlevel;
	}

}
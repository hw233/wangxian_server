package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 激活隐藏属性<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>indexs.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>indexs</td><td>int[]</td><td>indexs.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialids</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialtype</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>usesilver</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long id;
	int[] indexs;
	long materialids;
	int materialtype;
	int usesilver;

	public REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ(){
	}

	public REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ(long seqNum,long id,int[] indexs,long materialids,int materialtype,int usesilver){
		this.seqNum = seqNum;
		this.id = id;
		this.indexs = indexs;
		this.materialids = materialids;
		this.materialtype = materialtype;
		this.usesilver = usesilver;
	}

	public REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		id = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		indexs = new int[len];
		for(int i = 0 ; i < indexs.length ; i++){
			indexs[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		materialids = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		materialtype = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		usesilver = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x00F0EF20;
	}

	public String getTypeDescription() {
		return "REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ";
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
		len += 4;
		len += indexs.length * 4;
		len += 8;
		len += 4;
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

			buffer.putLong(id);
			buffer.putInt(indexs.length);
			for(int i = 0 ; i < indexs.length; i++){
				buffer.putInt(indexs[i]);
			}
			buffer.putLong(materialids);
			buffer.putInt(materialtype);
			buffer.putInt(usesilver);
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
	 *	法宝id
	 */
	public long getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	法宝id
	 */
	public void setId(long id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	刷新属性集0,1,2
	 */
	public int[] getIndexs(){
		return indexs;
	}

	/**
	 * 设置属性：
	 *	刷新属性集0,1,2
	 */
	public void setIndexs(int[] indexs){
		this.indexs = indexs;
	}

	/**
	 * 获取属性：
	 *	材料id
	 */
	public long getMaterialids(){
		return materialids;
	}

	/**
	 * 设置属性：
	 *	材料id
	 */
	public void setMaterialids(long materialids){
		this.materialids = materialids;
	}

	/**
	 * 获取属性：
	 *	0:重铸符；1:天铸符；2:神铸符
	 */
	public int getMaterialtype(){
		return materialtype;
	}

	/**
	 * 设置属性：
	 *	0:重铸符；1:天铸符；2:神铸符
	 */
	public void setMaterialtype(int materialtype){
		this.materialtype = materialtype;
	}

	/**
	 * 获取属性：
	 *	0:用物品，1：银子
	 */
	public int getUsesilver(){
		return usesilver;
	}

	/**
	 * 设置属性：
	 *	0:用物品，1：银子
	 */
	public void setUsesilver(int usesilver){
		this.usesilver = usesilver;
	}

}
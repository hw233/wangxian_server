package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.homestead.cave.ResourceCollection;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 向客户端弹出升级提示界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buildingName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buildingName</td><td>String</td><td>buildingName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>level</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>curResource.food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>curResource.wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>curResource.stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>upgradeCost.food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>upgradeCost.wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>upgradeCost.stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>costTime</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buidingDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buidingDes</td><td>String</td><td>buidingDes.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class CAVE_LEVEL_UP_MOTICE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long npcId;
	String buildingName;
	int level;
	ResourceCollection curResource;
	ResourceCollection upgradeCost;
	int costTime;
	String buidingDes;

	public CAVE_LEVEL_UP_MOTICE_RES(){
	}

	public CAVE_LEVEL_UP_MOTICE_RES(long seqNum,long npcId,String buildingName,int level,ResourceCollection curResource,ResourceCollection upgradeCost,int costTime,String buidingDes){
		this.seqNum = seqNum;
		this.npcId = npcId;
		this.buildingName = buildingName;
		this.level = level;
		this.curResource = curResource;
		this.upgradeCost = upgradeCost;
		this.costTime = costTime;
		this.buidingDes = buidingDes;
	}

	public CAVE_LEVEL_UP_MOTICE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		npcId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		buildingName = new String(content,offset,len,"UTF-8");
		offset += len;
		level = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		curResource = new ResourceCollection();
		curResource.setFood((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		curResource.setWood((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		curResource.setStone((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		upgradeCost = new ResourceCollection();
		upgradeCost.setFood((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		upgradeCost.setWood((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		upgradeCost.setStone((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		costTime = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		buidingDes = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x8F000017;
	}

	public String getTypeDescription() {
		return "CAVE_LEVEL_UP_MOTICE_RES";
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
			len +=buildingName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=buidingDes.getBytes("UTF-8").length;
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

			buffer.putLong(npcId);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = buildingName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(level);
			buffer.putInt((int)curResource.getFood());
			buffer.putInt((int)curResource.getWood());
			buffer.putInt((int)curResource.getStone());
			buffer.putInt((int)upgradeCost.getFood());
			buffer.putInt((int)upgradeCost.getWood());
			buffer.putInt((int)upgradeCost.getStone());
			buffer.putInt(costTime);
				try{
			tmpBytes1 = buidingDes.getBytes("UTF-8");
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
	 *	当前NPCID
	 */
	public long getNpcId(){
		return npcId;
	}

	/**
	 * 设置属性：
	 *	当前NPCID
	 */
	public void setNpcId(long npcId){
		this.npcId = npcId;
	}

	/**
	 * 获取属性：
	 *	建筑名字
	 */
	public String getBuildingName(){
		return buildingName;
	}

	/**
	 * 设置属性：
	 *	建筑名字
	 */
	public void setBuildingName(String buildingName){
		this.buildingName = buildingName;
	}

	/**
	 * 获取属性：
	 *	当前等级
	 */
	public int getLevel(){
		return level;
	}

	/**
	 * 设置属性：
	 *	当前等级
	 */
	public void setLevel(int level){
		this.level = level;
	}

	/**
	 * 获取属性：
	 *	当前资源
	 */
	public ResourceCollection getCurResource(){
		return curResource;
	}

	/**
	 * 设置属性：
	 *	当前资源
	 */
	public void setCurResource(ResourceCollection curResource){
		this.curResource = curResource;
	}

	/**
	 * 获取属性：
	 *	升级需要资源
	 */
	public ResourceCollection getUpgradeCost(){
		return upgradeCost;
	}

	/**
	 * 设置属性：
	 *	升级需要资源
	 */
	public void setUpgradeCost(ResourceCollection upgradeCost){
		this.upgradeCost = upgradeCost;
	}

	/**
	 * 获取属性：
	 *	升级所需时间(分钟)
	 */
	public int getCostTime(){
		return costTime;
	}

	/**
	 * 设置属性：
	 *	升级所需时间(分钟)
	 */
	public void setCostTime(int costTime){
		this.costTime = costTime;
	}

	/**
	 * 获取属性：
	 *	升级描述信息
	 */
	public String getBuidingDes(){
		return buidingDes;
	}

	/**
	 * 设置属性：
	 *	升级描述信息
	 */
	public void setBuidingDes(String buidingDes){
		this.buidingDes = buidingDes;
	}

}
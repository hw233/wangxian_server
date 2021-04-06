package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.datasource.article.entity.client.EquipmentEntity;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器通知客户端，装备道具本身属性发生了变化<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities.length</td><td>int</td><td>4个字节</td><td>EquipmentEntity数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].star</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].durability</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].binded</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].inscriptionFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].inlayArticleIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].inlayArticleIds</td><td>long[]</td><td>equipmentEntities[0].inlayArticleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].colorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].developEXP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].developUpValue</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].endowments</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].star</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].durability</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].binded</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].inscriptionFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].inlayArticleIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].inlayArticleIds</td><td>long[]</td><td>equipmentEntities[1].inlayArticleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].colorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].developEXP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].developUpValue</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].endowments</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].star</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].durability</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].binded</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].inscriptionFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].inlayArticleIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].inlayArticleIds</td><td>long[]</td><td>equipmentEntities[2].inlayArticleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].colorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].developEXP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].developUpValue</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].endowments</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class NOTIFY_EQUIPMENT_CHANGE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	EquipmentEntity[] equipmentEntities;

	public NOTIFY_EQUIPMENT_CHANGE_REQ(){
	}

	public NOTIFY_EQUIPMENT_CHANGE_REQ(long seqNum,EquipmentEntity[] equipmentEntities){
		this.seqNum = seqNum;
		this.equipmentEntities = equipmentEntities;
	}

	public NOTIFY_EQUIPMENT_CHANGE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		equipmentEntities = new EquipmentEntity[len];
		for(int i = 0 ; i < equipmentEntities.length ; i++){
			equipmentEntities[i] = new EquipmentEntity();
			equipmentEntities[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			equipmentEntities[i].setStar((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			equipmentEntities[i].setDurability((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			equipmentEntities[i].setBinded(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			equipmentEntities[i].setInscriptionFlag(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] inlayArticleIds_0001 = new long[len];
			for(int j = 0 ; j < inlayArticleIds_0001.length ; j++){
				inlayArticleIds_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			equipmentEntities[i].setInlayArticleIds(inlayArticleIds_0001);
			equipmentEntities[i].setColorType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			equipmentEntities[i].setDevelopEXP((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			equipmentEntities[i].setDevelopUpValue((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			equipmentEntities[i].setEndowments((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x000000FC;
	}

	public String getTypeDescription() {
		return "NOTIFY_EQUIPMENT_CHANGE_REQ";
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
		for(int i = 0 ; i < equipmentEntities.length ; i++){
			len += 8;
			len += 1;
			len += 1;
			len += 1;
			len += 1;
			len += 4;
			len += equipmentEntities[i].getInlayArticleIds().length * 8;
			len += 1;
			len += 4;
			len += 4;
			len += 4;
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

			buffer.putInt(equipmentEntities.length);
			for(int i = 0 ; i < equipmentEntities.length ; i++){
				buffer.putLong(equipmentEntities[i].getId());
				buffer.put((byte)equipmentEntities[i].getStar());
				buffer.put((byte)equipmentEntities[i].getDurability());
				buffer.put((byte)(equipmentEntities[i].isBinded()==false?0:1));
				buffer.put((byte)(equipmentEntities[i].isInscriptionFlag()==false?0:1));
				buffer.putInt(equipmentEntities[i].getInlayArticleIds().length);
				long[] inlayArticleIds_0002 = equipmentEntities[i].getInlayArticleIds();
				for(int j = 0 ; j < inlayArticleIds_0002.length ; j++){
					buffer.putLong(inlayArticleIds_0002[j]);
				}
				buffer.put((byte)equipmentEntities[i].getColorType());
				buffer.putInt((int)equipmentEntities[i].getDevelopEXP());
				buffer.putInt((int)equipmentEntities[i].getDevelopUpValue());
				buffer.putInt((int)equipmentEntities[i].getEndowments());
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
	 *	发生变化的装备
	 */
	public EquipmentEntity[] getEquipmentEntities(){
		return equipmentEntities;
	}

	/**
	 * 设置属性：
	 *	发生变化的装备
	 */
	public void setEquipmentEntities(EquipmentEntity[] equipmentEntities){
		this.equipmentEntities = equipmentEntities;
	}

}
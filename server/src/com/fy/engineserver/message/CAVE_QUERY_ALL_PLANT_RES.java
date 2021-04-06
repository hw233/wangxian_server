package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.homestead.cave.resource.PlantCfg;
import com.fy.engineserver.homestead.cave.ResourceCollection;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询可种植列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs.length</td><td>int</td><td>4个字节</td><td>PlantCfg数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[0].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[0].name</td><td>String</td><td>plantCfgs[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[0].fieldLvNeed</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[0].outputType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[0].outputName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[0].outputName</td><td>String</td><td>plantCfgs[0].outputName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[0].outputNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[0].grownUpTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[0].avataA.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[0].avataA</td><td>String</td><td>plantCfgs[0].avataA.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[0].avataB.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[0].avataB</td><td>String</td><td>plantCfgs[0].avataB.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[0].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[0].des</td><td>String</td><td>plantCfgs[0].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[1].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[1].name</td><td>String</td><td>plantCfgs[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[1].fieldLvNeed</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[1].outputType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[1].outputName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[1].outputName</td><td>String</td><td>plantCfgs[1].outputName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[1].outputNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[1].grownUpTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[1].avataA.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[1].avataA</td><td>String</td><td>plantCfgs[1].avataA.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[1].avataB.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[1].avataB</td><td>String</td><td>plantCfgs[1].avataB.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[1].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[1].des</td><td>String</td><td>plantCfgs[1].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[2].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[2].name</td><td>String</td><td>plantCfgs[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[2].fieldLvNeed</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[2].outputType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[2].outputName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[2].outputName</td><td>String</td><td>plantCfgs[2].outputName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[2].outputNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[2].grownUpTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[2].avataA.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[2].avataA</td><td>String</td><td>plantCfgs[2].avataA.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[2].avataB.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[2].avataB</td><td>String</td><td>plantCfgs[2].avataB.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>plantCfgs[2].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>plantCfgs[2].des</td><td>String</td><td>plantCfgs[2].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resourceCollections.length</td><td>int</td><td>4个字节</td><td>ResourceCollection数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resourceCollections[0].food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resourceCollections[0].wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resourceCollections[0].stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resourceCollections[1].food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resourceCollections[1].wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resourceCollections[1].stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resourceCollections[2].food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resourceCollections[2].wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resourceCollections[2].stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class CAVE_QUERY_ALL_PLANT_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long npcId;
	PlantCfg[] plantCfgs;
	ResourceCollection[] resourceCollections;

	public CAVE_QUERY_ALL_PLANT_RES(){
	}

	public CAVE_QUERY_ALL_PLANT_RES(long seqNum,long npcId,PlantCfg[] plantCfgs,ResourceCollection[] resourceCollections){
		this.seqNum = seqNum;
		this.npcId = npcId;
		this.plantCfgs = plantCfgs;
		this.resourceCollections = resourceCollections;
	}

	public CAVE_QUERY_ALL_PLANT_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		npcId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		plantCfgs = new PlantCfg[len];
		for(int i = 0 ; i < plantCfgs.length ; i++){
			plantCfgs[i] = new PlantCfg();
			plantCfgs[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			plantCfgs[i].setType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			plantCfgs[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			plantCfgs[i].setFieldLvNeed((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			plantCfgs[i].setOutputType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			plantCfgs[i].setOutputName(new String(content,offset,len,"UTF-8"));
			offset += len;
			plantCfgs[i].setOutputNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			plantCfgs[i].setGrownUpTime((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			plantCfgs[i].setAvataA(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			plantCfgs[i].setAvataB(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			plantCfgs[i].setDes(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		resourceCollections = new ResourceCollection[len];
		for(int i = 0 ; i < resourceCollections.length ; i++){
			resourceCollections[i] = new ResourceCollection();
			resourceCollections[i].setFood((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			resourceCollections[i].setWood((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			resourceCollections[i].setStone((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x8F000006;
	}

	public String getTypeDescription() {
		return "CAVE_QUERY_ALL_PLANT_RES";
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
		for(int i = 0 ; i < plantCfgs.length ; i++){
			len += 4;
			len += 4;
			len += 2;
			if(plantCfgs[i].getName() != null){
				try{
				len += plantCfgs[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 2;
			if(plantCfgs[i].getOutputName() != null){
				try{
				len += plantCfgs[i].getOutputName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 2;
			if(plantCfgs[i].getAvataA() != null){
				try{
				len += plantCfgs[i].getAvataA().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(plantCfgs[i].getAvataB() != null){
				try{
				len += plantCfgs[i].getAvataB().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(plantCfgs[i].getDes() != null){
				try{
				len += plantCfgs[i].getDes().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 4;
		for(int i = 0 ; i < resourceCollections.length ; i++){
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

			buffer.putLong(npcId);
			buffer.putInt(plantCfgs.length);
			for(int i = 0 ; i < plantCfgs.length ; i++){
				buffer.putInt((int)plantCfgs[i].getId());
				buffer.putInt((int)plantCfgs[i].getType());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = plantCfgs[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)plantCfgs[i].getFieldLvNeed());
				buffer.putInt((int)plantCfgs[i].getOutputType());
				try{
				tmpBytes2 = plantCfgs[i].getOutputName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)plantCfgs[i].getOutputNum());
				buffer.putInt((int)plantCfgs[i].getGrownUpTime());
				try{
				tmpBytes2 = plantCfgs[i].getAvataA().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = plantCfgs[i].getAvataB().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = plantCfgs[i].getDes().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(resourceCollections.length);
			for(int i = 0 ; i < resourceCollections.length ; i++){
				buffer.putInt((int)resourceCollections[i].getFood());
				buffer.putInt((int)resourceCollections[i].getWood());
				buffer.putInt((int)resourceCollections[i].getStone());
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
	 *	NPCID
	 */
	public long getNpcId(){
		return npcId;
	}

	/**
	 * 设置属性：
	 *	NPCID
	 */
	public void setNpcId(long npcId){
		this.npcId = npcId;
	}

	/**
	 * 获取属性：
	 *	可种植的列表
	 */
	public PlantCfg[] getPlantCfgs(){
		return plantCfgs;
	}

	/**
	 * 设置属性：
	 *	可种植的列表
	 */
	public void setPlantCfgs(PlantCfg[] plantCfgs){
		this.plantCfgs = plantCfgs;
	}

	/**
	 * 获取属性：
	 *	种植成本
	 */
	public ResourceCollection[] getResourceCollections(){
		return resourceCollections;
	}

	/**
	 * 设置属性：
	 *	种植成本
	 */
	public void setResourceCollections(ResourceCollection[] resourceCollections){
		this.resourceCollections = resourceCollections;
	}

}
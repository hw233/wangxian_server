package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端请求服务器，装备强化的各种信息，包括需要什么材料，需要几个，用什么来提高成功率，需要多少手续费等<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>baoDiName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>baoDiName</td><td>String</td><td>baoDiName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cost</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongedUUB.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongedUUB</td><td>String</td><td>strongedUUB.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongMaterialName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongMaterialName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongMaterialName[0]</td><td>String</td><td>strongMaterialName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongMaterialName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongMaterialName[1]</td><td>String</td><td>strongMaterialName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongMaterialName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongMaterialName[2]</td><td>String</td><td>strongMaterialName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongMaterialLuck.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongMaterialLuck</td><td>int[]</td><td>strongMaterialLuck.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>otherStrongMaterialName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>otherStrongMaterialName</td><td>String</td><td>otherStrongMaterialName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>otherStrongMaterialNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>otherStrongMaterialluck</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongMaterialName2.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongMaterialName2[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongMaterialName2[0]</td><td>String</td><td>strongMaterialName2[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongMaterialName2[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongMaterialName2[1]</td><td>String</td><td>strongMaterialName2[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongMaterialName2[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongMaterialName2[2]</td><td>String</td><td>strongMaterialName2[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongMaterialLuck2.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongMaterialLuck2</td><td>int[]</td><td>strongMaterialLuck2.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_NEW_EQUIPMENT_STRONG3_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String baoDiName;
	long equipmentId;
	long cost;
	String strongedUUB;
	String[] strongMaterialName;
	int[] strongMaterialLuck;
	String otherStrongMaterialName;
	int otherStrongMaterialNum;
	int otherStrongMaterialluck;
	String[] strongMaterialName2;
	int[] strongMaterialLuck2;

	public QUERY_NEW_EQUIPMENT_STRONG3_RES(){
	}

	public QUERY_NEW_EQUIPMENT_STRONG3_RES(long seqNum,String baoDiName,long equipmentId,long cost,String strongedUUB,String[] strongMaterialName,int[] strongMaterialLuck,String otherStrongMaterialName,int otherStrongMaterialNum,int otherStrongMaterialluck,String[] strongMaterialName2,int[] strongMaterialLuck2){
		this.seqNum = seqNum;
		this.baoDiName = baoDiName;
		this.equipmentId = equipmentId;
		this.cost = cost;
		this.strongedUUB = strongedUUB;
		this.strongMaterialName = strongMaterialName;
		this.strongMaterialLuck = strongMaterialLuck;
		this.otherStrongMaterialName = otherStrongMaterialName;
		this.otherStrongMaterialNum = otherStrongMaterialNum;
		this.otherStrongMaterialluck = otherStrongMaterialluck;
		this.strongMaterialName2 = strongMaterialName2;
		this.strongMaterialLuck2 = strongMaterialLuck2;
	}

	public QUERY_NEW_EQUIPMENT_STRONG3_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		baoDiName = new String(content,offset,len);
		offset += len;
		equipmentId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		cost = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		strongedUUB = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		strongMaterialName = new String[len];
		for(int i = 0 ; i < strongMaterialName.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			strongMaterialName[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		strongMaterialLuck = new int[len];
		for(int i = 0 ; i < strongMaterialLuck.length ; i++){
			strongMaterialLuck[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		otherStrongMaterialName = new String(content,offset,len,"UTF-8");
		offset += len;
		otherStrongMaterialNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		otherStrongMaterialluck = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		strongMaterialName2 = new String[len];
		for(int i = 0 ; i < strongMaterialName2.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			strongMaterialName2[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		strongMaterialLuck2 = new int[len];
		for(int i = 0 ; i < strongMaterialLuck2.length ; i++){
			strongMaterialLuck2[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FFF087;
	}

	public String getTypeDescription() {
		return "QUERY_NEW_EQUIPMENT_STRONG3_RES";
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
		len += 2;
		len +=baoDiName.getBytes().length;
		len += 8;
		len += 8;
		len += 2;
		try{
			len +=strongedUUB.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < strongMaterialName.length; i++){
			len += 2;
			try{
				len += strongMaterialName[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += strongMaterialLuck.length * 4;
		len += 2;
		try{
			len +=otherStrongMaterialName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		for(int i = 0 ; i < strongMaterialName2.length; i++){
			len += 2;
			try{
				len += strongMaterialName2[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += strongMaterialLuck2.length * 4;
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

			byte[] tmpBytes1;
			tmpBytes1 = baoDiName.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(equipmentId);
			buffer.putLong(cost);
				try{
			tmpBytes1 = strongedUUB.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(strongMaterialName.length);
			for(int i = 0 ; i < strongMaterialName.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = strongMaterialName[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(strongMaterialLuck.length);
			for(int i = 0 ; i < strongMaterialLuck.length; i++){
				buffer.putInt(strongMaterialLuck[i]);
			}
				try{
			tmpBytes1 = otherStrongMaterialName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(otherStrongMaterialNum);
			buffer.putInt(otherStrongMaterialluck);
			buffer.putInt(strongMaterialName2.length);
			for(int i = 0 ; i < strongMaterialName2.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = strongMaterialName2[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(strongMaterialLuck2.length);
			for(int i = 0 ; i < strongMaterialLuck2.length; i++){
				buffer.putInt(strongMaterialLuck2[i]);
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
	 *	保底符名字
	 */
	public String getBaoDiName(){
		return baoDiName;
	}

	/**
	 * 设置属性：
	 *	保底符名字
	 */
	public void setBaoDiName(String baoDiName){
		this.baoDiName = baoDiName;
	}

	/**
	 * 获取属性：
	 *	要强化的装备，玩家的背包中必须有此装备
	 */
	public long getEquipmentId(){
		return equipmentId;
	}

	/**
	 * 设置属性：
	 *	要强化的装备，玩家的背包中必须有此装备
	 */
	public void setEquipmentId(long equipmentId){
		this.equipmentId = equipmentId;
	}

	/**
	 * 获取属性：
	 *	价格
	 */
	public long getCost(){
		return cost;
	}

	/**
	 * 设置属性：
	 *	价格
	 */
	public void setCost(long cost){
		this.cost = cost;
	}

	/**
	 * 获取属性：
	 *	强化后的装备UUB
	 */
	public String getStrongedUUB(){
		return strongedUUB;
	}

	/**
	 * 设置属性：
	 *	强化后的装备UUB
	 */
	public void setStrongedUUB(String strongedUUB){
		this.strongedUUB = strongedUUB;
	}

	/**
	 * 获取属性：
	 *	可用的强化符
	 */
	public String[] getStrongMaterialName(){
		return strongMaterialName;
	}

	/**
	 * 设置属性：
	 *	可用的强化符
	 */
	public void setStrongMaterialName(String[] strongMaterialName){
		this.strongMaterialName = strongMaterialName;
	}

	/**
	 * 获取属性：
	 *	各颜色强化符的几率，不同名称强化符颜色相同几率一样
	 */
	public int[] getStrongMaterialLuck(){
		return strongMaterialLuck;
	}

	/**
	 * 设置属性：
	 *	各颜色强化符的几率，不同名称强化符颜色相同几率一样
	 */
	public void setStrongMaterialLuck(int[] strongMaterialLuck){
		this.strongMaterialLuck = strongMaterialLuck;
	}

	/**
	 * 获取属性：
	 *	其他额外可用的强化符
	 */
	public String getOtherStrongMaterialName(){
		return otherStrongMaterialName;
	}

	/**
	 * 设置属性：
	 *	其他额外可用的强化符
	 */
	public void setOtherStrongMaterialName(String otherStrongMaterialName){
		this.otherStrongMaterialName = otherStrongMaterialName;
	}

	/**
	 * 获取属性：
	 *	其他额外可用的强化符数量
	 */
	public int getOtherStrongMaterialNum(){
		return otherStrongMaterialNum;
	}

	/**
	 * 设置属性：
	 *	其他额外可用的强化符数量
	 */
	public void setOtherStrongMaterialNum(int otherStrongMaterialNum){
		this.otherStrongMaterialNum = otherStrongMaterialNum;
	}

	/**
	 * 获取属性：
	 *	成功
	 */
	public int getOtherStrongMaterialluck(){
		return otherStrongMaterialluck;
	}

	/**
	 * 设置属性：
	 *	成功
	 */
	public void setOtherStrongMaterialluck(int otherStrongMaterialluck){
		this.otherStrongMaterialluck = otherStrongMaterialluck;
	}

	/**
	 * 获取属性：
	 *	可用的强化符
	 */
	public String[] getStrongMaterialName2(){
		return strongMaterialName2;
	}

	/**
	 * 设置属性：
	 *	可用的强化符
	 */
	public void setStrongMaterialName2(String[] strongMaterialName2){
		this.strongMaterialName2 = strongMaterialName2;
	}

	/**
	 * 获取属性：
	 *	对应概率
	 */
	public int[] getStrongMaterialLuck2(){
		return strongMaterialLuck2;
	}

	/**
	 * 设置属性：
	 *	对应概率
	 */
	public void setStrongMaterialLuck2(int[] strongMaterialLuck2){
		this.strongMaterialLuck2 = strongMaterialLuck2;
	}

}
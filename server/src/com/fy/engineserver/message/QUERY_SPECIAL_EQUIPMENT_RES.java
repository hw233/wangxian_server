package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询特殊装备<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equpmentType</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equpmentTypeName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equpmentTypeName</td><td>String</td><td>equpmentTypeName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>isAppears.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>isAppears</td><td>boolean[]</td><td>isAppears.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equpmentNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equpmentNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equpmentNames[0]</td><td>String</td><td>equpmentNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equpmentNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equpmentNames[1]</td><td>String</td><td>equpmentNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equpmentNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equpmentNames[2]</td><td>String</td><td>equpmentNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equpmentLevels.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equpmentLevels</td><td>int[]</td><td>equpmentLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equpmentClasses.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equpmentClasses</td><td>int[]</td><td>equpmentClasses.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equpmentOnwerNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equpmentOnwerNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equpmentOnwerNames[0]</td><td>String</td><td>equpmentOnwerNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equpmentOnwerNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equpmentOnwerNames[1]</td><td>String</td><td>equpmentOnwerNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equpmentOnwerNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equpmentOnwerNames[2]</td><td>String</td><td>equpmentOnwerNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equpmentShowType.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equpmentShowType</td><td>int[]</td><td>equpmentShowType.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_SPECIAL_EQUIPMENT_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	boolean equpmentType;
	String equpmentTypeName;
	boolean[] isAppears;
	String[] equpmentNames;
	int[] equpmentLevels;
	int[] equpmentClasses;
	String[] equpmentOnwerNames;
	int[] equpmentShowType;

	public QUERY_SPECIAL_EQUIPMENT_RES(){
	}

	public QUERY_SPECIAL_EQUIPMENT_RES(long seqNum,boolean equpmentType,String equpmentTypeName,boolean[] isAppears,String[] equpmentNames,int[] equpmentLevels,int[] equpmentClasses,String[] equpmentOnwerNames,int[] equpmentShowType){
		this.seqNum = seqNum;
		this.equpmentType = equpmentType;
		this.equpmentTypeName = equpmentTypeName;
		this.isAppears = isAppears;
		this.equpmentNames = equpmentNames;
		this.equpmentLevels = equpmentLevels;
		this.equpmentClasses = equpmentClasses;
		this.equpmentOnwerNames = equpmentOnwerNames;
		this.equpmentShowType = equpmentShowType;
	}

	public QUERY_SPECIAL_EQUIPMENT_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		equpmentType = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		equpmentTypeName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		isAppears = new boolean[len];
		for(int i = 0 ; i < isAppears.length ; i++){
			isAppears[i] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		equpmentNames = new String[len];
		for(int i = 0 ; i < equpmentNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			equpmentNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		equpmentLevels = new int[len];
		for(int i = 0 ; i < equpmentLevels.length ; i++){
			equpmentLevels[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		equpmentClasses = new int[len];
		for(int i = 0 ; i < equpmentClasses.length ; i++){
			equpmentClasses[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		equpmentOnwerNames = new String[len];
		for(int i = 0 ; i < equpmentOnwerNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			equpmentOnwerNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		equpmentShowType = new int[len];
		for(int i = 0 ; i < equpmentShowType.length ; i++){
			equpmentShowType[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x700F0200;
	}

	public String getTypeDescription() {
		return "QUERY_SPECIAL_EQUIPMENT_RES";
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
		len += 1;
		len += 2;
		try{
			len +=equpmentTypeName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += isAppears.length;
		len += 4;
		for(int i = 0 ; i < equpmentNames.length; i++){
			len += 2;
			try{
				len += equpmentNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += equpmentLevels.length * 4;
		len += 4;
		len += equpmentClasses.length * 4;
		len += 4;
		for(int i = 0 ; i < equpmentOnwerNames.length; i++){
			len += 2;
			try{
				len += equpmentOnwerNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += equpmentShowType.length * 4;
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

			buffer.put((byte)(equpmentType==false?0:1));
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = equpmentTypeName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(isAppears.length);
			for(int i = 0 ; i < isAppears.length; i++){
				buffer.put((byte)(isAppears[i]==false?0:1));
			}
			buffer.putInt(equpmentNames.length);
			for(int i = 0 ; i < equpmentNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = equpmentNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(equpmentLevels.length);
			for(int i = 0 ; i < equpmentLevels.length; i++){
				buffer.putInt(equpmentLevels[i]);
			}
			buffer.putInt(equpmentClasses.length);
			for(int i = 0 ; i < equpmentClasses.length; i++){
				buffer.putInt(equpmentClasses[i]);
			}
			buffer.putInt(equpmentOnwerNames.length);
			for(int i = 0 ; i < equpmentOnwerNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = equpmentOnwerNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(equpmentShowType.length);
			for(int i = 0 ; i < equpmentShowType.length; i++){
				buffer.putInt(equpmentShowType[i]);
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
	 *	是那个特殊装备:鸿天帝宝(true)
	 */
	public boolean getEqupmentType(){
		return equpmentType;
	}

	/**
	 * 设置属性：
	 *	是那个特殊装备:鸿天帝宝(true)
	 */
	public void setEqupmentType(boolean equpmentType){
		this.equpmentType = equpmentType;
	}

	/**
	 * 获取属性：
	 *	是那个特殊装备:鸿天帝宝,伏天古宝
	 */
	public String getEqupmentTypeName(){
		return equpmentTypeName;
	}

	/**
	 * 设置属性：
	 *	是那个特殊装备:鸿天帝宝,伏天古宝
	 */
	public void setEqupmentTypeName(String equpmentTypeName){
		this.equpmentTypeName = equpmentTypeName;
	}

	/**
	 * 获取属性：
	 *	是否已经出现,fasle没有出现，下边属性没意义
	 */
	public boolean[] getIsAppears(){
		return isAppears;
	}

	/**
	 * 设置属性：
	 *	是否已经出现,fasle没有出现，下边属性没意义
	 */
	public void setIsAppears(boolean[] isAppears){
		this.isAppears = isAppears;
	}

	/**
	 * 获取属性：
	 *	已经出现的特殊装备名称
	 */
	public String[] getEqupmentNames(){
		return equpmentNames;
	}

	/**
	 * 设置属性：
	 *	已经出现的特殊装备名称
	 */
	public void setEqupmentNames(String[] equpmentNames){
		this.equpmentNames = equpmentNames;
	}

	/**
	 * 获取属性：
	 *	特殊装备级别
	 */
	public int[] getEqupmentLevels(){
		return equpmentLevels;
	}

	/**
	 * 设置属性：
	 *	特殊装备级别
	 */
	public void setEqupmentLevels(int[] equpmentLevels){
		this.equpmentLevels = equpmentLevels;
	}

	/**
	 * 获取属性：
	 *	特殊装备境界
	 */
	public int[] getEqupmentClasses(){
		return equpmentClasses;
	}

	/**
	 * 设置属性：
	 *	特殊装备境界
	 */
	public void setEqupmentClasses(int[] equpmentClasses){
		this.equpmentClasses = equpmentClasses;
	}

	/**
	 * 获取属性：
	 *	装备所属于玩家的姓名
	 */
	public String[] getEqupmentOnwerNames(){
		return equpmentOnwerNames;
	}

	/**
	 * 设置属性：
	 *	装备所属于玩家的姓名
	 */
	public void setEqupmentOnwerNames(String[] equpmentOnwerNames){
		this.equpmentOnwerNames = equpmentOnwerNames;
	}

	/**
	 * 获取属性：
	 *	0.斧子1.禅杖2.刺3.轮4.毛笔5.法剑6.法杖7.幡...8:头盔,9护肩,10胸,11护腕,12腰带,13靴子,14首饰,15项链,16戒指,
	 */
	public int[] getEqupmentShowType(){
		return equpmentShowType;
	}

	/**
	 * 设置属性：
	 *	0.斧子1.禅杖2.刺3.轮4.毛笔5.法剑6.法杖7.幡...8:头盔,9护肩,10胸,11护腕,12腰带,13靴子,14首饰,15项链,16戒指,
	 */
	public void setEqupmentShowType(int[] equpmentShowType){
		this.equpmentShowType = equpmentShowType;
	}

}
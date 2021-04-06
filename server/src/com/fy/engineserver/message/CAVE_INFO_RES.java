package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.homestead.cave.ResourceCollection;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查看仙府信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>curResource.food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>curResource.wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>curResource.stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maintenanceCost.food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maintenanceCost.wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maintenanceCost.stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currMaxResource.food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currMaxResource.wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currMaxResource.stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name[0]</td><td>String</td><td>name[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name[1]</td><td>String</td><td>name[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name[2]</td><td>String</td><td>name[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>types.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>types</td><td>int[]</td><td>types.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>grade.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>grade</td><td>int[]</td><td>grade.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>upgradeCost.length</td><td>int</td><td>4个字节</td><td>ResourceCollection数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>upgradeCost[0].food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>upgradeCost[0].wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>upgradeCost[0].stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>upgradeCost[1].food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>upgradeCost[1].wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>upgradeCost[1].stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>upgradeCost[2].food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>upgradeCost[2].wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>upgradeCost[2].stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>depend.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>depend[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>depend[0]</td><td>String</td><td>depend[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>depend[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>depend[1]</td><td>String</td><td>depend[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>depend[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>depend[2]</td><td>String</td><td>depend[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class CAVE_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	ResourceCollection curResource;
	ResourceCollection maintenanceCost;
	ResourceCollection currMaxResource;
	String[] name;
	int[] types;
	int[] grade;
	ResourceCollection[] upgradeCost;
	String[] depend;

	public CAVE_INFO_RES(){
	}

	public CAVE_INFO_RES(long seqNum,ResourceCollection curResource,ResourceCollection maintenanceCost,ResourceCollection currMaxResource,String[] name,int[] types,int[] grade,ResourceCollection[] upgradeCost,String[] depend){
		this.seqNum = seqNum;
		this.curResource = curResource;
		this.maintenanceCost = maintenanceCost;
		this.currMaxResource = currMaxResource;
		this.name = name;
		this.types = types;
		this.grade = grade;
		this.upgradeCost = upgradeCost;
		this.depend = depend;
	}

	public CAVE_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		curResource = new ResourceCollection();
		curResource.setFood((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		curResource.setWood((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		curResource.setStone((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		maintenanceCost = new ResourceCollection();
		maintenanceCost.setFood((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		maintenanceCost.setWood((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		maintenanceCost.setStone((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		currMaxResource = new ResourceCollection();
		currMaxResource.setFood((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		currMaxResource.setWood((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		currMaxResource.setStone((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		name = new String[len];
		for(int i = 0 ; i < name.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			name[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		types = new int[len];
		for(int i = 0 ; i < types.length ; i++){
			types[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		grade = new int[len];
		for(int i = 0 ; i < grade.length ; i++){
			grade[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		upgradeCost = new ResourceCollection[len];
		for(int i = 0 ; i < upgradeCost.length ; i++){
			upgradeCost[i] = new ResourceCollection();
			upgradeCost[i].setFood((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			upgradeCost[i].setWood((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			upgradeCost[i].setStone((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		depend = new String[len];
		for(int i = 0 ; i < depend.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			depend[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x8F000015;
	}

	public String getTypeDescription() {
		return "CAVE_INFO_RES";
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
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		for(int i = 0 ; i < name.length; i++){
			len += 2;
			try{
				len += name[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += types.length * 4;
		len += 4;
		len += grade.length * 4;
		len += 4;
		for(int i = 0 ; i < upgradeCost.length ; i++){
			len += 4;
			len += 4;
			len += 4;
		}
		len += 4;
		for(int i = 0 ; i < depend.length; i++){
			len += 2;
			try{
				len += depend[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
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

			buffer.putInt((int)curResource.getFood());
			buffer.putInt((int)curResource.getWood());
			buffer.putInt((int)curResource.getStone());
			buffer.putInt((int)maintenanceCost.getFood());
			buffer.putInt((int)maintenanceCost.getWood());
			buffer.putInt((int)maintenanceCost.getStone());
			buffer.putInt((int)currMaxResource.getFood());
			buffer.putInt((int)currMaxResource.getWood());
			buffer.putInt((int)currMaxResource.getStone());
			buffer.putInt(name.length);
			for(int i = 0 ; i < name.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = name[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(types.length);
			for(int i = 0 ; i < types.length; i++){
				buffer.putInt(types[i]);
			}
			buffer.putInt(grade.length);
			for(int i = 0 ; i < grade.length; i++){
				buffer.putInt(grade[i]);
			}
			buffer.putInt(upgradeCost.length);
			for(int i = 0 ; i < upgradeCost.length ; i++){
				buffer.putInt((int)upgradeCost[i].getFood());
				buffer.putInt((int)upgradeCost[i].getWood());
				buffer.putInt((int)upgradeCost[i].getStone());
			}
			buffer.putInt(depend.length);
			for(int i = 0 ; i < depend.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = depend[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
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
	 *	维护资源
	 */
	public ResourceCollection getMaintenanceCost(){
		return maintenanceCost;
	}

	/**
	 * 设置属性：
	 *	维护资源
	 */
	public void setMaintenanceCost(ResourceCollection maintenanceCost){
		this.maintenanceCost = maintenanceCost;
	}

	/**
	 * 获取属性：
	 *	资源上限
	 */
	public ResourceCollection getCurrMaxResource(){
		return currMaxResource;
	}

	/**
	 * 设置属性：
	 *	资源上限
	 */
	public void setCurrMaxResource(ResourceCollection currMaxResource){
		this.currMaxResource = currMaxResource;
	}

	/**
	 * 获取属性：
	 *	建筑名字
	 */
	public String[] getName(){
		return name;
	}

	/**
	 * 设置属性：
	 *	建筑名字
	 */
	public void setName(String[] name){
		this.name = name;
	}

	/**
	 * 获取属性：
	 *	建筑类型
	 */
	public int[] getTypes(){
		return types;
	}

	/**
	 * 设置属性：
	 *	建筑类型
	 */
	public void setTypes(int[] types){
		this.types = types;
	}

	/**
	 * 获取属性：
	 *	等级
	 */
	public int[] getGrade(){
		return grade;
	}

	/**
	 * 设置属性：
	 *	等级
	 */
	public void setGrade(int[] grade){
		this.grade = grade;
	}

	/**
	 * 获取属性：
	 *	lvup资源
	 */
	public ResourceCollection[] getUpgradeCost(){
		return upgradeCost;
	}

	/**
	 * 设置属性：
	 *	lvup资源
	 */
	public void setUpgradeCost(ResourceCollection[] upgradeCost){
		this.upgradeCost = upgradeCost;
	}

	/**
	 * 获取属性：
	 *	升级条件
	 */
	public String[] getDepend(){
		return depend;
	}

	/**
	 * 设置属性：
	 *	升级条件
	 */
	public void setDepend(String[] depend){
		this.depend = depend;
	}

}
package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 家族修炼信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gongzi</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xiulianZhi</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxShangxiangNums</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentShangxiang</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shangxiangId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shangxiangId</td><td>int[]</td><td>shangxiangId.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description[0]</td><td>String</td><td>description[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description[1]</td><td>String</td><td>description[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description[2]</td><td>String</td><td>description[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>costType.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>costType</td><td>int[]</td><td>costType.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>costNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>costNums</td><td>long[]</td><td>costNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardXiulian.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewardXiulian</td><td>int[]</td><td>rewardXiulian.length</td><td>*</td></tr>
 * </table>
 */
public class JIAZU_XIULIAN_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long gongzi;
	long xiulianZhi;
	int maxShangxiangNums;
	int currentShangxiang;
	int[] shangxiangId;
	String[] description;
	int[] costType;
	long[] costNums;
	int[] rewardXiulian;

	public JIAZU_XIULIAN_INFO_RES(){
	}

	public JIAZU_XIULIAN_INFO_RES(long seqNum,long gongzi,long xiulianZhi,int maxShangxiangNums,int currentShangxiang,int[] shangxiangId,String[] description,int[] costType,long[] costNums,int[] rewardXiulian){
		this.seqNum = seqNum;
		this.gongzi = gongzi;
		this.xiulianZhi = xiulianZhi;
		this.maxShangxiangNums = maxShangxiangNums;
		this.currentShangxiang = currentShangxiang;
		this.shangxiangId = shangxiangId;
		this.description = description;
		this.costType = costType;
		this.costNums = costNums;
		this.rewardXiulian = rewardXiulian;
	}

	public JIAZU_XIULIAN_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		gongzi = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		xiulianZhi = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		maxShangxiangNums = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		currentShangxiang = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		shangxiangId = new int[len];
		for(int i = 0 ; i < shangxiangId.length ; i++){
			shangxiangId[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		description = new String[len];
		for(int i = 0 ; i < description.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			description[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		costType = new int[len];
		for(int i = 0 ; i < costType.length ; i++){
			costType[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		costNums = new long[len];
		for(int i = 0 ; i < costNums.length ; i++){
			costNums[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		rewardXiulian = new int[len];
		for(int i = 0 ; i < rewardXiulian.length ; i++){
			rewardXiulian[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FF0035;
	}

	public String getTypeDescription() {
		return "JIAZU_XIULIAN_INFO_RES";
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
		len += 8;
		len += 4;
		len += 4;
		len += 4;
		len += shangxiangId.length * 4;
		len += 4;
		for(int i = 0 ; i < description.length; i++){
			len += 2;
			try{
				len += description[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += costType.length * 4;
		len += 4;
		len += costNums.length * 8;
		len += 4;
		len += rewardXiulian.length * 4;
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

			buffer.putLong(gongzi);
			buffer.putLong(xiulianZhi);
			buffer.putInt(maxShangxiangNums);
			buffer.putInt(currentShangxiang);
			buffer.putInt(shangxiangId.length);
			for(int i = 0 ; i < shangxiangId.length; i++){
				buffer.putInt(shangxiangId[i]);
			}
			buffer.putInt(description.length);
			for(int i = 0 ; i < description.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = description[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(costType.length);
			for(int i = 0 ; i < costType.length; i++){
				buffer.putInt(costType[i]);
			}
			buffer.putInt(costNums.length);
			for(int i = 0 ; i < costNums.length; i++){
				buffer.putLong(costNums[i]);
			}
			buffer.putInt(rewardXiulian.length);
			for(int i = 0 ; i < rewardXiulian.length; i++){
				buffer.putInt(rewardXiulian[i]);
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
	 *	工资
	 */
	public long getGongzi(){
		return gongzi;
	}

	/**
	 * 设置属性：
	 *	工资
	 */
	public void setGongzi(long gongzi){
		this.gongzi = gongzi;
	}

	/**
	 * 获取属性：
	 *	修炼值 
	 */
	public long getXiulianZhi(){
		return xiulianZhi;
	}

	/**
	 * 设置属性：
	 *	修炼值 
	 */
	public void setXiulianZhi(long xiulianZhi){
		this.xiulianZhi = xiulianZhi;
	}

	/**
	 * 获取属性：
	 *	最多可上香次数 
	 */
	public int getMaxShangxiangNums(){
		return maxShangxiangNums;
	}

	/**
	 * 设置属性：
	 *	最多可上香次数 
	 */
	public void setMaxShangxiangNums(int maxShangxiangNums){
		this.maxShangxiangNums = maxShangxiangNums;
	}

	/**
	 * 获取属性：
	 *	已经上香次数
	 */
	public int getCurrentShangxiang(){
		return currentShangxiang;
	}

	/**
	 * 设置属性：
	 *	已经上香次数
	 */
	public void setCurrentShangxiang(int currentShangxiang){
		this.currentShangxiang = currentShangxiang;
	}

	/**
	 * 获取属性：
	 *	对应id
	 */
	public int[] getShangxiangId(){
		return shangxiangId;
	}

	/**
	 * 设置属性：
	 *	对应id
	 */
	public void setShangxiangId(int[] shangxiangId){
		this.shangxiangId = shangxiangId;
	}

	/**
	 * 获取属性：
	 *	奖励描述
	 */
	public String[] getDescription(){
		return description;
	}

	/**
	 * 设置属性：
	 *	奖励描述
	 */
	public void setDescription(String[] description){
		this.description = description;
	}

	/**
	 * 获取属性：
	 *	消耗金钱类型  0绑银  2工资 1银子
	 */
	public int[] getCostType(){
		return costType;
	}

	/**
	 * 设置属性：
	 *	消耗金钱类型  0绑银  2工资 1银子
	 */
	public void setCostType(int[] costType){
		this.costType = costType;
	}

	/**
	 * 获取属性：
	 *	对应消耗数量
	 */
	public long[] getCostNums(){
		return costNums;
	}

	/**
	 * 设置属性：
	 *	对应消耗数量
	 */
	public void setCostNums(long[] costNums){
		this.costNums = costNums;
	}

	/**
	 * 获取属性：
	 *	对应奖励修炼值数量
	 */
	public int[] getRewardXiulian(){
		return rewardXiulian;
	}

	/**
	 * 设置属性：
	 *	对应奖励修炼值数量
	 */
	public void setRewardXiulian(int[] rewardXiulian){
		this.rewardXiulian = rewardXiulian;
	}

}
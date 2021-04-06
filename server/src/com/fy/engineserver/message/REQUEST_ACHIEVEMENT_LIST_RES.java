package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.achievement.LeftMenu;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询成就分类列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totalAchievement</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>doneAchievement</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>achievementDegree</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftMenus.length</td><td>int</td><td>4个字节</td><td>LeftMenu数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftMenus[0].mainName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftMenus[0].mainName</td><td>String</td><td>leftMenus[0].mainName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftMenus[0].subNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftMenus[0].subNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftMenus[0].subNames[0]</td><td>String</td><td>leftMenus[0].subNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftMenus[0].subNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftMenus[0].subNames[1]</td><td>String</td><td>leftMenus[0].subNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftMenus[0].subNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftMenus[0].subNames[2]</td><td>String</td><td>leftMenus[0].subNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftMenus[1].mainName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftMenus[1].mainName</td><td>String</td><td>leftMenus[1].mainName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftMenus[1].subNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftMenus[1].subNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftMenus[1].subNames[0]</td><td>String</td><td>leftMenus[1].subNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftMenus[1].subNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftMenus[1].subNames[1]</td><td>String</td><td>leftMenus[1].subNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftMenus[1].subNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftMenus[1].subNames[2]</td><td>String</td><td>leftMenus[1].subNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftMenus[2].mainName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftMenus[2].mainName</td><td>String</td><td>leftMenus[2].mainName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftMenus[2].subNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftMenus[2].subNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftMenus[2].subNames[0]</td><td>String</td><td>leftMenus[2].subNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftMenus[2].subNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftMenus[2].subNames[1]</td><td>String</td><td>leftMenus[2].subNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftMenus[2].subNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftMenus[2].subNames[2]</td><td>String</td><td>leftMenus[2].subNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class REQUEST_ACHIEVEMENT_LIST_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int totalAchievement;
	int doneAchievement;
	long achievementDegree;
	LeftMenu[] leftMenus;

	public REQUEST_ACHIEVEMENT_LIST_RES(){
	}

	public REQUEST_ACHIEVEMENT_LIST_RES(long seqNum,int totalAchievement,int doneAchievement,long achievementDegree,LeftMenu[] leftMenus){
		this.seqNum = seqNum;
		this.totalAchievement = totalAchievement;
		this.doneAchievement = doneAchievement;
		this.achievementDegree = achievementDegree;
		this.leftMenus = leftMenus;
	}

	public REQUEST_ACHIEVEMENT_LIST_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		totalAchievement = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		doneAchievement = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		achievementDegree = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		leftMenus = new LeftMenu[len];
		for(int i = 0 ; i < leftMenus.length ; i++){
			leftMenus[i] = new LeftMenu();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			leftMenus[i].setMainName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] subNames_0001 = new String[len];
			for(int j = 0 ; j < subNames_0001.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				subNames_0001[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			leftMenus[i].setSubNames(subNames_0001);
		}
	}

	public int getType() {
		return 0x0F70002B;
	}

	public String getTypeDescription() {
		return "REQUEST_ACHIEVEMENT_LIST_RES";
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
		len += 8;
		len += 4;
		for(int i = 0 ; i < leftMenus.length ; i++){
			len += 2;
			if(leftMenus[i].getMainName() != null){
				try{
				len += leftMenus[i].getMainName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] subNames = leftMenus[i].getSubNames();
			for(int j = 0 ; j < subNames.length; j++){
				len += 2;
				try{
					len += subNames[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
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

			buffer.putInt(totalAchievement);
			buffer.putInt(doneAchievement);
			buffer.putLong(achievementDegree);
			buffer.putInt(leftMenus.length);
			for(int i = 0 ; i < leftMenus.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = leftMenus[i].getMainName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(leftMenus[i].getSubNames().length);
				String[] subNames_0002 = leftMenus[i].getSubNames();
				for(int j = 0 ; j < subNames_0002.length ; j++){
				try{
					buffer.putShort((short)subNames_0002[j].getBytes("UTF-8").length);
					buffer.put(subNames_0002[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
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
	 *	总成就数
	 */
	public int getTotalAchievement(){
		return totalAchievement;
	}

	/**
	 * 设置属性：
	 *	总成就数
	 */
	public void setTotalAchievement(int totalAchievement){
		this.totalAchievement = totalAchievement;
	}

	/**
	 * 获取属性：
	 *	已达成成就数
	 */
	public int getDoneAchievement(){
		return doneAchievement;
	}

	/**
	 * 设置属性：
	 *	已达成成就数
	 */
	public void setDoneAchievement(int doneAchievement){
		this.doneAchievement = doneAchievement;
	}

	/**
	 * 获取属性：
	 *	成就点数
	 */
	public long getAchievementDegree(){
		return achievementDegree;
	}

	/**
	 * 设置属性：
	 *	成就点数
	 */
	public void setAchievementDegree(long achievementDegree){
		this.achievementDegree = achievementDegree;
	}

	/**
	 * 获取属性：
	 *	成就左侧列表
	 */
	public LeftMenu[] getLeftMenus(){
		return leftMenus;
	}

	/**
	 * 设置属性：
	 *	成就左侧列表
	 */
	public void setLeftMenus(LeftMenu[] leftMenus){
		this.leftMenus = leftMenus;
	}

}
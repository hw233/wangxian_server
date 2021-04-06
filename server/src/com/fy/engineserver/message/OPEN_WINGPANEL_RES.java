package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 打开主界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>star</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>brightInlayId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>wingId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>typeNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxTypeNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicPropertyValue.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicPropertyValue</td><td>int[]</td><td>basicPropertyValue.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicPropertyActive.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicPropertyActive</td><td>boolean[]</td><td>basicPropertyActive.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizePropertyValue.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizePropertyValue</td><td>int[]</td><td>prizePropertyValue.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizePropertyActive.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizePropertyActive</td><td>boolean[]</td><td>prizePropertyActive.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inlayHoleDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inlayHoleDes</td><td>String</td><td>inlayHoleDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inlayColors.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inlayColors</td><td>int[]</td><td>inlayColors.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inlayOpen.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inlayOpen</td><td>boolean[]</td><td>inlayOpen.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inlayArticleIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inlayArticleIds</td><td>long[]</td><td>inlayArticleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inlayDes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inlayDes[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inlayDes[0]</td><td>String</td><td>inlayDes[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inlayDes[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inlayDes[1]</td><td>String</td><td>inlayDes[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inlayDes[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inlayDes[2]</td><td>String</td><td>inlayDes[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class OPEN_WINGPANEL_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long playerId;
	int star;
	long brightInlayId;
	int wingId;
	int typeNum;
	int maxTypeNum;
	int[] basicPropertyValue;
	boolean[] basicPropertyActive;
	int[] prizePropertyValue;
	boolean[] prizePropertyActive;
	String inlayHoleDes;
	int[] inlayColors;
	boolean[] inlayOpen;
	long[] inlayArticleIds;
	String[] inlayDes;

	public OPEN_WINGPANEL_RES(){
	}

	public OPEN_WINGPANEL_RES(long seqNum,long playerId,int star,long brightInlayId,int wingId,int typeNum,int maxTypeNum,int[] basicPropertyValue,boolean[] basicPropertyActive,int[] prizePropertyValue,boolean[] prizePropertyActive,String inlayHoleDes,int[] inlayColors,boolean[] inlayOpen,long[] inlayArticleIds,String[] inlayDes){
		this.seqNum = seqNum;
		this.playerId = playerId;
		this.star = star;
		this.brightInlayId = brightInlayId;
		this.wingId = wingId;
		this.typeNum = typeNum;
		this.maxTypeNum = maxTypeNum;
		this.basicPropertyValue = basicPropertyValue;
		this.basicPropertyActive = basicPropertyActive;
		this.prizePropertyValue = prizePropertyValue;
		this.prizePropertyActive = prizePropertyActive;
		this.inlayHoleDes = inlayHoleDes;
		this.inlayColors = inlayColors;
		this.inlayOpen = inlayOpen;
		this.inlayArticleIds = inlayArticleIds;
		this.inlayDes = inlayDes;
	}

	public OPEN_WINGPANEL_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		star = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		brightInlayId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		wingId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		typeNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxTypeNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		basicPropertyValue = new int[len];
		for(int i = 0 ; i < basicPropertyValue.length ; i++){
			basicPropertyValue[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		basicPropertyActive = new boolean[len];
		for(int i = 0 ; i < basicPropertyActive.length ; i++){
			basicPropertyActive[i] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		prizePropertyValue = new int[len];
		for(int i = 0 ; i < prizePropertyValue.length ; i++){
			prizePropertyValue[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		prizePropertyActive = new boolean[len];
		for(int i = 0 ; i < prizePropertyActive.length ; i++){
			prizePropertyActive[i] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		inlayHoleDes = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		inlayColors = new int[len];
		for(int i = 0 ; i < inlayColors.length ; i++){
			inlayColors[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		inlayOpen = new boolean[len];
		for(int i = 0 ; i < inlayOpen.length ; i++){
			inlayOpen[i] = mf.byteArrayToNumber(content,offset,1) != 0;
			offset += 1;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		inlayArticleIds = new long[len];
		for(int i = 0 ; i < inlayArticleIds.length ; i++){
			inlayArticleIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		inlayDes = new String[len];
		for(int i = 0 ; i < inlayDes.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			inlayDes[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x70FF0019;
	}

	public String getTypeDescription() {
		return "OPEN_WINGPANEL_RES";
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
		len += 8;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += basicPropertyValue.length * 4;
		len += 4;
		len += basicPropertyActive.length;
		len += 4;
		len += prizePropertyValue.length * 4;
		len += 4;
		len += prizePropertyActive.length;
		len += 2;
		try{
			len +=inlayHoleDes.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += inlayColors.length * 4;
		len += 4;
		len += inlayOpen.length;
		len += 4;
		len += inlayArticleIds.length * 8;
		len += 4;
		for(int i = 0 ; i < inlayDes.length; i++){
			len += 2;
			try{
				len += inlayDes[i].getBytes("UTF-8").length;
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

			buffer.putLong(playerId);
			buffer.putInt(star);
			buffer.putLong(brightInlayId);
			buffer.putInt(wingId);
			buffer.putInt(typeNum);
			buffer.putInt(maxTypeNum);
			buffer.putInt(basicPropertyValue.length);
			for(int i = 0 ; i < basicPropertyValue.length; i++){
				buffer.putInt(basicPropertyValue[i]);
			}
			buffer.putInt(basicPropertyActive.length);
			for(int i = 0 ; i < basicPropertyActive.length; i++){
				buffer.put((byte)(basicPropertyActive[i]==false?0:1));
			}
			buffer.putInt(prizePropertyValue.length);
			for(int i = 0 ; i < prizePropertyValue.length; i++){
				buffer.putInt(prizePropertyValue[i]);
			}
			buffer.putInt(prizePropertyActive.length);
			for(int i = 0 ; i < prizePropertyActive.length; i++){
				buffer.put((byte)(prizePropertyActive[i]==false?0:1));
			}
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = inlayHoleDes.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(inlayColors.length);
			for(int i = 0 ; i < inlayColors.length; i++){
				buffer.putInt(inlayColors[i]);
			}
			buffer.putInt(inlayOpen.length);
			for(int i = 0 ; i < inlayOpen.length; i++){
				buffer.put((byte)(inlayOpen[i]==false?0:1));
			}
			buffer.putInt(inlayArticleIds.length);
			for(int i = 0 ; i < inlayArticleIds.length; i++){
				buffer.putLong(inlayArticleIds[i]);
			}
			buffer.putInt(inlayDes.length);
			for(int i = 0 ; i < inlayDes.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = inlayDes[i].getBytes("UTF-8");
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
	 *	被查看的玩家id
	 */
	public long getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	被查看的玩家id
	 */
	public void setPlayerId(long playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	面板强化星级
	 */
	public int getStar(){
		return star;
	}

	/**
	 * 设置属性：
	 *	面板强化星级
	 */
	public void setStar(int star){
		this.star = star;
	}

	/**
	 * 获取属性：
	 *	光效宝石id
	 */
	public long getBrightInlayId(){
		return brightInlayId;
	}

	/**
	 * 设置属性：
	 *	光效宝石id
	 */
	public void setBrightInlayId(long brightInlayId){
		this.brightInlayId = brightInlayId;
	}

	/**
	 * 获取属性：
	 *	当前使用翅膀id
	 */
	public int getWingId(){
		return wingId;
	}

	/**
	 * 设置属性：
	 *	当前使用翅膀id
	 */
	public void setWingId(int wingId){
		this.wingId = wingId;
	}

	/**
	 * 获取属性：
	 *	已收集多少类翅膀
	 */
	public int getTypeNum(){
		return typeNum;
	}

	/**
	 * 设置属性：
	 *	已收集多少类翅膀
	 */
	public void setTypeNum(int typeNum){
		this.typeNum = typeNum;
	}

	/**
	 * 获取属性：
	 *	最大有多少类翅膀
	 */
	public int getMaxTypeNum(){
		return maxTypeNum;
	}

	/**
	 * 设置属性：
	 *	最大有多少类翅膀
	 */
	public void setMaxTypeNum(int maxTypeNum){
		this.maxTypeNum = maxTypeNum;
	}

	/**
	 * 获取属性：
	 *	基础属性值加成(万分比):0血量,1物理攻击,2法术攻击,3物理防御,4法术防御,5闪躲,6免暴,7命中,8暴击,9精准,10破甲,11庚金攻击,12庚金抗性,13庚金减抗,14葵水攻击,15葵水抗性,16葵水减抗,17离火攻击,18离火抗性,19离火减抗,20乙木攻击,21乙木抗性,22乙木减抗
	 */
	public int[] getBasicPropertyValue(){
		return basicPropertyValue;
	}

	/**
	 * 设置属性：
	 *	基础属性值加成(万分比):0血量,1物理攻击,2法术攻击,3物理防御,4法术防御,5闪躲,6免暴,7命中,8暴击,9精准,10破甲,11庚金攻击,12庚金抗性,13庚金减抗,14葵水攻击,15葵水抗性,16葵水减抗,17离火攻击,18离火抗性,19离火减抗,20乙木攻击,21乙木抗性,22乙木减抗
	 */
	public void setBasicPropertyValue(int[] basicPropertyValue){
		this.basicPropertyValue = basicPropertyValue;
	}

	/**
	 * 获取属性：
	 *	基础属性加成是否激活 
	 */
	public boolean[] getBasicPropertyActive(){
		return basicPropertyActive;
	}

	/**
	 * 设置属性：
	 *	基础属性加成是否激活 
	 */
	public void setBasicPropertyActive(boolean[] basicPropertyActive){
		this.basicPropertyActive = basicPropertyActive;
	}

	/**
	 * 获取属性：
	 *	奖励属性值加成(万分比):0血量,1物理攻击,2法术攻击,3物理防御,4法术防御,5闪躲,6免暴,7命中,8暴击,9精准,10破甲,11庚金攻击,12庚金抗性,13庚金减抗,14葵水攻击,15葵水抗性,16葵水减抗,17离火攻击,18离火抗性,19离火减抗,20乙木攻击,21乙木抗性,22乙木减抗
	 */
	public int[] getPrizePropertyValue(){
		return prizePropertyValue;
	}

	/**
	 * 设置属性：
	 *	奖励属性值加成(万分比):0血量,1物理攻击,2法术攻击,3物理防御,4法术防御,5闪躲,6免暴,7命中,8暴击,9精准,10破甲,11庚金攻击,12庚金抗性,13庚金减抗,14葵水攻击,15葵水抗性,16葵水减抗,17离火攻击,18离火抗性,19离火减抗,20乙木攻击,21乙木抗性,22乙木减抗
	 */
	public void setPrizePropertyValue(int[] prizePropertyValue){
		this.prizePropertyValue = prizePropertyValue;
	}

	/**
	 * 获取属性：
	 *	奖励属性加成是否激活 
	 */
	public boolean[] getPrizePropertyActive(){
		return prizePropertyActive;
	}

	/**
	 * 设置属性：
	 *	奖励属性加成是否激活 
	 */
	public void setPrizePropertyActive(boolean[] prizePropertyActive){
		this.prizePropertyActive = prizePropertyActive;
	}

	/**
	 * 获取属性：
	 *	宝石孔总描述 
	 */
	public String getInlayHoleDes(){
		return inlayHoleDes;
	}

	/**
	 * 设置属性：
	 *	宝石孔总描述 
	 */
	public void setInlayHoleDes(String inlayHoleDes){
		this.inlayHoleDes = inlayHoleDes;
	}

	/**
	 * 获取属性：
	 *	宝石孔颜色
	 */
	public int[] getInlayColors(){
		return inlayColors;
	}

	/**
	 * 设置属性：
	 *	宝石孔颜色
	 */
	public void setInlayColors(int[] inlayColors){
		this.inlayColors = inlayColors;
	}

	/**
	 * 获取属性：
	 *	宝石孔是否已激活
	 */
	public boolean[] getInlayOpen(){
		return inlayOpen;
	}

	/**
	 * 设置属性：
	 *	宝石孔是否已激活
	 */
	public void setInlayOpen(boolean[] inlayOpen){
		this.inlayOpen = inlayOpen;
	}

	/**
	 * 获取属性：
	 *	宝石镶嵌数组 
	 */
	public long[] getInlayArticleIds(){
		return inlayArticleIds;
	}

	/**
	 * 设置属性：
	 *	宝石镶嵌数组 
	 */
	public void setInlayArticleIds(long[] inlayArticleIds){
		this.inlayArticleIds = inlayArticleIds;
	}

	/**
	 * 获取属性：
	 *	宝石加成和奖励属性描述
	 */
	public String[] getInlayDes(){
		return inlayDes;
	}

	/**
	 * 设置属性：
	 *	宝石加成和奖励属性描述
	 */
	public void setInlayDes(String[] inlayDes){
		this.inlayDes = inlayDes;
	}

}
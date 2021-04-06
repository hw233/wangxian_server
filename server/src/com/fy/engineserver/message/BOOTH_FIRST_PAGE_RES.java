package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * boss副本首界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[0]</td><td>String</td><td>names[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[1]</td><td>String</td><td>names[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[2]</td><td>String</td><td>names[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>damages.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>damages</td><td>long[]</td><td>damages.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rank</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>damage</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name</td><td>String</td><td>name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossName</td><td>String</td><td>bossName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossIcon</td><td>String</td><td>bossIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiaZuJoins</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>openTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>openTime</td><td>String</td><td>openTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleIds</td><td>long[]</td><td>articleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleNums</td><td>int[]</td><td>articleNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nextOpenTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class BOOTH_FIRST_PAGE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int billboardType;
	String[] names;
	long[] damages;
	int rank;
	long damage;
	String name;
	String bossName;
	String bossIcon;
	int jiaZuJoins;
	String openTime;
	long[] articleIds;
	int[] articleNums;
	long nextOpenTime;

	public BOOTH_FIRST_PAGE_RES(){
	}

	public BOOTH_FIRST_PAGE_RES(long seqNum,int billboardType,String[] names,long[] damages,int rank,long damage,String name,String bossName,String bossIcon,int jiaZuJoins,String openTime,long[] articleIds,int[] articleNums,long nextOpenTime){
		this.seqNum = seqNum;
		this.billboardType = billboardType;
		this.names = names;
		this.damages = damages;
		this.rank = rank;
		this.damage = damage;
		this.name = name;
		this.bossName = bossName;
		this.bossIcon = bossIcon;
		this.jiaZuJoins = jiaZuJoins;
		this.openTime = openTime;
		this.articleIds = articleIds;
		this.articleNums = articleNums;
		this.nextOpenTime = nextOpenTime;
	}

	public BOOTH_FIRST_PAGE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		billboardType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		names = new String[len];
		for(int i = 0 ; i < names.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			names[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		damages = new long[len];
		for(int i = 0 ; i < damages.length ; i++){
			damages[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		rank = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		damage = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		name = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		bossName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		bossIcon = new String(content,offset,len,"UTF-8");
		offset += len;
		jiaZuJoins = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		openTime = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		articleIds = new long[len];
		for(int i = 0 ; i < articleIds.length ; i++){
			articleIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		articleNums = new int[len];
		for(int i = 0 ; i < articleNums.length ; i++){
			articleNums[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		nextOpenTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x70FFF113;
	}

	public String getTypeDescription() {
		return "BOOTH_FIRST_PAGE_RES";
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
		for(int i = 0 ; i < names.length; i++){
			len += 2;
			try{
				len += names[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += damages.length * 8;
		len += 4;
		len += 8;
		len += 2;
		try{
			len +=name.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=bossName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=bossIcon.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 2;
		try{
			len +=openTime.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += articleIds.length * 8;
		len += 4;
		len += articleNums.length * 4;
		len += 8;
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

			buffer.putInt(billboardType);
			buffer.putInt(names.length);
			for(int i = 0 ; i < names.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = names[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(damages.length);
			for(int i = 0 ; i < damages.length; i++){
				buffer.putLong(damages[i]);
			}
			buffer.putInt(rank);
			buffer.putLong(damage);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = name.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = bossName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = bossIcon.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(jiaZuJoins);
				try{
			tmpBytes1 = openTime.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(articleIds.length);
			for(int i = 0 ; i < articleIds.length; i++){
				buffer.putLong(articleIds[i]);
			}
			buffer.putInt(articleNums.length);
			for(int i = 0 ; i < articleNums.length; i++){
				buffer.putInt(articleNums[i]);
			}
			buffer.putLong(nextOpenTime);
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
	 *	排行类型,0:个人，1:家族
	 */
	public int getBillboardType(){
		return billboardType;
	}

	/**
	 * 设置属性：
	 *	排行类型,0:个人，1:家族
	 */
	public void setBillboardType(int billboardType){
		this.billboardType = billboardType;
	}

	/**
	 * 获取属性：
	 *	名字集合
	 */
	public String[] getNames(){
		return names;
	}

	/**
	 * 设置属性：
	 *	名字集合
	 */
	public void setNames(String[] names){
		this.names = names;
	}

	/**
	 * 获取属性：
	 *	伤害集合
	 */
	public long[] getDamages(){
		return damages;
	}

	/**
	 * 设置属性：
	 *	伤害集合
	 */
	public void setDamages(long[] damages){
		this.damages = damages;
	}

	/**
	 * 获取属性：
	 *	本人排行:家族排名
	 */
	public int getRank(){
		return rank;
	}

	/**
	 * 设置属性：
	 *	本人排行:家族排名
	 */
	public void setRank(int rank){
		this.rank = rank;
	}

	/**
	 * 获取属性：
	 *	本人伤害:家族伤害
	 */
	public long getDamage(){
		return damage;
	}

	/**
	 * 设置属性：
	 *	本人伤害:家族伤害
	 */
	public void setDamage(long damage){
		this.damage = damage;
	}

	/**
	 * 获取属性：
	 *	本人名字:家族名字
	 */
	public String getName(){
		return name;
	}

	/**
	 * 设置属性：
	 *	本人名字:家族名字
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * 获取属性：
	 *	boss名字
	 */
	public String getBossName(){
		return bossName;
	}

	/**
	 * 设置属性：
	 *	boss名字
	 */
	public void setBossName(String bossName){
		this.bossName = bossName;
	}

	/**
	 * 获取属性：
	 *	bossIcon
	 */
	public String getBossIcon(){
		return bossIcon;
	}

	/**
	 * 设置属性：
	 *	bossIcon
	 */
	public void setBossIcon(String bossIcon){
		this.bossIcon = bossIcon;
	}

	/**
	 * 获取属性：
	 *	家族参与人数
	 */
	public int getJiaZuJoins(){
		return jiaZuJoins;
	}

	/**
	 * 设置属性：
	 *	家族参与人数
	 */
	public void setJiaZuJoins(int jiaZuJoins){
		this.jiaZuJoins = jiaZuJoins;
	}

	/**
	 * 获取属性：
	 *	开放时间
	 */
	public String getOpenTime(){
		return openTime;
	}

	/**
	 * 设置属性：
	 *	开放时间
	 */
	public void setOpenTime(String openTime){
		this.openTime = openTime;
	}

	/**
	 * 获取属性：
	 *	奖励id集
	 */
	public long[] getArticleIds(){
		return articleIds;
	}

	/**
	 * 设置属性：
	 *	奖励id集
	 */
	public void setArticleIds(long[] articleIds){
		this.articleIds = articleIds;
	}

	/**
	 * 获取属性：
	 *	奖励数量集
	 */
	public int[] getArticleNums(){
		return articleNums;
	}

	/**
	 * 设置属性：
	 *	奖励数量集
	 */
	public void setArticleNums(int[] articleNums){
		this.articleNums = articleNums;
	}

	/**
	 * 获取属性：
	 *	距离开启时间,小鱼等于0，正在进行中
	 */
	public long getNextOpenTime(){
		return nextOpenTime;
	}

	/**
	 * 设置属性：
	 *	距离开启时间,小鱼等于0，正在进行中
	 */
	public void setNextOpenTime(long nextOpenTime){
		this.nextOpenTime = nextOpenTime;
	}

}
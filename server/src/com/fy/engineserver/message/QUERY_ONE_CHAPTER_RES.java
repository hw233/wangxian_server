package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.playerAims.clientModel.PlayerAims;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 通过章节名请求章节信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chapterName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chapterName</td><td>String</td><td>chapterName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icon</td><td>String</td><td>icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totalScore</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targetScore</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentScore</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description</td><td>String</td><td>description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardArticles.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewardArticles</td><td>long[]</td><td>rewardArticles.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewardNums</td><td>long[]</td><td>rewardNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>levelLimit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>vipMul</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>vipLimit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totalAimNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>complateNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>receiveType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes.length</td><td>int</td><td>4个字节</td><td>PlayerAims数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].aimName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].aimName</td><td>String</td><td>messes[0].aimName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].description</td><td>String</td><td>messes[0].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].icon</td><td>String</td><td>messes[0].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].frameColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].aimNum</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].currentNum</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].showProcess</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].receiveStatus</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].vipReceiveLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].aimName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].aimName</td><td>String</td><td>messes[1].aimName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].description</td><td>String</td><td>messes[1].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].icon</td><td>String</td><td>messes[1].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].frameColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].aimNum</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].currentNum</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].showProcess</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].receiveStatus</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].vipReceiveLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].aimName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].aimName</td><td>String</td><td>messes[2].aimName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].description</td><td>String</td><td>messes[2].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].icon</td><td>String</td><td>messes[2].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].frameColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].aimNum</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].currentNum</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].showProcess</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].receiveStatus</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].vipReceiveLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_ONE_CHAPTER_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long playerId;
	String chapterName;
	String icon;
	int totalScore;
	int targetScore;
	int currentScore;
	String description;
	long[] rewardArticles;
	long[] rewardNums;
	int levelLimit;
	int vipMul;
	int vipLimit;
	int totalAimNum;
	int complateNum;
	byte receiveType;
	PlayerAims[] messes;

	public QUERY_ONE_CHAPTER_RES(){
	}

	public QUERY_ONE_CHAPTER_RES(long seqNum,long playerId,String chapterName,String icon,int totalScore,int targetScore,int currentScore,String description,long[] rewardArticles,long[] rewardNums,int levelLimit,int vipMul,int vipLimit,int totalAimNum,int complateNum,byte receiveType,PlayerAims[] messes){
		this.seqNum = seqNum;
		this.playerId = playerId;
		this.chapterName = chapterName;
		this.icon = icon;
		this.totalScore = totalScore;
		this.targetScore = targetScore;
		this.currentScore = currentScore;
		this.description = description;
		this.rewardArticles = rewardArticles;
		this.rewardNums = rewardNums;
		this.levelLimit = levelLimit;
		this.vipMul = vipMul;
		this.vipLimit = vipLimit;
		this.totalAimNum = totalAimNum;
		this.complateNum = complateNum;
		this.receiveType = receiveType;
		this.messes = messes;
	}

	public QUERY_ONE_CHAPTER_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		chapterName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		icon = new String(content,offset,len,"UTF-8");
		offset += len;
		totalScore = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		targetScore = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		currentScore = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		description = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		rewardArticles = new long[len];
		for(int i = 0 ; i < rewardArticles.length ; i++){
			rewardArticles[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		rewardNums = new long[len];
		for(int i = 0 ; i < rewardNums.length ; i++){
			rewardNums[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		levelLimit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		vipMul = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		vipLimit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		totalAimNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		complateNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		receiveType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		messes = new PlayerAims[len];
		for(int i = 0 ; i < messes.length ; i++){
			messes[i] = new PlayerAims();
			messes[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			messes[i].setAimName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			messes[i].setDescription(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			messes[i].setIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			messes[i].setFrameColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			messes[i].setAimNum((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			messes[i].setCurrentNum((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			messes[i].setShowProcess((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			messes[i].setReceiveStatus((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			messes[i].setVipReceiveLimit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x70F0EF41;
	}

	public String getTypeDescription() {
		return "QUERY_ONE_CHAPTER_RES";
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
			len +=chapterName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=icon.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=description.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += rewardArticles.length * 8;
		len += 4;
		len += rewardNums.length * 8;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 1;
		len += 4;
		for(int i = 0 ; i < messes.length ; i++){
			len += 4;
			len += 2;
			if(messes[i].getAimName() != null){
				try{
				len += messes[i].getAimName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(messes[i].getDescription() != null){
				try{
				len += messes[i].getDescription().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(messes[i].getIcon() != null){
				try{
				len += messes[i].getIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 8;
			len += 8;
			len += 1;
			len += 1;
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

			buffer.putLong(playerId);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = chapterName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = icon.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(totalScore);
			buffer.putInt(targetScore);
			buffer.putInt(currentScore);
				try{
			tmpBytes1 = description.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(rewardArticles.length);
			for(int i = 0 ; i < rewardArticles.length; i++){
				buffer.putLong(rewardArticles[i]);
			}
			buffer.putInt(rewardNums.length);
			for(int i = 0 ; i < rewardNums.length; i++){
				buffer.putLong(rewardNums[i]);
			}
			buffer.putInt(levelLimit);
			buffer.putInt(vipMul);
			buffer.putInt(vipLimit);
			buffer.putInt(totalAimNum);
			buffer.putInt(complateNum);
			buffer.put(receiveType);
			buffer.putInt(messes.length);
			for(int i = 0 ; i < messes.length ; i++){
				buffer.putInt((int)messes[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = messes[i].getAimName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = messes[i].getDescription().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = messes[i].getIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)messes[i].getFrameColor());
				buffer.putLong(messes[i].getAimNum());
				buffer.putLong(messes[i].getCurrentNum());
				buffer.put((byte)messes[i].getShowProcess());
				buffer.put((byte)messes[i].getReceiveStatus());
				buffer.putInt((int)messes[i].getVipReceiveLimit());
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
	 *	角色id
	 */
	public long getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	角色id
	 */
	public void setPlayerId(long playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	章节名
	 */
	public String getChapterName(){
		return chapterName;
	}

	/**
	 * 设置属性：
	 *	章节名
	 */
	public void setChapterName(String chapterName){
		this.chapterName = chapterName;
	}

	/**
	 * 获取属性：
	 *	icon
	 */
	public String getIcon(){
		return icon;
	}

	/**
	 * 设置属性：
	 *	icon
	 */
	public void setIcon(String icon){
		this.icon = icon;
	}

	/**
	 * 获取属性：
	 *	此章节可达到的最大积分
	 */
	public int getTotalScore(){
		return totalScore;
	}

	/**
	 * 设置属性：
	 *	此章节可达到的最大积分
	 */
	public void setTotalScore(int totalScore){
		this.totalScore = totalScore;
	}

	/**
	 * 获取属性：
	 *	领取章节奖励所需积分
	 */
	public int getTargetScore(){
		return targetScore;
	}

	/**
	 * 设置属性：
	 *	领取章节奖励所需积分
	 */
	public void setTargetScore(int targetScore){
		this.targetScore = targetScore;
	}

	/**
	 * 获取属性：
	 *	玩家当前此章节获得积分
	 */
	public int getCurrentScore(){
		return currentScore;
	}

	/**
	 * 设置属性：
	 *	玩家当前此章节获得积分
	 */
	public void setCurrentScore(int currentScore){
		this.currentScore = currentScore;
	}

	/**
	 * 获取属性：
	 *	章节描述
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * 设置属性：
	 *	章节描述
	 */
	public void setDescription(String description){
		this.description = description;
	}

	/**
	 * 获取属性：
	 *	章节奖励物品id列表
	 */
	public long[] getRewardArticles(){
		return rewardArticles;
	}

	/**
	 * 设置属性：
	 *	章节奖励物品id列表
	 */
	public void setRewardArticles(long[] rewardArticles){
		this.rewardArticles = rewardArticles;
	}

	/**
	 * 获取属性：
	 *	对应奖励物品数量
	 */
	public long[] getRewardNums(){
		return rewardNums;
	}

	/**
	 * 设置属性：
	 *	对应奖励物品数量
	 */
	public void setRewardNums(long[] rewardNums){
		this.rewardNums = rewardNums;
	}

	/**
	 * 获取属性：
	 *	领取奖励最低等级
	 */
	public int getLevelLimit(){
		return levelLimit;
	}

	/**
	 * 设置属性：
	 *	领取奖励最低等级
	 */
	public void setLevelLimit(int levelLimit){
		this.levelLimit = levelLimit;
	}

	/**
	 * 获取属性：
	 *	vip额外奖励倍数
	 */
	public int getVipMul(){
		return vipMul;
	}

	/**
	 * 设置属性：
	 *	vip额外奖励倍数
	 */
	public void setVipMul(int vipMul){
		this.vipMul = vipMul;
	}

	/**
	 * 获取属性：
	 *	领取vip奖励最低vip等级
	 */
	public int getVipLimit(){
		return vipLimit;
	}

	/**
	 * 设置属性：
	 *	领取vip奖励最低vip等级
	 */
	public void setVipLimit(int vipLimit){
		this.vipLimit = vipLimit;
	}

	/**
	 * 获取属性：
	 *	章节目标总数
	 */
	public int getTotalAimNum(){
		return totalAimNum;
	}

	/**
	 * 设置属性：
	 *	章节目标总数
	 */
	public void setTotalAimNum(int totalAimNum){
		this.totalAimNum = totalAimNum;
	}

	/**
	 * 获取属性：
	 *	本章节已完成目标总数
	 */
	public int getComplateNum(){
		return complateNum;
	}

	/**
	 * 设置属性：
	 *	本章节已完成目标总数
	 */
	public void setComplateNum(int complateNum){
		this.complateNum = complateNum;
	}

	/**
	 * 获取属性：
	 *	章节奖励领取状态（-1未达成条件  0可领取  1已普通领取  2已vip领取  3vip+普通都已领取）
	 */
	public byte getReceiveType(){
		return receiveType;
	}

	/**
	 * 设置属性：
	 *	章节奖励领取状态（-1未达成条件  0可领取  1已普通领取  2已vip领取  3vip+普通都已领取）
	 */
	public void setReceiveType(byte receiveType){
		this.receiveType = receiveType;
	}

	/**
	 * 获取属性：
	 *	目标信息
	 */
	public PlayerAims[] getMesses(){
		return messes;
	}

	/**
	 * 设置属性：
	 *	目标信息
	 */
	public void setMesses(PlayerAims[] messes){
		this.messes = messes;
	}

}
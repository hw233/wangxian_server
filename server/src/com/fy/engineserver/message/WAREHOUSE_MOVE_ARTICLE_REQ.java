package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 物品移动，从普通背包到仓库，从防爆背包到仓库，从仓库到普通背包，从仓库到防爆背包，从仓库到仓库<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>password.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>password</td><td>String</td><td>password.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>knapsackIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cellIndexFrom</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cellIndexTo</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveCount</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class WAREHOUSE_MOVE_ARTICLE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String password;
	int moveType;
	int knapsackIndex;
	int cellIndexFrom;
	int cellIndexTo;
	int moveCount;

	public WAREHOUSE_MOVE_ARTICLE_REQ(){
	}

	public WAREHOUSE_MOVE_ARTICLE_REQ(long seqNum,String password,int moveType,int knapsackIndex,int cellIndexFrom,int cellIndexTo,int moveCount){
		this.seqNum = seqNum;
		this.password = password;
		this.moveType = moveType;
		this.knapsackIndex = knapsackIndex;
		this.cellIndexFrom = cellIndexFrom;
		this.cellIndexTo = cellIndexTo;
		this.moveCount = moveCount;
	}

	public WAREHOUSE_MOVE_ARTICLE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		password = new String(content,offset,len,"UTF-8");
		offset += len;
		moveType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		knapsackIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		cellIndexFrom = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		cellIndexTo = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		moveCount = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x0000B002;
	}

	public String getTypeDescription() {
		return "WAREHOUSE_MOVE_ARTICLE_REQ";
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
		try{
			len +=password.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
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
				try{
			 tmpBytes1 = password.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(moveType);
			buffer.putInt(knapsackIndex);
			buffer.putInt(cellIndexFrom);
			buffer.putInt(cellIndexTo);
			buffer.putInt(moveCount);
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
	 *	仓库密码
	 */
	public String getPassword(){
		return password;
	}

	/**
	 * 设置属性：
	 *	仓库密码
	 */
	public void setPassword(String password){
		this.password = password;
	}

	/**
	 * 获取属性：
	 *	物品移动方式，0从普通背包到仓库，1从防爆背包到仓库，2从仓库到普通背包，3从仓库到防爆背包，4从仓库到仓库, 5普通背包到2号仓库    6防爆包到2号仓库  7从2号仓库到普通背包  8从2号仓库到防爆包  9从2号仓库到2号仓库
	 */
	public int getMoveType(){
		return moveType;
	}

	/**
	 * 设置属性：
	 *	物品移动方式，0从普通背包到仓库，1从防爆背包到仓库，2从仓库到普通背包，3从仓库到防爆背包，4从仓库到仓库, 5普通背包到2号仓库    6防爆包到2号仓库  7从2号仓库到普通背包  8从2号仓库到防爆包  9从2号仓库到2号仓库
	 */
	public void setMoveType(int moveType){
		this.moveType = moveType;
	}

	/**
	 * 获取属性：
	 *	当移动方式不是仓库到仓库时，移动背包的索引，背包可以是普通背包可以是防爆背包
	 */
	public int getKnapsackIndex(){
		return knapsackIndex;
	}

	/**
	 * 设置属性：
	 *	当移动方式不是仓库到仓库时，移动背包的索引，背包可以是普通背包可以是防爆背包
	 */
	public void setKnapsackIndex(int knapsackIndex){
		this.knapsackIndex = knapsackIndex;
	}

	/**
	 * 获取属性：
	 *	对应的背包或仓库的下标
	 */
	public int getCellIndexFrom(){
		return cellIndexFrom;
	}

	/**
	 * 设置属性：
	 *	对应的背包或仓库的下标
	 */
	public void setCellIndexFrom(int cellIndexFrom){
		this.cellIndexFrom = cellIndexFrom;
	}

	/**
	 * 获取属性：
	 *	对应的背包或仓库的下标
	 */
	public int getCellIndexTo(){
		return cellIndexTo;
	}

	/**
	 * 设置属性：
	 *	对应的背包或仓库的下标
	 */
	public void setCellIndexTo(int cellIndexTo){
		this.cellIndexTo = cellIndexTo;
	}

	/**
	 * 获取属性：
	 *	移动个数
	 */
	public int getMoveCount(){
		return moveCount;
	}

	/**
	 * 设置属性：
	 *	移动个数
	 */
	public void setMoveCount(int moveCount){
		this.moveCount = moveCount;
	}

}
package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端请求服务器，宠物强化的各种信息，包括需要什么材料，需要几个，用什么来提高成功率，需要多少手续费等<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petEntityId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>descript.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>descript</td><td>String</td><td>descript.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongedUUB.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongedUUB</td><td>String</td><td>strongedUUB.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongMaterialName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongMaterialName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongMaterialName[0]</td><td>String</td><td>strongMaterialName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongMaterialName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongMaterialName[1]</td><td>String</td><td>strongMaterialName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongMaterialName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongMaterialName[2]</td><td>String</td><td>strongMaterialName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongMaterialLuck.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongMaterialLuck</td><td>int[]</td><td>strongMaterialLuck.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_PET_STRONG_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long petEntityId;
	String descript;
	String strongedUUB;
	String[] strongMaterialName;
	int[] strongMaterialLuck;

	public QUERY_PET_STRONG_RES(){
	}

	public QUERY_PET_STRONG_RES(long seqNum,long petEntityId,String descript,String strongedUUB,String[] strongMaterialName,int[] strongMaterialLuck){
		this.seqNum = seqNum;
		this.petEntityId = petEntityId;
		this.descript = descript;
		this.strongedUUB = strongedUUB;
		this.strongMaterialName = strongMaterialName;
		this.strongMaterialLuck = strongMaterialLuck;
	}

	public QUERY_PET_STRONG_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		petEntityId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		descript = new String(content,offset,len,"UTF-8");
		offset += len;
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
	}

	public int getType() {
		return 0x70F0EE10;
	}

	public String getTypeDescription() {
		return "QUERY_PET_STRONG_RES";
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
			len +=descript.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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

			buffer.putLong(petEntityId);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = descript.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
	 *	要强化的宠物道具id，玩家的背包中必须有此宠物道具id
	 */
	public long getPetEntityId(){
		return petEntityId;
	}

	/**
	 * 设置属性：
	 *	要强化的宠物道具id，玩家的背包中必须有此宠物道具id
	 */
	public void setPetEntityId(long petEntityId){
		this.petEntityId = petEntityId;
	}

	/**
	 * 获取属性：
	 *	提示（价格）
	 */
	public String getDescript(){
		return descript;
	}

	/**
	 * 设置属性：
	 *	提示（价格）
	 */
	public void setDescript(String descript){
		this.descript = descript;
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

}
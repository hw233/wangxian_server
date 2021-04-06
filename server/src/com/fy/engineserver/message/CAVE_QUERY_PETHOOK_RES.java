package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.homestead.cave.PetHookInfo;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查看宠物挂机拦信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>action</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>NPCId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ownerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petHookInfos.length</td><td>int</td><td>4个字节</td><td>PetHookInfo数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petHookInfos[0].petOwnerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petHookInfos[0].petOwnerName</td><td>String</td><td>petHookInfos[0].petOwnerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petHookInfos[0].petId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petHookInfos[0].articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petHookInfos[0].petGrade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petHookInfos[0].petName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petHookInfos[0].petName</td><td>String</td><td>petHookInfos[0].petName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petHookInfos[0].hookTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petHookInfos[0].exp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petHookInfos[1].petOwnerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petHookInfos[1].petOwnerName</td><td>String</td><td>petHookInfos[1].petOwnerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petHookInfos[1].petId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petHookInfos[1].articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petHookInfos[1].petGrade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petHookInfos[1].petName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petHookInfos[1].petName</td><td>String</td><td>petHookInfos[1].petName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petHookInfos[1].hookTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petHookInfos[1].exp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petHookInfos[2].petOwnerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petHookInfos[2].petOwnerName</td><td>String</td><td>petHookInfos[2].petOwnerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petHookInfos[2].petId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petHookInfos[2].articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petHookInfos[2].petGrade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petHookInfos[2].petName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petHookInfos[2].petName</td><td>String</td><td>petHookInfos[2].petName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petHookInfos[2].hookTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petHookInfos[2].exp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>indexs.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>indexs</td><td>int[]</td><td>indexs.length</td><td>*</td></tr>
 * </table>
 */
public class CAVE_QUERY_PETHOOK_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int maxNum;
	byte action;
	long NPCId;
	long ownerId;
	PetHookInfo[] petHookInfos;
	int[] indexs;

	public CAVE_QUERY_PETHOOK_RES(){
	}

	public CAVE_QUERY_PETHOOK_RES(long seqNum,int maxNum,byte action,long NPCId,long ownerId,PetHookInfo[] petHookInfos,int[] indexs){
		this.seqNum = seqNum;
		this.maxNum = maxNum;
		this.action = action;
		this.NPCId = NPCId;
		this.ownerId = ownerId;
		this.petHookInfos = petHookInfos;
		this.indexs = indexs;
	}

	public CAVE_QUERY_PETHOOK_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		maxNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		action = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		NPCId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		ownerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		petHookInfos = new PetHookInfo[len];
		for(int i = 0 ; i < petHookInfos.length ; i++){
			petHookInfos[i] = new PetHookInfo();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			petHookInfos[i].setPetOwnerName(new String(content,offset,len,"UTF-8"));
			offset += len;
			petHookInfos[i].setPetId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			petHookInfos[i].setArticleId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			petHookInfos[i].setPetGrade((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			petHookInfos[i].setPetName(new String(content,offset,len,"UTF-8"));
			offset += len;
			petHookInfos[i].setHookTime((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			petHookInfos[i].setExp((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		indexs = new int[len];
		for(int i = 0 ; i < indexs.length ; i++){
			indexs[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x8F000020;
	}

	public String getTypeDescription() {
		return "CAVE_QUERY_PETHOOK_RES";
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
		len += 1;
		len += 8;
		len += 8;
		len += 4;
		for(int i = 0 ; i < petHookInfos.length ; i++){
			len += 2;
			if(petHookInfos[i].getPetOwnerName() != null){
				try{
				len += petHookInfos[i].getPetOwnerName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 8;
			len += 8;
			len += 4;
			len += 2;
			if(petHookInfos[i].getPetName() != null){
				try{
				len += petHookInfos[i].getPetName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 8;
			len += 8;
		}
		len += 4;
		len += indexs.length * 4;
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

			buffer.putInt(maxNum);
			buffer.put(action);
			buffer.putLong(NPCId);
			buffer.putLong(ownerId);
			buffer.putInt(petHookInfos.length);
			for(int i = 0 ; i < petHookInfos.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = petHookInfos[i].getPetOwnerName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putLong(petHookInfos[i].getPetId());
				buffer.putLong(petHookInfos[i].getArticleId());
				buffer.putInt((int)petHookInfos[i].getPetGrade());
				try{
				tmpBytes2 = petHookInfos[i].getPetName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putLong(petHookInfos[i].getHookTime());
				buffer.putLong(petHookInfos[i].getExp());
			}
			buffer.putInt(indexs.length);
			for(int i = 0 ; i < indexs.length; i++){
				buffer.putInt(indexs[i]);
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
	 *	可存放的数量
	 */
	public int getMaxNum(){
		return maxNum;
	}

	/**
	 * 设置属性：
	 *	可存放的数量
	 */
	public void setMaxNum(int maxNum){
		this.maxNum = maxNum;
	}

	/**
	 * 获取属性：
	 *	0-存放{自己仙洞} 1-挂机{其他人仙洞}
	 */
	public byte getAction(){
		return action;
	}

	/**
	 * 设置属性：
	 *	0-存放{自己仙洞} 1-挂机{其他人仙洞}
	 */
	public void setAction(byte action){
		this.action = action;
	}

	/**
	 * 获取属性：
	 *	NPCId
	 */
	public long getNPCId(){
		return NPCId;
	}

	/**
	 * 设置属性：
	 *	NPCId
	 */
	public void setNPCId(long NPCId){
		this.NPCId = NPCId;
	}

	/**
	 * 获取属性：
	 *	主人ID
	 */
	public long getOwnerId(){
		return ownerId;
	}

	/**
	 * 设置属性：
	 *	主人ID
	 */
	public void setOwnerId(long ownerId){
		this.ownerId = ownerId;
	}

	/**
	 * 获取属性：
	 *	宠物挂机信息
	 */
	public PetHookInfo[] getPetHookInfos(){
		return petHookInfos;
	}

	/**
	 * 设置属性：
	 *	宠物挂机信息
	 */
	public void setPetHookInfos(PetHookInfo[] petHookInfos){
		this.petHookInfos = petHookInfos;
	}

	/**
	 * 获取属性：
	 *	位置信息
	 */
	public int[] getIndexs(){
		return indexs;
	}

	/**
	 * 设置属性：
	 *	位置信息
	 */
	public void setIndexs(int[] indexs){
		this.indexs = indexs;
	}

}
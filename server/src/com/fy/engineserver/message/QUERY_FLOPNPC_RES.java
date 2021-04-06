package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询掉落NPC<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>NPCId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>NPCId</td><td>long[]</td><td>NPCId.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>oneClassNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>oneClassNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>oneClassNames[0]</td><td>String</td><td>oneClassNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>oneClassNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>oneClassNames[1]</td><td>String</td><td>oneClassNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>oneClassNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>oneClassNames[2]</td><td>String</td><td>oneClassNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>colors.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>colors</td><td>int[]</td><td>colors.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_FLOPNPC_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] NPCId;
	String[] oneClassNames;
	int[] colors;

	public QUERY_FLOPNPC_RES(){
	}

	public QUERY_FLOPNPC_RES(long seqNum,long[] NPCId,String[] oneClassNames,int[] colors){
		this.seqNum = seqNum;
		this.NPCId = NPCId;
		this.oneClassNames = oneClassNames;
		this.colors = colors;
	}

	public QUERY_FLOPNPC_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		NPCId = new long[len];
		for(int i = 0 ; i < NPCId.length ; i++){
			NPCId[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		oneClassNames = new String[len];
		for(int i = 0 ; i < oneClassNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			oneClassNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		colors = new int[len];
		for(int i = 0 ; i < colors.length ; i++){
			colors[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x8F000129;
	}

	public String getTypeDescription() {
		return "QUERY_FLOPNPC_RES";
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
		len += NPCId.length * 8;
		len += 4;
		for(int i = 0 ; i < oneClassNames.length; i++){
			len += 2;
			try{
				len += oneClassNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += colors.length * 4;
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

			buffer.putInt(NPCId.length);
			for(int i = 0 ; i < NPCId.length; i++){
				buffer.putLong(NPCId[i]);
			}
			buffer.putInt(oneClassNames.length);
			for(int i = 0 ; i < oneClassNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = oneClassNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(colors.length);
			for(int i = 0 ; i < colors.length; i++){
				buffer.putInt(colors[i]);
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
	 *	无帮助说明
	 */
	public long[] getNPCId(){
		return NPCId;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setNPCId(long[] NPCId){
		this.NPCId = NPCId;
	}

	/**
	 * 获取属性：
	 *	掉落物品一级分类类名
	 */
	public String[] getOneClassNames(){
		return oneClassNames;
	}

	/**
	 * 设置属性：
	 *	掉落物品一级分类类名
	 */
	public void setOneClassNames(String[] oneClassNames){
		this.oneClassNames = oneClassNames;
	}

	/**
	 * 获取属性：
	 *	掉落物品颜色
	 */
	public int[] getColors(){
		return colors;
	}

	/**
	 * 设置属性：
	 *	掉落物品颜色
	 */
	public void setColors(int[] colors){
		this.colors = colors;
	}

}
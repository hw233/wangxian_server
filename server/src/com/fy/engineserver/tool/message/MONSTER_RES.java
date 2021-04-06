package com.fy.engineserver.tool.message;

import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.monster.Monster;
import com.xuanzhi.tools.transport.ResponseMessage;




/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求MONSTER数据<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsters.length</td><td>int</td><td>4个字节</td><td>Monster数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsters[0].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsters[0].avataRace</td><td>String</td><td>monsters[0].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsters[0].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsters[0].avataSex</td><td>String</td><td>monsters[0].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsters[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsters[0].name</td><td>String</td><td>monsters[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsters[0].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsters[0].title</td><td>String</td><td>monsters[0].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsters[0].spriteCategoryId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsters[0].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsters[1].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsters[1].avataRace</td><td>String</td><td>monsters[1].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsters[1].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsters[1].avataSex</td><td>String</td><td>monsters[1].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsters[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsters[1].name</td><td>String</td><td>monsters[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsters[1].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsters[1].title</td><td>String</td><td>monsters[1].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsters[1].spriteCategoryId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsters[1].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsters[2].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsters[2].avataRace</td><td>String</td><td>monsters[2].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsters[2].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsters[2].avataSex</td><td>String</td><td>monsters[2].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsters[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsters[2].name</td><td>String</td><td>monsters[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsters[2].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsters[2].title</td><td>String</td><td>monsters[2].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsters[2].spriteCategoryId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsters[2].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class MONSTER_RES implements ResponseMessage{

	static DefaultMessageFactory mf = DefaultMessageFactory.getInstance();

	long seqNum;
	Monster[] monsters;

	public MONSTER_RES(long seqNum,Monster[] monsters){
		this.seqNum = seqNum;
		this.monsters = monsters;
	}

	public MONSTER_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		monsters = new Monster[len];
		for(int i = 0 ; i < monsters.length ; i++){
			monsters[i] = new Monster();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			monsters[i].setAvataRace(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			monsters[i].setAvataSex(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			monsters[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			monsters[i].setTitle(new String(content,offset,len,"UTF-8"));
			offset += len;
			monsters[i].setSpriteCategoryId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			monsters[i].setLevel((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
		}
	}

	public int getType() {
		return 0x80000002;
	}

	public String getTypeDescription() {
		return "MONSTER_RES";
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
		for(int i = 0 ; i < monsters.length ; i++){
			len += 2;
			if(monsters[i].getAvataRace() != null){
				try{
				len += monsters[i].getAvataRace().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(monsters[i].getAvataSex() != null){
				try{
				len += monsters[i].getAvataSex().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(monsters[i].getName() != null){
				try{
				len += monsters[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(monsters[i].getTitle() != null){
				try{
				len += monsters[i].getTitle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 2;
		}
		packet_length = len;
		return len;
	}

	public int writeTo(ByteBuffer buffer) {
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.putInt(monsters.length);
			for(int i = 0 ; i < monsters.length ; i++){
				byte[] tmpBytes2 = monsters[i].getAvataRace().getBytes("UTF-8");
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				tmpBytes2 = monsters[i].getAvataSex().getBytes("UTF-8");
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				tmpBytes2 = monsters[i].getName().getBytes("UTF-8");
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				tmpBytes2 = monsters[i].getTitle().getBytes("UTF-8");
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)monsters[i].getSpriteCategoryId());
				buffer.putShort((short)monsters[i].getLevel());
			}
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public Monster[] getMonsters(){
		return monsters;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setMonsters(Monster[] monsters){
		this.monsters = monsters;
	}

}

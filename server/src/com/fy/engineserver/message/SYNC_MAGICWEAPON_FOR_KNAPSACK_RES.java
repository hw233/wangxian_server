package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.datasource.article.data.magicweapon.operate.MoreMagicWeaponMess;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 同步背包中法宝信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes.length</td><td>int</td><td>4个字节</td><td>MoreMagicWeaponMess数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].shenShi</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].basicpropertyname.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].basicpropertyname</td><td>String</td><td>messes[0].basicpropertyname.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].mstar</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].mess3.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].mess3</td><td>String</td><td>messes[0].mess3.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].mess4.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].mess4</td><td>String</td><td>messes[0].mess4.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].shenShi</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].basicpropertyname.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].basicpropertyname</td><td>String</td><td>messes[1].basicpropertyname.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].mstar</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].mess3.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].mess3</td><td>String</td><td>messes[1].mess3.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].mess4.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].mess4</td><td>String</td><td>messes[1].mess4.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].shenShi</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].basicpropertyname.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].basicpropertyname</td><td>String</td><td>messes[2].basicpropertyname.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].mstar</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].mess3.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].mess3</td><td>String</td><td>messes[2].mess3.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].mess4.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].mess4</td><td>String</td><td>messes[2].mess4.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class SYNC_MAGICWEAPON_FOR_KNAPSACK_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	MoreMagicWeaponMess[] messes;

	public SYNC_MAGICWEAPON_FOR_KNAPSACK_RES(){
	}

	public SYNC_MAGICWEAPON_FOR_KNAPSACK_RES(long seqNum,MoreMagicWeaponMess[] messes){
		this.seqNum = seqNum;
		this.messes = messes;
	}

	public SYNC_MAGICWEAPON_FOR_KNAPSACK_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		messes = new MoreMagicWeaponMess[len];
		for(int i = 0 ; i < messes.length ; i++){
			messes[i] = new MoreMagicWeaponMess();
			messes[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			messes[i].setShenShi(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			messes[i].setBasicpropertyname(new String(content,offset,len,"UTF-8"));
			offset += len;
			messes[i].setMstar((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			messes[i].setMess3(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			messes[i].setMess4(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
	}

	public int getType() {
		return 0x70F0EF02;
	}

	public String getTypeDescription() {
		return "SYNC_MAGICWEAPON_FOR_KNAPSACK_RES";
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
		for(int i = 0 ; i < messes.length ; i++){
			len += 8;
			len += 1;
			len += 2;
			if(messes[i].getBasicpropertyname() != null){
				try{
				len += messes[i].getBasicpropertyname().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 2;
			if(messes[i].getMess3() != null){
				try{
				len += messes[i].getMess3().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(messes[i].getMess4() != null){
				try{
				len += messes[i].getMess4().getBytes("UTF-8").length;
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

			buffer.putInt(messes.length);
			for(int i = 0 ; i < messes.length ; i++){
				buffer.putLong(messes[i].getId());
				buffer.put((byte)(messes[i].isShenShi()==false?0:1));
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = messes[i].getBasicpropertyname().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)messes[i].getMstar());
				try{
				tmpBytes2 = messes[i].getMess3().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = messes[i].getMess4().getBytes("UTF-8");
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
	 *	法宝信息
	 */
	public MoreMagicWeaponMess[] getMesses(){
		return messes;
	}

	/**
	 * 设置属性：
	 *	法宝信息
	 */
	public void setMesses(MoreMagicWeaponMess[] messes){
		this.messes = messes;
	}

}
package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.datasource.article.data.magicweapon.model.TunShiModle;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 法宝吞噬确认<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes.length</td><td>int</td><td>4个字节</td><td>TunShiModle数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].startNums</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[0].endNums</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[0].upgradEexps</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].startNums</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[1].endNums</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[1].upgradEexps</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].startNums</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messes[2].endNums</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messes[2].upgradEexps</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class CONFIRM_MAGICWEAPON_EAT_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	boolean result;
	TunShiModle[] messes;

	public CONFIRM_MAGICWEAPON_EAT_RES(){
	}

	public CONFIRM_MAGICWEAPON_EAT_RES(long seqNum,boolean result,TunShiModle[] messes){
		this.seqNum = seqNum;
		this.result = result;
		this.messes = messes;
	}

	public CONFIRM_MAGICWEAPON_EAT_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		result = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		messes = new TunShiModle[len];
		for(int i = 0 ; i < messes.length ; i++){
			messes[i] = new TunShiModle();
			messes[i].setStartNums((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			messes[i].setEndNums((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			messes[i].setUpgradEexps((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
		}
	}

	public int getType() {
		return 0x70F0EF17;
	}

	public String getTypeDescription() {
		return "CONFIRM_MAGICWEAPON_EAT_RES";
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
		len += 1;
		len += 4;
		for(int i = 0 ; i < messes.length ; i++){
			len += 8;
			len += 8;
			len += 8;
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

			buffer.put((byte)(result==false?0:1));
			buffer.putInt(messes.length);
			for(int i = 0 ; i < messes.length ; i++){
				buffer.putLong(messes[i].getStartNums());
				buffer.putLong(messes[i].getEndNums());
				buffer.putLong(messes[i].getUpgradEexps());
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
	 *	结果
	 */
	public boolean getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	结果
	 */
	public void setResult(boolean result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	动画信息
	 */
	public TunShiModle[] getMesses(){
		return messes;
	}

	/**
	 * 设置属性：
	 *	动画信息
	 */
	public void setMesses(TunShiModle[] messes){
		this.messes = messes;
	}

}
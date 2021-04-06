package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bookName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bookName</td><td>String</td><td>bookName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>baseNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>baseNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>baseNames[0]</td><td>String</td><td>baseNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>baseNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>baseNames[1]</td><td>String</td><td>baseNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>baseNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>baseNames[2]</td><td>String</td><td>baseNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentNames[0]</td><td>String</td><td>talentNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentNames[1]</td><td>String</td><td>talentNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentNames[2]</td><td>String</td><td>talentNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>holeNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>holeNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>holeNames[0]</td><td>String</td><td>holeNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>holeNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>holeNames[1]</td><td>String</td><td>holeNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>holeNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>holeNames[2]</td><td>String</td><td>holeNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allTakeSkillBookNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allTakeSkillBookNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allTakeSkillBookNames[0]</td><td>String</td><td>allTakeSkillBookNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allTakeSkillBookNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allTakeSkillBookNames[1]</td><td>String</td><td>allTakeSkillBookNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allTakeSkillBookNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allTakeSkillBookNames[2]</td><td>String</td><td>allTakeSkillBookNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lockSkillNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lockSkillNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lockSkillNames[0]</td><td>String</td><td>lockSkillNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lockSkillNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lockSkillNames[1]</td><td>String</td><td>lockSkillNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lockSkillNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lockSkillNames[2]</td><td>String</td><td>lockSkillNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>openSlotNeedBooks.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>openSlotNeedBooks</td><td>int[]</td><td>openSlotNeedBooks.length</td><td>*</td></tr>
 * </table>
 */
public class PET_BOOK_UI_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String bookName;
	String[] baseNames;
	String[] talentNames;
	String[] holeNames;
	String[] allTakeSkillBookNames;
	String[] lockSkillNames;
	int[] openSlotNeedBooks;

	public PET_BOOK_UI_RES(){
	}

	public PET_BOOK_UI_RES(long seqNum,String bookName,String[] baseNames,String[] talentNames,String[] holeNames,String[] allTakeSkillBookNames,String[] lockSkillNames,int[] openSlotNeedBooks){
		this.seqNum = seqNum;
		this.bookName = bookName;
		this.baseNames = baseNames;
		this.talentNames = talentNames;
		this.holeNames = holeNames;
		this.allTakeSkillBookNames = allTakeSkillBookNames;
		this.lockSkillNames = lockSkillNames;
		this.openSlotNeedBooks = openSlotNeedBooks;
	}

	public PET_BOOK_UI_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		bookName = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		baseNames = new String[len];
		for(int i = 0 ; i < baseNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			baseNames[i] = new String(content,offset,len);
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		talentNames = new String[len];
		for(int i = 0 ; i < talentNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			talentNames[i] = new String(content,offset,len);
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		holeNames = new String[len];
		for(int i = 0 ; i < holeNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			holeNames[i] = new String(content,offset,len);
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		allTakeSkillBookNames = new String[len];
		for(int i = 0 ; i < allTakeSkillBookNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			allTakeSkillBookNames[i] = new String(content,offset,len);
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		lockSkillNames = new String[len];
		for(int i = 0 ; i < lockSkillNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			lockSkillNames[i] = new String(content,offset,len);
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		openSlotNeedBooks = new int[len];
		for(int i = 0 ; i < openSlotNeedBooks.length ; i++){
			openSlotNeedBooks[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x8E0EAA59;
	}

	public String getTypeDescription() {
		return "PET_BOOK_UI_RES";
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
		len +=bookName.getBytes().length;
		len += 4;
		for(int i = 0 ; i < baseNames.length; i++){
			len += 2;
			len += baseNames[i].getBytes().length;
		}
		len += 4;
		for(int i = 0 ; i < talentNames.length; i++){
			len += 2;
			len += talentNames[i].getBytes().length;
		}
		len += 4;
		for(int i = 0 ; i < holeNames.length; i++){
			len += 2;
			len += holeNames[i].getBytes().length;
		}
		len += 4;
		for(int i = 0 ; i < allTakeSkillBookNames.length; i++){
			len += 2;
			len += allTakeSkillBookNames[i].getBytes().length;
		}
		len += 4;
		for(int i = 0 ; i < lockSkillNames.length; i++){
			len += 2;
			len += lockSkillNames[i].getBytes().length;
		}
		len += 4;
		len += openSlotNeedBooks.length * 4;
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
			tmpBytes1 = bookName.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(baseNames.length);
			for(int i = 0 ; i < baseNames.length; i++){
				byte[] tmpBytes2 ;
				tmpBytes2 = baseNames[i].getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(talentNames.length);
			for(int i = 0 ; i < talentNames.length; i++){
				byte[] tmpBytes2 ;
				tmpBytes2 = talentNames[i].getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(holeNames.length);
			for(int i = 0 ; i < holeNames.length; i++){
				byte[] tmpBytes2 ;
				tmpBytes2 = holeNames[i].getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(allTakeSkillBookNames.length);
			for(int i = 0 ; i < allTakeSkillBookNames.length; i++){
				byte[] tmpBytes2 ;
				tmpBytes2 = allTakeSkillBookNames[i].getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(lockSkillNames.length);
			for(int i = 0 ; i < lockSkillNames.length; i++){
				byte[] tmpBytes2 ;
				tmpBytes2 = lockSkillNames[i].getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(openSlotNeedBooks.length);
			for(int i = 0 ; i < openSlotNeedBooks.length; i++){
				buffer.putInt(openSlotNeedBooks[i]);
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
	 *	技能书名称
	 */
	public String getBookName(){
		return bookName;
	}

	/**
	 * 设置属性：
	 *	技能书名称
	 */
	public void setBookName(String bookName){
		this.bookName = bookName;
	}

	/**
	 * 获取属性：
	 *	基础技能书名称
	 */
	public String[] getBaseNames(){
		return baseNames;
	}

	/**
	 * 设置属性：
	 *	基础技能书名称
	 */
	public void setBaseNames(String[] baseNames){
		this.baseNames = baseNames;
	}

	/**
	 * 获取属性：
	 *	天赋技能书名称
	 */
	public String[] getTalentNames(){
		return talentNames;
	}

	/**
	 * 设置属性：
	 *	天赋技能书名称
	 */
	public void setTalentNames(String[] talentNames){
		this.talentNames = talentNames;
	}

	/**
	 * 获取属性：
	 *	开孔技能书名称
	 */
	public String[] getHoleNames(){
		return holeNames;
	}

	/**
	 * 设置属性：
	 *	开孔技能书名称
	 */
	public void setHoleNames(String[] holeNames){
		this.holeNames = holeNames;
	}

	/**
	 * 获取属性：
	 *	所有技能抽取符名称
	 */
	public String[] getAllTakeSkillBookNames(){
		return allTakeSkillBookNames;
	}

	/**
	 * 设置属性：
	 *	所有技能抽取符名称
	 */
	public void setAllTakeSkillBookNames(String[] allTakeSkillBookNames){
		this.allTakeSkillBookNames = allTakeSkillBookNames;
	}

	/**
	 * 获取属性：
	 *	技能锁定符名称
	 */
	public String[] getLockSkillNames(){
		return lockSkillNames;
	}

	/**
	 * 设置属性：
	 *	技能锁定符名称
	 */
	public void setLockSkillNames(String[] lockSkillNames){
		this.lockSkillNames = lockSkillNames;
	}

	/**
	 * 获取属性：
	 *	开孔天赋需要技能书个数
	 */
	public int[] getOpenSlotNeedBooks(){
		return openSlotNeedBooks;
	}

	/**
	 * 设置属性：
	 *	开孔天赋需要技能书个数
	 */
	public void setOpenSlotNeedBooks(int[] openSlotNeedBooks){
		this.openSlotNeedBooks = openSlotNeedBooks;
	}

}
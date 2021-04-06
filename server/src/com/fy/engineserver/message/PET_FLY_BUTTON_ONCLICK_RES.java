package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetFlySkillInfo;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 点击宠物飞升按钮<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>flyAvata.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>flyAvata</td><td>String</td><td>flyAvata.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>animation</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buttonType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lingXingPoint</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xiaoHuaDate</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>canUseTimes</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo</td><td>String</td><td>skillInfo.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.name</td><td>String</td><td>pet.name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.breedTimes</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.breededTimes</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.identity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.rarity</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.maxLifeTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.lifeTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.wuXing</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos.skillId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos.name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos.name</td><td>String</td><td>infos.name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos.skillDesc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos.skillDesc</td><td>String</td><td>infos.skillDesc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>infos.icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>infos.icon</td><td>String</td><td>infos.icon.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class PET_FLY_BUTTON_ONCLICK_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String flyAvata;
	int animation;
	int buttonType;
	int lingXingPoint;
	long xiaoHuaDate;
	int canUseTimes;
	String skillInfo;
	Pet pet;
	PetFlySkillInfo infos;

	public PET_FLY_BUTTON_ONCLICK_RES(){
	}

	public PET_FLY_BUTTON_ONCLICK_RES(long seqNum,String flyAvata,int animation,int buttonType,int lingXingPoint,long xiaoHuaDate,int canUseTimes,String skillInfo,Pet pet,PetFlySkillInfo infos){
		this.seqNum = seqNum;
		this.flyAvata = flyAvata;
		this.animation = animation;
		this.buttonType = buttonType;
		this.lingXingPoint = lingXingPoint;
		this.xiaoHuaDate = xiaoHuaDate;
		this.canUseTimes = canUseTimes;
		this.skillInfo = skillInfo;
		this.pet = pet;
		this.infos = infos;
	}

	public PET_FLY_BUTTON_ONCLICK_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		flyAvata = new String(content,offset,len,"UTF-8");
		offset += len;
		animation = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		buttonType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		lingXingPoint = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		xiaoHuaDate = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		canUseTimes = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		skillInfo = new String(content,offset,len,"UTF-8");
		offset += len;
		pet = new Pet();
		pet.setId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		pet.setName(new String(content,offset,len,"UTF-8"));
		offset += len;
		pet.setBreedTimes((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		pet.setBreededTimes((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		pet.setIdentity(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		pet.setRarity((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		pet.setMaxLifeTime((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setLifeTime((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setWuXing((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setGrade((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		infos = new PetFlySkillInfo();
		infos.setSkillId((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		infos.setName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		infos.setSkillDesc(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		infos.setIcon(new String(content,offset,len,"UTF-8"));
		offset += len;
	}

	public int getType() {
		return 0x70FF0116;
	}

	public String getTypeDescription() {
		return "PET_FLY_BUTTON_ONCLICK_RES";
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
			len +=flyAvata.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		len += 8;
		len += 4;
		len += 2;
		try{
			len +=skillInfo.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 2;
		if(pet.getName() != null){
			try{
			len += pet.getName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 1;
		len += 1;
		len += 1;
		len += 1;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 2;
		if(infos.getName() != null){
			try{
			len += infos.getName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(infos.getSkillDesc() != null){
			try{
			len += infos.getSkillDesc().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(infos.getIcon() != null){
			try{
			len += infos.getIcon().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = flyAvata.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(animation);
			buffer.putInt(buttonType);
			buffer.putInt(lingXingPoint);
			buffer.putLong(xiaoHuaDate);
			buffer.putInt(canUseTimes);
				try{
			tmpBytes1 = skillInfo.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(pet.getId());
				try{
				tmpBytes1 = pet.getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put((byte)pet.getBreedTimes());
			buffer.put((byte)pet.getBreededTimes());
			buffer.put((byte)(pet.isIdentity()==false?0:1));
			buffer.put((byte)pet.getRarity());
			buffer.putInt((int)pet.getMaxLifeTime());
			buffer.putInt((int)pet.getLifeTime());
			buffer.putInt((int)pet.getWuXing());
			buffer.putInt((int)pet.getGrade());
			buffer.putInt((int)infos.getSkillId());
				try{
				tmpBytes1 = infos.getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = infos.getSkillDesc().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = infos.getIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
	 *	飞升avata
	 */
	public String getFlyAvata(){
		return flyAvata;
	}

	/**
	 * 设置属性：
	 *	飞升avata
	 */
	public void setFlyAvata(String flyAvata){
		this.flyAvata = flyAvata;
	}

	/**
	 * 获取属性：
	 *	0:默认不播放1:播放动画
	 */
	public int getAnimation(){
		return animation;
	}

	/**
	 * 设置属性：
	 *	0:默认不播放1:播放动画
	 */
	public void setAnimation(int animation){
		this.animation = animation;
	}

	/**
	 * 获取属性：
	 *	0:飞升按钮1:刷新技能
	 */
	public int getButtonType(){
		return buttonType;
	}

	/**
	 * 设置属性：
	 *	0:飞升按钮1:刷新技能
	 */
	public void setButtonType(int buttonType){
		this.buttonType = buttonType;
	}

	/**
	 * 获取属性：
	 *	灵性
	 */
	public int getLingXingPoint(){
		return lingXingPoint;
	}

	/**
	 * 设置属性：
	 *	灵性
	 */
	public void setLingXingPoint(int lingXingPoint){
		this.lingXingPoint = lingXingPoint;
	}

	/**
	 * 获取属性：
	 *	消化时间
	 */
	public long getXiaoHuaDate(){
		return xiaoHuaDate;
	}

	/**
	 * 设置属性：
	 *	消化时间
	 */
	public void setXiaoHuaDate(long xiaoHuaDate){
		this.xiaoHuaDate = xiaoHuaDate;
	}

	/**
	 * 获取属性：
	 *	升华露可使用次数
	 */
	public int getCanUseTimes(){
		return canUseTimes;
	}

	/**
	 * 设置属性：
	 *	升华露可使用次数
	 */
	public void setCanUseTimes(int canUseTimes){
		this.canUseTimes = canUseTimes;
	}

	/**
	 * 获取属性：
	 *	技能描述
	 */
	public String getSkillInfo(){
		return skillInfo;
	}

	/**
	 * 设置属性：
	 *	技能描述
	 */
	public void setSkillInfo(String skillInfo){
		this.skillInfo = skillInfo;
	}

	/**
	 * 获取属性：
	 *	返回宠物实体
	 */
	public Pet getPet(){
		return pet;
	}

	/**
	 * 设置属性：
	 *	返回宠物实体
	 */
	public void setPet(Pet pet){
		this.pet = pet;
	}

	/**
	 * 获取属性：
	 *	技能信息
	 */
	public PetFlySkillInfo getInfos(){
		return infos;
	}

	/**
	 * 设置属性：
	 *	技能信息
	 */
	public void setInfos(PetFlySkillInfo infos){
		this.infos = infos;
	}

}
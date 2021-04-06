package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.AbstractMessageFactory;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.Message;
import com.xuanzhi.tools.transport.MessageFormatErrorException;


/**
 * 消息工厂类，此工厂类是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 各数据包的定义如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>请求消息</td><td>类型</td><td>响应消息</td><td>类型</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_PLAYER_SAVE_REQ.html'>CACHE_PLAYER_SAVE_REQ</a></td><td>0x00000001</td><td><a href='./CACHE_PLAYER_SAVE_RES.html'>CACHE_PLAYER_SAVE_RES</a></td><td>0x80000001</td><td>保存一个玩家</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CACHE_PLAYER_GET_REQ.html'>CACHE_PLAYER_GET_REQ</a></td><td>0x00000002</td><td><a href='./CACHE_PLAYER_GET_RES.html'>CACHE_PLAYER_GET_RES</a></td><td>0x80000002</td><td>获取玩家消息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_PLAYER_UPDATE_REQ.html'>CACHE_PLAYER_UPDATE_REQ</a></td><td>0x00000003</td><td><a href='./CACHE_PLAYER_UPDATE_RES.html'>CACHE_PLAYER_UPDATE_RES</a></td><td>0x80000003</td><td>更新一个玩家</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CACHE_PLAYER_DELETE_REQ.html'>CACHE_PLAYER_DELETE_REQ</a></td><td>0x00000004</td><td><a href='./CACHE_PLAYER_DELETE_RES.html'>CACHE_PLAYER_DELETE_RES</a></td><td>0x80000004</td><td>删除一个玩家</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_PLAYERS_GET_REQ.html'>CACHE_PLAYERS_GET_REQ</a></td><td>0x00000005</td><td><a href='./CACHE_PLAYERS_GET_RES.html'>CACHE_PLAYERS_GET_RES</a></td><td>0x80000005</td><td>通过用户名获得所有角色</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CACHE_PLAYER_COUNT_REQ.html'>CACHE_PLAYER_COUNT_REQ</a></td><td>0x00000007</td><td><a href='./CACHE_PLAYER_COUNT_RES.html'>CACHE_PLAYER_COUNT_RES</a></td><td>0x80000007</td><td>获取角色数量</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_PLAYER_COUNT_USERNAME_REQ.html'>CACHE_PLAYER_COUNT_USERNAME_REQ</a></td><td>0x00000008</td><td><a href='./CACHE_PLAYER_COUNT_USERNAME_RES.html'>CACHE_PLAYER_COUNT_USERNAME_RES</a></td><td>0x80000008</td><td>获取用户角色数量</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CACHE_PLAYER_LIST_REQ.html'>CACHE_PLAYER_LIST_REQ</a></td><td>0x00000009</td><td><a href='./CACHE_PLAYER_LIST_RES.html'>CACHE_PLAYER_LIST_RES</a></td><td>0x80000009</td><td>获取用户角色列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_PLAYER_LIST_USERNAME_REQ.html'>CACHE_PLAYER_LIST_USERNAME_REQ</a></td><td>0x00000010</td><td><a href='./CACHE_PLAYER_LIST_USERNAME_RES.html'>CACHE_PLAYER_LIST_USERNAME_RES</a></td><td>0x80000010</td><td>获取用户角色料表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CACHE_ARTICLEENTITY_SAVE_REQ.html'>CACHE_ARTICLEENTITY_SAVE_REQ</a></td><td>0x00000011</td><td><a href='./CACHE_ARTICLEENTITY_SAVE_RES.html'>CACHE_ARTICLEENTITY_SAVE_RES</a></td><td>0x80000011</td><td>保存一个物品实体</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_EQUIPMENTENTITY_SAVE_REQ.html'>CACHE_EQUIPMENTENTITY_SAVE_REQ</a></td><td>0x00000012</td><td><a href='./CACHE_EQUIPMENTENTITY_SAVE_RES.html'>CACHE_EQUIPMENTENTITY_SAVE_RES</a></td><td>0x80000012</td><td>保存一个装备实体</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CACHE_PROPSENTITY_SAVE_REQ.html'>CACHE_PROPSENTITY_SAVE_REQ</a></td><td>0x00000013</td><td><a href='./CACHE_PROPSENTITY_SAVE_RES.html'>CACHE_PROPSENTITY_SAVE_RES</a></td><td>0x80000013</td><td>保存一个物品实体</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_ARTICLEENTITY_UPDATE_REQ.html'>CACHE_ARTICLEENTITY_UPDATE_REQ</a></td><td>0x00000014</td><td><a href='./CACHE_ARTICLEENTITY_UPDATE_RES.html'>CACHE_ARTICLEENTITY_UPDATE_RES</a></td><td>0x80000014</td><td>保存一个物品实体</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CACHE_EQUIPMENTENTITY_UPDATE_REQ.html'>CACHE_EQUIPMENTENTITY_UPDATE_REQ</a></td><td>0x00000015</td><td><a href='./CACHE_EQUIPMENTENTITY_UPDATE_RES.html'>CACHE_EQUIPMENTENTITY_UPDATE_RES</a></td><td>0x80000015</td><td>保存一个装备实体</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_PROPSENTITY_UPDATE_REQ.html'>CACHE_PROPSENTITY_UPDATE_REQ</a></td><td>0x00000016</td><td><a href='./CACHE_PROPSENTITY_UPDATE_RES.html'>CACHE_PROPSENTITY_UPDATE_RES</a></td><td>0x80000016</td><td>保存一个物品实体</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CACHE_ARTICLEENTITY_COUNT_REQ.html'>CACHE_ARTICLEENTITY_COUNT_REQ</a></td><td>0x00000018</td><td><a href='./CACHE_ARTICLEENTITY_COUNT_RES.html'>CACHE_ARTICLEENTITY_COUNT_RES</a></td><td>0x80000018</td><td>获取角色数量</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_ARTICLEENTITY_LIST_REQ.html'>CACHE_ARTICLEENTITY_LIST_REQ</a></td><td>0x00000019</td><td><a href='./CACHE_ARTICLEENTITY_LIST_RES.html'>CACHE_ARTICLEENTITY_LIST_RES</a></td><td>0x80000019</td><td>获取用户角色列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CACHE_ARTICLEENTITY_DELETE_REQ.html'>CACHE_ARTICLEENTITY_DELETE_REQ</a></td><td>0x00000020</td><td><a href='./CACHE_ARTICLEENTITY_DELETE_RES.html'>CACHE_ARTICLEENTITY_DELETE_RES</a></td><td>0x80000202</td><td>删除一个玩家</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_ARTICLEENTITY_GET_REQ.html'>CACHE_ARTICLEENTITY_GET_REQ</a></td><td>0x00000021</td><td><a href='./CACHE_ARTICLEENTITY_GET_RES.html'>CACHE_ARTICLEENTITY_GET_RES</a></td><td>0x80000021</td><td>获取一个物品</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CACHE_ARTICLEENTITY_GET_BYNAME_REQ.html'>CACHE_ARTICLEENTITY_GET_BYNAME_REQ</a></td><td>0x00000022</td><td><a href='./CACHE_ARTICLEENTITY_GET_BYNAME_RES.html'>CACHE_ARTICLEENTITY_GET_BYNAME_RES</a></td><td>0x80000022</td><td>获取一个物品</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_GOODS_SAVE_REQ.html'>CACHE_GOODS_SAVE_REQ</a></td><td>0x00000023</td><td><a href='./CACHE_GOODS_SAVE_RES.html'>CACHE_GOODS_SAVE_RES</a></td><td>0x80000023</td><td>保存一个商品</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CACHE_GOODS_UPDATE_REQ.html'>CACHE_GOODS_UPDATE_REQ</a></td><td>0x00000024</td><td><a href='./CACHE_GOODS_UPDATE_RES.html'>CACHE_GOODS_UPDATE_RES</a></td><td>0x80000024</td><td>更新一个仓库</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_GOODS_GET_REQ.html'>CACHE_GOODS_GET_REQ</a></td><td>0x00000025</td><td><a href='./CACHE_GOODS_GET_RES.html'>CACHE_GOODS_GET_RES</a></td><td>0x80000025</td><td>获取一个物品</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CACHE_GOODS_COUNT_REQ.html'>CACHE_GOODS_COUNT_REQ</a></td><td>0x00000026</td><td><a href='./CACHE_GOODS_COUNT_RES.html'>CACHE_GOODS_COUNT_RES</a></td><td>0x80000026</td><td>获取角色数量</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_GOODS_LIST_REQ.html'>CACHE_GOODS_LIST_REQ</a></td><td>0x00000027</td><td><a href='./CACHE_GOODS_LIST_RES.html'>CACHE_GOODS_LIST_RES</a></td><td>0x80000027</td><td>获取列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CACHE_GOODS_DELETE_REQ.html'>CACHE_GOODS_DELETE_REQ</a></td><td>0x00000028</td><td><a href='./CACHE_GOODS_DELETE_RES.html'>CACHE_GOODS_DELETE_RES</a></td><td>0x80000028</td><td>删除一个物品</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_PLAYER_RANDOM_GET_REQ.html'>CACHE_PLAYER_RANDOM_GET_REQ</a></td><td>0x00000029</td><td><a href='./CACHE_PLAYER_RANDOM_GET_RES.html'>CACHE_PLAYER_RANDOM_GET_RES</a></td><td>0x80000029</td><td>随机获取玩家</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_MAX_ARTICLE_ID_REQ.html'>GET_MAX_ARTICLE_ID_REQ</a></td><td>0x00000030</td><td><a href='./GET_MAX_ARTICLE_ID_RES.html'>GET_MAX_ARTICLE_ID_RES</a></td><td>0x80000030</td><td>获得数据库中最大的article id</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_LEVEL_PLAYER_LIST_REQ.html'>CACHE_LEVEL_PLAYER_LIST_REQ</a></td><td>0x00000031</td><td><a href='./CACHE_LEVEL_PLAYER_LIST_RES.html'>CACHE_LEVEL_PLAYER_LIST_RES</a></td><td>0x80000031</td><td>获取用户角色列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CACHE_LEVEL_PLAYER_COUNT_REQ.html'>CACHE_LEVEL_PLAYER_COUNT_REQ</a></td><td>0x00000032</td><td><a href='./CACHE_LEVEL_PLAYER_COUNT_RES.html'>CACHE_LEVEL_PLAYER_COUNT_RES</a></td><td>0x80000032</td><td>获取角色数量</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_MAGICWEAPON_SAVE_REQ.html'>CACHE_MAGICWEAPON_SAVE_REQ</a></td><td>0x00000033</td><td><a href='./CACHE_MAGICWEAPON_SAVE_RES.html'>CACHE_MAGICWEAPON_SAVE_RES</a></td><td>0x80000033</td><td>保存一个法宝</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CACHE_MAGICWEAPON_GET_REQ.html'>CACHE_MAGICWEAPON_GET_REQ</a></td><td>0x00000034</td><td><a href='./CACHE_MAGICWEAPON_GET_RES.html'>CACHE_MAGICWEAPON_GET_RES</a></td><td>0x80000034</td><td>获取法宝消息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CACHE_MAGICWEAPON_UPDATE_REQ.html'>CACHE_MAGICWEAPON_UPDATE_REQ</a></td><td>0x00000035</td><td><a href='./CACHE_MAGICWEAPON_UPDATE_RES.html'>CACHE_MAGICWEAPON_UPDATE_RES</a></td><td>0x80000035</td><td>更新一个法宝</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CACHE_MAGICWEAPONS_GET_REQ.html'>CACHE_MAGICWEAPONS_GET_REQ</a></td><td>0x00000036</td><td><a href='./CACHE_MAGICWEAPONS_GET_RES.html'>CACHE_MAGICWEAPONS_GET_RES</a></td><td>0x80000036</td><td>通过角色获取法宝</td></tr>
 * </table>
 */
public class CacheSystemMessageFactory extends AbstractMessageFactory {

	private static long sequnceNum = 0L;
	public synchronized static long nextSequnceNum(){
		sequnceNum ++;
		if(sequnceNum >= 0x7FFFFFFF){
			sequnceNum = 1L;
		}
		return sequnceNum;
	}

	protected static CacheSystemMessageFactory self;

	public static CacheSystemMessageFactory getInstance(){
		if(self != null){
			return self;
		}
		synchronized(CacheSystemMessageFactory.class){
			if(self != null) return self;
			self = new CacheSystemMessageFactory();
		}
		return self;
	}

	public Message newMessage(byte[] messageContent,int offset,int size)
		throws MessageFormatErrorException, ConnectionException {
		int len = (int)byteArrayToNumber(messageContent,offset,getNumOfByteForMessageLength());
		if(len != size)
			throw new MessageFormatErrorException("message length not match");
		int end = offset + size;
		offset += getNumOfByteForMessageLength();
		long type = byteArrayToNumber(messageContent,offset,4);
		offset += 4;
		long sn = byteArrayToNumber(messageContent,offset,4);
		offset += 4;

		try{
			if(type == 0x00000001L){
				try {
					return new CACHE_PLAYER_SAVE_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_SAVE_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000001L){
				try {
					return new CACHE_PLAYER_SAVE_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_SAVE_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000002L){
				try {
					return new CACHE_PLAYER_GET_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_GET_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000002L){
				try {
					return new CACHE_PLAYER_GET_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_GET_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000003L){
				try {
					return new CACHE_PLAYER_UPDATE_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_UPDATE_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000003L){
				try {
					return new CACHE_PLAYER_UPDATE_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_UPDATE_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000004L){
				try {
					return new CACHE_PLAYER_DELETE_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_DELETE_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000004L){
				try {
					return new CACHE_PLAYER_DELETE_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_DELETE_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000005L){
				try {
					return new CACHE_PLAYERS_GET_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYERS_GET_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000005L){
				try {
					return new CACHE_PLAYERS_GET_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYERS_GET_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000007L){
				try {
					return new CACHE_PLAYER_COUNT_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_COUNT_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000007L){
				try {
					return new CACHE_PLAYER_COUNT_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_COUNT_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000008L){
				try {
					return new CACHE_PLAYER_COUNT_USERNAME_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_COUNT_USERNAME_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000008L){
				try {
					return new CACHE_PLAYER_COUNT_USERNAME_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_COUNT_USERNAME_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000009L){
				try {
					return new CACHE_PLAYER_LIST_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_LIST_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000009L){
				try {
					return new CACHE_PLAYER_LIST_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_LIST_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000010L){
				try {
					return new CACHE_PLAYER_LIST_USERNAME_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_LIST_USERNAME_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000010L){
				try {
					return new CACHE_PLAYER_LIST_USERNAME_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_LIST_USERNAME_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000011L){
				try {
					return new CACHE_ARTICLEENTITY_SAVE_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_ARTICLEENTITY_SAVE_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000011L){
				try {
					return new CACHE_ARTICLEENTITY_SAVE_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_ARTICLEENTITY_SAVE_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000012L){
				try {
					return new CACHE_EQUIPMENTENTITY_SAVE_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_EQUIPMENTENTITY_SAVE_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000012L){
				try {
					return new CACHE_EQUIPMENTENTITY_SAVE_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_EQUIPMENTENTITY_SAVE_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000013L){
				try {
					return new CACHE_PROPSENTITY_SAVE_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PROPSENTITY_SAVE_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000013L){
				try {
					return new CACHE_PROPSENTITY_SAVE_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PROPSENTITY_SAVE_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000014L){
				try {
					return new CACHE_ARTICLEENTITY_UPDATE_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_ARTICLEENTITY_UPDATE_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000014L){
				try {
					return new CACHE_ARTICLEENTITY_UPDATE_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_ARTICLEENTITY_UPDATE_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000015L){
				try {
					return new CACHE_EQUIPMENTENTITY_UPDATE_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_EQUIPMENTENTITY_UPDATE_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000015L){
				try {
					return new CACHE_EQUIPMENTENTITY_UPDATE_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_EQUIPMENTENTITY_UPDATE_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000016L){
				try {
					return new CACHE_PROPSENTITY_UPDATE_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PROPSENTITY_UPDATE_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000016L){
				try {
					return new CACHE_PROPSENTITY_UPDATE_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PROPSENTITY_UPDATE_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000018L){
				try {
					return new CACHE_ARTICLEENTITY_COUNT_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_ARTICLEENTITY_COUNT_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000018L){
				try {
					return new CACHE_ARTICLEENTITY_COUNT_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_ARTICLEENTITY_COUNT_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000019L){
				try {
					return new CACHE_ARTICLEENTITY_LIST_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_ARTICLEENTITY_LIST_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000019L){
				try {
					return new CACHE_ARTICLEENTITY_LIST_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_ARTICLEENTITY_LIST_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000020L){
				try {
					return new CACHE_ARTICLEENTITY_DELETE_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_ARTICLEENTITY_DELETE_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000202L){
				try {
					return new CACHE_ARTICLEENTITY_DELETE_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_ARTICLEENTITY_DELETE_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000021L){
				try {
					return new CACHE_ARTICLEENTITY_GET_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_ARTICLEENTITY_GET_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000021L){
				try {
					return new CACHE_ARTICLEENTITY_GET_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_ARTICLEENTITY_GET_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000022L){
				try {
					return new CACHE_ARTICLEENTITY_GET_BYNAME_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_ARTICLEENTITY_GET_BYNAME_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000022L){
				try {
					return new CACHE_ARTICLEENTITY_GET_BYNAME_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_ARTICLEENTITY_GET_BYNAME_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000023L){
				try {
					return new CACHE_GOODS_SAVE_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_GOODS_SAVE_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000023L){
				try {
					return new CACHE_GOODS_SAVE_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_GOODS_SAVE_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000024L){
				try {
					return new CACHE_GOODS_UPDATE_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_GOODS_UPDATE_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000024L){
				try {
					return new CACHE_GOODS_UPDATE_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_GOODS_UPDATE_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000025L){
				try {
					return new CACHE_GOODS_GET_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_GOODS_GET_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000025L){
				try {
					return new CACHE_GOODS_GET_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_GOODS_GET_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000026L){
				try {
					return new CACHE_GOODS_COUNT_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_GOODS_COUNT_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000026L){
				try {
					return new CACHE_GOODS_COUNT_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_GOODS_COUNT_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000027L){
				try {
					return new CACHE_GOODS_LIST_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_GOODS_LIST_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000027L){
				try {
					return new CACHE_GOODS_LIST_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_GOODS_LIST_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000028L){
				try {
					return new CACHE_GOODS_DELETE_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_GOODS_DELETE_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000028L){
				try {
					return new CACHE_GOODS_DELETE_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_GOODS_DELETE_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000029L){
				try {
					return new CACHE_PLAYER_RANDOM_GET_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_RANDOM_GET_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000029L){
				try {
					return new CACHE_PLAYER_RANDOM_GET_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_PLAYER_RANDOM_GET_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000030L){
				try {
					return new GET_MAX_ARTICLE_ID_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct GET_MAX_ARTICLE_ID_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000030L){
				try {
					return new GET_MAX_ARTICLE_ID_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct GET_MAX_ARTICLE_ID_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000031L){
				try {
					return new CACHE_LEVEL_PLAYER_LIST_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_LEVEL_PLAYER_LIST_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000031L){
				try {
					return new CACHE_LEVEL_PLAYER_LIST_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_LEVEL_PLAYER_LIST_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000032L){
				try {
					return new CACHE_LEVEL_PLAYER_COUNT_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_LEVEL_PLAYER_COUNT_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000032L){
				try {
					return new CACHE_LEVEL_PLAYER_COUNT_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_LEVEL_PLAYER_COUNT_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000033L){
				try {
					return new CACHE_MAGICWEAPON_SAVE_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_MAGICWEAPON_SAVE_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000033L){
				try {
					return new CACHE_MAGICWEAPON_SAVE_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_MAGICWEAPON_SAVE_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000034L){
				try {
					return new CACHE_MAGICWEAPON_GET_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_MAGICWEAPON_GET_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000034L){
				try {
					return new CACHE_MAGICWEAPON_GET_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_MAGICWEAPON_GET_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000035L){
				try {
					return new CACHE_MAGICWEAPON_UPDATE_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_MAGICWEAPON_UPDATE_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000035L){
				try {
					return new CACHE_MAGICWEAPON_UPDATE_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_MAGICWEAPON_UPDATE_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000036L){
				try {
					return new CACHE_MAGICWEAPONS_GET_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_MAGICWEAPONS_GET_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000036L){
				try {
					return new CACHE_MAGICWEAPONS_GET_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CACHE_MAGICWEAPONS_GET_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else{
				throw new MessageFormatErrorException("unknown message type ["+type+"]");
			}
		}catch(IndexOutOfBoundsException e){
			throw new ConnectionException("parse message error",e);
		}
	}
}

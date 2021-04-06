package com.sqage.stat.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;


/**
 * 消息工厂类，此工厂类是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 各数据包的定义如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>请求消息</td><td>类型</td><td>响应消息</td><td>类型</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td><a href='./USEREGIST_LOG_REQ.html'>USEREGIST_LOG_REQ</a></td><td>0x00000000</td><td><a href='./USEREGIST_LOG_RES.html'>USEREGIST_LOG_RES</a></td><td>0x80000000</td><td>用户注册日志</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ENTERGAME_LOG_REQ.html'>ENTERGAME_LOG_REQ</a></td><td>0x00000001</td><td><a href='./ENTERGAME_LOG_RES.html'>ENTERGAME_LOG_RES</a></td><td>0x80000001</td><td>进入游戏日志</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./LOGOUTGAME_LOG_REQ.html'>LOGOUTGAME_LOG_REQ</a></td><td>0x00000002</td><td><a href='./LOGOUTGAME_LOG_RES.html'>LOGOUTGAME_LOG_RES</a></td><td>0x80000002</td><td>退出游戏日志</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DAYCHENG_LOG_REQ.html'>DAYCHENG_LOG_REQ</a></td><td>0x00000003</td><td><a href='./DAYCHENG_LOG_RES.html'>DAYCHENG_LOG_RES</a></td><td>0x80000003</td><td>日期变更日志</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PAYMONEY_LOG_REQ.html'>PAYMONEY_LOG_REQ</a></td><td>0x00000004</td><td><a href='./PAYMONEY_LOG_RES.html'>PAYMONEY_LOG_RES</a></td><td>0x80000004</td><td>充值日志</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SPENDMONEY_LOG_REQ.html'>SPENDMONEY_LOG_REQ</a></td><td>0x00000005</td><td><a href='./SPENDMONEY_LOG_RES.html'>SPENDMONEY_LOG_RES</a></td><td>0x80000005</td><td>消费日志</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ONLINEUSERSCOUNT_LOG_REQ.html'>ONLINEUSERSCOUNT_LOG_REQ</a></td><td>0x00000006</td><td><a href='./ONLINEUSERSCOUNT_LOG_RES.html'>ONLINEUSERSCOUNT_LOG_RES</a></td><td>0x80000006</td><td>用户在线人数日志</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CREATEPLAYER_LOG_REQ.html'>CREATEPLAYER_LOG_REQ</a></td><td>0x00000007</td><td><a href='./CREATEPLAYER_LOG_RES.html'>CREATEPLAYER_LOG_RES</a></td><td>0x80000007</td><td>用户创建角色日志</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DAOJU_LOG_REQ.html'>DAOJU_LOG_REQ</a></td><td>0x00000008</td><td><a href='./DAOJU_LOG_RES.html'>DAOJU_LOG_RES</a></td><td>0x80000008</td><td>道具得失日志</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TRANSACTION_LOG_REQ.html'>TRANSACTION_LOG_REQ</a></td><td>0x00000009</td><td><a href='./TRANSACTION_LOG_RES.html'>TRANSACTION_LOG_RES</a></td><td>0x80000009</td><td>道具变更日志</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ACCEPTTASK_LOG_REQ.html'>ACCEPTTASK_LOG_REQ</a></td><td>0x00000010</td><td><a href='./ACCEPTTASK_LOG_RES.html'>ACCEPTTASK_LOG_RES</a></td><td>0x80000010</td><td>接受任务日志</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FINISHTASK_LOG_REQ.html'>FINISHTASK_LOG_REQ</a></td><td>0x00000011</td><td><a href='./FINISHTASK_LOG_RES.html'>FINISHTASK_LOG_RES</a></td><td>0x80000011</td><td>完成任务日志</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ACCEPTHUODONGINFO_LOG_REQ.html'>ACCEPTHUODONGINFO_LOG_REQ</a></td><td>0x00000012</td><td><a href='./ACCEPTHUODONGINFO_LOG_RES.html'>ACCEPTHUODONGINFO_LOG_RES</a></td><td>0x80000012</td><td>接受活动日志</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FINISHHUODONGINFO_LOG_REQ.html'>FINISHHUODONGINFO_LOG_REQ</a></td><td>0x00000013</td><td><a href='./FINISHHUODONGINFO_LOG_RES.html'>FINISHHUODONGINFO_LOG_RES</a></td><td>0x80000013</td><td>完成活动日志</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GAMECHONGZHI_LOG_REQ.html'>GAMECHONGZHI_LOG_REQ</a></td><td>0x00000014</td><td><a href='./GAMECHONGZHI_LOG_RES.html'>GAMECHONGZHI_LOG_RES</a></td><td>0x80000014</td><td>游戏中的货币充值，消耗日志</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./STATUSERGUIDE_LOG_REQ.html'>STATUSERGUIDE_LOG_REQ</a></td><td>0x00000015</td><td><a href='./STATUSERGUIDE_LOG_RES.html'>STATUSERGUIDE_LOG_RES</a></td><td>0x80000015</td><td>新手引导日志</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TRANSACTION_SPECIAL_LOG_REQ.html'>TRANSACTION_SPECIAL_LOG_REQ</a></td><td>0x00000016</td><td><a href='./TRANSACTION_SPECIAL_RES.html'>TRANSACTION_SPECIAL_RES</a></td><td>0x80000016</td><td>特殊交易日志</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./YINZIKUCUN_LOG_REQ.html'>YINZIKUCUN_LOG_REQ</a></td><td>0x00000017</td><td><a href='./YINZIKUCUN_LOG_RES.html'>YINZIKUCUN_LOG_RES</a></td><td>0x80000017</td><td>银子库存日志</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PAYMONEYUPDATE_LOG_REQ.html'>PAYMONEYUPDATE_LOG_REQ</a></td><td>0x00000018</td><td><a href='./PAYMONEYUPDATE_LOG_RES.html'>PAYMONEYUPDATE_LOG_RES</a></td><td>0x80000018</td><td>充值日志</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./LIBAO_LOG_REQ.html'>LIBAO_LOG_REQ</a></td><td>0x00000019</td><td><a href='./LIBAO_LOG_RES.html'>LIBAO_LOG_RES</a></td><td>0x80000019</td><td>周月礼包日志</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TRANSACTION_FACE_LOG_REQ.html'>TRANSACTION_FACE_LOG_REQ</a></td><td>0x00000020</td><td><a href='./TRANSACTION_FACE_RES.html'>TRANSACTION_FACE_RES</a></td><td>0x80000020</td><td>面对面交易日志</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TRANSFER_PLATFORM_LOG_REQ.html'>TRANSFER_PLATFORM_LOG_REQ</a></td><td>0x00000021</td><td><a href='./TRANSFER_PLATFORM_RES.html'>TRANSFER_PLATFORM_RES</a></td><td>0x80000021</td><td>交易平台日志</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BATTLE_COSTTIME_LOG_REQ.html'>BATTLE_COSTTIME_LOG_REQ</a></td><td>0x00000022</td><td><a href='./BATTLE_COSTTIME_RES.html'>BATTLE_COSTTIME_RES</a></td><td>0x80000022</td><td>每场战斗用的时间日志</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BATTLE_PLAYERSTAT_LOG_REQ.html'>BATTLE_PLAYERSTAT_LOG_REQ</a></td><td>0x00000023</td><td><a href='./BATTLE_PLAYERSTAT_RES.html'>BATTLE_PLAYERSTAT_RES</a></td><td>0x80000023</td><td>每个人的功勋时间日志</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BATTLE_TEAMSTAT_LOG_REQ.html'>BATTLE_TEAMSTAT_LOG_REQ</a></td><td>0x00000024</td><td><a href='./BATTLE_TEAMSTAT_RES.html'>BATTLE_TEAMSTAT_RES</a></td><td>0x80000024</td><td>团队功勋时间日志</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FUMO_LOG_REQ.html'>FUMO_LOG_REQ</a></td><td>0x00000025</td><td><a href='./FUMO_LOG_RES.html'>FUMO_LOG_RES</a></td><td>0x80000025</td><td>附魔日志</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NPC_LOG_REQ.html'>NPC_LOG_REQ</a></td><td>0x00000026</td><td><a href='./NPC_LOG_RES.html'>NPC_LOG_RES</a></td><td>0x80000026</td><td>NPC日志</td></tr>
 * </table>
 */
public class StatMessageFactory extends AbstractMessageFactory {

	private static long sequnceNum = 0L;
	public synchronized static long nextSequnceNum(){
		sequnceNum ++;
		if(sequnceNum >= 0x7FFFFFFF){
			sequnceNum = 1L;
		}
		return sequnceNum;
	}

	protected static StatMessageFactory self;

	public static StatMessageFactory getInstance(){
		if(self != null){
			return self;
		}
		synchronized(StatMessageFactory.class){
			if(self != null) return self;
			self = new StatMessageFactory();
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
			if(type == 0x00000000L){
				try {
					return new USEREGIST_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct USEREGIST_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000000L){
				try {
					return new USEREGIST_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct USEREGIST_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000001L){
				try {
					return new ENTERGAME_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct ENTERGAME_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000001L){
				try {
					return new ENTERGAME_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct ENTERGAME_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000002L){
				try {
					return new LOGOUTGAME_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct LOGOUTGAME_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000002L){
				try {
					return new LOGOUTGAME_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct LOGOUTGAME_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000003L){
				try {
					return new DAYCHENG_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct DAYCHENG_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000003L){
				try {
					return new DAYCHENG_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct DAYCHENG_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000004L){
				try {
					return new PAYMONEY_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct PAYMONEY_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000004L){
				try {
					return new PAYMONEY_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct PAYMONEY_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000005L){
				try {
					return new SPENDMONEY_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct SPENDMONEY_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000005L){
				try {
					return new SPENDMONEY_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct SPENDMONEY_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000006L){
				try {
					return new ONLINEUSERSCOUNT_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct ONLINEUSERSCOUNT_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000006L){
				try {
					return new ONLINEUSERSCOUNT_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct ONLINEUSERSCOUNT_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000007L){
				try {
					return new CREATEPLAYER_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CREATEPLAYER_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000007L){
				try {
					return new CREATEPLAYER_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct CREATEPLAYER_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000008L){
				try {
					return new DAOJU_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct DAOJU_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000008L){
				try {
					return new DAOJU_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct DAOJU_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000009L){
				try {
					return new TRANSACTION_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct TRANSACTION_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000009L){
				try {
					return new TRANSACTION_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct TRANSACTION_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000010L){
				try {
					return new ACCEPTTASK_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct ACCEPTTASK_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000010L){
				try {
					return new ACCEPTTASK_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct ACCEPTTASK_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000011L){
				try {
					return new FINISHTASK_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct FINISHTASK_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000011L){
				try {
					return new FINISHTASK_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct FINISHTASK_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000012L){
				try {
					return new ACCEPTHUODONGINFO_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct ACCEPTHUODONGINFO_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000012L){
				try {
					return new ACCEPTHUODONGINFO_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct ACCEPTHUODONGINFO_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000013L){
				try {
					return new FINISHHUODONGINFO_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct FINISHHUODONGINFO_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000013L){
				try {
					return new FINISHHUODONGINFO_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct FINISHHUODONGINFO_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000014L){
				try {
					return new GAMECHONGZHI_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct GAMECHONGZHI_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000014L){
				try {
					return new GAMECHONGZHI_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct GAMECHONGZHI_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000015L){
				try {
					return new STATUSERGUIDE_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct STATUSERGUIDE_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000015L){
				try {
					return new STATUSERGUIDE_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct STATUSERGUIDE_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000016L){
				try {
					return new TRANSACTION_SPECIAL_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct TRANSACTION_SPECIAL_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000016L){
				try {
					return new TRANSACTION_SPECIAL_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct TRANSACTION_SPECIAL_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000017L){
				try {
					return new YINZIKUCUN_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct YINZIKUCUN_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000017L){
				try {
					return new YINZIKUCUN_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct YINZIKUCUN_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000018L){
				try {
					return new PAYMONEYUPDATE_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct PAYMONEYUPDATE_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000018L){
				try {
					return new PAYMONEYUPDATE_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct PAYMONEYUPDATE_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000019L){
				try {
					return new LIBAO_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct LIBAO_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000019L){
				try {
					return new LIBAO_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct LIBAO_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000020L){
				try {
					return new TRANSACTION_FACE_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct TRANSACTION_FACE_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000020L){
				try {
					return new TRANSACTION_FACE_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct TRANSACTION_FACE_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000021L){
				try {
					return new TRANSFER_PLATFORM_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct TRANSFER_PLATFORM_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000021L){
				try {
					return new TRANSFER_PLATFORM_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct TRANSFER_PLATFORM_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000022L){
				try {
					return new BATTLE_COSTTIME_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct BATTLE_COSTTIME_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000022L){
				try {
					return new BATTLE_COSTTIME_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct BATTLE_COSTTIME_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000023L){
				try {
					return new BATTLE_PLAYERSTAT_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct BATTLE_PLAYERSTAT_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000023L){
				try {
					return new BATTLE_PLAYERSTAT_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct BATTLE_PLAYERSTAT_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000024L){
				try {
					return new BATTLE_TEAMSTAT_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct BATTLE_TEAMSTAT_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000024L){
				try {
					return new BATTLE_TEAMSTAT_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct BATTLE_TEAMSTAT_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000025L){
				try {
					return new FUMO_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct FUMO_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000025L){
				try {
					return new FUMO_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct FUMO_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000026L){
				try {
					return new NPC_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct NPC_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000026L){
				try {
					return new NPC_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct NPC_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else{
				throw new MessageFormatErrorException("unknown message type ["+type+"]");
			}
		}catch(IndexOutOfBoundsException e){
			throw new ConnectionException("parse message error",e);
		}
	}
}

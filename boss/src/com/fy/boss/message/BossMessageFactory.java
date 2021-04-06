package com.fy.boss.message;

import com.fy.boss.message.*;
import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;


/**
 * 消息工厂类，此工厂类是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 各数据包的定义如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>请求消息</td><td>类型</td><td>响应消息</td><td>类型</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PASSPORT_REGISTER_REQ.html'>PASSPORT_REGISTER_REQ</a></td><td>0x0000E000</td><td><a href='./PASSPORT_REGISTER_RES.html'>PASSPORT_REGISTER_RES</a></td><td>0x8000E000</td><td>注册一个通行证</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PASSPORT_LOGIN_REQ.html'>PASSPORT_LOGIN_REQ</a></td><td>0x0000E001</td><td><a href='./PASSPORT_LOGIN_RES.html'>PASSPORT_LOGIN_RES</a></td><td>0x8000E001</td><td>登陆</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PASSPORT_GET_REQ.html'>PASSPORT_GET_REQ</a></td><td>0x0000E002</td><td><a href='./PASSPORT_GET_RES.html'>PASSPORT_GET_RES</a></td><td>0x8000E002</td><td>登陆</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PASSPORT_GET_REQ2.html'>PASSPORT_GET_REQ2</a></td><td>0x0000E003</td><td><a href='./PASSPORT_GET_RES2.html'>PASSPORT_GET_RES2</a></td><td>0x8000E003</td><td>登陆</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PASSPORT_UPDATE_REQ.html'>PASSPORT_UPDATE_REQ</a></td><td>0x0000E004</td><td><a href='./PASSPORT_UPDATE_RES.html'>PASSPORT_UPDATE_RES</a></td><td>0x8000E004</td><td>登陆</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./APPSTORE_UPDATE_IDENTY_REQ.html'>APPSTORE_UPDATE_IDENTY_REQ</a></td><td>0x0000E005</td><td><a href='./APPSTORE_UPDATE_IDENTY_RES.html'>APPSTORE_UPDATE_IDENTY_RES</a></td><td>0x8000E005</td><td>更新Passport的lastloginIp</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QQ_LOGIN_VALIDATE_REQ.html'>QQ_LOGIN_VALIDATE_REQ</a></td><td>0x0000A001</td><td><a href='./QQ_LOGIN_VALIDATE_RES.html'>QQ_LOGIN_VALIDATE_RES</a></td><td>0x8000A001</td><td>登陆</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QQ_SAVING_REQ.html'>QQ_SAVING_REQ</a></td><td>0x0000A002</td><td><a href='./QQ_SAVING_RES.html'>QQ_SAVING_RES</a></td><td>0x8000A002</td><td>QQ充值</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CHANNEL_UER_SAVING_REQ.html'>CHANNEL_UER_SAVING_REQ</a></td><td>0x0000A003</td><td><a href='./CHANNEL_UER_SAVING_RES.html'>CHANNEL_UER_SAVING_RES</a></td><td>0x8000A003</td><td>渠道充值</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NINEONE_SAVING_REQ.html'>NINEONE_SAVING_REQ</a></td><td>0x0000A004</td><td><a href='./NINEONE_SAVING_RES.html'>NINEONE_SAVING_RES</a></td><td>0x8000A004</td><td>91充值</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./APPCHINA_SAVING_REQ.html'>APPCHINA_SAVING_REQ</a></td><td>0x0000A005</td><td><a href='./APPCHINA_SAVING_RES.html'>APPCHINA_SAVING_RES</a></td><td>0x8000A005</td><td>应用汇充值</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QQ_SAVING_NEW_REQ.html'>QQ_SAVING_NEW_REQ</a></td><td>0x0000A006</td><td><a href='./QQ_SAVING_NEW_RES.html'>QQ_SAVING_NEW_RES</a></td><td>0x8000A006</td><td>QQ充值</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./USER_SAVING_REQ.html'>USER_SAVING_REQ</a></td><td>0x0000F001</td><td><a href='./USER_SAVING_RES.html'>USER_SAVING_RES</a></td><td>0x8000F001</td><td>用户充值</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SAVING_RECORD_REQ.html'>SAVING_RECORD_REQ</a></td><td>0x0000F002</td><td><a href='./SAVING_RECORD_RES.html'>SAVING_RECORD_RES</a></td><td>0x8000F002</td><td>客户端请求充值记录</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ALIPAY_ARGS_REQ.html'>ALIPAY_ARGS_REQ</a></td><td>0x0000F004</td><td><a href='./ALIPAY_ARGS_RES.html'>ALIPAY_ARGS_RES</a></td><td>0x8000F004</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ALIPAY_GET_ORDERID_REQ.html'>ALIPAY_GET_ORDERID_REQ</a></td><td>0x0000F005</td><td><a href='./ALIPAY_GET_ORDERID_RES.html'>ALIPAY_GET_ORDERID_RES</a></td><td>0x8000F005</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ORDER_STATUS_CHANGE_REQ.html'>ORDER_STATUS_CHANGE_REQ</a></td><td>0x0000F006</td><td><a href='./ORDER_STATUS_CHANGE_RES.html'>ORDER_STATUS_CHANGE_RES</a></td><td>0x8000F006</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./APPSTORE_RECEIPT_REQ.html'>APPSTORE_RECEIPT_REQ</a></td><td>0x0000F007</td><td><a href='./APPSTORE_RECEIPT_RES.html'>APPSTORE_RECEIPT_RES</a></td><td>0x8000F007</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./APPSTORE_SAVING_VERIFY_REQ.html'>APPSTORE_SAVING_VERIFY_REQ</a></td><td>0x0000F008</td><td><a href='./APPSTORE_SAVING_VERIFY_RES.html'>APPSTORE_SAVING_VERIFY_RES</a></td><td>0x8000F008</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ORDER_STATUS_NOTIFY_REQ.html'>ORDER_STATUS_NOTIFY_REQ</a></td><td>0x0000F009</td><td><a href='./ORDER_STATUS_NOTIFY_RES.html'>ORDER_STATUS_NOTIFY_RES</a></td><td>0x8000F009</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SAVING_UCIDTOUSERNAME_REQ.html'>SAVING_UCIDTOUSERNAME_REQ</a></td><td>0x0000F010</td><td><a href='./SAVING_UCIDTOUSERNAME_RES.html'>SAVING_UCIDTOUSERNAME_RES</a></td><td>0x8000F010</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_USERNAMEBYUCID_REQ.html'>GET_USERNAMEBYUCID_REQ</a></td><td>0x0000F011</td><td><a href='./GET_USERNAMEBYUCID_RES.html'>GET_USERNAMEBYUCID_RES</a></td><td>0x8000F011</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_UCIDBYUSERNAME_REQ.html'>GET_UCIDBYUSERNAME_REQ</a></td><td>0x0000F012</td><td><a href='./GET_UCIDBYUSERNAME_RES.html'>GET_UCIDBYUSERNAME_RES</a></td><td>0x8000F012</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FEEDBACK_COMMIT_BOSS_REQ.html'>FEEDBACK_COMMIT_BOSS_REQ</a></td><td>0x0000F013</td><td><a href='./FEEDBACK_COMMIT_BOSS_RES.html'>FEEDBACK_COMMIT_BOSS_RES</a></td><td>0x8000F013</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./FEEDBACK_HOME_PAGE_BOSS_REQ.html'>FEEDBACK_HOME_PAGE_BOSS_REQ</a></td><td>0x0000F014</td><td><a href='./FEEDBACK_HOME_PAGE_BOSS_RES.html'>FEEDBACK_HOME_PAGE_BOSS_RES</a></td><td>0x8000F014</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FEEDBACK_DELETE_BOSS_REQ.html'>FEEDBACK_DELETE_BOSS_REQ</a></td><td>0x0000F015</td><td><a href='./FEEDBACK_DELETE_BOSS_RES.html'>FEEDBACK_DELETE_BOSS_RES</a></td><td>0x8000F015</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./FEEDBACK_SCORE_BOSS_REQ.html'>FEEDBACK_SCORE_BOSS_REQ</a></td><td>0x0000F016</td><td><a href='./FEEDBACK_SCORE_BOSS_RES.html'>FEEDBACK_SCORE_BOSS_RES</a></td><td>0x8000F016</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FEEDBACK_LOOK_BOSS_REQ.html'>FEEDBACK_LOOK_BOSS_REQ</a></td><td>0x0000F017</td><td><a href='./FEEDBACK_LOOK_BOSS_RES.html'>FEEDBACK_LOOK_BOSS_RES</a></td><td>0x8000F017</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./FEEDBACK_LOOK_SCORE_BOSS_REQ.html'>FEEDBACK_LOOK_SCORE_BOSS_REQ</a></td><td>0x0000F018</td><td><a href='./FEEDBACK_LOOK_SCORE_BOSS_RES.html'>FEEDBACK_LOOK_SCORE_BOSS_RES</a></td><td>0x8000F018</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PASSPORT_UPDATE_REQ2.html'>PASSPORT_UPDATE_REQ2</a></td><td>0x0000D018</td><td><a href='./PASSPORT_UPDATE_RES2.html'>PASSPORT_UPDATE_RES2</a></td><td>0x8000D018</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./FEEDBACK_PLAYER_TALK_BOSS_REQ.html'>FEEDBACK_PLAYER_TALK_BOSS_REQ</a></td><td>0x0000F019</td><td><a href='./FEEDBACK_PLAYER_TALK_BOSS_RES.html'>FEEDBACK_PLAYER_TALK_BOSS_RES</a></td><td>0x8000F019</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./FEEDBACK_GM_TALK_BOSS_RES.html'>FEEDBACK_GM_TALK_BOSS_RES</a></td><td>0x8000F020</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_MOZUANINFO_REQ.html'>GET_MOZUANINFO_REQ</a></td><td>0x0000F021</td><td><a href='./GET_MOZUANINFO_RES.html'>GET_MOZUANINFO_RES</a></td><td>0x8000F021</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SEND_FUND_FOR_NINEONE_REQ.html'>SEND_FUND_FOR_NINEONE_REQ</a></td><td>0x0000F022</td><td><a href='./SEND_FUND_FOR_NINEONE_RES.html'>SEND_FUND_FOR_NINEONE_RES</a></td><td>0x8000F022</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CHANNEL_UER_SAVING_NEW_REQ.html'>CHANNEL_UER_SAVING_NEW_REQ</a></td><td>0x0000F023</td><td><a href='./CHANNEL_UER_SAVING_NEW_RES.html'>CHANNEL_UER_SAVING_NEW_RES</a></td><td>0x8000F023</td><td>渠道充值</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NINEONE_SAVING_NEW_REQ.html'>NINEONE_SAVING_NEW_REQ</a></td><td>0x0000F024</td><td><a href='./NINEONE_SAVING_NEW_RES.html'>NINEONE_SAVING_NEW_RES</a></td><td>0x8000F024</td><td>91充值</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./APPCHINA_SAVING_NEW_REQ.html'>APPCHINA_SAVING_NEW_REQ</a></td><td>0x0000F025</td><td><a href='./APPCHINA_SAVING_NEW_RES.html'>APPCHINA_SAVING_NEW_RES</a></td><td>0x8000F025</td><td>应用汇充值</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./USER_SAVING_NEW_REQ.html'>USER_SAVING_NEW_REQ</a></td><td>0x0000F026</td><td><a href='./USER_SAVING_NEW_RES.html'>USER_SAVING_NEW_RES</a></td><td>0x8000F026</td><td>用户充值</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ALIPAY_GET_ORDERID_NEW_REQ.html'>ALIPAY_GET_ORDERID_NEW_REQ</a></td><td>0x0000F027</td><td><a href='./ALIPAY_GET_ORDERID_NEW_RES.html'>ALIPAY_GET_ORDERID_NEW_RES</a></td><td>0x8000F027</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./APPSTORE_RECEIPT_NEW_REQ.html'>APPSTORE_RECEIPT_NEW_REQ</a></td><td>0x0000F028</td><td><a href='./APPSTORE_RECEIPT_NEW_RES.html'>APPSTORE_RECEIPT_NEW_RES</a></td><td>0x8000F028</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_SERVER_URL_REQ.html'>GET_SERVER_URL_REQ</a></td><td>0x0000F029</td><td><a href='./GET_SERVER_URL_RES.html'>GET_SERVER_URL_RES</a></td><td>0x8000F029</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_USER_LAST_SAVING_TIME_REQ.html'>GET_USER_LAST_SAVING_TIME_REQ</a></td><td>0x0000F030</td><td><a href='./GET_USER_LAST_SAVING_TIME_RES.html'>GET_USER_LAST_SAVING_TIME_RES</a></td><td>0x8000F030</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JUDGE_POP_WINDOW_FOR_VIP_REQ.html'>JUDGE_POP_WINDOW_FOR_VIP_REQ</a></td><td>0x0000F031</td><td><a href='./JUDGE_POP_WINDOW_FOR_VIP_RES.html'>JUDGE_POP_WINDOW_FOR_VIP_RES</a></td><td>0x8000F031</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CREATE_NEW_VIPINFORECORD_REQ.html'>CREATE_NEW_VIPINFORECORD_REQ</a></td><td>0x0000F032</td><td><a href='./CREATE_NEW_VIPINFORECORD_RES.html'>CREATE_NEW_VIPINFORECORD_RES</a></td><td>0x8000F032</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QQ_SAVING_PRO_REQ.html'>QQ_SAVING_PRO_REQ</a></td><td>0x0000F033</td><td><a href='./QQ_SAVING_PRO_RES.html'>QQ_SAVING_PRO_RES</a></td><td>0x8000F033</td><td>QQ充值</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_UCVIP_INFO_REQ.html'>GET_UCVIP_INFO_REQ</a></td><td>0x0000F034</td><td><a href='./GET_UCVIP_INFO_RES.html'>GET_UCVIP_INFO_RES</a></td><td>0x8000F034</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAN_SEND_UC_VID_MAIL_REQ.html'>CAN_SEND_UC_VID_MAIL_REQ</a></td><td>0x0000F035</td><td><a href='./CAN_SEND_UC_VID_MAIL_RES.html'>CAN_SEND_UC_VID_MAIL_RES</a></td><td>0x8000F035</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CREATE_UC_VID_MAIL_SEND_RECORD_REQ.html'>CREATE_UC_VID_MAIL_SEND_RECORD_REQ</a></td><td>0x0000F036</td><td><a href='./CREATE_UC_VID_MAIL_SEND_RECORD_RES.html'>CREATE_UC_VID_MAIL_SEND_RECORD_RES</a></td><td>0x8000F036</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MIGU_COMMUNICATE_REQ.html'>MIGU_COMMUNICATE_REQ</a></td><td>0x0000F037</td><td><a href='./MIGU_COMMUNICATE_RES.html'>MIGU_COMMUNICATE_RES</a></td><td>0x8000F037</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_SAVINGAMOUNT_REQ.html'>QUERY_SAVINGAMOUNT_REQ</a></td><td>0x0000F038</td><td><a href='./QUERY_SAVINGAMOUNT_RES.html'>QUERY_SAVINGAMOUNT_RES</a></td><td>0x8000F038</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MORE_ARGS_ORDER_STATUS_CHANGE_REQ.html'>MORE_ARGS_ORDER_STATUS_CHANGE_REQ</a></td><td>0x0000F039</td><td><a href='./MORE_ARGS_ORDER_STATUS_CHANGE_RES.html'>MORE_ARGS_ORDER_STATUS_CHANGE_RES</a></td><td>0x8000F039</td><td>无说明</td></tr>
 * </table>
 */
public class BossMessageFactory extends AbstractMessageFactory {

	private static long sequnceNum = 0L;
	public synchronized static long nextSequnceNum(){
		sequnceNum ++;
		if(sequnceNum >= 0x7FFFFFFF){
			sequnceNum = 1L;
		}
		return sequnceNum;
	}

	protected static BossMessageFactory self;

	public static BossMessageFactory getInstance(){
		if(self != null){
			return self;
		}
		synchronized(BossMessageFactory.class){
			if(self != null) return self;
			self = new BossMessageFactory();
		}
		return self;
	}

	public Message newMessage(byte[] messageContent,int offset,int size)
		throws MessageFormatErrorException, ConnectionException,Exception {
		int len = (int)byteArrayToNumber(messageContent,offset,getNumOfByteForMessageLength());
		if(len != size)
			throw new MessageFormatErrorException("message length not match");
		int end = offset + size;
		offset += getNumOfByteForMessageLength();
		long type = byteArrayToNumber(messageContent,offset,4);
		offset += 4;
		long sn = byteArrayToNumber(messageContent,offset,4);
		offset += 4;

			if(type == 0x0000E000L){
					return new PASSPORT_REGISTER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000E000L){
					return new PASSPORT_REGISTER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000E001L){
					return new PASSPORT_LOGIN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000E001L){
					return new PASSPORT_LOGIN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000E002L){
					return new PASSPORT_GET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000E002L){
					return new PASSPORT_GET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000E003L){
					return new PASSPORT_GET_REQ2(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000E003L){
					return new PASSPORT_GET_RES2(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000E004L){
					return new PASSPORT_UPDATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000E004L){
					return new PASSPORT_UPDATE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000E005L){
					return new APPSTORE_UPDATE_IDENTY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000E005L){
					return new APPSTORE_UPDATE_IDENTY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A001L){
					return new QQ_LOGIN_VALIDATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000A001L){
					return new QQ_LOGIN_VALIDATE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A002L){
					return new QQ_SAVING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000A002L){
					return new QQ_SAVING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A003L){
					return new CHANNEL_UER_SAVING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000A003L){
					return new CHANNEL_UER_SAVING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A004L){
					return new NINEONE_SAVING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000A004L){
					return new NINEONE_SAVING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A005L){
					return new APPCHINA_SAVING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000A005L){
					return new APPCHINA_SAVING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A006L){
					return new QQ_SAVING_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000A006L){
					return new QQ_SAVING_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F001L){
					return new USER_SAVING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F001L){
					return new USER_SAVING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F002L){
					return new SAVING_RECORD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F002L){
					return new SAVING_RECORD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F004L){
					return new ALIPAY_ARGS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F004L){
					return new ALIPAY_ARGS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F005L){
					return new ALIPAY_GET_ORDERID_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F005L){
					return new ALIPAY_GET_ORDERID_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F006L){
					return new ORDER_STATUS_CHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F006L){
					return new ORDER_STATUS_CHANGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F007L){
					return new APPSTORE_RECEIPT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F007L){
					return new APPSTORE_RECEIPT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F008L){
					return new APPSTORE_SAVING_VERIFY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F008L){
					return new APPSTORE_SAVING_VERIFY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F009L){
					return new ORDER_STATUS_NOTIFY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F009L){
					return new ORDER_STATUS_NOTIFY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F010L){
					return new SAVING_UCIDTOUSERNAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F010L){
					return new SAVING_UCIDTOUSERNAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F011L){
					return new GET_USERNAMEBYUCID_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F011L){
					return new GET_USERNAMEBYUCID_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F012L){
					return new GET_UCIDBYUSERNAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F012L){
					return new GET_UCIDBYUSERNAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F013L){
					return new FEEDBACK_COMMIT_BOSS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F013L){
					return new FEEDBACK_COMMIT_BOSS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F014L){
					return new FEEDBACK_HOME_PAGE_BOSS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F014L){
					return new FEEDBACK_HOME_PAGE_BOSS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F015L){
					return new FEEDBACK_DELETE_BOSS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F015L){
					return new FEEDBACK_DELETE_BOSS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F016L){
					return new FEEDBACK_SCORE_BOSS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F016L){
					return new FEEDBACK_SCORE_BOSS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F017L){
					return new FEEDBACK_LOOK_BOSS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F017L){
					return new FEEDBACK_LOOK_BOSS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F018L){
					return new FEEDBACK_LOOK_SCORE_BOSS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F018L){
					return new FEEDBACK_LOOK_SCORE_BOSS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000D018L){
					return new PASSPORT_UPDATE_REQ2(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000D018L){
					return new PASSPORT_UPDATE_RES2(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F019L){
					return new FEEDBACK_PLAYER_TALK_BOSS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F019L){
					return new FEEDBACK_PLAYER_TALK_BOSS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F020L){
					return new FEEDBACK_GM_TALK_BOSS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F021L){
					return new GET_MOZUANINFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F021L){
					return new GET_MOZUANINFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F022L){
					return new SEND_FUND_FOR_NINEONE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F022L){
					return new SEND_FUND_FOR_NINEONE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F023L){
					return new CHANNEL_UER_SAVING_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F023L){
					return new CHANNEL_UER_SAVING_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F024L){
					return new NINEONE_SAVING_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F024L){
					return new NINEONE_SAVING_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F025L){
					return new APPCHINA_SAVING_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F025L){
					return new APPCHINA_SAVING_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F026L){
					return new USER_SAVING_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F026L){
					return new USER_SAVING_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F027L){
					return new ALIPAY_GET_ORDERID_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F027L){
					return new ALIPAY_GET_ORDERID_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F028L){
					return new APPSTORE_RECEIPT_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F028L){
					return new APPSTORE_RECEIPT_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F029L){
					return new GET_SERVER_URL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F029L){
					return new GET_SERVER_URL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F030L){
					return new GET_USER_LAST_SAVING_TIME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F030L){
					return new GET_USER_LAST_SAVING_TIME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F031L){
					return new JUDGE_POP_WINDOW_FOR_VIP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F031L){
					return new JUDGE_POP_WINDOW_FOR_VIP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F032L){
					return new CREATE_NEW_VIPINFORECORD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F032L){
					return new CREATE_NEW_VIPINFORECORD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F033L){
					return new QQ_SAVING_PRO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F033L){
					return new QQ_SAVING_PRO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F034L){
					return new GET_UCVIP_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F034L){
					return new GET_UCVIP_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F035L){
					return new CAN_SEND_UC_VID_MAIL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F035L){
					return new CAN_SEND_UC_VID_MAIL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F036L){
					return new CREATE_UC_VID_MAIL_SEND_RECORD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F036L){
					return new CREATE_UC_VID_MAIL_SEND_RECORD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F037L){
					return new MIGU_COMMUNICATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F037L){
					return new MIGU_COMMUNICATE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F038L){
					return new QUERY_SAVINGAMOUNT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F038L){
					return new QUERY_SAVINGAMOUNT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F039L){
					return new MORE_ARGS_ORDER_STATUS_CHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F039L){
					return new MORE_ARGS_ORDER_STATUS_CHANGE_RES(sn,messageContent,offset,end - offset);
			}else{
				throw new MessageFormatErrorException("unknown message type ["+type+"]");
			}
	}
}

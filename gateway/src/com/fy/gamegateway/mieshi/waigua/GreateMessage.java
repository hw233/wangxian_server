package com.fy.gamegateway.mieshi.waigua;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class GreateMessage {

	public static String[] messageNameSS = {
		"TRY_GETPLAYERINFO",
		"WAIT_FOR_OTHER",
		"GET_REWARD_2_PLAYER",
		"REQUESTBUY_GET_ENTITY_INFO",
		"PLAYER_SOUL",
		"CARD_TRYSAVING",
		"GANG_WAREHOUSE_JOURNAL",
		"GET_WAREHOUSE",
		"QUERY__GETAUTOBACK",
		"GET_ZONGPAI_NAME",
		"TRY_LEAVE_ZONGPAI",
		"REBEL_EVICT_NEW",
		"GET_PLAYERTITLE",
		"MARRIAGE_TRY_BEQSTART",
		"MARRIAGE_GUESTNEW_OVER",
		"MARRIAGE_HUNLI",
		"COUNTRY_COMMENDCANCEL",
		"COUNTRY_NEWQIUJIN",
		"GET_COUNTRYJINKU",
		"CAVE_NEWBUILDING",
		"CAVE_FIELD",
		"CAVE_NEW_PET",
		"CAVE_TRY_SCHEDULE",
		"CAVE_SEND_COUNTYLIST",
		"PLAYER_NEW_LEVELUP",
		"CLEAN_FRIEND_LIST",
		"DO_ACTIVITY_NEW",
		"REF_TESK_LIST",
		"DELTE_PET_INFO",
		"MARRIAGE_DOACTIVITY",
		"LA_FRIEND",
		"TRY_NEWFRIEND_LIST",
		"QINGQIU_PET_INFO",
		"REMOVE_ACTIVITY_NEW",
		"TRY_LEAVE_GAME",
		"GET_TESK_LIST",
		"GET_GAME_PALAYERNAME",
		"GET_ACTIVITY_JOINIDS",
		"QUERY_GAMENAMES",
		"GET_PET_NBWINFO",
		"CLONE_FRIEND_LIST",
		"QUERY_OTHERPLAYER_PET_MSG",
		"CSR_GET_PLAYER",
		"HAVE_OTHERNEW_INFO",
		"SHANCHU_FRIENDLIST",
		"QUERY_TESK_LIST",
		"CL_HORSE_INFO",
		"CL_NEWPET_MSG",
		"GET_ACTIVITY_NEW",
		"DO_SOME",
		"TY_PET",
		"EQUIPMENT_GET_MSG",
		"EQU_NEW_EQUIPMENT",
		"DELETE_FRIEND_LIST",
		"DO_PET_EQUIPMENT",
		"QILING_NEW_INFO",
		"HORSE_QILING_INFO",
		"HORSE_EQUIPMENT_QILING",
		"PET_EQU_QILING",
		"MARRIAGE_TRYACTIVITY",
		"PET_TRY_QILING",
		"PLAYER_CLEAN_QILINGLIST",
		"DELETE_TESK_LIST",
		"GET_HEIMINGDAI_NEWLIST",
		"QUERY_CHOURENLIST",
		"QINCHU_PLAYER",
		"REMOVE_FRIEND_LIST",
		"TRY_REMOVE_CHOUREN",
		"REMOVE_CHOUREN",
		"DELETE_TASK_LIST",
		"PLAYER_TO_PLAYER_DEAL",
		"AUCTION_NEW_LIST_MSG",
		"REQUEST_BUY_PLAYER_INFO",
		"BOOTHER_PLAYER_MSGNAME",
		"BOOTHER_MSG_CLEAN",
		"TRY_REQUESTBUY_CLEAN_ALL",
		"SCHOOL_INFONAMES",
		"PET_NEW_LEVELUP",
		"VALIDATE_ASK_NEW",
		"JINGLIAN_NEW_TRY",
		"JINGLIAN_NEW_DO",
		"JINGLIAN_PET",
		"SEE_NEWFRIEND_LIST",
		"EQU_PET_HUN",
		"PET_ADD_HUNPO",
		"PET_ADD_SHENGMINGVALUE",
		"HORSE_REMOVE_HUNPO",
		"PET_REMOVE_HUNPO",
		"PLAYER_CLEAN_HORSEHUNLIANG",
		"GET_NEW_LEVELUP",
		"DO_HOSEE2OTHER",
		"TRYDELETE_PET_INFO",
		"HAHA_ACTIVITY_MSG",
		"VALIDATE_NEW",
		"VALIDATE_ANDSWER_NEW",
		"PLAYER_ASK_TO_OTHWE",
		"GA_GET_SOME",
		"OTHER_PET_LEVELUP",
		"MY_OTHER_FRIEDN",
		"DOSOME_SB_MSG",
	};
	
	public static String msg = "SEND_HAND_MESSAGE_NEW";
	
//	public static String[] messageNameSS = {
//		"SERVER_HAND_CLIENT_1",
//		"SERVER_HAND_CLIENT_2",
//		"SERVER_HAND_CLIENT_3",
//		"SERVER_HAND_CLIENT_4",
//		"SERVER_HAND_CLIENT_5",
//		"SERVER_HAND_CLIENT_6",
//		"SERVER_HAND_CLIENT_7",
//		"SERVER_HAND_CLIENT_8",
//		"SERVER_HAND_CLIENT_9",
//		"SERVER_HAND_CLIENT_10",
//		"SERVER_HAND_CLIENT_11",
//		"SERVER_HAND_CLIENT_12",
//		"SERVER_HAND_CLIENT_13",
//		"SERVER_HAND_CLIENT_14",
//		"SERVER_HAND_CLIENT_15",
//		"SERVER_HAND_CLIENT_16",
//		"SERVER_HAND_CLIENT_17",
//		"SERVER_HAND_CLIENT_18",
//		"SERVER_HAND_CLIENT_19",
//		"SERVER_HAND_CLIENT_20",
//		"SERVER_HAND_CLIENT_21",
//		"SERVER_HAND_CLIENT_22",
//		"SERVER_HAND_CLIENT_23",
//		"SERVER_HAND_CLIENT_24",
//		"SERVER_HAND_CLIENT_25",
//		"SERVER_HAND_CLIENT_26",
//		"SERVER_HAND_CLIENT_27",
//		"SERVER_HAND_CLIENT_28",
//		"SERVER_HAND_CLIENT_29",
//		"SERVER_HAND_CLIENT_30",
//		"SERVER_HAND_CLIENT_31",
//		"SERVER_HAND_CLIENT_32",
//		"SERVER_HAND_CLIENT_33",
//		"SERVER_HAND_CLIENT_34",
//		"SERVER_HAND_CLIENT_35",
//		"SERVER_HAND_CLIENT_36",
//		"SERVER_HAND_CLIENT_37",
//		"SERVER_HAND_CLIENT_38",
//		"SERVER_HAND_CLIENT_39",
//		"SERVER_HAND_CLIENT_40",
//		"SERVER_HAND_CLIENT_41",
//		"SERVER_HAND_CLIENT_42",
//		"SERVER_HAND_CLIENT_43",
//		"SERVER_HAND_CLIENT_44",
//		"SERVER_HAND_CLIENT_45",
//		"SERVER_HAND_CLIENT_46",
//		"SERVER_HAND_CLIENT_47",
//		"SERVER_HAND_CLIENT_48",
//		"SERVER_HAND_CLIENT_49",
//		"SERVER_HAND_CLIENT_50",
//		"SERVER_HAND_CLIENT_51",
//		"SERVER_HAND_CLIENT_52",
//		"SERVER_HAND_CLIENT_53",
//		"SERVER_HAND_CLIENT_54",
//		"SERVER_HAND_CLIENT_55",
//		"SERVER_HAND_CLIENT_56",
//		"SERVER_HAND_CLIENT_57",
//		"SERVER_HAND_CLIENT_58",
//		"SERVER_HAND_CLIENT_59",
//		"SERVER_HAND_CLIENT_60",
//		"SERVER_HAND_CLIENT_61",
//		"SERVER_HAND_CLIENT_62",
//		"SERVER_HAND_CLIENT_63",
//		"SERVER_HAND_CLIENT_64",
//		"SERVER_HAND_CLIENT_65",
//		"SERVER_HAND_CLIENT_66",
//		"SERVER_HAND_CLIENT_67",
//		"SERVER_HAND_CLIENT_68",
//		"SERVER_HAND_CLIENT_69",
//		"SERVER_HAND_CLIENT_70",
//		"SERVER_HAND_CLIENT_71",
//		"SERVER_HAND_CLIENT_72",
//		"SERVER_HAND_CLIENT_73",
//		"SERVER_HAND_CLIENT_74",
//		"SERVER_HAND_CLIENT_75",
//		"SERVER_HAND_CLIENT_76",
//		"SERVER_HAND_CLIENT_77",
//		"SERVER_HAND_CLIENT_78",
//		"SERVER_HAND_CLIENT_79",
//		"SERVER_HAND_CLIENT_80",
//		"SERVER_HAND_CLIENT_81",
//		"SERVER_HAND_CLIENT_82",
//		"SERVER_HAND_CLIENT_83",
//		"SERVER_HAND_CLIENT_84",
//		"SERVER_HAND_CLIENT_85",
//		"SERVER_HAND_CLIENT_86",
//		"SERVER_HAND_CLIENT_87",
//		"SERVER_HAND_CLIENT_88",
//		"SERVER_HAND_CLIENT_89",
//		"SERVER_HAND_CLIENT_90",
//		"SERVER_HAND_CLIENT_91",
//		"SERVER_HAND_CLIENT_92",
//		"SERVER_HAND_CLIENT_93",
//		"SERVER_HAND_CLIENT_94",
//		"SERVER_HAND_CLIENT_95",
//		"SERVER_HAND_CLIENT_96",
//		"SERVER_HAND_CLIENT_97",
//		"SERVER_HAND_CLIENT_98",
//		"SERVER_HAND_CLIENT_99",
//		"SERVER_HAND_CLIENT_100",
//	};
	
//	public static String msg = "SEND_HAND_MESSAGE";
	
	public static String REQ_START = "0x00A3B0";
	public static String RES_START = "0x80A3B0";
//	public static String REQ_START = "0x0002A3";
//	public static String RES_START = "0x8002A3";
	
	public static void creatMessage () {
		int chooseNum = 10;
		String[] fuhao = new String[]{"+","-","*","/"};
		Random random = new Random();
		StringBuffer server_handle = new StringBuffer("");
		StringBuffer game_server = new StringBuffer("");
		StringBuffer client_handle = new StringBuffer("");
		StringBuffer message_gateway_handle = new StringBuffer("");
		for (int i = 0; i < messageNameSS.length; i++) {
			String messageName = messageNameSS[i];
			ArrayList<Integer> chooseIndexs = new ArrayList<Integer>();
			ArrayList<String> chooseFuHaos = new ArrayList<String>();
			for (int j = 0; j < chooseNum; j++) {
				int ranIndex = random.nextInt(100);
				while (chooseIndexs.contains(ranIndex)) {
					ranIndex = random.nextInt(100);
				}
				chooseIndexs.add(ranIndex);
				chooseFuHaos.add(fuhao[random.nextInt(4)]);
			}
//			System.out.println(Arrays.toString(chooseIndexs.toArray()) + "  " + Arrays.toString(chooseFuHaos.toArray()));
			String mN_Server = messageName + "_REQ";
			String mN_Client = messageName + "_RES";
			String temp = "public ResponseMessage handle_"+mN_Client+"(Connection conn, RequestMessage message1, ResponseMessage message2){\n";
			//如果req不是对应的req
			temp = temp+"\t"+"Object message_req = conn.getAttachmentData(\""+msg+"\");"+"\n";
			temp = temp+"\t"+"if (message_req == null || (!(message_req instanceof "+mN_Server+"))) {"+"\n";
			temp = temp+"\t\tnoFindReq(conn, message2, "+(i+1)+");\n";
			temp = temp+"\t\treturn null;\n";
			temp = temp+"\t}\n";
			temp = temp+"\t"+mN_Server+" req = ("+mN_Server+")message_req;\n";
			temp = temp+"\t"+mN_Client+" res = ("+mN_Client+")message2"+";\n";
			temp = temp+"\t"+"long re = res.getCreate();\n";
			temp = temp+"\tlong[] sendValues = req.getSendValues();"+"\n";
			temp = temp+"\t"+"long create = 0;"+"\n";
			temp = temp+"\tcreate = sendValues["+chooseIndexs.get(0)+"]";
			for (int j = 1; j < chooseNum; j++) {
				temp = temp+chooseFuHaos.get(j-1)+"sendValues["+chooseIndexs.get(j)+"]";
			}
			temp = temp+";\n";
			//如果计算出来的值和预计的不相等
			temp = temp+"\t"+"if (create != re) {"+"\n";
			temp = temp+"\t\tbackValueNE(conn, create, re, sendValues, "+(i+1)+");\n";
			temp = temp+"\t\t"+"return null;"+"\n";
			temp = temp+"\t}\n";
			temp = temp+"\t"+"sendUseLogin(conn, create, sendValues, "+(i+1)+");"+"\n";
			temp = temp+"\treturn null;\n";
			temp = temp+"}";
			server_handle.append(temp);
			server_handle.append("\n\n");
			
			//-----------------------------------------------------------------------------------------
			//------------------------------游戏服的-----------------------------------------------------------
			//-----------------------------------------------------------------------------------------
			temp = "";
			temp = "public static void handle_"+mN_Client+"(Connection conn, ResponseMessage message){\n";
			//如果req不是对应的req
			temp = temp+"\t"+"Object message_req = conn.getAttachmentData(\""+msg+"\");"+"\n";
			temp = temp+"\t"+"if (message_req == null || (!(message_req instanceof "+mN_Server+"))) {"+"\n";
			temp = temp+"\t\tnoFindReq(conn, message, "+(i+1)+");\n";
			temp = temp+"\t\treturn;\n";
			temp = temp+"\t}\n";
			temp = temp+"\t"+mN_Server+" req = ("+mN_Server+")message_req;\n";
			temp = temp+"\t"+mN_Client+" res = ("+mN_Client+")message"+";\n";
			temp = temp+"\t"+"long re = res.getCreate();\n";
			temp = temp+"\tlong[] sendValues = req.getSendValues();"+"\n";
			temp = temp+"\t"+"long create = 0;"+"\n";
			temp = temp+"\tcreate = sendValues["+chooseIndexs.get(0)+"]";
			for (int j = 1; j < chooseNum; j++) {
				temp = temp+chooseFuHaos.get(j-1)+"sendValues["+chooseIndexs.get(j)+"]";
			}
			temp = temp+";\n";
			//如果计算出来的值和预计的不相等
			temp = temp+"\t"+"if (create != re) {"+"\n";
			temp = temp+"\t\tbackValueNE(conn, create, re, sendValues, "+(i+1)+");\n";
			temp = temp+"\t\t"+"return;"+"\n";
			temp = temp+"\t}\n";
			temp = temp+"\t"+"sendEnterServer(conn, create, sendValues, "+(i+1)+");"+"\n";
			temp = temp+"}";
			game_server.append(temp);
			game_server.append("\n\n");
			
			//-----------------------------------------------------------------------------------------
			//------------------------------客户端的---------------------------------------------------
			//-----------------------------------------------------------------------------------------
			
			temp = "";
			temp = temp+"void LoginModuleMessageHandlerImpl::handle_"+mN_Server+"(int seqNum, vector<long long> &backValues){\n";
			{
				//生成无用代码
				int fg = 1 + random.nextInt(1);
				for (int qq = 0; qq < fg; qq++) {
					int type = random.nextInt(6);
					if (type == 0) {
						temp = temp+"\tint version"+qq+"=0;\n";
						temp = temp+"\tversion"+qq+" = backValues[0]";
						int num = 1 + random.nextInt(20);
						for (int j = 1; j < num; j++) {
							int fu = random.nextInt(4);
							int in = random.nextInt(100);
							temp = temp+fuhao[fu]+"backValues["+in+"]";
						}
						temp = temp+";\n";
					}else if (type == 1) {
						temp = temp+"\tint re"+qq+"=0;\n";
						int num = 1 + random.nextInt(15);
						temp = temp+"\tfor(int i=0; i<"+num+"; i++){\n";
						temp = temp+"\t\tre"+qq+" += backValues[i];\n";
						temp = temp+"\t}\n";
					}else if (type == 2) {
						int cNum = 10 + random.nextInt(20);
						temp = temp+"\t"+"char c"+qq+"["+cNum+"]"+";\n";
						int fNum = 3 + random.nextInt(9);
						temp = temp+"\tfor(int i = 0; i < "+fNum+"; i++){\n";
						temp = temp+"\t\t"+"sprintf(c"+qq+", \"%lld\", backValues[i])"+";\n";
						temp = temp+"\t}\n";
						temp = temp+"\tstring ss"+qq+" = c"+qq+";\n";
					}else if (type == 3) {
						temp = temp+"\t"+"vector<int> vec"+qq+";"+"\n";
						int fNum = 3 + random.nextInt(9);
						temp = temp+"\tfor(int i = 0; i < "+fNum+"; i++){\n";
						temp = temp+"\t\t"+"vec"+qq+".push_back(backValues[i])"+";\n";
						temp = temp+"\t}\n";
						temp = temp+"\t"+"vec"+qq+".pop_back();"+"\n";
					}else if (type == 4) {
						long aa = random.nextLong() % 5000000;
						temp = temp+"\t"+"long long ll"+qq+"="+aa+"L"+";\n";
						int fNum = 1 + random.nextInt(15);
						for (int j = 0; j <fNum; j++) {
							int rr = random.nextInt(40);
							temp = temp+"\t"+"ll"+qq+" += backValues["+rr+"]"+";\n";
						}
						temp = temp+"\t"+"\n";
					}else if (type == 5) {
						temp = temp + "\t" + "string st = \"try_asd\";\n";
						int fNum = 13 + random.nextInt(19);
						temp = temp +"\t"+"st.append(MSUtil::intToString(backValues["+fNum+"]));\n";
					}
				}
			}
			temp = temp+"\t"+"long long create = 0;"+"\n";
			temp = temp+"\tcreate = backValues["+chooseIndexs.get(0)+"]";
			for (int j = 1; j < chooseNum; j++) {
				temp = temp+chooseFuHaos.get(j-1)+"backValues["+chooseIndexs.get(j)+"]";
			}
			temp = temp+";\n";
			temp = temp+"\t"+"if (sendGateWay) {"+"\n";
			temp = temp+"\t\t"+"DataEnvironment::netImpl->sendGatewayMessage(GameMessageFactory::construct_"+mN_Client+"(seqNum, create),false);"+"\n";
			temp = temp+"\t}else {\n";
			temp = temp+"\t\t"+"DataEnvironment::netImpl->sendServerMessage(GameMessageFactory::construct_"+mN_Client+"(seqNum, create),false);"+"\n";
			temp = temp+"\t}\n";
			{
				//生成无用代码
				int fg = 1 + random.nextInt(1);
				for (int qq = 0; qq < fg; qq++) {
					int type = random.nextInt(6);
					if (type == 0) {
						temp = temp+"\tint version_"+qq+"=0;\n";
						temp = temp+"\tversion_"+qq+" = backValues[0]";
						int num = 1 + random.nextInt(20);
						for (int j = 1; j < num; j++) {
							int fu = random.nextInt(4);
							int in = random.nextInt(100);
							temp = temp+fuhao[fu]+"backValues["+in+"]";
						}
						temp = temp+";\n";
					}else if (type == 1) {
						temp = temp+"\tint re_"+qq+"=0;\n";
						int num = 1 + random.nextInt(15);
						temp = temp+"\tfor(int i=0; i<"+num+"; i++){\n";
						temp = temp+"\t\tre_"+qq+" += backValues[i];\n";
						temp = temp+"\t}\n";
					}else if (type == 2) {
						int cNum = 30 + random.nextInt(50);
						temp = temp+"\t"+"char c_"+qq+"["+cNum+"]"+";\n";
						int fNum = 3 + random.nextInt(9);
						temp = temp+"\tfor(int i = 0; i < "+fNum+"; i++){\n";
						temp = temp+"\t\t"+"sprintf(c_"+qq+", \"%lld\", backValues[i])"+";\n";
						temp = temp+"\t}\n";
						temp = temp+"\tstring ss_"+qq+" = c_"+qq+";\n";
					}else if (type == 3) {
						temp = temp+"\t"+"vector<int> vec_"+qq+";"+"\n";
						int fNum = 3 + random.nextInt(9);
						temp = temp+"\tfor(int i = 0; i < "+fNum+"; i++){\n";
						temp = temp+"\t\t"+"vec_"+qq+".push_back(backValues[i])"+";\n";
						temp = temp+"\t}\n";
						temp = temp+"\t"+"vec_"+qq+".pop_back();"+"\n";
					}else if (type == 4) {
						long aa = random.nextLong() % 5000000;
						temp = temp+"\t"+"long long ll_"+qq+"="+aa+"L"+";\n";
						int fNum = 1 + random.nextInt(15);
						for (int j = 0; j <fNum; j++) {
							int rr = random.nextInt(40);
							temp = temp+"\t"+"ll_"+qq+" += backValues["+rr+"]"+";\n";
						}
						temp = temp+"\t"+"\n";
					}else if (type == 5) {
						int in = random.nextInt(60);
						temp = temp+"\tMessage* m_"+qq+" = GameMessageFactory::construct_"+mN_Client+"(seqNum, backValues["+in+"]);"+"\n";
						temp = temp+"\tdelete m_"+qq+";\n";
					}
				}
			}
			temp = temp+"}\n\n";
			client_handle.append(temp);
			
			//生成message_gateway.xml
			String[] abc = {"A","B","C","D","E","F"};
			int gewei = i%16;
			int shiwei = i/16%16;
			String geweiS = gewei < 10 ? gewei+"" : abc[gewei%10];
			String shiweiS = shiwei < 10 ? shiwei+"" : abc[shiwei%10];
			String reqS = REQ_START+""+shiweiS+""+geweiS;
			String resS = RES_START+""+shiweiS+""+geweiS;
			message_gateway_handle.append("\t<prototype req_name=\""+mN_Server+"\" req_type=\""+reqS+"\" " + 
					"res_name=\""+mN_Client+"\" res_type=\""+resS+"\" req_client_receive=\"true\" res_client_send=\"true\" comment=\"\" >\n");
			message_gateway_handle.append("\t\t<req>\n");
			message_gateway_handle.append("\t\t\t<property name=\"sendValues\" type=\"long[]\" />\n");
			message_gateway_handle.append("\t\t</req>\n");
			message_gateway_handle.append("\t\t<res>\n");
			message_gateway_handle.append("\t\t\t<property name=\"create\" type=\"long\" />\n");
			message_gateway_handle.append("\t\t</res>\n");
			message_gateway_handle.append("\t</prototype>\n\n");
		}
		try{
			File f = new File("E://server_handle");
			FileOutputStream sFout = new FileOutputStream(f);
			sFout.write(server_handle.toString().getBytes("UTF-8"));
			sFout.close();
			f = new File("E://gameserver_handle");
			FileOutputStream gFout = new FileOutputStream(f);
			gFout.write(game_server.toString().getBytes("UTF-8"));
			gFout.close();
			f = new File("E://client_handle");
			FileOutputStream cFout = new FileOutputStream(f);
			cFout.write(client_handle.toString().getBytes("UTF-8"));
			cFout.close();
			f = new File("E://message_gatewayxml");
			FileOutputStream mxFout = new FileOutputStream(f);
			mxFout.write(message_gateway_handle.toString().getBytes("UTF-8"));
			mxFout.close();
		}catch(Exception e) {
			
		}
	}
	
	/*
	 * 说明，这个方法会在E盘生成4个文件，分别是  网关的，游戏服的，协议xml，客户端的。
	 * 现在分2份协议，这个用这份 下次用另外一份。
	 * */
	public static void main (String[] args) {
		creatMessage();
	}
}

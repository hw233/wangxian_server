package com.fy.boss.cmd;

import com.fy.boss.cmd.message.CMDMessageFactory;
import com.fy.boss.cmd.message.COMMON_CMD_REQ;
import com.fy.boss.cmd.message.COMMON_CMD_RES;
import com.fy.boss.cmd.message.FILE_PACKET_REQ;
import com.fy.boss.cmd.message.FILE_PACKET_RES;
import com.fy.boss.cmd.CMDConnector;
import com.fy.boss.cmd.TestClient;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;

public class TestClient {
	public void startTest(String host, int port) {
		DefaultConnectionSelector selector = new com.xuanzhi.tools.transport.DefaultConnectionSelector();
		selector.setEnableHeapForTimeout(true);
		selector.setName("CMDConnectorClient");
		selector.setClientModel(true);
		selector.setPort(port);
		selector.setHost(host);
		CMDConnector conn = new CMDConnector(selector);
		selector.setConnectionConnectedHandler(conn);
		try {
			selector.init();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//传输
		String file = "d:\\game_init_config.tgz";
		byte data[] = FileUtils.readFileData(file);
		int packetSize = 4096;
		int total = data.length/packetSize;
		if(data.length % packetSize != 0) {
			total++;
		}
		String savepath = "/home/game/agent/game_init_config.tgz";
		String filename = "game_init_config.tgz";
		boolean succ = false;
		for(int i=0; i<total; i++) {
			int len = data.length - packetSize*i;
			if(len > packetSize) {
				len = packetSize;
			}
			byte sdata[] = new byte[len];
			System.arraycopy(data, i*packetSize, sdata, 0, len);
			FILE_PACKET_REQ req = new FILE_PACKET_REQ(CMDMessageFactory.nextSequnceNum(), sdata, total, i, filename, savepath);
			try {
				FILE_PACKET_RES res = (FILE_PACKET_RES)conn.requestMessage(req);
				String result = res.getResult()[0];
				System.out.println("[传送文件数据] [total:"+total+"] [index:"+i+"] 结果:\n" + result);
				if( i == total-1 ) {
					//传的最后一个
					if(result.indexOf("succ") != -1) {
						succ = true;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(succ) {
			String cmd = "tar xzvf /home/game/agent/game_init_config.tgz";
			COMMON_CMD_REQ req = new COMMON_CMD_REQ(CMDMessageFactory.nextSequnceNum() , cmd);
			try {
				COMMON_CMD_RES res = (COMMON_CMD_RES)conn.requestMessage(req);
				String result = res.getResult()[0];
				System.out.println("[执行命令] ["+cmd+"] 结果:\n" + result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/*
		for(int i=0; i<1000; i++) {
			
			COMMON_CMD_REQ req = new COMMON_CMD_REQ(CMDMessageFactory.nextSequnceNum() , "cmd-" + i);
			try {
				Thread.sleep(1000);
				COMMON_CMD_RES res = (COMMON_CMD_RES)conn.requestMessage(req);
				if(res != null) {
					System.out.println("[RESP] ["+res.getResult()[0]+"]");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
	}
	
	public static void main(String args[]) {
		String host = "221.179.174.53";
		int port = 4321;
		if(args.length > 1) {
			host = args[0];
			port = Integer.parseInt(args[1]);
		}
		TestClient client = new TestClient();
		client.startTest(host, port);
	}
}

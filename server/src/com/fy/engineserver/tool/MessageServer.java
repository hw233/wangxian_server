package com.fy.engineserver.tool;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.monster.MonsterManager;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager.MonsterTempalte;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.fy.engineserver.sprite.npc.MemoryNPCManager.NPCTempalte;
import com.fy.engineserver.tool.message.DefaultMessageFactory;
import com.fy.engineserver.tool.message.MONSTER_REQ;
import com.fy.engineserver.tool.message.MONSTER_RES;
import com.fy.engineserver.tool.message.NPC_REQ;
import com.fy.engineserver.tool.message.NPC_RES;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionConnectedHandler;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;
import com.xuanzhi.tools.transport.MessageHandler;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class MessageServer implements ConnectionConnectedHandler, MessageHandler{
	private DefaultConnectionSelector selector ;
	private int port = 7669;
//	static Logger logger = Logger.getLogger(MessageServer.class);
public	static Logger logger = LoggerFactory.getLogger(MessageServer.class);
	public MessageServer(){
		
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void init() throws Exception{
		
		if(VerifyServer.getInstance().isOpenBJQ()){
			selector = new DefaultConnectionSelector();
			selector.setConnectionSendBufferSize(5 * 1024 * 1024);
			selector.setClientModel(false);
			selector.setPort(port);
			selector.setConnectionConnectedHandler(this);
			selector.init();
			System.out.println("================ToolMessageServer init finish================");
		}
		ServiceStartRecord.startLog(this);
	}
	
	public void connected(Connection conn) throws IOException {
		// TODO Auto-generated method stub
		conn.setMessageFactory(DefaultMessageFactory.getInstance());
		conn.setMessageHandler(this);
	}

	public void discardRequestMessage(Connection arg0, RequestMessage arg1)
			throws ConnectionException {
		// TODO Auto-generated method stub
		System.out.println("Tool服务器丢弃请求[" + arg1.getTypeDescription() + "]");
	}

	public ResponseMessage receiveRequestMessage(Connection arg0,
			RequestMessage request) throws ConnectionException {
		// TODO Auto-generated method stub
		if(request instanceof NPC_REQ){
//			System.out.println("收到请求NPC_REQ数据请求");
			NPCManager nm = MemoryNPCManager.getNPCManager();
			NPCTempalte[] nts = ((MemoryNPCManager)nm).getNPCTemaplates();
			NPC[] npcs = new NPC[0];
			if(nts != null){
				npcs = new NPC[nts.length];
				for(int i = 0; i < nts.length; i++){
					NPCTempalte nt = nts[i];
					if(nt != null){
						npcs[i] = nt.npc;
					}
				}
			}
			
			return new NPC_RES(request.getSequnceNum(), npcs);
		}
		if(request instanceof MONSTER_REQ){
//			System.out.println("收到请求MONSTER_REQ数据请求");
			MonsterManager nm = MemoryMonsterManager.getMonsterManager();
			MonsterTempalte[] nts = ((MemoryMonsterManager)nm).getMonsterTemaplates();
			Monster[] monsters = new Monster[0];
			if(nts != null){
				monsters = new Monster[nts.length];
				for(int i = 0; i < nts.length; i++){
					MonsterTempalte nt = nts[i];
					if(nt != null){
						monsters[i] = nt.monster;
					}
				}
			}
			
			return new MONSTER_RES(request.getSequnceNum(), monsters);
		}
		return null;
	}

	public void receiveResponseMessage(Connection arg0, RequestMessage arg1,
			ResponseMessage arg2) throws ConnectionException {
		// TODO Auto-generated method stub
//		System.out.println("Tool服务器收到响应[" + arg2.getTypeDescription() + "]");
	}

	public RequestMessage waitingTimeout(Connection arg0, long arg1)
			throws ConnectionException {
		// TODO Auto-generated method stub
		return null;
	}

}


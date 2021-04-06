package com.xuanzhi.tools.transport2.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.Message;
import com.xuanzhi.tools.transport2.CHAT_MESSAGE_TEST_REQ;
import com.xuanzhi.tools.transport2.CHAT_MESSAGE_TEST_RES;
import com.xuanzhi.tools.transport2.Connection2;
import com.xuanzhi.tools.transport2.ConnectionCreatedHandler2;
import com.xuanzhi.tools.transport2.DefaultConnectionSelector2;
import com.xuanzhi.tools.transport2.GameMessageFactory;
import com.xuanzhi.tools.transport2.MessageHandler2;




public class ChatClient implements ConnectionCreatedHandler2,MessageHandler2,Runnable{

	GameMessageFactory factory = new GameMessageFactory();

	List<Player> playerList = new ArrayList<Player>();
	
	Thread thread = null;
	
	public synchronized void addPlayer(Player player){
		for(Player p : playerList){
			if(p.id == player.id){
				return;
			}
		}
		playerList.add(player);
	}
	
	public synchronized void removePlayer(Player player){
		Player tmp = null;
		for(Player p : playerList){
			if(p.id == player.id){
				tmp = p;
				break;
			}
		}
		if(tmp != null){
			playerList.remove(tmp);
		}
	}
	
	public synchronized Player getPlayer(int playerId){
		Player tmp = null;
		for(Player p : playerList){
			if(p.id == playerId){
				tmp = p;
				break;
			}
		}
		return tmp;
	}
	
	public synchronized List<Player> getOnlinePlayers(){
		List<Player> list = new ArrayList<Player>();
		
		for(Player p : playerList){
			if(p.conn != null && p.conn.getState() == Connection2.CONN_STATE_CONNECT){
				
				list.add(p);
			}
		}
		return list;
	}
	
	@Override
	public void created(Connection2 conn, String attachment) throws IOException {
		conn.setMessageFactory(factory);
		conn.setMessageHandler(this);
		
	}

	@Override
	public void discardMessage(Connection2 conn, Message req)
			throws ConnectionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveMessage(Connection2 conn, Message message)
			throws ConnectionException {
		if(message instanceof CHAT_MESSAGE_TEST_REQ){
			CHAT_MESSAGE_TEST_REQ req = (CHAT_MESSAGE_TEST_REQ)message;
			Player player = (Player)conn.getAttachment();
			//System.out.println("["+player.name+"] : ["+req.getSpriteDesp()+"] ["+req.getSpriteName()+"] ["+req.getMessage()+"]");
		}else if(message instanceof CHAT_MESSAGE_TEST_RES){
			CHAT_MESSAGE_TEST_RES res = (CHAT_MESSAGE_TEST_RES)message;
			Player player = (Player)conn.getAttachment();
			//System.out.println("["+player.name+"] : 【发送成功】");
		}
		
	}

	@Override
	public void waitingTimeout(Connection2 conn, long timeout)
			throws ConnectionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		long lastHeartbeatTime = System.currentTimeMillis();
		
		long lastPrintTime = System.currentTimeMillis();
		long lastReceiveMessageNum = 0;
		long lastReceiveMessagePacketSize = 0;
		long lastSendMessageNum = 0;
		long lastSendMessagePacketSize = 0;
		int count = 0;
		while(thread.isInterrupted() == false){
			try{
				Thread.sleep(100);
				
				for(Player p : playerList){
					long deltaT = System.currentTimeMillis() - lastHeartbeatTime;
					try{
						p.heartbeat(deltaT);
					}catch(Exception e){
						System.out.println("["+p.name+"] [心跳出现异常]");
						e.printStackTrace();
					}
				}
				
				if(System.currentTimeMillis() -lastPrintTime > 1000L ){
					count++;
					int onlineNum = this.getOnlinePlayers().size();
					int totalNum = this.playerList.size();
					
					long receiveMessageNum = 0;
					long receiveMessagePacketSize = 0;
					long sendMessageNum = 0;
					long sendMessagePacketSize = 0;
					
					synchronized(this){
						for(Player p : playerList){
							if(p.conn != null){
								receiveMessageNum += p.conn.getReceiveMessageNum();
								receiveMessagePacketSize += p.conn.getReceiveMessagePacketSize();
								sendMessageNum += p.conn.getSendMessageNum();
								sendMessagePacketSize += p.conn.getSendMessagePacketSize();
							}
						}
					}
					
					long deltaT = System.currentTimeMillis() - lastPrintTime;
	
					System.out.println("[CHATCLIENT] [编号:"+count+"] [在线:"+onlineNum+"] [离线:"+(totalNum - onlineNum)+"] "
							+"[接受速度:"+((receiveMessageNum-lastReceiveMessageNum)*1000/deltaT)+"/秒] [接受流量:"+((receiveMessagePacketSize-lastReceiveMessagePacketSize)*1000/deltaT/1024)+"K/秒] [发送速度:"+((sendMessageNum-lastSendMessageNum)*1000/deltaT)+"/秒] [发送流量:"+((sendMessagePacketSize-lastSendMessagePacketSize)*1000/deltaT/1024)+"K/秒] "
							+"[总接受包:"+receiveMessageNum+"] [总接受流量:"+(receiveMessagePacketSize/1024/1024)+"M] [总发送包:"+sendMessageNum+"] [总发送流量:"+(sendMessagePacketSize/1024/1024)+"M]");
	
					lastReceiveMessageNum = receiveMessageNum;
					lastReceiveMessagePacketSize = receiveMessagePacketSize;
					lastSendMessageNum = sendMessageNum;
					lastSendMessagePacketSize = sendMessagePacketSize;
					lastPrintTime = System.currentTimeMillis();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	DefaultConnectionSelector2 selector;
	
	public void startClient(String host,int port,int playerNum) throws Exception{
		selector = new DefaultConnectionSelector2(host,port,true);
		selector.setName("ChatClient");
		selector.setConnectionCreatedHandler(this);
		selector.init();
		
		for(int i = 0 ; i < playerNum ; i++){
			Player p = new Player(this);
			p.id = Integer.parseInt(StringUtil.randomIntegerString(9));
			p.name = p.id + "";
			
			this.addPlayer(p);
		}
		
		thread = new Thread(this);
		thread.start();
	}
	

	public static void main(String args[]) throws Exception{
		String userDir = System.getProperty("user.dir");
		System.getProperties().setProperty("logback.configurationFile", userDir + "/src/test/com/xuanzhi/tools/transport2/client/logback.xml");
		
		
		ChatClient cc = new ChatClient();
		cc.startClient("localhost", 10234, 256);
	}
}

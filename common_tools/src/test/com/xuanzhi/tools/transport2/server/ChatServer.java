package com.xuanzhi.tools.transport2.server;

import java.io.IOException;

import ch.qos.logback.classic.BasicConfigurator;

import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.Message;
import com.xuanzhi.tools.transport2.Connection2;
import com.xuanzhi.tools.transport2.ConnectionConnectedHandler2;
import com.xuanzhi.tools.transport2.DefaultConnectionSelector2;
import com.xuanzhi.tools.transport2.GameMessageFactory;
import com.xuanzhi.tools.transport2.MessageHandler2;
import com.xuanzhi.tools.transport2.CHAT_MESSAGE_TEST_REQ;
import com.xuanzhi.tools.transport2.CHAT_MESSAGE_TEST_RES;

import java.util.*;

public class ChatServer implements ConnectionConnectedHandler2,MessageHandler2,Runnable{

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
	public void connected(Connection2 conn) throws IOException {
		conn.setMessageFactory(factory);
		conn.setMessageHandler(this);
		
		//System.out.println("[有新链接] ["+conn.getName()+"] ["+conn.getRemoteAddress()+"]");
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
			int playerId = req.getSpriteId();
			String playerName = req.getSpriteName();
			String content = req.getMessage();
			
			Player p = this.getPlayer(playerId);
			
			if(p == null){
				p = new Player();
				p.id = playerId;
				p.name = playerName;
				p.conn = conn;
				
				this.addPlayer(p);
			}
			
			List<Player> list = this.getOnlinePlayers();
			for(Player pp : list){
				CHAT_MESSAGE_TEST_REQ req2 = new CHAT_MESSAGE_TEST_REQ(GameMessageFactory.nextSequnceNum(),p.id,p.name,"系统转发",content);
				try {
					pp.conn.sendMessage(req2);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			CHAT_MESSAGE_TEST_RES res = new CHAT_MESSAGE_TEST_RES(req.getSequnceNum(),true);
			try {
				conn.sendMessage(res);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//System.out.println("[有新链接] ["+conn.getName()+"] ["+conn.getRemoteAddress()+"]");
		}else if(message instanceof CHAT_MESSAGE_TEST_RES){
			
		}
	}

	@Override
	public void waitingTimeout(Connection2 conn, long timeout)
			throws ConnectionException {
		CHAT_MESSAGE_TEST_REQ req = new CHAT_MESSAGE_TEST_REQ(GameMessageFactory.nextSequnceNum(),0,"系统","系统提示","Hi,你已经很久没有说话了，不说点什么吗？");
		try {
			conn.sendMessage(req);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//===================================================================
	
	
	DefaultConnectionSelector2 selector;
	
	public void startServer(int port) throws Exception{
		selector = new DefaultConnectionSelector2(null,port,false);
		selector.setName("ChatServer");
		selector.setConnectionConnectedHandler(this);
		selector.init();
		
		thread = new Thread(this);
		thread.start();
	}
	
	public void run(){
		long startServerTime = System.currentTimeMillis();
		
		long lastPrintTime = System.currentTimeMillis();
		long lastReceiveMessageNum = 0;
		long lastReceiveMessagePacketSize = 0;
		long lastSendMessageNum = 0;
		long lastSendMessagePacketSize = 0;
		int count = 0;
		while(thread.isInterrupted() == false){
			try{
				Thread.sleep(5000);
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

				System.out.println("[CHATSERVER] [编号:"+count+"] [在线:"+onlineNum+"] [离线:"+(totalNum - onlineNum)+"] "
						+"[接受速度:"+((receiveMessageNum-lastReceiveMessageNum)*1000/deltaT)+"/秒] [接受流量:"+((receiveMessagePacketSize-lastReceiveMessagePacketSize)*1000/deltaT/1024)+"K/秒] [发送速度:"+((sendMessageNum-lastSendMessageNum)*1000/deltaT)+"/秒] [发送流量:"+((sendMessagePacketSize-lastSendMessagePacketSize)*1000/deltaT/1024)+"K/秒] "
						+"[总接受包:"+receiveMessageNum+"] [总接受流量:"+(receiveMessagePacketSize/1024/1024)+"M] [总发送包:"+sendMessageNum+"] [总发送流量:"+(sendMessagePacketSize/1024/1024)+"M]");

				lastReceiveMessageNum = receiveMessageNum;
				lastReceiveMessagePacketSize = receiveMessagePacketSize;
				lastSendMessageNum = sendMessageNum;
				lastSendMessagePacketSize = sendMessagePacketSize;
				lastPrintTime = System.currentTimeMillis();
				
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	
	public static void main(String args[]) throws Exception{
		String userDir = System.getProperty("user.dir");
		System.out.println(userDir);
		System.getProperties().setProperty("logback.configurationFile", userDir + "/src/test/com/xuanzhi/tools/transport2/server/logback.xml");
		
		ChatServer server = new ChatServer();
		server.startServer(10234);
	}
}

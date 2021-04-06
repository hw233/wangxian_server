package com.xuanzhi.tools.transport2;

import java.nio.channels.SelectionKey;
import java.util.*;
/**
 * 帮助类
 *
 */
public class ConnectionSelectorHelper2 {

	
	public static Hashtable<String,DefaultConnectionSelector2> selectorMap = new Hashtable<String,DefaultConnectionSelector2>();
	
	public static DefaultConnectionSelector2 getSelector(String name){
		return selectorMap.get(name);
	}
	
	DefaultConnectionSelector2 selector;
	
	public ConnectionSelectorHelper2(DefaultConnectionSelector2 selector){
		this.selector = selector;
	}
	
	public int getWaitingQueueEventNum(){
		return selector.notifyQueueForWaitting.size();
	}

	public int getSendBufferEventNum(){
		return selector.notifyQueueForBuffer.size();
	}
	
	//有消息等待发送，但发送缓冲已经满的队列，消息堵塞非常严重了
	public int getConnectionNumOfSendBufferFullAndWaitingQueueNotEmpty(){
		return selector.notifyQueueCheckThreadForWaitting.holdList.size();
	}
	
	public String getConnectionSelectorName(){
		return selector.getName();
	}
	
	public int getConnectionNum(){
		if(selector.isClientModel())
			return selector.selector.keys().size() + selector.newConnQueue.size();
		else
			return selector.selector.keys().size() -1 + selector.newConnQueue.size();
	}
	
	public int getConnectionInSelectorNum(){
		if(selector.isClientModel())
			return selector.selector.keys().size();
		else
			return selector.selector.keys().size() - 1;
	}
	
	public long getTotalSendBuffer(){
		long ll = 0;
		Connection2[] conns = getAllConnections();
		for(int i = 0 ; i < conns.length ; i++){
			if(conns[i].sendDataBuffer == null) {
				continue;
			}
			ll +=conns[i].sendDataBuffer.capacity();
		}
		return ll;
	}
	
	public long getTotalReceiveBuffer(){
		long ll = 0;
		Connection2[] conns = getAllConnections();
		for(int i = 0 ; i < conns.length ; i++){
			if(conns[i].receiveDataBuffer == null) {
				continue;
			}
			ll +=conns[i].receiveDataBuffer.capacity();
		}
		return ll;
	}
	
	public int getCreatingConecttionNum(){
		return selector.newConnQueue.size() + selector.newingConnectionList.size();
	}
	
	public int getClosingConecttionNum(){
		return selector.closingConnQueue.size();
	}
	
	public Connection2[] getCreatingConnections(){
		ArrayList<Connection2> al = new ArrayList<Connection2>();
		al.addAll(selector.newingConnectionList);
		return (Connection2[])al.toArray(new Connection2[0]);
	}
	
	public Connection2[] getAllConnections(){
		ArrayList al = new ArrayList();
		Object obs[] = selector.selector.keys().toArray();
		for(int i = 0 ; i < obs.length ; i++){
			SelectionKey sk = (SelectionKey)obs[i];
			Connection2 c = (Connection2)sk.attachment();
			if(c != null){
				al.add(c);
			}
		}
		Collections.sort(al,new Comparator(){
			public int compare(Object o1, Object o2) {
				Connection2 c1 = (Connection2)o1;
				Connection2 c2 = (Connection2)o2;
				String s = "connection-";
				String name1 = c1.name.substring(s.length());
				String name2 = c2.name.substring(s.length());
				if(name1.indexOf("/") > 0){
					name1 = name1.substring(0,name1.indexOf("/"));
				}
				if(name2.indexOf("/") > 0){
					name2= name2.substring(0,name2.indexOf("/"));
				}
				int i1 = Integer.parseInt(name1);
				int i2 = Integer.parseInt(name2);
				if(i1 < i2) return -1;
				else if(i1 > i2) return 1;
				return 0;
			}
			
		});
		return(Connection2[])al.toArray(new Connection2[0]);
	}
	

	
	public int getCurrentSendBufferSize(Connection2 conn){
		if(conn.sendDataBuffer == null) {
			return 0;
		}
		return conn.sendDataBuffer.position();
	}
	
	public int getMaxSendBufferSize(Connection2 conn){
		if(conn.sendDataBuffer == null) {
			return 0;
		}
		return conn.sendDataBuffer.capacity();
	}
	
	public int getCurrentReceiveBufferSize(Connection2 conn){
		if(conn.receiveDataBuffer == null) {
			return 0;
		}
		return conn.receiveDataBuffer.position();
	}
	
	public int getMaxReceiveBufferSize(Connection2 conn){
		if(conn.receiveDataBuffer == null) {
			return 0;
		}
		return conn.receiveDataBuffer.capacity();
	}
	
	public String getName(Connection2 conn){
		return conn.name;
	}
	
	public String getStatus(Connection2 conn){
		return Connection2.getStateString(conn.getState());
	}
	
	public Date getLastReceiveDataTime(Connection2 conn){
		return new Date(conn.lastReceiveDataTime);
	}
	
	public int getSendMessageNum(Connection2 conn){
		return conn.sendMessageNum;
	}
	
	public int getReceiveMesageNum(Connection2 conn){
		return conn.receiveMessageNum;
	}
	
	public int getWaitingMessageNum(Connection2 conn){
		return conn.waitingQueue.size();
	}
	
	public String getConnectionOperation(Connection2 conn){
		int newInterestOps = conn.selector_operation;
		String operationStr = "";
		if((newInterestOps & SelectionKey.OP_READ) != 0){
			operationStr = "OP_READ";
		}
		if((newInterestOps & SelectionKey.OP_WRITE) != 0){
			if(operationStr.length() == 0){
				operationStr = "OP_WRITE";
			}else{
				operationStr = operationStr + "|OP_WRITE";
			}
		}
		return operationStr;
	}
	
	public long getIdleTime(Connection2 conn){
		return System.currentTimeMillis() - Math.max(conn.lastSendDataTime,conn.lastReceiveDataTime);
	}
	public int getTotalWaitingMessageNum(){
		int t = 0;
		Connection2[] conns = getAllConnections();
		for(int i = 0 ; i < conns.length ; i++){
			t += getWaitingMessageNum(conns[i]);
		}
		return t;
	}
	
	public Date getSelectorCreateTime(){
		return selector.createTime;
	}
	
	public long getTotalSendMesageNum(){
		return selector.totalSendMessageNum;
	}
	
	public long getTotalSendMessagePacketSize(){
		return selector.totalSendMessagePacketSize;
	}
	
	public long getTotalReceiveMessageNum(){
		return selector.totalReceiveMessageNum;
	}
	
	public long getTotalReceiveMessagePacketSize(){
		return selector.totalReceiveMessagePacketSize;
	}
	
	public long getLastStatNumAndPacketTime(){
		return selector.lastStatNumAndPacketTime;
	}
	
	//protected boolean statProtocolFlag = false;
	//type --> [数量，大小]
	//protected HashMap<String,long[]> sendMessageStatMap = null;
	//type --> [数量，大小]
	//protected HashMap<String,long[]> receiveMessageStatMap = null;
	
	public void setSelectorStatProtocolFlag(boolean b){
		selector.statProtocolFlag = b;
	}
	
	public boolean getSelectorStatProtocolFlag(){
		return selector.statProtocolFlag;
	}
	
	public HashMap<String,long[]> getSendMessageStatMap(){
		return selector.sendMessageStatMap;
	}
	
	public HashMap<String,long[]> getReceiveMessageStatMap(){
		return selector.receiveMessageStatMap;
	}
	
	public HashMap<String,long[]> getSendMessageStatMap(Connection2 conn){
		return conn.sendMessageStatMap;
	}
	
	public HashMap<String,long[]> getReceiveMessageStatMap(Connection2 conn){
		return conn.receiveMessageStatMap;
	}
}

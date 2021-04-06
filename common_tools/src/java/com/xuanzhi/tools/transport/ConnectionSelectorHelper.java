package com.xuanzhi.tools.transport;

import java.nio.channels.SelectionKey;
import java.util.*;
/**
 * 帮助类
 *
 */
public class ConnectionSelectorHelper {

	DefaultConnectionSelector selector;
	public ConnectionSelectorHelper(DefaultConnectionSelector selector){
		this.selector = selector;
	}
	
	public String getConnectionSelectorName(){
		return selector.getName();
	}
	
	public int getConnectionNum(){
		return selector.selector.keys().size() -1 ;
	}
	
	public int getConnectionInSelectorNum(){
		return selector.selectedNum;
	}
	
	public long getTotalSendBuffer(){
		long ll = 0;
		Connection[] conns = getAllConnections();
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
		Connection[] conns = getAllConnections();
		for(int i = 0 ; i < conns.length ; i++){
			if(conns[i].receiveDataBuffer == null) {
				continue;
			}
			ll +=conns[i].receiveDataBuffer.capacity();
		}
		return ll;
	}
	
	public int getCreatingConecttionNum(){
		return selector.newConnQueue.size() + selector.newConnectionList.size();
	}
	
	public int getClosingConecttionNum(){
		return selector.closingConnQueue.size();
	}
	
	public Connection[] getCreatingConnections(){
		ArrayList<Connection> al = new ArrayList<Connection>();
		al.addAll(selector.newConnectionList);
		return (Connection[])al.toArray(new Connection[0]);
	}
	
	public Connection[] getAllConnections(){
		ArrayList al = new ArrayList();
		Object obs[] = selector.selector.keys().toArray();
		for(int i = 0 ; i < obs.length ; i++){
			SelectionKey sk = (SelectionKey)obs[i];
			Connection c = (Connection)sk.attachment();
			if(c != null){
				al.add(c);
			}
		}
		Collections.sort(al,new Comparator(){
			public int compare(Object o1, Object o2) {
				Connection c1 = (Connection)o1;
				Connection c2 = (Connection)o2;
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
		return(Connection[])al.toArray(new Connection[0]);
	}
	
	public int getCurrentWindowSize(Connection conn){
		return conn.sendMessageWindow.size();
	}
	
	public int getMaxWindowSize(Connection conn){
		return conn.maxWindowSize;
	}
	
	public int getCurrentSendBufferSize(Connection conn){
		if(conn.sendDataBuffer == null) {
			return 0;
		}
		return conn.sendDataBuffer.position();
	}
	
	public int getMaxSendBufferSize(Connection conn){
		if(conn.sendDataBuffer == null) {
			return 0;
		}
		return conn.sendDataBuffer.capacity();
	}
	
	public int getCurrentReceiveBufferSize(Connection conn){
		if(conn.receiveDataBuffer == null) {
			return 0;
		}
		return conn.receiveDataBuffer.position();
	}
	
	public int getMaxReceiveBufferSize(Connection conn){
		if(conn.receiveDataBuffer == null) {
			return 0;
		}
		return conn.receiveDataBuffer.capacity();
	}
	
	public String getName(Connection conn){
		return conn.name;
	}
	
	public String getStatus(Connection conn){
		return Connection.getStateString(conn.getState());
	}
	
	public Date getLastReceiveDataTime(Connection conn){
		return new Date(conn.lastReceiveDataTime);
	}
	
	public boolean isCheckout(Connection conn){
		return conn.hasCheckout;
	}
	
	public long getIdleOrUsingTime(Connection conn){
		if(conn.hasCheckout){
			return System.currentTimeMillis() - conn.lastCheckoutTime;
		}else{
			return System.currentTimeMillis() - conn.lastReturnTime;
		}
	}
	
	public int getSendMessageNum(Connection conn){
		return conn.sendMessageNum;
	}
	
	public int getReceiveMesageNum(Connection conn){
		return conn.receiveMessageNum;
	}
}

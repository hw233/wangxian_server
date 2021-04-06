package com.xuanzhi.tools.transport;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * 在ConnectionSelector中选择一个Connection出来发送消息的策略类。
 * 
 *
 */
public interface SelectorPolicy {

	
	/**
	 * <pre>
	 * 策略：
	 *      只创建新的链接，并返回这个新的链接，如果不能创建链接（比如，已经达到链接数的上限，或者是服务端的模式），那么返回null，
	 *      如果在创建链接的时候，发现IOException，那么就抛出这个异常。
	 * </pre>     
	 */
	public static SelectorPolicy OnlyCreatePolicy = new OnlyCreateSelectorPolicy();
	
	/**
	 * <pre>
	 * 策略：
	 *      只选择处于CONN_STATE_WAITING_MESSAGE状态的链接，如果有多个链接处于CONN_STATE_WAITING_MESSAGE状态，
	 *      选择处在这个状态下，等待时间最久的那个链接。
	 *      如果所有的链接都不是处于CONN_STATE_WAITING_MESSAGE状态，那么返回null
	 * </pre>
	 */
	public static SelectorPolicy OnlyWaitMessagePolicy = new OnlyWaitMessageSelectorPolicy();
	
	/**
	 * <pre>
	 * 策略：
	 *      只选择处于CONN_STATE_WAITING_MESSAGE状态的链接，如果有多个链接处于CONN_STATE_WAITING_MESSAGE状态，
	 *      选择处在这个状态下，等待时间最久的那个链接。
	 *      如果所有的链接都不是处于CONN_STATE_WAITING_MESSAGE状态，那么就创建一个新的链接，并返回这个新创建的链接，
	 *      如果不能创建链接（比如，已经达到链接数的上限，或者是服务端的模式），那么返回null，
	 *      如果在创建链接的时候，发现IOException，那么就抛出这个异常。
	 * </pre>
	 */
	public static SelectorPolicy FirstWaitMessageThenCreatePolidy = new FirstWaitMessageThenCreateSelectorPolicy();
	
	/**
	 * <pre>
	 * 策略：
	 *      只选择处于CONN_STATE_WAITING_MESSAGE或者CONN_STATE_WAITING_REPLY状态，并且滑动窗口还没有满的链接，
	 *      如果有多个链接符合条件，那么选择滑动窗口最小的链接。
	 *      如果所有的链接不满足上述的条件，那么返回null
	 * </pre>
	 */
	public static SelectorPolicy OnlyWaitMessageAndReplyPolicy = new WaitMessageAndWaitReplySelectorPolicy();
	
	/**
	 * <pre>
	 * 策略：
	 *      如果链接数目仍然小于设定的链接数目的下限，那么就创建新的链接，并返回这个链接。
	 *      否则，就执行<code>FirstWaitMessageThenCreatePolidy</code>策略。
	 *      如果仍然没有能找到合适的链接，就执行<code>OnlyWaitMessageAndReplyPolicy</code>策略。
	 * </pre>     
	 * 
	 */
	public static SelectorPolicy DefaultClientModelPolicy = new DefaultClientModelSelectorPolicy();
	
	/**
	 * <pre>
	 * 策略：
	 *      先执行<code>OnlyWaitMessagePolicy</code>策略，
	 *      如果仍然没有能找到合适的链接，就执行<code>OnlyWaitMessageAndReplyPolicy</code>策略。
	 * </pre>
	 */
	public static SelectorPolicy DefaultServerModelPolicy = new DefaultServerModelSelectorPolicy();
	
	/**
	 * 
	 * 选择一个用于发送消息的链接出来，如果无法选择一个出来，则返回null
	 * 
	 * @param connectionSelector
	 * @return
	 * @throws IOException 当创建新的链接，如果出现IOException，就抛出来
	 */
	public Connection select(DefaultConnectionSelector connectionSelector) throws IOException;
	
	static class OnlyCreateSelectorPolicy implements SelectorPolicy{
		public Connection select(DefaultConnectionSelector connectionSelector) throws IOException {
			Selector selector = connectionSelector.selector;
			if(connectionSelector.isClientModel() && 
					selector.keys().size() < connectionSelector.maxConnectionNum){
				Connection conn = connectionSelector.createConnection(connectionSelector.host,connectionSelector.port);
				if(connectionSelector.enableTimepiece)
					conn.lastCheckoutTime = connectionSelector.timeOfTimePiece;
				else
					conn.lastCheckoutTime = System.currentTimeMillis();
				conn.hasCheckout = true;
				return conn;
			}
			return null;
		}
	}
	static class OnlyWaitMessageSelectorPolicy implements SelectorPolicy{
		public Connection select(DefaultConnectionSelector connectionSelector) throws IOException {
			Selector selector = connectionSelector.selector;
			SelectionKey keys[] = selector.keys().toArray(new SelectionKey[0]);
			
			SelectionKey selectedKey = null;
			for(int i = 0 ; i < keys.length ; i++){
				SelectionKey sk = keys[i];
				if(sk.isValid() == false) continue;
				if(sk.interestOps() == 0 || sk.attachment() == null) continue;
				Connection conn = (Connection)sk.attachment();
				if(conn.hasCheckout) continue;
				
				if(conn.state == Connection.CONN_STATE_WAITING_MESSAGE){
					if(selectedKey == null){
						selectedKey = sk;
					}else{
						Connection conn2 = (Connection)selectedKey.attachment();
						if(conn.getTimeForNextTimeout() < conn2.getTimeForNextTimeout() 
								&& System.currentTimeMillis() < conn.getTimeForNextTimeout()){
							selectedKey = sk;
						}
					}
				}
			}
		
			if(selectedKey != null){
				selectedKey.interestOps(0);
				Connection conn2 = (Connection)selectedKey.attachment();
				conn2.hasCheckout = true;
				if(connectionSelector.enableTimepiece)
					conn2.lastCheckoutTime = connectionSelector.timeOfTimePiece;
				else
					conn2.lastCheckoutTime = System.currentTimeMillis();
				connectionSelector.wakeupSelectorFlag = true;
				selector.wakeup();
				return conn2;
			}else{
				return null;
			}
		}
	}
	
	static class FirstWaitMessageThenCreateSelectorPolicy implements SelectorPolicy{
		public Connection select(DefaultConnectionSelector connectionSelector) throws IOException {
			Connection conn = OnlyWaitMessagePolicy.select(connectionSelector);
			if(conn != null) return conn;
			Selector selector = connectionSelector.selector;
			if(connectionSelector.isClientModel() && 
					selector.keys().size() + connectionSelector.getNumOfConnectionNotInSelector(null) < connectionSelector.maxConnectionNum){
				conn = connectionSelector.createConnection(connectionSelector.host,connectionSelector.port);
				if(connectionSelector.enableTimepiece)
					conn.lastCheckoutTime = connectionSelector.timeOfTimePiece;
				else
					conn.lastCheckoutTime = System.currentTimeMillis();
				conn.hasCheckout = true;
				return conn;
			}
			return null;
		}
	}

	static class WaitMessageAndWaitReplySelectorPolicy implements SelectorPolicy{
		public Connection select(DefaultConnectionSelector connectionSelector) throws IOException {
			Selector selector = connectionSelector.selector;
			SelectionKey keys[] = selector.keys().toArray(new SelectionKey[0]);
			
			SelectionKey selectedKey = null;
			for(int i = 0 ; i < keys.length ; i++){
				SelectionKey sk = keys[i];
				if(sk.isValid() == false) continue;
				if(sk.interestOps() == 0 || sk.attachment() == null) continue;
				Connection conn = (Connection)sk.attachment();
				if(conn.hasCheckout) continue;
				if(conn.state == Connection.CONN_STATE_WAITING_MESSAGE
						|| (conn.state == Connection.CONN_STATE_WAITING_REPLY && conn.sendMessageWindow.size() < conn.maxWindowSize)){
					if(selectedKey == null){
						selectedKey = sk;
					}else{
						Connection conn2 = (Connection)selectedKey.attachment();
						if(conn.sendMessageWindow.size() < conn2.sendMessageWindow.size()){
							selectedKey = sk;
						}
					}
				}
			}
		
			if(selectedKey != null){
				selectedKey.interestOps(0);
				Connection conn2 = (Connection)selectedKey.attachment();
				conn2.hasCheckout = true;
				if(connectionSelector.enableTimepiece)
					conn2.lastCheckoutTime = connectionSelector.timeOfTimePiece;
				else
					conn2.lastCheckoutTime = System.currentTimeMillis();
				connectionSelector.wakeupSelectorFlag = true;
				selector.wakeup();
				return conn2;
			}else{
				return null;
			}
		}
	}

	static class DefaultClientModelSelectorPolicy implements SelectorPolicy{
		public Connection select(DefaultConnectionSelector connectionSelector) throws IOException {
			Selector selector = connectionSelector.selector;
			if(selector.keys().size() < connectionSelector.minConnectionNum){
				Connection conn = connectionSelector.createConnection(connectionSelector.host,connectionSelector.port);
				conn.hasCheckout = true;
				if(connectionSelector.enableTimepiece)
					conn.lastCheckoutTime = connectionSelector.timeOfTimePiece;
				else
					conn.lastCheckoutTime = System.currentTimeMillis();
				return conn;
			}else{
				
				Connection conn = FirstWaitMessageThenCreatePolidy.select(connectionSelector);
				if(conn != null) return conn;
				conn = OnlyWaitMessageAndReplyPolicy.select(connectionSelector);
				return conn;
			}
		}
	}
	
	static class DefaultServerModelSelectorPolicy implements SelectorPolicy{
		public Connection select(DefaultConnectionSelector connectionSelector) throws IOException {
			Connection conn = OnlyWaitMessagePolicy.select(connectionSelector);
			if(conn != null) return conn;
			conn = OnlyWaitMessageAndReplyPolicy.select(connectionSelector);
			return conn;
		}
	}
	
	/**
	 * 根据链接来取链接，如果此链接有其他线程在用，那么取不到此链接。
	 * 
	 * Connection.CONN_STATE_WAITING_MESSAGE
	 * Connection.CONN_STATE_WAITING_REPLY
	 * Connection.CONN_STATE_WAITING_MORE_DATA
	 * Connection.CONN_STATE_WAITING_SEND_DATA
	 * Connection.CONN_STATE_WAITING_SEND_REPLY
	 * 
	 * 上述几种情况下，Connection都可以被拿出来使用
	 * 
	 * @author <a href='mailto:myzdf.bj@gmail.com'>Yugang Wang</a>
	 * 2008-8-28
	 */
	public static class ConnectionSelectorPolicy implements SelectorPolicy{
		Connection conn;
		public ConnectionSelectorPolicy(Connection conn){
			this.conn = conn;
		}
		
		public Connection select(DefaultConnectionSelector connectionSelector) throws IOException {
			Selector selector = connectionSelector.selector;
			SelectableChannel sc = (SelectableChannel)conn.channel;
			SelectionKey selectedKey = sc.keyFor(selector);
			if(selectedKey != null && selectedKey.isValid() == false){
				throw new IOException("selectedKey is canceled!");
			}
			
			if(selectedKey != null && selectedKey.interestOps() != 0){
				if(conn.state == Connection.CONN_STATE_WAITING_MESSAGE
					|| conn.state == Connection.CONN_STATE_WAITING_REPLY
					|| conn.state == Connection.CONN_STATE_WAITING_MORE_DATA
					|| conn.state == Connection.CONN_STATE_WAITING_SEND_DATA
					|| conn.state == Connection.CONN_STATE_WAITING_SEND_REPLY){
					
					selectedKey.interestOps(0);
					Connection conn2 = (Connection)selectedKey.attachment();
					conn2.hasCheckout = true;
					if(connectionSelector.enableTimepiece)
						conn2.lastCheckoutTime = connectionSelector.timeOfTimePiece;
					else
						conn2.lastCheckoutTime = System.currentTimeMillis();
					
					connectionSelector.wakeupSelectorFlag = true;
					selector.wakeup();
					return conn2;
				}
			}
			return null;
		}
	}
	
	/**
	 * 选择指定标识的链接，此策略不创建链接，只选择已经存在的链接
	 * 选择的策略是：
	 * 1. 必须没有checkout
	 * 2. 必须为指定标识
	 * 3. 符合1，2条件，链接状态如果是CONN_STATE_WAITING_MESSAGE，选择最近超时的
	 * 4. 符合1，2条件，无链接状态是CONN_STATE_WAITING_MESSAGE，选择链接是CONN_STATE_WAITING_REPLY且滑动窗口不满的，同时选择滑动窗口占用最小的
	 * @author <a href='mailto:myzdf.bj@gmail.com'>Yugang Wang</a>
	 * 2008-4-28
	 */
	public static class IdentitySelectorPolicy implements SelectorPolicy{
		String identity;
		public IdentitySelectorPolicy(String identity){
			this.identity = identity;
		}
		
		public Connection select(DefaultConnectionSelector connectionSelector) throws IOException {
			Selector selector = connectionSelector.selector;
			SelectionKey keys[] = selector.keys().toArray(new SelectionKey[0]);
		
			SelectionKey selectedKey = null;
			for(int i = 0 ; i < keys.length ; i++){
				SelectionKey sk = keys[i];
				if(sk.isValid() == false) continue;
				if(sk.interestOps() == 0 || sk.attachment() == null) continue;
				Connection conn = (Connection)sk.attachment();
				if(conn.hasCheckout) continue;
				if(identity.equals(conn.getIdentity()) == false) continue;
				
				if(conn.state == Connection.CONN_STATE_WAITING_MESSAGE){
					if(selectedKey == null){
						selectedKey = sk;
					}else{
						Connection conn2 = (Connection)selectedKey.attachment();
						if(conn.getTimeForNextTimeout() < conn2.getTimeForNextTimeout() 
								&& System.currentTimeMillis() < conn.getTimeForNextTimeout()){
							selectedKey = sk;
						}
					}
				}
			}
			
			if(selectedKey == null){
				
				for(int i = 0 ; i < keys.length ; i++){
					SelectionKey sk = keys[i];
					if(sk.isValid() == false) continue;
					if(sk.interestOps() == 0 || sk.attachment() == null) continue;
					Connection conn = (Connection)sk.attachment();
					if(conn.hasCheckout) continue;
					if(identity.equals(conn.getIdentity()) == false) continue;
					
					if((conn.state == Connection.CONN_STATE_WAITING_REPLY 
							&& conn.sendMessageWindow.size() < conn.maxWindowSize)){
						if(selectedKey == null){
							selectedKey = sk;
						}else{
							Connection conn2 = (Connection)selectedKey.attachment();
							if(conn.sendMessageWindow.size() < conn2.sendMessageWindow.size()){
								selectedKey = sk;
							}
						}
					}
				}
			}
		
			if(selectedKey != null){
				selectedKey.interestOps(0);
				Connection conn2 = (Connection)selectedKey.attachment();
				conn2.hasCheckout = true;
				if(connectionSelector.enableTimepiece)
					conn2.lastCheckoutTime = connectionSelector.timeOfTimePiece;
				else
					conn2.lastCheckoutTime = System.currentTimeMillis();
				
				connectionSelector.wakeupSelectorFlag = true;
				selector.wakeup();
				return conn2;
			}else{
				return null;
			}
		}
	} 

	/**
	 * 选择指定标识的链接，如果选择不到链接，就创建链接。
	 * 选择的策略是：
	 * 1. 必须没有checkout
	 * 2. 必须为指定标识
	 * 3. 符合1，2条件，链接状态如果是CONN_STATE_WAITING_MESSAGE，选择最近超时的
	 * 4. 符合1，2条件，无链接状态是CONN_STATE_WAITING_MESSAGE，选择链接是CONN_STATE_WAITING_REPLY且滑动窗口不满的，同时选择滑动窗口占用最小的
	 * 5. 创建链接
	 * @author <a href='mailto:myzdf.bj@gmail.com'>Yugang Wang</a>
	 * 2008-4-28
	 */
	public static class IdentitySelectorAndCreatePolicy implements SelectorPolicy{
		String identity;
		String host;
		int port;
		public IdentitySelectorAndCreatePolicy(String identity,String host,int port){
			this.identity = identity;
			this.host = host;
			this.port = port;
		}
		
		public Connection select(DefaultConnectionSelector connectionSelector) throws IOException {
			Selector selector = connectionSelector.selector;
			SelectionKey keys[] = selector.keys().toArray(new SelectionKey[0]);
			
			int count = 0;
			SelectionKey selectedKey = null;
			for(int i = 0 ; i < keys.length ; i++){
				SelectionKey sk = keys[i];
				if(sk.isValid() == false) continue;
				if(sk.attachment() != null){
					Connection conn = (Connection)sk.attachment();
					if(identity.equals(conn.getIdentity()))
						count++;
				}
				if(sk.interestOps() == 0 || sk.attachment() == null) continue;
				Connection conn = (Connection)sk.attachment();
				if(conn.hasCheckout) continue;
				if(identity.equals(conn.getIdentity()) == false) continue;
				
				if(conn.state == Connection.CONN_STATE_WAITING_MESSAGE){
					if(selectedKey == null){
						selectedKey = sk;
					}else{
						Connection conn2 = (Connection)selectedKey.attachment();
						if(conn.getTimeForNextTimeout() < conn2.getTimeForNextTimeout() 
								&& System.currentTimeMillis() < conn.getTimeForNextTimeout()){
							selectedKey = sk;
						}
					}
				}
			}
			if(selectedKey == null){
				if(connectionSelector.isClientModel() && count+connectionSelector.getNumOfConnectionNotInSelector(identity) < connectionSelector.maxConnectionNum){
					Connection conn = connectionSelector.createConnection(host,port);
					conn.setIdentity(identity);
					if(connectionSelector.enableTimepiece)
						conn.lastCheckoutTime = connectionSelector.timeOfTimePiece;
					else
						conn.lastCheckoutTime = System.currentTimeMillis();
					conn.hasCheckout = true;
					
					return conn;
				}
			}
			
			if(selectedKey == null){

				for(int i = 0 ; i < keys.length ; i++){
					SelectionKey sk = keys[i];
					if(sk.isValid() == false) continue;
					if(sk.interestOps() == 0 || sk.attachment() == null) continue;
					Connection conn = (Connection)sk.attachment();
					if(conn.hasCheckout) continue;
					if(identity.equals(conn.getIdentity()) == false) continue;
					
					if((conn.state == Connection.CONN_STATE_WAITING_REPLY 
							&& conn.sendMessageWindow.size() < conn.maxWindowSize)){
						if(selectedKey == null){
							selectedKey = sk;
						}else{
							Connection conn2 = (Connection)selectedKey.attachment();
							if(conn.sendMessageWindow.size() < conn2.sendMessageWindow.size()){
								selectedKey = sk;
							}
						}
					}
				}
			}
		
			if(selectedKey != null){
				selectedKey.interestOps(0);
				Connection conn2 = (Connection)selectedKey.attachment();
				conn2.hasCheckout = true;
				if(connectionSelector.enableTimepiece)
					conn2.lastCheckoutTime = connectionSelector.timeOfTimePiece;
				else
					conn2.lastCheckoutTime = System.currentTimeMillis();
				
				connectionSelector.wakeupSelectorFlag = true;
				selector.wakeup();
				return conn2;
			}else{
				
				if(connectionSelector.isClientModel() && count+connectionSelector.getNumOfConnectionNotInSelector(identity) < connectionSelector.maxConnectionNum){
					Connection conn = connectionSelector.createConnection(host,port);
					conn.setIdentity(identity);
					if(connectionSelector.enableTimepiece)
						conn.lastCheckoutTime = connectionSelector.timeOfTimePiece;
					else
						conn.lastCheckoutTime = System.currentTimeMillis();
					conn.hasCheckout = true;
					
					return conn;
				}else{
					return null;
				}
			}
		}
	} 
}

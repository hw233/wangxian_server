package com.fy.gamegateway.mieshi.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.gamegateway.message.GameMessageFactory;
import com.xuanzhi.tools.queue.DefaultSelectableQueue;
import com.xuanzhi.tools.queue.RoundrobinQueueSelector;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionConnectedHandler;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.ConnectionSelector;
import com.xuanzhi.tools.transport.ConnectionTerminateHandler;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;
import com.xuanzhi.tools.transport.Message;
import com.xuanzhi.tools.transport.MessageHandler;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;
import com.xuanzhi.tools.transport.SelectorPolicy;

/**
 * 此类处理用户发送网关的各种协议
 * 
 * 
 *
 */
public class MieshiGatewayServer implements ConnectionConnectedHandler,
	ConnectionTerminateHandler, MessageHandler {

	static Logger logger = Logger.getLogger(MieshiGatewayServer.class);
	
	private static MieshiGatewayServer mself;

	public static MieshiGatewayServer getInstance() {
		return mself;
	}

	GameMessageFactory mf = GameMessageFactory.getInstance();


	protected int maxWindowSize = 32;
	protected long waitingResponseMessageTimeout = 30 * 1000L;
	protected long waitingRequestMessageTimeout = 180 * 1000L;
	protected long waitingMoreSocketSendBufferTimeout = 180 * 1000L;
	
	protected int maxReSendTimes = 2;
	protected long sendBufferSize = 256 * 1024L;//128 * 1024L;
	protected long receiveBufferSize = 8 * 1024L;
	protected long requestTimeout = 10000L;
	

	protected long takeOutTimeout = 10000;

	protected long receivePacketNum = 0;
	protected long sendPacketNum = 0;

	protected long receivePacketTotalSize = 0;
	protected long sendPacketTotalSize = 0;

	protected int connectionNum = 0;
	
	protected byte hostid = 0x31;
	protected byte serverid;
	
	protected float receiveSpeed = 0;
	protected float sendSpeed = 0;
	protected long lastSendPacketNum = 0;
	protected long lastReceivePacketNum = 0;
	protected long lastChecktime = System.currentTimeMillis();
	
	//QueueSelector queueSelector = null;
	RoundrobinQueueSelector queueSelector = null;
	int readyNum = 1;
	long waitReadyTimeout = 100;
	int threadNum =150;
	Thread threads[];
	
	protected HashMap<String, GameSubSystem> subSystemMap = new HashMap<String, GameSubSystem>();
	protected HashMap<String, GameSubSystem[]> message2SubSysMap = new HashMap<String, GameSubSystem[]>();
	
	public RoundrobinQueueSelector getQueueSelector(){
		return queueSelector;
	}
	protected boolean running = false;

	public long getReceivePacketNum() {
		return receivePacketNum;
	}

	public long getSendPacketTotalSize() {
		return sendPacketTotalSize;
	}

	public long getReceivePacketTotalSize() {
		return receivePacketTotalSize;
	}

	public int getConnectionNum() {
		return connectionNum;
	}

	public void init() throws Exception {
		running = true;
		//queueSelector = new QueueSelector(this.readyNum,this.waitReadyTimeout);
		queueSelector = new RoundrobinQueueSelector(20);
		
		threads = new Thread[this.threadNum];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new QueueSelectorThread(),
					"OutGameGateway-QueueSelectorThread-" + (i + 1));
			threads[i].start();
		}
		mself = this;
		long now = System.currentTimeMillis();
		System.out.println(this.getClass().getName() + " initialize successfully ["+(System.currentTimeMillis()-now)+"]");
		logger.info(this.getClass().getName() + " initialize successfully ["+(System.currentTimeMillis()-now)+"]");
	}
	
	//
	public void setGameSubSystems(List<GameSubSystem> subsysList) {
		Iterator<GameSubSystem> it = subsysList.iterator();
		while (it.hasNext()) {
			GameSubSystem gss = it.next();
			subSystemMap.put(gss.getName(), gss);
			String types[] = gss.getInterestedMessageTypes();
			for (int i = 0; i < types.length; i++) {
				GameSubSystem[] aa = message2SubSysMap.get(types[i]);
				if (aa == null) {
					aa = new GameSubSystem[1];
					aa[0] = gss;
				} else {
					GameSubSystem[] bb = new GameSubSystem[aa.length + 1];
					System.arraycopy(aa, 0, bb, 0, aa.length);
					bb[aa.length] = gss;
					aa = bb;
				}
				message2SubSysMap.put(types[i], aa);
			}
		}
	}
	
	class QueueSelectorThread implements Runnable {
		public void run() {
			while (running) {
				try {
					long l = System.currentTimeMillis();
					DefaultSelectableQueue queue = (DefaultSelectableQueue) queueSelector.select(100000);

					if (queue == null) {
						continue;
					}
				
					try {
						while (!queue.isEmpty()) {
							Message m = (Message) queue.peek();
							if(m != null){
								Connection conn = (Connection) queue.getAttachment();
								try{
									sendMessage(conn,m);
									queue.pop();
								}catch(Throwable e){
									if(conn == null || conn.getState() == Connection.CONN_STATE_CLOSE){
										while(!queue.isEmpty()){
											Message mm = (Message)queue.pop();
											if(mm != null)
												logger.warn("[连接已关闭，清空对应队列消息] ["+conn.getName()+"] [" + conn.getIdentity() + "] [" + Connection.getStateString(conn.getState()) + "] ["+mm.getTypeDescription()+"] [len:"+mm.getLength()+"]");
										}
										
										logger.warn("[发送失败，连接已关闭，已清空对应队列消息] ["+conn.getName()+"] [" + conn.getIdentity() + "] [" + Connection.getStateString(conn.getState()) + "] ["+m.getTypeDescription()+"] [len:"+m.getLength()+"]",e);
									}else{
										long now = System.currentTimeMillis();
										synchronized(conn){
											try{
												conn.wait(50);
											}catch(java.lang.InterruptedException ex){
												e.printStackTrace();
											}
										}
										logger.warn("[数据包发送失败,连接未关闭，保留数据] ["+conn.getName()+"] [" + conn.getIdentity() + "] ["+Connection.getStateString(conn.getState())+"] [出现发送异常，等待"+(System.currentTimeMillis() - now)+"毫秒] ["+m.getTypeDescription()+"] [len:"+m.getLength()+"] [将取出来的消息返回到队列中]",e);
									}
									break;
								}
								
							
							}//end of m == null
						}//while									
						
					}catch(Throwable e){
						logger.warn("[发送数据包] [不可能发生的异常]",e);
					}finally {
						queue.returnToSelector();
					}
				
					
					//java.util.Collection<SelectableQueue> readySet = queueSelector.select(100000);
					//if (readySet != null) {
					//	Iterator<SelectableQueue> it = readySet.iterator();
					//	while (it.hasNext()) {
					//		
					//	}
					//}
					l = System.currentTimeMillis() - l;
				
				} catch (Exception e) {
					logger.warn(
							"[IN-QUEUE-SELECTOR] [" + Thread.currentThread().getName() + "] [catch-exception]",
							e);
				}
			}
			
			//
			logger.warn("[IN-QUEUE-SELECTOR] [" + Thread.currentThread().getName() + "] [发送线程停止运行]");
		}
	}
	

	/**
	 * 将消息加入到对应连接的发送队列
	 * @param conn
	 * @param message
	 */
	public void sendMessageToClient(Connection conn,Message message) {
		DefaultSelectableQueue dq = (DefaultSelectableQueue) conn.getAttachment2();
		if (dq != null) {
			Message m = (Message)dq.push(message);
			if(m != null){
				logger.warn("[discard_message] [queue_is_full] ["+m.getTypeDescription()+"] [" + conn.getName() + "] [" + conn.getIdentity() + "] [" + Connection.getStateString(conn.getState()) + "]" );
			}
		} else {
			logger.warn("[conn_hasn't_select_queue] [" + conn.getName() + "] [" + conn.getIdentity() + "] [" + Connection.getStateString(conn.getState()) + "]" );
		}
	}

	public void setConnectionSelector(ConnectionSelector selector) {
		((DefaultConnectionSelector)selector).setConnectionSendBufferSize((int) sendBufferSize);
		((DefaultConnectionSelector)selector).setConnectionReceiveBufferSize((int) receiveBufferSize);
	}

	public void discardRequestMessage(Connection conn, RequestMessage req)
			throws ConnectionException {
		synchronized(req){
			req.notify();
		}
	}

	public ResponseMessage receiveRequestMessage(Connection conn,
			RequestMessage request) throws ConnectionException {
		long startTime = System.currentTimeMillis();
		receivePacketNum++;
		receivePacketTotalSize += request.getLength();
		
		GameSubSystem gss[] = message2SubSysMap.get(request.getTypeDescription());
		if (gss != null && gss.length > 0) {
			for (int i = 0; i < gss.length; i++) {
				ResponseMessage res;
				try {
					res = gss[i].handleReqeustMessage(conn, request);
					if (res != null) {
						if (logger.isDebugEnabled()) {
							logger.debug("["+this+"] [receive-request] [success] [return-" + res.getTypeDescription() + "-on-" + gss[i].getName() + "-subsystem] [" + request.getTypeDescription() + "] [len:"+request.getLength()+"] [len:"+res.getLength()+"] [" + conn.getName() + "] [" + conn.getIdentity() + "] [cost:" + (System.currentTimeMillis() - startTime) + "]");
						}
						this.sendPacketTotalSize += res.getLength();
						sendPacketNum++;
						return res;
					}
					else
					{
						if (logger.isDebugEnabled()) {
							logger.debug("["+this+"] [receive-request] [success] [return-null-on-" + gss[i].getName() + "-subsystem] [" + request.getTypeDescription() + "] [len:"+request.getLength()+"] [" + conn.getName() + "] [" + conn.getIdentity() + "] [cost:" + (System.currentTimeMillis() - startTime) + "]");
						}
					}
				} catch (ConnectionException e) {
					logger.error("["+this+"] [receive-request] [error] [catch-ConnectionException-on-" + gss[i].getName() + "-subsystem] [" + request.getTypeDescription() + "] [" + conn.getName() + "] [" + conn.getIdentity() + "] [cost:" + (System.currentTimeMillis() - startTime) + "]",
							e);
					throw e;
				} 
				catch (Exception e) {
					logger.error("["+this+"] [receive-request] [error] [catch-Exception-on-" + gss[i].getName() + "-subsystem] [" + request.getTypeDescription() + "] [" + conn.getName() + "] [" + conn.getIdentity() + "] [cost:" + (System.currentTimeMillis() - startTime) + "]",
							e);
					e.printStackTrace();
				}
				catch (Throwable e) {
					logger.warn(
							"["+this+"] [receive-request] [fail] [catch-throwable-on-" + gss[i].getName() + "-subsystem] [" + request.getTypeDescription() + "] [" + conn.getName() + "] [" + conn.getIdentity() + "] [cost:" + (System.currentTimeMillis() - startTime) + "]",
							e);
					return null;
				}
			}
		} else {
			logger.error("["+this+"] [receive-request] [error] [unknown-interested-subsystems] [" + request.getTypeDescription() + "] [" + conn.getName() + "] [" + conn.getIdentity() + "] [cost:" + (System.currentTimeMillis() - startTime) + "]");
			return null;
		}

		logger.warn("["+this+"] [receive-request] [success] [return-null] [" + request.getTypeDescription() + "] [len:"+request.getLength()+"] [" + conn.getName() + "] [" + conn.getIdentity() + "] [cost:" + (System.currentTimeMillis() - startTime) + "]");

		return null;
	}

	public void receiveResponseMessage(Connection conn, RequestMessage req,
			ResponseMessage res) throws ConnectionException {
		try {
			MieshiGatewaySubSystem.getInstance().handleResponseMessage(conn, req, res);
			logger.warn("[receiveResponseMessage] [success] ["+conn.getName()+"] ["+res.getTypeDescription()+"]");
		} catch (Exception e) {
			logger.error("[receiveResponseMessage] [error] ["+conn.getName()+"] ["+res.getTypeDescription()+"]", e);
		}
	}
	
	
	
	
	
	private void sendMessage(Connection conn, Message req) throws Exception {
		long now = System.currentTimeMillis();
		Connection oldConn = conn;
		if (conn.getState() != Connection.CONN_STATE_CLOSE && conn.getState() != Connection.CONN_STATE_UNKNOWN) {
			String name = Thread.currentThread().getName();
			try {
				
				Thread.currentThread().setName("prepare to takeout " +conn.getName()+"...");
				conn = conn.getConnectionSelector().takeoutConnection(new SelectorPolicy.ConnectionSelectorPolicy(conn),
						takeOutTimeout);
			} catch (IOException e) {
				logger.warn(
						"["+conn.getName()+"] [send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (System.currentTimeMillis() - now) + "]  [queueSize:"+queueSelector.size()+"]",
						e);
				throw e;
			}finally{
				Thread.currentThread().setName(name);
			}
			
			if(conn == null){
				if(oldConn != null){
					logger.warn("["+oldConn.getName()+"] [send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (System.currentTimeMillis() - now) + "] [不能取得连接]");
				}else{
					logger.warn("[conn==null] [--] [send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (System.currentTimeMillis() - now) + "] [传入的连接为空] ");					
				}
				throw new Exception("takeoutConnection 连接失败！超时");
				
			}
			try {
				if(req instanceof RequestMessage) {
					conn.writeMessage((RequestMessage)req, false);
				} else {
					conn.writeResponse((ResponseMessage)req);
				}
				sendPacketNum++;
				this.sendPacketTotalSize += req.getLength();
				if (logger.isDebugEnabled()) {
					logger.debug("["+conn.getName()+"] [send] [SUCC] [" + req.getTypeDescription() + "] [len:"+req.getLength()+"] [cost:" + (System.currentTimeMillis() - now) + "] [queueSize:"+queueSelector.size()+"]");
				}
			} catch (Exception e) {
				logger.warn(
						"["+conn.getName()+"] [send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (System.currentTimeMillis() - now) + "]  [queueSize:"+queueSelector.size()+"]",
						e);
				throw e;
			}
		} else {
			logger.warn("["+this+"] [send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (System.currentTimeMillis() - now) + "]  [queueSize:"+queueSelector.size()+"]");
			throw new Exception("takeoutConnection error cause connection has colsed.");
		}
	}

	public RequestMessage waitingTimeout(Connection arg0, long arg1)
			throws ConnectionException {
		//return new ACTIVE_TEST_REQ(GameMessageFactory.nextSequnceNum());
		return null;
	}

	

	public void connected(Connection conn) throws IOException {
		// TODO Auto-generated method stub
		int num = 2;
		try{
			ByteChannel channel = conn.getChannel();
			
			if (channel instanceof SocketChannel) {
				InetSocketAddress isa = (InetSocketAddress) ((SocketChannel) channel).socket().getRemoteSocketAddress();
				((SocketChannel) channel).socket().setTcpNoDelay(true);
				((SocketChannel) channel).socket().setSoTimeout(5000);
			} else {
				throw new IOException("invalid channel type");
			}
			
			ByteBuffer bb = ByteBuffer.allocate(num);
			while(bb.remaining() > 0){
				if(channel.read(bb) == -1){
					throw new IOException("读取分发头信息时，网络中断!");
				}
			}
			bb.flip();
			for(int i = 0 ; i < num ; i++){
				byte b = bb.get();
				logger.info("[read_header] ["+b+"]");
			}
		}catch(IOException e){
			logger.warn("[----] [connection] [连接失败] [" + conn.getName() + "] [" + conn.getIdentity() + "]",e);
			throw e;
		}
		conn.setMaxWindowSize(maxWindowSize);
		conn.setMaxReSendTimes(maxReSendTimes);
		conn.setSendBufferSize((int) sendBufferSize);
		conn.setReceiveBufferSize((int) receiveBufferSize);
		conn.setWaitingRequestMessageTimeout(waitingRequestMessageTimeout);
		conn.setWaitingResponseMessageTimeout(waitingResponseMessageTimeout);
		conn.setWaitingMoreSocketSendBufferTimeout(waitingMoreSocketSendBufferTimeout);
		conn.setMessageFactory(mf);
		conn.setMessageHandler(this);
		conn.setConnectionTerminateHandler(this);
		
		DefaultSelectableQueue dq = new DefaultSelectableQueue(4096);
		conn.setAttachment2(dq);
		dq.setAttachment(conn);
		dq.register(queueSelector);
		
		synchronized (this) {
			connectionNum++;
		}
	}

	public void ternimate(Connection conn,
			List<RequestMessage> noResponseMessages, ByteBuffer receiveBuffer) {
		// TODO Auto-generated method stub
		synchronized (this) {
			connectionNum--;
		}
		
		DefaultSelectableQueue dq = (DefaultSelectableQueue) conn.getAttachment2();
		if (dq != null) {
			dq.unregister();
		}
		
		
		logger.warn("[----] [connection] [连接关闭] [" + conn.getName() + "] [" + conn.getIdentity() + "]");
	}
	


	
}

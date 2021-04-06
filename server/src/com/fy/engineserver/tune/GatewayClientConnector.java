package com.fy.engineserver.tune;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.gamegateway.message.ACTIVE_TEST_REQ;
import com.fy.gamegateway.message.GameMessageFactory;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionConnectedHandler;
import com.xuanzhi.tools.transport.ConnectionCreatedHandler;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.ConnectionSelector;
import com.xuanzhi.tools.transport.ConnectionTerminateHandler;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;
import com.xuanzhi.tools.transport.MessageHandler;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;
import com.xuanzhi.tools.transport.SelectorPolicy;
import com.xuanzhi.tools.transport.SendBufferFullException;

public class GatewayClientConnector implements ConnectionConnectedHandler,
		ConnectionCreatedHandler, ConnectionTerminateHandler, MessageHandler, Runnable {

	// private Logger logger = Logger.getLogger(GatewayClientConnector.class);
	public static Logger logger = LoggerFactory.getLogger(GatewayClientConnector.class);

	private static GatewayClientConnector mself;

	public static GatewayClientConnector getInstance() {
		return mself;
	}

	GameMessageFactory mf = GameMessageFactory.getInstance();

	protected DefaultConnectionSelector selector;

	protected int maxWindowSize = 32;
	protected long waitingResponseMessageTimeout = 30 * 1000L;
	protected long waitingRequestMessageTimeout = 180 * 1000L;
	protected int maxReSendTimes = 2;

	protected long sendBufferSize = 64 * 1024L;
	protected long receiveBufferSize = 64 * 1024L;
	protected long requestTimeout = 20000L;

	protected long takeOutTimeout = 20000L;
	protected long connectTimeout = 10000;

	protected long receivePacketNum = 0;
	protected long sendPacketNum = 0;

	protected long receivePacketTotalSize = 0;
	protected long sendPacketTotalSize = 0;

	protected int connectionNum = 0;

	protected float receiveSpeed = 0;
	protected float sendSpeed = 0;
	protected long lastSendPacketNum = 0;
	protected long lastReceivePacketNum = 0;
	protected long lastChecktime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

	protected boolean running = false;
	protected Thread localThread = null;

	// 客户端的响应
	public static Hashtable<RequestMessage, ResponseMessage> responseMessageMap = new Hashtable<RequestMessage, ResponseMessage>();

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
		mself = this;
		start();
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		System.out.println("["+this.getClass().getSimpleName()+ " initialize successfully] [host:"+selector.getHost()+"] [port:"+selector.getPort()+"] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "]");
	}

	public void start() {
		if (!running) {
			running = true;
			localThread = new Thread(this, "GatewaySystemConnector");
			localThread.start();
		}
	}

	public void stop() {
		running = false;
		if (localThread.isAlive()) {
			localThread.interrupt();
		}
	}

	public ConnectionSelector getConnectionSelector() {
		return selector;
	}

	public void setConnectionSelector(ConnectionSelector selector) {
		this.selector = (DefaultConnectionSelector) selector;
		this.selector.setConnectionConnectedHandler(this);
		this.selector.setConnectionCreatedHandler(this);
		this.selector.setConnectionSendBufferSize((int) sendBufferSize);
		this.selector.setConnectionReceiveBufferSize((int) receiveBufferSize);
		this.selector.setConnectTimeout(connectTimeout);
	}

	public void discardRequestMessage(Connection conn, RequestMessage req) throws ConnectionException {
		synchronized (req) {
			req.notify();
		}
	}

	public ResponseMessage receiveRequestMessage(Connection conn, RequestMessage request) throws ConnectionException {
		receivePacketNum++;
		receivePacketTotalSize += request.getLength();
		// logger.info("[receiveRequestMessage] ["+request.getTypeDescription()+"]");
		if (logger.isInfoEnabled()) logger.info("[receiveRequestMessage] [{}]", new Object[] { request.getTypeDescription() });
		return null;
	}

	public void receiveResponseMessage(Connection conn, RequestMessage req, ResponseMessage res) throws ConnectionException {
		if (req != null) {
			// logger.info("[receiveResponseMessage] [seq:"+res.getSequenceNumAsString()+"] ["+req.getTypeDescription()+"] ["+res.getTypeDescription()+"] ["+res.getLength()+"]");
			if (logger.isInfoEnabled()) logger.info("[receiveResponseMessage] [seq:{}] [{}] [{}] [{}]", new Object[] { res.getSequenceNumAsString(), req.getTypeDescription(), res.getTypeDescription(), res.getLength() });
			responseMessageMap.put(req, res);
			synchronized (req) {
				req.notify();
			}
		}
	}

	public Connection getConnection() throws Exception {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Connection conn = null;
		try {
			conn = selector.takeoutConnection(SelectorPolicy.DefaultClientModelPolicy, takeOutTimeout);
		} catch (Exception e) {
			throw e;
		}
		ACTIVE_TEST_REQ req = new ACTIVE_TEST_REQ(GameMessageFactory.nextSequnceNum());
		try {
			ByteBuffer bb = ByteBuffer.allocate(2);
			bb.put(new byte[] { 0, 1 });
			bb.flip();
			conn.getChannel().write(bb);
			conn.writeMessage(req, false);
			sendPacketNum++;
			this.sendPacketTotalSize += req.getLength();
			if (logger.isInfoEnabled()) {
				// logger.info("[send] [SUCC] [" + req.getTypeDescription() + "] [len:"+req.getLength()+"] [cost:" +
				// (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "]");
				if (logger.isInfoEnabled()) logger.info("[send] [SUCC] [{}] [len:{}] [cost:{}]", new Object[] { req.getTypeDescription(), req.getLength(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
			}
		} catch (SendBufferFullException e) {
			try {
				long ll = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				synchronized (conn) {
					conn.wait(50);
				}
				if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - ll < 50) {
					sendMessage(conn, req);
				} else {
					// logger.warn(
					// "[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "] ",
					// e);
					if (logger.isWarnEnabled()) logger.warn("[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "] ", e);
				}
			} catch (InterruptedException e1) {
				// logger.warn(
				// "[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "] ",
				// e);
				if (logger.isWarnEnabled()) logger.warn("[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "] ", e);
			}
		} catch (Exception e) {
			// logger.warn(
			// "[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "] ",
			// e);
			if (logger.isWarnEnabled()) logger.warn("[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "] ", e);
		}
		return conn;
	}

	public ResponseMessage requestMessage(Connection conn, RequestMessage req) throws Exception {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		String connDesp = conn.getName();
		int state = conn.getState();
		if (conn.getState() != Connection.CONN_STATE_CLOSE && conn.getState() != Connection.CONN_STATE_UNKNOWN) {
			try {
				conn = selector.takeoutConnection(new SelectorPolicy.ConnectionSelectorPolicy(conn), takeOutTimeout);
			} catch (Exception e) {
				throw new Exception("takeoutConnection error cause " + e.getMessage());
			}
			if (conn == null) {
				// logger.warn("[无法取得此连接] ["+connDesp +"] [state:"+state+"] ["+Connection.getStateString(state)+"]");
				if (logger.isWarnEnabled()) logger.warn("[无法取得此连接] [{}] [state:{}] [{}]", new Object[] { connDesp, state, Connection.getStateString(state) });
				throw new Exception("无法取得连接(" + connDesp + ")");
			}
			conn.writeMessage(req, true);
			sendPacketNum++;
			this.sendPacketTotalSize += req.getLength();
			ResponseMessage resp = (ResponseMessage) responseMessageMap.get(req);
			if (resp == null) {
				synchronized (req) {
					try {
						req.wait(requestTimeout);
					} catch (Exception e) {
						// do something
					}
				}
				resp = (ResponseMessage) responseMessageMap.remove(req);
			} else {
				responseMessageMap.remove(req);
			}
			if (resp != null) {
				String ss = com.xuanzhi.tools.text.StringUtil.addcommas(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now);
				for (int i = ss.length(); i < 5; i++)
					ss += " ";
				// logger.info("[req#] [SUCC] ["+ss+"] ["+conn.getName()+"] [seq:"+req.getSequenceNumAsString()+"] ["+req.getTypeDescription()+"] ["+responseMessageMap.size()+"]");
				if (logger.isInfoEnabled()) logger.info("[req#] [SUCC] [{}] [{}] [seq:{}] [{}] [{}]", new Object[] { ss, conn.getName(), req.getSequenceNumAsString(), req.getTypeDescription(), responseMessageMap.size() });
				return resp;
			} else {
				// logger.warn(
				// "[req#] [FAIL] ["+conn.getName()+"] [seq:"+req.getSequenceNumAsString()+"] [" + req.getTypeDescription() + "]");
				if (logger.isWarnEnabled()) logger.warn("[req#] [FAIL] [{}] [seq:{}] [{}]", new Object[] { conn.getName(), req.getSequenceNumAsString(), req.getTypeDescription() });
				throw new Exception("request failed for no response.");
			}
		} else {
			// logger.warn("[req#] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "]");
			if (logger.isWarnEnabled()) logger.warn("[req#] [FAIL] [{}] [cost:{}]", new Object[] { req.getTypeDescription(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
			throw new Exception("takeoutConnection error (" + conn.getName() + ")");
		}
	}

	public void sendMessage(Connection conn, RequestMessage req) throws Exception {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (conn.getState() != Connection.CONN_STATE_CLOSE && conn.getState() != Connection.CONN_STATE_UNKNOWN) {
			try {
				conn = conn.getConnectionSelector().takeoutConnection(new SelectorPolicy.ConnectionSelectorPolicy(conn), takeOutTimeout);
			} catch (IOException e) {
				// logger.warn(
				// "[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "] ", e);
				if (logger.isWarnEnabled()) logger.warn("[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "] ", e);
				throw new Exception("takeoutConnection error cause " + e.getMessage());
			}
			if (conn == null) {
				if (logger.isWarnEnabled()) logger.warn("[无法取得此连接] [{}] [state:{}] [{}]", new Object[] { conn.getName(), conn.getState(), Connection.getStateString(conn.getState()) });
				throw new Exception("无法取得连接(" + conn.getName());
			}
			try {
				conn.writeMessage(req, false);
				sendPacketNum++;
				this.sendPacketTotalSize += req.getLength();
				// logger.info("[send] [SUCC] [" + req.getTypeDescription() + "] [len:"+req.getLength()+"] [cost:" +
				// (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "]");
				if (logger.isInfoEnabled()) logger.info("[send] [SUCC] [{}] [len:{}] [cost:{}]", new Object[] { req.getTypeDescription(), req.getLength(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
			} catch (SendBufferFullException e) {
				try {
					long ll = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					synchronized (conn) {
						conn.wait(50);
					}
					if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - ll < 50) {
						sendMessage(conn, req);
					} else {
						// logger.warn(
						// "[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "] ", e);
						if (logger.isWarnEnabled()) logger.warn("[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "] ", e);
					}
				} catch (InterruptedException e1) {
					// logger.warn(
					// "[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "] ", e);
					if (logger.isWarnEnabled()) logger.warn("[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "] ", e);
				}
			} catch (Exception e) {
				// logger.warn(
				// "[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "] " , e);
				if (logger.isWarnEnabled()) logger.warn("[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "] ", e);
			}
		} else {
			// logger.warn("[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "]");
			if (logger.isWarnEnabled()) logger.warn("[send] [FAIL] [{}] [cost:{}]", new Object[] { req.getTypeDescription(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
			throw new Exception("takeoutConnection error cause connection is null.");
		}
	}

	public RequestMessage waitingTimeout(Connection arg0, long arg1) throws ConnectionException {
		return new ACTIVE_TEST_REQ(GameMessageFactory.nextSequnceNum());
	}

	public void run() {
		// TODO Auto-generated method stub
		// 计算发送速度和接受速度
		long checkPeriod = 10000;
		while (running) {
			try {
				Thread.sleep(checkPeriod);
				long receivedPacket = receivePacketNum - lastReceivePacketNum;
				long sendPacket = sendPacketNum - lastSendPacketNum;
				long elaped = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - lastChecktime;
				receiveSpeed = new Float(receivedPacket) * 1000 / elaped;
				sendSpeed = new Float(sendPacket) * 1000 / elaped;
				lastReceivePacketNum = receivePacketNum;
				lastSendPacketNum = sendPacketNum;
				lastChecktime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				// logger.info("[check] [receiveSpeed:"+NumberUtils.cutFloat(receiveSpeed,1)+"] [sendSpeed:"+NumberUtils.cutFloat(sendSpeed,1)+"] [totalReceive:"+receivePacketNum+"] [totalSend:"+sendPacketNum+"]");
//				if (logger.isInfoEnabled()) logger.info("[check] [receiveSpeed:{}] [sendSpeed:{}] [totalReceive:{}] [totalSend:{}]", new Object[] { NumberUtils.cutFloat(receiveSpeed, 1), NumberUtils.cutFloat(sendSpeed, 1), receivePacketNum, sendPacketNum });
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// if(this.selector.isClientModel()) {
			// ACTIVE_TEST_REQ req = new ACTIVE_TEST_REQ(GameMessageFactory.nextSequnceNum());
			// try {
			// sendMessage(req);
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
		}
	}

	public void connected(Connection conn) throws IOException {
		// TODO Auto-generated method stub
		conn.setMaxWindowSize(maxWindowSize);
		conn.setMaxReSendTimes(maxReSendTimes);
		conn.setSendBufferSize((int) sendBufferSize);
		conn.setReceiveBufferSize((int) receiveBufferSize);
		conn.setWaitingRequestMessageTimeout(waitingRequestMessageTimeout);
		conn.setWaitingResponseMessageTimeout(waitingResponseMessageTimeout);
		conn.setMessageFactory(mf);
		conn.setMessageHandler(this);
		conn.setConnectionTerminateHandler(this);
		synchronized (this) {
			connectionNum++;
		}
	}

	public void created(Connection conn, String attachment) throws IOException {
		// TODO Auto-generated method stub
		conn.setMessageFactory(mf);
		conn.setMessageHandler(this);
		if (logger.isInfoEnabled()) logger.info("[连接已创建] [{}]", new Object[] { conn.getName() });
	}

	public void ternimate(Connection conn, List<RequestMessage> noResponseMessages, ByteBuffer receiveBuffer) {
		// TODO Auto-generated method stub
		synchronized (this) {
			connectionNum--;
		}
	}
}

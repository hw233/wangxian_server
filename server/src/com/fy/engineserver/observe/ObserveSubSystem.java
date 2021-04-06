package com.fy.engineserver.observe;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.ACTIVE_TEST_REQ;
import com.fy.engineserver.message.ACTIVE_TEST_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.OBSERVER_CONNECT_REQ;
import com.fy.engineserver.message.OBSERVER_CONNECT_RES;
import com.fy.engineserver.message.OBSERVER_MOVE_REQ;
import com.fy.engineserver.message.OBSERVER_MOVE_RES;
import com.fy.engineserver.message.OBSERVER_REGISTER_REQ;
import com.fy.engineserver.message.OBSERVER_UNREGISTER_REQ;
import com.fy.engineserver.message.TIME_SETTING_REQ;
import com.fy.engineserver.message.TIME_SYNC_REQ;
import com.fy.engineserver.message.TIME_SYNC_RES;
import com.xuanzhi.tools.queue.DefaultQueue;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionConnectedHandler;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.ConnectionSelector;
import com.xuanzhi.tools.transport.ConnectionTerminateHandler;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;
import com.xuanzhi.tools.transport.MessageHandler;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;
import com.xuanzhi.tools.transport.SelectorPolicy;
import com.xuanzhi.tools.transport.SendBufferFullException;

/**
 * 观察者系统，此系统支持观察者链接到此系统，并且注册关心哪些类型的消息，
 * 然后观察者系统会将观察员关心的消息全部转发给观察员。
 * 
 * 
 * 
 * 
 */
public class ObserveSubSystem implements ConnectionConnectedHandler, ConnectionTerminateHandler,
		MessageHandler, Runnable {

	// static Logger logger = Logger.getLogger(ObserveSubSystem.class);
	public static Logger logger = LoggerFactory.getLogger(ObserveSubSystem.class);

	GameMessageFactory mf = GameMessageFactory.getInstance();

	protected DefaultConnectionSelector selector;
	protected String[] m_ipAllows = null;

	protected int maxWindowSize = 16;
	protected long waitingResponseMessageTimeout = 30 * 1000L;
	protected long waitingRequestMessageTimeout = 180 * 1000L;
	protected int maxReSendTimes = 2;
	protected long sendBufferSize = 64 * 1024L;
	protected long receiveBufferSize = 8 * 1024L;

	protected long takeOutTimeout = 6000;
	protected boolean running = true;

	protected DefaultQueue messageQueue = new DefaultQueue(4096 * 16);

	protected long receivePacketNum = 0;
	protected long sendPacketNum = 0;

	protected long receivePacketTotalSize = 0;
	protected long sendPacketTotalSize = 0;

	protected int connectionNum = 0;

	protected int threadNum = 1;
	protected Thread threads[];

	protected ArrayList<Observer> observers = new ArrayList<Observer>();

	GameManager gm;

	public void setGameManager(GameManager sys) {
		gm = sys;
	}

	public boolean isObserverMatch(String username, String password) {
		return true;
	}

	public synchronized void addObserver(Observer o) {
		if (o == null) throw new NullPointerException("observer is null");
		if (observers.contains(o) == false) {
			observers.add(o);
		}
	}

	public synchronized void removeObserver(Observer o) {
		if (observers.contains(o)) {
			observers.remove(o);
			if (o.conn != null) {
				o.conn.close();
			}

			// Game game = gm.getGameByName(o.getGame());
			// if(game != null){
			// game.removeObserver(o);
			// }
		}
	}

	public Observer getObserver(String name) {
		Observer[] os = getObservers();
		for (int i = 0; i < os.length; i++) {
			if (os[i].name.equals(name)) {
				return os[i];
			}
		}
		return null;
	}

	public Observer[] getObservers() {
		return observers.toArray(new Observer[0]);
	}

	public long getReceivePacketNum() {
		return receivePacketNum;
	}

	public long getSendPacketTotalSize() {
		return sendPacketTotalSize;
	}

	public long getReceivePacketTotalSize() {
		return receivePacketTotalSize;
	}

	public long getSendPacketNum() {
		return sendPacketNum;
	}

	public long getPacketQueueSize() {
		return this.messageQueue.size();
	}

	public int getConnectionNum() {
		return connectionNum;
	}

	public void setThreadNum(int t) {
		this.threadNum = t;
	}

	public void init() throws Exception {
		threads = new Thread[this.threadNum];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(this, "Observer-SendThread-" + (i + 1));
			threads[i].start();
		}
	}

	public String getName() {
		return "ObserveSubSystem";
	}

	public void setIpAllows(String allows) {
		m_ipAllows = allows.split("[ ,;]+");
	}

	/**
	 * 判断来取消息的机器，是否在允许的范围内
	 * @param remoteHost
	 * @return
	 */
	public boolean isIpAllows(String remoteHost) {

		if (m_ipAllows == null) return true;

		for (int i = 0; i < m_ipAllows.length; i++) {
			if (remoteHost.matches(m_ipAllows[i])) return true;
		}
		return false;
	}

	public ConnectionSelector getConnectionSelector() {
		return selector;
	}

	public void setConnectionSelector(ConnectionSelector selector) {
		this.selector = (DefaultConnectionSelector) selector;
		this.selector.setConnectionConnectedHandler(this);

		this.selector.setConnectionSendBufferSize((int) sendBufferSize);
		this.selector.setConnectionReceiveBufferSize((int) receiveBufferSize);
	}

	public void connected(Connection conn) throws IOException {
		String identity = null;
		ByteChannel channel = conn.getChannel();
		if (channel instanceof SocketChannel) {
			InetSocketAddress isa = (InetSocketAddress) ((SocketChannel) channel).socket().getRemoteSocketAddress();
			((SocketChannel) channel).socket().setTcpNoDelay(true);

			String remoteHost = isa.getAddress().getHostAddress();
			identity = remoteHost + ":" + isa.getPort();
			if (isIpAllows(remoteHost) == false) {
				throw new IOException("invalid client ip [" + remoteHost + "]");
			}
		} else {
			throw new IOException("invalid channel type");
		}

		conn.setMaxWindowSize(maxWindowSize);
		conn.setMaxReSendTimes(maxReSendTimes);
		conn.setSendBufferSize((int) sendBufferSize);
		conn.setReceiveBufferSize((int) receiveBufferSize);
		conn.setWaitingRequestMessageTimeout(waitingRequestMessageTimeout);
		conn.setWaitingResponseMessageTimeout(waitingResponseMessageTimeout);
		conn.setMessageFactory(mf);
		conn.setMessageHandler(this);
		conn.setConnectionTerminateHandler(this);
		if (identity == null) identity = DateUtil.formatDate(new java.util.Date(), "yyyyMMddHHmmss") + StringUtil.randomString(6);
		conn.setIdentity(identity);
		synchronized (ObserveSubSystem.class) {
			connectionNum++;
		}

		TIME_SYNC_REQ req = new TIME_SYNC_REQ(GameMessageFactory.nextSequnceNum(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis());

		sendMessage(conn, req, "synchronize_game_time");

		if (logger.isWarnEnabled()) logger.warn("[connection] [connected] [{}]", new Object[] { conn.getName() });
	}

	public void ternimate(Connection conn, List<RequestMessage> messages, ByteBuffer receiveBuffer) {
		synchronized (ObserveSubSystem.class) {
			connectionNum--;
		}
		Observer observer = (Observer) conn.getAttachment();
		if (observer != null) {
			removeObserver(observer);
		}
		if (logger.isWarnEnabled()) logger.warn("[connection] [ternimate] [{}]", new Object[] { conn.getName() });
	}

	public void discardRequestMessage(Connection arg0, RequestMessage arg1) throws ConnectionException {
		// TODO Auto-generated method stub

	}

	public ResponseMessage receiveRequestMessage(Connection conn, RequestMessage message) throws ConnectionException {

		receivePacketNum++;
		sendPacketNum++;

		Observer observer = (Observer) conn.getAttachment();
		String observerName = null;
		if (observer != null) {
			observerName = observer.getName();
		}
		if (message instanceof ACTIVE_TEST_REQ) {
			ACTIVE_TEST_REQ req = (ACTIVE_TEST_REQ) message;
			return new ACTIVE_TEST_RES(req.getSequnceNum());
		} else if (message instanceof OBSERVER_CONNECT_REQ) {
			OBSERVER_CONNECT_REQ req = (OBSERVER_CONNECT_REQ) message;
			OBSERVER_CONNECT_RES res = null;
			String username = req.getUsername();
			String password = req.getPassword();
			if (isObserverMatch(username, password)) {
				observer = new Observer(this, username, conn);
				this.addObserver(observer);
				conn.setAttachment(observer);
				res = new OBSERVER_CONNECT_RES(req.getSequnceNum(), (byte) 0, "OK");
			} else {
				res = new OBSERVER_CONNECT_RES(req.getSequnceNum(), (byte) 1, Translate.text_5520);
			}
			if (logger.isWarnEnabled()) logger.warn("[OBSERVER_CONNECT_REQ] [{}] [{}] [{}] [{}] [{}]", new Object[] { observerName, (res.getResult() == 0 ? "success" : "failed"), res.getResultString(), username, password });
			return res;
		} else if (message instanceof OBSERVER_REGISTER_REQ) {

		} else if (message instanceof OBSERVER_UNREGISTER_REQ) {

		}
		// else if(message instanceof OBSERVER_QUERY_REQ){
		// OBSERVER_QUERY_REQ req = (OBSERVER_QUERY_REQ)message;
		// OBSERVER_QUERY_RES res;
		// GameWorld gws[] = gm.getGameWorlds();
		// GameWorldInfo gwis[] = new GameWorldInfo[gws.length];
		// for(int i = 0 ; i < gwis.length ; i++){
		// gwis[i] = new GameWorldInfo();
		// gwis[i].setName(gws[i].getName());
		// gwis[i].setNumOfGame(gws[i].getGames().length);
		// gwis[i].setIndexOf(gws[i].getIndexOf());
		// gwis[i].setNumOfPlayer(gws[i].getNumOfPlayer());
		// String gameNames[] = new String[gws[i].getGames().length];
		// int gameNums[] = new int[gameNames.length];
		// for(int j = 0 ; j < gameNames.length; j++){
		// gameNames[j] = gws[i].getGames()[j].getGameInfo().getName();
		// gameNums[j] = gws[i].getGames()[j].getNumOfPlayer();
		// }
		// gwis[i].setGames(gameNames);
		// gwis[i].setNumOfPlayerInGame(gameNums);
		// }
		// res = new OBSERVER_QUERY_RES(req.getSequnceNum(),gwis);
		// return res;
		// }else if(message instanceof OBSERVER_GETGAMEDATA_REQ){
		// OBSERVER_GETGAMEDATA_REQ req = (OBSERVER_GETGAMEDATA_REQ)message;
		// OBSERVER_GETGAMEDATA_RES res;
		// String worldId = req.getWorld();
		// String gameId = req.getGame();
		// GameWorld gw = gm.getGameWorld(worldId);
		// Game game = null;
		// if(gw != null){
		// game = gw.getGame(gameId);
		// }
		// if(game != null){
		// res = new OBSERVER_GETGAMEDATA_RES(req.getSequnceNum(),(byte)0,"OK",game.getGameInfo().getData());
		// }else{
		// res = new OBSERVER_GETGAMEDATA_RES(req.getSequnceNum(),(byte)1,"指定的地图不存在",new byte[0]);
		// }
		// return res;
		// }else
		if (message instanceof OBSERVER_MOVE_REQ) {
			OBSERVER_MOVE_REQ req = (OBSERVER_MOVE_REQ) message;
			OBSERVER_MOVE_RES res;
			if (observer != null) {
				observer.setX(observer.getX() + req.getDx());
				observer.setY(observer.getY() + req.getDy());
				res = new OBSERVER_MOVE_RES(req.getSequnceNum(), (byte) 0, "OK");
			} else {
				res = new OBSERVER_MOVE_RES(req.getSequnceNum(), (byte) 1, Translate.text_5522);
			}
			if (logger.isWarnEnabled()) logger.warn("[OBSERVER_MOVE_REQ] [{}] [{}] [{}] [dx:{}] [dy:{}]", new Object[] { observerName, (res.getResult() == 0 ? "success" : "failed"), res.getResultString(), req.getDx(), req.getDy() });
			return res;
		}
		return null;
	}

	public void receiveResponseMessage(Connection conn, RequestMessage arg1, ResponseMessage arg2) throws ConnectionException {
		receivePacketNum++;

		if (arg2 instanceof TIME_SYNC_RES) {
			TIME_SYNC_RES res = (TIME_SYNC_RES) arg2;
			long delay = (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - res.getCurrentTime()) / 2;

			sendMessage(conn, new TIME_SETTING_REQ(GameMessageFactory.nextSequnceNum(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis(), delay), "setting_time");

		}
	}

	public RequestMessage waitingTimeout(Connection arg0, long arg1) throws ConnectionException {
		return new ACTIVE_TEST_REQ(GameMessageFactory.nextSequnceNum());
	}

	public void sendMessage(Connection conn, RequestMessage req, String description) {
		SendMessage sm = new SendMessage();
		sm.conn = conn;
		sm.req = req;
		sm.desp = description;
		messageQueue.push(sm);
	}

	private void doSendMessage(Connection conn, RequestMessage req, String description) {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (conn.getState() != Connection.CONN_STATE_CLOSE && conn.getState() != Connection.CONN_STATE_UNKNOWN) {
			try {
				conn = selector.takeoutConnection(new SelectorPolicy.ConnectionSelectorPolicy(conn), takeOutTimeout);
			} catch (IOException e) {
				if (logger.isWarnEnabled()) logger.warn("[sendmessage] [fail] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "] " + description, e);
				return;
			}
			if (conn != null) {
				try {
					conn.writeMessage(req, false);
					sendPacketNum++;
					this.sendPacketTotalSize += req.getLength();
					if (logger.isInfoEnabled()) {
						if (logger.isInfoEnabled()) logger.info("[sendmessage] [succ] [{}]  [cost:{}] {}", new Object[] { req.getTypeDescription(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime), description });
					}
				} catch (SendBufferFullException e) {
					try {
						long ll = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
						synchronized (conn) {
							conn.wait(50);
						}
						if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - ll < 50) {
							doSendMessage(conn, req, description);
						} else {
							if (logger.isWarnEnabled()) logger.warn("[sendmessage] [fail] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "] " + description, e);
						}
					} catch (InterruptedException e1) {
						if (logger.isWarnEnabled()) logger.warn("[sendmessage] [fail] [" + req.getTypeDescription()  + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "] " + description, e);
					}
				} catch (Exception e) {
					if (logger.isWarnEnabled()) logger.warn("[sendmessage] [fail] [" + req.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "] " + description, e);
				}
			} else {
				if (logger.isWarnEnabled()) logger.warn("[sendmessage] [fail] [{}] [cost:{}] {} -- [can't_takeout_conn]", new Object[] { req.getTypeDescription(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime), description });
			}
		} else {
			if (logger.isWarnEnabled()) logger.warn("[sendmessage] [fail] [{}] [cost:{}] {} -- [observer_not_online]", new Object[] { req.getTypeDescription(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime), description });
		}
	}

	private static class SendMessage {
		Connection conn;
		RequestMessage req;
		String desp;
	}

	public void run() {
		while (running) {
			try {
				SendMessage sm = (SendMessage) this.messageQueue.pop(5000L);
				if (sm != null) {
					doSendMessage(sm.conn, sm.req, sm.desp);
				}
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("In " + Thread.currentThread().getName() + " run method catch exception :", e);
			}
		}
	}

}

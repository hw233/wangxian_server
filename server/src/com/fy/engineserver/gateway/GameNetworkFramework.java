package com.fy.engineserver.gateway;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.FieldChangeEvent;
import com.fy.engineserver.core.MoveTrace4Client;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.AROUND_CHANGE_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.REPORT_LONG_PROTOCAL_REQ;
import com.fy.engineserver.message.SEQUENCE_NUM_PARAMETERS_REQ;
import com.fy.engineserver.message.TIME_SYNC_REQ;
import com.fy.engineserver.security.SecuritySubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.stat.model.PlayerActionFlow;
import com.xuanzhi.tools.queue.DefaultQueue;
import com.xuanzhi.tools.queue.DefaultSelectableQueue;
import com.xuanzhi.tools.queue.RoundrobinQueueSelector;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;
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
import com.xuanzhi.tools.transport.SendBufferFullException;

/**
 * <pre>
 * 服务器构架：
 * 
 * 			子系统集合                  用户集合
 * 			 消息队列  -------------&gt;  消息队列
 *                &circ;                 /
 *                 \               /  
 *                  \             /
 *                   \           /
 *                    \ 网络框架  V
 *                      数据缓冲区 
 *                        &circ;  |
 *                        |  |
 *                        |  |
 *                        |  V
 * 
 *                      
 * 接收消息：
 * 1） 数据到达网络框架，网络框架负责将数据解析成消息
 * 2）根据消息类型和配置，将消息分发给特定的子系统处理
 * 3）对于场景子系统，会判断消息是属于哪个地图，将消息加入到地图消息队列中
 * 4）每个地图对应一个心跳线程，有心跳线程来处理队列中的消息。一个心跳线程可处理多个地图。
 * 5）心跳函数处理逻辑：
 *      a) 对象移动
 *      b) 处理消息，并设置标志
 *      c) 检索各对象的状态改变
 *      d) 将这些改变通知到应该通知的人
 *      e) 清空标志
 * 
 * 发送消息：
 * 1) 每个客户端对应一个发送队列
 * 2) 给某个客户端发送消息只需要将消息加入到对应的队列中
 * 3) 有几个线程来监控这些队列，一有消息就发送
 * </pre>
 * 
 * 
 * 
 */
public class GameNetworkFramework implements ConnectionConnectedHandler,
		ConnectionTerminateHandler, MessageHandler, Runnable {

	// 临时存储需要统计的玩家名
	public static HashSet<String> statFlowUserSet = new HashSet<String>();

	public static Logger logger = LoggerFactory.getLogger(GameNetworkFramework.class);

	public static Logger sendErrorLogger = LoggerFactory.getLogger("game.server.network.sendError");

	private static GameNetworkFramework instance;

	public static GameNetworkFramework getInstance() {
		return instance;
	}

	GameMessageFactory mf = GameMessageFactory.getInstance();

	protected GameConstants gameConstants;
	protected DefaultConnectionSelector selector;
	protected String[] m_ipAllows = null;

	protected int maxWindowSize = 32;
	protected long waitingResponseMessageTimeout = 30 * 1000L;
	protected long waitingRequestMessageTimeout = 180 * 1000L;
	protected long waitingMoreSocketSendBufferTimeout = 180 * 1000L;

	protected int maxReSendTimes = 2;
	protected long sendBufferSize = 128 * 1024L;//64 * 1024L;
	protected long receiveBufferSize = 64 * 1024L;//8 * 1024L;

	protected long takeOutTimeout = 6000;
	protected boolean running = true;

	protected DefaultQueue sendQueue = null;
	protected int sendQueueSize = 16 * 4096;

	protected boolean queueSelectorEnabled = false;
	protected int readyNum = 1;
	protected long waitReadyTimeout = 0;

	protected byte[] f5Header = new byte[0];

	public byte[] getF5Header() {
		return f5Header;
	}

	public void setGameConstants(GameConstants gameConstants) {
		this.gameConstants = gameConstants;
		this.f5Header = new byte[2];
		f5Header[0] = gameConstants.getTheHostid();
		f5Header[1] = gameConstants.getTheServerid();
	}

	public void setReadyNum(int num) {
		readyNum = num;
	}

	public void setWaitReadyTimeout(long timeout) {
		waitReadyTimeout = timeout;
	}

	public void setQueueSelectorEnabled(boolean b) {
		queueSelectorEnabled = b;
	}

	// protected QueueSelector queueSelector = null;

	// protected DefaultQueueSelector queueSelector = null;

	protected RoundrobinQueueSelector queueSelector = null;

	protected int threadNum = 1;
	protected Thread threads[];

	protected HashMap<String, GameSubSystem> subSystemMap = new HashMap<String, GameSubSystem>();

	protected HashMap<String, GameSubSystem[]> message2SubSysMap = new HashMap<String, GameSubSystem[]>();

	protected long receivePacketNum = 0;
	protected long sendPacketNum = 0;

	protected long receivePacketTotalSize = 0;
	protected long sendPacketTotalSize = 0;

	protected int connectionNum = 0;

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
		if (this.queueSelectorEnabled) {
			return queueSelector.size();
		} else {
			return this.sendQueue.size();
		}
	}

	public int getConnectionNum() {
		return connectionNum;
	}

	public void setThreadNum(int t) {
		this.threadNum = t;
	}

	public void setSendQueueSize(int t) {
		this.sendQueueSize = t;
	}

	public void setGameSubSystems(List<GameSubSystem> subsysList) {
		Iterator<GameSubSystem> it = subsysList.iterator();
		while (it.hasNext()) {
			GameSubSystem gss = it.next();
			addSubSystem(gss);
		}
	}

	public void addSubSystem(GameSubSystem gss) {
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

	public ThreadPoolExecutor threadPool;

	public void init() throws Exception {
		

		instance = this;

		sendQueue = new DefaultQueue(sendQueueSize);

		if (queueSelectorEnabled) {

			// this.queueSelector = new QueueSelector(this.readyNum, this.waitReadyTimeout);

			// this.queueSelector = new DefaultQueueSelector();
			queueSelector = new RoundrobinQueueSelector(20);

			threads = new Thread[this.threadNum];
			for (int i = 0; i < threads.length; i++) {
				threads[i] = new Thread(new QueueSelectorThread(), "GameGateway-QueueSelectorThread-" + (i + 1));
				threads[i].start();
			}
		} else {
			threads = new Thread[this.threadNum];
			for (int i = 0; i < threads.length; i++) {
				threads[i] = new Thread(this, "GameGateway-SendThread-" + (i + 1));
				threads[i].start();
			}
		}
		ServiceStartRecord.startLog(this);
	}

	class QueueSelectorThread implements Runnable {
		public void run() {
			RequestMessage messages[] = new RequestMessage[32];
			ResponseMessage responses[] = new ResponseMessage[32];
			while (running) {
				try {
					DefaultSelectableQueue queue = (DefaultSelectableQueue) queueSelector.select(100000);

					if (queue == null) {
						continue;
					}

					try {
						long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

						Connection conn = (Connection) queue.getAttachment();
						String connName = "conn_is_null";
						String playerName = "playername_is_null";
						String username = "username_is_null";
						Long lastReceiveRequest = (Long) conn.getAttachmentData(LASTRECEIVETIME);
						if (lastReceiveRequest == null) {
							lastReceiveRequest = now;
						}
						if (conn != null) {
							connName = conn.getName();
							Player player = (Player) conn.getAttachmentData("Player");
							if (player != null) {
								playerName = player.getName();
							}
							username = (String) conn.getAttachmentData(SecuritySubSystem.USERNAME);
						}

						int index = 0;
						int length = 0;

						// 取出队列中所有的请求消息，准备发送，发送失败将全部消息返回到队列中，连接关闭则情况所有的消息
						while (!queue.isEmpty()) {
							Message m = (Message) queue.peek();
							if (m == null) {
								if (sendErrorLogger.isWarnEnabled()) sendErrorLogger.warn("[从队列中取出消息时发生异常,取出了null,直接丢弃] [" + Thread.currentThread().getName() + "] [" + connName + "] [" + username + "] [" + playerName + "]");
								queue.pop();
								continue;
							}

							if (m instanceof RequestMessage) {
								int mlen = 0;
								try {
									mlen = m.getLength();
								} catch (Exception e) {
									queue.pop();
									if (sendErrorLogger.isWarnEnabled()) sendErrorLogger.warn("[从队列中取出消息时发生异常] [" + playerName + "] [" + connName + "] [--] [getLength方法发生异常] [" + m.getTypeDescription() + "]", e);
									logger.error("[" + Thread.currentThread().getName() + "] [" + username + "] [" + playerName + "] [send_messages] [failed] [" + m.getTypeDescription() + "] [len:--] [" + conn.getName() + "] [cost:0] ", e);
									break;
								}
								// 直接丢弃超过缓冲区大小的消息
								if (mlen > sendBufferSize) {
									queue.pop();
									if (sendErrorLogger.isWarnEnabled()) sendErrorLogger.warn("[从队列中取出消息并丢弃] [{}] [{}] [--] [消息太大,超过发送缓冲区大小] [{}] [len:{}]", new Object[] { playerName, connName, m.getTypeDescription(), mlen });
									logger.error("[{}] [{}] [{}] [send_messages] [failed] [{}] [len:" + mlen + "] [{}] [{}] [cost:0] [消息太大,超过发送缓冲区大小]", new Object[] { Thread.currentThread().getName(), username, playerName, m.getTypeDescription(), conn.getName() });
									continue;
								}
								if (index < messages.length && length + mlen < sendBufferSize) {
									messages[index] = (RequestMessage) m;
									length += mlen;
									queue.pop();
									index++;
								} else {
									break;
								}
							} else {
								break;
							}
						}

						if (index > 0) {
							try {
								doSendMessage(conn, messages, 0, index);
							} catch (Throwable e) {
								if (conn == null || conn.getState() == Connection.CONN_STATE_CLOSE) {
									StringBuffer sb = new StringBuffer();

									for (int i = 0; i < index; i++) {
										Message m = messages[i];
										sb.append("," + m.getTypeDescription());
									}

									while (!queue.isEmpty()) {
										Message m = (Message) queue.pop();
										if (m != null) {
											sb.append("," + m.getTypeDescription());
										}
									}
									if (sb.length() == 0) {
										sb.append(Translate.text_4213);
									}
									if (conn == null) {
										if (sendErrorLogger.isWarnEnabled()) sendErrorLogger.warn("[多个数据包发送失败] [" + playerName + "] [" + connName + "] [--] [对应队列上连接为空] [清空队列中剩余所有数据包:" + sb.toString() + "]", e);
									} else {
										if (sendErrorLogger.isWarnEnabled()) sendErrorLogger.warn("[多个数据包发送失败] [" + playerName + "] [" + connName + "] [" + Connection.getStateString(conn.getState()) + "] [连接已关闭] [清空队列中剩余所有数据包:" + sb.toString() + "]", e);
									}
								} else {
									// 10分钟没有收到用户的数据包
									if (now - lastReceiveRequest > 600000) {
										logger.error("[数据包发送失败，并且10分钟没有收到玩家的消息，强制中断连接] [" + username + "] [" + playerName + "] [send_messages] [failed] [--] [len:--] [" + conn.getName() + "] [cost:0]", e);
										conn.close();
									} else {
										queue.pushBackToHeader(messages, 0, index);
										synchronized (conn) {
											try {
												conn.wait(100);
											} catch (java.lang.InterruptedException ex) {
												e.printStackTrace();
											}
										}

										if (sendErrorLogger.isWarnEnabled()) sendErrorLogger.warn("[数据包发送失败] [" + playerName + "] [" + connName + "] [" + Connection.getStateString(conn.getState()) + "] [出现发送异常，等待" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "毫秒] [将取出来的消息返回到队列中]", e);
									}
								}
							}
						}

						for (int i = 0; i < index; i++) {
							messages[i] = null;
						}

						now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
						index = 0;
						length = 0;
						// 取出队列中所有的响应消息，准备发送，发送失败将未发送的消息返回到队列中，连接关闭则情况所有的消息
						while (!queue.isEmpty()) {
							Message m = (Message) queue.peek();

							if (m instanceof ResponseMessage) {
								int mlen = 0;
								try {
									mlen = m.getLength();
								} catch (Exception e) {
									queue.pop();
									if (sendErrorLogger.isWarnEnabled()) sendErrorLogger.warn("[从队列中取出消息时发生异常] [" + playerName + "] [" + connName + "] [--] [getLength方法发生异常] [" + m.getTypeDescription() + "]", e);
									logger.error("[" + Thread.currentThread().getName() + "] [" + username + "] [" + playerName + "] [send_messages] [failed] [" + m.getTypeDescription() + "] [len:--] [" + conn.getName() + "] [cost:0] ", e);
									break;
								}
								// 直接丢弃超过缓冲区大小的消息
								if (mlen > sendBufferSize) {
									queue.pop();
									if (sendErrorLogger.isWarnEnabled()) sendErrorLogger.warn("[从队列中取出消息并丢弃] [{}] [{}] [--] [消息太大,超过发送缓冲区大小] [{}] [len:{}]", new Object[] { playerName, connName, m.getTypeDescription(), mlen });
									logger.error("[{}] [{}] [{}] [send_messages] [failed] [{}] [len:--] [{}] [{}] [cost:0] [消息太大,超过发送缓冲区大小]", new Object[] { Thread.currentThread().getName(), username, playerName, m.getTypeDescription(), conn.getName() });
									continue;
								}

								if (index < responses.length && length + mlen < sendBufferSize) {
									responses[index] = (ResponseMessage) m;
									length += mlen;
									queue.pop();
									index++;
								} else {
									break;
								}
							} else {
								break;
							}
						}

						if (index > 0) {
							int k = 0;
							for (k = 0; k < index; k++) {
								try {
									doSendMessage(conn, responses[k]);
								} catch (Throwable e) {
									if (conn == null || conn.getState() == Connection.CONN_STATE_CLOSE) {
										StringBuffer sb = new StringBuffer();

										for (int j = k; j < index; j++) {
											Message m = responses[j];
											sb.append("," + m.getTypeDescription());
										}
										while (!queue.isEmpty()) {
											Message m = (Message) queue.pop();
											if (m != null) {
												sb.append("," + m.getTypeDescription());
											}
										}
										if (sb.length() == 0) {
											sb.append(Translate.text_4213);
										}
										if (conn == null) {
											if (sendErrorLogger.isWarnEnabled()) sendErrorLogger.warn("[数据包发送失败] [" + playerName + "] [" + connName + "] [--] [对应队列上连接为空] [清空队列中剩余所有数据包:" + sb.toString() + "]", e);
										} else {
											if (sendErrorLogger.isWarnEnabled()) sendErrorLogger.warn("[数据包发送失败] [" + playerName + "] [" + connName + "] [" + Connection.getStateString(conn.getState()) + "] [连接已关闭] [清空队列中剩余所有数据包:" + sb.toString() + "]", e);
										}
									} else {
										if (now - lastReceiveRequest > 600000) {
											logger.error("[数据包发送失败，并且10分钟没有收到玩家的消息，强制中断连接] [" + username + "] [" + playerName + "] [send_messages] [failed] [--] [len:--] [" + conn.getName() + "] [cost:0]", e);
											conn.close();
										} else {
											// 需要特别注意，曾今出现参数传错了，导致服务器负载特别高，此bug2年后才发现
											queue.pushBackToHeader(responses, k, index - k);
											synchronized (conn) {
												try {
													conn.wait(100);
												} catch (java.lang.InterruptedException ex) {
													e.printStackTrace();
												}
											}
											if (sendErrorLogger.isWarnEnabled()) sendErrorLogger.warn("[数据包发送失败] [" + playerName + "] [" + connName + "] [" + Connection.getStateString(conn.getState()) + "] [出现发送异常，等待" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "毫秒] [将取出来的消息返回到队列中]", e);
											break;
										}
									}
								}
							}
						}

						for (int i = 0; i < index; i++) {
							responses[i] = null;
						}

					} catch (Throwable e) {
						//
						sendErrorLogger.error(Translate.text_4214, e);
					} finally {
						queue.returnToSelector();
					}

				} catch (Exception e) {
					if (sendErrorLogger.isWarnEnabled()) sendErrorLogger.warn("[IN-QUEUE-SELECTOR] [" + Thread.currentThread().getName() + "] [catch-exception]", e);
				}
			}
		}
	}

	public void setIpAllows(String allows) {
		m_ipAllows = allows.split("[ ,;]+");
	}

	/**
	 * 判断来取消息的机器，是否在允许的范围内
	 * 
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
		synchronized (GameNetworkFramework.class) {
			connectionNum++;
		}

		String identity = null;
		ByteChannel channel = conn.getChannel();
		if (channel instanceof SocketChannel) {
			InetSocketAddress isa = (InetSocketAddress) ((SocketChannel) channel).socket().getRemoteSocketAddress();
			((SocketChannel) channel).socket().setTcpNoDelay(true);
			((SocketChannel) channel).socket().setSoTimeout(5000);

			String remoteHost = isa.getAddress().getHostAddress();
			identity = remoteHost + ":" + isa.getPort();
			if (isIpAllows(remoteHost) == false) {
				throw new IOException("invalid client ip [" + remoteHost + "]");
			}
		} else {
			throw new IOException("invalid channel type");
		}

		if (f5Header.length > 0) {
			try {
				ByteBuffer bb = ByteBuffer.allocate(f5Header.length);
				while (bb.remaining() > 0) {

					// System.out.println("[=================] ["+conn.getName()+"] [read_before]");

					int r = channel.read(bb);

					// System.out.println("[=================] ["+conn.getName()+"] [read_after] [r="+r+"]");

					if (r == -1) {
						throw new IOException("读取分发头信息时，网络中断!");
					}

					if (r == 0) {
						if (logger.isWarnEnabled()) logger.warn("读取分发头信息出错，读不到数据! conn = [" + conn.getName() + "]");
					}
				}
				bb.flip();
				byte head[] = new byte[f5Header.length];
				for (int i = 0; i < f5Header.length; i++) {
					byte b = bb.get();
					head[i] = b;
					if (b != f5Header[i]) {
						if (logger.isWarnEnabled()) logger.warn("被允许的:分发头信息不匹配，第({})个字节不同(0x{},0x{})!", new Object[] { i, Integer.toHexString(f5Header[i]), Integer.toHexString(b) });
					}
				}
				if (logger.isDebugEnabled()) logger.debug("[check_connection_header] {connection:[0x{}] [0x{}]} {need:[0x{}] [0x{}]}", new Object[] { Integer.toHexString(head[0]), Integer.toHexString(head[1]), Integer.toHexString(f5Header[0]), Integer.toHexString(f5Header[1]) });
			} catch (IOException e) {
				if (logger.isWarnEnabled()) logger.warn("[----] [connection] [连接失败] [" + conn.getName() + "]", e);
				throw e;
			}

		}
		conn.setMaxWindowSize(maxWindowSize);
		conn.setMaxReSendTimes(maxReSendTimes);
		conn.setSendBufferSize((int) sendBufferSize);
		conn.setReceiveBufferSize((int) receiveBufferSize);
		conn.setWaitingRequestMessageTimeout(waitingRequestMessageTimeout);
		conn.setWaitingResponseMessageTimeout(waitingResponseMessageTimeout);
		conn.setWaitingMoreSocketSendBufferTimeout(waitingMoreSocketSendBufferTimeout);

		conn.setExceptionObserver(new com.xuanzhi.tools.transport.ExceptionObserver() {
			public void catchException(Connection conn, Exception e, String description, byte data[], int offset, int size) {
				if (logger.isWarnEnabled()) logger.warn("[----] [connection] [catch-exception] [" + conn.getName() + "] [" + description + "] [" + StringUtil.toHex(data) + "]", e);
			}
		});

		conn.setMessageFactory(mf);
		conn.setMessageHandler(this);
		conn.setConnectionTerminateHandler(this);
		if (identity == null) identity = DateUtil.formatDate(new java.util.Date(), "yyyyMMddHHmmss") + StringUtil.randomString(6);
		conn.setIdentity(identity);

		if (this.queueSelectorEnabled) {
			DefaultSelectableQueue dq = new DefaultSelectableQueue(4096 * 2);
			conn.setAttachment2(dq);
			dq.setAttachment(conn);

			dq.register(queueSelector);
		}

		// 停止统计各个玩家发送的
		// FLOWSTAT f = new FLOWSTAT();
		// conn.setAttachmentData(FLOWSTAT, f);

		TIME_SYNC_REQ req = new TIME_SYNC_REQ(GameMessageFactory.nextSequnceNum(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis());

		sendMessage(conn, req, "synchronize_game_time");
		if (logger.isWarnEnabled()) logger.warn("[----] [connection] [connected] [{}]", new Object[] { conn.getName() });
	}

	public void ternimate(Connection conn, List<RequestMessage> messages, ByteBuffer receiveBuffer) {
		synchronized (GameNetworkFramework.class) {
			connectionNum--;
		}
		if (this.queueSelectorEnabled) {
			DefaultSelectableQueue dq = (DefaultSelectableQueue) conn.getAttachment2();
			if (dq != null) {
				dq.unregister();
			}
		}
		String username = (String) conn.getAttachmentData(SecuritySubSystem.USERNAME);
		Player player = (Player) conn.getAttachmentData("Player");
		String playerName = "-";
		if (player != null) {
			playerName = player.getName();
			String mapName = "";
			if (player.getMapName() != null) {
				mapName = player.getMapName();
			}
			if (mapName == null) {
				if (player.getLastGame() != null) {
					mapName = player.getLastGame();
				} else {
					mapName = "";
				}
			}
			Player.sendPlayerAction(player, PlayerActionFlow.行为类型_断开联接, mapName, 0, new Date(), GamePlayerManager.isAllowActionStat());
		}
		FLOWSTAT f = (FLOWSTAT) conn.getAttachmentData(FLOWSTAT);
		if (f == null) f = new FLOWSTAT();

		if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [connection] [ternimate] [{}] [时长:{}] [发送包:{}] [发送数据:{}] [接收包:{}] [接收数据:{}]", new Object[] { username, playerName, conn.getName(), (f.lastSendTime - f.startTime), f.sendPackets, f.sendBytes, f.receivePackets, f.receiveBytes });

		if (sendErrorLogger.isWarnEnabled()) sendErrorLogger.warn("[{}] [{}] [连接关闭] [流量统计] [{}] [时长:{}] [发送包:{}] [发送数据:{}] [接收包:{}] [接收数据:{}] [{}] [{}]", new Object[] { username, playerName, conn.getName(), (f.lastSendTime - f.startTime), f.sendPackets, f.sendBytes, f.receivePackets, f.receiveBytes, f.getSendMapInfo(10), f.getReceiveMapInfo(10) });
	}

	/**
	 * 发送一个消息给指定的客户端
	 * 
	 * @param conn
	 *            客户端连接
	 * @param req
	 *            消息
	 * @param description
	 *            日志中的描述
	 */
	public void sendMessage(Connection conn, Message req, String description) {
		if (conn == null) {
			if (logger.isWarnEnabled()) logger.warn("[discard_message] [conn_is_full] [{}]", new Object[] { req.getTypeDescription() });
			return;
		}
		if (this.queueSelectorEnabled) {
			DefaultSelectableQueue dq = (DefaultSelectableQueue) conn.getAttachment2();
			if (dq != null) {
				long startTime = 0;
				if (logger.isDebugEnabled()) {
					startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				}

				Message m = (Message) dq.push(req);

				if (m != null) {
					if (logger.isWarnEnabled()) logger.warn("[discard_message] [queue_is_full] [{}] [{}] [{}]", new Object[] { m.getTypeDescription(), conn.getName(), Connection.getStateString(conn.getState()) });
				}
				String username = (String) conn.getAttachmentData(SecuritySubSystem.USERNAME);
				if (statFlowUserSet.contains(username)) {
					logger.error("[put_message_to_queue] [{}] [size:{}] [{}] [{}] [cost:{}ms]", new Object[] { req.getTypeDescription(), req.getLength(), conn.getName(), Connection.getStateString(conn.getState()), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
				}

			} else {
				if (logger.isWarnEnabled()) logger.warn("[conn_hasn't_select_queue] [{}] [{}] {}", new Object[] { conn.getName(), Connection.getStateString(conn.getState()), description });
			}
		} else {
			SendMessage sm = (SendMessage) sendQueue.push(new SendMessage(conn, req, description));
			if (sm != null) {
				if (logger.isWarnEnabled()) logger.warn("[discard_send_message_queue_is_full] [{}] [{}] [{}] {} [{}]", new Object[] { conn.getName(), Connection.getStateString(conn.getState()), description, sendQueue.size(), sm.req.getTypeDescription() });
			}
		}
	}

	private static class SendMessage {
		Connection conn;
		Message req;
		String description;

		SendMessage(Connection s, Message req, String description) {
			this.conn = s;
			this.req = req;
			this.description = description;
		}
	}

	/**
	 * 底层发送数据：
	 * 1. 检查连接必须不是CONN_STATE_CLOSE和CONN_STATE_UNKNOWN，否则抛出异常
	 * 2. 先从Selector中取出连接，连接必须合法，没有chekcout，同时必须是CONN_STATE_WAITING_MESSAGE或者CONN_STATE_WAITING_REPLY，
	 * 如果取不出连接，抛出异常，取连接的过程中，也可能抛出异常IOException
	 * 3.
	 * 
	 * @param conn
	 * @param req
	 * @param offset
	 * @param len
	 */
	private void doSendMessage(Connection conn, Message req) throws Exception {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		String username = (String) conn.getAttachmentData(SecuritySubSystem.USERNAME);
		Player player = (Player) conn.getAttachmentData("Player");
		String playerName = "-";
		String connName = conn.getName();
		if (player != null) {
			playerName = player.getName();
		}

		if (conn.getState() != Connection.CONN_STATE_CLOSE && conn.getState() != Connection.CONN_STATE_UNKNOWN) {
			try {
				conn = conn.getConnectionSelector().takeoutConnection(new SelectorPolicy.ConnectionSelectorPolicy(conn), takeOutTimeout);
			} catch (IOException e) {
				if (logger.isWarnEnabled()) logger.warn("[" + Thread.currentThread().getName() + "] [" + username + "] [" + playerName + "] [send_message] [fail] [" + req.getTypeDescription() + "] [len:" + req.getLength() + "] [" + connName + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "]", e);
				throw e;
			}
			if (conn != null) {
				try {
					if (req instanceof RequestMessage) {
						conn.writeMessage((RequestMessage) req, false);
					} else {
						conn.writeResponse((ResponseMessage) req);
					}
					sendPacketNum++;
					this.sendPacketTotalSize += req.getLength() + 63;
					FLOWSTAT f = (FLOWSTAT) conn.getAttachmentData(FLOWSTAT);
					if (f != null) {
						f.sendPackets++;
						f.sendBytes += req.getLength() + 63;
						f.lastSendTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
						f.notifySendPacket(req, req.getLength() + 63);
					}
					if (statFlowUserSet.contains(username)) {
						logger.error("[{}] [{}] [{}] [send_message] [succ] [{}] [len:{}] [{}] [cost:{}]", new Object[] { Thread.currentThread().getName(), username, playerName, req.getTypeDescription(), req.getLength(), connName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
					} else if (logger.isDebugEnabled()) {
						logger.debug("[{}] [{}] [{}] [send_message] [succ] [{}] [len:{}] [{}] [cost:{}]", new Object[] { Thread.currentThread().getName(), username, playerName, req.getTypeDescription(), req.getLength(), connName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
					}
//					if(req instanceof SHOP_OTHER_INFO_RES) {
//						logger.error("[发送_SHOP_OTHER_INFO_RES] \n" + StringUtil.getStackTrace(Thread.currentThread().getStackTrace()));
//					}
				} catch (SendBufferFullException e) {
					throw e;
				} catch (Exception e) {
					if (logger.isWarnEnabled()) logger.warn("[" + Thread.currentThread().getName() + "] [" + username + "] [" + playerName + "] [send_message] [fail] [" + req.getTypeDescription() + "] [" + connName + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "]", e);
					throw e;
				}
			} else {
				if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [send_message] [fail] [{}] [{}] [cost:{}] -- [can't_takeout_conn]", new Object[] { Thread.currentThread().getName(), username, playerName, req.getTypeDescription(), connName, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
				throw new Exception("can't_takeout_connection");
			}
		} else {
			if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [send_message] [fail] [{}] [{}] [cost:{}] -- [sprite_not_online]", new Object[] { Thread.currentThread().getName(), username, playerName, req.getTypeDescription(), connName, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
			throw new Exception("player_not_online");
		}
	}

	/**
	 * 底层发送数据：
	 * 1. 检查连接必须不是CONN_STATE_CLOSE和CONN_STATE_UNKNOWN，否则抛出异常
	 * 2. 先从Selector中取出连接，连接必须合法，没有chekcout，同时必须是CONN_STATE_WAITING_MESSAGE或者CONN_STATE_WAITING_REPLY，
	 * 如果取不出连接，抛出异常，取连接的过程中，也可能抛出异常IOException
	 * 3.
	 * 
	 * @param conn
	 * @param req
	 * @param offset
	 * @param len
	 */
	private void doSendMessage(Connection conn, RequestMessage req[], int offset, int len) throws Exception {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		String username = (String) conn.getAttachmentData(SecuritySubSystem.USERNAME);
		Player player = (Player) conn.getAttachmentData("Player");
		String playerName = "-";
		if (player != null) {
			playerName = player.getName();
		}
		String connName = conn.getName();
		String connId = conn.getIdentity();

		if (conn.getState() != Connection.CONN_STATE_CLOSE && conn.getState() != Connection.CONN_STATE_UNKNOWN) {
			try {
				conn = conn.getConnectionSelector().takeoutConnection(new SelectorPolicy.ConnectionSelectorPolicy(conn), takeOutTimeout);
			} catch (IOException e) {
				if (logger.isWarnEnabled()) logger.warn("[" + Thread.currentThread().getName() + "] [" + username + "] [" + playerName + "] [send_messages] [fail] [" + (toString(req, offset, len)) + "] [len:" + (getLength(req, offset, len)) + "] [" + conn.getName() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "] ", e);
				throw e;
			}

			if (conn != null) {
				try {
					conn.writeMessage(req, offset, len, false);
					sendPacketNum += len;
					FLOWSTAT f = (FLOWSTAT) conn.getAttachmentData(FLOWSTAT);
					if (f != null) {
						f.sendPackets++;
						f.lastSendTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					}
					for (int i = 0; i < len; i++) {
						this.sendPacketTotalSize += req[i + offset].getLength() + 63;
						if (f != null) {
							f.sendBytes += req[i + offset].getLength() + 63;
							f.notifySendPacket(req[i + offset], req[i + offset].getLength() + 63);
						}

					}
					if (statFlowUserSet.contains(username)) {
						logger.error("[{}] [{}] [{}] [send_messages] [succ] [{}] [len:{}] [{}] [cost:{}] ", new Object[] { Thread.currentThread().getName(), username, playerName, (toString(req, offset, len)), (getLength(req, offset, len)), conn.getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
					} else if (logger.isDebugEnabled()) {
						logger.debug("[{}] [{}] [{}] [send_messages] [succ] [{}] [len:{}] [{}] [cost:{}] ", new Object[] { Thread.currentThread().getName(), username, playerName, (toString(req, offset, len)), (getLength(req, offset, len)), conn.getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
					}
				} catch (SendBufferFullException e) {
					throw e;
				} catch (Exception e) {
					if (logger.isWarnEnabled()) logger.warn("[" + Thread.currentThread().getName() + "] [" + username + "] [" + playerName + "] [send_messages] [fail] [" + (toString(req, offset, len)) + "] [len:" + (getLength(req, offset, len)) + "] [" + conn.getName() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "] ", e);
					throw e;
				}
			} else {
				if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [send_messages] [fail] [{}] [len:{}] [{}] [{}] [cost:{}] -- [can't_takeout_conn]", new Object[] { Thread.currentThread().getName(), username, playerName, (toString(req, offset, len)), (getLength(req, offset, len)), connName, connId, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
				throw new Exception("can't takeout connection");
			}
		} else {
			if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [send_messages] [fail] [{}] [len:{}] [{}] [cost:{}] -- [player_not_online]", new Object[] { Thread.currentThread().getName(), username, playerName, (toString(req, offset, len)), (getLength(req, offset, len)), conn.getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
			throw new Exception("player not online");
		}
	}

	public void discardRequestMessage(Connection conn, RequestMessage req) throws ConnectionException {
		String username = (String) conn.getAttachmentData(SecuritySubSystem.USERNAME);
		Player player = (Player) conn.getAttachmentData("Player");
		String playerName = "-";
		if (player != null) {
			playerName = player.getName();
		}

		if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [discard_messages] [-] [{}] [len:{}] [{}]", new Object[] { username, playerName, req.getTypeDescription(), req.getLength(), conn.getName() });

	}

	public ResponseMessage receiveRequestMessage(Connection conn, RequestMessage request) throws ConnectionException {

		while (!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		String username = (String) conn.getAttachmentData(SecuritySubSystem.USERNAME);
		Player player = (Player) conn.getAttachmentData("Player");
		String playerName = "-";
		if (player != null) {
			playerName = player.getName();
		}
		conn.setAttachmentData(LASTRECEIVETIME, startTime);

		// 检查此链接对应的玩家，身上的链接是否是这个链接
		if (player != null) {
			Connection conn2 = player.getConn();
			if (conn2 != null && conn2 != conn && conn2.getState() != Connection.CONN_STATE_CLOSE) {

				if (logger.isWarnEnabled()) logger.warn("[链接上的玩家身上的链接不是此链接，强制关闭此链接] [此链接：" + conn.getName() + "] [玩家身上链接：" + conn2.getName() + "] [" + username + "] [" + playerName + "] [" + request.getTypeDescription() + "]");

				throw new ConnectionException("链接上的玩家身上的链接不是此链接，强制关闭此链接");
			}
		}

		receivePacketNum++;
		receivePacketTotalSize += request.getLength() + 63;
		FLOWSTAT f = (FLOWSTAT) conn.getAttachmentData(FLOWSTAT);
		// 只统计配置了的用户
		if (f == null) {
			if (statFlowUserSet.contains(username)) {
				f = new FLOWSTAT();
				conn.setAttachmentData(FLOWSTAT, f);
			}
		}

		if (f != null) {
			f.receivePackets++;
			f.receiveBytes += request.getLength() + 63;
			f.notifyReceivePacket(request.getTypeDescription(), request.getLength() + 63);
			f.lastReceiveTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		}

		// 检查sequenceNum

		if (测试开启) {
			long sequenceNums[] = (long[]) conn.getAttachmentData(SEQUENCE);
			if (sequenceNums == null) {
				getReqSequenceNum(conn);
				// 发生成序列号的4个参数
				sequenceNums = (long[]) conn.getAttachmentData(SEQUENCE);
				SEQUENCE_NUM_PARAMETERS_REQ req = new SEQUENCE_NUM_PARAMETERS_REQ(GameMessageFactory.nextSequnceNum(), new long[] { sequenceNums[1], sequenceNums[2], sequenceNums[3], sequenceNums[4] });
				sendMessage(conn, req, "");
			} else {
				getReqSequenceNum(conn);
				if (sequenceNums[0] != request.getSequnceNum()) {
					sequenceNums[5]++;
				}
			}
		} else {
			long sequenceNums[] = (long[]) conn.getAttachmentData(SEQUENCE);
			if (sequenceNums == null) {
				sequenceNums = new long[2];
				sequenceNums[0] = request.getSequnceNum();
				conn.setAttachmentData(SEQUENCE, sequenceNums);
			} else {
				if (sequenceNums[0] >= request.getSequnceNum() || request.getSequnceNum() >= sequenceNums[0] + 4) {
					sequenceNums[1]++;
				} else {
					sequenceNums[0] = request.getSequnceNum();
				}
			}
		}

		GameSubSystem gss[] = message2SubSysMap.get(request.getTypeDescription());
		if (gss != null && gss.length > 0) {
			for (int i = 0; i < gss.length; i++) {
				ResponseMessage res;
				try {
					res = gss[i].handleReqeustMessage(conn, request);
					if (res != null) {

						if (statFlowUserSet.contains(username)) {
							if (username == null) username = (String) conn.getAttachmentData(SecuritySubSystem.USERNAME);
							if (player == null) player = (Player) conn.getAttachmentData("Player");
							if (player != null) {
								playerName = player.getName();
							}
							logger.error("[{}] [{}] [receive-request] [success] [return-{}-on-{}-subsystem] [{}] [req_len:{}] [res_len:{}] [seq:{}] [{}] [cost:{}]", new Object[] { username, playerName, res.getTypeDescription(), gss[i].getName(), request.getTypeDescription(), request.getLength(), res.getLength(), request.getSequnceNum(), conn.getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });

						} else {

							if (username == null) username = (String) conn.getAttachmentData(SecuritySubSystem.USERNAME);
							if (player == null) player = (Player) conn.getAttachmentData("Player");
							if (player != null) {
								playerName = player.getName();
							}

							long costTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime;
							if (costTime >= 500) {
								if (costTime >= 5000) {
									sendDelayMessageHandle(request.getTypeDescription(), costTime);
									logger.error("[{}] [{}] [receive-request] [success] [return-{}-on-{}-subsystem] [{}] [req_len:{}] [res_len:{}] [seq:{}] [{}] [cost:{}]", new Object[] { username, playerName, res.getTypeDescription(), gss[i].getName(), request.getTypeDescription(), request.getLength(), res.getLength(), request.getSequnceNum(), conn.getName(), costTime });
								} else {
									logger.warn("[{}] [{}] [receive-request] [success] [return-{}-on-{}-subsystem] [{}] [req_len:{}] [res_len:{}] [seq:{}] [{}] [cost:{}]", new Object[] { username, playerName, res.getTypeDescription(), gss[i].getName(), request.getTypeDescription(), request.getLength(), res.getLength(), request.getSequnceNum(), conn.getName(), costTime });
								}
							} else {
								if (logger.isInfoEnabled()) logger.info("[{}] [{}] [receive-request] [success] [return-{}-on-{}-subsystem] [{}] [req_len:{}] [res_len:{}] [seq:{}] [{}] [cost:{}]", new Object[] { username, playerName, res.getTypeDescription(), gss[i].getName(), request.getTypeDescription(), request.getLength(), res.getLength(), request.getSequnceNum(), conn.getName(), costTime });
							}
						}
						this.sendPacketTotalSize += res.getLength() + 63;
						sendPacketNum++;
						return res;
					}
				} catch (ConnectionException e) {
					logger.error("[" + username + "] [" + playerName + "] [receive-request] [error] [catch-ConnectionException-on-" + gss[i].getName() + "-subsystem] [" + request.getTypeDescription() + "] [" + conn.getName() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "]", e);
					throw e;
				} catch (Throwable e) {
					if (logger.isWarnEnabled()) logger.warn("[" + username + "] [" + playerName + "] [receive-request] [fail] [catch-exception-on-" + gss[i].getName() + "-subsystem] [" + request.getTypeDescription() + "] [" + conn.getName() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "] [error:" + e + "]", e);
					return null;
				}
			}
		} else {
			if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [receive-request] [error] [unknown-interested-subsystems] [{}] [{}] [cost:{}]", new Object[] { username, playerName, request.getTypeDescription(), conn.getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
			return null;
		}

		long costTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime;
		if (costTime >= 500) {
			if (costTime >= 5000) {
				sendDelayMessageHandle(request.getTypeDescription(), costTime);
				logger.error("[{}] [{}] [receive-request] [success] [return-null] [{}]  [req_len:{}] [res_len:0] [seq:{}] [{}] [cost:{}]", new Object[] { username, playerName, request.getTypeDescription(), request.getLength(), request.getSequnceNum(), conn.getName(), costTime });
			} else {
				logger.warn("[{}] [{}] [receive-request] [success] [return-null] [{}]  [req_len:{}] [res_len:0] [seq:{}] [{}] [cost:{}]", new Object[] { username, playerName, request.getTypeDescription(), request.getLength(), request.getSequnceNum(), conn.getName(), costTime });
			}
		}
		if (statFlowUserSet.contains(username) || logger.isInfoEnabled()) {

			if (username == null) username = (String) conn.getAttachmentData(SecuritySubSystem.USERNAME);
			if (player == null) player = (Player) conn.getAttachmentData("Player");
			if (player != null) {
				playerName = player.getName();
			}
			if (statFlowUserSet.contains(username)) {
				logger.error("[{}] [{}] [receive-request] [success] [return-null] [{}]  [req_len:{}] [res_len:0] [seq:{}] [{}] [cost:{}]", new Object[] { username, playerName, request.getTypeDescription(), request.getLength(), request.getSequnceNum(), conn.getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
			} else if (logger.isInfoEnabled()) {
				logger.info("[{}] [{}] [receive-request] [success] [return-null] [{}]  [req_len:{}] [res_len:0] [seq:{}] [{}] [cost:{}]", new Object[] { username, playerName, request.getTypeDescription(), request.getLength(), request.getSequnceNum(), conn.getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
			}
		}

		return null;
	}

	public static long delayTime = 5000L;
	public static boolean sendDelay = true;
	public static String serverName = "";
	MieshiGatewayClientService mgs;

	private void sendDelayMessageHandle(String messageName, long costTime) {
		if(serverName == null || serverName.equals("")){
			serverName = GameConstants.getInstance().getServerName();
		}
		if (sendDelay && costTime >= delayTime) {
			if (mgs == null) {
				mgs = MieshiGatewayClientService.getInstance();
			}
			Message m = new REPORT_LONG_PROTOCAL_REQ(GameMessageFactory.nextSequnceNum(), serverName, messageName, costTime);
			mgs.sendMessageToGateway(m);
			logger.warn("[discard_message] [MieshiGatewayClientService] [queue_is_full] [" + m.getTypeDescription() + "]");
		}
	}

	Random random = new Random();

	boolean 测试开启 = false;

	/**
	 * 根据conn中的SEQUENCE为key所对应的value数组的前4位为参数
	 * @param conn
	 */
	public long getReqSequenceNum(Connection conn) {
		long sequenceNums[] = (long[]) conn.getAttachmentData(SEQUENCE);
		if (sequenceNums == null) {
			sequenceNums = new long[6];
			sequenceNums[1] = random.nextInt();
			sequenceNums[2] = random.nextInt();
			sequenceNums[3] = random.nextInt();
			sequenceNums[4] = random.nextInt();
			conn.setAttachmentData(SEQUENCE, sequenceNums);
		} else {
			// 产生序列号的多项式
			long a = sequenceNums[1];
			long b = sequenceNums[2];
			long c = sequenceNums[3];
			long d = sequenceNums[4];

			sequenceNums[1] = a ^ b + b | c + 3 + d;
			sequenceNums[2] = b - a + d * 123;
			sequenceNums[3] = (c % 13456) + a * b + (long) (Math.sqrt(Math.abs(d)));
			sequenceNums[4] = (long) (a * 1.233 + b * 0.45456 + c * d + 9);
			sequenceNums[0] = (long) (a * 2 + b + c * 3 + d);
		}
		return sequenceNums[0];
	}

	public void receiveResponseMessage(Connection conn, RequestMessage req, ResponseMessage res) throws ConnectionException {

		while (!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		this.receivePacketTotalSize += res.getLength() + 63;
		String username = (String) conn.getAttachmentData(SecuritySubSystem.USERNAME);
		Player player = (Player) conn.getAttachmentData("Player");
		String playerName = "-";
		if (player != null) {
			playerName = player.getName();
		}

		GameSubSystem gss[] = message2SubSysMap.get(res.getTypeDescription());
		if (gss != null) {
			for (int i = 0; i < gss.length; i++) {
				boolean b;
				try {
					b = gss[i].handleResponseMessage(conn, req, res);
					if (b) {
						if (statFlowUserSet.contains(username)) {
							logger.error("[{}] [{}] [receive-response] [success] [return-return-on-{}-subsystem] [{}] [{}] [cost:{}]", new Object[] { username, playerName, gss[i].getName(), res.getTypeDescription(), conn.getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
						} else if (logger.isInfoEnabled()) {
							logger.info("[{}] [{}] [receive-response] [success] [return-return-on-{}-subsystem] [{}] [{}] [cost:{}]", new Object[] { username, playerName, gss[i].getName(), res.getTypeDescription(), conn.getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
						}
						return;
					}
				} catch (ConnectionException e) {
					logger.error("[" + username + "] [" + playerName + "] [receive-response] [error] [catch-ConnectionException-on-" + gss[i].getName() + "-subsystem] [" + res.getTypeDescription() + "] [" + conn.getName() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "]", e);
					throw e;
				} catch (Exception e) {
					if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [receive-response] [fail] [cat-exception-on-{}-subsystems] [{}] [{} [cost:{}]", new Object[] { username, playerName, gss[i].getName(), res.getTypeDescription(), conn.getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
					return;
				}
			}
		}

		if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [receive-response] [success] [no-interested-subsystems] [{}] [{}] [cost:{}]", new Object[] { username, playerName, res.getTypeDescription(), conn.getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });

	}

	public RequestMessage waitingTimeout(Connection arg0, long arg1) throws ConnectionException {
		// return new ACTIVE_TEST_REQ(GameMessageFactory.nextSequnceNum());
		return null;
	}

	public void run() {
		while (running) {
			try {
				SendMessage sm = (SendMessage) this.sendQueue.pop(5000L);
				if (sm != null && sm.conn != null) {
					doSendMessage(sm.conn, sm.req);
				}
			} catch (Exception e) {
				logger.error("[" + Thread.currentThread().getName() + "] [in run method]", e);
			}
		}
	}

	public static String toString(Message[] reqs, int offset, int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; reqs != null && i < len; i++) {
			sb.append(reqs[offset + i].getTypeDescription());

			// if (reqs[offset + i].getTypeDescription().equals("AROUND_CHANGE_REQ")) {
			// AROUND_CHANGE_REQ req = (AROUND_CHANGE_REQ) reqs[offset + i];
			// sb.append("{");
			// Player ps[] = req.getEnterPlayers();
			// if (ps.length > 0) {
			// sb.append(Translate.text_4215);
			// for (int j = 0; j < ps.length; j++) {
			// sb.append(":" + ps[j].getName());
			// }
			// }
			// Sprite ss[] = req.getEnterSprites();
			// if (ss.length > 0) {
			// sb.append(Translate.text_4216);
			// for (int j = 0; j < ss.length; j++) {
			// sb.append(":" + ss[j].getName() + "(" + ss[j].getId() + ")");
			// }
			// }
			// Sprite pets[] = req.getEnterPets();
			// if (pets.length > 0) {
			// sb.append(Translate.text_4216);
			// for (int j = 0; j < pets.length; j++) {
			// sb.append(":" + pets[j].getName() + "(" + pets[j].getId() + ")");
			// }
			// }
			// ps = req.getLeavePlayers();
			// if (ps.length > 0) {
			// sb.append(Translate.text_4217);
			// for (int j = 0; j < ps.length; j++) {
			// sb.append(":" + ps[j].getName());
			// }
			// }
			// ss = req.getLeaveSprites();
			// if (ss.length > 0) {
			// sb.append(Translate.text_4218);
			// for (int j = 0; j < ss.length; j++) {
			// sb.append(":" + ss[j].getName() + "(" + ss[j].getId() + ")");
			// }
			// }
			// ss = req.getLeavePets();
			// if (ss.length > 0) {
			// sb.append(Translate.text_4218);
			// for (int j = 0; j < ss.length; j++) {
			// sb.append(":" + ss[j].getName() + "(" + ss[j].getId() + ")");
			// }
			// }
			//
			// MoveTrace4Client[] mts = req.getMoveLivings();
			// if (mts.length > 0) {
			// sb.append("路径:");
			// for (int j = 0; j < mts.length; j++) {
			// sb.append(":" + mts[j].getType() + "(" + mts[j].getId() + ")");
			// }
			// }
			//
			// sb.append("}");
			// }

			if (i < len - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public static int getLength(Message[] reqs, int offset, int len) {
		int length = 0;
		for (int i = 0; reqs != null && i < len; i++) {
			length += reqs[offset + i].getLength();
		}
		return length;
	}

	public static final String SEQUENCE = "SEQUENCE";
	public static final String FLOWSTAT = "FLOWSTAT";
	public static final String LASTRECEIVETIME = "LASTRECEIVETIME";

	/**
	 * 流量统计
	 * 
	 * 
	 */
	public static class FLOWSTAT {
		public long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		public long lastSendTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 1;
		public long lastReceiveTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 1;
		public long receivePackets = 0;
		public long receiveBytes = 0;
		public long sendPackets = 0;
		public long sendBytes = 0;
		public HashMap<String, long[]> receiveMap = new HashMap<String, long[]>();
		public HashMap<String, long[]> sendMap = new HashMap<String, long[]>();
		public HashMap<String, long[]> aroundChangeMap = new HashMap<String, long[]>();

		public void notifyReceivePacket(String messageName, int size) {
			long r[] = receiveMap.get(messageName);
			if (r == null) {
				r = new long[2];
				receiveMap.put(messageName, r);
			}
			r[0] += 1;
			r[1] += size;
		}

		public void notifySendPacket(Message message, int size) {
			long r[] = sendMap.get(message.getTypeDescription());
			if (r == null) {
				r = new long[2];
				sendMap.put(message.getTypeDescription(), r);
			}
			r[0] += 1;
			r[1] += size;

			if (message instanceof AROUND_CHANGE_REQ) {
				AROUND_CHANGE_REQ req = (AROUND_CHANGE_REQ) message;

				Player enterPlayers[] = req.getEnterPlayers();
				r = aroundChangeMap.get("enterPlayers");
				if (r == null) {
					r = new long[2];
					aroundChangeMap.put("enterPlayers", r);
				}
				r[0] += enterPlayers.length;
				r[1] += enterPlayers.length * 360;

				Player leavePlayers[] = req.getLeavePlayers();
				r = aroundChangeMap.get("leavePlayers");
				if (r == null) {
					r = new long[2];
					aroundChangeMap.put("leavePlayers", r);
				}
				r[0] += leavePlayers.length;
				r[1] += leavePlayers.length * 8;

				Pet enterPets[] = req.getEnterPets();
				r = aroundChangeMap.get("enterPets");
				if (r == null) {
					r = new long[2];
					aroundChangeMap.put("enterPets", r);
				}
				r[0] += enterPets.length;
				r[1] += enterPets.length * 480;

				Pet leavePets[] = req.getLeavePets();
				r = aroundChangeMap.get("leavePets");
				if (r == null) {
					r = new long[2];
					aroundChangeMap.put("leavePets", r);
				}
				r[0] += leavePets.length;
				r[1] += leavePets.length * 8;

				Sprite enterSprites[] = req.getEnterSprites();
				r = aroundChangeMap.get("enterSprites");
				if (r == null) {
					r = new long[2];
					aroundChangeMap.put("enterSprites", r);
				}
				r[0] += enterSprites.length;
				r[1] += enterSprites.length * 250;

				Sprite leaveSprites[] = req.getLeaveSprites();
				r = aroundChangeMap.get("leaveSprites");
				if (r == null) {
					r = new long[2];
					aroundChangeMap.put("leaveSprites", r);
				}
				r[0] += leaveSprites.length;
				r[1] += leaveSprites.length * 8;

				MoveTrace4Client moveTrace4Clients[] = req.getMoveLivings();
				r = aroundChangeMap.get("moveTrace4Clients");
				if (r == null) {
					r = new long[2];
					aroundChangeMap.put("moveTrace4Clients", r);
				}
				r[0] += moveTrace4Clients.length;
				r[1] += moveTrace4Clients.length * 60;

				FieldChangeEvent fieldChangeEvents[] = req.getChangeEvents();
				r = aroundChangeMap.get("fieldChangeEvents");
				if (r == null) {
					r = new long[2];
					aroundChangeMap.put("fieldChangeEvents", r);
				}
				r[0] += fieldChangeEvents.length;
				r[1] += fieldChangeEvents.length * 20;
			}
		}

		public String getReceiveMapInfo(int topNum) {
			StringBuffer sb = new StringBuffer();
			String keys[] = receiveMap.keySet().toArray(new String[0]);
			Arrays.sort(keys, new Comparator<String>() {
				public int compare(String o1, String o2) {
					long r1[] = receiveMap.get(o1);
					long r2[] = receiveMap.get(o2);
					if (r1[1] < r2[1]) return 1;
					if (r1[1] > r2[1]) return -1;
					return 0;
				}
			});
			for (int i = 0; i < topNum && i < keys.length; i++) {
				long r[] = receiveMap.get(keys[i]);
				sb.append(keys[i] + "{" + r[0] + "," + r[1] + "};");
			}
			return sb.toString();
		}

		public String getSendMapInfo(int topNum) {
			StringBuffer sb = new StringBuffer();
			String keys[] = sendMap.keySet().toArray(new String[0]);
			Arrays.sort(keys, new Comparator<String>() {
				public int compare(String o1, String o2) {
					long r1[] = sendMap.get(o1);
					long r2[] = sendMap.get(o2);
					if (r1[1] < r2[1]) return 1;
					if (r1[1] > r2[1]) return -1;
					return 0;
				}
			});
			for (int i = 0; i < topNum && i < keys.length; i++) {
				long r[] = sendMap.get(keys[i]);
				sb.append(keys[i] + "{" + r[0] + "," + r[1] + "};");
			}
			return sb.toString();
		}
	}
}

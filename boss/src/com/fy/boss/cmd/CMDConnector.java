package com.fy.boss.cmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.cmd.message.CMDMessageFactory;
import com.fy.boss.cmd.message.COMMON_CMD_REQ;
import com.fy.boss.cmd.message.COMMON_CMD_RES;
import com.fy.boss.cmd.message.FILE_PACKET_REQ;
import com.fy.boss.cmd.message.FILE_PACKET_RES;
import com.fy.boss.cmd.message.SERVER_LOG_REQ;
import com.fy.boss.cmd.message.SERVER_LOG_RES;
import com.fy.boss.cmd.message.SERVER_STATUS_REQ;
import com.fy.boss.cmd.message.SERVER_STATUS_RES;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.NumberUtils;
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

public class CMDConnector implements ConnectionConnectedHandler, ConnectionCreatedHandler, ConnectionTerminateHandler, MessageHandler, Runnable {

	static Logger logger = Logger.getLogger(CMDConnector.class);

	CMDMessageFactory mf = CMDMessageFactory.getInstance();

	protected DefaultConnectionSelector selector;

	protected int maxWindowSize = 32;
	protected long waitingResponseMessageTimeout = 1800 * 1000L;
	protected long waitingRequestMessageTimeout = 1800 * 1000L;
	protected int maxReSendTimes = 0;
	// 千万不能开的太大，会出严重的问题，耗掉所有的内测；
	// 一个连接就需要这么多内存，曾经因为开得太大，耗费系统所有的内存，
	// 服务器就和消失了一样 -- myzdf
	protected long sendBufferSize = 5 * 512 * 1024L;
	protected long receiveBufferSize = 5 * 512 * 1024L;
	private long requestTimeout = 5 * 60 * 1000L;

	protected long takeOutTimeout = 60 * 1000;

	protected long receivePacketNum = 0;
	protected long sendPacketNum = 0;

	protected long receivePacketTotalSize = 0;
	protected long sendPacketTotalSize = 0;

	protected int connectionNum = 0;

	protected float receiveSpeed = 0;
	protected float sendSpeed = 0;
	protected long lastSendPacketNum = 0;
	protected long lastReceivePacketNum = 0;
	protected long lastChecktime = System.currentTimeMillis();

	protected boolean running = false;
	protected Thread localThread = null;

	// 客户端的响应
	protected static Hashtable<RequestMessage, ResponseMessage> responseMessageMap = new Hashtable<RequestMessage, ResponseMessage>();

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

	public CMDConnector(DefaultConnectionSelector selector) {
		this.selector = selector;
		this.selector.setConnectionCreatedHandler(this);
		this.selector.setConnectionConnectedHandler(this);
		this.selector.setConnectionSendBufferSize((int) sendBufferSize);
		this.selector.setConnectionReceiveBufferSize((int) receiveBufferSize);
		this.selector.setMaxCheckoutTime(5 * 60 * 1000L);
		localThread = new Thread(this, "CMDConnector");
		localThread.start();
		long now = System.currentTimeMillis();
		logger.info(this.getClass().getName() + " initialize successfully [" + (System.currentTimeMillis() - now) + "]");
	}

	public ConnectionSelector getConnectionSelector() {
		return selector;
	}

	public void setConnectionSelector(ConnectionSelector selector) {
		this.selector = (DefaultConnectionSelector) selector;
		this.selector.setConnectionCreatedHandler(this);
		this.selector.setConnectionConnectedHandler(this);
		this.selector.setConnectionSendBufferSize((int) sendBufferSize);
		this.selector.setConnectionReceiveBufferSize((int) receiveBufferSize);
		this.selector.setMaxCheckoutTime(5 * 60 * 1000L);
	}

	public void discardRequestMessage(Connection conn, RequestMessage req) throws ConnectionException {
		synchronized (req) {
			req.notify();
		}
	}

	private FileChunk chunk = new FileChunk();

	public ResponseMessage receiveRequestMessage(Connection conn, RequestMessage request) throws ConnectionException {
		long startTime = System.currentTimeMillis();
		receivePacketNum++;
		receivePacketTotalSize += request.getLength();

		if (request instanceof COMMON_CMD_REQ) {
			COMMON_CMD_REQ req = (COMMON_CMD_REQ) request;
			String cmd = req.getCmd();
			// 执行命令
			try {
				logger.info("[收到执行命令请求] [" + cmd + "]");
				//Runtime.getRuntime().addShutdownHook(new ShutdownHookThread("[退出应用] ["+cmd+"]"));
				Process proc = Runtime.getRuntime().exec(cmd);
				logger.info("[执行命令完毕0] [" + cmd + "]");
				
				StreamReader in1 = new StreamReader(proc.getInputStream());
				in1.start();
				StreamReader in2 = new StreamReader(proc.getErrorStream());
				in2.start();
				
				if(logger.isDebugEnabled())
				{
					logger.debug("[执行命令完毕] [等待返回执行结果] ["+cmd+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				}
				proc.waitFor();
				
				if(logger.isDebugEnabled())
				{
					logger.debug("[执行命令完毕] [返回执行结果] ["+cmd+"] [退出值:"+proc.exitValue()+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				}
				
				COMMON_CMD_RES res = new COMMON_CMD_RES(req.getSequnceNum(), new String[] { "执行完毕" });
				return res;
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("[执行命令时出错] [" + cmd + "]", e);
				COMMON_CMD_RES res = new COMMON_CMD_RES(req.getSequnceNum(), new String[] { "exec failed" });
				return res;
			}
		}
		if (request instanceof SERVER_STATUS_REQ) {
			SERVER_STATUS_REQ req = (SERVER_STATUS_REQ) request;
			String serverbase = req.getServerbase();
			String procname = req.getProcessname();
			boolean installed = false;
			boolean running = false;
			if (new File(serverbase).isDirectory()) {
				installed = true;
			}
			String pfile = serverbase + "/" + procname + ".pid";
			if (new File(pfile).isFile()) {
				running = true;
			}
			SERVER_STATUS_RES res = new SERVER_STATUS_RES(req.getSequnceNum(), installed, running);
			return res;
		}
		if (request instanceof SERVER_LOG_REQ) {
			SERVER_LOG_REQ req = (SERVER_LOG_REQ) request;
			String logfile = req.getLogpath();
			int num = req.getLastnum();
			String result = "";
			String cmd = "tail -n " + num + " " + logfile;
			try {
				Process proc = Runtime.getRuntime().exec(cmd);
				BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String line = null;
				StringBuffer sb = new StringBuffer();
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				logger.info("[获取日志] [" + num + "] [" + cmd + "] 执行结果:\n" + sb.toString());
				result = sb.toString();
				if (result.length() > 50000) {
					result = result.substring(0, 50000);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("[获取日志] [" + num + "] [执行命令时出错] [" + cmd + "]", e);
			}
			SERVER_LOG_RES res = new SERVER_LOG_RES(req.getSequnceNum(), result);
			return res;
		}
		if (request instanceof FILE_PACKET_REQ) {
			FILE_PACKET_REQ req = (FILE_PACKET_REQ) request;
			logger.info("[receive_FILE_PACKET_REQ] [" + request.getSequnceNum() + "] [" + req.getFilename() + "] [" + req.getIndex() + "] ["
					+ req.getSavepath() + "]");
			byte data[] = req.getFiledata();
			String filename = req.getFilename();
			int total = req.getTotal();
			int index = req.getIndex();
			/*在实际使用当中会发现出现了源文件和发送过来的文件名不符的问题，这个问题是因为可能在上次传输中出现了某些问题，导致chunk变量持有了原有*
			 * 错误的文件，此时发布就不会发布成功，解决方法是当判断索引号是第一个时，而此时chunk的文件名和传入的文件名又不相同，则重新创建一个filechunk
			 */
			
			
			
			String savepath = req.getSavepath();
			synchronized (chunk) {
				if(req.getIndex() == 0)
				{
					if(chunk.getFilename().length() > 0 && !chunk.isCompleted)
					{
						
						logger.warn("[接收文件块] [发现非正常状态,重新创建新的filechunk] [" + chunk.getFilename() + "] [total:" + chunk.getTotal() + "] ["+chunk.getIndex()+"] ["+req.getFilename()+"] ["+chunk.isCompleted+"]");
						chunk = new FileChunk();
					}
				}
				
				
				if (chunk.getFilename().length() == 0) {
					chunk = new FileChunk();
					chunk.setData(data);
					chunk.setFilename(filename);
					chunk.setIndex(index);
					chunk.setSavepath(savepath);
					chunk.setTotal(total);
					logger.info("[接受文件块] [" + chunk.getFilename() + "] [total:" + chunk.getTotal() + "] [index:" + chunk.getIndex() + "]");
					if (chunk != null && chunk.getIndex() == chunk.getTotal() - 1) {
						// 写文件
						FileUtils.chkFolder(chunk.getSavepath());
						try {
							FileOutputStream out = new FileOutputStream(chunk.getSavepath());
							out.write(chunk.getData());
							out.close();
							chunk.isCompleted = true;
							logger.info("[最后写文件] [" + chunk.getSavepath() + "] [size:" + chunk.getData().length + "] [total:" + chunk.getTotal() + "] [index:"
									+ chunk.getIndex() + "]");
							FILE_PACKET_RES res = new FILE_PACKET_RES(req.getSequnceNum(), new String[] { "Write chunk succ" });
							return res;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							logger.error("[最后写文件错误] [" + chunk.getFilename() + "] ["+chunk.getSavepath()+"] [total:" + chunk.getTotal() + "] [index:" + chunk.getIndex() + "]", e);
							FILE_PACKET_RES res = new FILE_PACKET_RES(req.getSequnceNum(), new String[] { "Write chunk error : " + e.getMessage() });
							return res;
						} finally {
							chunk = new FileChunk();
						}
					}
					FILE_PACKET_RES res = new FILE_PACKET_RES(req.getSequnceNum(), new String[] { "Create new chunk" });
					return res;
				} else {
					FileChunk fc = new FileChunk();
					fc.setData(data);
					fc.setFilename(filename);
					fc.setIndex(index);
					fc.setSavepath(savepath);
					fc.setTotal(total);
					int oldIndex = chunk.getIndex();
					try {
						chunk.append(fc);
						logger.info("[合并文件块] [" + chunk.getFilename() + "] [total:" + chunk.getTotal() + "] [index:" + chunk.getIndex() + "] [old index:"
								+ oldIndex + "]");

						if (chunk != null && chunk.getIndex() == chunk.getTotal() - 1) {
							// 写文件
							FileUtils.chkFolder(chunk.getSavepath());
							try {
								FileOutputStream out = new FileOutputStream(chunk.getSavepath());
								out.write(chunk.getData());
								out.close();
								logger.info("[最后写文件] [" + chunk.getSavepath() + "] [size:" + chunk.getData().length + "] [total:" + chunk.getTotal()
										+ "] [index:" + chunk.getIndex() + "] [old index:" + oldIndex + "]");
								FILE_PACKET_RES res = new FILE_PACKET_RES(req.getSequnceNum(), new String[] { "Write chunk succ" });
								return res;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								logger.error("[最后写文件错误] [" + chunk.getFilename() + "] [total:" + chunk.getTotal() + "] [index:" + chunk.getIndex()
										+ "] [old index:" + oldIndex + "]", e);
								FILE_PACKET_RES res = new FILE_PACKET_RES(req.getSequnceNum(), new String[] { "Write chunk error : " + e.getMessage() });
								return res;
							} finally {
								chunk = new FileChunk();
							}
						}

						FILE_PACKET_RES res = new FILE_PACKET_RES(req.getSequnceNum(), new String[] { "Append chunk." });
						return res;
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("[合并文件块] [异常] [" + chunk.getFilename() + "] [total:" + chunk.getTotal() + "] [index:" + chunk.getIndex() + "] [old index:"
								+ oldIndex + "]", e);
						FILE_PACKET_RES res = new FILE_PACKET_RES(req.getSequnceNum(), new String[] { "Append chunk error : " + e.getMessage() });
						return res;
					}
				}
			}
		}

		logger.warn("[receive-request] [success] [return-null] [" + request.getTypeDescription() + "] [len:" + request.getLength() + "] [" + conn.getName()
				+ "] [" + conn.getIdentity() + "] [cost:" + (System.currentTimeMillis() - startTime) + "]");

		return null;
	}

	public void receiveResponseMessage(Connection conn, RequestMessage req, ResponseMessage res) throws ConnectionException {
		if (req != null) {
			logger.info("[receiveResponseMessage] [" + req.getTypeDescription() + "] [" + res.getTypeDescription() + "] [" + res.getLength() + "]");
			responseMessageMap.put(req, res);
			synchronized (req) {
				req.notify();
			}
		}
	}

	public ResponseMessage requestMessage(RequestMessage req) throws Exception {
		long now = System.currentTimeMillis();
		Connection conn = null;
		try {
			conn = selector.takeoutConnection(SelectorPolicy.DefaultClientModelPolicy, takeOutTimeout);
		} catch (Exception e) {
			throw new Exception("takeoutConnection error cause " + e.getMessage());
		}
		if (conn != null) {
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

				logger.info("[req#] [SUCC] [" + req.getTypeDescription() + "] [" + resp.getTypeDescription() + "] [responseMessageMap="
						+ responseMessageMap.size() + "] [cost:" + (System.currentTimeMillis() - now) + "ms]");

				return resp;
			} else {
				logger.warn("[req#] [FAIL_for_no_resp] [" + req.getTypeDescription() + "] [--] [responseMessageMap=" + responseMessageMap.size() + "] [cost:"
						+ (System.currentTimeMillis() - now) + "ms]");
				throw new Exception("request failed for no response for waiting [" + (System.currentTimeMillis() - now) + "ms]");
			}

		} else {
			logger.warn("[req#] [FAIL_for_no_conn] [" + req.getTypeDescription() + "] [--] [responseMessageMap=" + responseMessageMap.size() + "] [cost:"
					+ (System.currentTimeMillis() - now) + "ms]");
			throw new Exception("request failed for no connection take out from selector for waiting [" + (System.currentTimeMillis() - now) + "ms]");
		}
	}
	
	public void requestMessageNoWait(RequestMessage req) throws Exception {
		long now = System.currentTimeMillis();
		Connection conn = null;
		try {
			conn = selector.takeoutConnection(SelectorPolicy.DefaultClientModelPolicy, takeOutTimeout);
		} catch (Exception e) {
			throw new Exception("takeoutConnection error cause " + e.getMessage());
		}
		if (conn != null) {
			conn.writeMessage(req, true);
			sendPacketNum++;
			this.sendPacketTotalSize += req.getLength();
			
		} else {
			logger.warn("[req#] [FAIL_for_no_conn] [" + req.getTypeDescription() + "] [--] [responseMessageMap=" + responseMessageMap.size() + "] [cost:"
					+ (System.currentTimeMillis() - now) + "ms]");
			throw new Exception("request failed for no connection take out from selector for waiting [" + (System.currentTimeMillis() - now) + "ms]");
		}
	}

	public ResponseMessage requestMessage(Connection conn, RequestMessage req) throws Exception {
		long now = System.currentTimeMillis();
		try {
			conn = selector.takeoutConnection(new SelectorPolicy.ConnectionSelectorPolicy(conn), takeOutTimeout);
		} catch (Exception e) {
			throw new Exception("takeoutConnection error cause " + e.getMessage());
		}
		conn.writeMessage(req, true);
		sendPacketNum++;
		this.sendPacketTotalSize += req.getLength();
		ResponseMessage resp = (ResponseMessage) responseMessageMap.remove(req);
		if (resp == null) {
			synchronized (req) {
				try {
					req.wait(requestTimeout);
				} catch (Exception e) {
					// do something
				}
			}
			resp = (ResponseMessage) responseMessageMap.remove(req);
		}
		if (resp != null) {
			String ss = com.xuanzhi.tools.text.StringUtil.addcommas(System.currentTimeMillis() - now);
			for (int i = ss.length(); i < 5; i++)
				ss += " ";
			logger.info("[req#] [SUCC] [" + ss + "] [" + req.getTypeDescription() + "] [" + responseMessageMap.size() + "]");
			return resp;
		} else {
			logger.warn("[req#] [FAIL] [" + req.getTypeDescription() + "]");
			throw new Exception("request failed for no response.");
		}
	}

	public void sendMessage(RequestMessage req) throws Exception {
		long now = System.currentTimeMillis();
		Connection conn = null;
		try {
			conn = selector.takeoutConnection(SelectorPolicy.DefaultClientModelPolicy, takeOutTimeout);
		} catch (Exception e) {
			try {
				Thread.sleep(1000L);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			throw new Exception("takeoutConnection error cause " + e.getMessage());
		}
		try {
			conn.writeMessage(req, false);
			sendPacketNum++;
			this.sendPacketTotalSize += req.getLength();
			if (logger.isInfoEnabled()) {
				logger.info("[send] [SUCC] [" + req.getTypeDescription() + "] [len:" + req.getLength() + "] [cost:" + (System.currentTimeMillis() - now) + "]");
			}
		} catch (SendBufferFullException e) {
			try {
				long ll = System.currentTimeMillis();
				synchronized (conn) {
					conn.wait(50);
				}
				if (System.currentTimeMillis() - ll < 50) {
					sendMessage(conn, req);
				} else {
					logger.warn("[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (System.currentTimeMillis() - now) + "] ", e);
				}
			} catch (InterruptedException e1) {
				logger.warn("[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (System.currentTimeMillis() - now) + "] ", e);
			}
		} catch (Exception e) {
			logger.warn("[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (System.currentTimeMillis() - now) + "] ", e);
		}
	}

	public void sendMessage(Connection conn, RequestMessage req) throws Exception {
		long now = System.currentTimeMillis();
		if (conn.getState() != Connection.CONN_STATE_CLOSE && conn.getState() != Connection.CONN_STATE_UNKNOWN) {
			try {
				conn = conn.getConnectionSelector().takeoutConnection(new SelectorPolicy.ConnectionSelectorPolicy(conn), takeOutTimeout);
			} catch (IOException e) {
				logger.warn("[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (System.currentTimeMillis() - now) + "] ", e);
				throw new Exception("takeoutConnection error cause " + e.getMessage());
			}
			try {
				conn.writeMessage(req, false);
				sendPacketNum++;
				this.sendPacketTotalSize += req.getLength();
				if (logger.isInfoEnabled()) {
					logger.info("[send] [SUCC] [" + req.getTypeDescription() + "] [len:" + req.getLength() + "] [cost:" + (System.currentTimeMillis() - now)
							+ "]");
				}
			} catch (SendBufferFullException e) {
				try {
					long ll = System.currentTimeMillis();
					synchronized (conn) {
						conn.wait(50);
					}
					if (System.currentTimeMillis() - ll < 50) {
						sendMessage(conn, req);
					} else {
						logger.warn("[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (System.currentTimeMillis() - now) + "] ", e);
					}
				} catch (InterruptedException e1) {
					logger.warn("[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (System.currentTimeMillis() - now) + "] ", e);
				}
			} catch (Exception e) {
				logger.warn("[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (System.currentTimeMillis() - now) + "] ", e);
			}
		} else {
			logger.warn("[send] [FAIL] [" + req.getTypeDescription() + "] [cost:" + (System.currentTimeMillis() - now) + "]");
			throw new Exception("takeoutConnection error cause connection is null.");
		}
	}

	public RequestMessage waitingTimeout(Connection arg0, long arg1) throws ConnectionException {
		return null;
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
				long elaped = System.currentTimeMillis() - lastChecktime;
				receiveSpeed = new Float(receivedPacket) * 1000 / elaped;
				sendSpeed = new Float(sendPacket) * 1000 / elaped;
				lastReceivePacketNum = receivePacketNum;
				lastSendPacketNum = sendPacketNum;
				lastChecktime = System.currentTimeMillis();
				logger.info("[check] [receiveSpeed:" + NumberUtils.cutFloat(receiveSpeed, 1) + "] [sendSpeed:" + NumberUtils.cutFloat(sendSpeed, 1)
						+ "] [totalReceive:" + receivePacketNum + "] [totalSend:" + sendPacketNum + "]");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void connected(Connection conn) throws IOException {
		// TODO Auto-generated method stub
		logger.info("[Connection connected] [" + conn.getName() + "]");
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

	public void ternimate(Connection conn, List<RequestMessage> noResponseMessages, ByteBuffer receiveBuffer) {
		// TODO Auto-generated method stub
		synchronized (this) {
			connectionNum--;
		}
	}

	public void created(Connection conn, String attachment) throws IOException {
		// TODO Auto-generated method stub
		logger.info("[Connection Created]");
		conn.setMessageFactory(mf);
		conn.setMessageHandler(this);
	}

	public static void main(String args[]) {
		String host = "";
		int port = 4321;
		if (args.length > 1) {
			host = args[0];
			port = Integer.parseInt(args[1]);
		}
		DefaultConnectionSelector selector = new com.xuanzhi.tools.transport.DefaultConnectionSelector();
		selector.setEnableHeapForTimeout(true);
		selector.setMultiServerModel(false);
		selector.setName("CMDConnector");
		selector.setClientModel(false);
		selector.setPort(port);
		selector.setHost(host);
		CMDConnector conn = new CMDConnector(selector);
		selector.setConnectionConnectedHandler(conn);
		try {
			selector.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("CMDConnector initialized and listenning on [" + selector.getHost() + "] [" + selector.getPort() + "]");
		logger.info("CMDConnector initialized and listenning on [" + selector.getHost() + "] [" + selector.getPort() + "]");
	}

	public class StreamReader extends Thread {

		private InputStream stream;

		public StreamReader(InputStream stream) {
			this.stream = stream;
		}

		public void run() {
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line = null;
			try {
				while ((line = br.readLine()) != null) {
					logger.info(line);
				}
				br.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public class ShutdownHookThread extends Thread
	{
		private String message = null;
		
		public ShutdownHookThread(String message)
		{
			this.message = message;
		}
		
		public ShutdownHookThread()
		{
			super();
			this.message = "正在退出应用..."; 
		}
		
		@Override
		public void run() {
			logger.warn("[正在退出应用] ["+message+"]");
		}
	}
	
}

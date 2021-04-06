package com.xuanzhi.tools.transport2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.Message;
import com.xuanzhi.tools.transport.MessageFactory;

import java.util.concurrent.locks.ReentrantLock;
/**
 * 
 * 代表着一个与服务器链接的SocketChannel，此Socket在建立链接后，
 * 采用non-blocking来传输数据。
 * 
 * 此实现也可以用作DatagramChannel，但是DatagramChannel必须是conected的。
 * 
 * 此实现用于在一个已经链接上的Channel上传输数据，接收数据。
 * 并进行丢包的检查。链路的检查等。
 * 
 *
 */
public class Connection2 {

	protected static Logger logger = LoggerFactory.getLogger(Connection2.class);
	
	/**
	 * 连接的各个状态，每个状态根据不同的条件可以变到另外一个状态，
	 * 请参加WIKI中的状态变迁图。
	 */
	public static final int CONN_STATE_UNKNOWN = 0;
	public static final int CONN_STATE_OPEN = 101;
	public static final int CONN_STATE_CONNECT = 201;
	public static final int CONN_STATE_CLOSE = 901;
	
	/**
	 * 链接的状态
	 */
	protected int state = CONN_STATE_UNKNOWN;
	
	protected ByteChannel channel = null;
	
	/**
	 * 获得当前链接对应的网络通道
	 * @return
	 */
	public ByteChannel getChannel(){
		return channel;
	}
	
	/**
	 * 获得当前链接的状态
	 * @return
	 */
	public int getState(){
		return state;
	}
	
	/**
	 * 发送RequestMessage或者发送ResponseMessage时，先将消息体写到这个Buffer中，
	 * 然后再将这个Buffer中的数据发送到网络上。
	 * 
	 * 所以消息体的大小不能超过Buffer的大小
	 * 
	 * 由于我们采用的是non-blocking模式，所以发送Buffer时，
	 * 可能会没有将所有的数据发送取出，此时发送的线程仍然立即返回。
	 * 但是SocketChannel会对OP_WRITE感兴趣，当网络可写的时候，
	 * Selector会收到通知，并启用新的线程调用channelReadyToWriteData方法。
	 * 
	 * 当发送RequestMessage时，sendDataBuffer没有足够的空间来放消息，
	 * 就阻塞当前线程，直到有空间放RequestMessage或者链接关闭。
	 * 
	 * 当发送ResponseMessage时，如果sendDataBuffer没有足够的空间来放消息体，
	 * 那么这个ResponseMessage将会被丢弃。
	 * 
	 * 由于一般情况下，RequestMessage包含的字节数都大于ResponseMessage字节数，
	 * 所以当receiveDataBuffer中同时收到n条RequestMessage，对应的n条ResponseMessage，
	 * 只要sendDataBuffer和receiveDataBuffer一样大，就不会出现有ResponseMessage
	 * 被丢弃的情况。
	 * 
	 * 正常情况下，建议sendDataBuffer的大小为64K.
	 * 
	 * 
	 */
	protected ByteBuffer sendDataBuffer = null;
	
	/**
	 * 设置发送缓冲区的大小，此方法只有在CONN_STATE_UNKNOWN或者CONN_STATE_OPEN状态下
	 * 调用才其作用，如果缓冲区中已经开始传输数据，则此方法效用直接返回
	 * @param size
	 */
	public void setSendBufferSize(int size){
		if(state == CONN_STATE_UNKNOWN || state == CONN_STATE_OPEN){
			if(sendDataBuffer == null || sendDataBuffer.capacity() != size)
				if(size >= 512 * 1024){
					sendDataBuffer = ByteBuffer.allocate(size);
				}else{
					sendDataBuffer = ByteBuffer.allocateDirect(size);
				}
		}
	}
	
	/**
	 * 接收RequestMessage或者接收ResponseMessage时，先将网络中的数据读取到这个Buffer中，
	 * 然后解析Buffer中的数据。
	 * 
	 * 所以消息体的大小不能超过Buffer的大小
	 * 
	 * 解析Buffer时，可能会出现Buffer中含有多个RequestMessage和多个ResponseMessage，
	 * 并且，还有可能最后一个Message的部分数据还没有接收到。
	 * 
	 * 目前的策略是：
	 * 1. 先解析出Buffer中所有的消息，放入一个List中。
	 * 1. 然后处理List中的ResponseMessage，不改变链接的状态，只需要调整滑动窗口
	 * 2. 然后处理List中的RequestMessage，产生多个ResponseMessage准备发送，同时改变链接的状态到CONN_STATE_WRITE_REPLY
	 * 3. 在CONN_STATE_WRITE_REPLY发送完多个ResponseMessage后，
	 *    判断是否需要继续等待没有完全到达的数据，还是继续等待ResponseMessage，还是等待RequestMessage
	 * 
	 * 有一种情况需要注意：当Buffer中同时收到多个RequestMessage，我们现在的处理
	 * 是同时向网络发送多个ResponseMessage。但是如sendDataBuffer的说明，我们
	 * 是将ResponseMessage先写到sendDataBuffer中，然后再发送到网络上。
	 * 如果sendDataBuffer不够大，导致不能将所有的ResponseMessage写入到sendDataBuffer中，
	 * 那么那些没有写入到sendDataBuffer中的ResponseMessage，将丢失。
	 */
	protected ByteBuffer receiveDataBuffer = null;
	
	/**
	 * 设置接收缓冲区的大小，此方法只有在CONN_STATE_UNKNOWN或者CONN_STATE_OPEN状态下
	 * 调用才其作用，如果缓冲区中已经开始接收数据，则此方法效用直接返回
	 * @param size
	 */
	public void setReceiveBufferSize(int size){
		if(state == CONN_STATE_UNKNOWN || state == CONN_STATE_OPEN){
			if(receiveDataBuffer == null || receiveDataBuffer.capacity() != size)
				if(size >= 512 * 1024){
					receiveDataBuffer = ByteBuffer.allocate(size);
				}else{
					receiveDataBuffer = ByteBuffer.allocateDirect(size);
				}
				
		}
	}
	
	//发送消息用的同一锁
	protected ReentrantLock sendLock = new ReentrantLock();
	
	/**
	 * 
	 * 滑动窗口中的等待ResponseMessage的RequestMessage
	 * 
	 * @author myzdf
	 *
	 */
	private static class WaitingSendMessage{
		
		Message m;
		//等待ResponseMessage的超时时间
		long createTime = 0L;
		
		public WaitingSendMessage(Message m){
			this.m = m;
			this.createTime = System.currentTimeMillis();
		}
	}
	
	//等待发送的消息，当此队列中有消息时，后续的消息必须放入此队列的尾部。
	//或者，当发送缓冲区无法放下此消息，且此消息大小小于发送缓冲区时，也放入此队列尾部
	//并且会发送通知消息给 Selector，通知此链接上，队列中有消息等待发送
	protected LinkedList<WaitingSendMessage> waitingQueue = new LinkedList<WaitingSendMessage>();
	

	/**
	 * 获取正在等待队列中发送的消息数目
	 * @return
	 */
	public int getWaitingQueueSize(){
		return waitingQueue.size();
	}
	
	/**
	 * 获取正在等待队列中发送的消息在内存的大小
	 * @return
	 */
	public long getWaitingQueueMessageInBytes(){
		if(waitingQueue.size() == 0) return 0;
		long bytes = 0;
		sendLock.lock();
		try{
			Iterator<WaitingSendMessage> it = waitingQueue.iterator();
			while(it.hasNext()){
				WaitingSendMessage wm = it.next();
				bytes += wm.m.getLength();
			}
		}finally{
			sendLock.unlock();
		}
		return bytes;
	}
	
	/**
	 * 等待RequestMessage超时时间，超时后，会发出响应的超时事件，默认为3分钟
	 */
	protected long waitingRequestMessageTimeout = 3 * 60 * 1000L;
	
	public void setWaitingRequestMessageTimeout(long timeout){
		waitingRequestMessageTimeout = timeout;
	}
	
	public long getWaitingRequestMessageTimeout(){
		return waitingRequestMessageTimeout;
	}
	
	/**
	 * 这个链接上最多多长时间的间隔可以没有数据到达，
	 * 超过这个时间，就断开此链接。
	 * 
	 * 默认这个时间为6分钟，如果6分钟内，链接上没有任何数据到达，则认为这个链接已经失效，
	 * 会强制断开此链接。
	 * 
	 * 链接断开时，如果滑动窗口中仍然有消息等待回应，那么这些消息将被放置到一个统一的发送
	 * 队列中，有另外的线程来重新发送。
	 */
	protected long connectionMaxIdleTimeout = 10 * 60 * 1000L;
	
	public void setConnectionMaxIdleTimeout(long timeout){
		connectionMaxIdleTimeout = timeout;
	}
	/**
	 * 记录最后一次收到数据的时间，此时间 + connectionReadTimeout就是最久可以接收不到数据的时间期限，
	 * 超过这个期限，链接将被强制关闭。
	 */
	protected long lastReceiveDataTime = System.currentTimeMillis();

	protected long lastSendDataTime = System.currentTimeMillis();
	
	protected long lastCheckTimeout = System.currentTimeMillis(); 
	
	/**
	 * 消息长度用几个字节来标识。我们认为消息是这样的：
	 * |----|------------------------------------------------------|
	 *  长度                  消息内容  
	 */
	protected int numOfByteForMessageLength = 4;
	
	/**
	 * 为了处理特殊的情况，有实际的情况，消息的长度数据不是放在包的开始，而是从某个位置开始。
	 * 如果是这种情况，这个值需要设置为非0值。
	 * 消息长度的数据，是从offsetOfByteForMessageLength到numOfByteForMessageLength之间的字节组成
	 */
	protected int offsetOfByteForMessageLength = 0;
	
	/**
	 * 消息的长度是否包含消息长度本身的数据。
	 * 分为两种情况，一种是消息的长度，包括所有的数据，及包含numOfByteForMessageLength指定的数据
	 * 另外一种情况，消息的长度只是数据，不包含numOfByteForMessageLength指定的数据
	 */
	protected boolean packetLengthContainsLengSelf = true;
	
	
	/**
	 * 消息的工厂类，用于收到数据后，构造Message
	 */
	protected MessageFactory factory = null;
	
	/**
	 * 消息处理器，服务器模式下一定要设置此变量，客户端模式下可以不设置，用MessageCallback接口了处理响应
	 */
	protected MessageHandler2 handler = null;
	
	protected ConnectionTerminateHandler2 terminateHandler = null;
	
	protected DefaultConnectionSelector2 selector = null;
	


	/**
	 * Connection的描述信息，包括名字，发送了多少个消息，接收了多少个消息
	 */
	protected String name ;
	protected String remoteAddress;
	protected int sendMessageNum = 0;
	protected int receiveMessageNum = 0;
	protected long sendMessagePacketSize = 0;
	protected long receiveMessagePacketSize = 0;
	
	protected Object attachment = null;
	
	protected Object attachment2 = null;
	
	protected HashMap<Object,Object> attachementMap = null;
	
	//最后处理的消息，为的是打印日志，表明如果连接被强制中断，是在处理那个消息时中断的。
	//方便更快的定位问题
	protected transient Message lastHandledMessage = null;
	protected transient long lastBeginHandleMessageTime = 0;
	
	protected transient int selector_operation = 0;
	
	//是否要打印此链接上的日志
	protected boolean printLogFlag = false;
	//统计协议的数量和大小
	//protected boolean statProtocolFlag = false;
	//type --> [数量，大小]
	protected HashMap<String,long[]> sendMessageStatMap = null;
	//type --> [数量，大小]
	protected HashMap<String,long[]> receiveMessageStatMap = null;
	
	//统计发送或者接受
	protected void statMessage(boolean isSendMessage,String typeDescription,long mLength){
		if(isSendMessage){
			if(sendMessageStatMap == null){
				sendMessageStatMap = new HashMap<String,long[]>();
			}
			long data[] = sendMessageStatMap.get(typeDescription);
			if(data == null){
				data = new long[2];
				sendMessageStatMap.put(typeDescription, data);
			}
			data[0]++;
			data[1] += mLength;
		}else{
			if(receiveMessageStatMap == null){
				receiveMessageStatMap = new HashMap<String,long[]>();
			}
			long data[] = receiveMessageStatMap.get(typeDescription);
			if(data == null){
				data = new long[2];
				receiveMessageStatMap.put(typeDescription, data);
			}
			data[0]++;
			data[1] += mLength;
		}
	}
	
	public String getName(){
		return name;
	}
	
	protected String identity;

	public String getRemoteAddress(){
		return remoteAddress;
	}	
	/**
	 * 链接的标识，此标识可以用于在链接集合中选择特定了链接
	 * @return
	 */
	public String getIdentity(){
		return identity;
	}
	
	public void setIdentity(String id){
		identity = id;
	}

	/**
         * 附件，任何对象都可以，以方便程序设计
         */
	public Object getAttachment(){
		return attachment;
	}

	public void setAttachment(Object attach){
		attachment = attach;
	}
	
	/**
         * 附件，任何对象都可以，以方便程序设计
         */
	public Object getAttachment2(){
		return attachment2;
	}

	public void setAttachment2(Object attach){
		attachment2 = attach;
	}
	
	/**
	 * 附件
	 * @param key
	 * @param value
	 */
	public Object getAttachmentData(Object key){
		if(attachementMap == null) return null;
		return attachementMap.get(key);
	}
	
	/**
	 * 附件
	 * @param key
	 * @param value
	 */
	public void setAttachmentData(Object key,Object value){
		if(attachementMap == null){
			attachementMap = new HashMap<Object,Object>();
			
		}
		if(value == null){
			attachementMap.remove(key);
		}else{
			attachementMap.put(key, value);
		}
		
	}
	
	public DefaultConnectionSelector2 getConnectionSelector(){
		return this.selector;
	}
	
	/**
	 * 构造函数，所有参数的帮助，请参加对应的属性
	 * 
	 * @param selector
	 * @param channel
	 * @param factory
	 * @param numOfByteForMessageLength
	 * @param handler
	 */
	protected Connection2(DefaultConnectionSelector2 selector,ByteChannel channel,MessageFactory factory,MessageHandler2 handler){
		this(selector,channel,factory,handler,64 * 1024,64 * 1024,3 * 60 * 1000L,6 * 60 * 1000L);
	}
	
	/**
	 * 构造函数，所有参数的帮助，请参加对应的属性
	 * 
	 * @param selector
	 * @param channel
	 * @param factory
	 * @param numOfByteForMessageLength
	 * @param handler
	 * @param sendBufferSize
	 * @param receiveBufferSize
	 * @param maxWindowSize
	 * @param maxReSendReplyTimes
	 * @param waitingResponseMessageTimeout
	 * @param waitingRequestMessageTimeout
	 * @param waitingMoreSocketSendBufferTimeout
	 * @param connectionReadTimeout
	 */
	protected Connection2(DefaultConnectionSelector2 selector,ByteChannel channel,
			MessageFactory factory,MessageHandler2 handler,int sendBufferSize,int receiveBufferSize,
			long waitingRequestMessageTimeout,long connectionMaxIdleTimeout){
		this.selector = selector;
		this.channel = channel;
		this.factory = factory;
		this.handler = handler;
		sendDataBuffer = ByteBuffer.allocateDirect(sendBufferSize);
		receiveDataBuffer = ByteBuffer.allocateDirect(receiveBufferSize);
		this.waitingRequestMessageTimeout = waitingRequestMessageTimeout;
		this.connectionMaxIdleTimeout = connectionMaxIdleTimeout;
	}
	
	/**
	 * 设置ConnectionTerminateHandler，
	 * 当链接被关闭时，如果滑动窗口中有消息或者receiveDataBuffer有数据，会启动新的线程调用接口中的方法。
	 * 否则不会启动新的线程，来调用接口中的方法
	 * @param handler
	 */
	public void setConnectionTerminateHandler(ConnectionTerminateHandler2 handler){
		terminateHandler = handler;
	}
	
	/**
	 * 消息的工厂类，用于收到数据后，构造Message
	 * @param factory
	 */
	public void setMessageFactory(MessageFactory factory){
		this.factory = factory;
		this.numOfByteForMessageLength = factory.getNumOfByteForMessageLength();
	}
	
	/**
	 * 设置消息处理类，当收到RequestMessage，ResponseMessage，或者超时都调用接口中的方法
	 * @param handler
	 */
	public void setMessageHandler(MessageHandler2 handler){
		this.handler = handler;
	}
	
	/**
	 * <pre>
	 * 通过此链接，发送一个消息给链接的另一端。
	 * 
	 * 返回值为true，表示此消息全部数据都已经写入到网络层（发送出去了）。
	 * 返回值为false，表示此消息没有写入网络层，可能在发送队列中，也可能写入到了发送缓冲区中，也可能部分数据已经发送到网络层。
	 * 
	 * 
	 * 此方法为即时方法，不会出现阻塞现象。
	 * 
	 * 如果：
	 * 	1. 链接不是处于CONNECT的状态，那么会抛出IllegalStateException
	 *  2. message包的大小太大，超过了发送缓冲区的大小，会抛出IllegalArgumentException
	 *  3. 在将数据写入网络底层时，如果发送IO错误，会抛出IOException，并改变链接的状态
	 * 
	 * 发送一条消息的基本逻辑是：
	 *  1. 判断链接的状态
	 *  2. 判断数据包的大小是否合法
	 *  3. 如果【等待发送队列】中有消息在等待发送，那么将此消息加入到【等待发送队列】尾部，并发送通知消息给Selector,并返回false
	 *  4. 如果【等待发送队列】中无消息在等待发送，查看【发送缓冲区】中剩余的空间是否够放下此消息，如果不够，那么将此消息加入到【等待发送队列】尾部，并发送通知消息给Selector,并返回false
	 *  5. 如果发送缓冲区可以放下此消息，将此消息写入到发送缓冲区。
	 *  6. 调用底层写方法，将发送缓冲区的数据写入网络层。如果过程中出现IO异常，抛出异常。
	 *  7. 如果没有出现异常，检查缓冲区中的数据是否全部写出，如果没有，并发送通知消息给Selector。
	 *  8. 检查此消息在这次写入网络层操作中，是否全部写入网络层。如果是返回true，否则返回false
	 *  
	 *  </pre>
	 * @param message 要发送的消息对象，不能为null
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * 
	 * 
	 * 
	 */
	public boolean sendMessage(Message message)throws IllegalStateException,IllegalArgumentException,IOException{
		return sendMessage(message,true);
	}
	

	/**
	 * <pre>
	 * 通过此链接，发送一个消息给链接的另一端。
	 * 
	 * 返回值为true，表示此消息全部数据都已经写入到网络层（发送出去了）。
	 * 返回值为false，表示此消息没有写入网络层，可能在发送队列中，也可能写入到了发送缓冲区中，也可能部分数据已经发送到网络层。
	 * 
	 * 当writeToSocketImmediately设置为false时，一定返回false。
	 * 
	 * 此方法为即时方法，不会出现阻塞现象。
	 * 如果writeToSocketImmediately设置为true，存在非阻塞IO的写操作。开发时需要额外注意这一点。
	 * 如果writeToSocketImmediately设置为false，此方法立即返回。并由系统线程来将数据发送出去。
	 * 
	 * 如果：
	 * 	1. 链接不是处于CONNECT的状态，那么会抛出IllegalStateException
	 *  2. message包的大小太大，超过了发送缓冲区的大小，会抛出IllegalArgumentException
	 *  3. 在将数据写入网络底层时，如果发送IO错误，会抛出IOException，并改变链接的状态
	 * 
	 * 发送一条消息的基本逻辑是：
	 *  1. 判断链接的状态
	 *  2. 判断数据包的大小是否合法
	 *  3. 如果【等待发送队列】中有消息在等待发送，那么将此消息加入到【等待发送队列】尾部，并发送通知消息给Selector,并返回false
	 *  4. 如果【等待发送队列】中无消息在等待发送，查看【发送缓冲区】中剩余的空间是否够放下此消息，如果不够，那么将此消息加入到【等待发送队列】尾部，并发送通知消息给Selector,并返回false
	 *  5. 如果发送缓冲区可以放下此消息，将此消息写入到发送缓冲区。
	 *  6. 调用底层写方法，将发送缓冲区的数据写入网络层。如果过程中出现IO异常，抛出异常。
	 *  7. 如果没有出现异常，检查缓冲区中的数据是否全部写出，如果没有，并发送通知消息给Selector。
	 *  8. 检查此消息在这次写入网络层操作中，是否全部写入网络层。如果是返回true，否则返回false
	 *  </pre>
	 * @param message 要发送的消息对象，不能为null
	 * @param writeToSocketImmediately 是否要将数据立即写入到网络层。如果为false，此消息将加入到发送队列，此方法立即返回，消息将有系统的发送线程稍后来发送到网络层。
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * 
	 * 
	 * 
	 */
	public boolean sendMessage(Message message,boolean writeToSocketImmediately)throws IllegalStateException,IllegalArgumentException,IOException{
		int mLength = 0;
		try{
			mLength = message.getLength();
		}catch(Exception e){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [send_message] [failed] [get_message_length_exception] ["+getStateString(state)+"] ["+this.remoteAddress+"] ["+message.getTypeDescription()+"] ["+message.getSequenceNumAsString()+"] [length:"+mLength+"] [queue:"+this.waitingQueue.size()+"]",e);
			throw new IllegalArgumentException("get message length error",e);
		}
		
		if(this.state != Connection2.CONN_STATE_CONNECT){
			logger.warn("[{},{},{}] [send_message] [failed] [connection_not_connected] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
			throw new IllegalStateException("connection not connected.");
		}
		
		if(mLength > this.sendDataBuffer.capacity()){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [send_message] [failed] [message_too_big_then_sendbuffer] ["+getStateString(state)+"] ["+this.remoteAddress+"] ["+message.getTypeDescription()+"] ["+message.getSequenceNumAsString()+"] [length:"+mLength+"] [queue:"+this.waitingQueue.size()+"]");
			throw new IllegalArgumentException("message too big");
		}
		
		boolean heldByCurrentThread =  sendLock.isHeldByCurrentThread();
		if(heldByCurrentThread == false){
			sendLock.lock();
		}
		try{
			if(this.waitingQueue.size() > 0){ //【等待发送队列】中有消息在等待发送
				waitingQueue.add(new WaitingSendMessage(message));
				selector.notify(this, DefaultConnectionSelector2.NOTIFY_TYPE_WAITTINGQUEUE);
				if(logger.isDebugEnabled() || printLogFlag){
					if(printLogFlag)
						logger.warn("[{},{},{}] [send_message] [success] [add_message_to_waitingqueue] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
					else
						logger.debug("[{},{},{}] [send_message] [success] [add_message_to_waitingqueue] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
				}
				return false;
			}else{ //【等待发送队列】中无消息在等待发送
				if(mLength > this.sendDataBuffer.remaining()){ //【发送缓冲区】中剩余的空间不够放下此消息
					waitingQueue.add(new WaitingSendMessage(message));
					selector.notify(this, DefaultConnectionSelector2.NOTIFY_TYPE_WAITTINGQUEUE);
					if(logger.isDebugEnabled() || printLogFlag){
						if(printLogFlag)
							logger.warn("[{},{},{}] [send_message] [success] [add_message_to_waitingqueue] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
						else
							logger.debug("[{},{},{}] [send_message] [success] [add_message_to_waitingqueue] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
					}
					return false;
				}else{ //【发送缓冲区】中剩余的空间够放下此消息
					//写入缓冲区
					int n = 0;
					try{
						n = message.writeTo(sendDataBuffer);
					}catch(Throwable e){
						logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [send_message] [failed] [write_message_to_buffer_exception] ["+getStateString(state)+"] ["+this.remoteAddress+"] ["+message.getTypeDescription()+"] ["+message.getSequenceNumAsString()+"] [length:"+mLength+"] [queue:"+this.waitingQueue.size()+"]",e);
						throw new IllegalArgumentException("write message to buffer error",e);
					}
					if(n == 0){ //消息无法写入【发送缓冲区】
						logger.warn("[{},{},{}] [send_message] [failed] [message_really_size_not_equals_getLength] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
						throw new IllegalArgumentException("message really size not equals getLength()");
					}else{
						this.sendMessageNum++;
						this.sendMessagePacketSize += mLength;
						
						if(selector.statProtocolFlag){
							statMessage(true,message.getTypeDescription(),mLength);
						}
						
						//准备写入网络层
						if(writeToSocketImmediately == false){
							selector.notify(this, DefaultConnectionSelector2.NOTIFY_TYPE_SENDBUFFER);
							if(logger.isDebugEnabled() || printLogFlag){
								if(printLogFlag)
									logger.warn("[{},{},{}] [send_message] [success] [write_message_to_sendbuffer] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
								else
									logger.debug("[{},{},{}] [send_message] [success] [write_message_to_sendbuffer] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
							}
							return false;
						}else{
							int position = sendDataBuffer.position();
							
							sendDataBuffer.flip();
							try{
								n = channel.write(sendDataBuffer);
								//没有写完的数据移到buffer的最前端
								sendDataBuffer.compact();
								
								if(n > 0){
									this.lastSendDataTime = System.currentTimeMillis();
								}
								
								if(sendDataBuffer.position() == 0){ //全部数据写出到网络层
									if(logger.isDebugEnabled() || printLogFlag){
										if(printLogFlag)
											logger.warn("[{},{},{}] [send_message] [success] [write_all_data_to_socket] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
										else
											logger.debug("[{},{},{}] [send_message] [success] [write_all_data_to_socket] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
									}
									return true;
								}else{ //有数据没有及时写出
									selector.notify(this, DefaultConnectionSelector2.NOTIFY_TYPE_SENDBUFFER);
									if(logger.isDebugEnabled() || printLogFlag){
										if(printLogFlag)
											logger.warn("[{},{},{}] [send_message] [success] [write_partially_data_to_socket] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
										else
											logger.debug("[{},{},{}] [send_message] [success] [write_partially_data_to_socket] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
									}
									return (n>=position);
								}
							}catch(IOException e){
								logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [send_message] [failed] [io_exception_and_close_connection] ["+getStateString(state)+"] ["+this.remoteAddress+"] ["+message.getTypeDescription()+"] ["+message.getSequenceNumAsString()+"] [length:"+mLength+"] [queue:"+this.waitingQueue.size()+"]",e);
								terminate();
								throw e;
							}
							
						}//需要写入到网络层
					}//消息写入【发送缓冲区】
				}//【发送缓冲区】中剩余的空间够放下此消息
			}//【等待发送队列】中无消息在等待发送
		}finally{
			if(heldByCurrentThread == false){
				sendLock.unlock();
			}
		}
	}
	
	/**
	 * 将【等待发送队列】中的消息，尽量写入到【发送缓冲区】中，
	 * 返回写入消息的数量
	 * 
	 * 此方法由内部线程调用
	 * @return
	 */
	protected int writeWaittingMessageToBuffer(){
		if(this.state != Connection2.CONN_STATE_CONNECT){
			logger.warn("[{},{},{}] [writeWaittingMessageToBuffer] [failed] [connection_not_connected] [{}] [{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress});
			return 0;
		}
		if(this.waitingQueue.size() == 0) return 0;
		
		sendLock.lock();
		try{
			if(this.waitingQueue.size() == 0) return 0;
			int writeMessageNum = 0;
			while(waitingQueue.size() > 0){
				WaitingSendMessage wm = waitingQueue.getFirst();
				Message message = wm.m;
				int mLength = 0;
				try{
					mLength = message.getLength();
				}catch(Exception e){
					waitingQueue.removeFirst();
					logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [writeWaittingMessageToBuffer] [failed] [get_message_length_exception_and_discard_message] ["+getStateString(state)+"] ["+this.remoteAddress+"] ["+message.getTypeDescription()+"] ["+message.getSequenceNumAsString()+"] [length:"+mLength+"] [queue:"+this.waitingQueue.size()+"]",e);
					continue;
				}
				
				if(mLength > this.sendDataBuffer.remaining()){ //【发送缓冲区】中剩余的空间不够放下此消息
					if(logger.isDebugEnabled() || printLogFlag){
						if(printLogFlag)
							logger.warn("[{},{},{}] [writeWaittingMessageToBuffer] [failed] [length_big_then_buffer_remaining] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
						else
							logger.debug("[{},{},{}] [writeWaittingMessageToBuffer] [failed] [length_big_then_buffer_remaining] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
					}
					break;
				}else{ //【发送缓冲区】中剩余的空间够放下此消息
					//写入缓冲区
					int n = 0;
					try{
						n = message.writeTo(sendDataBuffer);
					}catch(Throwable e){
						waitingQueue.removeFirst();
						logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [writeWaittingMessageToBuffer] [failed] [write_message_to_buffer_exception] ["+getStateString(state)+"] ["+this.remoteAddress+"] ["+message.getTypeDescription()+"] ["+message.getSequenceNumAsString()+"] [length:"+mLength+"] [queue:"+this.waitingQueue.size()+"]",e);
						continue;
					}
					if(n == 0){ //消息无法写入【发送缓冲区】
						waitingQueue.removeFirst();
						logger.warn("[{},{},{}] [writeWaittingMessageToBuffer] [failed] [message_really_size_not_equals_getLength] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
						continue;
					}else{
						this.sendMessageNum++;
						this.sendMessagePacketSize += mLength;

						if(selector.statProtocolFlag){
							statMessage(true,message.getTypeDescription(),mLength);
						}
						
						writeMessageNum++;
						waitingQueue.removeFirst();
						
						if(logger.isDebugEnabled() || printLogFlag){
							if(printLogFlag)
								logger.warn("[{},{},{}] [writeWaittingMessageToBuffer] [success] [write_message_to_sendbuffer] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
							else
								logger.debug("[{},{},{}] [writeWaittingMessageToBuffer] [success] [write_message_to_sendbuffer] [{}] [{}] [{}] [sid:{}] [length:{}] [queue:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString(),mLength,waitingQueue.size()});
						}
					}//消息写入【发送缓冲区】
				}//【发送缓冲区】中剩余的空间够放下此消息
			}
			return writeMessageNum;
		}finally{
			sendLock.unlock();
		}
	}
	
	protected int writeSendBufferToNetwork(){
		if(this.state != Connection2.CONN_STATE_CONNECT){
			logger.warn("[{},{},{}] [writeSendBufferToNetwork] [failed] [connection_not_connected] [{}] [{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress});
			return 0;
		}
		if(sendDataBuffer.position() == 0) return 0;
		
		sendLock.lock();
		try{
			if(state == Connection2.CONN_STATE_CLOSE){
				logger.warn("[{},{},{}] [writeSendBufferToNetwork] [failed] [connection_has_closed] [{}] [{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress});
				return 0;
			}
			sendDataBuffer.flip();
			try{
				int n = channel.write(sendDataBuffer);
								//没有写完的数据移到buffer的最前端
				sendDataBuffer.compact();
				
				if(n > 0){
					this.lastSendDataTime = System.currentTimeMillis();
				}
				
				if(sendDataBuffer.position() == 0){ //全部数据写出到网络层
					if(logger.isDebugEnabled() || printLogFlag){
						if(printLogFlag)
							logger.warn("[{},{},{}] [writeSendBufferToNetwork] [success] [write_all_data_to_socket] [{}] [{}] [n={}] [position:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,n,sendDataBuffer.position()});
						else
							logger.debug("[{},{},{}] [writeSendBufferToNetwork] [success] [write_all_data_to_socket] [{}] [{}] [n={}] [position:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,n,sendDataBuffer.position()});
					}
					return n;
				}else{ //有数据没有及时写出
					logger.warn("[{},{},{}] [writeSendBufferToNetwork] [success] [write_partially_data_to_socket] [{}] [{}] [n={}] [position:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,n,sendDataBuffer.position()});
					return n;				
				}
			}catch(IOException e){
				logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [writeSendBufferToNetwork] [failed] [io_exception_and_close_connection] ["+getStateString(state)+"] ["+this.remoteAddress+"]",e);
				terminate();
				return 0;
			}
		}finally{
			sendLock.unlock();
		}
	}
	/**
	 * <pre>
	 * 通过此链接，一次发送多个消息给链接的另一端。
	 * 
	 * 此方法为即时方法，不会出现阻塞现象。
	 * 如果writeToSocketImmediately设置为true，存在非阻塞IO的写操作。开发时需要额外注意这一点。
	 * 如果writeToSocketImmediately设置为false，此方法立即返回。并由系统线程来将数据发送出去。
	 * 
	 * 如果：
	 * 	1. 链接不是处于CONNECT的状态，那么会抛出IllegalStateException
	 *  2. 存在某个message包的大小太大，超过了发送缓冲区的大小，会抛出IllegalArgumentException
	 *  3. 在将数据写入网络底层时，如果发送IO错误，会抛出IOException，并改变链接的状态
	 * 
	 * 发送一条消息的基本逻辑是：
	 *  1. 判断链接的状态
	 *  2. 判断数据包的大小是否合法
	 *  3. 如果【等待发送队列】中有消息在等待发送，那么将此消息加入到【等待发送队列】尾部，并发送通知消息给Selector,并返回false
	 *  4. 如果【等待发送队列】中无消息在等待发送，查看【发送缓冲区】中剩余的空间是否够放下此消息，如果不够，那么将此消息加入到【等待发送队列】尾部，并发送通知消息给Selector,并返回false
	 *  5. 如果发送缓冲区可以放下此消息，将此消息写入到发送缓冲区。
	 *  6. 调用底层写方法，将发送缓冲区的数据写入网络层。如果过程中出现IO异常，抛出异常。
	 *  7. 如果没有出现异常，检查缓冲区中的数据是否全部写出，如果没有，并发送通知消息给Selector。
	 *  8. 检查此消息在这次写入网络层操作中，是否全部写入网络层。如果是返回true，否则返回false
	 *  </pre>
	 * @param message 要发送的消息对象，不能为null
	 * @param writeToSocketImmediately 是否要将数据立即写入到网络层。如果为false，此消息将加入到发送队列，此方法立即返回，消息将有系统的发送线程稍后来发送到网络层。
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * 
	 * 
	 * 
	 */
	public void sendMessage(Message[] messages,boolean writeToSocketImmediately) throws IllegalStateException,IllegalArgumentException,IOException{
		for(int i = 0 ; i < messages.length ; i++){
			Message message = messages[i];
			int mLength = 0;
			try{
				mLength = message.getLength();
			}catch(Exception e){
				logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [send_message] [failed] [get_message_length_exception] ["+getStateString(state)+"] ["+this.remoteAddress+"] ["+message.getTypeDescription()+"] ["+message.getSequenceNumAsString()+"] [length:"+mLength+"]",e);
				throw new IllegalArgumentException("get message{"+message.getTypeDescription()+"} length error cause exception{"+e+"}");
			}
			
			if(this.state != Connection2.CONN_STATE_CONNECT){
				logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [send_message] [failed] [connection_not_connected] ["+getStateString(state)+"] ["+this.remoteAddress+"] ["+message.getTypeDescription()+"] ["+message.getSequenceNumAsString()+"] [length:"+mLength+"]");
				throw new IllegalStateException("connection not connected.");
			}
			
			if(mLength > this.sendDataBuffer.capacity()){
				logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [send_message] [failed] [message_too_big_then_sendbuffer] ["+getStateString(state)+"] ["+this.remoteAddress+"] ["+message.getTypeDescription()+"] ["+message.getSequenceNumAsString()+"] [length:"+mLength+"]");
				throw new IllegalArgumentException("message too big");
			}
		}
		sendLock.lock();
		try{
			for(int i = 0 ; i < messages.length ; i++){
				sendMessage(messages[i],writeToSocketImmediately);
			}
		}finally{
			sendLock.unlock();
		}
	}
	
	
	/**
	 * 
	 * 外部直接关闭链接
	 * 
	 * 此方法会标记链接的状态为 CONN_STATE_CLOSE，之后就不能再发送任何数据。
	 * 包括此链接上还在等待队列中的消息
	 */
	public void close(String reason){
		logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [preparing_to_close_connection] [reason:"+reason+"] [handlingMessage:"+(lastHandledMessage!=null?lastHandledMessage.getTypeDescription():"null")+"]");
		this.state = CONN_STATE_CLOSE;
		selector.closeConnection(this);
	}
	

	
	
	/**
	 * channel的OP_WRITE被设置，同时Selector检查到此连接，
	 * 启动新的线程调用此方法。调用此方法的时候，channel关心的OP_WRITE和OP_READ都被清空。
	 * 以保证只有一个线程在操作此连接。
	 * 
	 * 此方法标识SocketChannel可以写了。
	 */
	protected void channelReadyToWriteData() throws IllegalStateException,IOException{

		if(logger.isDebugEnabled() || printLogFlag){
			if(printLogFlag)
				logger.warn("[{},{},{}] [selector_wakeup] [channelReadyToWriteData] [{}] [{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress});
			else
				logger.debug("[{},{},{}] [selector_wakeup] [channelReadyToWriteData] [{}] [{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress});
		}

		if(state == Connection2.CONN_STATE_CLOSE){
			logger.warn("[{},{},{}] [selector_wakeup] [channelReadyToWriteData] [connection_has_closed] [{}] [{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress});
		}
		
		sendLock.lock();
		try{
			if(state == Connection2.CONN_STATE_CLOSE){
				logger.warn("[{},{},{}] [selector_wakeup] [channelReadyToWriteData] [connection_has_closed] [{}] [{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress});
			}
			
			int position = sendDataBuffer.position();
			if(position == 0) return;
			sendDataBuffer.flip();
			try{
				int n = channel.write(sendDataBuffer);
				//没有写完的数据移到buffer的最前端
				sendDataBuffer.compact();
				if(n > 0){
					this.lastSendDataTime = System.currentTimeMillis();
				}
				
				if(sendDataBuffer.position() == 0){ //全部数据写出到网络层
					if(logger.isDebugEnabled() || printLogFlag){
						if(printLogFlag)
							logger.warn("[{},{},{}] [selector_wakeup] [channelReadyToWriteData] [write_all_data_to_socket] [{}] [{}] [n={}] [position:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,n,sendDataBuffer.position()});
						else
							logger.debug("[{},{},{}] [selector_wakeup] [channelReadyToWriteData] [write_all_data_to_socket] [{}] [{}] [n={}] [position:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,n,sendDataBuffer.position()});
						
					}
				}else{ //有数据没有及时写出
					selector.notify(this, DefaultConnectionSelector2.NOTIFY_TYPE_SENDBUFFER);
					logger.warn("[{},{},{}] [selector_wakeup] [channelReadyToWriteData] [write_partially_data_to_socket] [{}] [{}] [n={}] [position:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,n,sendDataBuffer.position()});
				}
			}catch(IOException e){
				logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [selector_wakeup] [channelReadyToWriteData] [io_exception_and_close_connection] ["+getStateString(state)+"]",e);
				terminate();
				throw e;
			}
		}finally{
			sendLock.unlock();
		}
	}
	
	
	/**
	 * <pre>
	 * channel的OP_READ被设置，同时Selector检查到此连接，
	 * 启动新的线程调用此方法。调用此方法的时候，channel关心的OP_WRITE和OP_READ都被清空。
	 * 以保证只有一个线程在操作此连接。
	 *
	 * 此方法，先将网络中的数据读取到这个receiveDataBuffer中，然后解析receiveDataBuffer中的数据。
	 * 
	 * 解析receiveDataBuffer时，可能会出现receiveDataBuffer中含有多个消息体和多个响应体，
	 * 并且，还有可能最后一个消息体的部分数据还没有接收到。
	 * 
	 * 目前的策略是：
	 * 1. 先处理receiveDataBuffer中的响应，不改变链接的状态，只需要调整滑动窗口
	 * 2. 然后处理receiveDataBuffer中的消息，将改变链接的状态到CONN_STATE_WRITE_REPLY
	 * 3. 最后处理还没有接收完整的消息或者响应。
	 * 
	 * 有一种情况需要注意：当receiveDataBuffer中同时收到多个消息，我们现在的处理
	 * 是同时向网络发送多个响应。但是如sendDataBuffer的说明，我们
	 * 是将响应先写到sendDataBuffer中，然后再发送到网络上。
	 * 如果sendDataBuffer不够大，导致不能将所有的Reply写入到sendDataBuffer中，
	 * 那么那些没有写入到sendDataBuffer中的Reply，将丢失。
	 * 
	 * </pre>
	 * 
	 */
	protected void channelReadyToReadData() throws IllegalStateException,IOException{
		if(logger.isDebugEnabled() || printLogFlag){
			if(printLogFlag)
				logger.warn("[{},{},{}] [selector_wakeup] [channelReadyToReadData] [{}] [{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress});
			else
				logger.debug("[{},{},{}] [selector_wakeup] [channelReadyToReadData] [{}] [{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress});
		}
		
		if(state == Connection2.CONN_STATE_CLOSE){
			throw new IllegalStateException("current state ["+getStateString(state)+"]");
		}
		
		if(factory == null){
			throw new IllegalStateException("factory is null. please set MessageFactory first.");
		}
		
		
		
		try{
			int n = channel.read(receiveDataBuffer);
			if(n > 0){
				this.lastReceiveDataTime = System.currentTimeMillis();
				lastBeginHandleMessageTime = lastReceiveDataTime;
			}else if(n == -1){ //the channel has reached end-of-stream
				throw new IOException("the channel has reached end-of-stream");
			}else if(receiveDataBuffer.remaining() > 0){ // n == 0 的情况
					//底层标记有数据可以读取，但是读不到数据的情况
					//这种情况按API说明不应该发生，但是实际情况下，发生了，而且还是发生在正常的连接上
					//潜龙游戏封测期间，剑霸这个用户在2010-03-14中午，他所在的连接为connection-5428
					//每次调用select()都立即返回，告知此连接上有数据可以读，但是每次读到的数据长度为0
					//但是当真有数据到达时，确实能读到数据
					//这种情况会导致无停顿的循环，最终导致系统的CS特别的高（每秒高达10万次），等同于死循环
					//为了处理这种情况，我们让当前线程停顿一下
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [selector_wakeup] [channelReadyToReadData] [read_data_from_socket] [n="+n+"] [remaining:"+receiveDataBuffer.remaining()+"] ["+getStateString(state)+"] [在标记可读的连接上读不到数据，强制当前线程Sleep100毫秒，以防止死循环]");
			}

			if(logger.isDebugEnabled() || printLogFlag){
				if(printLogFlag)
					logger.warn("[{},{},{}] [selector_wakeup] [channelReadyToReadData] [read_data_from_socket] [{}] [{}] [n={}] [position:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,n,receiveDataBuffer.position()});
				else
					logger.debug("[{},{},{}] [selector_wakeup] [channelReadyToReadData] [read_data_from_socket] [{}] [{}] [n={}] [position:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,n,receiveDataBuffer.position()});
				
			}

			receiveDataBuffer.flip();
			
			while(true){
				if(offsetOfByteForMessageLength + numOfByteForMessageLength <= receiveDataBuffer.remaining() ){
					byte tmpByte[] = new byte[numOfByteForMessageLength+offsetOfByteForMessageLength];
					receiveDataBuffer.mark();
					receiveDataBuffer.get(tmpByte);
					receiveDataBuffer.reset();
					long length = factory.byteArrayToNumber(tmpByte,offsetOfByteForMessageLength,numOfByteForMessageLength);
					
					if(length < numOfByteForMessageLength || length > receiveDataBuffer.capacity()){
						IOException e = new IOException("stream error for message length ["+length+"]. the connecton will be closed.");
						throw e;
					}
					
					long packetLength = length;
					
					if(!packetLengthContainsLengSelf){
						packetLength = length + offsetOfByteForMessageLength + numOfByteForMessageLength;
					}
					
					if(packetLength <= receiveDataBuffer.remaining()){
						byte bytes[] = new byte[(int)packetLength];
						
						receiveDataBuffer.get(bytes);
						try{
							Message message = null;
							try{
								message = factory.newMessage(bytes,0,bytes.length);
							}catch(Exception e){
								throw e;
							}
							this.receiveMessageNum ++;
							this.receiveMessagePacketSize += packetLength;
							
							if(selector.statProtocolFlag){
								statMessage(false,message.getTypeDescription(),packetLength);
							}
							
							lastHandledMessage = message;
							receiveMessage(message);
							if(state == Connection2.CONN_STATE_CLOSE){
								return;
							}
						}catch(ConnectionException e){
							logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [selector_wakeup] [channelReadyToReadData] [handle-data] [connection_exception_and_close_connection] ["+getStateString(state)+"]",e);
							this.terminate();
							return;
						}
						catch(Exception e){
							logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [selector_wakeup] [channelReadyToReadData] [handle-data] [unknown_exception] ["+getStateString(state)+"]",e);
						}finally{
							lastHandledMessage = null;
						}
						
					}else{
						if(logger.isDebugEnabled()){
							logger.debug("[{},{},{}] [selector_wakeup] [channelReadyToReadData] [read_data_from_socket] [handle-data] [need_more_data] [{}] [{}] [need:{}] [position:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,packetLength,receiveDataBuffer.position()});
							
						}
						break;
					}
				}else{
					break;
				}
			}
			
			//将buffer中还没有处理的数据，移到开头
			receiveDataBuffer.compact();
			
		}catch(IOException e){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [selector_wakeup] [channelReadyToReadData] [handle-data] [io_exception_and_close_connection] ["+getStateString(state)+"]",e);
			throw e;
		}
	}

	/**
	 * 
	 */
	protected void channelSelectorTimeout() throws IllegalStateException,IOException{
		
		if(logger.isDebugEnabled() || printLogFlag){
			if(printLogFlag)
				logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [selector_wakeup] [channelSelectorTimeout] ["+getStateString(state)+"]");
			else
				logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [selector_wakeup] [channelSelectorTimeout] ["+getStateString(state)+"]");
		}
		
		long now = System.currentTimeMillis();
		if(this.lastReceiveDataTime + this.connectionMaxIdleTimeout < now){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [selector_wakeup] [channelSelectorTimeout] [terminating-cause-readtimeout] [channelSelectorTimeout] [connectionMaxIdleTimeout:"+connectionMaxIdleTimeout+"] [lastReceiveDataTime:"+(lastReceiveDataTime - now)+"]");
			this.terminate();
			return;
		}
		try{
			if(handler != null)
				 handler.waitingTimeout(this,this.waitingRequestMessageTimeout);
		}catch(ConnectionException e){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [selector_wakeup] [channelSelectorTimeout] [waitingTimeout] [ConnectionException]",e);
			this.terminate();
			return;
		}catch(Exception e){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [selector_wakeup] [channelSelectorTimeout] [waitingTimeout] [unknown_exception]",e);
		}
	}
	
	private void receiveMessage(Message message) throws ConnectionException{
	
		try{
			if(handler != null){
				
				handler.receiveMessage(this,message);
				
				long now = System.currentTimeMillis();
				if(now - this.lastBeginHandleMessageTime >= 1000L){
					logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [receive_request_message] [cost_too_long] ["+getStateString(state)+"] ["+remoteAddress+"] [type:"+message.getTypeDescription()+"] [sid:"+message.getSequenceNumAsString()+"] [cost:"+(now-this.lastBeginHandleMessageTime)+"ms]");
				}else{
					if(logger.isDebugEnabled() || printLogFlag){
						if(printLogFlag)
							logger.warn("[{},{},{}] [receive_request_message] [ok] [{}] [{}] [type:{}] [sid:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString()});
						else
							logger.debug("[{},{},{}] [receive_request_message] [ok] [{}] [{}] [type:{}] [sid:{}]",new Object[]{Thread.currentThread().getName(),selector.getName(),name,getStateString(state),remoteAddress,message.getTypeDescription(),message.getSequenceNumAsString()});
					}
				}
				lastBeginHandleMessageTime = now;
			}else{
				logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [receive_request_message] [failed_handle_is_null] ["+getStateString(state)+"] ["+remoteAddress+"] [type:"+message.getTypeDescription()+"] [sid:"+message.getSequenceNumAsString()+"]");
			}
		}catch(ConnectionException e){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [receive_request_message] [failed_catch_connection_exception] ["+getStateString(state)+"] ["+remoteAddress+"] [type:"+message.getTypeDescription()+"] [sid:"+message.getSequenceNumAsString()+"]",e);
			throw e;
		}
		catch(Exception e){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [receive_request_message] [failed_catch_exception] ["+getStateString(state)+"] ["+remoteAddress+"] [type:"+message.getTypeDescription()+"] [sid:"+message.getSequenceNumAsString()+"]",e);
		}
	}
	
	
	private Object notifyTermimateLock = new Object();
	private boolean notidyTerminateFlag = false;
	private boolean notifyStatFlag = false;
	
	/**
	 * 此方法是有主线程主动关闭连接
	 * 
	 * @param reason
	 */
	protected void forceTerminate(String reason){
		state = CONN_STATE_CLOSE;
		try{
			channel.close();
		}catch(Exception e){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"],close channel exception:",e);

		}
		
		synchronized(notifyTermimateLock){
			if(notifyStatFlag == false){
				notifyStatFlag = true;
				selector.statClosedConnection(this);
			}
		}
		
		logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [connection_force_to_terminate] ["+reason+"]");

		if(terminateHandler != null){
			Runnable runnable = new Runnable(){
				public void run(){
					if(terminateHandler != null){
						
						boolean bbb = false;
						if(notidyTerminateFlag == false){
							synchronized(notifyTermimateLock){
								if(notidyTerminateFlag == false){
									notidyTerminateFlag = true;
									bbb = true;
								}
							}
						}	
						if(bbb){
							terminateHandler.ternimate(Connection2.this);
						}
					}
				}
			};
			try{
				this.selector.threadPool.execute(runnable);
			}catch(RejectedExecutionException e){
				logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [connection_force_to_terminate] [通知上层接口失败，线程池满，无可用线程]");
			}
		}
		
		receiveDataBuffer = null;
		sendDataBuffer = null;
		boolean heldByCurrentThread =  sendLock.isHeldByCurrentThread();
		if(heldByCurrentThread == false){
			sendLock.lock();
		}
		try{
			this.waitingQueue.clear();
		}finally{
			if(heldByCurrentThread == false){
				sendLock.unlock();
			}
		}
	}
	
	/**
	 * 此方法由线程池中的线程来关闭连接
	 */
	protected void terminate(){
		state = CONN_STATE_CLOSE;

		try{
			channel.close();
		}catch(Exception e){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"],close channel exception:",e);

		}
		
		synchronized(notifyTermimateLock){
			if(notifyStatFlag == false){
				notifyStatFlag = true;
				selector.statClosedConnection(this);
			}
		}
		
		logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [connection_terminate]");
		
		
		if(terminateHandler != null){
			Runnable runnable = new Runnable(){
				public void run(){
					if(terminateHandler != null){
						
						boolean bbb = false;
						if(notidyTerminateFlag == false){
							synchronized(notifyTermimateLock){
								if(notidyTerminateFlag == false){
									notidyTerminateFlag = true;
									bbb = true;
								}
							}
						}	
						if(bbb){
							terminateHandler.ternimate(Connection2.this);
						}
					}
				}
			};
			try{
				this.selector.threadPool.execute(runnable);
			}catch(RejectedExecutionException e){
				logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [connection_force_to_terminate] [通知上层接口失败，线程池满，无可用线程]");
			}
		}
		
		receiveDataBuffer = null;
		sendDataBuffer = null;
		
		boolean heldByCurrentThread =  sendLock.isHeldByCurrentThread();
		if(heldByCurrentThread == false){
			sendLock.lock();
		}
		try{
			this.waitingQueue.clear();
		}finally{
			if(heldByCurrentThread == false){
				sendLock.unlock();
			}
		}
	}
	
	public static String getStateString(int s){
		String str = "invalid_state";
		switch(s){
		case CONN_STATE_UNKNOWN:
			str = "CONN_STATE_UNKNOWN";
			break;
		case CONN_STATE_OPEN:
			str = "CONN_STATE_OPEN";
			break;
		case CONN_STATE_CONNECT:
			str = "CONN_STATE_CONNECT";
			break;
		
		case CONN_STATE_CLOSE:
			str = "CONN_STATE_CLOSE";
			break;
		}
		return str;
	}

	public int getSendMessageNum() {
		return sendMessageNum;
	}

	public void setSendMessageNum(int sendMessageNum) {
		this.sendMessageNum = sendMessageNum;
	}

	public int getReceiveMessageNum() {
		return receiveMessageNum;
	}

	public void setReceiveMessageNum(int receiveMessageNum) {
		this.receiveMessageNum = receiveMessageNum;
	}

	public long getSendMessagePacketSize() {
		return sendMessagePacketSize;
	}

	public void setSendMessagePacketSize(long sendMessagePacketSize) {
		this.sendMessagePacketSize = sendMessagePacketSize;
	}

	public long getReceiveMessagePacketSize() {
		return receiveMessagePacketSize;
	}

	public void setReceiveMessagePacketSize(long receiveMessagePacketSize) {
		this.receiveMessagePacketSize = receiveMessagePacketSize;
	}

	public long getConnectionMaxIdleTimeout() {
		return connectionMaxIdleTimeout;
	}

	/**
	 * 返回最近一次次链接上接收到数据的时间，此时间只有真正从网络上读到数据才被设置。
	 * 初始值为链接创建的时间。
	 * @return
	 */
	public long getLastReceiveDataTime() {
		return lastReceiveDataTime;
	}

	public int getOffsetOfByteForMessageLength() {
		return offsetOfByteForMessageLength;
	}

	public void setOffsetOfByteForMessageLength(int offsetOfByteForMessageLength) {
		this.offsetOfByteForMessageLength = offsetOfByteForMessageLength;
	}

	public boolean isPacketLengthContainsLengSelf() {
		return packetLengthContainsLengSelf;
	}

	public void setPacketLengthContainsLengSelf(boolean packetLengthContainsLengSelf) {
		this.packetLengthContainsLengSelf = packetLengthContainsLengSelf;
	}
	
	
}

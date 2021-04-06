package com.xuanzhi.tools.transport;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class Connection {

	protected static Logger logger = LoggerFactory.getLogger(Connection.class);
	
	/**
	 * 连接的各个状态，每个状态根据不同的条件可以变到另外一个状态，
	 * 请参加WIKI中的状态变迁图。
	 */
	public static final int CONN_STATE_UNKNOWN = 0;
	public static final int CONN_STATE_OPEN = 1;
	public static final int CONN_STATE_CONNECT = 2;
	public static final int CONN_STATE_WAITING_MESSAGE = 3;
	public static final int CONN_STATE_WRITE_MESSAGE = 4;
	public static final int CONN_STATE_WRITE_REPLY = 5;
	public static final int CONN_STATE_WAITING_REPLY = 6;
	public static final int CONN_STATE_HANDLE_DATA = 7;	
	public static final int CONN_STATE_WAITING_MORE_DATA = 8;
	public static final int CONN_STATE_CLOSE = 9;
	public static final int CONN_STATE_WAITING_SEND_DATA = 10;
	public static final int CONN_STATE_WAITING_SEND_REPLY = 11;
	
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
	
	/**
	 * 滑动窗口，当一个RequestMessage被发送后，会保留在这里，
	 * 直到对应的ResponseMessage消息到达，或者已经重发过多次后，
	 * ResponseMessage仍然没有回来。
	 * 
	 * 此列表是按照响应超时时间来排序的，最前面的消息是最近响应超时的。
	 * 
	 * 注意，ResponseMessage都不占用滑动窗口
	 */
	protected List<WindowItem> sendMessageWindow = new LinkedList<WindowItem>();
	
	/**
	 * 滑动的窗口最大值
	 */
	protected int maxWindowSize = 16;
	
	/**
	 * 此方法设置滑动窗口的最大值，任意时候都可以改变滑动窗口的值，
	 * 但是如果设置的值，小于窗口中消息的个数，那么此方法不起作用
	 * @param maxSize
	 */
	public void setMaxWindowSize(int maxSize){
		if(maxSize >= sendMessageWindow.size())
			maxWindowSize = maxSize;
	}
	
	/**
	 * 每个滑动窗口中的消息，最大超时次数，
	 * 也是最大重发的次数。
	 */
	protected int maxReSendReplyTimes = 3;
	
	/**
	 * 设置重发的次数，此方法可以随时调用
	 * 
	 * @param times
	 */
	public void setMaxReSendTimes(int times){
		maxReSendReplyTimes = times;
	}
	
	/**
	 * 等待响应消息的超时时间，当一个RequestMessage发出后，会等待对应的ResponseMessage,
	 * 如果等待replyTimout时间后，对应的ResponseMessage还没有收到，
	 * 就超时。
	 * 
	 * 默认这个时间为1分钟
	 */
	protected long waitingResponseMessageTimeout = 60* 1000L;
	
	public void setWaitingResponseMessageTimeout(long timeout){
		waitingResponseMessageTimeout = timeout;
	}
	
	public long getWaitingResponseMessageTimeout(){
		return waitingResponseMessageTimeout;
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
	 * 等待socket send buffer的时间，默认为30秒，30秒后如果仍然没有socket send buffer
	 * 供发送数据，则强制断开此链接
	 * 
	 */
	protected long waitingMoreSocketSendBufferTimeout = 30 * 1000L;
	
	public void setWaitingMoreSocketSendBufferTimeout(long timeout){
		waitingMoreSocketSendBufferTimeout = timeout;
	}
	
	public long getWaitingMoreSocketSendBufferTimeout(){
		return waitingMoreSocketSendBufferTimeout;
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
	protected long connectionReadTimeout = 6 * 60 * 1000L;
	
	public void setConnectionReadTimeout(long timeout){
		connectionReadTimeout = timeout;
	}
	/**
	 * 记录最后一次收到数据的时间，此时间 + connectionReadTimeout就是最久可以接收不到数据的时间期限，
	 * 超过这个期限，链接将被强制关闭。
	 */
	protected long lastReceiveDataTime = System.currentTimeMillis();
	
	
	/**
	 * 消息长度用几个字节来标识。我们认为消息是这样的：
	 * |----|------------------------------------------------------|
	 *  长度                  消息内容  
	 */
	protected int numOfByteForMessageLength = 4;
	
	
	/**
	 * 消息的工厂类，用于收到数据后，构造Message
	 */
	protected MessageFactory factory = null;
	
	/**
	 * 消息处理器，服务器模式下一定要设置此变量，客户端模式下可以不设置，用MessageCallback接口了处理响应
	 */
	protected MessageHandler handler = null;
	
	protected ConnectionTerminateHandler terminateHandler = null;
	
	protected DefaultConnectionSelector selector = null;
	
	protected ExceptionObserver observer = null;
	
	public void setExceptionObserver(ExceptionObserver ob){
		observer = ob;
	}
	
	/**
	 * 当Connectoin进入WAITING系列状态是，需要设置此属性，
	 * 以保证超时的时候，Selector能被唤醒。
	 * 
	 * 对于CONN_STATE_WAITING_REPLY，我们需要特殊处理，是因为
	 * 我们有一堆RequestMessage在等待回应，那么CONN_STATE_WAITING_REPLY状态
	 * 下的超时，就应该是即将超时的RequestMessage对应的超时时间。
	 */
	private long theTimeForNextSelctorTimeout = 0L;

	/**
	 * 标识Connection是否在Selector中。
	 * Selector的takeoutConnection在返回Connection前，
	 * 必须设置这个属性为true。
	 * 
	 * 所有外界对Connection的操作，都必须先检查此属性，除了调用close()方法强制关闭此链接。
	 */
	protected boolean hasCheckout = true;
	
	/**
	 * 最后一次checkout的时间，如果checkout的时间过长，selector会强制关闭此链接
	 */
	protected long lastCheckoutTime = System.currentTimeMillis();
	
	/**
	 * 最后一次takeout的时间
	 */
	protected long lastTakeoutTime = System.currentTimeMillis();
	
	/**
	 * 最后一次返回selector的时间，如果长时间没有处理消息，selector会强制关闭此链接
	 */
	protected long lastReturnTime = System.currentTimeMillis();

	/**
	 * Connection的描述信息，包括名字，发送了多少个消息，接收了多少个消息
	 */
	protected String name ;
	protected String remoteAddress;
	protected int sendMessageNum = 0;
	protected int receiveMessageNum = 0;

	protected Object attachment = null;
	
	protected Object attachment2 = null;
	
	protected HashMap<Object,Object> attachementMap = null;
	
	//最后处理的消息，为的是打印日志，表明如果连接被强制中断，是在处理那个消息时中断的。
	//方便更快的定位问题
	protected transient Message lastHandledMessage = null;
	
	//发送缓冲区满的标记，方便应用层在发送缓冲区满了后，可以等待此对象上
	//如果底层有数据发送出去，会调用此对象的notify方法
	protected transient boolean sendbuffFullFlag = false;
	
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
	
	public ConnectionSelector getConnectionSelector(){
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
	protected Connection(DefaultConnectionSelector selector,ByteChannel channel,MessageFactory factory,MessageHandler handler){
		this(selector,channel,factory,handler,64 * 1024,64 * 1024,16,3,60 * 1000L,3 * 60 * 1000L,30 * 1000L,6 * 60 * 1000L);
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
	protected Connection(DefaultConnectionSelector selector,ByteChannel channel,
			MessageFactory factory,MessageHandler handler,int sendBufferSize,int receiveBufferSize,
			int maxWindowSize,int maxReSendReplyTimes,
			long waitingResponseMessageTimeout,long waitingRequestMessageTimeout,long waitingMoreSocketSendBufferTimeout,long connectionReadTimeout){
		
		this.selector = selector;
		this.channel = channel;
		this.factory = factory;
		this.handler = handler;
		sendDataBuffer = ByteBuffer.allocateDirect(sendBufferSize);
		receiveDataBuffer = ByteBuffer.allocateDirect(receiveBufferSize);
		this.maxWindowSize = maxWindowSize;
		this.maxReSendReplyTimes = maxReSendReplyTimes;
		this.waitingResponseMessageTimeout = waitingResponseMessageTimeout;
		this.waitingRequestMessageTimeout = waitingRequestMessageTimeout;
		this.waitingMoreSocketSendBufferTimeout = waitingMoreSocketSendBufferTimeout;
		this.connectionReadTimeout = connectionReadTimeout;
	}
	
	/**
	 * 设置ConnectionTerminateHandler，
	 * 当链接被关闭时，如果滑动窗口中有消息或者receiveDataBuffer有数据，会启动新的线程调用接口中的方法。
	 * 否则不会启动新的线程，来调用接口中的方法
	 * @param handler
	 */
	public void setConnectionTerminateHandler(ConnectionTerminateHandler handler){
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
	public void setMessageHandler(MessageHandler handler){
		this.handler = handler;
	}
	
/**
	 * 一次发送多个消息，先将这些消息体写到这个sendDataBuffer中，然后再将这个Buffer中的数据发送到网络上。
	 * 
	 * 如果这些消息总和过大，超过sendDataBuffer的大小，将抛出IllegalArgumentException异常，这个消息一个都没有发送。
	 * 如果消息的个数，超过滑动窗口剩余的空间，抛出WindowFullException异常，这个消息一个都没有发送。
	 * 
	 * 
	 * 由于我们采用的是non-blocking模式，所以发送Buffer时，
	 * 可能会没有将所有的数据发送取出，此时发送的线程仍然立即返回。
	 * 但是SocketChannel会对OP_WRITE感兴趣，当网络可写的时候，
	 * Selector会收到通知，并启用新的线程调用channelReadyToWriteData方法。
	 * 
	 * 当发送消息时，sendDataBuffer没有足够的空间来放消息，
	 * 就阻塞当前线程，直到有空间放消息或者链接关闭。
	 * 
	 * 此方法调用返回时，conneciton要么已将加入到Selector中，要么已经被关闭，无论出现什么异常。
	 * 其中，出现IOException异常，代表connection已经被关闭。
	 * 出现IllegalStateException，IllegalArgumentException和WindowFullException，connection将会返回到selector中。
	 * 
	 * @param messages 多条消息
	 * @param timeout 当senderBuffer中没有足够的空间存放这些消息时，等待的时间。小于等于0表示一直等待
	 * @throws SendBufferFullException 等待超时，就是在一定的时间内仍然没有足够的空间存放消息时，抛出此异常
	 * @throws IllegalStateException 调用此方法是状态不是CONN_STATE_WAITING_MESSAGE，就抛出异常
	 * @throws IllegalArgumentException 消息长度超过sendDataBuffer的一半
	 * @throws WindowFullException 滑动窗口已经满了，不能再发送消息了，直到收到Reply
	 * @throws IOException，其他IO异常，出现此异常，链接的状态将为CONN_STATE_CLOSE
	 */
	public synchronized void writeMessage(RequestMessage[] messages) throws IllegalStateException,SendBufferFullException,IllegalArgumentException,WindowFullException,IOException{
		writeMessage(messages,0,messages.length,true);
	}
	
	/**
	 * 一次发送多个消息，先将这些消息体写到这个sendDataBuffer中，然后再将这个Buffer中的数据发送到网络上。
	 * 
	 * 如果这些消息总和过大，超过sendDataBuffer的大小，将抛出IllegalArgumentException异常，这个消息一个都没有发送。
	 * 如果消息的个数，超过滑动窗口剩余的空间，抛出WindowFullException异常，这个消息一个都没有发送。
	 * 
	 * 
	 * 由于我们采用的是non-blocking模式，所以发送Buffer时，
	 * 可能会没有将所有的数据发送取出，此时发送的线程仍然立即返回。
	 * 但是SocketChannel会对OP_WRITE感兴趣，当网络可写的时候，
	 * Selector会收到通知，并启用新的线程调用channelReadyToWriteData方法。
	 * 
	 * 当发送消息时，sendDataBuffer没有足够的空间来放消息，
	 * 就阻塞当前线程，直到有空间放消息或者链接关闭。
	 * 
	 * 此方法调用返回时，conneciton要么已将加入到Selector中，要么已经被关闭，无论出现什么异常。
	 * 其中，出现IOException异常，代表connection已经被关闭。
	 * 出现IllegalStateException，IllegalArgumentException和WindowFullException，connection将会返回到selector中。
	 * 
	 * @param messages 多条消息
	 * @param timeout 当senderBuffer中没有足够的空间存放这些消息时，等待的时间。小于等于0表示一直等待
	 * @throws SendBufferFullException 等待超时，就是在一定的时间内仍然没有足够的空间存放消息时，抛出此异常
	 * @throws IllegalStateException 调用此方法是状态不是CONN_STATE_WAITING_MESSAGE，就抛出异常
	 * @throws IllegalArgumentException 消息长度超过sendDataBuffer的一半
	 * @throws WindowFullException 滑动窗口已经满了，不能再发送消息了，直到收到Reply
	 * @throws IOException，其他IO异常，出现此异常，链接的状态将为CONN_STATE_CLOSE
	 */
	public synchronized void writeMessage(RequestMessage[] messages,int offset,int size,boolean needResponseMessage) throws IllegalStateException,SendBufferFullException,IllegalArgumentException,WindowFullException,IOException{
		if(hasCheckout == false){
			if(sendDataBuffer.position() > 0)
				selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			else	
				selector.returnConnectoin(this,SelectionKey.OP_READ);
			
			throw new IllegalStateException("connection must be checked out first,then you can use this connection to send message.");
		}
		
		int len = 0 ;
		for(int i = 0 ; i < size ; i++){
			len += messages[offset + i].getLength();
		}
		
		if(len > sendDataBuffer.capacity()){
			this.hasCheckout = false;
			if(sendDataBuffer.position() > 0)
				selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			else	
				selector.returnConnectoin(this,SelectionKey.OP_READ);
			
			throw new IllegalArgumentException("message array's total length["+len+"]too big than ["+sendDataBuffer.capacity()+"]");
		}
		
		if(needResponseMessage && sendMessageWindow.size()  + size > maxWindowSize){
			this.hasCheckout = false;
			if(sendDataBuffer.position() > 0)
				selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			else	
				selector.returnConnectoin(this,SelectionKey.OP_READ);
			throw new IllegalArgumentException("window has no enough space to send message array["+size+"]");
		}

		if(state != CONN_STATE_WAITING_MESSAGE && state != CONN_STATE_WAITING_REPLY && state != Connection.CONN_STATE_WAITING_MORE_DATA
				&& state != Connection.CONN_STATE_WAITING_SEND_DATA && state != Connection.CONN_STATE_WAITING_SEND_REPLY){
			this.hasCheckout = false;
			
			if(sendDataBuffer.position() > 0)
				selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			else	
				selector.returnConnectoin(this,SelectionKey.OP_READ);
			
			throw new IllegalStateException("current state["+getStateString(state)+"] not avaiable for sendMessage");
		}
		
		// write data
		if(sendDataBuffer.limit() - sendDataBuffer.position() < len){
			this.hasCheckout = false;
			
			if(sendDataBuffer.position() > 0)
				selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			else	
				selector.returnConnectoin(this,SelectionKey.OP_READ);	
			sendbuffFullFlag = true;
			throw new SendBufferFullException("no enough space in send buffer[limit: "+sendDataBuffer.limit()+"] [pos:"+sendDataBuffer.position()+"] [ msglen:"+len+"]");
		}

		if(logger.isDebugEnabled()){
			logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [write_message_array] [writeMessage] ["+getStateString(state)+"] [num:"+size+"]");
		}

		sendDataBuffer.mark();
		for(int i = 0 ; i < size ; i++){
			int n = messages[offset + i].writeTo(sendDataBuffer);
			if(n == 0){
				sendDataBuffer.reset();
				this.hasCheckout = false;
				
				if(sendDataBuffer.position() > 0)
					selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
				else	
					selector.returnConnectoin(this,SelectionKey.OP_READ);
				
				throw new IllegalArgumentException("can't write data to sendDataBuffer,but there is enough space. why? hope never throw this exception.");
			}
		}
		
		changeState(CONN_STATE_WRITE_MESSAGE);
		
		if(needResponseMessage){
			for(int i = 0 ; i < size ; i++){
				WindowItem wi =	new WindowItem(messages[offset + i]);
				sendMessageWindow.add(wi);
				if(selector.enableTimepiece)
					wi.nextTimeout = selector.timeOfTimePiece + waitingResponseMessageTimeout;
				else
					wi.nextTimeout = System.currentTimeMillis() + waitingResponseMessageTimeout;
			}
		}
		sendMessageNum += size;
		writeSendDataBuffer(false);
	}
	
	/**
	 * 发送消息，先将消息体写到这个sendDataBuffer中，然后再将这个Buffer中的数据发送到网络上。
	 * 
	 * 如果这个消息过大，超过sendDataBuffer大小的一半，将抛出IllegalArgumentException异常。
	 * 
	 * 如果此事滑动窗口大小达到了最大值，抛出WindowFullException。
	 * 
	 * 由于我们采用的是non-blocking模式，所以发送Buffer时，
	 * 可能会没有将所有的数据发送取出，此时发送的线程仍然立即返回。
	 * 但是SocketChannel会对OP_WRITE感兴趣，当网络可写的时候，
	 * Selector会收到通知，并启用新的线程调用channelReadyToWriteData方法。
	 * 
	 * 当发送消息时，sendDataBuffer没有足够的空间来放消息，
	 * 就阻塞当前线程，直到有空间放消息或者链接关闭。
	 * 
	 * 此方法调用返回时，conneciton要么已将加入到Selector中，要么已经被关闭，无论出现什么异常。
	 * 其中，出现IOException异常，代表connection已经被关闭。
	 * 出现IllegalStateException，IllegalArgumentException和WindowFullException，connection将会返回到selector中。
	 * @param message
	 * @param timeout 当senderBuffer中没有足够的空间存放这些消息时，等待的时间。小于等于0表示一直等待
	 * @throws WaitingTimeoutExceptoin 等待超时，就是在一定的时间内仍然没有足够的空间存放消息时，抛出此异常
	 * @throws IllegalStateException 调用此方法是状态不是CONN_STATE_WAITING_MESSAGE，就抛出异常
	 * @throws IllegalArgumentException 消息长度超过sendDataBuffer的一半
	 * @throws WindowFullException 滑动窗口已经满了，不能再发送消息了，直到收到Reply
	 * @throws IOException，其他IO异常，出现此异常，链接的状态将为CONN_STATE_CLOSE
	 */
	public synchronized void writeMessage(RequestMessage message) throws SendBufferFullException,IllegalStateException,IllegalArgumentException,WindowFullException,IOException{
		writeMessage(message,true);
	}
	
		/**
	 * 发送消息，先将消息体写到这个sendDataBuffer中，然后再将这个Buffer中的数据发送到网络上。
	 * 
	 * 如果这个消息过大，超过sendDataBuffer大小的一半，将抛出IllegalArgumentException异常。
	 * 
	 * 如果此事滑动窗口大小达到了最大值，抛出WindowFullException。
	 * 
	 * 由于我们采用的是non-blocking模式，所以发送Buffer时，
	 * 可能会没有将所有的数据发送取出，此时发送的线程仍然立即返回。
	 * 但是SocketChannel会对OP_WRITE感兴趣，当网络可写的时候，
	 * Selector会收到通知，并启用新的线程调用channelReadyToWriteData方法。
	 * 
	 * 当发送消息时，sendDataBuffer没有足够的空间来放消息，
	 * 就阻塞当前线程，直到有空间放消息或者链接关闭。
	 * 
	 * 此方法调用返回时，conneciton要么已将加入到Selector中，要么已经被关闭，无论出现什么异常。
	 * 其中，出现IOException异常，代表connection已经被关闭。
	 * 出现IllegalStateException，IllegalArgumentException和WindowFullException，connection将会返回到selector中。
	 * @param message
	 * @param timeout 当senderBuffer中没有足够的空间存放这些消息时，等待的时间。小于等于0表示一直等待
	 * @throws WaitingTimeoutExceptoin 等待超时，就是在一定的时间内仍然没有足够的空间存放消息时，抛出此异常
	 * @throws IllegalStateException 调用此方法是状态不是CONN_STATE_WAITING_MESSAGE，就抛出异常
	 * @throws IllegalArgumentException 消息长度超过sendDataBuffer的一半
	 * @throws WindowFullException 滑动窗口已经满了，不能再发送消息了，直到收到Reply
	 * @throws IOException，其他IO异常，出现此异常，链接的状态将为CONN_STATE_CLOSE
	 */
	public synchronized void writeMessage(RequestMessage message,MessageCallback callback) throws SendBufferFullException,IllegalStateException,IllegalArgumentException,WindowFullException,IOException{
		writeMessage(message,callback,true);
	}
	
	/**
	 * 发送消息，先将消息体写到这个sendDataBuffer中，然后再将这个Buffer中的数据发送到网络上。
	 * 
	 * 如果这个消息过大，超过sendDataBuffer大小的一半，将抛出IllegalArgumentException异常。
	 * 
	 * 如果此事滑动窗口大小达到了最大值，抛出WindowFullException。
	 * 
	 * 由于我们采用的是non-blocking模式，所以发送Buffer时，
	 * 可能会没有将所有的数据发送取出，此时发送的线程仍然立即返回。
	 * 但是SocketChannel会对OP_WRITE感兴趣，当网络可写的时候，
	 * Selector会收到通知，并启用新的线程调用channelReadyToWriteData方法。
	 * 
	 * 当发送消息时，sendDataBuffer没有足够的空间来放消息，
	 * 就阻塞当前线程，直到有空间放消息或者链接关闭。
	 * 
	 * 此方法调用返回时，conneciton要么已将加入到Selector中，要么已经被关闭，无论出现什么异常。
	 * 其中，出现IOException异常，代表connection已经被关闭。
	 * 出现IllegalStateException，IllegalArgumentException和WindowFullException，connection将会返回到selector中。
	 * 
	 * needResponseMessage表明此此请求消息是否需要等待对方的响应消息，如果为false，
	 * 那么将不使用滑动窗口。
	 * 
	 * @param message
	 * @param timeout 当senderBuffer中没有足够的空间存放这些消息时，等待的时间。小于等于0表示一直等待
	 * @throws WaitingTimeoutExceptoin 等待超时，就是在一定的时间内仍然没有足够的空间存放消息时，抛出此异常
	 * @throws IllegalStateException 调用此方法是状态不是CONN_STATE_WAITING_MESSAGE，就抛出异常
	 * @throws IllegalArgumentException 消息长度超过sendDataBuffer的一半
	 * @throws WindowFullException 滑动窗口已经满了，不能再发送消息了，直到收到Reply
	 * @throws IOException，其他IO异常，出现此异常，链接的状态将为CONN_STATE_CLOSE
	 */
	public synchronized void writeMessage(RequestMessage message,boolean needResponseMessage) throws SendBufferFullException,IllegalStateException,IllegalArgumentException,WindowFullException,IOException{
		writeMessage(message,null,needResponseMessage);
	}
	
	/**
	 * 发送消息，先将消息体写到这个sendDataBuffer中，然后再将这个Buffer中的数据发送到网络上。
	 * 
	 * 如果这个消息过大，超过sendDataBuffer大小的一半，将抛出IllegalArgumentException异常。
	 * 
	 * 如果此事滑动窗口大小达到了最大值，抛出WindowFullException。
	 * 
	 * 由于我们采用的是non-blocking模式，所以发送Buffer时，
	 * 可能会没有将所有的数据发送取出，此时发送的线程仍然立即返回。
	 * 但是SocketChannel会对OP_WRITE感兴趣，当网络可写的时候，
	 * Selector会收到通知，并启用新的线程调用channelReadyToWriteData方法。
	 * 
	 * 当发送消息时，sendDataBuffer没有足够的空间来放消息，
	 * 就阻塞当前线程，直到有空间放消息或者链接关闭。
	 * 
	 * 此方法调用返回时，conneciton要么已将加入到Selector中，要么已经被关闭，无论出现什么异常。
	 * 其中，出现IOException异常，代表connection已经被关闭。
	 * 出现IllegalStateException，IllegalArgumentException和WindowFullException，connection将会返回到selector中。
	 * 
	 * needResponseMessage表明此此请求消息是否需要等待对方的响应消息，如果为false，
	 * 那么将不使用滑动窗口。
	 * 
	 * @param message
	 * @param timeout 当senderBuffer中没有足够的空间存放这些消息时，等待的时间。小于等于0表示一直等待
	 * @throws WaitingTimeoutExceptoin 等待超时，就是在一定的时间内仍然没有足够的空间存放消息时，抛出此异常
	 * @throws IllegalStateException 调用此方法是状态不是CONN_STATE_WAITING_MESSAGE，就抛出异常
	 * @throws IllegalArgumentException 消息长度超过sendDataBuffer的一半
	 * @throws WindowFullException 滑动窗口已经满了，不能再发送消息了，直到收到Reply
	 * @throws IOException，其他IO异常，出现此异常，链接的状态将为CONN_STATE_CLOSE
	 */
	public synchronized void writeResponse(ResponseMessage message) throws SendBufferFullException,IllegalStateException,IllegalArgumentException,WindowFullException,IOException{
		if(hasCheckout == false){
			this.hasCheckout = false;
			if(sendDataBuffer.position() > 0)
				selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			else	
				selector.returnConnectoin(this,SelectionKey.OP_READ);
			throw new IllegalStateException("connection must be checked out first,then you can use this connection to send message.");
		}
		
		int len = message.getLength();
		if(len > sendDataBuffer.capacity()){
			this.hasCheckout = false;
			if(sendDataBuffer.position() > 0)
				selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			else	
				selector.returnConnectoin(this,SelectionKey.OP_READ);
			throw new IllegalArgumentException("message["+message.getTypeDescription()+"]'s length(len="+len+") too big than ["+sendDataBuffer.capacity()/2+"]");
		}
		
		if(state != CONN_STATE_WAITING_MESSAGE && state != CONN_STATE_WAITING_REPLY && state != Connection.CONN_STATE_WAITING_MORE_DATA
				&& state != Connection.CONN_STATE_WAITING_SEND_DATA && state != Connection.CONN_STATE_WAITING_SEND_REPLY){
			this.hasCheckout = false;
			if(sendDataBuffer.position() > 0)
				selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			else	
				selector.returnConnectoin(this,SelectionKey.OP_READ);
			throw new IllegalStateException("current state["+getStateString(state)+"] must be ["+getStateString(CONN_STATE_WAITING_MESSAGE)+"]");
		}
		
		// write data
		if(sendDataBuffer.limit() - sendDataBuffer.position() < len){
			this.hasCheckout = false;
			if(sendDataBuffer.position() > 0)
				selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			else	
				selector.returnConnectoin(this,SelectionKey.OP_READ);
			sendbuffFullFlag = true;
			throw new SendBufferFullException("no enough space in send buffer");
		}
			
		if(logger.isDebugEnabled()){
			logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [write_message] [writeMessage] ["+getStateString(state)+"] ["+message.getTypeDescription()+"]");
		}
		
		int n = message.writeTo(sendDataBuffer);
		
		if(n == 0){
			this.hasCheckout = false;
			if(sendDataBuffer.position() > 0)
				selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			else	
				selector.returnConnectoin(this,SelectionKey.OP_READ);
			throw new IllegalArgumentException("can't write data to sendDataBuffer,but there is enough space. why? hope never throw this exception.");
		}
		
		changeState(CONN_STATE_WRITE_REPLY);
		
		sendMessageNum ++;
		
		writeSendDataBuffer(true);
	}
	
	/**
	 * 发送消息，先将消息体写到这个sendDataBuffer中，然后再将这个Buffer中的数据发送到网络上。
	 * 
	 * 如果这个消息过大，超过sendDataBuffer大小的一半，将抛出IllegalArgumentException异常。
	 * 
	 * 如果此事滑动窗口大小达到了最大值，抛出WindowFullException。
	 * 
	 * 由于我们采用的是non-blocking模式，所以发送Buffer时，
	 * 可能会没有将所有的数据发送取出，此时发送的线程仍然立即返回。
	 * 但是SocketChannel会对OP_WRITE感兴趣，当网络可写的时候，
	 * Selector会收到通知，并启用新的线程调用channelReadyToWriteData方法。
	 * 
	 * 当发送消息时，sendDataBuffer没有足够的空间来放消息，
	 * 就阻塞当前线程，直到有空间放消息或者链接关闭。
	 * 
	 * 此方法调用返回时，conneciton要么已将加入到Selector中，要么已经被关闭，无论出现什么异常。
	 * 其中，出现IOException异常，代表connection已经被关闭。
	 * 出现IllegalStateException，IllegalArgumentException和WindowFullException，connection将会返回到selector中。
	 * 
	 * needResponseMessage表明此此请求消息是否需要等待对方的响应消息，如果为false，
	 * 那么将不使用滑动窗口。
	 * 
	 * @param message
	 * @param timeout 当senderBuffer中没有足够的空间存放这些消息时，等待的时间。小于等于0表示一直等待
	 * @throws WaitingTimeoutExceptoin 等待超时，就是在一定的时间内仍然没有足够的空间存放消息时，抛出此异常
	 * @throws IllegalStateException 调用此方法是状态不是CONN_STATE_WAITING_MESSAGE，就抛出异常
	 * @throws IllegalArgumentException 消息长度超过sendDataBuffer的一半
	 * @throws WindowFullException 滑动窗口已经满了，不能再发送消息了，直到收到Reply
	 * @throws IOException，其他IO异常，出现此异常，链接的状态将为CONN_STATE_CLOSE
	 */
	public synchronized void writeMessage(RequestMessage message,MessageCallback callback,boolean needResponseMessage) throws SendBufferFullException,IllegalStateException,IllegalArgumentException,WindowFullException,IOException{
		if(hasCheckout == false){
			this.hasCheckout = false;
			if(sendDataBuffer.position() > 0)
				selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			else	
				selector.returnConnectoin(this,SelectionKey.OP_READ);
			throw new IllegalStateException("connection must be checked out first,then you can use this connection to send message.");
		}
		
		int len = message.getLength();
		if(len > sendDataBuffer.capacity()){
			this.hasCheckout = false;
			if(sendDataBuffer.position() > 0)
				selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			else	
				selector.returnConnectoin(this,SelectionKey.OP_READ);
			throw new IllegalArgumentException("message["+message.getTypeDescription()+"]'s length(len="+len+") too big than ["+sendDataBuffer.capacity()/2+"]");
		}
		
		if(needResponseMessage && sendMessageWindow.size() >= maxWindowSize){
			this.hasCheckout = false;
			if(sendDataBuffer.position() > 0)
				selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			else	
				selector.returnConnectoin(this,SelectionKey.OP_READ);
			throw new WindowFullException("window reach its max size {"+maxWindowSize+"}");
		}

		if(state != CONN_STATE_WAITING_MESSAGE && state != CONN_STATE_WAITING_REPLY && state != Connection.CONN_STATE_WAITING_MORE_DATA
				&& state != Connection.CONN_STATE_WAITING_SEND_DATA && state != Connection.CONN_STATE_WAITING_SEND_REPLY){
			this.hasCheckout = false;
			if(sendDataBuffer.position() > 0)
				selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			else	
				selector.returnConnectoin(this,SelectionKey.OP_READ);
			throw new IllegalStateException("current state["+getStateString(state)+"] must be ["+getStateString(CONN_STATE_WAITING_MESSAGE)+"]");
		}
		
		// write data
		if(sendDataBuffer.limit() - sendDataBuffer.position() < len){
			this.hasCheckout = false;
			if(sendDataBuffer.position() > 0)
				selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			else	
				selector.returnConnectoin(this,SelectionKey.OP_READ);
			sendbuffFullFlag = true;
			throw new SendBufferFullException("no enough space in send buffer");
		}
			
		if(logger.isDebugEnabled()){
			logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [write_message] [writeMessage] ["+getStateString(state)+"] ["+message.getTypeDescription()+"]");
		}
		
		int n = message.writeTo(sendDataBuffer);
		
		if(n == 0){
			this.hasCheckout = false;
			if(sendDataBuffer.position() > 0)
				selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			else	
				selector.returnConnectoin(this,SelectionKey.OP_READ);
			throw new IllegalArgumentException("can't write data to sendDataBuffer,but there is enough space. why? hope never throw this exception.");
		}
		
		changeState(CONN_STATE_WRITE_MESSAGE);
		if(needResponseMessage){
			WindowItem wi =	new WindowItem(message);
			sendMessageWindow.add(wi);
			if(selector.enableTimepiece)
				wi.nextTimeout = selector.timeOfTimePiece + waitingResponseMessageTimeout;
			else
				wi.nextTimeout = System.currentTimeMillis() + waitingResponseMessageTimeout;
			wi.callback = callback;
		}
		sendMessageNum ++;
		
		writeSendDataBuffer(false);
	}
	/**
	 * 强制关闭这个链接，这个链接中所有正在等待回应消息的RequestMessage
	 * 以及receiveDataBuffer中的数据，将试图别解析成消息，通过关闭事件发送出来。
	 * 
	 * 此方法是对外使用的。
	 */
	public synchronized void close(){
		if(state == CONN_STATE_CLOSE) return;
		state = Connection.CONN_STATE_CLOSE;
		//terminate();
		selector.closeConnection(this);
	}
	
	/**
	 * 获取这个链接下一次超时的时间
	 * @return
	 */
	protected long getTimeForNextTimeout(){
		long now = 0;
		if(selector.enableTimepiece){
			now = selector.timeOfTimePiece;
		}else{
			now = System.currentTimeMillis();
		}
		if(theTimeForNextSelctorTimeout == 0){// not set
			switch(state){
			case CONN_STATE_WAITING_MESSAGE:
				theTimeForNextSelctorTimeout = now + this.waitingRequestMessageTimeout;
			case CONN_STATE_WAITING_REPLY:
				theTimeForNextSelctorTimeout = now + this.waitingResponseMessageTimeout;
			case CONN_STATE_WAITING_MORE_DATA:
				theTimeForNextSelctorTimeout = now + this.waitingResponseMessageTimeout;
			case CONN_STATE_WAITING_SEND_DATA:
				theTimeForNextSelctorTimeout = now + this.waitingMoreSocketSendBufferTimeout;
			case CONN_STATE_WAITING_SEND_REPLY:
				theTimeForNextSelctorTimeout = now + this.waitingMoreSocketSendBufferTimeout;
			default:
				theTimeForNextSelctorTimeout = now + this.waitingRequestMessageTimeout;
			}
		}
		
		return theTimeForNextSelctorTimeout;
		
	}
	
	/**
	 * 获得滑动窗口中，最近要超时的时间，如果滑动窗口中没有消息，那么返回等待waitingResponseMessageTimeout后的时间
	 * 
	 * 注意此方法必须在状态CONN_STATE_WAITING_REPLY下调到，否则抛出IllegalStateException。
	 * @return
	 */
	private long getNearTimeoutOfWindow(){
		if(state != CONN_STATE_WAITING_REPLY)
			throw new IllegalStateException("current state["+getStateString(state)+"] invalid for getNearTimeoutOfWindow() method.");
		
		if(this.sendMessageWindow.size() > 0){
			WindowItem wi = sendMessageWindow.get(0);
			return wi.nextTimeout;
		}else{
			if(selector.enableTimepiece)
				return selector.timeOfTimePiece + this.waitingResponseMessageTimeout;
			else
				return System.currentTimeMillis() + this.waitingResponseMessageTimeout;
		}
	}
	
	/**
	 * channel的OP_WRITE被设置，同时Selector检查到此连接，
	 * 启动新的线程调用此方法。调用此方法的时候，channel关心的OP_WRITE和OP_READ都被清空。
	 * 以保证只有一个线程在操作此连接。
	 * 
	 * 此方法标识SocketChannel可以写了。
	 */
	protected synchronized void  channelReadyToWriteData() throws IllegalStateException,IOException{

		if(logger.isDebugEnabled()){
			logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [selector_wakeup] [channelReadyToWriteData] ["+getStateString(state)+"]");
		}

		if(state == Connection.CONN_STATE_CLOSE){
			throw new IllegalStateException("current state ["+getStateString(state)+"]");
		}
		
		if(state == CONN_STATE_WAITING_SEND_REPLY ){
			changeState(CONN_STATE_WRITE_REPLY);
			writeSendDataBuffer(true);
		}else{
			changeState(CONN_STATE_WRITE_MESSAGE);
			writeSendDataBuffer(false);
		}
	}
	
	
	/**
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
	 */
	protected synchronized void channelReadyToReadData() throws IllegalStateException,IOException{
		handleData(true);
	}
	
	/**
	 * 处理数据函数，当receiveDataBuffer中存在数据，或者selector发现有数据到达，
	 * 调用此方法。
	 * 
	 * @param needRead
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void handleData(boolean needRead) throws IllegalStateException,IOException{
		if(logger.isDebugEnabled()){
			if(needRead)
				logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [selector_wakeup] [channelReadyToReadData] ["+getStateString(state)+"]");
			else
				logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [write_replay_call] [handle_data] ["+getStateString(state)+"]");
		}
		
		if(state == Connection.CONN_STATE_CLOSE){
			throw new IllegalStateException("current state ["+getStateString(state)+"]");
		}
		
		changeState(CONN_STATE_HANDLE_DATA);
		
		if(factory == null){
			throw new IllegalStateException("factory is null. please set MessageFactory first.");
		}
		
		try{
			if(needRead){
				int n = channel.read(receiveDataBuffer);
				if(n > 0){
					if(selector.enableTimepiece)
						this.lastReceiveDataTime = selector.timeOfTimePiece;
					else
						this.lastReceiveDataTime = System.currentTimeMillis();
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [read_data_from_socket] [n="+n+"] [remaining:"+receiveDataBuffer.remaining()+"] ["+getStateString(state)+"] [在标记可读的连接上读不到数据，强制当前线程Sleep100毫秒，以防止死循环]");
				}

				if(logger.isDebugEnabled()){
					logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [read_data_from_socket] [n="+n+"] [remaining:"+receiveDataBuffer.remaining()+"] ["+getStateString(state)+"]");
				}
			}
			receiveDataBuffer.flip();
			
			
			ResponseMessage reply = null;
			while(true){
				if(numOfByteForMessageLength <= receiveDataBuffer.remaining() ){
					byte tmpByte[] = new byte[numOfByteForMessageLength];
					receiveDataBuffer.mark();
					receiveDataBuffer.get(tmpByte);
					receiveDataBuffer.reset();
					long length = factory.byteArrayToNumber(tmpByte,0,numOfByteForMessageLength);
					
					if(length < numOfByteForMessageLength || length > receiveDataBuffer.capacity()){
						IOException e = new IOException("stream error for message length ["+length+"]. the connecton will be closed.");
						if(observer != null){
							int len = receiveDataBuffer.remaining();
							if( len>0){
								byte bytes[] = new byte[len];
								receiveDataBuffer.get(bytes);
								tmpByte = bytes;
							}							
							observer.catchException(this, e, "message length error", tmpByte, 0, tmpByte.length);
						}
						throw e;
					}
					
					if(length <= receiveDataBuffer.remaining()){
						byte bytes[] = new byte[(int)length];
						receiveDataBuffer.get(bytes);
						try{
							Message message = null;
							try{
								message = factory.newMessage(bytes,0,bytes.length);
							}catch(Exception e){
								if(observer != null){
									observer.catchException(this, e, "parse message error", bytes, 0, bytes.length);
								}
								throw e;
							}
							this.receiveMessageNum ++;
							if(message instanceof ResponseMessage){
								lastHandledMessage = message;
								receiveResponseMessage((ResponseMessage)message);
							}else{
								lastHandledMessage = message;
								reply = receiveRequestMessage((RequestMessage)message);
								if(reply != null)
									break;
							}
						}catch(ConnectionException e){
							logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [handle-data] [connection_exception_and_close_connection] ["+getStateString(state)+"]",e);
							this.terminate();
							return;
						}
						catch(Exception e){
							logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [handle-data] [unknown_exception] ["+getStateString(state)+"]",e);
						}finally{
							lastHandledMessage = null;
						}
						
					}else{
						if(logger.isDebugEnabled()){
							logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [handle-data] [need_more_data] [need:"+length+"] [remaining:"+receiveDataBuffer.remaining()+"]");
						}
						break;
					}
				}else{
					break;
				}
			}
			
			//将buffer中还没有处理的数据，移到开头
			receiveDataBuffer.compact();
			
			if(reply != null){
				changeState(CONN_STATE_WRITE_REPLY);
				writeResponseMessage(reply);
			}else{
				if(receiveDataBuffer.position() > 0){
					changeState(CONN_STATE_WAITING_MORE_DATA);
					if(selector.enableTimepiece)
						theTimeForNextSelctorTimeout = selector.timeOfTimePiece + this.waitingResponseMessageTimeout;
					else
						theTimeForNextSelctorTimeout = System.currentTimeMillis() + this.waitingResponseMessageTimeout;
				}else{
					if(this.sendMessageWindow.size() > 0){
						changeState(CONN_STATE_WAITING_REPLY);
						theTimeForNextSelctorTimeout = getNearTimeoutOfWindow();
					}else{
						changeState(CONN_STATE_WAITING_MESSAGE);
						if(selector.enableTimepiece)
							theTimeForNextSelctorTimeout = selector.timeOfTimePiece + this.waitingRequestMessageTimeout;
						else
							theTimeForNextSelctorTimeout = System.currentTimeMillis() + this.waitingRequestMessageTimeout;
					}
				}
				
				long now = 0;
				if(selector.enableTimepiece){
					now = selector.timeOfTimePiece;
				}else{
					now = System.currentTimeMillis();
				}
				
				this.hasCheckout = false;
				if(sendDataBuffer.position() > 0){
					selector.returnConnectoin(this,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
					if(logger.isDebugEnabled()){
						logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [return_conn_to_selector] [OP_READ|OP_WRITE] ["+getStateString(state)+"] [timeout:"+(theTimeForNextSelctorTimeout - now)+"]");
					}
				}
				else{
					selector.returnConnectoin(this,SelectionKey.OP_READ);
					if(logger.isDebugEnabled()){
						logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [return_conn_to_selector] [OP_READ] ["+getStateString(state)+"] [timeout:"+(theTimeForNextSelctorTimeout - now)+"]");
					}
				}
				

				
			}
			
		}catch(IOException e){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [handle-data] [io_exception_and_close_connection] ["+getStateString(state)+"]",e);
			throw e;
		}
	}
	
	
	/**
	 * Selector超时，启动新的线程调用此函数。
	 * 当滑动窗口中的消息对应的响应超时时，将重发消息。最多每个消息重发maxContiguousReplyTimeout次。
	 * 当滑动窗口中的所有的消息都重发过maxContiguousReplyTimeout次后，仍然没有响应，
	 * 则将断开此连接。
	 * 
	 */
	protected synchronized void channelSelectorTimeout() throws IllegalStateException,SendBufferFullException,WindowFullException,IOException{
		
		if(logger.isDebugEnabled()){
			logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [selector_wakeup] [channelSelectorTimeout] ["+getStateString(state)+"]");
		}
		long now = 0;
		if(selector.enableTimepiece){
			now = selector.timeOfTimePiece;
		}else{
			now = System.currentTimeMillis();
		}
		
		if(this.lastReceiveDataTime + this.connectionReadTimeout < now){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [terminating-cause-readtimeout] [channelSelectorTimeout] [connectionReadTimeout:"+connectionReadTimeout+"] [lastReceiveDataTime:"+(lastReceiveDataTime - now)+"]");
			this.terminate();
			return;
		}
		
		if(state == CONN_STATE_WAITING_MESSAGE){
			RequestMessage message = null;
			try{
				message = receiveTimeoutForWaitingRequestMessage();
			}catch(ConnectionException e){
				logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [waitingTimeout] [close_connection]",e);
				this.terminate();
				return;
			}catch(Exception e){
				logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [waitingTimeout] [unknown_exception]",e);
			}
			
			if(message != null){
				try{
					writeMessage(message);//此方法会改变conn的状态到CONN_STATE_WRITE_MESSAGE
				}catch(Exception e){
					logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [waitingTimeout] [write_message_error:"+message.getTypeDescription()+"]",e);
					this.terminate();
					
				}
			}else{
				
				this.hasCheckout = false;
				if(selector.enableTimepiece){
					now = selector.timeOfTimePiece;
				}else{
					now = System.currentTimeMillis();
				}
				
				this.theTimeForNextSelctorTimeout = now + this.waitingRequestMessageTimeout;
				
				if(sendDataBuffer.position() > 0){
					selector.returnConnectoin(this,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
					if(logger.isDebugEnabled()){
						logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [return_conn_to_selector] [OP_READ|OP_WRITE] ["+getStateString(state)+"] [timeout:"+(theTimeForNextSelctorTimeout - now)+"]");
					}
				}
				else{
					selector.returnConnectoin(this,SelectionKey.OP_READ);
					if(logger.isDebugEnabled()){
						logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [return_conn_to_selector] [OP_READ] ["+getStateString(state)+"] [timeout:"+(theTimeForNextSelctorTimeout - now)+"]");
					}
				}
				
				
			}
		}else if(state == CONN_STATE_WAITING_REPLY){
			WindowItem timeoutItem = null;
			Object [] items = sendMessageWindow.toArray();
			for(int i= 0 ; i < items.length ; i++){
				WindowItem wi = (WindowItem)items[i];
				if(wi.contiguousReplyTimeout >= maxReSendReplyTimes){
					sendMessageWindow.remove(wi);
					try{
						if(wi.callback != null){
							wi.callback.discardRequestMessage(this, wi.m);
						}else if(handler != null){
							this.handler.discardRequestMessage(this,wi.m);
						}
					}catch(ConnectionException e){
						
						logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [discardRequestMessage] [close_connection]",e);
						this.terminate();
						return;
					}catch(Exception e){
						logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [exception] [discardRequestMessage] [close_connection]",e);
						this.terminate();
						
					}
				}else if(wi.nextTimeout <= System.currentTimeMillis() + 5){
					sendMessageWindow.remove(wi);
					timeoutItem = wi;
					break;
				}
			}
			
			if(timeoutItem != null){
				timeoutItem.contiguousReplyTimeout ++;
				try{
					writeMessage(timeoutItem.m);
					WindowItem wi = sendMessageWindow.get(sendMessageWindow.size()-1);
					wi.contiguousReplyTimeout = timeoutItem.contiguousReplyTimeout ;
					
				}catch(Exception e){
					logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [exception] [write_timeoutItem_error:"+timeoutItem.m.getTypeDescription()+"] [close_connection]",e);
					this.terminate();
				}
				
				
			}else if(sendMessageWindow.size() > 0){
	
				if(selector.enableTimepiece){
					now = selector.timeOfTimePiece;
				}else{
					now = System.currentTimeMillis();
				}
				

				theTimeForNextSelctorTimeout = now + this.waitingResponseMessageTimeout;

				
				if(logger.isDebugEnabled()){
					logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [return_conn_to_selector] [OP_READ] ["+getStateString(state)+"] [timeout:"+(theTimeForNextSelctorTimeout - now)+"]");
				}
				this.hasCheckout = false;
				if(sendDataBuffer.position() > 0)
					selector.returnConnectoin(this,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
				else
					selector.returnConnectoin(this,SelectionKey.OP_READ);
			}else{
				if(selector.enableTimepiece){
					now = selector.timeOfTimePiece;
				}else{
					now = System.currentTimeMillis();
				}
				
				
				this.changeState(CONN_STATE_WAITING_MESSAGE);
				

				theTimeForNextSelctorTimeout = now + this.waitingRequestMessageTimeout;

				this.hasCheckout = false;
				if(sendDataBuffer.position() > 0){
					selector.returnConnectoin(this,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
					if(logger.isDebugEnabled()){
						logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [return_conn_to_selector] [OP_READ|OP_WRITE] ["+getStateString(state)+"] [timeout:"+(theTimeForNextSelctorTimeout - now)+"]");
					}
				}
				else{
					selector.returnConnectoin(this,SelectionKey.OP_READ);
					if(logger.isDebugEnabled()){
						logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [return_conn_to_selector] [OP_READ] ["+getStateString(state)+"] [timeout:"+(theTimeForNextSelctorTimeout - now)+"]");
					}
				}
			}
		}else if(state == CONN_STATE_WAITING_SEND_DATA){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [terminating] [channelSelectorTimeout] [CONN_STATE_WAITING_SEND_DATA]");
			terminate();
		}else if(state == CONN_STATE_WAITING_SEND_REPLY){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [terminating] [channelSelectorTimeout] [CONN_STATE_WAITING_SEND_REPLY]");
			terminate();
		}else if(state == CONN_STATE_WAITING_MORE_DATA){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [terminating] [channelSelectorTimeout] [CONN_STATE_WAITING_MORE_DATA]");
			terminate();
		}else{
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [terminating] [channelSelectorTimeout] ["+getStateString(state)+"]");
//			terminate();
			throw new IllegalStateException("current state ["+getStateString(state)+"]");
		}
			
	}
	
	
	/**
	 * 收到响应，查看滑动窗口，逐个删除滑动窗口中对应的消息
	 * 并产生日志，调用监听器。
	 */
	private void receiveResponseMessage(ResponseMessage message) throws ConnectionException{
		
		RequestMessage rm = null;
		MessageCallback callback = null;
		String sid = message.getSequenceNumAsString();
		if(sendMessageWindow.size() > 0){
			Iterator<WindowItem> it = sendMessageWindow.iterator();
			while(it.hasNext()){
				WindowItem wi = it.next();
				
				if(wi.m.getSequenceNumAsString().equals(sid)){
					it.remove();
					rm = wi.m;
					callback = wi.callback;
					break;
				}
			}
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [receive_response_message] [type:"+message.getTypeDescription()+"] [sid:"+message.getSequenceNumAsString()+"] [req_type:"+(rm!=null?rm.getTypeDescription():"null")+"] ["+getStateString(state)+"]");
		}
		
		try{
			if(callback != null){
				callback.receiveResponseMessage(this, rm, message);
			}else if(handler != null){
				handler.receiveResponseMessage(this,rm,message);
			}else{
				logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [handle_receive_response_mesage] [handler_is_null] [type:"+message.getTypeDescription()+"]");
			}
		}catch(ConnectionException e){
			throw e;
		}
		catch(Exception e){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [handle_receive_response_mesage] [exception]",e);
		}
	}
	
	private RequestMessage receiveTimeoutForWaitingRequestMessage() throws ConnectionException{
		RequestMessage req = null;
		try{
			 if(handler != null)
				 req = handler.waitingTimeout(this,this.waitingRequestMessageTimeout);
		}catch(ConnectionException e){
			throw e;
		}
		catch(Exception e){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [handle_receive_timeout] [exception]",e);
		}

		if(logger.isDebugEnabled()){
			logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [receive_timeout] [type:-] [sid:-] [req:"+(req!=null?req.getSequenceNumAsString():"-")+"] ["+getStateString(state)+"]");
		}
		
		return req;
	}
	
	private ResponseMessage receiveRequestMessage(RequestMessage message) throws ConnectionException{
		ResponseMessage res = null;
		try{
			if(handler != null){
				res = handler.receiveRequestMessage(this,message);
			}else{
				logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [handle_receive_request_message] [handle_is_null] [type:"+message.getTypeDescription()+"] [sid:"+message.getSequenceNumAsString()+"] ["+getStateString(state)+"]");
			}
		}catch(ConnectionException e){
			throw e;
		}
		catch(Exception e){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [handle_receive_request_message] [exception]",e);
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [receive_request_message] [type:"+message.getTypeDescription()+"] [sid:"+message.getSequenceNumAsString()+"] [res:"+(res!=null?res.getSequenceNumAsString():"-")+"] ["+getStateString(state)+"]");
		}
		
		return res;
	}
	
	/**
	 * 向对方发送ResponseMessage,如果sendDataBuffer中没有足够的空间来放ResponseMessage，
	 * 那么就丢弃这个ResponseMessage。
	 * 
	 * @param message
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void writeResponseMessage(ResponseMessage message) throws IllegalStateException,IOException{

		if(sendDataBuffer.remaining() >= message.getLength()){
			message.writeTo(sendDataBuffer);
			sendMessageNum ++;
			if(logger.isDebugEnabled()){
				logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [write_response_message] ["+message.getTypeDescription()+"] [len:"+message.getLength()+"]");
			}
		}else{
			sendDataBuffer.flip();
			int n = channel.write(sendDataBuffer);
				//没有写完的数据移到buffer的最前端
			sendDataBuffer.compact();
			
			if(logger.isDebugEnabled()){
				logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [write_data_to_socket_by_no_enough_send_buff] [n="+n+"] [position:"+sendDataBuffer.position()+"] ["+getStateString(state)+"]");
			}
			
			if(sendDataBuffer.remaining() >= message.getLength()){
				message.writeTo(sendDataBuffer);
				sendMessageNum ++;
				if(logger.isDebugEnabled()){
					logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [write_response_message] ["+message.getTypeDescription()+"] [len:"+message.getLength()+"]");
				}
			}else{
				
				if(message.getLength() > sendDataBuffer.capacity()){
					logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [discard_response_message] [response_message_too_big] ["+message.getTypeDescription()+"] [len:"+message.getLength()+"]");
				}else{
					long startTime = 0;
					if(selector.enableTimepiece){
						startTime = selector.timeOfTimePiece;
					}else{
						startTime = System.currentTimeMillis();
					}
					//
					int times = 0;
					while(true){
						times ++;
						if(times > 10) {
							logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [discard_response_message] [try_to_send_when_no_enough_send_buff] [total_retry_times:"+times+"] [total_waitting_time:"+(System.currentTimeMillis()-startTime)+"ms] ["+message.getTypeDescription()+"] [len:"+message.getLength()+"]");
							break;
						}
						try{
							Thread.sleep(100);
							sendDataBuffer.flip();
							n = channel.write(sendDataBuffer);
								//没有写完的数据移到buffer的最前端
							sendDataBuffer.compact();
							
							if(logger.isDebugEnabled()){
								logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [write_data_to_socket_by_no_enough_send_buff] [n="+n+"] [position:"+sendDataBuffer.position()+"] ["+getStateString(state)+"]");
							}
							
							if(sendDataBuffer.remaining() >= message.getLength()){
								message.writeTo(sendDataBuffer);
								sendMessageNum ++;
								if(logger.isDebugEnabled()){
									logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [write_response_message] [try_to_send_response_when_no_enough_send_buff] [total_retry_times:"+times+"] [total_waitting_time:"+(System.currentTimeMillis()-startTime)+"ms] ["+message.getTypeDescription()+"] [len:"+message.getLength()+"]");
								}
								break;
							}
						}catch(Exception e){
							logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [discard_response_message] [try_to_send_response_when_no_enough_send_buff] [total_retry_times:"+times+"] [total_waitting_time:"+(System.currentTimeMillis()-startTime)+"ms] ["+message.getTypeDescription()+"] [len:"+message.getLength()+"]",e);
							break;
						}
					}
					
					
				}
				
			}
		}
		writeSendDataBuffer(true);
	}
	
	/**
	 * 将sendDataBuffer中的数据，发送到网络中。如果发送过程中出现IOException，
	 * 这将关闭此链接。
	 * 
	 * 如果有数据发送成功，将仍然没有发送的数据块，移到sendDataBuffer的头部。
	 * 然后唤醒正在等待sendDataBuffer有更多空间的发送线程。
	 * 
	 * 如果没有将sendDataBuffer中的数据全部发送，那么链接就会进入
	 * CONN_STATE_WAITING_SEND_REPLY
	 * 或者
	 * CONN_STATE_WAITING_SEND_DATA
	 * 状态，并且设置OP_WRITE属性，将SocketChannel加入到selector中。
	 * 如果selector在上述状态超时，表示仍然Socket的sendBuffer仍然没有空间用于发送数据，
	 * 此时链接将被关闭。
	 * 
	 * 这种情况下，可能导致上述情况的发生。当有大量数据要发送时，很短的时间内，将sendDataBuffer填充满，
	 * 调用此方法，此方法将数据写入到Socket的sendBuffer中，Socket将sendBuffer中数据发给服务器，
	 * 服务器接收到消息后，向这个链接发送响应。但是由于我们不停的发送数据，如果我们处理响应的速度不够快，
	 * 会导致服务器端的Socket sendbuffer满，Socket sendbuffer满会导致服务器可能阻塞，
	 * 进而导致服务器不处理Socket receivebuffer中的数据，进而导致这个链接的Socket sendbuff满，
	 * 最终导致超时，关闭链接。
	 * 
	 * @param isReply 标识是否当前状态是否为CONN_STATE_WRITE_REPLY，
	 * @throws IllegalStateException 状态不对
	 * @throws IOException，其他IO异常，出现此异常，链接的状态将为CONN_STATE_CLOSE
	 */
	private void writeSendDataBuffer(boolean isReply) throws IllegalStateException,IOException{
		
//		if(isReply == false && state != CONN_STATE_WRITE_MESSAGE)
//			throw new IllegalStateException("current state["+getStateString(state)+"] must be ["+getStateString(CONN_STATE_WRITE_MESSAGE)+"]");
//		
//		if(isReply == true && state != CONN_STATE_WRITE_REPLY)
//			throw new IllegalStateException("current state["+getStateString(state)+"] must be ["+getStateString(CONN_STATE_WRITE_REPLY)+"]");
//		
		sendDataBuffer.flip();
		try{
			int n = channel.write(sendDataBuffer);
			//没有写完的数据移到buffer的最前端
			sendDataBuffer.compact();
			
			if(n > 0 && sendbuffFullFlag){
				sendbuffFullFlag = false;
				synchronized(this){
					notifyAll(); //唤醒正在等待的线程
				}
			}
			
			if(sendDataBuffer.position() == 0){
				if(logger.isDebugEnabled()){
					logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [write_data_to_socket] [n="+n+"] [position:"+sendDataBuffer.position()+"] ["+getStateString(state)+"]");
				}
			}else{
					logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [write_data_to_socket] [n="+n+"] [position:"+sendDataBuffer.position()+"] ["+getStateString(state)+"]");
			}
			
			long now = 0;
			if(selector.enableTimepiece){
				now = selector.timeOfTimePiece;
			}else{
				now = System.currentTimeMillis();
			}
			if(sendDataBuffer.position() > 0 && receiveDataBuffer.position() == 0 &&
					sendMessageWindow.size() == 0){ //有数据没有写完，并且receiveDataBuffer无消息要处理，并且无RequestMessage等待回应.
				if(isReply){
					changeState(CONN_STATE_WAITING_SEND_REPLY);
					this.theTimeForNextSelctorTimeout = now + this.waitingMoreSocketSendBufferTimeout;
				}else{
					changeState(CONN_STATE_WAITING_SEND_DATA);
					this.theTimeForNextSelctorTimeout = now + this.waitingMoreSocketSendBufferTimeout;
				}
				
				this.hasCheckout = false;
				selector.returnConnectoin(this,SelectionKey.OP_WRITE|SelectionKey.OP_READ);
				
				if(logger.isDebugEnabled()){
					logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [return_conn_to_selector] [OP_READ|OP_WRITE] ["+getStateString(state)+"] [timeout:"+(theTimeForNextSelctorTimeout - now)+"]");
				}

			}else{
				if(isReply){
					if(this.receiveDataBuffer.position() > 0){ //receiveDataBuffer有消息需要处理
						handleData(false);
						return;
					}else if(sendMessageWindow.size() == 0){    //没有RequestMessage等待ResponseMessage
						changeState(CONN_STATE_WAITING_MESSAGE);
						this.theTimeForNextSelctorTimeout = now + this.waitingRequestMessageTimeout;
					}
					else{
						changeState(CONN_STATE_WAITING_REPLY); //有RequestMessage等待ResponseMessage
						this.theTimeForNextSelctorTimeout = getNearTimeoutOfWindow();
					}
				}else{
					if(sendMessageWindow.size() == 0){
						changeState(CONN_STATE_WAITING_MESSAGE);
						this.theTimeForNextSelctorTimeout = now + this.waitingRequestMessageTimeout;
					}else{
						changeState(CONN_STATE_WAITING_REPLY);
						this.theTimeForNextSelctorTimeout = getNearTimeoutOfWindow();
					}
				}
				
				
				this.hasCheckout = false;
				
				if(sendDataBuffer.position() > 0){
					selector.returnConnectoin(this,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
					if(logger.isDebugEnabled()){
						logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [return_conn_to_selector] [OP_READ|OP_WRITE] ["+getStateString(state)+"] [timeout:"+(theTimeForNextSelctorTimeout - now)+"]");
					}
				}
				else{
					selector.returnConnectoin(this,SelectionKey.OP_READ);
					if(logger.isDebugEnabled()){
						logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [return_conn_to_selector] [OP_READ] ["+getStateString(state)+"] [timeout:"+(theTimeForNextSelctorTimeout - now)+"]");
					}
				}
			}
		}catch(IOException e){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [writeSendDataBuffer] [io_exception_and_close_connection] ["+getStateString(state)+"]",e);
			terminate();
			throw e;
		}
	}
	
	private Object notifyTermimateLock = new Object();
	private boolean notidyTerminateFlag = false;
	
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
		
		logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [connection_force_to_terminate] ["+reason+"]");

		if(terminateHandler != null){
			Runnable runnable = new Runnable(){
				public void run(){
					if(terminateHandler != null){
						List<RequestMessage> l = new LinkedList<RequestMessage>();
						Iterator<WindowItem> it = sendMessageWindow.iterator();
						while(it.hasNext()){
							WindowItem wi = it.next();
							l.add(wi.m);
						}
						if(receiveDataBuffer != null)
							receiveDataBuffer.flip();
						
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
							terminateHandler.ternimate(Connection.this,l,receiveDataBuffer);
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
	}
	
	/**
	 * 此方法由线程池中的线程来关闭连接
	 */
	protected void terminate(){
		state = CONN_STATE_CLOSE;
		synchronized(this){
			notifyAll();
		}
		
		try{
			channel.close();
		}catch(Exception e){
			logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"],close channel exception:",e);

		}
		
		logger.warn("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [connection_terminate]");
		
		
		if(terminateHandler != null){
			List<RequestMessage> l = new LinkedList<RequestMessage>();
			Iterator<WindowItem> it = sendMessageWindow.iterator();
			while(it.hasNext()){
				WindowItem wi = it.next();
				l.add(wi.m);
			}
			if(receiveDataBuffer != null)
				receiveDataBuffer.flip();

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
				terminateHandler.ternimate(this,l,receiveDataBuffer);
			}
		}
		
		receiveDataBuffer = null;
		sendDataBuffer = null;
	}
	/**
	 * 链接的状态将发生变化，此方法检查状态变化的合法性，
	 * 如果状态变化不合法，则抛出StateInvalidChangedException，原状态保持不变。
	 * 
	 * @param newState
	 * @throws StateInvalidChangedException
	 */
	protected void changeState(int newState) throws StateInvalidChangedException{
		
		if( newState == CONN_STATE_CLOSE){
			if(logger.isDebugEnabled()){
				logger.debug("["+Thread.currentThread().getName()+","+selector.getName()+","+name+"] [state_change] ["+getStateString(state)+"] ["+getStateString(newState)+"]");
			}
			this.state = newState;
			return;
		}
		
		if(state == CONN_STATE_CLOSE && newState != CONN_STATE_CLOSE){
			throw new StateInvalidChangedException(getStateString(state) + "->" + getStateString(newState));
		}
//		switch(state){
//		case CONN_STATE_UNKNOWN:
//			if(newState != CONN_STATE_OPEN)
//				throw new StateInvalidChangedException(getStateString(state) + "->" + getStateString(newState));
//			break;
//		case CONN_STATE_OPEN:
//			if(newState != CONN_STATE_CONNECT && newState != CONN_STATE_WAITING_MESSAGE && newState != CONN_STATE_CLOSE)
//				throw new StateInvalidChangedException(getStateString(state) + "->" + getStateString(newState));
//			break;
//		case CONN_STATE_CONNECT:
//			if(newState != CONN_STATE_WAITING_MESSAGE)
//				throw new StateInvalidChangedException(getStateString(state) + "->" + getStateString(newState));
//			break;
//		case CONN_STATE_WAITING_MESSAGE:
//			if(newState != CONN_STATE_WRITE_MESSAGE && newState != CONN_STATE_HANDLE_DATA && newState != CONN_STATE_WRITE_REPLY)
//				throw new StateInvalidChangedException(getStateString(state) + "->" + getStateString(newState));
//			break;
//		case CONN_STATE_WRITE_MESSAGE:
//			if(newState != CONN_STATE_WAITING_REPLY && newState != CONN_STATE_WAITING_MESSAGE && newState != CONN_STATE_CLOSE && newState != CONN_STATE_WAITING_SEND_DATA)
//				throw new StateInvalidChangedException(getStateString(state) + "->" + getStateString(newState));
//			break;
//		case CONN_STATE_WRITE_REPLY:
//			if(newState != CONN_STATE_WAITING_MESSAGE && newState != CONN_STATE_CLOSE 
//					&& newState != CONN_STATE_WAITING_SEND_REPLY && newState != CONN_STATE_WAITING_REPLY 
//					&& newState != CONN_STATE_HANDLE_DATA)
//				throw new StateInvalidChangedException(getStateString(state) + "->" + getStateString(newState));
//			break;
//		case CONN_STATE_WAITING_REPLY:
//			if(newState != CONN_STATE_WRITE_MESSAGE && newState != CONN_STATE_HANDLE_DATA && newState != CONN_STATE_CLOSE && newState != CONN_STATE_WAITING_MESSAGE)
//				throw new StateInvalidChangedException(getStateString(state) + "->" + getStateString(newState));
//			break;
//		case CONN_STATE_HANDLE_DATA:
//			if(newState != CONN_STATE_WAITING_MESSAGE && newState != CONN_STATE_WRITE_REPLY 
//					&& newState != CONN_STATE_WAITING_MORE_DATA && newState != CONN_STATE_CLOSE && newState != CONN_STATE_WAITING_REPLY)
//				throw new StateInvalidChangedException(getStateString(state) + "->" + getStateString(newState));
//			break;
//		case CONN_STATE_WAITING_MORE_DATA:
//			if(newState != CONN_STATE_HANDLE_DATA && newState != CONN_STATE_CLOSE)
//				throw new StateInvalidChangedException(getStateString(state) + "->" + getStateString(newState));
//			break;
//		case CONN_STATE_CLOSE:
//			if(newState != CONN_STATE_CLOSE)
//				throw new StateInvalidChangedException(getStateString(state) + "->" + getStateString(newState));
//			break;
//		case CONN_STATE_WAITING_SEND_REPLY:
//			if(newState != CONN_STATE_CLOSE && newState != CONN_STATE_WRITE_REPLY && newState != CONN_STATE_HANDLE_DATA)
//				throw new StateInvalidChangedException(getStateString(state) + "->" + getStateString(newState));
//			break;
//		case CONN_STATE_WAITING_SEND_DATA:
//			if(newState != CONN_STATE_CLOSE && newState != CONN_STATE_WRITE_MESSAGE && newState != CONN_STATE_HANDLE_DATA)
//				throw new StateInvalidChangedException(getStateString(state) + "->" + getStateString(newState));
//			break;
//		default:
//			throw new IllegalStateException("current state["+getStateString(state)+"] invalid.");	
//		}

		if(logger.isDebugEnabled()){
			logger.debug("["+Thread.currentThread().getName()+","+name+"] [state_change] ["+getStateString(state)+"] ["+getStateString(newState)+"]");
		}

		state = newState;
		
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
		case CONN_STATE_WAITING_MESSAGE:
			str = "CONN_STATE_WAITING_MESSAGE";
			break;
		case CONN_STATE_WRITE_MESSAGE:
			str = "CONN_STATE_WRITE_MESSAGE";
			break;
		case CONN_STATE_WRITE_REPLY:
			str = "CONN_STATE_WRITE_REPLY";
			break;
		case CONN_STATE_WAITING_REPLY:
			str = "CONN_STATE_WAITING_REPLY";
			break;
		case CONN_STATE_HANDLE_DATA:
			str = "CONN_STATE_HANDLE_DATA";
			break;
		case CONN_STATE_WAITING_MORE_DATA:
			str = "CONN_STATE_WAITING_MORE_DATA";
			break;
		case CONN_STATE_CLOSE:
			str = "CONN_STATE_CLOSE";
			break;
		case CONN_STATE_WAITING_SEND_DATA:
			str = "CONN_STATE_WAITING_SEND_DATA";
			break;
		case CONN_STATE_WAITING_SEND_REPLY:
			str = "CONN_STATE_WAITING_SEND_REPLY";
			break;	
		}
		return str;
	}
	
	/**
	 * 滑动窗口中的等待ResponseMessage的RequestMessage
	 * 
	 * @author myzdf
	 *
	 */
	private static class WindowItem{
		
		RequestMessage m;
		MessageCallback callback = null;
		//等待ResponseMessage的超时时间
		long nextTimeout = 0L;
		
		//重发的次数
		int contiguousReplyTimeout = 0;
		
		public WindowItem(RequestMessage m){
			this.m = m;
		}
	}
}

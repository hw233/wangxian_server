package com.xuanzhi.tools.dbpool;

import java.sql.*;
import java.util.*;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;


import org.apache.log4j.*;

/**
 * Individual pool associated with a JDBC datasource and username.  Each pool
 * is identified by an alias.  Use the alias name to acquire a ref to a
 * Connection from the pool.  See the ConnectionPoolManager for further
 * details on how to do this.
 * 
 * 原来的代码对同步的处理比较随意，大范围的代码同步可能带来僵持甚至死锁，比如
 * 当数据库操作慢（创建连接和执行SQL）时，创建连接的线程占用同步锁，长时间不
 * 释放，导致归还连接的线程阻塞，也就是说出现了想还还不掉，想借借不出的现象。
 * 优化后的代码在同步的处理上更加细致，通过加入复杂的控制，把不需要同步的代码
 * 解放出来，而只在必要的代码上进行同步；同时更改了连接池的存储结构，采用两个
 * 链表存放空闲和正在使用的连接。空闲链表是先进先出的，这样有利于把查询任务相
 * 对均衡的分配给所有连接。
 *
 * 但优化并不能解决根本问题，要从根本上保证系统效率，需要对SQL语句和数据库使用
 * 频度进行优化。
 * 
 * @see ConnectionPoolManager
 */
public class ConnectionPool 
{
	public static ArrayList<ConnectionPool> M_POOLLIST = new ArrayList<ConnectionPool>();
	
	public static boolean m_bDebug = false;
	public static final String DEBUG_LOG_DESC = "debug.com.airinbox.dbpool.ConnectionPool";
	
	// log4j
	private static Category cat = Category.getInstance(ConnectionPool.class.getName());
	
    // the connection vectors
	Vector closeVector;
	
	/**
	 * m_oIdleConnList 存放空闲的 Connection，正在被使用的链接则存放在 m_oBusyConnList 中。
	 * Connection 从 m_oIdleConnList checkout 后被放进 m_oBusyConnList 中，使用完毕之后再
	 * 从 m_oBusyConnList 转移到 m_oIdleConnList 中。
	 *
	 * m_oIdleConnList 实现了先进先出的队列功能，这样做有利于基本上均衡的使用各个连接，
	 * 同时在清理空闲过久连接的操作中，只需检查队首的部分连接并释放即可。
	 * 
	 * m_oBusyConnList 也是按照进入时间顺序排列，清理超时连接的操作中，只需检查列表首的部分
	 * 连接即可。
	 * 
	 * 这两者不是线程安全的，使用时通过外部同步保证数据状态的一致性。
	 */
	private LinkedList m_oIdleConnList, m_oBusyConnList;
	
	/**
	 * 用户 m_iCurSize 更新的同步 monitor
	 */
	private final Integer SIZE_SYNC_FLAG = new Integer(0);
	
	protected int m_iCurSize = 0;
	protected int m_iPeakSize = 0;
	private java.util.Date m_oCreateTime;

	//JDBC URL to connect to
    String url;
    
    // username passord and the pool's alias
	private String username, password, alias;
	
	private Properties props ; 
	
	private static int DUMP_LIMIT;
		
	//
    private int maxConn, timeoutSeconds, checkoutSeconds, numCheckoutTimeout, numIdleTimeout;
    
    private int m_iIdlePoolCursor = 0;
    private int m_iPreferredIdleTime = 100;
	
	//
    private int numRequests, numWaits, maxCheckout;
	//
    private boolean cacheStatements;
  
    /** Number of times connections within the pool have been found 
     *  to be closed 
     */
    int numConnectionFaults = 0;
	//
    private int prefetchSize = -1;

    /**
     * observer provides an interface for connection pool callbacks
     */
    private Vector observers = new Vector();


    private ConnectionValidator validator;
	
	private Driver driver;
	
    /**
     * Creates a Connection pool with no maxCheckout parameter.
     *
     * @param alias Name of the pool
     * @param url JDBC URL to connect to
     * @param username JDBC username to connect as
     * @param password username's password in the database
     * @param maxConn Maximum number of connections to open; When this limit
     *                is reached, threads requesting a connection are queued
     *                until a connection becomes available
     * @param timeoutSeconds Maximum number of seconds a Connection can go
     *                       unused before it is closed
     * @param checkoutSeconds Maximum number of seconds a Thread can checkout a
     *                        Connection before it is closed and returned to the
     *                        pool.  This is a protection against the Thread
     *                        dying and leaving the Connection checked out
     *                        indefinately
     */
    public ConnectionPool(Driver driver,String alias,String url, String username, String password,int maxConn, int timeoutSeconds, int checkoutSeconds) 
	{
        this(driver,alias, url, username, password, maxConn, timeoutSeconds,checkoutSeconds, 0);
    }

    /**
     * driver.gets a Connection pool
     *
     * @param alias Name of the pool
     * @param url JDBC URL to connect to
     * @param username JDBC username to connect as
     * @param password username's password in the database
     * @param maxConn Maximum number of connections to open; When this limit
     *                is reached, threads requesting a connection are queued
     *                until a connection becomes available
     * @param timeoutSeconds Maximum number of seconds a Connection can go
     *                       unused before it is closed
     * @param checkoutSeconds Maximum number of seconds a Thread can checkout a
     *                        Connection before it is closed and returned to the
     *                        pool.  This is a protection against the Thread
     *                        dying and leaving the Connection checked out
     *                        indefinately
     * @param maxCheckout If this is greater than 0, the number of times a
     *                    Connection may be checked out before it is closed.
     *                    Used as a safeguard against cursor leak, which occurs
     *                    if you don't call ResultSet.close() and
     *                    Statement.close()
     */
    public ConnectionPool(Driver driver,String alias,String url, String username, String password,int maxConn, int timeoutSeconds, int checkoutSeconds,int maxCheckout) 
	{
        this.timeoutSeconds = timeoutSeconds;
        this.checkoutSeconds = checkoutSeconds;

        this.alias = alias;
        this.url = url;
        this.username = username;
        this.password = password;
        this.maxConn = maxConn;
        this.maxCheckout = maxCheckout;

        this.numRequests = 0;
        this.numWaits = 0;
        this.numCheckoutTimeout = 0;
		this.numIdleTimeout = 0;
		
		this.driver = driver;
		
		this.m_oCreateTime = new java.util.Date();
		
		props = new Properties(); 
		
		props.put("user",this.username);
		props.put("password",this.password);

        m_oIdleConnList = new LinkedList();
        m_oBusyConnList = new LinkedList();
        
        closeVector     = new Vector(maxConn);
		
        cacheStatements = false;
        
        M_POOLLIST.add(this);
    }

	public void setDumpLimit(int l)
	{
		DUMP_LIMIT = l;
	}
	
	/**
	 * 可通过设置期望空闲时间控制连接在连接池中的驻留时间，从而达到控制
	 * 空闲连接数量的效果
	 */
	public void setPreferredIdleTime(int iPreferredIdleTime)
	{
		if (iPreferredIdleTime < 0)
		{
			iPreferredIdleTime = 0;
		}
		else if (iPreferredIdleTime > 1000)
		{
			iPreferredIdleTime = 1000;
		}
		
		m_iPreferredIdleTime = iPreferredIdleTime;
	}
	
    /**
     *  add one observer to observe one connection return to pool
     */
    public void addObserver(PoolObserver po) {
        observers.add(po);
    }
	/**
	 *  remove one observer
	 */
    public void removeObserver(PoolObserver po) {
        observers.remove(po);
    }
	/**
	 * remove all observers
	 */
    public void removeObservers() {
        observers.removeAllElements();
    }
	/**
	 *  get all observers
	 */
    public Enumeration getObservers() {
        return observers.elements();
    }

	/**
	 * 
	 */
    public ConnectionValidator getValidator() {
        return validator;
    }
	
    /**
     * 
     */
    public void setValidator(ConnectionValidator validator) {
        this.validator = validator;
    }

    /**
     * If set to true, connections in the pool will reuse Statements and
     * PreparedStatements.  If set to false, connections in the pool will
     * proxy calls to createStatement() and prepareStatement() straight through
     * to the driver.
     * <p>
     * Set this to false if you want to run multiple Statements over the same
     * Connection.
     * <p>
     * This is set to 'true' by default.
     */
    public void setCacheStatements(boolean cacheStmts) {
        this.cacheStatements = cacheStmts;
    }

    /**
     * Returns whether connections in this pool will cache Statement and
     * PreparedStatement objects.
     */
    public boolean getCacheStatements() {
        return cacheStatements;
    }

    /**
     * Returns the alias for this pool.  This name is defined by the user in
     * the constructor
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Returns the number of times a Connection has been checked out from the
     * pool
     */
    public int getNumRequests() {
        return numRequests;
    }

    /**
     * Returns the number of times a thread has had to block on wait() as a
     * result of all PooledConnections being in use.  Useful diagnostic tool to
     * see if your pool needs more nodes (which could require a database
     * license upgrade if you're running Oracle for instance)
     */
    public int getNumWaits() {
        return numWaits;
    }

    /**
     * Returns the number of times a Connection has been closed by the
     * reapIdleConnections() method due to being checked out for longer than
     * the checkoutSeconds interval.
     * <p>
     * If this is greater than 0 it means that
     * you either have queries that take longer to execute than the
     * checkoutSeconds interval allows, or it means that you are forgetting to
     * call conn.close() somewhere in your application.  Both conditions are
     * undesirable, but they have different solutions.  In the latter case either
     * tune your query to execute more quickly, or increase the checkoutSeconds
     * parameter to the pool.  In the former case you simply need to find the
     * code that calls DriverManager.getConnection() but not conn.close()
     */
    public int getNumCheckoutTimeouts() {
        return numCheckoutTimeout;
    }

    /**
     * Returns the maximum number of connections this pool can open
     */
    public int getMaxConn() {
        return maxConn;
    }

    /**
     * Returns the current number of Connections in the pool.
     */
    public int size()
    {
        return m_iCurSize;
    }
	
    public int getIdleSize(){
    	return this.m_oIdleConnList.size();
    }
    
	public int getPeakSize()
	{
		return m_iPeakSize;
	}
	
	public java.util.Date getCreateTime()
	{
		return m_oCreateTime;
	}
		
	public int getNumOfCachedStatement(){
		int c = 0;
		synchronized (m_oIdleConnList)
		{
			Iterator it = m_oIdleConnList.iterator();
			while(it.hasNext()){
				PooledConnection pc = (PooledConnection)it.next();
				c += pc.prepStmts.size();
				if(pc.theStatement != null) c++;
			}
		}
        
		synchronized (m_oBusyConnList)
		{
			Iterator it = m_oBusyConnList.iterator();
			while(it.hasNext()){
				PooledConnection pc = (PooledConnection)it.next();
				c += pc.prepStmts.size();
				if(pc.theStatement != null) c++;
			}
		}
		return c;
	}
    /**
     * Check  all connections to make sure they haven't:
     *   1) gone idle for too long<br>
     *   2) been checked out by a thread for too long (cursor leak)
     */
    public void reapIdleConnections()
    {
        long now = System.currentTimeMillis();
        long idleTimeout = now - (timeoutSeconds * 1000);
		long checkoutTimeout = now - (checkoutSeconds * 1000);
		boolean bReaped = false;
		
		// 清理空闲过久的连接
		synchronized (m_oIdleConnList)
		{
			while (m_oIdleConnList.size() > 0)
			{
				PooledConnection conn = (PooledConnection)m_oIdleConnList.getFirst();
				if (conn.getLastCheckin() < idleTimeout)
				{
					m_oIdleConnList.removeFirst();
					
					++numIdleTimeout;
					bReaped = true;
					removeConnection(conn);
				}
				else
				{
					// 后续的连接空闲时间更短，无需再检查
					break;
				}
			}
		}
        
		// 清理 checkout 超时的连接
		synchronized (m_oBusyConnList)
		{
			while (m_oBusyConnList.size() > 0)
			{
				PooledConnection conn = (PooledConnection)m_oBusyConnList.getFirst();
				if (conn.getLastCheckout() < checkoutTimeout)
				{
					m_oBusyConnList.removeFirst();
					
					cat.info("[" + alias + "] [idle/all/max:"+m_oIdleConnList.size()+"/"+size()+"/"+this.maxConn+"] [" + Thread.currentThread().getName() + "] Warning: found checkout timed-out connection\n" + conn.dumpRecentInfo());
					
					++numCheckoutTimeout;
					bReaped = true;
					removeConnection(conn);
				}
				else
				{
					// 后续的连接使用时间更短，无需再检查
					break;
				}
			}
		}
		
		// 如果连接被释放，连接池有多余的空间，唤醒 wait 的线程（当连接池已满且无空闲连接时，
		// 线程将等待连接被归还，或可以创建新的连接）。
		if (bReaped)
		{
			synchronized (m_oIdleConnList)
			{
				m_oIdleConnList.notifyAll();
			}
			
	        // Try to close the actual connections -- this part doesn't need to be
	        // synchronized because we're only operating on the closeVector
	        closeConnections();
		}

        cat.info("[" + alias + "] [" + Thread.currentThread().getName() + "] reapIdleConnections() finished in " + (System.currentTimeMillis() - now) + "ms");
    }

    /**
     * Removes all connections from the pool and calls close() on them.  It
     * squelches any SQLExceptions that might results from close().  That's
     * probably not ideal.
     */
    public void removeAllConnections()
    {
        // FIXME: what do we do when a connection is in progress.  Maybe we
        // pass a boolean to this method that tells us whether to shutdown
        // immediately, or to close connections as they are returned..
        // for now we'll just close 'em
        cat.info("[" + alias + "] [" + Thread.currentThread().getName() + "] removeAllConnections() called");
		
		
		synchronized (m_oIdleConnList)
		{
			while (m_oIdleConnList.size() > 0)
			{
				PooledConnection conn = (PooledConnection)m_oIdleConnList.removeFirst();
				removeConnection(conn);
			}
			
			m_oIdleConnList.notifyAll();
		}
        
		synchronized (m_oBusyConnList)
		{
			while (m_oBusyConnList.size() > 0)
			{
				PooledConnection conn = (PooledConnection)m_oBusyConnList.removeFirst();
				removeConnection(conn);
			}
		}
		
		closeConnections();
    }

    /**
     * Calls removeConnection(conn, false).  Won't close the connection
     * immediately.
     */
    private void removeConnection(PooledConnection conn)
    {
        removeConnection(conn, false);
    }

    /**
     * Removes the connection from the Vector of active connections.  If
     * closeConn is true, the actual JDBC Connection object is closed 
     * immediately.  Otherwise the Connection is placed on a Vector of
     * Connections to close later asynchronously by reapIdleConnections()
     */
    private void removeConnection(PooledConnection conn, boolean closeConn)
    {
    	synchronized (SIZE_SYNC_FLAG)
    	{
    		// 减少了连接，对 m_iCurSize 的修改保证在 SIZE_SYNC_FLAG 同步代码块内
			--m_iCurSize;
		}
		
        if (closeConn)
        {
            closeConnection(conn);
        }
        else
        {
            closeVector.addElement(conn);
        }
    }

    private void closeConnections()
    {  
        Vector closeTmp = (Vector)closeVector.clone();
        for (int i = 0; i < closeTmp.size(); i++)
        {
            PooledConnection conn = (PooledConnection)closeTmp.elementAt(i);
            closeConnection(conn);
        }
    }

    private void closeConnection(PooledConnection conn) 
	{
        //
        // I've had problems with Oracle drivers that resulted in a 
        // deadlock when trying to close a connection.
        //
        // So we'll set a timeout of 10 seconds on this operation.  if the
        // close doesn't complete within this interval, we'll return 
        // without removing the connection.
        //
        // This will hopefully still allow us to trap cases where buggy
        // application code fails to return the Connection back to the 
        // pool, but won't stop a query that's in the middle of running on 
        // some drivers (Oracle).  With some MySQL drivers, this will 
        // interrupt a query in progress.
        //
        try
        {
            // remove the connection in all cases
            closeVector.removeElement(conn);
	    
            JavaAlarm alarm = new JavaAlarm(conn, 10000);
			
			cat.debug("[" + alias + "] [idle/all/max:"+m_oIdleConnList.size()+"/"+size()+"/"+this.maxConn+"] [" + Thread.currentThread().getName() + "] close one native connection \n" +conn.dumpInfo()); 
        }
        catch(TimeoutException e)
        {
            // we were unable to close the connection within 10 seconds
			 cat.error("close connection timeout",e); 
			
        }
    }


    /**
     * Returns a connection for the pool.  There are three ways this can happen:
     *   <ol>
     *     <li>If a connection in the pool is free, it is locked and returned
     *         immediately
     *     <li>If no connections are free, but the pool is not full, a new
     *         Connection is opened and returned
     *     <li>If no connections are free, and the pool is full, the thread
     *         calls wait() and blocks until a connection is returned
     *   </ol>
     */
    public Connection getConnection() throws SQLException
    {
        ++numRequests;
        
        for (int iLoopTimes = 0; true; iLoopTimes++)
        {
            PooledConnection pc = null;
            
            /**
			 * 无空闲连接，且连接池未满，则创建并初始化新的 Connection 放入连接池中
			 * 为防止某些点背的线程总是得不到连接，设置循环次数阈值，超过阈值，即便
			 * 有空闲连接，也将可能创建新的连接（取决于连接池是否已满）
			 */
            if ((iLoopTimes > 9 || m_oIdleConnList.size() == 0) && size() < maxConn)
            {
	            cat.debug("[" + alias + "] [idle/all/max:"+m_oIdleConnList.size()+"/"+size()+"/"+this.maxConn+"] [" + Thread.currentThread().getName() + "] all connections["+size()+"] locked. Create connection if could");
				
            	synchronized (SIZE_SYNC_FLAG)
            	{
            		if ((iLoopTimes > 9 || m_oIdleConnList.size() == 0) && size() < maxConn)
            		{
		                long l = System.currentTimeMillis(); 
		                
		                Connection conn = null;
		                try
		                {
		                	conn = createDriverConnection();
		                }
		                catch(SQLException e)
		                {
							cat.debug("[" + alias + "] [" + Thread.currentThread().getName() + "] getConnection() failed and cost " + ((System.currentTimeMillis() - l) * 1.0f / 1000) + " seconds with exception", e);
							
		                	throw e;
		                }
		
		                if (prefetchSize != -1)
		                {
		                    //if (conn instanceof oracle.jdbc.driver.OracleConnection)
		                    //{
		                     //   ((oracle.jdbc.driver.OracleConnection)conn).setDefaultRowPrefetch(prefetchSize);
		                    //}
		                	try{
		                		Method method = conn.getClass().getMethod("setDefaultRowPrefetch",new Class[]{Integer.TYPE});
		                		method.invoke(conn,new Object[]{prefetchSize});
		                	}catch(Exception ex){
		                		
		                	}
		                }
		                
		                pc = new PooledConnection(conn, this);
		                
		                // 新增了连接，对 m_iCurSize 的修改保证在 SIZE_SYNC_FLAG 同步代码块内
	                	++m_iCurSize;
	                	
	                	if (m_iPeakSize < m_iCurSize)
	                	{
	                		m_iPeakSize = m_iCurSize;
	                	}
	                	
						cat.debug("[" + alias + "] [idle/all/max:"+m_oIdleConnList.size()+"/"+size()+"/"+this.maxConn+"] [" + Thread.currentThread().getName() + "] getConnection() called and return new connection[id="+pc.getID()+"] cost " + ((System.currentTimeMillis() - l) * 1.0f / 1000) + " seconds");
		            }
				}
			}
			
			// 如果创建连接成功，失败意味着 连接池已满 *或* 有空闲连接可用
			if (pc != null)
			{
				pc.getLock();
				return releaseConnection(pc);
			}
			
			/**
			 * 从连接池中取出连接，由于 m_oIdleConnList 中连接的排列顺序是按照 Last Checkin Time
			 * 由早到晚，根据 m_iIdlePoolCursor 先后查找一个空闲时间接近 m_iPreferredIdleTime 的连接
			 * 如果没有，则取 m_iIdlePoolCursor 之前最近的一个连接。
			 * 此算法将尽量保证每一个连接的池中驻留时间不超过驻留时间阈值，通过控制取连接的位置使
			 * 列表首部的连接 idle timeout 而被 reapIdleConnections() 清理掉。
			 */
			if (m_oIdleConnList.size() > 0)
			{
				synchronized (m_oIdleConnList)
				{
					int iLength = m_oIdleConnList.size();
					
					if (iLength > 0)
					{
						// 检查上次位置为中心的三个连接（如果都存在），选出一个离期望时间最近的。
						int iIndex = Math.min(m_iIdlePoolCursor, iLength - 1);
						
						long curTime = System.currentTimeMillis();
						
						PooledConnection tmpPC = (PooledConnection)m_oIdleConnList.get(iIndex);
						int iMiddleIdleTimeOff = (int)(curTime - tmpPC.getLastCheckin() - m_iPreferredIdleTime);
						
						int iLeftIdleTimeOff = Integer.MAX_VALUE;
						int iRightIdleTimeOff = Integer.MAX_VALUE;
						
						// 左边有元素
						if (iIndex > 0)
						{
							tmpPC = (PooledConnection)m_oIdleConnList.get(iIndex - 1);
							iLeftIdleTimeOff = (int)(curTime - tmpPC.getLastCheckin() - m_iPreferredIdleTime);
						}
						
						// 右边有元素
						if (iIndex < iLength - 1)
						{
							tmpPC = (PooledConnection)m_oIdleConnList.get(iIndex + 1);
							iRightIdleTimeOff = (int)(curTime - tmpPC.getLastCheckin() - m_iPreferredIdleTime);
						}
						
						if (Math.abs(iLeftIdleTimeOff) < Math.abs(iMiddleIdleTimeOff))
						{
							// 左移一个位置
							--iIndex;
						}
						else if (Math.abs(iRightIdleTimeOff) < Math.abs(iMiddleIdleTimeOff))
						{
							// 右移一个位置
							++iIndex;
						}
						else if (iIndex > 0 && iMiddleIdleTimeOff < 0 && Math.abs(iMiddleIdleTimeOff) > m_iPreferredIdleTime / 2)
						{
							// 如果左边连接空闲太长时间，将没有机会向左游动，此处使有可能向左游动
							--iIndex;
						}
						
						m_iIdlePoolCursor = iIndex;
						
						pc = (PooledConnection)m_oIdleConnList.remove(m_iIdlePoolCursor);
						
						cat.debug("[" + alias + "] [idle/all/max:"+m_oIdleConnList.size()+"/"+size()+"/"+this.maxConn+"] [" + Thread.currentThread().getName() + "] getConnection() called and return pooled connection[id="+pc.getID()+"] at ["+m_iIdlePoolCursor+"/"+iLength+"]");
					}
				}
			}
			
			// 如果尚有可用空闲连接，直接返回
			if (pc != null)
			{
				pc.getLock();
				return releaseConnection(pc);
			}
			else
			{
	            //
	            // If you got here, then there are no free connections in the pool and the number
	            // of connections in the pool is already at the maximum.  The only thing we can do
	            // now is to wait until one is returned.
	            //
	
	            // Wait until a Connection is returned
                cat.warn("[" + alias + "] [idle/all/max:"+m_oIdleConnList.size()+"/"+size()+"/"+this.maxConn+"] [" + Thread.currentThread().getName() + "] pool is full.  calling wait(1000ms)");

                numWaits++;
                
                synchronized (m_oIdleConnList)
                {
		            try
		            {
	                	m_oIdleConnList.wait(1000);
		            }
		            catch(InterruptedException e) { }
                }

	            cat.warn("[" + alias + "] [idle/all/max:"+m_oIdleConnList.size()+"/"+size()+"/"+this.maxConn+"] [" + Thread.currentThread().getName() + "] awoken from wait().  trying to grab" + " an available connection");
			}
        }
    }

    /**
     * This is a hook for doing things when you need to release a connection from
     * the pool.  (This might be a dumb idea, but it seemed better than cutting and
     * pasting the same code before each return statement in getConnection() --Orion).
     */
    private Connection releaseConnection(PooledConnection connection)
    {
    	synchronized (m_oBusyConnList)
    	{
    		m_oBusyConnList.add(connection);
    	}
    	
   		return connection;
    }
  
    /** Build a connection to the database using the appropriate database
     * driver.
     * @return a connection to the database
     */
    Connection createDriverConnection() throws SQLException
    {
        return  driver.connect(url,props);
    }

    public void returnConnection(PooledConnection conn)
    {
    	synchronized (m_oBusyConnList)
    	{
    		if (!m_oBusyConnList.remove(conn))
    		{
    			// m_oBusyConnList 中并不包含 conn，可能此连接已经因为超时被定期清理程序清除
    			cat.warn("[" + alias + "] [" + Thread.currentThread().getName() + "] connection["+conn.getID()+"] not exists in busy connection list while return to pool.");
    			return;
    		}
    	}
    	
    
    	try{
    		// call the observers' callbacks
	        Enumeration enum2 = getObservers();
	        while (enum2.hasMoreElements()){
	            PoolObserver po = (PoolObserver)enum2.nextElement();
		        po.connectionReturned(conn);
	        }
    	}catch(Throwable e){
	   		 cat.warn("[" + alias + "] [" + Thread.currentThread().getName() + "] connection["+conn.getID()+"] observer failed cause exception." + " closing it.",e);
			 removeConnection(conn, true);
			 synchronized (m_oIdleConnList){
					// Wake up any waiting threads
					m_oIdleConnList.notifyAll();
			}
			 return;
        }

    	try{
			long l = System.currentTimeMillis() - conn.getLastCheckout() ;
			if(cat.isDebugEnabled())
				cat.debug ("[" + alias + "] [idle/all/max:"+m_oIdleConnList.size()+"/"+size()+"/"+this.maxConn+"] [" + Thread.currentThread().getName() + "] connection["+conn.getID()+"] use "+(l*1.0f/1000)+" seconds and releasing lock and return to pool.");
			if (l > DUMP_LIMIT)
			{
				try{
					throw new Exception("数据库连接使用超时");
				}catch(Exception e){
					cat.warn("[" + alias + "] [" + Thread.currentThread().getName() + "] " + conn.dumpRecentInfo(),e);
				}
			}
    	}catch(Throwable e){
    		cat.error("unknown error:",e);
    	}
		
		if (maxCheckout > 0 && conn.getCheckoutCount() >= maxCheckout)
		{
			try{
				// 连接已经使用了太多次，将被关闭，无需归还到 m_oIdleConnList 中
				cat.info("[" + alias + "] [idle/all/max:"+m_oIdleConnList.size()+"/"+size()+"/"+this.maxConn+"] [" + Thread.currentThread().getName() + "] connection["+conn.getID()+"] checked out max # of times." + " closing it.");
				removeConnection(conn, true);
				
				synchronized (m_oIdleConnList)
				{
					// Wake up any waiting threads
					m_oIdleConnList.notifyAll();
				}
			}catch(Throwable e){
				cat.error("unknown error:",e);
			}
		}
		else
		{
			// 连接将被归还到 m_oIdleConnList 中
			synchronized (m_oIdleConnList)
			{
				conn.releaseLock();
				
				m_oIdleConnList.add(conn);
				
				// Wake up any waiting threads
				m_oIdleConnList.notifyAll();
			}
		}
    }


    /**
     * Dump some information about all connections
     *
     */
    public String dumpInfo()
    {
        String LS = System.getProperty("line.separator");
        String report = "\tPool: " + this.toString() + LS;
        report += "\t\tAlias: " + this.getAlias()+ LS;
        report += "\t\tCreate time: " + this.getCreateTime()+ LS;
        report += "\t\tMax  size: " + getMaxConn()+ LS;
        report += "\t\tPeak size: " + getPeakSize() + LS;
        report += "\t\tCur  size: " + size() + LS;
        report += "\t\tUse ratio: " + (m_oBusyConnList.size() * 100.0 / size()) + "%" +  LS;
        report += "\t\tURL: " + this.url + LS;
        report += "\t\tCheckouts: "+ this.getNumRequests() + LS;
        report += "\t\tThread waits: "+ this.getNumWaits() + LS;
        report += "\t\tConnections found closed: "+numConnectionFaults +LS;
        report += "\t\tConnections reaped by check out timeout: "+ this.getNumCheckoutTimeouts() + LS;
		report += "\t\tConnections reaped by idle      timeout: "+ this.numIdleTimeout + LS;
        report += "\t\tPreferred rest time for each connection: "+ m_iPreferredIdleTime + "ms" + LS;
        
        synchronized (m_oIdleConnList)
        {
        	PooledConnection[] oConnectionArr = (PooledConnection[])m_oIdleConnList.toArray(new PooledConnection[0]);
	        
	        int iLength = oConnectionArr == null ? 0 : oConnectionArr.length;
	        report += "\t\tConnections currently idle in pool: " + iLength + LS;
	        for (int i = 0 ; i < iLength ; i++)
	        {
                report += oConnectionArr[i].dumpInfo();
	        }
        }
        
        synchronized (m_oBusyConnList)
        {
        	PooledConnection[] oConnectionArr = (PooledConnection[])m_oBusyConnList.toArray(new PooledConnection[0]);
	        
	        int iLength = oConnectionArr == null ? 0 : oConnectionArr.length;
	        report += "\t\tConnections currently busy in pool: " + iLength + LS;
	        for (int i = 0 ; i < iLength ; i++)
	        {
                report += oConnectionArr[i].dumpInfo();
	        }
        }
        
        return report;
    }

    /** Change the number of rows prefetched for a result set, for those
     * drivers that support it.
     * @param newPrefetchSize the new prefetch size to use, -1 for the default
     */
    public void setPrefetchSize(int newPrefetchSize) {
        prefetchSize = newPrefetchSize;
    }

    /** Get the current prefetch size
     */
    public int getPrefetchSize() {
        return prefetchSize;
    }

}

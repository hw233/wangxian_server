package com.xuanzhi.tools.dbpool;


import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Savepoint;
import java.sql.Struct;
import java.util.*;
import java.util.concurrent.Executor;

import org.apache .log4j .Category ;

public class PooledConnection implements Connection, Runnable 
{

    public static final int RETRY_INTERVAL = 5000;
	
	private static int sequence = 0;
	
	private static Category cat = Category.getInstance(PooledConnection.class.getName());

    private ConnectionPool pool;
    private Connection conn;
    private boolean locked;
    private long lastAccess;
    private long lastCheckin;
	private long lastCheckout;
    private int checkoutCount;

    // these keep track of the default driver connection values, 
    // so the connection can be reset when returned to the pool
    private boolean defaultAutoCommit;
    private String defaultCatalog;
    private boolean defaultReadOnly;
    private int defaultTransactionIsolation;
    private Map defaultTypeMap;

    /** How many statements have been created on this connection */
    int totalStatements;
    /** How many prepared calls have been created on this connection */
    int preparedCalls;
    /** How many prepared statements have been found in the pool of PreparedStatements */
    int preparedStatementHits;
    /** How many prepared statements have been created for the pool of PreparedStatements */
    int preparedStatementMisses;

    /** How many prepared statements have been requested (if statement
     *  caching is turned off */
    int preparedStatements;

    /** 
     * Holds an exception which was thrown (and caught) when this connection pool was
     * checked out.  This will probably only be non-null when the connection pool is in
     * trace mode.
     */
    private Exception traceException;

    /** How many times to try re-opening a connection before giving up */
    private static final int closedConnMaxRetry = 5;
    /** How long to wait between re-opening retries, in milliseconds */
    private static final int closedConnRetryWait = 1000;


    /** statement for the connection is cached here. */
	protected String m_strRecentSQL = null;
    protected PooledStatement theStatement = null;
    protected Hashtable prepStmts;
	
    protected LinkedList<Statement> nonPooledStatmentList = new LinkedList<Statement>();
	private int id;
    //private List callableStmts;

    public PooledConnection(Connection conn, ConnectionPool pool) {
		this.id = sequence++;
        this.conn = conn;
        this.pool = pool;
        this.locked = false;
        this.lastAccess = 0;
        this.checkoutCount = 0;
        this.totalStatements = 0;

        try {
            // get the default connection properties
            defaultAutoCommit = getAutoCommit();
           // defaultCatalog = getCatalog();
            //defaultReadOnly = isReadOnly();
            defaultTransactionIsolation = getTransactionIsolation();
           // defaultTypeMap = getTypeMap();
        }
        catch(Exception e) {
            System.err.println("PooledConnection failed to get default connection settings!");
            e.printStackTrace();
        }

        prepStmts = new Hashtable();

        //callableStmts = new LinkedList();
    }

    public int getID()
	{
		return id;
	}
	/**
     * This is used by ConnectionPool.removeConnection().  It closes all
     * statements and closes the actual JDBC Connection.  We implement this
     * in a runnable method so that we can run this in a separate Thread and
     * use a timeout mechanism to avoid deadlock (which was occurring when
     * trying to close a Connection that had a query in progress)
     */
    public void run() {
        try {
            closeStatements();
        }
        catch(Exception e) {
            e.printStackTrace();
			cat.error("PooledConnection: closeStatements() catch",e); 
        }

        try {
            getConnection().close();
        }
        catch(SQLException e) {
            e.printStackTrace();
			cat.error("PooledConnection: close connection catch",e); 
        }
    }

    protected void closeNonPooledStatement(){
    	try{
	    	Iterator<Statement> it = nonPooledStatmentList.iterator();
	    	while(it.hasNext()){
	    		Statement stmt = it.next();
	    		try{
	    			stmt.close();
	            }catch (SQLException ex){
	                ex.printStackTrace();
	            }
	    	}
	    	nonPooledStatmentList.clear();
    	}catch(Exception e){
    		System.err.println("In PooledConnection.closeNonPooledStatement() Method, catch exception:");
    		e.printStackTrace();
    	}
    }
    /**
     * Closes all PreparedStatments and CallableStatements
     */
    public void closeStatements() throws SQLException {
    	
    	m_strRecentSQL = null;
    	
    	closeNonPooledStatement();
    	
        if (theStatement != null) {
            theStatement.getStatement().close();
			theStatement = null;
        }

		Enumeration enum2 = prepStmts.elements(); 
		
        //for prepared statements
        while(enum2.hasMoreElements())
		{
                try
                    {
                        //for pooled prepared statements
                        PooledPreparedStatement stmt =  (PooledPreparedStatement)enum2.nextElement(); 
                        stmt.getStatement().close();
                    }
					catch (SQLException ex)
                    {
                        ex.printStackTrace();
                    }
         }
		
		prepStmts.clear();

        //for callable statements

       
    }

    /**
     * Returns true if this Connection is not in use.  Returns false if it
     * is in use
     */
    public synchronized boolean getLock() {
        if(locked) return false;
        else {
            locked = true;
            checkoutCount++;

            // Touch our access time so that we don't get immediately reaped
            // by accident, which could happen if the idle timeout is close to
            // the max duration of the query
            lastAccess = System.currentTimeMillis();
			lastCheckout = lastAccess;
            return true;
        }
    }

    public boolean isLocked() {
        return locked;
    }

    /**
     * Returns number of times this node has been checked out from the pool
     */
    public int getCheckoutCount() {
        return checkoutCount;
    }

    /**
     * Returns when this connection was last checked out; 0 if it has never
     * been used.
     */
    public long getLastAccess() {
        return lastAccess;
    }

    /**
     * Returns when this connection was last checked in (when it's lock was last released);
     * 0 if it has never been released.
     */
    public long getLastCheckin() {
        return lastCheckin;
    }
	
	/**
     * Returns when this connection was last checked in (when it's lock was last released);
     * 0 if it has never been released.
     */
    public long getLastCheckout() {
        return lastCheckout;
    }
	

    /**
     * Doesn't actually close the connection, but instead returns itself
     * back to the pool to be re-used.  However, if you specified maxCheckouts
     * in the constructor, then this *will* close the JDBC Connection and 
     * re-open it if the number of checkouts has been exceeded.
     */
    public void close() throws SQLException {
    	closeNonPooledStatement();
        lastAccess = System.currentTimeMillis();
        pool.returnConnection(this);
    }

    
    /**
     * called by ConnectionPool.returnConnection() right before it wakes up
     * the threads
     */
    protected void releaseLock() {
        lastCheckin = System.currentTimeMillis();
        locked = false;
    }

    protected Connection getConnection() {
        return conn;
    }

    public Connection getNativeConnection() {
        return conn;
    }


    // these return the default driver connection values, 
    // so the connection can be reset by an observer when returned to the pool

    public boolean getDefaultAutoCommit() {
        return defaultAutoCommit;
    }

    public String getDefaultCatalog() {
        return defaultCatalog;
    }

    public boolean getDefaultReadOnly() {
        return defaultReadOnly;
    }

    public int getDefaultTransactionIsolation() {
        return defaultTransactionIsolation;
    }

    public Map getDefaultTypeMap() {
        return defaultTypeMap;
    }


    /**
     * Dump recent information about this connection and the statement
     *
     */
    public String dumpRecentInfo()
    {
    	String strRecentSQL = m_strRecentSQL;
    	if(strRecentSQL == null) strRecentSQL= "null";
    	
        String LS = System.getProperty("line.separator");
        String report = "\t\t\tConnection: [id="+id+"]" + this.toString()+ LS;
        report += "\t\t\t\tStatements Requested: " + this.totalStatements + LS;
        report += "\t\t\t\tPrepared Calls: " + this.preparedCalls + LS;
        if(pool.getCacheStatements()) {
            report += "\t\t\t\tPrepared Statements Hits: " + this.preparedStatementHits + LS;
            report += "\t\t\t\tPrepared Statements Misses: " + this.preparedStatementMisses + LS;
        }
        else {
            report += "\t\t\t\tPrepared Statements Requested: " + this.preparedStatements + LS;
        }
        report += "\t\t\t\tCheckout count: " + this.getCheckoutCount()+ LS;
        report += "\t\t\t\tLast Checkout: " + getLastCheckout() + ": " + new java.util.Date(this.getLastCheckout())+ LS;
        report += "\t\t\t\tLast Checkin : " + getLastCheckin() + ": " + new java.util.Date(getLastCheckin()) + LS;
        report += "\t\t\t\t" + (isLocked() ? "Connection IS checked out." : "Connection is NOT checked out.") + LS;
        report += "\t\t\t\tCheckout Stack Trace: ";
        if (traceException != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            traceException.printStackTrace(pw);
            report += sw.toString();
        }
        else {
            report += "null";
        }
        report += LS;

        PooledPreparedStatement stmt =  (PooledPreparedStatement)prepStmts.get(strRecentSQL);
		
        if (stmt != null)
		{
			report += stmt.dumpInfo();
        }
        else if (theStatement != null && strRecentSQL.equals("theStatement"))
        {
            report += theStatement.dumpInfo();
		}
		
        return report;
    }

    /**
     * Dump some information about this connection and the statement
     *
     */
    public String dumpInfo()
    {
    	String strRecentSQL = m_strRecentSQL;
    	if(strRecentSQL == null) strRecentSQL = "null";
    	
        String LS = System.getProperty("line.separator");
        String report = "\t\t\tConnection: [id="+id+"]" + this.toString()+ LS;
        report += "\t\t\t\tStatements Requested: " + this.totalStatements + LS;
        report += "\t\t\t\tPrepared Calls: " + this.preparedCalls + LS;
        if(pool.getCacheStatements()) {
            report += "\t\t\t\tPrepared Statements Hits: " + this.preparedStatementHits + LS;
            report += "\t\t\t\tPrepared Statements Misses: " + this.preparedStatementMisses + LS;
        }
        else {
            report += "\t\t\t\tPrepared Statements Requested: " + this.preparedStatements + LS;
        }
        report += "\t\t\t\tCheckout count: " + this.getCheckoutCount()+ LS;
        report += "\t\t\t\tLast Checkout: " + getLastCheckout() + ": " + new java.util.Date(this.getLastCheckout())+ LS;
        report += "\t\t\t\tLast Checkin : " + getLastCheckin() + ": " + new java.util.Date(getLastCheckin()) + LS;
        report += "\t\t\t\t" + (isLocked() ? "Connection IS checked out." : "Connection is NOT checked out.") + LS;
        report += "\t\t\t\tCheckout Stack Trace: ";
        if (traceException != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            traceException.printStackTrace(pw);
            report += sw.toString();
        }
        else {
            report += "null";
        }
        report += LS;

        if (theStatement!=null)
            report += (strRecentSQL.equals("theStatement") ? "[Recent] " : "") + theStatement.dumpInfo();
		
		Enumeration enum2 = prepStmts.elements(); 
		
        //for prepared statements
        while(enum2.hasMoreElements())
		{
                //for pooled prepared statements
                PooledPreparedStatement stmt =  (PooledPreparedStatement)enum2.nextElement(); 
				report += ((m_strRecentSQL != null && m_strRecentSQL.equals(stmt.sql)) ? "[Recent] " : "") + stmt.dumpInfo();
        }
        return report;
    }

    /* Check to see if the connection has been closed (typically by the server)
     * and attempt to recover if it has.
     */
    public void guardConnection() {
        boolean badConnection;
        try {
            badConnection  = conn.isClosed();
        } catch (SQLException sqe) {
            badConnection = true;
        }
        if (badConnection) {
            pool.numConnectionFaults++;
            System.err.println("PooledConnection.guardConnection(): "+
                "found closed Connection. "+
                "Statement information follows. Attempting to recover.");
            if (theStatement!=null)
                System.err.println("PooledConnection.guardConnection(): "+
                    theStatement.dumpInfo());
            else 
                System.err.println("PooledConnection.guardConnection: statement was null"); 
            this.theStatement = null;
            // Retry to open the connection, up to closedConnMaxRetry times.
            int retryCount = 0;
            for (retryCount = 0 ; retryCount < closedConnMaxRetry; retryCount++) {
                try {
                    conn=pool.createDriverConnection();
                } catch (SQLException SQE) {
                    System.err.println("PooledConnection.guardConnection(): "+
                        "failed to create connection on try #"+retryCount);
                    try {
                        Thread.sleep(closedConnRetryWait);
                    } catch (InterruptedException ie) {
                        System.err.println("PooledConnection: "+ie);
                    }
                    continue;         
                }
                System.err.println("PooledConnection.guardConnection(): "+
                    "Recovered connection");
                return;      
            }
        }
    }

    public Statement createStatement() throws SQLException {
        //guardConnection();
        this.totalStatements++;

        PoolCommand comm = new PoolCommand() { 
                public Object execute() throws SQLException {
                    return conn.createStatement();
                }
            };

        if(pool.getCacheStatements()) {
            if (theStatement == null) {
                Statement stmt = (Statement)executeCommand(comm);
                theStatement = new PooledStatement(stmt, this);
            }
            
            m_strRecentSQL = "theStatement";
            
            return theStatement;
        }
        else {
            Statement stmt = (Statement)executeCommand(comm);
            nonPooledStatmentList.add(stmt);
            return stmt;
        }
    }

	public Statement createStatement(int i1,int i2,int i3) throws SQLException {
        //guardConnection();
        this.totalStatements++;

        PoolCommand comm = new PoolCommand() { 
                public Object execute() throws SQLException {
                    return conn.createStatement();
                }
            };

        if(pool.getCacheStatements()) {
            if (theStatement == null) {
                Statement stmt = (Statement)executeCommand(comm);
                theStatement = new PooledStatement(stmt, this);
            }
            
            m_strRecentSQL = "theStatement";
            
            return theStatement;
        }
        else {
        	 Statement stmt = (Statement)executeCommand(comm);
             nonPooledStatmentList.add(stmt);
             return stmt;
        }
    }


    private Object executeCommand(PoolCommand comm) throws SQLException {
        while (true) {
            try {
                return comm.execute();
            }
            catch(SQLException e) {
                if (pool.getValidator() != null &&
                    pool.getValidator().connectionIsBroken(conn, e)) {

                    // Can't talk to database.  Attempt to re-establish a 
                    // connection to the database and re-try the query
                    try {
                        Thread.currentThread().sleep(RETRY_INTERVAL);
                    }
                    catch(InterruptedException e2) { }

                       cat.info("ConnectionPool: " + pool.getAlias() + " connection is broken: " + e.getMessage() + "  Attempting to refresh connection");
                      
                    // This closes the connection (a little hidden...)
                    run();

                    // get a new connection
                    while (true) {
                        try {
                            conn = pool.createDriverConnection();
                            break;
                        }
                        catch(SQLException e2) {
                            
							
							
                                cat.info("ConnectionPool: " +   pool.getAlias() + " unable to refresh connection: " +  e2.getMessage());
                           
                            try {
                                Thread.currentThread().sleep(RETRY_INTERVAL);
                            }
                            catch(InterruptedException e3) { }
                        }
                    }

                }
                else throw e;
            }
        }
    }


    /**************************************************************************
     *
     * Proxy all other JDBC calls to actual Connection object
     *
     *************************************************************************/
    
    public Clob createClob()
                    throws SQLException{
    	return conn.createClob();
    }
   
    public Blob createBlob()
                    throws SQLException{
    	return conn.createBlob();
    }

    public NClob createNClob()
                      throws SQLException{
    	return conn.createNClob();
    }

    public SQLXML createSQLXML() throws SQLException{
    	return conn.createSQLXML();
    }
    
    public boolean isValid(int timeout)
                    throws SQLException{
    	return conn.isValid(timeout);
    }
    public void setClientInfo(String name,
                       String value)
                       throws SQLClientInfoException{
    	conn.setClientInfo(name, value);
    }
    
    public void setClientInfo(Properties properties)
                       throws SQLClientInfoException{
    	conn.setClientInfo(properties);
    }
    public    String getClientInfo(String name)
                         throws SQLException{
    	return conn.getClientInfo(name);
    }
    
    public Properties getClientInfo()
                             throws SQLException{
    	return conn.getClientInfo();
    }
            
    public Array createArrayOf(String typeName,
            Object[] elements)
            throws SQLException{
    	return conn.createArrayOf(typeName, elements);
    	
    }
    public Struct createStruct(String typeName,
            Object[] attributes)
            throws SQLException{
    	return conn.createStruct(typeName,attributes);
    }


    public Map getTypeMap() throws SQLException {
		return conn.getTypeMap();
    }

    public PreparedStatement prepareStatement(final String sql) 
        throws SQLException {
        
        PoolCommand comm = new PoolCommand() { 
                public Object execute() throws SQLException {
                    return conn.prepareStatement(sql);
                }
            };

        if(pool.getCacheStatements()) {
            PreparedStatement stmt = (PreparedStatement)prepStmts.get(sql);
            if (stmt == null) {
                // The prepared statement was not found in the prepared statement cache
                stmt = (PreparedStatement)executeCommand(comm);
                stmt = new PooledPreparedStatement(stmt, sql, this);
                synchronized (prepStmts) {
	                if(prepStmts.size() < 32){
	                	prepStmts.put(sql, stmt);
	                }else{
	                	nonPooledStatmentList.add(stmt);
	                }
                }
                preparedStatementMisses++;
            } else {
                preparedStatementHits++;
            }
            
            m_strRecentSQL = sql;
            
            return stmt;
        }
        else {
            preparedStatements++;
            
            PreparedStatement pstmt = (PreparedStatement)executeCommand(comm);
            nonPooledStatmentList.add(pstmt);
            return pstmt;
        }
    }
	

    public PreparedStatement prepareStatement(String sql, int resultSetType,
        int resultSetConcurrency) throws SQLException 
	{
		
		
        if(pool.getCacheStatements()) {
            preparedStatementMisses++;
        }
        else {
            preparedStatements++;
        }
        PreparedStatement stmt = conn.prepareStatement(
            sql, resultSetType, resultSetConcurrency);
        nonPooledStatmentList.add(stmt);
        return stmt;
	}
	
	public PreparedStatement prepareStatement(String sql, int resultSetType) throws SQLException 
	{
        if(pool.getCacheStatements()) {
            preparedStatementMisses++;
        }
        else {
            preparedStatements++;
        }
        PreparedStatement stmt = conn.prepareStatement(
            sql, resultSetType);
        nonPooledStatmentList.add(stmt);
        return stmt;
    }

	public PreparedStatement prepareStatement(String sql, int i1,int i2,int i3) throws SQLException 
	{
        if(pool.getCacheStatements()) {
            preparedStatementMisses++;
        }
        else {
            preparedStatements++;
        }
        PreparedStatement stmt = conn.prepareStatement(sql,i1,i2,i3);
        nonPooledStatmentList.add(stmt);
        return stmt;
    }
	
	public PreparedStatement prepareStatement(String sql, int i[]) throws SQLException 
	{
        if(pool.getCacheStatements()) {
            preparedStatementMisses++;
        }
        else {
            preparedStatements++;
        }
        PreparedStatement stmt = conn.prepareStatement(sql,i);
        nonPooledStatmentList.add(stmt);
        return stmt;
    }
	
	public PreparedStatement prepareStatement(String sql, String param[]) throws SQLException 
	{
        if(pool.getCacheStatements()) {
            preparedStatementMisses++;
        }
        else {
            preparedStatements++;
        }
        PreparedStatement stmt = conn.prepareStatement(sql,param);
        nonPooledStatmentList.add(stmt);
        return stmt;
    }
	
    public CallableStatement prepareCall(String sql) throws SQLException {
        preparedCalls++;
        CallableStatement stmt =  conn.prepareCall(sql);

        nonPooledStatmentList.add(stmt);
        return stmt;
    }

    public CallableStatement prepareCall(String sql, int resultSetType,
        int resultSetConcurrency) throws SQLException {
        preparedCalls++;          
        CallableStatement stmt =  
        conn.prepareCall(sql, resultSetType, resultSetConcurrency);

        nonPooledStatmentList.add(stmt);
        return stmt;
    }
	
	public CallableStatement prepareCall(String sql,int i1,int i2,int i3) throws SQLException 
	{
		preparedCalls++;          
        CallableStatement stmt =  
            conn.prepareCall(sql, i1,i2,i3);

        nonPooledStatmentList.add(stmt);
        return stmt;
	}

    public Statement createStatement(int resultSetType, int resultSetConcurrency)
        throws SQLException {
        this.totalStatements++;
        Statement stmt = conn.createStatement(resultSetType, resultSetConcurrency);
        nonPooledStatmentList.add(stmt);
        return stmt;
    }

    public String nativeSQL(String sql) throws SQLException {
        return conn.nativeSQL(sql);
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        conn.setAutoCommit(autoCommit);
    }

    public boolean getAutoCommit() throws SQLException {
        return conn.getAutoCommit();
    }

    public void commit() throws SQLException {
        conn.commit();
    }

    public void rollback() throws SQLException {
        conn.rollback();
    }

    public boolean isClosed() throws SQLException {
        return conn.isClosed();
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return conn.getMetaData();
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        conn.setReadOnly(readOnly);
    }

    public boolean isReadOnly() throws SQLException {
        return conn.isReadOnly();
    }

    public void setCatalog(String catalog) throws SQLException {
        conn.setCatalog(catalog);
    }

    public String getCatalog() throws SQLException {
        return conn.getCatalog();
    }

    public void setTransactionIsolation(int level) throws SQLException {
        conn.setTransactionIsolation(level);
    }

    public int getTransactionIsolation() throws SQLException {
        return conn.getTransactionIsolation();
    }

    public SQLWarning getWarnings() throws SQLException {
        return conn.getWarnings();
    }

    public void clearWarnings() throws SQLException {
        conn.clearWarnings();
    }
	
	public int getHoldability() throws SQLException 
	{
		return conn.getHoldability();
	}
	
	public Savepoint setSavepoint() throws SQLException 
	{
		return conn.setSavepoint();
	}

	public Savepoint setSavepoint(String s) throws SQLException 
	{
		return conn.setSavepoint(s);
	}
	
	public void releaseSavepoint(Savepoint sp) throws SQLException 
	{
		conn.releaseSavepoint(sp);
	}
	
	public void rollback(Savepoint sp) throws SQLException 
	{
		conn.rollback(sp);
	}
	
	public void setHoldability(int i) throws SQLException 
	{
		conn.setHoldability(i);
	}
	
	
	
    protected void setTraceException(Exception e) {
        traceException = e;
    }

    /**
     * If the connection pool is in trace mode, you can examine the stack trace
     * in this exception to determine where it was last checked out.
     */
    public Exception getTraceException() { return traceException; }
    
    public void removeCachedStatement(PooledStatement stmt)
    {
    	if (theStatement == stmt)
    	{
    		theStatement = null;
    	}
        	
    	try
    	{
    		stmt.getStatement().close();
    	}
    	catch (Exception e)	{ }
    }
    
    public void removeCachedStatement(String sql)
    {
    	PooledPreparedStatement stmt =  (PooledPreparedStatement)prepStmts.remove(sql);
    	
    	if (stmt != null)
    	{
	    	try
	    	{
	    		stmt.getStatement().close();
	    	}
	    	catch (Exception e)	{ }
    	}
    }

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		conn.setTypeMap(map);
		
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return conn.isWrapperFor(iface);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return conn.unwrap(iface);
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
}


interface PoolCommand {

    public Object execute() throws SQLException;

}

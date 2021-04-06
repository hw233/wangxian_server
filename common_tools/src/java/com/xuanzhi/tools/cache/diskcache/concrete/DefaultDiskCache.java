package com.xuanzhi.tools.cache.diskcache.concrete;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.RemovedListener;
import com.xuanzhi.tools.ds.AvlTree;
import com.xuanzhi.tools.ds.AvlTree.TreeNode;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

import org.apache.log4j.*;
/**
 * DiskCache的实现，此实现采用一个文件作为数据的存储，我们称这个文件为数据文件。
 * 
 * 数据块：数据文件被拆分为多个数据块，每个数据块的大小任意，同时每个数据块中只存放一个(key,value)。
 * 数据块包括如下信息：
 * 		数据块在数据文件中的位置
 * 		数据块的大小
 * 		数据块是否包含数据（也就是(key,value)对）
 * 		数据块对应数据的生命结束时间
 * 		数据块对应数据的Idle超时时间
 * 		数据块对应数据key的hashcode
 * 		数据块对应数据key
 * 		数据块对应数据value
 * 		数据块对应数据的校验码
 * 
 * 此实现提供两个可供配置的选项：
 * 1. 关于Idle超时在系统宕机恢复后，是重新开始计时还是延续宕机前的状态
 * 2. 关于系统宕机恢复，是采用索引文件恢复，还是从数据文件中重新构建索引
 * 
 * 内存结构：
 * 		内存中存在1个列表，2个平衡树和一个hash表
 * 		分别是：
 * 		1. data-blocklist:数据块索引列表，此列表按照数据文件中数据块的顺序排列，包含所有的数据块，根据此列表可以生成其他2个平衡树和hash表
 * 		2. free-blocktree:空闲数据块平衡树，此平衡树由所有无数据（key，value对）的块组成，按照大小排序
 * 		3. timeout-blocktree:超时数据块平衡树，此平衡树由所有含数据的块组成，按照超时时间排序
 * 		4. hashcode-blockmap:数据块hash表，hash表的key为数据块对应key的hashcode，值为数据块的列表，此hash表用于get方法
 * 		
 * 		注意：我们保证data-blocklist相邻的两块数据块不同时为free，否则合并成一块。
 * 操作：
 * 
 * 	add: 在free-blocktree中找到第一个大小刚刚大于对象所需空间的block（假设为X），将此block拆分为两个block（分别为A和B），
 * 		 A作为数据存放，B不存放数据。A的大小等于存放数据所需的大小，B的大小不小于49个字节。
 * 		 将X从data-blocklist删除，并且将A和B在对应的位置插入
 * 		 将X从free-blocktree中删除，将B插入free-blocktree，将A插入timeout-blocktree
 * 		 将A存放于hashcode-blockmap中，
 * 		 将B写入到数据文件中，将A写入到数据文件中（注意写入顺序）
 * 
 * remove: 通过hashcode-blockmap找到key的hashcode对应的block列表，遍历此列表，找到对应的block，设为X
 * 		   将X标记为free，同时清空X中的相关数据。
 *         检查X前后的block，是否为free的，如果有，则合并，如果是与前面block合并，只需修改前面Block的大小信息，
 *         如果是与后面的Block合并，只需修改X的信息
 *         如果有合并需要对内存也进行相关操作。
 *         
 *         同时修改数据文件
 *         	
 * get：通过hashcode-blockmap找到key的hashcode对应的block列表，遍历此列表，找到对应的block，设为X
 * 		如果X不为null，修改X的idletime，并且将X从timeout-blocktree中删除，再插入
 *      同时，根据配置，看是否要修改数据文件
 *       		
 * put：先remove，然后再add。	
 * 
 * 本实现的Log采用log4j，取得logger的代码如下：
 * 		static Logger logger = Logger.getLogger(DefaultDiskCache.class);
 * 
 */
public class DefaultDiskCache extends AbstractDiskCache implements Runnable {

	static Logger logger = Logger.getLogger(DefaultDiskCache.class);
	
	/**
	 * 无上限的线程池
	 */
	protected static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1,32,30,TimeUnit.SECONDS,new SynchronousQueue(),new ThreadPoolExecutor.CallerRunsPolicy());
	
	
	//double list of data blocks
	protected DataBlock dbListHeader = new DataBlock(this);
	
	// free block list sort by size from small to big
	protected AvlTree freeDBTree = new AvlTree(new Comparator(){
		public int compare(Object o1, Object o2) {
			DataBlock d1 = (DataBlock)o1;
			DataBlock d2 = (DataBlock)o2;
			if(d1.length < d2.length) return -1;
			if(d1.length > d2.length) return 1;
			if(d1.offset < d2.offset) return -1;
			if(d1.offset > d2.offset) return 1;
			return 0;
		}
		
	});
	
	// access data block list sort by timeout from lately to far
	protected AvlTree timeoutDBTree = new AvlTree(new Comparator(){
		public int compare(Object o1, Object o2) {
			DataBlock d1 = (DataBlock)o1;
			DataBlock d2 = (DataBlock)o2;
			long t1 = d1.idletime<d1.lifetime?d1.idletime:d1.lifetime;
			long t2 = d2.idletime<d2.lifetime?d2.idletime:d2.lifetime;
			if(t1 < t2) return -1;
			if(t1 > t2) return 1;
			if(d1.length < d2.length) return -1;
			if(d1.length > d2.length) return 1;
			if(d1.offset < d2.offset) return -1;
			if(d1.offset > d2.offset) return 1;
			return 0;
		}
		
	});
	
	// hashcode map to data block list
	protected HashMap<Integer,LinkedList<DataBlock>> hashcodeBlockMap = new HashMap<Integer,LinkedList<DataBlock>>(1001,0.75f);
	
	protected long defaultLifeTimeTimeout = 100L * 365L * 24L * 3600L * 1000L;
	protected long defaultIdleTimeout = 1800 * 1000;
	protected long maxDiskSize = 1024 * 1024 * 1024;
	protected int maxMemorySize = 1024 * 1024 * 16;
	protected int maxElementNum = 1024 * 1024;
	
	protected long currentDiskSize = 0;
	protected int currentMemorySize = 0;
	protected int elementNum = 0;
	protected long hitCount = 0;
	protected long missCount = 0;
	
	//相关的存储属性
	protected File dataFile = null;
	protected File indexedFile = null;
	protected RandomAccessFile reader = null;
	protected RandomAccessFile writer = null;
	
	//关于Idle超时在系统宕机恢复后，是重新开始计时还是延续宕机前的状态，true表示延续宕机前的状态，默认为false
	protected boolean idleTimeContinousAfterSystemCrashing = false;
	
	//shrinker thread
	protected Thread thread;

	//protected boolean running = true;
	protected boolean enableShrinkerThread;

	
	protected String name;
	
	////////////////////////////////////////////////////////////////////////////////////////
	// 
	///////////////////////////////////////////////////////////////////////////////////////

	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * 默认生命周期，为100年
	 */
	public long getDefaultLifeTimeTimeout(){
		return defaultLifeTimeTimeout;
	}
	
	
	public void setDefaultLifeTimeTimeout(long timeout){
		defaultLifeTimeTimeout = timeout;
	}
	
	public int getMaxElementNum(){
		return maxElementNum;
	}
	
	public void setMaxElementNum(int size){
		maxElementNum = size;
	}
	
	/**
	 * 默认最大空闲时间，1800秒
	 * @param timeout
	 */
	public long getDefaultIdleTimeout(){
		return defaultIdleTimeout;
	}
	
	public void setDefaultIdleTimeout(long timeout){
		defaultIdleTimeout = timeout;
	}
	
	public void setIdleTimeContinousAfterSystemCrashing(boolean b){
		idleTimeContinousAfterSystemCrashing = b;
	}
	
	public boolean isIdleTimeContinousAfterSystemCrashing(){
		return idleTimeContinousAfterSystemCrashing;
	}
	
	public long getMaxDiskSize() {
		return maxDiskSize;
	}

	public void setMaxDiskSize(long maxSize) {
		maxDiskSize = maxSize;
	}

	public int getMaxMemorySize() {
		return maxMemorySize;
	}

	public void setMaxMemorySize(int maxSize) {
		maxMemorySize = maxSize;
	}

	public int getCurrentMemorySize() {
		return this.currentMemorySize;
	}

	public long getCurrentDiskSize() {
		return this.currentDiskSize;
	}

	public int getNumElements() {
		return this.elementNum;
	}

	public int getBlockNum(){
		return this.timeoutDBTree.size() + this.freeDBTree.size();
	}
	
	public int getFreeBlockNum(){
		return this.freeDBTree.size();
	}
	
	public int getDataBlockNum(){
		return this.timeoutDBTree.size() ;
	}
	
	public File getDataFile(){
		return dataFile;
	}
	
	public File getIndexedFile(){
		return this.indexedFile;
	}
	
	public long getCacheHits() {
		return this.hitCount;
	}

	public long getCacheMisses() {
		return this.missCount;
	}
	
	/**
	 * 获得此Cache所有的数据块的拷贝，对返回的对象进行修改不会影响此Cache的状态
	 * @return
	 */
	public DataBlock[] getDataBlocks(){
		ArrayList<DataBlock> al = new ArrayList<DataBlock>(this.getBlockNum());
		DataBlock db = this.dbListHeader.next;
		while(db != null && db != dbListHeader){
			DataBlock tmp = new DataBlock(null);
			tmp.offset = db.offset;
			tmp.length = db.length;
			tmp.containsData = db.containsData;
			tmp.hashcode = db.hashcode;
			tmp.lifetime = db.lifetime;
			tmp.idletime = db.idletime;
			tmp.keyLength = db.keyLength;
			tmp.valueLength = db.valueLength;
			tmp.key = db.key;
			tmp.value = db.value;
			al.add(tmp);
			
			db = db.next;
		}
		return al.toArray(new DataBlock[0]);
	}
	
	public String toString(){
		long total = getCacheHits()+getCacheMisses();
		if(total == 0) total = 1;
		
		return "Default{"+name+","+getNumElements()+","+this.getDataBlockNum()+"/"+this.getFreeBlockNum()+","+(getCacheHits()*100/total)+"%,"+(getCurrentDiskSize()/1024)+"K/"+(getMaxDiskSize()/(1024*1024))+"M," +
		(getCurrentMemorySize()/1024)+"K/"+(getMaxMemorySize()/1024)+"K}";
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	//
	///////////////////////////////////////////////////////////////////////////////////////
	public DefaultDiskCache(File dataFile){
		this(dataFile,null,"DiskCache-"+StringUtil.randomIntegerString(3),1800*1000L,false,false);
	}
	
	public DefaultDiskCache(File dataFile,String name,long defaultIdleTimeout){
		this(dataFile,null,name,defaultIdleTimeout,false,false);
	}
	
	public DefaultDiskCache(File dataFile,String name,long defaultIdleTimeout,boolean idleTimeContinousAfterSystemCrashing,boolean enableShrinkerThread){
		this(dataFile,null,name,defaultIdleTimeout,idleTimeContinousAfterSystemCrashing,enableShrinkerThread);
	}
	
	public DefaultDiskCache(File dataFile,String name,long defaultIdleTimeout,boolean idleTimeContinousAfterSystemCrashing){
		this(dataFile,null,name,defaultIdleTimeout,idleTimeContinousAfterSystemCrashing,false);
	}
	public DefaultDiskCache(File dataFile,File indexedFile,String name,long defaultIdleTimeout){
		this(dataFile,indexedFile,name,defaultIdleTimeout,false,false);
	}
		
	
	public DefaultDiskCache(File dataFile,File indexedFile,String name,long defaultIdleTimeout,boolean idleTimeContinousAfterSystemCrashing,boolean enableShrinkerThread){
		if(dataFile.isDirectory() && dataFile.exists()) throw new IllegalArgumentException("data file must be a file");
		this.dataFile = dataFile;
		this.indexedFile = indexedFile;
		this.name = name;
		this.defaultIdleTimeout = defaultIdleTimeout;
		this.idleTimeContinousAfterSystemCrashing = idleTimeContinousAfterSystemCrashing;
		
		dbListHeader.next = dbListHeader;
		dbListHeader.previous = dbListHeader;
		this.enableShrinkerThread = enableShrinkerThread;
		long startTime = System.currentTimeMillis();
		try{
			init();
			if(logger.isInfoEnabled()){
				logger.info("initialized success! " + toString() + " cost "+(System.currentTimeMillis() - startTime)+" ms");
			}
		}catch(Exception e){
			logger.warn("initialized failed! " + toString() + " cost "+(System.currentTimeMillis() - startTime)+" ms",e);
		}
	}
	
	public DefaultDiskCache(File dataFile,File indexedFile){
		this(dataFile,indexedFile,"DiskCache-"+StringUtil.randomIntegerString(3),1800*1000L,false,false);
	}
	
	protected void init() throws Exception{
		File f = dataFile.getParentFile();
		if(f.exists() == false) f.mkdirs();
		
		if(dataFile.isFile() && dataFile.exists()){
			reader = new RandomAccessFile(dataFile,"r");
			loadIndexFromDataFile(reader);
		}
		
		if(this.enableShrinkerThread){
			thread = new Thread(this,"DiskCache-"+this.name+"-Shrinker-Thread");
			thread.start();
		}
		
		DiskCacheHelper.addDiskCache(this);
	}
	
	
	protected void loadIndexFromDataFile(RandomAccessFile reader) throws Exception{
		boolean b = false;
		long startTime = System.currentTimeMillis();
		if(this.indexedFile != null && this.indexedFile.isFile()){
			try{
				this.loadIndexFromIndexedFile();
				b = true;
				if(logger.isInfoEnabled())
					logger.info("load indexed file success cost "+(System.currentTimeMillis() - startTime)+" ms");
			}catch(Exception e){
				dbListHeader.next = dbListHeader;
				dbListHeader.previous = dbListHeader;
				b = false;
				logger.warn("load indexed file error cost "+(System.currentTimeMillis() - startTime)+" ms, so it will load index from data file:",e);
			}
		}
		if(b == false){
			long offset = 0;
			long fileLen = reader.length();
			while(offset < fileLen){
				DataBlock db = new DataBlock(this);
				db.offset = offset;
				db.readHeader(reader);
				if(db.length > 0 && db.checkValid() && offset + db.length <= fileLen){
					this.dbListHeader.addPrevious(db);
					offset += db.length;
				}else{
					logger.error("disk cache ["+this.getName()+","+this.dataFile+"],some datablock error: offset="+offset+",fileLen="+fileLen+",db.length="+db.length+",db.length="+db.length+",db.checkValid()="+db.checkValid()+"");
					break;
				}
			}
		}
		
		DataBlock db = dbListHeader.next;
		while(db != dbListHeader && db != null){
			if(db.containsData){
				this.elementNum++;
				this.currentDiskSize+= db.length;
				
				LinkedList<DataBlock> ll = hashcodeBlockMap.get(db.hashcode);
				if(ll == null){
					ll = new LinkedList<DataBlock>();
					hashcodeBlockMap.put(db.hashcode,ll);
				}
				ll.add(db);
				
				if(this.idleTimeContinousAfterSystemCrashing == false)
					db.idletime = System.currentTimeMillis() + this.defaultIdleTimeout;
				
				this.timeoutDBTree.insert(db);
				
			}else{
				this.freeDBTree.insert(db);
			}
			db = db.next;
		}
	}
	
	private static long INDEX_FLAG_VALID = 3498745804880485L;
	
	/**
	 * 索引文件的格式：
	 * 		标记（8B） + 块数（4B）+ 块（块长4B + 块数据）+。。。。+块（块长4B + 块数据）+ 校验码（校验码长4B+校验码）
	 *
	 */
	protected synchronized void saveIndexedFile(){
		if(this.indexedFile == null) return;
		long startTime = System.currentTimeMillis();
		try{
			long total = 0L;
			
			MessageDigest	digest = MessageDigest.getInstance("MD5");
			
			RandomAccessFile fout = new RandomAccessFile(this.indexedFile,"rw");
			fout.write(ByteArrayUtils.numberToByteArray(INDEX_FLAG_VALID,8));
			fout.write(ByteArrayUtils.numberToByteArray(0,4));
			total += 12;
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DataBlock db = this.dbListHeader.next;
			int count = 0;
			
			while(db != null && db != dbListHeader){
				out.write(ByteArrayUtils.numberToByteArray(db.offset,8));
				out.write(ByteArrayUtils.numberToByteArray(db.length,4));
				out.write(ByteArrayUtils.numberToByteArray(db.containsData?1:0,1));
				out.write(ByteArrayUtils.numberToByteArray(db.lifetime,8));
				out.write(ByteArrayUtils.numberToByteArray(db.idletime,8));
				out.write(ByteArrayUtils.numberToByteArray(db.hashcode,4));
				db = db.next;
				
				if(out.size() >= 41 * 1024){
					byte bytes[] = out.toByteArray();
					out = new ByteArrayOutputStream();
					digest.update(bytes);
					
					fout.write(ByteArrayUtils.numberToByteArray(bytes.length,4));
					fout.write(bytes);
					total += bytes.length;
					
					count++;
				}
			}
			if(out.size() > 0){
				byte bytes[] = out.toByteArray();
				digest.update(bytes);
				fout.write(ByteArrayUtils.numberToByteArray(bytes.length,4));
				fout.write(bytes);
				total += bytes.length;
				
				count++;
			}
			byte bytes[] = digest.digest();
			fout.write(ByteArrayUtils.numberToByteArray(bytes.length,4));
			fout.write(bytes);
			total += bytes.length;
			
			fout.seek(8);
			fout.write(ByteArrayUtils.numberToByteArray(count,4));
			
			fout.close();
			if(logger.isInfoEnabled()){
				logger.info("save indexed file to ["+indexedFile+"], size is ["+total+"] bytes, cost ["+(System.currentTimeMillis() - startTime)+"] ms");
			}
		}catch(Exception e){
			logger.error("save indexed file to ["+indexedFile+"] failed,cost ["+(System.currentTimeMillis() - startTime)+"] ms",e);
		}
	}
	
	/**
	 * 索引文件的格式：
	 * 		标记（8B） + 块数（4B）+ 块（块长4B + 块数据）+。。。。+块（块长4B + 块数据）+ 校验码（校验码长4B+校验码）
	 *
	 */
	protected void loadIndexFromIndexedFile() throws Exception{
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(indexedFile));
		try{
		
			byte bytes[] = new byte[12];
			int m = input.read(bytes);
			if(m != 12) throw new Exception("indexed file ["+indexedFile+"] format error, must be big than 12 bytes.");
			
			long flag = ByteArrayUtils.byteArrayToNumber(bytes,0,8);
			if(flag != INDEX_FLAG_VALID)
				throw new Exception("indexed file ["+indexedFile+"] format error, INDEX_FLAG_VALID not match.");
			
			int count = (int)ByteArrayUtils.byteArrayToNumber(bytes,8,4);
			byte data[] = new byte[41 * 1024];
			
			MessageDigest	digest = MessageDigest.getInstance("MD5");
			
			for(int i = 0 ; i < count ; i ++){
				m = input.read(bytes,0,4);
				if(m != 4){
					throw new Exception("indexed file ["+indexedFile+"] format error, can't read block length.");
				}
				int length = (int)ByteArrayUtils.byteArrayToNumber(bytes,0,4);
				if(length <= 0 || length >= 1024* 1024 * 1024)
					throw new Exception("indexed file ["+indexedFile+"] format error, block ["+(i+1)+"] length ["+length+"] invalid.");
				if(length > data.length)
					data = new byte[length];
				
				m = input.read(data,0,length);
				if(m != length){
					throw new Exception("indexed file ["+indexedFile+"] format error, can't read block data.");
				}
				
				int offset = 0;
				while(offset < length){
					DataBlock db = new DataBlock(this);
					db.offset = ByteArrayUtils.byteArrayToNumber(data,offset,8);
					offset += 8;
					db.length = (int)ByteArrayUtils.byteArrayToNumber(data,offset,4);
					offset += 4;
					db.containsData = ((int)ByteArrayUtils.byteArrayToNumber(data,offset,1) == 1);
					offset += 1;
					db.lifetime = ByteArrayUtils.byteArrayToNumber(data,offset,8);
					offset += 8;
					db.idletime = ByteArrayUtils.byteArrayToNumber(data,offset,8);
					offset += 8;
					db.hashcode = (int)ByteArrayUtils.byteArrayToNumber(data,offset,4);
					offset += 4;
					
					this.dbListHeader.addPrevious(db);
				}
				
				digest.update(data,0,length);
			}
			m = input.read(bytes,0,4);
			if(m != 4){
				throw new Exception("indexed file ["+indexedFile+"] format error, can't read checksum length.");
			}
			byte checksum[] = new byte[(int)ByteArrayUtils.byteArrayToNumber(bytes,0,4)];
			m = input.read(checksum);
			if(m != checksum.length){
				throw new Exception("indexed file ["+indexedFile+"] format error, can't read checksum data.");
			}
			byte checksum2[] = digest.digest();
			if(!Arrays.equals(checksum,checksum2)){
				throw new Exception("indexed file ["+indexedFile+"] format error, checksum error.");
			}
		}finally{
			try{
				input.close();
				RandomAccessFile r = new RandomAccessFile(indexedFile,"rw");
				r.write(ByteArrayUtils.numberToByteArray(0,8));
				r.close();
				
				indexedFile.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
	
	protected synchronized RandomAccessFile getReader(){
		if(reader != null) return reader;
		try {
			reader = new RandomAccessFile(dataFile,"r");
		} catch (FileNotFoundException e) {
			logger.warn("create reader error",e);
		}
		return reader;
	}
	
	protected synchronized RandomAccessFile getWriter(){
		if(writer != null) return writer;
		try {
			writer = new RandomAccessFile(dataFile,"rw");
		} catch (FileNotFoundException e) {
			logger.warn("create writer error",e);
		}
		return writer;
	}
	
	
	
	public synchronized void put(Serializable key, Serializable object) {
		this.cutDiskSize();
		put(key,object,this.defaultLifeTimeTimeout,this.defaultIdleTimeout);
	}
	
	public synchronized void put(Serializable key, Serializable object,long lifeTimeout,long idleTimeout){
		
		if(this.enableShrinkerThread == false){
			removeTimeoutElements();
		}
		
		if(lifeTimeout == 0L) lifeTimeout = 100L * 365L * 24L * 3600L * 1000L;
		if(idleTimeout == 0L) idleTimeout = 100L * 365L * 24L * 3600L * 1000L;
		this.remove(key,RemovedListener.DUPLICATE_PUT);
		try {
			this.add(key,new ObjectWrapper(object,lifeTimeout,idleTimeout));
		} catch (Exception e) {
			logger.warn("add item{"+key+","+object+","+lifeTimeout+","+idleTimeout+"} error",e);
		}
	}

	/**
	 * add: 在free-blocktree中找到第一个大小刚刚大于对象所需空间的block（假设为X），将此block拆分为两个block（分别为A和B），
	 * 		 A作为数据存放，B不存放数据。A的大小等于存放数据所需的大小，B的大小不小于49个字节。
	 * 		 将X从data-blocklist删除，并且将A和B在对应的位置插入
	 * 		 将X从free-blocktree中删除，将B插入free-blocktree，将A插入timeout-blocktree
	 * 		 将A存放于hashcode-blockmap中，
	 * 		 将B写入到数据文件中，将A写入到数据文件中（注意写入顺序）
	 * 
	 * @param key
	 * @param object
	 * @param lifeTimeout
	 * @param idleTimeout
	 */
	protected void add(Serializable key, ObjectWrapper value) throws Exception{
		byte keyBytes[] = ByteArrayUtils.objectToByteArray(key);
		byte valueBytes[] = ByteArrayUtils.objectToByteArray(value);
		int newLength = DataBlock.HEADER_SIZE + keyBytes.length  + valueBytes.length;
		if(newLength >= DataBlock.MAX_BLOCK_SIZE){
			throw new Exception("too big block");
		}
		DataBlock tmp = new DataBlock(this);
		tmp.length = newLength;
		tmp.offset=-1;
		TreeNode tn = freeDBTree.findNearestBigger(tmp);
		
		if(tn == null){//
			DataBlock db = new DataBlock(this);
			db.offset = 0;
			db.length = newLength < DataBlock.MIN_BLOCK_SIZE?DataBlock.MIN_BLOCK_SIZE:newLength;
			db.containsData = true;
			db.hashcode = key.hashCode();
			db.lifetime = System.currentTimeMillis() + value.lifeTimeout;
			db.idletime = System.currentTimeMillis() + value.idleTimeout;
			db.keyLength = keyBytes.length;
			db.key = key;
			db.valueLength = valueBytes.length;
			db.value = value;
			
			DataBlock lastBlock = this.dbListHeader.previous;
			if(lastBlock == dbListHeader) lastBlock = null;
			if(lastBlock != null){
				db.offset = lastBlock.offset + lastBlock.length;
			}
			//更新数据文件
			db.invalidateAllData(getWriter(),keyBytes,valueBytes);
			
			//更新内存
			dbListHeader.addPrevious(db);
			timeoutDBTree.insert(db);
			LinkedList<DataBlock> ll = hashcodeBlockMap.get(db.hashcode);
			if(ll == null){
				ll = new LinkedList<DataBlock>();
				hashcodeBlockMap.put(db.hashcode,ll);
			}
			ll.add(db);
			
		}else{
			DataBlock db = (DataBlock)tn.getObject();
			if(db.length < newLength + DataBlock.HEADER_SIZE || db.length <= DataBlock.MIN_BLOCK_SIZE){ //不拆分
				//更新内存
				freeDBTree.remove(db);
				
				db.containsData = true;
				db.hashcode = key.hashCode();
				db.lifetime = System.currentTimeMillis() + value.lifeTimeout;
				db.idletime = System.currentTimeMillis() + value.idleTimeout;
				db.keyLength = keyBytes.length;
				db.key = key;
				db.valueLength = valueBytes.length;
				db.value = value;
				
				//更新数据文件
				try{
					db.invalidateAllData(getWriter(),keyBytes,valueBytes);
				}catch(Exception e){
					logger.warn("invalidateAllData of "+db+" error:",e);
				}
				timeoutDBTree.insert(db);
				LinkedList<DataBlock> ll = hashcodeBlockMap.get(db.hashcode);
				if(ll == null){
					ll = new LinkedList<DataBlock>();
					hashcodeBlockMap.put(db.hashcode,ll);
				}
				ll.add(db);
			}else{ //需要拆分Block
				DataBlock dbA = new DataBlock(this);
				dbA.offset =db.offset;
				dbA.length = newLength ;
				dbA.containsData = true;
				dbA.hashcode = key.hashCode();
				dbA.lifetime = System.currentTimeMillis() + value.lifeTimeout;
				dbA.idletime = System.currentTimeMillis() + value.idleTimeout;
				dbA.keyLength = keyBytes.length;
				dbA.key = key;
				dbA.valueLength = valueBytes.length;
				dbA.value = value;
				
				DataBlock dbB = new DataBlock(this);
				dbB.offset =dbA.offset + dbA.length;
				dbB.length = db.length - dbA.length ;
				dbB.containsData = false;
				
				//更新数据文件
				try{
					dbB.invalidateHeaderData(getWriter());
				}catch(Exception e){
					logger.warn("invalidateHeaderData of "+dbB+" error:",e);
				}
				try{
					dbA.invalidateAllData(getWriter(),keyBytes,valueBytes);
				}catch(Exception e){
					logger.warn("invalidateAllData of "+dbA+" error:",e);
				}
				
				//更新内存
				db.addPrevious(dbB);
				dbB.addPrevious(dbA);
				db.remove();
				freeDBTree.remove(db);
				freeDBTree.insert(dbB);
				timeoutDBTree.insert(dbA);
				
				LinkedList<DataBlock> ll = hashcodeBlockMap.get(dbA.hashcode);
				if(ll == null){
					ll = new LinkedList<DataBlock>();
					hashcodeBlockMap.put(dbA.hashcode,ll);
				}
				ll.add(dbA);
			}
		}
		this.elementNum ++;
		this.currentMemorySize += keyBytes.length + valueBytes.length;
		this.currentDiskSize +=  keyBytes.length + valueBytes.length + DataBlock.HEADER_SIZE;
	}
	
	/**
	  * get：通过hashcode-blockmap找到key的hashcode对应的block列表，遍历此列表，找到对应的block，设为X
	  * 		如果X不为null，修改X的idletime，并且将X从timeout-blocktree中删除，再插入
	  *      同时，根据配置，看是否要修改数据文件
 	  */
	public synchronized Serializable get(Serializable key) {
		this.cutMemorySize();
		
		if(this.enableShrinkerThread == false){
			removeTimeoutElements();
		}
		
		int hashcode = key.hashCode();
		DataBlock db = null;
		LinkedList<DataBlock> ll = hashcodeBlockMap.get(hashcode);
		if(ll != null){
			Iterator<DataBlock> it = ll.iterator();
			while(db == null && it.hasNext()){
				DataBlock _db = it.next();
				Object keyObj = null;
				try {
					keyObj = _db.getKey(getReader());
				} catch (Exception e) {
					logger.warn("get key of "+_db+" error",e);
				}
				if(keyObj != null && keyObj.equals(key)){
					db = _db;
				}
			}
		}
		ObjectWrapper obj = null;
		if(db != null){
			this.timeoutDBTree.remove(db);
			try {
				obj = (ObjectWrapper)db.getValue(getReader());
			} catch (Exception e) {
				logger.warn("get value of "+db+" error",e);
			}
			if(obj != null){
				db.idletime = System.currentTimeMillis() + obj.idleTimeout;
			}else{
				db.idletime = System.currentTimeMillis() + this.defaultIdleTimeout;
			}
			this.timeoutDBTree.insert(db);
			
			if(this.idleTimeContinousAfterSystemCrashing){
				try {
					db.invalidateIdleTime(getWriter());
				} catch (IOException e) {
					logger.warn("invalidateIdleTime of "+db+" error",e);
				}
			}
		}
		if(obj != null){
			this.hitCount++;
			return obj.value;
		}else{ 
			this.missCount++;
			return null;
		}
	}

	/**
	 * remove: 通过hashcode-blockmap找到key的hashcode对应的block列表，遍历此列表，找到对应的block，设为X
	 * 		   将X标记为free，同时清空X中的相关数据。
	 *         检查X前后的block，是否为free的，如果有，则合并，如果是与前面block合并，只需修改前面Block的大小信息，
	 *         如果是与后面的Block合并，只需修改X的信息
	 *         如果有合并需要对内存也进行相关操作。
	 *         
	 *         同时修改数据文件
	 * 
	 */
	public void remove(Serializable key) {
		remove(key,RemovedListener.MANUAL_REMOVE);
	}	
		
		
	protected synchronized void remove(Serializable key,int removeType) {
			
		int hashcode = key.hashCode();
		DataBlock db = null;
		LinkedList<DataBlock> ll = hashcodeBlockMap.get(hashcode);
		if(ll != null){
			Iterator<DataBlock> it = ll.iterator();
			while(db == null && it.hasNext()){
				DataBlock _db = it.next();
				Object keyObj = null;
				try {
					keyObj = _db.getKey(getReader());
				} catch (Exception e) {
					logger.warn("get key of "+_db+" error",e);
				}
				if(keyObj != null && keyObj.equals(key)){
					db = _db;
				}
			}
		}
		
		if(db != null){
			removeBlock(db,removeType);
		}
		
		logger.warn("remove data successful,key:"+key+" removetype:"+removeType);
	}	
	
	protected synchronized void removeBlock(DataBlock db,int removeType){
		
		if(db != null){
			LinkedList<DataBlock> ll = hashcodeBlockMap.get(db.hashcode);
			if(ll != null){
				ll.remove(db);
			}
			
			if(db.containsData == false){
				db.key = null;
				db.value = null;
				this.timeoutDBTree.remove(db);
				if(this.freeDBTree.find(db) == null)
					this.freeDBTree.insert(db);
				try{
					throw new Exception("the removing block is free,why?");
				}catch(Exception e){
					logger.warn("remove block of "+db+" error",e);
					return;
				}
			}
			
			Object value = null;
			try{
				value =((ObjectWrapper)db.getValue(getReader())).value;
			}catch(Exception e){
				logger.warn("get value of "+db+" error",e);
				return;
			}
			
			
			db.containsData = false;
			db.key = null;
			db.value = null;
			
			this.elementNum --;
			this.currentMemorySize -= db.keyLength;
			this.currentMemorySize -= db.valueLength;
			this.currentDiskSize -= db.keyLength;
			this.currentDiskSize -= db.valueLength;
			this.currentDiskSize -= DataBlock.HEADER_SIZE;
			
			db.keyLength=0;
			db.valueLength=0;
			
			DataBlock prevDB = db.previous;
			if(prevDB == this.dbListHeader) prevDB = null;
			DataBlock nextDB = db.next;
			if(nextDB == this.dbListHeader) nextDB = null;
			
			if(prevDB != null && prevDB.containsData ==false && nextDB != null && nextDB.containsData == false){
				//三块合并为一块
				DataBlock newDB = new DataBlock(this);
				newDB.offset = prevDB.offset;
				newDB.length = prevDB.length + db.length + nextDB.length;
				newDB.containsData = false;
				
				try {
					newDB.invalidateHeaderData(getWriter());
				} catch (Exception e) {
					logger.warn("invalidateHeaderData of "+newDB+" error",e);
				}
				
				prevDB.addPrevious(newDB);
				prevDB.remove();
				db.remove();
				nextDB.remove();
				
				this.timeoutDBTree.remove(db);
				this.freeDBTree.remove(prevDB);
				this.freeDBTree.remove(nextDB);
				this.freeDBTree.insert(newDB);
				
			}else if(prevDB != null && prevDB.containsData ==false){
				//与前面块合并为一块
				DataBlock newDB = new DataBlock(this);
				newDB.offset = prevDB.offset;
				newDB.length = prevDB.length + db.length ;
				newDB.containsData = false;
				
				try {
					newDB.invalidateHeaderData(getWriter());
				} catch (Exception e) {
					logger.warn("invalidateHeaderData of "+newDB+" error",e);
				}
				
				prevDB.addPrevious(newDB);
				prevDB.remove();
				db.remove();
				
				this.timeoutDBTree.remove(db);
				this.freeDBTree.remove(prevDB);
				this.freeDBTree.insert(newDB);
				
			}else if(nextDB != null && nextDB.containsData == false){
				//	与后面块合并为一块
				DataBlock newDB = new DataBlock(this);
				newDB.offset = db.offset;
				newDB.length = nextDB.length + db.length ;
				newDB.containsData = false;
				
				try {
					newDB.invalidateHeaderData(getWriter());
				} catch (Exception e) {
					logger.warn("invalidateHeaderData of "+newDB+" error",e);
				}
				
				db.addPrevious(newDB);
				db.remove();
				nextDB.remove();
				
				this.timeoutDBTree.remove(db);
				this.freeDBTree.remove(nextDB);
				this.freeDBTree.insert(newDB);
				
			}else{
				try {
					db.invalidateHeaderData(getWriter());
				} catch (Exception e) {
					logger.warn("invalidateHeaderData of "+db+" error",e);
				}
				this.timeoutDBTree.remove(db);
				this.freeDBTree.insert(db);
			}
			
			if(value instanceof RemovedListener){
				DoRemovedListener command = new DoRemovedListener((RemovedListener)value,removeType);
				threadPool.execute(command);
			}
			
			logger.warn("remove datablock "+db+" successful, removetype:"+removeType);
		}
	}
	
	/**
	 * 报告存在损坏的数据块
	 * 分几种类型：
	 * 	0	块长度不一致，需修正从此块的前后来修正
	 * 	1	包含数据标记冲突
	 * 	2	hashcode冲突
	 * 	3	校验码冲突
	 * @param db
	 * @param type
	 */
	protected void reportBadBlock(DataBlock db,int type){
		
		logger.warn("reportBadBlock "+db.previous+"<-"+db + "->"+db.next + " type:"+type);
		
		this.hashcodeBlockMap.remove(db.hashcode);
		this.timeoutDBTree.remove(db);
		
		if(db.containsData){
			this.elementNum --;
			this.currentMemorySize -= db.keyLength;
			this.currentMemorySize -= db.valueLength;
			this.currentDiskSize -= db.keyLength;
			this.currentDiskSize -= db.valueLength;
			this.currentDiskSize -= DataBlock.HEADER_SIZE;
			db.keyLength=0;
			db.valueLength=0;
		}
		db.containsData = false;
		
		DataBlock prev = db.previous;
		if(prev == this.dbListHeader) prev = null;
		DataBlock next = db.next;
		if(next == this.dbListHeader) next = null;
		if(prev == null) 
			db.offset = 0;
		else
			db.offset = prev.offset + prev.length;
		
		if(next == null) 
			db.length = 0;
		else
			db.length = (int)(next.offset - db.offset);
	
		try {
			db.invalidateHeaderData(writer);
		} catch (Exception e) {
			logger.warn("invalidateHeaderData of "+db+" error",e);
		}
		
		//坏块不加入到空块数组中 以便将来恢复，并设置idletime为4231 标识这个块为坏块
		//this.freeDBTree.insert(db);
		if(db != null)
		{
			db.idletime=4231l;
		}
	}
	
	/**
	 * 用线程池执行删除通知操作
	 * @author <a href='mailto:myzdf.bj@gmail.com'>Yugang Wang</a>
	 * 2008-5-6
	 */
	static class DoRemovedListener implements Runnable{
		RemovedListener l;
		int type;
		public DoRemovedListener(RemovedListener l,int removeType){
			this.l = l;
			this.type = removeType;
		}
		public void run(){
			l.remove(type);
		}
	}

	/**
	 * 清空此Cache，同时删除对应的数据文件,
	 * 此方法不触发RemovedListener
	 */
	public synchronized void clear() {
		this.hashcodeBlockMap.clear();
		this.freeDBTree.clear();
		this.timeoutDBTree.clear();
		this.elementNum = 0;
		this.currentMemorySize = 0;
		this.currentDiskSize = 0;
		DataBlock db = dbListHeader.next;
		while(db != null && db != dbListHeader){
			db.key = null;
			db.value = null;
			db = db.next;
		}
		this.dbListHeader.next = this.dbListHeader;
		this.dbListHeader.previous = this.dbListHeader;
		
		try {
			RandomAccessFile w = this.getWriter();
			if(w != null){
				w.seek(0);
				w.write(ByteArrayUtils.numberToByteArray(0,4));
			}
			if(this.writer != null)
				this.writer.close();
			this.writer = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if(this.reader != null)
				this.reader.close();
			this.reader = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(this.dataFile != null && this.dataFile.exists()){
			if(!dataFile.delete())
				dataFile.deleteOnExit();
		}
	}

	/**
	 * 从小到大扫描timeoutDBTree，将超时Block的删除
	 */
	protected void removeTimeoutElements(){
		ArrayList<DataBlock> al = new ArrayList<DataBlock>();
		ArrayList<DataBlock> al2 = new ArrayList<DataBlock>();
		synchronized(this){
			TreeNode tn = this.timeoutDBTree.minimum();
			while(tn != null){
				DataBlock db = (DataBlock)tn.getObject();
				tn = this.timeoutDBTree.next(tn);
				if(db.idletime <= System.currentTimeMillis()){
					al.add(db);
				}else if(db.lifetime <= System.currentTimeMillis()){
					al2.add(db);
				}else{
					break;
				}
			}
		}
		for(int i = 0 ; i < al.size() ; i++){
			this.removeBlock(al.get(i),RemovedListener.IDEL_TIMEOUT);
		}
		
		for(int i = 0 ; i < al2.size() ; i++){
			this.removeBlock(al2.get(i),RemovedListener.LIFE_TIMEOUT);
		}
	}
	
	/**
	 * 判断是否需要删除部分Block，以腾出足够的磁盘空间
	 *
	 */
	protected void cutDiskSize(){
		if(this.currentDiskSize >= this.maxDiskSize * 0.95){
			synchronized(this){
				while(this.currentDiskSize > this.maxDiskSize * 0.90){
					TreeNode tn = this.timeoutDBTree.minimum();
					DataBlock db = (DataBlock)tn.getObject();
					int k = this.timeoutDBTree.size();
					this.removeBlock(db,RemovedListener.SIZE_OVERFLOW);
					if(k == this.timeoutDBTree.size()){
						//maybe dead loop
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 判断是否需要删除部分Block，以腾出足够的磁盘空间
	 *
	 */
	protected void cutElementSize(){
		if(this.elementNum >= this.maxElementNum * 0.99){
			synchronized(this){
				while(this.elementNum > this.maxElementNum * 0.98){
					TreeNode tn = this.timeoutDBTree.minimum();
					DataBlock db = (DataBlock)tn.getObject();
					int k = this.timeoutDBTree.size();
					this.removeBlock(db,RemovedListener.SIZE_OVERFLOW);
					if(k == this.timeoutDBTree.size()){
						//maybe dead loop
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 清空部分Block缓存的数据，以腾出足够的内存空间
	 *
	 */
	protected void cutMemorySize(){
		if(this.currentMemorySize >= this.maxMemorySize * 0.90){
			synchronized(this){
				TreeNode tn = this.timeoutDBTree.minimum();
				while(tn != null){
					DataBlock db = (DataBlock)tn.getObject();
					tn = this.timeoutDBTree.next(tn);
					if(db.value != null){
						db.value = null;
						this.currentMemorySize -= db.valueLength;
					}
					
					if(this.currentMemorySize < this.maxMemorySize * 0.75)
						break;
				}
			}
		}
		
		if(this.currentMemorySize >= this.maxMemorySize * 0.90){
			synchronized(this){
				TreeNode tn = this.timeoutDBTree.minimum();
				while(tn != null){
					DataBlock db = (DataBlock)tn.getObject();
					tn = this.timeoutDBTree.next(tn);
					if(db.key != null){
						db.key = null;
						this.currentMemorySize -= db.keyLength;
					}
					if(this.currentMemorySize < this.maxMemorySize * 0.75)
						break;
				}
			}
		}

	}
	
	static class ObjectWrapper implements Serializable{
		private static final long serialVersionUID = 6388289815128544044L;
		Serializable value;
		long lifeTimeout;
		long idleTimeout;
		ObjectWrapper(Serializable value,long lt,long it){
			this.value = value;
			this.lifeTimeout = lt;
			this.idleTimeout = it;
		}
	}
	
	public void enableShrinkerThread(boolean b){
		enableShrinkerThread = b;
	}
	
	/**
	 * 停止cache的后台进程运行，
	 * 并且将cache状态保存到索引文件中。
	 * 待下一次用这个索引文件创建cache时，cache会从索引文件中读取索引信息。
	 *
	 * 此方法不修改cache中数据的任何状态
	 */
	public void destory(){
		this.enableShrinkerThread = false;
		DiskCacheHelper.removeDiskCache(this);
		saveIndexedFile();
		if(this.writer != null){
			try {
				this.writer.getChannel().force(true);
				this.writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void run(){
		while(enableShrinkerThread){
			try{
				Thread.sleep(1000L);
				if(enableShrinkerThread == false) break;
				long start = System.currentTimeMillis();
				removeTimeoutElements();
				if(enableShrinkerThread == false) break;
				cutElementSize();
				if(enableShrinkerThread == false) break;
				cutDiskSize();
				if(enableShrinkerThread == false) break;
				cutMemorySize();
				if(enableShrinkerThread == false) break;
				if(this.writer != null){
					this.writer.getChannel().force(true);
				}
				if(logger.isDebugEnabled()) {
					logger.debug("["+name+"] [shrink] [耗时:"+(System.currentTimeMillis()-start)+"ms");
				}
			}catch(Throwable e){
				logger.warn("in shrinker thread catch exception:",e);
			}
		}
		logger.warn("shrinker thread stopped!");
		//shrinker stopped!
	}

	@Override
	void addCurrentMemorySize(int size) {
		this.currentMemorySize += size;
		
	}
}

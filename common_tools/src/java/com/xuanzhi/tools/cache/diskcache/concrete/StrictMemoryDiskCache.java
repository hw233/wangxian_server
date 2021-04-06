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
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache.ObjectWrapper;
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
 * 此实现不同于DefaultDiskCache的实现。
 * 
 * 内存结构：
 * 		2个平衡树和一个hash表
 * 		分别是：
 * 		1. free-blocktree:部分空闲数据块平衡树，此平衡树由部分无数据（key，value对）的块组成，按照数据块在文件中的循序排序
 * 		2. data-blocktree:部分超时数据块平衡树，此平衡树由部分含数据的块组成，按照数据块在文件中的循序排序
 * 		3. hashcode-blockmap:数据块hash表，hash表的key为数据块对应key的hashcode，值为数据块在文件中的位置，此hash表用于get方法
 * 		
 * 		注意：此实现不保证对两块连续的空闲数据块进行合并
 * 		
 * 		此实现中为了节省更多的内存，我们只在hash表中，保存所有的hashcode和offset的对应，也就是说，
 * 		如果我们cache中存入n个(key,value)对，那么我们这个hashcode就有n个entry，所以此hashcode占用的
 * 		内存大约为 n * (4 + 8) = 12n，如果n为500万，占用内存大约为60M，实际上占用的内存可能会在100M左右。
 * 
 * 		在free-blocktree，我们根据内存的情况，尽可能多的放入DataBlock对象，但是为了节约内存，必要的时候，可能在内存中只保留部分DataBlock对象
 * 		
 * 		在data-blocktree，我们根据内存的情况，尽可能多的放入DataBlock对象，但是为了节约内存，必要的时候，可能在内存中只保留部分DataBlock对象
 * 		
 * 		由于free-blocktree和data-blocktree只保留了部分的DataBlock，所以会衍生出一些新的问题：
 * 		
 * 		1.当add一个数据到cache中时，我们遍历free-blocktree，找到第一个空间大于所需空间的DataBlock就可以了。当遍历完free-blocktree
 * 		  不能找到合适的DataBlock，就在文件尾新加入一个DataBlock，即使现在有符合要求的DataBlock在文件中（不在内存）。
 * 		2.当remove一个数据时，我们得到一个新的free DataBlock，本来我们应该将此DataBlock和相邻的DataBlock合并，但是
 * 		  很有可能相邻的DataBlock不在内存中，这种情况下，我们不合并。也就是说，我们只对内存中相邻两个Free 	DataBlock合并。
 * 		3.当free-blocktree比较小时，同时有free-block在文件中没有读入到内存，我们会启动新的线程，对数据文件进行扫描，
 * 		  并且读入一部分free datablock。
 * 		4.某个DataBlock不在内存中，但是可能已经超时，这时我们无法知道数据文件中的数据超时，所以此实现对于超时时间可能不准确，
 *        也就是说，到了超时时间，也不一定会得到数据超时的通知。但是在取数据的时候，一旦发现数据已经超时，那么立即丢弃此数据。
 *      
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
public class StrictMemoryDiskCache extends AbstractDiskCache implements Runnable {

	static Logger logger = Logger.getLogger(StrictMemoryDiskCache.class);
	
	/**
	 * 无上限的线程池
	 */
	protected static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1,32,30,TimeUnit.SECONDS,new SynchronousQueue(),new ThreadPoolExecutor.CallerRunsPolicy());
	
	// free block list sort by size from small to big
	protected AvlTree freeDBTree = new AvlTree(new Comparator(){
		public int compare(Object o1, Object o2) {
			DataBlock d1 = (DataBlock)o1;
			DataBlock d2 = (DataBlock)o2;
			if(d1.offset < d2.offset) return -1;
			if(d1.offset > d2.offset) return 1;
			return 0;
		}
		
	});
	
	// access data block list sort by timeout from lately to far
	protected AvlTree dataDBTree = new AvlTree(new Comparator(){
		public int compare(Object o1, Object o2) {
			DataBlock d1 = (DataBlock)o1;
			DataBlock d2 = (DataBlock)o2;
			if(d1.offset < d2.offset) return -1;
			if(d1.offset > d2.offset) return 1;
			return 0;
		}
	});
	
	// hashcode map to data block list
	protected HashMap<Integer,Object> hashcodeOffsetMap = new HashMap<Integer,Object>(1024,0.75f);
	
	protected long defaultLifeTimeTimeout = 100L * 365L * 24L * 3600L * 1000L;
	protected long defaultIdleTimeout = 1800 * 1000;
	protected long maxDiskSize = 1024L * 1024L * 1024L * 10L;
	protected int maxMemorySize = 1024 * 1024 * 16;
	protected int maxElementNum = 1024 * 1024 * 10;
	
	protected int maxFreeDBTreeSize = 1024 * 100;
	protected int maxDataDBTreeSize = 1024 * 100;
	
	protected long currentDiskSize = 0;
	protected int currentMemorySize = 0;
	protected int elementNum = 0;
	protected long hitCount = 0;
	protected long missCount = 0;
	
	protected int datablockNum = 0;
	protected int freeblockNum = 0;
	
	//相关的存储属性
	protected File dataFile = null;
	protected File indexedFile = null;
	protected RandomAccessFile reader = null;
	protected RandomAccessFile writer = null;
	
	//关于Idle超时在系统宕机恢复后，是重新开始计时还是延续宕机前的状态，true表示延续宕机前的状态，默认为false
	protected boolean idleTimeContinousAfterSystemCrashing = false;
	
	//shrinker thread
	protected Thread thread;
	protected boolean running = true;
	
	protected String name;
	
	/**
	 * 数据文件尾
	 */
	long dataFileTailer = 0L;
	
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
	
	public void setMaxElementNum(int num){
		maxElementNum = num;
	}
	
	public int getMaxNumOfFreeBlockInMemory(){
		return maxFreeDBTreeSize;
	}
	
	public void setMaxNumOfFreeBlockInMemory(int num){
		maxFreeDBTreeSize = num;
	}
	
	public int getMaxNumOfDataBlockInMemory(){
		return maxDataDBTreeSize;
	}
	
	public void setMaxNumOfDataBlockInMemory(int num){
		maxDataDBTreeSize = num;
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
		return datablockNum + freeblockNum;
	}
	
	public int getFreeBlockNum(){
		return freeblockNum;
	}
	
	public int getDataBlockNum(){
		return datablockNum;
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
		
		return al.toArray(new DataBlock[0]);
	}
	
	public String toString(){
		long total = getCacheHits()+getCacheMisses();
		if(total == 0) total = 1;
		
		return "Strict{"+name+",NumElement:"+StringUtil.addcommas(getNumElements())+"/"+StringUtil.addcommas(getMaxElementNum())+",DataBlockNum:"+StringUtil.addcommas(dataDBTree.size())+"/"+StringUtil.addcommas(getMaxNumOfDataBlockInMemory())+"/"+StringUtil.addcommas(getDataBlockNum())+",FreeBlockNum:"+StringUtil.addcommas(freeDBTree.size())+"/"+StringUtil.addcommas(getMaxNumOfFreeBlockInMemory())+"/"+StringUtil.addcommas(getFreeBlockNum())
		+",Hits:"+(getCacheHits()*100/total)+"%,DiskSize:"+StringUtil.addcommas(getCurrentDiskSize()/1024)+"K/"+StringUtil.addcommas(getMaxDiskSize()/(1024*1024))+"M,MemSize:" +
		StringUtil.addcommas(getCurrentMemorySize()/1024)+"K/"+StringUtil.addcommas(getMaxMemorySize()/1024)+"K}";
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	//
	///////////////////////////////////////////////////////////////////////////////////////
	public StrictMemoryDiskCache(File dataFile){
		this(dataFile,null,"DiskCache-"+StringUtil.randomIntegerString(3),1800*1000L,false);
	}
	
	public StrictMemoryDiskCache(File dataFile,String name,long defaultIdleTimeout){
		this(dataFile,null,name,defaultIdleTimeout,false);
	}
	
	public StrictMemoryDiskCache(File dataFile,String name,long defaultIdleTimeout,boolean idleTimeContinousAfterSystemCrashing){
		this(dataFile,null,name,defaultIdleTimeout,idleTimeContinousAfterSystemCrashing);
	}

	
	public StrictMemoryDiskCache(File dataFile,File indexedFile,String name,long defaultIdleTimeout,boolean idleTimeContinousAfterSystemCrashing){
		if(dataFile.isDirectory() && dataFile.exists()) throw new IllegalArgumentException("data file must be a file");
		this.dataFile = dataFile;
		this.indexedFile = indexedFile;
		this.name = name;
		this.defaultIdleTimeout = defaultIdleTimeout;
		this.idleTimeContinousAfterSystemCrashing = idleTimeContinousAfterSystemCrashing;
		
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
	
	public StrictMemoryDiskCache(File dataFile,File indexedFile){
		this(dataFile,indexedFile,"DiskCache-"+StringUtil.randomIntegerString(3),1800*1000L,false);
	}
	
	protected void init() throws Exception{
		File f = dataFile.getParentFile();
		if(f.exists() == false) f.mkdirs();
		
		if(dataFile.isFile() && dataFile.exists()){
			reader = new RandomAccessFile(dataFile,"r");
			loadIndexFromDataFile(reader);
		}
		
		running = true;
		thread = new Thread(this,"StrictMemoryDiskCache-Shrinker-Thread");
		thread.start();
		
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
				b = false;
				logger.warn("load indexed file error cost "+(System.currentTimeMillis() - startTime)+" ms, so it will load index from data file:",e);
			}
		}
		if(b == false){
			int offset = 0;
			long fileLen = reader.length();
			while(offset < fileLen){
				DataBlock db = new DataBlock(this);
				db.offset = offset;
				db.readHeader(reader);
				if(db.length > 0 && db.checkValid() && offset + db.length <= fileLen){
					offset += db.length;
					this.dataFileTailer = offset;
					if(db.containsData){
						this.elementNum ++;
						this.datablockNum ++;
						this.currentDiskSize+= db.length;
						this.addToHashCodeOffsetMap(db);
						
						if(this.idleTimeContinousAfterSystemCrashing == false)
							db.idletime = System.currentTimeMillis() + this.defaultIdleTimeout;
						
						if(this.dataDBTree.size() < this.maxDataDBTreeSize){
							this.dataDBTree.insert(db);
						}
					}else{
						this.freeblockNum ++;
						if(this.freeDBTree.size() < this.maxFreeDBTreeSize){
							this.loadOneFreeBlock(db);
						}
							
					}
				}else{
					break;
				}
			}
		}
		
	}
	
	private static long INDEX_FLAG_VALID = 4783984958097979L;
	
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
			fout.write(ByteArrayUtils.numberToByteArray(this.dataFileTailer,8));
			fout.write(ByteArrayUtils.numberToByteArray(this.elementNum,8));
			fout.write(ByteArrayUtils.numberToByteArray(this.currentDiskSize,8));
			fout.write(ByteArrayUtils.numberToByteArray(this.datablockNum,8));
			fout.write(ByteArrayUtils.numberToByteArray(this.freeblockNum,8));
			total += 48;
			
			byte bytes[] = ByteArrayUtils.objectToByteArray(this.hashcodeOffsetMap);
			
			fout.write(ByteArrayUtils.numberToByteArray(bytes.length,4));
			fout.write(bytes);
			total += bytes.length;
			
			digest.update(ByteArrayUtils.numberToByteArray(INDEX_FLAG_VALID,8));
			digest.update(ByteArrayUtils.numberToByteArray(this.dataFileTailer,8));
			digest.update(ByteArrayUtils.numberToByteArray(this.elementNum,8));
			digest.update(ByteArrayUtils.numberToByteArray(this.currentDiskSize,8));
			digest.update(ByteArrayUtils.numberToByteArray(this.datablockNum,8));
			digest.update(ByteArrayUtils.numberToByteArray(this.freeblockNum,8));
			digest.update(bytes);
			bytes = digest.digest();
			
			fout.write(ByteArrayUtils.numberToByteArray(bytes.length,4));
			fout.write(bytes);
			total += bytes.length;
			
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
		
			byte bytes[] = new byte[48];
			int m = input.read(bytes);
			if(m != 48) throw new Exception("indexed file ["+indexedFile+"] format error, must be big than 12 bytes.");
			
			long flag = ByteArrayUtils.byteArrayToNumber(bytes,0,8);
			if(flag != INDEX_FLAG_VALID)
				throw new Exception("indexed file ["+indexedFile+"] format error, INDEX_FLAG_VALID not match.");
			
			dataFileTailer = ByteArrayUtils.byteArrayToNumber(bytes,8,8);
			elementNum = (int)ByteArrayUtils.byteArrayToNumber(bytes,16,8);
			currentDiskSize = ByteArrayUtils.byteArrayToNumber(bytes,24,8);
			datablockNum = (int)ByteArrayUtils.byteArrayToNumber(bytes,32,8);
			freeblockNum = (int)ByteArrayUtils.byteArrayToNumber(bytes,40,8);
			
			m = input.read(bytes,0,4);
			if(m != 4){
				throw new Exception("indexed file ["+indexedFile+"] format error, can't read block length.");
			}
			m = (int)ByteArrayUtils.byteArrayToNumber(bytes,0,4);
			if(m <= 0 || m >= 1024* 1024 * 1024)
				throw new Exception("indexed file ["+indexedFile+"] format error, hashmap length ["+m+"] invalid.");
			bytes = new byte[m];
			m = input.read(bytes);
			if(m != bytes.length)
				throw new Exception("indexed file ["+indexedFile+"] format error, read hashmap data error,not enough data.");
			
			MessageDigest	digest = MessageDigest.getInstance("MD5");
			digest.update(ByteArrayUtils.numberToByteArray(INDEX_FLAG_VALID,8));
			digest.update(ByteArrayUtils.numberToByteArray(this.dataFileTailer,8));
			digest.update(ByteArrayUtils.numberToByteArray(this.elementNum,8));
			digest.update(ByteArrayUtils.numberToByteArray(this.currentDiskSize,8));
			digest.update(ByteArrayUtils.numberToByteArray(this.datablockNum,8));
			digest.update(ByteArrayUtils.numberToByteArray(this.freeblockNum,8));
			digest.update(bytes);
			byte checksum2[] = digest.digest();
			
			this.hashcodeOffsetMap = (HashMap<Integer,Object>)ByteArrayUtils.byteArrayToObject(bytes,0,bytes.length);
			
			m = input.read(bytes,0,4);
			if(m != 4){
				throw new Exception("indexed file ["+indexedFile+"] format error, can't read checksum length.");
			}
			byte checksum[] = new byte[(int)ByteArrayUtils.byteArrayToNumber(bytes,0,4)];
			m = input.read(checksum);
			if(m != checksum.length){
				throw new Exception("indexed file ["+indexedFile+"] format error, can't read checksum data.");
			}
			
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
		
		TreeNode tn = freeDBTree.minimum();
		while(tn != null){
			DataBlock db = (DataBlock)tn.getObject();
			if(db.length > newLength) break;
			tn = freeDBTree.next(tn);
		}
		
		if(tn == null){//
			DataBlock db = new DataBlock(this);
			db.offset = dataFileTailer;
			db.length = newLength < DataBlock.MIN_BLOCK_SIZE?DataBlock.MIN_BLOCK_SIZE:newLength;
			db.containsData = true;
			db.hashcode = key.hashCode();
			db.lifetime = System.currentTimeMillis() + value.lifeTimeout;
			db.idletime = System.currentTimeMillis() + value.idleTimeout;
			db.keyLength = keyBytes.length;
			db.key = key;
			db.valueLength = valueBytes.length;
			db.value = value;
			
			
			//	更新数据文件
			try{
				db.invalidateAllData(getWriter(),keyBytes,valueBytes);
			}catch(Exception e){
				logger.warn("invalidateAllData of "+db+" error:",e);
			}
			//更新文件尾
			dataFileTailer += db.length;
			
			//更新内存
			dataDBTree.insert(db);
			this.datablockNum ++;
			
			this.addToHashCodeOffsetMap(db);
			
		}else{
			DataBlock db = (DataBlock)tn.getObject();
			if(db.length < newLength + DataBlock.HEADER_SIZE || db.length <= DataBlock.MIN_BLOCK_SIZE){ //不拆分
				//更新内存
				freeDBTree.remove(db);
				this.freeblockNum --;
				
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
				
				dataDBTree.insert(db);
				this.datablockNum ++;
				this.addToHashCodeOffsetMap(db);
				
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
				freeDBTree.remove(db);
				freeDBTree.insert(dbB);
				dataDBTree.insert(dbA);
				this.datablockNum ++;
				
				this.addToHashCodeOffsetMap(dbA);
			}
		}
		this.elementNum ++;
		this.currentMemorySize += keyBytes.length + valueBytes.length;
		this.currentDiskSize +=  keyBytes.length + valueBytes.length + DataBlock.HEADER_SIZE;
	}
	
	private void addToHashCodeOffsetMap(DataBlock db){
		Object obj = hashcodeOffsetMap.get(db.hashcode);
		if(obj == null){
			hashcodeOffsetMap.put(db.hashcode,db.offset);
		}else{
			if(obj instanceof Long){
				long [] oo = new long[2];
				oo[0] = (Long)obj;
				oo[1] = db.offset;
				hashcodeOffsetMap.put(db.hashcode,oo);
			}else{
				long[] oo = (long[])obj;
				long[] newO = new long[oo.length+1];
				System.arraycopy(oo,0,newO,0,oo.length);
				newO[oo.length] = db.offset;
				hashcodeOffsetMap.put(db.hashcode,newO);
			}
		}
	}
	
	private void removeHashCodeOffsetMap(DataBlock db){
		Object obj = hashcodeOffsetMap.get(db.hashcode);
		if(obj == null){
			return;
		}else{
			if(obj instanceof Long){
				long l = (Long)obj;
				if(l == db.offset)
					hashcodeOffsetMap.remove(db.hashcode);
			}else{
				long[] oo = (long[])obj;
				int c = -1;
				for(int i = 0 ; i < oo.length ; i++){
					if(db.offset == oo[i]){
						c = i;
						break;
					}
				}
				if(c >= 0 && oo[c] == db.offset){
					if(oo.length == 2){
						long l = oo[0];
						if(c == 0) l = oo[1];
						hashcodeOffsetMap.put(db.hashcode,l);
					}else{
						long[] newO = new long[oo.length-1];
						System.arraycopy(oo,0,newO,0,c);
						System.arraycopy(oo,c+1,newO,c,oo.length-1-c);
						hashcodeOffsetMap.put(db.hashcode,newO);
					}
				}
			}
		}
	}
	
	private DataBlock findDataBlock(long offset){
		DataBlock tmp = new DataBlock(this);
		tmp.offset = offset;
		
		TreeNode tn = this.dataDBTree.find(tmp);
		if(tn != null) 
			return (DataBlock)tn.getObject();
		try{
			tmp.readHeader(getReader());
			if(tmp.idletime < System.currentTimeMillis()){
				this.removeBlock(tmp,RemovedListener.IDEL_TIMEOUT);
			}else if(tmp.lifetime < System.currentTimeMillis()){
				this.removeBlock(tmp,RemovedListener.LIFE_TIMEOUT);
			}else{
				this.dataDBTree.insert(tmp);
				return tmp;
			}
		}catch(Exception e){
			logger.warn("findDataBLock of "+offset+" error:",e);
		}
		return null;
	}
	
	/**
	  * get：通过hashcode-blockmap找到key的hashcode对应的block列表，遍历此列表，找到对应的block，设为X
	  * 		如果X不为null，修改X的idletime，并且将X从timeout-blocktree中删除，再插入
	  *      同时，根据配置，看是否要修改数据文件
 	  */
	public synchronized Serializable get(Serializable key) {
		this.cutMemorySize();
		
		int hashcode = key.hashCode();
		DataBlock db = null;
		Object of = this.hashcodeOffsetMap.get(hashcode);
		if(of != null){
			long offsets[] = null;
			if(of instanceof Long){
				offsets = new long[1];
				offsets[0] = (Long)of;
			}else{
				offsets = (long[])of;
			}
			for(int i = 0 ; i < offsets.length ; i++){
				DataBlock _db = findDataBlock(offsets[i]);
				if(_db == null) continue;
				Object keyObj = null;
				try {
					keyObj = _db.getKey(getReader());
				} catch (Exception e) {
					logger.warn("get key of "+_db+" error",e);
				}
				if(keyObj != null && keyObj.equals(key)){
					db = _db;
					break;
				}
			}
		}
		ObjectWrapper obj = null;
		if(db != null){
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
		Object of = this.hashcodeOffsetMap.get(hashcode);
		if(of != null){
			long offsets[] = null;
			if(of instanceof Long){
				offsets = new long[1];
				offsets[0] = (Long)of;
			}else{
				offsets = (long[])of;
			}
			for(int i = 0 ; i < offsets.length ; i++){
				DataBlock _db = findDataBlock(offsets[i]);
				if(_db == null) continue;
				Object keyObj = null;
				try {
					keyObj = _db.getKey(getReader());
				} catch (Exception e) {
					logger.warn("get key of "+_db+" error",e);
				}
				if(keyObj != null && keyObj.equals(key)){
					db = _db;
					break;
				}
			}
		}
		
		if(db != null){
			removeBlock(db,removeType);
		}
	}	
	
	protected synchronized void removeBlock(DataBlock db,int removeType){
		
		if(db != null){
			this.removeHashCodeOffsetMap(db);
			
			if(db.containsData == false){
				db.key = null;
				db.value = null;
				this.dataDBTree.remove(db);
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
			
			TreeNode tn = this.freeDBTree.findNearestBigger(db);
			DataBlock nextDB = null;
			if(tn != null){
				nextDB = (DataBlock)tn.getObject();
				if(db.offset + db.length != nextDB.offset)
					nextDB = null;
			}
			DataBlock prevDB = null;
			if(tn != null){
				tn = freeDBTree.previous(tn);
			}else{
				tn = freeDBTree.maximum();
			}
			if(tn != null){
				prevDB = (DataBlock)tn.getObject();
				if(prevDB.offset + prevDB.length != db.offset)
					prevDB = null;
			}

			if(prevDB != null && nextDB != null ){
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
				
				this.dataDBTree.remove(db);
				this.datablockNum --;
				this.freeDBTree.remove(prevDB);
				this.freeblockNum --;
				this.freeDBTree.remove(nextDB);
				this.freeblockNum --;
				this.freeDBTree.insert(newDB);
				this.freeblockNum ++;
				
			}else if(prevDB != null){
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
				
				this.dataDBTree.remove(db);
				this.datablockNum --;
				this.freeDBTree.remove(prevDB);
				this.freeblockNum --;
				this.freeDBTree.insert(newDB);
				this.freeblockNum ++;
				
			}else if(nextDB != null){
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
				
				this.dataDBTree.remove(db);
				this.datablockNum --;
				this.freeDBTree.remove(nextDB);
				this.freeblockNum --;
				this.freeDBTree.insert(newDB);
				this.freeblockNum ++;
				
			}else{
				try {
					db.invalidateHeaderData(getWriter());
				} catch (Exception e) {
					logger.warn("invalidateHeaderData of "+db+" error",e);
				}
				this.dataDBTree.remove(db);
				this.datablockNum --;
				this.freeDBTree.insert(db);
				this.freeblockNum ++;
			}
			
			if(value instanceof RemovedListener){
				DoRemovedListener command = new DoRemovedListener((RemovedListener)value,removeType);
				threadPool.execute(command);
			}
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
		
		logger.warn("reportBadBlock "+db.previous+"<-"+db + "->"+db.next + " and type is " + type);
		
		
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
		this.hashcodeOffsetMap.clear();
		this.freeDBTree.clear();
		this.dataDBTree.clear();
		this.elementNum = 0;
		this.currentMemorySize = 0;
		this.currentDiskSize = 0;
		this.datablockNum = 0;
		this.freeblockNum = 0;
		this.dataFileTailer = 0;
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
			TreeNode tn = this.dataDBTree.minimum();
			while(tn != null){
				DataBlock db = (DataBlock)tn.getObject();
				tn = this.dataDBTree.next(tn);
				if(db.idletime <= System.currentTimeMillis()){
					al.add(db);
				}else if(db.lifetime <= System.currentTimeMillis()){
					al2.add(db);
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
					TreeNode tn = this.dataDBTree.maximum();
					DataBlock db = (DataBlock)tn.getObject();
					int k = this.dataDBTree.size();
					this.removeBlock(db,RemovedListener.SIZE_OVERFLOW);
					if(k == this.dataDBTree.size()){
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
				TreeNode tn = this.dataDBTree.minimum();
				while(tn != null){
					DataBlock db = (DataBlock)tn.getObject();
					tn = this.dataDBTree.next(tn);
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
				TreeNode tn = this.dataDBTree.minimum();
				while(tn != null){
					DataBlock db = (DataBlock)tn.getObject();
					tn = this.dataDBTree.next(tn);
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
	
	/**
	 * 判断是否需要删除部分Block，以腾出足够的磁盘空间
	 *
	 */
	protected void cutElementSize(){
		if(this.elementNum >= this.maxElementNum){
			synchronized(this){
				while(this.elementNum >= this.maxElementNum){
					TreeNode tn = this.dataDBTree.maximum();
					DataBlock db = (DataBlock)tn.getObject();
					int k = this.dataDBTree.size();
					this.removeBlock(db,RemovedListener.SIZE_OVERFLOW);
					if(k == this.dataDBTree.size()){
						//maybe dead loop
						break;
					}
				}
			}
		}
	}
	
	protected void cutDataDBTreeSize(){
		if(this.dataDBTree.size() > this.maxDataDBTreeSize){
			synchronized(this){
				while(this.dataDBTree.size() > this.maxDataDBTreeSize * 0.98){
					TreeNode tn = this.dataDBTree.maximum();
					DataBlock db = (DataBlock)tn.getObject();
					if(db.value != null){
						db.value = null;
						this.currentMemorySize -= db.valueLength;
					}
					if(db.key != null){
						db.key = null;
						this.currentMemorySize -= db.keyLength;
					}
					this.dataDBTree.remove(tn);
				}
			}
		}
	}
	
	protected void cutFreeDBTreeSize(){
		if(this.freeDBTree.size() > this.maxFreeDBTreeSize){
			synchronized(this){
				ArrayList<DataBlock> al = new ArrayList<DataBlock>();
				TreeNode tn = this.freeDBTree.minimum();
				while(tn != null){
					DataBlock db = (DataBlock)tn.getObject();
					if(db.length < DataBlock.MIN_BLOCK_SIZE){
						al.add(db);
					}
				}
				for(int i = 0 ; i < al.size() ; i++){
					DataBlock db = al.get(i);
					freeDBTree.remove(db);
					if(this.freeDBTree.size() < this.maxFreeDBTreeSize * 0.98) break;
				}
				
				while(this.freeDBTree.size() > this.maxFreeDBTreeSize * 0.98){
					tn = this.freeDBTree.minimum();
					this.freeDBTree.remove(tn);
				}
			}
		}
	}
	
	protected void loadFreeDBTreeSize(){
		if(this.freeDBTree.size() < this.maxFreeDBTreeSize * 0.5 && this.freeDBTree.size() <  this.freeblockNum * 0.5){
			RandomAccessFile r = null;
			try{
				r = new RandomAccessFile(this.dataFile,"r");
				long fileLen = r.length();
				long offset = 0;
				int count = 0;
				LinkedList<DataBlock> al = new LinkedList<DataBlock>();
				while(true){
					DataBlock db = new DataBlock(this);
					db.offset = offset;
					db.readHeader(r);
					if(db.containsData == false){
						count ++;
						al.add(db);
					}
					if(count >= maxFreeDBTreeSize) break;
					if(db.offset + db.length >= fileLen) break;
					if(db.checkValid() == false) break;
				}
				
				Iterator<DataBlock> it = al.iterator();
				while(it.hasNext()){
					DataBlock db = it.next();
					synchronized(this){
						TreeNode tn = this.freeDBTree.find(db);
						if(tn == null){
							loadOneFreeBlock(db);
						}
					}
				}
			}catch(Exception e){
				logger.warn("loadFreeDBTreeSize error:",e);
			}finally{
				try {
					r.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected void loadOneFreeBlock(DataBlock db){
		TreeNode tn = this.freeDBTree.findNearestBigger(db);
		DataBlock nextDB = null;
		if(tn != null){
			nextDB = (DataBlock)tn.getObject();
			if(db.offset + db.length != nextDB.offset)
				nextDB = null;
		}
		DataBlock prevDB = null;
		if(tn != null){
			tn = freeDBTree.previous(tn);
		}else{
			tn = freeDBTree.maximum();
		}
		if(tn != null){
			prevDB = (DataBlock)tn.getObject();
			if(prevDB.offset + prevDB.length != db.offset)
				prevDB = null;
		}

		if(prevDB != null && nextDB != null ){
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

			this.freeDBTree.remove(prevDB);
			this.freeblockNum --;
			this.freeDBTree.remove(nextDB);
			this.freeblockNum --;
			this.freeDBTree.insert(newDB);
		}else if(prevDB != null){
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
			
			this.freeDBTree.remove(prevDB);
			this.freeblockNum --;
			this.freeDBTree.insert(newDB);
			
		}else if(nextDB != null){
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
			
			this.freeDBTree.remove(nextDB);
			this.freeblockNum --;
			this.freeDBTree.insert(newDB);
			
		}else{
			this.freeDBTree.insert(db);
		}
	}
		
	/**
	 * 停止cache的后台进程运行，
	 * 并且将cache状态保存到索引文件中。
	 * 待下一次用这个索引文件创建cache时，cache会从索引文件中读取索引信息。
	 *
	 * 此方法不修改cache中数据的任何状态
	 */
	public void destory(){
		running = false;
		DiskCacheHelper.removeDiskCache(this);
		saveIndexedFile();
		if(this.writer != null){
			try {
				this.writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void run(){
		while(running){
			try{
				Thread.sleep(1000L);
				if(running == false) break;
				removeTimeoutElements();
				if(running == false) break;
				cutElementSize();
				if(running == false) break;
				cutDiskSize();
				if(running == false) break;
				cutMemorySize();
				if(running == false) break;
				cutDataDBTreeSize();
				if(running == false) break;
				cutFreeDBTreeSize();
				if(running == false) break;
				if(this.writer != null){
					this.writer.getChannel().force(true);
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

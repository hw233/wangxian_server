package com.xuanzhi.tools.filesystem;

import java.io.*;


import java.util.*;



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
public class DefaultVirtualFileSystem{

	static Logger logger = Logger.getLogger(DefaultVirtualFileSystem.class);

	
	//double list of data blocks
	protected DataBlock dbListHeader = new DataBlock(this);
	
	// free block list sort by size from small to big
	protected DBTree freeDBTree = new DBTree();
	
	// hashcode map to data block list
	protected HashMap<String,DataBlock> blockMap = new HashMap<String,DataBlock>(1001,0.75f);
	
	protected int maxMemorySize = 1024 * 1024 * 16;
	
	protected long currentDiskSize = 0;
	protected int currentMemorySize = 0;
	protected int elementNum = 0;

	
	//相关的存储属性
	protected File dataFile = null;

	protected RandomAccessFile reader = null;
	protected RandomAccessFile writer = null;
	
	protected String name;
	
	protected byte headers[] = new byte[64];
	
	public byte[] getHeaders(){
		return headers;
	}
	
	/**
	 * offset为在头中偏移量
	 * 
	 * @param bytes
	 * @param offset
	 */
	public void setHeaders(byte bytes[],int offset){
		if(offset >= headers.length) return;
		System.arraycopy(bytes, 0, headers, offset,Math.min(bytes.length, headers.length-offset));
		try {
			writer.seek(0);
			writer.write(headers);
		} catch (IOException e) {
			logger.warn("write headers failed!",e);
		}
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	// 
	///////////////////////////////////////////////////////////////////////////////////////

	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
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
		return blockMap.size();
	}
	
	public int getFreeBlockNum(){
		return this.freeDBTree.size();
	}
	
	public int getDataBlockNum(){
		return elementNum ;
	}
	
	public File getDataFile(){
		return dataFile;
	}
	

	
	/**
	 * 获取所有的文件名
	 * @return
	 */
	public String[] getFileNames(){
		ArrayList<String> al = new ArrayList<String>();
		DataBlock db = this.dbListHeader.next;
		while(db != null && db != dbListHeader){
			if(db.containsData){
				al.add(db.fileName);
			}
			db = db.next;
		}
		return al.toArray(new String[0]);
	}
	
	/**
	 * 获得此Cache所有的数据块的拷贝，对返回的对象进行修改不会影响此Cache的状态
	 * @return
	 */
	public DataBlock[] getDataBlocks(){
		ArrayList<DataBlock> al = new ArrayList<DataBlock>();
		DataBlock db = this.dbListHeader.next;
		while(db != null && db != dbListHeader){
			DataBlock tmp = new DataBlock(null);
			tmp.offset = db.offset;
			tmp.length = db.length;
			tmp.containsData = db.containsData;
			tmp.reserve = db.reserve;
			tmp.keyLength = db.keyLength;
			tmp.valueLength = db.valueLength;
			tmp.fileName = db.fileName;
			tmp.fileContent = db.fileContent;
			al.add(tmp);
			
			db = db.next;
		}
		return al.toArray(new DataBlock[0]);
	}
	
	public String toString(){
	
		return "Default{"+name+",file="+this.dataFile.getPath()+",num="+getNumElements()+",dbNum="+this.getDataBlockNum()+",freeNum="+this.getFreeBlockNum()+",distSize="+(getCurrentDiskSize()/(1024*1024))+"M,memory=" +
		(getCurrentMemorySize()/1024)+"K/"+(getMaxMemorySize()/1024)+"K}";
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	//
	///////////////////////////////////////////////////////////////////////////////////////

		
	
	public DefaultVirtualFileSystem(File dataFile,String name){
		if(dataFile.isDirectory() && dataFile.exists()) throw new IllegalArgumentException("data file must be a file");
		this.dataFile = dataFile;
		this.name = name;
		
		dbListHeader.next = dbListHeader;
		dbListHeader.previous = dbListHeader;

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
	
	protected void init() throws Exception{
		File f = dataFile.getParentFile();
		if(f.exists() == false) f.mkdirs();
		
		if(dataFile.isFile() && dataFile.exists()){
			reader = new RandomAccessFile(dataFile,"r");
			loadIndexFromDataFile(reader);
		}else{
			RandomAccessFile w = getWriter();
			w.write(headers);
		}
	}
	
	
	protected void loadIndexFromDataFile(RandomAccessFile reader) throws Exception{
		long offset = 0;
		long fileLen = reader.length();
		
		reader.read(headers);
		
		offset += headers.length;
		
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
	
		
		DataBlock db = dbListHeader.next;
		while(db != dbListHeader && db != null){
			if(db.containsData){
				this.elementNum++;
				this.currentDiskSize+= db.length;
				
				blockMap.put(db.fileName,db);
			}else{
				if(db.reserve  != DataBlock.BAD_BLOCK_FLAG)
					this.freeDBTree.insert(db);
			}
			db = db.next;
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
	
	public synchronized void put(String fileName,int version,byte[] fileData) {

		this.remove(fileName,0);
		try {
			this.add(fileName,version,fileData);
		} catch (Exception e) {
			logger.warn("add item{"+fileName+",version="+version+",length="+fileData.length+"} error",e);
		}
	}

	/**
	 * 此方法用于向文件中加入一个带drop标记的块，用于在后续的文件合并中，删除指定的文件。
	 * 
	 * @param fileName
	 */
	public synchronized void addDropFlag(String fileName) {

		this.remove(fileName,0);
		try {
			byte keyBytes[] = fileName.getBytes();
			byte valueBytes[] = new byte[0];
			
			
			DataBlock db = new DataBlock(this);
			db.offset = headers.length;
			db.length = DataBlock.MIN_BLOCK_SIZE;
			db.containsData = true;
			db.reserve = DataBlock.DROP_BLOCK_FLAG;
			db.version = DataBlock.DROP_BLOCK_VERSION;
			db.keyLength = keyBytes.length;
			db.fileName = fileName;
			db.valueLength = valueBytes.length;
			db.fileContent = valueBytes;
			
			DataBlock lastBlock = this.dbListHeader.previous;
			if(lastBlock == dbListHeader) lastBlock = null;
			if(lastBlock != null){
				db.offset = lastBlock.offset + lastBlock.length;
			}
			//更新数据文件
			db.invalidateAllData(getWriter());
			
			db.fileContent = null;
			//更新内存
			dbListHeader.addPrevious(db);
			
			blockMap.put(db.fileName,db);
			this.currentDiskSize +=  db.length;
		} catch (Exception e) {
			logger.warn("add addDropFlag{"+fileName+",version="+DataBlock.DROP_BLOCK_VERSION+",length=0} error",e);
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
	protected void add(String fileName,int version,byte[] fileData) throws Exception{
		byte keyBytes[] = fileName.getBytes();
		byte valueBytes[] = fileData;
		
		int newLength = DataBlock.HEADER_SIZE + keyBytes.length  + valueBytes.length;

		DataBlock tmp = new DataBlock(this);
		tmp.length = newLength;
		tmp.offset=-1;
		DataBlock tn = freeDBTree.findNearestBigger(tmp);
		
		if(tn == null){//
			DataBlock db = new DataBlock(this);
			db.offset = headers.length;
			db.length = newLength < DataBlock.MIN_BLOCK_SIZE?DataBlock.MIN_BLOCK_SIZE:newLength;
			db.containsData = true;
			db.reserve = 0;
			db.version = version;
			db.keyLength = keyBytes.length;
			db.fileName = fileName;
			db.valueLength = valueBytes.length;
			db.fileContent = valueBytes;
			
			DataBlock lastBlock = this.dbListHeader.previous;
			if(lastBlock == dbListHeader) lastBlock = null;
			if(lastBlock != null){
				db.offset = lastBlock.offset + lastBlock.length;
			}
			//更新数据文件
			db.invalidateAllData(getWriter());
			
			db.fileContent = null;
			//更新内存
			dbListHeader.addPrevious(db);
			
			blockMap.put(db.fileName,db);
			this.currentDiskSize +=  db.length;
		}else{
			DataBlock db = tn;
			if(db.length < newLength + DataBlock.HEADER_SIZE || db.length <= DataBlock.MIN_BLOCK_SIZE){ //不拆分
				//更新内存
				freeDBTree.remove(db);
				
				db.containsData = true;
				db.reserve = 0;
				db.version = version;
				db.keyLength = keyBytes.length;
				db.fileName = fileName;
				db.valueLength = valueBytes.length;
				db.fileContent = valueBytes;
				
				//更新数据文件
				db.invalidateAllData(getWriter());
				
				db.fileContent = null;
				
				blockMap.put(db.fileName,db);
				
				this.currentDiskSize +=  db.length;
				
			}else{ //需要拆分Block
				DataBlock dbA = new DataBlock(this);
				dbA.offset =db.offset;
				dbA.length = newLength ;
				dbA.containsData = true;
				dbA.reserve = 0;
				dbA.version = version;
				dbA.keyLength = keyBytes.length;
				dbA.fileName = fileName;
				dbA.valueLength = valueBytes.length;
				dbA.fileContent = valueBytes;
				
				DataBlock dbB = new DataBlock(this);
				dbB.offset = dbA.offset + dbA.length;
				dbB.length = db.length - dbA.length ;
				dbB.containsData = false;
				dbB.reserve = 0;
				dbB.version = 0;
				
				//更新数据文件
				try{
					dbB.invalidateHeaderData(getWriter());
				}catch(Exception e){
					logger.warn("invalidateHeaderData of "+dbB+" error:",e);
				}
				try{
					dbA.invalidateAllData(getWriter());
				}catch(Exception e){
					logger.warn("invalidateAllData of "+dbA+" error:",e);
				}
				
				dbA.fileContent = null;
				this.currentDiskSize +=  dbA.length;
				
				//更新内存
				db.addPrevious(dbB);
				dbB.addPrevious(dbA);
				db.remove();
				
				freeDBTree.remove(db);
				freeDBTree.insert(dbB);
				
				blockMap.put(dbA.fileName,dbA);
			}
		}
		this.elementNum ++;
		
		
	}
	
	/**
	 * 返回对应文件的版本，如果文件不存在，返回-1
 	 */
	public synchronized int getFileVersion(String fileName) {
		this.cutMemorySize();
		
		DataBlock db = blockMap.get(fileName);
		if(db != null){
			return db.version;
		}
		return -1;
	}
	
	/**
	 * 返回对应文件的版本，如果文件不存在，返回-1
 	 */
	public synchronized byte[] getFileData(String fileName) throws Exception{
		this.cutMemorySize();
		
		DataBlock db = blockMap.get(fileName);
		if(db != null){
			byte[] data =  db.getValue(reader);
			
			if(db.fileContent != null){
				this.currentMemorySize -= db.valueLength;
				db.fileContent = null;
			}
			return data;
		}
		return null;
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
	public void remove(String fileName) {
		remove(fileName,1);
	}	
		
		
	protected synchronized void remove(String key,int removeType) {
		DataBlock db =  blockMap.get(key);

		if(db != null){
			removeBlock(db,removeType);
		}
		
		logger.warn("remove data successful,key:"+key+" removetype:"+removeType);
	}	
	
	protected synchronized void removeBlock(DataBlock db,int removeType){
		
		if(db != null){
			blockMap.remove(db.fileName);
			
			if(db.containsData == false){
				db.fileName = null;
				db.fileContent = null;
				if(this.freeDBTree.contains(db) == false)
					this.freeDBTree.insert(db);
				try{
					throw new Exception("the removing block is free,why?");
				}catch(Exception e){
					logger.warn("remove block of "+db+" error",e);
					return;
				}
			}

			
			db.containsData = false;
			db.fileName = null;
			
			if(db.fileContent != null){
				this.currentMemorySize -= db.valueLength;
				db.fileContent = null;
			}
			
			this.elementNum --;
			this.currentDiskSize -= db.length;
			
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
				
				this.freeDBTree.remove(nextDB);
				this.freeDBTree.insert(newDB);
				
			}else{
				try {
					db.invalidateHeaderData(getWriter());
				} catch (Exception e) {
					logger.warn("invalidateHeaderData of "+db+" error",e);
				}
				this.freeDBTree.insert(db);
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
		
		logger.warn("reportBadBlock "+db.previous+"<-"+db + "->"+db.next + " type:"+type);
		
		this.blockMap.remove(db.fileName);
		
		if(db.containsData){
			this.elementNum --;
			
			if(db.fileContent != null){
				this.currentMemorySize -= db.valueLength;
			}
			this.currentDiskSize -= db.length;
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
	
		//坏块不加入到空块数组中 以便将来恢复，并设置idletime为4231 标识这个块为坏块
		db.reserve = DataBlock.BAD_BLOCK_FLAG;
		
		try {
			db.invalidateHeaderData(writer);
		} catch (Exception e) {
			logger.warn("invalidateHeaderData of "+db+" error",e);
		}

	}

	/**
	 * 清空此Cache，同时删除对应的数据文件,
	 * 此方法不触发RemovedListener
	 */
	public synchronized void clear() {
		this.blockMap.clear();
		this.freeDBTree.clear();

		this.elementNum = 0;
		this.currentMemorySize = 0;
		this.currentDiskSize = 0;
		DataBlock db = dbListHeader.next;
		while(db != null && db != dbListHeader){
			db.fileName = null;
			db.fileContent = null;
			db = db.next;
		}
		this.dbListHeader.next = this.dbListHeader;
		this.dbListHeader.previous = this.dbListHeader;
		
		try {
			RandomAccessFile w = this.getWriter();
			if(w != null){
				w.seek(0);
				w.write(this.headers);
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
	 * 清空部分Block缓存的数据，以腾出足够的内存空间
	 *
	 */
	protected void cutMemorySize(){
		
	}
	
	/**
	 * 停止cache的后台进程运行，
	 * 并且将cache状态保存到索引文件中。
	 * 待下一次用这个索引文件创建cache时，cache会从索引文件中读取索引信息。
	 *
	 * 此方法不修改cache中数据的任何状态
	 */
	public void close(){
		if(this.writer != null){
			try {
				this.writer.getChannel().force(true);
				this.writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(this.reader != null){
			try {
				this.reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	void addCurrentMemorySize(int size) {
		this.currentMemorySize += size;
		
	}
	
	/**
	 * 将另外一个文件系统合并到这个文件系统中
	 * @param fs
	 */
	public void merge(DefaultVirtualFileSystem fs){
		if(this == fs) return;
		
		String fileNames[] = fs.getFileNames();
		for(int i = 0 ; i < fileNames.length ; i++){
			String fileName = fileNames[i];
			DataBlock db2 = fs.blockMap.get(fileName);
			DataBlock db = blockMap.get(fileName);
			
			if(db2 != null && db2.reserve != DataBlock.BAD_BLOCK_FLAG && db2.reserve != DataBlock.DROP_BLOCK_FLAG
					&& db2.version != DataBlock.DROP_BLOCK_VERSION
					&& (db == null || db.version < db2.version)){
				byte data[];
				try {
					data = fs.getFileData(fileName);
					this.put(fileName, db2.version, data);
				} catch (Exception e) {
					logger.warn("merge filesystem["+fs.getName()+","+fs.getDataFile()+"] to filesystem["+this.getName()+","+this.getDataFile()+"] error",e);
				}
				
			}else if(db2 != null && db2.reserve == DataBlock.DROP_BLOCK_FLAG && db2.version == DataBlock.DROP_BLOCK_VERSION && db != null){
				this.remove(fileName);
			}
		}
		
	}
}

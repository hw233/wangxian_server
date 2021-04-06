package com.xuanzhi.tools.queue;

import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;
import com.xuanzhi.tools.text.StringUtil;

/**
 * <pre>
 * 高级文件缓存队列，此实现保证系统重启的时候不会丢失数据，
 * 即时断电，也是只丢失正在处理的对象。而且系统重启的时候，
 * 不需要额外的处理时间。
 * 
 * 但是此队列的处理速度会有影响，因为每次取对象和保存对象，
 * 都是存在文件系统的IO操作。
 * 
 * 它的实现思想如下：
 * 用一个文件保存，当前读取的位置，每次读取，都会更新此文件的内容。
 * 此文件的内容保存两个变量：
 * 	<file-name> <offset> 
 * 其中 file-name表明文件名称，offset表明已经读取到什么位置。
 * 
 * 用一个文件保存，当前写入的位置，每次写入，都会更新此文件的内容。
 * 此文件的内容保存两个变量：
 * 	<file-name> <offset> 
 * 其中 file-name表明文件名称，offset表明已经读取到什么位置。
 * 
 * 
 * 队列实现如下：
 * 		  F(n)        F(n+1)       F(n+2)
 * 		__________   _________    __________
 *     |----------| |----------| |----------|
 *     |----------| |----------| |----------|
 *     |----------| |----------| |----------|
 *     |----------| |----------| |----------|
 head->|----------| |----------| |          |<-tail
 *     |----------| |----------| |          |
 *     |----------| |----------| |          |
 *     |----------| |----------| |          |
 *     |----------| |----------| |----------|
 * 
 * 2007-10-03：
 *    这个队列实现于2007年4，5月份，实现完后就投入了使用。我一直都以为
 *    没有问题。为什么以为没有问题是因为没有问题的反馈。我们不断的输出日志，
 *    但是日志太多太快，根本就不可能看出任何问题。
 *    
 *    直到最近我做了一个图形化显示队列使用的情况，才感觉有点问题。
 *    
 *    因为是测试系统，所以队列中经常被我盖满对象，而且测试系统经常重启，
 *    有时候就感觉到重启前队列的显示和重启后队列的显示不一样。但是也
 *    只是偶尔的情况，不是每次都这样。并且单元测试都没有发现过问题。
 *    
 *    正好十一我没事，经过大概3天左右的不断跟踪测试，我终于发现我设计上
 *    的问题。
 *    
 *    最大的问题是：在大量使用队列的情况下，如果让系统宕机，有时候会导致
 *    宕机前的状态信息丢失。
 *    
 *    开始的时候，我用一个文件来存储头文件和头文件读取指针，而尾文件和尾
 *    文件写入指针我没有存储，就用最后一个文件和文件的长度作为尾指针。
 *    一般情况下，这种做法是对的，但是特殊情况下，会出现错误。
 *    
 *    比如在下面这种情况下，就会出现问题：
 *       当某个线程调用push方法，在push方法中，执行到将输入写入文件中，
 *       比如要写入1k的数据，结果刚写入到200字节的时候，系统宕机了。
 *       这种情况下，虽然宕机前tailerPointer没有变化，但是尾文件中多
 *       写入了200个字节。
 *       
 *       重启后，我们自然的用尾文件的长度作为tailerPointer，这样重启
 *       前和重启后tailerPointer发生的变化，导致队列在判断是否为空
 *       的时候出现错误（tailerPointer比headerPointer始终要多200个字节）。
 *       
 *       最终导致不可预知的问题。
 *    
 *    解决的办法：
 *       和对待头文件和头指针一样，我们用另外一个文件将尾文件和尾指针存储起来，
 *       每次push，我们都将尾文件和尾指针写入到文件中。
 *       这样，当系统宕机时，即使往尾文件中多写入了一些字节，且tailerPointer
 *       没有更新。系统重启后，这些多写入的字节就会被自然的丢弃。
 *       
 *    接下来的问题是：
 *       我们用文件来存储头文件和头指针，尾文件和尾指针，这些同样也是文件操作，
 *       也有可能本来要写入10个字节，结果只写入了5个字节系统就宕机了。
 *       
 *       而且头文件，头指针，尾文件和尾指针是特别重要的信息，必须保证在系统
 *       宕机的时候，不能丢失。
 *       
 *    解决办法是：
 *       在每个文件尾加入一个校验码，只要校验通过就说明，这个文件要写入的数据
 *       都写入到文件中了。当校验不通过的时候，说明文件的数据没有完全写入，这
 *       样这个文件中的数据就不能使用了。
 *       自然的我们想到了用两个文件来存储头文件和头指针，每次都交替的写入，当
 *       一个文件被破坏的时候，另外一个文件肯定是好的，这样就能保证系统宕机的
 *       时候最多丢失正在处理的一个对象，而不会丢失更多的信息。
 *    
 *    这段经历，使我再次认识到：
 *    
 *       实践是检验程序是否正确的唯一标准！！！！
 *    
 *    任何情况下，我们都不能100%保证程序已经正确了。只有在不断的使用过程中来
 *    验证程序是否正确。即使已经验证了很长时间，也不能保证100%正确了。好的
 *    程序员，好的技术领导者，必须意识到这一点。
 *    
 *       所以，只有经过大量的使用以后，才能认为程序基本正确。所以，开源软件是
 *    一种非常好的验证程序是否正确的方式。
 *          	
 * </pre>    
 * 
 * 请注意：本实现打开的6个文件描述符，所以请注意你的OS对打开文件描述符的限制。
 * 要关闭这些文件描述符，可以调用clear方法，同时clear方法将清空队列和释放资源。
 * 
 * 
 * 增加新的功能：
 *    当队列出现IOException时，认为硬盘出现了问题，此时是否要容纳这个错误？
 *    
 *
 */
public class AdvancedFilePersistentQueue implements PersistentQueue {

	//保存头文件和头指针，以及popNum的文件
	public static final String headPointerFileName1 = ".headerpointer_1";
	public static final String headPointerFileName2 = ".headerpointer_2";
	
	//保存尾文件和尾指针，以及pushNum的文件
	public static final String tailPointerFileName1 = ".tailerpointer_1";
	public static final String tailPointerFileName2 = ".tailerpointer_2";
	
	public static final String storageFileSuffix = ".afpq";
	public static final long MAX_OBJECT_SIZE = 8 * 1024 * 1024L;
	
	protected File dir = null;
	protected int maxFileNum = 2;
	protected long fileCapacity = 1024 * 1024 * 16L;
	
	protected File headerFile = null;
	protected long headerPointer = 0;
	protected RandomAccessFile headerFileReader = null;
	
	protected File tailerFile = null;
	protected long tailerPointer = 0;
	protected RandomAccessFile tailerFileWriter = null;
	
	protected List<File> storages = new LinkedList<File>();
	
	protected RandomAccessFile headerPointFile1 = null;
	protected RandomAccessFile headerPointFile2 = null;
	
	protected RandomAccessFile tailerPointFile1 = null;
	protected RandomAccessFile tailerPointFile2 = null;
	
	protected Object popLock = new Object(){};
	protected Object pushLock = new Object(){};
	
	protected long pushSize = 0;
	protected long popSize = 0;
	protected long pushNum = 0;
	protected long popNum = 0;
	
	protected boolean forceFlush;
	
	protected boolean enableMemQueueWhileIOException = true;
	
	protected DefaultQueue memQueue;
	
	protected static Logger logger = Logger.getLogger(AdvancedFilePersistentQueue.class);
	
	/**
	 * 构造函数，此方法完成后，并没有打开文件描述符，只有对队列进行操作的时候，才打开文件描述符。
	 * 
	 * @param dir 指定文件储存的目录，此目录必须存在，否则抛出java.lang.IllegalArgumentException异常
	 * @param maxFileNum 最大文件的数目
	 * @param fileCapacity 每个文件最大的大小,in bytes
	 */
	public AdvancedFilePersistentQueue(File dir,int maxFileNum,long fileCapacity){
		this(dir,maxFileNum,fileCapacity,false);
	}
	
	public AdvancedFilePersistentQueue(File dir,int maxFileNum,long fileCapacity,boolean forceFlush){
		this(dir,maxFileNum,fileCapacity,forceFlush,true);
		
	}
	/**
	 * 构造函数，此方法完成后，并没有打开文件描述符，只有对队列进行操作的时候，才打开文件描述符。
	 * 
	 * @param dir 指定文件储存的目录，此目录必须存在，否则抛出java.lang.IllegalArgumentException异常
	 * @param maxFileNum 最大文件的数目
	 * @param fileCapacity 每个文件最大的大小,in bytes
	 * @param forceFlush 强制Flush，保证每次push方法都将内容真实的写入到磁盘中，此参数为true时将导致I/0的开销
	 */
	public AdvancedFilePersistentQueue(File dir,int maxFileNum,long fileCapacity,boolean forceFlush,boolean 
			enableMemQueueWhileIOException){
		if(dir == null || !dir.exists() || !dir.isDirectory()){
			throw new IllegalArgumentException("dir ["+dir+"] not exists.");
		}
		long startTime = System.currentTimeMillis();
		this.forceFlush = forceFlush;
		this.enableMemQueueWhileIOException = enableMemQueueWhileIOException;
		this.dir = dir;
		this.maxFileNum = maxFileNum;
		this.fileCapacity = fileCapacity;
		
		loadFileStorage();
		loadHeaderPointer();
		loadTailerPointer();
		
		if(enableMemQueueWhileIOException)
			memQueue = new DefaultQueue(Integer.MAX_VALUE);
		
		if(headerFile != null)
		{
			int k = storages.indexOf(headerFile);
			for(int i = k-1 ; i>=0 ; i--){
				storages.remove(i);
			}
			if(storages.size() == 0){
				headerFile = nextStorageFile();
				headerPointer = 0;
			}else{
				if(!storages.contains(headerFile)){
					headerFile = storages.get(0);
					headerPointer = 0;
				}
			}
		}else{
			if(storages.size() > 0){
				headerFile = storages.get(0);
				headerPointer = 0;
			}else{
				headerFile = nextStorageFile();
				headerPointer = 0;
			}
		}
		
		if(tailerFile != null){
			int k = storages.indexOf(tailerFile);
			for(int i = storages.size() -1 ; k >= 0 && i > k ; i--){
				storages.remove(i);
			}
			
			if(storages.size() == 0){
				tailerFile = nextStorageFile();
				tailerPointer = 0;
			}else{
				if(!storages.contains(tailerFile)){
					tailerFile = storages.get(storages.size() - 1);
					headerPointer = tailerFile.length();
				}
			}
		}else{
			if(storages.size() > 0){
				tailerFile = storages.get(storages.size() - 1);
				tailerPointer = tailerFile.length();
			}else{
				tailerFile = nextStorageFile();
				tailerPointer = 0;
			}
		}
		
		
		int h = storages.indexOf(headerFile);
		int t = storages.indexOf(tailerFile);
		long s = 0;
		for(int i = h; i < t ; i++){
			File f = storages.get(i);
			s +=f.length();
		}
		popSize = headerPointer;
		pushSize = s + tailerPointer;
		
		String files = "[";
		for(int i = 0 ; i < storages.size() ;i++){
			if(i < storages.size() - 1){
				files += storages.get(i).getName()+"->";
			}else
				files += storages.get(i).getName();
		}
		files +="]";
		if(logger.isInfoEnabled())
			logger.info("[initialize] ["+this.maxFileNum+"] ["+StringUtil.addcommas(fileCapacity)+"] -- " 
				+ getStatusAsString() + " -- " 
				+ files + " ["+(System.currentTimeMillis() - startTime)+"ms]");
		
	}
	
	protected String getStatusAsString(){
		return "["+dir.getAbsolutePath()+"] [H("+parseIntFromFileName(headerFile.getName())+")/"+StringUtil.addcommas(headerPointer)+"/"+StringUtil.addcommas(headerFile.length())
		+"] [T("+parseIntFromFileName(tailerFile.getName())+")/"+StringUtil.addcommas(tailerPointer)+"/"+StringUtil.addcommas(tailerFile.length())
		+"] ["+StringUtil.addcommas(pushNum)+"-"+StringUtil.addcommas(popNum)+"] ["+StringUtil.addcommas(pushSize)+"-"+StringUtil.addcommas(popSize)+"] ["+StringUtil.addcommas(this.capacity())+"KB]";
	}
	
	protected void loadFileStorage(){
		File fs[] = dir.listFiles(new FileFilter(){
			public boolean accept(File f){
				if(f.isFile()){
					if(parseIntFromFileName(f.getName()) > 0) return true;
					return false;
				}
				return false;
			}
		});
		
		for(int i = 0 ; i < fs.length ; i++){
			for(int j = i+1; j<fs.length ; j++){
				
				int ki = parseIntFromFileName(fs[i].getName());
				int kj = parseIntFromFileName(fs[j].getName());
				if(kj < ki){
					File f = fs[i];
					fs[i] = fs[j];
					fs[j] = f;
				}
			}
		}
		
		for(int i = 0 ; i < fs.length ; i++){
			storages.add(fs[i]);
		}
	}
	
	/**
	 * 返回0表示没有解析出来，返回大于0的整数表示正确
	 * @param s
	 * @return
	 */
	protected static int parseIntFromFileName(String s){
		if(s.endsWith(storageFileSuffix)){
			s = s.substring(0,s.length() - storageFileSuffix.length());
			if(s.startsWith("F_")){
				s = s.substring(2);
				try{
					int k = Integer.parseInt(s);
					if(k > 0) return k;
				}catch(Exception e){
					
				}
			}
		}
		return 0;
	}
	
	protected File nextStorageFile()
	{
		if(storages.size() > 0)
		{
			File f = storages.get(storages.size() -1);
			int ki = parseIntFromFileName(f.getName());
			if(ki > 0)
			{
				File newF = new File(dir,"F_"+(ki+1)+storageFileSuffix);
				storages.add(newF);
				return newF;
			}else
			{
				return null;
			}
		}else
		{
			File newF =  new File(dir,"F_1"+storageFileSuffix);
			storages.add(newF);
			return newF;
		}
	}
	
	/**
	 * 读取头文件和头指针，以及popNum。
	 * 文件是以二进制的的格式存储的，如下：
	 * 
	 * header_index 4 bytes
	 * headerpoint  8 bytes
	 * popNum       8 bytes
	 * checknum     8 bytes
	 * 
	 * 其中checknum为校验码，用于验证文件内容是否为完整的，
	 * 如果checknum不对，说明文件的内容不是完整的，那么这个文件别破坏，用另外一个文件。
	 * 
	 * checknum = header_index + headerpoint + popNum
	 *
	 * 之所以用两个文件，是为了防止系统宕机的时候，其中一个文件（宕机的时候正在写入）被破坏。
	 */
	protected void loadHeaderPointer(){
		long startTime = System.currentTimeMillis();
		try{
			int header_index1 = -1;
			long header_point1 = -1L;
			long pop_num1 = -1L;
			long check_num1 = -1L;
			
			int header_index2 = -1;
			long header_point2 = -1L;
			long pop_num2 = -1L;
			long check_num2 = -1L;
			
			File file = new File(dir,headPointerFileName1);
			if(file.exists()){
				try{
					DataInputStream reader = new DataInputStream(new FileInputStream(file));
					header_index1 = reader.readInt();
					header_point1 = reader.readLong();
					pop_num1 = reader.readLong();
					check_num1 = reader.readLong();
					reader.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			
			file = new File(dir,headPointerFileName2);
			if(file.exists()){
				try{
					DataInputStream reader = new DataInputStream(new FileInputStream(file));
					header_index2 = reader.readInt();
					header_point2 = reader.readLong();
					pop_num2 = reader.readLong();
					check_num2 = reader.readLong();
					reader.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			
			boolean b1 = (check_num1 != -1L) && (check_num1 == (header_index1 + header_point1 + pop_num1));
			boolean b2 = (check_num2 != -1L) && (check_num2 == (header_index2 + header_point2 + pop_num2));
			
			if(b1 && b2){
				if(pop_num1 > pop_num2){
					this.headerFile = new File(dir,"F_"+header_index1+storageFileSuffix);
					this.headerPointer = header_point1;
					this.popNum = pop_num1;
					this.saveHeaderFlag = 1;
				}else{
					this.headerFile = new File(dir,"F_"+header_index2+storageFileSuffix);
					this.headerPointer = header_point2;
					this.popNum = pop_num2;
					this.saveHeaderFlag = 0;
				}
			}else if(b1){
				this.headerFile = new File(dir,"F_"+header_index1+storageFileSuffix);
				this.headerPointer = header_point1;
				this.popNum = pop_num1;
				this.saveHeaderFlag = 1;
			}else if(b2){
				this.headerFile = new File(dir,"F_"+header_index2+storageFileSuffix);
				this.headerPointer = header_point2;
				this.popNum = pop_num2;
				this.saveHeaderFlag = 0;
			}
			
			String files = "[";
			for(int i = 0 ; i < storages.size() ;i++){
				if(i < storages.size() - 1){
					files += storages.get(i).getName()+"->";
				}else
					files += storages.get(i).getName();
			}
			files +="]";
			
			if(logger.isInfoEnabled())
				logger.info("[load-header] ["+dir.getAbsolutePath()+"] ["+this.headerFile+"] ["+this.headerPointer+"] ["+this.pushNum+"] ["+this.popNum
						+"] -- ["+b1+"] [F_"+header_index1+storageFileSuffix+"] ["+header_point1+"] ["+pop_num1+"] ["+check_num1
						+"] -- ["+b2+"] [F_"+header_index2+storageFileSuffix+"] ["+header_point2+"] ["+pop_num2+"] ["+check_num2
						+"] -- "+ files + " ["+(System.currentTimeMillis() - startTime)+"ms]");
			
		}catch(Exception e){
			logger.error("[load-header] [fail] [exception]",e);
		}
	}
	
	/**
	 * 读取尾文件和尾指针，以及pushNum。
	 * 文件是以二进制的的格式存储的，如下：
	 * 
	 * tailer_index 4 bytes
	 * tailerpoint  8 bytes
	 * pushNum       8 bytes
	 * checknum     8 bytes
	 * 
	 * 其中checknum为校验码，用于验证文件内容是否为完整的，
	 * 如果checknum不对，说明文件的内容不是完整的，那么这个文件别破坏，用另外一个文件。
	 * 
	 * checknum = tailer_index + tailerpoint + pushNum
	 *
	 * 之所以用两个文件，是为了防止系统宕机的时候，其中一个文件（宕机的时候正在写入）被破坏。
	 */
	protected void loadTailerPointer(){
		long startTime = System.currentTimeMillis();
		try{
			int tailer_index1 = -1;
			long tailer_point1 = -1L;
			long push_num1 = -1L;
			long check_num1 = -1L;
			
			int tailer_index2 = -1;
			long tailer_point2 = -1L;
			long push_num2 = -1L;
			long check_num2 = -1L;
			
			File file = new File(dir,tailPointerFileName1);
			if(file.exists()){
				try{
					DataInputStream reader = new DataInputStream(new FileInputStream(file));
					tailer_index1 = reader.readInt();
					tailer_point1 = reader.readLong();
					push_num1 = reader.readLong();
					check_num1 = reader.readLong();
					reader.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			
			file = new File(dir,tailPointerFileName2);
			if(file.exists()){
				try{
					DataInputStream reader = new DataInputStream(new FileInputStream(file));
					tailer_index2 = reader.readInt();
					tailer_point2 = reader.readLong();
					push_num2 = reader.readLong();
					check_num2 = reader.readLong();
					reader.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			
			boolean b1 = (check_num1 != -1L) && (check_num1 == (tailer_index1 + tailer_point1 + push_num1));
			boolean b2 = (check_num2 != -1L) && (check_num2 == (tailer_index2 + tailer_point2 + push_num2));
			
			if(b1 && b2){
				if(push_num1 > push_num2){
					this.tailerFile = new File(dir,"F_"+tailer_index1+storageFileSuffix);
					this.tailerPointer = tailer_point1;
					this.pushNum = push_num1;
					this.saveTailerFlag = 1;
				}else{
					this.tailerFile = new File(dir,"F_"+tailer_index2+storageFileSuffix);
					this.tailerPointer = tailer_point2;
					this.pushNum = push_num2;
					this.saveTailerFlag = 0;
				}
			}else if(b1){
				this.tailerFile = new File(dir,"F_"+tailer_index1+storageFileSuffix);
				this.tailerPointer = tailer_point1;
				this.pushNum = push_num1;
				this.saveTailerFlag = 1;
			}else if(b2){
				this.tailerFile = new File(dir,"F_"+tailer_index2+storageFileSuffix);
				this.tailerPointer = tailer_point2;
				this.pushNum = push_num2;
				this.saveTailerFlag = 0;
			}
			
			String files = "[";
			for(int i = 0 ; i < storages.size() ;i++){
				if(i < storages.size() - 1){
					files += storages.get(i).getName()+"->";
				}else
					files += storages.get(i).getName();
			}
			files +="]";
			
			if(logger.isInfoEnabled())
				logger.info("[load-tailer] ["+dir.getAbsolutePath()+"] ["+this.tailerFile+"] ["+this.tailerPointer+"] ["+this.pushNum+"] ["+this.popNum
						+"] -- ["+b1+"] [F_"+tailer_index1+storageFileSuffix+"] ["+tailer_point1+"] ["+push_num1+"] ["+check_num1
						+"] -- ["+b2+"] [F_"+tailer_index2+storageFileSuffix+"] ["+tailer_point2+"] ["+push_num2+"] ["+check_num2
						+"] -- "+ files + " ["+(System.currentTimeMillis() - startTime)+"ms]");
			
		}catch(Exception e){
			logger.error("[load-tailer] [fail] [exception]",e);
		}
	}
	
	private int saveHeaderFlag = 0;
	
	/**
	 * 用两个文件来保存当前头文件和头指针。
	 * 用两个文件是为了在系统宕机的时候，只丢失正在写入的头文件和头指针，
	 * 而上一次写入的头文件和头指针已经保存在另外一个文件中，不会因为系统宕机（比如killall -9 java）而丢失。
	 */
	protected void saveHeaderPointer(){
		if(saveHeaderFlag == 0) 
			saveHeaderFlag = 1;
		else
			saveHeaderFlag = 0;
		
		long startTime = System.currentTimeMillis();
		try{
			RandomAccessFile raf = null;
			if(saveHeaderFlag == 0){ 
				if(headerPointFile1 == null)
					headerPointFile1 = new RandomAccessFile(new File(dir,headPointerFileName1),"rw");
				raf = headerPointFile1;
			}
			else{
				if(headerPointFile2 == null)
					headerPointFile2 = new RandomAccessFile(new File(dir,headPointerFileName2),"rw");
				raf = headerPointFile2;
			}
			int headerIndex = parseIntFromFileName(this.headerFile.getName());
			long checkNum = headerIndex + this.headerPointer + this.popNum;
			raf.seek(0);
			raf.writeInt(headerIndex);
			raf.writeLong(this.headerPointer);
			raf.writeLong(this.popNum);
			raf.writeLong(checkNum);
		}catch(java.io.IOException e){
			logger.error("[save-header-pointer] [fail] [ioexception] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]",e);
		}
	}
	
	private int saveTailerFlag = 0;
	
	/**
	 * 用两个文件来保存当前头文件和头指针。
	 * 用两个文件是为了在系统宕机的时候，只丢失正在写入的头文件和头指针，
	 * 而上一次写入的头文件和头指针已经保存在另外一个文件中，不会因为系统宕机（比如killall -9 java）而丢失。
	 */
	protected void saveTailerPointer(){
		if(saveTailerFlag == 0) 
			saveTailerFlag = 1;
		else
			saveTailerFlag = 0;
		
		long startTime = System.currentTimeMillis();
		try{
			RandomAccessFile raf = null;
			if(saveTailerFlag == 0){ 
				if(tailerPointFile1 == null)
					tailerPointFile1 = new RandomAccessFile(new File(dir,tailPointerFileName1),"rw");
				raf = tailerPointFile1;
			}
			else{
				if(tailerPointFile2 == null)
					tailerPointFile2 = new RandomAccessFile(new File(dir,tailPointerFileName2),"rw");
				raf = tailerPointFile2;
			}
			int tailerIndex = parseIntFromFileName(this.tailerFile.getName());
			long checkNum = tailerIndex + this.tailerPointer + this.pushNum;
			raf.seek(0);
			raf.writeInt(tailerIndex);
			raf.writeLong(this.tailerPointer);
			raf.writeLong(this.pushNum);
			raf.writeLong(checkNum);
		}catch(java.io.IOException e){
			logger.error("[save-tailer-pointer] [fail] [ioexception] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]",e);
		}
	}
	
	private MyByteArrayOutputStream baos = new MyByteArrayOutputStream(8192);
	
	/**
	 * 将对象加入到队列的尾部，如果队列满了，返回false，对象没有加入队列；否则返回true，对象加入到队列尾部。
	 * 
	 * 此方法是线程安全的。
	 * 
	 * 请注意，此方法对对象的大小有一定的限制，目前是1M，对象序列化后的大小超过1M的，不能加入队列。
	 */
	public boolean push(Serializable object) {
		long startTime = System.currentTimeMillis();
		if(isFull()){
			if(logger.isDebugEnabled())
				logger.debug("[push] [fail] [full] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]");
			return false;
		}
		
		synchronized(pushLock){
			if(isFull()){
				if(logger.isDebugEnabled())
					logger.debug("[push] [fail] [full] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]");
				return false;
			}
			
			try{
				if(tailerFileWriter == null){
					if(forceFlush)
						tailerFileWriter = new RandomAccessFile(tailerFile,"rwd");
					else
						tailerFileWriter = new RandomAccessFile(tailerFile,"rw");
					
				}else if(this.tailerPointer >= this.fileCapacity){
					File nextFile = this.nextStorageFile();
					tailerFileWriter.close();
					
					tailerFile = nextFile;
					if(forceFlush)
						tailerFileWriter = new RandomAccessFile(tailerFile,"rwd");
					else
						tailerFileWriter = new RandomAccessFile(tailerFile,"rw");
					tailerPointer = 0;
				}
			
			}catch(IOException e){
				if(this.enableMemQueueWhileIOException){
					this.memQueue.push(object);
					this.pushNum ++;
					pushLock.notify();
					
					logger.warn("[push] [fail] [put-into-mem] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]",e);
					return true;
				}else{
					if(logger.isInfoEnabled())
						logger.info("[push] [fail] [tailer-error] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]",e);
					return false;
				}
			
			}catch(Exception e){
				if(logger.isInfoEnabled())
					logger.info("[push] [fail] [tailer-error] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]",e);
				return false;
			}
			
			try{
				if(tailerFileWriter.getFilePointer() != tailerPointer)
					tailerFileWriter.seek(tailerPointer);
				
				baos.reset();
				try{
					ObjectOutputStream oos = new ObjectOutputStream(baos);
					oos.writeObject(object);
				}catch(Exception e){
					if(logger.isInfoEnabled())
						logger.info("[push] [fail] [write-object] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]",e);
					return false;
				}
				
				if(baos.size() > MAX_OBJECT_SIZE){
					if(logger.isInfoEnabled())
						logger.info("[push] [fail] [object-too-big] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]");
					return false;
				}
				
				tailerFileWriter.writeInt(baos.size());
				tailerFileWriter.write(baos.getBuffer(),0,baos.size());
				
				tailerPointer = tailerFileWriter.getFilePointer();
				
				this.pushNum ++;		
				
				this.saveTailerPointer();
				
				if(logger.isDebugEnabled())
					logger.debug("[push] [succ] [ok] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]");
				
				pushLock.notify();
				
				this.pushSize += 4 + baos.size();

				
				return true;
				
			}catch(IOException e){
				if(this.enableMemQueueWhileIOException){
					this.memQueue.push(object);
					this.pushNum ++;
					pushLock.notify();
					
					logger.warn("[push] [fail] [put-into-mem] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]",e);
					
					return true;
				}else{
					if(logger.isInfoEnabled())
						logger.info("[push] [fail] [write-data] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]",e);
				}
			}
		}
		return false;
	}

	private byte popBuffer[] = new byte[8192];

	/**
	 * 取出队列头的对象，如果队列为空，返回null。
	 * 
	 * 此方法是线程安全的
	 */
	public Serializable pop() {
		return pop(true);
	}
	/**
	 * 取出队列头的对象，如果队列为空，返回null。
	 * 
	 * 此方法是线程安全的
	 */
	protected Serializable pop(boolean printLog) {
		long startTime = System.currentTimeMillis();
		if(isEmpty()){
			if(printLog && logger.isDebugEnabled())
				logger.debug("[pop] [succ] [empty] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]");
			return null;
		}
		
		synchronized(popLock){
			if(isEmpty()){
				if(printLog && logger.isDebugEnabled())
					logger.debug("[pop] [succ] [empty] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]");
				return null;
			}
			
			if(this.enableMemQueueWhileIOException && this.memQueue != null && !this.memQueue.isEmpty()){
				
				Serializable s =  (Serializable)memQueue.pop();
				if(s != null){
					this.popNum++;
					return s;
				}
			}
			
			try{
				if(headerFileReader == null){
					headerFileReader = new RandomAccessFile(headerFile,"r");
				}else if(this.headerPointer >= headerFileReader.length()){
					int k = storages.indexOf(headerFile);
					if(k == -1){
						//不可能出现这种情况，如果出现，说明此队列出现严重的问题，导致数据丢失
						//重新初始化队列   -- myzdf 2007-10-03
						if(storages.size() > 0){
							headerFile = storages.get(0);
							headerPointer = 0;
							headerFileReader = null;
						}else{
							headerFile = nextStorageFile();
							headerPointer = 0;
							headerFileReader = null;
							
						}
						throw new Exception("reinitialize the queue and will lost or re-read some data cause headerFile not exists in storages.");
					}else if(k == 0 && storages.size() == 1){
						//这种情况曾经发生过，即队列认为还有元素可以读，但是headerFileReader已经认为没有数据可以读了
						//出现这种情况的可能原因是，tailerFileWriter采用了rw的模式，虽然写了数据，但是数据没有写到硬盘上，而是
						//在系统buffer中，这样headerFileReader就读不到tailerFileWriter写的数据
						//我已经把tailerFileWriter修改为rwd模式，看看效果如何   
						//如果出现这种情况，强制将数据写入磁盘中 -- myzdf 2007-10-03
						
						//上述的观点是错误的，导致这个原因是正在写入数据的时候，系统宕机。这样只有部分数据写入到了文件中，
						//在系统重启后，我们认为tailerPointer就是文件的长度，从而出现队列逻辑上为空的时候，
						//tailerPointer和headerPointer不符合，解决这个问题的方法是把tailerPointer也用文件存储起来 -- myzdf 2007-10-03
						
						//2007-12-21
						//这段代码也可能会被执行，这种情况下会被执行：
						//当队列认为文件还有内容时，但是读的时候遇到了EOFException，队列将试图丢弃正在读的文件，就会将headerPointer设置为当前头文件长度
						//但是pushSize和popSize并没有改变，所以isEmpty方法返回false，当storage只有一个文件时，会执行这段代码
						String errMessage = "Fatal Error: queue is really empty but pushSize["+this.pushSize+"] not equals to popSize["+this.popSize+"], and reset this queue.";
						storages.remove(k);
						
						this.tailerFile = nextStorageFile();
						this.tailerFileWriter = null;
						this.tailerPointer = 0;
						
						headerPointer = 0;
						this.popSize = 0;
						this.pushSize = 0;
						this.pushNum = this.popNum;
						
						headerFile = tailerFile;
						headerFileReader = null;
						
						throw new Exception(errMessage);
					}
					storages.remove(k);
					File oldFile = headerFile;
					headerFileReader.close();
					if(!oldFile.delete())
						oldFile.deleteOnExit();
					
					headerFile = storages.get(k);
					headerFileReader = new RandomAccessFile(headerFile,"r");
					headerPointer = 0;
				}
			}catch(Exception e){
				if(logger.isInfoEnabled())
					logger.info("[pop] [fail] [header-error] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]",e);
				return null;
			}
			
			try{
				if(headerFileReader.getFilePointer() != headerPointer)
					headerFileReader.seek(headerPointer);
				
				int len = headerFileReader.readInt();
				if(len <= 0 || len > MAX_OBJECT_SIZE){
					throw new IOException("Oject len ["+len+"] is invalid. It must between (0,"+MAX_OBJECT_SIZE+").");
				}
				
				if(len > popBuffer.length)
					popBuffer = new byte[len];
				
				headerFileReader.readFully(popBuffer,0,len);
				
				headerPointer = headerFileReader.getFilePointer();
				
				this.popNum++;
				
				saveHeaderPointer();
				
				this.popSize += (4 + len);

				
				try{
					ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(popBuffer,0,len));
					Object o = ois.readObject();
					if(printLog && logger.isDebugEnabled())
						logger.debug("[pop] [succ] [ok] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]");
					return (Serializable)o;
					
				}catch(Exception e){
					if(logger.isInfoEnabled())
						logger.info("[pop] [fail] [read-object] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]",e);
					return null;
				}
			}catch(IOException e){
				if(logger.isInfoEnabled())
					logger.info("[pop] [fail] [read-data] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]",e);
				try{
					headerPointer = headerFileReader.length();
					if(logger.isInfoEnabled())
						logger.info("[repair] [-] [discard-current-file] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]");
				}catch(Exception ex){
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}

	/**
	 * 拷贝出队列尾部的部分对象，此方法是费时的方法，
	 * 他需要从队列头，遍历文件到队列尾部。
	 * 
	 * 此方法不修改队列的状态
	 */
	public Object[] cloneObjects(int num) throws Exception{
		if(isEmpty()){
			return new Object[0];
		}
		ArrayList<Object> al = new ArrayList<Object>();
		
		int en = elementNum();
		int n = elementNum() - num;
		
		RandomAccessFile rf = new RandomAccessFile(headerFile,"r");
		int fileIndex = storages.indexOf(headerFile);
		long pointer = headerPointer;
		rf.seek(pointer);
		
		byte[] buffer = new byte[8192];
		int index = 0;
		while(index < en){
			
			if(pointer >= rf.length()){
				fileIndex ++;
				if(fileIndex < storages.size()){
					rf.close();
					rf = new RandomAccessFile(storages.get(fileIndex),"r");
					pointer = 0;
				}else{
					break;
				}
			}
			
			int len = rf.readInt();
			if(len <= 0 || len > MAX_OBJECT_SIZE){
				throw new IOException("Oject len ["+len+"] is invalid. It must between (0,"+MAX_OBJECT_SIZE+").");
			}
			
			if(len > buffer.length)
				buffer = new byte[len];
				
			if(index >= n){
				rf.readFully(buffer,0,len);
				ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer,0,len));
				Object o = ois.readObject();
				al.add(o);
			}
			pointer += 4 + len;
			rf.seek(pointer);
			index++;
		}
		rf.close();
		return al.toArray();
	}
	/**
	 * 取出队列头的对象，如果队列中没有对象，就等待，
	 * 直到队列中有对象，或者超时
	 * @param timeout 如果小于等于0，就一直等到队列中有对象
	 */
	public Serializable pop(long timeout){
		if(timeout < 0 ) timeout = 0;
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis() + timeout;
		
		Serializable ret = pop(false);
		if(ret != null){
			if(logger.isDebugEnabled())
			logger.debug("[pop-timeout] [succ] [ok] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]");
			return ret;
		}
		
		while(true){
			long now = System.currentTimeMillis();
			if(timeout > 0 && now >= endTime){
				if(logger.isDebugEnabled())
				logger.debug("[pop-timeout] [succ] [timeout] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]");
				return null;
			}
			
			synchronized(pushLock){
				try{
					if(timeout == 0)
						pushLock.wait();
					else
						pushLock.wait(endTime - now);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			ret = pop(false);
			if(ret != null){
				if(logger.isDebugEnabled())
				logger.debug("[pop-timeout] [succ] [ok] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]");
				return ret;
			}
		}
	}

	/**
	 * 
	 */
	public Serializable peek() {
		long startTime = System.currentTimeMillis();
		if(isEmpty()){
			if(logger.isDebugEnabled())
				logger.debug("[peek] [succ] [empty] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]");
			return null;
		}
		
		synchronized(popLock){
			if(isEmpty()){
				if(logger.isDebugEnabled())
					logger.debug("[peek] [succ] [empty] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]");
				return null;
			}
			
			if(this.enableMemQueueWhileIOException && this.memQueue != null && !this.memQueue.isEmpty()){
				return (Serializable)memQueue.peek();
			}
			
			try{
				if(headerFileReader == null){
					headerFileReader = new RandomAccessFile(headerFile,"rw");
				}else if(this.headerPointer >= headerFileReader.length()){
					int k = storages.indexOf(headerFile);
					storages.remove(k);
					File oldFile = headerFile;
					headerFileReader.close();
					if(!oldFile.delete())
						oldFile.deleteOnExit();
					
					headerFile = storages.get(k);
					headerFileReader = new RandomAccessFile(headerFile,"rw");
					headerPointer = 0;
				}
			}catch(Exception e){
				if(logger.isInfoEnabled())
					logger.info("[peek] [fail] [header-error] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]",e);
				return null;
			}
			
			try{
				if(headerFileReader.getFilePointer() != headerPointer)
					headerFileReader.seek(headerPointer);
				
				int len = headerFileReader.readInt();
				if(len <= 0 || len > MAX_OBJECT_SIZE){
					throw new IOException("Oject len ["+len+"] is invalid. It must between (0,"+MAX_OBJECT_SIZE+").");
				}
				
				if(len > popBuffer.length)
					popBuffer = new byte[len];
				
				headerFileReader.readFully(popBuffer,0,len);
				
				headerFileReader.seek(headerPointer);
				
				try{
					ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(popBuffer,0,len));
					Object o = ois.readObject();
					if(logger.isDebugEnabled())
						logger.debug("[peek] [succ] [ok] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]");
					return (Serializable)o;
					
				}catch(Exception e){
					if(logger.isInfoEnabled())
						logger.info("[peek] [fail] [read-object] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]",e);
					return null;
				}
			}catch(IOException e){
				if(logger.isInfoEnabled())
					logger.info("[peek] [fail] [read-data] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]",e);
				try{
					headerPointer = headerFileReader.length();
					if(logger.isInfoEnabled())
						logger.info("[repair] [-] [discard-current-file] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]");
				}catch(Exception ex){
					e.printStackTrace();
				}
			}
		}
		
		return null;

	}
	
	/**
	 * 当前用了多少KByte
	 */
	public int size() {
		return (int)((this.pushSize - this.popSize)/1024.0f);
	}

	/**
	 * 最多可以用多少KByte
	 */
	public int capacity() {
		return (int)(this.fileCapacity * this.maxFileNum/1024.0f);
	}

	/**
	 * 返回队列中保存的元素的个数
	 * 公式：elementNum = pushNum - popNum
	 * @return 返回队列中保存的元素的个数
	 */
	public int elementNum(){
		return (int)(this.pushNum - this.popNum);
	}
	
	/**
	 * 返回自创建或者上一次清空以来，队列中一共push进入的元素的个数，
	 * 公式：elementNum = pushNum - popNum
	 * @return
	 */
	public long pushNum(){
		return pushNum;
	}

	/**
	 * 返回自创建或者上一次清空以来，队列中一共pop出来的元素的个数
	 * 公式：elementNum = pushNum - popNum
	 * @return
	 */
	public long popNum(){
		return popNum;
	}
	
	public void closeFileDescriptors(){
		if(headerFileReader != null){
			try{
				headerFileReader.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			headerFileReader = null;
		}
		
		if(tailerFileWriter != null){
			try {
				tailerFileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			tailerFileWriter = null;
		}
		
		if(this.headerPointFile1 != null){
			try{
				headerPointFile1.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			headerPointFile1 = null;
		}
		
		if(this.headerPointFile2 != null){
			try{
				headerPointFile2.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			headerPointFile2 = null;
		}
		
		if(this.tailerPointFile1 != null){
			try{
				tailerPointFile1.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			tailerPointFile1 = null;
		}
	
		if(this.tailerPointFile2 != null){
			try{
				tailerPointFile2.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			tailerPointFile2 = null;
		}
	}
	
	/**
	 * 清空队列
	 * 所有的数据都归为零，并且关闭和删除所有的文件，释放队列占用的所有的资源。
	 * 
	 * 队列清空后，就相当于刚创建的新的队列。
	 * 
	 */
	public void clear() {
		long startTime = System.currentTimeMillis();
		synchronized(pushLock){
			synchronized(popLock){
				try{
					int num = this.elementNum();
					long size = this.size(); 
					
					
					if(headerFileReader != null){
						try{
							headerFileReader.close();
						}catch(IOException e){
							e.printStackTrace();
						}
						headerFileReader = null;
					}
					
					headerFile = this.nextStorageFile();
					headerPointer = 0;
					
					if(tailerFileWriter != null){
						try{
							tailerFileWriter.close();
						}catch(IOException e){
							e.printStackTrace();
						}
						tailerFileWriter = null;
					}
					
					tailerFile = headerFile;
					tailerPointer = 0;
					this.pushNum = 0;
					this.popNum = 0;
					this.pushSize = 0;
					this.popSize = 0;
					
					for(int i = 0 ; i < storages.size()-1; i++){
						File f = storages.get(i);
						if(f.exists()){
							if(!f.delete())
								f.deleteOnExit();
						}
					}
					
					storages.clear();
					storages.add(headerFile);
					
					this.saveHeaderPointer();
					this.saveTailerPointer();

					this.saveHeaderPointer();
					this.saveTailerPointer();

					saveHeaderFlag = 0;
					saveTailerFlag = 0;
					
					if(headerPointFile1 != null){
						try{
							headerPointFile1.close();
						}catch(IOException e){
							e.printStackTrace();
						}
						headerPointFile1 = null;
					}
					
					if(headerPointFile2 != null){
						try{
							headerPointFile2.close();
						}catch(IOException e){
							e.printStackTrace();
						}
						headerPointFile2 = null;
					}
					
					if(tailerPointFile1 != null){
						try{
							tailerPointFile1.close();
						}catch(IOException e){
							e.printStackTrace();
						}
						tailerPointFile1 = null;
					}
					
					if(tailerPointFile2 != null){
						try{
							tailerPointFile2.close();
						}catch(IOException e){
							e.printStackTrace();
						}
						tailerPointFile2 = null;
					}
					
					if(enableMemQueueWhileIOException && memQueue != null){
						memQueue.clear();
					}
					
					if(logger.isInfoEnabled())
						logger.info("[clear] [succ] [num:"+num+",size="+size+"kbyte] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]");
					
				}catch(Exception e){
					if(logger.isInfoEnabled())
					logger.info("[clear] [fail] [exception] " + getStatusAsString() + " ["+(System.currentTimeMillis() - startTime)+"ms]",e);
				}
			}
		}
	}
	
	public int elementNumInMemQueue(){
		if(this.enableMemQueueWhileIOException && this.memQueue != null){
			return memQueue.size();
		}
		return -1;
	}

	public boolean isEmpty() {
		
		if(this.enableMemQueueWhileIOException && this.memQueue != null && !this.memQueue.isEmpty()){
			return false;
		}
		
		if(pushSize == popSize ){ 
			return true;
		}
		else
			return false;
	}

	public boolean isFull() {
		if(this.pushSize - this.popSize >= this.maxFileNum * this.fileCapacity)
			return true;
		else
			return false;
	}
	
	public static class MyByteArrayOutputStream extends ByteArrayOutputStream{
		public MyByteArrayOutputStream(int size){
			super(size);
		}
		
		public byte[] getBuffer(){
			return buf;
		}
	}

}

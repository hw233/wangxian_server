package com.xuanzhi.tools.queue;

import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;
import com.xuanzhi.tools.text.StringUtil;

/**
 * 默认的文件实现方式，此实现方式是用一个文件来存储一个对象。
 * 当内存队列满的时候，新存放到队列中的对象就会保存到一个新的文件中。
 * 同时，有一个线程在不断的将最早的文件内容读取到内存队列尾部。
 * 
 * 由于此实现方法是用单独的文件来存放对象，所以效率上对于小对象会存在问题。
 * 建议只有当队列中的对象为比较大的对象的时候，采用此种队列。
 * 
 * 此队列的storageCapacity不能太大，如果太大，导致系统重启的时候需要扫描硬盘上多个文件，
 * 导致系统重启需要太多时间，而且还需要对多个文件进行排序，也会耗费很多时间。
 * 
 * 建议storageCapacity不要超过 1024 × 256 = 262144，如果你要缓存大量的小对象，
 * 
 * 建议使用AdvancedFilePersistentQueue
 * 
 *
 */
public class DefaultFilePersistentQueue implements PersistentQueue,Runnable {

	protected static String FILE_EXTEND = ".pq";
	
	protected static String MQ_FILENAME = ".memery_queue";
	
	protected File dir;
	
	protected DefaultQueue mq;
	
	protected int memeryCapacity;
	
	protected int storageCapacity;
	
	protected LinkedList<File> files = new LinkedList<File>();
	
	protected long totalFileLength = 0;
	
	protected Thread m_thread;
	
	protected static Logger logger = Logger.getLogger(PersistentQueue.class);
	
	protected Object lock = new Object(){};
	
	protected int counter = 0;
	protected long lastCounterTime = 0L;
	/**
	 * 构造函数
	 * @param dir 指定文件储存的目录，此目录必须存在，否则抛出java.lang.IllegalArgumentException异常
	 * @param memeryCapacity 内存队列中，最多存放对象的个数，默认为1024
	 * @param storageCapacity 文件目录中，最多存放对象的个数，默认为1024 * 512
	 */
	public DefaultFilePersistentQueue(File dir){
		this(dir,1024,1024*512);
	}
	
	/**
	 * 构造函数
	 * @param dir 指定文件储存的目录，此目录必须存在，否则抛出java.lang.IllegalArgumentException异常
	 * @param memeryCapacity 内存队列中，最多存放对象的个数
	 * @param storageCapacity 文件目录中，最多存放对象的个数
	 */
	public DefaultFilePersistentQueue(File dir,int memeryCapacity,int storageCapacity){
		if(dir == null || !dir.exists() || !dir.isDirectory()){
			throw new IllegalArgumentException("dir ["+dir+"] not exists.");
		}
		this.dir = dir;
		this.memeryCapacity = memeryCapacity;
		this.storageCapacity = storageCapacity;
		
		mq = new DefaultQueue(memeryCapacity);
		
		loadMemeryQueue();
		loadStorageQueue();

		
		m_thread = new Thread(this,"PersistentQueue-Thread");
		m_thread.start();
		
		Runtime.getRuntime().addShutdownHook(new MyShutdownHook());
	}
	

	protected String getQueueStatusForLogger(){
		return "["+StringUtil.addcommas(this.getMemerySize())+"/"+StringUtil.addcommas(this.getMemeryCapacity())+"] ["+StringUtil.addcommas(this.getStorageSize())+"/"+StringUtil.addcommas(this.getStorageCapacity())+"] ["+StringUtil.addcommas(totalFileLength)+"]";
	}
	
	protected void loadStorageQueue(){
		
		files.clear();
		
		long startTime = System.currentTimeMillis();
		
		loadFiles(dir,files);
		
		logger.info("[load-storage-queue] [OK] ["+dir.getAbsolutePath()+"] [-] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms]");
		
		Comparator comparator = new Comparator(){
			public int compare(Object o1,Object o2){
				File f1 = (File)o1;
				File f2 = (File)o2;
				//System.out.println(f1.getName() +".lm="+f1.lastModified()+"    " + f2.getName()+".lm="+f2.lastModified());
				
				if(f1.lastModified()/1000 < f2.lastModified()/1000)
					return -1;
				else if(f1.lastModified()/1000 > f2.lastModified()/1000)
					return 1;
				else{
					String s1 = f1.getName().substring(0,f1.getName().length() - FILE_EXTEND.length());
					String s2 = f2.getName().substring(0,f2.getName().length() - FILE_EXTEND.length());
					s1 = s1.substring(s1.lastIndexOf("-")+1);
					s2 = s2.substring(s2.lastIndexOf("-")+1);
					int t1 = Integer.parseInt(s1);
					int t2 = Integer.parseInt(s2);
					if(t1 < t2)
						return -1;
					else if(t1 > t2)
						return 1;
					else{
						try{
							throw new Exception("tow file has same time and counter.["+f1+"] = ["+f2+"]");
						}catch(Exception e){
							e.printStackTrace();
						}
						return 0;
					}
					
				}
			}
			
			public boolean equals(Object o){
				if(o == this){
					return true;
				}
				return false;
			}
		};
		
		Collections.sort(files,comparator);
	}
	
	protected void loadMemeryQueue(){
		
		File mqFile = new File(dir,MQ_FILENAME);
		if(mqFile.isFile() && mqFile.exists()){
			long startTime = System.currentTimeMillis();
			try{
				ObjectInputStream ois = getObjectInputStream(mqFile);
				int count = ois.readInt();
				Serializable obj = null;
				for(int i = 0 ; i < count ; i++){
					obj = (Serializable)ois.readObject();
					if(!mq.isFull())
						mq.push(obj);
					else
						pushIntoFile(obj);
				}
				ois.close();
				if(!mqFile.delete())
					mqFile.deleteOnExit();
				
				logger.info("[load-memery-queue] [OK] ["+mqFile.getAbsolutePath()+"] ["+count+"] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms]");
			}catch(Exception e){
				e.printStackTrace();
				logger.info("[load-memery-queue] [Error] ["+mqFile.getAbsolutePath()+"] [-] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms]",e);
				if(!mqFile.delete())
					mqFile.deleteOnExit();
			}
		}
	}
	
	protected synchronized void saveMemeryQueue(){
		if(m_thread != null)
			m_thread.interrupt();
		
		File mqFile = new File(dir,MQ_FILENAME);
		long startTime = System.currentTimeMillis();
		if(mq.size() > 0){
			try{
				int count = 0;
				ObjectOutputStream oos = getObjectOutputStream(mqFile);
				oos.writeInt(mq.size());
				while(mq.size() > 0){
					Serializable obj = (Serializable)mq.pop();
					if(obj != null){
						oos.writeObject(obj);
						count++;
					}
				}
				oos.close();
				logger.info("[save-memery-queue] [OK] ["+mqFile.getAbsolutePath()+"] ["+count+"] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms]");
			}catch(Exception e){
				e.printStackTrace();
				logger.info("[save-memery-queue] [Error] ["+mqFile.getAbsolutePath()+"] [-] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms]",e);
			}
		}
	}
	
	/**
	 * 此操作用于准备重启系统，将内存队列中缓存的对象，全部写入到存储系统中，
	 * 以方便下次重启的时候，重新装载这些对象。
	 */
	public synchronized void persistForShutdown(){
		if(m_thread != null)
			m_thread.interrupt();
		
		File mqFile = new File(dir,MQ_FILENAME);
		long startTime = System.currentTimeMillis();
		if(mq.size() > 0){
			try{
				int count = 0;
				ObjectOutputStream oos = getObjectOutputStream(mqFile);
				oos.writeInt(mq.size());
				while(mq.size() > 0){
					Serializable obj = (Serializable)mq.pop();
					if(obj != null){
						oos.writeObject(obj);
						count++;
					}
				}
				oos.close();
				logger.info("[persist-for-shutdown] [OK] ["+mqFile.getAbsolutePath()+"] ["+count+"] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms]");
			}catch(Exception e){
				e.printStackTrace();
				logger.info("[persist-for-shutdown] [Error] ["+mqFile.getAbsolutePath()+"] [-] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms]",e);
			}
		}
	}
	
	
	protected void loadFiles(File rootDir,List<File> fileList){
		File fs[] = rootDir.listFiles(new FileFilter(){
			public boolean accept(File f){
				if(f.isDirectory() || f.getName().endsWith(FILE_EXTEND)){
					return true;
				}
				return false;
			}
		});
		
		for(int i = 0 ; i < fs.length ;i++){
			if(fs[i].isFile()){
				fileList.add(fs[i]);
				totalFileLength += fs[i].length();
			}else if(fs[i].isDirectory()){
				loadFiles(fs[i],fileList);
			}
		}
		
		if(fs.length == 0 && !rootDir.equals(dir)){
			if(!rootDir.delete()){
				rootDir.deleteOnExit();
			}
		}
	}
	
	public synchronized boolean push(Serializable object) {
		long startTime = System.currentTimeMillis();
		int s = getStorageSize();
		if( s > 0){
			if( s < storageCapacity){
				File b = pushIntoFile(object);
				notifyAll();
				logger.debug("[push] ["+(b==null?"fail":"succ")+"] [into-file] ["+(b==null?"-":b.getAbsolutePath())+"] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] ["+object+"]");
				return (b != null);
			}
			else{
				logger.debug("[push] [fail] [into-file] [storage_full] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] ["+object+"]");
				return false;
			}
		}else if(mq.isFull()){
			File b = pushIntoFile(object);
			logger.debug("[push] ["+(b==null?"fail":"succ")+"] [into-file] ["+(b==null?"-":b.getAbsolutePath())+"] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] ["+object+"]");
			return (b != null);
		}else{
			mq.push(object);
			if(mq.size() == 1){
				synchronized(lock){
					lock.notify();
				}
			}
			logger.debug("[push] [succ] [into-memery] [-] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] ["+object+"]");
			return true;
		}
	}
	
	protected File pushIntoFile(Serializable object){
		File file = getFile(object);
		try{
			ObjectOutputStream oos = getObjectOutputStream(file);
			oos.writeObject(object);
			oos.close();
			files.add(file);
			this.totalFileLength += file.length();
			return file;
		}catch(IOException e){
			logger.error("[save-object-into-file] ["+object+"] ["+file+"] [exception]",e);
			e.printStackTrace();
			return null;
		}
	}
	
	protected Serializable popFromFile(File file){
		try{
			ObjectInputStream bis = getObjectInputStream(file);
			Serializable obj = (Serializable)bis.readObject();
			bis.close();

			if(obj != null){
				return obj;
			}else{
				return null;
			}
		}catch(Exception e){
			logger.error("[read-object-from-file] [-] ["+file+"] [exception]",e);
			e.printStackTrace();
			return null;
		}
	}
	
	protected void deleteFile(File file){
		boolean b = file.delete();
		if(b){
			this.totalFileLength -= file.length();
		}else{
			file.deleteOnExit();
		}
	}
	
	protected ObjectOutputStream getObjectOutputStream(File file) throws IOException{
		return new ObjectOutputStream(new FileOutputStream(file));
	}

	protected ObjectInputStream getObjectInputStream(File file) throws IOException{
		return new ObjectInputStream(new FileInputStream(file));
	}

	
	protected File getFile(Serializable object){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		
		int y = cal.get(Calendar.YEAR);
		int m = cal.get(Calendar.MONTH)+1;
		int d = cal.get(Calendar.DAY_OF_MONTH);
		int h = cal.get(Calendar.HOUR_OF_DAY);
		int i = cal.get(Calendar.MINUTE);
		int s = cal.get(Calendar.SECOND);
		int ms = cal.get(Calendar.MILLISECOND);
		
		File dir1 = new File(dir,y+"-"+m+"-"+d);
		if(!dir1.exists()){
			dir1.mkdir();
		}
		
		File dir2 = new File(dir1,y+"-"+m+"-"+d+"-"+h+"-"+i);
		if(!dir2.exists()){
			dir2.mkdir();
		}
		
		if(lastCounterTime != cal.getTimeInMillis()/60000L)
			counter = 0;
		
		counter ++;
		lastCounterTime = cal.getTimeInMillis()/60000L;
		
		File f = new File(dir2,y+"-"+m+"-"+d+"-"+h+"-"+i+"-"+s+"-"+ms+"-"+counter+this.FILE_EXTEND);
		return f;
	}

	public synchronized Serializable pop() {
		long startTime = System.currentTimeMillis();
		Serializable obj = (Serializable)mq.pop();
		if(obj != null){
			if(this.getStorageSize() > 0)
				this.notifyAll();
			logger.debug("[pop] [succ] [from-memery] [direct] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] ["+obj+"]");
			return obj;
		}
		if(getStorageSize() > 0){
			this.notifyAll();
			try{
				synchronized(lock){
					lock.wait(10L);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			obj = (Serializable)mq.pop();
			
			if(obj != null){
				logger.debug("[pop] [succ] [from-memery] [after-notify] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] ["+obj+"]");
				return obj;
			}else{
				logger.debug("[pop] [fail] [from-memery] [after-notify] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] [-]");
				return null;
			}
		}else{
			logger.debug("[pop] [succ] [from-memery] [direct] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] [-]");
			return null;
		}
	}
	
	public synchronized Serializable pop(long timeout) {
		if(timeout < 0) timeout = 0;
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis() + timeout;
		
		Serializable obj = (Serializable)mq.pop();
		if(obj != null){
			if(this.getStorageSize() > 0)
				this.notifyAll();
			logger.debug("[pop-timeout] [succ] [from-memery] [direct] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] ["+obj+"]");
			return obj;
		}
		
		while(true){
			long now = System.currentTimeMillis();
			if(timeout > 0 && now >= endTime){
				logger.debug("[pop-timeout] [succ] [timeout] [direct] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] ["+obj+"]");
				return null;
			}
			if(getStorageSize() > 0){
				this.notifyAll();
			}
			
			try{
				synchronized(lock){
					if(timeout == 0)
						lock.wait();
					else
						lock.wait(endTime - now);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			obj = (Serializable)mq.pop();
			if(obj != null){
				logger.debug("[pop-timeout] [succ] [from-memery] [direct] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] ["+obj+"]");
				return obj;
			}
		}
		
	}


	public synchronized Serializable peek() {
		long startTime = System.currentTimeMillis();
		Serializable obj = (Serializable)mq.peek();
		if(obj != null){
			logger.debug("[peek] [succ] [from-memery] [direct] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] ["+obj+"]");
			return obj;
		}
		if(getStorageSize() > 0){
			this.notifyAll();
			try{
				synchronized(lock){
					lock.wait(10L);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			obj = (Serializable)mq.peek();
			if(obj != null){
				logger.debug("[peek] [succ] [from-memery] [after-notify] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] ["+obj+"]");
				return obj;
			}else{
				logger.debug("[peek] [fail] [from-memery] [after-notify] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] [-]");
				return null;
			}
		}else{
			logger.debug("[peek] [succ] [from-memery] [direct] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] [-]");
			return null;
		}
		
	}

	public int size() {
		return mq.size() + this.getStorageSize();
	}

	public int capacity(){
		return this.getMemeryCapacity() +  this.getStorageCapacity();
	}
	
	public synchronized void clear() {
		long startTime = System.currentTimeMillis();
		int k = mq.size();
		mq.clear();
		int count = 0;	
		while(files.size() > 0){
			File file = files.removeFirst();
			deleteFile(file);
			count ++;
		}
		logger.debug("[clear] [succ] ["+k+"] ["+count+"] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] [-]");
	}

	public int getMemerySize() {
		return mq.size();
	}

	public int getMemeryCapacity() {
		return this.memeryCapacity;
	}

	public int getStorageSize() {
		return files.size();
	}

	public int getStorageCapacity() {
		return this.storageCapacity;
	}

	public void setMemeryCapacity(int capacity) {
		if(capacity >= getMemerySize()){
			this.memeryCapacity = capacity;
		}else{
			throw new IllegalArgumentException("capacity less than current memery size");
		}
	}

	public void setStorageCapacity(int capacity) {
		if(capacity >= getStorageSize()){
			this.storageCapacity = capacity;
		}else{
			throw new IllegalArgumentException("capacity less than current storage size");
		}
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean isFull() {
		return getStorageSize() == this.getStorageCapacity();
	}

	public void run() {
		long sss = System.currentTimeMillis();
		while(Thread.currentThread().isInterrupted() == false){
			try{
				long startTime = System.currentTimeMillis();
				if(this.getStorageSize() > 0 && getMemerySize() < this.getMemeryCapacity()){
					synchronized(this){
						int num = Math.min(getStorageSize(),getMemeryCapacity() - getMemerySize());
						if(num > 0){
							File file = files.get(0);
							Serializable obj = popFromFile(file);
							if(Thread.currentThread().isInterrupted())
								break;
							if(obj != null){
								mq.push(obj);
								synchronized(lock){
									lock.notify();
								}
								logger.debug("[in-queue-thread] [succ] [file-to-memery] ["+file+"] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] ["+obj+"]");
							}else{
								logger.debug("[in-queue-thread] [fail] [file-to-memery] ["+file+"] " + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"ms] [-]");
							}
							files.removeFirst();
							deleteFile(file);
						}
					}
				}else{
					if(Thread.currentThread().isInterrupted())
						break;
					synchronized(this){
						if(Thread.currentThread().isInterrupted())
							break;
						try{
							logger.debug("[in-queue-thread] [-] [ready-to-sleep] [5s]" + getQueueStatusForLogger()+" ["+(System.currentTimeMillis() - startTime)+"] [-]");
							wait(5000L);
						}catch(Exception e){
							//e.printStackTrace();
						}
					}
				}
			}catch(Throwable e){
				logger.error("[in-queue-thread] [catch-exception]",e);
				e.printStackTrace();
			}
		}
		
		logger.debug("[in-queue-thread] [exit] [ready-to-exit] [-] " + getQueueStatusForLogger()+" ["+StringUtil.addcommas(System.currentTimeMillis() - sss)+"] [-]");
		
		
	}
	
	public class MyShutdownHook extends Thread{
		
		public void run(){
			if(m_thread != null)
				m_thread.interrupt();
			saveMemeryQueue();
		}
	}

}

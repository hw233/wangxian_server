package com.xuanzhi.tools.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;

/**
 * 一个可以按日超时删除的散列存储对象的实现
 * 适用于对性能要求不算很高的应用场合，本地测试性能为(以存储800KB对象为例)：平均存储耗时2ms/个,读取为10ms/个
 * 
 */
public class ObjectDiskSerializer<T> implements Runnable {
	
	public static Logger logger = LoggerFactory.getLogger(ObjectDiskSerializer.class);
	
	/**
	 * 保留的天数
	 */
	private int keepDays;
	
	/**
	 * 存储的根目录
	 */
	private String rootPath;
	
	/**
	 * 当前的天
	 */
	private String dayOfYear;
	
	/**
	 * 线程
	 */
	private Thread thread;
	
	public ObjectDiskSerializer(int keepDays, String rootPath) {
		this.keepDays = keepDays;
		this.rootPath = rootPath;
		this.dayOfYear = DateUtil.formatDate(new Date(), "yyyyMMdd");
		this.thread = new Thread(this, "ObjectDiskSerializer");
		this.thread.start();
	}
	
	/**
	 * 存储一个对象数据
	 * @param key 不能包含特殊字符，例如: '_', '.'
	 * @param t
	 * @return
	 */
	public T save(String key, Serializable t) throws Exception {
		String hash = StringUtil.hash(key);
		String path = this.getSavePath(rootPath, hash) + "/" + this.dayOfYear + "_" + key + ".obj";
		try {
			byte data[] = this.getByteData(t);
			FileUtils.writeFile(path, data);
			return (T)t;
		} catch(Exception e) {
			logger.error("[存储对象时发生异常] [key:"+key+"]", e);
			throw e;
		}
	}
	
	/**
	 * 获得一个对象数据
	 * @param key 不能包含特殊字符，例如: '_', '.'
	 * @return
	 */
	public T get(String key) throws Exception {
		String hash = StringUtil.hash(key);
		String path = this.getSavePath(rootPath, hash);
		File f = this.searchFile(path, key);
		if(f == null) {
			return null;
		}
		byte[] data = FileUtils.readFileData(f.getPath());
		if(data.length == 0) {
			return null;
		}
		ByteArrayInputStream bin = new ByteArrayInputStream(data);
		try {
			ObjectInputStream in = new ObjectInputStream(bin);
			return (T)in.readObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[通过key获得对象时发生异常] [key:"+key+"]", e);
			throw e;
		}
	}
	
	public void remove(String key) {
		String hash = StringUtil.hash(key);
		String path = this.getSavePath(rootPath, hash);
		File f = this.searchFile(path, key);
		if(f != null) {
			f.delete();
		}
	}
	
	public File searchFile(String path, String key) {
		File files[] = new File(path).listFiles();
		if(files != null) {
			for(File f : files) {
				String name = f.getName();
				name = name.split("\\.")[0];
				name = name.split("_")[1];
				if(name.equals(key)) {
					return f;
				}
			}
		}
		return null;
	}
	
	public String getSavePath(String path, String hash) {
		path = path + "/" + hash.substring(0, 2);
		path = path + "/" + hash.substring(2, 4);
		return path;
	}
	
	public byte[] getByteData(Serializable t) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bout);
		out.writeObject(t);
		out.close();
		bout.close();
		return bout.toByteArray();
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(5000);
				String day = DateUtil.formatDate(new Date(), "yyyyMMdd");
				if(!this.dayOfYear.equals(day)) {
					this.dayOfYear = day;
					notifyDayChange();
				}
			} catch(Throwable e) {
				logger.error("[心跳时发生异常]", e);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void notifyDayChange() {
		long start = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -this.keepDays-1);
		String day = DateUtil.formatDate(cal.getTime(), "yyyyMMdd");
		File file = new File(rootPath);
		this.searchAndRemoveTimeout(file, day);
		if(logger.isInfoEnabled()) {
			logger.info("[每日定时清除超时的数据] [共耗时:"+(System.currentTimeMillis()-start)+"ms]");
		}
	}
	
	public void searchAndRemoveTimeout(File file, String day) {
		if(file.isDirectory()) {
			File fs[] = file.listFiles();
			for(File f : fs) {
				searchAndRemoveTimeout(f, day);
			}
		} else {
			String name = file.getName();
			name = name.split("\\.")[0];
			name = name.split("_")[0];
			if(name.equals(day)) {
				file.delete();
			}
		}
	}
	
	public static void main(String args[]) {
		ObjectDiskSerializer<TestObject> os = new ObjectDiskSerializer<TestObject>(2, "f:/ostest");
		long start = System.currentTimeMillis();
		int n = 1000;
		for(int i=0; i<n; i++) {
			TestObject t = new TestObject(i, "test" + i);
			try {
				os.save(t.getName(), t);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("存储" + n + "个对象, 共耗时:" + (System.currentTimeMillis()-start) + "ms.");
		start = System.currentTimeMillis();
		os.notifyDayChange();
		System.out.println("检查一遍存储, 共耗时:" + (System.currentTimeMillis()-start) + "ms.");
		start = System.currentTimeMillis();
		for(int i=0; i<n; i++) {
			try {
				TestObject t = os.get("test" + i);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("查询" + n + "个对象, 共耗时:" + (System.currentTimeMillis()-start) + "ms. 平均每次查询耗时:" + (System.currentTimeMillis()-start)/n + "ms.");
	}
	
	public static class TestObject implements Serializable {
		
		public static final long serialVersionUID = 589324826429832L;
		
		private int id;
		
		private String name;
		
		private byte[] data;
		
		public TestObject(int id, String name) {
			this.id = id;
			this.name = name;
			this.data = new byte[800000];
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public byte[] getData() {
			return data;
		}

		public void setData(byte[] data) {
			this.data = data;
		}
		
		
	}
}

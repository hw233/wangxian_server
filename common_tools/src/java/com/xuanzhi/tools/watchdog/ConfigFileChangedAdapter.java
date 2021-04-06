package com.xuanzhi.tools.watchdog;

import java.io.*;
import java.lang.ref.WeakReference;
import java.util.*;
/**
 * 配置文件修改跟踪适配器
 * 
 *
 */
public class ConfigFileChangedAdapter {

	protected static ConfigFileChangedAdapter m_self = null;
	
	public static ConfigFileChangedAdapter getInstance(){
		if(m_self != null) return m_self;
		synchronized(ConfigFileChangedAdapter.class){
			m_self = new ConfigFileChangedAdapter();
			return m_self;
		}
	}
	protected MyFileWatchdog w;
	protected HashMap<String,List<ConfigFileChangedListener>> maps = new HashMap<String,List<ConfigFileChangedListener>>();
	protected ConfigFileChangedAdapter(){
		w = new MyFileWatchdog();
		w.setDelay(30000L);
		w.setName("File-Change-Adapter-Thread");
		w.start();
	}
	
	/**
	 * 对文件进行监听，当文件发生改变的时候，通知ConfigFileChangedListener。
	 * 
	 * @param file
	 * @param listener
	 */
	public void addListener(File file,ConfigFileChangedListener listener){
		
		List<ConfigFileChangedListener> l = maps.get(file.getAbsolutePath());
		if(l != null){
			l.add(listener);			
		}else{
			l = new LinkedList<ConfigFileChangedListener>();
			maps.put(file.getAbsolutePath(),l);
			l.add(listener);
			w.addFile(file);
		}
		
	}
	
	public void removeListener(File file,ConfigFileChangedListener listener){
		List<ConfigFileChangedListener> l = maps.get(file.getAbsolutePath());
		if(l != null){
			Iterator<ConfigFileChangedListener> it = l.iterator();
			while(it.hasNext()){
				ConfigFileChangedListener wr = it.next();
				if(wr != null && listener.equals(wr)){
					it.remove();
				}
			}
			if(l.size() == 0){
				maps.remove(file.getAbsolutePath());
				
			}
		}
	}
	
	protected class MyFileWatchdog extends FileWatchdog{

		protected void doOnChange(File file) {
			List<ConfigFileChangedListener> l = maps.get(file.getAbsolutePath());
			if(l != null){
				Iterator<ConfigFileChangedListener> it = l.iterator();
				while(it.hasNext()){
					ConfigFileChangedListener wr = it.next();
					if(wr != null){
						try{
							wr.fileChanged(file);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}


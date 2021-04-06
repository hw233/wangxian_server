package com.xuanzhi.tools.listcache.event;

import java.lang.ref.WeakReference;
import java.util.*;

import com.xuanzhi.tools.listcache.concrete.LruListCache;

import com.xuanzhi.tools.ds.PrefixTree;
import com.xuanzhi.tools.ds.PrefixTree.TreeNode;
public class ListCacheEventHandler {

	private static PrefixTree pTree = new PrefixTree();
	
	public static void addListener(PropertyChangeListener listener){
		String p = listener.getProperty();
		String n = listener.getName();
		
		if(p != null && n != null){
			synchronized(pTree){
				String path = p + "." + n;
				String paths[] = path.split("\\.");
				pTree.insert(paths,new WeakReference(listener));
			}
		}
	}
	
	
	public static void removeListener(PropertyChangeListener listener){
		String p = listener.getProperty();
		String n = listener.getName();
		
		if(p != null && n != null){
			synchronized(pTree){
				String path = p + "." + n;
				String paths[] = path.split("\\.");
				pTree.remove(paths);
			}
		}
	}
	
	public static void searchListener(HashSet listeners,String path){
		boolean search = false;
		if(path.endsWith(".*")){
			path = path.substring(0,path.length()-2);
			search = true;
		}
		String paths[] = path.split("\\.");
		if(search){
			List<TreeNode> list = pTree.search(paths);
			Iterator it = list.iterator();
			while(it.hasNext()){
				TreeNode tn = (TreeNode)it.next();
				WeakReference wr = (WeakReference)tn.getObject();
				if(wr != null){
					PropertyChangeListener l = (PropertyChangeListener)wr.get();
					if(l != null)
						listeners.add(l);
				}
			}
		}else{
			TreeNode tn = pTree.find(paths);
			if(tn != null){
				WeakReference wr = (WeakReference)tn.getObject();
				if(wr != null){
					PropertyChangeListener l = (PropertyChangeListener)wr.get();
					if(l != null)
						listeners.add(l);
				}
			}
		}

	}
	
	public static void fire(ObjectDeleteEvent event){
		String ns = event.getNameScope();
		String ps[] =  event.getProperties();
		String nsArray[] = ns.split(";");
		HashSet listeners = new HashSet();
		for(int j = 0 ; j < ps.length ; j++){
			for(int i = 0 ; i < nsArray.length ;i++){
				String path = ps[j] + "." +nsArray[i];
				searchListener(listeners,path);
			}
		}
		Iterator it = listeners.iterator();
		while(it.hasNext()){
			PropertyChangeListener l = (PropertyChangeListener)it.next();
			l.objectDeleted(event);
		}
		listeners.clear();
	}
	
	public static void fire(PropertyChangeEvent event){
		String ns = event.getNameScope();
		String nsArray[] = ns.split(";");
		HashSet listeners = new HashSet();
		for(int i = 0 ; i < nsArray.length ;i++){
			String path = event.getProperty() + "." +nsArray[i];
			searchListener(listeners,path);
		}
		Iterator it = listeners.iterator();
		while(it.hasNext()){
			PropertyChangeListener l = (PropertyChangeListener)it.next();
			l.propertyChanged(event);
		}
		listeners.clear();
	}
	
	public static void fire(ObjectCreateEvent event){
		String ns = event.getNameScope();
		String ps[] =  event.getProperties();
		String nsArray[] = ns.split(";");
		HashSet listeners = new HashSet();
		for(int j = 0 ; j < ps.length ; j++){
			for(int i = 0 ; i < nsArray.length ;i++){
				String path = ps[j] + "." +nsArray[i];
				searchListener(listeners,path);
			}
		}
		Iterator it = listeners.iterator();
		while(it.hasNext()){
			PropertyChangeListener l = (PropertyChangeListener)it.next();
			l.objectCreated(event);
		}
		listeners.clear();
	}
}

package com.xuanzhi.tools.ds;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 前缀树结构，每个节点包含一个直接子节点的列表。
 * 
 * 前缀树的插入，查询，删除操作都不会超过前缀树的最大深度。
 * 
 * 
 *
 */
public class PrefixTree {
	public static class TreeNode{
		protected Object obj;
		protected Object name;
		protected TreeNode parent;
		protected LinkedHashMap<Object,TreeNode> children = new LinkedHashMap<Object,TreeNode>();
		
		public TreeNode(Object name,TreeNode parent,Object o){
			this.name = name;
			this.parent = parent;
			this.obj = o;
		}
		
		public Object getObject(){
			return obj;
		}
		
		public boolean contains(Object name){
			return children.containsKey(name);
		}
		
		public TreeNode getChild(Object name){
			return children.get(name);
		}
		
		public Collection<TreeNode> children(){
			return children.values();
		}
	}
	
	protected TreeNode root = new TreeNode(null,null,null);
	
	
	
	public TreeNode find(Object paths[]){
		if(paths == null) return null;
		TreeNode n = root;
		for(int i = 0 ; i < paths.length ; i++){
			n = n.getChild(paths[i]);
			if(n == null) break;
		}
		return n;
	}
	
	/**
	 * 广度优先搜索
	 * @param paths
	 * @return
	 */
	public List<TreeNode> search(Object paths[]){
		LinkedList<TreeNode> list = new LinkedList<TreeNode>();
		TreeNode n = find(paths);
		searchWFS(list,n);
		return list;
	}
	
	protected void searchWFS(LinkedList<TreeNode> list,TreeNode node){
		if(node == null) return;
		
		if(node.getObject() != null)
			list.add(node);
		
		if(node.children.size() == 0) return;
		Iterator it = node.children().iterator();
		while(it.hasNext()){
			TreeNode tn = (TreeNode)it.next();
			searchWFS(list,tn);
		}
	}
	
	public TreeNode insert(Object paths[],Object obj){
		TreeNode n = root;
		for(int i = 0 ; i < paths.length ; i++){
			TreeNode n2 = n.getChild(paths[i]);
			if(n2 == null){
				n2 = new TreeNode(paths[i],n,null);
				n.children.put(n2.name,n2);
			}
			n = n2;
		}
		n.obj = obj;
		return n;
	}
	
	public TreeNode insert(TreeNode parent,Object name,Object obj){
		TreeNode n = parent.getChild(name);
		if(n == null){
			n = new TreeNode(name,parent,obj);
			parent.children.put(n.name,n);
		}
		n.obj = obj;
		return n;
	}
	
	public void remove(Object paths[]){
		TreeNode n = find(paths);
		if(n != null)
			n.obj = null;
		if(n.children.size() == 0 && n.parent != null){
			n.parent.children.remove(n.name);
		}
	}
	
	public void clear(){
		root.children.clear();
	}
	
	
}

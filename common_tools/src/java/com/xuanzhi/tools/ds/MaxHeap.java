package com.xuanzhi.tools.ds;

import java.util.Comparator;

/**
 * 最大堆的实现
 * 
 */
public class MaxHeap {

	private Comparator c;
	private Object nodes[];
	private int size;
	
	public MaxHeap(Comparator c){
		this.c = c;
		clear();
	}
	
	public void clear(){
		size = 0;
		nodes = new Object[10];
		
	}
	
	public Object findMax(){
		if(size>0) return nodes[0];
		return null;
	}
	
	public Object deleteMax(){
		if(size == 0) return null;
		Object m = nodes[0];
		size--;
		nodes[0] = nodes[size];
		shitDown(0,size);
		return m;
	}
	
	public void insert(Object t){
		if(nodes.length == size){
			Object newNodes[] = new Object[nodes.length+nodes.length];
			System.arraycopy(nodes, 0, newNodes, 0, nodes.length);
			nodes = newNodes;
		}
		nodes[size] = t;
		size++;
		percolate(size-1);
	}
	
	/**
	 * 从节点i向下过程筛选节点i以在T[0..n-1]上重建堆性质
	 * @param i
	 */
	public void shitDown(int i,int n){
		int k = i;
		int j;
		do{
			j = k;
			if(2*j < n && c.compare(nodes[2*j],nodes[k])>0)
				k = 2 * j;
			if(2*j < n-1 && c.compare(nodes[2*j+1],nodes[k])>0)
				k = 2*j+1;
			if(j!=k){
				Object o = nodes[j];
				nodes[j] = nodes[k];
				nodes[k] = o;
			}
		}while(j != k);
	}
	
	/**
	 * 从节点i向上过程筛选节点i以在T[0..n-1]上重建堆性质
	 * @param i
	 */
	public void percolate(int i){
		int k = i;
		int j;
		do{
			j = k;
			if(j > 0 && c.compare(nodes[j/2], nodes[k]) < 0)
				k = j/2;
			if(j!=k){
				Object o = nodes[j];
				nodes[j] = nodes[k];
				nodes[k] = o;
			}
		}while(j != k);
	}
	
	/**
	 * 用一个数组组建一个堆，算法时间复杂度为O(n)
	 * @param os
	 * @param offset
	 * @param size
	 * @param c
	 * @return
	 */
	public static MaxHeap makeHeap(Object os[],int offset,int size,Comparator c){
		MaxHeap m = new MaxHeap(c);
		m.nodes = new Object[size];
		m.size = size;
		System.arraycopy(os, offset, m.nodes, 0, size);
		
		for(int i = size/2 ; i >= 0 ; i--){
			m.shitDown(i,m.size);
		}
		return m;
	}
	
	/**
	 * 从小到大排序，时间复杂度为O(n*logn)
	 * @param os
	 * @param c
	 */
	public static void heapsort(Object os[],int length,Comparator c){
		MaxHeap m = new MaxHeap(c);
		m.nodes = os;
		m.size = length;
		for(int i = length/2 ; i >= 0 ; i--){
			m.shitDown(i,m.size);
		}

		for(int i = m.size-1 ; i >= 1 ; i--){
			Object o = m.nodes[0];
			m.nodes[0] = m.nodes[i];
			m.nodes[i] = o;
			m.shitDown(0,i);
		}
	}
	
	public int size(){
		return size;
	}
}

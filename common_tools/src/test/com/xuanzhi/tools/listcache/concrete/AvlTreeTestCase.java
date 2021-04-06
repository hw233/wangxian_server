package com.xuanzhi.tools.listcache.concrete;

import java.util.Comparator;

import junit.framework.*;

public class AvlTreeTestCase extends TestCase{

	public AvlTreeTestCase(){
		super("AvlTreeTest");
	}
	
	AvlTree avlTree = new AvlTree();
	Comparator c ;
	int n = 1000;
	public void setUp() throws Exception{

		c = new Comparator(){
			public int compare(Object arg0, Object arg1) {
				int i0 = Integer.parseInt((String)arg0);
				int i1 = Integer.parseInt((String)arg1);
				if(i0 < i1) return -1;
				if(i0 > i1) return 1;
				return 0;
			}
		};
		
		for(int i = 0 ; i < n ; i++){
			int start = (i+1)*100;
			int size = 25 + 25 * (start % 3);
			Object objs[] = new Object[size];
			for(int j = 0 ; j < size ; j++){
				objs[j] = "" + (start + j);
			}
			avlTree.insert(c,objs,start,size);
		}
	}
	
	public void tearDown(){
		avlTree.clear();
	}
	
	public void testCreate() throws Exception{
		
		Assert.assertEquals(n,avlTree.size());
		
		BlockTreeNode tn = avlTree.minimum();
		Assert.assertNotNull(tn);
		int k = 0;
		
		while(tn != null){
			k++;
			Assert.assertEquals(k*100,tn.start);
			Assert.assertEquals(25+(tn.start % 3)*25,tn.size);
			Assert.assertEquals(""+(k*100),tn.ids[0]);
			
			tn = avlTree.next(tn);
			
			if(k < n){
				Assert.assertNotNull(tn);
			}
		}
		
		tn = avlTree.maximum();
		Assert.assertNotNull(tn);
		k = n;
		while(tn != null){
			Assert.assertEquals(k*100,tn.start);
			Assert.assertEquals(25+(tn.start % 3)*25,tn.size);
			Assert.assertEquals(""+(k*100),tn.ids[0]);
			k--;
			tn = avlTree.previous(tn);
			if(k > 0){
				Assert.assertNotNull(tn);
			}
		}
	}
	
	public void testFindObject(){
		BlockTreeNode tn = avlTree.find("55501");
		
		Assert.assertNotNull(tn);
		
		Assert.assertEquals("55500",tn.ids[0]);
		Assert.assertEquals(55500,tn.start);
		
		tn = avlTree.find("149");
		Assert.assertNotNull(tn);
		Assert.assertEquals(100,tn.start);
		
		for(int i = 0 ; i < 100 ; i ++){
			int k = 100 + (int)(Math.random() * 10000);
			tn = avlTree.find(""+k);
			Assert.assertNotNull(tn);
			
			int r = k % 100;
			int s = (k/100) * 100;
			int t = 25 + 25 * (s % 3);
			if(r < t){
				Assert.assertEquals(s,tn.start);
			}else{
				Assert.assertEquals(s+100,tn.start);
			}
		}
		
	}
	
	public void testFindStart(){
		BlockTreeNode tn = avlTree.find(50);
		Assert.assertNull(tn);
		
		tn = avlTree.find(150);
		Assert.assertNotNull(tn);
		Assert.assertEquals(100,tn.start);
		
		tn = avlTree.find(55524);
		Assert.assertNotNull(tn);
		Assert.assertEquals(55500,tn.start);
		
		tn = avlTree.find(55599);
		Assert.assertNotNull(tn);
		Assert.assertEquals(55500,tn.start);
		
		tn = avlTree.find(110000);
		Assert.assertNotNull(tn);
		Assert.assertEquals(100000,tn.start);
	}
	
	public void testInsert(){
		BlockTreeNode tn = avlTree.find(55524);
		Assert.assertNotNull(tn);
		Assert.assertEquals(55500,tn.start);
		
		tn = avlTree.next(tn);
		Assert.assertEquals(55600,tn.start);
		
		Object objs[] = new Object[75];
		for(int i = 0 ; i < objs.length ; i++){
			objs[i] = "" + (55525 + i);
		}
		avlTree.insert(c,objs,55525,75);
		
		tn = avlTree.find(55524);
		tn = avlTree.next(tn);
		Assert.assertEquals(55525,tn.start);
		
		tn = avlTree.next(tn);
		Assert.assertEquals(55600,tn.start);
		
		
	}
	
	public void testRemove(){
		BlockTreeNode tn = avlTree.find(150);
		Assert.assertNotNull(tn);
		Assert.assertEquals(100,tn.start);
		
		avlTree.remove(tn);
		tn = avlTree.find(150);
		Assert.assertNull(tn);
		
		tn = avlTree.find(55524);
		Assert.assertNotNull(tn);
		Assert.assertEquals(55500,tn.start);
		
		avlTree.remove(tn);
		
		tn = avlTree.find(55524);
		Assert.assertNotNull(tn);
		Assert.assertEquals(55400,tn.start);
		
		tn = avlTree.next(tn);
		Assert.assertEquals(55600,tn.start);
		
	}
	
}

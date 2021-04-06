package com.xuanzhi.tools.transport;

import java.util.Comparator;

import com.xuanzhi.tools.ds.AvlTree;
import com.xuanzhi.tools.ds.AvlTree.TreeNode;

import junit.framework.*;

public class AvlTreeTestCase extends TestCase{

	
	
	public static class Connection{
		String name;
		long timeout;
		public Connection(int index){
			name = "conn-"+index;
		}
		
		public long getTimeForNextTimeout(){
			return timeout; 
		}
		
		public String getName(){
			return name;
		}
	}
	
	
	public void testA() throws Exception{
		AvlTree connTree = new AvlTree(new Comparator(){
					public int compare(Object o1, Object o2) {
						if(o1 == o2) return 0;
						Connection c1 = (Connection)o1;
						Connection c2 = (Connection)o2;
						if(c1.getTimeForNextTimeout() < c2.getTimeForNextTimeout()) return -1;
						if(c1.getTimeForNextTimeout() > c2.getTimeForNextTimeout()) return 1;
						int r = c1.getName().compareTo(c2.getName());
						if(r < 0) return -1;
						if(r > 0) return 1;
						return 0;
					}
					
				});
		
		Connection conn1 = new Connection(1);
		Connection conn2 = new Connection(2);
		Connection conn3 = new Connection(3);
		
		conn3.timeout = System.currentTimeMillis() + 1800000L;
		connTree.insert(conn3);
		
		Thread.sleep(1);
		
		conn2.timeout = System.currentTimeMillis() + 1800000L;
		connTree.insert(conn2);
		
		Thread.sleep(1);
		connTree.remove(conn2);
		
		Thread.sleep(2);
		connTree.remove(conn3);
		
		conn1.timeout = System.currentTimeMillis() + 1800000L;
		connTree.insert(conn1);
		
		connTree.remove(conn1);
		
		conn2.timeout = System.currentTimeMillis() + 1800000L;
		conn3.timeout = System.currentTimeMillis() + 1800000L;
		conn1.timeout = System.currentTimeMillis() + 1800000L;
		connTree.insert(conn2);
		
		Thread.sleep(1);
		
		connTree.insert(conn3);
		connTree.insert(conn1);
		
		Thread.sleep(8);
		connTree.remove(conn2);
		connTree.remove(conn1);
		connTree.remove(conn3);
		
		conn2.timeout = System.currentTimeMillis() + 1800000L;
		conn3.timeout = System.currentTimeMillis() + 1800000L;
		conn1.timeout = System.currentTimeMillis() + 1800000L;
		
		connTree.insert(conn1);
		connTree.insert(conn2);
		connTree.insert(conn3);
		
		
		
		
	}
	
	static String toString(String name,Connection conn,AvlTree tree){
		long l = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		sb.append(name+","+conn.getName()+":"+(conn.getTimeForNextTimeout() - l)+",tree{");
		
		TreeNode tn = tree.minimum();
		while(tn != null){
			Connection conn2 = (Connection)tn.getObject();
			sb.append(conn2.getName()+":"+(conn2.getTimeForNextTimeout() - l)+"->");
			tn = tree.next(tn);
		}
		sb.append("}");
		return sb.toString();
	}
}

package com.xuanzhi.tools.objectdb;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.xuanzhi.tools.objectdb.DBMap.KeyValue;

/**
 *
 * 
 */
public class Test {
	public static void main(String args[]) {
		long now = System.currentTimeMillis();
		EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();
		ObjectContainer db = Db4oEmbedded.openFile(conf, "d:/db2.db");
		int num = 10000;
		for(int i=0; i<num; i++) {
			Point p = new Point();
			p.name = "qqqqqqqqqqqqqqqq" + i;
			p.x = i+100;
			p.y = i+1000;
			db.store(p);
		}
		System.out.println("put "+num+" elements elaped " + (System.currentTimeMillis()-now) + "ms");
		now = System.currentTimeMillis();
		Query query = db.query();
		query.constrain(Point.class);
		ObjectSet result = query.execute();
		int count = result.size();
		System.out.println(count + " elements in db. elaped " + (System.currentTimeMillis()-now) + "ms");
		db.close();
	}
	

	public static final class Point {
		String name;
		int x;
		int y;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		
		
	}
}

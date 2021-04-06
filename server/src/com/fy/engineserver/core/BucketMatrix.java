package com.fy.engineserver.core;

/**
 * 桶阵列，桶阵列中保存的是某个地图上的一个二维的桶
 * 
 */
public class BucketMatrix {

	public Game game;
	public int x,y;
	public Bucket buckets[][];
	public int w;
	public int h;
	public BucketMatrix(Game game){
//		int a = game.gi.a * game.gi.rows;
//		int b = game.gi.b * game.gi.columns;
		int mapw = game.gi.getWidth();
		int maph = game.gi.getHeight();
		w = game.gm.getBucketWidth();
		h = game.gm.getBucketHeight();
		x = mapw/w;
		if(x * w < mapw)  x++;
		y = maph/h;
		if(y * h < maph) y++;
		this.game = game;
		
		buckets = new Bucket[x][y];
		for(int i = 0 ; i < x ; i++){
			for(int j = 0 ; j < y ; j++){
				buckets[i][j] = new Bucket(this,i,j);
			}
		}
	}
	
	public Bucket[][] getBuckets(){
		return buckets;
	}
	
	/**
	 * 清空桶阵列中各个桶中的数据
	 */
	public void clear(){
		for(int i = 0 ; i < x ; i++){
			for(int j = 0 ; j < y ; j++){
				if(buckets[i][j].size() > 0)
					buckets[i][j].clear();
			}
		}
	}
}

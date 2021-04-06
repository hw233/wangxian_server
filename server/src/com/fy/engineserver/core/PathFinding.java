package com.fy.engineserver.core;

import java.util.Comparator;
import java.util.HashMap;

import com.fy.engineserver.datasource.language.Translate;
import com.xuanzhi.tools.ds.Heap;

public class PathFinding {

	public static class Path {
		public GameInfo gi;
		// 路径的长度，也就是单元格的个数
		public int len;

		// 开始单元格
		public Cell startCell;
		// 结束单元格
		public Cell endCell;
		// 当前单元格
		public Cell currentCell;
	}

	/**
	 * 单元格
	 * 
	 * 
	 * 
	 * 
	 */
	public static class Cell {

		int i, j;

		int F, G, H;

		public Cell parent;

		public Cell next;

		public Cell(int i, int j) {
			this.i = i;
			this.j = j;
		}

		public boolean equals(Object o) {
			if (o instanceof Cell) {
				Cell c = (Cell) o;
				if (c.i == i && c.j == j)
					return true;
			}
			return false;
		}

		public void reset() {
			i = j = 0;
			F = G = H = 0;
			parent = null;
			next = null;
		}
	}

	public static final int COLUMNS = 10000;

	public static class HeapMap {
		private Heap heap = null;
		private HashMap<Integer, Cell> map = null;

		public HeapMap(Comparator<Cell> c) {
			heap = new Heap(c);
			map = new HashMap<Integer, Cell>();
		}

		public boolean isEmpty() {
			return heap.size() == 0;
		}

		public boolean contains(Cell c) {
			return map.containsKey(c.i * COLUMNS + c.j);
		}

		public Cell get(Cell c) {
			return (Cell) map.get(c.i * COLUMNS + c.j);
		}

		public void insert(Cell c) {
			heap.insert(c);
			map.put(c.i * COLUMNS + c.j, c);
		}

		public Cell extract() {
			Cell c = (Cell) heap.extract();
			if (c != null) {
				map.remove(c.i * COLUMNS + c.j);
			}
			return c;
		}

		public void remove(Cell c) {
			Cell n = (Cell) map.remove(c.i * COLUMNS + c.j);
			if (n != null)
				heap.remove(n);
		}

		public Cell[] toCells() {
			return (Cell[]) map.values().toArray(new Cell[0]);
		}

		public void clear() {
			heap.clear();
			map.clear();
		}
	}

	public static class MyMap {
		private HashMap<Integer, Cell> map = null;

		public MyMap() {
			map = new HashMap<Integer, Cell>();
		}

		public boolean isEmpty() {
			return map.size() == 0;
		}

		public boolean contains(Cell c) {
			return map.containsKey(c.i * COLUMNS + c.j);
		}

		public Cell get(Cell c) {
			return (Cell) map.get(c.i * COLUMNS + c.j);
		}

		public void insert(Cell c) {
			map.put(c.i * COLUMNS + c.j, c);
		}

		public void remove(Cell c) {
			map.remove(c.i * COLUMNS + c.j);
		}

		public Cell[] toCells() {
			return map.values().toArray(new Cell[0]);
		}

		public void clear() {
			map.clear();
		}

	}

	GameInfo gm;
	int startX, startY, endX, endY;
	Cell start;
	Cell end;

	HeapMap OPEN;
	MyMap CLOSE;

	static int D = 5000;

	public static final int NONE = 0;
	public static final int VERTICAL_FIRST = 1;
	public static final int HORIZENTAL_FIRST = 2;
	public static final int DIAGONAL_FIRST = 3;
	public static final int INTELLIGENT = 4;
	public static final int Dijkstra = 5;
	public static final int BFS = 6;
	public static final String[] TYPENAMES =
		new String[] { Translate.text_3101, Translate.text_3102, Translate.text_3103, Translate.text_3104, Translate.text_3105,
			Translate.text_3106, Translate.text_3107 };

	public static String getTypeName(int type) {
		if (type >= 0 && type < TYPENAMES.length) {
			return TYPENAMES[type];
		} else {
			return Translate.text_3108 + type + "}";
		}
	}

	public PathFinding(GameInfo gm, int startX, int startY, int endX, int endY) {
		this.gm = gm;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;

		start = new Cell(startX / gm.ejectA, startY / gm.ejectB);
		end = new Cell(endX / gm.ejectA, endY / gm.ejectB);

		OPEN = new HeapMap(new Comparator<Cell>() {
			public int compare(Cell c1, Cell c2) {
				if (c1 == c2)
					return 0;
				if (c1.F < c2.F)
					return -1;
				if (c1.F > c2.F)
					return 1;
				if (c1.H < c2.H)
					return -1;
				if (c1.H > c2.H)
					return 1;
				return 0;
			}
		});
		CLOSE = new MyMap();
	}

	/**
	 * 路径必须经过某些点
	 * 
	 * @param type
	 * @param passThroughX
	 * @param passThroughY
	 */
	/*
	 * public Path astar(int type,int maxStep,int passThroughX[],int
	 * passThroughY[]){ int step = 0; Cell lastEnd = null; for(int k = 0 ; k <
	 * passThroughX.length + 1; k++){ Cell start = null; Cell end = null; if(k
	 * == 0){ start = this.start; }else{ start = new
	 * Cell(passThroughX[k-1]/gm.ejectA,passThroughY[k-1]/gm.ejectB); } if(k <
	 * passThroughX.length){ end = new Cell(
	 * passThroughX[k]/gm.ejectA,passThroughY[k]/gm.ejectB); }else{ end =
	 * this.end; } OPEN.clear(); CLOSE.clear(); start.F = 0; start.G = 0;
	 * start.H = 0; OPEN.insert(start);
	 * 
	 * ArrayList<Cell> neighbor = new ArrayList<Cell>(); Cell n; Cell n1; Cell
	 * n2;
	 * 
	 * while(!OPEN.isEmpty()){ Cell c = OPEN.extract(); if(c.equals(end)){
	 * end.parent = c.parent; break; } if(step >= maxStep){ break; }
	 * CLOSE.insert(c); neighbor.clear(); if(c.i>0 && c.j >=0 && c.i <
	 * gm.ejectRows && c.j < gm.ejectColumns){ if(gm.getEjection(c.i-1, c.j) ==
	 * false){ neighbor.add(new Cell(c.i-1,c.j)); } } if(c.i>=0 && c.j >0 && c.i
	 * < gm.ejectRows && c.j < gm.ejectColumns){ if(gm.getEjection(c.i, c.j-1)
	 * == false){ neighbor.add(new Cell(c.i,c.j-1)); } } if(c.i>=0 && c.j >=0 &&
	 * c.i < gm.ejectRows-1 && c.j < gm.ejectColumns){ if(gm.getEjection(c.i+1,
	 * c.j) == false){ neighbor.add(new Cell(c.i+1,c.j)); } } if(c.i>=0 && c.j
	 * >=0 && c.i < gm.ejectRows && c.j < gm.ejectColumns-1){
	 * if(gm.getEjection(c.i, c.j+1) == false){ neighbor.add(new
	 * Cell(c.i,c.j+1)); } } for(int i = 0 ; i < neighbor.size() ; i++){ n =
	 * neighbor.get(i); int cost = c.G + movecost(c,n,type); n1 = OPEN.get(n);
	 * if(n1 != null && cost < n1.G){ OPEN.remove(n1); n1 = null; } n2 =
	 * CLOSE.get(n); if(n2 != null && cost < n2.G){ CLOSE.remove(n2); n2 = null;
	 * }
	 * 
	 * if(n1 == null && n2 == null){ n.G = cost; n.F = n.G +
	 * h(n,type,start,end); n.parent = c; OPEN.insert(n); } } step++; }
	 * 
	 * if(end.parent != null){ if(lastEnd != null){ start.parent =
	 * lastEnd.parent; } }else{ break; } lastEnd = end; }
	 * 
	 * if(end.parent != null){ int m = 0; Cell c = end; while(c != null){ m++;
	 * if(c.parent != null) c.parent.next = c; c = c.parent; } Path path = new
	 * Path(); path.gi = gm; path.startCell = start; path.endCell = end;
	 * path.len = m; return path; }else{ return null; } }
	 * 
	 * public static int movecost(Cell c,Cell n,int type){ if(type == BFS)
	 * return 0; return D; }
	 * 
	 * public static int h(Cell c,int type,Cell start,Cell end){ int h = D
	 * (Math.abs(end.j - c.j) + Math.abs(end.i - c.i)); int cross = 0; if(type
	 * == VERTICAL_FIRST){ cross = Math.abs((start.i - end.i)(start.j - end.j) -
	 * (c.i - start.i)(c.j - end.j)); }else if(type == HORIZENTAL_FIRST){ cross
	 * = Math.abs((start.i - end.i)(start.j - end.j) - (c.i - end.i)(c.j -
	 * start.j)); }else if(type == DIAGONAL_FIRST){ cross = Math.abs((c.i -
	 * end.i)(start.j - end.j) - (start.i - end.i)(c.j - end.j)); }else if(type
	 * == BFS){ cross = 0; }else if(type ==Dijkstra){ cross = -h; } h += cross;
	 * return h; }
	 */
}

package com.xuanzhi.tools.objectdb.entity;

import javax.persistence.*;

@Embeddable
public class Knapsack {

	private int cellSize_1;
	private long cellIds_1[];
	private int cellCount_1[];
	
	private int cellSize_2;
	private long cellIds_2[];
	private int cellCount_2[];
	
	private int cellSize_3;
	private long cellIds_3[];
	private int cellCount_3[];
	
	
	private int cellSize_4;
	private long cellIds_4[];
	private int cellCount_4[];
	
	public Knapsack(){
		cellSize_1 = 20;
		cellSize_2 = 20;
		cellSize_3 = 20;
		cellSize_4 = 20;
		
		cellIds_1 = new long[cellSize_1];
		cellCount_1 = new int[cellSize_1];
		
		cellIds_2 = new long[cellSize_1];
		cellCount_2 = new int[cellSize_1];
		
		cellIds_3 = new long[cellSize_1];
		cellCount_3 = new int[cellSize_1];
		
		cellIds_4 = new long[cellSize_1];
		cellCount_4 = new int[cellSize_1];
	}

	public int getCellSize_1() {
		return cellSize_1;
	}

	public void setCellSize_1(int cellSize_1) {
		this.cellSize_1 = cellSize_1;
	}

	public long[] getCellIds_1() {
		return cellIds_1;
	}

	public void setCellIds_1(long[] cellIds_1) {
		this.cellIds_1 = cellIds_1;
	}

	public int[] getCellCount_1() {
		return cellCount_1;
	}

	public void setCellCount_1(int[] cellCount_1) {
		this.cellCount_1 = cellCount_1;
	}

	public int getCellSize_2() {
		return cellSize_2;
	}

	public void setCellSize_2(int cellSize_2) {
		this.cellSize_2 = cellSize_2;
	}

	public long[] getCellIds_2() {
		return cellIds_2;
	}

	public void setCellIds_2(long[] cellIds_2) {
		this.cellIds_2 = cellIds_2;
	}

	public int[] getCellCount_2() {
		return cellCount_2;
	}

	public void setCellCount_2(int[] cellCount_2) {
		this.cellCount_2 = cellCount_2;
	}

	public int getCellSize_3() {
		return cellSize_3;
	}

	public void setCellSize_3(int cellSize_3) {
		this.cellSize_3 = cellSize_3;
	}

	public long[] getCellIds_3() {
		return cellIds_3;
	}

	public void setCellIds_3(long[] cellIds_3) {
		this.cellIds_3 = cellIds_3;
	}

	public int[] getCellCount_3() {
		return cellCount_3;
	}

	public void setCellCount_3(int[] cellCount_3) {
		this.cellCount_3 = cellCount_3;
	}

	public int getCellSize_4() {
		return cellSize_4;
	}

	public void setCellSize_4(int cellSize_4) {
		this.cellSize_4 = cellSize_4;
	}

	public long[] getCellIds_4() {
		return cellIds_4;
	}

	public void setCellIds_4(long[] cellIds_4) {
		this.cellIds_4 = cellIds_4;
	}

	public int[] getCellCount_4() {
		return cellCount_4;
	}

	public void setCellCount_4(int[] cellCount_4) {
		this.cellCount_4 = cellCount_4;
	}
	
	
}

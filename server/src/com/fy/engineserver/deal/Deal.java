package com.fy.engineserver.deal;

import com.fy.engineserver.sprite.Player;

/**
 * 一个交易
 * 
 * 
 */
public class Deal {
	private static final int CAPACITY = 10;

	private String id;

	private Player playerA;

	private int indexesA[];

	private byte packageTypeA[];

	private long entityIdsA[];

	private int countsA[];

	private int coinsA;

	private boolean createFlagA;
	
	private boolean lockA;

	private boolean agreedA;

	private Player playerB;

	private int indexesB[];

	private long entityIdsB[];

	private byte packageTypeB[];

	private int countsB[];

	private int coinsB;

	private boolean createFlagB;
	
	private boolean lockB;

	private boolean agreedB;

	private long startTime;

	public Deal(Player playerA, Player playerB) {
		this.playerA = playerA;
		this.playerB = playerB;
		indexesA = new int[CAPACITY];
		for (int i = 0; i < indexesA.length; i++) {
			indexesA[i] = -1;
		}
		indexesB = new int[CAPACITY];
		for (int i = 0; i < indexesB.length; i++) {
			indexesB[i] = -1;
		}
		entityIdsA = new long[CAPACITY];
		entityIdsB = new long[CAPACITY];
		packageTypeA = new byte[CAPACITY];
		packageTypeB = new byte[CAPACITY];
		for (int i = 0; i < entityIdsA.length; i++) {
			entityIdsA[i] = -1;
		}
		for (int i = 0; i < entityIdsB.length; i++) {
			entityIdsB[i] = -1;
		}
		countsA = new int[CAPACITY];
		countsB = new int[CAPACITY];
		this.startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Player getPlayerA() {
		return playerA;
	}

	public void setPlayerA(Player playerA) {
		this.playerA = playerA;
	}

	/**
	 * 甲方交换中背包的下标，与entityId, count下标对应
	 * @return
	 */
	public int[] getIndexesA() {
		return indexesA;
	}

	public void setIndexesA(int[] indexesA) {
		this.indexesA = indexesA;
	}

	/**
	 * 甲方交换中背包的物品id
	 * @return
	 */
	public long[] getEntityIdsA() {
		return entityIdsA;
	}

	public void setEntityIdsA(long[] entityIdsA) {
		this.entityIdsA = entityIdsA;
	}

	/**
	 * 甲方交换中背包内物品的数量
	 * @return
	 */
	public int[] getCountsA() {
		return countsA;
	}

	public void setCountsA(int[] countsA) {
		this.countsA = countsA;
	}

	/**
	 * 甲方出的金
	 * @return
	 */
	public int getCoinsA() {
		return coinsA;
	}

	public void setCoinsA(int coinsA) {
		this.coinsA = coinsA;
	}

	/**
	 * 甲方是否已同意
	 * @return
	 */
	public boolean isAgreedA() {
		return agreedA;
	}

	public void setAgreedA(boolean agreedA) {
		this.agreedA = agreedA;
	}

	public Player getPlayerB() {
		return playerB;
	}

	public void setPlayerB(Player playerB) {
		this.playerB = playerB;
	}

	public int[] getIndexesB() {
		return indexesB;
	}

	public void setIndexesB(int[] indexesB) {
		this.indexesB = indexesB;
	}

	public long[] getEntityIdsB() {
		return entityIdsB;
	}

	public void setEntityIdsB(long[] entityIdsB) {
		this.entityIdsB = entityIdsB;
	}

	public int[] getCountsB() {
		return countsB;
	}

	public void setCountsB(int[] countsB) {
		this.countsB = countsB;
	}

	/**
	 * 乙方出的金
	 * @return
	 */
	public int getCoinsB() {
		return coinsB;
	}

	public void setCoinsB(int coinsB) {
		this.coinsB = coinsB;
	}

	/**
	 * 乙方是否已同意
	 * @return
	 */
	public boolean isAgreedB() {
		return agreedB;
	}

	public void setAgreedB(boolean agreedB) {
		this.agreedB = agreedB;
	}

	/**
	 * 交易开始时间
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public boolean isCreateFlagA() {
		return createFlagA;
	}

	public void setCreateFlagA(boolean createFlagA) {
		this.createFlagA = createFlagA;
	}

	public boolean isCreateFlagB() {
		return createFlagB;
	}

	public void setCreateFlagB(boolean createFlagB) {
		this.createFlagB = createFlagB;
	}

	public Player getDstPlayer(Player player) {
		if (player.getId() == playerA.getId()) {
			return playerB;
		} else {
			return playerA;
		}
	}

	public byte[] getPackageTypeA() {
		return packageTypeA;
	}

	public void setPackageTypeA(byte[] packageTypeA) {
		this.packageTypeA = packageTypeA;
	}

	public byte[] getPackageTypeB() {
		return packageTypeB;
	}

	public void setPackageTypeB(byte[] packageTypeB) {
		this.packageTypeB = packageTypeB;
	}

	public void resetCondition(Player player) {
		if (player.getId() == playerA.getId()) {
			indexesA = new int[CAPACITY];
			entityIdsA = new long[CAPACITY];
			countsA = new int[CAPACITY];
			coinsA = 0;
		} else {
			indexesB = new int[CAPACITY];
			entityIdsB = new long[CAPACITY];
			countsB = new int[CAPACITY];
			coinsB = 0;
		}
	}

	/**
	 * 增加到交易栏
	 * @param player
	 * @param index
	 * @param entityId
	 * @param count
	 * @return
	 */
	public boolean addArticle(Player player,byte packageType, int index, long entityId, int count) {
		// TODO Auto-generated method stub
		if (player.getId() == playerA.getId()) {
			for (int i = 0; i < indexesA.length; i++) {
				if (index == indexesA[i]) {
					return false;
				}
			}
			for (int i = 0; i < indexesA.length; i++) {
				if (entityIdsA[i] <= 0) {
					indexesA[i] = index;
					entityIdsA[i] = entityId;
					countsA[i] = count;
					packageTypeA[i] = packageType;
					return true;
				}
			}
		} else if (player.getId() == playerB.getId()) {
			for (int i = 0; i < indexesB.length; i++) {
				if (index == indexesB[i]) {
					return false;
				}
			}
			for (int i = 0; i < indexesB.length; i++) {
				if (entityIdsB[i] <= 0) {
					indexesB[i] = index;
					entityIdsB[i] = entityId;
					countsB[i] = count;
					packageTypeB[i] = packageType;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 从交易栏删除物品
	 * @param player
	 * @param index
	 */
	public boolean deleteArticle(Player player, int index) {
		// TODO Auto-generated method stub
		if (player.getId() == playerA.getId()) {
			indexesA[index] = -1;
			entityIdsA[index] = -1;
			countsA[index] = 0;
			packageTypeA[index] = -1;
			return true;
		} else if (player.getId() == playerB.getId()) {
			indexesB[index] = -1;
			entityIdsB[index] = -1;
			countsB[index] = 0;
			packageTypeB[index] = -1;
			return true;
		}
		return false;
	}

	public void setLockA(boolean lockA) {
		this.lockA = lockA;
		if (!this.lockA) {
			setAgreedA(false);
			setAgreedB(false);
		}
	}

	public boolean isLockA() {
		return lockA;
	}

	public void setLockB(boolean lockB) {
		this.lockB = lockB;
		if (!this.lockB) {
			setAgreedA(false);
			setAgreedB(false);
		}
	}

	public boolean isLockB() {
		return lockB;
	}

}

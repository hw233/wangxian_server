package com.fy.engineserver.activity.newChongZhiActivity;

public class MoneyBillBoardActivityData implements Comparable<MoneyBillBoardActivityData> {
		private long playerID;
		private long money;
		
		@Override
		public int compareTo(MoneyBillBoardActivityData o) {
			if (o.getMoney() > this.getMoney()) {
				return 1;
			}
			return -1;
		}

		public void setPlayerID(long playerID) {
			this.playerID = playerID;
		}

		public long getPlayerID() {
			return playerID;
		}

		public void setMoney(long money) {
			this.money = money;
		}

		public long getMoney() {
			return money;
		}
		
		@Override
		public String toString() {
			return playerID + "-" + money;
		}
}

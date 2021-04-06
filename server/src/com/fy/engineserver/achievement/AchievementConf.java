package com.fy.engineserver.achievement;

public interface AchievementConf {

	enum RecordType {
		累加(0),
		设置(1),
		记录最大值(2),
		只记录第一次(3), ;

		private int id;

		private RecordType(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}

}

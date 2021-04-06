package com.fy.engineserver.gametime;

import com.fy.engineserver.util.TimeTool.TimeDistance;

public interface CurrentTimeApi {

	long currentTimeMillis();

	String getAverageVisitTimesInfo(TimeDistance distance);

	void start();

	void cancel();
}

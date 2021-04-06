package com.fy.engineserver.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 随机工具箱
 * 
 * 
 */
public class RandomTool {

	public static int modifyRateParm = 100000;

	private static Random RANDOM = new Random();

	public static enum RandomType {
		eachRandom, groupRandom,
	}

	/**
	 * 修正后的，都是int的
	 * @param randomType
	 * @param modifiedRates
	 * @param maxResult
	 * @return
	 */
	public static List<Integer> getResultIndexs(RandomType randomType, int[] modifiedRates, int maxResult) {
		List<Integer> result = new ArrayList<Integer>();
		if (RandomType.eachRandom.equals(randomType)) {
			// 修正，当时-1时表示默认，最大值是所有
			maxResult = maxResult == -1 ? modifiedRates.length : maxResult;
			if (maxResult > 0) {
				for (int i = 0; i < modifiedRates.length; i++) {
					int randomValue = modifiedRates[i];
					if (randomValue > RANDOM.nextInt(modifyRateParm)) {
						// 随机到了
						result.add(i);
						if (--maxResult <= 0) {
							break;
						}
					}
				}
			}
		} else if (RandomType.groupRandom.equals(randomType)) {
			// 修正，当时-1时表示没默认，最大值是1
			maxResult = maxResult == -1 ? 1 : maxResult;

			if (maxResult > 0) {
				int[] realUseRates = new int[modifiedRates.length];
				realUseRates[0] = modifiedRates[0];
				for (int i = 1; i < modifiedRates.length; i++) {
					realUseRates[i] = modifiedRates[i] + realUseRates[i - 1];
				}
				while (maxResult > 0) {
					int res = RANDOM.nextInt(realUseRates[realUseRates.length - 1]);
					inner: for (int i = 0; i < realUseRates.length; i++) {
						if (realUseRates[i] > res) {// 有开始都是0的时候,
							result.add(i);
							realUseRates[i] = 0;
							maxResult--;
							for (int k = 0; k < realUseRates.length; k++) {
								if (realUseRates[k] == 0) {// 当前被选中,则权值和前面的相同
									if (k != 0) {// 被选中的是第一个 则记为0
										realUseRates[k] = realUseRates[k - 1];
									}
								}
								if (k > i) {// 被选中后面的值都减去被选中这个的权值（后面的剔除选中的权值）
									realUseRates[k] = realUseRates[k] - modifiedRates[i];
								}
							}
							break inner;
						}
					}
				}

			}
		}
		return result;
	}

	/**
	 * 得到随机结果的indexs
	 * @param randomType
	 *            [组随机/每个独立随机]
	 * @param rates
	 *            [几率]
	 * @param maxResult
	 *            [结果上限,当时单独随机的时候-1表示默认的，即最多能出数组长度个]
	 * @return
	 */
	public static List<Integer> getResultIndexs(RandomType randomType, double[] rates, int maxResult) {
		int[] modifiedRates = new int[rates.length];

		for (int i = 0; i < rates.length; i++) {
			modifiedRates[i] = (int) (modifyRateParm * rates[i]);
		}
		return getResultIndexs(randomType, modifiedRates, maxResult);
	}

	public static void main(String[] args) {
		System.out.println((RandomTool.getResultIndexs(RandomType.eachRandom, new int[] { 1, 2, 3, 4 }, 2)));
	}
}

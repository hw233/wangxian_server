package com.fy.engineserver.validate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 
 * 
 * @version 创建时间：Dec 26, 2012 7:03:34 PM
 * 
 */
public class ValidateAskManager {

	private static ValidateAskManager instance;

	public HashMap<Integer, List<ValidateAsk>> askMap = new HashMap<Integer, List<ValidateAsk>>();
	public List<ValidateAsk> askList = new ArrayList<ValidateAsk>();

	private Random ran = new Random();

	public static boolean week_model = false;

	public static ValidateAskManager getInstance() {
		if (instance == null) {
			synchronized (ValidateAskManager.class) {
				if (instance == null) {
					instance = new ValidateAskManager();
					instance.init();
				}
			}
		}
		return instance;
	}

	public void init() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				ValidateAsk ask = new ValidateAsk();
				ask.setAsk(i + "+" + j + "= ?");
				ask.setAnswer(String.valueOf(i + j));
				askList.add(ask);
			}
		}
		for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
			List<ValidateAsk> askList = new ArrayList<ValidateAsk>();
			// 产生题目库，题目非常简单，就是两个阿拉伯数字相加
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					ArrayList<ValidateAsk> al = genOneAsk(i, j, dayIndex);
					askList.addAll(al);
				}
			}

			askMap.put(dayIndex, askList);
		}

	}

	public ArrayList<ValidateAsk> genOneAsk(int i, int j, int dayIndex) {

		String a = String.valueOf(i);
		String b = " ＋ ";
		String c = String.valueOf(j);
		String d = " ＝ ";
		String e = "?";
		String array[] = new String[] { a, b, c, d, e };

		String styleArray[] = new String[] { "", "style='italic'", "style='bold'", "color='0xffffff'", };

		ArrayList<String> al = new ArrayList<String>();

		//
		if (dayIndex == 0) {
			StringBuffer sb2 = new StringBuffer();
			for (int z = 0; z < array.length; z++) {
				sb2.append(array[z]);
			}
			al.add(sb2.toString());
		}

		//
		if (dayIndex == 1) {
			StringBuffer sb2 = new StringBuffer();
			for (int z = 0; z < array.length; z++) {

				if (Math.random() < 0.3) {
					String aaa = "<f size='1'>" + ((int) (Math.random() * 10)) + "</f>";
					sb2.append(aaa);
				}

				sb2.append(array[z]);

			}
			al.add(sb2.toString());
		}

		//
		if (dayIndex == 2) {
			for (int x = 0; x <= 5; x++) {
				for (int y = x; y <= 5; y++) {

					for (int xx = 0; xx < styleArray.length; xx++) {

						StringBuffer sb = new StringBuffer();
						for (int z = 0; z < x; z++) {
							if (Math.random() < 0.3) {
								String aaa = "<f size='1'>" + ((int) (Math.random() * 10)) + "</f>";
								sb.append(aaa);
							}
							sb.append(array[z]);
						}

						sb.append("<f " + styleArray[xx] + ">");
						for (int z = x; z < y; z++) {
							sb.append(array[z]);
						}
						sb.append("</f>");

						for (int z = y; z < array.length; z++) {
							sb.append(array[z]);

						}

						al.add(sb.toString());
					}
				}
			}
		}

		if (dayIndex == 3) {
			for (int x = 0; x <= 5; x++) {
				for (int y = x; y <= 5; y++) {

					for (int xx = 0; xx < styleArray.length; xx++) {

						StringBuffer sb = new StringBuffer();
						for (int z = 0; z < x; z++) {

							sb.append(array[z]);
						}

						sb.append("<f " + styleArray[xx] + ">");
						for (int z = x; z < y; z++) {
							sb.append(array[z]);
						}
						sb.append("</f>");

						for (int z = y; z < array.length; z++) {
							sb.append(array[z]);
							if (Math.random() < 0.3) {
								String aaa = "<f size='1'>" + ((int) (Math.random() * 10)) + "</f>";
								sb.append(aaa);
							}

						}

						al.add(sb.toString());
					}
				}
			}
		}

		//
		if (dayIndex == 4) {
			for (int x = 0; x <= 5; x++) {
				for (int y = x; y <= 5; y++) {

					for (int xx = 0; xx < styleArray.length; xx++) {

						StringBuffer sb = new StringBuffer();
						for (int z = 0; z < x; z++) {

							sb.append(array[z]);
						}

						sb.append("<f " + styleArray[xx] + ">");
						for (int z = x; z < y; z++) {
							sb.append(array[z]);
						}
						sb.append("</f>");

						for (int z = y; z < array.length; z++) {

							sb.append(array[z]);

						}

						al.add(sb.toString());
					}
				}
			}
		}

		if (dayIndex == 5) {
			//
			for (int x = 0; x <= 5; x++) {
				for (int y = x; y <= 5; y++) {
					for (int x1 = y; x1 <= 5; x1++) {
						for (int y1 = x1; y1 <= 5; y1++) {

							for (int xx = 0; xx < styleArray.length; xx++) {

								StringBuffer sb = new StringBuffer();
								for (int z = 0; z < x; z++) {
									sb.append(array[z]);
								}
								sb.append("<f " + styleArray[xx] + ">");
								for (int z = x; z < y; z++) {
									sb.append(array[z]);
								}
								sb.append("</f>");

								for (int z = y; z < x1; z++) {

									sb.append(array[z]);

								}

								sb.append("<f " + styleArray[xx] + ">");
								for (int z = x1; z < y1; z++) {
									sb.append(array[z]);
								}
								sb.append("</f>");
								for (int z = y1; z < array.length; z++) {

									sb.append(array[z]);

								}

								al.add(sb.toString());
							}

						}
					}
				}
			}
		}

		//
		if (dayIndex > 5 || dayIndex < 0) {
			for (int x = 0; x <= 5; x++) {
				for (int y = x; y <= 5; y++) {
					for (int x1 = y; x1 <= 5; x1++) {
						for (int y1 = x1; y1 <= 5; y1++) {

							for (int xx = 0; xx < styleArray.length; xx++) {

								StringBuffer sb = new StringBuffer();
								for (int z = 0; z < x; z++) {
									sb.append(array[z]);
								}
								sb.append("<f " + styleArray[xx] + ">");
								for (int z = x; z < y; z++) {
									sb.append(array[z]);
								}
								sb.append("</f>");

								for (int z = y; z < x1; z++) {

									if (Math.random() < 0.3) {
										String aaa = "<f size='1'>" + ((int) (Math.random() * 10)) + "</f>";
										sb.append(aaa);
									}

									sb.append(array[z]);

								}

								sb.append("<f " + styleArray[xx] + ">");
								for (int z = x1; z < y1; z++) {
									sb.append(array[z]);
								}
								sb.append("</f>");
								for (int z = y1; z < array.length; z++) {

									String aaa = "<f size='1'>" + ((int) (Math.random() * 10)) + "</f>";
									sb.append(aaa);

									sb.append(array[z]);

								}

								al.add(sb.toString());
							}

						}
					}
				}
			}
		}

		ArrayList<ValidateAsk> list = new ArrayList<ValidateAsk>();
		for (String s : al) {
			int answer = i + j;
			ValidateAsk askObj = new ValidateAsk();
			askObj.setAsk("请回答以下算式的结果：" + s);
			askObj.setAnswer(String.valueOf(answer));

			list.add(askObj);

		}
		return list;

	}

	/**
	 * 随机获得一个验证问题
	 * @return
	 */
	public String[] strs = new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", };
	public int randomNum = 2;
	public int askType = 1;
	public String[] askMsgs = Translate.答题提示;

	public ValidateAsk getRandomAsk() {
		if (askType == 1) {
			String ask = "";
			Random ran = new Random();
			for (int i = 0; i < randomNum; i++) {
				int index = ran.nextInt(strs.length);
				ask = ask + strs[index];
			}
			ValidateAsk validateAsk = new ValidateAsk();
			validateAsk.setAsk(ask);
			validateAsk.setAnswer(ask);
			return validateAsk;
		} else if (askType == 0) {
			if (week_model) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(System.currentTimeMillis());
				int d = cal.get(Calendar.DAY_OF_YEAR);
				d = d % 7;

				List<ValidateAsk> al = askMap.get(d);
				if (al == null) askMap.get(0);

				return al.get(ran.nextInt(al.size()));
			} else {
				return askList.get(ran.nextInt(askList.size()));
			}
		}
		return null;
	}

	public List<ValidateAsk> getAsks() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		int d = cal.get(Calendar.DAY_OF_YEAR);
		d = d % 7;

		List<ValidateAsk> al = askMap.get(d);
		if (al == null) askMap.get(0);

		return al;
	}

	/**
	 * 给题库一道题目
	 * 题目非常简单，简单的两个阿拉伯数字相加
	 * @return
	 */
	public void addAsk(ValidateAsk ask) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		int d = cal.get(Calendar.DAY_OF_YEAR);
		d = d % 7;

		List<ValidateAsk> al = askMap.get(d);
		if (al == null) askMap.get(0);

		al.add(ask);
	}
}

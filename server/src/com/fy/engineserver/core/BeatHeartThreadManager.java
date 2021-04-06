package com.fy.engineserver.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.xuanzhi.tools.text.XmlUtil;

public class BeatHeartThreadManager {

	protected File configFile;

	protected GameManager gm;

	protected List<BeatHeartThread> bhtList = new ArrayList<BeatHeartThread>();

	public void setGameManager(GameManager gm) {
		this.gm = gm;
	}
	
	private  static BeatHeartThreadManager instance;

	
	public static BeatHeartThreadManager getInstance() {
		return instance;
	}


	public void setConfigFile(File f) {
		configFile = f;
	}

	/**
	 * 解析XML得到每个心跳线程处理的地图 启动所有的心跳线程
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		
		loadGameScece();

		for (int i = 0; i < bhtList.size(); i++) {
			BeatHeartThread thread = bhtList.get(i);
			thread.setName("BeatHeart-Thread-" + (i + 1));
			thread.start();
		}
		instance = this;
		ServiceStartRecord.startLog(this);
	}

	public static String NEED_MORE_THREAD_GAMES[] = new String[] { "kunlunshengdian", "mizonglin", "baicaojing", "kunhuagucheng", "wujibingyuanyiceng"
		, "wujibingyuanwuceng", "wujibingyuansiceng", "wujibingyuanliuceng", "wujibingyuanbaceng", "wujibingyuanerceng", "wujibingyuansanceng", "wujibingyuanqiceng"};

	/**
	 * <beatheart-manager> <thread beatheart=''> <game world='' game=''/> <game
	 * world='' game=''/> <game world='' game=''/> <game world='' game=''/>
	 * </thread> </beatheart-manager>
	 * 
	 * @throws Exception
	 */
	protected void loadGameScece() throws Exception {
		if (configFile == null) throw new Exception("configFile is null");
		configFile = new File(ConfigServiceManager.getInstance().getFilePath(configFile));
		Document dom = XmlUtil.load(configFile.getAbsolutePath() + File.separator + "threads.xml");
		Element root = dom.getDocumentElement();
		Element threadEles[] = XmlUtil.getChildrenByName(root, "thread");
		for (int i = 0; i < threadEles.length; i++) {
			Element e = threadEles[i];

			int bh = XmlUtil.getAttributeAsInteger(e, "beatheart", 10);
			BeatHeartThread beatHeartThread = new BeatHeartThread();
			beatHeartThread.setBeatheart(bh);

			Element gameEles[] = XmlUtil.getChildrenByName(e, "game");
			for (int j = 0; j < gameEles.length; j++) {
				Element ge = gameEles[j];
				String gameId = XmlUtil.getAttributeAsString(ge, "game", TransferLanguage.getMap());
				GameInfo gi = ResourceManager.getInstance().getGameInfo(gameId, "");

				if (gi != null && gm.非中立的国家地图 != null && gm.非中立的国家地图.contains(gi.displayName)) {

					boolean needMoreThread = false;

					for (int k = 0; k < NEED_MORE_THREAD_GAMES.length; k++) {
						if (NEED_MORE_THREAD_GAMES[k].equals(gi.name)) {
							needMoreThread = true;
							// System.out.println("[需要分开心跳的地图] ["+gi.displayName+"]");
						}
					}

					if (needMoreThread) {

						Game game = gm.getGameByName(gameId, 1);

						if (game != null) {
							beatHeartThread.addGame(game);
						} else {
							throw new Exception("game [" + gameId + "] [" + 1 + "] not exist");
						}

						BeatHeartThread beatHeartThread2 = new BeatHeartThread();
						beatHeartThread2.setBeatheart(bh);
						game = gm.getGameByName(gameId, 2);

						if (game != null) {
							beatHeartThread2.addGame(game);
						} else {
							throw new Exception("game [" + gameId + "] [" + 2 + "] not exist");
						}
						bhtList.add(beatHeartThread2);

						BeatHeartThread beatHeartThread3 = new BeatHeartThread();
						beatHeartThread3.setBeatheart(bh);
						game = gm.getGameByName(gameId, 3);

						if (game != null) {
							beatHeartThread3.addGame(game);
						} else {
							throw new Exception("game [" + gameId + "] [" + 3 + "] not exist");
						}
						bhtList.add(beatHeartThread3);
					} else {
						for (int k = 1; k <= 3; k++) {
							Game game = gm.getGameByName(gameId, k);

							if (game != null) {
								beatHeartThread.addGame(game);
								// Thread sub = new Thread(game.getSubThread(), "GameSubThread-" + game.getGameInfo().getName() + k);
								// sub.start();
							} else {
								throw new Exception("game [" + gameId + "] [" + k + "] not exist");
							}
						}
					}
				} else {
					Game game = gm.getGameByName(gameId, CountryManager.中立);

					if (game != null) {
						beatHeartThread.addGame(game);
						// Thread sub = new Thread(game.getSubThread(), "GameSubThread-" + game.getGameInfo().getName());
						// sub.start();
					} else {
						throw new Exception("game [" + gameId + "] [0] not exist");
					}
				}

			}
			bhtList.add(beatHeartThread);
		}
	}
}

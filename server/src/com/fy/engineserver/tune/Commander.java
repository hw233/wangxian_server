package com.fy.engineserver.tune;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fy.engineserver.chat.ChatChannelType;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.g2d.Graphics2DUtil;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.g2d.SignPost;
import com.fy.engineserver.core.res.GameMap;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.entity.client.ArticleEntity;
import com.fy.engineserver.datasource.article.entity.client.BagInfo4Client;
import com.fy.engineserver.datasource.article.entity.client.EquipmentEntity;
import com.fy.engineserver.datasource.article.entity.client.PropsEntity;
import com.fy.engineserver.message.ARTICLE_OPRATION_REQ;
import com.fy.engineserver.message.CHAT_MESSAGE_REQ;
import com.fy.engineserver.message.CREATE_PLAYER_REQ;
import com.fy.engineserver.message.CREATE_PLAYER_RES;
import com.fy.engineserver.message.ENTER_GAME_REQ;
import com.fy.engineserver.message.ENTER_GAME_RES;
import com.fy.engineserver.message.FUCK_TRANSPORT_GAME;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NEW_USER_LOGIN_REQ;
import com.fy.engineserver.message.PLAYER_ENTER_REQ;
import com.fy.engineserver.message.PLAYER_ENTER_RES;
import com.fy.engineserver.message.PLAYER_MOVE_REQ;
import com.fy.engineserver.message.PLAYER_REVIVED_REQ;
import com.fy.engineserver.message.QUERY_ARTICLE_REQ;
import com.fy.engineserver.message.QUERY_ARTICLE_RES;
import com.fy.engineserver.message.QUERY_KNAPSACK_REQ;
import com.fy.engineserver.message.QUERY_KNAPSACK_RES;
import com.fy.engineserver.message.QUERY_PLAYER_REQ;
import com.fy.engineserver.message.QUERY_PLAYER_RES;
import com.fy.engineserver.message.TARGET_SKILL_REQ;
import com.fy.engineserver.message.USER_ENTER_SERVER_REQ;
import com.fy.engineserver.message.USER_ENTER_SERVER_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.gamegateway.message.NEW_USER_LOGIN_RES;
import com.fy.gamegateway.message.PASSPORT_REGISTER_REQ;
import com.fy.gamegateway.message.PASSPORT_REGISTER_RES;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.transport.Connection;

public class Commander {
	public static final byte UP = 0;
	public static final byte DOWN = 1;
	public static final byte LEFT = 2;
	public static final byte RIGHT = 3;
	public static final byte LEFT_UP = 4;
	public static final byte RIGHT_UP = 5;
	public static final byte LEFT_DOWN = 6;
	public static final byte RIGHT_DOWN = 7;

	private RobotPlayer robot;

	private long seqNum0;
	private long seqNum1;
	

	private long nextSequnceNum0() {
		return ++seqNum0;
	}

	private long nextSequnceNum1() {
		return ++seqNum1;
	}

	public Commander(RobotPlayer robot) {
		this.robot = robot;
	}

	public void 注册新用户(String username, String password) throws Exception {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PASSPORT_REGISTER_REQ req = new PASSPORT_REGISTER_REQ(nextSequnceNum0(), username, password, "", false, StringUtil.randomIntegerString(12), "", "", "", "",
				-1);
		GatewayClientConnector connector = robot.getManager().getGatewayClientConnector();
		Connection conn = robot.getGatewayConnection();
		PASSPORT_REGISTER_RES res = (PASSPORT_REGISTER_RES) connector.requestMessage(conn, req);
		long result = res.getId();
		if (result != 0) {
			throw new Exception("注册失败,result:"+result);
		}
		System.out.println("【成功注册】 [" + conn.getName() + "] [" + robot.getThreadname() + "] [" + robot.getId() + "] [" + username + "] [" + password + "] ["
				+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "ms]");
	}

	public String getMD5(String clientId,String userName,String pwd,String userType,String platform,String channel,String channelUserFlag,String phoneType,String gpuType,String mac,String network,String version,String res_version){
		  StringBuffer md5 = new StringBuffer();
		  md5.append(clientId).append(userName).append(pwd).append(userType).append(platform).append(channel).append(channelUserFlag).append(phoneType).
		  append(gpuType).append(mac).append(network).append(version).append(res_version);
	       md5.append("U").append("S").append("ER").append("L");
	        md5.append("sq");
	        md5.append("20130419");
	        String m = StringUtil.hash(md5.toString());
	       return m;
//		  md5.append(mieshi_clientId).append(LoginModuleMessageHandlerImpl::curLoginUsername)
//	        .append(LoginModuleMessageHandlerImpl::curLoginPasswd).append(mieshi_usertype)
//	        .append(mieshi_Platform).append(mieshi_channel).append(mieshi_channelUserFlag).append(mieshi_reall_platform)
//	        .append(mieshi_FULL_GPU).append(g_macaddress).append(g_networkApn).append(mieshi_version).append(mieshi_res_version);
//	        md5.append("U").append("S").append("ER").append("L");
//	        md5.append("sq");
//	        md5.append("20130419");
//	        md5 = CCFileUtils::md5Encrypt(md5.c_str(), 0, 10);
//	        return md5;
	}
	
	public String 网关登录(String username, String password) throws Exception {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//		USER_LOGIN_REQ req = new USER_LOGIN_REQ(nextSequnceNum0(), StringUtil.randomIntegerString(12), username, password, "","","unknown","false");
		String cid = StringUtil.randomIntegerString(12);
		String md5String = getMD5(cid, username, password, "", "IOS", "YOUAI_XUNXIAN", "", "iPhone Simulator", "IOS", "", "WIFI", "3.0.8", "34");
		NEW_USER_LOGIN_REQ req2 = new NEW_USER_LOGIN_REQ(nextSequnceNum0(), cid, username, password, "", "IOS", "YOUAI_XUNXIAN", "", "iPhone Simulator", "IOS", "", "WIFI", "33", "", md5String);
		GatewayClientConnector connector = robot.getManager().getGatewayClientConnector();
		Connection conn = robot.getGatewayConnection();
		NEW_USER_LOGIN_RES res = (NEW_USER_LOGIN_RES) connector.requestMessage(conn, req2);
		byte result = res.getResult();
		if (result != 0) {
			throw new Exception("登录失败");
		}
		System.out.println("【成功登录】 [" + conn.getName() + "] [" + robot.getThreadname() + "] [" + robot.getId() + "] [" + username + "] [" + password
				+ "] [authCode:" + res.getAuthCode() + "] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "ms]");
		return res.getAuthCode();
	}

	public byte 进入服务器(String username, String password, String authCode) throws Exception {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		USER_ENTER_SERVER_REQ req = new USER_ENTER_SERVER_REQ(nextSequnceNum1(), username, password, authCode);
		ServerClientConnector connector = robot.getManager().getServerClientConnector();
		Connection conn = robot.getServerConnection();
		USER_ENTER_SERVER_RES res = (USER_ENTER_SERVER_RES)connector.requestMessage(conn, req);
		System.out.println("【进入服务器】 [" + conn.getName() + "] [" + robot.getThreadname() + "] [" + robot.getId() + "] [" + username
				+ "] [" + password + "] [result:"+res.getResult()+"] [online:"+res.getOnlineNum()+"] [offline:"+res.getOfflineNum()+"] [lefttime:"+res.getLeftTime()/1000+"] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "ms]");
		return res.getResult();
	}

	public List<Player> 获得角色列表(String username, String authCode) throws Exception {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		QUERY_PLAYER_REQ req = new QUERY_PLAYER_REQ(nextSequnceNum1(), username, authCode);
		ServerClientConnector connector = robot.getManager().getServerClientConnector();
		Connection conn = robot.getServerConnection();
		QUERY_PLAYER_RES res = (QUERY_PLAYER_RES) connector.requestMessage(conn, req);
		Player ps[] = res.getPlayers();
		List<Player> list = new ArrayList<Player>();
		for (int i = 0; i < ps.length; i++) {
			list.add(ps[i]);
		}
		System.out.println("【获得角色列表】 [" + conn.getName() + "] [" + robot.getThreadname() + "] [" + robot.getId() + "] [" + username + "] [" + list.size()
				+ "] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "ms]");
		return list;
	}

	public Player 创建角色(Player player) throws Exception {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		CREATE_PLAYER_REQ req = new CREATE_PLAYER_REQ(nextSequnceNum1(), player.getName(), (byte) 0, (byte)0, (byte) 1,
				(byte) 1, false, 0);
		ServerClientConnector connector = robot.getManager().getServerClientConnector();
		Connection conn = robot.getServerConnection();
		CREATE_PLAYER_RES res = (CREATE_PLAYER_RES) connector.requestMessage(conn, req);
		int result = res.getResult();
		if (result != 0) {
			throw new Exception("创建角色失败");
		}
		System.out.println("【创建角色】 [" + conn.getName() + "] [" + robot.getThreadname() + "] [" + robot.getId() + "] [" + player.getName() + "] ["
				+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "ms]");
		return null;
	}

	public void 进入游戏(long playerId, String authCode) throws Exception {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PLAYER_ENTER_REQ req = new PLAYER_ENTER_REQ(nextSequnceNum1(), playerId, authCode);
		ServerClientConnector connector = robot.getManager().getServerClientConnector();
		Connection conn = robot.getServerConnection();
		PLAYER_ENTER_RES res = (PLAYER_ENTER_RES) connector.requestMessage(conn, req);
		int result = res.getResult();
		if (result != 0) {
			throw new Exception("进入游戏失败1");
		}
		ENTER_GAME_REQ req1 = new ENTER_GAME_REQ(nextSequnceNum1(), playerId, robot.getPlayer().getMapName(), "");
		ENTER_GAME_RES res1 = (ENTER_GAME_RES) connector.requestMessage(conn, req1);
		int x = res1.getX();
		int y = res1.getY();
		robot.getPlayer().setX(x);
		robot.getPlayer().setY(y);
		result = res1.getResult();
		if (result != 0) {
			throw new Exception("进入游戏失败2");
		}
		System.out.println("【进入游戏】 [username:"+robot.getUsername()+"] [" + conn.getName() + "] [" + robot.getThreadname() + "] [" + robot.getId() + "] [" + robot.getPlayer().getName()
				+ "] [pid:" + playerId + "] [pos:("+x+","+y+")] [mapName:"+robot.getPlayer().getMapName()+"] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "ms]");
	}
	
	public void 跳转地图(String mapName) throws Exception  {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if(robot.getPlayer().getMapName().equals(mapName)) {
			return;
		}
		GameMap map = GameMapManager.getInstance().getGameMap(mapName);
		if(map != null) {
			FUCK_TRANSPORT_GAME req = new FUCK_TRANSPORT_GAME(nextSequnceNum1(), mapName);
			ServerClientConnector connector = robot.getManager().getServerClientConnector();
			Connection conn = robot.getServerConnection();
			connector.sendMessage(conn, req);
			ENTER_GAME_REQ req1 = new ENTER_GAME_REQ(nextSequnceNum1(),robot.getPlayer().getId(), mapName, "");
			ENTER_GAME_RES res1 = (ENTER_GAME_RES) connector.requestMessage(conn, req1);
			int x = res1.getX();
			int y = res1.getY();
			robot.getPlayer().setX(x);
			robot.getPlayer().setY(y);
			int result = res1.getResult();
			if (result != 0) {
				throw new Exception("跳转地图失败");
			}
			System.out.println("【跳转地图】 [" + conn.getName() + "] [" + robot.getThreadname() + "] [" + robot.getId() + "] [" + robot.getPlayer().getName()
					+ "] [cur:"+robot.getPlayer().getMapName()+"] [target:"+mapName+"] [pos:("+x+","+y+")] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "ms]");
		}
	}

	public void 走动(long startTime) throws Exception {
		if (robot.getPlayer().isFighting()) {
			robot.setNextActionTime(startTime + 1000);
			return;
		}

		GameMapManager gmm = GameMapManager.getInstance();

		GameMap gameMap = gmm.getGameMap(robot.getPlayer().getMapName());

		int direction = new java.util.Random().nextInt(8);

		int len = 400;

		int x1 = robot.player.getX();
		int y1 = robot.player.getY();
		int x2 = 0;
		int y2 = 0;

		if (direction == UP) {
			x2 = x1;
			y2 = y1 - len;
		} else if (direction == DOWN) {
			x2 = x1;
			y2 = y1 + len;
		} else if (direction == RIGHT) {
			x2 = x1 + len;
			y2 = y1;
		} else if (direction == LEFT) {
			x2 = x1 - len;
			y2 = y1;
		} else if (direction == LEFT_UP) {
			x2 = x1 - (int) (len / 2 * Math.sqrt(2));
			y2 = y1 - (int) (len / 2 * Math.sqrt(2));
		} else if (direction == RIGHT_UP) {
			x2 = x1 + (int) (len / 2 * Math.sqrt(2));
			y2 = y1 - (int) (len / 2 * Math.sqrt(2));
		} else if (direction == LEFT_DOWN) {
			x2 = x1 - (int) (len / 2 * Math.sqrt(2));
			y2 = y1 + (int) (len / 2 * Math.sqrt(2));
		} else if (direction == RIGHT_DOWN) {
			x2 = x1 + (int) (len / 2 * Math.sqrt(2));
			y2 = y1 + (int) (len / 2 * Math.sqrt(2));
		}
		
		if(x2 < 0) {
			x2 = 16;
		}
		if(y2 < 0) {
			y2= 16;
		}
		
		if (x2 > gameMap.getWidth() - 16)
			x2 = gameMap.getWidth() - 16;
		if (y2 > gameMap.getHeight() - 16)
			y2 = gameMap.getHeight() - 16;

		SignPost sps[] = gameMap.navigator.findPath(new Point(x1, y1), new Point(x2, y2));

		if (sps == null) {
			robot.setNextActionTime(startTime + 200);
			return;
		}

		double endPoint[] = new double[] { x2, y2 };

		double x = endPoint[0];
		double y = endPoint[1];
		double[] roadLength = new double[sps.length + 1];
		double[] pointsX = new double[sps.length + 2];
		double[] pointsY = new double[sps.length + 2];
		short[] spId = new short[sps.length];
		int totalLength = 0;
		for (int i = 0; i < sps.length - 1; i++) {
			double L = Graphics2DUtil.distance(new Point(sps[i].x, sps[i].y), new Point(sps[i + 1].x, sps[i + 1].y));
			roadLength[i + 1] = L;
			totalLength += L;
		}
		for (int i = 0; i < sps.length; i++) {
			pointsX[i + 1] = sps[i].x;
			pointsY[i + 1] = sps[i].y;
			spId[i] = (short) sps[i].id;
		}
		if (sps.length == 0) {
			roadLength[0] = Graphics2DUtil.distance(new Point(x2, y2), new Point(x1, y1));
			pointsX[0] = x1;
			pointsX[1] = x;
			pointsY[0] = y1;
			pointsY[1] = y;
			totalLength += roadLength[0];
		} else {
			roadLength[0] = Graphics2DUtil.distance(new Point(sps[0].x, sps[0].y), new Point(x1, y1));
			pointsX[0] = x1;
			pointsY[0] = y1;
			roadLength[sps.length] = Graphics2DUtil.distance(new Point(sps[sps.length - 1].x, sps[sps.length - 1].y), new Point(x2, y2));
			pointsX[sps.length + 1] = x;
			pointsY[sps.length + 1] = y;
			totalLength += roadLength[0];
			totalLength += roadLength[sps.length];
		}
		robot.player.setX(x2);
		robot.player.setY(y2);
		
		long destTime = startTime + (totalLength * 1000) / robot.player.getSpeed();

		PLAYER_MOVE_REQ req = new PLAYER_MOVE_REQ(nextSequnceNum1(), startTime, robot.player.getSpeed(), destTime, (short) x1, (short) y1, (short) x2,
				(short) y2, spId);
		ServerClientConnector connector = robot.getManager().getServerClientConnector();
		Connection conn = robot.getServerConnection();
		connector.sendMessage(conn, req);
		robot.setNextActionTime(destTime + 3000);
		System.out.println("【移动】 [" + conn.getName() + "] [" + robot.getThreadname() + "] [" + robot.getId() + "] [" + robot.getPlayer().getName()
				+ "] [map:"+robot.getPlayer().getMapName()+"] [start:(" + x1 + "," + y1 + ")] [end:(" + x2 + "," + y2 + ")] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "ms]");
	}
	
	public boolean 追踪(long startTime, LivingObject living) throws Exception {

		GameMapManager gmm = GameMapManager.getInstance();

		GameMap gameMap = gmm.getGameMap(robot.getPlayer().getMapName());

		int x1 = robot.player.getX();
		int y1 = robot.player.getY();
		int x2 = living.getX();
		int y2 = living.getY();

		SignPost sps[] = gameMap.navigator.findPath(new Point(x1, y1), new Point(x2, y2));

		if (sps == null) {
			return false;
		}

		double endPoint[] = new double[] { x2, y2 };

		double x = endPoint[0];
		double y = endPoint[1];
		double[] roadLength = new double[sps.length + 1];
		double[] pointsX = new double[sps.length + 2];
		double[] pointsY = new double[sps.length + 2];
		short[] spId = new short[sps.length];
		int totalLength = 0;
		for (int i = 0; i < sps.length - 1; i++) {
			double L = Graphics2DUtil.distance(new Point(sps[i].x, sps[i].y), new Point(sps[i + 1].x, sps[i + 1].y));
			roadLength[i + 1] = L;
			totalLength += L;
		}
		for (int i = 0; i < sps.length; i++) {
			pointsX[i + 1] = sps[i].x;
			pointsY[i + 1] = sps[i].y;
			spId[i] = (short) sps[i].id;
		}
		if (sps.length == 0) {
			roadLength[0] = Graphics2DUtil.distance(new Point(x2, y2), new Point(x1, y1));
			pointsX[0] = x1;
			pointsX[1] = x;
			pointsY[0] = y1;
			pointsY[1] = y;
			totalLength += roadLength[0];
		} else {
			roadLength[0] = Graphics2DUtil.distance(new Point(sps[0].x, sps[0].y), new Point(x1, y1));
			pointsX[0] = x1;
			pointsY[0] = y1;
			roadLength[sps.length] = Graphics2DUtil.distance(new Point(sps[sps.length - 1].x, sps[sps.length - 1].y), new Point(x2, y2));
			pointsX[sps.length + 1] = x;
			pointsY[sps.length + 1] = y;
			totalLength += roadLength[0];
			totalLength += roadLength[sps.length];
		}
		robot.player.setX(x2);
		robot.player.setY(y2);
		
		long destTime = startTime + (totalLength * 1000) / robot.player.getSpeed();

		PLAYER_MOVE_REQ req = new PLAYER_MOVE_REQ(nextSequnceNum1(), startTime, robot.player.getSpeed(), destTime, (short) x1, (short) y1, (short) x2,
				(short) y2, spId);
		ServerClientConnector connector = robot.getManager().getServerClientConnector();
		Connection conn = robot.getServerConnection();
		connector.sendMessage(conn, req);
		robot.setNextActionTime(destTime+500);
		System.out.println("【追踪】 [" + conn.getName() + "] [" + robot.getThreadname() + "] [" + robot.getId() + "] [" + robot.getPlayer().getName()
				+ "] [map:"+robot.getPlayer().getMapName()+"] [start:(" + x1 + "," + y1 + ")] [end:(" + x2 + "," + y2 + ")] [living:("+(living.getX()+","+living.getY())+")] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "ms]");
		
		return true;
	}
	
	public void 同步背包数据() throws Exception {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		QUERY_KNAPSACK_REQ req = new QUERY_KNAPSACK_REQ(nextSequnceNum1(), (byte)0);
		ServerClientConnector connector = robot.getManager().getServerClientConnector();
		Connection conn = robot.getServerConnection();
		QUERY_KNAPSACK_RES res = (QUERY_KNAPSACK_RES) connector.requestMessage(conn, req);
		HashMap<Long, Byte> ids = new HashMap<Long, Byte>();
		BagInfo4Client bags[] = res.getBagInfo4Client();
		Knapsack knap[] = new Knapsack[bags.length];
		for(int i=0; i<bags.length; i++) {
			knap[i] = new Knapsack(robot.player);
			knap[i].init(robot.player, bags[i].getEntityId().length);
			Cell cells[] = new Cell[bags[i].getCounts().length];
			for(int j=0; j<cells.length; j++) {
				cells[j] = new Cell();
				cells[j].setEntityId(bags[i].getEntityId()[j]);
				cells[j].setCount(bags[i].getCounts()[j]);
				if(bags[i].getEntityId()[j] != -1) {
					ids.put(bags[i].getEntityId()[j], Byte.MAX_VALUE);
				}
				//System.out.println("trace_knapsack ["+i+"] ["+j+"] ["+bags[i].getEntityId()[j]+"] ["+bags[i].getCounts()[j]+"]");
			}
			knap[i].setCells(cells);
		}
		robot.getPlayer().setKnapsacks_common(knap);
		long aids[] = new long[ids.size()];
		Long idss[] = ids.keySet().toArray(new Long[0]);
		for(int i=0; i<aids.length; i++) {
			aids[i] = idss[i].longValue();
		}
		//以下获取所有物品信息
		QUERY_ARTICLE_REQ req1 = new QUERY_ARTICLE_REQ(nextSequnceNum1(), aids);
		QUERY_ARTICLE_RES res1 = (QUERY_ARTICLE_RES)connector.requestMessage(conn, req1);
		ArticleEntity aes[] = res1.getArticleEntities();
		PropsEntity props[] = res1.getPropsEntities();
		EquipmentEntity eqs[] = res1.getEquipmentEntities();
		for(ArticleEntity ae : aes) {
			robot.articleEntityMap.put(ae.getId(), ae);
		}
		for(PropsEntity pe : props) {
			robot.propsEntityMap.put(pe.getId(), pe);
		}
		for(EquipmentEntity ee : eqs) {
			robot.equipmentEntityMap.put(ee.getId(), ee);
		}
		System.out.println("【同步背包】 [" + conn.getName() + "] [" + robot.getThreadname() + "] [" + robot.getId() + "] [" + robot.getPlayer().getName()
				+ "] [map:"+robot.getPlayer().getMapName()+"] [trace:"+bags.length+"/"+bags[0].getEntityId().length+"] [物品数量:"+ids.size()+"] [物品:"+robot.articleEntityMap.size()+"] [道具:"+robot.propsEntityMap.size()+"] [装备:"+robot.equipmentEntityMap.size()+"] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "ms]");
	}
	
	public void 使用物品(int bagIndex, int cellIndex) throws Exception {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ARTICLE_OPRATION_REQ req = new ARTICLE_OPRATION_REQ(nextSequnceNum1(), (byte)0, (short)bagIndex, (short)cellIndex, (byte)0, (int)0);
		ServerClientConnector connector = robot.getManager().getServerClientConnector();
		Connection conn = robot.getServerConnection();
		connector.sendMessage(conn, req);
		String aname = "";
		long id = -1;
		Knapsack knaps[]=  robot.player.getKnapsacks_common();
		if(knaps.length > bagIndex) {
			if(knaps[bagIndex].getCells().length > cellIndex) {
				Cell cell = knaps[bagIndex].getCell(cellIndex);
				id = cell.getEntityId();
				ArticleEntity ae = robot.articleEntityMap.get(id);
				if(ae == null) {
					PropsEntity pe = robot.propsEntityMap.get(id);
					if(pe == null) {
						EquipmentEntity ee = robot.equipmentEntityMap.get(id);
						if(ee != null) {
							aname = ee.getShowName();
						}
					} else {
						aname = pe.getShowName();
					}
				}  else {
					aname = ae.getShowName();
				}
			}
		}
		System.out.println("【使用物品】 [" + conn.getName() + "] [" + robot.getThreadname() + "] [" + robot.getId() + "] [" + robot.getPlayer().getName()
				+ "] [map:"+robot.getPlayer().getMapName()+"] [物品:"+id+"/"+aname+"] [pos:"+bagIndex+"/"+cellIndex+"] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "ms]");
	}
	
	public void 攻击(LivingObject enemy, int career) throws Exception {
		int skillId = 100+career;
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		TARGET_SKILL_REQ req = new TARGET_SKILL_REQ(nextSequnceNum1(), (short)enemy.getX(), (short)enemy.getY(), new byte[]{(byte)1}, new long[]{enemy.getId()}, (short)skillId,(byte)0);
		ServerClientConnector connector = robot.getManager().getServerClientConnector();
		Connection conn = robot.getServerConnection();
		connector.sendMessage(conn, req);
		System.out.println("【攻击敌人】 [" + conn.getName() + "] [" + robot.getThreadname() + "] [" + robot.getId() + "] [" + robot.getPlayer().getName()
				+ "] [map:"+robot.getPlayer().getMapName()+"] ["+enemy.getId()+"] ["+enemy.getClass().getName()+"] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "ms]");
	}
	
	public void 复活点复活() throws Exception {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PLAYER_REVIVED_REQ req = new PLAYER_REVIVED_REQ(nextSequnceNum1(), (byte)0);
		ServerClientConnector connector = robot.getManager().getServerClientConnector();
		Connection conn = robot.getServerConnection();
		connector.sendMessage(conn, req);
		System.out.println("【复活点复活】 [" + conn.getName() + "] [" + robot.getThreadname() + "] [" + robot.getId() + "] [" + robot.getPlayer().getName()
				+ "] [map:"+robot.getPlayer().getMapName()+"] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "ms]");
	}
	
	public void 聊天() throws Exception{
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		String inputContent = "hello,我是:"+robot.getPlayer().getName();
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setMessageText(inputContent);
		ChatMessage message = chatMessage.clone();
		message.setSort(ChatChannelType.SYSTEM);
		message.setChatType(ChatMessageService.CHAT_TYPE_系统提示消息);
		CHAT_MESSAGE_REQ req = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), message, message.getAccessoryItem(), message.getAccessoryTask());
		ServerClientConnector connector = robot.getManager().getServerClientConnector();
		Connection conn = robot.getServerConnection();
		connector.sendMessage(conn, req);
		System.out.println("【聊天】 [username:"+robot.getUsername()+"] [playername:"+robot.getPlayer().getName()+"] [id:"+robot.getId()+"] ["+inputContent+"] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "ms]");
	}
}

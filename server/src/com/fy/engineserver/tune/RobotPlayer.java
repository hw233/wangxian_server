package com.fy.engineserver.tune;

import java.util.HashMap;
import java.util.List;

import com.fy.engineserver.core.FieldChangeEvent;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.g2d.Graphics2DUtil;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.entity.client.ArticleEntity;
import com.fy.engineserver.datasource.article.entity.client.EquipmentEntity;
import com.fy.engineserver.datasource.article.entity.client.PropsEntity;
import com.fy.engineserver.message.AROUND_CHANGE_REQ;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.message.NOTIFY_SELFCHANGE_REQ;
import com.fy.engineserver.message.SET_POSITION_REQ;
import com.fy.engineserver.message.USER_ENTER_SERVER_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class RobotPlayer {

	public static final int STATE_未开始 = 0;
	public static final int STATE_注册 = 1;
	public static final int STATE_登录 = 2;

	public static final int STATE_进入服务器 = 3;
	public static final int STATE_角色列表 = 4;
	public static final int STATE_创建角色 = 5;
	public static final int STATE_进入游戏 = 6;
	public static final int STATE_走动 = 7;
	public static final int STATE_使用技能 = 8;
	public static final int STATE_战斗 = 9;

	public static final String STATE_DESP[] = new String[] { "未开始", "注册", "登录","进入服务器", "角色列表", "创建角色", "进入游戏","走动","使用技能","战斗" };

	public HashMap<Long, ArticleEntity> articleEntityMap = new HashMap<Long, ArticleEntity>();
	public HashMap<Long, PropsEntity> propsEntityMap = new HashMap<Long, PropsEntity>();
	public HashMap<Long, EquipmentEntity> equipmentEntityMap = new HashMap<Long, EquipmentEntity>();

	public HashMap<Long, LivingObject> livings = new HashMap<Long, LivingObject>();

	private Connection gatewayConnection;

	private Connection serverConnection;

	private long id;

	private String username;

	private String password;

	private String mapName;

	public Player player;

	private RobotPlayerManager manager;

	private Commander comm;

	private boolean alive;

	private int state;

	private String threadname;

	private String errorMessage;

	private long nextActionTime;

	private LivingObject targetEnemy;

	private long endTime = Long.MAX_VALUE;

	private long lastFightTime;

	private long enterTime = System.currentTimeMillis();

	private long fightTime = 3 * 60 * 1000;

	public RobotPlayer(long id, String username, String password, String mapName, RobotPlayerManager manager) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.mapName = mapName;
		this.manager = manager;
		this.comm = new Commander(this);
	}

	public RobotPlayerManager getManager() {
		return manager;
	}

	public void setManager(RobotPlayerManager manager) {
		this.manager = manager;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Connection getGatewayConnection() {
		if (gatewayConnection == null) {
			try {
				gatewayConnection = manager.getGatewayClientConnection(this);
				gatewayConnection.setAttachmentData("robot", this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return gatewayConnection;
	}

	public void setGatewayConnection(Connection gatewayConnection) {
		this.gatewayConnection = gatewayConnection;
	}

	public Connection getServerConnection() {
		if (serverConnection == null) {
			try {
				serverConnection = manager.getServerClientConnection(this);
				serverConnection.setAttachmentData("robot", this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return serverConnection;
	}

	public void setServerConnection(Connection serverConnection) {
		this.serverConnection = serverConnection;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Commander getComm() {
		return comm;
	}

	public void setComm(Commander comm) {
		this.comm = comm;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getThreadname() {
		return threadname;
	}

	public void setThreadname(String threadname) {
		this.threadname = threadname;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public long getNextActionTime() {
		return nextActionTime;
	}

	public void setNextActionTime(long nextActionTime) {
		this.nextActionTime = nextActionTime;
	}

	private boolean revived = false;

	private String authCode;

	private boolean end;

	public boolean isEnd() {
		return end;
	}
	
	public String leaveReason;

	public void setEnd(boolean end) {
		this.end = end;
	}

	private boolean canEnterFlag;

	private boolean lineupFlag;
	
	private long lastBeatTime;
	private int lastStat;
	
	public Player initPlayer(){

		if(username == null){
			this.username = "ROBOT-" + StringUtil.randomIntegerString(12);
			this.password = "111111";
			try {
				comm.注册新用户(username, password);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("[注册新用户失败] [" + username + "] \n" + StringUtil.getStackTrace(e));
				return null;
			}
		}
		
		try {
			authCode = comm.网关登录(username, password);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[网关登录失败] [" + username + "] \n" + StringUtil.getStackTrace(e));
			return null;
		}
		
		try {
			byte result = comm.进入服务器(username, password, authCode);
			//0-可以直接进入，1-需要排队并且已经排队, 2-进入失败
			if(result == 1){
				throw new Exception("进入服务器失败,服务器排队");
			}else if(result == 2){
				throw new Exception("进入服务器失败,进入失败");
			}
		} catch (Exception e) {
			System.out.println("[进入服务器失败] [" + username + "] [" + password + "] \n" + StringUtil.getStackTrace(e));
			return null;
		}
		
		try {
			List<Player> plist = comm.获得角色列表(username, authCode);
			if (plist != null && plist.size() > 0) {
				this.player = plist.get(0);
			}
		} catch (Exception e) {
			System.out.println("[获得角色列表失败] [" + username + "] \n" + StringUtil.getStackTrace(e));
			return null;
		}
		
		if (this.player == null) {
			// 创建角色
			try {
				Player p = new Player();
				p.setName("机器人-" + StringUtil.randomIntegerString(12));
				p.setId(0);
				p.setUsername(username);
				comm.创建角色(p);
			} catch (Exception e) {
				System.out.println("[创建角色失败] [" + username + "] \n" + StringUtil.getStackTrace(e));
				return null;
			}
			try {
				List<Player> plist = comm.获得角色列表(username, authCode);
				if (plist != null && plist.size() > 0) {
					this.player = plist.get(0);
				}
			} catch (Exception e) {
				System.out.println("[获得角色列表失败] [" + username + "] \n" + StringUtil.getStackTrace(e));
				return null;
			}
		}
		
		return player;
		
	}
	
	public void heartbeat2(long start){
		if(this.state == STATE_未开始){
			try {
				comm.进入游戏(player.getId(), authCode);
				this.state = STATE_走动;
			} catch (Exception e) {
				System.out.println("[进入游戏失败] [" + username + "] [" + player.getName() + "] \n" + StringUtil.getStackTrace(e));
				end = true;
				return;
			}
		}
		
		if (this.state == STATE_走动) {
			if (start - nextActionTime >= 3000) {
				try {
					nextActionTime = start;
					comm.走动(start);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("[走动失败] [" + username + "] [" + player.getName() + "] \n" + StringUtil.getStackTrace(e));
					end = true;
					return;
				}
			}
		}
		
		if((System.currentTimeMillis() - lastBeatTime >= 10000L)){
			lastBeatTime = System.currentTimeMillis();
			if(player != null){
				try {
					comm.聊天();
				} catch (Exception e) {
					System.out.println("[聊天失败] [" + username + "] [" + player.getName() + "] \n" + StringUtil.getStackTrace(e));
					end = true;
					return;
				}
				System.out.println("["+TimeTool.formatter.varChar23.format(System.currentTimeMillis())+"] [玩家心跳] [10秒1跳] [账号:"+username+"] [角色名:"+player.getName()+"] [状态:"+STATE_DESP[lastStat]+"/"+STATE_DESP[state]+"] [地图:"+(player.getCurrentGame()!=null?player.getCurrentGame().gi.name:"null")+"] [x:"+player.getX()+"] [y:"+player.getY()+"]");
			}
		}
	}

	public void heartbeat(long start) {
		try {
//			if(player != null && player.getLevel() <= 1){
//				comm.同步背包数据();
//				使用道具("经验丹", 200);
//				使用道具("境界丹", 4);
//			}
			
//			if (this.state >= STATE_走动) {
//				if (player.getHp() <= 0 && revived == false) {
//					comm.复活点复活();
//					revived = true;
//					System.out.println("[角色死亡] [进行复活] [" + player.getName() + "]");
//				} else if (player.getHp() < player.getMaxHP() * 0.3) {
//					使用道具("一元丹", 1);
//				} else if (player.getHp() > 0 && revived) {
//					// 已经完成复活
//					revived = false;
//					if (mapName != null && !mapName.equals(player.getMapName())) {
//						// 跳地图；
//						comm.跳转地图(mapName);
//						player.setMapName(mapName);
//					}
//					this.targetEnemy = null;
//					this.state = STATE_走动;
//					System.out.println("[角色死亡] [完成复活] [" + player.getName() + "]");
//				} else if (revived) {
//					System.out.println("[角色死亡，正在复活] [" + player.getName() + "]");
//				}
//			}
			if (this.state == STATE_未开始) {
				if (username == null) {
					// 开始通过网关注册
					try {
						this.username = "ROBOT-" + StringUtil.randomIntegerString(12);
						this.password = StringUtil.randomString(8);
						this.state = STATE_注册;
						comm.注册新用户(username, password);
						manager.addRegisterUser(username, password);
					} catch (Exception e) {
						System.out.println("[注册新用户失败] [" + username + "] [" + password + "] \n" + StringUtil.getStackTrace(e));
						errorMessage = StringUtil.getStackTrace(e);
						end = true;
					}
					// 登录
					try {
						this.state = STATE_登录;
						authCode = comm.网关登录(username, password);
					} catch (Exception e) {
						System.out.println("[网关登录失败] [" + username + "] [" + password + "] \n" + StringUtil.getStackTrace(e));
						errorMessage = StringUtil.getStackTrace(e);
						end = true;
					}
					if (gatewayConnection != null && gatewayConnection.getState() != Connection.CONN_STATE_CLOSE) {
						gatewayConnection.close();
					}
				} else {
					// 登录
					try {
						this.state = STATE_登录;
						authCode = comm.网关登录(username, password);
					} catch (Exception e) {
						System.out.println("[网关登录失败] [" + username + "] [" + password + "] \n" + StringUtil.getStackTrace(e));
						errorMessage = StringUtil.getStackTrace(e);
						end = true;
					}

					if (gatewayConnection != null && gatewayConnection.getState() != Connection.CONN_STATE_CLOSE) {
						gatewayConnection.close();
					}
				}
			} 
			else if (this.state == STATE_登录) {
				// 进入游戏服务器
				if (!lineupFlag) {
					try {
						this.state = STATE_进入服务器;
						byte result = comm.进入服务器(username, password, authCode);
						lineupFlag = result == 1;
						canEnterFlag = result == 0;
					} catch (Exception e) {
						System.out.println("[进入服务器失败] [" + username + "] [" + password + "] \n" + StringUtil.getStackTrace(e));
						errorMessage = StringUtil.getStackTrace(e);
						end = true;
					}
				}
				if (canEnterFlag) {
					this.endTime = start + 20 * 60 * 1000L;//new java.util.Random().nextInt(10 * 60 * 1000);
					// 角色列表
					try {
						this.state = STATE_角色列表;
						List<Player> plist = comm.获得角色列表(username, authCode);
						if (plist != null && plist.size() > 0) {
							this.player = plist.get(0);
						}
					} catch (Exception e) {
						System.out.println("[获得角色列表失败] [" + username + "] \n" + StringUtil.getStackTrace(e));
						errorMessage = StringUtil.getStackTrace(e);
						end = true;
					}
					if (this.player == null) {
						// 创建角色
						try {
							this.state = STATE_创建角色;
							Player p = new Player();
							p.setName("机器人-" + StringUtil.randomIntegerString(12));
							p.setId(0);
							p.setUsername(username);
							comm.创建角色(p);
						} catch (Exception e) {
							System.out.println("[创建角色失败] [" + username + "] \n" + StringUtil.getStackTrace(e));
							errorMessage = StringUtil.getStackTrace(e);
							end = true;
						}
						try {
							this.state = STATE_角色列表;
							List<Player> plist = comm.获得角色列表(username, authCode);
							if (plist != null && plist.size() > 0) {
								this.player = plist.get(0);
							}
						} catch (Exception e) {
							System.out.println("[获得角色列表失败] [" + username + "] \n" + StringUtil.getStackTrace(e));
							errorMessage = StringUtil.getStackTrace(e);
							end = true;
						}
					}
					try {
						this.state = STATE_进入游戏;
						comm.进入游戏(player.getId(), authCode);
					} catch (Exception e) {
						System.out.println("[进入游戏失败] [" + username + "] [" + player.getName() + "] \n" + StringUtil.getStackTrace(e));
						errorMessage = StringUtil.getStackTrace(e);
						end = true;
					}
				}
			} 
			else if (this.state == STATE_进入游戏) {
				enterTime = start;
				try {
					if (mapName != null && !mapName.equals(player.getMapName())) {
						// 跳地图；
						comm.跳转地图(mapName);
						player.setMapName(mapName);
					}
				} catch (Exception e) {
					System.out.println("[跳转地图失败] [" + username + "] [" + player.getName() + "] [map:" + mapName + "] \n" + StringUtil.getStackTrace(e));
					errorMessage = StringUtil.getStackTrace(e);
					end = true;
				}
				this.state = STATE_走动;
			} 
			else if (this.state == STATE_走动) {
				try {
					if (start >= nextActionTime) {
						comm.走动(start);
					}
					if (start - lastFightTime > 10000 && start - enterTime > fightTime) {
						synchronized (this) {
							LivingObject los[] = livings.values().toArray(new LivingObject[0]);
							for (LivingObject o : los) {
								if (o instanceof Sprite) {
									Sprite s = (Sprite) o;
									if (s.isAlive() && s.getSpriteType() == 0 && player.getLevel() - s.getLevel() > 10) {
										this.targetEnemy = s;
										this.state = STATE_战斗;
									}
								}
							}
						}
					}

				} catch (Exception e) {
					System.out.println("[走动失败] [" + username + "] [" + player.getName() + "] \n" + StringUtil.getStackTrace(e));
					errorMessage = StringUtil.getStackTrace(e);
					end = true;
				}
			} 
			
//			else if (this.state == STATE_战斗) {
//				if (start > nextActionTime) {
//					if (targetEnemy != null) {
//						LivingObject o = livings.get(targetEnemy.getId());
//						if (o != null) {
//							if (o.isAlive()) {
//								int distance = 计算距离(player, o);
//								if (distance > 50) {
//									boolean succ = comm.追踪(start, o);
//									if (!succ) {
//										this.state = STATE_走动;
//										this.targetEnemy = null;
//										System.out.println("[追踪失败] [" + player.getName() + "] [" + distance + "] [player:" + player.getX() + "/" + player.getY() + "] [enemy:" + o.getX() + "/" + o.getY() + "]");
//									}
//									return;
//								} else {
//									comm.攻击(targetEnemy, player.getCareer());
//									nextActionTime = nextActionTime + 1000;
//									lastFightTime = start;
//								}
//							}
//						} else {
//							System.out.println("[战斗失败:生物不存在] [" + player.getName() + "] [player:" + player.getX() + "/" + player.getY() + "]");
//							this.targetEnemy = null;
//							this.state = STATE_走动;
//							return;
//						}
//					} else {
//						System.out.println("[战斗失败:生物不存在] [" + player.getName() + "] [player:" + player.getX() + "/" + player.getY() + "]");
//						this.state = STATE_走动;
//						return;
//					}
//				}
//			}
		} catch (Exception e) {
			System.out.println("[" + username + "] [机器人动作异常] \n" + StringUtil.getStackTrace(e));
			errorMessage = StringUtil.getStackTrace(e);
			end = true;
		}
		if (start > endTime) {
			this.end = true;
		}
		if((System.currentTimeMillis() - lastBeatTime >= 10000L)){
			lastBeatTime = System.currentTimeMillis();
			if(player != null){
				System.out.println("["+TimeTool.formatter.varChar23.format(System.currentTimeMillis())+"] [玩家心跳] [10秒1跳] [账号:"+username+"] [角色名:"+player.getName()+"] [状态:"+STATE_DESP[lastStat]+"/"+STATE_DESP[state]+"] [地图:"+(player.getCurrentGame()!=null?player.getCurrentGame().gi.name:"null")+"] [x:"+player.getX()+"] [y:"+player.getY()+"]");
			}
		}
		if(state  != lastStat){
			if(player != null){
				System.out.println("["+TimeTool.formatter.varChar23.format(System.currentTimeMillis())+"] [玩家心跳] [状态改变] [账号:"+username+"] [角色名:"+player.getName()+"] [状态:"+STATE_DESP[lastStat]+"/"+STATE_DESP[state]+"] [地图:"+(player.getCurrentGame()!=null?player.getCurrentGame().gi.name:"null")+"] [x:"+player.getX()+"] [y:"+player.getY()+"]");
			}
			lastStat = state;
		}
	}


	public boolean 使用道具(String articleName, int useNum) throws Exception {
		Knapsack knaps[] = player.getKnapsacks_common();
		for (int i = 0; i < knaps.length; i++) {
			Cell cells[] = knaps[i].getCells();
			for (int j = 0; j < cells.length; j++) {
				Cell cell = cells[j];
				if (cell != null) {
					long id = cell.getEntityId();
					PropsEntity pe = propsEntityMap.get(id);
					if (pe != null) {
						System.out.println("trace_article [" + i + "] [" + j + "] [道具:" + pe.getShowName() + "]");
						if (pe.getShowName().equals(articleName)) {
							int count = cell.getCount();
							for (int k = 0; k < useNum && k < count; k++) {
								comm.使用物品(i, j);
							}
							useNum -= count;
							if (useNum <= 0) break;
						}
					}
				}
			}
			if (useNum <= 0) break;
		}
		if (useNum > 0) {
			return false;
		}
		return true;
	}

	public int 计算距离(LivingObject o1, LivingObject o2) {
		double L = Graphics2DUtil.distance(new Point(o1.getX(), o1.getY()), new Point(o2.getX(), o2.getY()));
		return (int) L;
	}

	public void handleResponseMessage(ResponseMessage response) {
		if (response instanceof USER_ENTER_SERVER_RES) {
			USER_ENTER_SERVER_RES res = (USER_ENTER_SERVER_RES) response;
			byte result = res.getResult();
			if (result == 0) {
				this.state = STATE_登录;
				this.canEnterFlag = true;
			}
			System.out.println("[收到进入服务器通知] [result:" + result + "] [" + this.username + "] [state:" + state + "]");
		}
	}

	public void handleRequestMessage(RequestMessage request) {
		if (request instanceof NOTIFY_SELFCHANGE_REQ) {
			if(player == null){
				System.out.println("【Robot处理请求信息错误】 [stat:"+STATE_DESP[state]+"] [id:"+id+"] [username:"+username+"] ["+request.getTypeDescription()+"]");
				return;
			}
			NOTIFY_SELFCHANGE_REQ req = (NOTIFY_SELFCHANGE_REQ) request;
			FieldChangeEvent events[] = req.getChangeEvents();
			for (FieldChangeEvent event : events) {
				int fid = event.getFieldId();
				event.getValueType();
				Object value = event.getValue();
				long objectId = event.getObjectId();
				if (objectId == player.getId()) {
					player.setSelfValue(fid, value);
				}
			}
		} else if (request instanceof AROUND_CHANGE_REQ) {
			if(player == null){
				System.out.println("【Robot处理请求信息错误】 [stat:"+STATE_DESP[state]+"] [id:"+id+"] [username:"+username+"] ["+request.getTypeDescription()+"]");
				return;
			}
			AROUND_CHANGE_REQ req = (AROUND_CHANGE_REQ) request;
			FieldChangeEvent events[] = req.getChangeEvents();
			for (FieldChangeEvent event : events) {
				int fid = event.getFieldId();
				event.getValueType();
				Object value = event.getValue();
				long objectId = event.getObjectId();
				if (objectId == player.getId()) {
					player.setValue(fid, value);
				} else {
					LivingObject lo = livings.get(objectId);
					if (lo != null) {
						if (lo instanceof Player) {
							Player p = (Player) lo;
							p.setSelfValue(fid, value);
						} else if (lo instanceof Pet) {
							Pet p = (Pet) lo;
							p.setValue(fid, value);
						} else if (lo instanceof Sprite) {
							Sprite s = (Sprite) lo;
							s.setValue(fid, value);
						}
					}
				}
			}
			synchronized (this) {
				Player ps[] = req.getEnterPlayers();
				for (Player p : ps) {
					if (livings.get(p.getId()) == null) {
						livings.put(p.getId(), p);
					}
				}
				Pet pets[] = req.getEnterPets();
				for (Pet p : pets) {
					if (livings.get(p.getId()) == null) {
						livings.put(p.getId(), p);
					}
				}
				Sprite ss[] = req.getEnterSprites();
				for (Sprite s : ss) {
					if (livings.get(s.getId()) == null) {
						livings.put(s.getId(), s);
					}
				}
				ps = req.getLeavePlayers();
				for (Player p : ps) {
					livings.remove(p.getId());
				}
				pets = req.getLeavePets();
				for (Pet p : pets) {
					livings.remove(p.getId());
				}
				ss = req.getLeaveSprites();
				for (Sprite s : ss) {
					livings.remove(s.getId());
				}
			}
		} else if (request instanceof NOTIFY_EVENT_REQ) {
			if(player == null){
				System.out.println("【Robot处理请求信息错误】 [stat:"+STATE_DESP[state]+"] [id:"+id+"] [username:"+username+"] ["+request.getTypeDescription()+"]");
				return;
			}
		} else if (request instanceof SET_POSITION_REQ) {
			if(player == null){
				System.out.println("【Robot处理请求信息错误】 [stat:"+STATE_DESP[state]+"] [id:"+id+"] [username:"+username+"] ["+request.getTypeDescription()+"]");
				return;
			}
			SET_POSITION_REQ req = (SET_POSITION_REQ) request;
			player.setX(req.getX());
			player.setY(req.getY());
		}
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}

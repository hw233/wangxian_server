package com.fy.engineserver.lineup;

import java.util.ArrayList;
import java.util.List;

/**
 * 10人队的组合模板
 * 
 *
 */
public class Team10Template implements TeamTemplate {
	
	public static final int ROLE_NUM_NEED_奶妈 = 2;
	
	public static final int ROLE_NUM_NEED_坦克 = 2;
	
	public static final int ROLE_NUM_NEED_输出 = 6;
	
	public static final int MAX_PLAYER_NUM = 10;
	
	public static long seq_id = 1;
	
	public List<LineupPlayer> teamplayers;
	
	public Team10Template() {
		this.teamplayers = new ArrayList<LineupPlayer>();
		seq_id++;
	}
	
	/**
	 * 加入这个组合，如果可以加入，返回true
	 * @param luplayer
	 * @return
	 */
	public synchronized boolean takein(LineupPlayer luplayer) {
		if(teamplayers.size() < MAX_PLAYER_NUM) {
			int naimaNum = 0;
			int tankNum = 0;
			int shuchuNum = 0;
			for(LineupPlayer dp : teamplayers) {
				if(dp.getTeamrole() == TeamRole.奶妈) {
					naimaNum++;
				} else if(dp.getTeamrole() == TeamRole.坦克) {
					tankNum++;
				} else if(dp.getTeamrole() == TeamRole.输出) {
					shuchuNum++;
				}
			}
			//模拟加入后的情形
			if(luplayer.getTeamrole() == TeamRole.奶妈) {
				naimaNum++;
			} else if(luplayer.getTeamrole() == TeamRole.坦克) {
				tankNum++;
			} else if(luplayer.getTeamrole() == TeamRole.输出) {
				shuchuNum++;
			}
			int totalNum = teamplayers.size() + 1;
			if(ROLE_NUM_NEED_奶妈 - naimaNum > 0) {
				totalNum += ROLE_NUM_NEED_奶妈 - naimaNum;
			}
			if(ROLE_NUM_NEED_坦克 - tankNum > 0) {
				totalNum += ROLE_NUM_NEED_坦克 - tankNum;
			}
			if(ROLE_NUM_NEED_输出 - shuchuNum > 0) {
				totalNum += ROLE_NUM_NEED_输出 - shuchuNum;
			}
			if(totalNum > MAX_PLAYER_NUM) {
				return false;
			}
			teamplayers.add(luplayer);
			if(LineupManager.log.isDebugEnabled()) {
				StringBuffer sb = new StringBuffer();
				for(LineupPlayer lp : teamplayers) {
					sb.append(TeamRole.getRoleDesp(lp.teamrole) + ",");
				}
				String str = sb.toString();
				if(str.length() > 0) {
					str = str.substring(0, str.length()-1);
				}
//				LineupManager.log.debug("[尝试组合] [10人组合] ["+seq_id+"] [目前组合:"+str+"] ["+luplayer.player.getId()+"] ["+luplayer.player.getName()+"] ["+TeamRole.getRoleDesp(luplayer.teamrole)+"] ["+teamplayers.size()+"]");
					if (LineupManager.log.isDebugEnabled()) LineupManager.log.debug("[尝试组合] [10人组合] [{}] [目前组合:{}] [{}] [{}] [{}] [{}]", new Object[]{seq_id,str,luplayer.player.getId(),luplayer.player.getName(),TeamRole.getRoleDesp(luplayer.teamrole),teamplayers.size()});
			}
			return true;
		}
		return false;
	}
	
	public boolean isFull() {
		return teamplayers.size() == MAX_PLAYER_NUM;
	}

	public List<LineupPlayer> getTeamplayers() {
		return teamplayers;
	}

	public void setTeamplayers(List<LineupPlayer> teamplayers) {
		this.teamplayers = teamplayers;
	}
	
}

package com.fy.engineserver.sprite;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;

public class TeamManager {
	
	public static byte DEFAULT_ASSIGN_RULE = 0;
	
	public List<Team> getNearbyTeams(Player player) {
		
		Game game = player.getCurrentGame();
		LivingObject[] los = game.getVisbleLivings(player, false);
		
		ArrayList<Team> teams = new ArrayList<Team>();
		Team team = null;
		if(player.getTeam() != null){
			team = player.getTeam();
		}
		for(LivingObject lo : los){
			if(lo instanceof Player){
				Player p = (Player)lo;
				if(p.getTeam() != null && p.getTeam() != team){
					if(teams.indexOf(p.getTeam()) < 0){
						teams.add(p.getTeam());
					}
				}
			}
		}
		return teams;
		
	}
	
}

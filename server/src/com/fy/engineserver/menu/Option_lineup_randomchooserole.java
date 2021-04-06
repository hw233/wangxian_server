package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.lineup.LineupManager;
import com.fy.engineserver.lineup.TeamRole;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.text.StringUtil;

public class Option_lineup_randomchooserole extends Option {
	
	String[] downCityNames;
	byte roleId;

	public String[] getDownCityNames() {
		return downCityNames;
	}

	public void setDownCityNames(String[] downCityNames) {
		this.downCityNames = downCityNames;
	}
	
	public byte getRoleId() {
		return roleId;
	}

	public void setRoleId(byte roleId) {
		this.roleId = roleId;
	}

	public Option_lineup_randomchooserole(String[] downCityNames, byte roleId) {
		// TODO Auto-generated constructor stub
		this.downCityNames=downCityNames;
		this.roleId = roleId;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		LineupManager lum = LineupManager.getInstance();
		
		if(player.getLevel() < lum.getStartlevel()) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0, Translate.text_5292);
			player.addMessageToRightBag(req);
			return;
		}
		
		if(!TeamRole.canBeRole(CareerManager.careerNames[player.getCareer()], roleId)) {
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(120);
			mw.setTitle(Translate.text_5296);
			
			Option ops[] = new Option[3];
			Option_lineup_randomchooserole oc = new Option_lineup_randomchooserole(downCityNames,TeamRole.坦克);
			ops[0] = oc;
			oc.setText(TeamRole.getRoleDesp(TeamRole.坦克));
			oc.setOptionId(StringUtil.randomString(10));
			oc.setColor(0xffffff);
			oc.setIconId(TeamRole.iconid[TeamRole.坦克]);
			
			
			oc = new Option_lineup_randomchooserole(downCityNames,TeamRole.奶妈);
			ops[1] = oc;
			oc.setText(TeamRole.getRoleDesp(TeamRole.奶妈));
			oc.setOptionId(StringUtil.randomString(10));
			oc.setColor(0xffffff);
			oc.setIconId(TeamRole.iconid[TeamRole.奶妈]);
			
			
			oc = new Option_lineup_randomchooserole(downCityNames,TeamRole.输出);
			ops[2] = oc;
			oc.setText(TeamRole.getRoleDesp(TeamRole.输出));
			oc.setOptionId(StringUtil.randomString(10));
			oc.setColor(0xffffff);
			oc.setIconId(TeamRole.iconid[TeamRole.输出]);
			
			
			mw.setOptions(ops);
			StringBuffer sb = new StringBuffer();
			String roleCareers[] = TeamRole.getRoleCareers(TeamRole.坦克);
			for(String s : roleCareers) {
				sb.append(s + "、");
			}
			String danke = sb.toString();
			if(danke.length() > 0) {
				danke = danke.substring(0, danke.length()-1);
			}
			danke = "[color="+0xffff0000+"]" +danke + Translate.text_5299+0xffff0000+"]" + TeamRole.getRoleDesp(TeamRole.坦克) + Translate.text_5300;
			
			sb = new StringBuffer();
			roleCareers = TeamRole.getRoleCareers(TeamRole.奶妈);
			for(String s : roleCareers) {
				sb.append(s + "、");
			}
			String naima = sb.toString();
			if(naima.length() > 0) {
				naima = naima.substring(0, naima.length()-1);
			}
			naima = "[color="+0xffff0000+"]" +naima + Translate.text_5299+0xffff0000+"]" + TeamRole.getRoleDesp(TeamRole.奶妈) + Translate.text_5300;
			
			sb = new StringBuffer();
			roleCareers = TeamRole.getRoleCareers(TeamRole.输出);
			for(String s : roleCareers) {
				sb.append(s + "、");
			}
			String shuchu = sb.toString();
			if(shuchu.length() > 0) {
				shuchu = shuchu.substring(0, shuchu.length()-1);
			}
			shuchu = "[color="+0xffff0000+"]" +shuchu + Translate.text_5299+0xffff0000+"]" + TeamRole.getRoleDesp(TeamRole.输出) + Translate.text_5300;
			
			String outstr = danke + "\n" + naima + "\n" + shuchu;
			
			mw.setDescriptionInUUB(CareerManager.careerNames[player.getCareer()]+Translate.text_5301+TeamRole.getRoleDesp(roleId)+Translate.text_5302 + outstr);
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
			player.addMessageToRightBag(res);
		} else {
			lum.takein(player, roleId, downCityNames);
		}
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_排队进入随机副本选择角色;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5303;
	}

}

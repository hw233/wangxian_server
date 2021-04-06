package com.fy.engineserver.buffsave;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

/**
 * buff存储管理
 * 
 *
 */
public class BufferSaveManager {
	
	public static SimpleEntityManager<BuffSave> bem;
	
	private static BufferSaveManager self;
	
	public static Logger log = GamePlayerManager.logger;
	
	public static long BUFF_SAVE_DAYS = 3*24*60*60*1000;
	
	public static BufferSaveManager getInstance(){
		return self;
	}

	public void init(){
		
		bem = SimpleEntityManagerFactory.getSimpleEntityManager(BuffSave.class);
		self = this;
		ServiceStartRecord.startLog(this);
	}

	public synchronized BuffSave saveBuff(Player p,Buff b){
		long now = System.currentTimeMillis();
		BuffSave buff = null;
		try{
			buff = new BuffSave();
			buff.setSaveID(bem.nextId());
			buff.setPid(p.getId());
			buff.setSavetime(b.getInvalidTime()-now);
			buff.setBuffname(b.getTemplateName());
			buff.setBufflevel(b.getLevel());
			buff.setEndtime(b.getInvalidTime()+BUFF_SAVE_DAYS);
			bem.save(buff);
			log.warn("[玩家存储buff] [成功] [buff名字："+buff.getBuffname()+"] [buff级别："+buff.getBufflevel()+"] [buff剩余时间："+buff.getSavetime()+"ms] [开始时间："+TimeTool.formatter.varChar23.format(b.getStartTime())+"] [失效时间："+TimeTool.formatter.varChar23.format(buff.getEndtime())+"] ["+p.getLogString()+"] [耗时："+(System.currentTimeMillis()-now)+"]");
			return buff;
		}catch(Exception e){
			e.printStackTrace();
			log.warn("[玩家存储buff] [异常] [buff名字："+buff.getBuffname()+"] [buff级别："+buff.getBufflevel()+"] [buff剩余时间："+buff.getSavetime()+"ms] [开始时间："+TimeTool.formatter.varChar23.format(b.getStartTime())+"] [失效时间："+TimeTool.formatter.varChar23.format(buff.getEndtime())+"] ["+p.getLogString()+"] [耗时："+(System.currentTimeMillis()-now)+"]",e);
			return null;
		}
	}
	
	public void delbuff(BuffSave buff){
		long now = System.currentTimeMillis();
		try {
			bem.remove(buff);
			log.warn("[玩家删除过期buff] [成功] [buff名字："+buff.getBuffname()+"] [buff级别："+buff.getBufflevel()+"] [玩家id："+buff.getPid()+"] [耗时："+(System.currentTimeMillis()-now)+"]");
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("[玩家删除过期buff] [异常] [buff名字："+buff.getBuffname()+"] [buff级别："+buff.getBufflevel()+"] [玩家id："+buff.getPid()+"] [耗时："+(System.currentTimeMillis()-now)+"]");
		}
	}
	
	public List<BuffSave> getbuff(long pid){
		long now = System.currentTimeMillis();
		List<BuffSave> list = new ArrayList<BuffSave>();
		try {
			list = bem.query(BuffSave.class, " pid = "+pid, "", 1, 100);
			log.warn("[通过玩家id获得存储的buff] [成功] [id："+pid+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("[通过玩家id获得存储的buff] [异常] [pid："+pid+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	public synchronized boolean updateBuff(Player p,BuffSave newbuff){
		long now = System.currentTimeMillis();
		boolean issucc = false;
		try {
			bem.flush(newbuff);
			issucc = true;
			log.warn("[玩家替换已经存储的buff] [成功] ["+p.getLogString()+"] [buff："+newbuff+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			issucc = false;
			e.printStackTrace();
			log.warn("[玩家替换已经存储的buff] [异常] ["+p.getLogString()+"] [buff："+newbuff+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return issucc;
	}
	
	public String getNameByLevel(int bufflevel){
		String 酒的名字 = ""; 
		int level = bufflevel+1;
		if(level<6){
			酒的名字 = Translate.白玉泉;
		}else if(level>=6 && level<11){
			酒的名字 = Translate.金浆醒;
		}else if(level>=11 && level<16){
			酒的名字 = Translate.香桂郢酒;
		}else if(level>=16 && level<21){
			酒的名字 = Translate.琼浆玉液;
		}else if(level>=21){
			酒的名字 = Translate.诸神玉液;
		}
		return 酒的名字;
	}
	
	public long 取酒需消耗的钱(int bufflevel){
		int level = bufflevel+1;
		if(level==1 || level==6 ||level==11 || level==16||level==21) {
			return 10;
		}else if(level==2 || level==7 ||level==12 || level==17||level==22){
			return 20;
		}else if(level==3 || level==8 ||level==13|| level==18||level==23){
			return 30;
		}else if(level==4 || level==9 ||level==14|| level==19||level==24){
			return 40;
		}else if(level==5 || level==10 ||level==15|| level==20||level==25){
			return 50;
		}
		return 10;
	}
	
	public String getJiuLevel(int bufflevel){
		int level = bufflevel+1;
		if(level==1 || level==6 ||level==11 || level==16||level==21) {
			return "<f color=' "+ArticleManager.COLOR_WHITE+"'>";
		}else if(level==2 || level==7 ||level==12 || level==17||level==22){
			return "<f color=' "+ArticleManager.COLOR_GREEN+"'>";
		}else if(level==3 || level==8 ||level==13|| level==18||level==23){
			return "<f color=' "+ArticleManager.COLOR_BLUE+"'>";
		}else if(level==4 || level==9 ||level==14|| level==19||level==24){
			return "<f color=' "+ArticleManager.COLOR_PURPLE+"'>";
		}else if(level==5 || level==10 ||level==15|| level==20||level==25){
			return "<f color=' "+ArticleManager.COLOR_ORANGE+"'>";
		}
		return "<f color=' "+ArticleManager.COLOR_WHITE+"'>";
	}
	
	public boolean 取酒扣钱(Player p,int bufflevel){
		BillingCenter bc = BillingCenter.getInstance();
		long 消耗的钱 = 取酒需消耗的钱(bufflevel)*1000;
  		try {
			bc.playerExpense(p, 消耗的钱, CurrencyType.BIND_YINZI, ExpenseReasonType.取喝酒buff, "取酒buff");
			log.error("[取酒需消耗的钱] [ok] [bufflevel:"+bufflevel+"] [" + p.getLogString() + "] []");
			return true;
		} catch (Exception e) {
			log.error("[取酒需消耗的钱] [异常] [bufflevel:"+bufflevel+"] [" + p.getLogString() + "] []", e);
			return false;
		}
	}
	
	public boolean 玩家是否能使用该酒(int plevel,String jiuname){
		if(20<=plevel && plevel<81 && jiuname.equals(Translate.白玉泉)){
			return true;
		}
		if(81<=plevel && plevel<161 && jiuname.equals(Translate.金浆醒)){
			return true;
		}
		if(161<=plevel && jiuname.equals(Translate.香桂郢酒)){
			return true;
		}
		if(plevel>=221 && jiuname.equals(Translate.琼浆玉液)){
			return true;
		}
		if(plevel>=281 && jiuname.equals(Translate.诸神玉液)){
			return true;
		}
		return false;
	}
	
	public static SimpleEntityManager<BuffSave> getBem() {
		return bem;
	}

	public static void setBem(SimpleEntityManager<BuffSave> bem) {
		BufferSaveManager.bem = bem;
	}
	
	public void destroy(){
		bem.destroy();
	}

}

package com.fy.engineserver.datasource.article.data.props;

import static com.fy.engineserver.datasource.language.Translate.STRING_1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_ACTIVITY_STATUS_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.petdao.MonsterData;
import com.fy.engineserver.sprite.petdao.PetDao;
import com.fy.engineserver.sprite.petdao.PetDaoManager;

/**
 * 开启礼包，跑出怪物
 *
 */
public class RefreshMonsterPackage extends Props implements Serializable{
	
	private static Random random = new Random();
	
	public int [] monsterids;		//怪物id集
	
	private int [] refreshflop;		//怪物刷新概率
	
	private String []boxname;		//不为空是开启礼包，获得该物品
	
	private int xy[];				//当前箱子的坐标		
	
	public RefreshMonsterPackage(int [] monsterids,int [] refreshflop){
		this.monsterids = monsterids;
		this.refreshflop = refreshflop;
	}
	
	/**
	 * 1只2只的概率比
	 */
	public int flopnum [] = {6,1};
	public int 概率对应的数量 [] = {1,2};
	public String 概率对应的描述[] = {Translate.迷城1只宠物提示,Translate.迷城2只宠物提示};
	
	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		try{
			int index = getRandomId(refreshflop);
			PetDao pd = PetDaoManager.getInstance().getPetDao(player, player.getCurrentGame().gi.name);
			if(pd.getMc().getKeyrefreshnum()<4){
				if(index>=monsterids.length){	
					player.sendError(PetDaoManager.获得钥匙描述[PetDaoManager.等级索引(pd.getTypelevel())]);
					if(pd!=null){
						
						pd.getMc().setKeyrefreshnum(pd.getMc().getKeyrefreshnum()+1);
						pd.getMc().setBoxovernum(pd.getMc().getBoxovernum()-1);
						if(pd.getMc().getKeyovernum()<0){
							pd.getMc().setKeyovernum(0);
						}
						String mes = "<f color='0x78f4ff'>"+PetDaoManager.等级使用物品描述[PetDaoManager.等级索引(pd.getTypelevel())]+Translate.数量+"：</f>"+pd.getMc().getKeyovernum()+"/"+PetDaoManager.初始给钥匙数+"\n"+"<f color='0x78f4ff'>"+PetDaoManager.等级消耗物品描述[PetDaoManager.等级索引(pd.getTypelevel())]+Translate.数量+"：</f>"+pd.getMc().getBoxovernum()+"/"+30;
						try{
							ArrayList<RefreshMonsterPackage> overboxs = pd.getOverBox();
							overboxs.remove(this);
							pd.setOverBox(overboxs);
							PetDaoManager.getInstance().datas.put(player.getId(), pd);
						}catch(Exception e2){
							e2.printStackTrace();
						}
						
						NOTICE_ACTIVITY_STATUS_RES res_ = new NOTICE_ACTIVITY_STATUS_RES(GameMessageFactory.nextSequnceNum(), true, "", 1, Translate.倒计时, mes, (int)(pd.getMc().getContinuetime()/1000));
						player.addMessageToRightBag(res_);
						PetDaoManager.log.warn("[宠物迷城] 【开箱子】 [使用箱子刷钥匙] [成功] [玩家："+player.getName()+"] [剩余箱子："+(pd.getOverBox()==null?"null":pd.getOverBox().size())+"] [钥匙剩余数量："+pd.getMc().getKeyovernum()+"] [已开出钥匙的数量："+pd.getMc().getKeyrefreshnum()+"] [index:"+index+"] "); 
						return true;
					}
				}
			}						
			//是箱子	
			int keyflop = getRandomId(flopnum);
			int monsternum = 概率对应的数量[keyflop];
			String mess = Translate.translateString(概率对应的描述[keyflop], new String[][] { { STRING_1,PetDaoManager.等级消耗物品描述[PetDaoManager.等级索引(pd.getTypelevel())] } });
			int ids[] = new int[monsternum];
			for(int i=0;i<monsternum;i++){
				int ranid = getRandomId(monsterids);
				ids[i] = monsterids[ranid];
			}
			
			boolean issucc = false;
			if(ids.length>0){
				Sprite sprite = null;
				for(int i=0;i<ids.length;i++){
					int id = ids[i];
					sprite = MemoryMonsterManager.getMonsterManager().createMonster(id);
					if(sprite!=null){
						sprite.setX(xy[0]+random.nextInt(50));
						sprite.setY(xy[1]+random.nextInt(50));
						game.addSprite(sprite);
						issucc = true;
						
						MonsterData md = pd.getOvermonsters();
						if(md==null){
							md = new MonsterData();
							md.setMonsterids(new ArrayList<Integer>());
							md.setXys(new ArrayList<Integer []>());
						}
						md.setPid(player.getId());
						md.getMonsterids().add(new Integer(id));
						md.getXys().add(new Integer[]{new Integer(xy[0]),new Integer(xy[1])});
						pd.setOvermonsters(md);
						PetDaoManager.log.warn("[宠物迷城] 【开箱子】 [使用箱子刷怪] [成功]  [玩家："+player.getName()+"] [钥匙剩余数量："+pd.getMc().getKeyovernum()+"] [怪物ids："+(md.getMonsterids()==null?0:md.getMonsterids().size())+"] [坐标："+(md.getXys()==null?0:md.getXys().size())+"] [x:"+xy[0]+"] [y:"+xy[1]+"] [id:"+id+"] ["+Translate.数量+":"+monsternum+"]");
					}
				}
				if(issucc){
					pd.getMc().setBoxovernum(pd.getMc().getBoxovernum()-1);
					pd.getMc().setKeyovernum(pd.getMc().getKeyovernum()-1);
					if(pd.getMc().getKeyovernum()<0){
						pd.getMc().setKeyovernum(0);
					}
					String mes = "<f color='0x78f4ff'>"+PetDaoManager.等级使用物品描述[PetDaoManager.等级索引(pd.getTypelevel())]+Translate.数量+"：</f>"+pd.getMc().getKeyovernum()+"/"+PetDaoManager.初始给钥匙数+"\n"+"<f color='0x78f4ff'>"+PetDaoManager.等级消耗物品描述[PetDaoManager.等级索引(pd.getTypelevel())]+Translate.数量+"：</f>"+pd.getMc().getBoxovernum()+"/"+30;
					try{
						ArrayList<RefreshMonsterPackage> overboxs = pd.getOverBox();
						overboxs.remove(this);
						pd.setOverBox(overboxs);
						PetDaoManager.getInstance().datas.put(player.getId(), pd);
					}catch(Exception e2){
						e2.printStackTrace();
					}
					NOTICE_ACTIVITY_STATUS_RES res_ = new NOTICE_ACTIVITY_STATUS_RES(GameMessageFactory.nextSequnceNum(), true, "", 1, Translate.倒计时, mes, (int)((pd.getMc().getContinuetime())/1000));
					player.addMessageToRightBag(res_);
					player.sendError(mess);
				}
			}
			return issucc;
		}catch(Exception e){
			e.printStackTrace();
			PetDaoManager.log.warn("[宠物迷城] 【开箱子】 [异常] ["+player.getLogString()+"] ["+e+"]");
			return false;
		}
		
	}
	
	/**
	 * 获得随机怪物id
	 * @return
	 */
	public int getRandomId(int[] floparr){
		try{
			int count = 0;
			if(getflopcount(floparr)==1){
				for(int i=0;i<floparr.length;i++){
					if(floparr[i]==1){
						return i;
					}
				}
			}
			int randomnum = random.nextInt(getflopcount(floparr)+1);
			for(int i=0;i<floparr.length;i++){
				count += floparr[i];
				if(count>=randomnum){
					return i;
				}
			}
		}catch(Exception e){
			ArticleManager.logger.warn("[随机刷怪礼包] [获得随机怪物id] [出错] ["+e+"]");
		}
		return 0;
	}
	
	
	private int getflopcount(int[] floparr){
		int count = 0;
		for(int c:floparr){
			count += c;
		}
		return count;
	}

	public int[] getMonsterids() {
		return monsterids;
	}

	public int[] getRefreshflop() {
		return refreshflop;
	}

	public void setMonsterids(int[] monsterids) {
		this.monsterids = monsterids;
	}

	public void setRefreshflop(int[] refreshflop) {
		this.refreshflop = refreshflop;
	}

	public String[] getBoxname() {
		return boxname;
	}

	public void setBoxname(String[] boxname) {
		this.boxname = boxname;
	}

	public int[] getXy() {
		return xy;
	}

	public void setXy(int[] xy) {
		this.xy = xy;
	}

}

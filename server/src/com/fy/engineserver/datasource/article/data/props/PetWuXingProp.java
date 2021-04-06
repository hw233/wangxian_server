package com.fy.engineserver.datasource.article.data.props;

import java.util.Random;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;


public class PetWuXingProp extends Props{

	private int []wuxings;
	private int []jilvs;
	private String petnames[];
	

	public int[] getWuxings() {
		return wuxings;
	}

	public void setWuxings(int[] wuxings) {
		this.wuxings = wuxings;
	}

	public int[] getJilvs() {
		return jilvs;
	}

	public void setJilvs(int[] jilvs) {
		this.jilvs = jilvs;
	}

	public String[] getPetnames() {
		return petnames;
	}

	public void setPetnames(String[] petnames) {
		this.petnames = petnames;
	}

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		if(game==null || player==null || ae==null){
			PetManager.logger.warn("[PetWuXingProp] [use] [fail] [game==null || player==null || ae==null]");
			return false;
		}
		
		if(petnames==null || wuxings==null || jilvs==null){
			PetManager.logger.warn(player.getLogString()+"[PetWuXingProp] [use] [fail] [petnames==null wuxings==null || jilvs==null]");
		}
		
		Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
		
		if(a==null){
			PetManager.logger.warn(player.getLogString()+"[PetWuXingProp] [use] [fail] [a==null]");
			return false;
		}
		
		long petId = player.getActivePetId();
		Pet pet = null;
		try{
			pet = PetManager.getInstance().getPet(petId);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		if(pet == null){
			player.sendError(Translate.translateString(Translate.没有指定宠物不能使用, new String[][]{{Translate.STRING_1,this.getName()}}));
			return false;
		}

		boolean canuse = false;
		
		for(int i=0,len=petnames.length;i<len;i++){
			if(petnames[i].equals(pet.getCategory())){
				canuse = true;
				break;
			}
		}
		
		if(!canuse){
			String mes = Translate.translateString(Translate.此宠物不可使用悟性丹, new String[][] {{Translate.STRING_1, String.valueOf(ae.getArticleName())}});
			player.sendError(mes);
			return false;
		}
		
		if(pet.getWuXing()>=100){
			player.sendError(Translate.悟性已满);
			return false;
		}
		
		int wuxing = 0;
		int index = 0;
		int nums = 0;
		Random r = new Random();
		for(int i=0;i<jilvs.length;i++){
			nums += jilvs[i];
		}
		int rnum = r.nextInt(nums+1);
		int allnums = 0;
		for(int i=0;i<jilvs.length;i++){
			allnums += jilvs[i];
			if(allnums>=rnum){
				index = i;
				break;
			} 
		}
		wuxing = wuxings[index];
		int value = pet.getWuXing()+wuxing;
		if(value>=100){
			value = 100;
		}
		pet.setWuXing(value);
		PetManager.logger.warn(player.getLogString()+"[PetWuXingProp] [use] [succ] [pet:"+pet.getLogString()+"] [rnum:"+rnum+"] [index:"+index+"] [增加的悟性值:"+wuxing+"] [增加后的悟性:"+value+"]");
		return true;
	}
}

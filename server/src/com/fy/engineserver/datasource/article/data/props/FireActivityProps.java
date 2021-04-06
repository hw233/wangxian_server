package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.sprite.Player;

public class FireActivityProps extends Props {

	
	private int useNum;
	private String bufferName;
	protected String avata;
	
	@Override
	public String canUse(Player p) {
		return super.canUse(p);
	}
	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		return super.use(game, player, ae);
	}
	
	
	public int getUseNum() {
		return useNum;
	}
	public void setUseNum(int useNum) {
		this.useNum = useNum;
	}
	public String getBufferName() {
		return bufferName;
	}
	public void setBufferName(String bufferName) {
		this.bufferName = bufferName;
	}
	public String getAvata() {
		return avata;
	}
	public void setAvata(String avata) {
		this.avata = avata;
	}
	
	
	
}

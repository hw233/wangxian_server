package com.fy.engineserver.masterAndPrentice;

import com.fy.engineserver.sprite.Player;

public interface UpdateMasterPrenticeInterface {

	//升级师傅删除徒弟
	public void chushi(Player prentice);
	//判师徒弟背叛师傅
	public void rebel(Player master,Player prentice);
	//逐徒师傅逐出徒弟
	public void evict(Player master,Player prentice);
	//拜师成功 徒弟添加师傅
	public void addMaster(Player master,Player prentice);
	//收徒成功师傅添加徒弟
	public void addPrentice(Player master,Player prentice);
	//增加称号
	public void addShiTuChenghao(Player master,Player prentice);
	
	public void removeShiTuChenghao(Player master,Player prentice);
	
}

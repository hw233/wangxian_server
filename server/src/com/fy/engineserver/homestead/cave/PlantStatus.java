package com.fy.engineserver.homestead.cave;

import com.fy.engineserver.homestead.cave.resource.PlantCfg;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.sprite.npc.CaveNPC;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class PlantStatus {

	private int plantCfgId;
	private transient PlantCfg plantCfg;
	private long plantStartTime;
	private transient CaveNPC npc;
	/** 产物颜色 */
	private int outputColor;
	/** 树颜色 */
	private int treeColor;
	/** 总产量 */
	private int totalOutput;
	/** 剩余产量,当产量为空的时候重置形象 */
	private int leftOutput;
	/** 外形状态 */
	private int outShowStatus;

	private transient CaveField caveField;
	/** 变换形象时间 */
	private transient long changeAvtaTime;
	/** 成熟的时间(可收割) */
	private transient long harvestTime;

	private transient boolean autoFail;

	public PlantStatus() {

	}

	public static void createPlantStatus(PlantCfg plantCfg, CaveField caveField) {
		PlantStatus plantStatus = new PlantStatus();
		plantStatus.setPlantCfg(plantCfg);
		plantStatus.setPlantCfgId(plantCfg.getId());
		plantStatus.setCaveField(caveField);
		plantStatus.setPlantStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());


		// 设置第一次变化时间
		// 变形象的时间与现在时间的偏移量
		long diffTime = ((long) (plantCfg.getChangeOutShowRate() * plantCfg.getGrownUpTime()));
		plantStatus.setChangeAvtaTime(plantStatus.getPlantStartTime() + diffTime);
		plantStatus.setHarvestTime(plantStatus.getPlantStartTime() + plantCfg.getGrownUpTime());
		caveField.setPlantStatus(plantStatus);
		caveField.setCaveFieldBombData(null);
	}

	public void initCfg() {
		if (getPlantCfgId() > 0) {
			setPlantCfg(FaeryManager.getInstance().getPlantCfg(getPlantCfgId()));
		}
	}

	public int getPlantCfgId() {
		return plantCfgId;
	}

	public void setPlantCfgId(int plantCfgId) {
		this.plantCfgId = plantCfgId;
	}

	public PlantCfg getPlantCfg() {
		return plantCfg;
	}

	public void setPlantCfg(PlantCfg plantCfg) {
		this.plantCfg = plantCfg;
	}

	public long getPlantStartTime() {
		return plantStartTime;
	}

	public void setPlantStartTime(long plantStartTime) {
		this.plantStartTime = plantStartTime;
	}

	public CaveNPC getNpc() {
		return npc;
	}

	public void setNpc(CaveNPC npc) {
		this.npc = npc;
	}

	public int getOutputColor() {
		return outputColor;
	}

	public void setOutputColor(int outputColor) {
		this.outputColor = outputColor;
	}

	public int getTotalOutput() {
		return totalOutput;
	}

	public void setTotalOutput(int totalOutput) {
		this.totalOutput = totalOutput;
	}

	public int getLeftOutput() {
		return leftOutput;
	}

	public void setLeftOutput(int leftOutput) {
		this.leftOutput = leftOutput;
	}

	public CaveField getCaveField() {
		return caveField;
	}

	public void setCaveField(CaveField caveField) {
		this.caveField = caveField;
	}

	public long getChangeAvtaTime() {
		return changeAvtaTime;
	}

	public void setChangeAvtaTime(long changeAvtaTime) {
		this.changeAvtaTime = changeAvtaTime;
	}

	public long getHarvestTime() {
		return harvestTime;
	}

	public void setHarvestTime(long harvestTime) {
		this.harvestTime = harvestTime;
	}

	public int getOutShowStatus() {
		return outShowStatus;
	}

	public void setOutShowStatus(int outShowStatus) {
		this.outShowStatus = outShowStatus;
	}

	public boolean isAutoFail() {
		return autoFail;
	}

	public void setAutoFail(boolean autoFail) {
		this.autoFail = autoFail;
	}

	public int getTreeColor() {
		return treeColor;
	}

	public void setTreeColor(int treeColor) {
		this.treeColor = treeColor;
	}

	@Override
	public String toString() {
		return "PlantStatus [plantCfgId=" + plantCfgId + ", plantStartTime=" + plantStartTime + ", outputColor=" + outputColor + ", totalOutput=" + totalOutput + ", leftOutput=" + leftOutput + ", outShowStatus=" + outShowStatus + ", treeColor=" + treeColor + "]";
	}

}

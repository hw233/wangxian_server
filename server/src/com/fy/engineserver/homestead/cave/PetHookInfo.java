package com.fy.engineserver.homestead.cave;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 宠物挂机信息
 * 
 * 
 */
@SimpleEmbeddable
public class PetHookInfo {
	static long maxTime = 1000 * 60 * 60 * 24 * 3L;
	private long petOwnerId;
	private String petOwnerName;

	private long petId;
	private int petGrade;
	private String petName;

	private long caveId;
	private long houseOwnerId;
	private String houseOwnerName;

	private long hookTime;
	private long articleId;

	/**
	 * 已获得经验
	 */
	private transient long exp;

	public PetHookInfo() {
		// TODO Auto-generated constructor stub
	}

	public PetHookInfo(Player petOwner, Pet pet, long houseOwnerId, String houseOwnerName, long caveId, long articleId) {
		this.petOwnerId = petOwner.getId();
		this.petOwnerName = petOwner.getName();
		this.petGrade = pet.getLevel();
		this.petId = pet.getId();
		this.petName = pet.getName();
		this.houseOwnerId = houseOwnerId;
		this.houseOwnerName = houseOwnerName;
		this.caveId = caveId;
		this.articleId = articleId;
		this.hookTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
	}

	public long getPetOwnerId() {
		return petOwnerId;
	}

	public void setPetOwnerId(long petOwnerId) {
		this.petOwnerId = petOwnerId;
	}

	public String getPetOwnerName() {
		return petOwnerName;
	}

	public void setPetOwnerName(String petOwnerName) {
		this.petOwnerName = petOwnerName;
	}

	public long getPetId() {
		return petId;
	}

	public void setPetId(long petId) {
		this.petId = petId;
	}

	public int getPetGrade() {
		return petGrade;
	}

	public void setPetGrade(int petGrade) {
		this.petGrade = petGrade;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public long getHouseOwnerId() {
		return houseOwnerId;
	}

	public void setHouseOwnerId(long houseOwnerId) {
		this.houseOwnerId = houseOwnerId;
	}

	public String getHouseOwnerName() {
		return houseOwnerName;
	}

	public void setHouseOwnerName(String houseOwnerName) {
		this.houseOwnerName = houseOwnerName;
	}

	public long getHookTime() {
		return hookTime;
	}

	public void setHookTime(long hookTime) {
		this.hookTime = hookTime;
	}

	public long getCaveId() {
		return caveId;
	}

	public void setCaveId(long caveId) {
		this.caveId = caveId;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	@Override
	public String toString() {
		return "PetHookInfo [petOwnerId=" + petOwnerId + ", petOwnerName=" + petOwnerName + ", petId=" + petId + ", petGrade=" + petGrade + ", petName=" + petName + ", caveId=" + caveId + ", houseOwnerId=" + houseOwnerId + ", houseOwnerName=" + houseOwnerName + ", hookTime=" + hookTime + ", articleId=" + articleId + "]";
	}
}

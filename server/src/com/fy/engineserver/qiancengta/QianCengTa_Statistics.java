package com.fy.engineserver.qiancengta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;

//千层塔通塔时数据信息
public class QianCengTa_Statistics {
	
	public static int QCT_STATISTICS_NUM = 10000;
	public static ArrayList<QianCengTa_Statistics> statistics = new ArrayList<QianCengTa_Statistics>();
	public static void creatNewStatistics(Player player, int daoIndex, int cengIndex) {
		try{
			if (player == null) {
				return;
			}
			QianCengTa_Statistics qct_st = new QianCengTa_Statistics();
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			qct_st.setPassTime(format.format(new Date()));
			qct_st.setDaoIndex(daoIndex);
			qct_st.setCengIndex(cengIndex);
			qct_st.setPlayerID(player.getId());
			qct_st.setPlayerName(player.getName());
			qct_st.setPlayerLevel(player.getLevel());
			qct_st.setVipLevel(player.getVipLevel());
			qct_st.setMaxHp(player.getMaxHP());
			qct_st.setPhysicalDamage(player.getPhyAttack());
			qct_st.setMagicDamage(player.getMagicAttack());
			qct_st.setPhysicalDefence(player.getPhyDefence());
			qct_st.setMagicDefence(player.getMagicDefence());
			if (player.getActivePetId() > 0) {
				Pet pet = PetManager.getInstance().getPet(player.getActivePetId());
				if (pet != null) {
					qct_st.setPetName(pet.getCategory());
					qct_st.setPetLevel(pet.getLevel());
					qct_st.setPetMaxHp(pet.getMaxHP());
					qct_st.setPetPhysicalDamage(pet.getPhyAttack());
					qct_st.setPetMagicDamage(pet.getMagicAttack());
					qct_st.setPetPhysicalDefence(pet.getPhyDefence());
					qct_st.setPetMagicDefence(pet.getMagicDefence());
					qct_st.setPetSkills(pet.getSkillIds());
				}else {
					qct_st.setPetName("");
					qct_st.setPetLevel(0);
					qct_st.setPetMaxHp(0);
					qct_st.setPetPhysicalDamage(0);
					qct_st.setPetMagicDamage(0);
					qct_st.setPetPhysicalDefence(0);
					qct_st.setPetMagicDefence(0);
					qct_st.setPetSkills(new int[]{});
				}
			}else {
				qct_st.setPetName("");
				qct_st.setPetLevel(0);
				qct_st.setPetMaxHp(0);
				qct_st.setPetPhysicalDamage(0);
				qct_st.setPetMagicDamage(0);
				qct_st.setPetPhysicalDefence(0);
				qct_st.setPetMagicDefence(0);
				qct_st.setPetSkills(new int[]{});
			}
			synchronized (statistics) {
				if (statistics.size() > QCT_STATISTICS_NUM) {
					statistics.remove(0);
				}
				statistics.add(qct_st);
			}
		}catch(Exception e) {
			QianCengTaManager.getInstance().logger.error("creatNewStatistics", e);
		}
	}

	private String passTime;					//通过时间
	private int daoIndex;						//道
	private int cengIndex;						//层
	private long playerID;						//玩家ID
	private String playerName;					//玩家名字
	private int playerLevel;					//等级
	private int vipLevel;						//vip等级
	private int maxHp;							//最大血量
	private int physicalDamage;					//外功伤害
	private int magicDamage;					//内法伤害
	private int physicalDefence;				//物理防御
	private int magicDefence;					//法术防御
	private String petName;						//宠物名字
	private int petLevel;						//宠物级别
	private int petMaxHp;						//宠物最大血量
	private int petPhysicalDamage;				//宠物 外功
	private int petMagicDamage;					//宠物 内法
	private int petPhysicalDefence;				//宠物 外防
	private int petMagicDefence;				//宠物 内防
	private int[] petSkills;					//宠物 技能id
	
	public void setPlayerID(long playerID) {
		this.playerID = playerID;
	}
	public long getPlayerID() {
		return playerID;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}
	public int getPlayerLevel() {
		return playerLevel;
	}
	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	public int getVipLevel() {
		return vipLevel;
	}
	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}
	public int getMaxHp() {
		return maxHp;
	}
	public void setMagicDamage(int magicDamage) {
		this.magicDamage = magicDamage;
	}
	public int getMagicDamage() {
		return magicDamage;
	}
	public void setPhysicalDamage(int physicalDamage) {
		this.physicalDamage = physicalDamage;
	}
	public int getPhysicalDamage() {
		return physicalDamage;
	}
	public void setPhysicalDefence(int physicalDefence) {
		this.physicalDefence = physicalDefence;
	}
	public int getPhysicalDefence() {
		return physicalDefence;
	}
	public void setMagicDefence(int magicDefence) {
		this.magicDefence = magicDefence;
	}
	public int getMagicDefence() {
		return magicDefence;
	}
	public void setPetName(String petName) {
		this.petName = petName;
	}
	public String getPetName() {
		return petName;
	}
	public void setPetLevel(int petLevel) {
		this.petLevel = petLevel;
	}
	public int getPetLevel() {
		return petLevel;
	}
	public void setPetMaxHp(int petMaxHp) {
		this.petMaxHp = petMaxHp;
	}
	public int getPetMaxHp() {
		return petMaxHp;
	}
	public void setPetPhysicalDamage(int petPhysicalDamage) {
		this.petPhysicalDamage = petPhysicalDamage;
	}
	public int getPetPhysicalDamage() {
		return petPhysicalDamage;
	}
	public void setPetMagicDamage(int petMagicDamage) {
		this.petMagicDamage = petMagicDamage;
	}
	public int getPetMagicDamage() {
		return petMagicDamage;
	}
	public void setPetPhysicalDefence(int petPhysicalDefence) {
		this.petPhysicalDefence = petPhysicalDefence;
	}
	public int getPetPhysicalDefence() {
		return petPhysicalDefence;
	}
	public void setPetMagicDefence(int petMagicDefence) {
		this.petMagicDefence = petMagicDefence;
	}
	public int getPetMagicDefence() {
		return petMagicDefence;
	}
	public void setPetSkills(int[] petSkills) {
		this.petSkills = petSkills;
	}
	public int[] getPetSkills() {
		return petSkills;
	}
	public void setPassTime(String passTime) {
		this.passTime = passTime;
	}
	public String getPassTime() {
		return passTime;
	}
	public void setDaoIndex(int daoIndex) {
		this.daoIndex = daoIndex;
	}
	public int getDaoIndex() {
		return daoIndex;
	}
	public void setCengIndex(int cengIndex) {
		this.cengIndex = cengIndex;
	}
	public int getCengIndex() {
		return cengIndex;
	}
}

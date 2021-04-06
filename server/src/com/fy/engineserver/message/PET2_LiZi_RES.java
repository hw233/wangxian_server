package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.datasource.career.SkillInfo;
import com.fy.engineserver.sprite.pet.PetSkillReleaseProbability;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求宠物粒子信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.name</td><td>String</td><td>pet.name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.ownerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.category.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.category</td><td>String</td><td>pet.category.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.petSort.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.petSort</td><td>String</td><td>pet.petSort.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.character</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.breedTimes</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.breededTimes</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.identity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.maxHappyness</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.maxLifeTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.lifeTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.happyness</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.exp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.nextLevelExp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.quality</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.starClass</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.generation</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.variation</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.showStrengthQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.showDexterityQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.showSpellpowerQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.showConstitutionQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.showDingliQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.showMinStrengthQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.showMinDexterityQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.showMinSpellpowerQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.showMinConstitutionQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.showMinDingliQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.showMaxStrengthQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.showMaxDexterityQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.showMaxSpellpowerQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.showMaxConstitutionQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.showMaxDingliQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.trainLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.rarity</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.growthClass</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.growth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.skillIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.skillIds</td><td>int[]</td><td>pet.skillIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.activeSkillLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.activeSkillLevels</td><td>int[]</td><td>pet.activeSkillLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.petPropsId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.strength</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.dexterity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.spellpower</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.constitution</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.dingli</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.showPhyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.showMagicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.hitRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.dodgeRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.criticalHitRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.physicalDecrease</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.spellDecrease</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.unAllocatedPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.activationType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.avataRace</td><td>String</td><td>pet.avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.avataSex</td><td>String</td><td>pet.avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.avata[0]</td><td>String</td><td>pet.avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.avata[1]</td><td>String</td><td>pet.avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.avata[2]</td><td>String</td><td>pet.avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.avataType</td><td>byte[]</td><td>pet.avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.commonSkillId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.qualityScore</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.objectScale</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.particleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.particleName</td><td>String</td><td>pet.particleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.particleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.footParticleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>pet.footParticleName</td><td>String</td><td>pet.footParticleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pet.footParticleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos.length</td><td>int</td><td>4个字节</td><td>SkillInfo数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[0].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[0].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[0].indexOfCareerThread</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[0].needCareerThreadPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[0].name</td><td>String</td><td>skillInfos[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[0].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[0].iconId</td><td>String</td><td>skillInfos[0].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[0].columnIndex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[1].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[1].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[1].indexOfCareerThread</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[1].needCareerThreadPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[1].name</td><td>String</td><td>skillInfos[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[1].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[1].iconId</td><td>String</td><td>skillInfos[1].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[1].columnIndex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[2].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[2].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[2].indexOfCareerThread</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[2].needCareerThreadPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[2].name</td><td>String</td><td>skillInfos[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[2].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[2].iconId</td><td>String</td><td>skillInfos[2].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[2].columnIndex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petSkillReleaseProbability.length</td><td>int</td><td>4个字节</td><td>PetSkillReleaseProbability数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petSkillReleaseProbability[0].skillId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petSkillReleaseProbability[0].character</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petSkillReleaseProbability[0].match</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petSkillReleaseProbability[0].noMatch</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petSkillReleaseProbability[1].skillId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petSkillReleaseProbability[1].character</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petSkillReleaseProbability[1].match</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petSkillReleaseProbability[1].noMatch</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petSkillReleaseProbability[2].skillId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petSkillReleaseProbability[2].character</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petSkillReleaseProbability[2].match</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petSkillReleaseProbability[2].noMatch</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description[0]</td><td>String</td><td>description[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description[1]</td><td>String</td><td>description[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description[2]</td><td>String</td><td>description[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornSkills.length</td><td>int</td><td>4个字节</td><td>SkillInfo数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornSkills[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornSkills[0].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornSkills[0].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornSkills[0].indexOfCareerThread</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornSkills[0].needCareerThreadPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornSkills[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornSkills[0].name</td><td>String</td><td>bornSkills[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornSkills[0].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornSkills[0].iconId</td><td>String</td><td>bornSkills[0].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornSkills[0].columnIndex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornSkills[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornSkills[1].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornSkills[1].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornSkills[1].indexOfCareerThread</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornSkills[1].needCareerThreadPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornSkills[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornSkills[1].name</td><td>String</td><td>bornSkills[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornSkills[1].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornSkills[1].iconId</td><td>String</td><td>bornSkills[1].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornSkills[1].columnIndex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornSkills[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornSkills[2].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornSkills[2].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornSkills[2].indexOfCareerThread</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornSkills[2].needCareerThreadPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornSkills[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornSkills[2].name</td><td>String</td><td>bornSkills[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornSkills[2].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bornSkills[2].iconId</td><td>String</td><td>bornSkills[2].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bornSkills[2].columnIndex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentSkills.length</td><td>int</td><td>4个字节</td><td>SkillInfo数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentSkills[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentSkills[0].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentSkills[0].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentSkills[0].indexOfCareerThread</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentSkills[0].needCareerThreadPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentSkills[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentSkills[0].name</td><td>String</td><td>talentSkills[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentSkills[0].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentSkills[0].iconId</td><td>String</td><td>talentSkills[0].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentSkills[0].columnIndex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentSkills[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentSkills[1].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentSkills[1].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentSkills[1].indexOfCareerThread</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentSkills[1].needCareerThreadPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentSkills[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentSkills[1].name</td><td>String</td><td>talentSkills[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentSkills[1].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentSkills[1].iconId</td><td>String</td><td>talentSkills[1].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentSkills[1].columnIndex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentSkills[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentSkills[2].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentSkills[2].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentSkills[2].indexOfCareerThread</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentSkills[2].needCareerThreadPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentSkills[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentSkills[2].name</td><td>String</td><td>talentSkills[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentSkills[2].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>talentSkills[2].iconId</td><td>String</td><td>talentSkills[2].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>talentSkills[2].columnIndex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>grade</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>wuXing</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>addBooks</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rankIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hunExp</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>toNextHunExp</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jinJieAtt.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jinJieAtt</td><td>int[]</td><td>jinJieAtt.length</td><td>*</td></tr>
 * </table>
 */
public class PET2_LiZi_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	Pet pet;
	SkillInfo[] skillInfos;
	PetSkillReleaseProbability[] petSkillReleaseProbability;
	String[] description;
	SkillInfo[] bornSkills;
	SkillInfo[] talentSkills;
	int grade;
	int wuXing;
	int addBooks;
	int rankIndex;
	int hunExp;
	int toNextHunExp;
	int[] jinJieAtt;

	public PET2_LiZi_RES(){
	}

	public PET2_LiZi_RES(long seqNum,Pet pet,SkillInfo[] skillInfos,PetSkillReleaseProbability[] petSkillReleaseProbability,String[] description,SkillInfo[] bornSkills,SkillInfo[] talentSkills,int grade,int wuXing,int addBooks,int rankIndex,int hunExp,int toNextHunExp,int[] jinJieAtt){
		this.seqNum = seqNum;
		this.pet = pet;
		this.skillInfos = skillInfos;
		this.petSkillReleaseProbability = petSkillReleaseProbability;
		this.description = description;
		this.bornSkills = bornSkills;
		this.talentSkills = talentSkills;
		this.grade = grade;
		this.wuXing = wuXing;
		this.addBooks = addBooks;
		this.rankIndex = rankIndex;
		this.hunExp = hunExp;
		this.toNextHunExp = toNextHunExp;
		this.jinJieAtt = jinJieAtt;
	}

	public PET2_LiZi_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		pet = new Pet();
		pet.setId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		pet.setName(new String(content,offset,len,"UTF-8"));
		offset += len;
		pet.setOwnerId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		pet.setCategory(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		pet.setPetSort(new String(content,offset,len,"UTF-8"));
		offset += len;
		pet.setCareer((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		pet.setCharacter((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		pet.setSex((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		pet.setBreedTimes((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		pet.setBreededTimes((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		pet.setIdentity(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		pet.setMaxHappyness((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setMaxLifeTime((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setLifeTime((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setHappyness((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setLevel((short)mf.byteArrayToNumber(content,offset,2));
		offset += 2;
		pet.setExp((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setNextLevelExp((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setQuality((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		pet.setStarClass((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		pet.setGeneration((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		pet.setVariation((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		pet.setShowStrengthQuality((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowDexterityQuality((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowSpellpowerQuality((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowConstitutionQuality((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowDingliQuality((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowMinStrengthQuality((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowMinDexterityQuality((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowMinSpellpowerQuality((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowMinConstitutionQuality((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowMinDingliQuality((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowMaxStrengthQuality((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowMaxDexterityQuality((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowMaxSpellpowerQuality((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowMaxConstitutionQuality((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowMaxDingliQuality((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setTrainLevel((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setRarity((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		pet.setGrowthClass((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		pet.setGrowth((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] skillIds_0001 = new int[len];
		for(int j = 0 ; j < skillIds_0001.length ; j++){
			skillIds_0001[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		pet.setSkillIds(skillIds_0001);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] activeSkillLevels_0002 = new int[len];
		for(int j = 0 ; j < activeSkillLevels_0002.length ; j++){
			activeSkillLevels_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		pet.setActiveSkillLevels(activeSkillLevels_0002);
		pet.setPetPropsId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		pet.setStrength((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setDexterity((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setSpellpower((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setConstitution((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setDingli((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowPhyAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setPhyDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setShowMagicAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setMagicDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setMaxHP((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setHp((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setCriticalHit((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setHit((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setDodge((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setHitRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setDodgeRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setCriticalHitRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setPhysicalDecrease((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setSpellDecrease((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setFireDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setWindDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setBlizzardDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setThunderDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setUnAllocatedPoints((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setActivationType((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		pet.setAvataRace(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		pet.setAvataSex(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		String[] avata_0003 = new String[len];
		for(int j = 0 ; j < avata_0003.length ; j++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			avata_0003[j] = new String(content,offset,len,"UTF-8");
				offset += len;
		}
		pet.setAvata(avata_0003);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		byte[] avataType_0004 = new byte[len];
		System.arraycopy(content,offset,avataType_0004,0,len);
		offset += len;
		pet.setAvataType(avataType_0004);
		pet.setCommonSkillId((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setQualityScore((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		pet.setObjectScale((short)mf.byteArrayToNumber(content,offset,2));
		offset += 2;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		pet.setParticleName(new String(content,offset,len,"UTF-8"));
		offset += len;
		pet.setParticleY((short)mf.byteArrayToNumber(content,offset,2));
		offset += 2;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		pet.setFootParticleName(new String(content,offset,len,"UTF-8"));
		offset += len;
		pet.setFootParticleY((short)mf.byteArrayToNumber(content,offset,2));
		offset += 2;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		skillInfos = new SkillInfo[len];
		for(int i = 0 ; i < skillInfos.length ; i++){
			skillInfos[i] = new SkillInfo();
			skillInfos[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillInfos[i].setSkillType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillInfos[i].setMaxLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillInfos[i].setIndexOfCareerThread((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillInfos[i].setNeedCareerThreadPoints((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillInfos[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillInfos[i].setIconId(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillInfos[i].setColumnIndex((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		petSkillReleaseProbability = new PetSkillReleaseProbability[len];
		for(int i = 0 ; i < petSkillReleaseProbability.length ; i++){
			petSkillReleaseProbability[i] = new PetSkillReleaseProbability();
			petSkillReleaseProbability[i].setSkillId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			petSkillReleaseProbability[i].setCharacter((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			petSkillReleaseProbability[i].setMatch((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			petSkillReleaseProbability[i].setNoMatch((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		description = new String[len];
		for(int i = 0 ; i < description.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			description[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		bornSkills = new SkillInfo[len];
		for(int i = 0 ; i < bornSkills.length ; i++){
			bornSkills[i] = new SkillInfo();
			bornSkills[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			bornSkills[i].setSkillType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			bornSkills[i].setMaxLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			bornSkills[i].setIndexOfCareerThread((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			bornSkills[i].setNeedCareerThreadPoints((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			bornSkills[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			bornSkills[i].setIconId(new String(content,offset,len,"UTF-8"));
			offset += len;
			bornSkills[i].setColumnIndex((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		talentSkills = new SkillInfo[len];
		for(int i = 0 ; i < talentSkills.length ; i++){
			talentSkills[i] = new SkillInfo();
			talentSkills[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			talentSkills[i].setSkillType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			talentSkills[i].setMaxLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			talentSkills[i].setIndexOfCareerThread((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			talentSkills[i].setNeedCareerThreadPoints((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			talentSkills[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			talentSkills[i].setIconId(new String(content,offset,len,"UTF-8"));
			offset += len;
			talentSkills[i].setColumnIndex((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
		}
		grade = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		wuXing = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		addBooks = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		rankIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		hunExp = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		toNextHunExp = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		jinJieAtt = new int[len];
		for(int i = 0 ; i < jinJieAtt.length ; i++){
			jinJieAtt[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x8E0EAA7F;
	}

	public String getTypeDescription() {
		return "PET2_LiZi_RES";
	}

	public String getSequenceNumAsString() {
		return String.valueOf(seqNum);
	}

	public long getSequnceNum(){
		return seqNum;
	}

	private int packet_length = 0;

	public int getLength() {
		if(packet_length > 0) return packet_length;
		int len =  mf.getNumOfByteForMessageLength() + 4 + 4;
		len += 8;
		len += 2;
		if(pet.getName() != null){
			try{
			len += pet.getName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		len += 2;
		if(pet.getCategory() != null){
			try{
			len += pet.getCategory().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(pet.getPetSort() != null){
			try{
			len += pet.getPetSort().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 1;
		len += 1;
		len += 1;
		len += 1;
		len += 1;
		len += 1;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 2;
		len += 4;
		len += 4;
		len += 1;
		len += 1;
		len += 1;
		len += 1;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 1;
		len += 1;
		len += 4;
		len += 4;
		len += pet.getSkillIds().length * 4;
		len += 4;
		len += pet.getActiveSkillLevels().length * 4;
		len += 8;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 1;
		len += 2;
		if(pet.getAvataRace() != null){
			try{
			len += pet.getAvataRace().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(pet.getAvataSex() != null){
			try{
			len += pet.getAvataSex().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		String[] avata = pet.getAvata();
		for(int j = 0 ; j < avata.length; j++){
			len += 2;
			try{
				len += avata[j].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += pet.getAvataType().length * 1;
		len += 4;
		len += 4;
		len += 2;
		len += 2;
		if(pet.getParticleName() != null){
			try{
			len += pet.getParticleName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		len += 2;
		if(pet.getFootParticleName() != null){
			try{
			len += pet.getFootParticleName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		len += 4;
		for(int i = 0 ; i < skillInfos.length ; i++){
			len += 4;
			len += 1;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(skillInfos[i].getName() != null){
				try{
				len += skillInfos[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillInfos[i].getIconId() != null){
				try{
				len += skillInfos[i].getIconId().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
		}
		len += 4;
		for(int i = 0 ; i < petSkillReleaseProbability.length ; i++){
			len += 4;
			len += 4;
			len += 4;
			len += 4;
		}
		len += 4;
		for(int i = 0 ; i < description.length; i++){
			len += 2;
			try{
				len += description[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < bornSkills.length ; i++){
			len += 4;
			len += 1;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(bornSkills[i].getName() != null){
				try{
				len += bornSkills[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(bornSkills[i].getIconId() != null){
				try{
				len += bornSkills[i].getIconId().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
		}
		len += 4;
		for(int i = 0 ; i < talentSkills.length ; i++){
			len += 4;
			len += 1;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(talentSkills[i].getName() != null){
				try{
				len += talentSkills[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(talentSkills[i].getIconId() != null){
				try{
				len += talentSkills[i].getIconId().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
		}
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += jinJieAtt.length * 4;
		packet_length = len;
		return len;
	}

	public int writeTo(ByteBuffer buffer) {
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		int oldPos = buffer.position();
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.putLong(pet.getId());
			byte[] tmpBytes1 ;
				try{
				tmpBytes1 = pet.getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(pet.getOwnerId());
				try{
				tmpBytes1 = pet.getCategory().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = pet.getPetSort().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put((byte)pet.getCareer());
			buffer.put((byte)pet.getCharacter());
			buffer.put((byte)pet.getSex());
			buffer.put((byte)pet.getBreedTimes());
			buffer.put((byte)pet.getBreededTimes());
			buffer.put((byte)(pet.isIdentity()==false?0:1));
			buffer.putInt((int)pet.getMaxHappyness());
			buffer.putInt((int)pet.getMaxLifeTime());
			buffer.putInt((int)pet.getLifeTime());
			buffer.putInt((int)pet.getHappyness());
			buffer.putShort((short)pet.getLevel());
			buffer.putInt((int)pet.getExp());
			buffer.putInt((int)pet.getNextLevelExp());
			buffer.put((byte)pet.getQuality());
			buffer.put((byte)pet.getStarClass());
			buffer.put((byte)pet.getGeneration());
			buffer.put((byte)pet.getVariation());
			buffer.putInt((int)pet.getShowStrengthQuality());
			buffer.putInt((int)pet.getShowDexterityQuality());
			buffer.putInt((int)pet.getShowSpellpowerQuality());
			buffer.putInt((int)pet.getShowConstitutionQuality());
			buffer.putInt((int)pet.getShowDingliQuality());
			buffer.putInt((int)pet.getShowMinStrengthQuality());
			buffer.putInt((int)pet.getShowMinDexterityQuality());
			buffer.putInt((int)pet.getShowMinSpellpowerQuality());
			buffer.putInt((int)pet.getShowMinConstitutionQuality());
			buffer.putInt((int)pet.getShowMinDingliQuality());
			buffer.putInt((int)pet.getShowMaxStrengthQuality());
			buffer.putInt((int)pet.getShowMaxDexterityQuality());
			buffer.putInt((int)pet.getShowMaxSpellpowerQuality());
			buffer.putInt((int)pet.getShowMaxConstitutionQuality());
			buffer.putInt((int)pet.getShowMaxDingliQuality());
			buffer.putInt((int)pet.getTrainLevel());
			buffer.put((byte)pet.getRarity());
			buffer.put((byte)pet.getGrowthClass());
			buffer.putInt((int)pet.getGrowth());
			buffer.putInt(pet.getSkillIds().length);
			int[] skillIds_0005 = pet.getSkillIds();
			for(int j = 0 ; j < skillIds_0005.length ; j++){
				buffer.putInt(skillIds_0005[j]);
			}
			buffer.putInt(pet.getActiveSkillLevels().length);
			int[] activeSkillLevels_0006 = pet.getActiveSkillLevels();
			for(int j = 0 ; j < activeSkillLevels_0006.length ; j++){
				buffer.putInt(activeSkillLevels_0006[j]);
			}
			buffer.putLong(pet.getPetPropsId());
			buffer.putInt((int)pet.getStrength());
			buffer.putInt((int)pet.getDexterity());
			buffer.putInt((int)pet.getSpellpower());
			buffer.putInt((int)pet.getConstitution());
			buffer.putInt((int)pet.getDingli());
			buffer.putInt((int)pet.getShowPhyAttack());
			buffer.putInt((int)pet.getPhyDefence());
			buffer.putInt((int)pet.getShowMagicAttack());
			buffer.putInt((int)pet.getMagicDefence());
			buffer.putInt((int)pet.getMaxHP());
			buffer.putInt((int)pet.getHp());
			buffer.putInt((int)pet.getCriticalHit());
			buffer.putInt((int)pet.getHit());
			buffer.putInt((int)pet.getDodge());
			buffer.putInt((int)pet.getHitRate());
			buffer.putInt((int)pet.getDodgeRate());
			buffer.putInt((int)pet.getCriticalHitRate());
			buffer.putInt((int)pet.getPhysicalDecrease());
			buffer.putInt((int)pet.getSpellDecrease());
			buffer.putInt((int)pet.getFireDefence());
			buffer.putInt((int)pet.getWindDefence());
			buffer.putInt((int)pet.getBlizzardDefence());
			buffer.putInt((int)pet.getThunderDefence());
			buffer.putInt((int)pet.getUnAllocatedPoints());
			buffer.put((byte)pet.getActivationType());
				try{
				tmpBytes1 = pet.getAvataRace().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = pet.getAvataSex().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(pet.getAvata().length);
			String[] avata_0007 = pet.getAvata();
			for(int j = 0 ; j < avata_0007.length ; j++){
				try{
				buffer.putShort((short)avata_0007[j].getBytes("UTF-8").length);
				buffer.put(avata_0007[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			buffer.putInt(pet.getAvataType().length);
			buffer.put(pet.getAvataType());
			buffer.putInt((int)pet.getCommonSkillId());
			buffer.putInt((int)pet.getQualityScore());
			buffer.putShort((short)pet.getObjectScale());
				try{
				tmpBytes1 = pet.getParticleName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putShort((short)pet.getParticleY());
				try{
				tmpBytes1 = pet.getFootParticleName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putShort((short)pet.getFootParticleY());
			buffer.putInt(skillInfos.length);
			for(int i = 0 ; i < skillInfos.length ; i++){
				buffer.putInt((int)skillInfos[i].getId());
				buffer.put((byte)skillInfos[i].getSkillType());
				buffer.putInt((int)skillInfos[i].getMaxLevel());
				buffer.putInt((int)skillInfos[i].getIndexOfCareerThread());
				buffer.putInt((int)skillInfos[i].getNeedCareerThreadPoints());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = skillInfos[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillInfos[i].getIconId().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)skillInfos[i].getColumnIndex());
			}
			buffer.putInt(petSkillReleaseProbability.length);
			for(int i = 0 ; i < petSkillReleaseProbability.length ; i++){
				buffer.putInt((int)petSkillReleaseProbability[i].getSkillId());
				buffer.putInt((int)petSkillReleaseProbability[i].getCharacter());
				buffer.putInt((int)petSkillReleaseProbability[i].getMatch());
				buffer.putInt((int)petSkillReleaseProbability[i].getNoMatch());
			}
			buffer.putInt(description.length);
			for(int i = 0 ; i < description.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = description[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(bornSkills.length);
			for(int i = 0 ; i < bornSkills.length ; i++){
				buffer.putInt((int)bornSkills[i].getId());
				buffer.put((byte)bornSkills[i].getSkillType());
				buffer.putInt((int)bornSkills[i].getMaxLevel());
				buffer.putInt((int)bornSkills[i].getIndexOfCareerThread());
				buffer.putInt((int)bornSkills[i].getNeedCareerThreadPoints());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = bornSkills[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = bornSkills[i].getIconId().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)bornSkills[i].getColumnIndex());
			}
			buffer.putInt(talentSkills.length);
			for(int i = 0 ; i < talentSkills.length ; i++){
				buffer.putInt((int)talentSkills[i].getId());
				buffer.put((byte)talentSkills[i].getSkillType());
				buffer.putInt((int)talentSkills[i].getMaxLevel());
				buffer.putInt((int)talentSkills[i].getIndexOfCareerThread());
				buffer.putInt((int)talentSkills[i].getNeedCareerThreadPoints());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = talentSkills[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = talentSkills[i].getIconId().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)talentSkills[i].getColumnIndex());
			}
			buffer.putInt(grade);
			buffer.putInt(wuXing);
			buffer.putInt(addBooks);
			buffer.putInt(rankIndex);
			buffer.putInt(hunExp);
			buffer.putInt(toNextHunExp);
			buffer.putInt(jinJieAtt.length);
			for(int i = 0 ; i < jinJieAtt.length; i++){
				buffer.putInt(jinJieAtt[i]);
			}
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		int newPos = buffer.position();
		buffer.position(oldPos);
		buffer.put(mf.numberToByteArray(newPos-oldPos,mf.getNumOfByteForMessageLength()));
		buffer.position(newPos);
		return newPos-oldPos;
	}

	/**
	 * 获取属性：
	 *	返回宠物实体
	 */
	public Pet getPet(){
		return pet;
	}

	/**
	 * 设置属性：
	 *	返回宠物实体
	 */
	public void setPet(Pet pet){
		this.pet = pet;
	}

	/**
	 * 获取属性：
	 *	返回宠物技能
	 */
	public SkillInfo[] getSkillInfos(){
		return skillInfos;
	}

	/**
	 * 设置属性：
	 *	返回宠物技能
	 */
	public void setSkillInfos(SkillInfo[] skillInfos){
		this.skillInfos = skillInfos;
	}

	/**
	 * 获取属性：
	 *	返回宠物技能
	 */
	public PetSkillReleaseProbability[] getPetSkillReleaseProbability(){
		return petSkillReleaseProbability;
	}

	/**
	 * 设置属性：
	 *	返回宠物技能
	 */
	public void setPetSkillReleaseProbability(PetSkillReleaseProbability[] petSkillReleaseProbability){
		this.petSkillReleaseProbability = petSkillReleaseProbability;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String[] getDescription(){
		return description;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setDescription(String[] description){
		this.description = description;
	}

	/**
	 * 获取属性：
	 *	天生技能两个
	 */
	public SkillInfo[] getBornSkills(){
		return bornSkills;
	}

	/**
	 * 设置属性：
	 *	天生技能两个
	 */
	public void setBornSkills(SkillInfo[] bornSkills){
		this.bornSkills = bornSkills;
	}

	/**
	 * 获取属性：
	 *	天赋若干个
	 */
	public SkillInfo[] getTalentSkills(){
		return talentSkills;
	}

	/**
	 * 设置属性：
	 *	天赋若干个
	 */
	public void setTalentSkills(SkillInfo[] talentSkills){
		this.talentSkills = talentSkills;
	}

	/**
	 * 获取属性：
	 *	进阶等级
	 */
	public int getGrade(){
		return grade;
	}

	/**
	 * 设置属性：
	 *	进阶等级
	 */
	public void setGrade(int grade){
		this.grade = grade;
	}

	/**
	 * 获取属性：
	 *	悟性
	 */
	public int getWuXing(){
		return wuXing;
	}

	/**
	 * 设置属性：
	 *	悟性
	 */
	public void setWuXing(int wuXing){
		this.wuXing = wuXing;
	}

	/**
	 * 获取属性：
	 *	已经用过多少技能书
	 */
	public int getAddBooks(){
		return addBooks;
	}

	/**
	 * 设置属性：
	 *	已经用过多少技能书
	 */
	public void setAddBooks(int addBooks){
		this.addBooks = addBooks;
	}

	/**
	 * 获取属性：
	 *	排在多少名，从0开始，-1表示不在榜单。
	 */
	public int getRankIndex(){
		return rankIndex;
	}

	/**
	 * 设置属性：
	 *	排在多少名，从0开始，-1表示不在榜单。
	 */
	public void setRankIndex(int rankIndex){
		this.rankIndex = rankIndex;
	}

	/**
	 * 获取属性：
	 *	当前魂值
	 */
	public int getHunExp(){
		return hunExp;
	}

	/**
	 * 设置属性：
	 *	当前魂值
	 */
	public void setHunExp(int hunExp){
		this.hunExp = hunExp;
	}

	/**
	 * 获取属性：
	 *	升级魂值
	 */
	public int getToNextHunExp(){
		return toNextHunExp;
	}

	/**
	 * 设置属性：
	 *	升级魂值
	 */
	public void setToNextHunExp(int toNextHunExp){
		this.toNextHunExp = toNextHunExp;
	}

	/**
	 * 获取属性：
	 *	进阶获得的属性加成。
	 */
	public int[] getJinJieAtt(){
		return jinJieAtt;
	}

	/**
	 * 设置属性：
	 *	进阶获得的属性加成。
	 */
	public void setJinJieAtt(int[] jinJieAtt){
		this.jinJieAtt = jinJieAtt;
	}

}
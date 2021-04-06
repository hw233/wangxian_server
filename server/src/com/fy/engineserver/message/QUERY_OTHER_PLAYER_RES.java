package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.SoulEquipment4Client;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询其他玩家的信息，主要供界面显示<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.mapName</td><td>String</td><td>players.mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.hold</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.stun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.immunity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.poison</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.weak</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.invulnerable</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.cure</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.shield</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.fighting</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.flying</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.mp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.maxMP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.name</td><td>String</td><td>players.name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.countryPosition</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.soulLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.classLevel</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.totalRmbyuanbao</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.bindSilver</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.silver</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.exp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.nextLevelExp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.nextLevelExpPool</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.weaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.avataRace</td><td>String</td><td>players.avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.avata[0]</td><td>String</td><td>players.avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.avata[1]</td><td>String</td><td>players.avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.avata[2]</td><td>String</td><td>players.avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.avataType</td><td>byte[]</td><td>players.avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.aura</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.encloser</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.isUpOrDown</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.commonAttackSpeed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.commonAttackRange</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.jiazuId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.jiazuName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.jiazuName</td><td>String</td><td>players.jiazuName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.zongPaiName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.zongPaiName</td><td>String</td><td>players.zongPaiName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.title</td><td>String</td><td>players.title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.strength</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.dexterity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.constitution</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.spellpower</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.physicalDamageUpperLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.physicalDamageLowerLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.coolDownTimePercent</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.setupTimePercent</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.vitality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.energy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.xp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.totalXp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.breakDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.breakDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.hitRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.dodgeRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.accurateRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.fireDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.fireIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.fireIgnoreDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.blizzardAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.blizzardDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.blizzardIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.blizzardIgnoreDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.windDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.windIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.windIgnoreDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.thunderDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.thunderIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.thunderIgnoreDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.criticalDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.criticalHitRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.unallocatedSkillPoint</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.skillOneLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.skillOneLevels</td><td>byte[]</td><td>players.skillOneLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.teamMark</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.inBattleField</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.battleFieldSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.avataPropsId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.pkMode</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.nameColorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.careerBasicSkillsLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.careerBasicSkillsLevels</td><td>byte[]</td><td>players.careerBasicSkillsLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.skillOneLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.skillOneLevels</td><td>byte[]</td><td>players.skillOneLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.nuqiSkillsLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.nuqiSkillsLevels</td><td>byte[]</td><td>players.nuqiSkillsLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.xinfaLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.xinfaLevels</td><td>byte[]</td><td>players.xinfaLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.jiazuTitle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.jiazuTitle</td><td>String</td><td>players.jiazuTitle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.jiazuIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.jiazuIcon</td><td>String</td><td>players.jiazuIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.sealState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.bournExp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.spouse.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.spouse</td><td>String</td><td>players.spouse.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.maxHPX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.maxMPX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.phyDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.magicDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.breakDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.hitX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.dodgeX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.accurateX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.phyAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.magicAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.fireAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.fireDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.fireIgnoreDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.blizzardAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.blizzardDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.blizzardIgnoreDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.windAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.windDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.windIgnoreDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.thunderAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.thunderDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.thunderIgnoreDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.criticalHitX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players.criticalDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls.length</td><td>int</td><td>4个字节</td><td>Soul数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].soulType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].maxHp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].maxMp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].strength</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].spellpower</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].constitution</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].dexterity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].breakDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].fireIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].blizzardAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].blizzardIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].windIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].thunderIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].soulType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].maxHp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].maxMp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].strength</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].spellpower</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].constitution</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].dexterity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].breakDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].fireIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].blizzardAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].blizzardIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].windIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].thunderIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].soulType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].maxHp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].maxMp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].strength</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].spellpower</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].constitution</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].dexterity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].breakDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].fireIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].blizzardAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].blizzardIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].windIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].thunderIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soulEquipment4Client.length</td><td>int</td><td>4个字节</td><td>SoulEquipment4Client数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soulEquipment4Client[0].soulTyp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soulEquipment4Client[0].equipment.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soulEquipment4Client[0].equipment</td><td>long[]</td><td>soulEquipment4Client[0].equipment.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soulEquipment4Client[1].soulTyp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soulEquipment4Client[1].equipment.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soulEquipment4Client[1].equipment</td><td>long[]</td><td>soulEquipment4Client[1].equipment.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soulEquipment4Client[2].soulTyp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soulEquipment4Client[2].equipment.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>soulEquipment4Client[2].equipment</td><td>long[]</td><td>soulEquipment4Client[2].equipment.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propertyValues.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propertyValues</td><td>int[]</td><td>propertyValues.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_OTHER_PLAYER_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	Player players;
	Soul[] souls;
	SoulEquipment4Client[] soulEquipment4Client;
	int[] propertyValues;

	public QUERY_OTHER_PLAYER_RES(){
	}

	public QUERY_OTHER_PLAYER_RES(long seqNum,Player players,Soul[] souls,SoulEquipment4Client[] soulEquipment4Client,int[] propertyValues){
		this.seqNum = seqNum;
		this.players = players;
		this.souls = souls;
		this.soulEquipment4Client = soulEquipment4Client;
		this.propertyValues = propertyValues;
	}

	public QUERY_OTHER_PLAYER_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		players = new Player();
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		players.setMapName(new String(content,offset,len,"UTF-8"));
		offset += len;
		players.setHold(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		players.setStun(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		players.setImmunity(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		players.setPoison(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		players.setWeak(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		players.setInvulnerable(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		players.setCure(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		players.setShield((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		players.setFighting(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		players.setFlying(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		players.setHp((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setMaxHP((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setMp((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setMaxMP((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		players.setName(new String(content,offset,len,"UTF-8"));
		offset += len;
		players.setSex((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		players.setCountry((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		players.setCountryPosition((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setCareer((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		players.setLevel((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setSoulLevel((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setClassLevel((short)mf.byteArrayToNumber(content,offset,2));
		offset += 2;
		players.setTotalRmbyuanbao((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		players.setBindSilver((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		players.setSilver((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		players.setExp((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		players.setNextLevelExp((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		players.setNextLevelExpPool((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		players.setWeaponType((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		players.setState((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		players.setDirection((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		players.setAvataRace(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		String[] avata_0001 = new String[len];
		for(int j = 0 ; j < avata_0001.length ; j++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			avata_0001[j] = new String(content,offset,len,"UTF-8");
				offset += len;
		}
		players.setAvata(avata_0001);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		byte[] avataType_0002 = new byte[len];
		System.arraycopy(content,offset,avataType_0002,0,len);
		offset += len;
		players.setAvataType(avataType_0002);
		players.setAura((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		players.setEncloser((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		players.setIsUpOrDown(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		players.setCommonAttackSpeed((short)mf.byteArrayToNumber(content,offset,2));
		offset += 2;
		players.setCommonAttackRange((short)mf.byteArrayToNumber(content,offset,2));
		offset += 2;
		players.setSpeed((short)mf.byteArrayToNumber(content,offset,2));
		offset += 2;
		players.setJiazuId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		players.setJiazuName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		players.setZongPaiName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		players.setTitle(new String(content,offset,len,"UTF-8"));
		offset += len;
		players.setStrength((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setDexterity((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setConstitution((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setSpellpower((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setPhysicalDamageUpperLimit((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setPhysicalDamageLowerLimit((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setPhyDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setMagicDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setDodge((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setPhyAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setMagicAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setCoolDownTimePercent((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setSetupTimePercent((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setCriticalHit((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setVitality((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setEnergy((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setXp((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setTotalXp((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setBreakDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setBreakDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setHit((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setHitRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setDodge((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setDodgeRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setAccurate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setAccurateRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setFireAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setFireDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setFireDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setFireIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setFireIgnoreDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setBlizzardAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setBlizzardDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setBlizzardDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setBlizzardIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setBlizzardIgnoreDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setWindAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setWindDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setWindDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setWindIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setWindIgnoreDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setThunderAttack((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setThunderDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setThunderDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setThunderIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setThunderIgnoreDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setCriticalDefence((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setCriticalDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setCriticalHitRate((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setUnallocatedSkillPoint((short)mf.byteArrayToNumber(content,offset,2));
		offset += 2;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		byte[] skillOneLevels_0003 = new byte[len];
		System.arraycopy(content,offset,skillOneLevels_0003,0,len);
		offset += len;
		players.setSkillOneLevels(skillOneLevels_0003);
		players.setTeamMark((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		players.setX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setY((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setInBattleField(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		players.setBattleFieldSide((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		players.setAvataPropsId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		players.setPkMode((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		players.setNameColorType((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		byte[] careerBasicSkillsLevels_0004 = new byte[len];
		System.arraycopy(content,offset,careerBasicSkillsLevels_0004,0,len);
		offset += len;
		players.setCareerBasicSkillsLevels(careerBasicSkillsLevels_0004);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		byte[] skillOneLevels_0005 = new byte[len];
		System.arraycopy(content,offset,skillOneLevels_0005,0,len);
		offset += len;
		players.setSkillOneLevels(skillOneLevels_0005);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		byte[] nuqiSkillsLevels_0006 = new byte[len];
		System.arraycopy(content,offset,nuqiSkillsLevels_0006,0,len);
		offset += len;
		players.setNuqiSkillsLevels(nuqiSkillsLevels_0006);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		byte[] xinfaLevels_0007 = new byte[len];
		System.arraycopy(content,offset,xinfaLevels_0007,0,len);
		offset += len;
		players.setXinfaLevels(xinfaLevels_0007);
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		players.setJiazuTitle(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		players.setJiazuIcon(new String(content,offset,len,"UTF-8"));
		offset += len;
		players.setSealState(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		players.setBournExp((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		players.setSpouse(new String(content,offset,len,"UTF-8"));
		offset += len;
		players.setMaxHPX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setMaxMPX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setPhyDefenceX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setMagicDefenceX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setBreakDefenceX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setHitX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setDodgeX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setAccurateX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setPhyAttackX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setMagicAttackX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setFireAttackX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setFireDefenceX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setFireIgnoreDefenceX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setBlizzardAttackX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setBlizzardDefenceX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setBlizzardIgnoreDefenceX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setWindAttackX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setWindDefenceX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setWindIgnoreDefenceX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setThunderAttackX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setThunderDefenceX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setThunderIgnoreDefenceX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setCriticalHitX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		players.setCriticalDefenceX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		souls = new Soul[len];
		for(int i = 0 ; i < souls.length ; i++){
			souls[i] = new Soul();
			souls[i].setSoulType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setCareer((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			souls[i].setGrade((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setMaxHp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setMaxMp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setStrength((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setSpellpower((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setConstitution((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setDexterity((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setHit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setDodge((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setAccurate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setPhyAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setMagicAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setCriticalHit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setCriticalDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setPhyDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setMagicDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setBreakDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setFireAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setFireDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setFireIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setBlizzardAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setBlizzardDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setBlizzardIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setWindAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setWindDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setWindIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setThunderAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setThunderDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			souls[i].setThunderIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		soulEquipment4Client = new SoulEquipment4Client[len];
		for(int i = 0 ; i < soulEquipment4Client.length ; i++){
			soulEquipment4Client[i] = new SoulEquipment4Client();
			soulEquipment4Client[i].setSoulTyp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] equipment_0008 = new long[len];
			for(int j = 0 ; j < equipment_0008.length ; j++){
				equipment_0008[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			soulEquipment4Client[i].setEquipment(equipment_0008);
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		propertyValues = new int[len];
		for(int i = 0 ; i < propertyValues.length ; i++){
			propertyValues[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x700000E7;
	}

	public String getTypeDescription() {
		return "QUERY_OTHER_PLAYER_RES";
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
		len += 2;
		if(players.getMapName() != null){
			try{
			len += players.getMapName().getBytes("UTF-8").length;
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
		len += 1;
		len += 1;
		len += 1;
		len += 1;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 8;
		len += 2;
		if(players.getName() != null){
			try{
			len += players.getName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 1;
		len += 1;
		len += 4;
		len += 1;
		len += 4;
		len += 4;
		len += 2;
		len += 8;
		len += 8;
		len += 8;
		len += 8;
		len += 8;
		len += 8;
		len += 1;
		len += 1;
		len += 1;
		len += 2;
		if(players.getAvataRace() != null){
			try{
			len += players.getAvataRace().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		String[] avata = players.getAvata();
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
		len += players.getAvataType().length * 1;
		len += 1;
		len += 1;
		len += 1;
		len += 2;
		len += 2;
		len += 2;
		len += 8;
		len += 2;
		if(players.getJiazuName() != null){
			try{
			len += players.getJiazuName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(players.getZongPaiName() != null){
			try{
			len += players.getZongPaiName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(players.getTitle() != null){
			try{
			len += players.getTitle().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
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
		len += 4;
		len += 2;
		len += 4;
		len += players.getSkillOneLevels().length * 1;
		len += 1;
		len += 4;
		len += 4;
		len += 1;
		len += 1;
		len += 8;
		len += 1;
		len += 1;
		len += 4;
		len += players.getCareerBasicSkillsLevels().length * 1;
		len += 4;
		len += players.getSkillOneLevels().length * 1;
		len += 4;
		len += players.getNuqiSkillsLevels().length * 1;
		len += 4;
		len += players.getXinfaLevels().length * 1;
		len += 2;
		if(players.getJiazuTitle() != null){
			try{
			len += players.getJiazuTitle().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(players.getJiazuIcon() != null){
			try{
			len += players.getJiazuIcon().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 1;
		len += 4;
		len += 2;
		if(players.getSpouse() != null){
			try{
			len += players.getSpouse().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
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
		len += 4;
		for(int i = 0 ; i < souls.length ; i++){
			len += 4;
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
		}
		len += 4;
		for(int i = 0 ; i < soulEquipment4Client.length ; i++){
			len += 4;
			len += 4;
			len += soulEquipment4Client[i].getEquipment().length * 8;
		}
		len += 4;
		len += propertyValues.length * 4;
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

			byte[] tmpBytes1 ;
				try{
				tmpBytes1 = players.getMapName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put((byte)(players.isHold()==false?0:1));
			buffer.put((byte)(players.isStun()==false?0:1));
			buffer.put((byte)(players.isImmunity()==false?0:1));
			buffer.put((byte)(players.isPoison()==false?0:1));
			buffer.put((byte)(players.isWeak()==false?0:1));
			buffer.put((byte)(players.isInvulnerable()==false?0:1));
			buffer.put((byte)(players.isCure()==false?0:1));
			buffer.put((byte)players.getShield());
			buffer.put((byte)(players.isFighting()==false?0:1));
			buffer.put((byte)(players.isFlying()==false?0:1));
			buffer.putInt((int)players.getHp());
			buffer.putInt((int)players.getMaxHP());
			buffer.putInt((int)players.getMp());
			buffer.putInt((int)players.getMaxMP());
			buffer.putLong(players.getId());
				try{
				tmpBytes1 = players.getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put((byte)players.getSex());
			buffer.put((byte)players.getCountry());
			buffer.putInt((int)players.getCountryPosition());
			buffer.put((byte)players.getCareer());
			buffer.putInt((int)players.getLevel());
			buffer.putInt((int)players.getSoulLevel());
			buffer.putShort((short)players.getClassLevel());
			buffer.putLong(players.getTotalRmbyuanbao());
			buffer.putLong(players.getBindSilver());
			buffer.putLong(players.getSilver());
			buffer.putLong(players.getExp());
			buffer.putLong(players.getNextLevelExp());
			buffer.putLong(players.getNextLevelExpPool());
			buffer.put((byte)players.getWeaponType());
			buffer.put((byte)players.getState());
			buffer.put((byte)players.getDirection());
				try{
				tmpBytes1 = players.getAvataRace().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(players.getAvata().length);
			String[] avata_0009 = players.getAvata();
			for(int j = 0 ; j < avata_0009.length ; j++){
				try{
				buffer.putShort((short)avata_0009[j].getBytes("UTF-8").length);
				buffer.put(avata_0009[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			buffer.putInt(players.getAvataType().length);
			buffer.put(players.getAvataType());
			buffer.put((byte)players.getAura());
			buffer.put((byte)players.getEncloser());
			buffer.put((byte)(players.isIsUpOrDown()==false?0:1));
			buffer.putShort((short)players.getCommonAttackSpeed());
			buffer.putShort((short)players.getCommonAttackRange());
			buffer.putShort((short)players.getSpeed());
			buffer.putLong(players.getJiazuId());
				try{
				tmpBytes1 = players.getJiazuName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = players.getZongPaiName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = players.getTitle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt((int)players.getStrength());
			buffer.putInt((int)players.getDexterity());
			buffer.putInt((int)players.getConstitution());
			buffer.putInt((int)players.getSpellpower());
			buffer.putInt((int)players.getPhysicalDamageUpperLimit());
			buffer.putInt((int)players.getPhysicalDamageLowerLimit());
			buffer.putInt((int)players.getPhyDefence());
			buffer.putInt((int)players.getMagicDefence());
			buffer.putInt((int)players.getDodge());
			buffer.putInt((int)players.getPhyAttack());
			buffer.putInt((int)players.getMagicAttack());
			buffer.putInt((int)players.getCoolDownTimePercent());
			buffer.putInt((int)players.getSetupTimePercent());
			buffer.putInt((int)players.getCriticalHit());
			buffer.putInt((int)players.getVitality());
			buffer.putInt((int)players.getEnergy());
			buffer.putInt((int)players.getXp());
			buffer.putInt((int)players.getTotalXp());
			buffer.putInt((int)players.getBreakDefence());
			buffer.putInt((int)players.getBreakDefenceRate());
			buffer.putInt((int)players.getHit());
			buffer.putInt((int)players.getHitRate());
			buffer.putInt((int)players.getDodge());
			buffer.putInt((int)players.getDodgeRate());
			buffer.putInt((int)players.getAccurate());
			buffer.putInt((int)players.getAccurateRate());
			buffer.putInt((int)players.getFireAttack());
			buffer.putInt((int)players.getFireDefence());
			buffer.putInt((int)players.getFireDefenceRate());
			buffer.putInt((int)players.getFireIgnoreDefence());
			buffer.putInt((int)players.getFireIgnoreDefenceRate());
			buffer.putInt((int)players.getBlizzardAttack());
			buffer.putInt((int)players.getBlizzardDefence());
			buffer.putInt((int)players.getBlizzardDefenceRate());
			buffer.putInt((int)players.getBlizzardIgnoreDefence());
			buffer.putInt((int)players.getBlizzardIgnoreDefenceRate());
			buffer.putInt((int)players.getWindAttack());
			buffer.putInt((int)players.getWindDefence());
			buffer.putInt((int)players.getWindDefenceRate());
			buffer.putInt((int)players.getWindIgnoreDefence());
			buffer.putInt((int)players.getWindIgnoreDefenceRate());
			buffer.putInt((int)players.getThunderAttack());
			buffer.putInt((int)players.getThunderDefence());
			buffer.putInt((int)players.getThunderDefenceRate());
			buffer.putInt((int)players.getThunderIgnoreDefence());
			buffer.putInt((int)players.getThunderIgnoreDefenceRate());
			buffer.putInt((int)players.getCriticalDefence());
			buffer.putInt((int)players.getCriticalDefenceRate());
			buffer.putInt((int)players.getCriticalHitRate());
			buffer.putShort((short)players.getUnallocatedSkillPoint());
			buffer.putInt(players.getSkillOneLevels().length);
			buffer.put(players.getSkillOneLevels());
			buffer.put((byte)players.getTeamMark());
			buffer.putInt((int)players.getX());
			buffer.putInt((int)players.getY());
			buffer.put((byte)(players.isInBattleField()==false?0:1));
			buffer.put((byte)players.getBattleFieldSide());
			buffer.putLong(players.getAvataPropsId());
			buffer.put((byte)players.getPkMode());
			buffer.put((byte)players.getNameColorType());
			buffer.putInt(players.getCareerBasicSkillsLevels().length);
			buffer.put(players.getCareerBasicSkillsLevels());
			buffer.putInt(players.getSkillOneLevels().length);
			buffer.put(players.getSkillOneLevels());
			buffer.putInt(players.getNuqiSkillsLevels().length);
			buffer.put(players.getNuqiSkillsLevels());
			buffer.putInt(players.getXinfaLevels().length);
			buffer.put(players.getXinfaLevels());
				try{
				tmpBytes1 = players.getJiazuTitle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = players.getJiazuIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put((byte)(players.isSealState()==false?0:1));
			buffer.putInt((int)players.getBournExp());
				try{
				tmpBytes1 = players.getSpouse().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt((int)players.getMaxHPX());
			buffer.putInt((int)players.getMaxMPX());
			buffer.putInt((int)players.getPhyDefenceX());
			buffer.putInt((int)players.getMagicDefenceX());
			buffer.putInt((int)players.getBreakDefenceX());
			buffer.putInt((int)players.getHitX());
			buffer.putInt((int)players.getDodgeX());
			buffer.putInt((int)players.getAccurateX());
			buffer.putInt((int)players.getPhyAttackX());
			buffer.putInt((int)players.getMagicAttackX());
			buffer.putInt((int)players.getFireAttackX());
			buffer.putInt((int)players.getFireDefenceX());
			buffer.putInt((int)players.getFireIgnoreDefenceX());
			buffer.putInt((int)players.getBlizzardAttackX());
			buffer.putInt((int)players.getBlizzardDefenceX());
			buffer.putInt((int)players.getBlizzardIgnoreDefenceX());
			buffer.putInt((int)players.getWindAttackX());
			buffer.putInt((int)players.getWindDefenceX());
			buffer.putInt((int)players.getWindIgnoreDefenceX());
			buffer.putInt((int)players.getThunderAttackX());
			buffer.putInt((int)players.getThunderDefenceX());
			buffer.putInt((int)players.getThunderIgnoreDefenceX());
			buffer.putInt((int)players.getCriticalHitX());
			buffer.putInt((int)players.getCriticalDefenceX());
			buffer.putInt(souls.length);
			for(int i = 0 ; i < souls.length ; i++){
				buffer.putInt((int)souls[i].getSoulType());
				buffer.put((byte)souls[i].getCareer());
				buffer.putInt((int)souls[i].getGrade());
				buffer.putInt((int)souls[i].getMaxHp());
				buffer.putInt((int)souls[i].getMaxMp());
				buffer.putInt((int)souls[i].getStrength());
				buffer.putInt((int)souls[i].getSpellpower());
				buffer.putInt((int)souls[i].getConstitution());
				buffer.putInt((int)souls[i].getDexterity());
				buffer.putInt((int)souls[i].getHit());
				buffer.putInt((int)souls[i].getDodge());
				buffer.putInt((int)souls[i].getAccurate());
				buffer.putInt((int)souls[i].getPhyAttack());
				buffer.putInt((int)souls[i].getMagicAttack());
				buffer.putInt((int)souls[i].getCriticalHit());
				buffer.putInt((int)souls[i].getCriticalDefence());
				buffer.putInt((int)souls[i].getPhyDefence());
				buffer.putInt((int)souls[i].getMagicDefence());
				buffer.putInt((int)souls[i].getBreakDefence());
				buffer.putInt((int)souls[i].getFireAttack());
				buffer.putInt((int)souls[i].getFireDefence());
				buffer.putInt((int)souls[i].getFireIgnoreDefence());
				buffer.putInt((int)souls[i].getBlizzardAttack());
				buffer.putInt((int)souls[i].getBlizzardDefence());
				buffer.putInt((int)souls[i].getBlizzardIgnoreDefence());
				buffer.putInt((int)souls[i].getWindAttack());
				buffer.putInt((int)souls[i].getWindDefence());
				buffer.putInt((int)souls[i].getWindIgnoreDefence());
				buffer.putInt((int)souls[i].getThunderAttack());
				buffer.putInt((int)souls[i].getThunderDefence());
				buffer.putInt((int)souls[i].getThunderIgnoreDefence());
			}
			buffer.putInt(soulEquipment4Client.length);
			for(int i = 0 ; i < soulEquipment4Client.length ; i++){
				buffer.putInt((int)soulEquipment4Client[i].getSoulTyp());
				buffer.putInt(soulEquipment4Client[i].getEquipment().length);
				long[] equipment_0010 = soulEquipment4Client[i].getEquipment();
				for(int j = 0 ; j < equipment_0010.length ; j++){
					buffer.putLong(equipment_0010[j]);
				}
			}
			buffer.putInt(propertyValues.length);
			for(int i = 0 ; i < propertyValues.length; i++){
				buffer.putInt(propertyValues[i]);
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
	 *	角色
	 */
	public Player getPlayers(){
		return players;
	}

	/**
	 * 设置属性：
	 *	角色
	 */
	public void setPlayers(Player players){
		this.players = players;
	}

	/**
	 * 获取属性：
	 *	角色元神信息
	 */
	public Soul[] getSouls(){
		return souls;
	}

	/**
	 * 设置属性：
	 *	角色元神信息
	 */
	public void setSouls(Soul[] souls){
		this.souls = souls;
	}

	/**
	 * 获取属性：
	 *	玩家元神的装备信息
	 */
	public SoulEquipment4Client[] getSoulEquipment4Client(){
		return soulEquipment4Client;
	}

	/**
	 * 设置属性：
	 *	玩家元神的装备信息
	 */
	public void setSoulEquipment4Client(SoulEquipment4Client[] soulEquipment4Client){
		this.soulEquipment4Client = soulEquipment4Client;
	}

	/**
	 * 获取属性：
	 *	各个属性的最大值,顺序MHP,AP,AP2,AC,AC2,MMP,DAC,HITP,DGP,ACTP,CHP,DCHP,FAP,IAP,WAP,TAP,FRT,IRT,WRT,TRT,DFRT,DIRT,DWRT,DTRT
	 */
	public int[] getPropertyValues(){
		return propertyValues;
	}

	/**
	 * 设置属性：
	 *	各个属性的最大值,顺序MHP,AP,AP2,AC,AC2,MMP,DAC,HITP,DGP,ACTP,CHP,DCHP,FAP,IAP,WAP,TAP,FRT,IRT,WRT,TRT,DFRT,DIRT,DWRT,DTRT
	 */
	public void setPropertyValues(int[] propertyValues){
		this.propertyValues = propertyValues;
	}

}
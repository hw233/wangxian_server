package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.Player;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端向服务器发送查询角色的请求<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players.length</td><td>int</td><td>4个字节</td><td>Player数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].currentGameCountry</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].mapName</td><td>String</td><td>players[0].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].hold</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].stun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].immunity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].poison</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].weak</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].invulnerable</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].cure</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].liuxueState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].jiansuState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].pojiaState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].zhuoreState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].hanlengState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].tigaoBaojiState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].tigaoWaigongState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].tigaoNeigongState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].tigaoWaifangState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].tigaoNeifangState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].shield</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].fighting</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].flying</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].sitdownState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].mp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].maxMP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].name</td><td>String</td><td>players[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].countryPosition</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].soulLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].classLevel</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].totalRmbyuanbao</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].bindSilver</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].silver</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].exp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].nextLevelExp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].nextLevelExpPool</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].weaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].avataRace</td><td>String</td><td>players[0].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].avata[0]</td><td>String</td><td>players[0].avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].avata[1]</td><td>String</td><td>players[0].avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].avata[2]</td><td>String</td><td>players[0].avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].avataType</td><td>byte[]</td><td>players[0].avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].aura</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].encloser</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].isUpOrDown</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].commonAttackSpeed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].commonAttackRange</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].jiazuId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].jiazuName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].jiazuName</td><td>String</td><td>players[0].jiazuName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].zongPaiName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].zongPaiName</td><td>String</td><td>players[0].zongPaiName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].title</td><td>String</td><td>players[0].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].strength</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].dexterity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].constitution</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].spellpower</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].physicalDamageUpperLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].physicalDamageLowerLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].coolDownTimePercent</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].setupTimePercent</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].vitality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].maxVitality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].energy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].xp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].totalXp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].breakDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].breakDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].hitRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].dodgeRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].accurateRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].fireDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].fireIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].fireIgnoreDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].blizzardAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].blizzardDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].blizzardIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].blizzardIgnoreDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].windDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].windIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].windIgnoreDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].thunderDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].thunderIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].thunderIgnoreDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].criticalDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].criticalHitRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].unallocatedSkillPoint</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].skillOneLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].skillOneLevels</td><td>byte[]</td><td>players[0].skillOneLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].teamMark</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].inBattleField</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].battleFieldSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].avataPropsId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].pkMode</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].nameColorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].careerBasicSkillsLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].careerBasicSkillsLevels</td><td>byte[]</td><td>players[0].careerBasicSkillsLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].skillOneLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].skillOneLevels</td><td>byte[]</td><td>players[0].skillOneLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].nuqiSkillsLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].nuqiSkillsLevels</td><td>byte[]</td><td>players[0].nuqiSkillsLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].xinfaLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].xinfaLevels</td><td>byte[]</td><td>players[0].xinfaLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].jiazuTitle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].jiazuTitle</td><td>String</td><td>players[0].jiazuTitle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].jiazuIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].jiazuIcon</td><td>String</td><td>players[0].jiazuIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].sealState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].bournExp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].weaponParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].weaponParticle</td><td>String</td><td>players[0].weaponParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].horseParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].horseParticle</td><td>String</td><td>players[0].horseParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].suitBodyParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].suitBodyParticle</td><td>String</td><td>players[0].suitBodyParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].suitFootParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].suitFootParticle</td><td>String</td><td>players[0].suitFootParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].jiazuXuanZhanFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].jiazuXuanZhanData.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].jiazuXuanZhanData</td><td>long[]</td><td>players[0].jiazuXuanZhanData.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].citanStateLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].touzhuanStateLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].culture</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].todayUsedBindSilver</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].evil</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].spouse.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].spouse</td><td>String</td><td>players[0].spouse.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].RMB</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].vipLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].cityFightSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].isGuozhan</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].guozhanLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].lilian</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].repairCarry</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].mailCarry</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].wareHouseCarry</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].quickBuyCarry</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].maxHPX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].maxMPX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].phyDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].magicDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].breakDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].hitX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].dodgeX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].accurateX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].phyAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].magicAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].fireAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].fireDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].fireIgnoreDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].blizzardAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].blizzardDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].blizzardIgnoreDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].windAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].windDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].windIgnoreDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].thunderAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].thunderDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].thunderIgnoreDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].criticalHitX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].criticalDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[0].gongxun</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[0].vipPickedRewardLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].currentGameCountry</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].mapName</td><td>String</td><td>players[1].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].hold</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].stun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].immunity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].poison</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].weak</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].invulnerable</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].cure</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].liuxueState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].jiansuState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].pojiaState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].zhuoreState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].hanlengState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].tigaoBaojiState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].tigaoWaigongState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].tigaoNeigongState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].tigaoWaifangState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].tigaoNeifangState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].shield</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].fighting</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].flying</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].sitdownState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].mp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].maxMP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].name</td><td>String</td><td>players[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].countryPosition</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].soulLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].classLevel</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].totalRmbyuanbao</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].bindSilver</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].silver</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].exp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].nextLevelExp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].nextLevelExpPool</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].weaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].avataRace</td><td>String</td><td>players[1].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].avata[0]</td><td>String</td><td>players[1].avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].avata[1]</td><td>String</td><td>players[1].avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].avata[2]</td><td>String</td><td>players[1].avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].avataType</td><td>byte[]</td><td>players[1].avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].aura</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].encloser</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].isUpOrDown</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].commonAttackSpeed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].commonAttackRange</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].jiazuId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].jiazuName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].jiazuName</td><td>String</td><td>players[1].jiazuName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].zongPaiName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].zongPaiName</td><td>String</td><td>players[1].zongPaiName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].title</td><td>String</td><td>players[1].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].strength</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].dexterity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].constitution</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].spellpower</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].physicalDamageUpperLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].physicalDamageLowerLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].coolDownTimePercent</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].setupTimePercent</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].vitality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].maxVitality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].energy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].xp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].totalXp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].breakDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].breakDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].hitRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].dodgeRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].accurateRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].fireDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].fireIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].fireIgnoreDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].blizzardAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].blizzardDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].blizzardIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].blizzardIgnoreDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].windDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].windIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].windIgnoreDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].thunderDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].thunderIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].thunderIgnoreDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].criticalDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].criticalHitRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].unallocatedSkillPoint</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].skillOneLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].skillOneLevels</td><td>byte[]</td><td>players[1].skillOneLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].teamMark</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].inBattleField</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].battleFieldSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].avataPropsId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].pkMode</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].nameColorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].careerBasicSkillsLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].careerBasicSkillsLevels</td><td>byte[]</td><td>players[1].careerBasicSkillsLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].skillOneLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].skillOneLevels</td><td>byte[]</td><td>players[1].skillOneLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].nuqiSkillsLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].nuqiSkillsLevels</td><td>byte[]</td><td>players[1].nuqiSkillsLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].xinfaLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].xinfaLevels</td><td>byte[]</td><td>players[1].xinfaLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].jiazuTitle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].jiazuTitle</td><td>String</td><td>players[1].jiazuTitle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].jiazuIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].jiazuIcon</td><td>String</td><td>players[1].jiazuIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].sealState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].bournExp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].weaponParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].weaponParticle</td><td>String</td><td>players[1].weaponParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].horseParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].horseParticle</td><td>String</td><td>players[1].horseParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].suitBodyParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].suitBodyParticle</td><td>String</td><td>players[1].suitBodyParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].suitFootParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].suitFootParticle</td><td>String</td><td>players[1].suitFootParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].jiazuXuanZhanFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].jiazuXuanZhanData.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].jiazuXuanZhanData</td><td>long[]</td><td>players[1].jiazuXuanZhanData.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].citanStateLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].touzhuanStateLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].culture</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].todayUsedBindSilver</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].evil</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].spouse.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].spouse</td><td>String</td><td>players[1].spouse.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].RMB</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].vipLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].cityFightSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].isGuozhan</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].guozhanLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].lilian</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].repairCarry</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].mailCarry</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].wareHouseCarry</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].quickBuyCarry</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].maxHPX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].maxMPX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].phyDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].magicDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].breakDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].hitX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].dodgeX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].accurateX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].phyAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].magicAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].fireAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].fireDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].fireIgnoreDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].blizzardAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].blizzardDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].blizzardIgnoreDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].windAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].windDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].windIgnoreDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].thunderAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].thunderDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].thunderIgnoreDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].criticalHitX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].criticalDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[1].gongxun</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[1].vipPickedRewardLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].currentGameCountry</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].mapName</td><td>String</td><td>players[2].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].hold</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].stun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].immunity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].poison</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].weak</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].invulnerable</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].cure</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].liuxueState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].jiansuState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].pojiaState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].zhuoreState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].hanlengState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].tigaoBaojiState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].tigaoWaigongState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].tigaoNeigongState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].tigaoWaifangState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].tigaoNeifangState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].shield</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].fighting</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].flying</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].sitdownState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].mp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].maxMP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].name</td><td>String</td><td>players[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].countryPosition</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].soulLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].classLevel</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].totalRmbyuanbao</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].bindSilver</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].silver</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].exp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].nextLevelExp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].nextLevelExpPool</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].weaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].avataRace</td><td>String</td><td>players[2].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].avata[0]</td><td>String</td><td>players[2].avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].avata[1]</td><td>String</td><td>players[2].avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].avata[2]</td><td>String</td><td>players[2].avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].avataType</td><td>byte[]</td><td>players[2].avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].aura</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].encloser</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].isUpOrDown</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].commonAttackSpeed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].commonAttackRange</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].jiazuId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].jiazuName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].jiazuName</td><td>String</td><td>players[2].jiazuName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].zongPaiName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].zongPaiName</td><td>String</td><td>players[2].zongPaiName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].title</td><td>String</td><td>players[2].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].strength</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].dexterity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].constitution</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].spellpower</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].physicalDamageUpperLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].physicalDamageLowerLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].coolDownTimePercent</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].setupTimePercent</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].vitality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].maxVitality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].energy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].xp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].totalXp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].breakDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].breakDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].hitRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].dodgeRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].accurateRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].fireDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].fireIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].fireIgnoreDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].blizzardAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].blizzardDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].blizzardIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].blizzardIgnoreDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].windDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].windIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].windIgnoreDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].thunderDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].thunderIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].thunderIgnoreDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].criticalDefenceRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].criticalHitRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].unallocatedSkillPoint</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].skillOneLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].skillOneLevels</td><td>byte[]</td><td>players[2].skillOneLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].teamMark</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].inBattleField</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].battleFieldSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].avataPropsId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].pkMode</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].nameColorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].careerBasicSkillsLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].careerBasicSkillsLevels</td><td>byte[]</td><td>players[2].careerBasicSkillsLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].skillOneLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].skillOneLevels</td><td>byte[]</td><td>players[2].skillOneLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].nuqiSkillsLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].nuqiSkillsLevels</td><td>byte[]</td><td>players[2].nuqiSkillsLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].xinfaLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].xinfaLevels</td><td>byte[]</td><td>players[2].xinfaLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].jiazuTitle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].jiazuTitle</td><td>String</td><td>players[2].jiazuTitle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].jiazuIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].jiazuIcon</td><td>String</td><td>players[2].jiazuIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].sealState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].bournExp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].weaponParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].weaponParticle</td><td>String</td><td>players[2].weaponParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].horseParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].horseParticle</td><td>String</td><td>players[2].horseParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].suitBodyParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].suitBodyParticle</td><td>String</td><td>players[2].suitBodyParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].suitFootParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].suitFootParticle</td><td>String</td><td>players[2].suitFootParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].jiazuXuanZhanFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].jiazuXuanZhanData.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].jiazuXuanZhanData</td><td>long[]</td><td>players[2].jiazuXuanZhanData.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].citanStateLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].touzhuanStateLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].culture</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].todayUsedBindSilver</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].evil</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].spouse.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].spouse</td><td>String</td><td>players[2].spouse.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].RMB</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].vipLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].cityFightSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].isGuozhan</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].guozhanLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].lilian</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].repairCarry</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].mailCarry</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].wareHouseCarry</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].quickBuyCarry</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].maxHPX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].maxMPX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].phyDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].magicDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].breakDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].hitX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].dodgeX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].accurateX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].phyAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].magicAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].fireAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].fireDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].fireIgnoreDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].blizzardAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].blizzardDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].blizzardIgnoreDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].windAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].windDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].windIgnoreDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].thunderAttackX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].thunderDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].thunderIgnoreDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].criticalHitX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].criticalDefenceX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>players[2].gongxun</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>players[2].vipPickedRewardLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_PLAYER_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	Player[] players;

	public QUERY_PLAYER_RES(){
	}

	public QUERY_PLAYER_RES(long seqNum,Player[] players){
		this.seqNum = seqNum;
		this.players = players;
	}

	public QUERY_PLAYER_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		players = new Player[len];
		for(int i = 0 ; i < players.length ; i++){
			players[i] = new Player();
			players[i].setCurrentGameCountry((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			players[i].setMapName(new String(content,offset,len,"UTF-8"));
			offset += len;
			players[i].setHold(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setStun(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setImmunity(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setPoison(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setWeak(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setInvulnerable(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setCure(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setLiuxueState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setJiansuState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setPojiaState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setZhuoreState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setHanlengState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setTigaoBaojiState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setTigaoWaigongState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setTigaoNeigongState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setTigaoWaifangState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setTigaoNeifangState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setShield((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			players[i].setFighting(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setFlying(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setSitdownState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setHp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setMaxHP((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setMp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setMaxMP((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			players[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			players[i].setSex((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			players[i].setCountry((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			players[i].setCountryPosition((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setCareer((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			players[i].setLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setSoulLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setClassLevel((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			players[i].setTotalRmbyuanbao((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			players[i].setBindSilver((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			players[i].setSilver((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			players[i].setExp((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			players[i].setNextLevelExp((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			players[i].setNextLevelExpPool((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			players[i].setWeaponType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			players[i].setState((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			players[i].setDirection((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			players[i].setAvataRace(new String(content,offset,len,"UTF-8"));
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
			players[i].setAvata(avata_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] avataType_0002 = new byte[len];
			System.arraycopy(content,offset,avataType_0002,0,len);
			offset += len;
			players[i].setAvataType(avataType_0002);
			players[i].setAura((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			players[i].setEncloser((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			players[i].setIsUpOrDown(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setCommonAttackSpeed((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			players[i].setCommonAttackRange((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			players[i].setSpeed((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			players[i].setJiazuId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			players[i].setJiazuName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			players[i].setZongPaiName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			players[i].setTitle(new String(content,offset,len,"UTF-8"));
			offset += len;
			players[i].setStrength((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setDexterity((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setConstitution((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setSpellpower((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setPhysicalDamageUpperLimit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setPhysicalDamageLowerLimit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setPhyDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setMagicDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setDodge((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setPhyAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setMagicAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setCoolDownTimePercent((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setSetupTimePercent((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setCriticalHit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setVitality((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setMaxVitality((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setEnergy((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setXp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setTotalXp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setBreakDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setBreakDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setHit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setHitRate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setDodge((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setDodgeRate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setAccurate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setAccurateRate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setFireAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setFireDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setFireDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setFireIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setFireIgnoreDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setBlizzardAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setBlizzardDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setBlizzardDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setBlizzardIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setBlizzardIgnoreDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setWindAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setWindDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setWindDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setWindIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setWindIgnoreDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setThunderAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setThunderDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setThunderDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setThunderIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setThunderIgnoreDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setCriticalDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setCriticalDefenceRate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setCriticalHitRate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setUnallocatedSkillPoint((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] skillOneLevels_0003 = new byte[len];
			System.arraycopy(content,offset,skillOneLevels_0003,0,len);
			offset += len;
			players[i].setSkillOneLevels(skillOneLevels_0003);
			players[i].setTeamMark((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			players[i].setX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setY((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setInBattleField(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setBattleFieldSide((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			players[i].setAvataPropsId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			players[i].setPkMode((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			players[i].setNameColorType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] careerBasicSkillsLevels_0004 = new byte[len];
			System.arraycopy(content,offset,careerBasicSkillsLevels_0004,0,len);
			offset += len;
			players[i].setCareerBasicSkillsLevels(careerBasicSkillsLevels_0004);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] skillOneLevels_0005 = new byte[len];
			System.arraycopy(content,offset,skillOneLevels_0005,0,len);
			offset += len;
			players[i].setSkillOneLevels(skillOneLevels_0005);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] nuqiSkillsLevels_0006 = new byte[len];
			System.arraycopy(content,offset,nuqiSkillsLevels_0006,0,len);
			offset += len;
			players[i].setNuqiSkillsLevels(nuqiSkillsLevels_0006);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] xinfaLevels_0007 = new byte[len];
			System.arraycopy(content,offset,xinfaLevels_0007,0,len);
			offset += len;
			players[i].setXinfaLevels(xinfaLevels_0007);
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			players[i].setJiazuTitle(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			players[i].setJiazuIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			players[i].setSealState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setBournExp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			players[i].setWeaponParticle(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			players[i].setHorseParticle(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			players[i].setSuitBodyParticle(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			players[i].setSuitFootParticle(new String(content,offset,len,"UTF-8"));
			offset += len;
			players[i].setJiazuXuanZhanFlag(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] jiazuXuanZhanData_0008 = new long[len];
			for(int j = 0 ; j < jiazuXuanZhanData_0008.length ; j++){
				jiazuXuanZhanData_0008[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			players[i].setJiazuXuanZhanData(jiazuXuanZhanData_0008);
			players[i].setCitanStateLevel((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			players[i].setTouzhuanStateLevel((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			players[i].setCulture((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setTodayUsedBindSilver((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			players[i].setEvil((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			players[i].setSpouse(new String(content,offset,len,"UTF-8"));
			offset += len;
			players[i].setRMB((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			players[i].setVipLevel((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			players[i].setCityFightSide((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			players[i].setIsGuozhan(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setGuozhanLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setLilian((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			players[i].setRepairCarry(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setMailCarry(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setWareHouseCarry(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setQuickBuyCarry(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			players[i].setMaxHPX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setMaxMPX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setPhyDefenceX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setMagicDefenceX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setBreakDefenceX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setHitX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setDodgeX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setAccurateX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setPhyAttackX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setMagicAttackX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setFireAttackX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setFireDefenceX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setFireIgnoreDefenceX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setBlizzardAttackX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setBlizzardDefenceX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setBlizzardIgnoreDefenceX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setWindAttackX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setWindDefenceX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setWindIgnoreDefenceX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setThunderAttackX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setThunderDefenceX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setThunderIgnoreDefenceX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setCriticalHitX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setCriticalDefenceX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			players[i].setGongxun((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			players[i].setVipPickedRewardLevel((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
		}
	}

	public int getType() {
		return 0x70000013;
	}

	public String getTypeDescription() {
		return "QUERY_PLAYER_RES";
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
		len += 4;
		for(int i = 0 ; i < players.length ; i++){
			len += 4;
			len += 2;
			if(players[i].getMapName() != null){
				try{
				len += players[i].getMapName().getBytes("UTF-8").length;
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
			len += 1;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 8;
			len += 2;
			if(players[i].getName() != null){
				try{
				len += players[i].getName().getBytes("UTF-8").length;
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
			if(players[i].getAvataRace() != null){
				try{
				len += players[i].getAvataRace().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] avata = players[i].getAvata();
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
			len += players[i].getAvataType().length * 1;
			len += 1;
			len += 1;
			len += 1;
			len += 2;
			len += 2;
			len += 2;
			len += 8;
			len += 2;
			if(players[i].getJiazuName() != null){
				try{
				len += players[i].getJiazuName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(players[i].getZongPaiName() != null){
				try{
				len += players[i].getZongPaiName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(players[i].getTitle() != null){
				try{
				len += players[i].getTitle().getBytes("UTF-8").length;
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
			len += 4;
			len += 2;
			len += 4;
			len += players[i].getSkillOneLevels().length * 1;
			len += 1;
			len += 4;
			len += 4;
			len += 1;
			len += 1;
			len += 8;
			len += 1;
			len += 1;
			len += 4;
			len += players[i].getCareerBasicSkillsLevels().length * 1;
			len += 4;
			len += players[i].getSkillOneLevels().length * 1;
			len += 4;
			len += players[i].getNuqiSkillsLevels().length * 1;
			len += 4;
			len += players[i].getXinfaLevels().length * 1;
			len += 2;
			if(players[i].getJiazuTitle() != null){
				try{
				len += players[i].getJiazuTitle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(players[i].getJiazuIcon() != null){
				try{
				len += players[i].getJiazuIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 4;
			len += 2;
			if(players[i].getWeaponParticle() != null){
				try{
				len += players[i].getWeaponParticle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(players[i].getHorseParticle() != null){
				try{
				len += players[i].getHorseParticle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(players[i].getSuitBodyParticle() != null){
				try{
				len += players[i].getSuitBodyParticle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(players[i].getSuitFootParticle() != null){
				try{
				len += players[i].getSuitFootParticle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 4;
			len += players[i].getJiazuXuanZhanData().length * 8;
			len += 1;
			len += 1;
			len += 4;
			len += 8;
			len += 4;
			len += 2;
			if(players[i].getSpouse() != null){
				try{
				len += players[i].getSpouse().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 8;
			len += 1;
			len += 1;
			len += 1;
			len += 4;
			len += 8;
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
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 8;
			len += 1;
		}
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

			buffer.putInt(players.length);
			for(int i = 0 ; i < players.length ; i++){
				buffer.putInt((int)players[i].getCurrentGameCountry());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = players[i].getMapName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(players[i].isHold()==false?0:1));
				buffer.put((byte)(players[i].isStun()==false?0:1));
				buffer.put((byte)(players[i].isImmunity()==false?0:1));
				buffer.put((byte)(players[i].isPoison()==false?0:1));
				buffer.put((byte)(players[i].isWeak()==false?0:1));
				buffer.put((byte)(players[i].isInvulnerable()==false?0:1));
				buffer.put((byte)(players[i].isCure()==false?0:1));
				buffer.put((byte)(players[i].isLiuxueState()==false?0:1));
				buffer.put((byte)(players[i].isJiansuState()==false?0:1));
				buffer.put((byte)(players[i].isPojiaState()==false?0:1));
				buffer.put((byte)(players[i].isZhuoreState()==false?0:1));
				buffer.put((byte)(players[i].isHanlengState()==false?0:1));
				buffer.put((byte)(players[i].isTigaoBaojiState()==false?0:1));
				buffer.put((byte)(players[i].isTigaoWaigongState()==false?0:1));
				buffer.put((byte)(players[i].isTigaoNeigongState()==false?0:1));
				buffer.put((byte)(players[i].isTigaoWaifangState()==false?0:1));
				buffer.put((byte)(players[i].isTigaoNeifangState()==false?0:1));
				buffer.put((byte)players[i].getShield());
				buffer.put((byte)(players[i].isFighting()==false?0:1));
				buffer.put((byte)(players[i].isFlying()==false?0:1));
				buffer.put((byte)(players[i].isSitdownState()==false?0:1));
				buffer.putInt((int)players[i].getHp());
				buffer.putInt((int)players[i].getMaxHP());
				buffer.putInt((int)players[i].getMp());
				buffer.putInt((int)players[i].getMaxMP());
				buffer.putLong(players[i].getId());
				try{
				tmpBytes2 = players[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)players[i].getSex());
				buffer.put((byte)players[i].getCountry());
				buffer.putInt((int)players[i].getCountryPosition());
				buffer.put((byte)players[i].getCareer());
				buffer.putInt((int)players[i].getLevel());
				buffer.putInt((int)players[i].getSoulLevel());
				buffer.putShort((short)players[i].getClassLevel());
				buffer.putLong(players[i].getTotalRmbyuanbao());
				buffer.putLong(players[i].getBindSilver());
				buffer.putLong(players[i].getSilver());
				buffer.putLong(players[i].getExp());
				buffer.putLong(players[i].getNextLevelExp());
				buffer.putLong(players[i].getNextLevelExpPool());
				buffer.put((byte)players[i].getWeaponType());
				buffer.put((byte)players[i].getState());
				buffer.put((byte)players[i].getDirection());
				try{
				tmpBytes2 = players[i].getAvataRace().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(players[i].getAvata().length);
				String[] avata_0009 = players[i].getAvata();
				for(int j = 0 ; j < avata_0009.length ; j++){
				try{
					buffer.putShort((short)avata_0009[j].getBytes("UTF-8").length);
					buffer.put(avata_0009[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(players[i].getAvataType().length);
				buffer.put(players[i].getAvataType());
				buffer.put((byte)players[i].getAura());
				buffer.put((byte)players[i].getEncloser());
				buffer.put((byte)(players[i].isIsUpOrDown()==false?0:1));
				buffer.putShort((short)players[i].getCommonAttackSpeed());
				buffer.putShort((short)players[i].getCommonAttackRange());
				buffer.putShort((short)players[i].getSpeed());
				buffer.putLong(players[i].getJiazuId());
				try{
				tmpBytes2 = players[i].getJiazuName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = players[i].getZongPaiName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = players[i].getTitle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)players[i].getStrength());
				buffer.putInt((int)players[i].getDexterity());
				buffer.putInt((int)players[i].getConstitution());
				buffer.putInt((int)players[i].getSpellpower());
				buffer.putInt((int)players[i].getPhysicalDamageUpperLimit());
				buffer.putInt((int)players[i].getPhysicalDamageLowerLimit());
				buffer.putInt((int)players[i].getPhyDefence());
				buffer.putInt((int)players[i].getMagicDefence());
				buffer.putInt((int)players[i].getDodge());
				buffer.putInt((int)players[i].getPhyAttack());
				buffer.putInt((int)players[i].getMagicAttack());
				buffer.putInt((int)players[i].getCoolDownTimePercent());
				buffer.putInt((int)players[i].getSetupTimePercent());
				buffer.putInt((int)players[i].getCriticalHit());
				buffer.putInt((int)players[i].getVitality());
				buffer.putInt((int)players[i].getMaxVitality());
				buffer.putInt((int)players[i].getEnergy());
				buffer.putInt((int)players[i].getXp());
				buffer.putInt((int)players[i].getTotalXp());
				buffer.putInt((int)players[i].getBreakDefence());
				buffer.putInt((int)players[i].getBreakDefenceRate());
				buffer.putInt((int)players[i].getHit());
				buffer.putInt((int)players[i].getHitRate());
				buffer.putInt((int)players[i].getDodge());
				buffer.putInt((int)players[i].getDodgeRate());
				buffer.putInt((int)players[i].getAccurate());
				buffer.putInt((int)players[i].getAccurateRate());
				buffer.putInt((int)players[i].getFireAttack());
				buffer.putInt((int)players[i].getFireDefence());
				buffer.putInt((int)players[i].getFireDefenceRate());
				buffer.putInt((int)players[i].getFireIgnoreDefence());
				buffer.putInt((int)players[i].getFireIgnoreDefenceRate());
				buffer.putInt((int)players[i].getBlizzardAttack());
				buffer.putInt((int)players[i].getBlizzardDefence());
				buffer.putInt((int)players[i].getBlizzardDefenceRate());
				buffer.putInt((int)players[i].getBlizzardIgnoreDefence());
				buffer.putInt((int)players[i].getBlizzardIgnoreDefenceRate());
				buffer.putInt((int)players[i].getWindAttack());
				buffer.putInt((int)players[i].getWindDefence());
				buffer.putInt((int)players[i].getWindDefenceRate());
				buffer.putInt((int)players[i].getWindIgnoreDefence());
				buffer.putInt((int)players[i].getWindIgnoreDefenceRate());
				buffer.putInt((int)players[i].getThunderAttack());
				buffer.putInt((int)players[i].getThunderDefence());
				buffer.putInt((int)players[i].getThunderDefenceRate());
				buffer.putInt((int)players[i].getThunderIgnoreDefence());
				buffer.putInt((int)players[i].getThunderIgnoreDefenceRate());
				buffer.putInt((int)players[i].getCriticalDefence());
				buffer.putInt((int)players[i].getCriticalDefenceRate());
				buffer.putInt((int)players[i].getCriticalHitRate());
				buffer.putShort((short)players[i].getUnallocatedSkillPoint());
				buffer.putInt(players[i].getSkillOneLevels().length);
				buffer.put(players[i].getSkillOneLevels());
				buffer.put((byte)players[i].getTeamMark());
				buffer.putInt((int)players[i].getX());
				buffer.putInt((int)players[i].getY());
				buffer.put((byte)(players[i].isInBattleField()==false?0:1));
				buffer.put((byte)players[i].getBattleFieldSide());
				buffer.putLong(players[i].getAvataPropsId());
				buffer.put((byte)players[i].getPkMode());
				buffer.put((byte)players[i].getNameColorType());
				buffer.putInt(players[i].getCareerBasicSkillsLevels().length);
				buffer.put(players[i].getCareerBasicSkillsLevels());
				buffer.putInt(players[i].getSkillOneLevels().length);
				buffer.put(players[i].getSkillOneLevels());
				buffer.putInt(players[i].getNuqiSkillsLevels().length);
				buffer.put(players[i].getNuqiSkillsLevels());
				buffer.putInt(players[i].getXinfaLevels().length);
				buffer.put(players[i].getXinfaLevels());
				try{
				tmpBytes2 = players[i].getJiazuTitle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = players[i].getJiazuIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(players[i].isSealState()==false?0:1));
				buffer.putInt((int)players[i].getBournExp());
				try{
				tmpBytes2 = players[i].getWeaponParticle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = players[i].getHorseParticle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = players[i].getSuitBodyParticle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = players[i].getSuitFootParticle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(players[i].isJiazuXuanZhanFlag()==false?0:1));
				buffer.putInt(players[i].getJiazuXuanZhanData().length);
				long[] jiazuXuanZhanData_0010 = players[i].getJiazuXuanZhanData();
				for(int j = 0 ; j < jiazuXuanZhanData_0010.length ; j++){
					buffer.putLong(jiazuXuanZhanData_0010[j]);
				}
				buffer.put((byte)players[i].getCitanStateLevel());
				buffer.put((byte)players[i].getTouzhuanStateLevel());
				buffer.putInt((int)players[i].getCulture());
				buffer.putLong(players[i].getTodayUsedBindSilver());
				buffer.putInt((int)players[i].getEvil());
				try{
				tmpBytes2 = players[i].getSpouse().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putLong(players[i].getRMB());
				buffer.put((byte)players[i].getVipLevel());
				buffer.put((byte)players[i].getCityFightSide());
				buffer.put((byte)(players[i].isIsGuozhan()==false?0:1));
				buffer.putInt((int)players[i].getGuozhanLevel());
				buffer.putLong(players[i].getLilian());
				buffer.put((byte)(players[i].isRepairCarry()==false?0:1));
				buffer.put((byte)(players[i].isMailCarry()==false?0:1));
				buffer.put((byte)(players[i].isWareHouseCarry()==false?0:1));
				buffer.put((byte)(players[i].isQuickBuyCarry()==false?0:1));
				buffer.putInt((int)players[i].getMaxHPX());
				buffer.putInt((int)players[i].getMaxMPX());
				buffer.putInt((int)players[i].getPhyDefenceX());
				buffer.putInt((int)players[i].getMagicDefenceX());
				buffer.putInt((int)players[i].getBreakDefenceX());
				buffer.putInt((int)players[i].getHitX());
				buffer.putInt((int)players[i].getDodgeX());
				buffer.putInt((int)players[i].getAccurateX());
				buffer.putInt((int)players[i].getPhyAttackX());
				buffer.putInt((int)players[i].getMagicAttackX());
				buffer.putInt((int)players[i].getFireAttackX());
				buffer.putInt((int)players[i].getFireDefenceX());
				buffer.putInt((int)players[i].getFireIgnoreDefenceX());
				buffer.putInt((int)players[i].getBlizzardAttackX());
				buffer.putInt((int)players[i].getBlizzardDefenceX());
				buffer.putInt((int)players[i].getBlizzardIgnoreDefenceX());
				buffer.putInt((int)players[i].getWindAttackX());
				buffer.putInt((int)players[i].getWindDefenceX());
				buffer.putInt((int)players[i].getWindIgnoreDefenceX());
				buffer.putInt((int)players[i].getThunderAttackX());
				buffer.putInt((int)players[i].getThunderDefenceX());
				buffer.putInt((int)players[i].getThunderIgnoreDefenceX());
				buffer.putInt((int)players[i].getCriticalHitX());
				buffer.putInt((int)players[i].getCriticalDefenceX());
				buffer.putLong(players[i].getGongxun());
				buffer.put((byte)players[i].getVipPickedRewardLevel());
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
	 *	此账户在此服务器上创建的所有的角色，长度为0或者n的数组
	 */
	public Player[] getPlayers(){
		return players;
	}

	/**
	 * 设置属性：
	 *	此账户在此服务器上创建的所有的角色，长度为0或者n的数组
	 */
	public void setPlayers(Player[] players){
		this.players = players;
	}

}
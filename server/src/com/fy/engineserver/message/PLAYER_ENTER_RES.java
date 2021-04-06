package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.sprite.Soul;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 角色进入游戏请求<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description</td><td>String</td><td>description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs.length</td><td>int</td><td>4个字节</td><td>Buff数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[0].seqId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[0].templateId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[0].groupId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[0].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[0].iconId</td><td>String</td><td>buffs[0].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[0].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[0].invalidTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[0].startTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[0].templateName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[0].templateName</td><td>String</td><td>buffs[0].templateName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[0].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[0].description</td><td>String</td><td>buffs[0].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[0].advantageous</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[0].forover</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[1].seqId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[1].templateId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[1].groupId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[1].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[1].iconId</td><td>String</td><td>buffs[1].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[1].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[1].invalidTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[1].startTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[1].templateName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[1].templateName</td><td>String</td><td>buffs[1].templateName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[1].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[1].description</td><td>String</td><td>buffs[1].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[1].advantageous</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[1].forover</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[2].seqId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[2].templateId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[2].groupId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[2].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[2].iconId</td><td>String</td><td>buffs[2].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[2].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[2].invalidTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[2].startTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[2].templateName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[2].templateName</td><td>String</td><td>buffs[2].templateName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[2].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[2].description</td><td>String</td><td>buffs[2].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[2].advantageous</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[2].forover</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>worldMapDataVersion</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapDataVersion</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsterTypes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsterTypes</td><td>short[]</td><td>monsterTypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls.length</td><td>int</td><td>4个字节</td><td>Soul数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].soulType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].maxHp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].maxMp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].strength</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].spellpower</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].constitution</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].dexterity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].breakDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].fireIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].blizzardAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].blizzardIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].windIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[0].thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[0].thunderIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].soulType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].maxHp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].maxMp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].strength</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].spellpower</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].constitution</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].dexterity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].breakDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].fireIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].blizzardAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].blizzardIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].windIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[1].thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[1].thunderIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].soulType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].maxHp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].maxMp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].strength</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].spellpower</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].constitution</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].dexterity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].breakDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].fireIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].blizzardAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].blizzardIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].windIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>souls[2].thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>souls[2].thunderIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>switchColdDown</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>coldDownLeft</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxUseBindSilver</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class PLAYER_ENTER_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte result;
	String description;
	Buff[] buffs;
	int worldMapDataVersion;
	int mapDataVersion;
	short[] monsterTypes;
	Soul[] souls;
	long switchColdDown;
	long coldDownLeft;
	long maxUseBindSilver;

	public PLAYER_ENTER_RES(){
	}

	public PLAYER_ENTER_RES(long seqNum,byte result,String description,Buff[] buffs,int worldMapDataVersion,int mapDataVersion,short[] monsterTypes,Soul[] souls,long switchColdDown,long coldDownLeft,long maxUseBindSilver){
		this.seqNum = seqNum;
		this.result = result;
		this.description = description;
		this.buffs = buffs;
		this.worldMapDataVersion = worldMapDataVersion;
		this.mapDataVersion = mapDataVersion;
		this.monsterTypes = monsterTypes;
		this.souls = souls;
		this.switchColdDown = switchColdDown;
		this.coldDownLeft = coldDownLeft;
		this.maxUseBindSilver = maxUseBindSilver;
	}

	public PLAYER_ENTER_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		result = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		description = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		buffs = new Buff[len];
		for(int i = 0 ; i < buffs.length ; i++){
			buffs[i] = new Buff();
			buffs[i].setSeqId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			buffs[i].setTemplateId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			buffs[i].setGroupId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			buffs[i].setIconId(new String(content,offset,len,"UTF-8"));
			offset += len;
			buffs[i].setLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			buffs[i].setInvalidTime((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			buffs[i].setStartTime((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			buffs[i].setTemplateName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			buffs[i].setDescription(new String(content,offset,len,"UTF-8"));
			offset += len;
			buffs[i].setAdvantageous(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			buffs[i].setForover(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
		}
		worldMapDataVersion = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		mapDataVersion = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		monsterTypes = new short[len];
		for(int i = 0 ; i < monsterTypes.length ; i++){
			monsterTypes[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
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
		switchColdDown = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		coldDownLeft = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		maxUseBindSilver = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x70000016;
	}

	public String getTypeDescription() {
		return "PLAYER_ENTER_RES";
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
		len += 1;
		len += 2;
		try{
			len +=description.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < buffs.length ; i++){
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(buffs[i].getIconId() != null){
				try{
				len += buffs[i].getIconId().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 8;
			len += 8;
			len += 2;
			if(buffs[i].getTemplateName() != null){
				try{
				len += buffs[i].getTemplateName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(buffs[i].getDescription() != null){
				try{
				len += buffs[i].getDescription().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 1;
		}
		len += 4;
		len += 4;
		len += 4;
		len += monsterTypes.length * 2;
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
		len += 8;
		len += 8;
		len += 8;
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

			buffer.put(result);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = description.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(buffs.length);
			for(int i = 0 ; i < buffs.length ; i++){
				buffer.putInt((int)buffs[i].getSeqId());
				buffer.putInt((int)buffs[i].getTemplateId());
				buffer.putInt((int)buffs[i].getGroupId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = buffs[i].getIconId().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)buffs[i].getLevel());
				buffer.putLong(buffs[i].getInvalidTime());
				buffer.putLong(buffs[i].getStartTime());
				try{
				tmpBytes2 = buffs[i].getTemplateName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = buffs[i].getDescription().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(buffs[i].isAdvantageous()==false?0:1));
				buffer.put((byte)(buffs[i].isForover()==false?0:1));
			}
			buffer.putInt(worldMapDataVersion);
			buffer.putInt(mapDataVersion);
			buffer.putInt(monsterTypes.length);
			for(int i = 0 ; i < monsterTypes.length; i++){
				buffer.putShort(monsterTypes[i]);
			}
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
			buffer.putLong(switchColdDown);
			buffer.putLong(coldDownLeft);
			buffer.putLong(maxUseBindSilver);
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
	 *	结果, 0-可以进入游戏,1-不可以进入游戏
	 */
	public byte getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	结果, 0-可以进入游戏,1-不可以进入游戏
	 */
	public void setResult(byte result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	不可以的描述
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * 设置属性：
	 *	不可以的描述
	 */
	public void setDescription(String description){
		this.description = description;
	}

	/**
	 * 获取属性：
	 *	玩家身上的Buff
	 */
	public Buff[] getBuffs(){
		return buffs;
	}

	/**
	 * 设置属性：
	 *	玩家身上的Buff
	 */
	public void setBuffs(Buff[] buffs){
		this.buffs = buffs;
	}

	/**
	 * 获取属性：
	 *	世界地图的数据版本号
	 */
	public int getWorldMapDataVersion(){
		return worldMapDataVersion;
	}

	/**
	 * 设置属性：
	 *	世界地图的数据版本号
	 */
	public void setWorldMapDataVersion(int worldMapDataVersion){
		this.worldMapDataVersion = worldMapDataVersion;
	}

	/**
	 * 获取属性：
	 *	要进入地图的数据版本号
	 */
	public int getMapDataVersion(){
		return mapDataVersion;
	}

	/**
	 * 设置属性：
	 *	要进入地图的数据版本号
	 */
	public void setMapDataVersion(int mapDataVersion){
		this.mapDataVersion = mapDataVersion;
	}

	/**
	 * 获取属性：
	 *	精灵的类型（仅限用普通动画表达）
	 */
	public short[] getMonsterTypes(){
		return monsterTypes;
	}

	/**
	 * 设置属性：
	 *	精灵的类型（仅限用普通动画表达）
	 */
	public void setMonsterTypes(short[] monsterTypes){
		this.monsterTypes = monsterTypes;
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
	 *	切换元神冷却时间
	 */
	public long getSwitchColdDown(){
		return switchColdDown;
	}

	/**
	 * 设置属性：
	 *	切换元神冷却时间
	 */
	public void setSwitchColdDown(long switchColdDown){
		this.switchColdDown = switchColdDown;
	}

	/**
	 * 获取属性：
	 *	切换元神剩余
	 */
	public long getColdDownLeft(){
		return coldDownLeft;
	}

	/**
	 * 设置属性：
	 *	切换元神剩余
	 */
	public void setColdDownLeft(long coldDownLeft){
		this.coldDownLeft = coldDownLeft;
	}

	/**
	 * 获取属性：
	 *	最大使用绑银上限
	 */
	public long getMaxUseBindSilver(){
		return maxUseBindSilver;
	}

	/**
	 * 设置属性：
	 *	最大使用绑银上限
	 */
	public void setMaxUseBindSilver(long maxUseBindSilver){
		this.maxUseBindSilver = maxUseBindSilver;
	}

}
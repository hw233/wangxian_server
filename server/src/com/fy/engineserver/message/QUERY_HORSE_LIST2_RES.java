package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.horse.Horse;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端向服务器发送查询马匹列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList.length</td><td>int</td><td>4个字节</td><td>Horse数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].horseId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].horseLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].rank</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].horseShowName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].horseShowName</td><td>String</td><td>horseList[0].horseShowName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].avata.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].avata</td><td>String</td><td>horseList[0].avata.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].icon</td><td>String</td><td>horseList[0].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].equipmentIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].equipmentIds</td><td>long[]</td><td>horseList[0].equipmentIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].maxEnergy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].energy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].lastEnergyIndex</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].bloodStar</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].growRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].skillNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].fly</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].colorType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].speed</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].maxMP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].breakDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].blizzardAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].blizzardIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].fireIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].windIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[0].thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[0].thunderIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].horseId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].horseLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].rank</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].horseShowName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].horseShowName</td><td>String</td><td>horseList[1].horseShowName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].avata.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].avata</td><td>String</td><td>horseList[1].avata.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].icon</td><td>String</td><td>horseList[1].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].equipmentIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].equipmentIds</td><td>long[]</td><td>horseList[1].equipmentIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].maxEnergy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].energy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].lastEnergyIndex</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].bloodStar</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].growRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].skillNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].fly</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].colorType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].speed</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].maxMP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].breakDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].blizzardAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].blizzardIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].fireIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].windIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[1].thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[1].thunderIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].horseId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].horseLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].rank</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].horseShowName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].horseShowName</td><td>String</td><td>horseList[2].horseShowName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].avata.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].avata</td><td>String</td><td>horseList[2].avata.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].icon</td><td>String</td><td>horseList[2].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].equipmentIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].equipmentIds</td><td>long[]</td><td>horseList[2].equipmentIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].maxEnergy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].energy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].lastEnergyIndex</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].bloodStar</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].growRate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].skillNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].fly</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].colorType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].speed</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].maxMP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].breakDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].accurate</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].criticalDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].blizzardAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].blizzardIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].fireAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].fireIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].windAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].windIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].thunderAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseList[2].thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseList[2].thunderIgnoreDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_HORSE_LIST2_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long playerId;
	Horse[] horseList;

	public QUERY_HORSE_LIST2_RES(){
	}

	public QUERY_HORSE_LIST2_RES(long seqNum,long playerId,Horse[] horseList){
		this.seqNum = seqNum;
		this.playerId = playerId;
		this.horseList = horseList;
	}

	public QUERY_HORSE_LIST2_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		horseList = new Horse[len];
		for(int i = 0 ; i < horseList.length ; i++){
			horseList[i] = new Horse();
			horseList[i].setHorseId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			horseList[i].setHorseLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setRank((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			horseList[i].setHorseShowName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			horseList[i].setAvata(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			horseList[i].setIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] equipmentIds_0001 = new long[len];
			for(int j = 0 ; j < equipmentIds_0001.length ; j++){
				equipmentIds_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			horseList[i].setEquipmentIds(equipmentIds_0001);
			horseList[i].setMaxEnergy((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setEnergy((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setLastEnergyIndex((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setBloodStar((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setGrowRate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setSkillNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setFly(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			horseList[i].setColorType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setSpeed((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setMaxHP((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setPhyAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setMagicAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setPhyDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setMagicDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setMaxMP((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setBreakDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setAccurate((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setCriticalDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setCriticalHit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setHit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setDodge((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setBlizzardAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setBlizzardDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setBlizzardIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setFireAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setFireDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setFireIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setWindAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setWindDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setWindIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setThunderAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setThunderDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			horseList[i].setThunderIgnoreDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x70F0EF47;
	}

	public String getTypeDescription() {
		return "QUERY_HORSE_LIST2_RES";
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
		len += 4;
		for(int i = 0 ; i < horseList.length ; i++){
			len += 8;
			len += 4;
			len += 4;
			len += 2;
			if(horseList[i].getHorseShowName() != null){
				try{
				len += horseList[i].getHorseShowName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(horseList[i].getAvata() != null){
				try{
				len += horseList[i].getAvata().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(horseList[i].getIcon() != null){
				try{
				len += horseList[i].getIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += horseList[i].getEquipmentIds().length * 8;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
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

			buffer.putLong(playerId);
			buffer.putInt(horseList.length);
			for(int i = 0 ; i < horseList.length ; i++){
				buffer.putLong(horseList[i].getHorseId());
				buffer.putInt((int)horseList[i].getHorseLevel());
				buffer.putInt((int)horseList[i].getRank());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = horseList[i].getHorseShowName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = horseList[i].getAvata().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = horseList[i].getIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(horseList[i].getEquipmentIds().length);
				long[] equipmentIds_0002 = horseList[i].getEquipmentIds();
				for(int j = 0 ; j < equipmentIds_0002.length ; j++){
					buffer.putLong(equipmentIds_0002[j]);
				}
				buffer.putInt((int)horseList[i].getMaxEnergy());
				buffer.putInt((int)horseList[i].getEnergy());
				buffer.putInt((int)horseList[i].getLastEnergyIndex());
				buffer.putInt((int)horseList[i].getBloodStar());
				buffer.putInt((int)horseList[i].getGrowRate());
				buffer.putInt((int)horseList[i].getSkillNum());
				buffer.put((byte)(horseList[i].isFly()==false?0:1));
				buffer.putInt((int)horseList[i].getColorType());
				buffer.putInt((int)horseList[i].getSpeed());
				buffer.putInt((int)horseList[i].getMaxHP());
				buffer.putInt((int)horseList[i].getPhyAttack());
				buffer.putInt((int)horseList[i].getMagicAttack());
				buffer.putInt((int)horseList[i].getPhyDefence());
				buffer.putInt((int)horseList[i].getMagicDefence());
				buffer.putInt((int)horseList[i].getMaxMP());
				buffer.putInt((int)horseList[i].getBreakDefence());
				buffer.putInt((int)horseList[i].getAccurate());
				buffer.putInt((int)horseList[i].getCriticalDefence());
				buffer.putInt((int)horseList[i].getCriticalHit());
				buffer.putInt((int)horseList[i].getHit());
				buffer.putInt((int)horseList[i].getDodge());
				buffer.putInt((int)horseList[i].getBlizzardAttack());
				buffer.putInt((int)horseList[i].getBlizzardDefence());
				buffer.putInt((int)horseList[i].getBlizzardIgnoreDefence());
				buffer.putInt((int)horseList[i].getFireAttack());
				buffer.putInt((int)horseList[i].getFireDefence());
				buffer.putInt((int)horseList[i].getFireIgnoreDefence());
				buffer.putInt((int)horseList[i].getWindAttack());
				buffer.putInt((int)horseList[i].getWindDefence());
				buffer.putInt((int)horseList[i].getWindIgnoreDefence());
				buffer.putInt((int)horseList[i].getThunderAttack());
				buffer.putInt((int)horseList[i].getThunderDefence());
				buffer.putInt((int)horseList[i].getThunderIgnoreDefence());
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
	 *	角色id
	 */
	public long getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	角色id
	 */
	public void setPlayerId(long playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	返回个人马匹数组
	 */
	public Horse[] getHorseList(){
		return horseList;
	}

	/**
	 * 设置属性：
	 *	返回个人马匹数组
	 */
	public void setHorseList(Horse[] horseList){
		this.horseList = horseList;
	}

}
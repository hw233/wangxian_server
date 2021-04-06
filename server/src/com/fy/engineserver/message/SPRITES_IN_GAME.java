package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.Sprite;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器主动向客户端发送的数据包，告诉客户端本地图上有哪些怪,此数组客户端应该保留在内存中，在AROUND_CHANGE_REQ会通知此怪的下标来节省流量<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame.length</td><td>int</td><td>4个字节</td><td>Sprite数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].name</td><td>String</td><td>monstersInGame[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].title</td><td>String</td><td>monstersInGame[0].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].icon</td><td>String</td><td>monstersInGame[0].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].inBattleField</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].battleFieldSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].hold</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].stun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].immunity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].poison</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].weak</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].invulnerable</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].shield</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].aura</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].avataAction.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].avataAction</td><td>String</td><td>monstersInGame[0].avataAction.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].avataRace</td><td>String</td><td>monstersInGame[0].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].avataSex</td><td>String</td><td>monstersInGame[0].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].avata[0]</td><td>String</td><td>monstersInGame[0].avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].avata[1]</td><td>String</td><td>monstersInGame[0].avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].avata[2]</td><td>String</td><td>monstersInGame[0].avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].avataType</td><td>byte[]</td><td>monstersInGame[0].avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].height</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].spriteType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].monsterType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].npcType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].taskMark</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].skillUsingState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].nameColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].objectScale</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].objectColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].objectOpacity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].particleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].particleName</td><td>String</td><td>monstersInGame[0].particleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].particleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].particleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].footParticleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].footParticleName</td><td>String</td><td>monstersInGame[0].footParticleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].footParticleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[0].footParticleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[0].classLevel</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].name</td><td>String</td><td>monstersInGame[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].title</td><td>String</td><td>monstersInGame[1].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].icon</td><td>String</td><td>monstersInGame[1].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].inBattleField</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].battleFieldSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].hold</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].stun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].immunity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].poison</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].weak</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].invulnerable</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].shield</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].aura</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].avataAction.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].avataAction</td><td>String</td><td>monstersInGame[1].avataAction.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].avataRace</td><td>String</td><td>monstersInGame[1].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].avataSex</td><td>String</td><td>monstersInGame[1].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].avata[0]</td><td>String</td><td>monstersInGame[1].avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].avata[1]</td><td>String</td><td>monstersInGame[1].avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].avata[2]</td><td>String</td><td>monstersInGame[1].avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].avataType</td><td>byte[]</td><td>monstersInGame[1].avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].height</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].spriteType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].monsterType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].npcType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].taskMark</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].skillUsingState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].nameColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].objectScale</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].objectColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].objectOpacity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].particleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].particleName</td><td>String</td><td>monstersInGame[1].particleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].particleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].particleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].footParticleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].footParticleName</td><td>String</td><td>monstersInGame[1].footParticleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].footParticleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[1].footParticleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[1].classLevel</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].name</td><td>String</td><td>monstersInGame[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].title</td><td>String</td><td>monstersInGame[2].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].icon</td><td>String</td><td>monstersInGame[2].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].inBattleField</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].battleFieldSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].hold</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].stun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].immunity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].poison</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].weak</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].invulnerable</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].shield</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].aura</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].avataAction.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].avataAction</td><td>String</td><td>monstersInGame[2].avataAction.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].avataRace</td><td>String</td><td>monstersInGame[2].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].avataSex</td><td>String</td><td>monstersInGame[2].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].avata[0]</td><td>String</td><td>monstersInGame[2].avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].avata[1]</td><td>String</td><td>monstersInGame[2].avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].avata[2]</td><td>String</td><td>monstersInGame[2].avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].avataType</td><td>byte[]</td><td>monstersInGame[2].avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].height</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].spriteType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].monsterType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].npcType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].taskMark</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].skillUsingState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].nameColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].objectScale</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].objectColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].objectOpacity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].particleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].particleName</td><td>String</td><td>monstersInGame[2].particleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].particleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].particleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].footParticleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].footParticleName</td><td>String</td><td>monstersInGame[2].footParticleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].footParticleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstersInGame[2].footParticleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstersInGame[2].classLevel</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class SPRITES_IN_GAME implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	Sprite[] monstersInGame;

	public SPRITES_IN_GAME(){
	}

	public SPRITES_IN_GAME(long seqNum,Sprite[] monstersInGame){
		this.seqNum = seqNum;
		this.monstersInGame = monstersInGame;
	}

	public SPRITES_IN_GAME(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		monstersInGame = new Sprite[len];
		for(int i = 0 ; i < monstersInGame.length ; i++){
			monstersInGame[i] = new Sprite();
			monstersInGame[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			monstersInGame[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			monstersInGame[i].setTitle(new String(content,offset,len,"UTF-8"));
			offset += len;
			monstersInGame[i].setLevel((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			monstersInGame[i].setState((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			monstersInGame[i].setDirection((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			monstersInGame[i].setHp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			monstersInGame[i].setMaxHP((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			monstersInGame[i].setIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			monstersInGame[i].setX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			monstersInGame[i].setY((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			monstersInGame[i].setInBattleField(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			monstersInGame[i].setBattleFieldSide((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			monstersInGame[i].setHold(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			monstersInGame[i].setStun(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			monstersInGame[i].setImmunity(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			monstersInGame[i].setPoison(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			monstersInGame[i].setWeak(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			monstersInGame[i].setInvulnerable(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			monstersInGame[i].setShield((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			monstersInGame[i].setAura((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			monstersInGame[i].setAvataAction(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			monstersInGame[i].setAvataRace(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			monstersInGame[i].setAvataSex(new String(content,offset,len,"UTF-8"));
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
			monstersInGame[i].setAvata(avata_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] avataType_0002 = new byte[len];
			System.arraycopy(content,offset,avataType_0002,0,len);
			offset += len;
			monstersInGame[i].setAvataType(avataType_0002);
			monstersInGame[i].setHeight((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			monstersInGame[i].setSpriteType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			monstersInGame[i].setMonsterType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			monstersInGame[i].setNpcType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			monstersInGame[i].setTaskMark(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			monstersInGame[i].setCountry((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			monstersInGame[i].setSpeed((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			monstersInGame[i].setSkillUsingState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			monstersInGame[i].setNameColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			monstersInGame[i].setObjectScale((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			monstersInGame[i].setObjectColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			monstersInGame[i].setObjectOpacity(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			monstersInGame[i].setParticleName(new String(content,offset,len,"UTF-8"));
			offset += len;
			monstersInGame[i].setParticleX((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			monstersInGame[i].setParticleY((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			monstersInGame[i].setFootParticleName(new String(content,offset,len,"UTF-8"));
			offset += len;
			monstersInGame[i].setFootParticleX((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			monstersInGame[i].setFootParticleY((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			monstersInGame[i].setClassLevel((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
		}
	}

	public int getType() {
		return 0x000111D0;
	}

	public String getTypeDescription() {
		return "SPRITES_IN_GAME";
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
		for(int i = 0 ; i < monstersInGame.length ; i++){
			len += 8;
			len += 2;
			if(monstersInGame[i].getName() != null){
				try{
				len += monstersInGame[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(monstersInGame[i].getTitle() != null){
				try{
				len += monstersInGame[i].getTitle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 1;
			len += 1;
			len += 4;
			len += 4;
			len += 2;
			if(monstersInGame[i].getIcon() != null){
				try{
				len += monstersInGame[i].getIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
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
			len += 2;
			if(monstersInGame[i].getAvataAction() != null){
				try{
				len += monstersInGame[i].getAvataAction().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(monstersInGame[i].getAvataRace() != null){
				try{
				len += monstersInGame[i].getAvataRace().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(monstersInGame[i].getAvataSex() != null){
				try{
				len += monstersInGame[i].getAvataSex().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] avata = monstersInGame[i].getAvata();
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
			len += monstersInGame[i].getAvataType().length * 1;
			len += 2;
			len += 1;
			len += 1;
			len += 1;
			len += 1;
			len += 1;
			len += 2;
			len += 1;
			len += 4;
			len += 2;
			len += 4;
			len += 1;
			len += 2;
			if(monstersInGame[i].getParticleName() != null){
				try{
				len += monstersInGame[i].getParticleName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 2;
			len += 2;
			if(monstersInGame[i].getFootParticleName() != null){
				try{
				len += monstersInGame[i].getFootParticleName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 2;
			len += 2;
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

			buffer.putInt(monstersInGame.length);
			for(int i = 0 ; i < monstersInGame.length ; i++){
				buffer.putLong(monstersInGame[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = monstersInGame[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = monstersInGame[i].getTitle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)monstersInGame[i].getLevel());
				buffer.put((byte)monstersInGame[i].getState());
				buffer.put((byte)monstersInGame[i].getDirection());
				buffer.putInt((int)monstersInGame[i].getHp());
				buffer.putInt((int)monstersInGame[i].getMaxHP());
				try{
				tmpBytes2 = monstersInGame[i].getIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)monstersInGame[i].getX());
				buffer.putInt((int)monstersInGame[i].getY());
				buffer.put((byte)(monstersInGame[i].isInBattleField()==false?0:1));
				buffer.put((byte)monstersInGame[i].getBattleFieldSide());
				buffer.put((byte)(monstersInGame[i].isHold()==false?0:1));
				buffer.put((byte)(monstersInGame[i].isStun()==false?0:1));
				buffer.put((byte)(monstersInGame[i].isImmunity()==false?0:1));
				buffer.put((byte)(monstersInGame[i].isPoison()==false?0:1));
				buffer.put((byte)(monstersInGame[i].isWeak()==false?0:1));
				buffer.put((byte)(monstersInGame[i].isInvulnerable()==false?0:1));
				buffer.put((byte)monstersInGame[i].getShield());
				buffer.put((byte)monstersInGame[i].getAura());
				try{
				tmpBytes2 = monstersInGame[i].getAvataAction().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = monstersInGame[i].getAvataRace().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = monstersInGame[i].getAvataSex().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(monstersInGame[i].getAvata().length);
				String[] avata_0003 = monstersInGame[i].getAvata();
				for(int j = 0 ; j < avata_0003.length ; j++){
				try{
					buffer.putShort((short)avata_0003[j].getBytes("UTF-8").length);
					buffer.put(avata_0003[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(monstersInGame[i].getAvataType().length);
				buffer.put(monstersInGame[i].getAvataType());
				buffer.putShort((short)monstersInGame[i].getHeight());
				buffer.put((byte)monstersInGame[i].getSpriteType());
				buffer.put((byte)monstersInGame[i].getMonsterType());
				buffer.put((byte)monstersInGame[i].getNpcType());
				buffer.put((byte)(monstersInGame[i].isTaskMark()==false?0:1));
				buffer.put((byte)monstersInGame[i].getCountry());
				buffer.putShort((short)monstersInGame[i].getSpeed());
				buffer.put((byte)(monstersInGame[i].isSkillUsingState()==false?0:1));
				buffer.putInt((int)monstersInGame[i].getNameColor());
				buffer.putShort((short)monstersInGame[i].getObjectScale());
				buffer.putInt((int)monstersInGame[i].getObjectColor());
				buffer.put((byte)(monstersInGame[i].isObjectOpacity()==false?0:1));
				try{
				tmpBytes2 = monstersInGame[i].getParticleName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)monstersInGame[i].getParticleX());
				buffer.putShort((short)monstersInGame[i].getParticleY());
				try{
				tmpBytes2 = monstersInGame[i].getFootParticleName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)monstersInGame[i].getFootParticleX());
				buffer.putShort((short)monstersInGame[i].getFootParticleY());
				buffer.putShort((short)monstersInGame[i].getClassLevel());
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
	 *	地图上存在的怪物数据
	 */
	public Sprite[] getMonstersInGame(){
		return monstersInGame;
	}

	/**
	 * 设置属性：
	 *	地图上存在的怪物数据
	 */
	public void setMonstersInGame(Sprite[] monstersInGame){
		this.monstersInGame = monstersInGame;
	}

}
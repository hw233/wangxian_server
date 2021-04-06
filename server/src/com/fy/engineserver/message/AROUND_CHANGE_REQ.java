package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.core.MoveTrace4Client;
import com.fy.engineserver.core.FieldChangeEvent;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器主动向客户端发送的数据包，告诉客户端其周围发生的变化，包括谁来了，谁走了，谁的什么属性发生了什么变化，以及谁在移动<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers.length</td><td>int</td><td>4个字节</td><td>Player数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].hold</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].pkMode</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].stun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].immunity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].poison</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].weak</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].invulnerable</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].cure</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].liuxueState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].jiansuState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].pojiaState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].zhuoreState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].hanlengState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].tigaoBaojiState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].tigaoWaigongState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].tigaoNeigongState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].tigaoWaifangState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].tigaoNeifangState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].shield</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].fighting</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].flying</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].boothState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].sitdownState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].vitality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].maxVitality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].mp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].maxMP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].name</td><td>String</td><td>enterPlayers[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].countryPosition</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].soulLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].avataRace</td><td>String</td><td>enterPlayers[0].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].avata[0]</td><td>String</td><td>enterPlayers[0].avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].avata[1]</td><td>String</td><td>enterPlayers[0].avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].avata[2]</td><td>String</td><td>enterPlayers[0].avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].avataType</td><td>byte[]</td><td>enterPlayers[0].avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].aura</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].encloser</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].isUpOrDown</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].commonAttackSpeed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].commonAttackRange</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].zongPaiName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].zongPaiName</td><td>String</td><td>enterPlayers[0].zongPaiName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].title</td><td>String</td><td>enterPlayers[0].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].chuangongState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].teamMark</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].inBattleField</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].battleFieldSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].nameColorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].jiazuTitle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].jiazuTitle</td><td>String</td><td>enterPlayers[0].jiazuTitle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].jiazuIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].jiazuIcon</td><td>String</td><td>enterPlayers[0].jiazuIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].skillUsingState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].weaponParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].weaponParticle</td><td>String</td><td>enterPlayers[0].weaponParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].horseParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].horseParticle</td><td>String</td><td>enterPlayers[0].horseParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].suitBodyParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].suitBodyParticle</td><td>String</td><td>enterPlayers[0].suitBodyParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].suitFootParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].suitFootParticle</td><td>String</td><td>enterPlayers[0].suitFootParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].jiazuId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].boothName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].boothName</td><td>String</td><td>enterPlayers[0].boothName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].citanStateLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].touzhuanStateLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].objectScale</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].objectColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].objectOpacity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].spouse.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].spouse</td><td>String</td><td>enterPlayers[0].spouse.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].beerState</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].vipLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].cityFightSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[0].isGuozhan</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[0].guozhanLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].hold</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].pkMode</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].stun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].immunity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].poison</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].weak</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].invulnerable</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].cure</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].liuxueState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].jiansuState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].pojiaState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].zhuoreState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].hanlengState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].tigaoBaojiState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].tigaoWaigongState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].tigaoNeigongState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].tigaoWaifangState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].tigaoNeifangState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].shield</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].fighting</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].flying</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].boothState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].sitdownState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].vitality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].maxVitality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].mp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].maxMP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].name</td><td>String</td><td>enterPlayers[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].countryPosition</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].soulLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].avataRace</td><td>String</td><td>enterPlayers[1].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].avata[0]</td><td>String</td><td>enterPlayers[1].avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].avata[1]</td><td>String</td><td>enterPlayers[1].avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].avata[2]</td><td>String</td><td>enterPlayers[1].avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].avataType</td><td>byte[]</td><td>enterPlayers[1].avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].aura</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].encloser</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].isUpOrDown</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].commonAttackSpeed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].commonAttackRange</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].zongPaiName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].zongPaiName</td><td>String</td><td>enterPlayers[1].zongPaiName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].title</td><td>String</td><td>enterPlayers[1].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].chuangongState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].teamMark</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].inBattleField</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].battleFieldSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].nameColorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].jiazuTitle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].jiazuTitle</td><td>String</td><td>enterPlayers[1].jiazuTitle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].jiazuIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].jiazuIcon</td><td>String</td><td>enterPlayers[1].jiazuIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].skillUsingState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].weaponParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].weaponParticle</td><td>String</td><td>enterPlayers[1].weaponParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].horseParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].horseParticle</td><td>String</td><td>enterPlayers[1].horseParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].suitBodyParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].suitBodyParticle</td><td>String</td><td>enterPlayers[1].suitBodyParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].suitFootParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].suitFootParticle</td><td>String</td><td>enterPlayers[1].suitFootParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].jiazuId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].boothName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].boothName</td><td>String</td><td>enterPlayers[1].boothName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].citanStateLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].touzhuanStateLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].objectScale</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].objectColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].objectOpacity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].spouse.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].spouse</td><td>String</td><td>enterPlayers[1].spouse.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].beerState</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].vipLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].cityFightSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[1].isGuozhan</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[1].guozhanLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].hold</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].pkMode</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].stun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].immunity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].poison</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].weak</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].invulnerable</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].cure</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].liuxueState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].jiansuState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].pojiaState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].zhuoreState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].hanlengState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].tigaoBaojiState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].tigaoWaigongState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].tigaoNeigongState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].tigaoWaifangState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].tigaoNeifangState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].shield</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].fighting</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].flying</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].boothState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].sitdownState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].vitality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].maxVitality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].mp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].maxMP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].name</td><td>String</td><td>enterPlayers[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].countryPosition</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].soulLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].avataRace</td><td>String</td><td>enterPlayers[2].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].avata[0]</td><td>String</td><td>enterPlayers[2].avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].avata[1]</td><td>String</td><td>enterPlayers[2].avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].avata[2]</td><td>String</td><td>enterPlayers[2].avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].avataType</td><td>byte[]</td><td>enterPlayers[2].avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].aura</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].encloser</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].isUpOrDown</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].commonAttackSpeed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].commonAttackRange</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].zongPaiName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].zongPaiName</td><td>String</td><td>enterPlayers[2].zongPaiName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].title</td><td>String</td><td>enterPlayers[2].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].chuangongState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].teamMark</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].inBattleField</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].battleFieldSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].nameColorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].jiazuTitle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].jiazuTitle</td><td>String</td><td>enterPlayers[2].jiazuTitle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].jiazuIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].jiazuIcon</td><td>String</td><td>enterPlayers[2].jiazuIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].skillUsingState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].weaponParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].weaponParticle</td><td>String</td><td>enterPlayers[2].weaponParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].horseParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].horseParticle</td><td>String</td><td>enterPlayers[2].horseParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].suitBodyParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].suitBodyParticle</td><td>String</td><td>enterPlayers[2].suitBodyParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].suitFootParticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].suitFootParticle</td><td>String</td><td>enterPlayers[2].suitFootParticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].jiazuId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].boothName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].boothName</td><td>String</td><td>enterPlayers[2].boothName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].citanStateLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].touzhuanStateLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].objectScale</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].objectColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].objectOpacity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].spouse.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].spouse</td><td>String</td><td>enterPlayers[2].spouse.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].beerState</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].vipLevel</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].cityFightSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPlayers[2].isGuozhan</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPlayers[2].guozhanLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets.length</td><td>int</td><td>4个字节</td><td>Pet数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].name</td><td>String</td><td>enterPets[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].ownerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].ownerName</td><td>String</td><td>enterPets[0].ownerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].ownerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].category.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].category</td><td>String</td><td>enterPets[0].category.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].character</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].breedTimes</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].breededTimes</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].lifeTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].happyness</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].exp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].nextLevelExp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].quality</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].starClass</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].generation</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].variation</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].strengthQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].dexterityQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].spellpowerQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].constitutionQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].dingliQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].trainLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].rarity</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].growthClass</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].growth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].skillIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].skillIds</td><td>int[]</td><td>enterPets[0].skillIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].activeSkillLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].activeSkillLevels</td><td>int[]</td><td>enterPets[0].activeSkillLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].petPropsId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].strength</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].dexterity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].spellpower</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].constitution</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].dingli</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].unAllocatedPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].activationType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].avataRace</td><td>String</td><td>enterPets[0].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].avataSex</td><td>String</td><td>enterPets[0].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].avata[0]</td><td>String</td><td>enterPets[0].avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].avata[1]</td><td>String</td><td>enterPets[0].avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].avata[2]</td><td>String</td><td>enterPets[0].avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].avataType</td><td>byte[]</td><td>enterPets[0].avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].title</td><td>String</td><td>enterPets[0].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].icon</td><td>String</td><td>enterPets[0].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].inBattleField</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].battleFieldSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].hold</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].stun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].immunity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].poison</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].weak</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].invulnerable</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].shield</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].aura</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].height</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].spriteType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].monsterType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].npcType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].taskMark</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].skillUsingState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].nameColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].objectScale</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].objectColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].objectOpacity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].particleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].particleName</td><td>String</td><td>enterPets[0].particleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].particleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].particleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].footParticleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].footParticleName</td><td>String</td><td>enterPets[0].footParticleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[0].footParticleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[0].footParticleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].name</td><td>String</td><td>enterPets[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].ownerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].ownerName</td><td>String</td><td>enterPets[1].ownerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].ownerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].category.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].category</td><td>String</td><td>enterPets[1].category.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].character</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].breedTimes</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].breededTimes</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].lifeTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].happyness</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].exp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].nextLevelExp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].quality</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].starClass</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].generation</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].variation</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].strengthQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].dexterityQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].spellpowerQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].constitutionQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].dingliQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].trainLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].rarity</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].growthClass</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].growth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].skillIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].skillIds</td><td>int[]</td><td>enterPets[1].skillIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].activeSkillLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].activeSkillLevels</td><td>int[]</td><td>enterPets[1].activeSkillLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].petPropsId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].strength</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].dexterity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].spellpower</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].constitution</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].dingli</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].unAllocatedPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].activationType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].avataRace</td><td>String</td><td>enterPets[1].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].avataSex</td><td>String</td><td>enterPets[1].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].avata[0]</td><td>String</td><td>enterPets[1].avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].avata[1]</td><td>String</td><td>enterPets[1].avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].avata[2]</td><td>String</td><td>enterPets[1].avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].avataType</td><td>byte[]</td><td>enterPets[1].avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].title</td><td>String</td><td>enterPets[1].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].icon</td><td>String</td><td>enterPets[1].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].inBattleField</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].battleFieldSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].hold</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].stun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].immunity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].poison</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].weak</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].invulnerable</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].shield</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].aura</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].height</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].spriteType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].monsterType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].npcType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].taskMark</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].skillUsingState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].nameColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].objectScale</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].objectColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].objectOpacity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].particleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].particleName</td><td>String</td><td>enterPets[1].particleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].particleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].particleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].footParticleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].footParticleName</td><td>String</td><td>enterPets[1].footParticleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[1].footParticleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[1].footParticleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].name</td><td>String</td><td>enterPets[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].ownerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].ownerName</td><td>String</td><td>enterPets[2].ownerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].ownerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].category.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].category</td><td>String</td><td>enterPets[2].category.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].character</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].sex</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].breedTimes</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].breededTimes</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].lifeTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].happyness</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].exp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].nextLevelExp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].quality</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].starClass</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].generation</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].variation</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].strengthQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].dexterityQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].spellpowerQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].constitutionQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].dingliQuality</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].trainLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].rarity</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].growthClass</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].growth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].skillIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].skillIds</td><td>int[]</td><td>enterPets[2].skillIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].activeSkillLevels.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].activeSkillLevels</td><td>int[]</td><td>enterPets[2].activeSkillLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].petPropsId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].strength</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].dexterity</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].spellpower</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].constitution</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].dingli</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].phyAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].phyDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].magicAttack</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].magicDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].criticalHit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].hit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].dodge</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].fireDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].windDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].blizzardDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].thunderDefence</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].unAllocatedPoints</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].activationType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].avataRace</td><td>String</td><td>enterPets[2].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].avataSex</td><td>String</td><td>enterPets[2].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].avata[0]</td><td>String</td><td>enterPets[2].avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].avata[1]</td><td>String</td><td>enterPets[2].avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].avata[2]</td><td>String</td><td>enterPets[2].avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].avataType</td><td>byte[]</td><td>enterPets[2].avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].title</td><td>String</td><td>enterPets[2].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].icon</td><td>String</td><td>enterPets[2].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].inBattleField</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].battleFieldSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].hold</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].stun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].immunity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].poison</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].weak</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].invulnerable</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].shield</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].aura</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].height</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].spriteType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].monsterType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].npcType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].taskMark</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].skillUsingState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].nameColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].objectScale</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].objectColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].objectOpacity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].particleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].particleName</td><td>String</td><td>enterPets[2].particleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].particleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].particleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].footParticleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].footParticleName</td><td>String</td><td>enterPets[2].footParticleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterPets[2].footParticleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterPets[2].footParticleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites.length</td><td>int</td><td>4个字节</td><td>Sprite数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].name</td><td>String</td><td>enterSprites[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].title</td><td>String</td><td>enterSprites[0].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].icon</td><td>String</td><td>enterSprites[0].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].inBattleField</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].battleFieldSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].hold</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].stun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].immunity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].poison</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].weak</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].invulnerable</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].shield</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].aura</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].avataAction.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].avataAction</td><td>String</td><td>enterSprites[0].avataAction.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].avataRace</td><td>String</td><td>enterSprites[0].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].avataSex</td><td>String</td><td>enterSprites[0].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].avata[0]</td><td>String</td><td>enterSprites[0].avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].avata[1]</td><td>String</td><td>enterSprites[0].avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].avata[2]</td><td>String</td><td>enterSprites[0].avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].avataType</td><td>byte[]</td><td>enterSprites[0].avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].height</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].spriteType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].monsterType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].npcType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].taskMark</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].skillUsingState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].nameColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].objectScale</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].objectColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].objectOpacity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].particleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].particleName</td><td>String</td><td>enterSprites[0].particleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].particleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].particleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].footParticleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].footParticleName</td><td>String</td><td>enterSprites[0].footParticleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].footParticleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[0].footParticleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[0].classLevel</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].name</td><td>String</td><td>enterSprites[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].title</td><td>String</td><td>enterSprites[1].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].icon</td><td>String</td><td>enterSprites[1].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].inBattleField</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].battleFieldSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].hold</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].stun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].immunity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].poison</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].weak</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].invulnerable</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].shield</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].aura</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].avataAction.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].avataAction</td><td>String</td><td>enterSprites[1].avataAction.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].avataRace</td><td>String</td><td>enterSprites[1].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].avataSex</td><td>String</td><td>enterSprites[1].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].avata[0]</td><td>String</td><td>enterSprites[1].avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].avata[1]</td><td>String</td><td>enterSprites[1].avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].avata[2]</td><td>String</td><td>enterSprites[1].avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].avataType</td><td>byte[]</td><td>enterSprites[1].avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].height</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].spriteType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].monsterType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].npcType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].taskMark</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].skillUsingState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].nameColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].objectScale</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].objectColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].objectOpacity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].particleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].particleName</td><td>String</td><td>enterSprites[1].particleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].particleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].particleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].footParticleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].footParticleName</td><td>String</td><td>enterSprites[1].footParticleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].footParticleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[1].footParticleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[1].classLevel</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].name</td><td>String</td><td>enterSprites[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].title</td><td>String</td><td>enterSprites[2].title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].maxHP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].icon</td><td>String</td><td>enterSprites[2].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].inBattleField</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].battleFieldSide</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].hold</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].stun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].immunity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].poison</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].weak</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].invulnerable</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].shield</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].aura</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].avataAction.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].avataAction</td><td>String</td><td>enterSprites[2].avataAction.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].avataRace</td><td>String</td><td>enterSprites[2].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].avataSex</td><td>String</td><td>enterSprites[2].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].avata.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].avata[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].avata[0]</td><td>String</td><td>enterSprites[2].avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].avata[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].avata[1]</td><td>String</td><td>enterSprites[2].avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].avata[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].avata[2]</td><td>String</td><td>enterSprites[2].avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].avataType.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].avataType</td><td>byte[]</td><td>enterSprites[2].avataType.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].height</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].spriteType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].monsterType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].npcType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].taskMark</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].country</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].skillUsingState</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].nameColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].objectScale</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].objectColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].objectOpacity</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].particleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].particleName</td><td>String</td><td>enterSprites[2].particleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].particleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].particleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].footParticleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].footParticleName</td><td>String</td><td>enterSprites[2].footParticleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].footParticleX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSprites[2].footParticleY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSprites[2].classLevel</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leavePlayers.length</td><td>int</td><td>4个字节</td><td>Player数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leavePlayers[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leavePlayers[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leavePlayers[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leavePets.length</td><td>int</td><td>4个字节</td><td>Pet数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leavePets[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leavePets[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leavePets[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leaveSprites.length</td><td>int</td><td>4个字节</td><td>Sprite数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leaveSprites[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leaveSprites[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leaveSprites[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings.length</td><td>int</td><td>4个字节</td><td>MoveTrace4Client数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings[0].startTimestamp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[0].destineTimestamp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings[0].roadLen.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[0].roadLen</td><td>short[]</td><td>moveLivings[0].roadLen.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings[0].pointsX.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[0].pointsX</td><td>short[]</td><td>moveLivings[0].pointsX.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings[0].type</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[0].pointsY.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings[0].pointsY</td><td>short[]</td><td>moveLivings[0].pointsY.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[0].speed</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[1].startTimestamp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings[1].destineTimestamp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[1].roadLen.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings[1].roadLen</td><td>short[]</td><td>moveLivings[1].roadLen.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[1].pointsX.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings[1].pointsX</td><td>short[]</td><td>moveLivings[1].pointsX.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[1].type</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings[1].pointsY.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[1].pointsY</td><td>short[]</td><td>moveLivings[1].pointsY.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings[1].speed</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings[2].startTimestamp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[2].destineTimestamp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings[2].roadLen.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[2].roadLen</td><td>short[]</td><td>moveLivings[2].roadLen.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings[2].pointsX.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[2].pointsX</td><td>short[]</td><td>moveLivings[2].pointsX.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings[2].type</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[2].pointsY.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>moveLivings[2].pointsY</td><td>short[]</td><td>moveLivings[2].pointsY.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveLivings[2].speed</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents.length</td><td>int</td><td>4个字节</td><td>FieldChangeEvent数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeEvents[0].objectId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents[0].objectType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeEvents[0].fieldId</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents[0].valueType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeEvents[0].valueData.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents[0].valueData</td><td>byte[]</td><td>changeEvents[0].valueData.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeEvents[1].objectId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents[1].objectType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeEvents[1].fieldId</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents[1].valueType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeEvents[1].valueData.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents[1].valueData</td><td>byte[]</td><td>changeEvents[1].valueData.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeEvents[2].objectId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents[2].objectType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeEvents[2].fieldId</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents[2].valueType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>changeEvents[2].valueData.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>changeEvents[2].valueData</td><td>byte[]</td><td>changeEvents[2].valueData.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>indexOfenterSpritesInArray.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>indexOfenterSpritesInArray</td><td>int[]</td><td>indexOfenterSpritesInArray.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSpritesInArray.length</td><td>int</td><td>4个字节</td><td>Sprite数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSpritesInArray[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSpritesInArray[0].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSpritesInArray[0].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSpritesInArray[0].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSpritesInArray[0].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSpritesInArray[0].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSpritesInArray[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSpritesInArray[1].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSpritesInArray[1].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSpritesInArray[1].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSpritesInArray[1].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSpritesInArray[1].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSpritesInArray[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSpritesInArray[2].state</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSpritesInArray[2].direction</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSpritesInArray[2].hp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enterSpritesInArray[2].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterSpritesInArray[2].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>otherValue.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>otherValue[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>otherValue[0]</td><td>String</td><td>otherValue[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>otherValue[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>otherValue[1]</td><td>String</td><td>otherValue[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>otherValue[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>otherValue[2]</td><td>String</td><td>otherValue[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>otherValue2.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>otherValue2</td><td>long[]</td><td>otherValue2.length</td><td>*</td></tr>
 * </table>
 */
public class AROUND_CHANGE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	Player[] enterPlayers;
	Pet[] enterPets;
	Sprite[] enterSprites;
	Player[] leavePlayers;
	Pet[] leavePets;
	Sprite[] leaveSprites;
	MoveTrace4Client[] moveLivings;
	FieldChangeEvent[] changeEvents;
	int[] indexOfenterSpritesInArray;
	Sprite[] enterSpritesInArray;
	String[] otherValue;
	long[] otherValue2;

	public AROUND_CHANGE_REQ(){
	}

	public AROUND_CHANGE_REQ(long seqNum,Player[] enterPlayers,Pet[] enterPets,Sprite[] enterSprites,Player[] leavePlayers,Pet[] leavePets,Sprite[] leaveSprites,MoveTrace4Client[] moveLivings,FieldChangeEvent[] changeEvents,int[] indexOfenterSpritesInArray,Sprite[] enterSpritesInArray,String[] otherValue,long[] otherValue2){
		this.seqNum = seqNum;
		this.enterPlayers = enterPlayers;
		this.enterPets = enterPets;
		this.enterSprites = enterSprites;
		this.leavePlayers = leavePlayers;
		this.leavePets = leavePets;
		this.leaveSprites = leaveSprites;
		this.moveLivings = moveLivings;
		this.changeEvents = changeEvents;
		this.indexOfenterSpritesInArray = indexOfenterSpritesInArray;
		this.enterSpritesInArray = enterSpritesInArray;
		this.otherValue = otherValue;
		this.otherValue2 = otherValue2;
	}

	public AROUND_CHANGE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		enterPlayers = new Player[len];
		for(int i = 0 ; i < enterPlayers.length ; i++){
			enterPlayers[i] = new Player();
			enterPlayers[i].setHold(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setPkMode((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPlayers[i].setStun(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setImmunity(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setPoison(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setWeak(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setInvulnerable(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setCure(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setLiuxueState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setJiansuState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setPojiaState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setZhuoreState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setHanlengState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setTigaoBaojiState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setTigaoWaigongState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setTigaoNeigongState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setTigaoWaifangState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setTigaoNeifangState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setShield((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPlayers[i].setFighting(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setFlying(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setBoothState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setSitdownState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setVitality((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPlayers[i].setMaxVitality((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPlayers[i].setHp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPlayers[i].setMaxHP((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPlayers[i].setMp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPlayers[i].setMaxMP((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPlayers[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPlayers[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			enterPlayers[i].setSex((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPlayers[i].setCountry((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPlayers[i].setCountryPosition((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPlayers[i].setCareer((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPlayers[i].setLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPlayers[i].setSoulLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPlayers[i].setState((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPlayers[i].setDirection((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPlayers[i].setAvataRace(new String(content,offset,len,"UTF-8"));
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
			enterPlayers[i].setAvata(avata_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] avataType_0002 = new byte[len];
			System.arraycopy(content,offset,avataType_0002,0,len);
			offset += len;
			enterPlayers[i].setAvataType(avataType_0002);
			enterPlayers[i].setAura((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPlayers[i].setEncloser((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPlayers[i].setIsUpOrDown(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setCommonAttackSpeed((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			enterPlayers[i].setCommonAttackRange((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			enterPlayers[i].setSpeed((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPlayers[i].setZongPaiName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPlayers[i].setTitle(new String(content,offset,len,"UTF-8"));
			offset += len;
			enterPlayers[i].setChuangongState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setTeamMark((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPlayers[i].setX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPlayers[i].setY((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPlayers[i].setInBattleField(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setBattleFieldSide((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPlayers[i].setNameColorType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPlayers[i].setJiazuTitle(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPlayers[i].setJiazuIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			enterPlayers[i].setSkillUsingState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPlayers[i].setWeaponParticle(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPlayers[i].setHorseParticle(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPlayers[i].setSuitBodyParticle(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPlayers[i].setSuitFootParticle(new String(content,offset,len,"UTF-8"));
			offset += len;
			enterPlayers[i].setJiazuId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPlayers[i].setBoothName(new String(content,offset,len,"UTF-8"));
			offset += len;
			enterPlayers[i].setCitanStateLevel((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPlayers[i].setTouzhuanStateLevel((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPlayers[i].setObjectScale((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			enterPlayers[i].setObjectColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPlayers[i].setObjectOpacity(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPlayers[i].setSpouse(new String(content,offset,len,"UTF-8"));
			offset += len;
			enterPlayers[i].setBeerState((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPlayers[i].setVipLevel((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPlayers[i].setCityFightSide((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPlayers[i].setIsGuozhan(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPlayers[i].setGuozhanLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		enterPets = new Pet[len];
		for(int i = 0 ; i < enterPets.length ; i++){
			enterPets[i] = new Pet();
			enterPets[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPets[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPets[i].setOwnerName(new String(content,offset,len,"UTF-8"));
			offset += len;
			enterPets[i].setOwnerId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPets[i].setCategory(new String(content,offset,len,"UTF-8"));
			offset += len;
			enterPets[i].setCareer((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setCharacter((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setSex((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setBreedTimes((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setBreededTimes((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setLifeTime((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setHappyness((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setLevel((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			enterPets[i].setExp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setNextLevelExp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setQuality((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setStarClass((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setGeneration((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setVariation((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setStrengthQuality((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setDexterityQuality((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setSpellpowerQuality((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setConstitutionQuality((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setDingliQuality((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setTrainLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setRarity((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setGrowthClass((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setGrowth((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] skillIds_0003 = new int[len];
			for(int j = 0 ; j < skillIds_0003.length ; j++){
				skillIds_0003[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			enterPets[i].setSkillIds(skillIds_0003);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] activeSkillLevels_0004 = new int[len];
			for(int j = 0 ; j < activeSkillLevels_0004.length ; j++){
				activeSkillLevels_0004[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			enterPets[i].setActiveSkillLevels(activeSkillLevels_0004);
			enterPets[i].setPetPropsId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			enterPets[i].setStrength((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setDexterity((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setSpellpower((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setConstitution((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setDingli((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setPhyAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setPhyDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setMagicAttack((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setMagicDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setMaxHP((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setHp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setCriticalHit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setHit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setDodge((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setFireDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setWindDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setBlizzardDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setThunderDefence((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setUnAllocatedPoints((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setActivationType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPets[i].setAvataRace(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPets[i].setAvataSex(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] avata_0005 = new String[len];
			for(int j = 0 ; j < avata_0005.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				avata_0005[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			enterPets[i].setAvata(avata_0005);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] avataType_0006 = new byte[len];
			System.arraycopy(content,offset,avataType_0006,0,len);
			offset += len;
			enterPets[i].setAvataType(avataType_0006);
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPets[i].setTitle(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPets[i].setIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			enterPets[i].setState((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setDirection((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setY((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setInBattleField(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPets[i].setBattleFieldSide((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setHold(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPets[i].setStun(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPets[i].setImmunity(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPets[i].setPoison(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPets[i].setWeak(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPets[i].setInvulnerable(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPets[i].setShield((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setAura((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setHeight((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			enterPets[i].setSpriteType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setMonsterType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setNpcType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setTaskMark(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPets[i].setCountry((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterPets[i].setSpeed((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			enterPets[i].setSkillUsingState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterPets[i].setNameColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setObjectScale((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			enterPets[i].setObjectColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterPets[i].setObjectOpacity(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPets[i].setParticleName(new String(content,offset,len,"UTF-8"));
			offset += len;
			enterPets[i].setParticleX((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			enterPets[i].setParticleY((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterPets[i].setFootParticleName(new String(content,offset,len,"UTF-8"));
			offset += len;
			enterPets[i].setFootParticleX((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			enterPets[i].setFootParticleY((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		enterSprites = new Sprite[len];
		for(int i = 0 ; i < enterSprites.length ; i++){
			enterSprites[i] = new Sprite();
			enterSprites[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterSprites[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterSprites[i].setTitle(new String(content,offset,len,"UTF-8"));
			offset += len;
			enterSprites[i].setLevel((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			enterSprites[i].setState((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterSprites[i].setDirection((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterSprites[i].setHp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterSprites[i].setMaxHP((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterSprites[i].setIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			enterSprites[i].setX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterSprites[i].setY((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterSprites[i].setInBattleField(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterSprites[i].setBattleFieldSide((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterSprites[i].setHold(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterSprites[i].setStun(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterSprites[i].setImmunity(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterSprites[i].setPoison(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterSprites[i].setWeak(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterSprites[i].setInvulnerable(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterSprites[i].setShield((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterSprites[i].setAura((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterSprites[i].setAvataAction(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterSprites[i].setAvataRace(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterSprites[i].setAvataSex(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] avata_0007 = new String[len];
			for(int j = 0 ; j < avata_0007.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				avata_0007[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			enterSprites[i].setAvata(avata_0007);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] avataType_0008 = new byte[len];
			System.arraycopy(content,offset,avataType_0008,0,len);
			offset += len;
			enterSprites[i].setAvataType(avataType_0008);
			enterSprites[i].setHeight((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			enterSprites[i].setSpriteType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterSprites[i].setMonsterType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterSprites[i].setNpcType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterSprites[i].setTaskMark(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterSprites[i].setCountry((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterSprites[i].setSpeed((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			enterSprites[i].setSkillUsingState(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			enterSprites[i].setNameColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterSprites[i].setObjectScale((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			enterSprites[i].setObjectColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterSprites[i].setObjectOpacity(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterSprites[i].setParticleName(new String(content,offset,len,"UTF-8"));
			offset += len;
			enterSprites[i].setParticleX((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			enterSprites[i].setParticleY((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			enterSprites[i].setFootParticleName(new String(content,offset,len,"UTF-8"));
			offset += len;
			enterSprites[i].setFootParticleX((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			enterSprites[i].setFootParticleY((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			enterSprites[i].setClassLevel((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		leavePlayers = new Player[len];
		for(int i = 0 ; i < leavePlayers.length ; i++){
			leavePlayers[i] = new Player();
			leavePlayers[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		leavePets = new Pet[len];
		for(int i = 0 ; i < leavePets.length ; i++){
			leavePets[i] = new Pet();
			leavePets[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		leaveSprites = new Sprite[len];
		for(int i = 0 ; i < leaveSprites.length ; i++){
			leaveSprites[i] = new Sprite();
			leaveSprites[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		moveLivings = new MoveTrace4Client[len];
		for(int i = 0 ; i < moveLivings.length ; i++){
			moveLivings[i] = new MoveTrace4Client();
			moveLivings[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			moveLivings[i].setStartTimestamp((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			moveLivings[i].setDestineTimestamp((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] roadLen_0009 = new short[len];
			for(int j = 0 ; j < roadLen_0009.length ; j++){
				roadLen_0009[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			moveLivings[i].setRoadLen(roadLen_0009);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] pointsX_0010 = new short[len];
			for(int j = 0 ; j < pointsX_0010.length ; j++){
				pointsX_0010[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			moveLivings[i].setPointsX(pointsX_0010);
			moveLivings[i].setType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] pointsY_0011 = new short[len];
			for(int j = 0 ; j < pointsY_0011.length ; j++){
				pointsY_0011[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			moveLivings[i].setPointsY(pointsY_0011);
			moveLivings[i].setSpeed((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		changeEvents = new FieldChangeEvent[len];
		for(int i = 0 ; i < changeEvents.length ; i++){
			changeEvents[i] = new FieldChangeEvent();
			changeEvents[i].setObjectId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			changeEvents[i].setObjectType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			changeEvents[i].setFieldId((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			changeEvents[i].setValueType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] valueData_0012 = new byte[len];
			System.arraycopy(content,offset,valueData_0012,0,len);
			offset += len;
			changeEvents[i].setValueData(valueData_0012);
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		indexOfenterSpritesInArray = new int[len];
		for(int i = 0 ; i < indexOfenterSpritesInArray.length ; i++){
			indexOfenterSpritesInArray[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		enterSpritesInArray = new Sprite[len];
		for(int i = 0 ; i < enterSpritesInArray.length ; i++){
			enterSpritesInArray[i] = new Sprite();
			enterSpritesInArray[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			enterSpritesInArray[i].setState((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterSpritesInArray[i].setDirection((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			enterSpritesInArray[i].setHp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterSpritesInArray[i].setX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			enterSpritesInArray[i].setY((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		otherValue = new String[len];
		for(int i = 0 ; i < otherValue.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			otherValue[i] = new String(content,offset,len);
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		otherValue2 = new long[len];
		for(int i = 0 ; i < otherValue2.length ; i++){
			otherValue2[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x001100D0;
	}

	public String getTypeDescription() {
		return "AROUND_CHANGE_REQ";
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
		for(int i = 0 ; i < enterPlayers.length ; i++){
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
			len += 1;
			len += 1;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 8;
			len += 2;
			if(enterPlayers[i].getName() != null){
				try{
				len += enterPlayers[i].getName().getBytes("UTF-8").length;
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
			len += 1;
			len += 1;
			len += 2;
			if(enterPlayers[i].getAvataRace() != null){
				try{
				len += enterPlayers[i].getAvataRace().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] avata = enterPlayers[i].getAvata();
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
			len += enterPlayers[i].getAvataType().length * 1;
			len += 1;
			len += 1;
			len += 1;
			len += 2;
			len += 2;
			len += 2;
			len += 2;
			if(enterPlayers[i].getZongPaiName() != null){
				try{
				len += enterPlayers[i].getZongPaiName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(enterPlayers[i].getTitle() != null){
				try{
				len += enterPlayers[i].getTitle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 1;
			len += 4;
			len += 4;
			len += 1;
			len += 1;
			len += 1;
			len += 2;
			if(enterPlayers[i].getJiazuTitle() != null){
				try{
				len += enterPlayers[i].getJiazuTitle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(enterPlayers[i].getJiazuIcon() != null){
				try{
				len += enterPlayers[i].getJiazuIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 2;
			if(enterPlayers[i].getWeaponParticle() != null){
				try{
				len += enterPlayers[i].getWeaponParticle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(enterPlayers[i].getHorseParticle() != null){
				try{
				len += enterPlayers[i].getHorseParticle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(enterPlayers[i].getSuitBodyParticle() != null){
				try{
				len += enterPlayers[i].getSuitBodyParticle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(enterPlayers[i].getSuitFootParticle() != null){
				try{
				len += enterPlayers[i].getSuitFootParticle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 8;
			len += 2;
			if(enterPlayers[i].getBoothName() != null){
				try{
				len += enterPlayers[i].getBoothName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 1;
			len += 2;
			len += 4;
			len += 1;
			len += 2;
			if(enterPlayers[i].getSpouse() != null){
				try{
				len += enterPlayers[i].getSpouse().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 1;
			len += 1;
			len += 1;
			len += 4;
		}
		len += 4;
		for(int i = 0 ; i < enterPets.length ; i++){
			len += 8;
			len += 2;
			if(enterPets[i].getName() != null){
				try{
				len += enterPets[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(enterPets[i].getOwnerName() != null){
				try{
				len += enterPets[i].getOwnerName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 8;
			len += 2;
			if(enterPets[i].getCategory() != null){
				try{
				len += enterPets[i].getCategory().getBytes("UTF-8").length;
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
			len += 1;
			len += 1;
			len += 4;
			len += 4;
			len += enterPets[i].getSkillIds().length * 4;
			len += 4;
			len += enterPets[i].getActiveSkillLevels().length * 4;
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
			len += 1;
			len += 2;
			if(enterPets[i].getAvataRace() != null){
				try{
				len += enterPets[i].getAvataRace().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(enterPets[i].getAvataSex() != null){
				try{
				len += enterPets[i].getAvataSex().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] avata = enterPets[i].getAvata();
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
			len += enterPets[i].getAvataType().length * 1;
			len += 2;
			if(enterPets[i].getTitle() != null){
				try{
				len += enterPets[i].getTitle().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(enterPets[i].getIcon() != null){
				try{
				len += enterPets[i].getIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 1;
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
			if(enterPets[i].getParticleName() != null){
				try{
				len += enterPets[i].getParticleName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 2;
			len += 2;
			if(enterPets[i].getFootParticleName() != null){
				try{
				len += enterPets[i].getFootParticleName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 2;
		}
		len += 4;
		for(int i = 0 ; i < enterSprites.length ; i++){
			len += 8;
			len += 2;
			if(enterSprites[i].getName() != null){
				try{
				len += enterSprites[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(enterSprites[i].getTitle() != null){
				try{
				len += enterSprites[i].getTitle().getBytes("UTF-8").length;
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
			if(enterSprites[i].getIcon() != null){
				try{
				len += enterSprites[i].getIcon().getBytes("UTF-8").length;
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
			if(enterSprites[i].getAvataAction() != null){
				try{
				len += enterSprites[i].getAvataAction().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(enterSprites[i].getAvataRace() != null){
				try{
				len += enterSprites[i].getAvataRace().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(enterSprites[i].getAvataSex() != null){
				try{
				len += enterSprites[i].getAvataSex().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] avata = enterSprites[i].getAvata();
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
			len += enterSprites[i].getAvataType().length * 1;
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
			if(enterSprites[i].getParticleName() != null){
				try{
				len += enterSprites[i].getParticleName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 2;
			len += 2;
			if(enterSprites[i].getFootParticleName() != null){
				try{
				len += enterSprites[i].getFootParticleName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 2;
			len += 2;
		}
		len += 4;
		for(int i = 0 ; i < leavePlayers.length ; i++){
			len += 8;
		}
		len += 4;
		for(int i = 0 ; i < leavePets.length ; i++){
			len += 8;
		}
		len += 4;
		for(int i = 0 ; i < leaveSprites.length ; i++){
			len += 8;
		}
		len += 4;
		for(int i = 0 ; i < moveLivings.length ; i++){
			len += 8;
			len += 8;
			len += 8;
			len += 4;
			len += moveLivings[i].getRoadLen().length * 2;
			len += 4;
			len += moveLivings[i].getPointsX().length * 2;
			len += 1;
			len += 4;
			len += moveLivings[i].getPointsY().length * 2;
			len += 4;
		}
		len += 4;
		for(int i = 0 ; i < changeEvents.length ; i++){
			len += 8;
			len += 1;
			len += 2;
			len += 1;
			len += 4;
			len += changeEvents[i].getValueData().length * 1;
		}
		len += 4;
		len += indexOfenterSpritesInArray.length * 4;
		len += 4;
		for(int i = 0 ; i < enterSpritesInArray.length ; i++){
			len += 8;
			len += 1;
			len += 1;
			len += 4;
			len += 4;
			len += 4;
		}
		len += 4;
		for(int i = 0 ; i < otherValue.length; i++){
			len += 2;
			len += otherValue[i].getBytes().length;
		}
		len += 4;
		len += otherValue2.length * 8;
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

			buffer.putInt(enterPlayers.length);
			for(int i = 0 ; i < enterPlayers.length ; i++){
				buffer.put((byte)(enterPlayers[i].isHold()==false?0:1));
				buffer.put((byte)enterPlayers[i].getPkMode());
				buffer.put((byte)(enterPlayers[i].isStun()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isImmunity()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isPoison()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isWeak()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isInvulnerable()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isCure()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isLiuxueState()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isJiansuState()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isPojiaState()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isZhuoreState()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isHanlengState()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isTigaoBaojiState()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isTigaoWaigongState()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isTigaoNeigongState()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isTigaoWaifangState()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isTigaoNeifangState()==false?0:1));
				buffer.put((byte)enterPlayers[i].getShield());
				buffer.put((byte)(enterPlayers[i].isFighting()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isFlying()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isBoothState()==false?0:1));
				buffer.put((byte)(enterPlayers[i].isSitdownState()==false?0:1));
				buffer.putInt((int)enterPlayers[i].getVitality());
				buffer.putInt((int)enterPlayers[i].getMaxVitality());
				buffer.putInt((int)enterPlayers[i].getHp());
				buffer.putInt((int)enterPlayers[i].getMaxHP());
				buffer.putInt((int)enterPlayers[i].getMp());
				buffer.putInt((int)enterPlayers[i].getMaxMP());
				buffer.putLong(enterPlayers[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = enterPlayers[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)enterPlayers[i].getSex());
				buffer.put((byte)enterPlayers[i].getCountry());
				buffer.putInt((int)enterPlayers[i].getCountryPosition());
				buffer.put((byte)enterPlayers[i].getCareer());
				buffer.putInt((int)enterPlayers[i].getLevel());
				buffer.putInt((int)enterPlayers[i].getSoulLevel());
				buffer.put((byte)enterPlayers[i].getState());
				buffer.put((byte)enterPlayers[i].getDirection());
				try{
				tmpBytes2 = enterPlayers[i].getAvataRace().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(enterPlayers[i].getAvata().length);
				String[] avata_0013 = enterPlayers[i].getAvata();
				for(int j = 0 ; j < avata_0013.length ; j++){
				try{
					buffer.putShort((short)avata_0013[j].getBytes("UTF-8").length);
					buffer.put(avata_0013[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(enterPlayers[i].getAvataType().length);
				buffer.put(enterPlayers[i].getAvataType());
				buffer.put((byte)enterPlayers[i].getAura());
				buffer.put((byte)enterPlayers[i].getEncloser());
				buffer.put((byte)(enterPlayers[i].isIsUpOrDown()==false?0:1));
				buffer.putShort((short)enterPlayers[i].getCommonAttackSpeed());
				buffer.putShort((short)enterPlayers[i].getCommonAttackRange());
				buffer.putShort((short)enterPlayers[i].getSpeed());
				try{
				tmpBytes2 = enterPlayers[i].getZongPaiName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = enterPlayers[i].getTitle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(enterPlayers[i].isChuangongState()==false?0:1));
				buffer.put((byte)enterPlayers[i].getTeamMark());
				buffer.putInt((int)enterPlayers[i].getX());
				buffer.putInt((int)enterPlayers[i].getY());
				buffer.put((byte)(enterPlayers[i].isInBattleField()==false?0:1));
				buffer.put((byte)enterPlayers[i].getBattleFieldSide());
				buffer.put((byte)enterPlayers[i].getNameColorType());
				try{
				tmpBytes2 = enterPlayers[i].getJiazuTitle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = enterPlayers[i].getJiazuIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(enterPlayers[i].isSkillUsingState()==false?0:1));
				try{
				tmpBytes2 = enterPlayers[i].getWeaponParticle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = enterPlayers[i].getHorseParticle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = enterPlayers[i].getSuitBodyParticle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = enterPlayers[i].getSuitFootParticle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putLong(enterPlayers[i].getJiazuId());
				try{
				tmpBytes2 = enterPlayers[i].getBoothName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)enterPlayers[i].getCitanStateLevel());
				buffer.put((byte)enterPlayers[i].getTouzhuanStateLevel());
				buffer.putShort((short)enterPlayers[i].getObjectScale());
				buffer.putInt((int)enterPlayers[i].getObjectColor());
				buffer.put((byte)(enterPlayers[i].isObjectOpacity()==false?0:1));
				try{
				tmpBytes2 = enterPlayers[i].getSpouse().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)enterPlayers[i].getBeerState());
				buffer.put((byte)enterPlayers[i].getVipLevel());
				buffer.put((byte)enterPlayers[i].getCityFightSide());
				buffer.put((byte)(enterPlayers[i].isIsGuozhan()==false?0:1));
				buffer.putInt((int)enterPlayers[i].getGuozhanLevel());
			}
			buffer.putInt(enterPets.length);
			for(int i = 0 ; i < enterPets.length ; i++){
				buffer.putLong(enterPets[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = enterPets[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = enterPets[i].getOwnerName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putLong(enterPets[i].getOwnerId());
				try{
				tmpBytes2 = enterPets[i].getCategory().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)enterPets[i].getCareer());
				buffer.put((byte)enterPets[i].getCharacter());
				buffer.put((byte)enterPets[i].getSex());
				buffer.put((byte)enterPets[i].getBreedTimes());
				buffer.put((byte)enterPets[i].getBreededTimes());
				buffer.putInt((int)enterPets[i].getLifeTime());
				buffer.putInt((int)enterPets[i].getHappyness());
				buffer.putShort((short)enterPets[i].getLevel());
				buffer.putInt((int)enterPets[i].getExp());
				buffer.putInt((int)enterPets[i].getNextLevelExp());
				buffer.put((byte)enterPets[i].getQuality());
				buffer.put((byte)enterPets[i].getStarClass());
				buffer.put((byte)enterPets[i].getGeneration());
				buffer.put((byte)enterPets[i].getVariation());
				buffer.putInt((int)enterPets[i].getStrengthQuality());
				buffer.putInt((int)enterPets[i].getDexterityQuality());
				buffer.putInt((int)enterPets[i].getSpellpowerQuality());
				buffer.putInt((int)enterPets[i].getConstitutionQuality());
				buffer.putInt((int)enterPets[i].getDingliQuality());
				buffer.putInt((int)enterPets[i].getTrainLevel());
				buffer.put((byte)enterPets[i].getRarity());
				buffer.put((byte)enterPets[i].getGrowthClass());
				buffer.putInt((int)enterPets[i].getGrowth());
				buffer.putInt(enterPets[i].getSkillIds().length);
				int[] skillIds_0014 = enterPets[i].getSkillIds();
				for(int j = 0 ; j < skillIds_0014.length ; j++){
					buffer.putInt(skillIds_0014[j]);
				}
				buffer.putInt(enterPets[i].getActiveSkillLevels().length);
				int[] activeSkillLevels_0015 = enterPets[i].getActiveSkillLevels();
				for(int j = 0 ; j < activeSkillLevels_0015.length ; j++){
					buffer.putInt(activeSkillLevels_0015[j]);
				}
				buffer.putLong(enterPets[i].getPetPropsId());
				buffer.putInt((int)enterPets[i].getStrength());
				buffer.putInt((int)enterPets[i].getDexterity());
				buffer.putInt((int)enterPets[i].getSpellpower());
				buffer.putInt((int)enterPets[i].getConstitution());
				buffer.putInt((int)enterPets[i].getDingli());
				buffer.putInt((int)enterPets[i].getPhyAttack());
				buffer.putInt((int)enterPets[i].getPhyDefence());
				buffer.putInt((int)enterPets[i].getMagicAttack());
				buffer.putInt((int)enterPets[i].getMagicDefence());
				buffer.putInt((int)enterPets[i].getMaxHP());
				buffer.putInt((int)enterPets[i].getHp());
				buffer.putInt((int)enterPets[i].getCriticalHit());
				buffer.putInt((int)enterPets[i].getHit());
				buffer.putInt((int)enterPets[i].getDodge());
				buffer.putInt((int)enterPets[i].getFireDefence());
				buffer.putInt((int)enterPets[i].getWindDefence());
				buffer.putInt((int)enterPets[i].getBlizzardDefence());
				buffer.putInt((int)enterPets[i].getThunderDefence());
				buffer.putInt((int)enterPets[i].getUnAllocatedPoints());
				buffer.put((byte)enterPets[i].getActivationType());
				try{
				tmpBytes2 = enterPets[i].getAvataRace().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = enterPets[i].getAvataSex().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(enterPets[i].getAvata().length);
				String[] avata_0016 = enterPets[i].getAvata();
				for(int j = 0 ; j < avata_0016.length ; j++){
				try{
					buffer.putShort((short)avata_0016[j].getBytes("UTF-8").length);
					buffer.put(avata_0016[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(enterPets[i].getAvataType().length);
				buffer.put(enterPets[i].getAvataType());
				try{
				tmpBytes2 = enterPets[i].getTitle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = enterPets[i].getIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)enterPets[i].getState());
				buffer.put((byte)enterPets[i].getDirection());
				buffer.putInt((int)enterPets[i].getX());
				buffer.putInt((int)enterPets[i].getY());
				buffer.put((byte)(enterPets[i].isInBattleField()==false?0:1));
				buffer.put((byte)enterPets[i].getBattleFieldSide());
				buffer.put((byte)(enterPets[i].isHold()==false?0:1));
				buffer.put((byte)(enterPets[i].isStun()==false?0:1));
				buffer.put((byte)(enterPets[i].isImmunity()==false?0:1));
				buffer.put((byte)(enterPets[i].isPoison()==false?0:1));
				buffer.put((byte)(enterPets[i].isWeak()==false?0:1));
				buffer.put((byte)(enterPets[i].isInvulnerable()==false?0:1));
				buffer.put((byte)enterPets[i].getShield());
				buffer.put((byte)enterPets[i].getAura());
				buffer.putShort((short)enterPets[i].getHeight());
				buffer.put((byte)enterPets[i].getSpriteType());
				buffer.put((byte)enterPets[i].getMonsterType());
				buffer.put((byte)enterPets[i].getNpcType());
				buffer.put((byte)(enterPets[i].isTaskMark()==false?0:1));
				buffer.put((byte)enterPets[i].getCountry());
				buffer.putShort((short)enterPets[i].getSpeed());
				buffer.put((byte)(enterPets[i].isSkillUsingState()==false?0:1));
				buffer.putInt((int)enterPets[i].getNameColor());
				buffer.putShort((short)enterPets[i].getObjectScale());
				buffer.putInt((int)enterPets[i].getObjectColor());
				buffer.put((byte)(enterPets[i].isObjectOpacity()==false?0:1));
				try{
				tmpBytes2 = enterPets[i].getParticleName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)enterPets[i].getParticleX());
				buffer.putShort((short)enterPets[i].getParticleY());
				try{
				tmpBytes2 = enterPets[i].getFootParticleName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)enterPets[i].getFootParticleX());
				buffer.putShort((short)enterPets[i].getFootParticleY());
			}
			buffer.putInt(enterSprites.length);
			for(int i = 0 ; i < enterSprites.length ; i++){
				buffer.putLong(enterSprites[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = enterSprites[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = enterSprites[i].getTitle().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)enterSprites[i].getLevel());
				buffer.put((byte)enterSprites[i].getState());
				buffer.put((byte)enterSprites[i].getDirection());
				buffer.putInt((int)enterSprites[i].getHp());
				buffer.putInt((int)enterSprites[i].getMaxHP());
				try{
				tmpBytes2 = enterSprites[i].getIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)enterSprites[i].getX());
				buffer.putInt((int)enterSprites[i].getY());
				buffer.put((byte)(enterSprites[i].isInBattleField()==false?0:1));
				buffer.put((byte)enterSprites[i].getBattleFieldSide());
				buffer.put((byte)(enterSprites[i].isHold()==false?0:1));
				buffer.put((byte)(enterSprites[i].isStun()==false?0:1));
				buffer.put((byte)(enterSprites[i].isImmunity()==false?0:1));
				buffer.put((byte)(enterSprites[i].isPoison()==false?0:1));
				buffer.put((byte)(enterSprites[i].isWeak()==false?0:1));
				buffer.put((byte)(enterSprites[i].isInvulnerable()==false?0:1));
				buffer.put((byte)enterSprites[i].getShield());
				buffer.put((byte)enterSprites[i].getAura());
				try{
				tmpBytes2 = enterSprites[i].getAvataAction().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = enterSprites[i].getAvataRace().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = enterSprites[i].getAvataSex().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(enterSprites[i].getAvata().length);
				String[] avata_0017 = enterSprites[i].getAvata();
				for(int j = 0 ; j < avata_0017.length ; j++){
				try{
					buffer.putShort((short)avata_0017[j].getBytes("UTF-8").length);
					buffer.put(avata_0017[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(enterSprites[i].getAvataType().length);
				buffer.put(enterSprites[i].getAvataType());
				buffer.putShort((short)enterSprites[i].getHeight());
				buffer.put((byte)enterSprites[i].getSpriteType());
				buffer.put((byte)enterSprites[i].getMonsterType());
				buffer.put((byte)enterSprites[i].getNpcType());
				buffer.put((byte)(enterSprites[i].isTaskMark()==false?0:1));
				buffer.put((byte)enterSprites[i].getCountry());
				buffer.putShort((short)enterSprites[i].getSpeed());
				buffer.put((byte)(enterSprites[i].isSkillUsingState()==false?0:1));
				buffer.putInt((int)enterSprites[i].getNameColor());
				buffer.putShort((short)enterSprites[i].getObjectScale());
				buffer.putInt((int)enterSprites[i].getObjectColor());
				buffer.put((byte)(enterSprites[i].isObjectOpacity()==false?0:1));
				try{
				tmpBytes2 = enterSprites[i].getParticleName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)enterSprites[i].getParticleX());
				buffer.putShort((short)enterSprites[i].getParticleY());
				try{
				tmpBytes2 = enterSprites[i].getFootParticleName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)enterSprites[i].getFootParticleX());
				buffer.putShort((short)enterSprites[i].getFootParticleY());
				buffer.putShort((short)enterSprites[i].getClassLevel());
			}
			buffer.putInt(leavePlayers.length);
			for(int i = 0 ; i < leavePlayers.length ; i++){
				buffer.putLong(leavePlayers[i].getId());
			}
			buffer.putInt(leavePets.length);
			for(int i = 0 ; i < leavePets.length ; i++){
				buffer.putLong(leavePets[i].getId());
			}
			buffer.putInt(leaveSprites.length);
			for(int i = 0 ; i < leaveSprites.length ; i++){
				buffer.putLong(leaveSprites[i].getId());
			}
			buffer.putInt(moveLivings.length);
			for(int i = 0 ; i < moveLivings.length ; i++){
				buffer.putLong(moveLivings[i].getId());
				buffer.putLong(moveLivings[i].getStartTimestamp());
				buffer.putLong(moveLivings[i].getDestineTimestamp());
				buffer.putInt(moveLivings[i].getRoadLen().length);
				short[] roadLen_0018 = moveLivings[i].getRoadLen();
				for(int j = 0 ; j < roadLen_0018.length ; j++){
					buffer.putShort(roadLen_0018[j]);
				}
				buffer.putInt(moveLivings[i].getPointsX().length);
				short[] pointsX_0019 = moveLivings[i].getPointsX();
				for(int j = 0 ; j < pointsX_0019.length ; j++){
					buffer.putShort(pointsX_0019[j]);
				}
				buffer.put((byte)moveLivings[i].getType());
				buffer.putInt(moveLivings[i].getPointsY().length);
				short[] pointsY_0020 = moveLivings[i].getPointsY();
				for(int j = 0 ; j < pointsY_0020.length ; j++){
					buffer.putShort(pointsY_0020[j]);
				}
				buffer.putInt((int)moveLivings[i].getSpeed());
			}
			buffer.putInt(changeEvents.length);
			for(int i = 0 ; i < changeEvents.length ; i++){
				buffer.putLong(changeEvents[i].getObjectId());
				buffer.put((byte)changeEvents[i].getObjectType());
				buffer.putShort((short)changeEvents[i].getFieldId());
				buffer.put((byte)changeEvents[i].getValueType());
				buffer.putInt(changeEvents[i].getValueData().length);
				buffer.put(changeEvents[i].getValueData());
			}
			buffer.putInt(indexOfenterSpritesInArray.length);
			for(int i = 0 ; i < indexOfenterSpritesInArray.length; i++){
				buffer.putInt(indexOfenterSpritesInArray[i]);
			}
			buffer.putInt(enterSpritesInArray.length);
			for(int i = 0 ; i < enterSpritesInArray.length ; i++){
				buffer.putLong(enterSpritesInArray[i].getId());
				buffer.put((byte)enterSpritesInArray[i].getState());
				buffer.put((byte)enterSpritesInArray[i].getDirection());
				buffer.putInt((int)enterSpritesInArray[i].getHp());
				buffer.putInt((int)enterSpritesInArray[i].getX());
				buffer.putInt((int)enterSpritesInArray[i].getY());
			}
			buffer.putInt(otherValue.length);
			for(int i = 0 ; i < otherValue.length; i++){
				byte[] tmpBytes2 ;
				tmpBytes2 = otherValue[i].getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(otherValue2.length);
			for(int i = 0 ; i < otherValue2.length; i++){
				buffer.putLong(otherValue2[i]);
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
	 *	进入到周围的玩家
	 */
	public Player[] getEnterPlayers(){
		return enterPlayers;
	}

	/**
	 * 设置属性：
	 *	进入到周围的玩家
	 */
	public void setEnterPlayers(Player[] enterPlayers){
		this.enterPlayers = enterPlayers;
	}

	/**
	 * 获取属性：
	 *	进入到周围的宠物
	 */
	public Pet[] getEnterPets(){
		return enterPets;
	}

	/**
	 * 设置属性：
	 *	进入到周围的宠物
	 */
	public void setEnterPets(Pet[] enterPets){
		this.enterPets = enterPets;
	}

	/**
	 * 获取属性：
	 *	进入到周围的精灵
	 */
	public Sprite[] getEnterSprites(){
		return enterSprites;
	}

	/**
	 * 设置属性：
	 *	进入到周围的精灵
	 */
	public void setEnterSprites(Sprite[] enterSprites){
		this.enterSprites = enterSprites;
	}

	/**
	 * 获取属性：
	 *	离开周围的玩家
	 */
	public Player[] getLeavePlayers(){
		return leavePlayers;
	}

	/**
	 * 设置属性：
	 *	离开周围的玩家
	 */
	public void setLeavePlayers(Player[] leavePlayers){
		this.leavePlayers = leavePlayers;
	}

	/**
	 * 获取属性：
	 *	离开周围的精灵
	 */
	public Pet[] getLeavePets(){
		return leavePets;
	}

	/**
	 * 设置属性：
	 *	离开周围的精灵
	 */
	public void setLeavePets(Pet[] leavePets){
		this.leavePets = leavePets;
	}

	/**
	 * 获取属性：
	 *	离开周围的精灵
	 */
	public Sprite[] getLeaveSprites(){
		return leaveSprites;
	}

	/**
	 * 设置属性：
	 *	离开周围的精灵
	 */
	public void setLeaveSprites(Sprite[] leaveSprites){
		this.leaveSprites = leaveSprites;
	}

	/**
	 * 获取属性：
	 *	玩家移动
	 */
	public MoveTrace4Client[] getMoveLivings(){
		return moveLivings;
	}

	/**
	 * 设置属性：
	 *	玩家移动
	 */
	public void setMoveLivings(MoveTrace4Client[] moveLivings){
		this.moveLivings = moveLivings;
	}

	/**
	 * 获取属性：
	 *	周围发生的状态变化
	 */
	public FieldChangeEvent[] getChangeEvents(){
		return changeEvents;
	}

	/**
	 * 设置属性：
	 *	周围发生的状态变化
	 */
	public void setChangeEvents(FieldChangeEvent[] changeEvents){
		this.changeEvents = changeEvents;
	}

	/**
	 * 获取属性：
	 *	enterSpritesInArray数据中各个元素对应的在SPRITE_IN_GAME中的下标
	 */
	public int[] getIndexOfenterSpritesInArray(){
		return indexOfenterSpritesInArray;
	}

	/**
	 * 设置属性：
	 *	enterSpritesInArray数据中各个元素对应的在SPRITE_IN_GAME中的下标
	 */
	public void setIndexOfenterSpritesInArray(int[] indexOfenterSpritesInArray){
		this.indexOfenterSpritesInArray = indexOfenterSpritesInArray;
	}

	/**
	 * 获取属性：
	 *	进入到周围的精灵，但数据都在事先传递的数组中
	 */
	public Sprite[] getEnterSpritesInArray(){
		return enterSpritesInArray;
	}

	/**
	 * 设置属性：
	 *	进入到周围的精灵，但数据都在事先传递的数组中
	 */
	public void setEnterSpritesInArray(Sprite[] enterSpritesInArray){
		this.enterSpritesInArray = enterSpritesInArray;
	}

	/**
	 * 获取属性：
	 *	otherValue
	 */
	public String[] getOtherValue(){
		return otherValue;
	}

	/**
	 * 设置属性：
	 *	otherValue
	 */
	public void setOtherValue(String[] otherValue){
		this.otherValue = otherValue;
	}

	/**
	 * 获取属性：
	 *	otherValue2
	 */
	public long[] getOtherValue2(){
		return otherValue2;
	}

	/**
	 * 设置属性：
	 *	otherValue2
	 */
	public void setOtherValue2(long[] otherValue2){
		this.otherValue2 = otherValue2;
	}

}
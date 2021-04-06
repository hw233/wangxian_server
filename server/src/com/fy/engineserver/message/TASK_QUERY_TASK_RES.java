package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.targets.TaskTarget;
import com.fy.engineserver.newtask.prizes.TaskPrize;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端查询某个任务详细数据<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.name</td><td>String</td><td>task.name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.showType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.des</td><td>String</td><td>task.des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.unDeliverTalk.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.unDeliverTalk</td><td>String</td><td>task.unDeliverTalk.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.startNpc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.startNpc</td><td>String</td><td>task.startNpc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.startMap.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.startMap</td><td>String</td><td>task.startMap.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.startMapResName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.startMapResName</td><td>String</td><td>task.startMapResName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.startTalk.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.startTalk</td><td>String</td><td>task.startTalk.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.startX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.startY</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.startNPCAvataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.startNPCAvataRace</td><td>String</td><td>task.startNPCAvataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.startNPCAvataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.startNPCAvataSex</td><td>String</td><td>task.startNPCAvataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.endNpc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.endNpc</td><td>String</td><td>task.endNpc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.endMap.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.endMap</td><td>String</td><td>task.endMap.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.endMapResName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.endMapResName</td><td>String</td><td>task.endMapResName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.endTalk.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.endTalk</td><td>String</td><td>task.endTalk.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.notice.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.notice</td><td>String</td><td>task.notice.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.endX</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.endY</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.endNPCAvataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.endNPCAvataRace</td><td>String</td><td>task.endNPCAvataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.endNPCAvataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.endNPCAvataSex</td><td>String</td><td>task.endNPCAvataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.thewCost</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.showTarget</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.dailyTaskCycle</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>task.dailyTaskMaxNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>task.reGetCodeDown</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets.length</td><td>int</td><td>4个字节</td><td>TaskTarget数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[0].targetByteType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].targetName.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[0].targetName[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].targetName[0]</td><td>String</td><td>targets[0].targetName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[0].targetName[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].targetName[1]</td><td>String</td><td>targets[0].targetName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[0].targetName[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].targetName[2]</td><td>String</td><td>targets[0].targetName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].targetNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[0].targetColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].mapName.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[0].mapName[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].mapName[0]</td><td>String</td><td>targets[0].mapName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[0].mapName[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].mapName[1]</td><td>String</td><td>targets[0].mapName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[0].mapName[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].mapName[2]</td><td>String</td><td>targets[0].mapName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].resMapName.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[0].resMapName[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].resMapName[0]</td><td>String</td><td>targets[0].resMapName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[0].resMapName[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].resMapName[1]</td><td>String</td><td>targets[0].resMapName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[0].resMapName[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].resMapName[2]</td><td>String</td><td>targets[0].resMapName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].x.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[0].x</td><td>int[]</td><td>targets[0].x.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].y.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[0].y</td><td>int[]</td><td>targets[0].y.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].showMonsterNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[0].showMonsterNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].showMonsterNames[0]</td><td>String</td><td>targets[0].showMonsterNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[0].showMonsterNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].showMonsterNames[1]</td><td>String</td><td>targets[0].showMonsterNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[0].showMonsterNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[0].showMonsterNames[2]</td><td>String</td><td>targets[0].showMonsterNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[1].targetByteType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].targetName.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[1].targetName[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].targetName[0]</td><td>String</td><td>targets[1].targetName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[1].targetName[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].targetName[1]</td><td>String</td><td>targets[1].targetName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[1].targetName[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].targetName[2]</td><td>String</td><td>targets[1].targetName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].targetNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[1].targetColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].mapName.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[1].mapName[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].mapName[0]</td><td>String</td><td>targets[1].mapName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[1].mapName[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].mapName[1]</td><td>String</td><td>targets[1].mapName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[1].mapName[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].mapName[2]</td><td>String</td><td>targets[1].mapName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].resMapName.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[1].resMapName[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].resMapName[0]</td><td>String</td><td>targets[1].resMapName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[1].resMapName[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].resMapName[1]</td><td>String</td><td>targets[1].resMapName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[1].resMapName[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].resMapName[2]</td><td>String</td><td>targets[1].resMapName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].x.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[1].x</td><td>int[]</td><td>targets[1].x.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].y.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[1].y</td><td>int[]</td><td>targets[1].y.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].showMonsterNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[1].showMonsterNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].showMonsterNames[0]</td><td>String</td><td>targets[1].showMonsterNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[1].showMonsterNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].showMonsterNames[1]</td><td>String</td><td>targets[1].showMonsterNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[1].showMonsterNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[1].showMonsterNames[2]</td><td>String</td><td>targets[1].showMonsterNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[2].targetByteType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].targetName.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[2].targetName[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].targetName[0]</td><td>String</td><td>targets[2].targetName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[2].targetName[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].targetName[1]</td><td>String</td><td>targets[2].targetName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[2].targetName[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].targetName[2]</td><td>String</td><td>targets[2].targetName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].targetNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[2].targetColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].mapName.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[2].mapName[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].mapName[0]</td><td>String</td><td>targets[2].mapName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[2].mapName[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].mapName[1]</td><td>String</td><td>targets[2].mapName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[2].mapName[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].mapName[2]</td><td>String</td><td>targets[2].mapName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].resMapName.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[2].resMapName[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].resMapName[0]</td><td>String</td><td>targets[2].resMapName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[2].resMapName[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].resMapName[1]</td><td>String</td><td>targets[2].resMapName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[2].resMapName[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].resMapName[2]</td><td>String</td><td>targets[2].resMapName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].x.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[2].x</td><td>int[]</td><td>targets[2].x.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].y.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[2].y</td><td>int[]</td><td>targets[2].y.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].showMonsterNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[2].showMonsterNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].showMonsterNames[0]</td><td>String</td><td>targets[2].showMonsterNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[2].showMonsterNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].showMonsterNames[1]</td><td>String</td><td>targets[2].showMonsterNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targets[2].showMonsterNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targets[2].showMonsterNames[2]</td><td>String</td><td>targets[2].showMonsterNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes.length</td><td>int</td><td>4个字节</td><td>TaskPrize数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[0].prizeByteType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[0].prizeName.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[0].prizeName[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[0].prizeName[0]</td><td>String</td><td>prizes[0].prizeName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[0].prizeName[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[0].prizeName[1]</td><td>String</td><td>prizes[0].prizeName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[0].prizeName[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[0].prizeName[2]</td><td>String</td><td>prizes[0].prizeName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[0].prizeNum.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[0].prizeNum</td><td>long[]</td><td>prizes[0].prizeNum.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[0].totalNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[0].prizeColor.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[0].prizeColor</td><td>int[]</td><td>prizes[0].prizeColor.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[0].prizeId.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[0].prizeId</td><td>long[]</td><td>prizes[0].prizeId.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[1].prizeByteType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[1].prizeName.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[1].prizeName[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[1].prizeName[0]</td><td>String</td><td>prizes[1].prizeName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[1].prizeName[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[1].prizeName[1]</td><td>String</td><td>prizes[1].prizeName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[1].prizeName[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[1].prizeName[2]</td><td>String</td><td>prizes[1].prizeName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[1].prizeNum.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[1].prizeNum</td><td>long[]</td><td>prizes[1].prizeNum.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[1].totalNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[1].prizeColor.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[1].prizeColor</td><td>int[]</td><td>prizes[1].prizeColor.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[1].prizeId.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[1].prizeId</td><td>long[]</td><td>prizes[1].prizeId.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[2].prizeByteType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[2].prizeName.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[2].prizeName[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[2].prizeName[0]</td><td>String</td><td>prizes[2].prizeName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[2].prizeName[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[2].prizeName[1]</td><td>String</td><td>prizes[2].prizeName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[2].prizeName[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[2].prizeName[2]</td><td>String</td><td>prizes[2].prizeName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[2].prizeNum.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[2].prizeNum</td><td>long[]</td><td>prizes[2].prizeNum.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[2].totalNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[2].prizeColor.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[2].prizeColor</td><td>int[]</td><td>prizes[2].prizeColor.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prizes[2].prizeId.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prizes[2].prizeId</td><td>long[]</td><td>prizes[2].prizeId.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class TASK_QUERY_TASK_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	Task task;
	TaskTarget[] targets;
	TaskPrize[] prizes;

	public TASK_QUERY_TASK_RES(){
	}

	public TASK_QUERY_TASK_RES(long seqNum,Task task,TaskTarget[] targets,TaskPrize[] prizes){
		this.seqNum = seqNum;
		this.task = task;
		this.targets = targets;
		this.prizes = prizes;
	}

	public TASK_QUERY_TASK_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		task = new Task();
		task.setId((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		task.setName(new String(content,offset,len,"UTF-8"));
		offset += len;
		task.setGrade((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		task.setShowType((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		task.setDes(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		task.setUnDeliverTalk(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		task.setStartNpc(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		task.setStartMap(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		task.setStartMapResName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		task.setStartTalk(new String(content,offset,len,"UTF-8"));
		offset += len;
		task.setStartX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		task.setStartY((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		task.setStartNPCAvataRace(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		task.setStartNPCAvataSex(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		task.setEndNpc(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		task.setEndMap(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		task.setEndMapResName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		task.setEndTalk(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		task.setNotice(new String(content,offset,len,"UTF-8"));
		offset += len;
		task.setEndX((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		task.setEndY((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		task.setEndNPCAvataRace(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		task.setEndNPCAvataSex(new String(content,offset,len,"UTF-8"));
		offset += len;
		task.setThewCost((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		task.setShowTarget((byte)mf.byteArrayToNumber(content,offset,1));
		offset += 1;
		task.setDailyTaskCycle((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		task.setDailyTaskMaxNum((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		task.setReGetCodeDown((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		targets = new TaskTarget[len];
		for(int i = 0 ; i < targets.length ; i++){
			targets[i] = new TaskTarget();
			targets[i].setTargetByteType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] targetName_0001 = new String[len];
			for(int j = 0 ; j < targetName_0001.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				targetName_0001[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			targets[i].setTargetName(targetName_0001);
			targets[i].setTargetNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			targets[i].setTargetColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] mapName_0002 = new String[len];
			for(int j = 0 ; j < mapName_0002.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				mapName_0002[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			targets[i].setMapName(mapName_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] resMapName_0003 = new String[len];
			for(int j = 0 ; j < resMapName_0003.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				resMapName_0003[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			targets[i].setResMapName(resMapName_0003);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] x_0004 = new int[len];
			for(int j = 0 ; j < x_0004.length ; j++){
				x_0004[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			targets[i].setX(x_0004);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] y_0005 = new int[len];
			for(int j = 0 ; j < y_0005.length ; j++){
				y_0005[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			targets[i].setY(y_0005);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] showMonsterNames_0006 = new String[len];
			for(int j = 0 ; j < showMonsterNames_0006.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				showMonsterNames_0006[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			targets[i].setShowMonsterNames(showMonsterNames_0006);
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		prizes = new TaskPrize[len];
		for(int i = 0 ; i < prizes.length ; i++){
			prizes[i] = new TaskPrize();
			prizes[i].setPrizeByteType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] prizeName_0007 = new String[len];
			for(int j = 0 ; j < prizeName_0007.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				prizeName_0007[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			prizes[i].setPrizeName(prizeName_0007);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] prizeNum_0008 = new long[len];
			for(int j = 0 ; j < prizeNum_0008.length ; j++){
				prizeNum_0008[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			prizes[i].setPrizeNum(prizeNum_0008);
			prizes[i].setTotalNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] prizeColor_0009 = new int[len];
			for(int j = 0 ; j < prizeColor_0009.length ; j++){
				prizeColor_0009[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			prizes[i].setPrizeColor(prizeColor_0009);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] prizeId_0010 = new long[len];
			for(int j = 0 ; j < prizeId_0010.length ; j++){
				prizeId_0010[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			prizes[i].setPrizeId(prizeId_0010);
		}
	}

	public int getType() {
		return 0x70000FA0;
	}

	public String getTypeDescription() {
		return "TASK_QUERY_TASK_RES";
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
		len += 2;
		if(task.getName() != null){
			try{
			len += task.getName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 1;
		len += 2;
		if(task.getDes() != null){
			try{
			len += task.getDes().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(task.getUnDeliverTalk() != null){
			try{
			len += task.getUnDeliverTalk().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(task.getStartNpc() != null){
			try{
			len += task.getStartNpc().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(task.getStartMap() != null){
			try{
			len += task.getStartMap().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(task.getStartMapResName() != null){
			try{
			len += task.getStartMapResName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(task.getStartTalk() != null){
			try{
			len += task.getStartTalk().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 4;
		len += 2;
		if(task.getStartNPCAvataRace() != null){
			try{
			len += task.getStartNPCAvataRace().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(task.getStartNPCAvataSex() != null){
			try{
			len += task.getStartNPCAvataSex().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(task.getEndNpc() != null){
			try{
			len += task.getEndNpc().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(task.getEndMap() != null){
			try{
			len += task.getEndMap().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(task.getEndMapResName() != null){
			try{
			len += task.getEndMapResName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(task.getEndTalk() != null){
			try{
			len += task.getEndTalk().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(task.getNotice() != null){
			try{
			len += task.getNotice().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 4;
		len += 2;
		if(task.getEndNPCAvataRace() != null){
			try{
			len += task.getEndNPCAvataRace().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(task.getEndNPCAvataSex() != null){
			try{
			len += task.getEndNPCAvataSex().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 1;
		len += 4;
		len += 4;
		len += 8;
		len += 4;
		for(int i = 0 ; i < targets.length ; i++){
			len += 1;
			len += 4;
			String[] targetName = targets[i].getTargetName();
			for(int j = 0 ; j < targetName.length; j++){
				len += 2;
				try{
					len += targetName[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 4;
			String[] mapName = targets[i].getMapName();
			for(int j = 0 ; j < mapName.length; j++){
				len += 2;
				try{
					len += mapName[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] resMapName = targets[i].getResMapName();
			for(int j = 0 ; j < resMapName.length; j++){
				len += 2;
				try{
					len += resMapName[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += targets[i].getX().length * 4;
			len += 4;
			len += targets[i].getY().length * 4;
			len += 4;
			String[] showMonsterNames = targets[i].getShowMonsterNames();
			for(int j = 0 ; j < showMonsterNames.length; j++){
				len += 2;
				try{
					len += showMonsterNames[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 4;
		for(int i = 0 ; i < prizes.length ; i++){
			len += 1;
			len += 4;
			String[] prizeName = prizes[i].getPrizeName();
			for(int j = 0 ; j < prizeName.length; j++){
				len += 2;
				try{
					len += prizeName[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += prizes[i].getPrizeNum().length * 8;
			len += 4;
			len += 4;
			len += prizes[i].getPrizeColor().length * 4;
			len += 4;
			len += prizes[i].getPrizeId().length * 8;
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

			buffer.putInt((int)task.getId());
			byte[] tmpBytes1 ;
				try{
				tmpBytes1 = task.getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt((int)task.getGrade());
			buffer.put((byte)task.getShowType());
				try{
				tmpBytes1 = task.getDes().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = task.getUnDeliverTalk().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = task.getStartNpc().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = task.getStartMap().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = task.getStartMapResName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = task.getStartTalk().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt((int)task.getStartX());
			buffer.putInt((int)task.getStartY());
				try{
				tmpBytes1 = task.getStartNPCAvataRace().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = task.getStartNPCAvataSex().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = task.getEndNpc().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = task.getEndMap().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = task.getEndMapResName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = task.getEndTalk().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = task.getNotice().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt((int)task.getEndX());
			buffer.putInt((int)task.getEndY());
				try{
				tmpBytes1 = task.getEndNPCAvataRace().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = task.getEndNPCAvataSex().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt((int)task.getThewCost());
			buffer.put((byte)task.getShowTarget());
			buffer.putInt((int)task.getDailyTaskCycle());
			buffer.putInt((int)task.getDailyTaskMaxNum());
			buffer.putLong(task.getReGetCodeDown());
			buffer.putInt(targets.length);
			for(int i = 0 ; i < targets.length ; i++){
				buffer.put((byte)targets[i].getTargetByteType());
				buffer.putInt(targets[i].getTargetName().length);
				String[] targetName_0011 = targets[i].getTargetName();
				for(int j = 0 ; j < targetName_0011.length ; j++){
				try{
					buffer.putShort((short)targetName_0011[j].getBytes("UTF-8").length);
					buffer.put(targetName_0011[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt((int)targets[i].getTargetNum());
				buffer.putInt((int)targets[i].getTargetColor());
				buffer.putInt(targets[i].getMapName().length);
				String[] mapName_0012 = targets[i].getMapName();
				for(int j = 0 ; j < mapName_0012.length ; j++){
				try{
					buffer.putShort((short)mapName_0012[j].getBytes("UTF-8").length);
					buffer.put(mapName_0012[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(targets[i].getResMapName().length);
				String[] resMapName_0013 = targets[i].getResMapName();
				for(int j = 0 ; j < resMapName_0013.length ; j++){
				try{
					buffer.putShort((short)resMapName_0013[j].getBytes("UTF-8").length);
					buffer.put(resMapName_0013[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(targets[i].getX().length);
				int[] x_0014 = targets[i].getX();
				for(int j = 0 ; j < x_0014.length ; j++){
					buffer.putInt(x_0014[j]);
				}
				buffer.putInt(targets[i].getY().length);
				int[] y_0015 = targets[i].getY();
				for(int j = 0 ; j < y_0015.length ; j++){
					buffer.putInt(y_0015[j]);
				}
				buffer.putInt(targets[i].getShowMonsterNames().length);
				String[] showMonsterNames_0016 = targets[i].getShowMonsterNames();
				for(int j = 0 ; j < showMonsterNames_0016.length ; j++){
				try{
					buffer.putShort((short)showMonsterNames_0016[j].getBytes("UTF-8").length);
					buffer.put(showMonsterNames_0016[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
			}
			buffer.putInt(prizes.length);
			for(int i = 0 ; i < prizes.length ; i++){
				buffer.put((byte)prizes[i].getPrizeByteType());
				buffer.putInt(prizes[i].getPrizeName().length);
				String[] prizeName_0017 = prizes[i].getPrizeName();
				for(int j = 0 ; j < prizeName_0017.length ; j++){
				try{
					buffer.putShort((short)prizeName_0017[j].getBytes("UTF-8").length);
					buffer.put(prizeName_0017[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(prizes[i].getPrizeNum().length);
				long[] prizeNum_0018 = prizes[i].getPrizeNum();
				for(int j = 0 ; j < prizeNum_0018.length ; j++){
					buffer.putLong(prizeNum_0018[j]);
				}
				buffer.putInt((int)prizes[i].getTotalNum());
				buffer.putInt(prizes[i].getPrizeColor().length);
				int[] prizeColor_0019 = prizes[i].getPrizeColor();
				for(int j = 0 ; j < prizeColor_0019.length ; j++){
					buffer.putInt(prizeColor_0019[j]);
				}
				buffer.putInt(prizes[i].getPrizeId().length);
				long[] prizeId_0020 = prizes[i].getPrizeId();
				for(int j = 0 ; j < prizeId_0020.length ; j++){
					buffer.putLong(prizeId_0020[j]);
				}
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
	 *	玩家正在做的任务列表，也就是已经接了，但还没有交付的任务
	 */
	public Task getTask(){
		return task;
	}

	/**
	 * 设置属性：
	 *	玩家正在做的任务列表，也就是已经接了，但还没有交付的任务
	 */
	public void setTask(Task task){
		this.task = task;
	}

	/**
	 * 获取属性：
	 *	任务目标 数组length > 1 则为或关系
	 */
	public TaskTarget[] getTargets(){
		return targets;
	}

	/**
	 * 设置属性：
	 *	任务目标 数组length > 1 则为或关系
	 */
	public void setTargets(TaskTarget[] targets){
		this.targets = targets;
	}

	/**
	 * 获取属性：
	 *	任务奖励 数组length > 1 则为可选择奖励
	 */
	public TaskPrize[] getPrizes(){
		return prizes;
	}

	/**
	 * 设置属性：
	 *	任务奖励 数组length > 1 则为可选择奖励
	 */
	public void setPrizes(TaskPrize[] prizes){
		this.prizes = prizes;
	}

}
package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.datasource.career.Career;
import com.fy.engineserver.datasource.career.SkillInfo;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询门派信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>career.weaponTypeLimit.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>career.weaponTypeLimit</td><td>int[]</td><td>career.weaponTypeLimit.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>career.name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>career.name</td><td>String</td><td>career.name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills.length</td><td>int</td><td>4个字节</td><td>SkillInfo数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[0].name</td><td>String</td><td>basicSkills[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[0].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[0].iconId</td><td>String</td><td>basicSkills[0].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[0].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[0].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[0].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[0].description</td><td>String</td><td>basicSkills[0].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[0].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[0].needMoney</td><td>int[]</td><td>basicSkills[0].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[0].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[0].needYuanQi</td><td>int[]</td><td>basicSkills[0].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[0].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[0].needLongExp</td><td>long[]</td><td>basicSkills[0].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[0].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[0].needPoint</td><td>int[]</td><td>basicSkills[0].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[0].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[0].needPlayerLevel</td><td>int[]</td><td>basicSkills[0].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[1].name</td><td>String</td><td>basicSkills[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[1].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[1].iconId</td><td>String</td><td>basicSkills[1].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[1].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[1].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[1].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[1].description</td><td>String</td><td>basicSkills[1].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[1].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[1].needMoney</td><td>int[]</td><td>basicSkills[1].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[1].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[1].needYuanQi</td><td>int[]</td><td>basicSkills[1].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[1].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[1].needLongExp</td><td>long[]</td><td>basicSkills[1].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[1].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[1].needPoint</td><td>int[]</td><td>basicSkills[1].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[1].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[1].needPlayerLevel</td><td>int[]</td><td>basicSkills[1].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[2].name</td><td>String</td><td>basicSkills[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[2].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[2].iconId</td><td>String</td><td>basicSkills[2].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[2].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[2].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[2].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[2].description</td><td>String</td><td>basicSkills[2].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[2].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[2].needMoney</td><td>int[]</td><td>basicSkills[2].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[2].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[2].needYuanQi</td><td>int[]</td><td>basicSkills[2].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[2].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[2].needLongExp</td><td>long[]</td><td>basicSkills[2].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[2].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[2].needPoint</td><td>int[]</td><td>basicSkills[2].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicSkills[2].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicSkills[2].needPlayerLevel</td><td>int[]</td><td>basicSkills[2].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills.length</td><td>int</td><td>4个字节</td><td>SkillInfo数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[0].name</td><td>String</td><td>professorSkills[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[0].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[0].iconId</td><td>String</td><td>professorSkills[0].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[0].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[0].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[0].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[0].description</td><td>String</td><td>professorSkills[0].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[0].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[0].needMoney</td><td>int[]</td><td>professorSkills[0].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[0].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[0].needYuanQi</td><td>int[]</td><td>professorSkills[0].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[0].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[0].needLongExp</td><td>long[]</td><td>professorSkills[0].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[0].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[0].needPoint</td><td>int[]</td><td>professorSkills[0].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[0].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[0].needPlayerLevel</td><td>int[]</td><td>professorSkills[0].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[1].name</td><td>String</td><td>professorSkills[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[1].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[1].iconId</td><td>String</td><td>professorSkills[1].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[1].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[1].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[1].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[1].description</td><td>String</td><td>professorSkills[1].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[1].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[1].needMoney</td><td>int[]</td><td>professorSkills[1].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[1].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[1].needYuanQi</td><td>int[]</td><td>professorSkills[1].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[1].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[1].needLongExp</td><td>long[]</td><td>professorSkills[1].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[1].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[1].needPoint</td><td>int[]</td><td>professorSkills[1].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[1].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[1].needPlayerLevel</td><td>int[]</td><td>professorSkills[1].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[2].name</td><td>String</td><td>professorSkills[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[2].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[2].iconId</td><td>String</td><td>professorSkills[2].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[2].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[2].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[2].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[2].description</td><td>String</td><td>professorSkills[2].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[2].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[2].needMoney</td><td>int[]</td><td>professorSkills[2].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[2].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[2].needYuanQi</td><td>int[]</td><td>professorSkills[2].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[2].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[2].needLongExp</td><td>long[]</td><td>professorSkills[2].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[2].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[2].needPoint</td><td>int[]</td><td>professorSkills[2].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>professorSkills[2].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>professorSkills[2].needPlayerLevel</td><td>int[]</td><td>professorSkills[2].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills.length</td><td>int</td><td>4个字节</td><td>SkillInfo数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[0].name</td><td>String</td><td>nuqiSkills[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[0].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[0].iconId</td><td>String</td><td>nuqiSkills[0].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[0].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[0].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[0].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[0].description</td><td>String</td><td>nuqiSkills[0].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[0].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[0].needMoney</td><td>int[]</td><td>nuqiSkills[0].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[0].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[0].needYuanQi</td><td>int[]</td><td>nuqiSkills[0].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[0].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[0].needLongExp</td><td>long[]</td><td>nuqiSkills[0].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[0].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[0].needPoint</td><td>int[]</td><td>nuqiSkills[0].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[0].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[0].needPlayerLevel</td><td>int[]</td><td>nuqiSkills[0].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[1].name</td><td>String</td><td>nuqiSkills[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[1].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[1].iconId</td><td>String</td><td>nuqiSkills[1].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[1].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[1].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[1].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[1].description</td><td>String</td><td>nuqiSkills[1].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[1].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[1].needMoney</td><td>int[]</td><td>nuqiSkills[1].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[1].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[1].needYuanQi</td><td>int[]</td><td>nuqiSkills[1].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[1].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[1].needLongExp</td><td>long[]</td><td>nuqiSkills[1].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[1].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[1].needPoint</td><td>int[]</td><td>nuqiSkills[1].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[1].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[1].needPlayerLevel</td><td>int[]</td><td>nuqiSkills[1].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[2].name</td><td>String</td><td>nuqiSkills[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[2].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[2].iconId</td><td>String</td><td>nuqiSkills[2].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[2].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[2].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[2].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[2].description</td><td>String</td><td>nuqiSkills[2].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[2].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[2].needMoney</td><td>int[]</td><td>nuqiSkills[2].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[2].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[2].needYuanQi</td><td>int[]</td><td>nuqiSkills[2].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[2].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[2].needLongExp</td><td>long[]</td><td>nuqiSkills[2].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[2].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[2].needPoint</td><td>int[]</td><td>nuqiSkills[2].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nuqiSkills[2].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nuqiSkills[2].needPlayerLevel</td><td>int[]</td><td>nuqiSkills[2].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills.length</td><td>int</td><td>4个字节</td><td>SkillInfo数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].name</td><td>String</td><td>xinfaSkills[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].iconId</td><td>String</td><td>xinfaSkills[0].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].description</td><td>String</td><td>xinfaSkills[0].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].needMoney</td><td>int[]</td><td>xinfaSkills[0].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].needYuanQi</td><td>int[]</td><td>xinfaSkills[0].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].needLongExp</td><td>long[]</td><td>xinfaSkills[0].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].needPoint</td><td>int[]</td><td>xinfaSkills[0].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[0].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[0].needPlayerLevel</td><td>int[]</td><td>xinfaSkills[0].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].name</td><td>String</td><td>xinfaSkills[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].iconId</td><td>String</td><td>xinfaSkills[1].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].description</td><td>String</td><td>xinfaSkills[1].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].needMoney</td><td>int[]</td><td>xinfaSkills[1].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].needYuanQi</td><td>int[]</td><td>xinfaSkills[1].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].needLongExp</td><td>long[]</td><td>xinfaSkills[1].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].needPoint</td><td>int[]</td><td>xinfaSkills[1].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[1].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[1].needPlayerLevel</td><td>int[]</td><td>xinfaSkills[1].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].name</td><td>String</td><td>xinfaSkills[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].iconId</td><td>String</td><td>xinfaSkills[2].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].description</td><td>String</td><td>xinfaSkills[2].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].needMoney</td><td>int[]</td><td>xinfaSkills[2].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].needYuanQi</td><td>int[]</td><td>xinfaSkills[2].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].needLongExp</td><td>long[]</td><td>xinfaSkills[2].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].needPoint</td><td>int[]</td><td>xinfaSkills[2].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>xinfaSkills[2].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>xinfaSkills[2].needPlayerLevel</td><td>int[]</td><td>xinfaSkills[2].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills.length</td><td>int</td><td>4个字节</td><td>SkillInfo数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[0].name</td><td>String</td><td>kingSkills[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[0].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[0].iconId</td><td>String</td><td>kingSkills[0].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[0].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[0].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[0].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[0].description</td><td>String</td><td>kingSkills[0].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[0].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[0].needMoney</td><td>int[]</td><td>kingSkills[0].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[0].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[0].needYuanQi</td><td>int[]</td><td>kingSkills[0].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[0].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[0].needLongExp</td><td>long[]</td><td>kingSkills[0].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[0].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[0].needPoint</td><td>int[]</td><td>kingSkills[0].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[0].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[0].needPlayerLevel</td><td>int[]</td><td>kingSkills[0].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[1].name</td><td>String</td><td>kingSkills[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[1].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[1].iconId</td><td>String</td><td>kingSkills[1].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[1].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[1].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[1].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[1].description</td><td>String</td><td>kingSkills[1].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[1].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[1].needMoney</td><td>int[]</td><td>kingSkills[1].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[1].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[1].needYuanQi</td><td>int[]</td><td>kingSkills[1].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[1].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[1].needLongExp</td><td>long[]</td><td>kingSkills[1].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[1].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[1].needPoint</td><td>int[]</td><td>kingSkills[1].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[1].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[1].needPlayerLevel</td><td>int[]</td><td>kingSkills[1].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[2].name</td><td>String</td><td>kingSkills[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[2].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[2].iconId</td><td>String</td><td>kingSkills[2].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[2].skillType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[2].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[2].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[2].description</td><td>String</td><td>kingSkills[2].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[2].needMoney.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[2].needMoney</td><td>int[]</td><td>kingSkills[2].needMoney.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[2].needYuanQi.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[2].needYuanQi</td><td>int[]</td><td>kingSkills[2].needYuanQi.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[2].needLongExp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[2].needLongExp</td><td>long[]</td><td>kingSkills[2].needLongExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[2].needPoint.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[2].needPoint</td><td>int[]</td><td>kingSkills[2].needPoint.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingSkills[2].needPlayerLevel.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingSkills[2].needPlayerLevel</td><td>int[]</td><td>kingSkills[2].needPlayerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class NEW_QUERY_CAREER_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	Career career;
	SkillInfo[] basicSkills;
	SkillInfo[] professorSkills;
	SkillInfo[] nuqiSkills;
	SkillInfo[] xinfaSkills;
	SkillInfo[] kingSkills;

	public NEW_QUERY_CAREER_INFO_RES(){
	}

	public NEW_QUERY_CAREER_INFO_RES(long seqNum,Career career,SkillInfo[] basicSkills,SkillInfo[] professorSkills,SkillInfo[] nuqiSkills,SkillInfo[] xinfaSkills,SkillInfo[] kingSkills){
		this.seqNum = seqNum;
		this.career = career;
		this.basicSkills = basicSkills;
		this.professorSkills = professorSkills;
		this.nuqiSkills = nuqiSkills;
		this.xinfaSkills = xinfaSkills;
		this.kingSkills = kingSkills;
	}

	public NEW_QUERY_CAREER_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		career = new Career();
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] weaponTypeLimit_0001 = new int[len];
		for(int j = 0 ; j < weaponTypeLimit_0001.length ; j++){
			weaponTypeLimit_0001[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		career.setWeaponTypeLimit(weaponTypeLimit_0001);
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		career.setName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		basicSkills = new SkillInfo[len];
		for(int i = 0 ; i < basicSkills.length ; i++){
			basicSkills[i] = new SkillInfo();
			basicSkills[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			basicSkills[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			basicSkills[i].setIconId(new String(content,offset,len,"UTF-8"));
			offset += len;
			basicSkills[i].setSkillType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			basicSkills[i].setMaxLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			basicSkills[i].setDescription(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needMoney_0002 = new int[len];
			for(int j = 0 ; j < needMoney_0002.length ; j++){
				needMoney_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			basicSkills[i].setNeedMoney(needMoney_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needYuanQi_0003 = new int[len];
			for(int j = 0 ; j < needYuanQi_0003.length ; j++){
				needYuanQi_0003[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			basicSkills[i].setNeedYuanQi(needYuanQi_0003);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] needLongExp_0004 = new long[len];
			for(int j = 0 ; j < needLongExp_0004.length ; j++){
				needLongExp_0004[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			basicSkills[i].setNeedLongExp(needLongExp_0004);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needPoint_0005 = new int[len];
			for(int j = 0 ; j < needPoint_0005.length ; j++){
				needPoint_0005[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			basicSkills[i].setNeedPoint(needPoint_0005);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needPlayerLevel_0006 = new int[len];
			for(int j = 0 ; j < needPlayerLevel_0006.length ; j++){
				needPlayerLevel_0006[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			basicSkills[i].setNeedPlayerLevel(needPlayerLevel_0006);
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		professorSkills = new SkillInfo[len];
		for(int i = 0 ; i < professorSkills.length ; i++){
			professorSkills[i] = new SkillInfo();
			professorSkills[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			professorSkills[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			professorSkills[i].setIconId(new String(content,offset,len,"UTF-8"));
			offset += len;
			professorSkills[i].setSkillType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			professorSkills[i].setMaxLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			professorSkills[i].setDescription(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needMoney_0007 = new int[len];
			for(int j = 0 ; j < needMoney_0007.length ; j++){
				needMoney_0007[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			professorSkills[i].setNeedMoney(needMoney_0007);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needYuanQi_0008 = new int[len];
			for(int j = 0 ; j < needYuanQi_0008.length ; j++){
				needYuanQi_0008[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			professorSkills[i].setNeedYuanQi(needYuanQi_0008);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] needLongExp_0009 = new long[len];
			for(int j = 0 ; j < needLongExp_0009.length ; j++){
				needLongExp_0009[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			professorSkills[i].setNeedLongExp(needLongExp_0009);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needPoint_0010 = new int[len];
			for(int j = 0 ; j < needPoint_0010.length ; j++){
				needPoint_0010[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			professorSkills[i].setNeedPoint(needPoint_0010);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needPlayerLevel_0011 = new int[len];
			for(int j = 0 ; j < needPlayerLevel_0011.length ; j++){
				needPlayerLevel_0011[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			professorSkills[i].setNeedPlayerLevel(needPlayerLevel_0011);
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		nuqiSkills = new SkillInfo[len];
		for(int i = 0 ; i < nuqiSkills.length ; i++){
			nuqiSkills[i] = new SkillInfo();
			nuqiSkills[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			nuqiSkills[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			nuqiSkills[i].setIconId(new String(content,offset,len,"UTF-8"));
			offset += len;
			nuqiSkills[i].setSkillType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			nuqiSkills[i].setMaxLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			nuqiSkills[i].setDescription(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needMoney_0012 = new int[len];
			for(int j = 0 ; j < needMoney_0012.length ; j++){
				needMoney_0012[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			nuqiSkills[i].setNeedMoney(needMoney_0012);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needYuanQi_0013 = new int[len];
			for(int j = 0 ; j < needYuanQi_0013.length ; j++){
				needYuanQi_0013[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			nuqiSkills[i].setNeedYuanQi(needYuanQi_0013);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] needLongExp_0014 = new long[len];
			for(int j = 0 ; j < needLongExp_0014.length ; j++){
				needLongExp_0014[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			nuqiSkills[i].setNeedLongExp(needLongExp_0014);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needPoint_0015 = new int[len];
			for(int j = 0 ; j < needPoint_0015.length ; j++){
				needPoint_0015[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			nuqiSkills[i].setNeedPoint(needPoint_0015);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needPlayerLevel_0016 = new int[len];
			for(int j = 0 ; j < needPlayerLevel_0016.length ; j++){
				needPlayerLevel_0016[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			nuqiSkills[i].setNeedPlayerLevel(needPlayerLevel_0016);
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		xinfaSkills = new SkillInfo[len];
		for(int i = 0 ; i < xinfaSkills.length ; i++){
			xinfaSkills[i] = new SkillInfo();
			xinfaSkills[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			xinfaSkills[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			xinfaSkills[i].setIconId(new String(content,offset,len,"UTF-8"));
			offset += len;
			xinfaSkills[i].setSkillType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			xinfaSkills[i].setMaxLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			xinfaSkills[i].setDescription(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needMoney_0017 = new int[len];
			for(int j = 0 ; j < needMoney_0017.length ; j++){
				needMoney_0017[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			xinfaSkills[i].setNeedMoney(needMoney_0017);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needYuanQi_0018 = new int[len];
			for(int j = 0 ; j < needYuanQi_0018.length ; j++){
				needYuanQi_0018[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			xinfaSkills[i].setNeedYuanQi(needYuanQi_0018);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] needLongExp_0019 = new long[len];
			for(int j = 0 ; j < needLongExp_0019.length ; j++){
				needLongExp_0019[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			xinfaSkills[i].setNeedLongExp(needLongExp_0019);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needPoint_0020 = new int[len];
			for(int j = 0 ; j < needPoint_0020.length ; j++){
				needPoint_0020[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			xinfaSkills[i].setNeedPoint(needPoint_0020);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needPlayerLevel_0021 = new int[len];
			for(int j = 0 ; j < needPlayerLevel_0021.length ; j++){
				needPlayerLevel_0021[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			xinfaSkills[i].setNeedPlayerLevel(needPlayerLevel_0021);
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		kingSkills = new SkillInfo[len];
		for(int i = 0 ; i < kingSkills.length ; i++){
			kingSkills[i] = new SkillInfo();
			kingSkills[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			kingSkills[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			kingSkills[i].setIconId(new String(content,offset,len,"UTF-8"));
			offset += len;
			kingSkills[i].setSkillType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			kingSkills[i].setMaxLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			kingSkills[i].setDescription(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needMoney_0022 = new int[len];
			for(int j = 0 ; j < needMoney_0022.length ; j++){
				needMoney_0022[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			kingSkills[i].setNeedMoney(needMoney_0022);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needYuanQi_0023 = new int[len];
			for(int j = 0 ; j < needYuanQi_0023.length ; j++){
				needYuanQi_0023[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			kingSkills[i].setNeedYuanQi(needYuanQi_0023);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] needLongExp_0024 = new long[len];
			for(int j = 0 ; j < needLongExp_0024.length ; j++){
				needLongExp_0024[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			kingSkills[i].setNeedLongExp(needLongExp_0024);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needPoint_0025 = new int[len];
			for(int j = 0 ; j < needPoint_0025.length ; j++){
				needPoint_0025[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			kingSkills[i].setNeedPoint(needPoint_0025);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] needPlayerLevel_0026 = new int[len];
			for(int j = 0 ; j < needPlayerLevel_0026.length ; j++){
				needPlayerLevel_0026[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			kingSkills[i].setNeedPlayerLevel(needPlayerLevel_0026);
		}
	}

	public int getType() {
		return 0x700000EC;
	}

	public String getTypeDescription() {
		return "NEW_QUERY_CAREER_INFO_RES";
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
		len += career.getWeaponTypeLimit().length * 4;
		len += 2;
		if(career.getName() != null){
			try{
			len += career.getName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < basicSkills.length ; i++){
			len += 4;
			len += 2;
			if(basicSkills[i].getName() != null){
				try{
				len += basicSkills[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(basicSkills[i].getIconId() != null){
				try{
				len += basicSkills[i].getIconId().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 4;
			len += 2;
			if(basicSkills[i].getDescription() != null){
				try{
				len += basicSkills[i].getDescription().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += basicSkills[i].getNeedMoney().length * 4;
			len += 4;
			len += basicSkills[i].getNeedYuanQi().length * 4;
			len += 4;
			len += basicSkills[i].getNeedLongExp().length * 8;
			len += 4;
			len += basicSkills[i].getNeedPoint().length * 4;
			len += 4;
			len += basicSkills[i].getNeedPlayerLevel().length * 4;
		}
		len += 4;
		for(int i = 0 ; i < professorSkills.length ; i++){
			len += 4;
			len += 2;
			if(professorSkills[i].getName() != null){
				try{
				len += professorSkills[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(professorSkills[i].getIconId() != null){
				try{
				len += professorSkills[i].getIconId().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 4;
			len += 2;
			if(professorSkills[i].getDescription() != null){
				try{
				len += professorSkills[i].getDescription().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += professorSkills[i].getNeedMoney().length * 4;
			len += 4;
			len += professorSkills[i].getNeedYuanQi().length * 4;
			len += 4;
			len += professorSkills[i].getNeedLongExp().length * 8;
			len += 4;
			len += professorSkills[i].getNeedPoint().length * 4;
			len += 4;
			len += professorSkills[i].getNeedPlayerLevel().length * 4;
		}
		len += 4;
		for(int i = 0 ; i < nuqiSkills.length ; i++){
			len += 4;
			len += 2;
			if(nuqiSkills[i].getName() != null){
				try{
				len += nuqiSkills[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(nuqiSkills[i].getIconId() != null){
				try{
				len += nuqiSkills[i].getIconId().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 4;
			len += 2;
			if(nuqiSkills[i].getDescription() != null){
				try{
				len += nuqiSkills[i].getDescription().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += nuqiSkills[i].getNeedMoney().length * 4;
			len += 4;
			len += nuqiSkills[i].getNeedYuanQi().length * 4;
			len += 4;
			len += nuqiSkills[i].getNeedLongExp().length * 8;
			len += 4;
			len += nuqiSkills[i].getNeedPoint().length * 4;
			len += 4;
			len += nuqiSkills[i].getNeedPlayerLevel().length * 4;
		}
		len += 4;
		for(int i = 0 ; i < xinfaSkills.length ; i++){
			len += 4;
			len += 2;
			if(xinfaSkills[i].getName() != null){
				try{
				len += xinfaSkills[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(xinfaSkills[i].getIconId() != null){
				try{
				len += xinfaSkills[i].getIconId().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 4;
			len += 2;
			if(xinfaSkills[i].getDescription() != null){
				try{
				len += xinfaSkills[i].getDescription().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += xinfaSkills[i].getNeedMoney().length * 4;
			len += 4;
			len += xinfaSkills[i].getNeedYuanQi().length * 4;
			len += 4;
			len += xinfaSkills[i].getNeedLongExp().length * 8;
			len += 4;
			len += xinfaSkills[i].getNeedPoint().length * 4;
			len += 4;
			len += xinfaSkills[i].getNeedPlayerLevel().length * 4;
		}
		len += 4;
		for(int i = 0 ; i < kingSkills.length ; i++){
			len += 4;
			len += 2;
			if(kingSkills[i].getName() != null){
				try{
				len += kingSkills[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(kingSkills[i].getIconId() != null){
				try{
				len += kingSkills[i].getIconId().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 4;
			len += 2;
			if(kingSkills[i].getDescription() != null){
				try{
				len += kingSkills[i].getDescription().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += kingSkills[i].getNeedMoney().length * 4;
			len += 4;
			len += kingSkills[i].getNeedYuanQi().length * 4;
			len += 4;
			len += kingSkills[i].getNeedLongExp().length * 8;
			len += 4;
			len += kingSkills[i].getNeedPoint().length * 4;
			len += 4;
			len += kingSkills[i].getNeedPlayerLevel().length * 4;
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

			buffer.putInt(career.getWeaponTypeLimit().length);
			int[] weaponTypeLimit_0027 = career.getWeaponTypeLimit();
			for(int j = 0 ; j < weaponTypeLimit_0027.length ; j++){
				buffer.putInt(weaponTypeLimit_0027[j]);
			}
			byte[] tmpBytes1 ;
				try{
				tmpBytes1 = career.getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(basicSkills.length);
			for(int i = 0 ; i < basicSkills.length ; i++){
				buffer.putInt((int)basicSkills[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = basicSkills[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = basicSkills[i].getIconId().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)basicSkills[i].getSkillType());
				buffer.putInt((int)basicSkills[i].getMaxLevel());
				try{
				tmpBytes2 = basicSkills[i].getDescription().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(basicSkills[i].getNeedMoney().length);
				int[] needMoney_0028 = basicSkills[i].getNeedMoney();
				for(int j = 0 ; j < needMoney_0028.length ; j++){
					buffer.putInt(needMoney_0028[j]);
				}
				buffer.putInt(basicSkills[i].getNeedYuanQi().length);
				int[] needYuanQi_0029 = basicSkills[i].getNeedYuanQi();
				for(int j = 0 ; j < needYuanQi_0029.length ; j++){
					buffer.putInt(needYuanQi_0029[j]);
				}
				buffer.putInt(basicSkills[i].getNeedLongExp().length);
				long[] needLongExp_0030 = basicSkills[i].getNeedLongExp();
				for(int j = 0 ; j < needLongExp_0030.length ; j++){
					buffer.putLong(needLongExp_0030[j]);
				}
				buffer.putInt(basicSkills[i].getNeedPoint().length);
				int[] needPoint_0031 = basicSkills[i].getNeedPoint();
				for(int j = 0 ; j < needPoint_0031.length ; j++){
					buffer.putInt(needPoint_0031[j]);
				}
				buffer.putInt(basicSkills[i].getNeedPlayerLevel().length);
				int[] needPlayerLevel_0032 = basicSkills[i].getNeedPlayerLevel();
				for(int j = 0 ; j < needPlayerLevel_0032.length ; j++){
					buffer.putInt(needPlayerLevel_0032[j]);
				}
			}
			buffer.putInt(professorSkills.length);
			for(int i = 0 ; i < professorSkills.length ; i++){
				buffer.putInt((int)professorSkills[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = professorSkills[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = professorSkills[i].getIconId().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)professorSkills[i].getSkillType());
				buffer.putInt((int)professorSkills[i].getMaxLevel());
				try{
				tmpBytes2 = professorSkills[i].getDescription().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(professorSkills[i].getNeedMoney().length);
				int[] needMoney_0033 = professorSkills[i].getNeedMoney();
				for(int j = 0 ; j < needMoney_0033.length ; j++){
					buffer.putInt(needMoney_0033[j]);
				}
				buffer.putInt(professorSkills[i].getNeedYuanQi().length);
				int[] needYuanQi_0034 = professorSkills[i].getNeedYuanQi();
				for(int j = 0 ; j < needYuanQi_0034.length ; j++){
					buffer.putInt(needYuanQi_0034[j]);
				}
				buffer.putInt(professorSkills[i].getNeedLongExp().length);
				long[] needLongExp_0035 = professorSkills[i].getNeedLongExp();
				for(int j = 0 ; j < needLongExp_0035.length ; j++){
					buffer.putLong(needLongExp_0035[j]);
				}
				buffer.putInt(professorSkills[i].getNeedPoint().length);
				int[] needPoint_0036 = professorSkills[i].getNeedPoint();
				for(int j = 0 ; j < needPoint_0036.length ; j++){
					buffer.putInt(needPoint_0036[j]);
				}
				buffer.putInt(professorSkills[i].getNeedPlayerLevel().length);
				int[] needPlayerLevel_0037 = professorSkills[i].getNeedPlayerLevel();
				for(int j = 0 ; j < needPlayerLevel_0037.length ; j++){
					buffer.putInt(needPlayerLevel_0037[j]);
				}
			}
			buffer.putInt(nuqiSkills.length);
			for(int i = 0 ; i < nuqiSkills.length ; i++){
				buffer.putInt((int)nuqiSkills[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = nuqiSkills[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = nuqiSkills[i].getIconId().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)nuqiSkills[i].getSkillType());
				buffer.putInt((int)nuqiSkills[i].getMaxLevel());
				try{
				tmpBytes2 = nuqiSkills[i].getDescription().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(nuqiSkills[i].getNeedMoney().length);
				int[] needMoney_0038 = nuqiSkills[i].getNeedMoney();
				for(int j = 0 ; j < needMoney_0038.length ; j++){
					buffer.putInt(needMoney_0038[j]);
				}
				buffer.putInt(nuqiSkills[i].getNeedYuanQi().length);
				int[] needYuanQi_0039 = nuqiSkills[i].getNeedYuanQi();
				for(int j = 0 ; j < needYuanQi_0039.length ; j++){
					buffer.putInt(needYuanQi_0039[j]);
				}
				buffer.putInt(nuqiSkills[i].getNeedLongExp().length);
				long[] needLongExp_0040 = nuqiSkills[i].getNeedLongExp();
				for(int j = 0 ; j < needLongExp_0040.length ; j++){
					buffer.putLong(needLongExp_0040[j]);
				}
				buffer.putInt(nuqiSkills[i].getNeedPoint().length);
				int[] needPoint_0041 = nuqiSkills[i].getNeedPoint();
				for(int j = 0 ; j < needPoint_0041.length ; j++){
					buffer.putInt(needPoint_0041[j]);
				}
				buffer.putInt(nuqiSkills[i].getNeedPlayerLevel().length);
				int[] needPlayerLevel_0042 = nuqiSkills[i].getNeedPlayerLevel();
				for(int j = 0 ; j < needPlayerLevel_0042.length ; j++){
					buffer.putInt(needPlayerLevel_0042[j]);
				}
			}
			buffer.putInt(xinfaSkills.length);
			for(int i = 0 ; i < xinfaSkills.length ; i++){
				buffer.putInt((int)xinfaSkills[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = xinfaSkills[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = xinfaSkills[i].getIconId().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)xinfaSkills[i].getSkillType());
				buffer.putInt((int)xinfaSkills[i].getMaxLevel());
				try{
				tmpBytes2 = xinfaSkills[i].getDescription().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(xinfaSkills[i].getNeedMoney().length);
				int[] needMoney_0043 = xinfaSkills[i].getNeedMoney();
				for(int j = 0 ; j < needMoney_0043.length ; j++){
					buffer.putInt(needMoney_0043[j]);
				}
				buffer.putInt(xinfaSkills[i].getNeedYuanQi().length);
				int[] needYuanQi_0044 = xinfaSkills[i].getNeedYuanQi();
				for(int j = 0 ; j < needYuanQi_0044.length ; j++){
					buffer.putInt(needYuanQi_0044[j]);
				}
				buffer.putInt(xinfaSkills[i].getNeedLongExp().length);
				long[] needLongExp_0045 = xinfaSkills[i].getNeedLongExp();
				for(int j = 0 ; j < needLongExp_0045.length ; j++){
					buffer.putLong(needLongExp_0045[j]);
				}
				buffer.putInt(xinfaSkills[i].getNeedPoint().length);
				int[] needPoint_0046 = xinfaSkills[i].getNeedPoint();
				for(int j = 0 ; j < needPoint_0046.length ; j++){
					buffer.putInt(needPoint_0046[j]);
				}
				buffer.putInt(xinfaSkills[i].getNeedPlayerLevel().length);
				int[] needPlayerLevel_0047 = xinfaSkills[i].getNeedPlayerLevel();
				for(int j = 0 ; j < needPlayerLevel_0047.length ; j++){
					buffer.putInt(needPlayerLevel_0047[j]);
				}
			}
			buffer.putInt(kingSkills.length);
			for(int i = 0 ; i < kingSkills.length ; i++){
				buffer.putInt((int)kingSkills[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = kingSkills[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = kingSkills[i].getIconId().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)kingSkills[i].getSkillType());
				buffer.putInt((int)kingSkills[i].getMaxLevel());
				try{
				tmpBytes2 = kingSkills[i].getDescription().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(kingSkills[i].getNeedMoney().length);
				int[] needMoney_0048 = kingSkills[i].getNeedMoney();
				for(int j = 0 ; j < needMoney_0048.length ; j++){
					buffer.putInt(needMoney_0048[j]);
				}
				buffer.putInt(kingSkills[i].getNeedYuanQi().length);
				int[] needYuanQi_0049 = kingSkills[i].getNeedYuanQi();
				for(int j = 0 ; j < needYuanQi_0049.length ; j++){
					buffer.putInt(needYuanQi_0049[j]);
				}
				buffer.putInt(kingSkills[i].getNeedLongExp().length);
				long[] needLongExp_0050 = kingSkills[i].getNeedLongExp();
				for(int j = 0 ; j < needLongExp_0050.length ; j++){
					buffer.putLong(needLongExp_0050[j]);
				}
				buffer.putInt(kingSkills[i].getNeedPoint().length);
				int[] needPoint_0051 = kingSkills[i].getNeedPoint();
				for(int j = 0 ; j < needPoint_0051.length ; j++){
					buffer.putInt(needPoint_0051[j]);
				}
				buffer.putInt(kingSkills[i].getNeedPlayerLevel().length);
				int[] needPlayerLevel_0052 = kingSkills[i].getNeedPlayerLevel();
				for(int j = 0 ; j < needPlayerLevel_0052.length ; j++){
					buffer.putInt(needPlayerLevel_0052[j]);
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
	 *	门派基本信息
	 */
	public Career getCareer(){
		return career;
	}

	/**
	 * 设置属性：
	 *	门派基本信息
	 */
	public void setCareer(Career career){
		this.career = career;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public SkillInfo[] getBasicSkills(){
		return basicSkills;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setBasicSkills(SkillInfo[] basicSkills){
		this.basicSkills = basicSkills;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public SkillInfo[] getProfessorSkills(){
		return professorSkills;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setProfessorSkills(SkillInfo[] professorSkills){
		this.professorSkills = professorSkills;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public SkillInfo[] getNuqiSkills(){
		return nuqiSkills;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setNuqiSkills(SkillInfo[] nuqiSkills){
		this.nuqiSkills = nuqiSkills;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public SkillInfo[] getXinfaSkills(){
		return xinfaSkills;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setXinfaSkills(SkillInfo[] xinfaSkills){
		this.xinfaSkills = xinfaSkills;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public SkillInfo[] getKingSkills(){
		return kingSkills;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setKingSkills(SkillInfo[] kingSkills){
		this.kingSkills = kingSkills;
	}

}
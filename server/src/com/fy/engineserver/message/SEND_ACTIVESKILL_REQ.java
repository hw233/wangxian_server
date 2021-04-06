package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.datasource.skill.activeskills.CommonAttackSkill;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffect;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffectAndQuickMove;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithRange;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithTargetOrPosition;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithSummonNPC;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithTraceAndDirectionOrTarget;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithMatrix;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndOnTeamMember;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端请求服务器关于主动技能的数据，服务器返回给客户端所有的主动技能数据<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkillIdsForWeapon.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkillIdsForWeapon</td><td>int[]</td><td>commonAttackSkillIdsForWeapon.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills.length</td><td>int</td><td>4个字节</td><td>CommonAttackSkill数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].effectiveTimes</td><td>int[]</td><td>commonAttackSkills[0].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].style1</td><td>String</td><td>commonAttackSkills[0].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].style2</td><td>String</td><td>commonAttackSkills[0].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].effectExplosionLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].effectType</td><td>String</td><td>commonAttackSkills[0].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].avataRace</td><td>String</td><td>commonAttackSkills[0].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].avataSex</td><td>String</td><td>commonAttackSkills[0].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].effectLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].flySound</td><td>String</td><td>commonAttackSkills[0].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].explodeSound</td><td>String</td><td>commonAttackSkills[0].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].flyParticle[0]</td><td>String</td><td>commonAttackSkills[0].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].flyParticle[1]</td><td>String</td><td>commonAttackSkills[0].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].flyParticle[2]</td><td>String</td><td>commonAttackSkills[0].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].explodeParticle[0]</td><td>String</td><td>commonAttackSkills[0].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].explodeParticle[1]</td><td>String</td><td>commonAttackSkills[0].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].explodeParticle[2]</td><td>String</td><td>commonAttackSkills[0].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].bodyParticle[0]</td><td>String</td><td>commonAttackSkills[0].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].bodyParticle[1]</td><td>String</td><td>commonAttackSkills[0].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].bodyParticle[2]</td><td>String</td><td>commonAttackSkills[0].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].bodyParticleOffset</td><td>short[]</td><td>commonAttackSkills[0].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].bodyPartPath</td><td>String</td><td>commonAttackSkills[0].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].bodyPartAnimation</td><td>String</td><td>commonAttackSkills[0].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].bodyPartAnimationOffset</td><td>short[]</td><td>commonAttackSkills[0].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].footParticle[0]</td><td>String</td><td>commonAttackSkills[0].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].footParticle[1]</td><td>String</td><td>commonAttackSkills[0].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].footParticle[2]</td><td>String</td><td>commonAttackSkills[0].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].footParticleOffset</td><td>short[]</td><td>commonAttackSkills[0].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].footPartPath</td><td>String</td><td>commonAttackSkills[0].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].footPartAnimation</td><td>String</td><td>commonAttackSkills[0].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].footPartAnimationOffset</td><td>short[]</td><td>commonAttackSkills[0].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].targetFootParticle[0]</td><td>String</td><td>commonAttackSkills[0].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].targetFootParticle[1]</td><td>String</td><td>commonAttackSkills[0].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].targetFootParticle[2]</td><td>String</td><td>commonAttackSkills[0].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[0].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[0].buffName</td><td>String</td><td>commonAttackSkills[0].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].effectiveTimes</td><td>int[]</td><td>commonAttackSkills[1].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].style1</td><td>String</td><td>commonAttackSkills[1].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].style2</td><td>String</td><td>commonAttackSkills[1].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].effectExplosionLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].effectType</td><td>String</td><td>commonAttackSkills[1].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].avataRace</td><td>String</td><td>commonAttackSkills[1].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].avataSex</td><td>String</td><td>commonAttackSkills[1].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].effectLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].flySound</td><td>String</td><td>commonAttackSkills[1].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].explodeSound</td><td>String</td><td>commonAttackSkills[1].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].flyParticle[0]</td><td>String</td><td>commonAttackSkills[1].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].flyParticle[1]</td><td>String</td><td>commonAttackSkills[1].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].flyParticle[2]</td><td>String</td><td>commonAttackSkills[1].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].explodeParticle[0]</td><td>String</td><td>commonAttackSkills[1].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].explodeParticle[1]</td><td>String</td><td>commonAttackSkills[1].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].explodeParticle[2]</td><td>String</td><td>commonAttackSkills[1].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].bodyParticle[0]</td><td>String</td><td>commonAttackSkills[1].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].bodyParticle[1]</td><td>String</td><td>commonAttackSkills[1].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].bodyParticle[2]</td><td>String</td><td>commonAttackSkills[1].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].bodyParticleOffset</td><td>short[]</td><td>commonAttackSkills[1].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].bodyPartPath</td><td>String</td><td>commonAttackSkills[1].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].bodyPartAnimation</td><td>String</td><td>commonAttackSkills[1].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].bodyPartAnimationOffset</td><td>short[]</td><td>commonAttackSkills[1].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].footParticle[0]</td><td>String</td><td>commonAttackSkills[1].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].footParticle[1]</td><td>String</td><td>commonAttackSkills[1].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].footParticle[2]</td><td>String</td><td>commonAttackSkills[1].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].footParticleOffset</td><td>short[]</td><td>commonAttackSkills[1].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].footPartPath</td><td>String</td><td>commonAttackSkills[1].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].footPartAnimation</td><td>String</td><td>commonAttackSkills[1].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].footPartAnimationOffset</td><td>short[]</td><td>commonAttackSkills[1].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].targetFootParticle[0]</td><td>String</td><td>commonAttackSkills[1].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].targetFootParticle[1]</td><td>String</td><td>commonAttackSkills[1].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].targetFootParticle[2]</td><td>String</td><td>commonAttackSkills[1].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[1].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[1].buffName</td><td>String</td><td>commonAttackSkills[1].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].effectiveTimes</td><td>int[]</td><td>commonAttackSkills[2].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].style1</td><td>String</td><td>commonAttackSkills[2].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].style2</td><td>String</td><td>commonAttackSkills[2].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].effectExplosionLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].effectType</td><td>String</td><td>commonAttackSkills[2].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].avataRace</td><td>String</td><td>commonAttackSkills[2].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].avataSex</td><td>String</td><td>commonAttackSkills[2].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].effectLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].flySound</td><td>String</td><td>commonAttackSkills[2].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].explodeSound</td><td>String</td><td>commonAttackSkills[2].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].flyParticle[0]</td><td>String</td><td>commonAttackSkills[2].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].flyParticle[1]</td><td>String</td><td>commonAttackSkills[2].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].flyParticle[2]</td><td>String</td><td>commonAttackSkills[2].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].explodeParticle[0]</td><td>String</td><td>commonAttackSkills[2].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].explodeParticle[1]</td><td>String</td><td>commonAttackSkills[2].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].explodeParticle[2]</td><td>String</td><td>commonAttackSkills[2].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].bodyParticle[0]</td><td>String</td><td>commonAttackSkills[2].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].bodyParticle[1]</td><td>String</td><td>commonAttackSkills[2].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].bodyParticle[2]</td><td>String</td><td>commonAttackSkills[2].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].bodyParticleOffset</td><td>short[]</td><td>commonAttackSkills[2].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].bodyPartPath</td><td>String</td><td>commonAttackSkills[2].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].bodyPartAnimation</td><td>String</td><td>commonAttackSkills[2].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].bodyPartAnimationOffset</td><td>short[]</td><td>commonAttackSkills[2].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].footParticle[0]</td><td>String</td><td>commonAttackSkills[2].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].footParticle[1]</td><td>String</td><td>commonAttackSkills[2].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].footParticle[2]</td><td>String</td><td>commonAttackSkills[2].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].footParticleOffset</td><td>short[]</td><td>commonAttackSkills[2].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].footPartPath</td><td>String</td><td>commonAttackSkills[2].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].footPartAnimation</td><td>String</td><td>commonAttackSkills[2].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].footPartAnimationOffset</td><td>short[]</td><td>commonAttackSkills[2].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].targetFootParticle[0]</td><td>String</td><td>commonAttackSkills[2].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].targetFootParticle[1]</td><td>String</td><td>commonAttackSkills[2].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].targetFootParticle[2]</td><td>String</td><td>commonAttackSkills[2].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>commonAttackSkills[2].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>commonAttackSkills[2].buffName</td><td>String</td><td>commonAttackSkills[2].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect.length</td><td>int</td><td>4个字节</td><td>SkillWithoutEffect数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].style1</td><td>String</td><td>skillsWithoutEffect[0].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].style2</td><td>String</td><td>skillsWithoutEffect[0].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].effectiveTimes</td><td>int[]</td><td>skillsWithoutEffect[0].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].bodyParticle[0]</td><td>String</td><td>skillsWithoutEffect[0].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].bodyParticle[1]</td><td>String</td><td>skillsWithoutEffect[0].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].bodyParticle[2]</td><td>String</td><td>skillsWithoutEffect[0].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].bodyParticleOffset</td><td>short[]</td><td>skillsWithoutEffect[0].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].bodyPartPath</td><td>String</td><td>skillsWithoutEffect[0].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].bodyPartAnimation</td><td>String</td><td>skillsWithoutEffect[0].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].bodyPartAnimationOffset</td><td>short[]</td><td>skillsWithoutEffect[0].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].footParticle[0]</td><td>String</td><td>skillsWithoutEffect[0].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].footParticle[1]</td><td>String</td><td>skillsWithoutEffect[0].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].footParticle[2]</td><td>String</td><td>skillsWithoutEffect[0].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].footParticleOffset</td><td>short[]</td><td>skillsWithoutEffect[0].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].footPartPath</td><td>String</td><td>skillsWithoutEffect[0].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].footPartAnimation</td><td>String</td><td>skillsWithoutEffect[0].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].footPartAnimationOffset</td><td>short[]</td><td>skillsWithoutEffect[0].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].targetFootParticle[0]</td><td>String</td><td>skillsWithoutEffect[0].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].targetFootParticle[1]</td><td>String</td><td>skillsWithoutEffect[0].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].targetFootParticle[2]</td><td>String</td><td>skillsWithoutEffect[0].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[0].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[0].buffName</td><td>String</td><td>skillsWithoutEffect[0].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].style1</td><td>String</td><td>skillsWithoutEffect[1].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].style2</td><td>String</td><td>skillsWithoutEffect[1].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].effectiveTimes</td><td>int[]</td><td>skillsWithoutEffect[1].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].bodyParticle[0]</td><td>String</td><td>skillsWithoutEffect[1].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].bodyParticle[1]</td><td>String</td><td>skillsWithoutEffect[1].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].bodyParticle[2]</td><td>String</td><td>skillsWithoutEffect[1].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].bodyParticleOffset</td><td>short[]</td><td>skillsWithoutEffect[1].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].bodyPartPath</td><td>String</td><td>skillsWithoutEffect[1].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].bodyPartAnimation</td><td>String</td><td>skillsWithoutEffect[1].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].bodyPartAnimationOffset</td><td>short[]</td><td>skillsWithoutEffect[1].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].footParticle[0]</td><td>String</td><td>skillsWithoutEffect[1].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].footParticle[1]</td><td>String</td><td>skillsWithoutEffect[1].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].footParticle[2]</td><td>String</td><td>skillsWithoutEffect[1].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].footParticleOffset</td><td>short[]</td><td>skillsWithoutEffect[1].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].footPartPath</td><td>String</td><td>skillsWithoutEffect[1].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].footPartAnimation</td><td>String</td><td>skillsWithoutEffect[1].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].footPartAnimationOffset</td><td>short[]</td><td>skillsWithoutEffect[1].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].targetFootParticle[0]</td><td>String</td><td>skillsWithoutEffect[1].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].targetFootParticle[1]</td><td>String</td><td>skillsWithoutEffect[1].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].targetFootParticle[2]</td><td>String</td><td>skillsWithoutEffect[1].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[1].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[1].buffName</td><td>String</td><td>skillsWithoutEffect[1].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].style1</td><td>String</td><td>skillsWithoutEffect[2].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].style2</td><td>String</td><td>skillsWithoutEffect[2].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].effectiveTimes</td><td>int[]</td><td>skillsWithoutEffect[2].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].bodyParticle[0]</td><td>String</td><td>skillsWithoutEffect[2].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].bodyParticle[1]</td><td>String</td><td>skillsWithoutEffect[2].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].bodyParticle[2]</td><td>String</td><td>skillsWithoutEffect[2].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].bodyParticleOffset</td><td>short[]</td><td>skillsWithoutEffect[2].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].bodyPartPath</td><td>String</td><td>skillsWithoutEffect[2].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].bodyPartAnimation</td><td>String</td><td>skillsWithoutEffect[2].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].bodyPartAnimationOffset</td><td>short[]</td><td>skillsWithoutEffect[2].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].footParticle[0]</td><td>String</td><td>skillsWithoutEffect[2].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].footParticle[1]</td><td>String</td><td>skillsWithoutEffect[2].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].footParticle[2]</td><td>String</td><td>skillsWithoutEffect[2].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].footParticleOffset</td><td>short[]</td><td>skillsWithoutEffect[2].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].footPartPath</td><td>String</td><td>skillsWithoutEffect[2].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].footPartAnimation</td><td>String</td><td>skillsWithoutEffect[2].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].footPartAnimationOffset</td><td>short[]</td><td>skillsWithoutEffect[2].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].targetFootParticle[0]</td><td>String</td><td>skillsWithoutEffect[2].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].targetFootParticle[1]</td><td>String</td><td>skillsWithoutEffect[2].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].targetFootParticle[2]</td><td>String</td><td>skillsWithoutEffect[2].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffect[2].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffect[2].buffName</td><td>String</td><td>skillsWithoutEffect[2].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove.length</td><td>int</td><td>4个字节</td><td>SkillWithoutEffectAndQuickMove数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].style2</td><td>String</td><td>skillsWithoutEffectAndQuickMove[0].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].effectiveTimes</td><td>int[]</td><td>skillsWithoutEffectAndQuickMove[0].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].style1</td><td>String</td><td>skillsWithoutEffectAndQuickMove[0].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].traceType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].distance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].mp</td><td>short[]</td><td>skillsWithoutEffectAndQuickMove[0].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyParticle[0]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[0].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyParticle[1]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[0].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyParticle[2]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[0].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyParticleOffset</td><td>short[]</td><td>skillsWithoutEffectAndQuickMove[0].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyPartPath</td><td>String</td><td>skillsWithoutEffectAndQuickMove[0].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyPartAnimation</td><td>String</td><td>skillsWithoutEffectAndQuickMove[0].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyPartAnimationOffset</td><td>short[]</td><td>skillsWithoutEffectAndQuickMove[0].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].footParticle[0]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[0].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].footParticle[1]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[0].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].footParticle[2]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[0].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].footParticleOffset</td><td>short[]</td><td>skillsWithoutEffectAndQuickMove[0].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].footPartPath</td><td>String</td><td>skillsWithoutEffectAndQuickMove[0].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].footPartAnimation</td><td>String</td><td>skillsWithoutEffectAndQuickMove[0].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].footPartAnimationOffset</td><td>short[]</td><td>skillsWithoutEffectAndQuickMove[0].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].targetFootParticle[0]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[0].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].targetFootParticle[1]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[0].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].targetFootParticle[2]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[0].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[0].buffName</td><td>String</td><td>skillsWithoutEffectAndQuickMove[0].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[0].skillPlayType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].style2</td><td>String</td><td>skillsWithoutEffectAndQuickMove[1].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].effectiveTimes</td><td>int[]</td><td>skillsWithoutEffectAndQuickMove[1].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].style1</td><td>String</td><td>skillsWithoutEffectAndQuickMove[1].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].traceType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].distance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].mp</td><td>short[]</td><td>skillsWithoutEffectAndQuickMove[1].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyParticle[0]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[1].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyParticle[1]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[1].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyParticle[2]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[1].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyParticleOffset</td><td>short[]</td><td>skillsWithoutEffectAndQuickMove[1].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyPartPath</td><td>String</td><td>skillsWithoutEffectAndQuickMove[1].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyPartAnimation</td><td>String</td><td>skillsWithoutEffectAndQuickMove[1].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyPartAnimationOffset</td><td>short[]</td><td>skillsWithoutEffectAndQuickMove[1].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].footParticle[0]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[1].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].footParticle[1]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[1].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].footParticle[2]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[1].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].footParticleOffset</td><td>short[]</td><td>skillsWithoutEffectAndQuickMove[1].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].footPartPath</td><td>String</td><td>skillsWithoutEffectAndQuickMove[1].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].footPartAnimation</td><td>String</td><td>skillsWithoutEffectAndQuickMove[1].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].footPartAnimationOffset</td><td>short[]</td><td>skillsWithoutEffectAndQuickMove[1].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].targetFootParticle[0]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[1].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].targetFootParticle[1]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[1].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].targetFootParticle[2]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[1].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[1].buffName</td><td>String</td><td>skillsWithoutEffectAndQuickMove[1].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[1].skillPlayType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].style2</td><td>String</td><td>skillsWithoutEffectAndQuickMove[2].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].effectiveTimes</td><td>int[]</td><td>skillsWithoutEffectAndQuickMove[2].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].style1</td><td>String</td><td>skillsWithoutEffectAndQuickMove[2].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].traceType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].distance</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].mp</td><td>short[]</td><td>skillsWithoutEffectAndQuickMove[2].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyParticle[0]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[2].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyParticle[1]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[2].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyParticle[2]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[2].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyParticleOffset</td><td>short[]</td><td>skillsWithoutEffectAndQuickMove[2].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyPartPath</td><td>String</td><td>skillsWithoutEffectAndQuickMove[2].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyPartAnimation</td><td>String</td><td>skillsWithoutEffectAndQuickMove[2].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyPartAnimationOffset</td><td>short[]</td><td>skillsWithoutEffectAndQuickMove[2].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].footParticle[0]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[2].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].footParticle[1]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[2].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].footParticle[2]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[2].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].footParticleOffset</td><td>short[]</td><td>skillsWithoutEffectAndQuickMove[2].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].footPartPath</td><td>String</td><td>skillsWithoutEffectAndQuickMove[2].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].footPartAnimation</td><td>String</td><td>skillsWithoutEffectAndQuickMove[2].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].footPartAnimationOffset</td><td>short[]</td><td>skillsWithoutEffectAndQuickMove[2].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].targetFootParticle[0]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[2].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].targetFootParticle[1]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[2].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].targetFootParticle[2]</td><td>String</td><td>skillsWithoutEffectAndQuickMove[2].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutEffectAndQuickMove[2].buffName</td><td>String</td><td>skillsWithoutEffectAndQuickMove[2].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutEffectAndQuickMove[2].skillPlayType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange.length</td><td>int</td><td>4个字节</td><td>SkillWithoutTraceAndWithRange数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].style2</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].effectiveTimes</td><td>int[]</td><td>skillsWithoutTraceAndWithRange[0].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].style1</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].rangeType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].rangeWidth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].rangeHeight</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].maxAttackNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].maxAttackNums</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[0].maxAttackNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].effectType</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].avataRace</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].avataSex</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].effectLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].effectExplosionLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].mp</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[0].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].flySound</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].explodeSound</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].flyParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].flyParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].flyParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].explodeParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].explodeParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].explodeParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyParticleOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[0].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyPartPath</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyPartAnimation</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyPartAnimationOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[0].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].footParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].footParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].footParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].footParticleOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[0].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].footPartPath</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].footPartAnimation</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].footPartAnimationOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[0].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].targetFootParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].targetFootParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].targetFootParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[0].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[0].buffName</td><td>String</td><td>skillsWithoutTraceAndWithRange[0].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].style2</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].effectiveTimes</td><td>int[]</td><td>skillsWithoutTraceAndWithRange[1].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].style1</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].rangeType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].rangeWidth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].rangeHeight</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].maxAttackNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].maxAttackNums</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[1].maxAttackNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].effectType</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].avataRace</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].avataSex</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].effectLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].effectExplosionLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].mp</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[1].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].flySound</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].explodeSound</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].flyParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].flyParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].flyParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].explodeParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].explodeParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].explodeParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyParticleOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[1].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyPartPath</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyPartAnimation</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyPartAnimationOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[1].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].footParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].footParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].footParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].footParticleOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[1].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].footPartPath</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].footPartAnimation</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].footPartAnimationOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[1].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].targetFootParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].targetFootParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].targetFootParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[1].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[1].buffName</td><td>String</td><td>skillsWithoutTraceAndWithRange[1].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].style2</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].effectiveTimes</td><td>int[]</td><td>skillsWithoutTraceAndWithRange[2].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].style1</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].rangeType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].rangeWidth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].rangeHeight</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].maxAttackNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].maxAttackNums</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[2].maxAttackNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].effectType</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].avataRace</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].avataSex</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].effectLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].effectExplosionLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].mp</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[2].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].flySound</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].explodeSound</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].flyParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].flyParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].flyParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].explodeParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].explodeParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].explodeParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyParticleOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[2].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyPartPath</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyPartAnimation</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyPartAnimationOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[2].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].footParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].footParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].footParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].footParticleOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[2].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].footPartPath</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].footPartAnimation</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].footPartAnimationOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithRange[2].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].targetFootParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].targetFootParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].targetFootParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithRange[2].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithRange[2].buffName</td><td>String</td><td>skillsWithoutTraceAndWithRange[2].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition.length</td><td>int</td><td>4个字节</td><td>SkillWithoutTraceAndWithTargetOrPosition数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].style2</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].effectiveTimes</td><td>int[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].style1</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].effectType</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].avataRace</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].avataSex</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].effectLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].effectExplosionLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].mp</td><td>short[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].flySound</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].explodeSound</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].flyParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].flyParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].flyParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].explodeParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].explodeParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].explodeParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyParticleOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyPartPath</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyPartAnimation</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyPartAnimationOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footParticleOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footPartPath</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footPartAnimation</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footPartAnimationOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].targetFootParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].targetFootParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].targetFootParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[0].buffName</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[0].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].style2</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].effectiveTimes</td><td>int[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].style1</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].effectType</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].avataRace</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].avataSex</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].effectLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].effectExplosionLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].mp</td><td>short[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].flySound</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].explodeSound</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].flyParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].flyParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].flyParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].explodeParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].explodeParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].explodeParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyParticleOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyPartPath</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyPartAnimation</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyPartAnimationOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footParticleOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footPartPath</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footPartAnimation</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footPartAnimationOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].targetFootParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].targetFootParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].targetFootParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[1].buffName</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[1].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].style2</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].effectiveTimes</td><td>int[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].style1</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].effectType</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].avataRace</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].avataSex</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].effectLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].effectExplosionLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].mp</td><td>short[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].flySound</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].explodeSound</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].flyParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].flyParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].flyParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].explodeParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].explodeParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].explodeParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyParticleOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyPartPath</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyPartAnimation</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyPartAnimationOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footParticleOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footPartPath</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footPartAnimation</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footPartAnimationOffset</td><td>short[]</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].targetFootParticle[0]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].targetFootParticle[1]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].targetFootParticle[2]</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithoutTraceAndWithTargetOrPosition[2].buffName</td><td>String</td><td>skillsWithoutTraceAndWithTargetOrPosition[2].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC.length</td><td>int</td><td>4个字节</td><td>SkillWithoutTraceAndWithSummonNPC数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].style2</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].effectiveTimes</td><td>int[]</td><td>skillWithoutTraceAndWithSummonNPC[0].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].style1</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].matrixType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].attackNum.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].attackNum</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[0].attackNum.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].mp</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[0].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].gapWidth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].gapHeight</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].maxTimeLength</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].heigth</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].maxParticleEachTime</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].flySound</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].explodeSound</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].flyParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].flyParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].flyParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].explodeParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].explodeParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].explodeParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[0].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyPartPath</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyPartAnimation</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[0].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[0].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footPartPath</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footPartAnimation</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[0].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].targetFootParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].targetFootParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].targetFootParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[0].buffName</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[0].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].style2</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].effectiveTimes</td><td>int[]</td><td>skillWithoutTraceAndWithSummonNPC[1].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].style1</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].matrixType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].attackNum.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].attackNum</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[1].attackNum.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].mp</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[1].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].gapWidth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].gapHeight</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].maxTimeLength</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].heigth</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].maxParticleEachTime</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].flySound</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].explodeSound</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].flyParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].flyParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].flyParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].explodeParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].explodeParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].explodeParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[1].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyPartPath</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyPartAnimation</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[1].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[1].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footPartPath</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footPartAnimation</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[1].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].targetFootParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].targetFootParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].targetFootParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[1].buffName</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[1].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].style2</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].effectiveTimes</td><td>int[]</td><td>skillWithoutTraceAndWithSummonNPC[2].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].style1</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].matrixType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].attackNum.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].attackNum</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[2].attackNum.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].mp</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[2].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].gapWidth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].gapHeight</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].maxTimeLength</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].speed</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].heigth</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].maxParticleEachTime</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].flySound</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].explodeSound</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].flyParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].flyParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].flyParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].explodeParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].explodeParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].explodeParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[2].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyPartPath</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyPartAnimation</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[2].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[2].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footPartPath</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footPartAnimation</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndWithSummonNPC[2].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].targetFootParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].targetFootParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].targetFootParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithSummonNPC[2].buffName</td><td>String</td><td>skillWithoutTraceAndWithSummonNPC[2].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget.length</td><td>int</td><td>4个字节</td><td>SkillWithTraceAndDirectionOrTarget数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].style2</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].effectiveTimes</td><td>int[]</td><td>skillsWithTraceAndDirectionOrTarget[0].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].style1</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].targetType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].effectType</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].avataRace</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].avataSex</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].trackType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].speed</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].attackWidth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].explosionLastingTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].effectNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].effectInitPositionX.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].effectInitPositionX</td><td>int[]</td><td>skillsWithTraceAndDirectionOrTarget[0].effectInitPositionX.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].effectInitPositionY.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].effectInitPositionY</td><td>int[]</td><td>skillsWithTraceAndDirectionOrTarget[0].effectInitPositionY.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].effectInitDirection.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].effectInitDirection</td><td>byte[]</td><td>skillsWithTraceAndDirectionOrTarget[0].effectInitDirection.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].penetrateNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].mp</td><td>short[]</td><td>skillsWithTraceAndDirectionOrTarget[0].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].flySound</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].explodeSound</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].flyParticle[0]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].flyParticle[1]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].flyParticle[2]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].explodeParticle[0]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].explodeParticle[1]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].explodeParticle[2]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyParticle[0]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyParticle[1]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyParticle[2]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyParticleOffset</td><td>short[]</td><td>skillsWithTraceAndDirectionOrTarget[0].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyPartPath</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyPartAnimation</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyPartAnimationOffset</td><td>short[]</td><td>skillsWithTraceAndDirectionOrTarget[0].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footParticle[0]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footParticle[1]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footParticle[2]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footParticleOffset</td><td>short[]</td><td>skillsWithTraceAndDirectionOrTarget[0].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footPartPath</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footPartAnimation</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footPartAnimationOffset</td><td>short[]</td><td>skillsWithTraceAndDirectionOrTarget[0].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].targetFootParticle[0]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].targetFootParticle[1]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].targetFootParticle[2]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].sendEffectTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[0].buffName</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[0].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].style2</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].effectiveTimes</td><td>int[]</td><td>skillsWithTraceAndDirectionOrTarget[1].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].style1</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].targetType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].effectType</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].avataRace</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].avataSex</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].trackType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].speed</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].attackWidth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].explosionLastingTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].effectNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].effectInitPositionX.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].effectInitPositionX</td><td>int[]</td><td>skillsWithTraceAndDirectionOrTarget[1].effectInitPositionX.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].effectInitPositionY.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].effectInitPositionY</td><td>int[]</td><td>skillsWithTraceAndDirectionOrTarget[1].effectInitPositionY.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].effectInitDirection.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].effectInitDirection</td><td>byte[]</td><td>skillsWithTraceAndDirectionOrTarget[1].effectInitDirection.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].penetrateNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].mp</td><td>short[]</td><td>skillsWithTraceAndDirectionOrTarget[1].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].flySound</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].explodeSound</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].flyParticle[0]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].flyParticle[1]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].flyParticle[2]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].explodeParticle[0]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].explodeParticle[1]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].explodeParticle[2]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyParticle[0]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyParticle[1]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyParticle[2]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyParticleOffset</td><td>short[]</td><td>skillsWithTraceAndDirectionOrTarget[1].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyPartPath</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyPartAnimation</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyPartAnimationOffset</td><td>short[]</td><td>skillsWithTraceAndDirectionOrTarget[1].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footParticle[0]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footParticle[1]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footParticle[2]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footParticleOffset</td><td>short[]</td><td>skillsWithTraceAndDirectionOrTarget[1].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footPartPath</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footPartAnimation</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footPartAnimationOffset</td><td>short[]</td><td>skillsWithTraceAndDirectionOrTarget[1].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].targetFootParticle[0]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].targetFootParticle[1]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].targetFootParticle[2]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].sendEffectTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[1].buffName</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[1].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].style2</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].effectiveTimes</td><td>int[]</td><td>skillsWithTraceAndDirectionOrTarget[2].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].style1</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].targetType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].effectType</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].avataRace</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].avataSex</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].trackType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].speed</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].attackWidth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].explosionLastingTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].effectNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].effectInitPositionX.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].effectInitPositionX</td><td>int[]</td><td>skillsWithTraceAndDirectionOrTarget[2].effectInitPositionX.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].effectInitPositionY.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].effectInitPositionY</td><td>int[]</td><td>skillsWithTraceAndDirectionOrTarget[2].effectInitPositionY.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].effectInitDirection.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].effectInitDirection</td><td>byte[]</td><td>skillsWithTraceAndDirectionOrTarget[2].effectInitDirection.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].penetrateNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].mp</td><td>short[]</td><td>skillsWithTraceAndDirectionOrTarget[2].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].flySound</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].explodeSound</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].flyParticle[0]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].flyParticle[1]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].flyParticle[2]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].explodeParticle[0]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].explodeParticle[1]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].explodeParticle[2]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyParticle[0]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyParticle[1]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyParticle[2]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyParticleOffset</td><td>short[]</td><td>skillsWithTraceAndDirectionOrTarget[2].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyPartPath</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyPartAnimation</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyPartAnimationOffset</td><td>short[]</td><td>skillsWithTraceAndDirectionOrTarget[2].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footParticle[0]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footParticle[1]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footParticle[2]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footParticleOffset</td><td>short[]</td><td>skillsWithTraceAndDirectionOrTarget[2].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footPartPath</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footPartAnimation</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footPartAnimationOffset</td><td>short[]</td><td>skillsWithTraceAndDirectionOrTarget[2].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].targetFootParticle[0]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].targetFootParticle[1]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].targetFootParticle[2]</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].sendEffectTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillsWithTraceAndDirectionOrTarget[2].buffName</td><td>String</td><td>skillsWithTraceAndDirectionOrTarget[2].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix.length</td><td>int</td><td>4个字节</td><td>SkillWithoutTraceAndWithMatrix数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].style1</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].style2</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].effectiveTimes</td><td>int[]</td><td>skillWithoutTraceAndWithMatrix[0].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].matrixType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].gapWidth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].gapHeight</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].effectNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].effectType</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].avataRace</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].avataSex</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].effectLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].effectExplosionLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].mp</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[0].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].flySound</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].explodeSound</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].flyParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].flyParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].flyParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].explodeParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].explodeParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].explodeParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[0].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyPartPath</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyPartAnimation</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[0].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].footParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].footParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].footParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].footParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[0].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].footPartPath</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].footPartAnimation</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].footPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[0].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].targetFootParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].targetFootParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].targetFootParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].buffName</td><td>String</td><td>skillWithoutTraceAndWithMatrix[0].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[0].maxAttackNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[0].maxAttackNums</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[0].maxAttackNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].style1</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].style2</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].effectiveTimes</td><td>int[]</td><td>skillWithoutTraceAndWithMatrix[1].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].matrixType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].gapWidth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].gapHeight</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].effectNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].effectType</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].avataRace</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].avataSex</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].effectLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].effectExplosionLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].mp</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[1].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].flySound</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].explodeSound</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].flyParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].flyParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].flyParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].explodeParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].explodeParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].explodeParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[1].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyPartPath</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyPartAnimation</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[1].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].footParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].footParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].footParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].footParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[1].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].footPartPath</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].footPartAnimation</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].footPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[1].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].targetFootParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].targetFootParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].targetFootParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].buffName</td><td>String</td><td>skillWithoutTraceAndWithMatrix[1].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[1].maxAttackNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[1].maxAttackNums</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[1].maxAttackNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].style1</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].style2</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].effectiveTimes</td><td>int[]</td><td>skillWithoutTraceAndWithMatrix[2].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].matrixType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].gapWidth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].gapHeight</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].effectNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].effectType</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].avataRace</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].avataSex</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].effectLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].effectExplosionLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].mp</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[2].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].range</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].flySound</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].explodeSound</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].flyParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].flyParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].flyParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].explodeParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].explodeParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].explodeParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[2].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyPartPath</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyPartAnimation</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[2].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].footParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].footParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].footParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].footParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[2].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].footPartPath</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].footPartAnimation</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].footPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[2].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].targetFootParticle[0]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].targetFootParticle[1]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].targetFootParticle[2]</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].buffName</td><td>String</td><td>skillWithoutTraceAndWithMatrix[2].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndWithMatrix[2].maxAttackNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndWithMatrix[2].maxAttackNums</td><td>short[]</td><td>skillWithoutTraceAndWithMatrix[2].maxAttackNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember.length</td><td>int</td><td>4个字节</td><td>SkillWithoutTraceAndOnTeamMember数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].style1</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].style2</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].effectiveTimes</td><td>int[]</td><td>skillWithoutTraceAndOnTeamMember[0].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].rangeType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].rangeWidth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].rangeHeight</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].effectType</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].avataRace</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].avataSex</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].effectLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].effectExplosionLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].mp</td><td>short[]</td><td>skillWithoutTraceAndOnTeamMember[0].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].flySound</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].explodeSound</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].flyParticle[0]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].flyParticle[1]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].flyParticle[2]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].explodeParticle[0]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].explodeParticle[1]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].explodeParticle[2]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyParticle[0]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyParticle[1]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyParticle[2]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndOnTeamMember[0].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyPartPath</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyPartAnimation</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndOnTeamMember[0].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footParticle[0]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footParticle[1]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footParticle[2]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndOnTeamMember[0].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footPartPath</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footPartAnimation</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndOnTeamMember[0].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].targetFootParticle[0]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].targetFootParticle[1]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].targetFootParticle[2]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[0].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[0].buffName</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[0].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].style1</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].style2</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].effectiveTimes</td><td>int[]</td><td>skillWithoutTraceAndOnTeamMember[1].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].rangeType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].rangeWidth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].rangeHeight</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].effectType</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].avataRace</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].avataSex</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].effectLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].effectExplosionLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].mp</td><td>short[]</td><td>skillWithoutTraceAndOnTeamMember[1].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].flySound</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].explodeSound</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].flyParticle[0]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].flyParticle[1]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].flyParticle[2]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].explodeParticle[0]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].explodeParticle[1]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].explodeParticle[2]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyParticle[0]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyParticle[1]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyParticle[2]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndOnTeamMember[1].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyPartPath</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyPartAnimation</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndOnTeamMember[1].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footParticle[0]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footParticle[1]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footParticle[2]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndOnTeamMember[1].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footPartPath</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footPartAnimation</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndOnTeamMember[1].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].targetFootParticle[0]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].targetFootParticle[1]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].targetFootParticle[2]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[1].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[1].buffName</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[1].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].enableWeaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].weaponTypeLimit</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].followByCommonAttack</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].ignoreStun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].duration1</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].duration2</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].duration3</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].style1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].style1</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].style1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].style2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].style2</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].style2.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].effectiveTimes.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].effectiveTimes</td><td>int[]</td><td>skillWithoutTraceAndOnTeamMember[2].effectiveTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].rangeType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].rangeWidth</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].rangeHeight</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].effectType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].effectType</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].effectType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].avataRace.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].avataRace</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].avataRace.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].avataSex.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].avataSex</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].avataSex.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].effectLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].effectExplosionLastTime</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].mp.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].mp</td><td>short[]</td><td>skillWithoutTraceAndOnTeamMember[2].mp.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].flySound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].flySound</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].flySound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].explodeSound.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].explodeSound</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].explodeSound.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].flyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].flyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].flyParticle[0]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].flyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].flyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].flyParticle[1]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].flyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].flyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].flyParticle[2]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].flyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].explodeParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].explodeParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].explodeParticle[0]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].explodeParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].explodeParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].explodeParticle[1]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].explodeParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].explodeParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].explodeParticle[2]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].explodeParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].explodePercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyParticle[0]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].bodyParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyParticle[1]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].bodyParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyParticle[2]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].bodyParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndOnTeamMember[2].bodyParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyPartPath</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].bodyPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyPartAnimation</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].bodyPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyPartAnimationPercent</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndOnTeamMember[2].bodyPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].bodyPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footParticle[0]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].footParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footParticle[1]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].footParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footParticle[2]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].footParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footParticleOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footParticleOffset</td><td>short[]</td><td>skillWithoutTraceAndOnTeamMember[2].footParticleOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footParticlePlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footParticlePlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footPartPath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footPartPath</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].footPartPath.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footPartAnimation.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footPartAnimation</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].footPartAnimation.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footPartAnimationOffset.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footPartAnimationOffset</td><td>short[]</td><td>skillWithoutTraceAndOnTeamMember[2].footPartAnimationOffset.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footPartAnimationPlayStart</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].footPartAnimationPlayEnd</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].angle</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].targetFootParticle.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].targetFootParticle[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].targetFootParticle[0]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].targetFootParticle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].targetFootParticle[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].targetFootParticle[1]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].targetFootParticle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].targetFootParticle[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].targetFootParticle[2]</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].targetFootParticle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].guajiFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillWithoutTraceAndOnTeamMember[2].buffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillWithoutTraceAndOnTeamMember[2].buffName</td><td>String</td><td>skillWithoutTraceAndOnTeamMember[2].buffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class SEND_ACTIVESKILL_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int[] commonAttackSkillIdsForWeapon;
	CommonAttackSkill[] commonAttackSkills;
	SkillWithoutEffect[] skillsWithoutEffect;
	SkillWithoutEffectAndQuickMove[] skillsWithoutEffectAndQuickMove;
	SkillWithoutTraceAndWithRange[] skillsWithoutTraceAndWithRange;
	SkillWithoutTraceAndWithTargetOrPosition[] skillsWithoutTraceAndWithTargetOrPosition;
	SkillWithoutTraceAndWithSummonNPC[] skillWithoutTraceAndWithSummonNPC;
	SkillWithTraceAndDirectionOrTarget[] skillsWithTraceAndDirectionOrTarget;
	SkillWithoutTraceAndWithMatrix[] skillWithoutTraceAndWithMatrix;
	SkillWithoutTraceAndOnTeamMember[] skillWithoutTraceAndOnTeamMember;

	public SEND_ACTIVESKILL_REQ(){
	}

	public SEND_ACTIVESKILL_REQ(long seqNum,int[] commonAttackSkillIdsForWeapon,CommonAttackSkill[] commonAttackSkills,SkillWithoutEffect[] skillsWithoutEffect,SkillWithoutEffectAndQuickMove[] skillsWithoutEffectAndQuickMove,SkillWithoutTraceAndWithRange[] skillsWithoutTraceAndWithRange,SkillWithoutTraceAndWithTargetOrPosition[] skillsWithoutTraceAndWithTargetOrPosition,SkillWithoutTraceAndWithSummonNPC[] skillWithoutTraceAndWithSummonNPC,SkillWithTraceAndDirectionOrTarget[] skillsWithTraceAndDirectionOrTarget,SkillWithoutTraceAndWithMatrix[] skillWithoutTraceAndWithMatrix,SkillWithoutTraceAndOnTeamMember[] skillWithoutTraceAndOnTeamMember){
		this.seqNum = seqNum;
		this.commonAttackSkillIdsForWeapon = commonAttackSkillIdsForWeapon;
		this.commonAttackSkills = commonAttackSkills;
		this.skillsWithoutEffect = skillsWithoutEffect;
		this.skillsWithoutEffectAndQuickMove = skillsWithoutEffectAndQuickMove;
		this.skillsWithoutTraceAndWithRange = skillsWithoutTraceAndWithRange;
		this.skillsWithoutTraceAndWithTargetOrPosition = skillsWithoutTraceAndWithTargetOrPosition;
		this.skillWithoutTraceAndWithSummonNPC = skillWithoutTraceAndWithSummonNPC;
		this.skillsWithTraceAndDirectionOrTarget = skillsWithTraceAndDirectionOrTarget;
		this.skillWithoutTraceAndWithMatrix = skillWithoutTraceAndWithMatrix;
		this.skillWithoutTraceAndOnTeamMember = skillWithoutTraceAndOnTeamMember;
	}

	public SEND_ACTIVESKILL_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		commonAttackSkillIdsForWeapon = new int[len];
		for(int i = 0 ; i < commonAttackSkillIdsForWeapon.length ; i++){
			commonAttackSkillIdsForWeapon[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		commonAttackSkills = new CommonAttackSkill[len];
		for(int i = 0 ; i < commonAttackSkills.length ; i++){
			commonAttackSkills[i] = new CommonAttackSkill();
			commonAttackSkills[i].setDuration1((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			commonAttackSkills[i].setDuration2((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			commonAttackSkills[i].setDuration3((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] effectiveTimes_0001 = new int[len];
			for(int j = 0 ; j < effectiveTimes_0001.length ; j++){
				effectiveTimes_0001[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			commonAttackSkills[i].setEffectiveTimes(effectiveTimes_0001);
			commonAttackSkills[i].setEnableWeaponType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			commonAttackSkills[i].setWeaponTypeLimit((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			commonAttackSkills[i].setFollowByCommonAttack((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			commonAttackSkills[i].setIgnoreStun(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			commonAttackSkills[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			commonAttackSkills[i].setStyle1(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			commonAttackSkills[i].setStyle2(new String(content,offset,len,"UTF-8"));
			offset += len;
			commonAttackSkills[i].setEffectExplosionLastTime((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			commonAttackSkills[i].setRange((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			commonAttackSkills[i].setEffectType(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			commonAttackSkills[i].setAvataRace(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			commonAttackSkills[i].setAvataSex(new String(content,offset,len,"UTF-8"));
			offset += len;
			commonAttackSkills[i].setEffectLastTime((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			commonAttackSkills[i].setFlySound(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			commonAttackSkills[i].setExplodeSound(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] flyParticle_0002 = new String[len];
			for(int j = 0 ; j < flyParticle_0002.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				flyParticle_0002[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			commonAttackSkills[i].setFlyParticle(flyParticle_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] explodeParticle_0003 = new String[len];
			for(int j = 0 ; j < explodeParticle_0003.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				explodeParticle_0003[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			commonAttackSkills[i].setExplodeParticle(explodeParticle_0003);
			commonAttackSkills[i].setExplodePercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] bodyParticle_0004 = new String[len];
			for(int j = 0 ; j < bodyParticle_0004.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				bodyParticle_0004[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			commonAttackSkills[i].setBodyParticle(bodyParticle_0004);
			commonAttackSkills[i].setBodyPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyParticleOffset_0005 = new short[len];
			for(int j = 0 ; j < bodyParticleOffset_0005.length ; j++){
				bodyParticleOffset_0005[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			commonAttackSkills[i].setBodyParticleOffset(bodyParticleOffset_0005);
			commonAttackSkills[i].setBodyParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			commonAttackSkills[i].setBodyParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			commonAttackSkills[i].setBodyPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			commonAttackSkills[i].setBodyPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			commonAttackSkills[i].setBodyPartAnimationPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyPartAnimationOffset_0006 = new short[len];
			for(int j = 0 ; j < bodyPartAnimationOffset_0006.length ; j++){
				bodyPartAnimationOffset_0006[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			commonAttackSkills[i].setBodyPartAnimationOffset(bodyPartAnimationOffset_0006);
			commonAttackSkills[i].setBodyPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			commonAttackSkills[i].setBodyPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] footParticle_0007 = new String[len];
			for(int j = 0 ; j < footParticle_0007.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				footParticle_0007[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			commonAttackSkills[i].setFootParticle(footParticle_0007);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footParticleOffset_0008 = new short[len];
			for(int j = 0 ; j < footParticleOffset_0008.length ; j++){
				footParticleOffset_0008[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			commonAttackSkills[i].setFootParticleOffset(footParticleOffset_0008);
			commonAttackSkills[i].setFootParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			commonAttackSkills[i].setFootParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			commonAttackSkills[i].setFootPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			commonAttackSkills[i].setFootPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footPartAnimationOffset_0009 = new short[len];
			for(int j = 0 ; j < footPartAnimationOffset_0009.length ; j++){
				footPartAnimationOffset_0009[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			commonAttackSkills[i].setFootPartAnimationOffset(footPartAnimationOffset_0009);
			commonAttackSkills[i].setFootPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			commonAttackSkills[i].setFootPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			commonAttackSkills[i].setAngle((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] targetFootParticle_0010 = new String[len];
			for(int j = 0 ; j < targetFootParticle_0010.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				targetFootParticle_0010[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			commonAttackSkills[i].setTargetFootParticle(targetFootParticle_0010);
			commonAttackSkills[i].setGuajiFlag(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			commonAttackSkills[i].setBuffName(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		skillsWithoutEffect = new SkillWithoutEffect[len];
		for(int i = 0 ; i < skillsWithoutEffect.length ; i++){
			skillsWithoutEffect[i] = new SkillWithoutEffect();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutEffect[i].setStyle1(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutEffect[i].setStyle2(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] effectiveTimes_0011 = new int[len];
			for(int j = 0 ; j < effectiveTimes_0011.length ; j++){
				effectiveTimes_0011[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			skillsWithoutEffect[i].setEffectiveTimes(effectiveTimes_0011);
			skillsWithoutEffect[i].setEnableWeaponType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithoutEffect[i].setWeaponTypeLimit((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithoutEffect[i].setFollowByCommonAttack((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithoutEffect[i].setIgnoreStun(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			skillsWithoutEffect[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutEffect[i].setDuration1((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutEffect[i].setDuration2((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutEffect[i].setDuration3((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutEffect[i].setRange((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] bodyParticle_0012 = new String[len];
			for(int j = 0 ; j < bodyParticle_0012.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				bodyParticle_0012[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithoutEffect[i].setBodyParticle(bodyParticle_0012);
			skillsWithoutEffect[i].setBodyPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyParticleOffset_0013 = new short[len];
			for(int j = 0 ; j < bodyParticleOffset_0013.length ; j++){
				bodyParticleOffset_0013[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutEffect[i].setBodyParticleOffset(bodyParticleOffset_0013);
			skillsWithoutEffect[i].setBodyParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutEffect[i].setBodyParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutEffect[i].setBodyPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutEffect[i].setBodyPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillsWithoutEffect[i].setBodyPartAnimationPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyPartAnimationOffset_0014 = new short[len];
			for(int j = 0 ; j < bodyPartAnimationOffset_0014.length ; j++){
				bodyPartAnimationOffset_0014[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutEffect[i].setBodyPartAnimationOffset(bodyPartAnimationOffset_0014);
			skillsWithoutEffect[i].setBodyPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutEffect[i].setBodyPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] footParticle_0015 = new String[len];
			for(int j = 0 ; j < footParticle_0015.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				footParticle_0015[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithoutEffect[i].setFootParticle(footParticle_0015);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footParticleOffset_0016 = new short[len];
			for(int j = 0 ; j < footParticleOffset_0016.length ; j++){
				footParticleOffset_0016[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutEffect[i].setFootParticleOffset(footParticleOffset_0016);
			skillsWithoutEffect[i].setFootParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutEffect[i].setFootParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutEffect[i].setFootPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutEffect[i].setFootPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footPartAnimationOffset_0017 = new short[len];
			for(int j = 0 ; j < footPartAnimationOffset_0017.length ; j++){
				footPartAnimationOffset_0017[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutEffect[i].setFootPartAnimationOffset(footPartAnimationOffset_0017);
			skillsWithoutEffect[i].setFootPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutEffect[i].setFootPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutEffect[i].setAngle((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] targetFootParticle_0018 = new String[len];
			for(int j = 0 ; j < targetFootParticle_0018.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				targetFootParticle_0018[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithoutEffect[i].setTargetFootParticle(targetFootParticle_0018);
			skillsWithoutEffect[i].setGuajiFlag(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutEffect[i].setBuffName(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		skillsWithoutEffectAndQuickMove = new SkillWithoutEffectAndQuickMove[len];
		for(int i = 0 ; i < skillsWithoutEffectAndQuickMove.length ; i++){
			skillsWithoutEffectAndQuickMove[i] = new SkillWithoutEffectAndQuickMove();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutEffectAndQuickMove[i].setStyle2(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] effectiveTimes_0019 = new int[len];
			for(int j = 0 ; j < effectiveTimes_0019.length ; j++){
				effectiveTimes_0019[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			skillsWithoutEffectAndQuickMove[i].setEffectiveTimes(effectiveTimes_0019);
			skillsWithoutEffectAndQuickMove[i].setEnableWeaponType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithoutEffectAndQuickMove[i].setWeaponTypeLimit((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithoutEffectAndQuickMove[i].setFollowByCommonAttack((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithoutEffectAndQuickMove[i].setIgnoreStun(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			skillsWithoutEffectAndQuickMove[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutEffectAndQuickMove[i].setDuration1((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutEffectAndQuickMove[i].setDuration2((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutEffectAndQuickMove[i].setDuration3((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutEffectAndQuickMove[i].setStyle1(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillsWithoutEffectAndQuickMove[i].setTraceType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutEffectAndQuickMove[i].setDistance((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] mp_0020 = new short[len];
			for(int j = 0 ; j < mp_0020.length ; j++){
				mp_0020[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutEffectAndQuickMove[i].setMp(mp_0020);
			skillsWithoutEffectAndQuickMove[i].setRange((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] bodyParticle_0021 = new String[len];
			for(int j = 0 ; j < bodyParticle_0021.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				bodyParticle_0021[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithoutEffectAndQuickMove[i].setBodyParticle(bodyParticle_0021);
			skillsWithoutEffectAndQuickMove[i].setBodyPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyParticleOffset_0022 = new short[len];
			for(int j = 0 ; j < bodyParticleOffset_0022.length ; j++){
				bodyParticleOffset_0022[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutEffectAndQuickMove[i].setBodyParticleOffset(bodyParticleOffset_0022);
			skillsWithoutEffectAndQuickMove[i].setBodyParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutEffectAndQuickMove[i].setBodyParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutEffectAndQuickMove[i].setBodyPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutEffectAndQuickMove[i].setBodyPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillsWithoutEffectAndQuickMove[i].setBodyPartAnimationPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyPartAnimationOffset_0023 = new short[len];
			for(int j = 0 ; j < bodyPartAnimationOffset_0023.length ; j++){
				bodyPartAnimationOffset_0023[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutEffectAndQuickMove[i].setBodyPartAnimationOffset(bodyPartAnimationOffset_0023);
			skillsWithoutEffectAndQuickMove[i].setBodyPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutEffectAndQuickMove[i].setBodyPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] footParticle_0024 = new String[len];
			for(int j = 0 ; j < footParticle_0024.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				footParticle_0024[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithoutEffectAndQuickMove[i].setFootParticle(footParticle_0024);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footParticleOffset_0025 = new short[len];
			for(int j = 0 ; j < footParticleOffset_0025.length ; j++){
				footParticleOffset_0025[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutEffectAndQuickMove[i].setFootParticleOffset(footParticleOffset_0025);
			skillsWithoutEffectAndQuickMove[i].setFootParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutEffectAndQuickMove[i].setFootParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutEffectAndQuickMove[i].setFootPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutEffectAndQuickMove[i].setFootPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footPartAnimationOffset_0026 = new short[len];
			for(int j = 0 ; j < footPartAnimationOffset_0026.length ; j++){
				footPartAnimationOffset_0026[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutEffectAndQuickMove[i].setFootPartAnimationOffset(footPartAnimationOffset_0026);
			skillsWithoutEffectAndQuickMove[i].setFootPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutEffectAndQuickMove[i].setFootPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutEffectAndQuickMove[i].setAngle((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] targetFootParticle_0027 = new String[len];
			for(int j = 0 ; j < targetFootParticle_0027.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				targetFootParticle_0027[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithoutEffectAndQuickMove[i].setTargetFootParticle(targetFootParticle_0027);
			skillsWithoutEffectAndQuickMove[i].setGuajiFlag(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutEffectAndQuickMove[i].setBuffName(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillsWithoutEffectAndQuickMove[i].setSkillPlayType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		skillsWithoutTraceAndWithRange = new SkillWithoutTraceAndWithRange[len];
		for(int i = 0 ; i < skillsWithoutTraceAndWithRange.length ; i++){
			skillsWithoutTraceAndWithRange[i] = new SkillWithoutTraceAndWithRange();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithRange[i].setStyle2(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] effectiveTimes_0028 = new int[len];
			for(int j = 0 ; j < effectiveTimes_0028.length ; j++){
				effectiveTimes_0028[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			skillsWithoutTraceAndWithRange[i].setEffectiveTimes(effectiveTimes_0028);
			skillsWithoutTraceAndWithRange[i].setEnableWeaponType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithoutTraceAndWithRange[i].setWeaponTypeLimit((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithoutTraceAndWithRange[i].setFollowByCommonAttack((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithoutTraceAndWithRange[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutTraceAndWithRange[i].setDuration1((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutTraceAndWithRange[i].setDuration2((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutTraceAndWithRange[i].setDuration3((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithRange[i].setStyle1(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillsWithoutTraceAndWithRange[i].setIgnoreStun(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			skillsWithoutTraceAndWithRange[i].setRangeType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithoutTraceAndWithRange[i].setRangeWidth((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutTraceAndWithRange[i].setRangeHeight((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] maxAttackNums_0029 = new short[len];
			for(int j = 0 ; j < maxAttackNums_0029.length ; j++){
				maxAttackNums_0029[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutTraceAndWithRange[i].setMaxAttackNums(maxAttackNums_0029);
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithRange[i].setEffectType(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithRange[i].setAvataRace(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithRange[i].setAvataSex(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillsWithoutTraceAndWithRange[i].setEffectLastTime((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutTraceAndWithRange[i].setEffectExplosionLastTime((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] mp_0030 = new short[len];
			for(int j = 0 ; j < mp_0030.length ; j++){
				mp_0030[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutTraceAndWithRange[i].setMp(mp_0030);
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithRange[i].setFlySound(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithRange[i].setExplodeSound(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] flyParticle_0031 = new String[len];
			for(int j = 0 ; j < flyParticle_0031.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				flyParticle_0031[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithoutTraceAndWithRange[i].setFlyParticle(flyParticle_0031);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] explodeParticle_0032 = new String[len];
			for(int j = 0 ; j < explodeParticle_0032.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				explodeParticle_0032[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithoutTraceAndWithRange[i].setExplodeParticle(explodeParticle_0032);
			skillsWithoutTraceAndWithRange[i].setExplodePercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] bodyParticle_0033 = new String[len];
			for(int j = 0 ; j < bodyParticle_0033.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				bodyParticle_0033[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithoutTraceAndWithRange[i].setBodyParticle(bodyParticle_0033);
			skillsWithoutTraceAndWithRange[i].setBodyPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyParticleOffset_0034 = new short[len];
			for(int j = 0 ; j < bodyParticleOffset_0034.length ; j++){
				bodyParticleOffset_0034[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutTraceAndWithRange[i].setBodyParticleOffset(bodyParticleOffset_0034);
			skillsWithoutTraceAndWithRange[i].setBodyParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutTraceAndWithRange[i].setBodyParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithRange[i].setBodyPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithRange[i].setBodyPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillsWithoutTraceAndWithRange[i].setBodyPartAnimationPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyPartAnimationOffset_0035 = new short[len];
			for(int j = 0 ; j < bodyPartAnimationOffset_0035.length ; j++){
				bodyPartAnimationOffset_0035[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutTraceAndWithRange[i].setBodyPartAnimationOffset(bodyPartAnimationOffset_0035);
			skillsWithoutTraceAndWithRange[i].setBodyPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutTraceAndWithRange[i].setBodyPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] footParticle_0036 = new String[len];
			for(int j = 0 ; j < footParticle_0036.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				footParticle_0036[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithoutTraceAndWithRange[i].setFootParticle(footParticle_0036);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footParticleOffset_0037 = new short[len];
			for(int j = 0 ; j < footParticleOffset_0037.length ; j++){
				footParticleOffset_0037[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutTraceAndWithRange[i].setFootParticleOffset(footParticleOffset_0037);
			skillsWithoutTraceAndWithRange[i].setFootParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutTraceAndWithRange[i].setFootParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithRange[i].setFootPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithRange[i].setFootPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footPartAnimationOffset_0038 = new short[len];
			for(int j = 0 ; j < footPartAnimationOffset_0038.length ; j++){
				footPartAnimationOffset_0038[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutTraceAndWithRange[i].setFootPartAnimationOffset(footPartAnimationOffset_0038);
			skillsWithoutTraceAndWithRange[i].setFootPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutTraceAndWithRange[i].setFootPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutTraceAndWithRange[i].setAngle((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] targetFootParticle_0039 = new String[len];
			for(int j = 0 ; j < targetFootParticle_0039.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				targetFootParticle_0039[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithoutTraceAndWithRange[i].setTargetFootParticle(targetFootParticle_0039);
			skillsWithoutTraceAndWithRange[i].setGuajiFlag(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithRange[i].setBuffName(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		skillsWithoutTraceAndWithTargetOrPosition = new SkillWithoutTraceAndWithTargetOrPosition[len];
		for(int i = 0 ; i < skillsWithoutTraceAndWithTargetOrPosition.length ; i++){
			skillsWithoutTraceAndWithTargetOrPosition[i] = new SkillWithoutTraceAndWithTargetOrPosition();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithTargetOrPosition[i].setStyle2(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] effectiveTimes_0040 = new int[len];
			for(int j = 0 ; j < effectiveTimes_0040.length ; j++){
				effectiveTimes_0040[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			skillsWithoutTraceAndWithTargetOrPosition[i].setEffectiveTimes(effectiveTimes_0040);
			skillsWithoutTraceAndWithTargetOrPosition[i].setEnableWeaponType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithoutTraceAndWithTargetOrPosition[i].setWeaponTypeLimit((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithoutTraceAndWithTargetOrPosition[i].setFollowByCommonAttack((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithoutTraceAndWithTargetOrPosition[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutTraceAndWithTargetOrPosition[i].setDuration1((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutTraceAndWithTargetOrPosition[i].setDuration2((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutTraceAndWithTargetOrPosition[i].setDuration3((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithTargetOrPosition[i].setStyle1(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillsWithoutTraceAndWithTargetOrPosition[i].setIgnoreStun(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithTargetOrPosition[i].setEffectType(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithTargetOrPosition[i].setAvataRace(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithTargetOrPosition[i].setAvataSex(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillsWithoutTraceAndWithTargetOrPosition[i].setEffectLastTime((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithoutTraceAndWithTargetOrPosition[i].setEffectExplosionLastTime((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] mp_0041 = new short[len];
			for(int j = 0 ; j < mp_0041.length ; j++){
				mp_0041[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutTraceAndWithTargetOrPosition[i].setMp(mp_0041);
			skillsWithoutTraceAndWithTargetOrPosition[i].setRange((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithTargetOrPosition[i].setFlySound(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithTargetOrPosition[i].setExplodeSound(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] flyParticle_0042 = new String[len];
			for(int j = 0 ; j < flyParticle_0042.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				flyParticle_0042[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithoutTraceAndWithTargetOrPosition[i].setFlyParticle(flyParticle_0042);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] explodeParticle_0043 = new String[len];
			for(int j = 0 ; j < explodeParticle_0043.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				explodeParticle_0043[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithoutTraceAndWithTargetOrPosition[i].setExplodeParticle(explodeParticle_0043);
			skillsWithoutTraceAndWithTargetOrPosition[i].setExplodePercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] bodyParticle_0044 = new String[len];
			for(int j = 0 ; j < bodyParticle_0044.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				bodyParticle_0044[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithoutTraceAndWithTargetOrPosition[i].setBodyParticle(bodyParticle_0044);
			skillsWithoutTraceAndWithTargetOrPosition[i].setBodyPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyParticleOffset_0045 = new short[len];
			for(int j = 0 ; j < bodyParticleOffset_0045.length ; j++){
				bodyParticleOffset_0045[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutTraceAndWithTargetOrPosition[i].setBodyParticleOffset(bodyParticleOffset_0045);
			skillsWithoutTraceAndWithTargetOrPosition[i].setBodyParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutTraceAndWithTargetOrPosition[i].setBodyParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithTargetOrPosition[i].setBodyPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithTargetOrPosition[i].setBodyPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillsWithoutTraceAndWithTargetOrPosition[i].setBodyPartAnimationPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyPartAnimationOffset_0046 = new short[len];
			for(int j = 0 ; j < bodyPartAnimationOffset_0046.length ; j++){
				bodyPartAnimationOffset_0046[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutTraceAndWithTargetOrPosition[i].setBodyPartAnimationOffset(bodyPartAnimationOffset_0046);
			skillsWithoutTraceAndWithTargetOrPosition[i].setBodyPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutTraceAndWithTargetOrPosition[i].setBodyPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] footParticle_0047 = new String[len];
			for(int j = 0 ; j < footParticle_0047.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				footParticle_0047[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithoutTraceAndWithTargetOrPosition[i].setFootParticle(footParticle_0047);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footParticleOffset_0048 = new short[len];
			for(int j = 0 ; j < footParticleOffset_0048.length ; j++){
				footParticleOffset_0048[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutTraceAndWithTargetOrPosition[i].setFootParticleOffset(footParticleOffset_0048);
			skillsWithoutTraceAndWithTargetOrPosition[i].setFootParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutTraceAndWithTargetOrPosition[i].setFootParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithTargetOrPosition[i].setFootPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithTargetOrPosition[i].setFootPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footPartAnimationOffset_0049 = new short[len];
			for(int j = 0 ; j < footPartAnimationOffset_0049.length ; j++){
				footPartAnimationOffset_0049[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithoutTraceAndWithTargetOrPosition[i].setFootPartAnimationOffset(footPartAnimationOffset_0049);
			skillsWithoutTraceAndWithTargetOrPosition[i].setFootPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutTraceAndWithTargetOrPosition[i].setFootPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithoutTraceAndWithTargetOrPosition[i].setAngle((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] targetFootParticle_0050 = new String[len];
			for(int j = 0 ; j < targetFootParticle_0050.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				targetFootParticle_0050[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithoutTraceAndWithTargetOrPosition[i].setTargetFootParticle(targetFootParticle_0050);
			skillsWithoutTraceAndWithTargetOrPosition[i].setGuajiFlag(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithoutTraceAndWithTargetOrPosition[i].setBuffName(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		skillWithoutTraceAndWithSummonNPC = new SkillWithoutTraceAndWithSummonNPC[len];
		for(int i = 0 ; i < skillWithoutTraceAndWithSummonNPC.length ; i++){
			skillWithoutTraceAndWithSummonNPC[i] = new SkillWithoutTraceAndWithSummonNPC();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithSummonNPC[i].setStyle2(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] effectiveTimes_0051 = new int[len];
			for(int j = 0 ; j < effectiveTimes_0051.length ; j++){
				effectiveTimes_0051[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			skillWithoutTraceAndWithSummonNPC[i].setEffectiveTimes(effectiveTimes_0051);
			skillWithoutTraceAndWithSummonNPC[i].setEnableWeaponType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillWithoutTraceAndWithSummonNPC[i].setWeaponTypeLimit((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillWithoutTraceAndWithSummonNPC[i].setFollowByCommonAttack((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillWithoutTraceAndWithSummonNPC[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndWithSummonNPC[i].setDuration1((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndWithSummonNPC[i].setDuration2((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndWithSummonNPC[i].setDuration3((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithSummonNPC[i].setStyle1(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillWithoutTraceAndWithSummonNPC[i].setIgnoreStun(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			skillWithoutTraceAndWithSummonNPC[i].setMatrixType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] attackNum_0052 = new short[len];
			for(int j = 0 ; j < attackNum_0052.length ; j++){
				attackNum_0052[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndWithSummonNPC[i].setAttackNum(attackNum_0052);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] mp_0053 = new short[len];
			for(int j = 0 ; j < mp_0053.length ; j++){
				mp_0053[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndWithSummonNPC[i].setMp(mp_0053);
			skillWithoutTraceAndWithSummonNPC[i].setRange((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndWithSummonNPC[i].setGapWidth((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndWithSummonNPC[i].setGapHeight((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndWithSummonNPC[i].setMaxTimeLength((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillWithoutTraceAndWithSummonNPC[i].setSpeed((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			skillWithoutTraceAndWithSummonNPC[i].setAngle((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			skillWithoutTraceAndWithSummonNPC[i].setHeigth((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			skillWithoutTraceAndWithSummonNPC[i].setMaxParticleEachTime((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithSummonNPC[i].setFlySound(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithSummonNPC[i].setExplodeSound(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] flyParticle_0054 = new String[len];
			for(int j = 0 ; j < flyParticle_0054.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				flyParticle_0054[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillWithoutTraceAndWithSummonNPC[i].setFlyParticle(flyParticle_0054);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] explodeParticle_0055 = new String[len];
			for(int j = 0 ; j < explodeParticle_0055.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				explodeParticle_0055[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillWithoutTraceAndWithSummonNPC[i].setExplodeParticle(explodeParticle_0055);
			skillWithoutTraceAndWithSummonNPC[i].setExplodePercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] bodyParticle_0056 = new String[len];
			for(int j = 0 ; j < bodyParticle_0056.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				bodyParticle_0056[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillWithoutTraceAndWithSummonNPC[i].setBodyParticle(bodyParticle_0056);
			skillWithoutTraceAndWithSummonNPC[i].setBodyPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyParticleOffset_0057 = new short[len];
			for(int j = 0 ; j < bodyParticleOffset_0057.length ; j++){
				bodyParticleOffset_0057[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndWithSummonNPC[i].setBodyParticleOffset(bodyParticleOffset_0057);
			skillWithoutTraceAndWithSummonNPC[i].setBodyParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillWithoutTraceAndWithSummonNPC[i].setBodyParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithSummonNPC[i].setBodyPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithSummonNPC[i].setBodyPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillWithoutTraceAndWithSummonNPC[i].setBodyPartAnimationPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyPartAnimationOffset_0058 = new short[len];
			for(int j = 0 ; j < bodyPartAnimationOffset_0058.length ; j++){
				bodyPartAnimationOffset_0058[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndWithSummonNPC[i].setBodyPartAnimationOffset(bodyPartAnimationOffset_0058);
			skillWithoutTraceAndWithSummonNPC[i].setBodyPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillWithoutTraceAndWithSummonNPC[i].setBodyPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] footParticle_0059 = new String[len];
			for(int j = 0 ; j < footParticle_0059.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				footParticle_0059[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillWithoutTraceAndWithSummonNPC[i].setFootParticle(footParticle_0059);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footParticleOffset_0060 = new short[len];
			for(int j = 0 ; j < footParticleOffset_0060.length ; j++){
				footParticleOffset_0060[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndWithSummonNPC[i].setFootParticleOffset(footParticleOffset_0060);
			skillWithoutTraceAndWithSummonNPC[i].setFootParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillWithoutTraceAndWithSummonNPC[i].setFootParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithSummonNPC[i].setFootPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithSummonNPC[i].setFootPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footPartAnimationOffset_0061 = new short[len];
			for(int j = 0 ; j < footPartAnimationOffset_0061.length ; j++){
				footPartAnimationOffset_0061[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndWithSummonNPC[i].setFootPartAnimationOffset(footPartAnimationOffset_0061);
			skillWithoutTraceAndWithSummonNPC[i].setFootPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillWithoutTraceAndWithSummonNPC[i].setFootPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillWithoutTraceAndWithSummonNPC[i].setAngle((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] targetFootParticle_0062 = new String[len];
			for(int j = 0 ; j < targetFootParticle_0062.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				targetFootParticle_0062[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillWithoutTraceAndWithSummonNPC[i].setTargetFootParticle(targetFootParticle_0062);
			skillWithoutTraceAndWithSummonNPC[i].setGuajiFlag(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithSummonNPC[i].setBuffName(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		skillsWithTraceAndDirectionOrTarget = new SkillWithTraceAndDirectionOrTarget[len];
		for(int i = 0 ; i < skillsWithTraceAndDirectionOrTarget.length ; i++){
			skillsWithTraceAndDirectionOrTarget[i] = new SkillWithTraceAndDirectionOrTarget();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithTraceAndDirectionOrTarget[i].setStyle2(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] effectiveTimes_0063 = new int[len];
			for(int j = 0 ; j < effectiveTimes_0063.length ; j++){
				effectiveTimes_0063[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			skillsWithTraceAndDirectionOrTarget[i].setEffectiveTimes(effectiveTimes_0063);
			skillsWithTraceAndDirectionOrTarget[i].setEnableWeaponType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithTraceAndDirectionOrTarget[i].setWeaponTypeLimit((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithTraceAndDirectionOrTarget[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithTraceAndDirectionOrTarget[i].setDuration1((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithTraceAndDirectionOrTarget[i].setDuration2((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithTraceAndDirectionOrTarget[i].setDuration3((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithTraceAndDirectionOrTarget[i].setStyle1(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillsWithTraceAndDirectionOrTarget[i].setFollowByCommonAttack((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithTraceAndDirectionOrTarget[i].setIgnoreStun(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			skillsWithTraceAndDirectionOrTarget[i].setTargetType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithTraceAndDirectionOrTarget[i].setEffectType(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithTraceAndDirectionOrTarget[i].setAvataRace(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithTraceAndDirectionOrTarget[i].setAvataSex(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillsWithTraceAndDirectionOrTarget[i].setTrackType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillsWithTraceAndDirectionOrTarget[i].setSpeed((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithTraceAndDirectionOrTarget[i].setRange((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithTraceAndDirectionOrTarget[i].setAttackWidth((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithTraceAndDirectionOrTarget[i].setExplosionLastingTime((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillsWithTraceAndDirectionOrTarget[i].setEffectNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] effectInitPositionX_0064 = new int[len];
			for(int j = 0 ; j < effectInitPositionX_0064.length ; j++){
				effectInitPositionX_0064[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			skillsWithTraceAndDirectionOrTarget[i].setEffectInitPositionX(effectInitPositionX_0064);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] effectInitPositionY_0065 = new int[len];
			for(int j = 0 ; j < effectInitPositionY_0065.length ; j++){
				effectInitPositionY_0065[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			skillsWithTraceAndDirectionOrTarget[i].setEffectInitPositionY(effectInitPositionY_0065);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] effectInitDirection_0066 = new byte[len];
			System.arraycopy(content,offset,effectInitDirection_0066,0,len);
			offset += len;
			skillsWithTraceAndDirectionOrTarget[i].setEffectInitDirection(effectInitDirection_0066);
			skillsWithTraceAndDirectionOrTarget[i].setPenetrateNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] mp_0067 = new short[len];
			for(int j = 0 ; j < mp_0067.length ; j++){
				mp_0067[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithTraceAndDirectionOrTarget[i].setMp(mp_0067);
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithTraceAndDirectionOrTarget[i].setFlySound(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithTraceAndDirectionOrTarget[i].setExplodeSound(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] flyParticle_0068 = new String[len];
			for(int j = 0 ; j < flyParticle_0068.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				flyParticle_0068[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithTraceAndDirectionOrTarget[i].setFlyParticle(flyParticle_0068);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] explodeParticle_0069 = new String[len];
			for(int j = 0 ; j < explodeParticle_0069.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				explodeParticle_0069[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithTraceAndDirectionOrTarget[i].setExplodeParticle(explodeParticle_0069);
			skillsWithTraceAndDirectionOrTarget[i].setExplodePercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] bodyParticle_0070 = new String[len];
			for(int j = 0 ; j < bodyParticle_0070.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				bodyParticle_0070[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithTraceAndDirectionOrTarget[i].setBodyParticle(bodyParticle_0070);
			skillsWithTraceAndDirectionOrTarget[i].setBodyPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyParticleOffset_0071 = new short[len];
			for(int j = 0 ; j < bodyParticleOffset_0071.length ; j++){
				bodyParticleOffset_0071[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithTraceAndDirectionOrTarget[i].setBodyParticleOffset(bodyParticleOffset_0071);
			skillsWithTraceAndDirectionOrTarget[i].setBodyParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithTraceAndDirectionOrTarget[i].setBodyParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithTraceAndDirectionOrTarget[i].setBodyPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithTraceAndDirectionOrTarget[i].setBodyPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillsWithTraceAndDirectionOrTarget[i].setBodyPartAnimationPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyPartAnimationOffset_0072 = new short[len];
			for(int j = 0 ; j < bodyPartAnimationOffset_0072.length ; j++){
				bodyPartAnimationOffset_0072[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithTraceAndDirectionOrTarget[i].setBodyPartAnimationOffset(bodyPartAnimationOffset_0072);
			skillsWithTraceAndDirectionOrTarget[i].setBodyPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithTraceAndDirectionOrTarget[i].setBodyPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] footParticle_0073 = new String[len];
			for(int j = 0 ; j < footParticle_0073.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				footParticle_0073[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithTraceAndDirectionOrTarget[i].setFootParticle(footParticle_0073);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footParticleOffset_0074 = new short[len];
			for(int j = 0 ; j < footParticleOffset_0074.length ; j++){
				footParticleOffset_0074[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithTraceAndDirectionOrTarget[i].setFootParticleOffset(footParticleOffset_0074);
			skillsWithTraceAndDirectionOrTarget[i].setFootParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithTraceAndDirectionOrTarget[i].setFootParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithTraceAndDirectionOrTarget[i].setFootPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithTraceAndDirectionOrTarget[i].setFootPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footPartAnimationOffset_0075 = new short[len];
			for(int j = 0 ; j < footPartAnimationOffset_0075.length ; j++){
				footPartAnimationOffset_0075[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillsWithTraceAndDirectionOrTarget[i].setFootPartAnimationOffset(footPartAnimationOffset_0075);
			skillsWithTraceAndDirectionOrTarget[i].setFootPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithTraceAndDirectionOrTarget[i].setFootPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithTraceAndDirectionOrTarget[i].setAngle((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] targetFootParticle_0076 = new String[len];
			for(int j = 0 ; j < targetFootParticle_0076.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				targetFootParticle_0076[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillsWithTraceAndDirectionOrTarget[i].setTargetFootParticle(targetFootParticle_0076);
			skillsWithTraceAndDirectionOrTarget[i].setSendEffectTime((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillsWithTraceAndDirectionOrTarget[i].setGuajiFlag(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillsWithTraceAndDirectionOrTarget[i].setBuffName(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		skillWithoutTraceAndWithMatrix = new SkillWithoutTraceAndWithMatrix[len];
		for(int i = 0 ; i < skillWithoutTraceAndWithMatrix.length ; i++){
			skillWithoutTraceAndWithMatrix[i] = new SkillWithoutTraceAndWithMatrix();
			skillWithoutTraceAndWithMatrix[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndWithMatrix[i].setDuration1((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndWithMatrix[i].setDuration2((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndWithMatrix[i].setDuration3((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithMatrix[i].setStyle1(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithMatrix[i].setStyle2(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] effectiveTimes_0077 = new int[len];
			for(int j = 0 ; j < effectiveTimes_0077.length ; j++){
				effectiveTimes_0077[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			skillWithoutTraceAndWithMatrix[i].setEffectiveTimes(effectiveTimes_0077);
			skillWithoutTraceAndWithMatrix[i].setEnableWeaponType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillWithoutTraceAndWithMatrix[i].setWeaponTypeLimit((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillWithoutTraceAndWithMatrix[i].setFollowByCommonAttack((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillWithoutTraceAndWithMatrix[i].setIgnoreStun(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			skillWithoutTraceAndWithMatrix[i].setMatrixType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillWithoutTraceAndWithMatrix[i].setGapWidth((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndWithMatrix[i].setGapHeight((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndWithMatrix[i].setEffectNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithMatrix[i].setEffectType(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithMatrix[i].setAvataRace(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithMatrix[i].setAvataSex(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillWithoutTraceAndWithMatrix[i].setEffectLastTime((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndWithMatrix[i].setEffectExplosionLastTime((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] mp_0078 = new short[len];
			for(int j = 0 ; j < mp_0078.length ; j++){
				mp_0078[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndWithMatrix[i].setMp(mp_0078);
			skillWithoutTraceAndWithMatrix[i].setRange((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithMatrix[i].setFlySound(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithMatrix[i].setExplodeSound(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] flyParticle_0079 = new String[len];
			for(int j = 0 ; j < flyParticle_0079.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				flyParticle_0079[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillWithoutTraceAndWithMatrix[i].setFlyParticle(flyParticle_0079);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] explodeParticle_0080 = new String[len];
			for(int j = 0 ; j < explodeParticle_0080.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				explodeParticle_0080[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillWithoutTraceAndWithMatrix[i].setExplodeParticle(explodeParticle_0080);
			skillWithoutTraceAndWithMatrix[i].setExplodePercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] bodyParticle_0081 = new String[len];
			for(int j = 0 ; j < bodyParticle_0081.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				bodyParticle_0081[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillWithoutTraceAndWithMatrix[i].setBodyParticle(bodyParticle_0081);
			skillWithoutTraceAndWithMatrix[i].setBodyPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyParticleOffset_0082 = new short[len];
			for(int j = 0 ; j < bodyParticleOffset_0082.length ; j++){
				bodyParticleOffset_0082[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndWithMatrix[i].setBodyParticleOffset(bodyParticleOffset_0082);
			skillWithoutTraceAndWithMatrix[i].setBodyParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillWithoutTraceAndWithMatrix[i].setBodyParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithMatrix[i].setBodyPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithMatrix[i].setBodyPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillWithoutTraceAndWithMatrix[i].setBodyPartAnimationPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyPartAnimationOffset_0083 = new short[len];
			for(int j = 0 ; j < bodyPartAnimationOffset_0083.length ; j++){
				bodyPartAnimationOffset_0083[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndWithMatrix[i].setBodyPartAnimationOffset(bodyPartAnimationOffset_0083);
			skillWithoutTraceAndWithMatrix[i].setBodyPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillWithoutTraceAndWithMatrix[i].setBodyPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] footParticle_0084 = new String[len];
			for(int j = 0 ; j < footParticle_0084.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				footParticle_0084[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillWithoutTraceAndWithMatrix[i].setFootParticle(footParticle_0084);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footParticleOffset_0085 = new short[len];
			for(int j = 0 ; j < footParticleOffset_0085.length ; j++){
				footParticleOffset_0085[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndWithMatrix[i].setFootParticleOffset(footParticleOffset_0085);
			skillWithoutTraceAndWithMatrix[i].setFootParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillWithoutTraceAndWithMatrix[i].setFootParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithMatrix[i].setFootPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithMatrix[i].setFootPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footPartAnimationOffset_0086 = new short[len];
			for(int j = 0 ; j < footPartAnimationOffset_0086.length ; j++){
				footPartAnimationOffset_0086[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndWithMatrix[i].setFootPartAnimationOffset(footPartAnimationOffset_0086);
			skillWithoutTraceAndWithMatrix[i].setFootPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillWithoutTraceAndWithMatrix[i].setFootPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillWithoutTraceAndWithMatrix[i].setAngle((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] targetFootParticle_0087 = new String[len];
			for(int j = 0 ; j < targetFootParticle_0087.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				targetFootParticle_0087[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillWithoutTraceAndWithMatrix[i].setTargetFootParticle(targetFootParticle_0087);
			skillWithoutTraceAndWithMatrix[i].setGuajiFlag(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndWithMatrix[i].setBuffName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] maxAttackNums_0088 = new short[len];
			for(int j = 0 ; j < maxAttackNums_0088.length ; j++){
				maxAttackNums_0088[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndWithMatrix[i].setMaxAttackNums(maxAttackNums_0088);
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		skillWithoutTraceAndOnTeamMember = new SkillWithoutTraceAndOnTeamMember[len];
		for(int i = 0 ; i < skillWithoutTraceAndOnTeamMember.length ; i++){
			skillWithoutTraceAndOnTeamMember[i] = new SkillWithoutTraceAndOnTeamMember();
			skillWithoutTraceAndOnTeamMember[i].setEnableWeaponType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillWithoutTraceAndOnTeamMember[i].setWeaponTypeLimit((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillWithoutTraceAndOnTeamMember[i].setFollowByCommonAttack((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillWithoutTraceAndOnTeamMember[i].setIgnoreStun(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			skillWithoutTraceAndOnTeamMember[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndOnTeamMember[i].setDuration1((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndOnTeamMember[i].setDuration2((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndOnTeamMember[i].setDuration3((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndOnTeamMember[i].setStyle1(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndOnTeamMember[i].setStyle2(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] effectiveTimes_0089 = new int[len];
			for(int j = 0 ; j < effectiveTimes_0089.length ; j++){
				effectiveTimes_0089[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			skillWithoutTraceAndOnTeamMember[i].setEffectiveTimes(effectiveTimes_0089);
			skillWithoutTraceAndOnTeamMember[i].setRangeType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			skillWithoutTraceAndOnTeamMember[i].setRangeWidth((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndOnTeamMember[i].setRangeHeight((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndOnTeamMember[i].setEffectType(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndOnTeamMember[i].setAvataRace(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndOnTeamMember[i].setAvataSex(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillWithoutTraceAndOnTeamMember[i].setEffectLastTime((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillWithoutTraceAndOnTeamMember[i].setEffectExplosionLastTime((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] mp_0090 = new short[len];
			for(int j = 0 ; j < mp_0090.length ; j++){
				mp_0090[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndOnTeamMember[i].setMp(mp_0090);
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndOnTeamMember[i].setFlySound(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndOnTeamMember[i].setExplodeSound(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] flyParticle_0091 = new String[len];
			for(int j = 0 ; j < flyParticle_0091.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				flyParticle_0091[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillWithoutTraceAndOnTeamMember[i].setFlyParticle(flyParticle_0091);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] explodeParticle_0092 = new String[len];
			for(int j = 0 ; j < explodeParticle_0092.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				explodeParticle_0092[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillWithoutTraceAndOnTeamMember[i].setExplodeParticle(explodeParticle_0092);
			skillWithoutTraceAndOnTeamMember[i].setExplodePercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] bodyParticle_0093 = new String[len];
			for(int j = 0 ; j < bodyParticle_0093.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				bodyParticle_0093[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillWithoutTraceAndOnTeamMember[i].setBodyParticle(bodyParticle_0093);
			skillWithoutTraceAndOnTeamMember[i].setBodyPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyParticleOffset_0094 = new short[len];
			for(int j = 0 ; j < bodyParticleOffset_0094.length ; j++){
				bodyParticleOffset_0094[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndOnTeamMember[i].setBodyParticleOffset(bodyParticleOffset_0094);
			skillWithoutTraceAndOnTeamMember[i].setBodyParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillWithoutTraceAndOnTeamMember[i].setBodyParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndOnTeamMember[i].setBodyPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndOnTeamMember[i].setBodyPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillWithoutTraceAndOnTeamMember[i].setBodyPartAnimationPercent((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bodyPartAnimationOffset_0095 = new short[len];
			for(int j = 0 ; j < bodyPartAnimationOffset_0095.length ; j++){
				bodyPartAnimationOffset_0095[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndOnTeamMember[i].setBodyPartAnimationOffset(bodyPartAnimationOffset_0095);
			skillWithoutTraceAndOnTeamMember[i].setBodyPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillWithoutTraceAndOnTeamMember[i].setBodyPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] footParticle_0096 = new String[len];
			for(int j = 0 ; j < footParticle_0096.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				footParticle_0096[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillWithoutTraceAndOnTeamMember[i].setFootParticle(footParticle_0096);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footParticleOffset_0097 = new short[len];
			for(int j = 0 ; j < footParticleOffset_0097.length ; j++){
				footParticleOffset_0097[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndOnTeamMember[i].setFootParticleOffset(footParticleOffset_0097);
			skillWithoutTraceAndOnTeamMember[i].setFootParticlePlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillWithoutTraceAndOnTeamMember[i].setFootParticlePlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndOnTeamMember[i].setFootPartPath(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndOnTeamMember[i].setFootPartAnimation(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] footPartAnimationOffset_0098 = new short[len];
			for(int j = 0 ; j < footPartAnimationOffset_0098.length ; j++){
				footPartAnimationOffset_0098[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			skillWithoutTraceAndOnTeamMember[i].setFootPartAnimationOffset(footPartAnimationOffset_0098);
			skillWithoutTraceAndOnTeamMember[i].setFootPartAnimationPlayStart((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillWithoutTraceAndOnTeamMember[i].setFootPartAnimationPlayEnd((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			skillWithoutTraceAndOnTeamMember[i].setAngle((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] targetFootParticle_0099 = new String[len];
			for(int j = 0 ; j < targetFootParticle_0099.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				targetFootParticle_0099[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			skillWithoutTraceAndOnTeamMember[i].setTargetFootParticle(targetFootParticle_0099);
			skillWithoutTraceAndOnTeamMember[i].setGuajiFlag(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillWithoutTraceAndOnTeamMember[i].setBuffName(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
	}

	public int getType() {
		return 0x000000E3;
	}

	public String getTypeDescription() {
		return "SEND_ACTIVESKILL_REQ";
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
		len += commonAttackSkillIdsForWeapon.length * 4;
		len += 4;
		for(int i = 0 ; i < commonAttackSkills.length ; i++){
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += commonAttackSkills[i].getEffectiveTimes().length * 4;
			len += 1;
			len += 1;
			len += 1;
			len += 1;
			len += 4;
			len += 2;
			if(commonAttackSkills[i].getStyle1() != null){
				try{
				len += commonAttackSkills[i].getStyle1().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(commonAttackSkills[i].getStyle2() != null){
				try{
				len += commonAttackSkills[i].getStyle2().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 2;
			if(commonAttackSkills[i].getEffectType() != null){
				try{
				len += commonAttackSkills[i].getEffectType().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(commonAttackSkills[i].getAvataRace() != null){
				try{
				len += commonAttackSkills[i].getAvataRace().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(commonAttackSkills[i].getAvataSex() != null){
				try{
				len += commonAttackSkills[i].getAvataSex().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 2;
			if(commonAttackSkills[i].getFlySound() != null){
				try{
				len += commonAttackSkills[i].getFlySound().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(commonAttackSkills[i].getExplodeSound() != null){
				try{
				len += commonAttackSkills[i].getExplodeSound().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] flyParticle = commonAttackSkills[i].getFlyParticle();
			for(int j = 0 ; j < flyParticle.length; j++){
				len += 2;
				try{
					len += flyParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] explodeParticle = commonAttackSkills[i].getExplodeParticle();
			for(int j = 0 ; j < explodeParticle.length; j++){
				len += 2;
				try{
					len += explodeParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			String[] bodyParticle = commonAttackSkills[i].getBodyParticle();
			for(int j = 0 ; j < bodyParticle.length; j++){
				len += 2;
				try{
					len += bodyParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += commonAttackSkills[i].getBodyParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(commonAttackSkills[i].getBodyPartPath() != null){
				try{
				len += commonAttackSkills[i].getBodyPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(commonAttackSkills[i].getBodyPartAnimation() != null){
				try{
				len += commonAttackSkills[i].getBodyPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += commonAttackSkills[i].getBodyPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 4;
			String[] footParticle = commonAttackSkills[i].getFootParticle();
			for(int j = 0 ; j < footParticle.length; j++){
				len += 2;
				try{
					len += footParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += commonAttackSkills[i].getFootParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(commonAttackSkills[i].getFootPartPath() != null){
				try{
				len += commonAttackSkills[i].getFootPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(commonAttackSkills[i].getFootPartAnimation() != null){
				try{
				len += commonAttackSkills[i].getFootPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += commonAttackSkills[i].getFootPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			len += 4;
			String[] targetFootParticle = commonAttackSkills[i].getTargetFootParticle();
			for(int j = 0 ; j < targetFootParticle.length; j++){
				len += 2;
				try{
					len += targetFootParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 2;
			if(commonAttackSkills[i].getBuffName() != null){
				try{
				len += commonAttackSkills[i].getBuffName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 4;
		for(int i = 0 ; i < skillsWithoutEffect.length ; i++){
			len += 2;
			if(skillsWithoutEffect[i].getStyle1() != null){
				try{
				len += skillsWithoutEffect[i].getStyle1().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithoutEffect[i].getStyle2() != null){
				try{
				len += skillsWithoutEffect[i].getStyle2().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillsWithoutEffect[i].getEffectiveTimes().length * 4;
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
			String[] bodyParticle = skillsWithoutEffect[i].getBodyParticle();
			for(int j = 0 ; j < bodyParticle.length; j++){
				len += 2;
				try{
					len += bodyParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += skillsWithoutEffect[i].getBodyParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(skillsWithoutEffect[i].getBodyPartPath() != null){
				try{
				len += skillsWithoutEffect[i].getBodyPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithoutEffect[i].getBodyPartAnimation() != null){
				try{
				len += skillsWithoutEffect[i].getBodyPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += skillsWithoutEffect[i].getBodyPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 4;
			String[] footParticle = skillsWithoutEffect[i].getFootParticle();
			for(int j = 0 ; j < footParticle.length; j++){
				len += 2;
				try{
					len += footParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillsWithoutEffect[i].getFootParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(skillsWithoutEffect[i].getFootPartPath() != null){
				try{
				len += skillsWithoutEffect[i].getFootPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithoutEffect[i].getFootPartAnimation() != null){
				try{
				len += skillsWithoutEffect[i].getFootPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillsWithoutEffect[i].getFootPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			len += 4;
			String[] targetFootParticle = skillsWithoutEffect[i].getTargetFootParticle();
			for(int j = 0 ; j < targetFootParticle.length; j++){
				len += 2;
				try{
					len += targetFootParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 2;
			if(skillsWithoutEffect[i].getBuffName() != null){
				try{
				len += skillsWithoutEffect[i].getBuffName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 4;
		for(int i = 0 ; i < skillsWithoutEffectAndQuickMove.length ; i++){
			len += 2;
			if(skillsWithoutEffectAndQuickMove[i].getStyle2() != null){
				try{
				len += skillsWithoutEffectAndQuickMove[i].getStyle2().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillsWithoutEffectAndQuickMove[i].getEffectiveTimes().length * 4;
			len += 1;
			len += 1;
			len += 1;
			len += 1;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(skillsWithoutEffectAndQuickMove[i].getStyle1() != null){
				try{
				len += skillsWithoutEffectAndQuickMove[i].getStyle1().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 4;
			len += skillsWithoutEffectAndQuickMove[i].getMp().length * 2;
			len += 4;
			len += 4;
			String[] bodyParticle = skillsWithoutEffectAndQuickMove[i].getBodyParticle();
			for(int j = 0 ; j < bodyParticle.length; j++){
				len += 2;
				try{
					len += bodyParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += skillsWithoutEffectAndQuickMove[i].getBodyParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(skillsWithoutEffectAndQuickMove[i].getBodyPartPath() != null){
				try{
				len += skillsWithoutEffectAndQuickMove[i].getBodyPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithoutEffectAndQuickMove[i].getBodyPartAnimation() != null){
				try{
				len += skillsWithoutEffectAndQuickMove[i].getBodyPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += skillsWithoutEffectAndQuickMove[i].getBodyPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 4;
			String[] footParticle = skillsWithoutEffectAndQuickMove[i].getFootParticle();
			for(int j = 0 ; j < footParticle.length; j++){
				len += 2;
				try{
					len += footParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillsWithoutEffectAndQuickMove[i].getFootParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(skillsWithoutEffectAndQuickMove[i].getFootPartPath() != null){
				try{
				len += skillsWithoutEffectAndQuickMove[i].getFootPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithoutEffectAndQuickMove[i].getFootPartAnimation() != null){
				try{
				len += skillsWithoutEffectAndQuickMove[i].getFootPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillsWithoutEffectAndQuickMove[i].getFootPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			len += 4;
			String[] targetFootParticle = skillsWithoutEffectAndQuickMove[i].getTargetFootParticle();
			for(int j = 0 ; j < targetFootParticle.length; j++){
				len += 2;
				try{
					len += targetFootParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 2;
			if(skillsWithoutEffectAndQuickMove[i].getBuffName() != null){
				try{
				len += skillsWithoutEffectAndQuickMove[i].getBuffName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
		}
		len += 4;
		for(int i = 0 ; i < skillsWithoutTraceAndWithRange.length ; i++){
			len += 2;
			if(skillsWithoutTraceAndWithRange[i].getStyle2() != null){
				try{
				len += skillsWithoutTraceAndWithRange[i].getStyle2().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillsWithoutTraceAndWithRange[i].getEffectiveTimes().length * 4;
			len += 1;
			len += 1;
			len += 1;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(skillsWithoutTraceAndWithRange[i].getStyle1() != null){
				try{
				len += skillsWithoutTraceAndWithRange[i].getStyle1().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 1;
			len += 4;
			len += 4;
			len += 4;
			len += skillsWithoutTraceAndWithRange[i].getMaxAttackNums().length * 2;
			len += 2;
			if(skillsWithoutTraceAndWithRange[i].getEffectType() != null){
				try{
				len += skillsWithoutTraceAndWithRange[i].getEffectType().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithoutTraceAndWithRange[i].getAvataRace() != null){
				try{
				len += skillsWithoutTraceAndWithRange[i].getAvataRace().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithoutTraceAndWithRange[i].getAvataSex() != null){
				try{
				len += skillsWithoutTraceAndWithRange[i].getAvataSex().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 4;
			len += skillsWithoutTraceAndWithRange[i].getMp().length * 2;
			len += 2;
			if(skillsWithoutTraceAndWithRange[i].getFlySound() != null){
				try{
				len += skillsWithoutTraceAndWithRange[i].getFlySound().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithoutTraceAndWithRange[i].getExplodeSound() != null){
				try{
				len += skillsWithoutTraceAndWithRange[i].getExplodeSound().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] flyParticle = skillsWithoutTraceAndWithRange[i].getFlyParticle();
			for(int j = 0 ; j < flyParticle.length; j++){
				len += 2;
				try{
					len += flyParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] explodeParticle = skillsWithoutTraceAndWithRange[i].getExplodeParticle();
			for(int j = 0 ; j < explodeParticle.length; j++){
				len += 2;
				try{
					len += explodeParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			String[] bodyParticle = skillsWithoutTraceAndWithRange[i].getBodyParticle();
			for(int j = 0 ; j < bodyParticle.length; j++){
				len += 2;
				try{
					len += bodyParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += skillsWithoutTraceAndWithRange[i].getBodyParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(skillsWithoutTraceAndWithRange[i].getBodyPartPath() != null){
				try{
				len += skillsWithoutTraceAndWithRange[i].getBodyPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithoutTraceAndWithRange[i].getBodyPartAnimation() != null){
				try{
				len += skillsWithoutTraceAndWithRange[i].getBodyPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += skillsWithoutTraceAndWithRange[i].getBodyPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 4;
			String[] footParticle = skillsWithoutTraceAndWithRange[i].getFootParticle();
			for(int j = 0 ; j < footParticle.length; j++){
				len += 2;
				try{
					len += footParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillsWithoutTraceAndWithRange[i].getFootParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(skillsWithoutTraceAndWithRange[i].getFootPartPath() != null){
				try{
				len += skillsWithoutTraceAndWithRange[i].getFootPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithoutTraceAndWithRange[i].getFootPartAnimation() != null){
				try{
				len += skillsWithoutTraceAndWithRange[i].getFootPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillsWithoutTraceAndWithRange[i].getFootPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			len += 4;
			String[] targetFootParticle = skillsWithoutTraceAndWithRange[i].getTargetFootParticle();
			for(int j = 0 ; j < targetFootParticle.length; j++){
				len += 2;
				try{
					len += targetFootParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 2;
			if(skillsWithoutTraceAndWithRange[i].getBuffName() != null){
				try{
				len += skillsWithoutTraceAndWithRange[i].getBuffName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 4;
		for(int i = 0 ; i < skillsWithoutTraceAndWithTargetOrPosition.length ; i++){
			len += 2;
			if(skillsWithoutTraceAndWithTargetOrPosition[i].getStyle2() != null){
				try{
				len += skillsWithoutTraceAndWithTargetOrPosition[i].getStyle2().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillsWithoutTraceAndWithTargetOrPosition[i].getEffectiveTimes().length * 4;
			len += 1;
			len += 1;
			len += 1;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(skillsWithoutTraceAndWithTargetOrPosition[i].getStyle1() != null){
				try{
				len += skillsWithoutTraceAndWithTargetOrPosition[i].getStyle1().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 2;
			if(skillsWithoutTraceAndWithTargetOrPosition[i].getEffectType() != null){
				try{
				len += skillsWithoutTraceAndWithTargetOrPosition[i].getEffectType().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithoutTraceAndWithTargetOrPosition[i].getAvataRace() != null){
				try{
				len += skillsWithoutTraceAndWithTargetOrPosition[i].getAvataRace().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithoutTraceAndWithTargetOrPosition[i].getAvataSex() != null){
				try{
				len += skillsWithoutTraceAndWithTargetOrPosition[i].getAvataSex().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 4;
			len += skillsWithoutTraceAndWithTargetOrPosition[i].getMp().length * 2;
			len += 4;
			len += 2;
			if(skillsWithoutTraceAndWithTargetOrPosition[i].getFlySound() != null){
				try{
				len += skillsWithoutTraceAndWithTargetOrPosition[i].getFlySound().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithoutTraceAndWithTargetOrPosition[i].getExplodeSound() != null){
				try{
				len += skillsWithoutTraceAndWithTargetOrPosition[i].getExplodeSound().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] flyParticle = skillsWithoutTraceAndWithTargetOrPosition[i].getFlyParticle();
			for(int j = 0 ; j < flyParticle.length; j++){
				len += 2;
				try{
					len += flyParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] explodeParticle = skillsWithoutTraceAndWithTargetOrPosition[i].getExplodeParticle();
			for(int j = 0 ; j < explodeParticle.length; j++){
				len += 2;
				try{
					len += explodeParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			String[] bodyParticle = skillsWithoutTraceAndWithTargetOrPosition[i].getBodyParticle();
			for(int j = 0 ; j < bodyParticle.length; j++){
				len += 2;
				try{
					len += bodyParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += skillsWithoutTraceAndWithTargetOrPosition[i].getBodyParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(skillsWithoutTraceAndWithTargetOrPosition[i].getBodyPartPath() != null){
				try{
				len += skillsWithoutTraceAndWithTargetOrPosition[i].getBodyPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithoutTraceAndWithTargetOrPosition[i].getBodyPartAnimation() != null){
				try{
				len += skillsWithoutTraceAndWithTargetOrPosition[i].getBodyPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += skillsWithoutTraceAndWithTargetOrPosition[i].getBodyPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 4;
			String[] footParticle = skillsWithoutTraceAndWithTargetOrPosition[i].getFootParticle();
			for(int j = 0 ; j < footParticle.length; j++){
				len += 2;
				try{
					len += footParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillsWithoutTraceAndWithTargetOrPosition[i].getFootParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(skillsWithoutTraceAndWithTargetOrPosition[i].getFootPartPath() != null){
				try{
				len += skillsWithoutTraceAndWithTargetOrPosition[i].getFootPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithoutTraceAndWithTargetOrPosition[i].getFootPartAnimation() != null){
				try{
				len += skillsWithoutTraceAndWithTargetOrPosition[i].getFootPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillsWithoutTraceAndWithTargetOrPosition[i].getFootPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			len += 4;
			String[] targetFootParticle = skillsWithoutTraceAndWithTargetOrPosition[i].getTargetFootParticle();
			for(int j = 0 ; j < targetFootParticle.length; j++){
				len += 2;
				try{
					len += targetFootParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 2;
			if(skillsWithoutTraceAndWithTargetOrPosition[i].getBuffName() != null){
				try{
				len += skillsWithoutTraceAndWithTargetOrPosition[i].getBuffName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 4;
		for(int i = 0 ; i < skillWithoutTraceAndWithSummonNPC.length ; i++){
			len += 2;
			if(skillWithoutTraceAndWithSummonNPC[i].getStyle2() != null){
				try{
				len += skillWithoutTraceAndWithSummonNPC[i].getStyle2().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillWithoutTraceAndWithSummonNPC[i].getEffectiveTimes().length * 4;
			len += 1;
			len += 1;
			len += 1;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(skillWithoutTraceAndWithSummonNPC[i].getStyle1() != null){
				try{
				len += skillWithoutTraceAndWithSummonNPC[i].getStyle1().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 1;
			len += 4;
			len += skillWithoutTraceAndWithSummonNPC[i].getAttackNum().length * 2;
			len += 4;
			len += skillWithoutTraceAndWithSummonNPC[i].getMp().length * 2;
			len += 4;
			len += 4;
			len += 4;
			len += 8;
			len += 2;
			len += 2;
			len += 2;
			len += 2;
			len += 2;
			if(skillWithoutTraceAndWithSummonNPC[i].getFlySound() != null){
				try{
				len += skillWithoutTraceAndWithSummonNPC[i].getFlySound().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillWithoutTraceAndWithSummonNPC[i].getExplodeSound() != null){
				try{
				len += skillWithoutTraceAndWithSummonNPC[i].getExplodeSound().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] flyParticle = skillWithoutTraceAndWithSummonNPC[i].getFlyParticle();
			for(int j = 0 ; j < flyParticle.length; j++){
				len += 2;
				try{
					len += flyParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] explodeParticle = skillWithoutTraceAndWithSummonNPC[i].getExplodeParticle();
			for(int j = 0 ; j < explodeParticle.length; j++){
				len += 2;
				try{
					len += explodeParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			String[] bodyParticle = skillWithoutTraceAndWithSummonNPC[i].getBodyParticle();
			for(int j = 0 ; j < bodyParticle.length; j++){
				len += 2;
				try{
					len += bodyParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += skillWithoutTraceAndWithSummonNPC[i].getBodyParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(skillWithoutTraceAndWithSummonNPC[i].getBodyPartPath() != null){
				try{
				len += skillWithoutTraceAndWithSummonNPC[i].getBodyPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillWithoutTraceAndWithSummonNPC[i].getBodyPartAnimation() != null){
				try{
				len += skillWithoutTraceAndWithSummonNPC[i].getBodyPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += skillWithoutTraceAndWithSummonNPC[i].getBodyPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 4;
			String[] footParticle = skillWithoutTraceAndWithSummonNPC[i].getFootParticle();
			for(int j = 0 ; j < footParticle.length; j++){
				len += 2;
				try{
					len += footParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillWithoutTraceAndWithSummonNPC[i].getFootParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(skillWithoutTraceAndWithSummonNPC[i].getFootPartPath() != null){
				try{
				len += skillWithoutTraceAndWithSummonNPC[i].getFootPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillWithoutTraceAndWithSummonNPC[i].getFootPartAnimation() != null){
				try{
				len += skillWithoutTraceAndWithSummonNPC[i].getFootPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillWithoutTraceAndWithSummonNPC[i].getFootPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			len += 4;
			String[] targetFootParticle = skillWithoutTraceAndWithSummonNPC[i].getTargetFootParticle();
			for(int j = 0 ; j < targetFootParticle.length; j++){
				len += 2;
				try{
					len += targetFootParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 2;
			if(skillWithoutTraceAndWithSummonNPC[i].getBuffName() != null){
				try{
				len += skillWithoutTraceAndWithSummonNPC[i].getBuffName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 4;
		for(int i = 0 ; i < skillsWithTraceAndDirectionOrTarget.length ; i++){
			len += 2;
			if(skillsWithTraceAndDirectionOrTarget[i].getStyle2() != null){
				try{
				len += skillsWithTraceAndDirectionOrTarget[i].getStyle2().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillsWithTraceAndDirectionOrTarget[i].getEffectiveTimes().length * 4;
			len += 1;
			len += 1;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(skillsWithTraceAndDirectionOrTarget[i].getStyle1() != null){
				try{
				len += skillsWithTraceAndDirectionOrTarget[i].getStyle1().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 1;
			len += 1;
			len += 2;
			if(skillsWithTraceAndDirectionOrTarget[i].getEffectType() != null){
				try{
				len += skillsWithTraceAndDirectionOrTarget[i].getEffectType().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithTraceAndDirectionOrTarget[i].getAvataRace() != null){
				try{
				len += skillsWithTraceAndDirectionOrTarget[i].getAvataRace().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithTraceAndDirectionOrTarget[i].getAvataSex() != null){
				try{
				len += skillsWithTraceAndDirectionOrTarget[i].getAvataSex().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += skillsWithTraceAndDirectionOrTarget[i].getEffectInitPositionX().length * 4;
			len += 4;
			len += skillsWithTraceAndDirectionOrTarget[i].getEffectInitPositionY().length * 4;
			len += 4;
			len += skillsWithTraceAndDirectionOrTarget[i].getEffectInitDirection().length * 1;
			len += 4;
			len += 4;
			len += skillsWithTraceAndDirectionOrTarget[i].getMp().length * 2;
			len += 2;
			if(skillsWithTraceAndDirectionOrTarget[i].getFlySound() != null){
				try{
				len += skillsWithTraceAndDirectionOrTarget[i].getFlySound().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithTraceAndDirectionOrTarget[i].getExplodeSound() != null){
				try{
				len += skillsWithTraceAndDirectionOrTarget[i].getExplodeSound().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] flyParticle = skillsWithTraceAndDirectionOrTarget[i].getFlyParticle();
			for(int j = 0 ; j < flyParticle.length; j++){
				len += 2;
				try{
					len += flyParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] explodeParticle = skillsWithTraceAndDirectionOrTarget[i].getExplodeParticle();
			for(int j = 0 ; j < explodeParticle.length; j++){
				len += 2;
				try{
					len += explodeParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			String[] bodyParticle = skillsWithTraceAndDirectionOrTarget[i].getBodyParticle();
			for(int j = 0 ; j < bodyParticle.length; j++){
				len += 2;
				try{
					len += bodyParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += skillsWithTraceAndDirectionOrTarget[i].getBodyParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(skillsWithTraceAndDirectionOrTarget[i].getBodyPartPath() != null){
				try{
				len += skillsWithTraceAndDirectionOrTarget[i].getBodyPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithTraceAndDirectionOrTarget[i].getBodyPartAnimation() != null){
				try{
				len += skillsWithTraceAndDirectionOrTarget[i].getBodyPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += skillsWithTraceAndDirectionOrTarget[i].getBodyPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 4;
			String[] footParticle = skillsWithTraceAndDirectionOrTarget[i].getFootParticle();
			for(int j = 0 ; j < footParticle.length; j++){
				len += 2;
				try{
					len += footParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillsWithTraceAndDirectionOrTarget[i].getFootParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(skillsWithTraceAndDirectionOrTarget[i].getFootPartPath() != null){
				try{
				len += skillsWithTraceAndDirectionOrTarget[i].getFootPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillsWithTraceAndDirectionOrTarget[i].getFootPartAnimation() != null){
				try{
				len += skillsWithTraceAndDirectionOrTarget[i].getFootPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillsWithTraceAndDirectionOrTarget[i].getFootPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			len += 4;
			String[] targetFootParticle = skillsWithTraceAndDirectionOrTarget[i].getTargetFootParticle();
			for(int j = 0 ; j < targetFootParticle.length; j++){
				len += 2;
				try{
					len += targetFootParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 8;
			len += 1;
			len += 2;
			if(skillsWithTraceAndDirectionOrTarget[i].getBuffName() != null){
				try{
				len += skillsWithTraceAndDirectionOrTarget[i].getBuffName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 4;
		for(int i = 0 ; i < skillWithoutTraceAndWithMatrix.length ; i++){
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(skillWithoutTraceAndWithMatrix[i].getStyle1() != null){
				try{
				len += skillWithoutTraceAndWithMatrix[i].getStyle1().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillWithoutTraceAndWithMatrix[i].getStyle2() != null){
				try{
				len += skillWithoutTraceAndWithMatrix[i].getStyle2().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillWithoutTraceAndWithMatrix[i].getEffectiveTimes().length * 4;
			len += 1;
			len += 1;
			len += 1;
			len += 1;
			len += 1;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(skillWithoutTraceAndWithMatrix[i].getEffectType() != null){
				try{
				len += skillWithoutTraceAndWithMatrix[i].getEffectType().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillWithoutTraceAndWithMatrix[i].getAvataRace() != null){
				try{
				len += skillWithoutTraceAndWithMatrix[i].getAvataRace().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillWithoutTraceAndWithMatrix[i].getAvataSex() != null){
				try{
				len += skillWithoutTraceAndWithMatrix[i].getAvataSex().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 4;
			len += skillWithoutTraceAndWithMatrix[i].getMp().length * 2;
			len += 4;
			len += 2;
			if(skillWithoutTraceAndWithMatrix[i].getFlySound() != null){
				try{
				len += skillWithoutTraceAndWithMatrix[i].getFlySound().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillWithoutTraceAndWithMatrix[i].getExplodeSound() != null){
				try{
				len += skillWithoutTraceAndWithMatrix[i].getExplodeSound().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] flyParticle = skillWithoutTraceAndWithMatrix[i].getFlyParticle();
			for(int j = 0 ; j < flyParticle.length; j++){
				len += 2;
				try{
					len += flyParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] explodeParticle = skillWithoutTraceAndWithMatrix[i].getExplodeParticle();
			for(int j = 0 ; j < explodeParticle.length; j++){
				len += 2;
				try{
					len += explodeParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			String[] bodyParticle = skillWithoutTraceAndWithMatrix[i].getBodyParticle();
			for(int j = 0 ; j < bodyParticle.length; j++){
				len += 2;
				try{
					len += bodyParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += skillWithoutTraceAndWithMatrix[i].getBodyParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(skillWithoutTraceAndWithMatrix[i].getBodyPartPath() != null){
				try{
				len += skillWithoutTraceAndWithMatrix[i].getBodyPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillWithoutTraceAndWithMatrix[i].getBodyPartAnimation() != null){
				try{
				len += skillWithoutTraceAndWithMatrix[i].getBodyPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += skillWithoutTraceAndWithMatrix[i].getBodyPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 4;
			String[] footParticle = skillWithoutTraceAndWithMatrix[i].getFootParticle();
			for(int j = 0 ; j < footParticle.length; j++){
				len += 2;
				try{
					len += footParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillWithoutTraceAndWithMatrix[i].getFootParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(skillWithoutTraceAndWithMatrix[i].getFootPartPath() != null){
				try{
				len += skillWithoutTraceAndWithMatrix[i].getFootPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillWithoutTraceAndWithMatrix[i].getFootPartAnimation() != null){
				try{
				len += skillWithoutTraceAndWithMatrix[i].getFootPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillWithoutTraceAndWithMatrix[i].getFootPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			len += 4;
			String[] targetFootParticle = skillWithoutTraceAndWithMatrix[i].getTargetFootParticle();
			for(int j = 0 ; j < targetFootParticle.length; j++){
				len += 2;
				try{
					len += targetFootParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 2;
			if(skillWithoutTraceAndWithMatrix[i].getBuffName() != null){
				try{
				len += skillWithoutTraceAndWithMatrix[i].getBuffName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillWithoutTraceAndWithMatrix[i].getMaxAttackNums().length * 2;
		}
		len += 4;
		for(int i = 0 ; i < skillWithoutTraceAndOnTeamMember.length ; i++){
			len += 1;
			len += 1;
			len += 1;
			len += 1;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(skillWithoutTraceAndOnTeamMember[i].getStyle1() != null){
				try{
				len += skillWithoutTraceAndOnTeamMember[i].getStyle1().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillWithoutTraceAndOnTeamMember[i].getStyle2() != null){
				try{
				len += skillWithoutTraceAndOnTeamMember[i].getStyle2().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillWithoutTraceAndOnTeamMember[i].getEffectiveTimes().length * 4;
			len += 1;
			len += 4;
			len += 4;
			len += 2;
			if(skillWithoutTraceAndOnTeamMember[i].getEffectType() != null){
				try{
				len += skillWithoutTraceAndOnTeamMember[i].getEffectType().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillWithoutTraceAndOnTeamMember[i].getAvataRace() != null){
				try{
				len += skillWithoutTraceAndOnTeamMember[i].getAvataRace().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillWithoutTraceAndOnTeamMember[i].getAvataSex() != null){
				try{
				len += skillWithoutTraceAndOnTeamMember[i].getAvataSex().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 4;
			len += skillWithoutTraceAndOnTeamMember[i].getMp().length * 2;
			len += 2;
			if(skillWithoutTraceAndOnTeamMember[i].getFlySound() != null){
				try{
				len += skillWithoutTraceAndOnTeamMember[i].getFlySound().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillWithoutTraceAndOnTeamMember[i].getExplodeSound() != null){
				try{
				len += skillWithoutTraceAndOnTeamMember[i].getExplodeSound().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] flyParticle = skillWithoutTraceAndOnTeamMember[i].getFlyParticle();
			for(int j = 0 ; j < flyParticle.length; j++){
				len += 2;
				try{
					len += flyParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] explodeParticle = skillWithoutTraceAndOnTeamMember[i].getExplodeParticle();
			for(int j = 0 ; j < explodeParticle.length; j++){
				len += 2;
				try{
					len += explodeParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			String[] bodyParticle = skillWithoutTraceAndOnTeamMember[i].getBodyParticle();
			for(int j = 0 ; j < bodyParticle.length; j++){
				len += 2;
				try{
					len += bodyParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += skillWithoutTraceAndOnTeamMember[i].getBodyParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(skillWithoutTraceAndOnTeamMember[i].getBodyPartPath() != null){
				try{
				len += skillWithoutTraceAndOnTeamMember[i].getBodyPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillWithoutTraceAndOnTeamMember[i].getBodyPartAnimation() != null){
				try{
				len += skillWithoutTraceAndOnTeamMember[i].getBodyPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 4;
			len += skillWithoutTraceAndOnTeamMember[i].getBodyPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 4;
			String[] footParticle = skillWithoutTraceAndOnTeamMember[i].getFootParticle();
			for(int j = 0 ; j < footParticle.length; j++){
				len += 2;
				try{
					len += footParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillWithoutTraceAndOnTeamMember[i].getFootParticleOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			if(skillWithoutTraceAndOnTeamMember[i].getFootPartPath() != null){
				try{
				len += skillWithoutTraceAndOnTeamMember[i].getFootPartPath().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillWithoutTraceAndOnTeamMember[i].getFootPartAnimation() != null){
				try{
				len += skillWithoutTraceAndOnTeamMember[i].getFootPartAnimation().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += skillWithoutTraceAndOnTeamMember[i].getFootPartAnimationOffset().length * 2;
			len += 8;
			len += 8;
			len += 2;
			len += 4;
			String[] targetFootParticle = skillWithoutTraceAndOnTeamMember[i].getTargetFootParticle();
			for(int j = 0 ; j < targetFootParticle.length; j++){
				len += 2;
				try{
					len += targetFootParticle[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 2;
			if(skillWithoutTraceAndOnTeamMember[i].getBuffName() != null){
				try{
				len += skillWithoutTraceAndOnTeamMember[i].getBuffName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
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

			buffer.putInt(commonAttackSkillIdsForWeapon.length);
			for(int i = 0 ; i < commonAttackSkillIdsForWeapon.length; i++){
				buffer.putInt(commonAttackSkillIdsForWeapon[i]);
			}
			buffer.putInt(commonAttackSkills.length);
			for(int i = 0 ; i < commonAttackSkills.length ; i++){
				buffer.putInt((int)commonAttackSkills[i].getDuration1());
				buffer.putInt((int)commonAttackSkills[i].getDuration2());
				buffer.putInt((int)commonAttackSkills[i].getDuration3());
				buffer.putInt(commonAttackSkills[i].getEffectiveTimes().length);
				int[] effectiveTimes_0100 = commonAttackSkills[i].getEffectiveTimes();
				for(int j = 0 ; j < effectiveTimes_0100.length ; j++){
					buffer.putInt(effectiveTimes_0100[j]);
				}
				buffer.put((byte)commonAttackSkills[i].getEnableWeaponType());
				buffer.put((byte)commonAttackSkills[i].getWeaponTypeLimit());
				buffer.put((byte)commonAttackSkills[i].getFollowByCommonAttack());
				buffer.put((byte)(commonAttackSkills[i].isIgnoreStun()==false?0:1));
				buffer.putInt((int)commonAttackSkills[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = commonAttackSkills[i].getStyle1().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = commonAttackSkills[i].getStyle2().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)commonAttackSkills[i].getEffectExplosionLastTime());
				buffer.putInt((int)commonAttackSkills[i].getRange());
				try{
				tmpBytes2 = commonAttackSkills[i].getEffectType().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = commonAttackSkills[i].getAvataRace().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = commonAttackSkills[i].getAvataSex().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)commonAttackSkills[i].getEffectLastTime());
				try{
				tmpBytes2 = commonAttackSkills[i].getFlySound().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = commonAttackSkills[i].getExplodeSound().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(commonAttackSkills[i].getFlyParticle().length);
				String[] flyParticle_0101 = commonAttackSkills[i].getFlyParticle();
				for(int j = 0 ; j < flyParticle_0101.length ; j++){
				try{
					buffer.putShort((short)flyParticle_0101[j].getBytes("UTF-8").length);
					buffer.put(flyParticle_0101[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(commonAttackSkills[i].getExplodeParticle().length);
				String[] explodeParticle_0102 = commonAttackSkills[i].getExplodeParticle();
				for(int j = 0 ; j < explodeParticle_0102.length ; j++){
				try{
					buffer.putShort((short)explodeParticle_0102[j].getBytes("UTF-8").length);
					buffer.put(explodeParticle_0102[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putShort((short)commonAttackSkills[i].getExplodePercent());
				buffer.putInt(commonAttackSkills[i].getBodyParticle().length);
				String[] bodyParticle_0103 = commonAttackSkills[i].getBodyParticle();
				for(int j = 0 ; j < bodyParticle_0103.length ; j++){
				try{
					buffer.putShort((short)bodyParticle_0103[j].getBytes("UTF-8").length);
					buffer.put(bodyParticle_0103[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putShort((short)commonAttackSkills[i].getBodyPercent());
				buffer.putInt(commonAttackSkills[i].getBodyParticleOffset().length);
				short[] bodyParticleOffset_0104 = commonAttackSkills[i].getBodyParticleOffset();
				for(int j = 0 ; j < bodyParticleOffset_0104.length ; j++){
					buffer.putShort(bodyParticleOffset_0104[j]);
				}
				buffer.putLong(commonAttackSkills[i].getBodyParticlePlayStart());
				buffer.putLong(commonAttackSkills[i].getBodyParticlePlayEnd());
				try{
				tmpBytes2 = commonAttackSkills[i].getBodyPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = commonAttackSkills[i].getBodyPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)commonAttackSkills[i].getBodyPartAnimationPercent());
				buffer.putInt(commonAttackSkills[i].getBodyPartAnimationOffset().length);
				short[] bodyPartAnimationOffset_0105 = commonAttackSkills[i].getBodyPartAnimationOffset();
				for(int j = 0 ; j < bodyPartAnimationOffset_0105.length ; j++){
					buffer.putShort(bodyPartAnimationOffset_0105[j]);
				}
				buffer.putLong(commonAttackSkills[i].getBodyPartAnimationPlayStart());
				buffer.putLong(commonAttackSkills[i].getBodyPartAnimationPlayEnd());
				buffer.putInt(commonAttackSkills[i].getFootParticle().length);
				String[] footParticle_0106 = commonAttackSkills[i].getFootParticle();
				for(int j = 0 ; j < footParticle_0106.length ; j++){
				try{
					buffer.putShort((short)footParticle_0106[j].getBytes("UTF-8").length);
					buffer.put(footParticle_0106[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(commonAttackSkills[i].getFootParticleOffset().length);
				short[] footParticleOffset_0107 = commonAttackSkills[i].getFootParticleOffset();
				for(int j = 0 ; j < footParticleOffset_0107.length ; j++){
					buffer.putShort(footParticleOffset_0107[j]);
				}
				buffer.putLong(commonAttackSkills[i].getFootParticlePlayStart());
				buffer.putLong(commonAttackSkills[i].getFootParticlePlayEnd());
				try{
				tmpBytes2 = commonAttackSkills[i].getFootPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = commonAttackSkills[i].getFootPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(commonAttackSkills[i].getFootPartAnimationOffset().length);
				short[] footPartAnimationOffset_0108 = commonAttackSkills[i].getFootPartAnimationOffset();
				for(int j = 0 ; j < footPartAnimationOffset_0108.length ; j++){
					buffer.putShort(footPartAnimationOffset_0108[j]);
				}
				buffer.putLong(commonAttackSkills[i].getFootPartAnimationPlayStart());
				buffer.putLong(commonAttackSkills[i].getFootPartAnimationPlayEnd());
				buffer.putShort((short)commonAttackSkills[i].getAngle());
				buffer.putInt(commonAttackSkills[i].getTargetFootParticle().length);
				String[] targetFootParticle_0109 = commonAttackSkills[i].getTargetFootParticle();
				for(int j = 0 ; j < targetFootParticle_0109.length ; j++){
				try{
					buffer.putShort((short)targetFootParticle_0109[j].getBytes("UTF-8").length);
					buffer.put(targetFootParticle_0109[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.put((byte)(commonAttackSkills[i].isGuajiFlag()==false?0:1));
				try{
				tmpBytes2 = commonAttackSkills[i].getBuffName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(skillsWithoutEffect.length);
			for(int i = 0 ; i < skillsWithoutEffect.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = skillsWithoutEffect[i].getStyle1().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithoutEffect[i].getStyle2().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillsWithoutEffect[i].getEffectiveTimes().length);
				int[] effectiveTimes_0110 = skillsWithoutEffect[i].getEffectiveTimes();
				for(int j = 0 ; j < effectiveTimes_0110.length ; j++){
					buffer.putInt(effectiveTimes_0110[j]);
				}
				buffer.put((byte)skillsWithoutEffect[i].getEnableWeaponType());
				buffer.put((byte)skillsWithoutEffect[i].getWeaponTypeLimit());
				buffer.put((byte)skillsWithoutEffect[i].getFollowByCommonAttack());
				buffer.put((byte)(skillsWithoutEffect[i].isIgnoreStun()==false?0:1));
				buffer.putInt((int)skillsWithoutEffect[i].getId());
				buffer.putInt((int)skillsWithoutEffect[i].getDuration1());
				buffer.putInt((int)skillsWithoutEffect[i].getDuration2());
				buffer.putInt((int)skillsWithoutEffect[i].getDuration3());
				buffer.putInt((int)skillsWithoutEffect[i].getRange());
				buffer.putInt(skillsWithoutEffect[i].getBodyParticle().length);
				String[] bodyParticle_0111 = skillsWithoutEffect[i].getBodyParticle();
				for(int j = 0 ; j < bodyParticle_0111.length ; j++){
				try{
					buffer.putShort((short)bodyParticle_0111[j].getBytes("UTF-8").length);
					buffer.put(bodyParticle_0111[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putShort((short)skillsWithoutEffect[i].getBodyPercent());
				buffer.putInt(skillsWithoutEffect[i].getBodyParticleOffset().length);
				short[] bodyParticleOffset_0112 = skillsWithoutEffect[i].getBodyParticleOffset();
				for(int j = 0 ; j < bodyParticleOffset_0112.length ; j++){
					buffer.putShort(bodyParticleOffset_0112[j]);
				}
				buffer.putLong(skillsWithoutEffect[i].getBodyParticlePlayStart());
				buffer.putLong(skillsWithoutEffect[i].getBodyParticlePlayEnd());
				try{
				tmpBytes2 = skillsWithoutEffect[i].getBodyPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithoutEffect[i].getBodyPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)skillsWithoutEffect[i].getBodyPartAnimationPercent());
				buffer.putInt(skillsWithoutEffect[i].getBodyPartAnimationOffset().length);
				short[] bodyPartAnimationOffset_0113 = skillsWithoutEffect[i].getBodyPartAnimationOffset();
				for(int j = 0 ; j < bodyPartAnimationOffset_0113.length ; j++){
					buffer.putShort(bodyPartAnimationOffset_0113[j]);
				}
				buffer.putLong(skillsWithoutEffect[i].getBodyPartAnimationPlayStart());
				buffer.putLong(skillsWithoutEffect[i].getBodyPartAnimationPlayEnd());
				buffer.putInt(skillsWithoutEffect[i].getFootParticle().length);
				String[] footParticle_0114 = skillsWithoutEffect[i].getFootParticle();
				for(int j = 0 ; j < footParticle_0114.length ; j++){
				try{
					buffer.putShort((short)footParticle_0114[j].getBytes("UTF-8").length);
					buffer.put(footParticle_0114[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(skillsWithoutEffect[i].getFootParticleOffset().length);
				short[] footParticleOffset_0115 = skillsWithoutEffect[i].getFootParticleOffset();
				for(int j = 0 ; j < footParticleOffset_0115.length ; j++){
					buffer.putShort(footParticleOffset_0115[j]);
				}
				buffer.putLong(skillsWithoutEffect[i].getFootParticlePlayStart());
				buffer.putLong(skillsWithoutEffect[i].getFootParticlePlayEnd());
				try{
				tmpBytes2 = skillsWithoutEffect[i].getFootPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithoutEffect[i].getFootPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillsWithoutEffect[i].getFootPartAnimationOffset().length);
				short[] footPartAnimationOffset_0116 = skillsWithoutEffect[i].getFootPartAnimationOffset();
				for(int j = 0 ; j < footPartAnimationOffset_0116.length ; j++){
					buffer.putShort(footPartAnimationOffset_0116[j]);
				}
				buffer.putLong(skillsWithoutEffect[i].getFootPartAnimationPlayStart());
				buffer.putLong(skillsWithoutEffect[i].getFootPartAnimationPlayEnd());
				buffer.putShort((short)skillsWithoutEffect[i].getAngle());
				buffer.putInt(skillsWithoutEffect[i].getTargetFootParticle().length);
				String[] targetFootParticle_0117 = skillsWithoutEffect[i].getTargetFootParticle();
				for(int j = 0 ; j < targetFootParticle_0117.length ; j++){
				try{
					buffer.putShort((short)targetFootParticle_0117[j].getBytes("UTF-8").length);
					buffer.put(targetFootParticle_0117[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.put((byte)(skillsWithoutEffect[i].isGuajiFlag()==false?0:1));
				try{
				tmpBytes2 = skillsWithoutEffect[i].getBuffName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(skillsWithoutEffectAndQuickMove.length);
			for(int i = 0 ; i < skillsWithoutEffectAndQuickMove.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = skillsWithoutEffectAndQuickMove[i].getStyle2().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillsWithoutEffectAndQuickMove[i].getEffectiveTimes().length);
				int[] effectiveTimes_0118 = skillsWithoutEffectAndQuickMove[i].getEffectiveTimes();
				for(int j = 0 ; j < effectiveTimes_0118.length ; j++){
					buffer.putInt(effectiveTimes_0118[j]);
				}
				buffer.put((byte)skillsWithoutEffectAndQuickMove[i].getEnableWeaponType());
				buffer.put((byte)skillsWithoutEffectAndQuickMove[i].getWeaponTypeLimit());
				buffer.put((byte)skillsWithoutEffectAndQuickMove[i].getFollowByCommonAttack());
				buffer.put((byte)(skillsWithoutEffectAndQuickMove[i].isIgnoreStun()==false?0:1));
				buffer.putInt((int)skillsWithoutEffectAndQuickMove[i].getId());
				buffer.putInt((int)skillsWithoutEffectAndQuickMove[i].getDuration1());
				buffer.putInt((int)skillsWithoutEffectAndQuickMove[i].getDuration2());
				buffer.putInt((int)skillsWithoutEffectAndQuickMove[i].getDuration3());
				try{
				tmpBytes2 = skillsWithoutEffectAndQuickMove[i].getStyle1().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)skillsWithoutEffectAndQuickMove[i].getTraceType());
				buffer.putInt((int)skillsWithoutEffectAndQuickMove[i].getDistance());
				buffer.putInt(skillsWithoutEffectAndQuickMove[i].getMp().length);
				short[] mp_0119 = skillsWithoutEffectAndQuickMove[i].getMp();
				for(int j = 0 ; j < mp_0119.length ; j++){
					buffer.putShort(mp_0119[j]);
				}
				buffer.putInt((int)skillsWithoutEffectAndQuickMove[i].getRange());
				buffer.putInt(skillsWithoutEffectAndQuickMove[i].getBodyParticle().length);
				String[] bodyParticle_0120 = skillsWithoutEffectAndQuickMove[i].getBodyParticle();
				for(int j = 0 ; j < bodyParticle_0120.length ; j++){
				try{
					buffer.putShort((short)bodyParticle_0120[j].getBytes("UTF-8").length);
					buffer.put(bodyParticle_0120[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putShort((short)skillsWithoutEffectAndQuickMove[i].getBodyPercent());
				buffer.putInt(skillsWithoutEffectAndQuickMove[i].getBodyParticleOffset().length);
				short[] bodyParticleOffset_0121 = skillsWithoutEffectAndQuickMove[i].getBodyParticleOffset();
				for(int j = 0 ; j < bodyParticleOffset_0121.length ; j++){
					buffer.putShort(bodyParticleOffset_0121[j]);
				}
				buffer.putLong(skillsWithoutEffectAndQuickMove[i].getBodyParticlePlayStart());
				buffer.putLong(skillsWithoutEffectAndQuickMove[i].getBodyParticlePlayEnd());
				try{
				tmpBytes2 = skillsWithoutEffectAndQuickMove[i].getBodyPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithoutEffectAndQuickMove[i].getBodyPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)skillsWithoutEffectAndQuickMove[i].getBodyPartAnimationPercent());
				buffer.putInt(skillsWithoutEffectAndQuickMove[i].getBodyPartAnimationOffset().length);
				short[] bodyPartAnimationOffset_0122 = skillsWithoutEffectAndQuickMove[i].getBodyPartAnimationOffset();
				for(int j = 0 ; j < bodyPartAnimationOffset_0122.length ; j++){
					buffer.putShort(bodyPartAnimationOffset_0122[j]);
				}
				buffer.putLong(skillsWithoutEffectAndQuickMove[i].getBodyPartAnimationPlayStart());
				buffer.putLong(skillsWithoutEffectAndQuickMove[i].getBodyPartAnimationPlayEnd());
				buffer.putInt(skillsWithoutEffectAndQuickMove[i].getFootParticle().length);
				String[] footParticle_0123 = skillsWithoutEffectAndQuickMove[i].getFootParticle();
				for(int j = 0 ; j < footParticle_0123.length ; j++){
				try{
					buffer.putShort((short)footParticle_0123[j].getBytes("UTF-8").length);
					buffer.put(footParticle_0123[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(skillsWithoutEffectAndQuickMove[i].getFootParticleOffset().length);
				short[] footParticleOffset_0124 = skillsWithoutEffectAndQuickMove[i].getFootParticleOffset();
				for(int j = 0 ; j < footParticleOffset_0124.length ; j++){
					buffer.putShort(footParticleOffset_0124[j]);
				}
				buffer.putLong(skillsWithoutEffectAndQuickMove[i].getFootParticlePlayStart());
				buffer.putLong(skillsWithoutEffectAndQuickMove[i].getFootParticlePlayEnd());
				try{
				tmpBytes2 = skillsWithoutEffectAndQuickMove[i].getFootPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithoutEffectAndQuickMove[i].getFootPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillsWithoutEffectAndQuickMove[i].getFootPartAnimationOffset().length);
				short[] footPartAnimationOffset_0125 = skillsWithoutEffectAndQuickMove[i].getFootPartAnimationOffset();
				for(int j = 0 ; j < footPartAnimationOffset_0125.length ; j++){
					buffer.putShort(footPartAnimationOffset_0125[j]);
				}
				buffer.putLong(skillsWithoutEffectAndQuickMove[i].getFootPartAnimationPlayStart());
				buffer.putLong(skillsWithoutEffectAndQuickMove[i].getFootPartAnimationPlayEnd());
				buffer.putShort((short)skillsWithoutEffectAndQuickMove[i].getAngle());
				buffer.putInt(skillsWithoutEffectAndQuickMove[i].getTargetFootParticle().length);
				String[] targetFootParticle_0126 = skillsWithoutEffectAndQuickMove[i].getTargetFootParticle();
				for(int j = 0 ; j < targetFootParticle_0126.length ; j++){
				try{
					buffer.putShort((short)targetFootParticle_0126[j].getBytes("UTF-8").length);
					buffer.put(targetFootParticle_0126[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.put((byte)(skillsWithoutEffectAndQuickMove[i].isGuajiFlag()==false?0:1));
				try{
				tmpBytes2 = skillsWithoutEffectAndQuickMove[i].getBuffName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)skillsWithoutEffectAndQuickMove[i].getSkillPlayType());
			}
			buffer.putInt(skillsWithoutTraceAndWithRange.length);
			for(int i = 0 ; i < skillsWithoutTraceAndWithRange.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = skillsWithoutTraceAndWithRange[i].getStyle2().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillsWithoutTraceAndWithRange[i].getEffectiveTimes().length);
				int[] effectiveTimes_0127 = skillsWithoutTraceAndWithRange[i].getEffectiveTimes();
				for(int j = 0 ; j < effectiveTimes_0127.length ; j++){
					buffer.putInt(effectiveTimes_0127[j]);
				}
				buffer.put((byte)skillsWithoutTraceAndWithRange[i].getEnableWeaponType());
				buffer.put((byte)skillsWithoutTraceAndWithRange[i].getWeaponTypeLimit());
				buffer.put((byte)skillsWithoutTraceAndWithRange[i].getFollowByCommonAttack());
				buffer.putInt((int)skillsWithoutTraceAndWithRange[i].getId());
				buffer.putInt((int)skillsWithoutTraceAndWithRange[i].getDuration1());
				buffer.putInt((int)skillsWithoutTraceAndWithRange[i].getDuration2());
				buffer.putInt((int)skillsWithoutTraceAndWithRange[i].getDuration3());
				try{
				tmpBytes2 = skillsWithoutTraceAndWithRange[i].getStyle1().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(skillsWithoutTraceAndWithRange[i].isIgnoreStun()==false?0:1));
				buffer.put((byte)skillsWithoutTraceAndWithRange[i].getRangeType());
				buffer.putInt((int)skillsWithoutTraceAndWithRange[i].getRangeWidth());
				buffer.putInt((int)skillsWithoutTraceAndWithRange[i].getRangeHeight());
				buffer.putInt(skillsWithoutTraceAndWithRange[i].getMaxAttackNums().length);
				short[] maxAttackNums_0128 = skillsWithoutTraceAndWithRange[i].getMaxAttackNums();
				for(int j = 0 ; j < maxAttackNums_0128.length ; j++){
					buffer.putShort(maxAttackNums_0128[j]);
				}
				try{
				tmpBytes2 = skillsWithoutTraceAndWithRange[i].getEffectType().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithoutTraceAndWithRange[i].getAvataRace().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithoutTraceAndWithRange[i].getAvataSex().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)skillsWithoutTraceAndWithRange[i].getEffectLastTime());
				buffer.putInt((int)skillsWithoutTraceAndWithRange[i].getEffectExplosionLastTime());
				buffer.putInt(skillsWithoutTraceAndWithRange[i].getMp().length);
				short[] mp_0129 = skillsWithoutTraceAndWithRange[i].getMp();
				for(int j = 0 ; j < mp_0129.length ; j++){
					buffer.putShort(mp_0129[j]);
				}
				try{
				tmpBytes2 = skillsWithoutTraceAndWithRange[i].getFlySound().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithoutTraceAndWithRange[i].getExplodeSound().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillsWithoutTraceAndWithRange[i].getFlyParticle().length);
				String[] flyParticle_0130 = skillsWithoutTraceAndWithRange[i].getFlyParticle();
				for(int j = 0 ; j < flyParticle_0130.length ; j++){
				try{
					buffer.putShort((short)flyParticle_0130[j].getBytes("UTF-8").length);
					buffer.put(flyParticle_0130[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(skillsWithoutTraceAndWithRange[i].getExplodeParticle().length);
				String[] explodeParticle_0131 = skillsWithoutTraceAndWithRange[i].getExplodeParticle();
				for(int j = 0 ; j < explodeParticle_0131.length ; j++){
				try{
					buffer.putShort((short)explodeParticle_0131[j].getBytes("UTF-8").length);
					buffer.put(explodeParticle_0131[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putShort((short)skillsWithoutTraceAndWithRange[i].getExplodePercent());
				buffer.putInt(skillsWithoutTraceAndWithRange[i].getBodyParticle().length);
				String[] bodyParticle_0132 = skillsWithoutTraceAndWithRange[i].getBodyParticle();
				for(int j = 0 ; j < bodyParticle_0132.length ; j++){
				try{
					buffer.putShort((short)bodyParticle_0132[j].getBytes("UTF-8").length);
					buffer.put(bodyParticle_0132[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putShort((short)skillsWithoutTraceAndWithRange[i].getBodyPercent());
				buffer.putInt(skillsWithoutTraceAndWithRange[i].getBodyParticleOffset().length);
				short[] bodyParticleOffset_0133 = skillsWithoutTraceAndWithRange[i].getBodyParticleOffset();
				for(int j = 0 ; j < bodyParticleOffset_0133.length ; j++){
					buffer.putShort(bodyParticleOffset_0133[j]);
				}
				buffer.putLong(skillsWithoutTraceAndWithRange[i].getBodyParticlePlayStart());
				buffer.putLong(skillsWithoutTraceAndWithRange[i].getBodyParticlePlayEnd());
				try{
				tmpBytes2 = skillsWithoutTraceAndWithRange[i].getBodyPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithoutTraceAndWithRange[i].getBodyPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)skillsWithoutTraceAndWithRange[i].getBodyPartAnimationPercent());
				buffer.putInt(skillsWithoutTraceAndWithRange[i].getBodyPartAnimationOffset().length);
				short[] bodyPartAnimationOffset_0134 = skillsWithoutTraceAndWithRange[i].getBodyPartAnimationOffset();
				for(int j = 0 ; j < bodyPartAnimationOffset_0134.length ; j++){
					buffer.putShort(bodyPartAnimationOffset_0134[j]);
				}
				buffer.putLong(skillsWithoutTraceAndWithRange[i].getBodyPartAnimationPlayStart());
				buffer.putLong(skillsWithoutTraceAndWithRange[i].getBodyPartAnimationPlayEnd());
				buffer.putInt(skillsWithoutTraceAndWithRange[i].getFootParticle().length);
				String[] footParticle_0135 = skillsWithoutTraceAndWithRange[i].getFootParticle();
				for(int j = 0 ; j < footParticle_0135.length ; j++){
				try{
					buffer.putShort((short)footParticle_0135[j].getBytes("UTF-8").length);
					buffer.put(footParticle_0135[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(skillsWithoutTraceAndWithRange[i].getFootParticleOffset().length);
				short[] footParticleOffset_0136 = skillsWithoutTraceAndWithRange[i].getFootParticleOffset();
				for(int j = 0 ; j < footParticleOffset_0136.length ; j++){
					buffer.putShort(footParticleOffset_0136[j]);
				}
				buffer.putLong(skillsWithoutTraceAndWithRange[i].getFootParticlePlayStart());
				buffer.putLong(skillsWithoutTraceAndWithRange[i].getFootParticlePlayEnd());
				try{
				tmpBytes2 = skillsWithoutTraceAndWithRange[i].getFootPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithoutTraceAndWithRange[i].getFootPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillsWithoutTraceAndWithRange[i].getFootPartAnimationOffset().length);
				short[] footPartAnimationOffset_0137 = skillsWithoutTraceAndWithRange[i].getFootPartAnimationOffset();
				for(int j = 0 ; j < footPartAnimationOffset_0137.length ; j++){
					buffer.putShort(footPartAnimationOffset_0137[j]);
				}
				buffer.putLong(skillsWithoutTraceAndWithRange[i].getFootPartAnimationPlayStart());
				buffer.putLong(skillsWithoutTraceAndWithRange[i].getFootPartAnimationPlayEnd());
				buffer.putShort((short)skillsWithoutTraceAndWithRange[i].getAngle());
				buffer.putInt(skillsWithoutTraceAndWithRange[i].getTargetFootParticle().length);
				String[] targetFootParticle_0138 = skillsWithoutTraceAndWithRange[i].getTargetFootParticle();
				for(int j = 0 ; j < targetFootParticle_0138.length ; j++){
				try{
					buffer.putShort((short)targetFootParticle_0138[j].getBytes("UTF-8").length);
					buffer.put(targetFootParticle_0138[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.put((byte)(skillsWithoutTraceAndWithRange[i].isGuajiFlag()==false?0:1));
				try{
				tmpBytes2 = skillsWithoutTraceAndWithRange[i].getBuffName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(skillsWithoutTraceAndWithTargetOrPosition.length);
			for(int i = 0 ; i < skillsWithoutTraceAndWithTargetOrPosition.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = skillsWithoutTraceAndWithTargetOrPosition[i].getStyle2().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillsWithoutTraceAndWithTargetOrPosition[i].getEffectiveTimes().length);
				int[] effectiveTimes_0139 = skillsWithoutTraceAndWithTargetOrPosition[i].getEffectiveTimes();
				for(int j = 0 ; j < effectiveTimes_0139.length ; j++){
					buffer.putInt(effectiveTimes_0139[j]);
				}
				buffer.put((byte)skillsWithoutTraceAndWithTargetOrPosition[i].getEnableWeaponType());
				buffer.put((byte)skillsWithoutTraceAndWithTargetOrPosition[i].getWeaponTypeLimit());
				buffer.put((byte)skillsWithoutTraceAndWithTargetOrPosition[i].getFollowByCommonAttack());
				buffer.putInt((int)skillsWithoutTraceAndWithTargetOrPosition[i].getId());
				buffer.putInt((int)skillsWithoutTraceAndWithTargetOrPosition[i].getDuration1());
				buffer.putInt((int)skillsWithoutTraceAndWithTargetOrPosition[i].getDuration2());
				buffer.putInt((int)skillsWithoutTraceAndWithTargetOrPosition[i].getDuration3());
				try{
				tmpBytes2 = skillsWithoutTraceAndWithTargetOrPosition[i].getStyle1().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(skillsWithoutTraceAndWithTargetOrPosition[i].isIgnoreStun()==false?0:1));
				try{
				tmpBytes2 = skillsWithoutTraceAndWithTargetOrPosition[i].getEffectType().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithoutTraceAndWithTargetOrPosition[i].getAvataRace().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithoutTraceAndWithTargetOrPosition[i].getAvataSex().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)skillsWithoutTraceAndWithTargetOrPosition[i].getEffectLastTime());
				buffer.putInt((int)skillsWithoutTraceAndWithTargetOrPosition[i].getEffectExplosionLastTime());
				buffer.putInt(skillsWithoutTraceAndWithTargetOrPosition[i].getMp().length);
				short[] mp_0140 = skillsWithoutTraceAndWithTargetOrPosition[i].getMp();
				for(int j = 0 ; j < mp_0140.length ; j++){
					buffer.putShort(mp_0140[j]);
				}
				buffer.putInt((int)skillsWithoutTraceAndWithTargetOrPosition[i].getRange());
				try{
				tmpBytes2 = skillsWithoutTraceAndWithTargetOrPosition[i].getFlySound().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithoutTraceAndWithTargetOrPosition[i].getExplodeSound().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillsWithoutTraceAndWithTargetOrPosition[i].getFlyParticle().length);
				String[] flyParticle_0141 = skillsWithoutTraceAndWithTargetOrPosition[i].getFlyParticle();
				for(int j = 0 ; j < flyParticle_0141.length ; j++){
				try{
					buffer.putShort((short)flyParticle_0141[j].getBytes("UTF-8").length);
					buffer.put(flyParticle_0141[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(skillsWithoutTraceAndWithTargetOrPosition[i].getExplodeParticle().length);
				String[] explodeParticle_0142 = skillsWithoutTraceAndWithTargetOrPosition[i].getExplodeParticle();
				for(int j = 0 ; j < explodeParticle_0142.length ; j++){
				try{
					buffer.putShort((short)explodeParticle_0142[j].getBytes("UTF-8").length);
					buffer.put(explodeParticle_0142[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putShort((short)skillsWithoutTraceAndWithTargetOrPosition[i].getExplodePercent());
				buffer.putInt(skillsWithoutTraceAndWithTargetOrPosition[i].getBodyParticle().length);
				String[] bodyParticle_0143 = skillsWithoutTraceAndWithTargetOrPosition[i].getBodyParticle();
				for(int j = 0 ; j < bodyParticle_0143.length ; j++){
				try{
					buffer.putShort((short)bodyParticle_0143[j].getBytes("UTF-8").length);
					buffer.put(bodyParticle_0143[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putShort((short)skillsWithoutTraceAndWithTargetOrPosition[i].getBodyPercent());
				buffer.putInt(skillsWithoutTraceAndWithTargetOrPosition[i].getBodyParticleOffset().length);
				short[] bodyParticleOffset_0144 = skillsWithoutTraceAndWithTargetOrPosition[i].getBodyParticleOffset();
				for(int j = 0 ; j < bodyParticleOffset_0144.length ; j++){
					buffer.putShort(bodyParticleOffset_0144[j]);
				}
				buffer.putLong(skillsWithoutTraceAndWithTargetOrPosition[i].getBodyParticlePlayStart());
				buffer.putLong(skillsWithoutTraceAndWithTargetOrPosition[i].getBodyParticlePlayEnd());
				try{
				tmpBytes2 = skillsWithoutTraceAndWithTargetOrPosition[i].getBodyPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithoutTraceAndWithTargetOrPosition[i].getBodyPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)skillsWithoutTraceAndWithTargetOrPosition[i].getBodyPartAnimationPercent());
				buffer.putInt(skillsWithoutTraceAndWithTargetOrPosition[i].getBodyPartAnimationOffset().length);
				short[] bodyPartAnimationOffset_0145 = skillsWithoutTraceAndWithTargetOrPosition[i].getBodyPartAnimationOffset();
				for(int j = 0 ; j < bodyPartAnimationOffset_0145.length ; j++){
					buffer.putShort(bodyPartAnimationOffset_0145[j]);
				}
				buffer.putLong(skillsWithoutTraceAndWithTargetOrPosition[i].getBodyPartAnimationPlayStart());
				buffer.putLong(skillsWithoutTraceAndWithTargetOrPosition[i].getBodyPartAnimationPlayEnd());
				buffer.putInt(skillsWithoutTraceAndWithTargetOrPosition[i].getFootParticle().length);
				String[] footParticle_0146 = skillsWithoutTraceAndWithTargetOrPosition[i].getFootParticle();
				for(int j = 0 ; j < footParticle_0146.length ; j++){
				try{
					buffer.putShort((short)footParticle_0146[j].getBytes("UTF-8").length);
					buffer.put(footParticle_0146[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(skillsWithoutTraceAndWithTargetOrPosition[i].getFootParticleOffset().length);
				short[] footParticleOffset_0147 = skillsWithoutTraceAndWithTargetOrPosition[i].getFootParticleOffset();
				for(int j = 0 ; j < footParticleOffset_0147.length ; j++){
					buffer.putShort(footParticleOffset_0147[j]);
				}
				buffer.putLong(skillsWithoutTraceAndWithTargetOrPosition[i].getFootParticlePlayStart());
				buffer.putLong(skillsWithoutTraceAndWithTargetOrPosition[i].getFootParticlePlayEnd());
				try{
				tmpBytes2 = skillsWithoutTraceAndWithTargetOrPosition[i].getFootPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithoutTraceAndWithTargetOrPosition[i].getFootPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillsWithoutTraceAndWithTargetOrPosition[i].getFootPartAnimationOffset().length);
				short[] footPartAnimationOffset_0148 = skillsWithoutTraceAndWithTargetOrPosition[i].getFootPartAnimationOffset();
				for(int j = 0 ; j < footPartAnimationOffset_0148.length ; j++){
					buffer.putShort(footPartAnimationOffset_0148[j]);
				}
				buffer.putLong(skillsWithoutTraceAndWithTargetOrPosition[i].getFootPartAnimationPlayStart());
				buffer.putLong(skillsWithoutTraceAndWithTargetOrPosition[i].getFootPartAnimationPlayEnd());
				buffer.putShort((short)skillsWithoutTraceAndWithTargetOrPosition[i].getAngle());
				buffer.putInt(skillsWithoutTraceAndWithTargetOrPosition[i].getTargetFootParticle().length);
				String[] targetFootParticle_0149 = skillsWithoutTraceAndWithTargetOrPosition[i].getTargetFootParticle();
				for(int j = 0 ; j < targetFootParticle_0149.length ; j++){
				try{
					buffer.putShort((short)targetFootParticle_0149[j].getBytes("UTF-8").length);
					buffer.put(targetFootParticle_0149[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.put((byte)(skillsWithoutTraceAndWithTargetOrPosition[i].isGuajiFlag()==false?0:1));
				try{
				tmpBytes2 = skillsWithoutTraceAndWithTargetOrPosition[i].getBuffName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(skillWithoutTraceAndWithSummonNPC.length);
			for(int i = 0 ; i < skillWithoutTraceAndWithSummonNPC.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = skillWithoutTraceAndWithSummonNPC[i].getStyle2().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillWithoutTraceAndWithSummonNPC[i].getEffectiveTimes().length);
				int[] effectiveTimes_0150 = skillWithoutTraceAndWithSummonNPC[i].getEffectiveTimes();
				for(int j = 0 ; j < effectiveTimes_0150.length ; j++){
					buffer.putInt(effectiveTimes_0150[j]);
				}
				buffer.put((byte)skillWithoutTraceAndWithSummonNPC[i].getEnableWeaponType());
				buffer.put((byte)skillWithoutTraceAndWithSummonNPC[i].getWeaponTypeLimit());
				buffer.put((byte)skillWithoutTraceAndWithSummonNPC[i].getFollowByCommonAttack());
				buffer.putInt((int)skillWithoutTraceAndWithSummonNPC[i].getId());
				buffer.putInt((int)skillWithoutTraceAndWithSummonNPC[i].getDuration1());
				buffer.putInt((int)skillWithoutTraceAndWithSummonNPC[i].getDuration2());
				buffer.putInt((int)skillWithoutTraceAndWithSummonNPC[i].getDuration3());
				try{
				tmpBytes2 = skillWithoutTraceAndWithSummonNPC[i].getStyle1().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(skillWithoutTraceAndWithSummonNPC[i].isIgnoreStun()==false?0:1));
				buffer.put((byte)skillWithoutTraceAndWithSummonNPC[i].getMatrixType());
				buffer.putInt(skillWithoutTraceAndWithSummonNPC[i].getAttackNum().length);
				short[] attackNum_0151 = skillWithoutTraceAndWithSummonNPC[i].getAttackNum();
				for(int j = 0 ; j < attackNum_0151.length ; j++){
					buffer.putShort(attackNum_0151[j]);
				}
				buffer.putInt(skillWithoutTraceAndWithSummonNPC[i].getMp().length);
				short[] mp_0152 = skillWithoutTraceAndWithSummonNPC[i].getMp();
				for(int j = 0 ; j < mp_0152.length ; j++){
					buffer.putShort(mp_0152[j]);
				}
				buffer.putInt((int)skillWithoutTraceAndWithSummonNPC[i].getRange());
				buffer.putInt((int)skillWithoutTraceAndWithSummonNPC[i].getGapWidth());
				buffer.putInt((int)skillWithoutTraceAndWithSummonNPC[i].getGapHeight());
				buffer.putLong(skillWithoutTraceAndWithSummonNPC[i].getMaxTimeLength());
				buffer.putShort((short)skillWithoutTraceAndWithSummonNPC[i].getSpeed());
				buffer.putShort((short)skillWithoutTraceAndWithSummonNPC[i].getAngle());
				buffer.putShort((short)skillWithoutTraceAndWithSummonNPC[i].getHeigth());
				buffer.putShort((short)skillWithoutTraceAndWithSummonNPC[i].getMaxParticleEachTime());
				try{
				tmpBytes2 = skillWithoutTraceAndWithSummonNPC[i].getFlySound().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillWithoutTraceAndWithSummonNPC[i].getExplodeSound().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillWithoutTraceAndWithSummonNPC[i].getFlyParticle().length);
				String[] flyParticle_0153 = skillWithoutTraceAndWithSummonNPC[i].getFlyParticle();
				for(int j = 0 ; j < flyParticle_0153.length ; j++){
				try{
					buffer.putShort((short)flyParticle_0153[j].getBytes("UTF-8").length);
					buffer.put(flyParticle_0153[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(skillWithoutTraceAndWithSummonNPC[i].getExplodeParticle().length);
				String[] explodeParticle_0154 = skillWithoutTraceAndWithSummonNPC[i].getExplodeParticle();
				for(int j = 0 ; j < explodeParticle_0154.length ; j++){
				try{
					buffer.putShort((short)explodeParticle_0154[j].getBytes("UTF-8").length);
					buffer.put(explodeParticle_0154[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putShort((short)skillWithoutTraceAndWithSummonNPC[i].getExplodePercent());
				buffer.putInt(skillWithoutTraceAndWithSummonNPC[i].getBodyParticle().length);
				String[] bodyParticle_0155 = skillWithoutTraceAndWithSummonNPC[i].getBodyParticle();
				for(int j = 0 ; j < bodyParticle_0155.length ; j++){
				try{
					buffer.putShort((short)bodyParticle_0155[j].getBytes("UTF-8").length);
					buffer.put(bodyParticle_0155[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putShort((short)skillWithoutTraceAndWithSummonNPC[i].getBodyPercent());
				buffer.putInt(skillWithoutTraceAndWithSummonNPC[i].getBodyParticleOffset().length);
				short[] bodyParticleOffset_0156 = skillWithoutTraceAndWithSummonNPC[i].getBodyParticleOffset();
				for(int j = 0 ; j < bodyParticleOffset_0156.length ; j++){
					buffer.putShort(bodyParticleOffset_0156[j]);
				}
				buffer.putLong(skillWithoutTraceAndWithSummonNPC[i].getBodyParticlePlayStart());
				buffer.putLong(skillWithoutTraceAndWithSummonNPC[i].getBodyParticlePlayEnd());
				try{
				tmpBytes2 = skillWithoutTraceAndWithSummonNPC[i].getBodyPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillWithoutTraceAndWithSummonNPC[i].getBodyPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)skillWithoutTraceAndWithSummonNPC[i].getBodyPartAnimationPercent());
				buffer.putInt(skillWithoutTraceAndWithSummonNPC[i].getBodyPartAnimationOffset().length);
				short[] bodyPartAnimationOffset_0157 = skillWithoutTraceAndWithSummonNPC[i].getBodyPartAnimationOffset();
				for(int j = 0 ; j < bodyPartAnimationOffset_0157.length ; j++){
					buffer.putShort(bodyPartAnimationOffset_0157[j]);
				}
				buffer.putLong(skillWithoutTraceAndWithSummonNPC[i].getBodyPartAnimationPlayStart());
				buffer.putLong(skillWithoutTraceAndWithSummonNPC[i].getBodyPartAnimationPlayEnd());
				buffer.putInt(skillWithoutTraceAndWithSummonNPC[i].getFootParticle().length);
				String[] footParticle_0158 = skillWithoutTraceAndWithSummonNPC[i].getFootParticle();
				for(int j = 0 ; j < footParticle_0158.length ; j++){
				try{
					buffer.putShort((short)footParticle_0158[j].getBytes("UTF-8").length);
					buffer.put(footParticle_0158[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(skillWithoutTraceAndWithSummonNPC[i].getFootParticleOffset().length);
				short[] footParticleOffset_0159 = skillWithoutTraceAndWithSummonNPC[i].getFootParticleOffset();
				for(int j = 0 ; j < footParticleOffset_0159.length ; j++){
					buffer.putShort(footParticleOffset_0159[j]);
				}
				buffer.putLong(skillWithoutTraceAndWithSummonNPC[i].getFootParticlePlayStart());
				buffer.putLong(skillWithoutTraceAndWithSummonNPC[i].getFootParticlePlayEnd());
				try{
				tmpBytes2 = skillWithoutTraceAndWithSummonNPC[i].getFootPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillWithoutTraceAndWithSummonNPC[i].getFootPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillWithoutTraceAndWithSummonNPC[i].getFootPartAnimationOffset().length);
				short[] footPartAnimationOffset_0160 = skillWithoutTraceAndWithSummonNPC[i].getFootPartAnimationOffset();
				for(int j = 0 ; j < footPartAnimationOffset_0160.length ; j++){
					buffer.putShort(footPartAnimationOffset_0160[j]);
				}
				buffer.putLong(skillWithoutTraceAndWithSummonNPC[i].getFootPartAnimationPlayStart());
				buffer.putLong(skillWithoutTraceAndWithSummonNPC[i].getFootPartAnimationPlayEnd());
				buffer.putShort((short)skillWithoutTraceAndWithSummonNPC[i].getAngle());
				buffer.putInt(skillWithoutTraceAndWithSummonNPC[i].getTargetFootParticle().length);
				String[] targetFootParticle_0161 = skillWithoutTraceAndWithSummonNPC[i].getTargetFootParticle();
				for(int j = 0 ; j < targetFootParticle_0161.length ; j++){
				try{
					buffer.putShort((short)targetFootParticle_0161[j].getBytes("UTF-8").length);
					buffer.put(targetFootParticle_0161[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.put((byte)(skillWithoutTraceAndWithSummonNPC[i].isGuajiFlag()==false?0:1));
				try{
				tmpBytes2 = skillWithoutTraceAndWithSummonNPC[i].getBuffName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(skillsWithTraceAndDirectionOrTarget.length);
			for(int i = 0 ; i < skillsWithTraceAndDirectionOrTarget.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = skillsWithTraceAndDirectionOrTarget[i].getStyle2().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillsWithTraceAndDirectionOrTarget[i].getEffectiveTimes().length);
				int[] effectiveTimes_0162 = skillsWithTraceAndDirectionOrTarget[i].getEffectiveTimes();
				for(int j = 0 ; j < effectiveTimes_0162.length ; j++){
					buffer.putInt(effectiveTimes_0162[j]);
				}
				buffer.put((byte)skillsWithTraceAndDirectionOrTarget[i].getEnableWeaponType());
				buffer.put((byte)skillsWithTraceAndDirectionOrTarget[i].getWeaponTypeLimit());
				buffer.putInt((int)skillsWithTraceAndDirectionOrTarget[i].getId());
				buffer.putInt((int)skillsWithTraceAndDirectionOrTarget[i].getDuration1());
				buffer.putInt((int)skillsWithTraceAndDirectionOrTarget[i].getDuration2());
				buffer.putInt((int)skillsWithTraceAndDirectionOrTarget[i].getDuration3());
				try{
				tmpBytes2 = skillsWithTraceAndDirectionOrTarget[i].getStyle1().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)skillsWithTraceAndDirectionOrTarget[i].getFollowByCommonAttack());
				buffer.put((byte)(skillsWithTraceAndDirectionOrTarget[i].isIgnoreStun()==false?0:1));
				buffer.put((byte)skillsWithTraceAndDirectionOrTarget[i].getTargetType());
				try{
				tmpBytes2 = skillsWithTraceAndDirectionOrTarget[i].getEffectType().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithTraceAndDirectionOrTarget[i].getAvataRace().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithTraceAndDirectionOrTarget[i].getAvataSex().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)skillsWithTraceAndDirectionOrTarget[i].getTrackType());
				buffer.putInt((int)skillsWithTraceAndDirectionOrTarget[i].getSpeed());
				buffer.putInt((int)skillsWithTraceAndDirectionOrTarget[i].getRange());
				buffer.putInt((int)skillsWithTraceAndDirectionOrTarget[i].getAttackWidth());
				buffer.putInt((int)skillsWithTraceAndDirectionOrTarget[i].getExplosionLastingTime());
				buffer.putInt((int)skillsWithTraceAndDirectionOrTarget[i].getEffectNum());
				buffer.putInt(skillsWithTraceAndDirectionOrTarget[i].getEffectInitPositionX().length);
				int[] effectInitPositionX_0163 = skillsWithTraceAndDirectionOrTarget[i].getEffectInitPositionX();
				for(int j = 0 ; j < effectInitPositionX_0163.length ; j++){
					buffer.putInt(effectInitPositionX_0163[j]);
				}
				buffer.putInt(skillsWithTraceAndDirectionOrTarget[i].getEffectInitPositionY().length);
				int[] effectInitPositionY_0164 = skillsWithTraceAndDirectionOrTarget[i].getEffectInitPositionY();
				for(int j = 0 ; j < effectInitPositionY_0164.length ; j++){
					buffer.putInt(effectInitPositionY_0164[j]);
				}
				buffer.putInt(skillsWithTraceAndDirectionOrTarget[i].getEffectInitDirection().length);
				buffer.put(skillsWithTraceAndDirectionOrTarget[i].getEffectInitDirection());
				buffer.putInt((int)skillsWithTraceAndDirectionOrTarget[i].getPenetrateNum());
				buffer.putInt(skillsWithTraceAndDirectionOrTarget[i].getMp().length);
				short[] mp_0165 = skillsWithTraceAndDirectionOrTarget[i].getMp();
				for(int j = 0 ; j < mp_0165.length ; j++){
					buffer.putShort(mp_0165[j]);
				}
				try{
				tmpBytes2 = skillsWithTraceAndDirectionOrTarget[i].getFlySound().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithTraceAndDirectionOrTarget[i].getExplodeSound().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillsWithTraceAndDirectionOrTarget[i].getFlyParticle().length);
				String[] flyParticle_0166 = skillsWithTraceAndDirectionOrTarget[i].getFlyParticle();
				for(int j = 0 ; j < flyParticle_0166.length ; j++){
				try{
					buffer.putShort((short)flyParticle_0166[j].getBytes("UTF-8").length);
					buffer.put(flyParticle_0166[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(skillsWithTraceAndDirectionOrTarget[i].getExplodeParticle().length);
				String[] explodeParticle_0167 = skillsWithTraceAndDirectionOrTarget[i].getExplodeParticle();
				for(int j = 0 ; j < explodeParticle_0167.length ; j++){
				try{
					buffer.putShort((short)explodeParticle_0167[j].getBytes("UTF-8").length);
					buffer.put(explodeParticle_0167[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putShort((short)skillsWithTraceAndDirectionOrTarget[i].getExplodePercent());
				buffer.putInt(skillsWithTraceAndDirectionOrTarget[i].getBodyParticle().length);
				String[] bodyParticle_0168 = skillsWithTraceAndDirectionOrTarget[i].getBodyParticle();
				for(int j = 0 ; j < bodyParticle_0168.length ; j++){
				try{
					buffer.putShort((short)bodyParticle_0168[j].getBytes("UTF-8").length);
					buffer.put(bodyParticle_0168[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putShort((short)skillsWithTraceAndDirectionOrTarget[i].getBodyPercent());
				buffer.putInt(skillsWithTraceAndDirectionOrTarget[i].getBodyParticleOffset().length);
				short[] bodyParticleOffset_0169 = skillsWithTraceAndDirectionOrTarget[i].getBodyParticleOffset();
				for(int j = 0 ; j < bodyParticleOffset_0169.length ; j++){
					buffer.putShort(bodyParticleOffset_0169[j]);
				}
				buffer.putLong(skillsWithTraceAndDirectionOrTarget[i].getBodyParticlePlayStart());
				buffer.putLong(skillsWithTraceAndDirectionOrTarget[i].getBodyParticlePlayEnd());
				try{
				tmpBytes2 = skillsWithTraceAndDirectionOrTarget[i].getBodyPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithTraceAndDirectionOrTarget[i].getBodyPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)skillsWithTraceAndDirectionOrTarget[i].getBodyPartAnimationPercent());
				buffer.putInt(skillsWithTraceAndDirectionOrTarget[i].getBodyPartAnimationOffset().length);
				short[] bodyPartAnimationOffset_0170 = skillsWithTraceAndDirectionOrTarget[i].getBodyPartAnimationOffset();
				for(int j = 0 ; j < bodyPartAnimationOffset_0170.length ; j++){
					buffer.putShort(bodyPartAnimationOffset_0170[j]);
				}
				buffer.putLong(skillsWithTraceAndDirectionOrTarget[i].getBodyPartAnimationPlayStart());
				buffer.putLong(skillsWithTraceAndDirectionOrTarget[i].getBodyPartAnimationPlayEnd());
				buffer.putInt(skillsWithTraceAndDirectionOrTarget[i].getFootParticle().length);
				String[] footParticle_0171 = skillsWithTraceAndDirectionOrTarget[i].getFootParticle();
				for(int j = 0 ; j < footParticle_0171.length ; j++){
				try{
					buffer.putShort((short)footParticle_0171[j].getBytes("UTF-8").length);
					buffer.put(footParticle_0171[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(skillsWithTraceAndDirectionOrTarget[i].getFootParticleOffset().length);
				short[] footParticleOffset_0172 = skillsWithTraceAndDirectionOrTarget[i].getFootParticleOffset();
				for(int j = 0 ; j < footParticleOffset_0172.length ; j++){
					buffer.putShort(footParticleOffset_0172[j]);
				}
				buffer.putLong(skillsWithTraceAndDirectionOrTarget[i].getFootParticlePlayStart());
				buffer.putLong(skillsWithTraceAndDirectionOrTarget[i].getFootParticlePlayEnd());
				try{
				tmpBytes2 = skillsWithTraceAndDirectionOrTarget[i].getFootPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillsWithTraceAndDirectionOrTarget[i].getFootPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillsWithTraceAndDirectionOrTarget[i].getFootPartAnimationOffset().length);
				short[] footPartAnimationOffset_0173 = skillsWithTraceAndDirectionOrTarget[i].getFootPartAnimationOffset();
				for(int j = 0 ; j < footPartAnimationOffset_0173.length ; j++){
					buffer.putShort(footPartAnimationOffset_0173[j]);
				}
				buffer.putLong(skillsWithTraceAndDirectionOrTarget[i].getFootPartAnimationPlayStart());
				buffer.putLong(skillsWithTraceAndDirectionOrTarget[i].getFootPartAnimationPlayEnd());
				buffer.putShort((short)skillsWithTraceAndDirectionOrTarget[i].getAngle());
				buffer.putInt(skillsWithTraceAndDirectionOrTarget[i].getTargetFootParticle().length);
				String[] targetFootParticle_0174 = skillsWithTraceAndDirectionOrTarget[i].getTargetFootParticle();
				for(int j = 0 ; j < targetFootParticle_0174.length ; j++){
				try{
					buffer.putShort((short)targetFootParticle_0174[j].getBytes("UTF-8").length);
					buffer.put(targetFootParticle_0174[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putLong(skillsWithTraceAndDirectionOrTarget[i].getSendEffectTime());
				buffer.put((byte)(skillsWithTraceAndDirectionOrTarget[i].isGuajiFlag()==false?0:1));
				try{
				tmpBytes2 = skillsWithTraceAndDirectionOrTarget[i].getBuffName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(skillWithoutTraceAndWithMatrix.length);
			for(int i = 0 ; i < skillWithoutTraceAndWithMatrix.length ; i++){
				buffer.putInt((int)skillWithoutTraceAndWithMatrix[i].getId());
				buffer.putInt((int)skillWithoutTraceAndWithMatrix[i].getDuration1());
				buffer.putInt((int)skillWithoutTraceAndWithMatrix[i].getDuration2());
				buffer.putInt((int)skillWithoutTraceAndWithMatrix[i].getDuration3());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = skillWithoutTraceAndWithMatrix[i].getStyle1().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillWithoutTraceAndWithMatrix[i].getStyle2().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillWithoutTraceAndWithMatrix[i].getEffectiveTimes().length);
				int[] effectiveTimes_0175 = skillWithoutTraceAndWithMatrix[i].getEffectiveTimes();
				for(int j = 0 ; j < effectiveTimes_0175.length ; j++){
					buffer.putInt(effectiveTimes_0175[j]);
				}
				buffer.put((byte)skillWithoutTraceAndWithMatrix[i].getEnableWeaponType());
				buffer.put((byte)skillWithoutTraceAndWithMatrix[i].getWeaponTypeLimit());
				buffer.put((byte)skillWithoutTraceAndWithMatrix[i].getFollowByCommonAttack());
				buffer.put((byte)(skillWithoutTraceAndWithMatrix[i].isIgnoreStun()==false?0:1));
				buffer.put((byte)skillWithoutTraceAndWithMatrix[i].getMatrixType());
				buffer.putInt((int)skillWithoutTraceAndWithMatrix[i].getGapWidth());
				buffer.putInt((int)skillWithoutTraceAndWithMatrix[i].getGapHeight());
				buffer.putInt((int)skillWithoutTraceAndWithMatrix[i].getEffectNum());
				try{
				tmpBytes2 = skillWithoutTraceAndWithMatrix[i].getEffectType().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillWithoutTraceAndWithMatrix[i].getAvataRace().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillWithoutTraceAndWithMatrix[i].getAvataSex().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)skillWithoutTraceAndWithMatrix[i].getEffectLastTime());
				buffer.putInt((int)skillWithoutTraceAndWithMatrix[i].getEffectExplosionLastTime());
				buffer.putInt(skillWithoutTraceAndWithMatrix[i].getMp().length);
				short[] mp_0176 = skillWithoutTraceAndWithMatrix[i].getMp();
				for(int j = 0 ; j < mp_0176.length ; j++){
					buffer.putShort(mp_0176[j]);
				}
				buffer.putInt((int)skillWithoutTraceAndWithMatrix[i].getRange());
				try{
				tmpBytes2 = skillWithoutTraceAndWithMatrix[i].getFlySound().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillWithoutTraceAndWithMatrix[i].getExplodeSound().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillWithoutTraceAndWithMatrix[i].getFlyParticle().length);
				String[] flyParticle_0177 = skillWithoutTraceAndWithMatrix[i].getFlyParticle();
				for(int j = 0 ; j < flyParticle_0177.length ; j++){
				try{
					buffer.putShort((short)flyParticle_0177[j].getBytes("UTF-8").length);
					buffer.put(flyParticle_0177[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(skillWithoutTraceAndWithMatrix[i].getExplodeParticle().length);
				String[] explodeParticle_0178 = skillWithoutTraceAndWithMatrix[i].getExplodeParticle();
				for(int j = 0 ; j < explodeParticle_0178.length ; j++){
				try{
					buffer.putShort((short)explodeParticle_0178[j].getBytes("UTF-8").length);
					buffer.put(explodeParticle_0178[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putShort((short)skillWithoutTraceAndWithMatrix[i].getExplodePercent());
				buffer.putInt(skillWithoutTraceAndWithMatrix[i].getBodyParticle().length);
				String[] bodyParticle_0179 = skillWithoutTraceAndWithMatrix[i].getBodyParticle();
				for(int j = 0 ; j < bodyParticle_0179.length ; j++){
				try{
					buffer.putShort((short)bodyParticle_0179[j].getBytes("UTF-8").length);
					buffer.put(bodyParticle_0179[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putShort((short)skillWithoutTraceAndWithMatrix[i].getBodyPercent());
				buffer.putInt(skillWithoutTraceAndWithMatrix[i].getBodyParticleOffset().length);
				short[] bodyParticleOffset_0180 = skillWithoutTraceAndWithMatrix[i].getBodyParticleOffset();
				for(int j = 0 ; j < bodyParticleOffset_0180.length ; j++){
					buffer.putShort(bodyParticleOffset_0180[j]);
				}
				buffer.putLong(skillWithoutTraceAndWithMatrix[i].getBodyParticlePlayStart());
				buffer.putLong(skillWithoutTraceAndWithMatrix[i].getBodyParticlePlayEnd());
				try{
				tmpBytes2 = skillWithoutTraceAndWithMatrix[i].getBodyPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillWithoutTraceAndWithMatrix[i].getBodyPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)skillWithoutTraceAndWithMatrix[i].getBodyPartAnimationPercent());
				buffer.putInt(skillWithoutTraceAndWithMatrix[i].getBodyPartAnimationOffset().length);
				short[] bodyPartAnimationOffset_0181 = skillWithoutTraceAndWithMatrix[i].getBodyPartAnimationOffset();
				for(int j = 0 ; j < bodyPartAnimationOffset_0181.length ; j++){
					buffer.putShort(bodyPartAnimationOffset_0181[j]);
				}
				buffer.putLong(skillWithoutTraceAndWithMatrix[i].getBodyPartAnimationPlayStart());
				buffer.putLong(skillWithoutTraceAndWithMatrix[i].getBodyPartAnimationPlayEnd());
				buffer.putInt(skillWithoutTraceAndWithMatrix[i].getFootParticle().length);
				String[] footParticle_0182 = skillWithoutTraceAndWithMatrix[i].getFootParticle();
				for(int j = 0 ; j < footParticle_0182.length ; j++){
				try{
					buffer.putShort((short)footParticle_0182[j].getBytes("UTF-8").length);
					buffer.put(footParticle_0182[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(skillWithoutTraceAndWithMatrix[i].getFootParticleOffset().length);
				short[] footParticleOffset_0183 = skillWithoutTraceAndWithMatrix[i].getFootParticleOffset();
				for(int j = 0 ; j < footParticleOffset_0183.length ; j++){
					buffer.putShort(footParticleOffset_0183[j]);
				}
				buffer.putLong(skillWithoutTraceAndWithMatrix[i].getFootParticlePlayStart());
				buffer.putLong(skillWithoutTraceAndWithMatrix[i].getFootParticlePlayEnd());
				try{
				tmpBytes2 = skillWithoutTraceAndWithMatrix[i].getFootPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillWithoutTraceAndWithMatrix[i].getFootPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillWithoutTraceAndWithMatrix[i].getFootPartAnimationOffset().length);
				short[] footPartAnimationOffset_0184 = skillWithoutTraceAndWithMatrix[i].getFootPartAnimationOffset();
				for(int j = 0 ; j < footPartAnimationOffset_0184.length ; j++){
					buffer.putShort(footPartAnimationOffset_0184[j]);
				}
				buffer.putLong(skillWithoutTraceAndWithMatrix[i].getFootPartAnimationPlayStart());
				buffer.putLong(skillWithoutTraceAndWithMatrix[i].getFootPartAnimationPlayEnd());
				buffer.putShort((short)skillWithoutTraceAndWithMatrix[i].getAngle());
				buffer.putInt(skillWithoutTraceAndWithMatrix[i].getTargetFootParticle().length);
				String[] targetFootParticle_0185 = skillWithoutTraceAndWithMatrix[i].getTargetFootParticle();
				for(int j = 0 ; j < targetFootParticle_0185.length ; j++){
				try{
					buffer.putShort((short)targetFootParticle_0185[j].getBytes("UTF-8").length);
					buffer.put(targetFootParticle_0185[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.put((byte)(skillWithoutTraceAndWithMatrix[i].isGuajiFlag()==false?0:1));
				try{
				tmpBytes2 = skillWithoutTraceAndWithMatrix[i].getBuffName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillWithoutTraceAndWithMatrix[i].getMaxAttackNums().length);
				short[] maxAttackNums_0186 = skillWithoutTraceAndWithMatrix[i].getMaxAttackNums();
				for(int j = 0 ; j < maxAttackNums_0186.length ; j++){
					buffer.putShort(maxAttackNums_0186[j]);
				}
			}
			buffer.putInt(skillWithoutTraceAndOnTeamMember.length);
			for(int i = 0 ; i < skillWithoutTraceAndOnTeamMember.length ; i++){
				buffer.put((byte)skillWithoutTraceAndOnTeamMember[i].getEnableWeaponType());
				buffer.put((byte)skillWithoutTraceAndOnTeamMember[i].getWeaponTypeLimit());
				buffer.put((byte)skillWithoutTraceAndOnTeamMember[i].getFollowByCommonAttack());
				buffer.put((byte)(skillWithoutTraceAndOnTeamMember[i].isIgnoreStun()==false?0:1));
				buffer.putInt((int)skillWithoutTraceAndOnTeamMember[i].getId());
				buffer.putInt((int)skillWithoutTraceAndOnTeamMember[i].getDuration1());
				buffer.putInt((int)skillWithoutTraceAndOnTeamMember[i].getDuration2());
				buffer.putInt((int)skillWithoutTraceAndOnTeamMember[i].getDuration3());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = skillWithoutTraceAndOnTeamMember[i].getStyle1().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillWithoutTraceAndOnTeamMember[i].getStyle2().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillWithoutTraceAndOnTeamMember[i].getEffectiveTimes().length);
				int[] effectiveTimes_0187 = skillWithoutTraceAndOnTeamMember[i].getEffectiveTimes();
				for(int j = 0 ; j < effectiveTimes_0187.length ; j++){
					buffer.putInt(effectiveTimes_0187[j]);
				}
				buffer.put((byte)skillWithoutTraceAndOnTeamMember[i].getRangeType());
				buffer.putInt((int)skillWithoutTraceAndOnTeamMember[i].getRangeWidth());
				buffer.putInt((int)skillWithoutTraceAndOnTeamMember[i].getRangeHeight());
				try{
				tmpBytes2 = skillWithoutTraceAndOnTeamMember[i].getEffectType().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillWithoutTraceAndOnTeamMember[i].getAvataRace().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillWithoutTraceAndOnTeamMember[i].getAvataSex().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)skillWithoutTraceAndOnTeamMember[i].getEffectLastTime());
				buffer.putInt((int)skillWithoutTraceAndOnTeamMember[i].getEffectExplosionLastTime());
				buffer.putInt(skillWithoutTraceAndOnTeamMember[i].getMp().length);
				short[] mp_0188 = skillWithoutTraceAndOnTeamMember[i].getMp();
				for(int j = 0 ; j < mp_0188.length ; j++){
					buffer.putShort(mp_0188[j]);
				}
				try{
				tmpBytes2 = skillWithoutTraceAndOnTeamMember[i].getFlySound().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillWithoutTraceAndOnTeamMember[i].getExplodeSound().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillWithoutTraceAndOnTeamMember[i].getFlyParticle().length);
				String[] flyParticle_0189 = skillWithoutTraceAndOnTeamMember[i].getFlyParticle();
				for(int j = 0 ; j < flyParticle_0189.length ; j++){
				try{
					buffer.putShort((short)flyParticle_0189[j].getBytes("UTF-8").length);
					buffer.put(flyParticle_0189[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(skillWithoutTraceAndOnTeamMember[i].getExplodeParticle().length);
				String[] explodeParticle_0190 = skillWithoutTraceAndOnTeamMember[i].getExplodeParticle();
				for(int j = 0 ; j < explodeParticle_0190.length ; j++){
				try{
					buffer.putShort((short)explodeParticle_0190[j].getBytes("UTF-8").length);
					buffer.put(explodeParticle_0190[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putShort((short)skillWithoutTraceAndOnTeamMember[i].getExplodePercent());
				buffer.putInt(skillWithoutTraceAndOnTeamMember[i].getBodyParticle().length);
				String[] bodyParticle_0191 = skillWithoutTraceAndOnTeamMember[i].getBodyParticle();
				for(int j = 0 ; j < bodyParticle_0191.length ; j++){
				try{
					buffer.putShort((short)bodyParticle_0191[j].getBytes("UTF-8").length);
					buffer.put(bodyParticle_0191[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putShort((short)skillWithoutTraceAndOnTeamMember[i].getBodyPercent());
				buffer.putInt(skillWithoutTraceAndOnTeamMember[i].getBodyParticleOffset().length);
				short[] bodyParticleOffset_0192 = skillWithoutTraceAndOnTeamMember[i].getBodyParticleOffset();
				for(int j = 0 ; j < bodyParticleOffset_0192.length ; j++){
					buffer.putShort(bodyParticleOffset_0192[j]);
				}
				buffer.putLong(skillWithoutTraceAndOnTeamMember[i].getBodyParticlePlayStart());
				buffer.putLong(skillWithoutTraceAndOnTeamMember[i].getBodyParticlePlayEnd());
				try{
				tmpBytes2 = skillWithoutTraceAndOnTeamMember[i].getBodyPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillWithoutTraceAndOnTeamMember[i].getBodyPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)skillWithoutTraceAndOnTeamMember[i].getBodyPartAnimationPercent());
				buffer.putInt(skillWithoutTraceAndOnTeamMember[i].getBodyPartAnimationOffset().length);
				short[] bodyPartAnimationOffset_0193 = skillWithoutTraceAndOnTeamMember[i].getBodyPartAnimationOffset();
				for(int j = 0 ; j < bodyPartAnimationOffset_0193.length ; j++){
					buffer.putShort(bodyPartAnimationOffset_0193[j]);
				}
				buffer.putLong(skillWithoutTraceAndOnTeamMember[i].getBodyPartAnimationPlayStart());
				buffer.putLong(skillWithoutTraceAndOnTeamMember[i].getBodyPartAnimationPlayEnd());
				buffer.putInt(skillWithoutTraceAndOnTeamMember[i].getFootParticle().length);
				String[] footParticle_0194 = skillWithoutTraceAndOnTeamMember[i].getFootParticle();
				for(int j = 0 ; j < footParticle_0194.length ; j++){
				try{
					buffer.putShort((short)footParticle_0194[j].getBytes("UTF-8").length);
					buffer.put(footParticle_0194[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(skillWithoutTraceAndOnTeamMember[i].getFootParticleOffset().length);
				short[] footParticleOffset_0195 = skillWithoutTraceAndOnTeamMember[i].getFootParticleOffset();
				for(int j = 0 ; j < footParticleOffset_0195.length ; j++){
					buffer.putShort(footParticleOffset_0195[j]);
				}
				buffer.putLong(skillWithoutTraceAndOnTeamMember[i].getFootParticlePlayStart());
				buffer.putLong(skillWithoutTraceAndOnTeamMember[i].getFootParticlePlayEnd());
				try{
				tmpBytes2 = skillWithoutTraceAndOnTeamMember[i].getFootPartPath().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillWithoutTraceAndOnTeamMember[i].getFootPartAnimation().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(skillWithoutTraceAndOnTeamMember[i].getFootPartAnimationOffset().length);
				short[] footPartAnimationOffset_0196 = skillWithoutTraceAndOnTeamMember[i].getFootPartAnimationOffset();
				for(int j = 0 ; j < footPartAnimationOffset_0196.length ; j++){
					buffer.putShort(footPartAnimationOffset_0196[j]);
				}
				buffer.putLong(skillWithoutTraceAndOnTeamMember[i].getFootPartAnimationPlayStart());
				buffer.putLong(skillWithoutTraceAndOnTeamMember[i].getFootPartAnimationPlayEnd());
				buffer.putShort((short)skillWithoutTraceAndOnTeamMember[i].getAngle());
				buffer.putInt(skillWithoutTraceAndOnTeamMember[i].getTargetFootParticle().length);
				String[] targetFootParticle_0197 = skillWithoutTraceAndOnTeamMember[i].getTargetFootParticle();
				for(int j = 0 ; j < targetFootParticle_0197.length ; j++){
				try{
					buffer.putShort((short)targetFootParticle_0197[j].getBytes("UTF-8").length);
					buffer.put(targetFootParticle_0197[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.put((byte)(skillWithoutTraceAndOnTeamMember[i].isGuajiFlag()==false?0:1));
				try{
				tmpBytes2 = skillWithoutTraceAndOnTeamMember[i].getBuffName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
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
	 *	各个武器类型对应的普通攻击的Id
	 */
	public int[] getCommonAttackSkillIdsForWeapon(){
		return commonAttackSkillIdsForWeapon;
	}

	/**
	 * 设置属性：
	 *	各个武器类型对应的普通攻击的Id
	 */
	public void setCommonAttackSkillIdsForWeapon(int[] commonAttackSkillIdsForWeapon){
		this.commonAttackSkillIdsForWeapon = commonAttackSkillIdsForWeapon;
	}

	/**
	 * 获取属性：
	 *	普通攻击
	 */
	public CommonAttackSkill[] getCommonAttackSkills(){
		return commonAttackSkills;
	}

	/**
	 * 设置属性：
	 *	普通攻击
	 */
	public void setCommonAttackSkills(CommonAttackSkill[] commonAttackSkills){
		this.commonAttackSkills = commonAttackSkills;
	}

	/**
	 * 获取属性：
	 *	无后效的攻击
	 */
	public SkillWithoutEffect[] getSkillsWithoutEffect(){
		return skillsWithoutEffect;
	}

	/**
	 * 设置属性：
	 *	无后效的攻击
	 */
	public void setSkillsWithoutEffect(SkillWithoutEffect[] skillsWithoutEffect){
		this.skillsWithoutEffect = skillsWithoutEffect;
	}

	/**
	 * 获取属性：
	 *	普通攻击
	 */
	public SkillWithoutEffectAndQuickMove[] getSkillsWithoutEffectAndQuickMove(){
		return skillsWithoutEffectAndQuickMove;
	}

	/**
	 * 设置属性：
	 *	普通攻击
	 */
	public void setSkillsWithoutEffectAndQuickMove(SkillWithoutEffectAndQuickMove[] skillsWithoutEffectAndQuickMove){
		this.skillsWithoutEffectAndQuickMove = skillsWithoutEffectAndQuickMove;
	}

	/**
	 * 获取属性：
	 *	单个飞镖类技能
	 */
	public SkillWithoutTraceAndWithRange[] getSkillsWithoutTraceAndWithRange(){
		return skillsWithoutTraceAndWithRange;
	}

	/**
	 * 设置属性：
	 *	单个飞镖类技能
	 */
	public void setSkillsWithoutTraceAndWithRange(SkillWithoutTraceAndWithRange[] skillsWithoutTraceAndWithRange){
		this.skillsWithoutTraceAndWithRange = skillsWithoutTraceAndWithRange;
	}

	/**
	 * 获取属性：
	 *	8个飞镖类技能，向8个方向飞行
	 */
	public SkillWithoutTraceAndWithTargetOrPosition[] getSkillsWithoutTraceAndWithTargetOrPosition(){
		return skillsWithoutTraceAndWithTargetOrPosition;
	}

	/**
	 * 设置属性：
	 *	8个飞镖类技能，向8个方向飞行
	 */
	public void setSkillsWithoutTraceAndWithTargetOrPosition(SkillWithoutTraceAndWithTargetOrPosition[] skillsWithoutTraceAndWithTargetOrPosition){
		this.skillsWithoutTraceAndWithTargetOrPosition = skillsWithoutTraceAndWithTargetOrPosition;
	}

	/**
	 * 获取属性：
	 *	召唤npc技能，暴风雪火墙都是这个技能实现
	 */
	public SkillWithoutTraceAndWithSummonNPC[] getSkillWithoutTraceAndWithSummonNPC(){
		return skillWithoutTraceAndWithSummonNPC;
	}

	/**
	 * 设置属性：
	 *	召唤npc技能，暴风雪火墙都是这个技能实现
	 */
	public void setSkillWithoutTraceAndWithSummonNPC(SkillWithoutTraceAndWithSummonNPC[] skillWithoutTraceAndWithSummonNPC){
		this.skillWithoutTraceAndWithSummonNPC = skillWithoutTraceAndWithSummonNPC;
	}

	/**
	 * 获取属性：
	 *	广播区域内所有的人都被闪电击中
	 */
	public SkillWithTraceAndDirectionOrTarget[] getSkillsWithTraceAndDirectionOrTarget(){
		return skillsWithTraceAndDirectionOrTarget;
	}

	/**
	 * 设置属性：
	 *	广播区域内所有的人都被闪电击中
	 */
	public void setSkillsWithTraceAndDirectionOrTarget(SkillWithTraceAndDirectionOrTarget[] skillsWithTraceAndDirectionOrTarget){
		this.skillsWithTraceAndDirectionOrTarget = skillsWithTraceAndDirectionOrTarget;
	}

	/**
	 * 获取属性：
	 *	广播区域内所有的人都被闪电击中
	 */
	public SkillWithoutTraceAndWithMatrix[] getSkillWithoutTraceAndWithMatrix(){
		return skillWithoutTraceAndWithMatrix;
	}

	/**
	 * 设置属性：
	 *	广播区域内所有的人都被闪电击中
	 */
	public void setSkillWithoutTraceAndWithMatrix(SkillWithoutTraceAndWithMatrix[] skillWithoutTraceAndWithMatrix){
		this.skillWithoutTraceAndWithMatrix = skillWithoutTraceAndWithMatrix;
	}

	/**
	 * 获取属性：
	 *	广播区域内所有的人都被闪电击中
	 */
	public SkillWithoutTraceAndOnTeamMember[] getSkillWithoutTraceAndOnTeamMember(){
		return skillWithoutTraceAndOnTeamMember;
	}

	/**
	 * 设置属性：
	 *	广播区域内所有的人都被闪电击中
	 */
	public void setSkillWithoutTraceAndOnTeamMember(SkillWithoutTraceAndOnTeamMember[] skillWithoutTraceAndOnTeamMember){
		this.skillWithoutTraceAndOnTeamMember = skillWithoutTraceAndOnTeamMember;
	}

}
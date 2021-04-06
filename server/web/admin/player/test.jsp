
<%@page import="com.fy.engineserver.message.MIESHI_RESOURCE_FILE2_REQ"%>
<%@page import="com.fy.engineserver.message.MIESHI_RESOURCE_FILE1_REQ"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="com.fy.engineserver.message.MIESHI_GET_RESOURCE_RES"%>
<%@page import="java.io.IOException"%>
<%@page import="com.fy.engineserver.message.MIESHI_SKILL_INFO_REQ"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.nio.MappedByteBuffer"%>
<%@page import="java.nio.channels.FileChannel"%>
<%@page import="java.io.File"%>
<%@page import="java.io.RandomAccessFile"%>
<%@page import="java.nio.ByteBuffer"%>
<%@page import="com.fy.engineserver.datasource.skill.ActiveSkillEntity"%>
<%@page import="com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndOnTeamMember"%>
<%@page import="com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithRange"%>
<%@page import="com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffectAndQuickMove"%>
<%@page import="com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffect"%>
<%@page import="com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithMatrix"%>
<%@page import="com.fy.engineserver.datasource.skill.activeskills.SkillWithTraceAndDirectionOrTarget"%>
<%@page import="com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithSummonNPC"%>
<%@page import="com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithTargetOrPosition"%>
<%@page import="com.fy.engineserver.message.SEND_ACTIVESKILL_REQ"%>
<%@page import="com.fy.engineserver.datasource.skill.activeskills.CommonAttackSkill"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Weapon"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%@page import="com.fy.engineserver.gateway.MieshiGatewayClientService"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.message.MIESHI_RESOURCE_FILE_REQ"%><%@ page contentType="text/html;charset=gbk" import="java.util.*,com.xuanzhi.tools.text.*,java.lang.reflect.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.google.gson.*,com.xuanzhi.boss.game.*"%><% 

%><%!
int LITTLE_PACKAGE_LENGTH = 32*1024;
/**
 * 发送资源数据给客户端
 * 第一个发送的包内容为 文件名，文件版本，文件大小，以及文件被拆分成多少个小包发送
 * 第二批发送的包为资源数据包，分多个小包发送。小包内容为 文件名，偏移量，数据
 * @param conn
 * @param fileName
 * @param clientMachineType
 * @param gpu
 * @throws IOException 
 * @throws WindowFullException 
 * @throws SendBufferFullException 
 * @throws IllegalArgumentException 
 * @throws IllegalStateException 
 */
public void sendResourceData(MieshiGatewayClientService mgs, String fileName, String fileVersion, File file) throws IllegalStateException, IllegalArgumentException, SendBufferFullException, WindowFullException, IOException{
	int length = 0;
	long count = 0;
	if(file != null && file.isFile()){
		length = (int)file.length();
		if(length <= LITTLE_PACKAGE_LENGTH){
			count = 1;
		}else{
			count = length/LITTLE_PACKAGE_LENGTH;
			if(length % LITTLE_PACKAGE_LENGTH != 0){
				count += 1;
			}
		}
	}
	MIESHI_RESOURCE_FILE1_REQ res = new MIESHI_RESOURCE_FILE1_REQ(GameMessageFactory.nextSequnceNum(), fileName, fileVersion, length, count);
	mgs.sendMessageToGateway(res);
	if(length > 0){
		byte[] bs = new byte[LITTLE_PACKAGE_LENGTH];
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(file);
			for(int i = 0; i < count; i++){
				if(i == count - 1){
					bs = new byte[length - i*LITTLE_PACKAGE_LENGTH];
				}
				fis.read(bs);
				MIESHI_RESOURCE_FILE2_REQ res2 = new MIESHI_RESOURCE_FILE2_REQ(GameMessageFactory.nextSequnceNum(), fileName, i*LITTLE_PACKAGE_LENGTH, bs);
				mgs.sendMessageToGateway(res2);
			}
		}catch(Exception ex){
			
		}finally{
			if(fis != null){
				fis.close();
			}
		}
	}
}

%>
<%
//测试

try {
	
	CareerManager cm = CareerManager.getInstance();
	int ids[] = new int[Weapon.WEAPONTYPE_NAME.length];
	for (int i = 0; i < ids.length; i++) {
		CommonAttackSkill skill = cm.getCommonAttackSkill((byte) i);
		if (skill != null) {
			ids[i] = skill.getId();
		}
	}

	SEND_ACTIVESKILL_REQ res = new SEND_ACTIVESKILL_REQ(0, ids, (CommonAttackSkill[]) cm.getSkillsByClass(CommonAttackSkill.class), (SkillWithoutEffect[]) cm.getSkillsByClass(SkillWithoutEffect.class), (SkillWithoutEffectAndQuickMove[]) cm.getSkillsByClass(SkillWithoutEffectAndQuickMove.class), (SkillWithoutTraceAndWithRange[]) cm.getSkillsByClass(SkillWithoutTraceAndWithRange.class),
	(SkillWithoutTraceAndWithTargetOrPosition[]) cm.getSkillsByClass(SkillWithoutTraceAndWithTargetOrPosition.class),
	(SkillWithoutTraceAndWithSummonNPC[]) cm.getSkillsByClass(SkillWithoutTraceAndWithSummonNPC.class), (SkillWithTraceAndDirectionOrTarget[]) cm.getSkillsByClass(SkillWithTraceAndDirectionOrTarget.class), (SkillWithoutTraceAndWithMatrix[]) cm.getSkillsByClass(SkillWithoutTraceAndWithMatrix.class),
	(SkillWithoutTraceAndOnTeamMember[]) cm.getSkillsByClass(SkillWithoutTraceAndOnTeamMember.class));
	
	ByteBuffer b = ByteBuffer.allocate(res.getLength());
	res.writeTo(b);
	b.flip();
	byte[] bs = new byte[b.limit()];
	b.get(bs);
	String fileName = "skillinfo.txt";
	File file = new File("/home/jyt/resin_server/webapps/game_server/"+fileName);
	FileOutputStream fos = new FileOutputStream(file);
	fos.write(bs);
	fos.flush();
	fos.close();
	MIESHI_SKILL_INFO_REQ info = new MIESHI_SKILL_INFO_REQ(GameMessageFactory.nextSequnceNum(),bs);
	MieshiGatewayClientService.getInstance().sendMessageToGateway(info);
	out.println("已经发送消息，请查看日志");
} catch (Exception e) {
	// TODO Auto-generated catch block
	throw e;
}
%>


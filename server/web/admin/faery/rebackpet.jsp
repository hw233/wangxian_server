<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.homestead.cave.PetHookInfo"%>
<%@page import="com.fy.engineserver.homestead.faery.service.FaeryConfig"%>
<%@page import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	long caveId = 1102000000000020620L;
	Cave cave = FaeryManager.caveEm.find(caveId);
	if (cave != null) {
		if (cave.getStatus() == FaeryConfig.CAVE_STATUS_KHATAM) {
			PetHookInfo [] phs =  cave.getPethouse().getHookInfos();
			
			if (phs != null) {
				for (int i = 0; i < phs.length ;i++) {
					PetHookInfo ph = phs[i];
					if (ph != null) {
						out.print(ph.toString());
						cave.getPethouse().getHookInfos()[i] = null;
						FaeryManager.caveEm.flush(cave);
						/**
						Pet pet = PetManager.getInstance().getPet(ph.getPetId());
						if (pet != null) {
							if (pet.getHookInfo() != null) {
								pet.setHookInfo(null);
								cave.getPethouse().getHookInfos()[i] = null;
								FaeryManager.caveEm.notifyFieldChange(cave,"hookInfos");
								FaeryManager.caveEm.save(cave);
								out.print("退还宠物成功:主人:" + pet.getOwnerName() + ",宠物名字:" + pet.getName() + ",颜色:" + pet.getNameColor());
							} else {
								out.print("PET没有在挂机:" + ph.getPetId());
							}
						} else {
							out.print("PET不存在,id:" + ph.getPetId());
						}
						*/
					}
				}
			}
		} else {
			out.print("仙府没有在封印");
		}
	}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>
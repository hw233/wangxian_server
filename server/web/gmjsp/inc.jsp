<%@ page import="com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType"%>
<%@ page import="com.fy.engineserver.septbuilding.templet.*"%>
<%@ page import="com.fy.engineserver.septbuilding.service.SeptBuildingManager"%>
<%@ page import="com.fy.engineserver.septbuilding.entity.*"%>
<%@ page import="com.fy.engineserver.septstation.*"%>
<%@ page import="com.fy.engineserver.septstation.service.*"%>
<%@ page import="com.xuanzhi.tools.cache.CacheObject" %>
<%@ page import="com.fy.engineserver.sept.exception.*" %>
<%@ page import="java.util.*"%>
<%!
	Comparator<SeptBuildingEntity> order = new Comparator<SeptBuildingEntity> () {
		public int compare(SeptBuildingEntity o1, SeptBuildingEntity o2) {
			int o1Type = o1.getBuildingState().getTempletType().getBuildingType().getType();
			int o2Type = o2.getBuildingState().getTempletType().getBuildingType().getType();
			
			if (o1Type == o2Type) {
				return o2.getBuildingState().getLevel() - o1.getBuildingState().getLevel();	
			}
			return o2Type - o1Type; 
		}
	};
 %>
 <%!
 	String baseColor = "#3EE17B";
 	String otherColor = "#A5EDDE";
  %>

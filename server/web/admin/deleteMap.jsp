<%@page import="java.io.FileFilter"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>删除地图资源</title>
</head>
<body>
	<%
		File ff = GameManager.getInstance().getConfigFile();
		if (ff != null) {
			out.println(ff.getPath() + "<br>");
			FileFilter gamemapFilter = new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.getAbsolutePath().endsWith(".xmd");
				}
			};
			File dir = new File(ff.getPath() + File.separator + "map/lowMap");
			out.println(dir.getPath() + "<br>");
			File fs[] = dir.listFiles(gamemapFilter);
			out.println(fs.length + "<br>");
			if (fs == null) return;
			for (File f : fs) {
				if (f.getPath().indexOf("lingxiaowangcheng") >= 0 || f.getPath().indexOf("taihuazhidingA") >= 0) {
					f.delete();
					out.print(f.getPath() + "删除成功<br>");
				}
			}
		}
		ff = new File("/home/game/deploy/resin_server/webapps/game_server/WEB-INF/game_init_config/bindata/map/lowMap");
		if (ff.exists()) {
			FileFilter gamemapFilter = new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.getAbsolutePath().endsWith(".xmd");
				}
			};
			File dir = ff;
			out.println(dir.getPath() + "<br>");
			File fs[] = dir.listFiles(gamemapFilter);
			out.println(fs.length + "<br>");
			if (fs == null) return;
			for (File f : fs) {
				if (f.getPath().indexOf("lingxiaowangcheng") >= 0 || f.getPath().indexOf("taihuazhidingA") >= 0) {
					f.delete();
					out.print(f.getPath() + "删除成功<br>");
				}
			}
		} else {
			out.print("1不存在");
		}
		ff = new File("/home/game/deploy/resin_server_2/webapps/game_server/WEB-INF/game_init_config/bindata/map/lowMap");
		if (ff.exists()) {
			FileFilter gamemapFilter = new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.getAbsolutePath().endsWith(".xmd");
				}
			};
			File dir = ff;
			out.println(dir.getPath() + "<br>");
			File fs[] = dir.listFiles(gamemapFilter);
			out.println(fs.length + "<br>");
			if (fs == null) return;
			for (File f : fs) {
				if (f.getPath().indexOf("lingxiaowangcheng") >= 0 || f.getPath().indexOf("taihuazhidingA") >= 0) {
					f.delete();
					out.print(f.getPath() + "删除成功<br>");
				}
			}
		} else {
			out.print("2不存在");
		}
		ff = new File("/home/game/deploy/resin_server_3/webapps/game_server/WEB-INF/game_init_config/bindata/map/lowMap");
		if (ff.exists()) {
			FileFilter gamemapFilter = new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.getAbsolutePath().endsWith(".xmd");
				}
			};
			File dir = ff;
			out.println(dir.getPath() + "<br>");
			File fs[] = dir.listFiles(gamemapFilter);
			out.println(fs.length + "<br>");
			if (fs == null) return;
			for (File f : fs) {
				if (f.getPath().indexOf("lingxiaowangcheng") >= 0 || f.getPath().indexOf("taihuazhidingA") >= 0) {
					f.delete();
					out.print(f.getPath() + "删除成功<br>");
				}
			}
		} else {
			out.print("3不存在");
		}
	%>
</body>
</html>
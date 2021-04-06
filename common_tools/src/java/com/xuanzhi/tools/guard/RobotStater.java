package com.xuanzhi.tools.guard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.parser.JSONParser;
import com.xuanzhi.tools.guard.RobotGroupPOJO;
/**
 *
 */
public class RobotStater extends HttpServlet {
	
	public static Logger logger = LoggerFactory.getLogger(RobotStater.class);
	
	public static HashMap<String, RobotGroup> groupMap = new HashMap<String,RobotGroup>();

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		InputStream in = request.getInputStream();
		String json = readFromStream(in);
		if(json != null) {
			//反序列化
			JSONParser parser = new JSONParser(new StringReader(json));  
	        try {
				RobotGroupPOJO rgPOJO = (RobotGroupPOJO) JSONMapper.toJava(parser.nextValue(), RobotGroupPOJO.class);
				RobotGroup rg = fromPOJO(rgPOJO);
				groupMap.put(rg.getName(), rg);
				logger.debug("[得到报告] ["+rg.getName()+"] ["+rg.getRobots().size()+"]");
			} catch (TokenStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RecognitionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MapperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}
	}
	
	private String readFromStream(InputStream in)  {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte buffer[] = new byte[512];
			int n=0; 
			while((n = in.read(buffer)) > 0) {
				out.write(buffer, 0, n);
			}
			in.close();
			out.close();
			return out.toString();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	
	private RobotGroup fromPOJO(RobotGroupPOJO pojo) {
		RobotGroup rg = new RobotGroup();
		rg.setLastRefreshTime(pojo.getLastRefreshTime());
		rg.setName(pojo.getName());
		List<Robot> rs = new ArrayList<Robot>(pojo.getRobots().size());
		for(RobotPOJO rp : pojo.getRobots()) {
			rs.add(rp);
		}
		rg.setRobots(rs);
		return rg;
	}
	
}

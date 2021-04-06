package com.fy.boss.gm;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.gm.Module;
import com.fy.boss.gm.XmlRole;
import com.fy.boss.gm.XmlRoleManager;
import com.fy.boss.gm.XmlRoleUtil;

public class XmlRoleManager {
	protected static Logger logger = Logger
			.getLogger("com.xuanzhi.boss.gmxml.XmlroleManager");
	protected String roleConfFile;
	private List<XmlRole> roles = new ArrayList<XmlRole>();
	private List<String> roleids = new ArrayList<String>();
	private static XmlRoleManager self;

	public static XmlRoleManager getInstance() {
		return self;
	}

	public void initialize() {
		// ......
		long now = System.currentTimeMillis();
		try {
			roles = XmlRoleUtil.loadPage(roleConfFile);
			for (XmlRole r : roles)
				roleids.add(r.getId());
			self = this;
			System.out.println(this.getClass().getName()
					+ " initialize successfully ["
					+ (System.currentTimeMillis() - now) + "]");
			logger.info(this.getClass().getName()
					+ " initialize successfully ["
					+ (System.currentTimeMillis() - now) + "][saveFile:"
					+ roleConfFile + "]");
		} catch (Exception e) {
			logger.info(this.getClass().getName()
					+ " initialize fail [saveFile:" + roleConfFile + "]"
					+ e.getMessage());
		}
	}

	public List<String> getXmoduleIds(String roleId) {
		List<String> results = new ArrayList<String>();
		XmlRole role = getRole(roleId);
		List<Module> moduls = role.getModules();
		for (Module m : moduls) {
			results.add(m.getId());
		}
		return results;
	}

	public XmlRole getRole(String id) {
		XmlRole role = new XmlRole();
		for (XmlRole ro : roles) {
			if (id.equals(ro.getId())) {
				role = ro;
				break;
			}
		}
		return role;
	}

	public boolean serverIsIn(String id, XmlRole role) {
		boolean result = false;
		for (String se : role.getServers()) {
			if (id.equals(se)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public boolean moduleIsIn(String id, XmlRole role) {
		boolean result = false;
		for (Module mo : role.getModules()) {
			if (id.equals(mo.getId())) {
				result = true;
				break;
			}
		}
		return result;
	}

	public String modulePermission(String id, XmlRole role) {
		String result = null;
		for (Module mo : role.getModules()) {
			if (id.equals(mo.getId())) {
				result = mo.getPermission();
				break;
			}
		}
		return result;
	}

	public void save() {
		try {
			XmlRoleUtil.savePage(roles, roleConfFile);
			logger.info(this.getClass().getName() + " save success [saveFile:"
					+ roleConfFile + "]");
		} catch (Exception e) {
			logger.info(this.getClass().getName() + " save fail [saveFile:"
					+ roleConfFile + "]");
		}
	}

	public void insert(XmlRole role, int index) {
		try {
			role.setId(idGenerator());
			roles.add(index, role);
			roleids.add(role.getId());
			save();
			logger.info("role :[" + role.getName() + "] add success at "
					+ new java.util.Date());
		} catch (Exception e) {
			logger.info("role :[" + role.getName() + "] add fail at "
					+ new java.util.Date());
		}
	}

	public void update(XmlRole role) {
		try {
			int index = 0;
			for (int i = 0; i < roles.size(); i++) {
				if (roles.get(i).getId().equals(role.getId())) {
					index = i;
					break;
				}
			}
			roles.set(index, role);
			save();
			logger.info("role :[" + role.getName() + "] update success at "
					+ new java.util.Date());
		} catch (Exception e) {
			logger.info("role :[" + role.getName() + ":] update fail at "
					+ new java.util.Date());
		}
	}

	public void delete(String id) {
		try {
			for (int i = 0; i < roles.size(); i++) {
				XmlRole role = roles.get(i);
				if (id.equals(role.getId())) {
					roles.remove(role);
					save();
				}
			}
			logger.info("role :[" + id + "] delete success at "
					+ new java.util.Date());
		} catch (Exception e) {
			logger.info("role :[" + id + "] delete fail at "
					+ new java.util.Date());
		}
	}

	public String idGenerator() {
		long id = 1000;
		for (XmlRole role : roles) {
			long r = Long.parseLong(role.getId());
			if (r >= id)
				id = r + 1;
		}
		return id + "";
	}

	public String getRoleConfFile() {
		return roleConfFile;
	}

	public void setRoleConfFile(String roleConfFile) {
		this.roleConfFile = roleConfFile;
	}

	public List<XmlRole> getRoles() {
		return roles;
	}

	public void setRoles(List<XmlRole> roles) {
		this.roles = roles;
	}

	public List<String> getRoleids() {
		return roleids;
	}

	public void setRoleids(List<String> roleids) {
		this.roleids = roleids;
	}

}

package com.fy.boss.game.model;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;


@SimpleEntity(name="Server")
public class Server  implements java.io.Serializable {
	
    // Fields
	 @SimpleId
     private long id;
	 @SimpleVersion
	 private int version;
     private String name;
     private String gameipaddr;
     private int gameport;
     private String savingNotifyUrl;
     private String dburi;
     private String dbusername;
     private String dbpassword;
     private String serverid;
     private String resinhome = "/home/game/resin";
     private boolean dirty;
     private String clientid;
     private String jiguiname;

    // Constructors

    /** default constructor */
    public Server() {
    	
    }
   
    // Property accessors

    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getGameipaddr() {
        return this.gameipaddr;
    }
    
    public void setGameipaddr(String gameipaddr) {
        this.gameipaddr = gameipaddr;
    }

    public int getGameport() {
        return this.gameport;
    }
    
    public void setGameport(int gameport) {
        this.gameport = gameport;
    }
	
	public String getSavingNotifyUrl() {
		return savingNotifyUrl;
	}

	public void setSavingNotifyUrl(String savingNotifyUrl) {
		this.savingNotifyUrl = savingNotifyUrl;
	}

	public String getDburi() {
        return this.dburi;
    }
    
    public void setDburi(String dburi) {
        this.dburi = dburi;
    }

    public String getDbusername() {
        return this.dbusername;
    }
    
    public void setDbusername(String dbusername) {
        this.dbusername = dbusername;
    }

    public String getDbpassword() {
        return this.dbpassword;
    }
    
    public void setDbpassword(String dbpassword) {
        this.dbpassword = dbpassword;
    }

	public String getServerid() {
		return serverid;
	}

	public void setServerid(String serverid) {
		this.serverid = serverid;
	}

	public String getResinhome() {
		return resinhome;
	}

	public void setResinhome(String resinhome) {
		this.resinhome = resinhome;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getJiguiname() {
		return jiguiname;
	}

	public void setJiguiname(String jiguiname) {
		this.jiguiname = jiguiname;
	}

}

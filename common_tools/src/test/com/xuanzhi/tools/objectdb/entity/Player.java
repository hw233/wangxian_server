package com.xuanzhi.tools.objectdb.entity;

import javax.persistence.*;
import javax.jdo.annotations.Indices;
import javax.jdo.annotations.Index;

@Entity
public class Player {

	@Version private long version;
	@Id @GeneratedValue private long id; //must be initialized by the application
	
	@ManyToOne private Person owner;
	
	private String name;
	private int level;
	private int career;

	//各种需要存储的属性
	private long exp;
	private long money;
	private long yuanbo;
	private long lastLeaveGameTime;
	private long lastEnterGameTime;
	
	//各种不需要存储的属性
	private transient long hp;
	private transient long mhp;
	private transient long mp;
	private transient long mmp;
	private transient long pap;
	private transient long map;
	private transient long pac;
	private transient long mac;
	private transient long hit;
	private transient long dgp;
	private transient long chit;
	private transient long dac;
	private transient long crt;
	private transient long dchit;
	private transient long fap;
	private transient long fac;
	private transient long dfac;
	private transient long iap;
	private transient long iac;
	private transient long ifac;
	private transient long wap;
	private transient long wac;
	private transient long wfac;
	private transient long tap;
	private transient long tac;
	private transient long tfac;
	
	@Embedded private Knapsack ks;
	
	@Embedded private EquipColumn ec;

	
}

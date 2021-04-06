package com.xuanzhi.tools.objectdb.entity;

import javax.persistence.*;

@Entity
public class Task {
	@Version private long version;
	@Id @GeneratedValue private long id; //must be initialized by the application
	
	String name;
	String description;
	
	@Embedded TaskGoal goals[];
}

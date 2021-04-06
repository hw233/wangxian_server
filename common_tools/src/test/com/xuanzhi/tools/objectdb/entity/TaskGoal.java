package com.xuanzhi.tools.objectdb.entity;

import javax.persistence.*;

@Embeddable
public class TaskGoal {

	int goalType;
	String description;
	
	String targetName;
	int targetNum;
	
	String targetFrom;
}

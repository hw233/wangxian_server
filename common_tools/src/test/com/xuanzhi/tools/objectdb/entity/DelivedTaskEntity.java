package com.xuanzhi.tools.objectdb.entity;

import javax.persistence.*;

@Embeddable
public class DelivedTaskEntity {
	@Version private long version;
	@Id @GeneratedValue private long id; //must be initialized by the application
}

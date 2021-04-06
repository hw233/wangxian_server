package com.xuanzhi.tools.objectdb.entity;

import javax.persistence.*;

@Entity
public class ArticleEntity {

	@Version private long version;
	@Id @GeneratedValue private long id; //must be initialized by the application
}

package com.xuanzhi.tools.objectdb.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Person {

	@Version private long version;
	@Id @GeneratedValue private long id; //must be initialized by the application

	private String name;
	private int age;
	private int sex;
	private String address;
	private String phone;
	
	//The mappedBy element (above) specifies that the employees field is an inverse field rather than a
	//persistent field. The content of the employees set is not stored as part of a Department entity. Instead,
	//employees is automatically populated when a Department entity is retrieved from the database.
	@OneToMany(mappedBy="owner") private ArrayList<Player> persons = new ArrayList<Player>();
	
	public long getVersion() {
		return version;
	}



	public void setVersion(long version) {
		this.version = version;
	}



	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public int getAge() {
		return age;
	}



	public void setAge(int age) {
		this.age = age;
	}



	public int getSex() {
		return sex;
	}



	public void setSex(int sex) {
		this.sex = sex;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	public Person(String name,int age,int sex,String address,String phone){
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.address = address;
		this.phone = phone;
	}
}

package com.fy.engineserver.menu.question;

public class Question {
	private String name;
	private String description;
	private String[] options;
	private byte rightOption;
	int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getOptions() {
		return options;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}

	public byte getRightOption() {
		return rightOption;
	}
	
	public String getRightOptionText(){
		return this.options[this.getRightOption()];
	}

	public void setRightOption(byte rightOption) {
		this.rightOption = rightOption;
	}
}

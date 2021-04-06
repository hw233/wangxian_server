package com.fy.engineserver.dialog;

public class StringNode extends Node {
	String[] text;
	Node nextNode;

	public StringNode(String[] text) {
		this.text = text;
	}

	@Override
	Node goNext(int enter) {
		return nextNode;
	}

	@Override
	int doNode(int strIndex) {
		return NORMAL;
	}
}

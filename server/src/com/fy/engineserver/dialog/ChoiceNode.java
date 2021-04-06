package com.fy.engineserver.dialog;

import java.util.ArrayList;
import java.util.List;

public class ChoiceNode extends Node {
	String intro;
	List<String> choiceTitles = new ArrayList<String>();
	List<Node> nodes = new ArrayList<Node>();

	public ChoiceNode(String intro) {
		this.intro = intro;
	}

	@Override
	Node goNext(int enter) {
		if (enter >= 0 && enter < nodes.size()) {
			return nodes.get(enter);
		}
		return this;
	}

	@Override
	int doNode(int strIndex) {
		for (int i = 0; i < choiceTitles.size(); i++) {
		}
		return NORMAL;
	}

	public void addNode(Node node, String title) {
		nodes.add(node);
		choiceTitles.add(title);
	}
}

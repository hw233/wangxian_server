package com.fy.engineserver.dialog;

import java.io.IOException;

import com.fy.engineserver.datasource.language.Translate;

public class DialogTest {
	final Node startNode;
	Node currentNode;
	int strIndex;

	public DialogTest(Node startNode) {
		this.startNode = startNode;
	}

	public void start() {
		currentNode = startNode;
		int strIndex = 0;
		int strNum = 0;
		if (currentNode instanceof StringNode) {
			strNum = ((StringNode) currentNode).text.length;
		}
		int returnCode = Node.NORMAL;
		try {
			do {
				returnCode = currentNode.doNode(strIndex);
				strIndex++;
				if (strNum > 0)
					strNum--;
				System.in.skip(System.in.available());
				int enter = System.in.read();
				if (strNum == 0) {
					currentNode = currentNode.goNext(enter - '0');
					strIndex = 0;
					if (currentNode instanceof StringNode) {
						strNum = ((StringNode) currentNode).text.length;
					}
				}
			} while (returnCode != Node.EXIT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Node exitNode = new ExitNode();
		StringNode nameNode = new StringNode(new String[] { Translate.text_3954, Translate.text_3955 });
		StringNode componyNode =
			new StringNode(new String[] { Translate.text_3956, Translate.text_3957 });
		ChoiceNode cover = new ChoiceNode(Translate.text_3958);
		cover.addNode(nameNode, Translate.text_3959);
		cover.addNode(componyNode, Translate.text_3960);
		cover.addNode(exitNode, Translate.text_3961);
		nameNode.nextNode = cover;
		componyNode.nextNode = cover;
		new DialogTest(cover).start();
	}
}

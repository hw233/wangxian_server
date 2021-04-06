package com.fy.engineserver.dialog;
/**
 * 暂时只执行退出对话的功能
 * 
 * @author Administrator
 * 
 */
public class ExitNode extends Node {

	@Override
	int doNode(int strIndex) {
		return EXIT;
	}

	@Override
	Node goNext(int enter) {
		return null;
	}
}

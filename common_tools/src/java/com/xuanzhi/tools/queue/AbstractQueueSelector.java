package com.xuanzhi.tools.queue;

public abstract class AbstractQueueSelector {

	protected abstract void register(DefaultSelectableQueue queue);
	
	protected abstract void unregister(DefaultSelectableQueue queue);
	
	protected abstract void returnToSelector(DefaultSelectableQueue queue);
	
	protected abstract void messagePushed(DefaultSelectableQueue queue);
}

package com.xuanzhi.tools.timer;

public interface PersistTimerTaskFactory {

    /**
     * create one new PersistTimerTask
     */
    public PersistTimerTask createTimerTask(java.io.Serializable obj);
}

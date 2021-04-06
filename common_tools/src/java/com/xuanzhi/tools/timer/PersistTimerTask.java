package com.xuanzhi.tools.timer;

import java.io.*;
import java.util.*;

public abstract class PersistTimerTask extends TimerTask {
    protected Serializable m_obj;

    private PersistTimer m_timer;

    private PersistTimer.TaskItem m_item;

    protected PersistTimerTask(Serializable obj) {
        m_obj = obj;
    }

    public final void run() {
        m_timer.taskExecuted(m_item);
        run(m_obj);
    }

    protected final void setTimer(PersistTimer timer, PersistTimer.TaskItem item) {
        m_timer = timer;
        m_item = item;
    }

    public abstract void run(Serializable obj);
}

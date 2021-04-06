package com.xuanzhi.tools.timer;

import java.util.*;
import java.io.*;

/**
 * <pre>
 * 具有Persist功能的Timer。
 * 
 * 适用范围：当你对某一种对象进行同一钟处理，并且这种处理要具有Timer功能
 *           比如，延迟发送一条短信，或者一条彩信。
 * 
 * 功能描述：当Timer停止时，PersistTimer可以帮助你保存等待执行处理的对象到
 *           指定文件或者流中。当Timer再次启动时，PersistTimer可以载入这些
 *           对象，继续进行同一种处理。
 *           为了达到处理的延续性，PersistTimer比Timer多需要一个参数，也就是
 *           完成处理的对象PersistTimerTaskFactory。
 * 程序示例：请参看此类的测试用例util.timer.testcase.PersistTimerTestCase
 * </pre>
 * 
 * @see util.timer.testcase.PersistTimerTestCase
 */
public class PersistTimer {

    protected Timer m_timer;

    protected PersistTimerTaskFactory m_factory;

    protected File m_pFile;

    protected List m_taskList = Collections.synchronizedList(new LinkedList());

    /**
     * Creates a new timer whose associated thread has the specified name, and
     * may be specified to run as a daemon.
     */
    public PersistTimer(PersistTimerTaskFactory factory, boolean isDaemon) {
        this(factory, isDaemon, null);
    }

    /**
     * Creates a new timer whose associated thread has the specified name, and
     * may be specified to run as a daemon. <br/> <B>NOTE: </B> if the
     * <code>persistFile</code> exists, this constructor will call method
     * <code>schedule(java.io.InputStream)</code> immediately.
     * 
     * @param factory
     *            the PersistTimerTask Factory, it will know how to handle the
     *            given objects.
     * @param isDaemon
     *            true if the associated thread should run as a daemon
     * @param persistFile
     *            the file for reading task or saving task
     * @see #schedule(java.io.InputStream)
     */
    public PersistTimer(PersistTimerTaskFactory factory, boolean isDaemon,
            File persistFile) {
        m_timer = new Timer(isDaemon);
        m_factory = factory;
        m_pFile = persistFile;

        if (m_pFile != null && m_pFile.exists() && !m_pFile.isDirectory()) {
            try {
                FileInputStream input = new FileInputStream(m_pFile);
                schedule(input);
                input.close();

            } catch (Exception e) {
                System.err.println("[PersistTimer] [read-task] [error] ["
                        + m_pFile + "] [" + e + "]");
                e.printStackTrace();
            }

            m_pFile.delete();

        }
    }

    /**
     * Terminates this timer, discarding any currently scheduled tasks and save
     * the tasks to given file.
     */
    public void cancel() {
        if (m_pFile != null) {
            try {
                FileOutputStream output = new FileOutputStream(m_pFile);
                cancel(output);
                output.close();
            } catch (Exception e) {
                System.err.println("[PersistTimer] [save-task] [error] ["
                        + m_pFile + "] [" + e + "]");
                e.printStackTrace();
            }
        } else {
            try {
                cancel(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Terminates this timer, discarding any currently scheduled tasks and save
     * the tasks to given output stream.
     */
    public void cancel(OutputStream output) throws IOException {
        m_timer.cancel();

        List list = new LinkedList();
        list.addAll(m_taskList);

        m_taskList.clear();

        if (output != null) {
            ObjectOutputStream o = new ObjectOutputStream(output);

            o.writeInt(list.size());

            for (int i = 0; i < list.size(); i++) {
                TaskItem ti = (TaskItem) list.get(i);
                o.writeObject(ti.task.m_obj);
                o.writeObject(ti.date);
                o.writeObject(new Long(ti.period));
            }

            o.flush();
        }
    }

    /**
     * read tasks from given input stream and schedule these tasks. if one task
     * executing time has be expired, then the task will be executed.
     */
    public void schedule(InputStream input) throws Exception {
        ObjectInputStream oinput = new ObjectInputStream(input);

        int n = oinput.readInt();

        for (int i = 0; i < n; i++) {
            Serializable obj = (Serializable) oinput.readObject();
            Date d = (Date) oinput.readObject();
            Long period = (Long) oinput.readObject();

            // System.out.println("[read-task] ["+System.currentTimeMillis()+"]
            // ["+(i+1)+"] ["+obj+"] ["+d.getTime()+"] ["+period+"]");
            if (period.longValue() <= 0L) {
                if (System.currentTimeMillis() >= d.getTime()) {
                    schedule(obj, 0L);
                } else {
                    schedule(obj, d.getTime() - System.currentTimeMillis());
                }
            } else {
                if (System.currentTimeMillis() >= d.getTime()) {
                    long l = d.getTime();
                    while (l <= System.currentTimeMillis()) {
                        l += period.longValue();
                    }

                    schedule(obj, l - System.currentTimeMillis(), period
                            .longValue());
                } else {
                    schedule(obj, System.currentTimeMillis() - d.getTime(),
                            period.longValue());
                }
            }
        }
    }

    /**
     * Schedules the specified task for execution after the specified delay.
     */
    public PersistTimerTask schedule(java.io.Serializable obj, long delay) {
        PersistTimerTask task = m_factory.createTimerTask(obj);
        TaskItem item = new TaskItem(task, delay, -1);
        task.setTimer(this, item);

        m_timer.schedule(task, delay);

        m_taskList.add(item);

        return task;
    }

    /**
     * Schedules the specified task for repeated fixed-delay execution,
     * beginning after the specified delay. Subsequent executions take place at
     * approximately regular intervals separated by the specified period. In
     * fixed-delay execution, each execution is scheduled relative to the actual
     * execution time of the previous execution. If an execution is delayed for
     * any reason (such as garbage collection or other background activity),
     * subsequent executions will be delayed as well. In the long run, the
     * frequency of execution will generally be slightly lower than the
     * reciprocal of the specified period (assuming the system clock underlying
     * Object.wait(long) is accurate). Fixed-delay execution is appropriate for
     * recurring activities that require "smoothness." In other words, it is
     * appropriate for activities where it is more important to keep the
     * frequency accurate in the short run than in the long run. This includes
     * most animation tasks, such as blinking a cursor at regular intervals. It
     * also includes tasks wherein regular activity is performed in response to
     * human input, such as automatically repeating a character as long as a key
     * is held down.
     * 
     * @param task
     *            task to be scheduled.
     * @param delay
     *            delay in milliseconds before task is to be executed.
     * @param period
     *            time in milliseconds between successive task executions.
     */
    public PersistTimerTask schedule(java.io.Serializable obj, long delay,
            long period) {
        PersistTimerTask task = m_factory.createTimerTask(obj);
        TaskItem item = new TaskItem(task, delay, period);

        task.setTimer(this, item);

        m_timer.schedule(task, delay, period);

        m_taskList.add(item);

        return task;
    }

    protected void taskExecuted(TaskItem item) {
        if (item != null && item.period == -1)
            m_taskList.remove(item);
    }

    static class TaskItem {
        PersistTimerTask task;

        Date date;

        long period;

        public TaskItem(PersistTimerTask task, long delay, long period) {
            this.task = task;
            date = new Date(System.currentTimeMillis() + delay);
            this.period = period;
        }
    }
}

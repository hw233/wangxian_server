package com.xuanzhi.tools.watchdog;

// Contributors:  Mathias Bogaert

import java.io.File;
import java.util.*;
/**
  * Check every now and then that a certain file has not changed. If it
  * has, then call the {@link #doOnChange} method.
  *
  * @since 1.0
  */

public abstract class FileWatchdog extends Thread {

  /**
     The default delay between every file modification check, set to 60
     seconds.  */
  static final public long DEFAULT_DELAY = 60000; 
  /**
     The name of the file to observe  for changes.
   */
  
  protected HashMap fileList;
  /**
     The delay to observe between every check. By default set {@link
     #DEFAULT_DELAY}. */
  protected long delay = DEFAULT_DELAY; 
  
  boolean warnedAlready = false;
  
  boolean interrupted = false;

  public static class Entity
  {
		File file;
		long lastModify;
	    Entity(File file,long lastModify)
		{
			this.file = file;
			this.lastModify = lastModify;
		}
  }
  
  protected  FileWatchdog() {
	  fileList = new HashMap ();
    setDaemon(true);
  }

  /**
     Set the delay to observe between each check of the file changes.
   */
  public  void setDelay(long delay) {
    this.delay = delay;
  }

  public void addFile(File file)
  {
		fileList.put(file.getAbsolutePath(),new Entity(file,file.lastModified())); 	
  }
  
  public boolean contains(File file)
  {
		if( fileList.get(file.getAbsolutePath()) != null) return true;
		else return false;
  }
  
  abstract   protected   void doOnChange(File file);

  protected  void checkAndConfigure() {
	  HashMap map = (HashMap)fileList.clone(); 
	  Iterator it = map.values().iterator();
	  
	  while( it.hasNext())
	  {
		  
			Entity entity = (Entity)it.next();
			
			boolean fileExists;
			try {
			  fileExists = entity.file.exists();
			} catch(SecurityException  e) 
			{
			  System.err.println ("Was not allowed to read check file existance, file:["+ entity.file .getAbsolutePath() +"].");
			  interrupted = true; // there is no point in continuing
			  return;
			}

			if(fileExists) 
			{
				
			  long l = entity.file.lastModified(); // this can also throw a SecurityException
			  if(l > entity.lastModify) {           // however, if we reached this point this
					entity.lastModify = l;              // is very unlikely.
					newThread(entity.file);
			  }
			}
			else 
			{
				System.err.println ("["+entity.file .getAbsolutePath()+"] does not exist.");
			}
	  }
  }
  
  private void newThread(File file)
  {
	  class MyThread extends Thread
	  {
			File f;
			public MyThread(File f)
			{
				this.f = f;
			}
			
			public void run()
			{
				doOnChange(f);
			}
	  }
	  
	  MyThread mt = new MyThread(file);
	  mt.start();
  }

  public  void run() 
  {    
    while(!interrupted) {
      try {
		Thread.currentThread().sleep(delay);
      } catch(InterruptedException e) {
	// no interruption expected
      }
      checkAndConfigure();
    }
  }
}

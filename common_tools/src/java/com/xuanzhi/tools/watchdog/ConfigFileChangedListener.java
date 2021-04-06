
package com.xuanzhi.tools.watchdog;

public interface ConfigFileChangedListener {
	
	/**
	 * 当文件被修改后，会调这个接口。
	 * @param file
	 */
	public void fileChanged(java.io.File file);
}

package com.xuanzhi.tools.resource;

import java.io.File;

import com.xuanzhi.tools.filesystem.ByteArrayUtils;
import com.xuanzhi.tools.filesystem.DefaultVirtualFileSystem;

/**
 * 资源包
 * 表达的是一个版本的资源，对应一个vfs文件。
 * 
 * 这个vfs文件中，存储了这个包所包含的所有的资源文件，比如图片，文本，视频，以及其他信息。
 * 
 * 我们将所有的资源文件，分为A包和B包。
 * 
 * A包为必须存在的包，只有A包为最新的包，才能进入游戏。
 * B包为扩展包，只有特定的条件下，才需要B包必须存在。
 * 
 * 这么设计是为了满足资源特别大的情况。
 * 
 * 如果只有不是特别大，可以只有A包存在，B包不存在。
 * 
 * 资源包文件有版本的概念，版本是这样的：
 * 1.0
 * 1.1
 * 1.2 
 * 等等。 分为 masterVersion  slaveVersion
 * 
 * 
 * 资源包文件还有是否为全包和补丁包的区分。全包是指包含了所有的资源文件。
 * 
 * 
 * 资源包的头的组织方式：
 * 
 * 前8个字节固定
 * 接下来4个字节，存储masterVersion
 * 接下来4个字节，存储slaveVersion
 * 接下来4个字节，存储fullPackage，0标识不是fullPackage
 * 接下来4个字节，存储aPackageFlag，0标识不是aPackageFlag
 * 接下来4个字节，存储releaseFlag，0标识不是releaseFlag
 * @author <a href='myzdf.bj@gmail.com'>yugang.wang</a>
 *
 */
public class ResourceFile {

	public static byte[] HEADER_FLAG = new byte[]{(byte)0x1a,(byte)0xb1,(byte)0xc2,(byte)0xd3,(byte)0xf4,(byte)0x67,(byte)0x89,(byte)0xa0};
	
	int masterVersion;
	
	int slaveVersion;
	
	boolean fullPackage;
	
	boolean aPackageFlag;
	
	boolean releaseFlag;
	
	File dataFile;
	DefaultVirtualFileSystem vfs ;
	
	public ResourceFile(File f){
		dataFile = f;
	}
	
	public boolean init(){
		if(dataFile.isFile() && dataFile.exists()){
			vfs = new DefaultVirtualFileSystem(dataFile,dataFile.getPath());
			byte[] headers = vfs.getHeaders();
			
			//前面8个字节为固定的格式
			for(int i = 0 ; i < HEADER_FLAG.length ; i++){
				if(headers[i] != HEADER_FLAG[i]){
					return false;
				}
			}
			int offset = 8;
			//
			masterVersion = (int)ByteArrayUtils.byteArrayToNumber(headers, offset, 4);
			offset += 4;
			
			slaveVersion = (int)ByteArrayUtils.byteArrayToNumber(headers, offset, 4);
			offset += 4;
			
			fullPackage = (ByteArrayUtils.byteArrayToNumber(headers, offset, 4) == 1);
			offset += 4;
			
			aPackageFlag = (ByteArrayUtils.byteArrayToNumber(headers, offset, 4) == 1);
			offset += 4;
			
			releaseFlag = (ByteArrayUtils.byteArrayToNumber(headers, offset, 4) == 1);
			offset += 4;
			
			return true;
		}else{
			vfs = new DefaultVirtualFileSystem(dataFile,dataFile.getPath());
			vfs.setHeaders(HEADER_FLAG, 0);
			vfs.setHeaders(ByteArrayUtils.numberToByteArray(masterVersion, 4), 8);
			vfs.setHeaders(ByteArrayUtils.numberToByteArray(slaveVersion, 4), 12);
			vfs.setHeaders(ByteArrayUtils.numberToByteArray(fullPackage?1:0, 4), 16);
			vfs.setHeaders(ByteArrayUtils.numberToByteArray(aPackageFlag?1:0, 4), 20);
			vfs.setHeaders(ByteArrayUtils.numberToByteArray(releaseFlag?1:0, 4), 24);
			
			return true;
		}
	}
	
	public void deletePackage(){
		vfs.clear();
	}
}

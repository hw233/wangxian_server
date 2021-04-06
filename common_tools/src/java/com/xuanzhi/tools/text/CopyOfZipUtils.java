package com.xuanzhi.tools.text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipInputStream;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

public class CopyOfZipUtils {

	public static void main(String args[]) {
		if (args.length < 2) {
			printHelp();
		} else if (args[0].equals("-h")) {
			printHelp();
		} else {
			if (args[0].equals("-g")) {
				String filepath = args[1];
				File f = new File(filepath);
				try {
					gzip(f);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (args[0].equals("-uz")) {
				String filepath = args[1];
				File file = new File(filepath);
				String fullPath = file.getAbsolutePath();
				String name = file.getName();
				name = name.substring(0, name.lastIndexOf("."));
				String dest = fullPath.substring(0, fullPath.lastIndexOf("/")) + File.separator + name;
				try {
					System.out.println("[trace1] ["+fullPath+"]");
					extractZip(fullPath, dest);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(args[0].equals("-z")) {
				String path = args[1];
			}
		}
	}

	private static void printHelp() {
		System.out.println("usage: ZipUtils [-zgu] [file ...]");
		System.out.println("-z		use common zip format");
		System.out.println("-g		use gzip format");
		System.out.println("-u		gunzip file(s)");
		System.out.println("-uz	unzip file(s)");
		System.out
				.println("file...	files to (de)compress. If none given, use standard input.");
	}

	public static void zip(ZipOutputStream out, File f, String baseEntryName)
			throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(baseEntryName + "/"));
			baseEntryName = baseEntryName.length() == 0 ? "" : baseEntryName
					+ "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], baseEntryName + fl[i].getName());
			}
		} else {
			out.putNextEntry(new ZipEntry(baseEntryName));
			FileInputStream in = new FileInputStream(f);
			byte buffer[] = new byte[1024];
			int b;
			while ((b = in.read(buffer)) > -1) {
				out.write(buffer, 0, b);
			}
			out.closeEntry();
			in.close();
		}
	}

	public static void zip(ZipOutputStream out, InputStream in,
			String baseEntryName) throws Exception {
		out.putNextEntry(new ZipEntry(baseEntryName));
		byte buffer[] = new byte[1024];
		int b;
		while ((b = in.read(buffer)) > -1) {
			out.write(buffer, 0, b);
		}
		out.closeEntry();
		in.close();
	}

	public void zip(File sourceFile, File dstFile, String encoding) {
		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(dstFile);
		//zip.setBasedir(sourceFile);
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(sourceFile);
		//fileSet.setIncludes("**/*.java"); 包括哪些文件或文件夹 eg:zip.setIncludes("*.java");
		//fileSet.setExcludes(...); 排除哪些文件或文件夹
		zip.addFileset(fileSet);
		if(encoding != null) {
			zip.setEncoding(encoding);
		}
		zip.execute();
	}
	
	public static void gzip(GZIPOutputStream out, File f) throws Exception {
		if (f.isFile()) {
			FileInputStream in = new FileInputStream(f);
			byte buffer[] = new byte[1024];
			int b;
			while ((b = in.read(buffer)) > -1) {
				out.write(buffer, 0, b);
			}
			in.close();
		}
	}

	public static void gzip(File f) throws Exception {
		if (f.isDirectory()) {
			File fs[] = f.listFiles();
			for (File ff : fs) {
				gzip(ff);
			}
		} else {
			String filename = f.getPath();
			String zipFile = filename + ".gz";
			GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(
					zipFile));
			FileInputStream in = new FileInputStream(f);
			byte buffer[] = new byte[1024];
			int b;
			while ((b = in.read(buffer)) > -1) {
				out.write(buffer, 0, b);
			}
			in.close();
			out.close();
		}
	}

	public static void gzip(GZIPOutputStream out, InputStream in)
			throws Exception {
		byte buffer[] = new byte[1024];
		int b;
		while ((b = in.read(buffer)) > -1) {
			out.write(buffer, 0, b);
		}
	}

	public static byte[] gunzip(String filePath) throws Exception {
		GZIPInputStream in = new GZIPInputStream(new FileInputStream(filePath));
		byte bs[] = gunzip(in);
		in.close();
		return bs;
	}

	public static byte[] gunzip(GZIPInputStream in) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte buffer[] = new byte[1024];
		int n = 0;
		while ((n = in.read(buffer)) > -1) {
			out.write(buffer, 0, n);
		}
		out.close();
		return out.toByteArray();
	}

    /**
     * @param path 要压缩的路径, 可以是目录, 也可以是文件.
     * @param basePath 如果path是目录,它一般为new File(path), 作用是:使输出的zip文件以此目录为根目录, 如果为null它只压缩文件, 不解压目录.
     * @param zo 压缩输出流
     * @param isRecursive 是否递归
     * @param isOutBlankDir 是否输出空目录, 要使输出空目录为true,同时baseFile不为null.
     * @throws IOException
     */
    public void zip(String path, File basePath, ZipOutputStream zo, boolean isRecursive, boolean isOutBlankDir, String encoding) throws IOException {
        
        File inFile = new File(path);

        File[] files = new File[0];
        if(inFile.isDirectory()) {    //是目录
            files = inFile.listFiles();
        } else if(inFile.isFile()) {    //是文件
            files = new File[1];
            files[0] = inFile;
        }
        byte[] buf = new byte[1024];
        int len;
        //System.out.println("baseFile: "+baseFile.getPath());
        for(int i=0; i<files.length; i++) {
            String pathName = "";
            if(basePath != null) {
                if(basePath.isDirectory()) {
                    pathName = files[i].getPath().substring(basePath.getPath().length()+1);
                } else {//文件
                    pathName = files[i].getPath().substring(basePath.getParent().length()+1);
                }
            } else {
                pathName = files[i].getName();
            }
            //System.out.println(pathName);
            if(files[i].isDirectory()) {
                if(isOutBlankDir && basePath != null) {
                	String entryName = pathName + "/";
                	if(encoding != null && encoding.length() > 0) {
                		entryName = new String((pathName+"/").getBytes(),encoding);
                	}
                    zo.putNextEntry(new ZipEntry(entryName));    //可以使空目录也放进去
                    System.out.println(pathName+"/");
                }
                if(isRecursive) {    //递归
                    zip(files[i].getPath(), basePath, zo, isRecursive, isOutBlankDir, encoding);
                }
            } else {
                FileInputStream fin = new FileInputStream(files[i]);
            	String entryName = pathName;
            	if(encoding != null && encoding.length() > 0) {
            		entryName = new String((pathName).getBytes(),encoding);
            	}
                zo.putNextEntry(new ZipEntry(entryName));
                System.out.println(pathName);
                while((len=fin.read(buf))>0) {
                    zo.write(buf,0,len);
                }
                fin.close();
            }
        }
    }

	/**
	 * @param args
	 */
	public static int iCompressLevel; // 压缩比 取值范围为0~9
	public static boolean bOverWrite; // 是否覆盖同名文件 取值范围为True和False
	private static ArrayList allFiles = new ArrayList();
	public static String sErrorMessage;

	public static ArrayList extractZip(String sZipPathFile, String sDestPath) {
		ArrayList allFileName = new ArrayList();
		File file = new File(sDestPath);
		if(!file.isDirectory()) {
			file.mkdir();
		}
		if(!sDestPath.endsWith(File.separator))
			sDestPath = sDestPath + File.separator;
		System.out.println("[extractZip] ["+sZipPathFile+"] ["+sDestPath+"]");
		try {
			// 先指定压缩档的位置和档名，建立FileInputStream对象
			FileInputStream fins = new FileInputStream(sZipPathFile);
			// 将fins传入ZipInputStream中
			ZipInputStream zins = new ZipInputStream(fins);
			java.util.zip.ZipEntry ze = null;
			byte ch[] = new byte[1024];
			System.out.println("[extractZip] [trace1] ["+zins.available()+"]");
			while ((ze = zins.getNextEntry()) != null) {
					String zeName = encode(ze.getName(),"gb2312","iso-8859-1");
					
					System.out.println("[extractZip] [trace11] ["+zeName+"]");
					File zfile = new File(sDestPath + zeName);
					System.out.println("[extractZip] [trace2] ["+sDestPath + zeName+"]");
					File fpath = new File(zfile.getParentFile().getPath());
					System.out.println("[extractZip] [trace3] ["+fpath.getAbsolutePath()+"]");
					if (ze.isDirectory()) {
						if (!zfile.exists())
							zfile.mkdirs();
						zins.closeEntry();
					} else {
						if (!fpath.exists())
							fpath.mkdirs();
						FileOutputStream fouts = null;
						try {
							fouts = new FileOutputStream(zfile);
							int i;
							allFileName.add(zfile.getAbsolutePath());
							while ((i = zins.read(ch)) != -1)
								fouts.write(ch, 0, i);
							zins.closeEntry();
							fouts.close();
						} catch(Exception e) {
							e.printStackTrace();
							continue;
						} finally {
							zins.closeEntry();
							fouts.close();
						}
					}
			}
			fins.close();
			zins.close();
			sErrorMessage = "OK";
		} catch (Exception e) {
			e.printStackTrace();
			sErrorMessage = e.getMessage();
		}
		allFiles.clear();
		return allFileName;
	}
	

	public static String encode(String s, String srcEncoding, String dstEncoding) {
		try {
			byte[] bytes = s.getBytes(srcEncoding);
			return new String(bytes, dstEncoding);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

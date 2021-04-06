package com.fy.engineserver.gm.record;

import java.util.Hashtable;

public class UploadFiles
{

    protected UploadFiles()
    {
        hFiles = new Hashtable();
        count = 0;
        totalSize = 0L;
    }

    protected void addFile(UploadFile pFile)
    {
        hFiles.put(new Integer(count), pFile);
        count++;
        totalSize += pFile.getSize();
    }

    public UploadFile getFile(int pCount)
        throws Exception
    {
        if(pCount >= count || pCount <= -1)
        {
            throw new Exception("\u53C2\u6570\u9519\u8BEF");
        } else
        {
        	UploadFile tempFile = (UploadFile)hFiles.get(new Integer(pCount));
            return tempFile;
        }
    }

    public int getCount()
    {
        return count;
    }

    public long getSize()
    {
        return totalSize;
    }

    private Hashtable hFiles;
    private long totalSize;
    private int count;
}

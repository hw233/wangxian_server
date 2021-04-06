package com.fy.boss.cmd;

import com.fy.boss.cmd.FileChunk;

public class FileChunk {
	private String filename = "";
	
	private String savepath = "";
	
	private byte data[];
	
	private int total;
	
	private int index;
	
	public boolean isCompleted = false;
	
	public FileChunk append(FileChunk chunk) throws Exception {
		if(!this.equals(chunk)) {
			throw new Exception("FileChunk belongs to diff file : [src file:"+filename+"] [chunk file:"+chunk.getFilename()+"]");
		} 
		if(chunk.getIndex() >= total) {
			throw new Exception("FileChunk index upper than total : [total:"+total+"] [chunk index:"+chunk.getIndex()+"]");
		}
		if(chunk.getIndex() <= index) {
			throw new Exception("FileChunk index lower than now index : [now index:"+index+"] [chunk index:"+chunk.getIndex()+"]");
		}
		if(index != chunk.getIndex() - 1) {
			throw new Exception("FileChunk index chaos : [now index:"+index+"] [chunk index:"+chunk.getIndex()+"]");
		}
		byte ndata[] = chunk.getData();
		if(ndata != null && ndata.length > 0) {
			byte tdata[] = new byte[data.length + ndata.length];
			System.arraycopy(data, 0, tdata, 0, data.length);
			System.arraycopy(ndata, 0, tdata, data.length, ndata.length);
			this.data = tdata;
		}
		this.index = chunk.getIndex();
		return this;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSavepath() {
		return savepath;
	}

	public void setSavepath(String savepath) {
		this.savepath = savepath;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public boolean equals(Object o) {
		if(o instanceof FileChunk) {
			FileChunk fb = (FileChunk)o;
			return this.getFilename().equals(fb.getFilename()) && this.getSavepath().equals(fb.getSavepath());
		}
		return false;
	}
	
}

package com.fy.engineserver.gm.tesk;

public class Tesk {
	final static int TESK_RECEIVE_LIMIT=1;
	final static int TESK_COMMIT_LIMIT=2;
	final static int TESK_QUESTION=3;
	private long id;
	private String name;
	private String coords_x;
	private String coords_y;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoords_x() {
		return coords_x;
	}

	public void setCoords_x(String coordsX) {
		coords_x = coordsX;
	}

	public String getCoords_y() {
		return coords_y;
	}

	public void setCoords_y(String coordsY) {
		coords_y = coordsY;
	}

}

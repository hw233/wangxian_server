package com.fy.engineserver.soulpith;
/**
 * 灵髓种类
 * 
 * @date on create 2016年3月22日 上午10:30:01
 */
public enum SoulPithTypes {
	TIAN(0, "天", "<N imagePath='ui/texture_linggen.png' imageRect='tianlingsui.png'  width='32' height='32'></N>"),
	DI(1, "地", "<N imagePath='ui/texture_linggen.png' imageRect='dilingsui.png'  width='32' height='32'></N>"),
	XIAN(2, "仙", "<N imagePath='ui/texture_linggen.png' imageRect='xianlingsui.png'  width='32' height='32'></N>"),
	SHEN(3, "神", "<N imagePath='ui/texture_linggen.png' imageRect='shenlingsui.png'  width='32' height='32'></N>"),
	REN(4, "人", "<N imagePath='ui/texture_linggen.png' imageRect='renlingsui.png'  width='32' height='32'></N>"),
	MO(5, "魔", "<N imagePath='ui/texture_linggen.png' imageRect='molingsui.png'  width='32' height='32'></N>"),
	YAO(6, "妖", "<N imagePath='ui/texture_linggen.png' imageRect='yaolingsui.png'  width='32' height='32'></N>"),
	GUI(7, "鬼", "<N imagePath='ui/texture_linggen.png' imageRect='guilingsui.png'  width='32' height='32'></N>"),
	;
	private int id;
	
	private String name;
	
	private String infoShow;
	
	SoulPithTypes(int id, String name, String infoShow) {
		this.id = id;
		this.name = name;
		this.infoShow = infoShow;
	}
	
	public static SoulPithTypes valueOf(int index) {
		for (SoulPithTypes s : SoulPithTypes.values()) {
			if (s.getId() == index) {
				return s;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInfoShow() {
		return infoShow;
	}

	public void setInfoShow(String infoShow) {
		this.infoShow = infoShow;
	}
}

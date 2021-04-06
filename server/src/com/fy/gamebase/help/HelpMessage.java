package com.fy.gamebase.help;

/**
 * 帮助系统
 * 
 * 一条帮助信息
 * @author Administrator
 *
 */
public class HelpMessage {	
	/**
	 * 矩形区域
	 */
	short rectx,recty,rectw,recth;
	int rectcolor;
	byte rectlinew;
	/**
	 * 额外的线条
	 */
	short lineX1[],lineY1[],lineX2[],lineY2[];
	int lineColor[];
	byte lineW[];
	/**
	 * 按钮
	 */
	short btnx,btny;
	short btnFontSize;
	String btnText="";
	int btnColor;
	/**
	 * 按钮触发泡泡的线条
	 */
	short bubbleLineX1[],bubbleLineY1[],bubbleLineX2[],bubbleLineY2[];
	int bubbleLineColor[];
	byte bubbleLineW[];
	/**
	 * 泡泡文字
	 */
	String bubbleContent = "";
	short bubbleContentX, bubbleContentY, bubbleContentW, bubbleContentH;
	int bubbleTextColor;
	short bubbleFontSize;
	
	boolean activeBubble;
	
	
	
	public boolean isActiveBubble() {
		return activeBubble;
	}
	public void setActiveBubble(boolean activeBubble) {
		this.activeBubble = activeBubble;
	}
	public short[] getBubbleLineX1() {
		return bubbleLineX1;
	}
	public void setBubbleLineX1(short[] bubbleLineX1) {
		this.bubbleLineX1 = bubbleLineX1;
	}
	public short[] getBubbleLineY1() {
		return bubbleLineY1;
	}
	public void setBubbleLineY1(short[] bubbleLineY1) {
		this.bubbleLineY1 = bubbleLineY1;
	}
	public short[] getBubbleLineX2() {
		return bubbleLineX2;
	}
	public void setBubbleLineX2(short[] bubbleLineX2) {
		this.bubbleLineX2 = bubbleLineX2;
	}
	public short[] getBubbleLineY2() {
		return bubbleLineY2;
	}
	public void setBubbleLineY2(short[] bubbleLineY2) {
		this.bubbleLineY2 = bubbleLineY2;
	}
	public int[] getBubbleLineColor() {
		return bubbleLineColor;
	}
	public void setBubbleLineColor(int[] bubbleLineColor) {
		this.bubbleLineColor = bubbleLineColor;
	}
	public byte[] getBubbleLineW() {
		return bubbleLineW;
	}
	public void setBubbleLineW(byte[] bubbleLineW) {
		this.bubbleLineW = bubbleLineW;
	}
	public String getBubbleContent() {
		return bubbleContent;
	}
	public void setBubbleContent(String bubbleContent) {
		this.bubbleContent = bubbleContent;
	}
	public int getBubbleContentX() {
		return bubbleContentX;
	}
	public short[] getLineX1() {
		return lineX1;
	}
	public void setLineX1(short[] lineX1) {
		this.lineX1 = lineX1;
	}
	public short[] getLineY1() {
		return lineY1;
	}
	public void setLineY1(short[] lineY1) {
		this.lineY1 = lineY1;
	}
	public short[] getLineX2() {
		return lineX2;
	}
	public void setLineX2(short[] lineX2) {
		this.lineX2 = lineX2;
	}
	public short[] getLineY2() {
		return lineY2;
	}
	public void setLineY2(short[] lineY2) {
		this.lineY2 = lineY2;
	}
	public int[] getLineColor() {
		return lineColor;
	}
	public void setLineColor(int[] lineColor) {
		this.lineColor = lineColor;
	}
	public byte[] getLineW() {
		return lineW;
	}
	public void setLineW(byte[] lineW) {
		this.lineW = lineW;
	}
	public short getBtnx() {
		return btnx;
	}
	public void setBtnx(short btnx) {
		this.btnx = btnx;
	}
	public short getBtny() {
		return btny;
	}
	public void setBtny(short btny) {
		this.btny = btny;
	}
	public String getBtnText() {
		return btnText;
	}
	public void setBtnText(String btnText) {
		this.btnText = btnText;
	}
	public int getBtnColor() {
		return btnColor;
	}
	public void setBtnColor(int btnColor) {
		this.btnColor = btnColor;
	}
	public short getRectx() {
		return rectx;
	}
	public void setRectx(short rectx) {
		this.rectx = rectx;
	}
	public short getRecty() {
		return recty;
	}
	public void setRecty(short recty) {
		this.recty = recty;
	}
	public short getRectw() {
		return rectw;
	}
	public void setRectw(short rectw) {
		this.rectw = rectw;
	}
	public short getRecth() {
		return recth;
	}
	public void setRecth(short recth) {
		this.recth = recth;
	}
	public int getRectcolor() {
		return rectcolor;
	}
	public void setRectcolor(int rectcolor) {
		this.rectcolor = rectcolor;
	}
	public byte getRectlinew() {
		return rectlinew;
	}
	public void setRectlinew(byte rectlinew) {
		this.rectlinew = rectlinew;
	}	
	public short getBtnFontSize() {
		return btnFontSize;
	}
	public void setBtnFontSize(short btnFontSize) {
		this.btnFontSize = btnFontSize;
	}
	public short getBubbleContentY() {
		return bubbleContentY;
	}
	public void setBubbleContentY(short bubbleContentY) {
		this.bubbleContentY = bubbleContentY;
	}
	public short getBubbleContentW() {
		return bubbleContentW;
	}
	public void setBubbleContentW(short bubbleContentW) {
		this.bubbleContentW = bubbleContentW;
	}
	public short getBubbleContentH() {
		return bubbleContentH;
	}
	public void setBubbleContentH(short bubbleContentH) {
		this.bubbleContentH = bubbleContentH;
	}
	public short getBubbleFontSize() {
		return bubbleFontSize;
	}
	public void setBubbleFontSize(short bubbleFontSize) {
		this.bubbleFontSize = bubbleFontSize;
	}
	public void setBubbleContentX(short bubbleContentX) {
		this.bubbleContentX = bubbleContentX;
	}
	public int getBubbleTextColor() {
		return bubbleTextColor;
	}
	public void setBubbleTextColor(int bubbleTextColor) {
		this.bubbleTextColor = bubbleTextColor;
	}
	
}

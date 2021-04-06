package com.fy.engineserver.shortcut;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

/**
 * 管理两套快捷键组合，玩家可以自由切换和设定快捷键
 * 
 * @author 
 * 
 */
public class ShortcutAgent {
	private static final int SHORTCUT_NUM = 16;

	/**
	 * 支持数字键行走
	 */
	public boolean numberDirection;

	private Shortcut[] shortcuts = new Shortcut[SHORTCUT_NUM];

	public Shortcut[] getShortcuts() {
		return shortcuts;
	}
	//检查玩家前2个快捷栏有没有设置快捷键，如果没有，自动设置
	public boolean checkAndSetFirst2Shortcut(int skillId){
		for( int i=0;i< SHORTCUT_NUM;i++){
			if( shortcuts[i] instanceof SkillShortcut){
				SkillShortcut s = (SkillShortcut)shortcuts[i];
				if( s.skillId == skillId) return false;
			}
		}
		for( int i=0;i< 2;i++){
			if( shortcuts[i] == Shortcut.UNDEFINED){
				shortcuts[i] = new SkillShortcut(skillId);
				return true;
			}			
		}
		return false;
	}
	/**
	 * 创建新角色，设置默认快捷键
	 */
	public void onCreatePlayerShortCut(){}
	public void load(byte[] data) {
		if (data != null && data.length > 0) {
			DataInput din = new DataInputStream(new ByteArrayInputStream(data));
			boolean sucess = true;
			try {
				numberDirection = din.readBoolean();
				for (int i = 0; i < shortcuts.length; i++) {
					switch (din.readByte()) {
					case Shortcut.NONE:
						shortcuts[i] = Shortcut.UNDEFINED;
						break;

					case Shortcut.SKILL_SHORTCUT:
						int skillId = din.readShort();
						shortcuts[i] = new SkillShortcut(skillId);
						break;

					case Shortcut.PROPS_SHORTCUT:
						long articleId = din.readLong();
						short iconID = din.readShort();
						String categateName = din.readUTF();
						shortcuts[i] = new PropsShortcut(articleId,  iconID, categateName);
						break;

					case Shortcut.SYSTEM_SHORTCUT:
						shortcuts[i] = new SystemShortcut(din.readByte());
						break;
					case Shortcut.GongFu_SHORTCUT:
						int skillnum = din.readShort();						
						short skillids[] = new short[ skillnum];
						for( int j=0;j< skillnum;j++){
							skillids[j] = din.readShort();
						}
						shortcuts[i]= new GongFukillShortcut(skillids);
						break;
					}
				}
			}catch(EOFException e){
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				clearShortcut();
				sucess = false;
			}			
			for (int i = 0; i < shortcuts.length; i++) {
				if( shortcuts[i]==null){
					shortcuts[i] = Shortcut.UNDEFINED;
				}
			}
			//快捷键数据格式错误，使用默认快捷键
			if( !sucess){
				onCreatePlayerShortCut();				
			}
		} else {
		}
	}

	/**
	 * 清除所有的快捷键，都恢复为“未定义”
	 */
	public void clearShortcut() {
		for (int i = 0; i < shortcuts.length; i++) {
			shortcuts[i] = Shortcut.UNDEFINED;
		}
	}
	
	public void clearSkillShortcur() {
		for (int i = 0; i < shortcuts.length; i++) {
			if (shortcuts[i] instanceof SkillShortcut) {
				shortcuts[i] = Shortcut.UNDEFINED;
			}else if( shortcuts[i] instanceof GongFukillShortcut){
				shortcuts[i] = Shortcut.UNDEFINED;
			}
		}
	}

	public byte[] toByteArray() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutput dout = new DataOutputStream(baos);
		try {
			dout.writeBoolean(numberDirection);
			for (int i = 0; i < shortcuts.length; i++) {
				if (shortcuts[i] instanceof SkillShortcut) {
					SkillShortcut shortcut = (SkillShortcut) shortcuts[i];
					dout.writeByte(Shortcut.SKILL_SHORTCUT);
					dout.writeShort(shortcut.skillId);
				} else if (shortcuts[i] instanceof PropsShortcut) {
					PropsShortcut shortcut = (PropsShortcut) shortcuts[i];
					dout.writeByte(Shortcut.PROPS_SHORTCUT);
					dout.writeLong(shortcut.articleId);
					dout.writeShort(shortcut.iconId);
					dout.writeUTF(shortcut.categoryName);
				} else if (shortcuts[i] instanceof SystemShortcut) {
					SystemShortcut shortcut = (SystemShortcut) shortcuts[i];
					dout.writeByte(Shortcut.SYSTEM_SHORTCUT);
					dout.writeByte(shortcut.functionType);
				}else if( shortcuts[i] instanceof GongFukillShortcut){
					GongFukillShortcut shortcut = (GongFukillShortcut)shortcuts[i];										
					short skillids[] = shortcut.skillIds;
					
					dout.writeByte(Shortcut.GongFu_SHORTCUT);
					dout.writeShort(skillids.length);
					for( int j=0;j< skillids.length;j++){
						dout.writeShort(skillids[j]);
					}				
				} else {
					dout.writeByte(Shortcut.NONE);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] data = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
}

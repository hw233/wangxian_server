package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class Buff_WuFaHuiXue extends Buff {
	@Override
	public void start(Fighter owner) {
		if (owner instanceof Player) {
			Player p = (Player) owner;
			p.setCanNotIncHp(true);

		} else if (owner instanceof Sprite) {
			Sprite s = (Sprite) owner;
			s.setCanNotIncHp(true);
		}
	}

	@Override
	public void end(Fighter owner) {
		if (owner instanceof Player) {
			Player p = (Player) owner;
			p.setCanNotIncHp(false);

		} else if (owner instanceof Sprite) {
			Sprite s = (Sprite) owner;
			s.setCanNotIncHp(false);
		}
	}
}

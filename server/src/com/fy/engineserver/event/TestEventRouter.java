package com.fy.engineserver.event;

import com.fy.engineserver.event.cate.EventPlayerEnterScene;
import com.fy.engineserver.sprite.Player;

public class TestEventRouter implements EventProc{
	
	public static void main(String[] args) {
		new TestEventRouter();
		Player p = new Player();
		p.setName("测试玩家");
		EventPlayerEnterScene epe = new EventPlayerEnterScene(p);
		EventRouter.getInst().addEvent(epe);
		System.out.println("OK");
	}
	
	public TestEventRouter(){
		doReg();
	}

	@Override
	public void proc(Event evt) {
		switch(evt.id){
		case Event.PLAYER_ENTER_SCENE:
			EventPlayerEnterScene epe = (EventPlayerEnterScene) evt;
			playerEnterScene(epe);
			break;
		default:
			EventRouter.log.error("unhandle event {}", evt.id);	
		}
	}

	private void playerEnterScene(EventPlayerEnterScene epe) {
		EventRouter.log.info("玩家{}进入场景",epe.player.getName());
	}

	@Override
	public void doReg() {
		EventRouter.register(Event.PLAYER_ENTER_SCENE, this);
	}

}

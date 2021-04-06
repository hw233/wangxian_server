package com.fy.engineserver.event;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器内部事件管理器。用于逻辑断耦。
 * 
 *         create on 2013年7月29日
 */
public class EventRouter {

	public static int overTime4Log = 5000;
	public long addEventCnt;
	public long procEventCnt;
	public static Logger log = LoggerFactory.getLogger(EventRouter.class);
	private static final Event stick = new Event() {
		public void initId() {
		}
	};
	public static Map<Integer, List<EventProc>> map = new HashMap<Integer, List<EventProc>>();
	public static EventRouter inst;
	public Queue<Event> queue;

	public static EventRouter getInst() {
		if (inst == null) {
			new EventRouter();
			new EventThread().start();
		}
		return inst;
	}

	/**
	 * 请勿调用。
	 */
	public EventRouter() {
		inst = this;
		queue = new ConcurrentLinkedQueue<Event>();
	}

	public void addEvent(Event e) {
		addEventCnt++;
		queue.add(e);
	}

	public static void register(int id, EventProc ep) {
		getInst().addEvent(new EventAddProc(id, ep));
	}

	public void update() {
		queue.add(stick);
		while (true) {
			Event e = queue.poll();
			if (e == stick) {
				break;
			}
			procEventCnt++;
			int id = e.id;
			if (id == Event.EVT_ADD_PROC) {
				EventAddProc eap = (EventAddProc) e;
				register0(eap.realId, eap.ep);
				continue;
			}
			List<EventProc> list = map.get(id);
			if (list == null) {
				log.error("no proc care event {} {}", id, e.getClass().getSimpleName());
				continue;
			}
			int len = list.size();
			for (int i = 0; i < len; i++) {
				EventProc ep = list.get(i);
				try {
					long startTime = System.currentTimeMillis();
					ep.proc(e);
					if ((System.currentTimeMillis() - startTime) > overTime4Log) {
						log.warn("[proc event overTime] [procTime:" + (System.currentTimeMillis() - startTime) + "] [className:" + ep.getClass().getName() + "] [evtid:" + e.id + "]");
					}
				} catch (Throwable t) {
					log.error("Exception when proc event {} id {} by {}", new Object[] { e, id, ep.getClass().getSimpleName() });
					log.error("", t);
				}
			}
		}
	}

	protected void register0(int id, EventProc ep) {
		List<EventProc> list = map.get(id);
		if (list == null) {
			list = new LinkedList<EventProc>();
			map.put(id, list);
		}
		list.add(ep);
	}

	//
	static class EventAddProc extends Event {

		EventProc ep;
		int realId;

		private EventAddProc(int realId, EventProc p) {
			ep = p;
			this.realId = realId;
		}

		@Override
		public void initId() {
			id = EVT_ADD_PROC;
		}

	}
}

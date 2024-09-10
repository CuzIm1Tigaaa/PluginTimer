package de.cuzim1tigaaa.plugintimer.events;

import de.cuzim1tigaaa.plugintimer.Timer;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class TimerPauseEvent extends Event {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private final Timer timer;

	public TimerPauseEvent(Timer timer) {
		this.timer = timer;
	}


	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}
}
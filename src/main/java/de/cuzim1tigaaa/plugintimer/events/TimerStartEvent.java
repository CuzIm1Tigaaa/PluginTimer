package de.cuzim1tigaaa.plugintimer.events;

import de.cuzim1tigaaa.plugintimer.Timer;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class TimerStartEvent extends Event {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private final Timer timer;
	private final boolean resumed;

	public TimerStartEvent(Timer timer, boolean resumed) {
		this.timer = timer;
		this.resumed = resumed;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}
}
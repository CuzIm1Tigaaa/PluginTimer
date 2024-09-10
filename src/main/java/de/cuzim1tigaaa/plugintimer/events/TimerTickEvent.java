package de.cuzim1tigaaa.plugintimer.events;

import de.cuzim1tigaaa.plugintimer.Timer;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class TimerTickEvent extends Event {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private final Timer timer;
	private final long seconds;
	private final boolean countUp;

	public TimerTickEvent(Timer timer, long seconds, boolean countUp) {
		this.timer = timer;
		this.seconds = seconds;
		this.countUp = countUp;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}
}
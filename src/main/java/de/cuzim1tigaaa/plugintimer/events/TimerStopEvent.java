package de.cuzim1tigaaa.plugintimer.events;

import de.cuzim1tigaaa.plugintimer.Timer;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class TimerStopEvent extends Event {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private final Timer timer;
	private final StopReason reason;

	public TimerStopEvent(Timer timer) {
		this.timer = timer;
		this.reason = StopReason.UNKNOWN;
	}

	public TimerStopEvent(Timer timer, StopReason reason) {
		this.timer = timer;
		this.reason = reason;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	public enum StopReason {
		COMMAND, DEATH, FINISHED, EVENT_END, UNKNOWN
	}
}
package de.cuzim1tigaaa.plugintimer.exception;

import de.cuzim1tigaaa.plugintimer.Timer;

public class TimerNotFoundException extends Exception {

	public TimerNotFoundException(Timer timer) {
		super("Timer with name " + timer.getName() + " not found!");
	}

	public TimerNotFoundException() {
		super("No active timer not found!");
	}
}
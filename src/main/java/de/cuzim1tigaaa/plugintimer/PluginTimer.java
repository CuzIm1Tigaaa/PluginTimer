package de.cuzim1tigaaa.plugintimer;

import de.cuzim1tigaaa.plugintimer.commands.TimerCommand;
import de.cuzim1tigaaa.plugintimer.files.Timers;
import de.cuzim1tigaaa.plugintimer.listener.TimerListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class PluginTimer extends JavaPlugin {

	private TimerAPI timerAPI;
	private Timers files;

	@Override
	public void onEnable() {
		this.timerAPI = new TimerAPI(this);
		this.files = new Timers(this);

		new TimerCommand(this);
		new TimerListener(this);
	}

	@Override
	public void onDisable() {
		if(this.timerAPI.getActiveTimer() != null)
			this.timerAPI.stopTimer();
		this.files.saveTimers();
	}
}
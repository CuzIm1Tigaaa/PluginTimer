package de.cuzim1tigaaa.plugintimer.listener;

import de.cuzim1tigaaa.plugintimer.PluginTimer;
import de.cuzim1tigaaa.plugintimer.Timer;
import de.cuzim1tigaaa.plugintimer.events.*;
import de.cuzim1tigaaa.plugintimer.files.Message;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TimerListener implements Listener {

	private final PluginTimer plugin;

	public TimerListener(PluginTimer plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void timerTick(TimerTickEvent event) {
		Timer timer = event.getTimer();

		if(timer.isCountUp())
			return;
		if(timer.getInitialValue() <= 0)
			return;

		if(!timer.isBossbar())
			return;

		BossBar bossBar = timer.getTimerBar();
		Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
		bossBar.setProgress((double) timer.getSeconds() / timer.getInitialValue());
	}

	@EventHandler
	public void timerStart(TimerStartEvent event) {
		Message.broadcast("&7Der Timer wurde &agestartet");
		Timer timer = event.getTimer();
		if(timer.getTimerBar() != null) {
			timer.getTimerBar().setVisible(true);
		}
	}

	@EventHandler
	public void timerStop(TimerStopEvent event) {
		Timer timer = event.getTimer();
		if(timer.getTimerBar() != null) {
			BossBar bossBar = timer.getTimerBar();
			bossBar.setProgress(0);
			Bukkit.getScheduler().runTaskLater(plugin, bossBar::removeAll, 20);
		}
	}
}
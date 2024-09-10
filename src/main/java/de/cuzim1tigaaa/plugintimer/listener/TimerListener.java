package de.cuzim1tigaaa.plugintimer.listener;

import de.cuzim1tigaaa.plugintimer.PluginTimer;
import de.cuzim1tigaaa.plugintimer.Timer;
import de.cuzim1tigaaa.plugintimer.events.*;
import de.cuzim1tigaaa.plugintimer.files.Message;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TimerListener implements Listener {

	private final PluginTimer plugin;

	public TimerListener(PluginTimer plugin1) {
		this.plugin = plugin1;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void timerTick(TimerTickEvent event) {
		Timer timer = event.getTimer();

		if(timer.isCountUp())
			return;

		String msg = null;
		switch((int) event.getSeconds()) {
			case 5 -> msg = "&a5";
			case 4 -> msg = "&24";
			case 3 -> msg = "&63";
			case 2 -> msg = "&42";
			case 1 -> msg = "&c1";
		}

		if(msg != null) {
			final String message = msg;
			Bukkit.getOnlinePlayers().forEach(player ->
					Message.sendTitle(player, new Message.Title(message), new Message.Title(""), 5, 20, 5));
		}

		if(timer.getInitialValue() <= 0)
			return;

		if(!timer.isBossbar())
			return;

		BossBar bossBar = timer.getTimerBar();
		Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
		bossBar.setProgress((double) timer.getSeconds() / timer.getInitialValue());

		switch((int) event.getSeconds()) {
			case 10 -> bossBar.setColor(BarColor.YELLOW);
			case 5 -> bossBar.setColor(BarColor.RED);
		}
	}

	@EventHandler
	public void timerStart(TimerStartEvent event) {
		Message.broadcast("&7Der Timer wurde &agestartet");
		Timer timer = event.getTimer();
		if(timer.getTimerBar() != null) {
			timer.getTimerBar().setColor(BarColor.PURPLE);
			timer.getTimerBar().setVisible(true);
		}
	}

	@EventHandler
	public void timerStop(TimerStopEvent event) {
		Message.broadcast("&7Der Timer wurde &cgestoppt");
	}

	@EventHandler
	public void timerPause(TimerPauseEvent event) {
		Message.broadcast("&7Der Timer wurde &epausiert");
	}
}
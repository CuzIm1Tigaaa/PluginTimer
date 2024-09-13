package de.cuzim1tigaaa.plugintimer;

import de.cuzim1tigaaa.plugintimer.events.*;
import de.cuzim1tigaaa.plugintimer.files.Message;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;

import java.util.HashSet;
import java.util.Set;

public class TimerAPI {

	@Getter
	private final Set<Timer> timers = new HashSet<>();

	private final PluginTimer plugin;
	@Getter
	private boolean paused;
	private Integer taskId;

	public TimerAPI(PluginTimer plugin) {
		this.plugin = plugin;

		this.paused = false;
		this.taskId = null;
	}

	public boolean isRunning() {
		return taskId != null;
	}

	public boolean startTimer() {
		Timer timer = getActiveTimer();
		if(timer == null)
			return false;

		if(this.taskId != null && !this.paused)
			return false;

		TimerStartEvent tSe = new TimerStartEvent(timer, this.paused);

		if(this.paused)
			this.paused = false;

		if(timer.isBossbar() && !timer.isCountUp()) {
			BossBar bossBar = timer.getTimerBar();
			Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
			bossBar.setProgress((double) timer.getSeconds() / timer.getInitialValue());
		}

		Bukkit.getOnlinePlayers().forEach(player -> Message.sendActionBar(player, timer.toString()));
		this.taskId = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			timer.tick();
			Bukkit.getOnlinePlayers().forEach(player -> Message.sendActionBar(player, timer.toString()));

			if(!timer.isCountUp() && timer.getSeconds() <= 0) {
				TimerStopEvent tse = new TimerStopEvent(timer, TimerStopEvent.StopReason.FINISHED);
				stopTimer(false);
				Bukkit.getPluginManager().callEvent(tse);
				return;
			}
			TimerTickEvent tte = new TimerTickEvent(timer, timer.getSeconds(), timer.isCountUp());
			Bukkit.getPluginManager().callEvent(tte);
		}, 20L, 20L).getTaskId();

		Bukkit.getPluginManager().callEvent(tSe);
		return true;
	}

	public void stopTimer(boolean shutdown) {
		Timer timer = getActiveTimer();
		if(timer == null)
			return;

		if(taskId != null)
			Bukkit.getScheduler().cancelTask(taskId);
		this.paused = false;
		this.taskId = null;
	}

	public void pauseTimer() {
		Timer timer = getActiveTimer();
		if(timer == null)
			return;

		if(taskId == null)
			return;

		TimerPauseEvent event = new TimerPauseEvent(timer);
		Bukkit.getScheduler().cancelTask(taskId);
		Bukkit.getPluginManager().callEvent(event);

		this.taskId = Bukkit.getScheduler().runTaskTimer(plugin, () ->
				Bukkit.getOnlinePlayers().forEach(player -> Message.sendActionBar(player, timer.toString())), 0, 20).getTaskId();
		this.paused = true;
	}

	public Timer getActiveTimer() {
		return timers.stream().filter(Timer::isActive).findFirst().orElse(null);
	}

	public void setActiveTimer(Timer timer) {
		timers.forEach(t -> t.setActive(false));
		timer.setActive(true);
	}

	public Timer createTimer(String name, boolean bossbar) {
		Timer timer = new Timer(name, bossbar);
		timers.add(timer);
		return timer;
	}

	public Timer createTimer(String name, int seconds, boolean countUp, boolean active, boolean bossbar) {
		Timer timer = new Timer(name, seconds, countUp, active, bossbar);
		timers.add(timer);
		setActiveTimer(timer);
		return timer;
	}

	public void deleteTimer(String name) {
		Timer timer = getTimer(name);
		if(timer == null)
			return;
		timers.remove(timer);
	}

	public void resetTimer() {
		Timer timer = getActiveTimer();
		if(timer == null)
			return;
		timer.setSeconds(0);
	}

	public Timer getTimer(String name) {
		return getTimers().stream().filter(timer ->
				timer.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
}
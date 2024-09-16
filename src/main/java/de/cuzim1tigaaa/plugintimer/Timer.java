package de.cuzim1tigaaa.plugintimer;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;


public class Timer {

	@Getter @Expose
	private final String name;
	@Getter @Expose
	private long time, initialValue;
	@Getter @Setter @Expose
	private boolean countUp, active, bossbar, seconds;

	private BossBar timerBar;

	public Timer(String name, boolean bossBar, boolean seconds) {
		this(name, 0, true, false, bossBar, seconds);
	}

	public Timer(String name, long time, boolean countUp, boolean active, boolean bossbar, boolean seconds) {
		this.name = name;
		this.time = this.initialValue = time;
		this.countUp = countUp;
		this.active = active;
		this.bossbar = bossbar;
		this.seconds = seconds;

		if(bossbar) {
			this.timerBar = Bukkit.createBossBar("", org.bukkit.boss.BarColor.WHITE, org.bukkit.boss.BarStyle.SOLID);
			return;
		}
		this.timerBar = null;
	}

	public BossBar getTimerBar() {
		if(timerBar != null)
			return timerBar;

		if(bossbar) {
			this.timerBar = Bukkit.createBossBar("", org.bukkit.boss.BarColor.WHITE, org.bukkit.boss.BarStyle.SOLID);
			return timerBar;
		}
		return null;
	}


	public void setTime(long time) {
		this.initialValue = time;
		this.time = time;
	}

	public void tick() {
		time += countUp ? 1 : -1;
	}

	@Override
	public String toString() {
		return formatTime(ChatColor.YELLOW, ChatColor.GRAY);
	}

	public String formatTime(ChatColor primary, ChatColor secondary) {
		int millis = -1;
		long seconds = getTime();

		if(this.seconds) {
			seconds /= 1000;
			millis = (int) (getTime() % 1000);
		}

		int days = (int) (seconds / 86400);
		int hours = (int) (seconds % 86400 / 3600);
		int minutes = (int) (seconds % 3600 / 60);
		int secs = (int) (seconds % 60);

		final String millisFormat =
				millis > -1 ? String.format("%s.%s%03d%s", secondary, primary, millis, secondary) : "";

		if(days > 0) {
			return String.format("%s%s %sTag%s %s%02d%s:%s%02d%s:%s%02d%s",
					primary, days, secondary, days == 1 ? "" : "e", primary, hours,
					secondary, primary, minutes, secondary, primary, secs, millisFormat);
		}

		return String.format("%s%02d%s:%s%02d%s:%s%02d%s", primary, hours,
				secondary, primary, minutes, secondary, primary, secs, millisFormat);
	}
}
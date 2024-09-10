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
	private int seconds, initialValue;
	@Getter @Setter @Expose
	private boolean countUp, active, bossbar;

	private BossBar timerBar;

	public Timer(String name, boolean bossBar) {
		this(name, 0, true, false, bossBar);
	}

	public Timer(String name, int seconds, boolean countUp, boolean active, boolean bossbar) {
		this.name = name;
		this.seconds = this.initialValue = seconds;
		this.countUp = countUp;
		this.active = active;
		this.bossbar = bossbar;

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


	public void setSeconds(int seconds) {
		this.initialValue = seconds;
		this.seconds = seconds;
	}

	public void tick() {
		seconds += countUp ? 1 : -1;
	}

	@Override
	public String toString() {
		return formatTime(ChatColor.YELLOW, ChatColor.GRAY);
	}

	public String formatTime(ChatColor primary, ChatColor secondary) {
		int seconds = getSeconds();
		int days = seconds / 86400;
		int hours = seconds % 86400 / 3600;
		int minutes = seconds % 3600 / 60;
		int secs = seconds % 60;

		if(days > 0)
			return String.format("%s%s %sTag%s %s%02d%s:%s%02d%s:%s%02d%s",
					primary, days, secondary, days == 1 ? "" : "e", primary, hours,
					secondary, primary, minutes, secondary, primary, secs, secondary);
		return String.format("%s%02d%s:%s%02d%s:%s%02d%s",
				primary, hours, secondary, primary, minutes, secondary, primary, secs, secondary);
	}
}
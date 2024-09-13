package de.cuzim1tigaaa.plugintimer.commands;

import de.cuzim1tigaaa.plugintimer.*;
import de.cuzim1tigaaa.plugintimer.events.TimerStopEvent;
import de.cuzim1tigaaa.plugintimer.files.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.*;

import java.util.List;

import static de.cuzim1tigaaa.plugintimer.files.Permissions.*;

public class TimerCommand implements CommandExecutor, TabCompleter {

	private final TimerAPI timerAPI;

	public TimerCommand(PluginTimer plugin) {
		this.timerAPI = plugin.getTimerAPI();
		plugin.getCommand("timer").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if(args.length == 0) {
			Message.sendMessage(sender, "&cUngültiger Befehl, bitte gebe einen Unterbefehl an!");
			return true;
		}

		switch(args[0].toLowerCase()) {
			case "start" -> {
				if(noPermission(sender, TIMER_START))
					return true;

				if(timerAPI.getActiveTimer() == null) {
					Message.sendMessage(sender, "&cBitte wähle einen Timer aus!");
					return true;
				}
				if(timerAPI.isRunning()) {
					Message.sendMessage(sender, "&cDer Timer läuft bereits!");
					return true;
				}
				boolean success = timerAPI.startTimer();
			}
			case "stop" -> {
				if(noPermission(sender, TIMER_STOP))
					return true;

				if(timerAPI.getActiveTimer() == null) {
					Message.sendMessage(sender, "&cBitte wähle einen Timer aus!");
					return true;
				}
				if(!timerAPI.isRunning()) {
					Message.sendMessage(sender, "&cDer Timer läuft nicht!");
					return true;
				}
				TimerStopEvent tse = new TimerStopEvent(timerAPI.getActiveTimer(), TimerStopEvent.StopReason.COMMAND);
				timerAPI.stopTimer(false);
				Bukkit.getPluginManager().callEvent(tse);
			}
			case "pause" -> {
				if(noPermission(sender, TIMER_PAUSE))
					return true;

				if(timerAPI.getActiveTimer() == null) {
					Message.sendMessage(sender, "&cBitte wähle einen Timer aus!");
					return true;
				}
				if(timerAPI.isPaused()) {
					Message.sendMessage(sender, "&cDer Timer ist bereits pausiert!");
					return true;
				}
				if(!timerAPI.isRunning()) {
					Message.sendMessage(sender, "&cDer Timer läuft nicht!");
					return true;
				}
				timerAPI.pauseTimer();
			}
			case "reset" -> {
				if(noPermission(sender, TIMER_RESET))
					return true;

				if(timerAPI.getActiveTimer() == null) {
					Message.sendMessage(sender, "&cBitte wähle einen Timer aus!");
					return true;
				}
				timerAPI.resetTimer();
				Message.sendMessage(sender, "&7Du hast den Timer auf &a0 &7gesetzt!");
			}
			case "create" -> {
				if(noPermission(sender, TIMER_CREATE))
					return true;

				if(args.length == 1) {
					Message.sendMessage(sender, "&cBitte gib einen Namen an!");
					return true;
				}
				String name = args[1];
				if(timerAPI.getTimer(name) != null) {
					Message.sendMessage(sender, "&cEin Timer mit diesem Namen existiert bereits!");
					return true;
				}
				timerAPI.createTimer(name, args.length > 2 && args[2].equalsIgnoreCase("bossbar"));
				Message.sendMessage(sender, "&7Der Timer &a%s &7wurde erstellt!", name);
			}
			case "select" -> {
				if(noPermission(sender, TIMER_SELECT))
					return true;

				if(args.length == 1) {
					Timer timer = timerAPI.getActiveTimer();
					if(timer == null)
						Message.sendMessage(sender, "&cEs ist kein Timer ausgewählt!");
					else
						Message.sendMessage(sender, "&7Aktuell ist der Timer &a%s &7ausgewählt!", timer.getName());
					return true;
				}
				String name = args[1];
				if(timerAPI.getTimer(name) == null) {
					Message.sendMessage(sender, "&cEin Timer mit diesem Namen existiert nicht!");
					return true;
				}
				timerAPI.setActiveTimer(timerAPI.getTimer(name));
				Message.sendMessage(sender, "&7Der Timer &a%s &7wurde ausgewählt!", name);
			}
			case "delete" -> {
				if(noPermission(sender, TIMER_DELETE))
					return true;

				if(args.length == 1) {
					Message.sendMessage(sender, "&cBitte gib einen Namen an!");
					return true;
				}
				String name = args[1];
				if(timerAPI.getTimer(name) == null) {
					Message.sendMessage(sender, "&cEin Timer mit diesem Namen existiert nicht!");
					return true;
				}
				timerAPI.deleteTimer(name);
				Message.sendMessage(sender, "&7Der Timer &a%s &7wurde gelöscht!", name);
			}
			case "list" -> {
				if(noPermission(sender, TIMER_LIST))
					return true;

				Message.sendMessage(sender, "&7Verfügbare Timer:");
				timerAPI.getTimers().forEach(timer -> Message.sendMessage(sender, " - &a%s", timer.getName()));
			}
			case "set" -> {
				if(noPermission(sender, TIMER_RESET))
					return true;

				if(timerAPI.getActiveTimer() == null) {
					Message.sendMessage(sender, "&cBitte wähle einen Timer aus!");
					return true;
				}

				Timer timer = timerAPI.getActiveTimer();
				if(args.length == 1) {
					Message.sendMessage(sender, "&7Der Timer ist aktuell auf &a%s &7Sekunden gesetzt!", timer.getSeconds());
					return true;
				}

				int value = 0;
				try {
					value = Integer.parseInt(args[1]);
				}catch(NumberFormatException ignored) {}

				timer.setSeconds(value);
				Message.sendMessage(sender, "&7Der Timer wurde auf &a%s &7Sekunden gesetzt!", value);
			}
			case "direction" -> {
				if(noPermission(sender, TIMER_DIRECTION))
					return true;

				if(timerAPI.getActiveTimer() == null) {
					Message.sendMessage(sender, "&cBitte wähle einen Timer aus!");
					return true;
				}

				Timer timer = timerAPI.getActiveTimer();
				if(args.length == 1) {
					Message.sendMessage(sender, "&7Der Timer läuft aktuell %s", timer.isCountUp() ? "&avorwärts" : "&crückwärts");
					return true;
				}
				switch(args[1].toLowerCase()) {
					case "up" -> timer.setCountUp(true);
					case "down" -> timer.setCountUp(false);
					case "toggle" -> timer.setCountUp(!timer.isCountUp());
				}
				Message.sendMessage(sender, "&7Der Timer läuft nun %s", timer.isCountUp() ? "&avorwärts" : "&crückwärts");
				return true;
			}
			default -> Message.sendMessage(sender, "&cUngültiger Unterbefehl!");
		}


		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
		if(args.length == 1)
			return List.of("start", "stop", "pause", "reset", "create", "select", "delete", "list", "set", "direction");

		if(args.length == 2)
			switch(args[0].toLowerCase()) {
				case "start", "stop", "pause", "reset", "select", "delete", "list" -> {
					return timerAPI.getTimers().stream().map(Timer::getName).toList();
				}
				case "direction" -> {
					return List.of("up", "down", "toggle");
				}
			}

		if(args.length == 3) {
			switch(args[0].toLowerCase()) {
				case "create" -> {
					return List.of("bossbar");
				}
			}
		}

		return List.of();
	}

	private boolean noPermission(CommandSender sender, String permission) {
		if(!sender.hasPermission(permission)) {
			sender.sendMessage("§cDazu hast du keine Rechte!");
			return true;
		}
		return false;

	}
}
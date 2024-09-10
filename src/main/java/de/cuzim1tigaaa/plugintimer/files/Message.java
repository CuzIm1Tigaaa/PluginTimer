package de.cuzim1tigaaa.plugintimer.files;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

	private static final Pattern pattern = Pattern.compile("(#[a-fA-F0-9]{6})");

	public static void sendMessage(CommandSender sender, String message, Object... args) {
		sender.sendMessage(format(message, args));
	}

	public static void sendTitle(Player player, Title title, Title subTitle, int fadeIn, int stay, int fadeOut) {
		player.sendTitle(format(title.getMessage(), title.getArgs()),
				format(subTitle.getMessage(), subTitle.getArgs()), fadeIn, stay, fadeOut);
	}

	public static void sendActionBar(Player player, String message, Object... args) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(format(message, args)));
	}

	public static String format(String msg, Object... args) {
		Matcher matcher = pattern.matcher(msg);
		while(matcher.find()) {
			String color = msg.substring(matcher.start(), matcher.end());
			msg = msg.replace(color, "" + ChatColor.of(color));
			matcher = pattern.matcher(msg);
		}
		return ChatColor.translateAlternateColorCodes('&', String.format(msg, args));
	}

	public static void broadcast(String message, Object... args) {
		Bukkit.broadcastMessage(format(message, args));
	}


	@Getter
	public static class Title {
		private final String message;
		private final Object[] args;

		public Title(String message, Object... args) {
			this.message = message;
			this.args = args;
		}
	}
}
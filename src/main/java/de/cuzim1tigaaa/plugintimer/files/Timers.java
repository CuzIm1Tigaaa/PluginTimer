package de.cuzim1tigaaa.plugintimer.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import de.cuzim1tigaaa.plugintimer.*;

import java.io.*;
import java.util.List;
import java.util.logging.Level;

public class Timers {

	private final PluginTimer plugin;
	private final TimerAPI timerAPI;

	public Timers(PluginTimer plugin) {
		this.plugin = plugin;
		this.timerAPI = plugin.getTimerAPI();
		this.loadTimers();
	}

	public void saveTimers() {
		try {
			File file = new File(plugin.getDataFolder(), "timers.json");
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}

			FileWriter writer = new FileWriter(file, false);
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

			gson.toJson(timerAPI.getTimers(), writer);
			writer.flush();
			writer.close();
		}catch(IOException exception) {
			plugin.getLogger().log(Level.INFO, "An error occurred while trying to save the timers", exception);
		}
	}

	public void loadTimers() {
		timerAPI.getTimers().clear();
		try {
			File file = new File(plugin.getDataFolder(), "timers.json");
			if(file.exists()) {
				Gson gson = new Gson();
				JsonReader reader = new JsonReader(new FileReader(file));

				Timer[] timers = gson.fromJson(reader, Timer[].class);
				if(timers != null)
					timerAPI.getTimers().addAll(List.of(timers));
			}
		}catch(IOException exception) {
			plugin.getLogger().log(Level.INFO, "An error occurred while trying to load the timers", exception);
		}
	}
}
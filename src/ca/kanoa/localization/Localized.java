package ca.kanoa.localization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Localized {
	
	private final HashMap<String, String> data;
	private final String language;
	private final JavaPlugin plugin;
	
	public Localized(String language, JavaPlugin plugin) throws LanguageNotFoundException {
		this.language = language;
		this.plugin = plugin;
		data = getData();
	}

	private HashMap<String, String> getData() throws LanguageNotFoundException {
		HashMap<String, String> map = new HashMap<String, String>();
		
		File file = new File(plugin.getDataFolder(), "localization" + 
				File.pathSeparatorChar + language + ".txt");
		if (file.isDirectory()) {
			throw new LanguageNotFoundException(language);
		}

		BufferedReader reader = null;
		String[] value;
		String key;
		StringBuilder builder;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String s;
			
			while ((s = reader.readLine()) != null) {
				if (s.contains(":")) {
					value = s.split(":");
					if (value.length < 2) {
						continue;
					}
					key = value[0];
					builder = new StringBuilder();
					for (int i = 1; i < value.length; i++) {
						builder.append(value[i]).append(':');
					}
					map.put(key, builder.toString().trim());
				}
			}
		} catch (FileNotFoundException e) {
			Bukkit.getLogger().warning("Could not find file: " + file.getName());
			throw new LanguageNotFoundException(language);
		} catch (IOException e) {
			e.printStackTrace();
			Bukkit.getLogger().severe("Could not read file: " + file.getName());
			throw new LanguageNotFoundException(language);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return map;
	}

	/**
	 * Retrieves a localized message with variables still in it
	 * @param message The unlocalized name of the message
	 * @return The localized message based on the language of this Object
	 */
	public String getMessage(String unlocalized) {
		return data.get(unlocalized);
	}
	
	/**
	 * Retrieves a message from the language file and replaces %ARG{num}% with each String.
	 * @param message The message the get from the language file.
	 * @param arguments The Strings to replace each %ARG{num}% with.
	 * @return The formatted message gotten from the config.
	 */
	public String getMessageNumbered(String unlocalized, String... arguments) {
		String string = data.get(unlocalized);
		for (int i = 0; i < arguments.length; i++)
			string.replace(String.format("%s%s%s", "%ARG", i, "%"), 
					arguments[i]);
		return string;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public String format(String unlocalized, String constant, String variable) {
		unlocalized = data.get(unlocalized);
		return unlocalized.replace(String.format("%s%s%s", "%", 
				constant.toUpperCase(), "%"), variable);
	}
	
	public String format(String unlocalized, String[] constants, String[] variables) {
		unlocalized = data.get(unlocalized);
		int i = 0;
		
		while (i < constants.length && i < variables.length) {
			unlocalized = unlocalized.replace("%" + constants[i].toUpperCase() + "%", 
					variables[i]);
			i++;
		}
		return unlocalized;
	}
	
}

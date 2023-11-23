package org.ConfigManager;

import de.upb.swtpra1819interface.models.Configuration;
import com.google.gson.*;
import java.io.*;

/**
 * 
 * Manages loading and saving of the config files on hard disk.
 *
 */
public class ConfigManager {

	/**
	 * 
	 * This function saves a provided config as a json file.
	 * 
	 * @param file   File Object in which the config should be saved
	 * @param config Config that should be saved
	 * @return whether the save was successful
	 */
	public static boolean saveConfig(File file, Configuration config) {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		Gson gson = builder.create();
		String jsonString = gson.toJson(config);
		
		File parentFile = file.getParentFile();
		try {
			if (!parentFile.exists() && !parentFile.mkdirs()) {
				throw new IOException();
			}
			if (!file.createNewFile()) {
				file.delete();
				if (!file.createNewFile()) {
					return false;
				}
			}
		} catch (IOException ex) {
			System.out.println("Error creating file '" + file + "'");
			return false;
		}
		try (PrintWriter out = new PrintWriter(file)) {
			out.println(jsonString);
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + file + "'");
			return false;
		}
		return true;
	}

	/**
	 * 
	 * This function loads a config from a provided file.
	 * 
	 * @param file file that should be loaded
	 * @return The loaded Configuration object
	 */
	public static Configuration loadConfig(File file) {
		String pathname = file.getPath();
		try (BufferedReader br = new BufferedReader(new FileReader(pathname))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting();
			Gson gson = builder.create();
			return gson.fromJson(everything, Configuration.class);
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + file + ".json" + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + file + ".json" + "'");
		}
		return null;

	}

}

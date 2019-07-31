package me.badbones69.skript;

import me.badbones69.skript.FileManager.Files;

public class Main {
	
	private static FileManager fileManager = FileManager.getInstance();
	
	public static void main(String[] args) {
		fileManager.logInfo(true).setup();
		//Goes through the Messages option and grabs the full path of each option.
		for(String section : Files.TEST.getFile().getConfigurationSection("Messages").getKeys(true)) {
			//Makes sure the option has a string and isn't just a category spacer.
			if(Files.TEST.getFile().isString("Messages." + section)) {
				//Splits the path at the ".".
				String[] split = section.split("\\.");
				//Grabs the option name after the last "." in the path and makes it uppercase and changes "-" to "_" due to enums not allowing "-".
				String enumName = split[split.length - 1].toUpperCase().replace('-', '_');
				//Gets the default message from that option.
				String defaultString = Files.TEST.getFile().getString("Messages." + section);
				//Prints it into console as "EXAMPLE_ENUM("Path.To.Option", "Default string message"),"
				System.out.println(enumName + "(\"" + section + "\", \"" + defaultString + "\"),");
			}
		}
	}
	
}
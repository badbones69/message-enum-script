package me.badbones69.skript;

import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 *
 * @author BadBones69
 * @version v1.0
 *
 */
public class FileManager {
	
	private static FileManager instance = new FileManager();
	private String prefix = "";
	private Boolean log = false;
	private HashMap<Files, File> files = new HashMap<>();
	private HashMap<Files, FileConfiguration> configurations = new HashMap<>();
	
	public static FileManager getInstance() {
		return instance;
	}
	
	private static String getJarLocation() {
		return new File("").getAbsolutePath();
	}
	
	/**
	 * Sets up the plugin and loads all necessary files.
	 */
	public FileManager setup() {
		prefix = "[CrazyBot] ";
		files.clear();
		//Loads all the normal static files.
		for(Files file : Files.values()) {
			File newFile = new File(getJarLocation(), file.getFileLocation());
			if(log) System.out.println(prefix + "Loading the " + file.getFileName());
			if(!newFile.exists()) {
				try {
					File serverFile = new File(getJarLocation(), "/" + file.getFileLocation());
					InputStream jarFile = getClass().getResourceAsStream("/" + file.getFileLocation());
					copyFile(jarFile, serverFile);
				}catch(Exception e) {
					if(log) System.out.println(prefix + "Failed to load " + file.getFileName());
					e.printStackTrace();
					continue;
				}
			}
			files.put(file, newFile);
			configurations.put(file, YamlConfiguration.loadConfiguration(newFile));
			if(log) System.out.println(prefix + "Successfully loaded " + file.getFileName());
		}
		return this;
	}
	
	/**
	 * Turn on the logger system for the FileManager.
	 * @param log True to turn it on and false for it to be off.
	 */
	public FileManager logInfo(Boolean log) {
		this.log = log;
		return this;
	}
	
	/**
	 * Check if the logger is logging in console.
	 * @return True if it is and false if it isn't.
	 */
	public Boolean isLogging() {
		return log;
	}
	
	/**
	 * Gets the file from the system.
	 * @return The file from the system.
	 */
	public FileConfiguration getFile(Files file) {
		return configurations.get(file);
	}
	
	/**
	 * Saves the file from the loaded state to the file system.
	 */
	public void saveFile(Files file) {
		try {
			configurations.get(file).save(files.get(file));
		}catch(IOException e) {
			System.out.println(prefix + "Could not save " + file.getFileName() + "!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Overrides the loaded state file and loads the file systems file.
	 */
	public void reloadFile(Files file) {
		configurations.put(file, YamlConfiguration.loadConfiguration(files.get(file)));
	}
	
	/**
	 * Was found here: https://bukkit.org/threads/extracting-file-from-jar.16962
	 */
	private void copyFile(InputStream in, File out) throws Exception {
		try(FileOutputStream fos = new FileOutputStream(out)) {
			byte[] buf = new byte[1024];
			int i;
			while((i = in.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		}finally {
			if(in != null) {
				in.close();
			}
			
		}
	}
	
	public enum Files {
		
		//ENUM_NAME("FileName.yml", "FilePath.yml"),
		TEST("test.yml", "test.yml");
		
		private String fileName;
		private String fileLocation;
		
		/**
		 * The files that the server will try and load.
		 * @param fileName The file name that will be in the plugin's folder.
		 * @param fileLocation The location the file is in while in the Jar.
		 */
		private Files(String fileName, String fileLocation) {
			this.fileName = fileName;
			this.fileLocation = fileLocation;
		}
		
		/**
		 * Get the name of the file.
		 * @return The name of the file.
		 */
		public String getFileName() {
			return fileName;
		}
		
		/**
		 * The location the jar it is at.
		 * @return The location in the jar the file is in.
		 */
		public String getFileLocation() {
			return fileLocation;
		}
		
		/**
		 * Gets the file from the system.
		 * @return The file from the system.
		 */
		public FileConfiguration getFile() {
			return getInstance().getFile(this);
		}
		
		/**
		 * Saves the file from the loaded state to the file system.
		 */
		public void saveFile() {
			getInstance().saveFile(this);
		}
		
		/**
		 * Overrides the loaded state file and loads the file systems file.
		 */
		public void relaodFile() {
			getInstance().reloadFile(this);
		}
		
	}
	
}
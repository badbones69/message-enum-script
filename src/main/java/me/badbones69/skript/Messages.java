package me.badbones69.skript;

public enum Messages {
	
	//Past full output here and fix any duplicate named options.
	;
	
	private String path;
	private String defaultMessage;
	
	private Messages(String path, String defaultMessage) {
		this.path = "Messages." + path;
		this.defaultMessage = defaultMessage;
	}
	
}
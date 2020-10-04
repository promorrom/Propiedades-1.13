package us.mytheria.privateproperties;

import org.bukkit.configuration.file.YamlConfiguration;

public class Messages {
	String msg = "Message.";
	String prefix;
	String nopermission;
	String consoledenied;
	String propadded;
	String propremoved;
	String propexists;
	String propnotexists;
	String reload;
	PropertiesMain main;
	public Messages(PropertiesMain main) {
		this.main = main;
	}
	public String applyPrefix(String msg) {
		return prefix + msg;
	}
	public void load() {
		YamlConfiguration langYml = main.fm.getYml(main.fm.lang);
		prefix = langYml.getString("Prefix").replaceAll("&", "§");
		nopermission = langYml.getString(msg + "No-permission").replaceAll("&", "§");
		consoledenied = langYml.getString(msg + "Console-denied").replaceAll("&", "§");
		propadded = langYml.getString(msg + "Prop-added").replaceAll("&", "§");
		propremoved = langYml.getString(msg + "Prop-removed").replaceAll("&", "§");
		propexists = langYml.getString(msg + "Prop-exists").replaceAll("&", "§");
		propnotexists = langYml.getString(msg + "Prop-not-exists").replaceAll("&", "§");
		reload = langYml.getString(msg + "Reload").replaceAll("&", "§");
	}
}

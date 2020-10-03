package us.mytheria.privateproperties;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager {
	PropertiesMain main;
	File path = new File("plugins/PublicProperties");
	public File lang = new File(path.getPath() + "/lang.yml");
	public File properties = new File(path.getPath() + "/Properties");
	public FileManager(PropertiesMain main) {
		this.main = main;
	}
	public void loadFiles() {
		try {
			//File creation
			if (!path.exists()) path.mkdir();
			if (!properties.exists()) properties.mkdir();
			if (!lang.exists()) lang.createNewFile();
			//Lang generation
			YamlConfiguration langYml = getYml(lang);
			if (!langYml.contains("Prefix")) langYml.set("Prefix", "&4[&2Vice&bProperties&4] &7");
			if (!langYml.contains("Message.No-permission")) langYml.set("Message.No-permission", "&7You don't have permissions to this");
			if (!langYml.contains("Message.Console-denied")) langYml.set("Message.Console-denied", "&7You can't use this command in console");
			if (!langYml.contains("Message.Prop-added")) langYml.set("Message.Prop-added", "&7New property added successfully");
			if (!langYml.contains("Message.Prop-removed")) langYml.set("Message.Prop-removed", "&7Property removed successfully");
			if (!langYml.contains("Message.Prop-exists")) langYml.set("Message.Prop-exists", "&7This property already exists");
			if (!langYml.contains("Message.Prop-not-exists")) langYml.set("Message.Prop-not-exists", "&7This property doesn't exists");
			if (!langYml.contains("Message.Reload")) langYml.set("Message.Reload", "&7Plugin was reloaded");
			langYml.save(lang);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void createYml(File f) {
		try {
			f.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public YamlConfiguration getYml(File f) {
		return YamlConfiguration.loadConfiguration(f);
	}
	public void saveYml(YamlConfiguration yml, File f) {
		try {
			yml.save(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

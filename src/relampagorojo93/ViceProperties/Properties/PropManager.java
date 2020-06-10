package relampagorojo93.ViceProperties.Properties;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import relampagorojo93.ViceProperties.PropertiesMain;

public class PropManager {
	PropertiesMain main;
	HashMap<World, HashMap<String, PropData>> properties;
	public PropManager(PropertiesMain main) {
		this.main = main;
		properties = new HashMap<>();
	}
	public void load() {
		for (World world:Bukkit.getWorlds()) {
			properties.put(world, new HashMap<>());
			File f = new File(main.fm.properties.getPath() + "/" + world.getName() + ".yml");
			if (!f.exists()) main.fm.createYml(f);
			YamlConfiguration yml = main.fm.getYml(f);
			for (String region:yml.getConfigurationSection("").getKeys(false)) properties.get(world).put(region, new PropData(yml.getString(region + ".Name"), yml.getInt(region + ".Price"), yml.getInt(region + ".Chests"), yml.getInt(region + ".MembersMax"), (yml.contains(region + ".Home") ? main.locParse(yml.getString(region + ".Home")) : null)));
		}
	}
	public void unload() {
		for (World world:properties.keySet()) {
			File w = new File(main.fm.properties.getPath() + "/" + world.getName() + ".yml");
			if (!w.exists()) main.fm.createYml(w);
			YamlConfiguration yml = main.fm.getYml(w);
			for (String region:properties.get(world).keySet()) {
				PropData pd = properties.get(world).get(region);
				yml.set(region + ".Name", pd.name());
				yml.set(region + ".Price", pd.price());
				yml.set(region + ".Chests", pd.chests());
				yml.set(region + ".MembersMax", pd.membersmax());
				if (pd.home() != null) yml.set(region + ".Home", main.locParse(pd.home()));
			}
			main.fm.saveYml(yml, w);
		}
		properties.clear();
	}
	public List<String> getAllowedRegions(UUID uuid) {
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		List<String> regions = new ArrayList<>();
		for (World w:properties.keySet()) for (String region:properties.get(w).keySet()) {
			RegionManager rm = container.get(BukkitAdapter.adapt(w));
			List<UUID> owners = new ArrayList<>(rm.getRegion(region).getOwners().getUniqueIds());
			List<UUID> members = new ArrayList<>(rm.getRegion(region).getMembers().getUniqueIds());
			if (owners.contains(uuid) || members.contains(uuid)) regions.add(w.getName() + "%" + region);
		}
		return regions;
	}
	public void createProperty(World w, String region) {
		properties.get(w).put(region, new PropData(region, 999, 0, 0, null));
	}
	public boolean hasProperty(World w, String region) {
		return properties.get(w).containsKey(region);
	}
	public PropData getProperty(World w, String region) {
		return properties.get(w).get(region);
	}
	public void removeProperty(World w, String region) {
		properties.get(w).remove(region);
	}
}

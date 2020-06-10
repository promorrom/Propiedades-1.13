package relampagorojo93.ViceProperties;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.realized.tokenmanager.api.TokenManager;
import relampagorojo93.ViceProperties.Inventories.AdminPanel;
import relampagorojo93.ViceProperties.Inventories.InfoPanel;
import relampagorojo93.ViceProperties.Inventories.PropPanel;
import relampagorojo93.ViceProperties.Properties.PropManager;

public class PropertiesMain extends JavaPlugin{
	public String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	//---------------------------------------------------------------------//
	//APIs
	//---------------------------------------------------------------------//
	public TokenManager tm;
	//---------------------------------------------------------------------//
	//Inventories
	//---------------------------------------------------------------------//
	public InfoPanel ipanel;
	public AdminPanel apanel;
	public PropPanel ppanel;
	//---------------------------------------------------------------------//
	//Managers
	//---------------------------------------------------------------------//
	public FileManager fm;
	public PropManager pm;
	public Command command;
	public Messages messages;
	public Events events;
	
	NamespacedKey propiedad = new NamespacedKey(this, "PropertyKey");
	
	public void onEnable() {
		tm = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");
		ipanel = new InfoPanel(this);
		apanel = new AdminPanel(this);
		ppanel = new PropPanel(this);
		fm = new FileManager(this);
		pm = new PropManager(this);
		command = new Command(this);
		messages = new Messages(this);
		events = new Events(this);
		Bukkit.getPluginManager().registerEvents(events, this);
		getCommand("vprop").setExecutor(command);
		new BukkitRunnable() {
			@Override
			public void run() {
				load();
			}
		}.runTask(this);
	}
	public void onDisable() {
		unload();
	}
	public void load() {
		fm.loadFiles();
		messages.load();
		pm.load();
	}
	public void unload() {
		pm.unload();
	}
	//---------------------------------------------------------------------//
	//Utils
	//---------------------------------------------------------------------//
	public String format(int m) {
		String result = "§f";
		String[] i = String.valueOf(m).replace(".", "/").split("/")[0].split("");
		for (int in = 0; in < i.length; in++) {
			result = result + i[in];
			int pos = i.length - 1 - in;
			if ((pos != 0) && (pos % 3 == 0)) {
				result = result + ",";
			}
		}
		result += (m > 1 ? " Permisos" : " Permiso");
		return result;
	}
	public ItemStack setItemData(ItemStack item, String prop) {
		ItemStack i = item.clone();
		ItemMeta im = i.getItemMeta();
		im.getPersistentDataContainer().set(propiedad, PersistentDataType.STRING, prop);
		i.setItemMeta(im);
		return i;
	}
	public String getItemData(ItemStack item) {
		String property = item.clone().getItemMeta().getPersistentDataContainer().get(propiedad, PersistentDataType.STRING);
		return property;
	}
	public String locParse(Location l) {
		return l.getWorld().getName() + "_" + l.getBlockX() + "_" + l.getBlockY() + "_" + l.getBlockZ() + "_" + l.getYaw() + "_" + l.getPitch();
	}
	public Location locParse(String l) {
		String[] loc = l.split("_");
		return new Location(Bukkit.getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3]), Float.parseFloat(loc[4]), Float.parseFloat(loc[5]));
	}
}
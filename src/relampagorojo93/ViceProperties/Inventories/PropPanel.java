package relampagorojo93.ViceProperties.Inventories;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import relampagorojo93.ViceProperties.PropertiesMain;
import relampagorojo93.ViceProperties.Properties.PropData;

public class PropPanel {
	PropertiesMain main;
	public String invname = "§e§lPropiedades";
	public PropPanel(PropertiesMain main) {
		this.main = main;
	}
	public Inventory getInventory(UUID uuid, int p) {
		List<String> regions = main.pm.getAllowedRegions(uuid);
		int max = (int) ((double) regions.size() / 45.99D);
		if (max == 0) max = 1;
		Inventory inv = Bukkit.createInventory(null, 54, invname + " " + p + "/" + max);
		ItemStack bp = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta bpm = bp.getItemMeta();
		bpm.setDisplayName(" ");
		bp.setItemMeta(bpm);
		for (int i = 1; i < 45; i++) inv.setItem(i, bp);
		ItemStack reg = new ItemStack(Material.IRON_DOOR);
		ItemMeta regm = reg.getItemMeta();
		regm.setLore(Arrays.asList("§8§m--------------------------------", " ", "§7Click para teletransportarte", " ", "§eDueño§r: ", " ", "§8§m--------------------------------"));
		for (int i = 0; i < 45 + 9; i++) {
			if (i < 45) {
				if (i + ((p - 1) * 45) < regions.size()) {
					String rawprop = regions.get(i + ((p - 1) * 45));
					String[] prop = rawprop.split("%");
					World w = Bukkit.getWorld(prop[0]);
					PropData data = main.pm.getProperty(w, prop[1]);
					ItemMeta fm = regm.clone();
					fm.setDisplayName(data.name());
					List<String> lore = fm.getLore();
					RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
					RegionManager rm = container.get(BukkitAdapter.adapt(w));
					lore.set(4, fm.getLore().get(4) + Bukkit.getOfflinePlayer(rm.getRegion(prop[1]).getOwners().getUniqueIds().iterator().next()).getName());
					fm.setLore(lore);
					ItemStack fi = reg.clone();
					fi.setItemMeta(fm);
					inv.setItem(i, main.setItemData(fi, rawprop));
				}
			}
			else  inv.setItem(i, bp);
		}
		ItemStack b = new ItemStack(Material.REDSTONE);
		ItemMeta bm = b.getItemMeta();
		bm.setDisplayName("§cCerrar");
		bm.setLore(Arrays.asList("§8§m--------------------------------", " ", "§7Click para cerrar", " ", "§8§m--------------------------------"));
		b.setItemMeta(bm);
		inv.setItem(49, b);
		if (p > 1) {
			ItemStack lp = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
			ItemMeta lm = lp.getItemMeta();
			lm.setDisplayName("§a§l<< IZQUIERDA");
			lp.setItemMeta(lm);
			inv.setItem(45, lp);
		}
		if (p < max) {
			ItemStack rp = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
			ItemMeta rm = rp.getItemMeta();
			rm.setDisplayName("§a§lDERECHA >>");
			rp.setItemMeta(rm);
			inv.setItem(53, rp);
		}
		return inv;
	}
}

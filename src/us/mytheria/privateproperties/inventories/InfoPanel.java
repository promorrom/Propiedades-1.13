package us.mytheria.privateproperties.inventories;

import java.util.ArrayList;
import java.util.Iterator;
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

import us.mytheria.privateproperties.PropertiesMain;
import us.mytheria.privateproperties.properties.PropData;

public class InfoPanel {
	PropertiesMain main;
	public String invname;
	public InfoPanel(PropertiesMain main) {
		this.main = main;
		invname = "§8§lPROPIEDADES";
	}
	public Inventory getInventory(World w, String region) {
		//Creating Inventory
		Inventory inv = Bukkit.createInventory(null, 45, invname);
		//Getting property data
		PropData prop = main.pm.getProperty(w, region);
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionManager rm = container.get(BukkitAdapter.adapt(w));
		Iterator<UUID> owners = rm.getRegion(region).getOwners().getUniqueIds().iterator();
		String owner = owners.hasNext() ? Bukkit.getOfflinePlayer(owners.next()).getName() : null;
		//Refilling with Stained Glass Panes
		ItemStack bp = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta bpm = bp.getItemMeta();
		bpm.setDisplayName(" ");
		bp.setItemMeta(bpm);
		for (int i = 1; i < 45; i++) inv.setItem(i, bp);
		ItemStack ebp = main.setItemData(bp, region);
		inv.setItem(0, ebp);
		//Close Item
		ItemStack c = new ItemStack(Material.RED_DYE);
		ItemMeta cm = c.getItemMeta();
		cm.setDisplayName("§cCerrar");
		List<String> clore = new ArrayList<>();
		clore.add("§8§m----------------------------------");
		clore.add(" ");
		clore.add("§7Click parar cerrar");
		clore.add(" ");
		clore.add("§8§m----------------------------------");
		cm.setLore(clore);
		c.setItemMeta(cm);
		inv.setItem(20, c);
		//Info Item
		ItemStack i = new ItemStack(Material.IRON_DOOR);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName("§eInformación");
		List<String> ilore = new ArrayList<>();
		ilore.add("§8§m----------------------------------");
		ilore.add(" ");
		ilore.add("§7Dueño§f: " + (owner != null ? owner : "Sin dueño"));
		ilore.add("§7Contenedores§f: " + prop.chests());
		ilore.add("§7Máximo de huéspedes§f: " + prop.membersmax());
		ilore.add(" ");
		ilore.add("§8§m----------------------------------");
		im.setLore(ilore);
		i.setItemMeta(im);
		inv.setItem(22, i);
		//Sell-Contact Item
		ItemStack sc;
		List<String> sclore = new ArrayList<>();
		if (owner == null) {
			sc = new ItemStack(Material.SLIME_BALL);
			ItemMeta scm = sc.getItemMeta();
			scm.setDisplayName("§aComprar");
			sclore.add("§8§m----------------------------------");
			sclore.add(" ");
			sclore.add("§7Click para comprar esta propiedad");
			sclore.add(" ");
			sclore.add("§7Precio: "+main.format(prop.price()));
			sclore.add(" ");
			sclore.add("§8§m----------------------------------");
			scm.setLore(sclore);
			sc.setItemMeta(scm);
		}
		else {
			sc = new ItemStack(Material.WRITABLE_BOOK);
			ItemMeta scm = sc.getItemMeta();
			scm.setDisplayName("§aContacto");
			sclore.add("§8§m----------------------------------");
			sclore.add(" ");
			sclore.add("§7Click si tienes interés en");
			sclore.add("§7comprar esta propiedad");
			sclore.add(" ");
			sclore.add("§8§m----------------------------------");
			scm.setLore(sclore);
			sc.setItemMeta(scm);
		}
		inv.setItem(24, sc);
		return inv;
	}
}
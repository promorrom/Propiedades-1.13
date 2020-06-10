package relampagorojo93.ViceProperties.Inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import relampagorojo93.ViceProperties.PropertiesMain;
import relampagorojo93.ViceProperties.Properties.PropData;

public class AdminPanel {
	PropertiesMain main;
	public String invname;
	public AdminPanel(PropertiesMain main) {
		this.main = main;
		invname = "§a§lMANTENIMIENTO";
	}
	public Inventory getInventory(World w, String region) {
		//Creating Inventory
		Inventory inv = Bukkit.createInventory(null, 45, invname);
		//Getting property data
		PropData prop = main.pm.getProperty(w, region);
		//Refilling with Stained Glass Panes
		ItemStack bp = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta bpm = bp.getItemMeta();
		bpm.setDisplayName(" ");
		bp.setItemMeta(bpm);
		for (int i = 1; i < 45; i++) inv.setItem(i, bp);
		ItemStack ebp = main.setItemData(bp, region);
		inv.setItem(0, ebp);
		//Name Item
		ItemStack n = new ItemStack(Material.OAK_SIGN);
		ItemMeta nm = n.getItemMeta();
		nm.setDisplayName("§aNombre: §r" + prop.name());
		n.setItemMeta(nm);
		inv.setItem(19, n);
		//Price Item
		ItemStack p = new ItemStack(Material.GHAST_TEAR);
		ItemMeta pm = p.getItemMeta();
		pm.setDisplayName("§cPrecio: §r" + main.format(prop.price()));
		p.setItemMeta(pm);
		inv.setItem(21, p);
		//Remove Owner Item
		ItemStack r = new ItemStack(Material.BARRIER);
		ItemMeta rm = r.getItemMeta();
		rm.setDisplayName("§4Eliminar dueño");
		r.setItemMeta(rm);
		inv.setItem(22, r);
		//Chest Items
		ItemStack c = new ItemStack(Material.CHEST);
		ItemMeta cm = c.getItemMeta();
		cm.setDisplayName("§6Cofres: §r" + prop.chests());
		c.setItemMeta(cm);
		inv.setItem(23, c);
		ItemStack ic = new ItemStack(Material.LIME_DYE);
		ItemMeta icm = ic.getItemMeta();
		icm.setDisplayName("§aIncrementar cofres");
		ic.setItemMeta(icm);
		inv.setItem(14, ic);
		ItemStack dc = new ItemStack(Material.RED_DYE);
		ItemMeta dcm = dc.getItemMeta();
		dcm.setDisplayName("§cDisminuir cofres");
		dc.setItemMeta(dcm);
		inv.setItem(32, dc);
		//Members Max Items
		ItemStack d = new ItemStack(Material.PLAYER_HEAD, 1);
		ItemMeta dm = d.getItemMeta();
		dm.setDisplayName("§7Máximo de huéspedes: §r" + prop.membersmax());
		d.setItemMeta(dm);
		inv.setItem(25, d);
		ItemStack id = new ItemStack(Material.LIME_DYE);
		ItemMeta idm = id.getItemMeta();
		idm.setDisplayName("§aIncrementar huéspedes");
		id.setItemMeta(idm);
		inv.setItem(16, id);
		ItemStack dd = new ItemStack(Material.RED_DYE);
		ItemMeta ddm = dd.getItemMeta();
		ddm.setDisplayName("§cDisminuir huéspedes");
		dd.setItemMeta(ddm);
		inv.setItem(34, dd);
		return inv;
	}
}
package us.mytheria.privateproperties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import us.mytheria.privateproperties.properties.PropData;

public class Events implements Listener{
	PropertiesMain main;
	
    private static Set<Material> shulkerBoxes = new HashSet<>();
    
    public void toggleDoor(Player player, Location location) {
        Block block = location.getBlock();
        BlockData blockData = block.getBlockData();
        if (blockData instanceof Openable) {
            Openable openable = (Openable) blockData;
            if (openable.isOpen()) {
                if (blockData instanceof Door) {
                    block.getWorld().playSound(block.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1, 1);
                } else if (blockData instanceof TrapDoor) {
                    block.getWorld().playSound(block.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_CLOSE, 1, 1);
                }
                openable.setOpen(false);
            } else {
                if (blockData instanceof Door) {
                    block.getWorld().playSound(block.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 1, 1);
                } else if (blockData instanceof TrapDoor) {
                    block.getWorld().playSound(block.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_OPEN, 1, 1);
                }
                openable.setOpen(true);
            }
            block.setBlockData(openable);
        }
    }
    
    static {
        shulkerBoxes.add(Material.SHULKER_BOX);
        shulkerBoxes.add(Material.WHITE_SHULKER_BOX);
        shulkerBoxes.add(Material.ORANGE_SHULKER_BOX);
        shulkerBoxes.add(Material.MAGENTA_SHULKER_BOX);
        shulkerBoxes.add(Material.LIGHT_BLUE_SHULKER_BOX);
        shulkerBoxes.add(Material.YELLOW_SHULKER_BOX);
        shulkerBoxes.add(Material.LIME_SHULKER_BOX);
        shulkerBoxes.add(Material.PINK_SHULKER_BOX);
        shulkerBoxes.add(Material.GRAY_SHULKER_BOX);
        shulkerBoxes.add(Material.LIGHT_GRAY_SHULKER_BOX);
        shulkerBoxes.add(Material.CYAN_SHULKER_BOX);
        shulkerBoxes.add(Material.PURPLE_SHULKER_BOX);
        shulkerBoxes.add(Material.BLUE_SHULKER_BOX);
        shulkerBoxes.add(Material.BROWN_SHULKER_BOX);
        shulkerBoxes.add(Material.GREEN_SHULKER_BOX);
        shulkerBoxes.add(Material.RED_SHULKER_BOX);
        shulkerBoxes.add(Material.BLACK_SHULKER_BOX);
    }
	
	public HashMap<String, String> name;
	public HashMap<String, String> price;
	public Events(PropertiesMain main) {
		this.main = main;
		name = new HashMap<>();
		price = new HashMap<>();
	}	

    public static boolean isInventoryBlock(Material material) {
        return material == Material.CHEST
                || material == Material.JUKEBOX
                || material == Material.DISPENSER
                || material == Material.FURNACE
                || material == Material.BREWING_STAND
                || material == Material.TRAPPED_CHEST
                || material == Material.HOPPER
                || material == Material.DROPPER
                || material == Material.BARREL
                || material == Material.BLAST_FURNACE
                || material == Material.SMOKER
                || shulkerBoxes.contains(material);
    }
    
    
	@EventHandler
	public void onDoorUsage(PlayerInteractEvent e) {
		if (e.getHand() == EquipmentSlot.HAND) {
			if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.IRON_DOOR) {
				Block block = e.getClickedBlock();
				RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
				for (ProtectedRegion r:container.createQuery().getApplicableRegions(BukkitAdapter.adapt(block.getLocation()))) {
					if (main.pm.hasProperty(block.getWorld(), r.getId())) {
						e.setCancelled(true);
						if (e.getPlayer().hasPermission("ViceProp.Admin") && e.getPlayer().isSneaking()) e.getPlayer().openInventory(main.apanel.getInventory(e.getPlayer().getWorld(), r.getId()));
						else if (r.getOwners().contains(e.getPlayer().getUniqueId()) || r.getMembers().contains(e.getPlayer().getUniqueId())) {
							if (e.getClickedBlock().getType().equals(Material.IRON_DOOR)
									|| e.getClickedBlock().getType().equals(Material.IRON_TRAPDOOR)) {
								this.toggleDoor(e.getPlayer(), e.getClickedBlock().getLocation());
							}
						}
						else e.getPlayer().openInventory(main.ipanel.getInventory(e.getPlayer().getWorld(), r.getId()));
						break;
					}
				}
			}
		}
	}
	@EventHandler
	public void onContainerUsage(PlayerInteractEvent e) {
		if (e.getHand() == EquipmentSlot.HAND) {
			if (e.getClickedBlock() != null) {
				Material material = e.getClickedBlock().getType();
				if (isInventoryBlock(material) && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					Block block = e.getClickedBlock();
					for (ProtectedRegion r:WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(BukkitAdapter.adapt(block.getLocation()))) {
						if (main.pm.hasProperty(block.getWorld(), r.getId())) {
							if (!r.getOwners().contains(e.getPlayer().getUniqueId()) && !e.getPlayer().hasPermission("ViceProperties.Admin")) {
								if (r.getMembers().contains(e.getPlayer().getUniqueId())) {
									/*Location l = block.getLocation();
									Inventory inv = ((Chest) block.getState()).getBlockInventory();
									if (inv instanceof DoubleChestInventory) l = ((DoubleChestInventory) inv).getLocation();*/
								}
								else e.setCancelled(true);
							}
							else if (e.getPlayer().isSneaking()) {
								e.setCancelled(true);
								e.getPlayer().sendActionBar(ChatColor.GOLD+"Próximamente");
							}
							break;
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onInfoChange(AsyncPlayerChatEvent e) {
		String name = e.getPlayer().getName();
		if (this.name.containsKey(name)) {
			e.setCancelled(true);
			String[] data = this.name.get(name).split("%");
			if (e.getMessage().equalsIgnoreCase("Cancelar")) {
				this.name.remove(name);
				e.getPlayer().openInventory(main.apanel.getInventory(Bukkit.getWorld(data[0]), data[1]));
			}
			else {
				main.pm.getProperty(Bukkit.getWorld(data[0]), data[1]).setName(e.getMessage().replace("&", "§"));
				this.name.remove(name);
				e.getPlayer().openInventory(main.apanel.getInventory(Bukkit.getWorld(data[0]), data[1]));
			}
		}
		else if (this.price.containsKey(name)) {
			e.setCancelled(true);
			String[] data = this.price.get(name).split("%");
			if (e.getMessage().equalsIgnoreCase("Cancelar")) {
				this.price.remove(name);
				e.getPlayer().openInventory(main.apanel.getInventory(Bukkit.getWorld(data[0]), data[1]));
			}
			else {
				try {
					int price = Integer.parseInt(e.getMessage());
					main.pm.getProperty(Bukkit.getWorld(data[0]), data[1]).setPrice(price);
					this.price.remove(name);
					e.getPlayer().openInventory(main.apanel.getInventory(Bukkit.getWorld(data[0]), data[1]));
				} catch(NumberFormatException e2) {
					e.getPlayer().sendMessage("Debes introducir un numero!");
				}
			}
		}
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
			Player pl = (Player) e.getWhoClicked();
			Inventory top = pl.getOpenInventory().getTopInventory();
			String invname = pl.getOpenInventory().getTitle();
			if (invname.equals(main.ipanel.invname)) {
				String region = main.getItemData(top.getItem(0));
				RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
				RegionManager rm = container.get(BukkitAdapter.adapt(e.getWhoClicked().getWorld()));
				ProtectedRegion pr = rm.getRegion(region);
				PropData prop = main.pm.getProperty(pl.getWorld(), region);
				e.setCancelled(true);
				switch(e.getSlot()) {
					case 20:
						pl.closeInventory();
						break;
					case 24:
						pl.closeInventory();
						if (pr != null) {
							if (pr.getOwners().getPlayers().isEmpty()) {
								long tokens = main.tm.getTokens(pl).getAsLong();
								long restantes = prop.price() - tokens;
								String hilorestantes = Long.toString(restantes);
								if (tokens >= prop.price()) {
									main.tm.removeTokens(pl.getName(), (long) prop.price(), true);
									pr.getOwners().addPlayer(pl.getUniqueId());
									pl.sendTitle("§a§lAPROBADO", "§7Has comprado " + prop.name(), 20, 40, 20);
								}
								else pl.sendTitle("§c§lDENEGADO", "§7Faltan "+ ChatColor.WHITE + hilorestantes + " §7permisos", 20, 40, 20);
							}
							else pl.sendMessage("Comming soon");
						}else{
							e.getWhoClicked().sendMessage("Error, contacta a Developer (pr == null)");
						}
					default:
						break;
				}
			}
			else if (invname.equals(main.apanel.invname)) {
				String region = main.getItemData(top.getItem(0));
				PropData prop = main.pm.getProperty(pl.getWorld(), region);
				RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
				RegionManager rm = container.get(BukkitAdapter.adapt(e.getWhoClicked().getWorld()));
				e.setCancelled(true);
				switch(e.getSlot()) {
					case 19:
						pl.closeInventory();
						this.name.put(pl.getName(), pl.getWorld().getName() + "%" + region);
						pl.sendTitle("§a§lNOMBRE", "§7Escriba el nuevo nombre", 20, 20, 0);
						new BukkitRunnable() {
							String n = pl.getName();
							public void run() {
								Player pl = Bukkit.getPlayer(n);
								if (pl.isOnline() && name.containsKey(pl.getName())) pl.sendTitle("§a§lNOMBRE", "§7Escriba el nuevo nombre", 0, 20, 20);
								else cancel();
							}
						}.runTaskTimer(main, 20L, 20L);
						break;
					case 21:
						pl.closeInventory();
						this.price.put(pl.getName(), pl.getWorld().getName() + "%" + region);
						pl.sendTitle("§c§lPRECIO", "§7Escriba el nuevo precio", 20, 20, 0);
						new BukkitRunnable() {
							String n = pl.getName();
							public void run() {
								Player pl = Bukkit.getPlayer(n);
								if (pl.isOnline() && price.containsKey(n)) pl.sendTitle("§c§lPRECIO", "§7Escriba el nuevo precio", 0, 20, 20);
								else cancel();
							}
						}.runTaskTimer(main, 20L, 20L);
						break;
					case 22:
						pl.closeInventory();
						rm.getRegion(region).getOwners().clear();
						rm.getRegion(region).getMembers().clear();
						pl.sendTitle("§c§lDUEÑO ELIMINADO", "§7Has eliminado al dueño", 20, 40, 20);
						break;
					case 14:
						prop.addChest();
						top.setContents(main.apanel.getInventory(pl.getWorld(), region).getContents());
						pl.updateInventory();
						break;
					case 32:
						prop.remChest();
						top.setContents(main.apanel.getInventory(pl.getWorld(), region).getContents());
						pl.updateInventory();
						break;
					case 16:
						prop.addMemberMax();
						top.setContents(main.apanel.getInventory(pl.getWorld(), region).getContents());
						pl.updateInventory();
						break;
					case 34:
						prop.remMemberMax();
						top.setContents(main.apanel.getInventory(pl.getWorld(), region).getContents());
						pl.updateInventory();
						break;
					default:
						break;
				}
			}
			else if (invname.startsWith(main.ppanel.invname)) {
				e.setCancelled(true);
				if (e.getSlot() < 45) {
					String rawprop = main.getItemData(e.getCurrentItem());
					if (!rawprop.equals("")) {
						pl.closeInventory();
						String[] prop = rawprop.split("%");
						PropData data = main.pm.getProperty(Bukkit.getWorld(prop[0]), prop[1]);
						if (data.home() != null) pl.teleport(data.home());
						else pl.sendMessage(main.messages.applyPrefix("El dueÃ±o de esta propiedad no ha assignado ninguna ubicaciÃ³n por el momento"));
					}
				}
				else if (!e.getCurrentItem().getType().name().contains("STAINED_GLASS_PANE")) {
					String[] splt = e.getWhoClicked().getOpenInventory().getTitle().split(" ");
					int page = Integer.parseInt(splt[splt.length - 1].split("/")[0]);
					switch(e.getSlot()) {
						case 49:
							pl.closeInventory();
							break;
						case 45:
							pl.openInventory(main.ppanel.getInventory(pl.getUniqueId(), page - 1));
							break;
						case 53:
							pl.openInventory(main.ppanel.getInventory(pl.getUniqueId(), page + 1));
							break;
						default:
							break;
					}
				}
			}
		}
	}
}
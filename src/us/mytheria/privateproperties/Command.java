package us.mytheria.privateproperties;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import us.mytheria.privateproperties.properties.PropData;

public class Command implements CommandExecutor {
	PropertiesMain main;
	

	public ProtectedRegion getRegion(Player p, Location loc, String rg, boolean warning) {
        Location l = (loc != null) ? loc : p.getLocation();
        RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(l.getWorld()));
        if (!rg.isEmpty()) {
          if (rm.hasRegion(rg))
            return rm.getRegion(rg); 
          if (warning)
            p.sendMessage("Region doesn't exists"); 
        } else {
          List<ProtectedRegion> pr = new ArrayList<>(rm.getApplicableRegions(BlockVector3.at(l.getBlockX(), l.getBlockY(), l.getBlockZ())).getRegions());
          if (pr.size() == 0) {
            if (warning)
              p.sendMessage("Not in any protection"); 
          } else {
            return pr.get(0);
          } 
        } 
        return null;
      }
	
	public Command(PropertiesMain main) {
		this.main = main;
	}
	void getCommands(CommandSender sender){
		sender.sendMessage(main.messages.applyPrefix(" §a" + main.getDescription().getVersion()));
		sender.sendMessage("        - §fvprop");
		sender.sendMessage("    §7Consigue todos los comandos");
		sender.sendMessage("        - §fvprop add <rgID>");
		sender.sendMessage("    §7Agrega una nueva propiedad");
		sender.sendMessage("        - §fvprop remove <rgID>");
		sender.sendMessage("    §7Remueve una propiedad");
		sender.sendMessage("        - §fvprop reload");
		sender.sendMessage("    §7Hace un reload");
		sender.sendMessage("        - §fvprop home");
		sender.sendMessage("    §7Revisa tus propiedades");
		sender.sendMessage("        - §fvprop sethome");
		sender.sendMessage("    §7Deja un sethome en tu posición");
	}
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender.hasPermission("PrivateProperties.Manager")) {
			if (args.length != 0 ) {
				if (args[0].equalsIgnoreCase("add")) {
					if (sender.hasPermission("PrivateProperties.Add")) {
						if (sender instanceof Player) {
							Player pl = (Player) sender;
							if (args.length >= 2) {
								if (!main.pm.hasProperty(pl.getWorld(), args[1])) {
									main.pm.createProperty(pl.getWorld(), args[1]);
									sender.sendMessage(main.messages.applyPrefix(main.messages.propadded));
								}
								else sender.sendMessage(main.messages.applyPrefix(main.messages.propexists));
							}
							else sender.sendMessage(main.messages.applyPrefix("/ViceProp Add <Region Name>"));
						}
						else sender.sendMessage(main.messages.applyPrefix(main.messages.consoledenied));
					}
					else sender.sendMessage(main.messages.applyPrefix(main.messages.nopermission));
				}
				else if (args[0].equalsIgnoreCase("remove")) {
					if (sender.hasPermission("PrivateProperties.Remove")) {
						if (sender instanceof Player) {
							Player pl = (Player) sender;
							if (args.length >= 2) {
								if (main.pm.hasProperty(pl.getWorld(), args[1])) {
									main.pm.removeProperty(pl.getWorld(), args[1]);
									sender.sendMessage(main.messages.applyPrefix(main.messages.propremoved));
								}
								else sender.sendMessage(main.messages.applyPrefix(main.messages.propnotexists));
							}
							else sender.sendMessage(main.messages.applyPrefix("/ViceProp Remove <Region Name>"));
						}
						else sender.sendMessage(main.messages.applyPrefix(main.messages.consoledenied));
					}
					else sender.sendMessage(main.messages.applyPrefix(main.messages.nopermission));
				}
				else if (args[0].equalsIgnoreCase("reload")) {
					if (sender.hasPermission("PrivateProperties.Reload")) {
						main.unload();
						main.load();
						sender.sendMessage(main.messages.applyPrefix(main.messages.reload));
					}
					else sender.sendMessage(main.messages.applyPrefix(main.messages.nopermission));
				}
				else if (args[0].equalsIgnoreCase("home")) {
					if (sender instanceof Player) {
						Player pl = (Player) sender;
						if (pl.hasPermission("PrivateProperties.Home")) {
							pl.openInventory(main.ppanel.getInventory(pl.getUniqueId(), 1));
						}else{
						}
					}
					else sender.sendMessage(main.messages.applyPrefix(main.messages.consoledenied));
				}
				else if (args[0].equalsIgnoreCase("sethome")) {
					if (sender.hasPermission("PrivateProperties.SetHome")) {
						if (sender instanceof Player) {
							Player pl = (Player) sender;
							RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
							for (ProtectedRegion region: container.createQuery().getApplicableRegions(BukkitAdapter.adapt(pl.getLocation()))) {
								if (main.pm.hasProperty(pl.getWorld(), region.getId())) {
									PropData data = main.pm.getProperty(pl.getWorld(), region.getId());
									data.setHome(pl.getLocation());
									sender.sendMessage(main.messages.applyPrefix("Nueva ubicación assignada a la propiedad " + data.name()));
									break;
								}
								else sender.sendMessage(main.messages.applyPrefix("No estás dentro de ninguna propiedad"));
							}
						}
						else sender.sendMessage(main.messages.applyPrefix(main.messages.consoledenied));
					}
					else sender.sendMessage(main.messages.applyPrefix(main.messages.nopermission));
				}
			}
		} else {
			sender.sendMessage("Comando desconocido");
		}
		if (sender.hasPermission("PrivateProperties.User")) {
			if (args.length != 0 ) {
				if (args[0].equalsIgnoreCase("addmember")) {
					
				}
			}
		} else {
			sender.sendMessage("Comando desconocido");
		}
		return true;
	}
}
package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

public class CmdBack implements Listener{
	static Plugin plugin;
	public CmdBack(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onTp(PlayerTeleportEvent e){
		if(e.getCause().equals(TeleportCause.COMMAND) || e.getCause().equals(TeleportCause.PLUGIN) || e.getCause().equals(TeleportCause.UNKNOWN)){
			UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(e.getPlayer()));
			Location loc = e.getFrom();
		    String location = loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
		    data.set("back", location);
		    data.save(UltimateFileLoader.getPlayerFile(e.getPlayer()));
		}
	}
	public static void handle(CommandSender sender, String[] args){
        if(!r.isPlayer(sender)){
        	return;
        }
        if(!r.perm(sender, "uc.back", true, true)){
        	return;
        }
        Player p = ((Player) sender);
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
	    if(data.get("back") != null){
		 String[] loc = data.getString("back").split(",");
	        World w = Bukkit.getWorld(loc[0]);
	        Double x = Double.parseDouble(loc[1]);
	        Double y = Double.parseDouble(loc[2]);
	        Double z = Double.parseDouble(loc[3]);
	        float yaw = Float.parseFloat(loc[4]);
	        float pitch = Float.parseFloat(loc[5]);
	        Location location = new Location(w, x, y, z, yaw, pitch);
	        p.teleport(location, TeleportCause.COMMAND);
		    sender.sendMessage(r.mes("Back"));
	    }
	}
}

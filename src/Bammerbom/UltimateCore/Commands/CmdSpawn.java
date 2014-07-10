package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

public class CmdSpawn{
	static Plugin plugin;
	public CmdSpawn(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String label, String[] args){
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.DFspawns);
	    if(data.get("spawn") != null){
		 String[] loc = data.getString("spawn").split(",");
	        World w = Bukkit.getWorld(loc[0]);
	        Double x = Double.parseDouble(loc[1]);
	        Double y = Double.parseDouble(loc[2]);
	        Double z = Double.parseDouble(loc[3]);
	        float yaw = Float.parseFloat(loc[4]);
	        float pitch = Float.parseFloat(loc[5]);
	        Location location = new Location(w, x, y, z, yaw, pitch);
	        if(r.isPlayer(sender)){
	        	Player p = (Player) sender;
	        	if(r.perm(p, "uc.spawn", true, true) == false){
	    			return;
	    		}
	        	p.teleport(location, TeleportCause.COMMAND);
	        }
	    }else{
	    	if(!r.isPlayer(sender)) return;
	    	Player p = (Player) sender;
	    	p.teleport(p.getWorld().getSpawnLocation(), TeleportCause.COMMAND);
	    	return;
	    }
	}
}

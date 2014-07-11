package Bammerbom.UltimateCore.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

public class EventSpawn implements Listener {
	static Plugin plugin;
	public EventSpawn(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onRespawn(final PlayerRespawnEvent e){
		Location dl = e.getPlayer().getLocation();
		UltimateConfiguration data = new UltimateConfiguration(UltimateFileLoader.DFspawns);
	    if(data.get("spawn") != null){
		 String[] loc = data.getString("spawn").split(",");
	        World w = Bukkit.getWorld(loc[0]);
	        Double x = Double.parseDouble(loc[1]);
	        Double y = Double.parseDouble(loc[2]);
	        Double z = Double.parseDouble(loc[3]);
	        float yaw = Float.parseFloat(loc[4]);
	        float pitch = Float.parseFloat(loc[5]);
	        final Location location = new Location(w, x, y, z, yaw, pitch);
	        e.setRespawnLocation(location);
	        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				@Override
				public void run() {
			        e.getPlayer().teleport(location, TeleportCause.ENDER_PEARL);
					
				}
	        	
	        }, 1L);
	        //Back
	        if(r.perm(e.getPlayer(), "uc.back.death", true, false) == false){
    			return;
    		}
	        UltimateConfiguration data2 = new UltimateConfiguration(UltimateFileLoader.getPlayerFile(e.getPlayer()));
			Location loc2 = dl;
		    String location2 = loc2.getWorld().getName() + "," + loc2.getX() + "," + loc2.getY() + "," + loc2.getZ() + "," + loc2.getYaw() + "," + loc2.getPitch();
		    data2.set("back", location2);
		    data2.save(UltimateFileLoader.getPlayerFile(e.getPlayer()));
	        
	        //
	    }else{
	    	
	    }
	}
}

package Bammerbom.UltimateCore.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

public class EventSpawn implements Listener {
	static Plugin plugin;
	public EventSpawn(Plugin instance){
		plugin = instance;
		EventPriority p;
		String s = r.getCnfg().getString("Spawn.priority");
		if(s.equalsIgnoreCase("low")){
			p = EventPriority.LOW;
		}else if(s.equalsIgnoreCase("high")){
			p = EventPriority.HIGH;
		}else if(s.equalsIgnoreCase("highest")){
			p = EventPriority.HIGHEST;
		}else{
			r.log("Spawn priority is invalid.");
			return;
		}
		Bukkit.getPluginManager().registerEvent(PlayerRespawnEvent.class, this, p, new EventExecutor(){
			@Override
			public void execute(Listener l, Event e) throws EventException {
				onRespawn((PlayerRespawnEvent) e);
			}
		}, plugin);
		Bukkit.getPluginManager().registerEvents((Listener) this, instance);
	}
	public void onRespawn(final PlayerRespawnEvent e){
	    Location bed = e.getPlayer().getBedSpawnLocation();
	    if (bed != null){
	        e.setRespawnLocation(bed);
	    	return;
	    }
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
	        //Back
	        if(r.perm(e.getPlayer(), "uc.back.death", true, false) == false){
    			return;
    		}
	        //e.getPlayer().sendMessage(ChatColor.GREEN + "> Back location set: " + dl.getWorld() + " " + dl.getBlockX() + " " + dl.getBlockY() + " " + dl.getBlockZ());
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

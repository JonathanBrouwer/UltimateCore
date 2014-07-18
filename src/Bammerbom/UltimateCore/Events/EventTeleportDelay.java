package Bammerbom.UltimateCore.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class EventTeleportDelay implements Listener{
	
	static Plugin plugin;
	public EventTeleportDelay(Plugin instance){
		plugin = instance;
		if(!r.getCnfg().contains("Teleport") || !r.getCnfg().getBoolean("Teleport.EnableDelay")) return;
		delay = r.getCnfg().getInt("Teleport.Delay");
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	Integer delay = 0;
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onTeleport(final PlayerTeleportEvent e){
		if(e.getCause().equals(TeleportCause.COMMAND)){
			if(r.perm(e.getPlayer(), "uc.tp.timer.bypass", false, false)) return;
			final Location loc = e.getPlayer().getLocation().getBlock().getLocation();
			final Location to = e.getTo();
			e.setCancelled(true);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run(){
					e.getPlayer().sendMessage(ChatColor.GRAY + "Teleporting in " + delay + " seconds...");
				}
			});
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				@Override
				public void run() {
					if(e.getPlayer().getLocation().getBlock().getLocation().equals(loc)){
					    e.getPlayer().teleport(to);
					    e.getPlayer().sendMessage(ChatColor.GREEN + "Teleportation succeed!");
					}else{
						e.getPlayer().sendMessage(ChatColor.DARK_RED + "Teleportation failed, you moved!");
					}
				}
			}, 20L * delay);
		}
	}
}

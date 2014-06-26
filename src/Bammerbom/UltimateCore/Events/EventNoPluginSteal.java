package Bammerbom.UltimateCore.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class EventNoPluginSteal implements Listener{
	
	static Plugin plugin;
	public EventNoPluginSteal(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@EventHandler
	public void onPluginSteal(PlayerCommandPreprocessEvent e){
		String m = e.getMessage().startsWith("/") ? e.getMessage().replaceFirst("/", "") : e.getMessage();
		if(m.equalsIgnoreCase("plugins") || m.equalsIgnoreCase("bukkit:plugins") || m.equalsIgnoreCase("pl") || m.equalsIgnoreCase("bukkit:pl")){
			if(!r.perm(e.getPlayer(), "uc.plugins", false, true)) e.setCancelled(true);
		}
		if(m.equalsIgnoreCase("?") || m.equalsIgnoreCase("bukkit:?") || m.equalsIgnoreCase("help") || m.equalsIgnoreCase("bukkit:help")){
			if(!r.perm(e.getPlayer(), "uc.help", false, true)) e.setCancelled(true);
		}
	}
}

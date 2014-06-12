package Bammerbom.UltimateCore.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;

public class EventMOTD implements Listener{
	static Plugin plugin;
	public EventMOTD(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void ColoredMotdMaker(ServerListPingEvent e){
		e.setMotd(ChatColor.translateAlternateColorCodes('&', (e.getMotd())));
	}
	
}

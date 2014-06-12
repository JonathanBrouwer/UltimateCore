package Bammerbom.UltimateCore;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class t implements Listener{
	Plugin plugin;
	public t(Plugin instance){
		plugin = instance;
		instance.getServer().getPluginManager().registerEvents(this, instance);
	}
	public void enable(){
		
	}
	@EventHandler
	public void interact(PlayerInteractEvent e){
		@SuppressWarnings("unused")
		Player p = e.getPlayer();
	}
}

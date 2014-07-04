package Bammerbom.UltimateCore;

import org.bukkit.Material;
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
		if(!e.getPlayer().getItemInHand().getType().equals(Material.STONE)) return;
		//Player p = e.getPlayer(); 
		
	}
}

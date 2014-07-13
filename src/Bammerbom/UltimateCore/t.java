package Bammerbom.UltimateCore;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class t implements Listener{
	Plugin plugin;
	public t(Plugin instance){
		plugin = instance;
		//instance.getServer().getPluginManager().registerEvents(this, instance);
	}
}

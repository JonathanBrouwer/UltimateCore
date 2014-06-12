package Bammerbom.UltimateCore.Events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateCore;
import Bammerbom.UltimateCore.r;

public class EventAutosave{
	static Plugin plugin;
	public EventAutosave(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		startAutoSave(instance);
	}
	public static void startAutoSave(final Plugin plugin){
		if(plugin.getConfig().getBoolean("Autosave.enabled") == false){ return; }
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, 
				new Runnable(){

					@Override
					public void run() {
						if(plugin.getConfig().getBoolean("Autosave.message") == true){
							Bukkit.broadcastMessage(r.mes("Save.Start"));
							for(World w : Bukkit.getWorlds()){
								w.save();
							}
							UltimateCore.getSQLdatabase().save();
							Bukkit.broadcastMessage(r.mes("Save.Done"));
						}
						
					}
			
		},plugin.getConfig().getInt("Autosave.time") * 20, plugin.getConfig().getInt("Autosave.time") * 20);
		
	}
}

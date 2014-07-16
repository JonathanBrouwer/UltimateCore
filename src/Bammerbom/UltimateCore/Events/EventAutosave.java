package Bammerbom.UltimateCore.Events;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

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
		if(r.getCnfg().getBoolean("Autosave.enabled") == false){ return; }
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, 
				new Runnable(){

					@Override
					public void run() {
						if(r.getCnfg().getBoolean("Autosave.message") == true){
							Bukkit.broadcastMessage(r.mes("Save.Start"));
							for(World w : Bukkit.getWorlds()){
								w.save();
								for(Chunk chunk : w.getLoadedChunks()){
									try{
									chunk.unload(true, true);
									}catch(Exception ex){
										return;
									}
								}
							}
							Bukkit.broadcastMessage(r.mes("Save.Done"));
						}else{
							for(World w : Bukkit.getWorlds()){
								w.save();
								for(Chunk chunk : w.getLoadedChunks()){
									try{
									chunk.unload(true, true);
									}catch(Exception ex){
										return;
									}
								}
							}
						}
						
					}
			
		},r.getCnfg().getInt("Autosave.time") * 20, r.getCnfg().getInt("Autosave.time") * 20);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			public void run(){
				for(World w : Bukkit.getWorlds()){
					for(Chunk chunk : w.getLoadedChunks()){
						try{
						chunk.unload(true, true);
						}catch(Exception ex){
							return;
						}
					}
				}
			}
		}, 60 * 20, 60 * 20);
	}
}

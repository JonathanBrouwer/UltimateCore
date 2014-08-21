package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdReload{
	static Plugin plugin;
	public CmdReload(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		
	}
	public static void reload(final CommandSender sender, String[] args){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				if(!r.perm(sender, "uc.reload", false, true)){
					return;
				}
				try{
				Bukkit.broadcastMessage(r.mes("Reload.Start").replaceAll("%Player", sender.getName()));
				Bukkit.getServer().reload();
				Bukkit.broadcastMessage(r.mes("Reload.Done").replaceAll("%Player", sender.getName()));
				}catch(Exception ex){
					Bukkit.getServer().reload();
				}
			}
		}, 1L);
    }

}

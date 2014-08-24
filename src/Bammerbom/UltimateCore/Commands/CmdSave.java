package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdSave{
	static Plugin plugin;
	public CmdSave(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
		if(!r.perm(sender, "uc.save", false, true)){
			return;
		}
		if(r.getCnfg().getBoolean("Autosave.message") == true){
			Bukkit.broadcastMessage(r.mes("Save.Start"));
		}else{
			sender.sendMessage(r.mes("Save.Start"));
		}
		for(World w : Bukkit.getWorlds()){
			w.save();
		}
		//UltimateCore.getSQLdatabase().save();
		if(r.getCnfg().getBoolean("Autosave.message") == true){
			Bukkit.broadcastMessage(r.mes("Save.Done"));
		}else{
			sender.sendMessage(r.mes("Save.Done"));
		}
	}
}

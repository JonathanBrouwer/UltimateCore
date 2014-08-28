package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.MobType;

public class CmdRemoveAll{
	static Plugin plugin;
	public CmdRemoveAll(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@SuppressWarnings("deprecation")
	public static void handle(CommandSender sender, String[] args){
		if(!r.isPlayer(sender)){
			return;
		}
		if(!r.perm(sender, "uc.killall", false, true)){
			return;
		}
		Integer range = 500;
		if(r.checkArgs(args, 0) == true && r.isNumber(args[0])){
			range = Integer.parseInt(args[0]);
			if(range > 10000){
				range = 10000;
			}
		}else if(r.checkArgs(args, 1) && r.isNumber(args[1])){
			range = Integer.parseInt(args[1]);
			if(range > 10000){
				range = 10000;
			}
		}
		EntityType et = null;
		if(r.checkArgs(args, 0)){
			if(EntityType.fromName(args[0].toUpperCase()) != null) et = EntityType.fromName(args[0].toUpperCase());
			if(EntityType.fromName(args[0].replaceAll("_", "").toUpperCase()) != null) et = EntityType.fromName(args[0].replaceAll("_", "").toUpperCase());
			if(MobType.fromName(args[0]) != null) et = MobType.fromName(args[0]).getType();
			
		}
		Player p = (Player) sender;
		Integer amount = 0;
		for(Entity en : p.getNearbyEntities(range,256,range)){
			if((en instanceof Painting) || (en instanceof ItemFrame) || (en instanceof Player)) continue;
		    if(et != null && !en.getType().equals(et)) continue;
			en.remove();
			amount++;
		}
		p.sendMessage(r.mes("RemoveAll").replaceAll("%Amount", amount + "").replaceAll("%Radius", range + ""));
	}
}

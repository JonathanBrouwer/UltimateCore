package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdKillAll{
	static Plugin plugin;
	public CmdKillAll(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.isPlayer(sender)){
			return;
		}
		if(!r.perm(sender, "uc.killall", false, true)){
			return;
		}
		Integer range = 500;
		if(r.checkArgs(args, 0) == true){
			if(r.isNumber(args[0])){
			range = Integer.parseInt(args[0]);
			if(range > 10000){
				range = 10000;
			}
			}
		}
		
		Player p = (Player) sender;
		Integer amount = 0;
		for(Entity en : p.getNearbyEntities(range,256,range)){
			if(en instanceof LivingEntity && !(en instanceof Player)){
				en.remove();
				amount++;
				en.playEffect(EntityEffect.DEATH);
			}
		}
		p.sendMessage(r.mes("KillAll").replaceAll("%Amount", amount + "").replaceAll("%Radius", range + ""));
	}
}

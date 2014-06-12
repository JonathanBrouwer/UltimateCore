package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.MobType;

public class CmdButcher{
	static Plugin plugin;
	public CmdButcher(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.isPlayer(sender)){
			return;
		}
		if(!r.perm(sender, "uc.butcher", false, true)){
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
				MobType mob = MobType.fromBukkitType(en.getType());
				if(mob.type.equals(MobType.Enemies.ENEMY) || en instanceof Monster){
					en.remove();
					amount++;
					en.playEffect(EntityEffect.DEATH);
				}
			}
		}
		p.sendMessage(r.mes("Butcher").replaceAll("%Amount", amount + "").replaceAll("%Radius", range + ""));
	}
}

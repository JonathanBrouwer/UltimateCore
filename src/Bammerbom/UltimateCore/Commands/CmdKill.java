package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;

public class CmdKill{
	static Plugin plugin;
	public CmdKill(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@SuppressWarnings("deprecation")
	public static void handle(CommandSender sender, String[] args) {
		if(r.checkArgs(args, 0) == false){
			if(!r.isPlayer(sender)){
				return;
			}
				if(!r.perm(sender, "uc.kill", true, true)){
					return;
				}
				Player p = (Player) sender;
			p.setLastDamageCause(new EntityDamageEvent(p, DamageCause.SUICIDE, Double.MAX_VALUE));
			p.setHealth(0.0);
		}
		else{
			if(!r.perm(sender, "uc.kill.others", false, true)){
				return;
			}
			Player target = UC.searchPlayer(args[0]);
			if(target == null){
				sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
				return;
			}	
			else{
				target.sendMessage(r.mes("Kill.target").replaceAll("%Player", sender.getName()));
				sender.sendMessage(r.mes("Kill.killer").replaceAll("%Player", target.getName()));

				target.setLastDamageCause(new EntityDamageEvent(target, DamageCause.CUSTOM, Double.MAX_VALUE));
				target.setHealth(0.0);
			}
			
		}
	}
}



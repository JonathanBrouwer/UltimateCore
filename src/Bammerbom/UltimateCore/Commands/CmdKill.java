package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdKill{
	static Plugin plugin;
	public CmdKill(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
		if(r.checkArgs(args, 0) == false){
			if(!(r.isPlayer(sender))){
				return;
			}
			if(sender instanceof Player){
				if(!r.perm(sender, "uc.kill", true, true)){
					return;
				}
				Player p = (Player) sender;
			p.setLastDamageCause(new EntityDamageEvent(p, DamageCause.SUICIDE, 0.0));
			p.setHealth(0.0);
			p.sendMessage(r.mes("Kill.Suicide"));
			}
		}
		else{
			if(!r.perm(sender, "uc.kill.others", false, true)){
				return;
			}
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null){
				sender.sendMessage(r.mes("PlayerNotFoundError").replaceAll("%Player", args[0]));
				return;
			}	
			else{
				target.sendMessage(r.mes("Kill.target").replaceAll("%Player", sender.getName()));
				sender.sendMessage(r.mes("Kill.killer").replaceAll("%Player", target.getName()));
				target.setLastDamageCause(new EntityDamageEvent(target, DamageCause.CUSTOM, 0.0));
				target.setHealth(0.0);
			}
			
		}
	}
}



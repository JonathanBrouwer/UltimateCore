package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import Bammerbom.UltimateCore.r;

public class CmdFullheal{
	static Plugin plugin;
	public CmdFullheal(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
 
	public static void handle(CommandSender sender, String[] args) {
		if(!(r.isPlayer(sender))){
			return;
		}
		if(!r.perm(sender, "uc.fullheal", false, true)){
			return;
		}
		if(sender instanceof Player){
		Player p = (Player)sender;
		if(r.checkArgs(args, 0) == false){
			p.setHealth(((Damageable)p).getMaxHealth());
			for(PotionEffect effects: p.getActivePotionEffects()){
	            p.removePotionEffect(effects.getType());              
	        }
			p.setFoodLevel(20);
			p.setFireTicks(0);
			p.setRemainingAir(p.getMaximumAir());
			p.sendMessage(r.mes("Heal.Message"));
		}
		else{
			if(!r.perm(sender, "uc.fullheal.others", false, true)){
				return;
			}
			Player target = r.searchPlayer(args[0]);
			if(target == null || !target.isOnline()){
				p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
				return;
			}	
			else{
				target.sendMessage(r.mes("Heal.ToOther").replaceAll("%Player", p.getName()));
				p.sendMessage(r.mes("Heal.ToSelf").replaceAll("%Player", target.getName()));
				for(PotionEffect effect : target.getActivePotionEffects())
				{
				    target.removePotionEffect(effect.getType());
				}
				target.setHealth(((Damageable)target).getMaxHealth());
				target.setFoodLevel(20);
				target.setFireTicks(0);
				target.getActivePotionEffects().clear();
				target.setRemainingAir(target.getMaximumAir());
			}
		}
		}
	}
}

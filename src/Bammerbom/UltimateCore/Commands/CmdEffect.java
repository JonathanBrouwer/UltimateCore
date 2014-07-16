package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.Resources.MapPotionEffects;

public class CmdEffect{
	static Plugin plugin;
	public CmdEffect(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.effect", false, true)) return;
		if(!r.isPlayer(sender)) return;
		if(!r.checkArgs(args, 1)){
			sender.sendMessage(r.mes("Effect.Usage"));
			return;
		}
		Player p = (Player) sender;
		Player t = UC.searchPlayer(args[0]);
		if(t == null){
			p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		PotionEffectType ef = MapPotionEffects.getByName(args[1]);
		if(ef == null){
			if(args[1].equalsIgnoreCase("clear")){
				for(PotionEffect effect : t.getActivePotionEffects()){
					t.removePotionEffect(effect.getType());
				}
				sender.sendMessage(r.mes("Effect.Clear").replaceAll("%Target", args[0]));
				return;
			}
			sender.sendMessage(r.mes("Effect.EffectNotFound").replaceAll("%Effect", args[1]));
			return;
		}
		Integer dur = 120;
		Integer lev = 1;
		if(r.checkArgs(args, 2)){
			if(!r.isNumber(args[2])){
				sender.sendMessage(r.mes("Effect.AmountNotValid").replaceAll("%Amount", args[2]));
				return;
			}
			dur = Integer.parseInt(args[2]);
		}
		if(r.checkArgs(args, 3)){
			if(!r.isNumber(args[3])){
				sender.sendMessage(r.mes("Effect.AmountNotValid").replaceAll("%Amount", args[3]));
				return;
			}
			lev = Integer.parseInt(args[3]);
		}
		lev = r.normalize(lev, 0, 999999);
		dur = r.normalize(dur, 0, 999999);
		if(lev == 0 || dur == 0){
			t.removePotionEffect(ef);
			sender.sendMessage(r.mes("Effect.Succes").replaceAll("%Effect", ef.getName().toLowerCase()).replaceAll("%Target", t.getName()).replaceAll("%Duration", dur + "").replaceAll("%Level", lev + ""));
			return;
		}
		t.removePotionEffect(ef);
		PotionEffect effect = new PotionEffect(ef, dur * 20, lev - 1);
		t.addPotionEffect(effect);
		sender.sendMessage(r.mes("Effect.Succes").replaceAll("%Effect", ef.getName().toLowerCase()).replaceAll("%Target", t.getName()).replaceAll("%Duration", dur + "").replaceAll("%Level", lev + ""));
	}
}

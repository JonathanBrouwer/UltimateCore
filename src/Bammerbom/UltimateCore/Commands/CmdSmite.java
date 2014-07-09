package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdSmite{
	static Plugin plugin;
	public CmdSmite(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@SuppressWarnings("deprecation")
	public static void handle(CommandSender sender, String[] args) {
    	if(r.checkArgs(args, 0)){
    		if(!r.perm(sender, "uc.smite.others", false, true)){
    			return;
    		}
    		Player target = Bukkit.getPlayer(args[0]);
    		if(target == null){
    			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
    			return;
    		}
    		Location tPlayerLocation = target.getLocation();
    		if(r.getCnfg().getBoolean("SmiteDamage") == false){
    		    target.getWorld().strikeLightningEffect(tPlayerLocation);
    		}else{
    			target.getWorld().strikeLightning(tPlayerLocation);
    		}
    	}else{
    		if(!r.perm(sender, "uc.smite", false, true)){
    			return;
    		}
    		if(!(r.isPlayer(sender))){
    			return;
    		}
    		Player p = (Player) sender;
    		Block strike = p.getTargetBlock(null, 150);
    		Location strikel = strike.getLocation();
    		if(r.getCnfg().getBoolean("SmiteDamage") == false){
    		    p.getWorld().strikeLightningEffect(strikel);
    		}else{
    			p.getWorld().strikeLightning(strikel);
    		}
    	}
	}
	@SuppressWarnings("deprecation")
	public static void handle2(CommandSender sender, String[] args) {
    	if(r.checkArgs(args, 0)){
    		if(!r.perm(sender, "uc.megasmite.others", false, true)){
    			return;
    		}
    		Player target = Bukkit.getPlayer(args[0]);
    		if(target == null){
    			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
    			return;
    		}
    		Location tPlayerLocation = target.getLocation();
    		if(r.getCnfg().getBoolean("SmiteDamage") == false){
    			for (int i = 0; i < 20; i++){
    		    target.getWorld().strikeLightningEffect(tPlayerLocation);
    			}
    		}else{
    			for (int i = 0; i < 20; i++){
    			target.getWorld().strikeLightning(tPlayerLocation);
    			}
    		}
    	}else{
    		if(!r.perm(sender, "uc.megasmite", false, true)){
    			return;
    		}
    		if(!(r.isPlayer(sender))){
    			return;
    		}
    		Player p = (Player) sender;
    		Block strike = p.getTargetBlock(null, 150);
    		Location strikel = strike.getLocation();
    		if(r.getCnfg().getBoolean("SmiteDamage") == false){
    			for (int i = 0; i < 20; i++){
    		    p.getWorld().strikeLightningEffect(strikel);
    			}
    		}else{
    			for (int i = 0; i < 20; i++){
    			p.getWorld().strikeLightning(strikel);
    			}
    		}
    	}
	}
}

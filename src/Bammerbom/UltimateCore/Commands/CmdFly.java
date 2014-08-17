package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdFly{
	static Plugin plugin;
	public CmdFly(Plugin instance){
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
    		if(!r.perm(sender, "uc.fly", false, true)){
    			return;
    		}
    		Player p = (Player) sender;
    	    if(p.getAllowFlight() == true){
    		    p.setAllowFlight(false);
    		    p.sendMessage(r.mes("Fly.Message").replaceAll("%Status", "off"));
    	    }
    	    else{
    		    p.setAllowFlight(true);
    		    p.setFlySpeed(0.1F);
    		    p.sendMessage(r.mes("Fly.Message").replaceAll("%Status", "on"));
    	    }
    	}else{
    		if(!r.perm(sender, "uc.fly.others", false, true)){
    			return;
    		}
    		CommandSender p = sender;
    		Player target = r.searchPlayer(args[0]);
    		if(target !=null){
    	    if(target.getAllowFlight() == true){
    		    target.setAllowFlight(false);
    		    target.sendMessage(r.mes("Fly.toOther").replaceAll("%Status", "off"));
    		    p.sendMessage(r.mes("Fly.toSelf").replaceAll("%Status", "off").replaceAll("%Player", target.getName()));
    	    }
    	    else{
    		    target.setAllowFlight(true);
    		    target.sendMessage(r.mes("Fly.toOther").replaceAll("%Status", "on"));
    		    p.sendMessage(r.mes("Fly.toSelf").replaceAll("%Status", "on").replaceAll("%Player", target.getName()));
    	    }
    		}else{
    			p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
    		}
    		
    	}
	}
}

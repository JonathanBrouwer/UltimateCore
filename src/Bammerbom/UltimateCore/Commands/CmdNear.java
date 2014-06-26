 package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdNear{
	static Plugin plugin;
	public CmdNear(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String label, String[] args){
		if(!r.perm(sender, "uc.near", true, true)){
			return;
		}
	    if(!r.isPlayer(sender)) return;
	    Player p = (Player) sender;
	    StringBuilder builder = new StringBuilder();
	    builder.append(r.default1 + "Nearby players: " + r.default2);
	    Boolean a = true;
	    Integer b = 0;
	    for(Entity e : p.getNearbyEntities(250.0, 256.0, 250.0)){
	    	if(e instanceof Player){
	    		Player t = (Player) e;
	    		if(!a) builder.append(", ");
	    		builder.append(t.getName());
	    		builder.append("(" + Double.valueOf(t.getLocation().distance(p.getLocation())).intValue() + ")");
	    		a = false;
	    		b++;
	    	}
	    }
	    if(a) builder.append(r.default2 + "none");
	    sender.sendMessage(builder.toString());
	}
	
	//
	
}

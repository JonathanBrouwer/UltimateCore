package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdMore{
	static Plugin plugin;
	public CmdMore(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
    public static void handle(CommandSender sender, String[] args){
    	if(!r.isPlayer(sender)){
    		return;
    	}
    	if(!r.perm(sender, "uc.more", false, true)){
    		return;
    	}
    	Player p = (Player) sender;
    	if(p.getItemInHand() == null){ return; }
    	if(p.getItemInHand().getType() == null){ return; }
    	if(p.getItemInHand().getType().equals(Material.AIR)){ return; }
        p.getItemInHand().setAmount(64);
    	p.sendMessage(r.mes("More"));
    }
}

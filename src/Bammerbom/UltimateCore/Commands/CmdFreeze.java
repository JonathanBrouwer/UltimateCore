package Bammerbom.UltimateCore.Commands;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

public class CmdFreeze implements Listener {
	static Plugin plugin;
	public CmdFreeze(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		for(OfflinePlayer p : Bukkit.getOfflinePlayers()){
			if(Freeze(p)){
				frozen.add(p.getUniqueId().toString());
			}
		}
	}
	static ArrayList<String> frozen = new ArrayList<String>();
	public static void handle(CommandSender sender, String[] args) {
		if(!r.isPlayer(sender)){
			return;
		}
		Player p = (Player)sender;
		if(r.checkArgs(args, 0) == true){
			Player target = Bukkit.getPlayer(args[0]);
	        if(target==null){
	    	    p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
	        }
	        else{
				if(p.hasPermission("uc.antiban")){
					sender.sendMessage(r.mes("AntiBan").replaceAll("%Player", p.getName()).replaceAll("%Action", "freeze"));
					return;
				}
			        Freeze(target, true);
			        p.sendMessage(r.mes("Freeze.Freezed").replaceAll("%Player", target.getName()));
	        }
	    }
	    else{
	    	p.sendMessage(r.mes("Freeze.Usage"));
	    }
	}
	public static void unfreeze(CommandSender sender, String[] args){
		if(!r.isPlayer(sender)){
			return;
		}
		Player p = (Player)sender;
		if(r.checkArgs(args, 0) == true){
			Player target = Bukkit.getPlayer(args[0]);
	        if(target==null){
	    	    p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
	        }
	        else{
			        Freeze(target, false);
			        p.sendMessage(r.mes("Freeze.Unfreezed").replaceAll("%Player", target.getName()));
	        }
	    }
	    else{
	    	p.sendMessage(r.mes("Freeze.Usage2"));
	    }
	}
	public static boolean Freeze(OfflinePlayer p){
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
		if(data.get("freeze") == null){
			return false;
		}
		return data.getBoolean("freeze");
	}
	public static void Freeze(OfflinePlayer p, Boolean set){
		if(set){
			frozen.add(p.getUniqueId().toString());
		}else{
			frozen.remove(p.getUniqueId().toString());
		}
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
		data.set("freeze", set);
		try {
			data.save(UltimateFileLoader.getPlayerFile(p));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMove(PlayerMoveEvent e){
		if(frozen.contains(e.getPlayer().getUniqueId().toString()) && Freeze(e.getPlayer()) ){
			/*if(
					e.getFrom().getBlockX() == e.getTo().getBlockX() &&
					e.getFrom().getBlockY() == e.getTo().getBlockY() &&
					e.getFrom().getBlockZ() == e.getTo().getBlockZ()
					) return;*/
			
			Location to = e.getFrom();
			to.setPitch(e.getTo().getPitch());
			to.setYaw(e.getTo().getYaw());
			e.setTo(to);
			
			//e.getPlayer().setPassenger(e.getPlayer());
			e.getPlayer().sendMessage(r.mes("Freeze.MoveMessage"));
		/*}else{
			if(e.getPlayer().getPassenger().equals(e.getPlayer())){
				e.getPlayer().setPassenger(null);
			}*/
		}
	}
	
}

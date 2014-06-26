package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;

public class CmdFreeze implements Listener {
	static Plugin plugin;
	public CmdFreeze(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@SuppressWarnings("deprecation")
	public static void handle(CommandSender sender, String[] args) {
		if(!r.perm(sender, "uc.freeze", false, true)) return;
		if(r.checkArgs(args, 0) == true){
			OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
	        if(target==null){
	    	    sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
	        }
	        else{
			        UC.getPlayer(target).setFrozen(true);
			        sender.sendMessage(r.mes("Freeze.Freezed").replaceAll("%Player", target.getName()));
	        }
	    }
	    else{
	    	sender.sendMessage(r.mes("Freeze.Usage"));
	    }
	}
	@SuppressWarnings("deprecation")
	public static void unfreeze(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.unfreeze", false, true)) return;
		if(r.checkArgs(args, 0) == true){
			OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
	        if(target==null){
	    	    sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
	        }
	        else{
			        UC.getPlayer(target).setFrozen(false);
			        sender.sendMessage(r.mes("Freeze.Unfreezed").replaceAll("%Player", target.getName()));
	        }
	    }
	    else{
	    	sender.sendMessage(r.mes("Freeze.Usage2"));
	    }
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMove(PlayerMoveEvent e){
		if(UC.getPlayer(e.getPlayer()).isFrozen()){
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
	@EventHandler
	public void interact(PlayerInteractEvent e){
		if(UC.getPlayer(e.getPlayer()).isFrozen()){
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void pMove(PlayerTeleportEvent e){
		if(UC.getPlayer(e.getPlayer()).isFrozen()){
			if(e.getCause().equals(TeleportCause.COMMAND)){
			e.setCancelled(true);
			}
		}
	}
	public static void freezes(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.freezelist", false, true)) return;
		StringBuilder mutes = new StringBuilder();
		Integer i = 0;
		for(OfflinePlayer pl : Bukkit.getOfflinePlayers()){
			if(UC.getPlayer(pl).isFrozen()){
				if(!mutes.toString().equalsIgnoreCase(""))mutes.append(", ");
				mutes.append(pl.getName());
				i++;
			}
		}
		sender.sendMessage(r.mes("Freeze.List").replaceAll("%Amount", i + "").replaceAll("%Frozen", mutes.toString()));
	}
	
}

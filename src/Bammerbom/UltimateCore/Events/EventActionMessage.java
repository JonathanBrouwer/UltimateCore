package Bammerbom.UltimateCore.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.Resources.Utils.LocationUtil;

public class EventActionMessage implements Listener{
	Plugin plugin;
	public EventActionMessage(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	static Boolean enb = true;
	@EventHandler(priority = EventPriority.LOW)
	public void JoinMessage(PlayerJoinEvent e){
		if(enb == true){
			if(!e.getPlayer().hasPlayedBefore()){
				Bukkit.broadcastMessage(r.mes("FirstJoin").replaceAll("%Player", e.getPlayer().getName()));
				LocationUtil.teleportUnsafe(e.getPlayer(), UC.getServer().getCustomSpawn() != null ? UC.getServer().getCustomSpawn() : e.getPlayer().getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
			}
			if(UltimateFileLoader.getPlayerConfig(e.getPlayer()).get("banned") != null && UltimateFileLoader.getPlayerConfig(e.getPlayer()).getBoolean("banned") == true ){
				e.setJoinMessage(null);
				return;
			}
		e.setJoinMessage(r.mes("JoinMessage").replaceAll("%Player", UC.getPlayer(e.getPlayer()).getNick()));
		}else{
			e.setJoinMessage(null);
		}
		}
	@EventHandler(priority = EventPriority.LOW)
	public void QuitMessage(PlayerQuitEvent e){
		if(enb == true){
			if(UltimateFileLoader.getPlayerConfig(e.getPlayer()).get("banned") != null && UltimateFileLoader.getPlayerConfig(e.getPlayer()).getBoolean("banned") == true ){
				e.setQuitMessage(null);
				return;
			}
		    e.setQuitMessage(r.mes("LeaveMessage").replaceAll("%Player", UC.getPlayer(e.getPlayer()).getNick()));
		}else{
			e.setQuitMessage(null);
		}
	}
	@EventHandler(priority = EventPriority.LOW)
	public void KickMessage(PlayerKickEvent e){
		if(enb == true){
		    e.setLeaveMessage(r.mes("LeaveMessage").replaceAll("%Player", UC.getPlayer(e.getPlayer()).getNick()));
		}else{
			e.setLeaveMessage(null);
		}
	}
	@EventHandler(priority = EventPriority.LOW)
	public void JoinFail(PlayerLoginEvent e){
		if(e.getResult().equals(PlayerLoginEvent.Result.KICK_WHITELIST)){
			e.setKickMessage(r.mes("WhitelistMessage"));
		}else if(e.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)){
			e.setKickMessage(r.mes("FullMessage"));
		}
	}
	public static void setEnb(Boolean b){
		enb = b;
	}
	
	
}

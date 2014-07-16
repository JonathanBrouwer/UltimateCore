package Bammerbom.UltimateCore.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;

public class EventAFK implements Listener{
	static Plugin plugin;
	public EventAFK(Plugin instance){
		plugin = instance;
		if(!instance.getConfig().getBoolean("Afk.Enabled")) return;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		for(Player p : UC.getOnlinePlayers()){
			lastaction.put(p.getName(), System.currentTimeMillis());
		}
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable(){
			public void run(){
				for(String str : lastaction.keySet()){
					Player p = UC.searchPlayer(str);
					Long time = lastaction.get(str);
						Long seconds1 = time / 1000;
						Long seconds2 = System.currentTimeMillis() / 1000;
						Long dif = seconds2 - seconds1;
						if(dif > plugin.getConfig().getInt("Afk.AfkTime")){
							if(!afk.contains(p.getUniqueId())){
							afk.add(p.getUniqueId());
							Bukkit.broadcastMessage(r.mes("Afk.Afk").replaceAll("%Player", UC.getPlayer(p).getNick()));
							}
						}
						if(dif > plugin.getConfig().getInt("Afk.KickTime")){
							if(plugin.getConfig().getBoolean("Afk.Enabled")){
								if(!r.perm(p, "uc.antiban", false, false)){
							p.kickPlayer(r.mes("Afk.Kick"));
								}
							}
						}
					
					
				}
			}
		}, 100L, 100L);
	}
	public static boolean isAfk(Player p){
		return afk.contains(p.getUniqueId());
	}
	static HashMap<String, Long> lastaction = new HashMap<String, Long>();
	static ArrayList<UUID> afk = new ArrayList<UUID>();
	public static void handle(CommandSender sender, String[] args){
		if(!r.checkArgs(args, 0)){
			if(!r.isPlayer(sender)) return;
			if(!r.perm(sender, "uc.afk", true, true)) return;
			if(!afk.contains(((Player) sender).getUniqueId())){
				Bukkit.broadcastMessage(r.mes("Afk.Afk").replaceAll("%Player", UC.getPlayer((Player)sender).getNick()));
				afk.add(((Player) sender).getUniqueId());
			}else{
				afk.remove(((Player) sender).getUniqueId());
				Bukkit.broadcastMessage(r.mes("Afk.Unafk").replaceAll("%Player", UC.getPlayer((Player) sender).getNick()));
			}
		}else{
			if(!r.perm(sender, "uc.afk.others", false, true)) return;
			String target = args[0];
			if(UC.searchPlayer(target) != null){
				Player t = UC.searchPlayer(target);
				if(!afk.contains(t.getUniqueId())){
					Bukkit.broadcastMessage(r.mes("Afk.Afk").replaceAll("%Player", UC.getPlayer(t).getNick()));
					afk.add(t.getUniqueId());
				}else{
					afk.remove(t.getUniqueId());
					Bukkit.broadcastMessage(r.mes("Afk.Unafk").replaceAll("%Player", UC.getPlayer(t).getNick()));
				}
			}else{
				sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", target));
			}
		}
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void event(AsyncPlayerChatEvent e){
		update(e);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerBedEnterEvent e){
		update(e);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerBedLeaveEvent e){
		update(e);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerChatTabCompleteEvent e){
		update(e);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerEditBookEvent e){
		update(e);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerInteractEvent e){
		update(e);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerInteractEntityEvent e){
		update(e);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerItemHeldEvent e){
		update(e);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerMoveEvent e){
		if(!e.getFrom().getBlock().equals(e.getTo().getBlock())){
		update(e);
		}
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerRespawnEvent e){
		update(e);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerToggleSneakEvent e){
		update(e);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerVelocityEvent e){
		update(e);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerJoin(PlayerJoinEvent e){
		update(e);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerCommand(PlayerCommandPreprocessEvent e){
		if(e.getMessage().equalsIgnoreCase("/afk") || e.getMessage().equalsIgnoreCase("afk")) return;
		update(e);
	}
	@EventHandler
	public void playerQuit(PlayerQuitEvent e){
		if(afk.contains(e.getPlayer().getUniqueId())){
			afk.remove(e.getPlayer().getUniqueId());
		}
		if(lastaction.containsKey(e.getPlayer().getName())){
			lastaction.remove(e.getPlayer().getName());
		}
	}
	@EventHandler
	public void playerKick(PlayerKickEvent e){
		if(afk.contains(e.getPlayer().getUniqueId())){
			afk.remove(e.getPlayer().getUniqueId());
		}
		if(lastaction.containsKey(e.getPlayer().getName())){
			lastaction.remove(e.getPlayer().getName());
		}
	}
	public void update(final PlayerEvent e){
		Thread thread = new Thread(new Runnable(){
			public void run(){
				Player p = e.getPlayer();
				lastaction.put(p.getName(), System.currentTimeMillis());
				if(afk.contains(p.getUniqueId())){
					afk.remove(p.getUniqueId());
					Bukkit.broadcastMessage(r.mes("Afk.Unafk").replaceAll("%Player", UC.getPlayer(p).getNick()));
				}
			}
		});
		thread.setName("UltimateCore: AFK Thread");
		thread.start();
	}
}

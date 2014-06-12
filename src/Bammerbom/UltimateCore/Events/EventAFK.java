package Bammerbom.UltimateCore.Events;

import java.util.ArrayList;
import java.util.HashMap;

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

public class EventAFK implements Listener{
	static Plugin plugin;
	public EventAFK(Plugin instance){
		plugin = instance;
		if(!instance.getConfig().getBoolean("Afk.Enabled")) return;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		for(Player p : Bukkit.getOnlinePlayers()){
			lastaction.put(p.getName(), System.currentTimeMillis());
		}
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable(){
			public void run(){
				for(String str : lastaction.keySet()){
					Player p = Bukkit.getPlayer(str);
					Long time = lastaction.get(str);
						Long seconds1 = time / 1000;
						Long seconds2 = System.currentTimeMillis() / 1000;
						Long dif = seconds2 - seconds1;
						if(dif > plugin.getConfig().getInt("Afk.AfkTime")){
							if(!afk.contains(str)){
							afk.add(str);
							Bukkit.broadcastMessage(r.mes("Afk.Afk").replaceAll("%Player", p.getName()));
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
	
	static HashMap<String, Long> lastaction = new HashMap<String, Long>();
	static ArrayList<String> afk = new ArrayList<String>();
	public static void handle(CommandSender sender, String[] args){
		if(!r.checkArgs(args, 0)){
			if(!r.isPlayer(sender)) return;
			if(!r.perm(sender, "uc.afk", true, true)) return;
			if(!afk.contains(sender.getName())){
				Bukkit.broadcastMessage(r.mes("Afk.Afk").replaceAll("%Player", sender.getName()));
				afk.add(sender.getName());
			}
		}else{
			if(!r.perm(sender, "uc.afk.others", false, true)) return;
			String target = args[0];
			if(Bukkit.getPlayer(target) != null){
				Player t = Bukkit.getPlayer(target);
				if(!afk.contains(t.getName())){
					Bukkit.broadcastMessage(r.mes("Afk.Afk").replaceAll("%Player", t.getName()));
					afk.add(t.getName());
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
		update(e);
	}
	@EventHandler
	public void playerQuit(PlayerQuitEvent e){
		if(afk.contains(e.getPlayer().getName())){
			afk.remove(e.getPlayer().getName());
		}
		if(lastaction.containsKey(e.getPlayer().getName())){
			lastaction.remove(e.getPlayer().getName());
		}
	}
	@EventHandler
	public void playerKick(PlayerKickEvent e){
		if(afk.contains(e.getPlayer().getName())){
			afk.remove(e.getPlayer().getName());
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
				if(afk.contains(p.getName())){
					afk.remove(p.getName());
					Bukkit.broadcastMessage(r.mes("Afk.Unafk").replaceAll("%Player", p.getName()));
				}
			}
		});
		thread.setName("UltimateCore: AFK Thread");
		thread.start();
	}
}

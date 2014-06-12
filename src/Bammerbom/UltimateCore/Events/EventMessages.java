package Bammerbom.UltimateCore.Events;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.BossBar;

public class EventMessages implements Listener{
	static Plugin plugin;
	public EventMessages(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		show();
	}
	public static void setCurrentMessage(String msg){ currentmessage = msg; }
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent e){
		if(plugin.getConfig().get("messages.list") == null){ return; }
		List<String> messgs = plugin.getConfig().getStringList("messages.list");
		Integer length = messgs.size();
		if(length == 0){ return; }
		if(length == 1){
			if(plugin.getConfig().getBoolean("messages.enabledbossbar") == true){
			BossBar.setMessage(e.getPlayer(), ChatColor.translateAlternateColorCodes('&', r.default1 + messgs.get(0)), 100F);
			currentmessage = ChatColor.translateAlternateColorCodes('&', r.default1 + messgs.get(0));
			}
			if(plugin.getConfig().getBoolean("messages.enabledchat") == true){
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', r.default1 + messgs.get(0)));
			}
		}else{
			if(plugin.getConfig().getBoolean("messages.enabledbossbar") == true){
			BossBar.setMessage(e.getPlayer(), ChatColor.AQUA + "Welcome on this server!");
			currentmessage = ChatColor.translateAlternateColorCodes('&', r.default1 + messgs.get(0));
			}
		}
	}
	static String currentmessage = "";
	public static void startt(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()){
					BossBar.handleTeleport(p, p.getLocation());
				}
				
			}}, 20L * 10, 20L * 10);
	}
	public static void timer(final List<String> messgs){
		final Integer time = plugin.getConfig().getInt("messages.time");
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
		@Override
		public void run() {
			Random random = new Random();
			String mess = messgs.get(random.nextInt(messgs.size()));
			for(Player p : Bukkit.getOnlinePlayers()){
				if(plugin.getConfig().getBoolean("messages.enabledbossbar") == true){
					BossBar.setMessage(p, ChatColor.translateAlternateColorCodes('&', r.default1  + mess), time);
					currentmessage = ChatColor.translateAlternateColorCodes('&', r.default1  + mess);
				}
				if(plugin.getConfig().getBoolean("messages.enabledchat") == true){
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', r.default1 + mess));
				}
				
			}
		}}, time * 20, time * 20);
	}
	public static void show(){
		if(plugin.getConfig().getBoolean("messages.enabledchat") == false && plugin.getConfig().getBoolean("messages.enabledbossbar") == false){ return; }
		if(plugin.getConfig().get("messages.list") == null){ return; }
		List<String> messgs = plugin.getConfig().getStringList("messages.list");
		Integer length = messgs.size();
		if(length == 0){ return; }
		//startt();
		if(length == 1){
			for(Player p : Bukkit.getOnlinePlayers()){
				if(plugin.getConfig().getBoolean("messages.enabledbossbar") == true){
					BossBar.setMessage(p, ChatColor.translateAlternateColorCodes('&', r.default1 + messgs.get(0)), 100F);
					currentmessage = ChatColor.translateAlternateColorCodes('&', r.default1 + messgs.get(0));
					}
					if(plugin.getConfig().getBoolean("messages.enabledchat") == true){
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', r.default1 + messgs.get(0)));
					}
			}
		}else{
			timer(messgs);
		}
	}
	
}

package Bammerbom.UltimateCore.Events;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.API.UC;

public class EventMOTD implements Listener{
	static Plugin plugin;
	public static String msg = "";
	public EventMOTD(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		List<String> lines = new ArrayList<String>();
		try{
			File file = new File(plugin.getDataFolder(), "motd.txt");
		if(!file.exists()) file.createNewFile();
	    Path path = Paths.get(file.getAbsolutePath());
	    Charset ENCODING = StandardCharsets.UTF_8;
			    try (Scanner scanner =  new Scanner(path, ENCODING.name())){
			      while (scanner.hasNextLine()){
			    	  lines.add(scanner.nextLine());
			      }      
			    }
		}catch(Exception ex){
			ex.printStackTrace();
		}
		for(String str : lines){
			msg = msg + ChatColor.translateAlternateColorCodes('&', str) + "\n";
		}
		
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void ColoredMotdMaker(ServerListPingEvent e){
		e.setMotd(ChatColor.translateAlternateColorCodes('&', (e.getMotd())));
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void joinMSG(final PlayerJoinEvent e){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				if(!msg.equalsIgnoreCase("")){
					e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', msg
							.replaceAll("%Player", e.getPlayer().getName()).replaceAll("\\{PLAYER\\}", e.getPlayer().getName())
							.replaceAll("%Online", UC.getOnlinePlayers().length + "").replaceAll("\\{ONLINE\\}", UC.getOnlinePlayers().length + "")
							.replaceAll("%Unique", Bukkit.getOfflinePlayers().length + "").replaceAll("\\{UNIQUE\\}", Bukkit.getOfflinePlayers().length + "")));
				}
			}
		}, 4L);
	}
	
}

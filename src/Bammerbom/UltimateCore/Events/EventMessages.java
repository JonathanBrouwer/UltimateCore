package Bammerbom.UltimateCore.Events;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.Resources.BossBar;

public class EventMessages implements Listener{
	static Plugin plugin;
	static ArrayList<String> messages = new ArrayList<String>();
	public EventMessages(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		//
		ArrayList<String> lines = new ArrayList<String>();
		try{
			File file = new File(plugin.getDataFolder(), "messages.txt");
		if(!file.exists()) plugin.saveResource("messages.txt", true);
	    Path path = Paths.get(file.getAbsolutePath());
	    Charset ENCODING = StandardCharsets.UTF_8;
			    try (Scanner scanner =  new Scanner(path, ENCODING.name())){
			      while (scanner.hasNextLine()){
			    	  lines.add(scanner.nextLine().replaceAll("\\\\n", "\n").replaceAll("\\n", "\n"));
			      }      
			    }
		}catch(Exception ex){
			ex.printStackTrace();
		}
		messages = lines;
		//
		if(r.getCnfg().contains("messages.decrease")){
			decrease = r.getCnfg().getBoolean("messages.decrease");
		}
		show();
		startt(); //TODO
	}
	//public static void setCurrentMessage(String msg){ currentmessage = msg; }
	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(final PlayerJoinEvent e){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				ArrayList<String> messgs = messages;
				Integer length = messgs.size();
				if(length == 0){ return; }
				if(length == 1){
					if(r.getCnfg().getBoolean("messages.enabledbossbar") == true){
					BossBar.setMessage(e.getPlayer(), ChatColor.translateAlternateColorCodes('&', currentmessage), 100F);
					}
				}else{
					if(r.getCnfg().getBoolean("messages.enabledbossbar") == true){
					BossBar.setMessage(e.getPlayer(), ChatColor.translateAlternateColorCodes('&', currentmessage));
					}
				}
			}
		}, 100L);
	}
	static String currentmessage = "";
	static Integer currentID = -1;
	static Boolean decrease = true;
	public static void startt(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run() {
				for(Player p : UC.getOnlinePlayers()){
					BossBar.handleTeleport(p, p.getLocation());
				}
				
			}}, 20L * 10, 20L * 10);
	}
	public static void timer(final List<String> messgs){
		final Integer time = r.getCnfg().getInt("messages.time");
		final Boolean ur = r.getCnfg().contains("messages.randomise") ? r.getCnfg().getBoolean("messages.randomise") : true;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			Random random = new Random();
			@Override
		public void run() {
			String mess = ur ? messgs.get(random.nextInt(messgs.size())) : "";
			if(!ur){
				currentID++;
				if((messgs.size() - 1) < currentID){
					mess = messgs.get(0);
					currentID = 0;
				}else{
					mess = messgs.get(currentID);
				}
			}
			for(Player p : UC.getOnlinePlayers()){
				if(r.getCnfg().getBoolean("messages.enabledbossbar") == true){
					if(decrease){
						BossBar.setMessage(p, ChatColor.translateAlternateColorCodes('&', r.default1  + mess), time);
					}else{
						BossBar.setMessage(p, ChatColor.translateAlternateColorCodes('&', r.default1  + mess));
					}
					currentmessage = ChatColor.translateAlternateColorCodes('&', r.default1  + mess);
				}
				if(r.getCnfg().getBoolean("messages.enabledchat") == true){
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', r.default1 + mess));
				}
				
			}
		}}, 0, time * 20);
	}
	public static void show(){
		if(r.getCnfg().getBoolean("messages.enabledchat") == false && r.getCnfg().getBoolean("messages.enabledbossbar") == false){ return; }
		ArrayList<String> messgs = messages;
		Integer length = messgs.size();
		if(length == 0){ return; }
		if(length == 1){
			for(Player p : UC.getOnlinePlayers()){
				if(r.getCnfg().getBoolean("messages.enabledbossbar") == true){
					BossBar.setMessage(p, ChatColor.translateAlternateColorCodes('&', r.default1 + messgs.get(0)), 100F);
					currentmessage = ChatColor.translateAlternateColorCodes('&', r.default1 + messgs.get(0));
				}
					if(r.getCnfg().getBoolean("messages.enabledchat") == true){
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', r.default1 + messgs.get(0)));
					}
			}
		}else{
			timer(messgs);
		}
	}
	
}

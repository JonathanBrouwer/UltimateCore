package Bammerbom.UltimateCore.Events;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Events.ServerCheck.Server;
import Bammerbom.UltimateCore.Events.ServerCheck.Status;

public class EventMinecraftServers{
	static Plugin plugin;
	public EventMinecraftServers(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		/*Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run() {
				EventMinecraftServers.run();
			}
		}, 60L, 600L);*/
		if(!r.getCnfg().getBoolean("mcstats")){
			disabled = true;
		}
	}
	static Boolean disabled = false;
	static Boolean on = false;
	static ArrayList<Server> offline = new ArrayList<Server>();
	static ArrayList<Server> unknown = new ArrayList<Server>();
	static ArrayList<Server> problems = new ArrayList<Server>();
	static ArrayList<Server> online = new ArrayList<Server>();
	
	
	public static void handle(final CommandSender sender, String[] args){
		if(disabled){
			sender.sendMessage(r.mes("UnknownCommandMessage"));
			return;
		}
		Thread thread = new Thread(new Runnable(){
			public void run(){
		if(!on) runcheck();
		if(!r.perm(sender, "uc.mcservers", false, true)) return;
				String os = "";
				for(Server str : online){
					if(!os.equals("")){
					    os = os + ", " + ChatColor.GREEN + str.toString().toLowerCase() + r.default1 + "";
					}else{
						os = os + ChatColor.GREEN + str.toString().toLowerCase() + r.default1 + "";
					}
				}
				for(Server str : problems){
					if(!os.equals("")){
					os = os + ", " + ChatColor.GOLD + str.toString().toLowerCase() + r.default1 + "";
					}else{
						os = os + ChatColor.GOLD + str.toString().toLowerCase() + r.default1 + "";
					}
				}
				for(Server str : offline){
					if(!os.equals("")){
					os = os + ", " + ChatColor.DARK_RED + str.toString().toLowerCase() + r.default1 + "";
					}else{
						os = os + ChatColor.DARK_RED + str.toString().toLowerCase() + r.default1 + "";
					}
				}
				for(Server str : unknown){
					if(!os.equals("")){
					os = os + ", " + ChatColor.GRAY + str.toString().toLowerCase() + r.default1 + "";
					}else{
						os = os + ChatColor.GRAY + str.toString().toLowerCase() + r.default1 + "";
					}
				}
				sender.sendMessage(r.default1 + "" + ChatColor.BOLD + "Servers: " + ChatColor.RESET + os);
	
			}
		});
		thread.setName("UltimateCore: Server Check Thread");
		thread.start();
	}
	private static void runcheck() {
		//
		on = true;
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				on = false;
			}
		}, 20 * 10L);
		//
		offline.clear();
		unknown.clear();
		problems.clear();
		online.clear();
		for(Server serv : Server.values()){
			Status status = ServerCheck.getStatus(serv);
			if(status.equals(Status.ONLINE)){
				online.add(serv);
			}else if(status.equals(Status.EXPERIENCE)){
				problems.add(serv);
			}else if(status.equals(Status.OFFLINE)){
				offline.add(serv);
			}else if(status.equals(Status.UNKNOWN)){
				unknown.add(serv);
			}
		}
	}	
}
class ServerCheck {

	private static JSONParser parser = new JSONParser();
	
	public enum Server {
		//Minecraft
		WEBSITE("minecraft.net"),
		SKIN("skins.minecraft.net"),
		SESSION("session.minecraft.net"),
		//Mojang
		ACCOUNT("account.mojang.com"),
		AUTH("auth.mojang.com"),
		AUTHSERVER("authserver.mojang.com"),
		MOJANGSESSION("sessionserver.mojang.com");
		
		private String url;
		
		Server(String url) {
			this.url = url;
		}
		
		private String getURL() {
			return url;
		}
	}
	public enum Status {
		
		ONLINE("No problems detected!"),
		EXPERIENCE("May be experiencing issues"),
		OFFLINE("Experiencing problems!"),
		UNKNOWN("Couldn't connect to Mojang!");
		
		private String description;
		
		Status(String description) {
			this.description = description;
		}
		
		public String getDescription() {
			return description;
		}
		
	}
	
	public static Status getStatus(Server service) {
		String status = null;

		try {
			URL url = new URL("http://status.mojang.com/check?service=" + service.getURL());
			BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
			Object object = parser.parse(input);
			JSONObject jsonObject = (JSONObject) object;
			
			status = (String) jsonObject.get(service.getURL());
		} catch (Exception e) {
			return Status.UNKNOWN;
		}
		
		return status(status);
	}
	
	private static Status status(String status) {
		switch(status.toLowerCase()) {
			case "green":
				return Status.ONLINE;
			
			case "yellow":
				return Status.EXPERIENCE;
		
			case "red":
				return Status.OFFLINE;
				
			default:
				return Status.UNKNOWN;
		}
	}

}

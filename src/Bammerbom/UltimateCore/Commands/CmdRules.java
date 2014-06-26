package Bammerbom.UltimateCore.Commands;

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
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdRules {
	static Plugin plugin;
	static String msg = "";
	public CmdRules(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		List<String> lines = new ArrayList<String>();
		try{
			File file = new File(plugin.getDataFolder(), "rules.txt");
		if(!file.exists()) plugin.saveResource("rules.txt", true);
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
	public static void handle(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.rules", true, true)) return;
		sender.sendMessage(msg);
	}
}

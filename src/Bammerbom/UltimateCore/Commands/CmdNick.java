package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

public class CmdNick implements Listener{
	static Plugin plugin;
	public CmdNick(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.nick", false, true)){
			return;
		}
		if(!r.checkArgs(args, 0)){
			sender.sendMessage(r.mes("Nick.Usage"));
			return;
		}
		Boolean o = false;
		if(r.checkArgs(args, 0) && args[0].equalsIgnoreCase("off")){
			Player t;
			if(r.checkArgs(args, 1)){
				o = true;
				t = Bukkit.getPlayer(args[1]);
				if(t == null){ sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[1])); return; }
			}else{
				if(!r.isPlayer(sender)) return;
				t = (Player) sender;
			}
			if(o && !r.perm(sender, "uc.nick.others", false, true)) return;
			sender.sendMessage(r.mes("Nick.Message").replaceAll("%Name", "off").replaceAll("%Player", t.getName()));
			if(o) t.sendMessage(r.mes("Nick.MessageOthers").replaceAll("%Player", sender.getName()).replaceAll("%Name", "off"));
			UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(t));
			data.set("nick", null);
			data.save(UltimateFileLoader.getPlayerFile(t));
			return;
		}
		Player t;
		if(r.checkArgs(args, 1)){
			o = true;
			t = Bukkit.getPlayer(args[1]);
			if(t == null){ sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[1])); return; }
		}else{
			if(!r.isPlayer(sender)) return;
			t = (Player) sender;
		}
		if(o && !r.perm(sender, "uc.nick.others", false, true)) return;
		String name = args[0].replaceAll("&k", "").replaceAll("%n", "").replaceAll("&l", "");
		if(r.perm(sender, "uc.nick.colors", false, false)) name = ChatColor.translateAlternateColorCodes('&', name);
		if(!ChatColor.stripColor(name.replaceAll(" ", "").replaceAll("§", "").replaceAll("&y", "").replaceAll("_", "").replaceAll("[a-zA-Z0-9]", "")).equalsIgnoreCase("")){
			sender.sendMessage(r.mes("Nick.NonAlpha"));
			return;
		}
		name = ChatColor.translateAlternateColorCodes('&', args[0].replaceAll("&k", "").replaceAll("%n", "").replaceAll("&l", ""));
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(t));
		data.set("nick", name);
		data.save(UltimateFileLoader.getPlayerFile(t));
		sender.sendMessage(r.mes("Nick.Message").replaceAll("%Name", name).replaceAll("%Player", t.getName()));
		if(o) t.sendMessage(r.mes("Nick.MessageOthers").replaceAll("%Player", sender.getName()).replaceAll("%Name", name));
	}
}

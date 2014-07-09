package Bammerbom.UltimateCore.Commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.API.UCplayer;

public class CmdMsg{
	static Plugin plugin;
	public CmdMsg(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
		if(!r.perm(sender, "uc.msg", false, true)){
			return;
		}
		if(!r.checkArgs(args, 1)){
			sender.sendMessage(r.mes("Whisper.Usage"));
			return;
		}

		Player pl = Bukkit.getPlayer(args[0]);
		if(pl == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		if(sender instanceof Player){
			//sender
		UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile((Player) sender));
		conf.set("lastmessage", pl.getUniqueId().toString());
		conf.save(UltimateFileLoader.getPlayerFile((Player) sender));
		//target
		UltimateConfiguration conf2 = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(pl));
		conf2.set("lastmessage", ((Player)sender).getUniqueId().toString());
		conf2.save(UltimateFileLoader.getPlayerFile(pl));
		}
		String chatcolor2 = r.getCnfg().getString("Chat.Color2") + "";
		String cc2 = ChatColor.translateAlternateColorCodes('&', chatcolor2);
		sender.sendMessage(r.default1 + "[" + r.default2 + "me " + r.default1 + "-> " + r.default2 + pl.getName() + r.default1 + "] " + cc2 + r.getFinalArg(args, 1));
		pl.sendMessage(r.default1 + "[" + r.default2 + sender.getName() + r.default1 + " -> " + r.default2 + "me" + r.default1 + "] " + cc2 + r.getFinalArg(args, 1));
		//Spy
		for(Player p : Bukkit.getOnlinePlayers()){
			UCplayer up = UC.getPlayer(p);
			if(up.isSpy()){
				p.sendMessage(ChatColor.GRAY + "[" + ChatColor.WHITE + sender.getName() + ChatColor.GRAY + " -> " + ChatColor.WHITE + pl.getName() + ChatColor.GRAY + "] " + ChatColor.WHITE + r.getFinalArg(args, 1));
			}
		}
	}
	public static void handle2(CommandSender sender, String[] args) {
		if(!r.isPlayer(sender)){
			return;
		}
		Player p = (Player) sender;
		if(!r.perm(sender, "uc.msg", false, true)){
			return;
		}
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.mes("Whisper.Usage2"));
			return;
		}
		UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
		if(conf.get("lastmessage") == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", "-"));
			return;
		}
		String pl = conf.getString("lastmessage");
		try{
		UUID u = UUID.fromString(pl);
		if(Bukkit.getPlayer(u) == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", "-"));
			return;
		}
		}catch(Exception ex){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", "-"));
			return;
		}
		Player t = Bukkit.getPlayer(UUID.fromString(pl));
		//TODO
		if(sender instanceof Player){
			//sender
		conf.set("lastmessage", t.getUniqueId().toString());
		conf.save(UltimateFileLoader.getPlayerFile((Player) sender));
		//target
		UltimateConfiguration conf2 = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(t));
		conf2.set("lastmessage", ((Player)sender).getUniqueId().toString());
		conf2.save(UltimateFileLoader.getPlayerFile(t));
		}
		
		
		//TODO
		
		
		
		String chatcolor2 = r.getCnfg().getString("Chat.Color2") + "";
		String cc2 = ChatColor.translateAlternateColorCodes('&', chatcolor2);
		sender.sendMessage(r.default1 + "[" + r.default2 + "me " + r.default1 + "-> " + r.default2 + t.getName() + r.default1 + "] " + cc2 + r.getFinalArg(args, 0));
		t.sendMessage(r.default1 + "[" + r.default2 + sender.getName() + r.default1 + " -> " + r.default2 + "me" + r.default1 + "] " + cc2 + r.getFinalArg(args, 0));
		//Spy
		for(Player pp : Bukkit.getOnlinePlayers()){
			UCplayer up = UC.getPlayer(pp);
			if(up.isSpy()){
				pp.sendMessage(ChatColor.GRAY + "[" + ChatColor.WHITE + sender.getName() + ChatColor.GRAY + " -> " + ChatColor.WHITE + t.getName() + ChatColor.GRAY + "] " + ChatColor.WHITE + r.getFinalArg(args, 0));
			}
		}
	}
}
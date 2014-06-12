package Bammerbom.UltimateCore.Commands;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

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
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile((Player) sender));
		conf.set("lastmessage", pl.getUniqueId().toString());
		try {
			conf.save(UltimateFileLoader.getPlayerFile((Player) sender));
		} catch (IOException e) {
			e.printStackTrace();
		}
		YamlConfiguration conf2 = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(pl));
		conf2.set("lastmessage", ((Player)sender).getUniqueId().toString());
		try {
			conf2.save(UltimateFileLoader.getPlayerFile(pl));
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		String chatcolor2 = r.getCnfg().getString("Chat.Color2") + "";
		String cc2 = ChatColor.translateAlternateColorCodes('&', chatcolor2);
		sender.sendMessage(r.default1 + "[" + r.default2 + "me " + r.default1 + "-> " + r.default2 + pl.getName() + r.default1 + "] " + cc2 + r.getFinalArg(args, 1));
		pl.sendMessage(r.default1 + "[" + r.default2 + sender.getName() + r.default1 + " -> " + r.default2 + "me" + r.default1 + "] " + cc2 + r.getFinalArg(args, 1));
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
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
		if(conf.get("lastmessage") == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", "-"));
			return;
		}
		String pl = conf.getString("lastmessage");
		if(Bukkit.getPlayer(pl) == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", "-"));
			return;
		}
		Player t = Bukkit.getPlayer(pl);
		String chatcolor2 = r.getCnfg().getString("Chat.Color2") + "";
		String cc2 = ChatColor.translateAlternateColorCodes('&', chatcolor2);
		sender.sendMessage(r.default1 + "[" + r.default2 + "me " + r.default1 + "-> " + r.default2 + t.getName() + r.default1 + "] " + cc2 + r.getFinalArg(args, 0));
		t.sendMessage(r.default1 + "[" + r.default2 + sender.getName() + r.default1 + " -> " + r.default2 + "me" + r.default1 + "] " + cc2 + r.getFinalArg(args, 0));
	}
}
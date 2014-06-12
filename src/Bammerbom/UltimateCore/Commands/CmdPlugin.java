package Bammerbom.UltimateCore.Commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Utils.PluginUtil;

public class CmdPlugin{
	static Plugin plugin;
	public CmdPlugin(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	} 
	@SuppressWarnings("unchecked")
	public static void handle(CommandSender sender, String[] args){
		if(r.checkArgs(args, 0) && r.checkArgs(args, 1)){
			Plugin pl = Bukkit.getServer().getPluginManager().getPlugin(args[1]);
			String message = null;
			if(pl == null){
				if(args[0].equalsIgnoreCase("load")){
					if(!r.perm(sender, "uc.plugin", false, false) && !r.perm(sender, "uc.plugin.load", false, false)){ return; }
					message = PluginUtil.load(args[1]);
					sender.sendMessage(r.mes(message).replaceAll("%Plugin", args[1]));
					return;
				}else{
				sender.sendMessage(r.mes("Plugin.NotFound").replaceAll("%Plugin", args[1]));
				return;
				}
			}
			ArrayList<String> mesl = null;
			String pn = pl.getName();
			
			if(args[0].equalsIgnoreCase("info")){
				if(!r.perm(sender, "uc.plugin", false, false) && !r.perm(sender, "uc.plugin.info", false, false)){ sender.sendMessage(r.mes("Nor.permissions"));return; }
				mesl = PluginUtil.infoPlugin(Bukkit.getPluginManager().getPlugin(args[1]));
			}else if(args[0].equalsIgnoreCase("enable")){
				if(!r.perm(sender, "uc.plugin", false, false) && !r.perm(sender, "uc.plugin.enable", false, false)){ sender.sendMessage(r.mes("Nor.permissions"));return; }
				message = PluginUtil.enable(Bukkit.getPluginManager().getPlugin(pn));
			}else if(args[0].equalsIgnoreCase("disable")){
				if(!r.perm(sender, "uc.plugin", false, false) && !r.perm(sender, "uc.plugin.disable", false, false)){ sender.sendMessage(r.mes("Nor.permissions"));return; }
				message = PluginUtil.disable(Bukkit.getPluginManager().getPlugin(pn));
			}else if(args[0].equalsIgnoreCase("unload")){
				if(!r.perm(sender, "uc.plugin", false, false) && !r.perm(sender, "uc.plugin.unload", false, false)){ sender.sendMessage(r.mes("Nor.permissions"));return; }
				message = PluginUtil.unload(Bukkit.getPluginManager().getPlugin(args[1]));
			}else if(args[0].equalsIgnoreCase("reload")){
				if(!r.perm(sender, "uc.plugin", false, false) && !r.perm(sender, "uc.plugin.reload", false, false)){ sender.sendMessage(r.mes("Nor.permissions"));return; }
				message = PluginUtil.reload(Bukkit.getPluginManager().getPlugin(args[1]));
			}else{
				sender.sendMessage(r.default1 + "/plugin " + r.default2 + "info [Plugin] " + r.default1 + "- Info about plugin");
				sender.sendMessage(r.default1 + "/plugin " + r.default2 + "enable [Plugin] " + r.default1 + "- Enable plugin");
				sender.sendMessage(r.default1 + "/plugin " + r.default2 + "disable [Plugin] " + r.default1 + "- Disable plugin");
				sender.sendMessage(r.default1 + "/plugin " + r.default2 + "load [Plugin] " + r.default1 + "- Load plugin");
				sender.sendMessage(r.default1 + "/plugin " + r.default2 + "unload [Plugin] " + r.default1 + "- Unload plugin");
				sender.sendMessage(r.default1 + "/plugin " + r.default2 + "reload [Plugin] " + r.default1 + "- Disable and Enable plugin");
			}
			if(message != null){
				sender.sendMessage(r.mes(message).replaceAll("%Plugin", args[1]));
			}else if(mesl != null){
				for(String st : mesl){
					sender.sendMessage(r.mes(st, false).replaceAll("%Plugin", args[1]));
				}
			}
		}else{
			sender.sendMessage(r.default1 + "/plugin " + r.default2 + "info [Plugin] " + r.default1 + "- Info about plugin");
			sender.sendMessage(r.default1 + "/plugin " + r.default2 + "enable [Plugin] " + r.default1 + "- Enable plugin");
			sender.sendMessage(r.default1 + "/plugin " + r.default2 + "disable [Plugin] " + r.default1 + "- Disable plugin");
			sender.sendMessage(r.default1 + "/plugin " + r.default2 + "load [Plugin] " + r.default1 + "- Load plugin");
			sender.sendMessage(r.default1 + "/plugin " + r.default2 + "unload [Plugin] " + r.default1 + "- Unload plugin");
			sender.sendMessage(r.default1 + "/plugin " + r.default2 + "reload [Plugin] " + r.default1 + "- Disable and Enable plugin");
		}
		
	}
}

package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateUpdater;
import Bammerbom.UltimateCore.r;

public class CmdUC{
	static Plugin plugin;
	public CmdUC(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void core(CommandSender sender, String[] args){
		if(args.length == 0){
			if(!r.perm(sender, "uc.menu", false, true)) return;
			sender.sendMessage(r.default1 + "-----------------------------------------");
			sender.sendMessage(r.default2 + "               UltimateCore Menu");
			sender.sendMessage(r.default1 + "-----------------------------------------");
			sender.sendMessage(r.default1 + "UltimateCore commands:");
			sender.sendMessage(r.default1 + "/uc reload  " + r.default2 + "> Reload Ultimate Core");
			sender.sendMessage(r.default1 + "/uc credits " + r.default2 + "> Credits of Ultimate Core");
			sender.sendMessage(r.default1 + "/uc disable " + r.default2 + "> Disable core (If chrashed)");
			sender.sendMessage(r.default1 + "/uc version " + r.default2 + "> Get your, and the newest version of UltimateCore");
		}else if(args[0].equalsIgnoreCase("reload")){
			if(!r.perm(sender, "uc.menu.reload", false, true)) return;
			Plugin core = plugin.getServer().getPluginManager().getPlugin(plugin.getName());
			plugin.getServer().getPluginManager().disablePlugin(core);
			plugin.getServer().getPluginManager().enablePlugin(core);
			sender.sendMessage(r.default1 + "UltimateCore has been reloaded!");
		}else if(args[0].equalsIgnoreCase("disable")){
			if(!r.perm(sender, "uc.menu.disable", false, true)) return;
			Plugin core = plugin.getServer().getPluginManager().getPlugin(plugin.getName());
			plugin.getServer().getPluginManager().disablePlugin(core);
			sender.sendMessage(r.default1 + "UltimateCore has been disabled! :(");
		}else if(args[0].equalsIgnoreCase("credits")){
			if(!r.perm(sender, "uc.menu", false, true)) return;
			sender.sendMessage(r.default1 + "Credits:");
			sender.sendMessage(r.default1 + "Owner: " + r.default2 + "Jhtzb");
			sender.sendMessage(r.default1 + "Test/Translate: " + r.default2 + "Blockbreaker21");
		}else if(args[0].equalsIgnoreCase("noreturn")){
			return;
		}else if(args[0].equalsIgnoreCase("version")){
			if(!r.perm(sender, "uc.menu.version", false, true)) return;
			sender.sendMessage(r.default1 + "Your version of UltimateCore: " + r.default2 + plugin.getDescription().getVersion());
			sender.sendMessage(r.default1 + "Newest version of UltimateCore: " + r.default2 + UltimateUpdater.getLatestUpdate());
		}else{
			if(!r.perm(sender, "uc.menu", false, true)) return;
			sender.sendMessage(r.default1 + "-----------------------------------------");
			sender.sendMessage(r.default2 + "               UltimateCore Menu");
			sender.sendMessage(r.default1 + "-----------------------------------------");
			sender.sendMessage(r.default1 + "UltimateCore commands:");
			sender.sendMessage(r.default1 + "/uc reload  " + r.default2 + "> Reload Ultimate Core");
			sender.sendMessage(r.default1 + "/uc credits " + r.default2 + "> Credits of Ultimate Core");
			sender.sendMessage(r.default1 + "/uc disable " + r.default2 + "> Disable core (If chrashed)");
			sender.sendMessage(r.default1 + "/uc version " + r.default2 + "> Get your, and the newest version of UltimateCore");
		}
	}

}

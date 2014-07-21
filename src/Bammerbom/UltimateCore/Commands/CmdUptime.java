package Bammerbom.UltimateCore.Commands;

import java.lang.management.ManagementFactory;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Utils.DateUtil;

public class CmdUptime {
	static Plugin plugin;
	public CmdUptime(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(r.perm(sender, "uc.uptime", false, true) == false) return;
		sender.sendMessage(r.default1 + "Time since server start: " + r.default2 + DateUtil.formatDateDiff(ManagementFactory.getRuntimeMXBean().getStartTime()));
	}
}

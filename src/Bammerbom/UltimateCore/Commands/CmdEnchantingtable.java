package Bammerbom.UltimateCore.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdEnchantingtable {
	static Plugin plugin;
	public CmdEnchantingtable(Plugin instance){
		plugin = instance;
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.isPlayer(sender)) return;
		if(!r.perm(sender, "uc.enchantingtable", false,true))return;
		Player p = (Player) sender;
		p.openEnchanting(null, true);
		
	}
}

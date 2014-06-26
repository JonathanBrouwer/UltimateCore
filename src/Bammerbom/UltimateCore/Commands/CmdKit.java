package Bammerbom.UltimateCore.Commands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

public class CmdKit{
	static Plugin plugin;
	public CmdKit(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	static File f = UltimateFileLoader.DFkits;
	public static void kit(CommandSender sender, String[] args) {
		if(!(r.isPlayer(sender))){
			return;
		}
		if(!r.perm(sender, "uc.kit", false, true)){
			return;
		}
		Player p = (Player) sender;
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(f);
		if(!r.checkArgs(args, 0)){
			StringBuilder b = new StringBuilder(r.default1 + "Kits: " + r.default2);
			   Boolean a = false;
			   for(String str : conf.getConfigurationSection("Kits").getKeys(true)){
				   if(a) b.append(", ");
				   a = true;
				   b.append(str);
			   }
			   sender.sendMessage(b.toString());
			return;
		}
		if(conf.get("Kits." + args[0]) == null){
			p.sendMessage(r.default1 + "Kit not found: " + args[0]);
			return;
		}
		//Inventory inv = InventoryUtil.StringToInventory(conf.getString("Kits." + args[0] + ".Inventory"));
		
	}
	public static void setkit(CommandSender sender, String[] args) {
		if(!(r.isPlayer(sender))){
			return;
		}
		if(!r.perm(sender, "uc.setkit", false, true)){
			return;
		}
		//Player p = (Player) sender;
	}
	public static void delkit(CommandSender sender, String[] args) {
		if(!r.perm(sender, "uc.delkit", false, true)){
			return;
		}
	}
}

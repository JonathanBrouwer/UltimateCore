package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.Resources.Utils.InventoryUtil;

public class CmdSkull implements Listener{
	static Plugin plugin;
	public CmdSkull(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.skull", false, true)){
			return;
		}
		if(!r.isPlayer(sender)) return;
		Player p = (Player) sender;
		if(r.checkArgs(args, 0)){
			if(!r.perm(sender, "uc.skull.others", false, true)){
				return;
			}
			Player t = UC.searchPlayer(args[0]);
			if(t == null){
				sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
				return;
			}
			sender.sendMessage(r.mes("Skull").replaceAll("%Player", t.getName()));
		    ItemStack skull = new ItemStack(Material.SKULL_ITEM);
		    skull.setDurability(Short.parseShort("3"));
		    SkullMeta meta = (SkullMeta) skull.getItemMeta();
		    meta.setOwner(t.getName());
		    skull.setItemMeta(meta);
		    InventoryUtil.addItem(p.getInventory(), skull);
		}else{
			Player t = p;
		    sender.sendMessage(r.mes("Skull").replaceAll("%Player", p.getName()));
		    ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1);
		    skull.setDurability(Short.parseShort("3"));
		    SkullMeta meta = (SkullMeta) skull.getItemMeta();
		    meta.setOwner(t.getName());
		    skull.setItemMeta(meta);
		    InventoryUtil.addItem(p.getInventory(), skull);
		}
	}
}

package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.MetaItemStack;
import Bammerbom.UltimateCore.Resources.Databases.ItemDatabase;
import Bammerbom.UltimateCore.Resources.Utils.InventoryUtil;

public class CmdGive implements Listener{
	static Plugin plugin;
	public CmdGive(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.give", false, true)) return;
		if(!r.checkArgs(args, 1)){
			sender.sendMessage(r.mes("Give.Usage"));
			return;
		}
		Player target = Bukkit.getPlayer(args[0]);
		if(target == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		ItemStack item;
		try { item = new ItemStack(ItemDatabase.getItem(args[1]));
		} catch (Exception e) {
			sender.sendMessage(r.mes("Give.ItemNotFound").replaceAll("%Item", args[1]));
			return;}
		if(item == null || item.getType() == null || item.getType().equals(Material.AIR)){
			sender.sendMessage(r.mes("Give.ItemNotFound").replaceAll("%Item", args[1]));
			return;
		}
		if(InventoryUtil.isFullInventory(target.getInventory())){
			sender.sendMessage(r.mes("Give.InventoryFull").replaceAll("%Item", args[1]));
			return;
		}
		Integer amount = item.getMaxStackSize();
		if(r.checkArgs(args, 2)){
			if(!r.isNumber(args[2])){
				sender.sendMessage(r.mes("Give.AmountNotValid").replaceAll("%Amount", args[2]));
				return;
			}
			amount = Integer.parseInt(args[2]);
		}
		item.setAmount(amount);
		if(r.checkArgs(args, 3)){
			if(r.isNumber(args[3])){
			item.setDurability(Short.parseShort(args[3]));
			}
		MetaItemStack meta = new MetaItemStack(item);
		int metaStart = r.isNumber(args[3]) ? 4 : 3;

	      if (args.length > metaStart)
	      {
	        try {
				meta.parseStringMeta(sender, true, args, metaStart, plugin);
			} catch (Exception e) {
				e.printStackTrace();
				sender.sendMessage(r.mes("Give.MetadataFailed").replaceAll("%Item", args[1]));
			}
	      }
	      item = meta.getItemStack();
		}
		InventoryUtil.addItem(target.getInventory(), item);
		sender.sendMessage(r.mes("Give.Succes").replaceAll("%Item", item.getType().name().toLowerCase().replaceAll("_", "")).replaceAll("%Amount", amount.toString()).replaceAll("%Player", target.getName()));
	}
	public static void handle2(CommandSender sender, String[] args){
		if(!r.isPlayer(sender)){
			return;
		}
		if(!r.perm(sender, "uc.item", false, true)) return;
		Player p = (Player) sender;
		if(!r.checkArgs(args, 0)){
			p.sendMessage(r.mes("Give.Usage2"));
			return;
		}
		Player target = p;
		ItemStack item;
		try { item = new ItemStack(ItemDatabase.getItem(args[0]));
		} catch (Exception e) {
			sender.sendMessage(r.mes("Give.ItemNotFound").replaceAll("%Item", args[0]));
			return;}
		if(item == null || item.getType() == null || item.getType().equals(Material.AIR)){
			sender.sendMessage(r.mes("Give.ItemNotFound").replaceAll("%Item", args[0]));
			return;
		}
		if(InventoryUtil.isFullInventory(target.getInventory())){
			sender.sendMessage(r.mes("Give.InventoryFull").replaceAll("%Item", args[0]));
			return;
		}
		Integer amount = item.getMaxStackSize();
		if(r.checkArgs(args, 1)){
			if(!r.isNumber(args[1])){
				sender.sendMessage(r.mes("Give.AmountNotValid").replaceAll("%Amount", args[1]));
				return;
			}
			amount = Integer.parseInt(args[1]);
		}
		item.setAmount(amount);
		if(r.checkArgs(args, 2)){
			if(r.isNumber(args[2])){
			item.setDurability(Short.parseShort(args[2]));
			}
		MetaItemStack meta = new MetaItemStack(item);
		int metaStart = r.isNumber(args[2]) ? 3 : 2;

	      if (args.length > metaStart)
	      {
	        try {
				meta.parseStringMeta(sender, true, args, metaStart, plugin);
			} catch (Exception e) {
				e.printStackTrace();
				sender.sendMessage(r.mes("Give.MetadataFailed").replaceAll("%Item", args[0]));
			}
	      }
	      item = meta.getItemStack();
		}
		InventoryUtil.addItem(target.getInventory(), item);
		sender.sendMessage(r.mes("Give.SuccesSelf").replaceAll("%Item", item.getType().name().toLowerCase().replaceAll("_", "")).replaceAll("%Amount", amount.toString()).replaceAll("%Player", target.getName()));
	}
}

package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.MapEnchantments;
import Bammerbom.UltimateCore.Resources.MetaItemStack;

public class CmdEnchant{
	static Plugin plugin;
	public CmdEnchant(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.enchant", false, true)) return;
		if(!r.isPlayer(sender)) return;
		if(!r.checkArgs(args, 0)){
			sender.sendMessage(r.mes("Enchantment.Usage"));
			return;
		}
		Player p = (Player) sender;
		Enchantment ench = MapEnchantments.getByName(args[0]);
		if(ench == null){
			sender.sendMessage(r.mes("Enchant.EnchantNotFound").replaceAll("%Enchant", args[0]));
			return;
		}
		ItemStack stack = p.getItemInHand();
		if(stack == null || stack.getType() == null || stack.getType().equals(Material.AIR)){
			sender.sendMessage(r.mes("Enchant.NoItemInHand"));
			return;
		}
		String name = ench.getName().replaceAll("_", "").toLowerCase();
		Integer level = 1;
		if(r.checkArgs(args, 1) == true && r.isNumber(args[1])){
			level = Integer.parseInt(args[1]);
		}
		if(level < 0) level = 0;
		if(level == 0){
		stack.removeEnchantment(ench);	
		}else{
			MetaItemStack stack2 = new MetaItemStack(stack);
			stack2.addEnchantment(sender, true, ench, level);
		}
		sender.sendMessage(r.mes("Enchant.Succes").replaceAll("%Enchant", name).replaceAll("%Level", level + "").replaceAll("%Item", stack.getType().name().toLowerCase().replaceAll("_", "")));
	}
}

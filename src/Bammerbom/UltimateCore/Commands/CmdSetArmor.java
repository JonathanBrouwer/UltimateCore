package Bammerbom.UltimateCore.Commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Utils.StringUtil;

public class CmdSetArmor {
	static Plugin plugin;
	public CmdSetArmor(Plugin instance){
		plugin = instance;
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.checkArgs(args, 0)){
		    sender.sendMessage(r.mes("Setarmor.Usage"));
			return;
		}else if(r.checkArgs(args, 0) && !r.checkArgs(args, 1)){
			if(!r.isPlayer(sender)) return;
			if(!r.perm(sender, "uc.setarmor", false, true)){
				return;
			}
			Player p = (Player) sender;
			if(isArmor(args[0])){
				setArmor(p, getArmor(args[0]));
				sender.sendMessage(r.mes("Setarmor.Set").replaceAll("%Player", sender.getName()).replaceAll("%Armor", StringUtil.firstUpperCase(getArmor(args[0]).name())));
			}else{
				sender.sendMessage(r.mes("Setarmor.NotFound").replaceAll("%Armor", args[0]));
			}
		}else if(r.checkArgs(args, 1)){
			if(!r.perm(sender, "uc.setarmor.others", false, true)){
				return;
			}
			if(isArmor(args[0])){
				Player t = r.searchPlayer(args[1]);
				if(t == null){
					sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[1]));
					return;
				}
				setArmor(t, getArmor(args[0]));
				sender.sendMessage(r.mes("Setarmor.Set").replaceAll("%Player", t.getName()).replaceAll("%Armor", StringUtil.firstUpperCase(getArmor(args[0]).name())));
				t.sendMessage(r.mes("Setarmor.To").replaceAll("%Player", sender.getName()).replaceAll("%Armor", StringUtil.firstUpperCase(getArmor(args[0]).name())));
			}else if(isArmor(args[1])){
				Player t = r.searchPlayer(args[0]);
				if(t == null){
					sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
					return;
				}
				setArmor(t, getArmor(args[1]));
				sender.sendMessage(r.mes("Setarmor.Set").replaceAll("%Player", t.getName()).replaceAll("%Armor", StringUtil.firstUpperCase(getArmor(args[1]).name())));
				t.sendMessage(r.mes("Setarmor.To").replaceAll("%Player", sender.getName()).replaceAll("%Armor", StringUtil.firstUpperCase(getArmor(args[1]).name())));
			}else{
				sender.sendMessage(r.mes("Setarmor.NotFound").replaceAll("%Armor", args[0]));
			}
		}
	}
	public static boolean isArmor(String a){
		for(ArmorType t : ArmorType.values()){
			if(a.toLowerCase().contains(t.name().toLowerCase())){
				return true;
			}
		}
		return false;
	}
	public static ArmorType getArmor(String a){
		for(ArmorType t : ArmorType.values()){
			if(a.toLowerCase().contains(t.name().toLowerCase())){
				return t;
			}
		}
		return null;
	}
	public static void setArmor(Player p, ArmorType a){
		if(a.equals(ArmorType.leather)){
			p.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
			p.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
			p.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
			p.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
		}else if(a.equals(ArmorType.gold)){
			p.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET));
			p.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
			p.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
			p.getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS));
		}else if(a.equals(ArmorType.chain)){
			p.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
			p.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
			p.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
			p.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
		}else if(a.equals(ArmorType.iron)){
			p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
			p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
		}else if(a.equals(ArmorType.diamond)){
			p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
			p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
			p.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
			p.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
		}
	}
	public enum ArmorType{
		leather, gold, chain, iron, diamond
	}
}

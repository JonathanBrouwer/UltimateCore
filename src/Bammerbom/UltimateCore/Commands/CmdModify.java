package Bammerbom.UltimateCore.Commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.MetaItemStack;

public class CmdModify{
	public static void handle(CommandSender sender, String[] args) {
		if(!r.isPlayer(sender)) return;
		if(!r.perm(sender, "uc.modify", false, true)) return;
		Player p = (Player) sender;
		ItemStack stack = p.getItemInHand();
		if(stack == null || stack.getType() == null || stack.getType().equals(Material.AIR)){
			p.sendMessage(r.default1 + "You cant modify air.");
			return;
		}
		MetaItemStack item = new MetaItemStack(stack);
		item.parseStringMeta(sender, true, args, 0);
		stack = item.getItemStack();
		p.setItemInHand(stack);
		sender.sendMessage("Your item has been modified.");
	}
}

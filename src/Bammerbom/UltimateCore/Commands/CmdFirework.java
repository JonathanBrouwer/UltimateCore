package Bammerbom.UltimateCore.Commands;

import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.MetaItemStack;

public class CmdFirework{
	public static void handle(CommandSender sender, String[] args){
    	if(!r.isPlayer(sender)) return;
  	    Player p = (Player) sender;
  	    Boolean spawnin = !(p.getItemInHand().getType() == Material.FIREWORK);
        ItemStack stack = p.getItemInHand().getType() == Material.FIREWORK ? p.getItemInHand() : new ItemStack(Material.FIREWORK); 
        if(!r.checkArgs(args, 0)){
        	sender.sendMessage(r.default1 + "/firework " + r.default2 + "[clear] [power:<amount>] [color:<COLOR[,COLOR]]>] [fade:<COLOR[,COLOR]]>] [shape:<star/ball/large/creeper/burst>] [effect:<trail/twinkle>[,trail/twinkle]]");
        	return;
        }
        if (args[0].equalsIgnoreCase("clear") && (p.getItemInHand().getType() == Material.FIREWORK)){
            FireworkMeta fmeta = (FireworkMeta)stack.getItemMeta();
            fmeta.clearEffects();
            stack.setItemMeta(fmeta);
            sender.sendMessage(r.default1 + "Cleared all effects of the firework in your hand.");;
            return;
        }
        MetaItemStack mStack = new MetaItemStack(stack);
        for(String arg : args){
            if(arg.equalsIgnoreCase("power") || arg.equalsIgnoreCase("p")){
                FireworkMeta fmeta = (FireworkMeta)stack.getItemMeta();
                try{
                    int power = Integer.parseInt(arg.split(":")[1]);
                    fmeta.setPower(power > 3 ? 4 : power);
                }catch (NumberFormatException e){
                    p.sendMessage(ChatColor.DARK_RED + "Failed to spawn in Firework. (Invalid format?)");
                    return;
                }
                stack.setItemMeta(fmeta);
            }else if(r.isNumber(arg)){
                stack.setAmount(Integer.parseInt(arg));
            }else{
                try{
                    mStack.addFireworkMeta(p, true, arg);
                }catch (Exception e){
                	p.sendMessage(ChatColor.DARK_RED + "Failed to spawn in Firework. (Invalid format?)");
                	return;
                }
            }
        }
        if (mStack.isValidFirework()){
            FireworkMeta fmeta = (FireworkMeta)mStack.getItemStack().getItemMeta();
            FireworkEffect effect = mStack.getFireworkBuilder().build();
            fmeta.addEffect(effect);
            stack.setItemMeta(fmeta);
        }else{
        	p.sendMessage(ChatColor.DARK_RED + "Failed to spawn in Firework. (Invalid format?)");
        	return;
        }
        p.sendMessage(r.default1 + "Succesfully " + (spawnin ? "spawned in" : "modified") + " firework.");
        if(spawnin){
        	p.getInventory().addItem(stack);
        }
    }
}
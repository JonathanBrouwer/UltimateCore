package Bammerbom.UltimateCore.Commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.MapPotionEffects;
import Bammerbom.UltimateCore.Resources.Utils.StringUtil;

public class CmdPotion{
	@SuppressWarnings("deprecation")
	public static void handle(CommandSender sender, String[] args){
    	if(!r.isPlayer(sender)) return;
    	if(!r.perm(sender, "uc.potion", false, true)) return;
  	    Player p = (Player) sender;
  	    if(!r.checkArgs(args, 0)){
  	    	StringBuilder sb = new StringBuilder();
  	    	sb.append(r.default1 + "/potion" + r.default2 + " <Effect> [Duration] [Amplifier] [Splash]");
  	    	sb.append("\n" + r.default1 + "PotionEffect types: " + r.default2);
  	        Boolean a = false;
  	    	for (PotionEffectType type : PotionEffectType.values()){
  	        	if(type == null || type.getName() == null) continue;
  	    		if(a) sb.append(", ");
  	        	a = true;
  	        	sb.append(type.getName().toLowerCase().replaceAll("_", ""));
  	        }
  	    	sender.sendMessage(sb.toString());
  	    	return;
  	    }
  	    Boolean spawnin = !(p.getItemInHand().getType() == Material.POTION);
        ItemStack stack = p.getItemInHand().getType() == Material.POTION ? p.getItemInHand() : new ItemStack(Material.POTION); 
        PotionMeta meta = (PotionMeta) stack.getItemMeta();
        if(args[0].equalsIgnoreCase("clear")){
  	    	stack.setDurability(Short.parseShort("0"));
  	    	meta.clearCustomEffects();
  	    	stack.setItemMeta(meta);
  	    	sender.sendMessage(r.default1 + "Removed all effects from the potion.");
  	    	return;
  	    }
  	    PotionEffectType ef = MapPotionEffects.getByName(args[0]);
  	    if(ef == null){
  	    	sender.sendMessage(r.mes("Effect.EffectNotFound").replaceAll("%Effect", args[0]));
			return;
  	    }
		Integer dur = 120;
		Integer lev = 1;
		if(r.checkArgs(args, 1)){
			if(!r.isNumber(args[1]) && !args[1].equalsIgnoreCase("splash")){
				sender.sendMessage(r.mes("Effect.AmountNotValid").replaceAll("%Amount", args[1]));
				return;
			}else if(r.isNumber(args[1]))
			dur = Integer.parseInt(args[1]);
		}
		if(r.checkArgs(args, 2)){
			if(!r.isNumber(args[2]) && !args[1].equalsIgnoreCase("splash")){
				sender.sendMessage(r.mes("Effect.AmountNotValid").replaceAll("%Amount", args[2]));
				return;
			}else if(r.isNumber(args[2]))
			lev = Integer.parseInt(args[2]);
		}
		lev = r.normalize(lev, 0, 999999);
		dur = r.normalize(dur, 0, 999999);
		if(lev == 0 || dur == 0){
			if(!spawnin){
			meta.removeCustomEffect(ef);
			if(StringUtil.nullOrEmpty(meta.getCustomEffects())){
			stack.setDurability(Short.parseShort("0"));
			}
			stack.setItemMeta(meta);
			sender.sendMessage(r.default1 + "Removed " + ef.getName().toLowerCase() + " from the potion.");
			return;
			}else{
				lev = r.normalize(lev, 1, 999999);
				dur = r.normalize(dur, 1, 999999);
			}
		}
		PotionEffect effect = new PotionEffect(ef, dur * 20, lev - 1);
		meta.addCustomEffect(effect, true);
		meta.setMainEffect(ef);
	    stack.setItemMeta(meta);
	    stack.setDurability(Short.parseShort("1"));
	    Potion potion = Potion.fromItemStack(stack);
		for(String str : args){
			if(str.equalsIgnoreCase("splash")){
				potion.setSplash(true);
			}
		}
	    potion.apply(stack);
	    Potion potion2 = new Potion(PotionType.getByEffect(ef));
	    potion2.setSplash(potion.isSplash());
	    stack.setDurability(potion2.toDamageValue());
	    if(PotionType.getByEffect(ef) == null){
	    	if(!potion.isSplash()){
	    	    stack.setDurability(Short.parseShort("8232"));
	    	}else{
	    		stack.setDurability(Short.parseShort("16424"));
	    	}
	    }
	    String splash = potion.isSplash() ? "splash " : "";
	    if(spawnin){
	    	p.getInventory().addItem(stack);
	    	sender.sendMessage(r.default1 + "Given you a " + splash + "potion with " + ef.getName().toLowerCase());
	    }else{
	    	sender.sendMessage(r.default1 + "Added " + ef.getName().toLowerCase() + " to your " + splash + "potion.");

	    }
	    
    }
}
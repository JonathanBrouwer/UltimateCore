package Bammerbom.UltimateCore.Resources;

import java.util.HashMap;

import org.bukkit.enchantments.Enchantment;

public class MapEnchantments {
	  static HashMap<String, Enchantment> ench = new HashMap<String, Enchantment>();
public static Enchantment getByName(String str){
	  if(ench.isEmpty()){
			ench.put("power",Enchantment.ARROW_DAMAGE);
			ench.put("flame",Enchantment.ARROW_FIRE);
			ench.put("infinite",Enchantment.ARROW_INFINITE);
			ench.put("infinity",Enchantment.ARROW_INFINITE);
			ench.put("punch",Enchantment.ARROW_KNOCKBACK);
			ench.put("sharp",Enchantment.DAMAGE_ALL);
			ench.put("sharpness",Enchantment.DAMAGE_ALL);
			ench.put("smite",Enchantment.DAMAGE_UNDEAD);
			ench.put("bane",Enchantment.DAMAGE_ARTHROPODS);
			ench.put("baneofarthropods",Enchantment.DAMAGE_ARTHROPODS);
			ench.put("arthropods",Enchantment.DAMAGE_ARTHROPODS);
			ench.put("efficiency",Enchantment.DIG_SPEED);
			ench.put("digspeed",Enchantment.DIG_SPEED);
			ench.put("durability",Enchantment.DURABILITY);
			ench.put("fireaspect",Enchantment.FIRE_ASPECT);
			ench.put("fire", Enchantment.FIRE_ASPECT);
			ench.put("knockback",Enchantment.KNOCKBACK);
			ench.put("looting",Enchantment.LOOT_BONUS_MOBS);
			ench.put("fortune",Enchantment.LOOT_BONUS_BLOCKS);
			ench.put("luck",Enchantment.LUCK);
			ench.put("luckofthesea", Enchantment.LUCK);
			ench.put("lure",Enchantment.LURE);
			ench.put("respiration",Enchantment.OXYGEN);
			ench.put("prot",Enchantment.PROTECTION_ENVIRONMENTAL);
			ench.put("protection",Enchantment.PROTECTION_ENVIRONMENTAL);
			ench.put("blastprot",Enchantment.PROTECTION_EXPLOSIONS);
			ench.put("blastprotection",Enchantment.PROTECTION_EXPLOSIONS);
			ench.put("fallprot",Enchantment.PROTECTION_FALL);
			ench.put("fallprotection",Enchantment.PROTECTION_FALL);
			ench.put("featherfall",Enchantment.PROTECTION_FALL);
			ench.put("featherfalling",Enchantment.PROTECTION_FALL);
			ench.put("fireprot",Enchantment.PROTECTION_FIRE);
			ench.put("fireprotection",Enchantment.PROTECTION_FIRE);
			ench.put("projprot",Enchantment.PROTECTION_PROJECTILE);
			ench.put("projprotection",Enchantment.PROTECTION_PROJECTILE);
			ench.put("projectileprot",Enchantment.PROTECTION_PROJECTILE);
			ench.put("projectileprotection",Enchantment.PROTECTION_PROJECTILE);
			ench.put("silktouch",Enchantment.SILK_TOUCH);
			ench.put("thorns",Enchantment.THORNS);
			ench.put("aqua",Enchantment.WATER_WORKER);
			ench.put("aquaaffinity",Enchantment.WATER_WORKER);
			ench.put("unbreak",Enchantment.DURABILITY);
			ench.put("unbreaking",Enchantment.DURABILITY);
	  }
	  return ench.get(str.toLowerCase());
	  }
}
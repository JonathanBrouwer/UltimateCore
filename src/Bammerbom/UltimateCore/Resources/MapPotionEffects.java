package Bammerbom.UltimateCore.Resources;

import java.util.HashMap;

import org.bukkit.potion.PotionEffectType;

import Bammerbom.UltimateCore.r;

public class MapPotionEffects{
	  static HashMap<String, PotionEffectType> pf = new HashMap<String, PotionEffectType>();
  @SuppressWarnings("deprecation")
public static PotionEffectType getByName(String str){
	  if(pf.isEmpty()){
    pf.put("swiftness", PotionEffectType.SPEED);
    pf.put("slowness", PotionEffectType.SLOW);
    pf.put("haste", PotionEffectType.FAST_DIGGING);
    pf.put("miningfatigue", PotionEffectType.SLOW_DIGGING);
    pf.put("strength", PotionEffectType.INCREASE_DAMAGE);
    pf.put("instanthealth", PotionEffectType.HEAL);
    pf.put("instantdamage", PotionEffectType.HARM);
    pf.put("jumpboost", PotionEffectType.JUMP);
    pf.put("nausea", PotionEffectType.CONFUSION);
    pf.put("regen", PotionEffectType.REGENERATION);
    pf.put("resistance", PotionEffectType.DAMAGE_RESISTANCE);
    pf.put("invis", PotionEffectType.INVISIBILITY);
    pf.put("blind", PotionEffectType.BLINDNESS);
    for(PotionEffectType type : PotionEffectType.values()){
    	if(type == null || type.getName() == null) continue;
    	if(type.getName().contains("_")){
        	pf.put(type.getName().toLowerCase().replaceAll("_", ""), type);
    	    pf.put(type.getName().toLowerCase(), type);
    	}else{
    		pf.put(type.getName().toLowerCase(), type);
    	}
    }
	  }
    return !r.isNumber(str) ? pf.get(str.toLowerCase()) : PotionEffectType.getById(Integer.parseInt(str));
  }
}
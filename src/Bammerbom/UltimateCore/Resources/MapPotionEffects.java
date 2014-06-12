package Bammerbom.UltimateCore.Resources;

import java.util.HashMap;

import org.bukkit.potion.PotionEffectType;

import Bammerbom.UltimateCore.r;

public class MapPotionEffects{
	  static HashMap<String, PotionEffectType> pf = new HashMap<String, PotionEffectType>();
  @SuppressWarnings("deprecation")
public static PotionEffectType getByName(String str){
	  if(pf.isEmpty()){
    pf.put("speed", PotionEffectType.SPEED);
    pf.put("swiftness", PotionEffectType.SPEED);
    pf.put("slowness", PotionEffectType.SLOW);
    pf.put("haste", PotionEffectType.FAST_DIGGING);
    pf.put("miningfatigue", PotionEffectType.SLOW_DIGGING);
    pf.put("slowdigging", PotionEffectType.SLOW_DIGGING);
    pf.put("strength", PotionEffectType.INCREASE_DAMAGE);
    pf.put("instanthealth", PotionEffectType.HEAL);
    pf.put("heal", PotionEffectType.HEAL);
    pf.put("instantdamage", PotionEffectType.HARM);
    pf.put("jumpboost", PotionEffectType.JUMP);
    pf.put("nausea", PotionEffectType.CONFUSION);
    pf.put("regeneration", PotionEffectType.REGENERATION);
    pf.put("resistance", PotionEffectType.DAMAGE_RESISTANCE);
    pf.put("fireresistance", PotionEffectType.FIRE_RESISTANCE);
    pf.put("waterbreathing", PotionEffectType.WATER_BREATHING);
    pf.put("invisibility", PotionEffectType.INVISIBILITY);
    pf.put("blindness", PotionEffectType.BLINDNESS);
    pf.put("nightvision", PotionEffectType.NIGHT_VISION);
    pf.put("hunger", PotionEffectType.HUNGER);
    pf.put("weakness", PotionEffectType.WEAKNESS);
    pf.put("poison", PotionEffectType.POISON);
    pf.put("wither", PotionEffectType.WITHER);
    pf.put("healthboost", PotionEffectType.HEALTH_BOOST);
    pf.put("absorption", PotionEffectType.ABSORPTION);
    pf.put("saturation", PotionEffectType.SATURATION);
	  }
    return !r.isNumber(str) ? pf.get(str.toLowerCase()) : PotionEffectType.getById(Integer.parseInt(str));
  }
}
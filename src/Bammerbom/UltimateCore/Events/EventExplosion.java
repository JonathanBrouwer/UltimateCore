package Bammerbom.UltimateCore.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class EventExplosion implements Listener{
	Plugin plugin;
	public EventExplosion(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void EntityExplode(EntityExplodeEvent e){
		try{
		if(e.getEntityType() != null){
		if(r.getCnfg().getBoolean("Explode.Creeper") == true && e.getEntityType().equals(EntityType.CREEPER)){
			e.setYield(0.0F);
		}
		if(r.getCnfg().getBoolean("Explode.TNT") == true && e.getEntityType().equals(EntityType.PRIMED_TNT) || e.getEntityType().equals(EntityType.MINECART_TNT)){
			e.setYield(0.0F);
		}
		if(r.getCnfg().getBoolean("Explode.Ghast") == true && e.getEntityType().equals(EntityType.GHAST) || e.getEntityType().equals(Fireball.class) || e.getEntityType().equals(LargeFireball.class)){
			e.setYield(0.0F);
		}
		if(r.getCnfg().getBoolean("Explode.Enderdragon") == true && e.getEntityType().equals(EntityType.ENDER_DRAGON) || e.getEntityType().equals(EntityType.ENDER_CRYSTAL)){
			e.setCancelled(true);
		}
		if(r.getCnfg().getBoolean("Explode.Wither") == true && e.getEntityType().equals(EntityType.WITHER) || e.getEntityType().equals(EntityType.WITHER_SKULL)){
			e.setYield(0.0F);
		}
		if(r.getCnfg().getBoolean("Explode.Lightning") == true && e.getEntityType().equals(EntityType.LIGHTNING)){
			e.setYield(0.0F);
		}
		}
		}catch(Exception exc){
		}
	}
	
}

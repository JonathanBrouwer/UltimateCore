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
	Boolean creeper = r.getCnfg().getBoolean("Explode.Creeper");
	Boolean tnt = r.getCnfg().getBoolean("Explode.TNT");
	Boolean ghast = r.getCnfg().getBoolean("Explode.Ghast");
	Boolean enderdragon = r.getCnfg().getBoolean("Explode.Enderdragon");
	Boolean wither = r.getCnfg().getBoolean("Explode.Wither");
	Boolean lightning = r.getCnfg().getBoolean("Explode.Lightning");
	@EventHandler(priority = EventPriority.LOWEST)
	public void EntityExplode(EntityExplodeEvent e){
		try{
		if(e.getEntityType() != null){
		if(creeper && e.getEntityType().equals(EntityType.CREEPER)){
			e.setYield(0.0F);
			e.setCancelled(true);
		}
		if(tnt&& (e.getEntityType().equals(EntityType.PRIMED_TNT) || e.getEntityType().equals(EntityType.MINECART_TNT))){
			e.setYield(0.0F);
			e.setCancelled(true);
		}
		if(ghast&& (e.getEntityType().equals(EntityType.GHAST) || e.getEntity() instanceof Fireball || e.getEntity() instanceof LargeFireball)){
			e.setYield(0.0F);
			e.setCancelled(true);
		}
		if(enderdragon && (e.getEntityType().equals(EntityType.ENDER_DRAGON) || e.getEntityType().equals(EntityType.ENDER_CRYSTAL))){
			e.setCancelled(true);
		}
		if(wither && (e.getEntityType().equals(EntityType.WITHER) || e.getEntityType().equals(EntityType.WITHER_SKULL))){
			e.setYield(0.0F);
			e.setCancelled(true);
		}
		if(lightning && e.getEntityType().equals(EntityType.LIGHTNING)){
			e.setYield(0.0F);
			e.setCancelled(true);
		}
		}
		}catch(Exception exc){
		}
	}
	
}

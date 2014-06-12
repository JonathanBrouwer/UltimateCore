package Bammerbom.UltimateCore.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class EventDeathmessages implements Listener{
	Plugin plugin;
	public EventDeathmessages(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@SuppressWarnings("deprecation")
	@org.bukkit.event.EventHandler
	  public void death(org.bukkit.event.entity.PlayerDeathEvent e)
	  {
	    Player p = e.getEntity().getPlayer();
	    EntityDamageEvent damageEvent = p.getLastDamageCause();
	    if(damageEvent.getCause() != null){
	    if (!(damageEvent instanceof org.bukkit.event.entity.EntityDamageByEntityEvent))
	    {

	      if (damageEvent.getCause().toString().equals("CONTACT")) {
	    	   e.setDeathMessage(r.mes("DeathMessages.Cactus").replaceAll("%Player", p.getName()));
	      }
	      else if (damageEvent.getCause().toString().equals("BLOCK_EXPLOSION")) {
	    	  
	    		  e.setDeathMessage(r.mes("DeathMessages.TNT").replaceAll("%Player", p.getName()));
	    	  
	      }
	      else if (damageEvent.getCause().toString().equals("DROWNING")) {
	    	  
	    		  e.setDeathMessage(r.mes("DeathMessages.Drowning").replaceAll("%Player", p.getName()));
	    	  

	      }
	      else if (damageEvent.getCause().toString().equals("FALL")) {
	    	  
	    		  e.setDeathMessage(r.mes("DeathMessages.Fall").replaceAll("%Player", p.getName()));
	    	  

	      }
	      else if (damageEvent.getCause().toString().equals("FIRE")) {
	    	  
	    		  e.setDeathMessage(r.mes("DeathMessages.Fire").replaceAll("%Player", p.getName()));
	    	  
	      }
	      else if (damageEvent.getCause().toString().equals("SUFFOCATION")) {
	    	  
	    		  e.setDeathMessage(r.mes("DeathMessages.Suffocated").replaceAll("%Player", p.getName()));
	    	  

	      }
	      else if (damageEvent.getCause().toString().equals("VOID")) {
	    	  
	    		  e.setDeathMessage(r.mes("DeathMessages.OutOfWorld").replaceAll("%Player", p.getName()));
	    	  

	      }
	      else if (damageEvent.getCause().toString().equals("STARVATION")) {
	    	  
	    		  e.setDeathMessage(r.mes("DeathMessages.Hunger").replaceAll("%Player", p.getName()));
	    	  

	      }
	      else if (damageEvent.getCause().toString().equals("SUICIDE")) {
	    	  
	    		  e.setDeathMessage(r.mes("DeathMessages.Suicide").replaceAll("%Player", p.getName()));
	    	  

	      }
	      else if (damageEvent.getCause().toString().equals("MAGIC")) {
	    	  
	    		  e.setDeathMessage(r.mes("DeathMessages.Potion").replaceAll("%Player", p.getName()));
	    	  

	      }
	      else if (damageEvent.getCause().toString().equals("POISON")) {
	    	  
	    		  e.setDeathMessage(r.mes("DeathMessages.Potion").replaceAll("%Player", p.getName()));
	    	  

	      }
	      else if (damageEvent.getCause().toString().equals("LIGHTNING")) {
	    	  
	    		  e.setDeathMessage(r.mes("DeathMessages.Lightning").replaceAll("%Player", p.getName()));
	    	  

	      }
	      else if (damageEvent.getCause().toString().equals("LAVA")) {
	    	  
	    		  e.setDeathMessage(r.mes("DeathMessages.Lava").replaceAll("%Player", p.getName()));
	    	  

	      }
	      else if (damageEvent.getCause().toString().equals("FIRE_TICK")) {
	    	  
	    		  e.setDeathMessage(r.mes("DeathMessages.Fire").replaceAll("%Player", p.getName()));
	    	  

	      }
	      else {
	    	  
	    		  e.setDeathMessage(r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
	    	  
	      } 
	    }
	    else
	    {
	      org.bukkit.entity.Entity damager = ((org.bukkit.event.entity.EntityDamageByEntityEvent)damageEvent).getDamager();
	      
	        if ((damager instanceof org.bukkit.entity.Zombie))
	        {
	          if (damager.getType().toString().equals("PIG_ZOMBIE")) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.ZombiePigman").replaceAll("%Player", p.getName()));
		    	  
	          }
	          else if (damager.getType().toString().equals("ZOMBIE")) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Zombie").replaceAll("%Player", p.getName()));
		    	  
	          }
	          else {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Zombie").replaceAll("%Player", p.getName()));
		    	  
	          }
	          

	        }
	        else if (damager.getType().toString().equals("PRIMED_TNT")) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.TNT").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if (damager.getType().toString().equals("SPLASH_POTION")) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Potion").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if (damager.getType().toString().equals("ENDER_PEARL")) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Enderpearl").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if (damager.getType().toString().equals("FIREBALL")) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Fireball").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if ((damager instanceof org.bukkit.entity.PigZombie)) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.ZombiePigMan").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if (damager.getType().toString().equals("ARROW")) {
	          damageEvent = (org.bukkit.event.entity.EntityDamageByEntityEvent)e.getEntity().getLastDamageCause();
	          org.bukkit.entity.Projectile arrow = (org.bukkit.entity.Arrow)damager;
	          if (arrow.getShooter() instanceof Skeleton) {
	        	  Arrow ar = (Arrow) damager;
	        	  Skeleton skel = (Skeleton) ar.getShooter();
	        	  if(skel.getSkeletonType().equals(SkeletonType.NORMAL)){
		    		  e.setDeathMessage(r.mes("DeathMessages.Skeleton").replaceAll("%Player", p.getName()));
		    	  }else{
		    		  e.setDeathMessage(r.mes("DeathMessages.WitherSkeleton").replaceAll("%Player", p.getName()));
		    	  }
	          }
	          else if (arrow.getShooter() instanceof Player) {
		    	  Player pl = (Player) arrow.getShooter();
		    		  e.setDeathMessage(r.mes("DeathMessages.Player").replaceAll("%Killed", p.getName()).replaceAll("%Killer", pl.getName()).replaceAll("%Weapon", pl.getItemInHand().getType().toString()));
		    	  
	          }
	          
	        }
	        else if ((damager instanceof org.bukkit.entity.Skeleton)) {
		    	  Skeleton skel = (Skeleton) damager;
		    	  if(skel.getSkeletonType().equals(SkeletonType.NORMAL)){
		    		  e.setDeathMessage(r.mes("DeathMessages.Skeleton").replaceAll("%Player", p.getName()));
		    	  }else{
		    		  e.setDeathMessage(r.mes("DeathMessages.WitherSkeleton").replaceAll("%Player", p.getName()));
		    	  }
	
		    	  
	        }
	        else if ((damager instanceof org.bukkit.entity.Creeper)) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Creeper").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if ((damager instanceof org.bukkit.entity.Ghast)) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Ghast").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if ((damager instanceof org.bukkit.entity.Blaze)) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Blaze").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if (damager.getType().toString().equals("SMALL_FIREBALL")) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Blaze").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if ((damager instanceof org.bukkit.entity.Slime)) {
	          if (damager.getType().toString().equals("MAGMA_CUBE")) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.MagmaCube").replaceAll("%Player", p.getName()));
		    	  
	          }
	          else if (damager.getType().toString().equals("SLIME")) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Slime").replaceAll("%Player", p.getName()));
		    	  
	          }
	          else {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Slime").replaceAll("%Player", p.getName()));
		    	  
	          }
	          
	        }
	        else if ((damager instanceof org.bukkit.entity.MagmaCube)) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.MagmaCube").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if ((damager instanceof org.bukkit.entity.Wolf)) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Wolf").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if ((damager instanceof org.bukkit.entity.Spider)) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Spider").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if ((damager instanceof org.bukkit.entity.CaveSpider)) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.CaveSpider").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if ((damager instanceof org.bukkit.entity.Silverfish)) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Silverfish").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if ((damager instanceof org.bukkit.entity.Enderman)) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Enderman").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if ((damager instanceof org.bukkit.entity.EnderDragon)) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.EnderDragon").replaceAll("%Player", p.getName()));
		    	  
	        }
	        else if ((damager instanceof org.bukkit.entity.Wither)) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Wither").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if ((damager instanceof org.bukkit.entity.Witch)) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Witch").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if ((damager instanceof org.bukkit.entity.WitherSkull)) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Wither").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if ((damager instanceof org.bukkit.entity.IronGolem)) {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.IronGolem").replaceAll("%Player", p.getName()));
		    	  

	        }
	        else if ((damager instanceof Player)) {
	          if (p.getKiller().getItemInHand().getType().toString().toLowerCase().equals("air")) {
	        	  Player pl = (Player) damager;
		    		  e.setDeathMessage(r.mes("DeathMessages.Player").replaceAll("%Killed", p.getName()).replaceAll("%Killer", pl.getName()).replaceAll("%Weapon", "hand"));
		    	  
	          }
	          else {
		    	  Player pl = (Player) damager;
		    	  e.setDeathMessage(r.mes("DeathMessages.Player").replaceAll("%Killed", p.getName()).replaceAll("%Killer", pl.getName()).replaceAll("%Weapon", pl.getItemInHand().getType().name().toLowerCase().replaceAll("_", "")));
		    	  
	          } 
	        }
	        else {
		    	  
		    		  e.setDeathMessage(r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
		    	  
	        } 
	      } 
	    }
	    
	  } 


}

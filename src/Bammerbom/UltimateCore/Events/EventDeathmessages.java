package Bammerbom.UltimateCore.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Utils.ItemUtil;

public class EventDeathmessages implements Listener{
	Plugin plugin;
	public EventDeathmessages(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@EventHandler(priority = EventPriority.LOW)
	  public void death(PlayerDeathEvent e)
	  {
	    Player p = e.getEntity().getPlayer();
	    EntityDamageEvent damageEvent = p.getLastDamageCause();
	    if(damageEvent.getCause() != null){
	    	switch(damageEvent.getCause()){
			case BLOCK_EXPLOSION:
				e.setDeathMessage(r.mes("DeathMessages.TNT").replaceAll("%Player", p.getName()));
				break;
			case CONTACT:
				e.setDeathMessage(r.mes("DeathMessages.Cactus").replaceAll("%Player", p.getName()));
				break;
			case CUSTOM:
				e.setDeathMessage(r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				break;
			case DROWNING:
				e.setDeathMessage(r.mes("DeathMessages.Drowning").replaceAll("%Player", p.getName()));
				break;
			case ENTITY_ATTACK:
				break;
			case ENTITY_EXPLOSION:
				e.setDeathMessage(r.mes("DeathMessages.Creeper").replaceAll("%Player", p.getName()));
				break;
			case FALL:
				 e.setDeathMessage(r.mes("DeathMessages.Fall").replaceAll("%Player", p.getName()));
				break;
			case FALLING_BLOCK:
				e.setDeathMessage(r.mes("DeathMessages.FallingBlock").replaceAll("%Player", p.getName()));
				break;
			case FIRE:
				e.setDeathMessage(r.mes("DeathMessages.Fire").replaceAll("%Player", p.getName()));
				break;
			case FIRE_TICK:
				 e.setDeathMessage(r.mes("DeathMessages.Fire").replaceAll("%Player", p.getName()));
				break;
			case LAVA:
				 e.setDeathMessage(r.mes("DeathMessages.Lava").replaceAll("%Player", p.getName()));
				break;
			case LIGHTNING:
	    		  e.setDeathMessage(r.mes("DeathMessages.Lightning").replaceAll("%Player", p.getName()));
				break;
			case MAGIC:
				e.setDeathMessage(r.mes("DeathMessages.Potion").replaceAll("%Player", p.getName()));
				break;
			case MELTING:
				e.setDeathMessage(r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				break;
			case POISON:
				 e.setDeathMessage(r.mes("DeathMessages.Potion").replaceAll("%Player", p.getName()));
				break;
			case PROJECTILE:
				e.setDeathMessage(r.mes("DeathMessages.Skeleton").replaceAll("%Player", p.getName()));
				break;
			case STARVATION:
				e.setDeathMessage(r.mes("DeathMessages.Hunger").replaceAll("%Player", p.getName()));
				break;
			case SUFFOCATION:
				e.setDeathMessage(r.mes("DeathMessages.Suffocated").replaceAll("%Player", p.getName()));
				break;
			case SUICIDE:
	    		  e.setDeathMessage(r.mes("DeathMessages.Suicide").replaceAll("%Player", p.getName()));
				break;
			case THORNS:
				e.setDeathMessage(r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				break;
			case VOID:
				 e.setDeathMessage(r.mes("DeathMessages.OutOfWorld").replaceAll("%Player", p.getName()));
				break;
			case WITHER:
				e.setDeathMessage(r.mes("DeathMessages.Wither").replaceAll("%Player", p.getName()));
				break;
	    	
	    	}
			String ep = en(damageEvent);
			if(!ep.equalsIgnoreCase("FALSE")){
				e.setDeathMessage(ep);
			}
	    }    
	  } 
	@SuppressWarnings("deprecation")
	public String en(EntityDamageEvent damageEvent){
		Player p = (Player) damageEvent.getEntity();
		if(damageEvent instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) damageEvent;
		    switch(ev.getDamager().getType()){
			case ARROW:
				Projectile pr = (Projectile) ev.getDamager();
				if(((ProjectileSource) pr.getShooter()) instanceof Player){
					return (r.mes("DeathMessages.Player").replaceAll("%Killed", p.getName()).replaceAll("%Killer", ((Player) pr.getShooter()).getName()).replaceAll("%Weapon", "Bow"));
					
				}else if(pr.getShooter() instanceof Skeleton){
					Skeleton sk = (Skeleton) pr.getShooter();
					if(sk.getType().equals(SkeletonType.NORMAL)){
						return (r.mes("DeathMessages.Skeleton").replaceAll("%Player", p.getName()));
					}else{
						return (r.mes("DeathMessages.WitherSkeleton").replaceAll("%Player", p.getName()));
					}
					
				}else if(pr.getShooter() instanceof Ghast){
					return (r.mes("DeathMessages.Ghast").replaceAll("%Player", p.getName()));
				}else if(pr.getShooter() instanceof Blaze){
					return (r.mes("DeathMessages.Blaze").replaceAll("%Player", p.getName()));
				}else{
					return (r.mes("DeathMessages.Projectile").replaceAll("%Player", p.getName()));
				}
				
			case BAT:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case BLAZE:
				return (r.mes("DeathMessages.Blaze").replaceAll("%Player", p.getName()));
				
			case BOAT:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case CAVE_SPIDER:
				return (r.mes("DeathMessages.CaveSpider").replaceAll("%Player", p.getName()));
				
			case CHICKEN:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case COMPLEX_PART:
				return (r.mes("DeathMessages.EnderDragon").replaceAll("%Player", p.getName()));
				
			case COW:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case CREEPER:
				return (r.mes("DeathMessages.Creeper").replaceAll("%Player", p.getName()));
				
			case DROPPED_ITEM:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case EGG:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case ENDERMAN:
				return (r.mes("DeathMessages.Enderman").replaceAll("%Player", p.getName()));
				
			case ENDER_CRYSTAL:
				return (r.mes("DeathMessages.EnderCrystal").replaceAll("%Player", p.getName()));
				
			case ENDER_DRAGON:
				return (r.mes("DeathMessages.EnderDragon").replaceAll("%Player", p.getName()));
				
			case ENDER_PEARL:
				return (r.mes("DeathMessages.Enderpearl").replaceAll("%Player", p.getName()));
				
			case ENDER_SIGNAL:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case EXPERIENCE_ORB:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case FALLING_BLOCK:
				return (r.mes("DeathMessages.Suffocated").replaceAll("%Player", p.getName()));
				
			case FIREBALL:
				return (r.mes("DeathMessages.Ghast").replaceAll("%Player", p.getName()));
				
			case FIREWORK:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case FISHING_HOOK:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case GHAST:
				return (r.mes("DeathMessages.Ghast").replaceAll("%Player", p.getName()));
				
			case GIANT:
				return (r.mes("DeathMessages.Giant").replaceAll("%Player", p.getName()));
				
			case HORSE:
				return (r.mes("DeathMessages.Horse").replaceAll("%Player", p.getName()));
				
			case IRON_GOLEM:
				return (r.mes("DeathMessages.IronGolem").replaceAll("%Player", p.getName()));
				
			case ITEM_FRAME:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case LEASH_HITCH:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case LIGHTNING:
				return (r.mes("DeathMessages.Lightning").replaceAll("%Player", p.getName()));
				
			case MAGMA_CUBE:
				return (r.mes("DeathMessages.MagmaCube").replaceAll("%Player", p.getName()));
				
			case MINECART:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case MINECART_CHEST:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case MINECART_COMMAND:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case MINECART_FURNACE:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case MINECART_HOPPER:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case MINECART_MOB_SPAWNER:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case MINECART_TNT:
				return (r.mes("DeathMessages.TNT").replaceAll("%Player", p.getName()));
				
			case MUSHROOM_COW:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case OCELOT:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case PAINTING:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case PIG:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case PIG_ZOMBIE:
				return (r.mes("DeathMessages.ZombiePigMan").replaceAll("%Player", p.getName()));
				
			case PLAYER:
				String name = ItemUtil.getTypeName(((Player) ev.getDamager()).getItemInHand());
				return (r.mes("DeathMessages.Player").replaceAll("%Killed", p.getName()).replaceAll("%Killer", ((Player) ev.getDamager()).getName()).replaceAll("%Weapon", /**/ name /**/));
				
			case PRIMED_TNT:
				return (r.mes("DeathMessages.TNT").replaceAll("%Player", p.getName()));
				
			case SHEEP:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case SILVERFISH:
				return (r.mes("DeathMessages.Silverfish").replaceAll("%Player", p.getName()));
				
			case SKELETON:
				Skeleton skel = (Skeleton) ev.getDamager();
				if(skel.getSkeletonType().equals(SkeletonType.NORMAL)){
				return (r.mes("DeathMessages.Skeleton").replaceAll("%Player", p.getName()));
				}else{
					return (r.mes("DeathMessages.WitherSkeleton").replaceAll("%Player", p.getName()));
				}
				
			case SLIME:
				return (r.mes("DeathMessages.Slime").replaceAll("%Player", p.getName()));
				
			case SMALL_FIREBALL:
				return (r.mes("DeathMessages.Blaze").replaceAll("%Player", p.getName()));
				
			case SNOWBALL:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case SNOWMAN:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case SPIDER:
				return (r.mes("DeathMessages.Spider").replaceAll("%Player", p.getName()));
				
			case SPLASH_POTION:
				return (r.mes("DeathMessages.Potion").replaceAll("%Player", p.getName()));
				
			case SQUID:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case THROWN_EXP_BOTTLE:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case UNKNOWN:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case VILLAGER:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case WEATHER:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", p.getName()));
				
			case WITCH:
				return (r.mes("DeathMessages.Witch").replaceAll("%Player", p.getName()));
				
			case WITHER:
				return (r.mes("DeathMessages.Wither").replaceAll("%Player", p.getName()));
				
			case WITHER_SKULL:
				return (r.mes("DeathMessages.Wither").replaceAll("%Player", p.getName()));
				
			case WOLF:
				return (r.mes("DeathMessages.Wolf").replaceAll("%Player", p.getName()));
				
			case ZOMBIE:
				return (r.mes("DeathMessages.Zombie").replaceAll("%Player", p.getName()));
		    }
		}
		return "FALSE";
	}

}

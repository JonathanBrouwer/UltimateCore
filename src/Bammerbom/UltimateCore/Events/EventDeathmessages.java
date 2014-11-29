package Bammerbom.UltimateCore.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import Bammerbom.UltimateCore.API.UC;
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
	    if(e.getDeathMessage() == null || e.getDeathMessage().equalsIgnoreCase("")) return;
	    if(damageEvent.getCause() != null){
	    	switch(damageEvent.getCause()){
			case BLOCK_EXPLOSION:
				e.setDeathMessage(r.mes("DeathMessages.TNT").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case CONTACT:
				e.setDeathMessage(r.mes("DeathMessages.Cactus").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case CUSTOM:
				e.setDeathMessage(r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case DROWNING:
				e.setDeathMessage(r.mes("DeathMessages.Drowning").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case ENTITY_ATTACK:
				break;
			case ENTITY_EXPLOSION:
				e.setDeathMessage(r.mes("DeathMessages.Creeper").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case FALL:
				 e.setDeathMessage(r.mes("DeathMessages.Fall").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case FALLING_BLOCK:
				e.setDeathMessage(r.mes("DeathMessages.FallingBlock").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case FIRE:
				e.setDeathMessage(r.mes("DeathMessages.Fire").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case FIRE_TICK:
				 e.setDeathMessage(r.mes("DeathMessages.Fire").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case LAVA:
				 e.setDeathMessage(r.mes("DeathMessages.Lava").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case LIGHTNING:
	    		  e.setDeathMessage(r.mes("DeathMessages.Lightning").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case MAGIC:
				e.setDeathMessage(r.mes("DeathMessages.Potion").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case MELTING:
				e.setDeathMessage(r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case POISON:
				 e.setDeathMessage(r.mes("DeathMessages.Potion").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case PROJECTILE:
				e.setDeathMessage(r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case STARVATION:
				e.setDeathMessage(r.mes("DeathMessages.Hunger").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case SUFFOCATION:
				e.setDeathMessage(r.mes("DeathMessages.Suffocated").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case SUICIDE:
	    		  e.setDeathMessage(r.mes("DeathMessages.Suicide").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case THORNS:
				e.setDeathMessage(r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case VOID:
				 e.setDeathMessage(r.mes("DeathMessages.OutOfWorld").replaceAll("%Player", UC.getPlayer(p).getNick()));
				break;
			case WITHER:
				e.setDeathMessage(r.mes("DeathMessages.Wither").replaceAll("%Player", UC.getPlayer(p).getNick()));
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
					Player k = (Player) pr.getShooter();
					String name = "a Bow";
					if(k.getItemInHand().hasItemMeta() && k.getItemInHand().getItemMeta().hasDisplayName()) name = k.getItemInHand().getItemMeta().getDisplayName();
					return r.mes("DeathMessages.Player").replaceAll("%Killed", UC.getPlayer(p).getNick()).replaceAll("%Killer", (UC.getPlayer((Player) pr.getShooter()).getNick())).replaceAll("%Weapon", name);
					
				}else if(pr.getShooter() instanceof Skeleton){
					Skeleton sk = (Skeleton) pr.getShooter();
					if(sk.getSkeletonType().equals(SkeletonType.NORMAL)){
						return (r.mes("DeathMessages.Skeleton").replaceAll("%Player", UC.getPlayer(p).getNick()));
					}else{
						return (r.mes("DeathMessages.WitherSkeleton").replaceAll("%Player", UC.getPlayer(p).getNick()));
					}
					
				}else if(pr.getShooter() instanceof Ghast){
					return (r.mes("DeathMessages.Ghast").replaceAll("%Player", UC.getPlayer(p).getNick()));
				}else if(pr.getShooter() instanceof Blaze){
					return (r.mes("DeathMessages.Blaze").replaceAll("%Player", UC.getPlayer(p).getNick()));
				}else{
					return (r.mes("DeathMessages.Projectile").replaceAll("%Player", UC.getPlayer(p).getNick()));
				}
				
			case BAT:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case BLAZE:
				return (r.mes("DeathMessages.Blaze").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case BOAT:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case CAVE_SPIDER:
				return (r.mes("DeathMessages.CaveSpider").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case CHICKEN:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case COMPLEX_PART:
				return (r.mes("DeathMessages.EnderDragon").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case COW:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case CREEPER:
				return (r.mes("DeathMessages.Creeper").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case DROPPED_ITEM:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case EGG:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case ENDERMAN:
				return (r.mes("DeathMessages.Enderman").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case ENDER_CRYSTAL:
				return (r.mes("DeathMessages.EnderCrystal").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case ENDER_DRAGON:
				return (r.mes("DeathMessages.EnderDragon").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case ENDER_PEARL:
				return (r.mes("DeathMessages.Enderpearl").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case ENDER_SIGNAL:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case EXPERIENCE_ORB:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case FALLING_BLOCK:
				return (r.mes("DeathMessages.Suffocated").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case FIREBALL:
				return (r.mes("DeathMessages.Ghast").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case FIREWORK:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case FISHING_HOOK:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case GHAST:
				return (r.mes("DeathMessages.Ghast").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case GIANT:
				return (r.mes("DeathMessages.Giant").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case HORSE:
				return (r.mes("DeathMessages.Horse").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case IRON_GOLEM:
				return (r.mes("DeathMessages.IronGolem").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case ITEM_FRAME:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case LEASH_HITCH:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case LIGHTNING:
				return (r.mes("DeathMessages.Lightning").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case MAGMA_CUBE:
				return (r.mes("DeathMessages.MagmaCube").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case MINECART:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case MINECART_CHEST:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case MINECART_COMMAND:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case MINECART_FURNACE:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case MINECART_HOPPER:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case MINECART_MOB_SPAWNER:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case MINECART_TNT:
				return (r.mes("DeathMessages.TNT").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case MUSHROOM_COW:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case OCELOT:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case PAINTING:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case PIG:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case PIG_ZOMBIE:
				return (r.mes("DeathMessages.ZombiePigMan").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case PLAYER:
				Player k = (Player) ev.getDamager();
				String name = ItemUtil.getTypeName(((Player) ev.getDamager()).getItemInHand());
				if(k.getItemInHand().hasItemMeta() && k.getItemInHand().getItemMeta().hasDisplayName()){
					name = k.getItemInHand().getItemMeta().getDisplayName();
				}else{
					if(name.toLowerCase().startsWith("a") || name.toLowerCase().startsWith("e") || name.toLowerCase().startsWith("o") || name.toLowerCase().startsWith("u") || name.toLowerCase().startsWith("i")){
						name = "an " + name;
					}else{
						name = "a " + name;
					}
				}
				name = ChatColor.stripColor(name);
				return (r.mes("DeathMessages.Player").replaceAll("%Killed", UC.getPlayer(p).getNick()).replaceAll("%Killer", ((Player) ev.getDamager()).getName()).replaceAll("%Weapon", /**/ name /**/));
				
			case PRIMED_TNT:
				return (r.mes("DeathMessages.TNT").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case SHEEP:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case SILVERFISH:
				return (r.mes("DeathMessages.Silverfish").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case SKELETON:
				Skeleton skel = (Skeleton) ev.getDamager();
				if(skel.getSkeletonType().equals(SkeletonType.NORMAL)){
				return (r.mes("DeathMessages.Skeleton").replaceAll("%Player", UC.getPlayer(p).getNick()));
				}else{
					return (r.mes("DeathMessages.WitherSkeleton").replaceAll("%Player", UC.getPlayer(p).getNick()));
				}
				
			case SLIME:
				return (r.mes("DeathMessages.Slime").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case SMALL_FIREBALL:
				return (r.mes("DeathMessages.Blaze").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case SNOWBALL:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case SNOWMAN:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case SPIDER:
				return (r.mes("DeathMessages.Spider").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case SPLASH_POTION:
				return (r.mes("DeathMessages.Potion").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case SQUID:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case THROWN_EXP_BOTTLE:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case UNKNOWN:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case VILLAGER:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case WEATHER:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case WITCH:
				return (r.mes("DeathMessages.Witch").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case WITHER:
				return (r.mes("DeathMessages.Wither").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case WITHER_SKULL:
				return (r.mes("DeathMessages.Wither").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case WOLF:
				return (r.mes("DeathMessages.Wolf").replaceAll("%Player", UC.getPlayer(p).getNick()));
				
			case ZOMBIE:
				return (r.mes("DeathMessages.Zombie").replaceAll("%Player", UC.getPlayer(p).getNick()));
			case ARMOR_STAND:
				return (r.mes("DeathMessages.Unknown").replaceAll("%Player", UC.getPlayer(p).getNick()));
			case ENDERMITE:
				return (r.mes("DeathMessages.Endermite").replaceAll("%Player", UC.getPlayer(p).getNick()));
			case GUARDIAN:
				return (r.mes("DeathMessages.Guardian").replaceAll("%Player", UC.getPlayer(p).getNick()));
			case RABBIT:
				return (r.mes("DeathMessages.Rabbit").replaceAll("%Player", UC.getPlayer(p).getNick()));
			default:
				break;
		    }
		}
		return "FALSE";
	}

}

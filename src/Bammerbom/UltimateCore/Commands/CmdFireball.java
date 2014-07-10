package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import Bammerbom.UltimateCore.r;

public class CmdFireball implements Listener{
	static Plugin plugin;
	public CmdFireball(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	public static void handle(CommandSender sender, String[] args) {
		if(!r.isPlayer(sender)){
			return;
		}
		Player p = (Player)sender;
		if(!r.perm(p, "uc.fireball", false, true)) return;
		Class type = Fireball.class;
		 if (args.length > 0)
		    {
		      if (args[0].equalsIgnoreCase("small"))
		      {
		        type = SmallFireball.class;
		      }
		      else if (args[0].equalsIgnoreCase("arrow"))
		      {
		        type = Arrow.class;
		      }
		      else if (args[0].equalsIgnoreCase("skull"))
		      {
		        type = WitherSkull.class;
		      }
		      else if (args[0].equalsIgnoreCase("egg"))
		      {
		        type = Egg.class;
		      }
		      else if (args[0].equalsIgnoreCase("snowball"))
		      {
		        type = Snowball.class;
		      }
		      else if (args[0].equalsIgnoreCase("expbottle"))
		      {
		        type = ThrownExpBottle.class;
		      }
		      else if (args[0].equalsIgnoreCase("large"))
		      {
		        type = LargeFireball.class;
		      }
		    }
		    Vector direction = p.getEyeLocation().getDirection().multiply(2);
		    Projectile projectile = (Projectile)p.getWorld().spawn(p.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), type);
		    projectile.setShooter(p);
		    projectile.setVelocity(direction);
	}
}

package Bammerbom.UltimateCore.Commands;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdKittycannon {
	static Plugin plugin;
	public CmdKittycannon(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	private static final Random random = new Random();
	public static void handle(CommandSender sender, String[] args){
		if(!(r.isPlayer(sender))){
			return;
		}
		if(!r.perm(sender, "uc.kittycannon", false, true)){
			return;
		}
		Player p = (Player)sender;
	    final Ocelot ocelot = (Ocelot)p.getWorld().spawnEntity(p.getLocation(), EntityType.OCELOT);
	    if (ocelot == null)
	    {
	      return;
	    }
	    int i = random.nextInt(Ocelot.Type.values().length);
	    ocelot.setCatType(Ocelot.Type.values()[i]);
	    ocelot.setTamed(true);
	    ocelot.setBaby();
	    ocelot.setVelocity(p.getEyeLocation().getDirection().multiply(2));
	    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
	    {
	      public void run()
	      {
	        Location loc = ocelot.getLocation();
	        ocelot.playEffect(EntityEffect.HURT);
	        ocelot.remove();
	        loc.getWorld().createExplosion(loc, 0.0F, false);
	      }
	    }
	    , 15L);
	}
}

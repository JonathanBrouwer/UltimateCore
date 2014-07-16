package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;

public class CmdGod implements Listener{
	static Plugin plugin;
	public CmdGod(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
		if(r.checkArgs(args, 0) == false){
			if(!(r.isPlayer(sender))){
				return;
			}
			if(r.perm(sender, "uc.god", false, true) == false){
				return;
			}
			Player p = (Player)sender;
			UltimateConfiguration pconf = null;
			String status = "on";
			pconf = UltimateFileLoader.getPlayerConfig(p);
			 if(pconf.get("godmode") == null){
				 pconf.set("godmode", true); 
			 }
			 else if(pconf.getBoolean("godmode") == true){
				 pconf.set("godmode", false);
				 status = "off";
			 }else{
				 pconf.set("godmode", true);
			 }
			 pconf.save(UltimateFileLoader.getPlayerFile(p));
			sender.sendMessage(r.mes("God.forSelf").replaceAll("%Status", status));
		}else{
			if(r.perm(sender, "uc.god.others", false, true) == false){
				return;
			}
			Player t = UC.searchPlayer(args[0]);
			if(t == null){
				sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			}else{
				UltimateConfiguration pconf = null;
				String status = "on";
				pconf = UltimateFileLoader.getPlayerConfig(t);
				 if(pconf.get("godmode") == null){
					 pconf.set("godmode", true); 
				 }
				 else if(pconf.getBoolean("godmode") == true){
					 pconf.set("godmode", false);
					 status = "off";
				 }else{
					 pconf.set("godmode", true);
				 }
				 pconf.save(UltimateFileLoader.getPlayerFile(t));
				sender.sendMessage(r.mes("God.selfMessage").replaceAll("%Status", status).replaceAll("%Player", t.getName()));
				t.sendMessage(r.mes("God.otherMessage").replaceAll("%Status", status));
			}
		}
        
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onDamage(EntityDamageByEntityEvent e){
		if(e.getEntity().getType().equals(EntityType.PLAYER)){
		    Player p = (Player) e.getEntity();
		    if(UltimateFileLoader.getPlayerConfig(p).getBoolean("godmode") == true){
		    	e.setCancelled(true);
		    	p.setFireTicks(0);
		    	p.setHealth(20.0);
		    	p.setRemainingAir(p.getMaximumAir());
		    }
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onDamage(EntityDamageEvent e){
		if(e.getEntity().getType().equals(EntityType.PLAYER)){
		    Player p = (Player) e.getEntity();
		    if(UltimateFileLoader.getPlayerConfig(p).getBoolean("godmode") == true){
		    	p.setFireTicks(0);
		    	p.setHealth(20.0);
		    	p.setRemainingAir(p.getMaximumAir());
		    	e.setCancelled(true);
		    }
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
    public void foodMode(FoodLevelChangeEvent event){
        Entity e = event.getEntity();
        if(e instanceof Player){
        	 if(UltimateFileLoader.getPlayerConfig(((Player) e).getPlayer()).getBoolean("godmode") == true){
                event.setCancelled(true);
                Player p = (Player) e;
                p.setFoodLevel(20);
        	 }
        }
    }

}

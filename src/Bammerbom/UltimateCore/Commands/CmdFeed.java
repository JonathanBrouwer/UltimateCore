package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

public class CmdFeed implements Listener{
	static Plugin plugin;
	public CmdFeed(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
		if(!r.isPlayer(sender)){
			return;
		}
		Player p = (Player)sender;
		Boolean tb = false;
		Player t = p;
		if(r.checkArgs(args, 0) == true){
			if(r.perm(sender, "uc.feed.others", false, true) == false){
				return;
			}
			Player tl = Bukkit.getPlayer(args[0]);
			if(tl == null){
				sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
				return;
			}
			t = tl;
			tb = true;
		}else{
			if(r.perm(sender, "uc.feed", false, true) == false){
				return;
			}
		}
		if(r.getCnfg().getBoolean("FeedMode") == false){
			feed(t);
			if(tb == false){
				p.sendMessage(r.mes("Feed.forSelf"));
			}else{
				p.sendMessage(r.mes("Feed.selfMessage").replaceAll("%Player", t.getName()));
				t.sendMessage(r.mes("Feed.otherMessage"));
			}
		}else{
			UltimateConfiguration data = new UltimateConfiguration(UltimateFileLoader.getPlayerFile(t));
			
			if(UltimateFileLoader.getPlayerConfig(t).get("feedmode") != null && UltimateFileLoader.getPlayerConfig(t).getBoolean("feedmode") == true){
				data.set("feedmode", false);
				if(tb == false){
					p.sendMessage(r.mes("Feed.forSelfMode").replaceAll("%Status", "off"));
				}else{
					p.sendMessage(r.mes("Feed.selfMessageMode").replaceAll("%Status", "off").replaceAll("%Player", t.getName()));
					t.sendMessage(r.mes("Feed.otherMessageMode").replaceAll("%Status", "off"));
				}
			}else{
				data.set("feedmode", true);
				feed(t);
				if(tb == false){
					p.sendMessage(r.mes("Feed.forSelfMode").replaceAll("%Status", "on"));
				}else{
					p.sendMessage(r.mes("Feed.selfMessageMode").replaceAll("%Status", "on").replaceAll("%Player", t.getName()));
					t.sendMessage(r.mes("Feed.otherMessageMode").replaceAll("%Status", "on"));
				}
			}
			
			data.save(UltimateFileLoader.getPlayerFile(t));
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onFeedLose(FoodLevelChangeEvent e){
		if(e.getEntity() instanceof Player){
			UltimateConfiguration data = new UltimateConfiguration(UltimateFileLoader.getPlayerFile((Player) e.getEntity()));
			if(data.getBoolean("feedmode") == true){
				if(r.getCnfg().getBoolean("FeedMode") == false){
					return;
				}
				feed((Player) e.getEntity());
				e.setCancelled(true);
			}
		}
	}
	
	
	
	
	
	
	
	
	//
	public static void feed(Player p){
		p.setFoodLevel(20);
	}
}

package Bammerbom.UltimateCore.Commands;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.API.UCplayer;
import Bammerbom.UltimateCore.Resources.Utils.DateUtil;
import Bammerbom.UltimateCore.Resources.Utils.LocationUtil;

public class CmdJail implements Listener{
	static Plugin plugin;
	public CmdJail(Plugin instance){
		plugin = instance;
		if(r.getCnfg().contains("jailmove")){
			m = !r.getCnfg().getBoolean("jailmove");
		}
		if(this instanceof Listener){
			if(m){
				Bukkit.getPluginManager().registerEvent(PlayerMoveEvent.class, this, EventPriority.HIGH, new EventExecutor(){
					public void execute(Listener l, Event e)
							throws EventException {
						PlayerMoveEvent ev = (PlayerMoveEvent) e;
						if(ev.getFrom().getBlock().getLocation().equals(ev.getTo().getBlock().getLocation())) return;
						if(UC.getPlayer(ev.getPlayer()).isJailed()) ev.setTo(ev.getFrom());
					}
				}, plugin);
			}
			Bukkit.getPluginManager().registerEvents(this, plugin);
		}
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run(){
				for(Player p : Bukkit.getOnlinePlayers()){
					if(UC.getPlayer(p).isJailed()){
					if(jailgone(p, true)){
						UC.getPlayer(p).setJailed(false, "", 0L);
						p.sendMessage(r.mes("Jail.Unjail"));
						p.teleport(UC.getServer().getCustomSpawn() != null ? UC.getServer().getCustomSpawn() : p.getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
					}
					}
				}
			}
		}, 100L, 100L);
	}
	
	Boolean m = true;
	static Random ra = new Random();
	public static void jail(CommandSender sender, String label, String[] args){
		if(r.checkArgs(args, 0) == false){
			if(!r.perm(sender, "uc.jail", false, true)) return;
		   UltimateConfiguration conf = new UltimateConfiguration(UltimateFileLoader.DFjails);
		   StringBuilder b = new StringBuilder(r.mes("Jail.List").replaceAll("%Jails", ""));
		   Boolean a = false;
		   for(String str : conf.getConfigurationSection("Jails").getKeys(true)){
			   if(a) b.append(", ");
			   a = true;
			   b.append(str);
		   }
		   if(!a) b.append("none");
		   sender.sendMessage(b.toString());
		}else{
			if(!r.perm(sender, "uc.jail", false, true)) return;
			if(!r.checkArgs(args, 0)){
				sender.sendMessage(r.mes("Jail.Usage"));
				return;
			}
			UltimateConfiguration conf = new UltimateConfiguration(UltimateFileLoader.DFjails);
			String player = args[0];
			if(Bukkit.getPlayer(player) == null){
				sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
				return;
			}
			Player t = Bukkit.getPlayer(player);
			UCplayer ut = UC.getPlayer(t);
			if(ut.isJailed()){
				sender.sendMessage(r.mes("Jail.AlreadyJailed").replaceAll("%Player", args[0]));
				return;
			}
			String jail = "";
			Long time = -1L;
			if(r.checkArgs(args, 1) && DateUtil.getTimeMillis(args[1]) != -1){
				time = DateUtil.getTimeMillis(args[1]);
			}
			if(r.checkArgs(args, 2)){
				jail = args[2];
			}else if(r.checkArgs(args, 1) && time == -1){
				jail = args[1];
			}else{
				ArrayList<String> jails = new ArrayList<String>();
				   for(String str : conf.getConfigurationSection("Jails").getKeys(true)){
					   jails.add(str);
				   }	
				   jail = jails.get(ra.nextInt(jails.size()));
			}
			if(conf.get("Jails." + jail) == null){
				sender.sendMessage(r.mes("Jail.NotFound"));
				return;
			}
			Location loc = LocationUtil.convertStringToLocation(conf.getString("Jails." + jail));
			t.teleport(loc, TeleportCause.PLUGIN);
			t.setGameMode(GameMode.ADVENTURE);
			ut.setJailed(true, jail, time);
			t.sendMessage(r.mes("Jail.JailTarget").replaceAll("%Time", (time != -1L ? DateUtil.format(time) : "ever")));
			sender.sendMessage(r.mes("Jail.JailSender").replaceAll("%Player", t.getName()).replaceAll("%Jail", jail).replaceAll("%Time", (time != -1L ? DateUtil.format(time) : "ever")));
		}
	}
	public static boolean jailgone(OfflinePlayer p, Boolean directreset){
		final UltimateConfiguration conf = new UltimateConfiguration(UltimateFileLoader.getPlayerFile(p));
		if(conf.get("jail") == null) return false;
		if(conf.getLong("jailtime") == 0 || conf.getLong("jailtime") == -1) return false;
		if(System.currentTimeMillis() >= conf.getLong("jailtime")){
			if(directreset == true){
				UC.getPlayer(p).setJailed(false, "", 0L);
			}
			return true;
		}
		return false;
	}
	public static void unJail(CommandSender sender, String label, String[] args){
		if(!r.perm(sender, "uc.unjail", false, true)) return;
		if(!r.checkArgs(args, 0)){
			sender.sendMessage(r.mes("Jail.Usage2"));
			return;
		}
		Player t = Bukkit.getPlayer(args[0]);
		if(t == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		UCplayer ut = UC.getPlayer(t);
		if(!ut.isJailed()){
			sender.sendMessage(r.mes("Jail.NotJailed").replaceAll("%Player", t.getName()));
			return;
		}
		ut.setJailed(false, null, 0L);
		//
		UltimateConfiguration data = new UltimateConfiguration(UltimateFileLoader.DFspawns);
	    if(data.get("spawn") != null){
		 String[] loc = data.getString("spawn").split(",");
	        World w = Bukkit.getWorld(loc[0]);
	        Double x = Double.parseDouble(loc[1]);
	        Double y = Double.parseDouble(loc[2]);
	        Double z = Double.parseDouble(loc[3]);
	        float yaw = Float.parseFloat(loc[4]);
	        float pitch = Float.parseFloat(loc[5]);
	        Location location = new Location(w, x, y, z, yaw, pitch);
			t.teleport(location, TeleportCause.PLUGIN);
	    }
	    t.setGameMode(Bukkit.getDefaultGameMode());
		//
		sender.sendMessage(r.mes("Jail.UnjailSender").replaceAll("%Player", t.getName()));
	}
	public static void setJail(CommandSender sender, String label, String[] args){
		if(!r.perm(sender, "uc.setjail", false, true)) return;
		if(!r.checkArgs(args, 0)){
			sender.sendMessage(r.mes("Jail.Usage3"));
			return;
		}
		if(!r.isPlayer(sender)) return;
		Player p = (Player) sender;
		String loc = LocationUtil.convertLocationToString(p.getLocation());
		UltimateConfiguration conf = new UltimateConfiguration(UltimateFileLoader.DFjails);
		conf.set("Jails." + args[0], loc);
		conf.save(UltimateFileLoader.DFjails);
		sender.sendMessage(r.mes("Jail.JailSet").replaceAll("%Name", args[0]));
	}
	public static void delJail(CommandSender sender, String label, String[] args){
		if(!r.perm(sender, "uc.deljail", false, true)) return;
		if(!r.checkArgs(args, 0)){
			sender.sendMessage(r.mes("Jail.Usage4"));
			return;
		}
		UltimateConfiguration conf = new UltimateConfiguration(UltimateFileLoader.DFjails);
		if(conf.get("Jails." + args[0]) == null){
			sender.sendMessage(r.mes("Jail.NotFound").replaceAll("%Jail", args[0]));
			return;
		}
		conf.set("Jails." + args[0], null);
		conf.save(UltimateFileLoader.DFjails);
        sender.sendMessage(r.mes("Jail.JailRemoved").replaceAll("%Name", args[0]));
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(UC.getPlayer(e.getPlayer()).isJailed()) e.setCancelled(true);
	}
	@EventHandler
	public void onTP(PlayerTeleportEvent e){
		if(e.getCause().equals(TeleportCause.UNKNOWN) && m) return;
		if(UC.getPlayer(e.getPlayer()).isJailed()) e.setCancelled(true);
	}
	@EventHandler
	public void onDamage(EntityDamageEvent e){
		if(!(e.getEntity() instanceof Player)) return;
		if(UC.getPlayer((Player) e.getEntity()).isJailed()) e.setCancelled(true);
	}
	@EventHandler
	public void onTP(PlayerCommandPreprocessEvent e){
		if(UC.getPlayer(e.getPlayer()).isJailed()){
			if(!e.getMessage().startsWith("unjail") && !e.getMessage().startsWith("/unjail")){
				e.setCancelled(true);
				e.getPlayer().sendMessage(r.mes("Jail.CantUseCommands"));
			}
		}
	}
	public static void jails(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.jaillist", false, true)) return;
		StringBuilder mutes = new StringBuilder();
		Integer i = 0;
		for(OfflinePlayer pl : Bukkit.getOfflinePlayers()){
			if(UC.getPlayer(pl).isJailed()){
				if(!mutes.toString().equalsIgnoreCase(""))mutes.append(", ");
				mutes.append(pl.getName());
				i++;
			}
		}
		sender.sendMessage(r.mes("Jail.List2").replaceAll("%Amount", i + "").replaceAll("%List", mutes.toString()));
	}
}

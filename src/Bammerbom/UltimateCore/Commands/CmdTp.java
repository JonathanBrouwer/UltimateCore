package Bammerbom.UltimateCore.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateCommands;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.API.UCplayer;

public class CmdTp implements Listener{
	static Plugin plugin;
	public CmdTp(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static Map <String, String> tp = new HashMap <String, String>();
	public static Map <String, String> tph = new HashMap <String, String>();
	public static ArrayList<String> oi = new ArrayList<String>();
	public static void handle(CommandSender sender, String label, String[] args){
		if(label.equalsIgnoreCase("tptoggle")){
			tptoggle(sender);
			return;
		}
		if(label.equalsIgnoreCase("tpaccept") || label.equalsIgnoreCase("tpyes")){
			if(!r.isPlayer(sender)){
				return;
			}
			if(!r.perm(sender, "uc.tpaccept", true, false)){
				sender.sendMessage(r.mes("NoPermissions"));
				return;
			}
			tpaccept(sender);
			return;
		}
		if(label.equalsIgnoreCase("tpdeny") || label.equalsIgnoreCase("tpno")){
			if(!r.isPlayer(sender)){
				return;
			}
			if(!r.perm(sender, "uc.tpdeny", true, false)){
				sender.sendMessage(r.mes("NoPermissions"));
				return;
			}
			tpdeny(sender);
			return;
		}
		if(label.equalsIgnoreCase("tpaall")){
			if(!r.perm(sender, "uc.tpaall", true, true)) return;
			if(!r.isPlayer(sender)) return;
			final Player p = (Player) sender;
			p.sendMessage(r.mes("Tp.Tpa.Send").replaceAll("%Player", "everyone"));
			for(final Player t : UC.getOnlinePlayers()){
				if(UC.getPlayer(t).hasTeleportEnabled()){
			tph.put(t.getName(), p.getName());
			t.sendMessage(r.mes("Tp.Tpa.TpaTarget1_HERE").replaceAll("%Player", p.getName()));
			t.sendMessage(r.mes("Tp.Tpa.TpaTarget2").replaceAll("%Player", p.getName()));
			t.sendMessage(r.mes("Tp.Tpa.TpaTarget3").replaceAll("%Player", p.getName()));
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, 
					new Runnable(){
						public void run() {
							tph.remove(p.getName());
							tph.remove(t.getName());
							
						}}, plugin.getConfig().getInt("Tp.TpaCancelDelay") * 20L);
			}
			}
			return;
		}
		if(label.equalsIgnoreCase("tpahere")){
			if(!r.isPlayer(sender)) return;
			if(!r.perm(sender, "uc.tpahere", true, true)) return;
			if(!r.checkArgs(args, 0)){
				sender.sendMessage(r.default1 + "/tpahere " + r.default2 + "<Player>");
				return;
			}
			final Player p = (Player) sender;
			final Player t = UC.searchPlayer(args[0]);
			if(t == null){ p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0])); return; }
			if(UC.getPlayer(t).hasTeleportEnabled() == false && !r.perm(sender, "uc.tptoggle.override", false, false)){
				p.sendMessage(r.default1 + "This player has teleportation Disabled!");
				return;
			}
			p.sendMessage(r.mes("Tp.Tpa.Send").replaceAll("%Player", t.getName()));
			tph.put(t.getName(), p.getName());
			t.sendMessage(r.mes("Tp.Tpa.TpaTarget1_HERE").replaceAll("%Player", p.getName()));
			t.sendMessage(r.mes("Tp.Tpa.TpaTarget2").replaceAll("%Player", p.getName()));
			t.sendMessage(r.mes("Tp.Tpa.TpaTarget3").replaceAll("%Player", p.getName()));
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, 
					new Runnable(){
						public void run() {
							tph.remove(p.getName());
							tph.remove(t.getName());
							
						}}, plugin.getConfig().getInt("Tp.TpaCancelDelay") * 20L);
			return;
		}
		if(r.checkArgs(args, 0) == false){
			if(!r.isPlayer(sender)){
				return;
			}
			if(!r.perm(sender, "uc.tp", false, false) && !r.perm(sender, "uc.tpa", true, false)){
				sender.sendMessage(r.mes("NoPermissions"));
				return;
			}
			openTpMenu((Player) sender);
			return;
		}
		if(r.isNumber(args[0])){
			if(!r.isPlayer(sender)){
				return;
			}
			if(!r.perm(sender, "uc.tp", false, false) && !r.perm(sender, "uc.tp.co.others", false, false)){
				sender.sendMessage(r.mes("NoPermissions"));
				return;
			}else{
				tpCo((Player) sender, args, false);
				return;
			}
		}
		if(r.checkArgs(args, 1) == true && r.isNumber(args[1]) && !(r.isNumber(args[0]))){
			if(!r.perm(sender, "uc.tp", false, false) && !r.perm(sender, "uc.tp.co", false, false)){
				sender.sendMessage(r.mes("NoPermissions"));
				return;
			}else{
				if(!r.isPlayer(sender)) return;
				tpCo((Player) sender, args, true);
				return;
			}
		}
		Player tg = UC.searchPlayer(args[0]);
		if(tg == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
		}else{
			if(label.equalsIgnoreCase("tpa")){
				if(!r.isPlayer(sender)){
					return;
				}
				if(!r.perm(sender, "uc.tp", false, false) && !r.perm(sender, "uc.tpa", true, false)){
					sender.sendMessage(r.mes("NoPermissions"));
					return;
				}
				tpa((Player) sender, tg);
				return;
			}
			if(!r.perm(sender, "uc.tp", false, false) && !r.perm(sender, "uc.tp.tp", false, false)){
				if(!r.isPlayer(sender)){
					return;
				}
				if(!r.perm(sender, "uc.tp", false, false) && !r.perm(sender, "uc.tpa", true, false)){
					sender.sendMessage(r.mes("NoPermissions"));
					return;
				}
				tpa((Player) sender, tg);
				return;
			}
			if(!label.equalsIgnoreCase("tp")){
				sender.sendMessage(r.error + "Error. Teleport command not registered.");
				return;
			}
			if(r.checkArgs(args, 1) == false){
				if(!r.isPlayer(sender)){
					return;
				}
				Player p = (Player) sender;
				if(UC.getPlayer(tg).hasTeleportEnabled() == false && !r.perm(sender, "uc.tptoggle.override", false, false)){
					p.sendMessage(r.default1 + "This player has teleportation Disabled!");
					return;
				}
				p.teleport(tg, TeleportCause.COMMAND);
				playEffect(p, tg.getLocation());
				sender
				.sendMessage(r.mes("Tp.Tp1").replaceAll("%Player", tg.getName()));
			}else{
				Player p = UC.searchPlayer(args[1]);
				if(p == null){
					sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[1]));
				}else{
					if(UC.getPlayer(tg).hasTeleportEnabled() == false && !r.perm(sender, "uc.tptoggle.override", false, false)){
						p.sendMessage(r.default1 + "The player " + tg.getName() + " has teleportation Disabled!");
						return;
					}
					if(UC.getPlayer(p).hasTeleportEnabled() == false && !r.perm(sender, "uc.tptoggle.override", false, false)){
						p.sendMessage(r.default1 + "The player " + p.getName() + " has teleportation Disabled!");
						return;
					}
					tg.teleport(p, TeleportCause.COMMAND);
					playEffect(p, p.getLocation());
					sender.sendMessage(r.mes("Tp.Tp2").replaceAll("%Player1", tg.getName()).replaceAll("%Player2", p.getName()));
				}
			}
		
		
		}
		
	}
	public static void tpCo(Player p, String[] args, Boolean ip){
		Player pl = p;
		if(UC.searchPlayer(args[0]) != null){
			pl = UC.searchPlayer(args[0]);
			if(UC.getPlayer(pl).hasTeleportEnabled() == false && !r.perm(p, "uc.tptoggle.override", false, false)){
				p.sendMessage(r.default1 + "This player has teleportation Disabled!");
				return;
			}
		}
		try{
			if(ip){
				World w = p.getWorld();
				Integer x = Integer.parseInt(args[1]);
				Integer y = null;
				Integer z = null;
				if(r.checkArgs(args, 3) == false){
					z = Integer.parseInt(args[2]);
					y = w.getHighestBlockYAt(x, z);
				}else{
					y = Integer.parseInt(args[2]);
					z= Integer.parseInt(args[3]);
				}
				pl.teleport(new Location(w,x,y,z), TeleportCause.COMMAND);
				playEffect(pl, new Location(w,x,y,z));
				p.sendMessage(r.mes("Tp.Tp4").replaceAll("%Player", pl.getName()).replaceAll("%x", x.toString()).replaceAll("%y", y.toString()).replaceAll("%z", z.toString()));
			}else{
				World w = p.getWorld();
				Integer x = Integer.parseInt(args[0]);
				Integer y = null;
				Integer z = null;
				if(r.checkArgs(args, 2) == false){
					z = Integer.parseInt(args[1]);
					y = w.getHighestBlockYAt(x, z);
				}else{
					y = Integer.parseInt(args[1]);
					z= Integer.parseInt(args[2]);
				}
				p.teleport(new Location(w,x,y,z), TeleportCause.COMMAND);
				playEffect(p, new Location(w,x,y,z));
				p.sendMessage(r.mes("Tp.Tp3").replaceAll("%Player", pl.getName()).replaceAll("%x", x.toString()).replaceAll("%y", y.toString()).replaceAll("%z", z.toString()));
			}
		}catch(Exception e){
			p.sendMessage(ChatColor.RED + "Arguments not good entered");
		}
	}
	public static void openTpMenu(Player p){
		if(p == null){
			return;
		}
		if(UC.getOnlinePlayers().length > 64){
			p.sendMessage(r.default1 + "Too much players online to display, use /tp(a) <Player>");
			return;
		}
		Integer size = 9;
		while(UC.getOnlinePlayers().length > size){
			size = size + 9;
		}
		Inventory inv = Bukkit.createInventory(null, size, r.default1 + "Select player");
		for(Player pl : UC.getOnlinePlayers()){
			if(!(pl == p)){
			ItemStack item = new ItemStack(Material.SKULL_ITEM);
			item.setDurability(Short.parseShort("3")); 
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(r.default1 + pl.getName());
			item.setItemMeta(meta);
			inv.addItem(item);
			}
		}
		if(inv.getItem(0) == null){
			Inventory inv2 = Bukkit.createInventory(null, 9, ChatColor.RED + "No players online");
			p.openInventory(inv2);
			oi.add(p.getName());
			return;
		}
		oi.add(p.getName());
		p.openInventory(inv);
	}
	
	public static void tpa(final Player p, final Player t){
		if(UC.getPlayer(t).hasTeleportEnabled() == false && !r.perm(p, "uc.tptoggle.override", false, false)){
			p.sendMessage(r.default1 + "This player has teleportation Disabled!");
			return;
		}
		p.sendMessage(r.mes("Tp.Tpa.Send").replaceAll("%Player", t.getName()));
		tp.put(t.getName(), p.getName());
		if(tph.containsKey(t.getName())){
			tph.remove(t.getName());
		}
		ArrayList<String> remove = new ArrayList<String>();
		for(String s : tph.keySet()){
			if(tph.get(s).equals(t.getName())){
				remove.add(s);
			}
		}
		for(String str : remove) tph.remove(str);
		t.sendMessage(r.mes("Tp.Tpa.TpaTarget1").replaceAll("%Player", p.getName()));
		t.sendMessage(r.mes("Tp.Tpa.TpaTarget2").replaceAll("%Player", p.getName()));
		t.sendMessage(r.mes("Tp.Tpa.TpaTarget3").replaceAll("%Player", p.getName()));
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, 
				new Runnable(){
					public void run() {
						tp.remove(p.getName());
						tp.remove(t.getName());
						
					}}, plugin.getConfig().getInt("Tp.TpaCancelDelay") * 20L);
	}
	public static void tpaccept(CommandSender player) {
		Player p = (Player)player;
		if(tph.containsKey(p.getName())){
			Player t = UC.searchPlayer(tph.get(p.getName()));
			if(t == null) {
				p.sendMessage(r.mes("Tp.Tpa.NoRequests"));
			}else{
				p.teleport(t, TeleportCause.COMMAND);
				playEffect(t, p.getLocation());
				p.sendMessage(r.mes("Tp.Tpa.AcceptTarget"));
				t.sendMessage(r.mes("Tp.Tpa.AcceptSender").replaceAll("%Player", p.getName()));
				tph.remove(p.getName());
				tph.remove(t.getName());
			}

			
			return;
		}
		if(!tp.containsKey(p.getName())){p.sendMessage(r.mes("Tp.Tpa.NoRequests")); return; }
		Player t = UC.searchPlayer(tp.get(p.getName()));
		if(tp.containsKey(p.getName())){
			if(t == null) {
				p.sendMessage(r.mes("Tp.Tpa.NoRequests"));
			}else{
				t.teleport(p, TeleportCause.COMMAND);
				playEffect(t, p.getLocation());
				p.sendMessage(r.mes("Tp.Tpa.AcceptTarget"));
				t.sendMessage(r.mes("Tp.Tpa.AcceptSender").replaceAll("%Player", p.getName()));
				tp.remove(p.getName());
				tp.remove(t.getName());
			}
		}else{
			p.sendMessage(r.mes("Tp.Tpa.NoRequests"));
	    }
	
    }
	public static void tptoggle(CommandSender sender){
		if(!r.isPlayer(sender)) return;
		if(!r.perm(sender, "uc.tptoggle", true, true)) return;
	    Player p = (Player) sender;
	    UCplayer up = UC.getPlayer(p);
	    if(up.hasTeleportEnabled()){
	    	up.setTeleportEnabled(false);
	    	sender.sendMessage(r.default1 + "Teleportation is now " + r.default2 + "Disabled");
	    }else{
	    	up.setTeleportEnabled(true);
	    	sender.sendMessage(r.default1 + "Teleportation is now " + r.default2 + "Enabled");
	    }
	}
	public static void tpdeny(CommandSender player) {
		Player p = (Player)player;
		if(!tp.containsKey(p.getName())){p.sendMessage(r.mes("Tp.Tpa.NoRequests")); return; }
		Player t = UC.searchPlayer(tp.get(p.getName()));
		if(tp.containsKey(p.getName())){
			if(t == null) {
				p.sendMessage(r.mes("Tp.Tpa.NoRequests"));
			}else{
				tp.remove(p.getName());
				tp.remove(t.getName());
				p.sendMessage(r.mes("Tp.Tpa.DenySender"));
				t.sendMessage(r.mes("Tp.Tpa.DenyTarget").replaceAll("%Player", p.getName()));
			}
		}else{
			p.sendMessage(r.mes("Tp.Tpa.NoRequests"));
	    }
	
    }
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInvClick(InventoryClickEvent e){
		if(oi.contains(e.getWhoClicked().getName())){
			oi.remove(e.getWhoClicked().getName());
			e.setCancelled(true);
			Player p = UC.searchPlayer(e.getWhoClicked().getName());
			UltimateCommands.executecommand(p, "tp " + ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
			e.getWhoClicked().closeInventory();
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLeave(PlayerQuitEvent e){
		if(oi.contains(e.getPlayer().getName())){
			oi.remove(e.getPlayer().getName());
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onClose(InventoryCloseEvent e){
		if(oi.contains(e.getPlayer().getName())){
			oi.remove(e.getPlayer().getName());
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onKick(PlayerKickEvent e){
		if(oi.contains(e.getPlayer().getName())){
			oi.remove(e.getPlayer().getName());
		}
	}
	public static void playEffect(Player p, Location loc){
		if(CmdVanish.Vanish(p)) return;
		loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 10);
		loc.getWorld().playSound(loc, Sound.ENDERMAN_TELEPORT, 1, 1);
	}
}

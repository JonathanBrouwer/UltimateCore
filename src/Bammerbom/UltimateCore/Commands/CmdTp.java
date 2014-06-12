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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateCommands;
import Bammerbom.UltimateCore.r;

public class CmdTp implements Listener{
	static Plugin plugin;
	public CmdTp(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static Map <String, String> tp = new HashMap <String, String>();
	public static ArrayList<String> oi = new ArrayList<String>();
	public static void handle(CommandSender sender, String label, String[] args){
		if(label.equalsIgnoreCase("tpaccept")){
			if(!r.isPlayer(sender)){
				return;
			}
			if(!r.perm(sender, "uc.tp", false, false) && !r.perm(sender, "uc.tpa", true, false)){
				sender.sendMessage(r.mes("NoPermissions"));
				return;
			}
			tpaccept(sender);
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
		Player tg = Bukkit.getPlayer(args[0]);
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
			if(r.checkArgs(args, 1) == false){
				if(!r.isPlayer(sender)){
					return;
				}
				Player p = (Player) sender;
				p.teleport(tg);
				playEffect(p, tg.getLocation());
				sender
				.sendMessage(r.mes("Tp.Tp1").replaceAll("%Player", tg.getName()));
			}else{
				Player p = Bukkit.getPlayer(args[1]);
				if(p == null){
					sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[1]));
				}else{
					tg.teleport(p);
					playEffect(p, p.getLocation());
					sender.sendMessage(r.mes("Tp.Tp2").replaceAll("%Player1", tg.getName()).replaceAll("%Player2", p.getName()));
				}
			}
		
		
		}
		
	}
	public static void tpCo(Player p, String[] args, Boolean ip){
		Player pl = p;
		if(Bukkit.getPlayer(args[0]) != null){
			pl = Bukkit.getPlayer(args[0]);
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
				pl.teleport(new Location(w,x,y,z));
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
				p.teleport(new Location(w,x,y,z));
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
		Integer size = 9;
		while(Bukkit.getOnlinePlayers().length > size){
			size = size + 9;
		}
		Inventory inv = Bukkit.createInventory(null, size, r.default1 + "Select player");
		for(Player pl : Bukkit.getOnlinePlayers()){
			if(!(pl == p)){
			ItemStack item = new ItemStack(Material.SKULL_ITEM);
			item.setDurability(Short.parseShort("3")); 
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(r.mes(r.default1 + pl.getName()));
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
		p.sendMessage(r.mes("Tp.Tpa.Send").replaceAll("%Player", t.getName()));
		tp.put(t.getName(), p.getName());
		t.sendMessage(r.mes("Tp.Tpa.TpaTarget1").replaceAll("%Player", p.getName()));
		t.sendMessage(r.mes("Tp.Tpa.TpaTarget2").replaceAll("%Player", p.getName()));
		t.sendMessage(r.mes("Tp.Tpa.TpaTarget3").replaceAll("%Player", p.getName()));
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, 
				new Runnable(){
					public void run() {
						tp.remove(p);
						tp.remove(t);
						
					}}, plugin.getConfig().getInt("Tp.TpaCancelDelay") * 20L);
	}
	public static void tpaccept(CommandSender player) {
		Player p = (Player)player;
		if(!tp.containsKey(p.getName())){p.sendMessage(r.mes("Tp.Tpa.NoRequests")); return; }
		Player t = Bukkit.getPlayer(tp.get(p.getName()));
		if(tp.containsKey(p.getName())){
			if(t == null) {
				p.sendMessage(r.mes("Tp.Tpa.NoRequests"));
			}else{
				t.teleport(p);
				playEffect(t, p.getLocation());
				p.sendMessage(r.mes("Tp.Tpa.AcceptTarget"));
				t.sendMessage(r.mes("Tp.Tpa.AcceptSender"));
				tp.remove(p);
				tp.remove(t);
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
			Player p = Bukkit.getPlayer(e.getWhoClicked().getName());
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

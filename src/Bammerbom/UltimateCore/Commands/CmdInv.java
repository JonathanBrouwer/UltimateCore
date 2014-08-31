package Bammerbom.UltimateCore.Commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Utils.InventoryUtil;

public class CmdInv implements Listener{
	static Plugin plugin;
	public CmdInv(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	static ArrayList<UUID> inOnlineInv = new ArrayList<UUID>();
	static ArrayList<UUID> inOfflineInv = new ArrayList<UUID>();
	
	@SuppressWarnings("deprecation")
	public static void handle(CommandSender sender, String[] args) {
		if(!(r.isPlayer(sender))){
			return;
		}
		if(!r.perm(sender, "uc.inv", false, true)){ return; }
		Player p = (Player)sender;
    	if(r.checkArgs(args, 0)){
    		Player target = r.searchPlayer(args[0]);
    		if(target !=null){
    			//Inventory inv = Bukkit.createInventory(null, 45, ChatColor.RED + "Inventory from " + ChatColor.BLUE + target.getName());
    			PlayerInventory pinv = target.getInventory();
	    	    p.openInventory(pinv);
	    	    inOnlineInv.add(p.getUniqueId());
    	  	}else{
    	  		OfflinePlayer t = Bukkit.getOfflinePlayer(args[0]);
    	  		if(t == null){
    	  			p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
    	  			return;
    	  		}
	  			UltimateConfiguration conf = new UltimateConfiguration(UltimateFileLoader.getPlayerFile(t));
	  			if(conf.get("lastinventory") == null){
	  				p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
    	  			return;
	  			}
	  			Inventory inv = InventoryUtil.StringToInventory(conf.getString("lastinventory"));
	  			p.openInventory(inv);
	  			inOfflineInv.add(p.getUniqueId());
    	  	}
    	}else{
    		p.sendMessage(r.mes("Invsee.Usage"));
    	}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerquit(PlayerQuitEvent e){
		UltimateConfiguration conf = new UltimateConfiguration(UltimateFileLoader.getPlayerFile(e.getPlayer()));
	    String inv = InventoryUtil.InventoryToString(e.getPlayer().getInventory());
	    conf.set("lastinventory", inv);
	    conf.save(UltimateFileLoader.getPlayerFile(e.getPlayer()));
	    inOfflineInv.remove(e.getPlayer().getUniqueId());
	    inOnlineInv.remove(e.getPlayer().getUniqueId());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClick(InventoryClickEvent e){
		if((inOfflineInv.contains(e.getWhoClicked().getUniqueId()) || inOnlineInv.contains(e.getWhoClicked().getUniqueId()))){
			if(!r.perm((CommandSender) e.getWhoClicked(), "uc.inv.edit", false, false) && !e.getInventory().getHolder().equals(e.getWhoClicked())){ 
				e.setCancelled(true);
				return;
			}
			if(inOfflineInv.contains(e.getWhoClicked().getUniqueId())){
				e.setCancelled(true);
			}
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryDrag(InventoryDragEvent e){
			if(!r.perm((CommandSender) e.getWhoClicked(), "uc.inv.edit", false, false) && !e.getInventory().getHolder().equals(e.getWhoClicked())){ 
				e.setCancelled(true);
				return;
			}
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onInventoryClose(InventoryCloseEvent e){
		inOfflineInv.remove(e.getPlayer().getUniqueId());
	    inOnlineInv.remove(e.getPlayer().getUniqueId());
	}
}

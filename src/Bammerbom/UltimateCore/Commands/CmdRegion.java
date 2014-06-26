package Bammerbom.UltimateCore.Commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Region;
import Bammerbom.UltimateCore.Resources.Utils.InventoryUtil;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class CmdRegion implements Listener{
	static Plugin plugin;
	static Boolean worldedit = false;
	static WorldEditPlugin we;
	static ItemStack customwand = new ItemStack(Material.WOOD_AXE);{
		ItemMeta meta = customwand.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_AQUA + "Region Wand");
		customwand.setItemMeta(meta);
	}
	static ItemStack worldeditwand;
	@SuppressWarnings("deprecation")
	public CmdRegion(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		if(Bukkit.getPluginManager().getPlugin("WorldEdit") != null && Bukkit.getPluginManager().isPluginEnabled("WorldEdit")){
			worldedit = true;
			Plugin worldeditplugin = Bukkit.getPluginManager().getPlugin("WorldEdit");
			if(!(worldeditplugin instanceof WorldEditPlugin)){
				r.log(r.error + "WorldEdit incorrect. Is worldedit outdated?\n" + r.error + "Using UltimateCore wand.");
				worldedit = false;
			}
			if(worldeditplugin != null && ((WorldEditPlugin)worldeditplugin).getLocalConfiguration() != null){
			we = (WorldEditPlugin) worldeditplugin;
			worldeditwand = new ItemStack(Material.getMaterial(we.getLocalConfiguration().wandItem));
			}else{
				r.log(r.error + "WorldEdit incorrect. Is worldedit outdated?\n" + r.error + "Using UltimateCore wand.");
				worldedit = false;
			}
		}
	}
	@SuppressWarnings("deprecation")
	public static void handle(CommandSender sender, String[] args) {
		if(!(r.isPlayer(sender))){
			return;
		}
		if(!r.perm(sender, "uc.region", false, true)){
			return;
		}
		Player p = (Player)sender;
		if(!r.checkArgs(args, 0)){
			//TODO message
			return;
		}
		if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("new") || args[0].equalsIgnoreCase("define")){
			create(p, args);	
		}else if(args[0].equalsIgnoreCase("wand")){
			if(worldedit){
				Material mat = Material.getMaterial(we.getLocalConfiguration().wandItem);
				InventoryUtil.addItem(p.getInventory(), new ItemStack(mat));
			}else{
				InventoryUtil.addItem(p.getInventory(), customwand);
			}
		}
	
	}
	static HashMap<UUID, Location> left = new HashMap<UUID, Location>();
	static HashMap<UUID, Location> right = new HashMap<UUID, Location>();
	private static void create(Player p, String[] args){
		Location loc1 = null;
		Location loc2 = null;
		if(worldedit){
			if(we.getSelection(p) == null){
				p.sendMessage(r.default1 + "Please select a world-edit region first.");
			}else{
			    loc1 = we.getSelection(p).getMaximumPoint();
			    loc2 = we.getSelection(p).getMinimumPoint();
			}
		}else{
			if(!left.containsKey(p.getUniqueId()) || !right.containsKey(p.getUniqueId())){
				p.sendMessage(r.default1 + "Please select a region first using the /region wand");
			}
			Region region = new Region(left.get(p.getUniqueId()), right.get(p.getUniqueId()));
			loc1 = region.getUpperSW();
			loc2 = region.getLowerNE();
		}
		if(!r.checkArgs(args, 1)){
			p.sendMessage(r.default1 + "/region create " + r.default2 + "<Name>");
			return;
		}
		if(!loc1.getWorld().getName().equals(loc2.getWorld().getName())){
			p.sendMessage(r.default1 + "The selected points are not in the same world!");
			return;
		}
		String name = args[1];
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.DFregions);
		conf.set("regions." + name + "w", loc1.getWorld());
		conf.set("regions." + name + ".x1", loc1.getBlockX());
		conf.set("regions." + name + ".y1", loc1.getBlockY());
		conf.set("regions." + name + ".z1", loc1.getBlockZ());
		conf.set("regions." + name + ".x2", loc2.getBlockX());
		conf.set("regions." + name + ".y2", loc2.getBlockY());
		conf.set("regions." + name + ".z2", loc2.getBlockZ());
		p.sendMessage(r.default1 + "Region created! (" + r.default2 + name + r.default1 + ")");
	}
	public enum RegionFlag{
		PVP, PLACE, BREAK, MOBDAMAGE, MOBEXPLODE, SLEEP, TNT, FIRESPREAD, LAVAFIRE, INTERACT, CHESTACCES, WATERFLOW, LAVAFLOW, SNOWFALL, LEAFDECAY, DAMAGE, MOBSPAWN, ANIMALSPAWN, FOOD, BLOCKCMD, ALLOWCMD, SENDCHAT, RECEIVECHAT, POTIONUSE, GAMEMODEENTER, GAMEMODEEXIT
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void regionWand(PlayerInteractEvent e){
		if(!e.getPlayer().getItemInHand().equals(customwand) && (worldeditwand == null || !e.getPlayer().getItemInHand().equals(worldeditwand))) return;
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
			left.put(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());
		}else if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			right.put(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());
		}
	}
	
}

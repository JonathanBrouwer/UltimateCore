package Bammerbom.UltimateCore.Commands;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Openable;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateCore;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Databases.BlockDatabase.BlockAction;
import Bammerbom.UltimateCore.Resources.Databases.BlockDatabase.SQLset2;

public class CmdGTool implements Listener{
	static Plugin plugin;
	public CmdGTool(Plugin instance){
		plugin = instance;
		if(!r.getCnfg().contains("gtool") || r.getCnfg().getBoolean("gtool")){
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!(r.isPlayer(sender))){
			return;
		}
		if(!r.perm(sender, "uc.gtool", false, true)){
			return;
		}
		Player p = (Player)sender;
		ItemStack stack = new ItemStack(Material.BONE);
		stack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 10);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "GTool");
		stack.setItemMeta(meta);
		p.getInventory().addItem(stack);
	}
	Boolean locked = false;
	@EventHandler
	public void onInteractWithBone(final PlayerInteractEvent e){
		if(e.getPlayer().getItemInHand().getType().equals(Material.BONE)&&
				e.getAction().toString().contains("BLOCK")&&
				e.getPlayer().getItemInHand().getItemMeta() != null &&
				e.getPlayer().getItemInHand().getItemMeta().getDisplayName() != null &&
				e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "GTool")){
			Thread thr = new Thread(new Runnable(){
				public void run(){
					if(!r.perm(e.getPlayer(), "uc.gtool", false, true)) return;
					UltimateCore.getSQLdatabase().save();
					Location l = e.getAction().equals(Action.RIGHT_CLICK_BLOCK) ? e.getClickedBlock().getLocation().clone().add(e.getBlockFace().getModX(), e.getBlockFace().getModY(), e.getBlockFace().getModZ()) : e.getClickedBlock().getLocation();
					ArrayList<SQLset2> strlist = UltimateCore.getSQLdatabase().getActionsAt(l);
					//str.add(id + " " + playerid + " " + location + " " + action + " " + special + " " + special2);
					for(SQLset2 set : strlist){
						String pls = "";
						try{
						OfflinePlayer pl = Bukkit.getOfflinePlayer(UUID.fromString(set.playerid));
						pls = pl.getName();
						}catch(Exception ex){
							pls = set.playerid;
						}
						String action = set.action.toLowerCase().substring(0, 1).toUpperCase() + set.action.toLowerCase().substring(1);
						String str1 = r.default1 + set.date + r.default2 + " " + pls + " " + r.default1 + action;
						String str2 = r.default1 + "Loc: " + r.default2 + set.loc + r.default1 + " Data: " + r.default2 + set.special; 
						e.getPlayer().sendMessage(str1);
						e.getPlayer().sendMessage(str2);
						if(set.special2 != null && !set.special2.equalsIgnoreCase("")){
							String str3 = set.special2;
							e.getPlayer().sendMessage(str3);
						}
					}
					locked = false;
				}
			});
			e.setCancelled(true);
			locked = true;
			thr.run();
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent e){
		if(!e.isCancelled()){
		UltimateCore.getSQLdatabase().add(e.getPlayer().getUniqueId().toString(), Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.BLOCKBREAK, e.getBlock().getType().name() + ":" + e.getBlock().getData(), "");
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent e){
		if(!e.isCancelled()){
		UltimateCore.getSQLdatabase().add(e.getPlayer().getUniqueId().toString(), Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.BLOCKPLACE, e.getBlock().getType().name() + ":" + e.getBlock().getData(), "");
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockBurnEvent e){
		if(!e.isCancelled()){
		UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.BURN, e.getBlock().getType().name() + ":" + e.getBlock().getData(), "");
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockFade(BlockFadeEvent e){
		if(!e.isCancelled()){
		UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.MELT, e.getBlock().getType().name() + ":" + e.getBlock().getData(), "");
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockForm(BlockFormEvent e){
		if(!e.isCancelled()){
			if(e.getNewState().getType().equals(Material.ICE)){
				UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.ICEFORM, e.getBlock().getType().name() + ":" + e.getBlock().getData(), "");
			}else if(e.getNewState().getType().toString().toLowerCase().contains("snow")){
				UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.SNOWFORM, e.getBlock().getType().name() + ":" + e.getBlock().getData(), "");
			}else{
				UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.BLOCKFORM, e.getBlock().getType().name() + ":" + e.getBlock().getData(), "");
			}
				
		
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockFormTo(BlockFromToEvent e){
		if(!e.isCancelled()){
			if(e.getBlock().isLiquid()){
		UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.LIQUIDSPREAD, e.getToBlock().getType().name() + ":" + e.getToBlock().getData(), "");
			}else{
				UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.DRAGONEGGTELEPORT, e.getToBlock().getLocation().getWorld() + " " + e.getToBlock().getLocation().getBlockX() + " " + e.getToBlock().getLocation().getBlockY() + " " + e.getToBlock().getLocation().getBlockZ(), "");
				UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.DRAGONEGGTELEPORT, e.getBlock().getLocation().getWorld() + " " + e.getBlock().getLocation().getBlockX() + " " + e.getBlock().getLocation().getBlockY() + " " + e.getBlock().getLocation().getBlockZ(), "");
			}
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockGrow(BlockGrowEvent e){
		if(!e.isCancelled()){
		UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.GROW, e.getBlock().getType().name() + ":" + e.getBlock().getData()+ ", " + e.getNewState().getData().getData(), "");
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockIgnite(BlockIgniteEvent e){
		if(!e.isCancelled()){
			if(e.getPlayer() != null){
				UltimateCore.getSQLdatabase().add(e.getPlayer().getUniqueId().toString(), Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.IGNITE, e.getPlayer().getTargetBlock(null, 10).getType().name() + ":" + e.getPlayer().getTargetBlock(null, 10).getData(), "");
			}else{
				UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.IGNITE, e.getBlock().getType().name() + ":" + e.getBlock().getData(), "");
			}
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPistonMove(BlockPistonExtendEvent e){
		if(!e.isCancelled()){
			for(Block b : e.getBlocks()){
				UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), b.getLocation(), BlockAction.PISTONMOVE, b.getType().name() + ":" + b.getData(), "");
			}
	    }
		UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.PISTONMOVE, e.getBlock().getType().name() + ":" + e.getBlock().getData(), "");
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPistonMove(BlockPistonRetractEvent e){
		if(!e.isCancelled()){
		UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.PISTONMOVE, e.getBlock().getType().name() + ":" + e.getBlock().getData(), "");
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockFade2(BlockRedstoneEvent e){
		if(e.getBlock() instanceof Openable){
			Boolean isOpen = e.getNewCurrent() > 0;
			String open = isOpen ? "Open" : "Closed";
			
		UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.TOGGLE, e.getBlock().getType().name() + ":" + e.getBlock().getData() + ", Now " + open, "");
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSpread(BlockSpreadEvent e){
		if(!e.isCancelled()){
		UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.SPREAD, e.getNewState().getBlock().getType().name() + ":" + e.getNewState().getBlock().getData(), "");
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityBlockForm(EntityBlockFormEvent e){
		if(!e.isCancelled()){
		UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.BLOCKFORM, e.getNewState().getBlock().getType().name() + ":" + e.getNewState().getBlock().getData() + ", by " + e.getEntity().getType().name(), "");
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onLeavesDecay(LeavesDecayEvent e){
		if(!e.isCancelled()){
		UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.LEAVESECAY, e.getBlock().getType().name() + ":" + e.getBlock().getData(), "");
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSignChange(SignChangeEvent e){
		if(!e.isCancelled()){
		UltimateCore.getSQLdatabase().add(e.getPlayer().getUniqueId().toString(), Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.SIGNTEXT, e.getBlock().getType().name() + ":" + e.getBlock().getData(), r.default1 + "1: " + r.default2 + e.getLine(0) + r.default1 + " 2: " + r.default2 + e.getLine(1) + r.default1 + " 3: " + r.default2 + e.getLine(2) + r.default1 + " 4: " + r.default2 + e.getLine(3));
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBreakDoor(EntityBreakDoorEvent e){
		if(!e.isCancelled()){
		UltimateCore.getSQLdatabase().add(e.getEntity().getType().name(), Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.BLOCKBREAK, e.getBlock().getType().name() + ":" + e.getBlock().getData(), "");
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCreatePortal(EntityCreatePortalEvent e){
		if(!e.isCancelled()){
			String entityname = e.getEntity() instanceof Player ? ((Player)e.getEntity()).getUniqueId().toString() : e.getEntity().getType().name();
			for(BlockState b : e.getBlocks()){
				UltimateCore.getSQLdatabase().add(entityname, Calendar.getInstance().getTime(), b.getLocation(), BlockAction.PORTALCREATE, b.getBlock().getType().name() + ":" + b.getBlock().getData() + " Type: " + e.getPortalType().name(), "");
			}
		
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onExplode(EntityExplodeEvent e){
		if(!e.isCancelled() && e.getYield() > 0){
			String entityname = e.getEntity() instanceof Player ? ((Player)e.getEntity()).getUniqueId().toString() : e.getEntity().getType().name();
			for(Block b : e.blockList()){
				UltimateCore.getSQLdatabase().add(entityname, Calendar.getInstance().getTime(), b.getLocation(), BlockAction.EXPLODE, b.getType().name() + ":" + b.getData(), "");
			}
		
		}
		
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreakH(HangingBreakEvent e){
		if(!e.isCancelled()){
			if(!e.getCause().equals(RemoveCause.ENTITY)) return;
		UltimateCore.getSQLdatabase().add(e.getCause().toString(), Calendar.getInstance().getTime(), e.getEntity().getLocation(), BlockAction.BLOCKBREAK, e.getEntity().getType().name(), "");
		}
		
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreakHE(HangingBreakByEntityEvent e){
		if(!e.isCancelled()){
			if(!e.getCause().equals(RemoveCause.ENTITY)) return;
		UltimateCore.getSQLdatabase().add(e.getRemover() instanceof Player ? ((Player) e.getRemover()).getUniqueId().toString() : e.getRemover().getType().name(), Calendar.getInstance().getTime(), e.getEntity().getLocation(), BlockAction.BLOCKBREAK, e.getEntity().getType().name(), "");
		}
		
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlaceH(HangingPlaceEvent e){
		if(!e.isCancelled()){
		UltimateCore.getSQLdatabase().add(e.getPlayer().getUniqueId().toString(), Calendar.getInstance().getTime(), e.getBlock().getLocation(), BlockAction.BLOCKPLACE, e.getEntity().getType().name(), "");
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBucketFill(PlayerBucketFillEvent e){
		if(!e.isCancelled()){
		UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlockClicked().getLocation(), BlockAction.BUCKETFILL, e.getBlockClicked().getType().name() + ":" + e.getBlockClicked().getData(), "");
		}
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBucketEmpty(PlayerBucketEmptyEvent e){
		if(!e.isCancelled()){
		UltimateCore.getSQLdatabase().add("World", Calendar.getInstance().getTime(), e.getBlockClicked().getLocation(), BlockAction.BUCKETEMPTY, e.getBlockClicked().getType().name() + ":" + e.getBlockClicked().getData(), "");
		}
		
	}
}

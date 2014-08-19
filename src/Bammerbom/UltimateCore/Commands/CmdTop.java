package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Utils.LocationUtil;

public class CmdTop {
	static Plugin plugin;
	public CmdTop(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.isPlayer(sender)) return;
		Player p = (Player) sender;
		if(!r.perm(sender, "uc.top", false, true)) return;
		Location loc = getHighestY(p.getLocation());
		loc.add(0, 1.01, 0);
		if(loc == null || loc.getY() == 0){
			p.sendMessage(r.mes("Top.Failed"));
			return;
		}
		LocationUtil.teleport(p, loc, TeleportCause.COMMAND);
		p.sendMessage(r.mes("Top.Succes"));
	}
	public static Location getHighestY(Location loc){
		Integer highest = 0;
		Integer current = 0;
		while(current < loc.getWorld().getMaxHeight()){
			Integer cur = current;
			current++;
			Location loc2 = new Location(loc.getWorld(), loc.getX(), cur, loc.getZ());
			if(loc2 == null || loc2.getBlock() == null || loc2.getBlock().getType() == null || loc2.getBlock().getType().equals(Material.AIR)){
			}else{
				highest = cur;
			}
		}
		Location loc3 = new Location(loc.getWorld(), loc.getX(), highest, loc.getZ());
		loc3.setPitch(loc.getPitch());
		loc3.setYaw(loc.getYaw());
		return loc3;
	}
}

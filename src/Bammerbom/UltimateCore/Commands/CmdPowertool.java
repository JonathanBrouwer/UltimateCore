package Bammerbom.UltimateCore.Commands;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

public class CmdPowertool implements Listener{
	static Plugin plugin;
	public CmdPowertool(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args, Plugin plugin){
		if(!r.isPlayer(sender)){
			return;
		}
		if(!r.perm(sender, "uc.powertool", false, true)) return;
		Player p = (Player) sender;
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
		ItemStack item = p.getItemInHand();
		if(item == null || item.getType().equals(Material.AIR)){
			p.sendMessage(r.mes("Powertool.NoItemInHand"));
			return;
		}
		
		if(!r.checkArgs(args, 0)){
			data.set("powertool." + item.getType().name(), null);
			try {
				data.save(UltimateFileLoader.getPlayerFile(p));
			} catch (IOException e) {
				e.printStackTrace();
			}
			p.sendMessage(r.mes("Powertool.CommandReset").replaceAll("%Item", item.getType().name().toLowerCase().replaceAll("_", "")));
			return;
		}
		if(args[0].equalsIgnoreCase("reset")){
			data.set("powertool", null);
			try {
				data.save(UltimateFileLoader.getPlayerFile(p));
			} catch (IOException e) {
				e.printStackTrace();
			}
			p.sendMessage(r.mes("Powertool.CommandReset").replaceAll("%Item", "All"));
			return;
		}
		String cmd = r.getFinalArg(args, 0);
		if(cmd.startsWith("/")) cmd.replaceFirst("/", "");
		data.set("powertool." + item.getType().name(), cmd);
		try {
			data.save(UltimateFileLoader.getPlayerFile(p));
		} catch (IOException e) {
			e.printStackTrace();
		}
		p.sendMessage(r.mes("Powertool.CommandSet").replaceAll("%Command", cmd).replaceAll("%Item", item.getType().name().toLowerCase().replaceAll("_", "")));
		
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void interact(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(!r.perm(p, "uc.powertool", false, false)) return;
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
		ItemStack item = p.getItemInHand();
		if(data.get("powertool." + item.getType().name()) == null) return;
		String cmd = data.getString("powertool." + item.getType().name());
		Bukkit.getServer().dispatchCommand(p, cmd);
		r.log(ChatColor.YELLOW + e.getPlayer().getName() + " used powertool command: /" + cmd);
	}
}

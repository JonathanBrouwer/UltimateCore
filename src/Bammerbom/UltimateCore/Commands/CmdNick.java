package Bammerbom.UltimateCore.Commands;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

public class CmdNick implements Listener{
	static Plugin plugin;
	public CmdNick(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		enable();
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.isPlayer(sender)){
			return;
		}
		if(!r.perm(sender, "uc.nick", false, true)){
			return;
		}
		Player p = (Player) sender;
		if(!r.checkArgs(args, 0)){
			p.sendMessage(r.mes("Nick.Usage"));
			return;
		}
		if(r.checkArgs(args, 0) && args[0].equalsIgnoreCase("off")){
			p.sendMessage(r.mes("Nick.Message").replaceAll("%Name", "off"));
			p.setCustomName(null);
			p.setCustomNameVisible(false);
			YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
			data.set("nick", null);
			try {
				data.save(UltimateFileLoader.getPlayerFile(p));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String name = ChatColor.translateAlternateColorCodes('&', args[0]);
		p.setCustomName(name);
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
		data.set("nick", name);
		try {
			data.save(UltimateFileLoader.getPlayerFile(p));
		} catch (IOException e) {
			e.printStackTrace();
		}
		p.sendMessage(r.mes("Nick.Message").replaceAll("%Name", name));
	}
	public static void enable(){
		for(Player p : Bukkit.getOnlinePlayers()){
			YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
			String nick = data.getString("nick");
			if(nick == null) nick = p.getName();
			p.setCustomName(nick);
		}
	}
	@EventHandler(priority = EventPriority.LOW)
	public void join(PlayerJoinEvent e){
		Player p = e.getPlayer();
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
		String nick = data.getString("nick");
		if(nick == null) nick = p.getName();
		p.setCustomName(nick);
	}
}

package Bammerbom.UltimateCore;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import net.minecraft.util.org.apache.commons.io.FilenameUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.Resources.Utils.StringUtil;

public class UltimateFileLoader implements Listener{
	
	static Plugin plugin;
	public UltimateFileLoader(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	
	public static File messages;
	public static File datamap;
	public static File LANGf;
	
	public static File getLangFile(){
		return LANGf;
	}
	public static void createLang(){
		if(!new File(plugin.getDataFolder() + File.separator + "Messages" + File.separator + "NL.yml").exists()){
			plugin.saveResource("Messages" + File.separator + "NL.yml", true);
		}if(!new File(plugin.getDataFolder() + File.separator + "Messages" + File.separator + "EN.yml").exists()){
			plugin.saveResource("Messages" + File.separator + "EN.yml", true);
		}if(!new File(plugin.getDataFolder() + File.separator + "Messages" + File.separator + "DE.yml").exists()){
			plugin.saveResource("Messages" + File.separator + "DE.yml", true);
		}
	}
	public static void loadLang(){
		File file = new File(plugin.getDataFolder() + File.separator + "Messages", plugin.getConfig().getString("Language").toUpperCase() + ".yml");
		if(file.exists()){
			LANGf = file;
		}else{
			LANGf = new File(plugin.getDataFolder() + File.separator + "Messages", "EN.yml");
		}
	}
	//enable
	public static File DFspawns;
	public static File DFwarps;
	public static File DFworlds;
	public static File DFregions;
	public static File DFminigames;
	public static File DFreports;
	public static File DFglobal;
	public static File DFjails;
	public static File DFkits;

	public static void Enable(){
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		messages = new File(plugin.getDataFolder(), "Messages");
		datamap = new File(plugin.getDataFolder(), "Data");
		if(!datamap.exists()){
			datamap.mkdir();
		}
		
		DFglobal = new File(plugin.getDataFolder() + File.separator + "Data", "settings.yml");
		DFspawns = new File(plugin.getDataFolder() + File.separator + "Data", "spawns.yml");
		DFwarps = new File(plugin.getDataFolder() + File.separator + "Data", "warps.yml");
		DFworlds = new File(plugin.getDataFolder() + File.separator + "Data", "worlds.yml");
		DFregions = new File(plugin.getDataFolder() + File.separator + "Data", "regions.yml");
		DFminigames = new File(plugin.getDataFolder() + File.separator + "Data", "minigames.yml");
		DFreports = new File(plugin.getDataFolder() + File.separator + "Data", "reports.yml");
		DFjails = new File(plugin.getDataFolder() + File.separator + "Data", "jails.yml");
		DFkits = new File(plugin.getDataFolder() + File.separator + "Data", "kits.yml");
		if(messages.exists() && new File(plugin.getDataFolder(), "config.yml").exists()){
			//r.log(ChatColor.YELLOW + "Loaded files." + ChatColor.RESET);
		}else{
			//r.log(ChatColor.YELLOW + "Created files." + ChatColor.RESET);
		}
		//File creation
		plugin.saveDefaultConfig();
		//Languages
		if(!messages.exists()){
			messages.mkdir();
		}
		createLang();
		loadLang();
		try{
			if(!DFglobal.exists()){ 
				DFglobal.createNewFile();
				UltimateConfiguration conf = new UltimateConfiguration(DFglobal);
				conf.set("debug", false);
				conf.save(DFglobal);}
			if(!DFspawns.exists()) DFspawns.createNewFile();
			if(!DFwarps.exists()) DFwarps.createNewFile();
			if(!DFworlds.exists()) DFworlds.createNewFile();
			if(!DFregions.exists()) DFregions.createNewFile();
			if(!DFminigames.exists()) DFminigames.createNewFile();
			if(!DFreports.exists()) DFreports.createNewFile();
			if(!DFjails.exists()) DFjails.createNewFile();
			if(!DFkits.exists()) DFkits.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
		}
		for(OfflinePlayer pl : Bukkit.getOfflinePlayers()){
			getPlayerFile(pl);
		}
		configOptions();
		addConfig();
	} 
	private static void addConfig(){
		if(StringUtil.nullOrEmpty(r.mes("Words.Yes"))){
			resetFile(new File(plugin.getDataFolder() + "/Messages", "EN.yml"));
			createLang();
			loadLang();
			r.log(r.error + "Configuration update found, files reset: 1 (EN.yml)");
			r.log(r.error + "Configuration backups made: 1 (EN.yml)");
		}
	}
	public static void resetFile(File file){
	    Integer i = 1;
	    File parent = new File(file.getParent());
	    try {
	    	File ren = new File(parent.getCanonicalPath(), FilenameUtils.removeExtension(file.getName()) + "_OLD" + i + ".yml");
			while(ren.exists()){
				i++;
				 ren = new File(parent.getCanonicalPath(), FilenameUtils.removeExtension(file.getName()) + "_OLD" + i + ".yml");
			}
	    	file.renameTo(ren);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static File getPlayerFile(OfflinePlayer p){
		UUID id = p.getUniqueId();
		File file = new File(plugin.getDataFolder() + File.separator + "Players" + File.separator + id.toString() + ".yml");
		File directory = new File(plugin.getDataFolder() + File.separator + "Players");
		if(!file.exists()){
			try {
				directory.mkdir();
				file.createNewFile();
				UltimateConfiguration config = new UltimateConfiguration(file);
				config.set("name", p.getName());
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
		
	}
	public static UltimateConfiguration getPlayerConfig(OfflinePlayer p){
		File file = getPlayerFile(p);
		UltimateConfiguration config = new UltimateConfiguration(file);
		return config;
	}
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void join(PlayerJoinEvent e){
		if(!e.getPlayer().isOnline()) return;
		OfflinePlayer p = Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId());
		getPlayerFile(p);
		File file = getPlayerFile(e.getPlayer());
		UltimateConfiguration conf = new UltimateConfiguration(file);
		conf.set("lastconnect", System.currentTimeMillis());
		conf.set("ip", (e.getPlayer().getAddress().toString().toString().split("/")[1].split(":")[0]));
		conf.save(file);
	}
	@EventHandler()
	public void quit(PlayerQuitEvent e){
		File file = getPlayerFile(e.getPlayer());
		UltimateConfiguration conf = new UltimateConfiguration(file);
		conf.set("lastconnect", System.currentTimeMillis());
		conf.save(file);
	}
	@EventHandler()
	public void quit(PlayerKickEvent e){
		File file = getPlayerFile(e.getPlayer());
		UltimateConfiguration conf = new UltimateConfiguration(file);
		conf.set("lastconnect", System.currentTimeMillis());
		conf.save(file);
	}
	public static void configOptions(){
		//
		UltimateConfiguration conf = new UltimateConfiguration(new File(plugin.getDataFolder(), "config.yml"));
		
		//
		
		if(!conf.contains("Chat.Default1") || !conf.contains("Chat.Default2")) return;
		String privatecolor1 = conf.getString("Chat.Default1");
		String privatecolor2 = conf.getString("Chat.Default2");
		ChatColor pc1 = ChatColor.getByChar(privatecolor1.replaceFirst("&", ""));
		ChatColor pc2 = ChatColor.getByChar(privatecolor2.replaceFirst("&", ""));
		Bammerbom.UltimateCore.r.setColors(pc1, pc2);
	}

}


package Bammerbom.UltimateCore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class UltimateFileLoader implements Listener{
	
	static Plugin plugin;
	static{
		plugin = Bukkit.getPluginManager().getPlugin("UltimateCore");
	}
	public UltimateFileLoader(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	
	public static File messages;
	public static File datamap;
	public static File LANGf;
	public static File ENf;
	
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
		File file = new File(plugin.getDataFolder() + File.separator + "Messages", r.getCnfg().getString("Language").toUpperCase() + ".yml");
		if(file.exists()){
			LANGf = file;
		}else{
			LANGf = new File(plugin.getDataFolder() + File.separator + "Messages", "EN.yml");
		}
		ENf = new File(plugin.getDataFolder() + File.separator + "Messages", "EN.yml");
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
	    File tempFile = null;
		try {
			tempFile = File.createTempFile("temp_config", ".yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	    try (FileOutputStream out = new FileOutputStream(tempFile)) {
		    tempFile.deleteOnExit();
	        copy(Bukkit.getPluginManager().getPlugin("UltimateCore").getResource("config.yml"), out);
	    } catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			r.log("WARNING: Failed to complete config.yml");
			r.log("Restart your server to fix this problem.");
		}
	    UltimateConfiguration confL = new UltimateConfiguration(tempFile);
	    UltimateConfiguration confS = r.getCnfg();
	    for(String s : confL.getKeys()){
	    	if(!confS.contains(s) && !(confL.get(s) instanceof MemorySection)){
	    		confS.set(s, confL.get(s));
	    	}
	    }
	    confS.save();
	    tempFile.deleteOnExit();
		}
	    //lang
		{
	    File tempFile = null;
		try {
			tempFile = File.createTempFile("temp_EN", ".yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	    try (FileOutputStream out = new FileOutputStream(tempFile)) {
		    tempFile.deleteOnExit();
	        copy(plugin.getResource("Messages/EN.yml"), out);
	    } catch (IOException e) {
			e.printStackTrace();
		}
	    UltimateConfiguration confL = new UltimateConfiguration(tempFile);
	    UltimateConfiguration confS = new UltimateConfiguration(new File(plugin.getDataFolder() + File.separator + "Messages", "EN.yml"));
	    for(String s : confL.getKeys()){
	    	if(!confS.contains(s) && !(confL.get(s) instanceof MemorySection)){
	    		confS.set(s, confL.get(s));
	    	}
	    }
	    confS.save();
	    tempFile.deleteOnExit();
	}
	  public static int copy(InputStream input, OutputStream output)
			    throws IOException
			  {
			    long count = copyLarge(input, output);
			    if (count > 2147483647L) {
			      return -1;
			    }
			    return (int)count;
			  }

			  public static long copyLarge(InputStream input, OutputStream output)
			    throws IOException
			  {
			    return copyLarge(input, output, new byte[4096]);
			  }

			  public static long copyLarge(InputStream input, OutputStream output, byte[] buffer)
			    throws IOException
			  {
				  try{
			    long count = 0L;
			    int n = 0;
			    while (-1 != (n = input.read(buffer))) {
			      output.write(buffer, 0, n);
			      count += n;
			    }
			    return count;
					} catch (Exception e){
						r.log("WARNING: Failed to complete config.yml");
						r.log("Restart your server to fix this problem.");
						return -1L;
					}
			  }
	public static void resetFile(File file){
	    Integer i = 1;
	    File parent = new File(file.getParent());
	    try {
	    	File ren = new File(parent.getCanonicalPath(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "_OLD" + i + ".yml");
			while(ren.exists()){
				i++;
				 ren = new File(parent.getCanonicalPath(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "_OLD" + i + ".yml");
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
				directory.mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		UltimateConfiguration config = new UltimateConfiguration(file);
		if(!config.contains("name")){
			config.set("name", p.getName());
			config.save();
		}
		if(!config.contains("names")){
			ArrayList<String> names = new ArrayList<String>();
			Calendar timeCal = Calendar.getInstance();
		    timeCal.setTimeInMillis(System.currentTimeMillis());
		    String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(timeCal.getTime());
			names.add(p.getName() + " - " + date);
			config.set("names", names);
			config.save();
		}
		if(!config.getString("name").equalsIgnoreCase(p.getName())){
			String oldname = config.getString("name");
			config.set("name", p.getName());
			Calendar timeCal = Calendar.getInstance();
		    timeCal.setTimeInMillis(System.currentTimeMillis());
		    String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(timeCal.getTime());
		    List<String> names = config.getStringList("names");
		    if(names == null) names = new ArrayList<String>();
		    names.add(p.getName() + " - " + date);
		    config.set("names", names);
		    if(p.isOnline()){
		    	((Player) p).sendMessage(r.default1 + "Your name has been changed from " + r.default2 + oldname + r.default1 + " to " + r.default2 + p.getName() + r.default1 + ".");
		    }
		}
		config.save(file);
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


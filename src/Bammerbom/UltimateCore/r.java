package Bammerbom.UltimateCore;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class r {
	static Plugin plugin;
	public static Permission permission;
	public r(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		if(plugin.getServer().getPluginManager().getPlugin("Vault") != null){
			setupPermissions();
		}
		/*if(fl == null){
			logfile = new File(plugin.getDataFolder(), "log.txt");
			if(!plugin.getDataFolder().exists()){
				plugin.getDataFolder().mkdir();
			}
			if(!logfile.exists()){
				try {
					logfile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			fl = new FileLogger(logfile);
			fl.load();
			fl.addLine("#----SERVER RESTART/RELOAD----#");
			fl.save();
		}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
				@Override
				public void run(){
					fl.save();
				}
			}, 1200L, 1200L);*/
	}
    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
	//All settings
	public static ChatColor default1 = ChatColor.GOLD;
	public static ChatColor default2 = ChatColor.YELLOW;
	public static ChatColor error = ChatColor.RED;
	//Resource saving
	File language = null;
	static File config = null;
	static YamlConfiguration cnfg;
	public static void log(String message){
		String logo = "&9[&bUC&9]&r";
		if(message.contains("@3")){
			logo = "&4[&bUC&4]&r";
		}
		String msg = ChatColor.translateAlternateColorCodes('&', (logo + " " + ChatColor.YELLOW + message.replaceAll("@1", default1 + "")
				.replaceAll("@2", default2 + "").replaceAll("@3", "" + error)));
		Bukkit.getConsoleSender().sendMessage(msg);
		//
	}
	public static boolean checkArgs(Object[] args, Integer numb){
		try{
			args[numb].equals("Ritja");
			return true;
		}catch(Exception e){
			return false;
		}
	}
	public static enum MesType {
		Normal, Direct
	}
	public static String mes(String pad, Boolean conf){
		if(conf){
			return mes(pad);
		}else{
			 return default1 + ChatColor.translateAlternateColorCodes('&', pad.replaceAll("@1", default1 + "").replaceAll("@2", default2 + ""));
		}
	}
	public static String mes(String padMessage){
			YamlConfiguration lang = null;
			if(UltimateFileLoader.LANGf == null){
				return null;
			}
		    lang = YamlConfiguration.loadConfiguration(UltimateFileLoader.LANGf);
		    if(lang.get(padMessage) == null){
		    	return null;
		    }
		    
		    if(lang.get(padMessage) != null){
		    	String try1 = default1 + ChatColor.translateAlternateColorCodes('&', lang.getString(padMessage).replaceAll("@1", default1 + "").replaceAll("@2", default2 + ""));
		        return try1;
		    }
		    lang = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "Messages/EN.yml"));
		    if(lang.get(padMessage) != null){
		    	String try2 = default1 + ChatColor.translateAlternateColorCodes('&', lang.getString(padMessage).replaceAll("@1", default1 + "").replaceAll("@2", default2 + ""));
		        return try2;
		    }
			return null;
		
	}
	public static YamlConfiguration getCnfg(){
		return YamlConfiguration.loadConfiguration(new File(r.plugin.getDataFolder(), "config.yml"));
	}
	public static void saveCnfg(){
	    try {
			cnfg.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isNumber(String check){
		try{
			Integer.parseInt(check);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	public static boolean isDouble(String str){
		try{
			Double.parseDouble(str);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	public static boolean isFloat(String str){
		try{
			Float.parseFloat(str);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	public static boolean isLong(String str){
		try{
			Long.parseLong(str);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	public static boolean perm(CommandSender p, String perm, Boolean def, Boolean message){
		if(!(p instanceof Player)){
			return true;
		}
		Player pl = (Player) p;
		Boolean hasperm = perm(pl, perm, def);
		if(hasperm == false && message == true){
			p.sendMessage(mes("NoPermissions"));
		}
		return hasperm;
	}
	@Deprecated
	public static boolean perm(Player p, String perm, Boolean def){
		if(permission != null){
			if(p.isOp()) return true;
			return permission.has(p.getWorld(), p.getName(), perm) || p.isOp();
		}else{
		if(def == true){
			return true;
		}
		if(p.hasPermission(perm)){
			return true;
			
		}
		if(p.isOp()){
			return true;
		}
		return false;
		}
	}
	public static boolean isPlayer(CommandSender sender){
		if(sender instanceof Player){
			return true;
		}
		sender.sendMessage(mes("NeedToBePlayer"));
		return false;
	}
	public static String getFinalArg(String[] args, int start)
    {
      StringBuilder bldr = new StringBuilder();
      for (int i = start; i < args.length; i++)
      {
        if (i != start)
        {
          bldr.append(" ");
        } 
        bldr.append(args[i]);
      } 
      return bldr.toString();
    } 
	public static void setColors(ChatColor c1, ChatColor c2){
		default1 = c1;
		default2 = c2;
	}

	@SuppressWarnings("rawtypes")
	public static void unRegisterBukkitCommand(String str) {
	try {
	    CommandMap commandMap = null;
	    Map commands = null;
        Field commandMapField = Bukkit.getServer().getPluginManager().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);
        commandMap = (CommandMap)commandMapField.get(Bukkit.getPluginManager());

        Field knownCommandsField = CommandMap.class.getDeclaredField("knownCommands");
        knownCommandsField.setAccessible(true);
        commands = (Map)knownCommandsField.get(commandMap);
        Iterator it;
        if (commandMap != null) {
		      for (it = commands.entrySet().iterator(); it.hasNext(); ) {
		        Map.Entry entry = (Map.Entry)it.next();
		        if ((entry.getValue() instanceof PluginCommand)) {
		          PluginCommand c = (PluginCommand)entry.getValue();
		          if (c.getPlugin() == plugin && (c.getLabel().equalsIgnoreCase(str) || c.getAliases().contains(str))) {
		            c.unregister(commandMap);
		            it.remove();
		          }
		        }
		      }
		    }
        
	} catch (Exception e) {
	    e.printStackTrace();
	}
	}
	public static boolean isPlayer(CommandSender sender, Boolean b) {
		if(sender instanceof Player){
			return true;
		}
		if(b)
		sender.sendMessage(mes("NeedToBePlayer"));
		return false;
	}
}
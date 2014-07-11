package Bammerbom.UltimateCore;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class r {
	static Plugin plugin;
	static Permission permission;
	public r(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		if(plugin.getServer().getPluginManager().getPlugin("Vault") != null && Bukkit.getPluginManager().isPluginEnabled("Vault")){
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
	public static ChatColor default1 = ChatColor.DARK_AQUA;
	public static ChatColor default2 = ChatColor.AQUA;
	
	public static ChatColor error = ChatColor.RED;
	//Resource saving
	File language = null;
	static File config = null;
	static UltimateConfiguration cnfg;
	public static void debug(Object message){
		if(!new UltimateConfiguration(UltimateFileLoader.DFglobal).getBoolean("debug")) return;
		String logo = "&9[&bUC&9]&r";
		if(message.toString().contains("@3")){
			logo = "&4[&bUC&4]&r";
		}
		String msg = ChatColor.translateAlternateColorCodes('&', (logo + " " + ChatColor.WHITE + message.toString().replaceAll("@1", default1 + "")
				.replaceAll("@2", default2 + "").replaceAll("@3", "" + error).replaceAll("\\\\n", "\n")));
		Bukkit.getConsoleSender().sendMessage(msg);
		//
	}
	public static void log(Object message){
		if(message == null){
			r.log("null");
			return;
		}
		String logo = "&9[&bUC&9]&r";
		String msg = ChatColor.translateAlternateColorCodes('&', (logo + " " + ChatColor.YELLOW + message.toString().replaceAll("@1", default1 + "")
				.replaceAll("@2", default2 + "").replaceAll("@3", "" + error).replaceAll("\\\\n", "\n")));
		Bukkit.getConsoleSender().sendMessage(msg);
		//
	}
	public static void logF(Object message){
		if(message == null){
			r.log("null");
			return;
		}
		String logo = "&9[&bUC&9]&r ";
		if(message.toString().contains("@3")){
			logo = "&4[&bUC&4]&r ";
		}
		Bukkit.getConsoleSender().sendMessage(logo + message.toString());
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
	public static Integer normalize(Integer a, Integer b, Integer c){
		if(a < b){
			a = b;
		}
		if(a > c){
			a = c;
		}
		return a;
	}
	public static Double normalize(Double a, Double b, Double c){
		if(a < b){
			a = b;
		}
		if(a > c){
			a = c;
		}
		return a;
	}
	public static String mes(String pad, Boolean conf){
		if(conf){
			return mes(pad);
		}else{
			 return default1 + ChatColor.translateAlternateColorCodes('&', pad.replaceAll("@1", default1 + "").replaceAll("@2", default2 + ""));
		}
	}
	public static String word(String padMessage){
		if(mes(padMessage) == null) return null;
		return mes(padMessage).substring(2);
	}
	public static String mes(String padMessage){
		if(padMessage.equalsIgnoreCase("Words.Yes")) padMessage = "Words.true";
		if(padMessage.equalsIgnoreCase("Words.No")) padMessage = "Words.false";
			UltimateConfiguration lang = null;
			if(UltimateFileLoader.LANGf == null){
				return null;
			}
		    lang = new UltimateConfiguration(UltimateFileLoader.LANGf);
		    if(lang.contains(padMessage)){
		    	String try1 = default1 + ChatColor.translateAlternateColorCodes('&', lang.getString(padMessage).replaceAll("@1", default1 + "").replaceAll("@2", default2 + "").replaceAll("\\\\n", "\n"));
		        return try1;
		    }
		    lang = new UltimateConfiguration(new File(plugin.getDataFolder() + "/Messages", "EN.yml"));
		    if(lang.contains(padMessage)){
		    	String try2 = default1 + ChatColor.translateAlternateColorCodes('&', lang.getString(padMessage).replaceAll("@1", default1 + "").replaceAll("@2", default2 + "").replaceAll("\\\\n", "\n"));
		        return try2;
		    }
		    r.log(r.error + "Failed to find " + padMessage + " in Messages file.");
			return null;
		
	}
	public static UltimateConfiguration getCnfg(){
		return new UltimateConfiguration(new File(plugin.getDataFolder(), "config.yml"));
	}
	public static void saveCnfg(){
	    cnfg.save(config);
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
	public static boolean isPlayer(CommandSender sender, Boolean b) {
		if(sender instanceof Player){
			return true;
		}
		if(b)
		sender.sendMessage(mes("NeedToBePlayer"));
		return false;
	}
	static Random ra = new Random();
	public static ChatColor getRandomChatColor(){
		ArrayList<ChatColor> values = new ArrayList<ChatColor>();
		for(ChatColor c : ChatColor.values()){
			if(!c.isFormat()){
				values.add(c);
			}
		}
		return values.get(ra.nextInt(values.size()));
	}
}
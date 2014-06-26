package Bammerbom.UltimateCore.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import Bammerbom.UltimateCore.UltimateCommands;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.Resources.Utils.StringUtil;

public class EventChat implements Listener{
	static Plugin plugin;
	Random ra = new Random();
	@Heavy
	public EventChat(Plugin instance){
		plugin = instance;
		if(!r.getCnfg().contains("Chat.Groups.Enabled")){
			r.log(r.error + "Config reset required: Chat Settings have been updated.");
			r.log(r.error + "- Custom Chat disabled.");
			return;
		}
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		if(plugin.getServer().getPluginManager().getPlugin("Vault") != null && Bukkit.getPluginManager().isPluginEnabled("Vault")){
		setupChat();
		setupPermissions();
		}
		if(plugin.getConfig().getBoolean("Chat.SpamFilter") || plugin.getConfig().getBoolean("Chat.RepeatFilter"))
		spamTask();
	}
	net.milkbowl.vault.chat.Chat chat = null;
	public void setupChat(){
		RegisteredServiceProvider<net.milkbowl.vault.chat.Chat> chatProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }
	}
	net.milkbowl.vault.permission.Permission permission = null;
    private boolean setupPermissions()
    {
        RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
	@Heavy
	@EventHandler(priority = EventPriority.LOWEST)
	public void ChatListener(AsyncPlayerChatEvent e){
		if(!e.isCancelled()){
			/*if(e.getMessage().contains("%")){
				e.setCancelled(true);
				e.getPlayer().sendMessage(r.error + "Illegal characters in chat.");
				return;
			}*/
			String m = e.getMessage();
			if(r.perm(e.getPlayer(), "uc.coloredchat", false, false)){
				m = ChatColor.translateAlternateColorCodes('&', m);
			}
			/*if(UC.getPlayer(e.getPlayer()).isMuted()){
				e.setCancelled(true);
				return;
			}*/
			ChatSet set = SwearDetector(m, e.getPlayer());
			if(set.isCancelled()){
				e.setCancelled(true);
				return;
			}
			m = set.getMessage();
			e.setMessage(m);
			//
			if(r.getCnfg().getBoolean("Chat.EnableCustomChat") == false) return;
			if((Bukkit.getPluginManager().getPlugin("EssentialsChat") != null && Bukkit.getPluginManager().getPlugin("EssentialsChat").isEnabled()) || (Bukkit.getPluginManager().getPlugin("Essentials") != null && Bukkit.getPluginManager().isPluginEnabled("Essentials"))){
				if(!ChatColor.stripColor(e.getFormat()).equalsIgnoreCase("<%1$s> %2$s")){
					return;
				}
			}
			if(r.getCnfg().getBoolean("Chat.Groups.Enabled")){
				if(permission != null){
				String group = permission.getPrimaryGroup(e.getPlayer());
				if(!(group == null) && !group.equalsIgnoreCase("") && r.getCnfg().get("Chat.Groups." + group) != null){
					String f = r.getCnfg().getString("Chat.Groups." + group);
					String prefix = "";
					String suffix = "";
					if(permission != null && chat != null){
						prefix = chat.getGroupPrefix(e.getPlayer().getWorld(), permission.getPrimaryGroup(e.getPlayer()));
						suffix = chat.getGroupSuffix(e.getPlayer().getWorld(), permission.getPrimaryGroup(e.getPlayer()));
						if((chat.getPlayerPrefix(e.getPlayer()) != null) && !chat.getPlayerPrefix(e.getPlayer()).equalsIgnoreCase("")){
							prefix = chat.getPlayerPrefix(e.getPlayer());
						}
						if((chat.getPlayerSuffix(e.getPlayer()) != null) && !chat.getPlayerSuffix(e.getPlayer()).equalsIgnoreCase("")){
							suffix = chat.getPlayerSuffix(e.getPlayer());
						}
					}
					if(!f.contains("\\+Name")){
						e.getPlayer().setDisplayName(UC.getPlayer(e.getPlayer()).getNick());
					}else{
						e.getPlayer().setDisplayName(e.getPlayer().getName());
					}
					f = r(f, "\\+Group", r.perm(e.getPlayer(), "uc.rainbow", false, false) ? group.replaceAll("&y", r.getRandomChatColor() + "") : group);
					f = r(f, "\\+Prefix", r.perm(e.getPlayer(), "uc.rainbow", false, false) ? prefix.replaceAll("&y", r.getRandomChatColor() + "") : prefix);
					f = r(f, "\\+Suffix", r.perm(e.getPlayer(), "uc.rainbow", false, false) ? suffix.replaceAll("&y", r.getRandomChatColor() + "") : suffix);
					f = r(f, "\\+Name", "\\%1\\$s");
					f = r(f, "\\+Displayname", "\\%1\\$s");
					f = r(f, "\\+World", e.getPlayer().getWorld().getName());
					f = r(f, "\\+WorldAlias", e.getPlayer().getWorld().getName().charAt(0) + "");
					f = ChatColor.translateAlternateColorCodes('&', f);
					if(r.perm(e.getPlayer(), "uc.rainbow", false, false)) f = r(f, "&y", r.getRandomChatColor() + "");
					f = r(f, "\\+Message", "\\%2\\$s");
					synchronized (f){
						e.setMessage(m);
						e.setFormat(f);
						}
					return;
				}
				}
			}
			String f = r.getCnfg().getString("Chat.Format");
			String group = "";
			String prefix = "";
			String suffix = "";
			if(permission != null && chat != null){
				group = permission.getPrimaryGroup(e.getPlayer());
				prefix = chat.getGroupPrefix(e.getPlayer().getWorld(), permission.getPrimaryGroup(e.getPlayer()));
				suffix = chat.getGroupSuffix(e.getPlayer().getWorld(), permission.getPrimaryGroup(e.getPlayer()));
			}
			if((chat.getPlayerPrefix(e.getPlayer()) != null) && !chat.getPlayerPrefix(e.getPlayer()).equalsIgnoreCase("")){
				prefix = chat.getPlayerPrefix(e.getPlayer());
			}
			if((chat.getPlayerSuffix(e.getPlayer()) != null) && !chat.getPlayerSuffix(e.getPlayer()).equalsIgnoreCase("")){
				prefix = chat.getPlayerSuffix(e.getPlayer());
			}
			if(!f.contains("\\+Name")){
				e.getPlayer().setDisplayName(UC.getPlayer(e.getPlayer()).getNick());
			}else{
				e.getPlayer().setDisplayName(e.getPlayer().getName());
			}
			f = r(f, "\\+Group", r.perm(e.getPlayer(), "uc.rainbow", false, false) ? (group != null ? group.replaceAll("&y", r.getRandomChatColor() + "") : "") : (group != null ? group : ""));
			f = r(f, "\\+Prefix", r.perm(e.getPlayer(), "uc.rainbow", false, false) ? (prefix != null ? prefix.replaceAll("&y", r.getRandomChatColor() + "") : "") : (prefix != null ? prefix : ""));
			f = r(f, "\\+Suffix", r.perm(e.getPlayer(), "uc.rainbow", false, false) ? (suffix != null ? suffix.replaceAll("&y", r.getRandomChatColor() + "") : "") : (suffix != null ? suffix : ""));
			f = r(f, "\\+Name", "\\%1\\$s");
			f = r(f, "\\+Displayname", "\\%1\\$s");
			f = r(f, "\\+World", e.getPlayer().getWorld().getName());
			f = r(f, "\\+WorldAlias", e.getPlayer().getWorld().getName().charAt(0) + "");
			f = ChatColor.translateAlternateColorCodes('&', f);
			ChatColor value = Arrays.asList(ChatColor.values()).get(ra.nextInt(Arrays.asList(ChatColor.values()).size()));
			if(r.perm(e.getPlayer(), "uc.rainbow", false, false)) f = r(f, "&y", value + "");
			f = r(f, "\\+Message", "\\%2\\$s");
			synchronized (f){
				e.setMessage(m);
			    e.setFormat(f);
			}
		}
	}
	public String r(String str, String str2, String str3){
		if(str == null || str2 == null) return str;
		if(str3 == null) return str.replaceAll(str2, "");
		return str.replaceAll(str2, str3);
	}
	static HashMap<String, String> lastChatMessage = new HashMap<String, String>();
	static HashMap<String, Integer> lastChatMessageTimes = new HashMap<String, Integer>();
	static HashMap<String, Integer> spamTime = new HashMap<String, Integer>();
	static HashMap<String, Integer> swearAmount = new HashMap<String, Integer>();
	public static ChatSet SwearDetector(String mr, Player p){
		ChatSet set = new ChatSet(mr);
		if(r.perm(p, "uc.chat", false, false)){
			return set;
		}
		//Anti REPEAT
		if(!r.perm(p, "uc.chat.repeat", false, false)){
		if(plugin.getConfig().getBoolean("Chat.RepeatFilter")){
		String lastmessage = "";
		Integer lastmessageTimes = 0;
		if(lastChatMessage.get(p.getName()) != null){
			lastmessage = lastChatMessage.get(p.getName());
			lastmessageTimes = lastChatMessageTimes.get(p.getName());
		}
		lastChatMessage.put(p.getName(), mr);
		lastChatMessageTimes.put(p.getName(), lastmessageTimes + 1);
		if(lastmessage.equalsIgnoreCase(mr)){
			if(lastmessageTimes + 1 == 3){
			    p.sendMessage(r.default1 + "REPEAT detected! Stop repeating yourself or you will be muted");
			    set.setCancelled(true);
			}
			if(lastmessageTimes + 1 == 4){
				p.sendMessage(r.default1 + "REPEAT detected! Stop repeating yourself or you will be muted");
				set.setCancelled(true);
			}
			if(lastmessageTimes + 1 == 5){
				UltimateCommands.executecommand(Bukkit.getConsoleSender(), "mute " + p.getName() + " 5m");
				set.setCancelled(true);
			}
		}else{
			lastChatMessageTimes.put(p.getName(), 1);
		}
		}
		}
		//Anti SPAM
		if(!r.perm(p, "uc.chat.spam", false, false)){
		if(plugin.getConfig().getBoolean("Chat.SpamFilter")){
		if(spamTime.containsKey(p.getName())){
	    Integer amount = spamTime.get(p.getName());
	    spamTime.put(p.getName(), amount + 1);
	    if(amount >= 4){
			UltimateCommands.executecommand(Bukkit.getConsoleSender(), "mute " + p.getName() + " 5m");
			set.setCancelled(true);
	    }else if(amount >= 3){
			p.sendMessage(r.default1 + "SPAM detected! Stop spamming or you will be muted");
	    }
		}else{
			spamTime.put(p.getName(), 1);
		}
		}
		}
		//Anti SWEAR 
		if(!r.perm(p, "uc.chat.swear", false, false)){
		if(r.getCnfg().getBoolean("Chat.SwearFilter") || r.getCnfg().getBoolean("Chat.SwearFiler")){
		Boolean stop = false;
		for(String sw : r.getCnfg().getStringList("SwearWords")){
			if(mr.toLowerCase().contains(sw.toLowerCase())){
				//set.setCancelled(true);
				if(!stop){
					stop = true;
				Integer s = swearAmount.get(p.getName());
				if(s == null){
					s = 0;
				}
				s++;
				swearAmount.put(p.getName(), s);
				p.sendMessage(r.default1 + "SWEAR detected! Stop swearing or you will be muted");
				if(s >= 3){
					UltimateCommands.executecommand(Bukkit.getConsoleSender(), "mute " + p.getName() + " 5m");
					set.setCancelled(true);
				}
				}
				set.setMessage(set.getMessage().replaceAll("(?i)" + sw, "****"));
			}
		}
		}
		}
		//Anti CAPS
		if(!r.perm(p, "uc.chat.caps", false, false)){
		if(r.getCnfg().get("Chat.CapsFilter") == null || r.getCnfg().getBoolean("Chat.CapsFilter")){
	        double msglength = set.getMessage().toCharArray().length;
			double capsCountt = 0.0D;
	        if(msglength > 3.0){
	        for (char c : set.getMessage().toCharArray()) {
	          if (Character.isUpperCase(c)) {
	            capsCountt += 1.0D;
	          }
	          if (!Character.isLetterOrDigit(c)) {
	            msglength -= 1.0D;
	          }
	        }
	        }
	        if((capsCountt / msglength * 100) > 60.0){
	        	set.setMessage(StringUtil.firstUpperCase(set.getMessage().toLowerCase()));
	        }
		}
		}
		return set;
	}
	private void spamTask(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			public void run(){
				ArrayList<String> spamtime_remove = new ArrayList<String>();
				if(!spamTime.isEmpty()){
			    for(String key : spamTime.keySet()){
			    	Integer value = spamTime.get(key);
			    	value--;
			    	if(value == 0){
			    		spamtime_remove.add(key);
			    	}else{
			    	spamTime.put(key, value);
			    	}
			    }
				}
				for(String str : spamtime_remove){
					spamTime.remove(str);
				}
			}
		}, 70L, 70L);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			public void run(){
				ArrayList<String> spamtime_remove = new ArrayList<String>();
				if(!swearAmount.isEmpty()){
			    for(String key : swearAmount.keySet()){
			    	Integer value = swearAmount.get(key);
			    	value--;
			    	if(value == 0){
			    		spamtime_remove.add(key);
			    	}else{
			    		swearAmount.put(key, value);
			    	}
			    }
				for(String str : spamtime_remove){
					swearAmount.remove(str);
				}
				}
			}
		}, 160L, 160L);
	}
	
}
class ChatSet{
	Boolean cancelled;
	String message;
	public boolean isCancelled(){
		return cancelled;
	}
	public String getMessage(){
		return message;
	}
	public void setCancelled(Boolean can){
		cancelled = can;
	}
	public void setMessage(String msg){
		message = msg;
	}
	public ChatSet(String mes){
		cancelled = false;
		message = mes;
	}
}
/**
 * This class is heavy. Touch it can make this class unstable.
 */
@interface Heavy {
}


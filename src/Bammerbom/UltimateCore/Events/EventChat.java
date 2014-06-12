package Bammerbom.UltimateCore.Events;

import java.util.ArrayList;
import java.util.HashMap;

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
import Bammerbom.UltimateCore.Commands.CmdMute;

public class EventChat implements Listener{
	static Plugin plugin;
	@Heavy
	public EventChat(Plugin instance){
		plugin = instance;
		if(!r.getCnfg().contains("Chat.Format")){
			r.log(r.error + "Config reset required: Chat Settings have been updated.");
			r.log(r.error + "- Custom Chat disabled.");
			return;
		}
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		if(plugin.getServer().getPluginManager().getPlugin("Vault") != null){
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
	@EventHandler(priority = EventPriority.LOW)
	public void ChatListener(AsyncPlayerChatEvent e){
		if(!e.isCancelled()){
			if(e.getMessage().contains("%")){
				e.setCancelled(true);
				e.getPlayer().sendMessage(r.error + "Illegal characters in chat.");
				return;
			}
	
			String m = e.getMessage();
			if(r.perm(e.getPlayer(), "uc.coloredchat", false, false)){
				m = ChatColor.translateAlternateColorCodes('&', m);
			}
			if(CmdMute.Mute(e.getPlayer())){
				e.setCancelled(true);
				return;
			}
			//TODO
			ChatSet set = SwearDetector(m, e.getPlayer());
			if(set.isCancelled()){
				e.setCancelled(true);
				return;
			}
			m = set.getMessage();
			String f = r.getCnfg().getString("Chat.Format");
			String group = "";
			String prefix = "";
			String suffix = "";
			if(permission != null && chat != null){
				group = permission.getPrimaryGroup(e.getPlayer());
				prefix = chat.getGroupPrefix(e.getPlayer().getWorld(), permission.getPrimaryGroup(e.getPlayer()));
				suffix = chat.getGroupSuffix(e.getPlayer().getWorld(), permission.getPrimaryGroup(e.getPlayer()));
				
			}
			f = r(f, "\\+Group", group);
			f = r(f, "\\+Prefix", prefix);
			f = r(f, "\\+Suffix", suffix);
			f = r(f, "\\+Name", e.getPlayer().getName());
			f = r(f, "\\+Displayname", e.getPlayer().getDisplayName());
			f = r(f, "\\+World", e.getPlayer().getWorld().getName());
			f = r(f, "\\+WorldAlias", e.getPlayer().getWorld().getName().charAt(0) + "");
			f = r(f, "\\+Message", m);
			f = r(f, "", "");
			f = r(f, "&", "§");
			e.setFormat(f);
		}
	}
	public String r(String str, String str2, String str3){
		return str.replaceAll(str2, str3);
	}
	//TODO
	static HashMap<String, String> lastChatMessage = new HashMap<String, String>();
	static HashMap<String, Integer> lastChatMessageTimes = new HashMap<String, Integer>();
	static HashMap<String, Integer> spamTime = new HashMap<String, Integer>();
	static HashMap<String, Integer> swearAmount = new HashMap<String, Integer>();
	public static ChatSet SwearDetector(String mr, Player p){
		ChatSet set = new ChatSet(mr);
		if(CmdMute.Mute(p)) return set;
		if(r.perm(p, "uc.chat.nofilter", false, false)){
			return set;
		}
		//Anti REPEAT
		if(r.perm(p, "uc.chat.nofilter.repeat", false, false)){
		if(plugin.getConfig().getBoolean("Chat.RepeatFilter")){
		String lastmessage = "";
		Integer lastmessageTimes = 0;
		if(lastChatMessage.get(p.getName()) != null){
			lastmessage = lastChatMessage.get(p.getName());
			lastmessageTimes = lastChatMessageTimes.get(p.getName());
		}
		lastChatMessage.put(p.getName(), mr);
		lastChatMessageTimes.put(p.getName(), lastmessageTimes + 1);
		if(lastmessage.startsWith(mr)
				|| mr.toLowerCase().startsWith(lastmessage.toLowerCase())
				|| lastmessage.toLowerCase().endsWith(mr.toLowerCase()) 
				|| mr.toLowerCase().endsWith(lastmessage.toLowerCase())
                || lastmessage.toLowerCase().equalsIgnoreCase(mr.toLowerCase())){
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
		if(r.perm(p, "uc.chat.nofilter.spam", false, false)){
		if(plugin.getConfig().getBoolean("Chat.SpamFilter")){
		if(spamTime.containsKey(p.getName())){
	    Integer amount = spamTime.get(p.getName());
	    spamTime.put(p.getName(), amount + 1);
	    if(amount >= 3){
			UltimateCommands.executecommand(Bukkit.getConsoleSender(), "mute " + p.getName() + " 5m");
			set.setCancelled(true);
	    }else if(amount >= 2){
			p.sendMessage(r.default1 + "SPAM detected! Stop spamming or you will be muted");
	    }
		}else{
			spamTime.put(p.getName(), 1);
		}
		}
		}
		//Anti SWEAR 
		if(r.perm(p, "uc.chat.nofilter.swear", false, false)){
		if(plugin.getConfig().getBoolean("Chat.SwearFilter")){
		Boolean stop = false;
		for(String sw : plugin.getConfig().getStringList("SwearWords")){
			if(mr.toLowerCase().contains(sw)){
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
				if(s >= 4){
					UltimateCommands.executecommand(Bukkit.getConsoleSender(), "mute " + p.getName() + " 5m");
					set.setCancelled(true);
				}
				}
				set.setMessage(set.getMessage().replaceAll(sw, "****"));
			}
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
		}, 60L, 60L);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			public void run(){
				if(!swearAmount.isEmpty()){
			    for(String key : swearAmount.keySet()){
			    	Integer value = swearAmount.get(key);
			    	value--;
			    	if(value == 0){
			    		swearAmount.remove(key);
			    	}else{
			    		swearAmount.put(key, value);
			    	}
			    }
				}
			}
		}, 100L, 100L);
	}
	//
	
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


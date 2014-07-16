package Bammerbom.UltimateCore.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;

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
		if(r.getCnfg().getBoolean("Chat.SpamFilter") || r.getCnfg().getBoolean("Chat.RepeatFilter"))
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
					f = r(f, "\\+Group", r.perm(e.getPlayer(), "uc.chat.rainbow", false, false) ? group.replaceAll("&y", r.getRandomChatColor() + "") : group);
					f = r(f, "\\+Prefix", r.perm(e.getPlayer(), "uc.chat.rainbow", false, false) ? prefix.replaceAll("&y", r.getRandomChatColor() + "") : prefix);
					f = r(f, "\\+Suffix", r.perm(e.getPlayer(), "uc.chat.rainbow", false, false) ? suffix.replaceAll("&y", r.getRandomChatColor() + "") : suffix);
					f = r(f, "\\+Name", "\\%1\\$s");
					f = r(f, "\\+Displayname", "\\%1\\$s");
					f = r(f, "\\+World", e.getPlayer().getWorld().getName());
					f = r(f, "\\+WorldAlias", e.getPlayer().getWorld().getName().charAt(0) + "");
					f = ChatColor.translateAlternateColorCodes('&', f);
					if(r.perm(e.getPlayer(), "uc.chat.rainbow", false, false)) f = r(f, "&y", r.getRandomChatColor() + "");
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
			if(chat != null && (chat.getPlayerPrefix(e.getPlayer()) != null) && !chat.getPlayerPrefix(e.getPlayer()).equalsIgnoreCase("")){
				prefix = chat.getPlayerPrefix(e.getPlayer());
			}
			if(chat != null && (chat.getPlayerSuffix(e.getPlayer()) != null) && !chat.getPlayerSuffix(e.getPlayer()).equalsIgnoreCase("")){
				prefix = chat.getPlayerSuffix(e.getPlayer());
			}
			if(!f.contains("\\+Name")){
				e.getPlayer().setDisplayName(UC.getPlayer(e.getPlayer()).getNick());
			}else{
				e.getPlayer().setDisplayName(e.getPlayer().getName());
			}
			f = r(f, "\\+Group", r.perm(e.getPlayer(), "uc.chat.rainbow", false, false) ? (group != null ? group.replaceAll("&y", r.getRandomChatColor() + "") : "") : (group != null ? group : ""));
			f = r(f, "\\+Prefix", r.perm(e.getPlayer(), "uc.chat.rainbow", false, false) ? (prefix != null ? prefix.replaceAll("&y", r.getRandomChatColor() + "") : "") : (prefix != null ? prefix : ""));
			f = r(f, "\\+Suffix", r.perm(e.getPlayer(), "uc.chat.rainbow", false, false) ? (suffix != null ? suffix.replaceAll("&y", r.getRandomChatColor() + "") : "") : (suffix != null ? suffix : ""));
			f = r(f, "\\+Name", "\\%1\\$s");
			f = r(f, "\\+Displayname", "\\%1\\$s");
			f = r(f, "\\+World", e.getPlayer().getWorld().getName());
			f = r(f, "\\+WorldAlias", e.getPlayer().getWorld().getName().charAt(0) + "");
			f = ChatColor.translateAlternateColorCodes('&', f);
			ChatColor value = Arrays.asList(ChatColor.values()).get(ra.nextInt(Arrays.asList(ChatColor.values()).size()));
			if(r.perm(e.getPlayer(), "uc.chat.rainbow", false, false)) f = r(f, "&y", value + "");
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
		if(r.getCnfg().getBoolean("Chat.RepeatFilter")){
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
		if(r.getCnfg().getBoolean("Chat.SpamFilter")){
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
		//Anti IP
		if(!r.perm(p, "uc.chat.ip", false, false)){
			if(!r.getCnfg().contains("Chat.IpFilter") || r.getCnfg().getBoolean("Chat.IpFilter")){
		    String ipPattern = "([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})";
		    if (Pattern.compile(ipPattern).matcher(set.getMessage()).find()) {
		    	set.setCancelled(true);
		    	p.sendMessage(r.default1 + "You may not say IPs!");
		    }
			}
		}
		//Anti URL
		if(!r.perm(p, "uc.chat.url", false, false)){
			if(!r.getCnfg().contains("Chat.UrlFilter") || r.getCnfg().getBoolean("Chat.UrlFilter")){
				String tlds = "AC|ACADEMY|ACCOUNTANTS|ACTIVE|ACTOR|AD|AE|AERO|AF|AG|AGENCY|AI"
						+ "|AIRFORCE|AL|AM|AN|AO|AQ|AR|ARCHI|ARMY|ARPA|AS|ASIA|ASSOCIATES|AT|"
						+ "ATTORNEY|AU|AUDIO|AUTOS|AW|AX|AXA|AZ|BA|BAR|BARGAINS|BAYERN|BB|BD|"
						+ "BE|BEER|BERLIN|BEST|BF|BG|BH|BI|BID|BIKE|BIO|BIZ|BJ|BLACK|BLACKFRIDAY"
						+ "|BLUE|BM|BMW|BN|BO|BOUTIQUE|BR|BRUSSELS|BS|BT|BUILD|BUILDERS|BUZZ"
						+ "|BV|BW|BY|BZ|BZH|CA|CAB|CAMERA|CAMP|CANCERRESEARCH|CAPETOWN|"
						+ "CAPITAL|CARDS|CARE|CAREER|CAREERS|CASH|CAT|CATERING|CC|CD|CENTER"
						+ "|CEO|CF|CG|CH|CHEAP|CHRISTMAS|CHURCH|CI|CITIC|CITY|CK|CL|CLAIMS"
						+ "|CLEANING|CLINIC|CLOTHING|CLUB|CM|CN|CO|CODES|COFFEE|COLLEGE|"
						+ "COLOGNE|COM|COMMUNITY|COMPANY|COMPUTER|CONDOS|CONSTRUCTION|"
						+ "CONSULTING|CONTRACTORS|COOKING|COOL|COOP|COUNTRY|CR|CREDIT|"
						+ "CREDITCARD|CRUISES|CU|CUISINELLA|CV|CW|CX|CY|CZ|DANCE|DATING"
						+ "|DE|DEALS|DEGREE|DEMOCRAT|DENTAL|DENTIST|DESI|DIAMONDS|DIGITAL"
						+ "|DIRECT|DIRECTORY|DISCOUNT|DJ|DK|DM|DNP|DO|DOMAINS|DURBAN|DZ|EC"
						+ "|EDU|EDUCATION|EE|EG|EMAIL|ENGINEER|ENGINEERING|ENTERPRISES"
						+ "|EQUIPMENT|ER|ES|ESTATE|ET|EU|EUS|EVENTS|EXCHANGE|EXPERT|EXPOSED"
						+ "|FAIL|FARM|FEEDBACK|FI|FINANCE|FINANCIAL|FISH|FISHING|FITNESS|FJ"
						+ "|FK|FLIGHTS|FLORIST|FM|FO|FOO|FOUNDATION|FR|FROGANS|FUND|FURNITURE"
						+ "|FUTBOL|GA|GAL|GALLERY|GB|GD|GE|GENT|GF|GG|GH|GI|GIFT|GIVES|GL|"
						+ "GLASS|GLOBAL|GLOBO|GM|GMO|GN|GOP|GOV|GP|GQ|GR|GRAPHICS|GRATIS|"
						+ "GREEN|GRIPE|GS|GT|GU|GUIDE|GUITARS|GURU|GW|GY|HAMBURG|HAUS|HIPHOP"
						+ "|HIV|HK|HM|HN|HOLDINGS|HOLIDAY|HOMES|HORSE|HOST|HOUSE|HR|HT|HU|ID|"
						+ "IE|IL|IM|IMMOBILIEN|IN|INDUSTRIES|INFO|INK|INSTITUTE|INSURE|INT|"
						+ "INTERNATIONAL|INVESTMENTS|IO|IQ|IR|IS|IT|JE|JETZT|JM|JO|JOBS|"
						+ "JOBURG|JP|JUEGOS|KAUFEN|KE|KG|KH|KI|KIM|KITCHEN|KIWI|KM|KN|"
						+ "KOELN|KP|KR|KRED|KW|KY|KZ|LA|LAND|LAWYER|LB|LC|LEASE|LI|LIFE|"
						+ "LIGHTING|LIMITED|LIMO|LINK|LK|LOANS|LONDON|LOTTO|LR|LS|LT|LU|"
						+ "LUXE|LUXURY|LV|LY|MA|MAISON|MANAGEMENT|MANGO|MARKET|MARKETING|"
						+ "MC|MD|ME|MEDIA|MEET|MELBOURNE|MENU|MG|MH|MIAMI|MIL|MINI|MK|ML|"
						+ "MM|MN|MO|MOBI|MODA|MOE|MONASH|MORTGAGE|MOSCOW|MOTORCYCLES|MP|MQ"
						+ "|MR|MS|MT|MU|MUSEUM|MV|MW|MX|MY|MZ|NA|NAGOYA|NAME|NAVY|NC|NE|NET"
						+ "|NEUSTAR|NF|NG|NHK|NI|NINJA|NL|NO|NP|NR|NRW|NU|NYC|NZ|OKINAWA|OM"
						+ "|ONL|ORG|ORGANIC|OVH|PA|PARIS|PARTNERS|PARTS|PE|PF|PG|PH|PHOTO|P"
						+ "HOTOGRAPHY|PHOTOS|PHYSIO|PICS|PICTURES|PINK|PK|PL|PLACE|PLUMBING"
						+ "|PM|PN|POST|PR|PRESS|PRO|PRODUCTIONS|PROPERTIES|PS|PT|PUB|PW|PY|"
						+ "QA|QPON|QUEBEC|RE|RECIPES|RED|REHAB|REISE|REISEN|REN|RENTALS|REP"
						+ "AIR|REPORT|REPUBLICAN|REST|REVIEWS|RICH|RIO|RO|ROCKS|RODEO|RS|RU"
						+ "|RUHR|RW|RYUKYU|SA|SAARLAND|SB|SC|SCB|SCHMIDT|SCHULE|SCOT|SD|SE|"
						+ "SERVICES|SEXY|SG|SH|SHIKSHA|SHOES|SI|SINGLES|SJ|SK|SL|SM|SN|SO|SOC"
						+ "IAL|SOFTWARE|SOHU|SOLAR|SOLUTIONS|SOY|SPACE|SR|ST|SU|SUPPLIES|SUPPL"
						+ "Y|SUPPORT|SURF|SURGERY|SUZUKI|SV|SX|SY|SYSTEMS|SZ|TATTOO|TAX|TC|TD|TE"
						+ "CHNOLOGY|TEL|TF|TG|TH|TIENDA|TIPS|TIROL|TJ|TK|TL|TM|TN|TO|TODAY|TOKYO|"
						+ "TOOLS|TOWN|TOYS|TP|TR|TRADE|TRAINING|TRAVEL|TT|TV|TW|TZ|UA|UG|UK|UNIVE"
						+ "RSITY|UNO|US|UY|UZ|VA|VACATIONS|VC|VE|VEGAS|VENTURES|VERSICHERUNG|VET|V"
						+ "G|VI|VIAJES|VILLAS|VISION|VLAANDEREN|VN|VODKA|VOTE|VOTING|VOTO|VOYAGE|V"
						+ "U|WANG|WATCH|WEBCAM|WEBSITE|WED|WF|WIEN|WIKI|WORKS|WS|WTC|WTF|XN--3BST0"
						+ "0M|XN--3DS443G|XN--3E0B707E|XN--45BRJ9C|XN--4GBRIM|XN--55QW42G|XN--55QX"
						+ "5D|XN--6FRZ82G|XN--6QQ986B3XL|XN--80ADXHKS|XN--80AO21A|XN--80ASEHDB|XN-"
						+ "-80ASWG|XN--90A3AC|XN--C1AVG|XN--CG4BKI|XN--CLCHC0EA0B2G2A9GCD|XN--CZR6"
						+ "94B|XN--CZRU2D|XN--D1ACJ3B|XN--FIQ228C5HS|XN--FIQ64B|XN--FIQS8S|XN--FIQ"
						+ "Z9S|XN--FPCRJ9C3D|XN--FZC2C9E2C|XN--GECRJ9C|XN--H2BRJ9C|XN--I1B6B1A6A2E|X"
						+ "N--IO0A7I|XN--J1AMH|XN--J6W193G|XN--KPRW13D|XN--KPRY57D|XN--KPUT3I|XN--L1"
						+ "ACC|XN--LGBBAT1AD8J|XN--MGB9AWBF|XN--MGBA3A4F16A|XN--MGBAAM7A8H|XN--MGBAB"
						+ "2BD|XN--MGBAYH7GPA|XN--MGBBH1A71E|XN--MGBC0A9AZCG|XN--MGBERP4A5D4AR|XN--M"
						+ "GBX4CD0AB|XN--NGBC5AZD|XN--NQV7F|XN--NQV7FS00EMA|XN--O3CW4H|XN--OGBPF8FL|X"
						+ "N--P1AI|XN--PGBS0DH|XN--Q9JYB4C|XN--RHQV96G|XN--S9BRJ9C|XN--SES554G|XN--UN"
						+ "UP4Y|XN--WGBH1C|XN--WGBL6A|XN--XKC2AL3HYE2A|XN--XKC2DL3A5EE0H|XN--YFRO4I67"
						+ "O|XN--YGBI2AMMX|XN--ZFR164B|XXX|XYZ|YACHTS|YE|YOKOHAMA|YT|ZA|ZM|ZONE|ZW";
				String domainPattern = "([a-z-0-9]{1,50})\\.(" + tlds + ")(?![a-z0-9])";
			    if (Pattern.compile(domainPattern).matcher(set.getMessage()).find()) {
			    	set.setCancelled(true);
			    	p.sendMessage(r.default1 + "You may not say URLs!");
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


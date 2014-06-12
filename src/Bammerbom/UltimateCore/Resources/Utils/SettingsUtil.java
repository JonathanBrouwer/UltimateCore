package Bammerbom.UltimateCore.Resources.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.GamePhase;

public class SettingsUtil {
	Plugin plugin;
	public SettingsUtil(Plugin instance){
		plugin = instance;
		new Listeners(instance);
	}
	public static HashMap<UUID, PSettings> settings = new HashMap<UUID, PSettings>();
    public static String getLanguage(Player p){
        
    	try {

    		//
        Object obj = getMethod("getHandle", p.getClass()).invoke(p, (Object[]) null);
        Field f = obj.getClass().getDeclaredField("locale");
        f.setAccessible(true);
        String language = (String) f.get(obj);
        //
      
        return language;
        } catch (Exception e) {
            return "en_en";
          }
        }
    @Deprecated
    public static Difficulty getDifficulty(Player p){
    	try {
    	} catch (Exception e) {
    		e.printStackTrace();
                return Difficulty.NORMAL;
              }
    	return null;
    }
    private static Method getMethod(String n, Class<?> c) {
    for (Method m : c.getDeclaredMethods()) {
    if (m.getName().equals(n))
    return m;
    }
    return null;
    }
    //
    
}
class PSettings{

		
}
class Listeners{
	@SuppressWarnings("unused")
	public Listeners(final Plugin plugin){
		for(Player p : Bukkit.getOnlinePlayers()){
		//Values
		String locale = "en_US";
		Integer renderdistance = 0;
		String chatvisibility = "Full";
		Boolean showcolors = true;
		Boolean showcapes = true;
		//Run
		try{
		locale = (String) ReflectionUtil.execute("getHandle().locale", p);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		try{
		chatvisibility = StringUtil.firstUpperCase(((String) ReflectionUtil.execute("getHandle().getChatFlags().name()", p)).toLowerCase());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		try{
		showcolors = (Boolean) ReflectionUtil.execute("getHandle().bW", p);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		try{
			Byte s1 = (Byte) ReflectionUtil.execute("getHandle().getDataWatcher().getByte({1})", p, 16);
		  showcapes = s1.equals(0) ? true : false;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		}
		
		
		//ProtocolLIB
		if(Bukkit.getPluginManager().getPlugin("ProtocolLib") != null){
		ProtocolLibrary.getProtocolManager().addPacketListener(new com.comphenix.protocol.events.PacketListener(){
            public Plugin getPlugin() {return plugin;}
            public ListeningWhitelist getReceivingWhitelist() {return ListeningWhitelist.newBuilder().gamePhase(GamePhase.PLAYING).highest().types(com.comphenix.protocol.PacketType.Play.Client.SETTINGS).build();}
            public ListeningWhitelist getSendingWhitelist() {return ListeningWhitelist.newBuilder().gamePhase(GamePhase.PLAYING).highest().types(com.comphenix.protocol.PacketType.Play.Client.SETTINGS).build();}
            @Override
            public void onPacketReceiving(PacketEvent e) {
                if (!(e.getPacket().getType() == com.comphenix.protocol.PacketType.Play.Client.SETTINGS)) return;
                Integer renderdistance = e.getPacket().getIntegers().readSafely(0);
            }
            public void onPacketSending(PacketEvent e) {return;}
 
        });
		//PacketLIB
		}
		
	}
}
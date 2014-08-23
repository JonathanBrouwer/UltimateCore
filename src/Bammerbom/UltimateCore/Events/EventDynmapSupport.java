package Bammerbom.UltimateCore.Events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapAPI;
import org.dynmap.DynmapWebChatEvent;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.API.UCplayer;

public class EventDynmapSupport implements Listener{
	public static Plugin plugin;
	static Plugin dynmap;
	static DynmapAPI api;
	static MarkerAPI markerapi;
	//private Layer homelayer;
	static Layer warplayer;
	static long updperiod;
	static long playerupdperiod;
	static boolean reload;
	static boolean stop = false;
	static boolean warpsE = r.getCnfg().getBoolean("Dynmap.warps");
	static private Set<UUID> hiddenasserts = new HashSet<UUID>();
	public EventDynmapSupport(Plugin instance){
		
		reload = false;
		plugin = instance;
		if(r.getCnfg().getBoolean("Dynmap.enable") == true 
				&& Bukkit.getPluginManager().getPlugin("dynmap") != null
				&& Bukkit.getPluginManager().isPluginEnabled("dynmap")){
		    dynmap = Bukkit.getPluginManager().getPlugin("dynmap");
			api = (DynmapAPI) dynmap;
			Bukkit.getPluginManager().registerEvents(this, plugin);
			markerapi = api.getMarkerAPI();
			if(markerapi == null) return;
            if (reload) {
		        r.getCnfg().reload();
		        if(warpsE){
			        if (warplayer != null) {
				        if (warplayer.set != null) {
				          warplayer.set.deleteMarkerSet();
				          warplayer.set = null;
				        }
				        warplayer = null;
				    }
		        	warplayer = new WarpsLayer("[%name%]");
		        }
		    }else{
		      reload = true;
		      warplayer = new WarpsLayer("[%name%]");
		    }
            //Update task
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
            	public void run(){
            		if(!stop){
            			//Markers
            			if(warpsE){
            				warplayer.updateMarkerSet();
            			}
            			//Players
            			for(Player pl : r.getOnlinePlayers()){
            				if(UC.getPlayer(pl).isVanished() && !hiddenasserts.contains(pl.getUniqueId())){
            					api.assertPlayerInvisibility(pl, true, plugin);
                				hiddenasserts.add(pl.getUniqueId());
            				}
            			}
            			for(UUID id : hiddenasserts){
            				if(!UC.getPlayer(id).isVanished()){
            				    api.assertPlayerInvisibility(Bukkit.getPlayer(id), false, plugin);
                				hiddenasserts.remove(id);
            				}
            			}
            		}
            	}
            }, 100L, 100L);
            
		}
		
	}
	public static void stop(){
		if(warplayer != null){
			warplayer.cleanup();
			warplayer = null;
		}
		stop = true;
	}
	//Listeners
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
    public void onDynmapWebChat(DynmapWebChatEvent e){
    	String name = e.getName();
    	if(name != null){
    		Player p = r.searchPlayer(name);
    		if(p != null){
    			UCplayer pl = UC.getPlayer(p);
    			if(pl != null && (pl.isBanned() || pl.isMuted())){
    				e.setCancelled(true);
    			}
    		}
    	}
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
    	HashSet<UUID> newasserts = new HashSet<UUID>();
		for(Player pl : r.getOnlinePlayers()){
			if(UC.getPlayer(pl).isVanished() && !hiddenasserts.contains(pl.getUniqueId())){
				api.assertPlayerInvisibility(pl, true, plugin);
			}
			newasserts.add(pl.getUniqueId());
			hiddenasserts.remove(pl.getUniqueId());
		}
		for(UUID id : hiddenasserts){
			api.assertPlayerInvisibility(Bukkit.getPlayer(id), false, plugin);
		}
		hiddenasserts = newasserts;
    }
    
    //Layers
    private class WarpsLayer extends Layer
    {
      public WarpsLayer(String fmt)
      {
        super("warps", "Warps", "portal", fmt);
      }

      public Map<String, Location> getMarkers() {
    	  return UC.getServer().getWarps();
      }
    }
    public abstract class Layer
    {
      MarkerSet set;
      MarkerIcon deficon;
      String labelfmt;
      Set<String> hidden;
      Map<String, Marker> markers = new HashMap<String, Marker>();

      public Layer(String id, String deflabel, String deficon, String deflabelfmt) {
        this.set = markerapi.getMarkerSet("ultimatecore." + id);
        if (this.set == null)
          this.set = markerapi.createMarkerSet("ultimatecore." + id, deflabel, null, false);
        else
          this.set.setMarkerSetLabel(deflabel);
        if (this.set == null) {
          return;
        }
        this.set.setLayerPriority(10);
        this.set.setHideByDefault(false);
        int minzoom = 0;
        if (minzoom > 0)
          this.set.setMinZoom(minzoom);
        String icon = deficon;
        this.deficon = markerapi.getMarkerIcon(icon);
        if (this.deficon == null) {
          this.deficon = markerapi.getMarkerIcon(deficon);
        }
        this.labelfmt = deflabelfmt;
        List<String> lst = r.getCnfg().getStringList("Dynmap.hiddenwarps");
        if (lst != null)
          this.hidden = new HashSet<String>(lst);
      }

      void cleanup() {
        if (this.set != null) {
          this.set.deleteMarkerSet();
          this.set = null;
        }
        this.markers.clear();
      }

      boolean isVisible(String id, String wname) {
        if ((this.hidden != null) && (!this.hidden.isEmpty()) && (
          (this.hidden.contains(id)) || (this.hidden.contains("world:" + wname)))) {
          return false;
        }
        return true;
      }

      void updateMarkerSet() {
        Map<String, Marker> newmap = new HashMap<String, Marker>();

        Map<String, Location> marks = getMarkers();
        for (String name : marks.keySet()) {
          Location loc = (Location)marks.get(name);

          String wname = loc.getWorld().getName();

          if (isVisible(name, wname))
          {
            String id = wname + "/" + name;

            String label = this.labelfmt.replace("%name%", name);

            Marker m = (Marker)this.markers.remove(id);
            if (m == null) {
              m = this.set.createMarker(id, label, wname, loc.getX(), loc.getY(), loc.getZ(), this.deficon, false);
            }
            else {
              m.setLocation(wname, loc.getX(), loc.getY(), loc.getZ());
              m.setLabel(label);
              m.setMarkerIcon(this.deficon);
            }
            newmap.put(id, m);
          }
        }
        for (Marker oldm : this.markers.values()) {
          oldm.deleteMarker();
        }

        this.markers.clear();
        this.markers = newmap;
      }

      public abstract Map<String, Location> getMarkers();
    }
}

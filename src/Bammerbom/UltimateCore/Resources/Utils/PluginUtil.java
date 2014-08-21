package Bammerbom.UltimateCore.Resources.Utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;

public class PluginUtil
{
  @SuppressWarnings("rawtypes")
public static boolean unload(Plugin plugin)
  {
	  	if(plugin == null){
	  		return false;
    	}
	  	//
        HandlerList.unregisterAll(plugin);
        Bukkit.getServicesManager().unregisterAll(plugin);
        plugin.getServer().getScheduler().cancelTasks(plugin);
        //
    String name = plugin.getName();

    PluginManager pluginManager = Bukkit.getPluginManager();

    SimpleCommandMap commandMap = null;

    List plugins = null;

    Map names = null;
    Map commands = null;
    Map listeners = null;

    boolean reloadlisteners = true;

    if (pluginManager != null)
    {
      try
      {
        Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
        pluginsField.setAccessible(true);
        plugins = (List)pluginsField.get(pluginManager);

        Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
        lookupNamesField.setAccessible(true);
        names = (Map)lookupNamesField.get(pluginManager);
        try
        {
          Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
          listenersField.setAccessible(true);
          listeners = (Map)listenersField.get(pluginManager);
        } catch (Exception e) {
          reloadlisteners = false;
        }

        Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);
        commandMap = (SimpleCommandMap)commandMapField.get(pluginManager);

        Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
        knownCommandsField.setAccessible(true);
        commands = (Map)knownCommandsField.get(commandMap);
      }
      catch (NoSuchFieldException e) {
        e.printStackTrace();
        return false;
      } catch (IllegalAccessException e) {
        e.printStackTrace();
        return false;
      }
    }

    pluginManager.disablePlugin(plugin);

    if ((plugins != null) && (plugins.contains(plugin))) {
      plugins.remove(plugin);
    }
    if ((names != null) && (names.containsKey(name))) {
      names.remove(name);
    }
    Iterator it;
    if ((listeners != null) && (reloadlisteners))
      for (Object set2 : listeners.values()){
    	  SortedSet set = (SortedSet) set2;
        for (it = set.iterator(); it.hasNext(); ) {
          RegisteredListener value = (RegisteredListener)it.next();
          if (value.getPlugin() == plugin)
            it.remove();
        }
      }
    if (commandMap != null) {
      for (it = commands.entrySet().iterator(); it.hasNext(); ) {
        Map.Entry entry = (Map.Entry)it.next();
        if ((entry.getValue() instanceof PluginCommand)) {
          PluginCommand c = (PluginCommand)entry.getValue();
          if (c.getPlugin() == plugin) {
            c.unregister(commandMap);
            it.remove();
          }
        }

      }

    }

    ClassLoader cl = plugin.getClass().getClassLoader();

    if ((cl instanceof URLClassLoader)) {
      try {
        ((URLClassLoader)cl).close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }

    }
    System.gc();

    return true;
  }
  @SuppressWarnings("rawtypes")
public boolean unloadNS(Plugin plugin)
  {
	  	if(plugin == null){
	  		return false;
    	}
	  	//
        HandlerList.unregisterAll(plugin);
        Bukkit.getServicesManager().unregisterAll(plugin);
        plugin.getServer().getScheduler().cancelTasks(plugin);
        //
    String name = plugin.getName();

    PluginManager pluginManager = Bukkit.getPluginManager();

    SimpleCommandMap commandMap = null;

    List plugins = null;

    Map names = null;
    Map commands = null;
    Map listeners = null;

    boolean reloadlisteners = true;

    if (pluginManager != null)
    {
      try
      {
        Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
        pluginsField.setAccessible(true);
        plugins = (List)pluginsField.get(pluginManager);

        Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
        lookupNamesField.setAccessible(true);
        names = (Map)lookupNamesField.get(pluginManager);
        try
        {
          Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
          listenersField.setAccessible(true);
          listeners = (Map)listenersField.get(pluginManager);
        } catch (Exception e) {
          reloadlisteners = false;
        }

        Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);
        commandMap = (SimpleCommandMap)commandMapField.get(pluginManager);

        Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
        knownCommandsField.setAccessible(true);
        commands = (Map)knownCommandsField.get(commandMap);
      }
      catch (NoSuchFieldException e) {
        e.printStackTrace();
        return false;
      } catch (IllegalAccessException e) {
        e.printStackTrace();
        return false;
      }
    }

    pluginManager.disablePlugin(plugin);

    if ((plugins != null) && (plugins.contains(plugin))) {
      plugins.remove(plugin);
    }
    if ((names != null) && (names.containsKey(name))) {
      names.remove(name);
    }
    Iterator it;
    if ((listeners != null) && (reloadlisteners))
      for (Object set2 : listeners.values()){
    	  SortedSet set = (SortedSet) set2;
        for (it = set.iterator(); it.hasNext(); ) {
          RegisteredListener value = (RegisteredListener)it.next();
          if (value.getPlugin() == plugin)
            it.remove();
        }
      }
    if (commandMap != null) {
      for (it = commands.entrySet().iterator(); it.hasNext(); ) {
        Map.Entry entry = (Map.Entry)it.next();
        if ((entry.getValue() instanceof PluginCommand)) {
          PluginCommand c = (PluginCommand)entry.getValue();
          if (c.getPlugin() == plugin) {
            c.unregister(commandMap);
            it.remove();
          }
        }

      }

    }

    ClassLoader cl = plugin.getClass().getClassLoader();

    if ((cl instanceof URLClassLoader)) {
      try {
        ((URLClassLoader)cl).close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }

    }
    System.gc();

    return true;
  }
}
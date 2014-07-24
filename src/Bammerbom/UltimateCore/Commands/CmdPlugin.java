package Bammerbom.UltimateCore.Commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Utils.PluginUtil;
import Bammerbom.UltimateCore.Resources.Utils.StringUtil;

import com.google.common.io.Files;

public class CmdPlugin {
	static Plugin plugin;
    public CmdPlugin(Plugin plugin) {
    	CmdPlugin.plugin = plugin;
    }

    private static void unregisterAllPluginCommands(String pluginName) {
        try {
            Object result = getPrivateField(plugin.getServer().getPluginManager(), "commandMap");
            SimpleCommandMap commandMap = (SimpleCommandMap) result;
            Object map = getPrivateField(commandMap, "knownCommands");
            @SuppressWarnings("unchecked") HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
            final List<Command> commands = new ArrayList<>(commandMap.getCommands());
            for (Command c : commands) {
                if (!(c instanceof PluginCommand)) continue;
                final PluginCommand pc = (PluginCommand) c;
                if (!pc.getPlugin().getName().equals(pluginName)) continue;
                knownCommands.remove(pc.getName());
                for (String alias : pc.getAliases()) {
                    if (knownCommands.containsKey(alias)) {
                        final Command ac = knownCommands.get(alias);
                        if (!(ac instanceof PluginCommand)) continue;
                        final PluginCommand apc = (PluginCommand) ac;
                        if (!apc.getPlugin().getName().equals(pluginName)) continue;
                        knownCommands.remove(alias);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void removePluginFromList(Plugin p) {
        try {
            @SuppressWarnings("unchecked")
            final List<Plugin> plugins = (List<Plugin>) getPrivateField(plugin.getServer().getPluginManager(), "plugins");
            plugins.remove(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean downloadAndMovePlugin(String url, String saveAs, boolean recursive, CommandSender cs) {
        if (saveAs == null) saveAs = "";
        BufferedInputStream bis;
        final HttpURLConnection huc;
        try {
            huc = (HttpURLConnection) new URL(url).openConnection();
            huc.setInstanceFollowRedirects(true);
            huc.connect();
            bis = new BufferedInputStream(huc.getInputStream());
        } catch (MalformedURLException e) {
            cs.sendMessage(r.default1 + "The download link is invalid!");
            return false;
        } catch (IOException e) {
            cs.sendMessage(r.default1 + "An internal input/output error occurred. Please try again. (" + r.default2 + e.getMessage() + r.default1 + ")");
            return false;
        }
        String[] urlParts = huc.getURL().toString().split("(\\\\|/)");
        final String fileName = (!saveAs.isEmpty()) ? saveAs : urlParts[urlParts.length - 1];
        File f = new File(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString() + File.separator + fileName);
        while (f.getParentFile().exists())
            f = new File(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString() + File.separator + fileName);
        if (!fileName.endsWith(".zip") && !fileName.endsWith(".jar")) {
            cs.sendMessage(r.default1 + "The file wasn't a zip or jar file, so it was not downloaded.");
            cs.sendMessage(r.default1 + "Filename: " + r.default2 + fileName);
            return false;
        }
        f.getParentFile().mkdirs();
        BufferedOutputStream bos;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(f));
        } catch (FileNotFoundException e) {
            cs.sendMessage(r.default1 + "The temporary download folder was not found. Make sure that " + r.default2 + System.getProperty("java.io.tmpdir") + r.default1 + " is writable.");
            return false;
        }
        int b;
        cs.sendMessage(r.default1 + "Downloading file " + r.default2 + fileName + r.default1 + "...");
        try {
            try {
                while ((b = bis.read()) != -1) bos.write(b);
            } finally {
                bos.flush();
                bos.close();
            }
        } catch (IOException e) {
            cs.sendMessage(r.default1 + "An internal input/output error occurred. Please try again. (" + r.default2 + e.getMessage() + r.default1 + ")");
            return false;
        }
        if (fileName.endsWith(".zip")) {
            cs.sendMessage(r.default1 + "Decompressing zip...");
            UnZip.decompress(f.getAbsolutePath(), f.getParent());
        }
        for (File fi : listFiles(f.getParentFile(), recursive)) {
            if (!fi.getName().endsWith(".jar")) continue;
            cs.sendMessage(r.default1 + "Moving " + r.default2 + fi.getName() + r.default1 + " to plugins folder...");
            try {
                Files.move(fi, new File(plugin.getDataFolder().getParentFile() + File.separator + fi.getName()));
            } catch (IOException e) {
                cs.sendMessage(r.default1 + "Couldn't move " + r.default2 + fi.getName() + r.default1 + ": " + r.default2 + e.getMessage());
            }
        }
        cs.sendMessage(r.default1 + "Removing temporary folder...");
        deleteDirectory(f.getParentFile());
        return true;
    }

    private static List<String> getDependedOnBy(Plugin dep) {
        return getDependedOnBy(dep.getName());
    }

    private static List<String> getDependedOnBy(String name) {
        final List<String> dependedOnBy = new ArrayList<>();
        for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {
            if (pl == null) continue;
            if (!pl.isEnabled()) continue;
            PluginDescriptionFile pdf = pl.getDescription();
            if (pdf == null) continue;
            List<String> depends = pdf.getDepend();
            if (depends == null) continue;
            for (String depend : depends) if (name.equalsIgnoreCase(depend)) dependedOnBy.add(pl.getName());
        }
        return dependedOnBy;
    }

    private static String getCustomTag(String name) {
        ConfigurationSection cs = plugin.getConfig().getConfigurationSection("pluginmanager.custom_tags");
        if (cs == null) return null;
        for (String key : cs.getKeys(false)) {
            if (!key.equalsIgnoreCase(name)) continue;
            return cs.getString(key);
        }
        return null;
    }

    public static String updateCheck(String name, String currentVersion) throws Exception {
        String tag = getCustomTag(name);
        if (tag == null) tag = name.toLowerCase();
        String pluginUrlString = "http://dev.bukkit.org/bukkit-plugins/" + tag + "/files.rss";
        URL url = new URL(pluginUrlString);
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openConnection().getInputStream());
        doc.getDocumentElement().normalize();
        NodeList nodes = doc.getElementsByTagName("item");
        Node firstNode = nodes.item(0);
        if (firstNode.getNodeType() == 1) {
            Element firstElement = (Element) firstNode;
            NodeList firstElementTagName = firstElement.getElementsByTagName("title");
            Element firstNameElement = (Element) firstElementTagName.item(0);
            NodeList firstNodes = firstNameElement.getChildNodes();
            return firstNodes.item(0).getNodeValue();
        }
        return currentVersion;
    }

    public static void handle(final CommandSender cs, Command cmd, String label, final String[] args) {
        if (args.length < 1) {
            Bukkit.dispatchCommand(cs, "plugin help");
            return;
        }
        String subcmd = args[0];
        final PluginManager pm = plugin.getServer().getPluginManager();
        if (subcmd.equalsIgnoreCase("load")) {
        	if(!r.perm(cs, "uc.plugin.load", false, false) && !r.perm(cs, "uc.plugin", false, false)){
        		cs.sendMessage(r.mes("NoPermissions")); return;
        	}
            if (args.length < 2) {
                cs.sendMessage(r.default1 + "Please provide the name of the jar to load!");
                return;
            }
            File f = new File(plugin.getDataFolder().getParentFile() + File.separator + args[1]);
            if (!f.exists()) {
                cs.sendMessage(r.default1 + "That file does not exist!");
                return;
            }
            if (!f.canRead()) {
                cs.sendMessage(r.default1 + "Can't read that file!");
                return;
            }
            Plugin p;
            try {
                p = pm.loadPlugin(f);
                if (p == null) {
                    cs.sendMessage(r.default1 + "Could not load plugin: plugin was invalid.");
                    cs.sendMessage(r.default1 + "Make sure it ends with .jar!");
                    return;
                }
                pm.enablePlugin(p);
            } catch (UnknownDependencyException e) {
                cs.sendMessage(r.default1 + "Missing dependency: " + e.getMessage());
                return;
            } catch (InvalidDescriptionException e) {
                cs.sendMessage(r.default1 + "That plugin contained an invalid description!");
                return;
            } catch (InvalidPluginException e) {
                cs.sendMessage(r.default1 + "That file is not a plugin!");
                return;
            }
            if (p.isEnabled())
                cs.sendMessage(r.default1 + "Loaded and enabled " + r.default2 + p.getName() + r.default1 + " successfully.");
            else
                cs.sendMessage(r.default1 + "Could not load and enable " + r.default2 + p.getName() + r.default1 + ".");
            return;
        } else if (subcmd.equalsIgnoreCase("disable")) {
        	if(!r.perm(cs, "uc.plugin.disable", false, false) && !r.perm(cs, "uc.plugin", false, false)){
        		cs.sendMessage(r.mes("NoPermissions")); return;
        	}
            if (args.length < 2) {
                cs.sendMessage(r.default1 + "Please provide the name of the plugin to disable!");
                return;
            }
            Plugin p = pm.getPlugin(args[1]);
            if (p == null) {
                cs.sendMessage(r.default1 + "No such plugin!");
                return;
            }
            if (!p.isEnabled()) {
                cs.sendMessage(r.default2 + p.getName() + r.default1 + " is already disabled!");
            }
            final List<String> depOnBy = getDependedOnBy(p);
            if (!depOnBy.isEmpty()) {
                cs.sendMessage(r.default1 + "Could not unload " + r.default2 + p.getName() + r.default1 + " because it is depended on by the following:");
                StringBuilder sb = new StringBuilder();
                for (String dep : depOnBy) {
                    sb.append(r.default2);
                    sb.append(dep);
                    sb.append(ChatColor.RESET);
                    sb.append(", ");
                }
                cs.sendMessage(sb.substring(0, sb.length() - 4));
                return;
            }
            pm.disablePlugin(p);
            if (!p.isEnabled())
                cs.sendMessage(r.default1 + "Disabled " + r.default2 + p.getName() + r.default1 + " successfully!");
            else cs.sendMessage(r.default1 + "Could not disabled that plugin!");
            return;
        } else if (subcmd.equalsIgnoreCase("enable")) {
        	if(!r.perm(cs, "uc.plugin.enable", false, false) && !r.perm(cs, "uc.plugin", false, false)){
        		cs.sendMessage(r.mes("NoPermissions")); return;
        	}
            if (args.length < 2) {
                cs.sendMessage(r.default1 + "Please provide the name of the plugin to enable!");
                return;
            }
            Plugin p = pm.getPlugin(args[1]);
            if (p == null) {
                cs.sendMessage(r.default1 + "No such plugin!");
                return;
            }
            if (p.isEnabled()) {
                cs.sendMessage(r.default1 + "Plugin is already enabled!");
                return;
            }
            pm.enablePlugin(p);
            if (p.isEnabled())
                cs.sendMessage(r.default1 + "Successfully enabled " + r.default2 + p.getName() + r.default1 + "!");
            else
                cs.sendMessage(r.default1 + "Could not enable " + r.default2 + p.getName() + r.default1 + ".");
            return;
        } else if (subcmd.equalsIgnoreCase("reload")) {
        	if(!r.perm(cs, "uc.plugin.reload", false, false) && !r.perm(cs, "uc.plugin", false, false)){
        		cs.sendMessage(r.mes("NoPermissions")); return;
        	}
            if (args.length < 2) {
                cs.sendMessage(r.default1 + "Please provide the name of the plugin to reload!");
                return;
            }
            Plugin p = pm.getPlugin(args[1]);
            if (p == null) {
                cs.sendMessage(r.default1 + "No such plugin!");
                return;
            }
            pm.disablePlugin(p);
            pm.enablePlugin(p);
            cs.sendMessage(r.default1 + "Reloaded " + r.default2 + p.getName() + r.default1 + ".");
            return;
        } else if (subcmd.equalsIgnoreCase("unload")) {
        	if(!r.perm(cs, "uc.plugin.unload", false, false) && !r.perm(cs, "uc.plugin", false, false)){
        		cs.sendMessage(r.mes("NoPermissions")); return;
        	}
            if (args.length < 2) {
                cs.sendMessage(r.default1 + "Please provide the name of the plugin to unload!");
                return;
            }
            final Plugin p = pm.getPlugin(args[1]);
            if (p == null) {
                cs.sendMessage(r.default1 + "No such plugin!");
                return;
            }
            List<String> depOnBy = getDependedOnBy(p);
            if (!depOnBy.isEmpty()) {
                cs.sendMessage(r.default1 + "Could not unload " + r.default2 + p.getName() + r.default1 + " because it is depended on by the following:");
                StringBuilder sb = new StringBuilder();
                for (String dep : depOnBy) {
                    sb.append(r.default2);
                    sb.append(dep);
                    sb.append(ChatColor.RESET);
                    sb.append(", ");
                }
                cs.sendMessage(sb.substring(0, sb.length() - 4));
                return;
            }
            final Runnable run = new Runnable() {
                @Override
                public void run() {
                    /*unregisterAllPluginCommands(p.getName());
                    HandlerList.unregisterAll(p);
                    Bukkit.getServicesManager().unregisterAll(p);
                    CmdPlugin.plugin.getServer().getScheduler().cancelTasks(p);
                    pm.disablePlugin(p);
                    removePluginFromList(p);*/
                	
                	PluginUtil.unload(p);
                    cs.sendMessage(r.default1 + "Unloaded " + r.default2 + p.getName() + r.default1 + ".");
                }
            };
            cs.sendMessage(r.default1 + "Unloading...");
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, run);
            return;
        } else if (subcmd.equalsIgnoreCase("update")) {
        	if(!r.perm(cs, "uc.plugin.update", false, false) && !r.perm(cs, "uc.plugin", false, false)){
        		cs.sendMessage(r.mes("NoPermissions")); return;
        	}
            if (args.length < 3) {
                cs.sendMessage(r.default1 + "Please provide the name of the plugin to update and its filename!");
                return;
            }
            final Plugin p = pm.getPlugin(args[1]);
            if (p == null) {
                cs.sendMessage(r.default1 + "No such plugin!");
                return;
            }
            List<String> depOnBy = getDependedOnBy(p);
            if (!depOnBy.isEmpty()) {
                cs.sendMessage(r.default1 + "Could not unload " + r.default2 + p.getName() + r.default1 + " because it is depended on by the following:");
                StringBuilder sb = new StringBuilder();
                for (String dep : depOnBy) {
                    sb.append(r.default2);
                    sb.append(dep);
                    sb.append(ChatColor.RESET);
                    sb.append(", ");
                }
                cs.sendMessage(sb.substring(0, sb.length() - 4));
                return;
            }
            final File f = new File(plugin.getDataFolder().getParentFile() + File.separator + args[2]);
            if (!f.exists()) {
                cs.sendMessage(r.default1 + "That file does not exist!");
                return;
            }
            if (!f.canRead()) {
                cs.sendMessage(r.default1 + "Can't read that file!");
                return;
            }
            cs.sendMessage(r.default1 + "Starting update process.");
            final Runnable run = new Runnable() {
                @Override
                public void run() {
                    unregisterAllPluginCommands(p.getName());
                    HandlerList.unregisterAll(p);
                    CmdPlugin.plugin.getServer().getScheduler().cancelTasks(p);
                    pm.disablePlugin(p);
                    try {
                        Plugin loadedPlugin = pm.loadPlugin(f);
                        if (loadedPlugin == null) {
                            cs.sendMessage(r.default1 + "Could not load plugin: plugin was invalid.");
                            cs.sendMessage(r.default1 + "Make sure it ends with .jar!");
                            return;
                        }
                        pm.enablePlugin(loadedPlugin);
                    } catch (UnknownDependencyException e) {
                        cs.sendMessage(r.default1 + "Missing dependency: " + e.getMessage());
                        return;
                    } catch (InvalidDescriptionException e) {
                        cs.sendMessage(r.default1 + "That plugin contained an invalid description!");
                        return;
                    } catch (InvalidPluginException e) {
                        cs.sendMessage(r.default1 + "That file is not a plugin!");
                        return;
                    }
                    removePluginFromList(p);
                    cs.sendMessage(r.default1 + "Updated " + r.default2 + p.getName() + r.default1 + " successfully.");
                }
            };
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, run);
            return;
        } else if (subcmd.equalsIgnoreCase("reloadall")) {
        	if(!r.perm(cs, "uc.plugin.reloadall", false, false) && !r.perm(cs, "uc.plugin", false, false)){
        		cs.sendMessage(r.mes("NoPermissions")); return;
        	}
            final Runnable run = new Runnable() {
                @Override
                public void run() {
                    for (Plugin p : pm.getPlugins()) {
                        pm.disablePlugin(p);
                        pm.enablePlugin(p);
                    }
                    cs.sendMessage(r.default1 + "Reloaded all plugins!");
                }
            };
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, run);
            return;
        } else if (subcmd.equalsIgnoreCase("list")) {
        	if(!r.perm(cs, "uc.plugin.list", false, false) && !r.perm(cs, "uc.plugin", false, false) && !r.perm(cs, "uc.plugins", false, false)){
        		cs.sendMessage(r.mes("NoPermissions")); return;
        	}
            Plugin[] ps = pm.getPlugins();
            StringBuilder list = new StringBuilder();
            int enabled = 0;
            int disabled = 0;
            for (Plugin p : ps) {
                String name = p.getName();
                if (!p.isEnabled()) {
                    name += r.default1 + " (disabled)";
                    disabled += 1;
                } else enabled += 1;
                list.append(r.default2);
                list.append(name);
                list.append(ChatColor.RESET);
                list.append(", ");
            }
            cs.sendMessage(r.default1 + "Plugins (" + r.default2 + enabled + ((disabled > 0) ? r.default1 + "/" + r.default2 + disabled + " disabled" : "") + r.default1 + "): " + list.substring(0, list.length() - 4));
            return;
        } else if (subcmd.equalsIgnoreCase("info")) {
        	if(!r.perm(cs, "uc.plugin.info", false, false) && !r.perm(cs, "uc.plugin", false, false)){
        		cs.sendMessage(r.mes("NoPermissions")); return;
        	}
            if (args.length < 2) {
                cs.sendMessage(r.default1 + "Please provide the name of the plugin!");
                return;
            }
            Plugin p = pm.getPlugin(args[1]);
            if (p == null) {
                cs.sendMessage(r.default1 + "No such plugin!");
                return;
            }
            PluginDescriptionFile pdf = p.getDescription();
            if (pdf == null) {
                cs.sendMessage(r.default1 + "Can't get information from " + r.default2 + p.getName() + r.default1 + ".");
                return;
            }
            String version = pdf.getVersion();
            List<String> authors = pdf.getAuthors();
            String site = pdf.getWebsite();
            List<String> softDep = pdf.getSoftDepend();
            List<String> dep = pdf.getDepend();
            String name = pdf.getName();
            String desc = pdf.getDescription();
            if (name != null && !name.isEmpty())
                cs.sendMessage(r.default1 + "Name: " + r.default2 + name);
            if (version != null && !version.isEmpty())
                cs.sendMessage(r.default1 + "Version: " + r.default2 + version);
            if (site != null && !site.isEmpty())
                cs.sendMessage(r.default1 + "Site: " + r.default2 + site);
            if (desc != null && !desc.isEmpty())
                cs.sendMessage(r.default1 + "Description: " + r.default2 + desc.replaceAll("\r?\n", ""));
            if (authors != null && !authors.isEmpty())
                cs.sendMessage(r.default1 + "Author" + ((authors.size() > 1) ? "s" : "") + ": " + r.default2 + StringUtil.join(ChatColor.RESET + ", " + r.default2, authors));
            if (softDep != null && !softDep.isEmpty())
                cs.sendMessage(r.default1 + "Soft Dependencies: " + r.default2 + StringUtil.join(ChatColor.RESET + ", " + r.default2, softDep));
            if (dep != null && !dep.isEmpty())
                cs.sendMessage(r.default1 + "Dependencies: " + r.default2 + StringUtil.join(ChatColor.RESET + ", " + r.default2, dep));
            cs.sendMessage(r.default1 + "Enabled: " + r.default2 + ((p.isEnabled()) ? "Yes" : "No"));
            return;
        } else if (subcmd.equalsIgnoreCase("commands")) {
        	if(!r.perm(cs, "uc.plugin.commands", false, false) && !r.perm(cs, "uc.plugin", false, false)){
        		cs.sendMessage(r.mes("NoPermissions")); return;
        	}
            if (args.length < 2) {
                cs.sendMessage(r.default1 + "Please provide the name of the plugin!");
                return;
            }
            Plugin p = pm.getPlugin(args[1]);
            if (p == null) {
                cs.sendMessage(r.default1 + "No such plugin!");
                return;
            }
            Map<String, Map<String, Object>> commands = p.getDescription().getCommands();
            if (commands == null) {
                cs.sendMessage(r.default2 + p.getName() + r.default1 + " has no registered commands.");
                return;
            }
            for (Entry<String, Map<String, Object>> entry : commands.entrySet()) {
                Object odesc = entry.getValue().get("description");
                String desc = (odesc != null) ? odesc.toString() : "";
                cs.sendMessage(r.default2 + "/" + entry.getKey() + ((desc.equals("")) ? "" : r.default1 + " - " + desc));
            }
            return;
        } else if (subcmd.equalsIgnoreCase("help") || subcmd.equals("?")) {
        	if(!r.perm(cs, "uc.plugin.help", false, false) && !r.perm(cs, "uc.plugin", false, false)){
        		cs.sendMessage(r.mes("NoPermissions")); return;
        	}
            cs.sendMessage(ChatColor.GOLD + "================================");
            cs.sendMessage(r.default2 + "/" + label + " load [jar]" + r.default1 + " - Loads and enables a new plugin");
            cs.sendMessage(r.default2 + "/" + label + " unload [plugin]" + r.default1 + " - Unloads a plugin and removes it from the plugin list");
            cs.sendMessage(r.default2 + "/" + label + " disable [plugin]" + r.default1 + " - Disables an already loaded plugin");
            cs.sendMessage(r.default2 + "/" + label + " enable [plugin]" + r.default1 + " - Enables a disabled plugin");
            cs.sendMessage(r.default2 + "/" + label + " reload [plugin]" + r.default1 + " - Disables then enables a plugin");
            cs.sendMessage(r.default2 + "/" + label + " reloadall" + r.default1 + " - Reloads every plugin");
            cs.sendMessage(r.default2 + "/" + label + " delete [jar]" + r.default1 + " - Tries to delete the specified jar");
            cs.sendMessage(r.default2 + "/" + label + " update [plugin] [jar]" + r.default1 + " - Disables the plugin and loads the new jar");
            cs.sendMessage(r.default2 + "/" + label + " commands [plugin]" + r.default1 + " - Lists all registered commands and their description of a plugin");
            cs.sendMessage(r.default2 + "/" + label + " list" + r.default1 + " - Lists all the plugins");
            cs.sendMessage(r.default2 + "/" + label + " info [plugin]" + r.default1 + " - Displays information about a plugin");
            cs.sendMessage(r.default2 + "/" + label + " updatecheck [plugin] (tag)" + r.default1 + " - Attempts to check for the newest version of a plugin; may not always work correctly");
            cs.sendMessage(r.default2 + "/" + label + " updatecheckall" + r.default1 + " - Attempts to check for newest version of all plugins");
            cs.sendMessage(r.default2 + "/" + label + " download [tag]" + r.default1 + " - Attempts to download a plugin from BukkitDev using its tag");
            cs.sendMessage(r.default2 + "/" + label + " search [search] (page)" + r.default1 + " - Searches BukkitDev for a tag to use in download");
            cs.sendMessage(ChatColor.GOLD + "================================");
            return;
        } else if (subcmd.equalsIgnoreCase("download") || subcmd.equalsIgnoreCase("install")) {
        	if(!r.perm(cs, "uc.plugin.download", false, false) && !r.perm(cs, "uc.plugin", false, false)){
        		cs.sendMessage(r.mes("NoPermissions")); return;
        	}
            if (args.length < 2) {
                cs.sendMessage(r.default1 + "Please provide plugin tag!");
                cs.sendMessage(r.default1 + "http://dev.bukkit.org/bukkit-plugins/" + r.default2 + "ultimate_core" + r.default1 + "/");
                return;
            }
            final boolean recursive = args.length > 2 && args[2].equalsIgnoreCase("true");
            String customTag = getCustomTag(args[1]);
            final String tag = (customTag == null) ? args[1].toLowerCase() : customTag;
            final String commandUsed = label;
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    cs.sendMessage(r.default1 + "Getting download link...");
                    String pluginUrlString = "http://dev.bukkit.org/server-mods/" + tag + "/files.rss";
                    String file;
                    try {
                        URL url = new URL(pluginUrlString);
                        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openConnection().getInputStream());
                        doc.getDocumentElement().normalize();
                        NodeList nodes = doc.getElementsByTagName("item");
                        Node firstNode = nodes.item(0);
                        if (firstNode.getNodeType() == 1) {
                            Element firstElement = (Element) firstNode;
                            NodeList firstElementTagName = firstElement.getElementsByTagName("link");
                            Element firstNameElement = (Element) firstElementTagName.item(0);
                            NodeList firstNodes = firstNameElement.getChildNodes();
                            String link = firstNodes.item(0).getNodeValue();
                            URL dpage = new URL(link);
                            BufferedReader br = new BufferedReader(new InputStreamReader(dpage.openStream()));
                            String inputLine;
                            StringBuilder content = new StringBuilder();
                            while ((inputLine = br.readLine()) != null) content.append(inputLine);
                            br.close();
                            file = StringUtils.substringBetween(content.toString(), "<li class=\"user-action user-action-download\"><span><a href=\"", "\">Download</a></span></li>");
                        } else throw new Exception();
                    } catch (Exception e) {
                        cs.sendMessage(r.default1 + "Could not fetch download link! Either this plugin has no downloads, or you specified an invalid tag.");
                        cs.sendMessage(r.default1 + "Tag: http://dev.bukkit.org/server-mods/" + r.default2 + "plugin-name" + r.default1 + "/");
                        return;
                    }
                    if (downloadAndMovePlugin(file, null, recursive, cs))
                        cs.sendMessage(r.default1 + "Downloaded plugin. Use " + r.default2 + "/" + commandUsed + " load" + r.default1 + " to enable it.");
                    else cs.sendMessage(r.default1 + "Could not download that plugin. Please try again.");
                }
            };
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, run);
            return;
        } else if (subcmd.equalsIgnoreCase("updatecheckall")) {
        	if(!r.perm(cs, "uc.plugin.updatecheckall", false, false) && !r.perm(cs, "uc.plugin", false, false)){
        		cs.sendMessage(r.mes("NoPermissions")); return;
        	}
            final Runnable run = new Runnable() {
                @Override
                public void run() {
                    for (Plugin p : pm.getPlugins()) {
                        String version = p.getDescription().getVersion();
                        if (version == null) continue;
                        String checked;
                        try {
                            checked = updateCheck(p.getName(), version);
                        } catch (Exception e) {
                            continue;
                        }
                        if (checked.contains(version)) continue;
                        cs.sendMessage(r.default2 + p.getName() + r.default1 + " may have an update. O: " + r.default2 + version + r.default1 + " N: " + r.default2 + checked);
                    }
                    cs.sendMessage(r.default1 + "Finished checking for updates.");
                }
            };
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, run);
            return;
        } else if (subcmd.equalsIgnoreCase("updatecheck")) {
        	if(!r.perm(cs, "uc.plugin.updatecheck", false, false) && !r.perm(cs, "uc.plugin", false, false)){
        		cs.sendMessage(r.mes("NoPermissions")); return;
        	}
            if (args.length < 2) {
                cs.sendMessage(r.default1 + "Please provide the name of the plugin!");
                return;
            }
            Plugin p = pm.getPlugin(args[1]);
            if (p == null) {
                cs.sendMessage(r.default1 + "No such plugin!");
                return;
            }
            String tag = (args.length > 2) ? r.getFinalArg(args, 2) : p.getName();
            try {
                tag = URLEncoder.encode(tag, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                cs.sendMessage(r.default1 + "Tell the developer enc1.");
                return;
            }
            if (p.getDescription() == null) {
                cs.sendMessage(r.default1 + "Plugin has no description!");
                return;
            }
            String version = p.getDescription().getVersion();
            if (version == null) {
                cs.sendMessage(r.default1 + "Plugin has not set a version!");
                return;
            }
            try {
                String checked = updateCheck(tag, version);
                cs.sendMessage(r.default1 + "Current version is " + r.default2 + version + r.default1 + "; newest version is " + r.default2 + checked + r.default1 + ".");
            } catch (Exception e) {
                cs.sendMessage(r.default1 + "Could not check for update!");
            }
            return;
        } else if (subcmd.equalsIgnoreCase("findtag") || subcmd.equalsIgnoreCase("search")) {
        	if(!r.perm(cs, "uc.plugin.search", false, false) && !r.perm(cs, "uc.plugin", false, false)){
        		cs.sendMessage(r.mes("NoPermissions")); return;
        	}
            if (args.length < 2) {
                cs.sendMessage(r.default1 + "Please specify a search term!");
                return;
            }
            final Runnable run = new Runnable() {
                @Override
                public void run() {
            int page = 1;
            if (args.length > 2) {
                try {
                    page = Integer.parseInt(args[args.length - 1]);
                } catch (NumberFormatException ignored) {
                    page = 1;
                }
            }
            final String search;
            try {
                search = URLEncoder.encode(r.getFinalArg(args, 1), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                cs.sendMessage(r.default1 + "Tell the developer enc1.");
                return;
            }
            final URL u;
            try {
                u = new URL("http://dev.bukkit.org/search/?scope=projects&search=" + search + "&page=" + page);
            } catch (MalformedURLException e) {
                cs.sendMessage(r.default1 + "Malformed search term!");
                return;
            }
            final BufferedReader br;
            try {
                br = new BufferedReader(new InputStreamReader(u.openStream()));
            } catch (IOException e) {
                cs.sendMessage(r.default1 + "Internal input/output error. Please try again.");
                return;
            }
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    try {
                        while ((inputLine = br.readLine()) != null) content.append(inputLine);
                    } catch (IOException e) {
                        cs.sendMessage(r.default1 + "Internal input/output error. Please try again.");
                        return;
                    }
                    cs.sendMessage(ChatColor.GOLD + "   Project name - Project tag   ");
                    for (int i = 0; i < 20; i++) {
                        String project = StringUtils.substringBetween(content.toString(), " row-joined-to-next\">", "</tr>");
                        String base = StringUtils.substringBetween(project, "<td class=\"col-search-entry\">", "</td>");
                        if (base == null) {
                            if (i == 0) cs.sendMessage(r.default1 + "No results found.");
                            return;
                        }
                        Pattern p = Pattern.compile("<h2><a href=\"/bukkit-plugins/([\\W\\w]+)/\">([\\w\\W]+)</a></h2>");
                        Matcher m = p.matcher(base);
                        if (!m.find()) {
                            if (i == 0) cs.sendMessage(r.default1 + "No results found.");
                            return;
                        }
                        String name = m.group(2).replaceAll("</?\\w+>", "");
                        String tag = m.group(1);
                        int beglen = StringUtils.substringBefore(content.toString(), base).length();
                        content = new StringBuilder(content.substring(beglen + project.length()));
                        cs.sendMessage(r.default1 + name + r.default2 + " - " + tag);
                    }
                }
            };
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, run);
            return;
        } else if (subcmd.equalsIgnoreCase("delete")) {
        	if(!r.perm(cs, "uc.plugin.delete", false, false) && !r.perm(cs, "uc.plugin", false, false)){
        		cs.sendMessage(r.mes("NoPermissions")); return;
        	}
            if (args.length < 2) {
                cs.sendMessage(r.default1 + "Please specify the filename to delete!");
                return;
            }
            String toDelete = args[1];
            if (!toDelete.endsWith(".jar")) {
                cs.sendMessage(r.default1 + "Please only specify jar files!");
                return;
            }
            if (toDelete.contains(File.separator)) {
                cs.sendMessage(r.default1 + "Please don't try to leave the plugins directory!");
                return;
            }
            File f = new File(plugin.getDataFolder().getParentFile() + File.separator + toDelete);
            if (!f.exists()) {
                cs.sendMessage(r.default1 + "No such file!");
                return;
            }
            boolean success = f.delete();
            if (!success)
                cs.sendMessage(r.default1 + "Could not delete " + r.default2 + f.getName() + r.default1 + ".");
            else
                cs.sendMessage(r.default1 + "Deleted " + r.default2 + f.getName() + r.default1 + ".");
            return;
        } else {
            cs.sendMessage(r.default1 + "Unknown subcommand!");
            cs.sendMessage(r.default1 + "Try " + r.default2 + "/" + label + " help");
            return;
        }
    }
    private static Object getPrivateField(Object object, String field) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field objectField = clazz.getDeclaredField(field);
        final boolean wasAccessible = objectField.isAccessible();
        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(wasAccessible);
        return result;
    }
    private static List<File> listFiles(File f, boolean recursive) {
        List<File> fs = new ArrayList<>();
        if (!f.isDirectory()) return fs;
        File[] listed = f.listFiles();
        if (listed == null) return fs;
        for (File in : listed) {
            if (in.isDirectory()) {
                if (!recursive) continue;
                fs.addAll(listFiles(in, true));
                continue;
            }
            fs.add(in);
        }
        return fs;
    }
    private static boolean deleteDirectory(File f) {
        boolean success = true;
        if (!f.isDirectory()) return false;
        File[] files = f.listFiles();
        if (files == null) return false;
        for (File delete : files) {
            if (delete.isDirectory()) {
                boolean recur = deleteDirectory(delete);
                if (success) success = recur;
                continue;
            }
            if (!delete.delete()) {
                success = false;
            }
        }
        if (success) success = f.delete();
        return success;
    }
}
class UnZip {
    private final static int BUFFER = 2048;

    /**
     * Decompresses a zipped file - respects directories
     *
     * @param fileName          File name of the zipped file
     * @param destinationFolder Folder to unzip in
     */
    public static void decompress(String fileName, String destinationFolder) {
        BufferedOutputStream dest = null;
        BufferedInputStream is = null;
        try {
            ZipEntry entry;
            ZipFile zipfile = new ZipFile(fileName);
            Enumeration<? extends ZipEntry> e = zipfile.entries();
            while (e.hasMoreElements()) {
                entry = (ZipEntry) e.nextElement();
                if (entry.isDirectory()) {
                    new File(destinationFolder + File.separator + entry.getName()).mkdir();
                    continue;
                }
                is = new BufferedInputStream(zipfile.getInputStream(entry));
                int count;
                byte data[] = new byte[BUFFER];
                File f = new File(destinationFolder + File.separator + entry.getName());
                if (!f.exists()) f.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(destinationFolder + File.separator + entry.getName());
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = is.read(data, 0, BUFFER)) != -1)
                    dest.write(data, 0, count);
                dest.flush();
                dest.close();
                is.close();
            }
            zipfile.close();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        } finally {
            try {
                if (dest != null) dest.close();
                if (is != null) is.close();
            } catch (IOException ignore) {
            }
        }
    }
}
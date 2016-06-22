/*
 * This file is part of UltimateCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) Bammerbom
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package bammerbom.ultimatecore.bukkit.commands;

import bammerbom.ultimatecore.bukkit.UltimateCommand;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.bukkit.resources.utils.PluginUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.StringUtil;
import com.google.common.io.Files;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmdPlugin implements UltimateCommand {

    static PluginManager pm = Bukkit.getPluginManager();

    @Override
    public String getName() {
        return "plugin";
    }

    @Override
    public String getPermission() {
        return "uc.plugin";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, final String[] args) {
        //help
        if (!r.checkArgs(args, 0) || args[0].equalsIgnoreCase("help")) {
            if (!r.perm(cs, "uc.plugin", false, false) && !r.perm(cs, "uc.plugin.help", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            cs.sendMessage(ChatColor.GOLD + "================================");
            r.sendMes(cs, "pluginHelpLoad");
            r.sendMes(cs, "pluginHelpUnload");
            r.sendMes(cs, "pluginHelpEnable");
            r.sendMes(cs, "pluginHelpDisable");
            r.sendMes(cs, "pluginHelpReload");
            r.sendMes(cs, "pluginHelpReloadall");
            r.sendMes(cs, "pluginHelpDelete");
            r.sendMes(cs, "pluginHelpCommands");
            r.sendMes(cs, "pluginHelpList");
            r.sendMes(cs, "pluginHelpUpdatecheck");
            r.sendMes(cs, "pluginHelpUpdatecheckall");
            r.sendMes(cs, "pluginHelpDownload");
            r.sendMes(cs, "pluginHelpSearch");
            cs.sendMessage(ChatColor.GOLD + "================================");
        } //load
        else if (args[0].equalsIgnoreCase("load")) {
            if (!r.perm(cs, "uc.plugin", false, false) && !r.perm(cs, "uc.plugin.load", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "pluginHelpLoad");
                return;
            }
            File f = new File(r.getUC().getDataFolder().getParentFile(), args[1].endsWith(".jar") ? args[1] : args[1] + ".jar");
            if (!f.exists()) {
                r.sendMes(cs, "pluginFileNotFound", "%File", args[1].endsWith(".jar") ? args[1] : args[1] + ".jar");
                return;
            }
            if (!f.canRead()) {
                r.sendMes(cs, "pluginFileNoReadAccess");
                return;
            }
            Plugin p;
            if (PluginUtil.getPluginByName(args[1].replace("\\.jar", "")) != null) {
                r.sendMes(cs, "pluginAlreadyEnabled");
                return;
            }
            try {
                p = pm.loadPlugin(f);
                if (p == null) {
                    r.sendMes(cs, "pluginLoadFailed");
                    return;
                }
                p.onLoad();
                pm.enablePlugin(p);
            } catch (UnknownDependencyException ex) {
                r.sendMes(cs, "pluginLoadMissingDependency", "%Message", ex.getMessage() != null ? ex.getMessage() : "");
                ex.printStackTrace();
                return;
            } catch (InvalidDescriptionException ex) {
                r.sendMes(cs, "pluginLoadInvalidDescription");
                ex.printStackTrace();
                return;
            } catch (InvalidPluginException ex) {
                r.sendMes(cs, "pluginLoadFailed");
                ex.printStackTrace();
                return;
            }
            if (p.isEnabled()) {
                r.sendMes(cs, "pluginLoadSucces");
            } else {
                r.sendMes(cs, "pluginLoadFailed");
            }
        } //unload
        else if (args[0].equalsIgnoreCase("unload")) {
            if (!r.perm(cs, "uc.plugin", false, false) && !r.perm(cs, "uc.plugin.unload", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "pluginHelpUnload");
                return;
            }
            Plugin p = pm.getPlugin(args[1]);
            if (p == null) {
                r.sendMes(cs, "pluginNotFound", "%Plugin", args[1]);
                return;
            }
            List<String> deps = PluginUtil.getSoftDependedOnBy(p.getName());
            if (!deps.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String dep : deps) {
                    sb.append(r.neutral);
                    sb.append(dep);
                    sb.append(ChatColor.RESET);
                    sb.append(", ");
                }
                r.sendMes(cs, "pluginUnloadDependent", "%Plugins", sb.substring(0, sb.length() - 4));
                return;
            }
            r.sendMes(cs, "pluginUnloadUnloading");
            if (PluginUtil.unload(p)) {
                r.sendMes(cs, "pluginUnloadUnloaded");
            } else {
                r.sendMes(cs, "pluginUnloadUnloadFail");
            }
        } //enable
        else if (args[0].equalsIgnoreCase("enable")) {
            if (!r.perm(cs, "uc.plugin", false, false) && !r.perm(cs, "uc.plugin.enable", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "pluginHelpEnable");
                return;
            }
            Plugin p = pm.getPlugin(args[1]);
            if (p == null) {
                r.sendMes(cs, "pluginNotFound", "%Plugin", args[1]);
                return;
            }
            if (p.isEnabled()) {
                r.sendMes(cs, "pluginAlreadyEnabled");
                return;
            }
            pm.enablePlugin(p);
            if (p.isEnabled()) {
                r.sendMes(cs, "pluginEnableSucces");
            } else {
                r.sendMes(cs, "pluginEnableFail");
            }
        } //disable
        else if (args[0].equalsIgnoreCase("disable")) {
            if (!r.perm(cs, "uc.plugin", false, false) && !r.perm(cs, "uc.plugin.disable", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "pluginHelpDisable");
                return;
            }
            Plugin p = pm.getPlugin(args[1]);
            if (p == null) {
                r.sendMes(cs, "pluginNotFound", "%Plugin", args[1]);
                return;
            }
            if (!p.isEnabled()) {
                r.sendMes(cs, "pluginNotEnabled");
                return;
            }
            List<String> deps = PluginUtil.getSoftDependedOnBy(p.getName());
            if (!deps.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String dep : deps) {
                    sb.append(r.neutral);
                    sb.append(dep);
                    sb.append(ChatColor.RESET);
                    sb.append(", ");
                }
                r.sendMes(cs, "pluginUnloadDependent", "%Plugins", sb.substring(0, sb.length() - 4));
                return;
            }
            pm.disablePlugin(p);
            if (!p.isEnabled()) {
                r.sendMes(cs, "pluginDisableSucces");
            } else {
                r.sendMes(cs, "pluginDisableFailed");
            }
        } //reload
        else if (args[0].equalsIgnoreCase("reload")) {
            if (!r.perm(cs, "uc.plugin", false, false) && !r.perm(cs, "uc.plugin.reload", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "pluginHelpReload");
                return;
            }
            Plugin p = pm.getPlugin(args[1]);
            if (p == null) {
                r.sendMes(cs, "pluginNotFound", "%Plugin", args[1]);
                return;
            }
            if (!p.isEnabled()) {
                r.sendMes(cs, "pluginNotEnabled");
                return;
            }
            pm.disablePlugin(p);
            pm.enablePlugin(p);
            r.sendMes(cs, "pluginReloadMessage");
        } //reloadall
        else if (args[0].equalsIgnoreCase("reloadall")) {
            if (!r.perm(cs, "uc.plugin", false, false) && !r.perm(cs, "uc.plugin.reloadall", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            Bukkit.getServer().reload();
            r.sendMes(cs, "pluginReloadallMessage");
        } //delete
        else if (args[0].equalsIgnoreCase("delete")) {
            if (!r.perm(cs, "uc.plugin", false, false) && !r.perm(cs, "uc.plugin.delete", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "pluginHelpDelete");
                return;
            }
            String del = args[1];
            if (!del.endsWith(".jar")) {
                del = del + ".jar";
            }
            if (del.contains(File.separator)) {
                r.sendMes(cs, "pluginDeleteDontLeavePluginFolder");
                return;
            }
            File f = new File(r.getUC().getDataFolder().getParentFile() + File.separator + del);
            if (!f.exists()) {
                r.sendMes(cs, "pluginFileNotFound", "%File", del);
                return;
            }
            if (!f.canWrite()) {
                r.sendMes(cs, "pluginFileNoWriteAccess");
            }
            if (f.delete()) {
                r.sendMes(cs, "pluginDeleteSucces");
            } else {
                r.sendMes(cs, "pluginDeleteFailed");
            }
        } //commands
        else if (args[0].equalsIgnoreCase("commands")) {
            if (!r.perm(cs, "uc.plugin", false, false) && !r.perm(cs, "uc.plugin.commands", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "pluginHelpCommands");
                return;
            }
            Plugin p = pm.getPlugin(args[1]);
            if (p == null) {
                r.sendMes(cs, "pluginNotFound", "%Plugin", args[1]);
                return;
            }
            Map<String, Map<String, Object>> cmds = p.getDescription().getCommands();
            if (cmds == null) {
                r.sendMes(cs, "pluginCommandsNoneRegistered");
                return;
            }
            String command = "plugin " + p.getName();
            String pageStr = args.length > 2 ? args[2] : null;
            UText input = new TextInput(cs);
            UText output;
            if (input.getLines().isEmpty()) {
                if ((r.isInt(pageStr)) || (pageStr == null)) {
                    output = new PluginCommandsInput(cs, args[1].toLowerCase());
                } else {
                    r.sendMes(cs, "pluginCommandsPageNotNumber");
                    return;
                }
            } else {
                output = input;
            }
            TextPager pager = new TextPager(output);
            pager.showPage(pageStr, null, command, cs);
        } //list
        else if (args[0].equalsIgnoreCase("list")) {
            if (!r.perm(cs, "uc.plugin", false, false) && !r.perm(cs, "uc.plugin.list", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            r.sendMes(cs, "pluginsList", "%Plugins", PluginUtil.getPluginList());
        } //info
        else if (args[0].equalsIgnoreCase("info")) {
            if (!r.perm(cs, "uc.plugin.info", false, false) && !r.perm(cs, "uc.plugin", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "pluginHelpInfo");
                return;
            }
            Plugin p = pm.getPlugin(args[1]);
            if (p == null) {
                r.sendMes(cs, "pluginNotFound", "%Plugin", args[1]);
                return;
            }
            PluginDescriptionFile pdf = p.getDescription();
            if (pdf == null) {
                r.sendMes(cs, "pluginLoadInvalidDescription");
                return;
            }
            String version = pdf.getVersion();
            List<String> authors = pdf.getAuthors();
            String site = pdf.getWebsite();
            List<String> softDep = pdf.getSoftDepend();
            List<String> dep = pdf.getDepend();
            String name = pdf.getName();
            String desc = pdf.getDescription();
            if (name != null && !name.isEmpty()) {
                r.sendMes(cs, "pluginInfoName", "%Name", name);
            }
            if (version != null && !version.isEmpty()) {
                r.sendMes(cs, "pluginInfoVersion", "%Version", version);
            }
            if (site != null && !site.isEmpty()) {
                r.sendMes(cs, "pluginInfoWebsite", "%Website", site);
            }
            if (desc != null && !desc.isEmpty()) {
                r.sendMes(cs, "pluginInfoDescription", "%Description", desc.replaceAll("\r?\n", ""));
            }
            if (authors != null && !authors.isEmpty()) {
                r.sendMes(cs, "pluginInfoAuthor", "%S", ((authors.size() > 1) ? "s" : ""), "%Author", StringUtil.join(ChatColor.RESET + ", " + r.neutral, authors));
            }
            if (softDep != null && !softDep.isEmpty()) {
                r.sendMes(cs, "pluginInfoSoftdeps", "%Softdeps", StringUtil.join(ChatColor.RESET + ", " + r.neutral, softDep));
            }
            if (dep != null && !dep.isEmpty()) {
                r.sendMes(cs, "pluginInfoDeps", "%Deps", StringUtil.join(ChatColor.RESET + ", " + r.neutral, dep));
            }
            r.sendMes(cs, "pluginInfoEnabled", "%Enabled", ((p.isEnabled()) ? r.mes("yes") : r.mes("no")));
        } //updatecheck
        else if (args[0].equalsIgnoreCase("updatecheck")) {
            if (!r.perm(cs, "uc.plugin.updatecheck", false, false) && !r.perm(cs, "uc.plugin", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "pluginHelpUpdatecheck");
                return;
            }
            Plugin p = pm.getPlugin(args[1]);
            if (p == null) {
                r.sendMes(cs, "pluginNotFound", "%Plugin", args[1]);
                return;
            }
            String tag;
            try {
                tag = URLEncoder.encode(r.checkArgs(args, 2) ? args[2] : p.getName(), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                r.sendMes(cs, "pluginNoUTF8");
                return;
            }
            if (p.getDescription() == null) {
                r.sendMes(cs, "pluginLoadInvalidDescription");
                return;
            }
            if (p.getDescription().getWebsite().startsWith("http://dev.bukkit.org/bukkit-plugins/")) {
                tag = p.getDescription().getWebsite().split("dev.bukkit.org/bukkit-plugins/")[1].split("/")[0];
            }
            final String ftag = tag;
            final String v = p.getDescription().getVersion() == null ? r.mes("pluginNotSet") : p.getDescription().getVersion();
            Runnable ru = new Runnable() {
                @Override
                public void run() {
                    try {
                        String n = "";
                        String pluginUrlString = "http://dev.bukkit.org/bukkit-plugins/" + ftag + "/files.rss";
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
                            n = firstNodes.item(0).getNodeValue();
                        }

                        r.sendMes(cs, "pluginUpdatecheckCurrent", "%Current", v + "");
                        r.sendMes(cs, "pluginUpdatecheckNew", "%New", n + "");
                    } catch (Exception ex) {
                        ErrorLogger.log(ex, "Update check failed.");
                        r.sendMes(cs, "pluginUpdatecheckFailed");
                    }
                }
            };
            Bukkit.getServer().getScheduler().runTaskAsynchronously(r.getUC(), ru);
        } //updatecheckall
        else if (args[0].equalsIgnoreCase("updatecheckall")) {
            if (!r.perm(cs, "uc.plugin.updatecheckall", false, false) && !r.perm(cs, "uc.plugin", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            final Runnable ru = new Runnable() {
                @Override
                public void run() {
                    int a = 0;
                    for (Plugin p : pm.getPlugins()) {
                        if (p.getDescription() == null) {
                            continue;
                        }
                        String version = p.getDescription().getVersion();
                        if (version == null) {
                            continue;
                        }
                        String n = "";
                        try {
                            String tag = p.getName().toLowerCase();
                            if (p.getDescription().getWebsite().startsWith("http://dev.bukkit.org/bukkit-plugins/") || p.getDescription().getWebsite().startsWith("dev.bukkit.org/bukkit-plugins/")) {
                                tag = p.getDescription().getWebsite().split("dev\\.bukkit\\.org/bukkit-plugins/")[1].split("/")[0];
                            }
                            String pluginUrlString = "http://dev.bukkit.org/bukkit-plugins/" + p.getName().toLowerCase() + "/files.rss";
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
                                n = firstNodes.item(0).getNodeValue();
                            }
                        } catch (Exception e) {
                            continue;
                        }
                        if (PluginUtil.isSameVersion(n, version)) {
                            continue;
                        }
                        r.sendMes(cs, "pluginUpdatecheckallAvailable", "%Old", version, "%New", n, "%Plugin", p.getName());
                        a++;
                    }
                    r.sendMes(cs, "pluginUpdatecheckallFinish", "%Amount", a);
                }
            };
            Bukkit.getServer().getScheduler().runTaskAsynchronously(r.getUC(), ru);
        } //download
        else if (args[0].equalsIgnoreCase("download")) {
            if (!r.perm(cs, "uc.plugin.download", false, false) && !r.perm(cs, "uc.plugin", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "pluginHelpDownload");
                cs.sendMessage(r.negative + "http://dev.bukkit.org/server-mods/" + r.neutral + "ultimate_core" + r.negative + "/");
                return;
            }
            final Runnable ru = new Runnable() {
                @Override
                public void run() {
                    String tag = args[1];
                    r.sendMes(cs, "pluginDownloadGettingtag");
                    String pluginUrlString = "http://dev.bukkit.org/server-mods/" + tag + "/files.rss";
                    String file;
                    try {
                        final URL url = new URL(pluginUrlString);
                        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openConnection().getInputStream());
                        doc.getDocumentElement().normalize();
                        final NodeList nodes = doc.getElementsByTagName("item");
                        final Node firstNode = nodes.item(0);
                        if (firstNode.getNodeType() == 1) {
                            final Element firstElement = (Element) firstNode;
                            final NodeList firstElementTagName = firstElement.getElementsByTagName("link");
                            final Element firstNameElement = (Element) firstElementTagName.item(0);
                            final NodeList firstNodes = firstNameElement.getChildNodes();
                            final String link = firstNodes.item(0).getNodeValue();
                            final URL dpage = new URL(link);
                            final BufferedReader br = new BufferedReader(new InputStreamReader(dpage.openStream()));
                            final StringBuilder content = new StringBuilder();
                            String inputLine;
                            while ((inputLine = br.readLine()) != null) {
                                content.append(inputLine);
                            }
                            br.close();
                            file = StringUtils.substringBetween(content.toString(), "<li class=\"user-action " + "user-action-download\"><span><a href=\"", "\">Download</a></span></li>");
                        } else {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        r.sendMes(cs, "pluginDownloadInvalidtag");
                        cs.sendMessage(r.negative + "http://dev.bukkit.org/server-mods/" + r.neutral +
                                "ultimate_core" + r.negative + "/");
                        return;
                    }
                    BufferedInputStream bis;
                    final HttpURLConnection huc;
                    try {
                        huc = (HttpURLConnection) new URL(file).openConnection();
                        huc.setInstanceFollowRedirects(true);
                        huc.connect();
                        bis = new BufferedInputStream(huc.getInputStream());
                    } catch (MalformedURLException e) {
                        r.sendMes(cs, "pluginDownloadInvaliddownloadlink");
                        return;
                    } catch (IOException e) {
                        r.sendMes(cs, "pluginDownloadFailed", "%Message", e.getMessage());
                        return;
                    }
                    String[] urlParts = huc.getURL().toString().split("(\\\\|/)");
                    final String fileName = urlParts[urlParts.length - 1];
                    r.sendMes(cs, "pluginDownloadCreatingTemp");
                    File f = new File(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString() + File.separator + fileName);
                    while (f.getParentFile().exists()) {
                        f = new File(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString() + File.separator + fileName);
                    }
                    if (!fileName.endsWith(".zip") && !fileName.endsWith(".jar")) {
                        r.sendMes(cs, "pluginDownloadNotJarOrZip", "%Filename", fileName);
                        return;
                    }
                    f.getParentFile().mkdirs();
                    BufferedOutputStream bos;
                    try {
                        bos = new BufferedOutputStream(new FileOutputStream(f));
                    } catch (FileNotFoundException e) {
                        r.sendMes(cs, "pluginDownloadTempNotFound", "%Dir", System.getProperty("java.io.tmpdir"));
                        return;
                    }
                    int b;
                    r.sendMes(cs, "pluginDownloadDownloading");
                    try {
                        try {
                            while ((b = bis.read()) != -1) {
                                bos.write(b);
                            }
                        } finally {
                            bos.flush();
                            bos.close();
                        }
                    } catch (IOException e) {
                        r.sendMes(cs, "pluginDownloadFailed", "%Message", e.getMessage());
                        return;
                    }
                    if (fileName.endsWith(".zip")) {
                        r.sendMes(cs, "pluginDownloadDecompressing");
                        PluginUtil.decompress(f.getAbsolutePath(), f.getParent());

                    }
                    String name = null;
                    for (File fi : PluginUtil.listFiles(f.getParentFile())) {
                        if (!fi.getName().endsWith(".jar")) {
                            continue;
                        }
                        if (name == null) {
                            name = fi.getName();
                        }
                        r.sendMes(cs, "pluginDownloadMoving", "%File", fi.getName());
                        try {
                            Files.move(fi, new File(r.getUC().getDataFolder().getParentFile() + File.separator + fi.getName()));
                        } catch (IOException e) {
                            r.sendMes(cs, "pluginDownloadCouldntMove", "%Message", e.getMessage());
                        }
                    }
                    PluginUtil.deleteDirectory(f.getParentFile());
                    r.sendMes(cs, "pluginDownloadSucces", "%File", fileName);
                }
            };
            Bukkit.getServer().getScheduler().runTaskAsynchronously(r.getUC(), ru);
        } else if (args[0].equalsIgnoreCase("search")) {
            if (!r.perm(cs, "uc.plugin.search", false, false) && !r.perm(cs, "uc.plugin", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            int page = 1;
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "pluginHelpSearch");
                return;
            }
            Boolean b = false;
            if (r.checkArgs(args, 2)) {
                try {
                    page = Integer.parseInt(args[args.length - 1]);
                    b = true;
                } catch (NumberFormatException ignored) {
                }
            }
            String search = r.getFinalArg(args, 1);
            if (b) {
                search = new StringBuilder(new StringBuilder(search).reverse().toString().replaceFirst(new StringBuilder(" " + page).reverse().toString(), "")).reverse().toString();
            }
            try {
                search = URLEncoder.encode(search, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                r.sendMes(cs, "pluginNoUTF8");
                return;
            }
            final URL u;
            try {
                u = new URL("http://dev.bukkit.org/search/?scope=projects&search=" + search + "&page=" + page);
            } catch (MalformedURLException e) {
                r.sendMes(cs, "pluginSearchMalformedTerm");
                return;
            }
            final Runnable ru = new Runnable() {
                @Override
                public void run() {
                    final BufferedReader br;
                    try {
                        br = new BufferedReader(new InputStreamReader(u.openStream()));
                    } catch (IOException e) {
                        r.sendMes(cs, "pluginSearchFailed", "%Message", e.getMessage());
                        return;
                    }
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    try {
                        while ((inputLine = br.readLine()) != null) {
                            content.append(inputLine);
                        }
                    } catch (IOException e) {
                        r.sendMes(cs, "pluginSearchFailed", "%Message", e.getMessage());
                        return;
                    }
                    r.sendMes(cs, "pluginSearchHeader");
                    for (int i = 0;
                         i < 20;
                         i++) {
                        final String project = StringUtils.substringBetween(content.toString(), " " + "row-joined-to-next\">", "</tr>");
                        final String base = StringUtils.substringBetween(project, "<td class=\"col-search-entry\">", "</td>");
                        if (base == null) {
                            if (i == 0) {
                                r.sendMes(cs, "pluginSearchNoResults");
                            }
                            return;
                        }
                        final Pattern p = Pattern.compile("<h2><a href=\"/bukkit-plugins/([\\W\\w]+)/\">([\\w\\W]+)" + "</a></h2>");
                        final Matcher m = p.matcher(base);
                        if (!m.find()) {
                            if (i == 0) {
                                r.sendMes(cs, "pluginSearchNoResults");
                            }
                            return;
                        }
                        final String name = m.group(2).replaceAll("</?\\w+>", "");
                        final String tag = m.group(1);
                        final int beglen = StringUtils.substringBefore(content.toString(), base).length();
                        content = new StringBuilder(content.substring(beglen + project.length()));
                        r.sendMes(cs, "pluginSearchResult", "%Name", name, "%Tag", tag);
                    }
                }
            };
            Bukkit.getServer().getScheduler().runTaskAsynchronously(r.getUC(), ru);
        } else {
            cs.sendMessage(ChatColor.GOLD + "================================");
            r.sendMes(cs, "pluginHelpLoad");
            r.sendMes(cs, "pluginHelpUnload");
            r.sendMes(cs, "pluginHelpEnable");
            r.sendMes(cs, "pluginHelpDisable");
            r.sendMes(cs, "pluginHelpReload");
            r.sendMes(cs, "pluginHelpReloadall");
            r.sendMes(cs, "pluginHelpDelete");
            r.sendMes(cs, "pluginHelpCommands");
            r.sendMes(cs, "pluginHelpList");
            r.sendMes(cs, "pluginHelpUpdatecheck");
            r.sendMes(cs, "pluginHelpUpdatecheckall");
            r.sendMes(cs, "pluginHelpDownload");
            r.sendMes(cs, "pluginHelpSearch");
            cs.sendMessage(ChatColor.GOLD + "================================");
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}

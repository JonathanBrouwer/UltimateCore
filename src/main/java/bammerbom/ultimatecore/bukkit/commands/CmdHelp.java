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

import bammerbom.ultimatecore.bukkit.r;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.*;

abstract interface UText {

    public abstract List<String> getLines();

    public abstract List<String> getChapters();

    public abstract Map<String, Integer> getBookmarks();
}

public class CmdHelp implements UltimateCommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getPermission() {
        return "uc.help";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        String command = label;
        String pageStr = args.length > 0 ? args[0] : null;
        String chapterPageStr = args.length > 1 ? args[1] : null;
        UText input = new TextInput(cs);
        UText output;
        if (input.getLines().isEmpty()) {
            if ((r.isInt(pageStr)) || (pageStr == null)) {
                output = new HelpInput(cs, "");
            } else {
                output = new HelpInput(cs, pageStr.toLowerCase(Locale.ENGLISH));
                command = command.concat(" ").concat(pageStr);
                pageStr = chapterPageStr;
            }
            chapterPageStr = null;
        } else {
            output = input;
        }
        TextPager pager = new TextPager(output);
        pager.showPage(pageStr, chapterPageStr, command, cs);
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}

//Textinput
class TextInput implements UText {

    private final transient List<String> lines;
    private final transient List<String> chapters;
    private final transient Map<String, Integer> bookmarks;

    public TextInput(CommandSender sender) {
        this.lines = Collections.emptyList();
        this.chapters = Collections.emptyList();
        this.bookmarks = Collections.emptyMap();
    }

    @Override
    public List<String> getLines() {
        return this.lines;
    }

    @Override
    public List<String> getChapters() {
        return this.chapters;
    }

    @Override
    public Map<String, Integer> getBookmarks() {
        return this.bookmarks;
    }
}

class HelpInput
        implements UText {

    private final transient List<String> lines = new ArrayList<>();
    private final transient List<String> chapters = new ArrayList<>();
    private final transient Map<String, Integer> bookmarks = new HashMap<>();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public HelpInput(CommandSender user, String match) {
        boolean reported = false;
        List newLines = new ArrayList();
        String pluginName = "";
        String pluginNameLow = "";

        for (Plugin p : Bukkit.getServer().getPluginManager().getPlugins()) {
            try {
                List pluginLines = new ArrayList();
                PluginDescriptionFile desc = p.getDescription();
                Map<String, Map<String, Object>> cmds = desc.getCommands();
                pluginName = p.getDescription().getName();
                pluginNameLow = pluginName.toLowerCase(Locale.ENGLISH);
                if (pluginNameLow.equals(match)) {
                    this.lines.clear();
                    newLines.clear();
                }

                for (Map.Entry k : cmds.entrySet()) {
                    try {
                        if ((pluginNameLow.contains(match)) || (((String) k.getKey()).toLowerCase(Locale.ENGLISH).contains(match))) {
                            Map value = (Map) k.getValue();
                            Object permissions = null;
                            if (value.containsKey("permission")) {
                                permissions = value.get("permission");
                            } else if (value.containsKey("permissions")) {
                                permissions = value.get("permissions");
                            }
                            if (permissions == null || r.perm(user, permissions.toString(), false, false)) {
                                pluginLines.add(r.mes("helpCommand", "%Command", "/" + k.getKey(), "%Description", value.get("description")));
                            }
                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                if (!pluginLines.isEmpty()) {
                    newLines.addAll(pluginLines);
                    if (pluginNameLow.equals(match)) {
                        break;
                    }
                    if (match.equalsIgnoreCase("")) {
                        lines.add(r.mes("helpPlugin", "%Enabled", (p.isEnabled() ? ChatColor.DARK_GREEN : ChatColor.RED), "%Name", pluginName, "%Lowname", pluginNameLow));
                    }
                }
            } catch (NullPointerException ex) {
            } catch (Exception ex) {
                if (!reported) {
                }
                reported = true;
            }
        }
        this.lines.addAll(newLines);
    }

    @Override
    public List<String> getLines() {
        return this.lines;
    }

    @Override
    public List<String> getChapters() {
        return this.chapters;
    }

    @Override
    public Map<String, Integer> getBookmarks() {
        return this.bookmarks;
    }
}

class PluginCommandsInput
        implements UText {

    private final transient List<String> lines = new ArrayList<>();
    private final transient List<String> chapters = new ArrayList<>();
    private final transient Map<String, Integer> bookmarks = new HashMap<>();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public PluginCommandsInput(CommandSender user, String match) {
        boolean reported = false;
        List newLines = new ArrayList();
        String pluginName;
        String pluginNameLow;

        for (Plugin p : Bukkit.getServer().getPluginManager().getPlugins()) {
            try {
                List pluginLines = new ArrayList();
                PluginDescriptionFile desc = p.getDescription();
                Map<String, Map<String, Object>> cmds = desc.getCommands();
                pluginName = p.getDescription().getName();
                pluginNameLow = pluginName.toLowerCase(Locale.ENGLISH);
                if (pluginNameLow.equals(match)) {
                    this.lines.clear();
                    newLines.clear();
                }

                for (Map.Entry k : cmds.entrySet()) {
                    try {
                        if ((pluginNameLow.contains(match)) || (((String) k.getKey()).toLowerCase(Locale.ENGLISH).contains(match))) {
                            Map value = (Map) k.getValue();
                            Object permissions = null;
                            if (value.containsKey("permission")) {
                                permissions = value.get("permission");
                            } else if (value.containsKey("permissions")) {
                                permissions = value.get("permissions");
                            }
                            if (permissions == null || r.perm(user, permissions.toString(), false, false)) {
                                pluginLines.add(r.mes("helpCommand", "%Command", "/" + k.getKey(), "%Description", value.get("description")));
                            }
                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                if (!pluginLines.isEmpty()) {
                    newLines.addAll(pluginLines);
                    if (pluginNameLow.equals(match)) {
                        break;
                    }
                    if (match.equalsIgnoreCase("")) {
                        lines.add(r.mes("helpPlugin", "%Enabled", (p.isEnabled() ? ChatColor.DARK_GREEN : ChatColor.RED), "%Name", pluginName, "%Lowname", pluginNameLow));
                    }
                }
            } catch (NullPointerException ex) {
            } catch (Exception ex) {
                if (!reported) {
                }
                reported = true;
            }
        }
        this.lines.addAll(newLines);
    }

    @Override
    public List<String> getLines() {
        return this.lines;
    }

    @Override
    public List<String> getChapters() {
        return this.chapters;
    }

    @Override
    public Map<String, Integer> getBookmarks() {
        return this.bookmarks;
    }
}

class TextPager {

    private final transient UText text;
    private final transient boolean onePage;

    public TextPager(UText text) {
        this(text, false);
    }

    public TextPager(UText text, boolean onePage) {
        this.text = text;
        this.onePage = onePage;
    }

    private static String capitalCase(String input) {
        return input.toUpperCase(Locale.ENGLISH).charAt(0) + input.toLowerCase(Locale.ENGLISH).substring(1);
    }

    public void showPage(String pageStr, String chapterPageStr, String commandName, CommandSender sender) {
        List<String> lines = this.text.getLines();
        List<String> chapters = this.text.getChapters();
        Map<String, Integer> bookmarks = this.text.getBookmarks();

        if ((pageStr == null) || (pageStr.isEmpty()) || (pageStr.matches("[0-9]+"))) {
            if ((!lines.isEmpty()) && (((String) lines.get(0)).startsWith("#"))) {
                if (this.onePage) {
                    return;
                }
                r.sendMes(sender, "helpSelectChapter");
                StringBuilder sb = new StringBuilder();
                boolean first = true;
                for (String string : chapters) {
                    if (!first) {
                        sb.append(", ");
                    }
                    first = false;
                    sb.append(string);
                }
                sender.sendMessage(sb.toString());
                return;
            }

            int page = 1;
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException ex) {
                page = 1;
            }
            if (page < 1) {
                page = 1;
            }

            int start = this.onePage ? 0 : (page - 1) * 9;
            int end;
            for (end = 0; end < lines.size(); end++) {
                String line = (String) lines.get(end);
                if (line.startsWith("#")) {
                    break;
                }
            }

            int pages = end / 9 + (end % 9 > 0 ? 1 : 0);
            String search = "";
            if ((!this.onePage) && (commandName != null)) {
                StringBuilder content = new StringBuilder();
                String[] title = commandName.split(" ", 2);
                if (title.length > 1) {
                    content.append(capitalCase(title[0])).append(": ");
                    content.append(title[1]);
                    search = title[1] + " ";
                } else {
                    content.append(capitalCase(commandName));
                }
                r.sendMes(sender, "helpHeader", "%Content", content, "%Page", page, "%MaxPages", pages);
            }
            for (int i = start; i < end; i++) {
                if (i >= start + (this.onePage ? 20 : 9)) {
                    break;
                }
                sender.sendMessage(new StringBuilder().append("ï¿½r").append((String) lines.get(i)).toString());
            }
            if ((!this.onePage) && (page < pages) && (commandName != null)) {
                if (commandName.startsWith("plugin")) {
                    r.sendMes(sender, "pluginTip", "%Page", search + Integer.valueOf(page + 1));
                } else {
                    r.sendMes(sender, "helpTip", "%Page", search + Integer.valueOf(page + 1));
                }
            }
            return;
        }

        int chapterpage = 0;
        if (chapterPageStr != null) {
            try {
                chapterpage = Integer.parseInt(chapterPageStr) - 1;
            } catch (NumberFormatException ex) {
                chapterpage = 0;
            }
            if (chapterpage < 0) {
                chapterpage = 0;
            }

        }

        if (!bookmarks.containsKey(pageStr.toLowerCase(Locale.ENGLISH))) {
            r.sendMes(sender, "helpUnknownChapter");
            return;
        }

        int chapterstart = ((Integer) bookmarks.get(pageStr.toLowerCase(Locale.ENGLISH))).intValue() + 1;

        int chapterend;
        for (chapterend = chapterstart; chapterend < lines.size(); chapterend++) {
            String line = (String) lines.get(chapterend);
            if ((line.length() > 0) && (line.charAt(0) == '#')) {
                break;
            }

        }

        int start = chapterstart + (this.onePage ? 0 : chapterpage * 9);
        int page = chapterpage + 1;
        int pages = (chapterend - chapterstart) / 9 + ((chapterend - chapterstart) % 9 > 0 ? 1 : 0);
        if ((!this.onePage) && (commandName != null)) {
            StringBuilder content = new StringBuilder();
            content.append(capitalCase(commandName)).append(": ");
            content.append(pageStr);
            r.sendMes(sender, "helpHeader", "%Content", content, "%Page", page, "%Pages", pages);
        }
        for (int i = start; i < chapterend; i++) {
            if (i >= start + (this.onePage ? 20 : 9)) {
                break;
            }
            sender.sendMessage(new StringBuilder().append("ï¿½r").append((String) lines.get(i)).toString());
        }
        if ((!this.onePage) && (page < pages) && (commandName != null)) {
            if (commandName.startsWith("plugin")) {
                r.sendMes(sender, "pluginTip", "%Page", pageStr + " " + (page + 1));
            } else {
                r.sendMes(sender, "helpTip", "%Page", pageStr + " " + (page + 1));
            }
            r.sendMes(sender, "helpTip", "%Page", pageStr + " " + (page + 1));
        }
    }
}

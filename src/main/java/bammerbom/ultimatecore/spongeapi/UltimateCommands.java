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
package bammerbom.ultimatecore.spongeapi;

import bammerbom.ultimatecore.spongeapi.commands.*;
import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.spongeapi.resources.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class UltimateCommands implements TabCompleter {

    public static List<UltimateCommand> cmds = new ArrayList<>();
    public static UltimateCommands ucmds;

    public static void load() {
        cmds.add(new CmdAccountstatus());
        cmds.add(new CmdAfk());
        cmds.add(new CmdAlert());
        cmds.add(new CmdAnswer());
        cmds.add(new CmdAsk());
        cmds.add(new CmdBack());
        cmds.add(new CmdBan());
        cmds.add(new CmdBanip());
        cmds.add(new CmdBanlist());
        cmds.add(new CmdBroadcast());
        cmds.add(new CmdBurn());
        cmds.add(new CmdButcher());
        cmds.add(new CmdClean());
        cmds.add(new CmdClear());
        cmds.add(new CmdClearchat());
        cmds.add(new CmdCompact());
        cmds.add(new CmdCompass());
        cmds.add(new CmdCoordinates());
        cmds.add(new CmdDamage());
        cmds.add(new CmdDeaf());
        cmds.add(new CmdDeaflist());
        cmds.add(new CmdDelhome());
        cmds.add(new CmdDeljail());
        cmds.add(new CmdDelwarp());
        cmds.add(new CmdEditsign());
        cmds.add(new CmdEffect());
        cmds.add(new CmdEnchant());
        //cmds.add(new CmdEnchantingtable());
        cmds.add(new CmdEnderchest());
        cmds.add(new CmdExperience());
        cmds.add(new CmdExtinguish());
        cmds.add(new CmdFeed());
        cmds.add(new CmdFireball());
        cmds.add(new CmdFirework());
        cmds.add(new CmdFly());
        cmds.add(new CmdFreeze());
        cmds.add(new CmdFreezelist());
        cmds.add(new CmdGamemode());
        cmds.add(new CmdGarbagecollector());
        cmds.add(new CmdGive());
        cmds.add(new CmdGod());
        cmds.add(new CmdHat());
        cmds.add(new CmdHeal());
        cmds.add(new CmdHelp());
        cmds.add(new CmdHome());
        cmds.add(new CmdHomelist());
        cmds.add(new CmdHunger());
        cmds.add(new CmdInventory());
        cmds.add(new CmdIp());
        cmds.add(new CmdItem());
        cmds.add(new CmdJail());
        cmds.add(new CmdJaillist());
        cmds.add(new CmdJump());
        cmds.add(new CmdKick());
        cmds.add(new CmdKickall());
        cmds.add(new CmdKill());
        cmds.add(new CmdKillall());
        cmds.add(new CmdKit());
        cmds.add(new CmdKittycannon());
        cmds.add(new CmdLag());
        cmds.add(new CmdList());
        cmds.add(new CmdSetmaxhealth());
        cmds.add(new CmdMe());
        cmds.add(new CmdMegasmite());
        cmds.add(new CmdMinecraftservers());
        cmds.add(new CmdMobtp());
        cmds.add(new CmdModify());
        cmds.add(new CmdMoney());
        cmds.add(new CmdMore());
        cmds.add(new CmdMotd());
        cmds.add(new CmdMsg());
        cmds.add(new CmdMute());
        cmds.add(new CmdMutelist());
        cmds.add(new CmdNames());
        cmds.add(new CmdNear());
        cmds.add(new CmdNick());
        cmds.add(new CmdPay());
        cmds.add(new CmdPing());
        cmds.add(new CmdPlugin());
        cmds.add(new CmdPotion());
        cmds.add(new CmdPowertool());
        cmds.add(new CmdRealname());
        cmds.add(new CmdRecipe());
        cmds.add(new CmdRemoveall());
        cmds.add(new CmdRepair());
        cmds.add(new CmdReply());
        cmds.add(new CmdRules());
        cmds.add(new CmdSave());
        cmds.add(new CmdSay());
        cmds.add(new CmdSeen());
        cmds.add(new CmdSetarmor());
        cmds.add(new CmdSetexperience());
        cmds.add(new CmdSethealth());
        cmds.add(new CmdSethome());
        cmds.add(new CmdSethunger());
        cmds.add(new CmdSetjail());
        cmds.add(new CmdSetlevel());
        cmds.add(new CmdSetspawn());
        cmds.add(new CmdSetwarp());
        cmds.add(new CmdSkull());
        cmds.add(new CmdSilence());
        cmds.add(new CmdSmite());
        cmds.add(new CmdSpawn());
        cmds.add(new CmdSpawner());
        cmds.add(new CmdSpawnmob());
        cmds.add(new CmdSpeed());
        cmds.add(new CmdSpy());
        cmds.add(new CmdSudo());
        cmds.add(new CmdTeleport());
        cmds.add(new CmdTeleportaccept());
        cmds.add(new CmdTeleportall());
        cmds.add(new CmdTeleportask());
        cmds.add(new CmdTeleportaskall());
        cmds.add(new CmdTeleportdeny());
        cmds.add(new CmdTeleporthere());
        cmds.add(new CmdTeleporttoggle());
        cmds.add(new CmdTime());
        cmds.add(new CmdTop());
        cmds.add(new CmdUltimatecore());
        cmds.add(new CmdUnban());
        cmds.add(new CmdUndeaf());
        cmds.add(new CmdUnfreeze());
        cmds.add(new CmdUnjail());
        cmds.add(new CmdUnmute());
        cmds.add(new CmdUnsilence());
        cmds.add(new CmdUptime());
        cmds.add(new CmdUuid());
        cmds.add(new CmdVanish());
        cmds.add(new CmdVillager());
        cmds.add(new CmdWarp());
        cmds.add(new CmdWarplist());
        cmds.add(new CmdWeather());
        cmds.add(new CmdWorkbench());
        cmds.add(new CmdWorld());
        //
        ucmds = new UltimateCommands();
        //
        for (UltimateCommand cmd : cmds) {
            if (Bukkit.getPluginCommand("ultimatecore:" + cmd.getName()) == null) {
                r.log("Failed to load command: " + cmd.getName());
                continue;
            }
            Bukkit.getPluginCommand("ultimatecore:" + cmd.getName()).setTabCompleter(ucmds);
        }
    }

    public static void onCmd(final CommandSender sender, Command cmd, String label, final String[] args) {
        if (Overrider.checkOverridden(sender, cmd, label, args)) {
            return;
        }
        if (label.startsWith("ultimatecore:")) {
            label = label.replaceFirst("ultimatecore:", "");
        }
        for (UltimateCommand cmdr : cmds) {
            if (label.equals(cmdr.getName()) || cmdr.getAliases().contains(label)) {
                cmdr.run(sender, label, args);
                break;
            }
        }
    }

    public static String[] convertsArgs(String arg) {
        if (!arg.contains(" ")) {
            String[] args = {arg};
            return args;
        }
        return arg.split(" ");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (Overrider.checkOverridden(sender, cmd, label, args)) {
            return null;
        }
        List<String> rtrn = null;
        if (label.startsWith("ultimatecore:")) {
            label = label.replaceFirst("ultimatecore:", "");
        }
        for (UltimateCommand cmdr : cmds) {
            if (cmdr.getName().equals(label) || cmdr.getAliases().contains(label)) {
                try {
                    rtrn = cmdr.onTabComplete(sender, cmd, label, args, args[args.length - 1], args.length - 1);
                } catch (Exception ex) {
                    ErrorLogger.log(ex, "Failed tabcompleting for " + label);
                }
                break;
            }
        }
        if (rtrn == null) {
            rtrn = new ArrayList<>();
            for (Player p : r.getOnlinePlayers()) {
                rtrn.add(p.getName());
            }
        }
        ArrayList<String> rtrn2 = new ArrayList<>();
        rtrn2.addAll(rtrn);
        rtrn = rtrn2;
        if (!StringUtil.nullOrEmpty(args[args.length - 1])) {
            List<String> remv = new ArrayList<>();
            for (String s : rtrn) {
                if (!StringUtils.startsWithIgnoreCase(s, args[args.length - 1])) {
                    remv.add(s);
                }
            }
            rtrn.removeAll(remv);
        }
        return rtrn;
    }

    static class Overrider {

        private static final transient Map<PluginCommand, PluginCommand> overriddenList = new HashMap<>();

        public static void fixCommands() {
            for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
                if (pl.isEnabled() && !pl.equals(r.getUC())) {
                    addPlugin(pl);
                }
            }
        }

        public static void addPlugin(Plugin pl) {
            if (pl.getName().contains("Essentials")) {
                return;
            }
            List<Command> commands = PluginCommandYamlParser.parse(pl);
            for (Command command : commands) {
                PluginCommand pc = (PluginCommand) command;
                List<String> labels = new ArrayList<>(pc.getAliases());
                labels.add(pc.getName());
                for (String lab : labels) {
                    PluginCommand uc;
                    uc = Bukkit.getServer().getPluginCommand("ultimatecore:" + lab);
                    /*if(uc == null){
                     uc = plugin.getServer().getPluginCommand(pc.getName().toLowerCase(Locale.ENGLISH));
                     }*/
                    if ((uc != null) && uc.getPlugin().equals(r.getUC())) {
                        if (lab.equalsIgnoreCase(uc.getLabel())) {
                            overriddenList.put(uc, pc);
                            r.debug(ChatColor.WHITE + "Command overridden: " + lab + " (" + pc.getPlugin() + ")");
                        }
                    }
                }
            }
        }

        public static void removePlugin(Plugin pl) {
            List<Command> commands = PluginCommandYamlParser.parse(pl);
            for (Command command : commands) {
                PluginCommand pc = (PluginCommand) command;
                List<String> labels = new ArrayList<>(pc.getAliases());
                labels.add(pc.getName());
                PluginCommand uc;
                uc = Bukkit.getServer().getPluginCommand("ultimatecore:" + pc.getName());
                if (uc == null) {
                    uc = Bukkit.getServer().getPluginCommand(pc.getName().toLowerCase(Locale.ENGLISH));
                }
                if ((uc != null) && uc.getPlugin().equals(r.getUC())) {
                    for (String label : labels) {
                        if (label.equalsIgnoreCase(uc.getLabel())) {
                            if (overriddenList.containsKey(uc)) {
                                r.debug(ChatColor.WHITE + "Command un-overridden: " + label + " (" + pc.getPlugin() +
                                        ")");
                                overriddenList.remove(uc);
                            }
                        }
                    }
                }
            }
        }

        public static boolean checkOverridden(final CommandSender cs, Command cmd, final String label, final String[] args) {
            PluginCommand uc = (PluginCommand) cmd;
            if (overriddenList.containsKey(uc) || r.getCnfg().getList("disabledcommands").contains(label)) {
                r.debug(uc + " " + overriddenList.get(uc));
                PluginCommand pc = overriddenList.get(uc);
                if (pc == null || pc.getExecutor() == null) {
                    r.sendMes(cs, "unknownCommand");
                    return true;
                }
                r.debug("Executing " + cs + " " + pc + " " + label);

                pc.execute(cs, label, args);
                return true;
            }
            return false;
        }

        @EventHandler
        public void plEnable(PluginEnableEvent e) {
            if (!e.getPlugin().equals(r.getUC())) {
                addPlugin(e.getPlugin());
            }
        }

        @EventHandler
        public void plDisable(PluginDisableEvent e) {
            if (!e.getPlugin().equals(r.getUC())) {
                removePlugin(e.getPlugin());
            }
        }
    }

}

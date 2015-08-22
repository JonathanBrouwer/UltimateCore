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
import bammerbom.ultimatecore.bukkit.api.UC;
import bammerbom.ultimatecore.bukkit.api.UWorld;
import bammerbom.ultimatecore.bukkit.api.UWorld.WorldFlag;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.classes.MobType;
import bammerbom.ultimatecore.bukkit.resources.utils.LocationUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.StringUtil;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdWorld implements UltimateCommand {

    public static void usage(CommandSender cs) {
        r.sendMes(cs, "worldUsage1");
        r.sendMes(cs, "worldUsage2");
        r.sendMes(cs, "worldUsage3");
        r.sendMes(cs, "worldUsage4");
        r.sendMes(cs, "worldUsage5");
        r.sendMes(cs, "worldUsage6");
        r.sendMes(cs, "worldUsage7");
        r.sendMes(cs, "worldUsage8");
        r.sendMes(cs, "worldUsage9", "%Flags", StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
    }

    public static void create(CommandSender cs, String[] args) {
        if (!r.perm(cs, "uc.world", false, false) && !r.perm(cs, "uc.world.create", false, false)) {
            r.sendMes(cs, "noPermissions");
            return;
        }
        if (r.checkArgs(args, 0) == false) {
            usage(cs);
            return;
        }
        if (r.checkArgs(args, 1) == true) {
            if (Bukkit.getWorld(args[1]) != null) {
                r.sendMes(cs, "worldAlreadyExist");
                return;
            }
            if (!args[1].replaceAll("[_a-zA-Z0-9]", "").isEmpty()) {
                r.sendMes(cs, "worldNonAlpha");
                return;
            }
            WorldCreator settings = new WorldCreator(args[1]);
            String gen = null;
            Integer na = 2;
            for (int i = 0;
                 i < args.length + 3;
                 i++) {

                if (r.checkArgs(args, na) == true) {
                    if (args[na].equalsIgnoreCase("flat") || args[na].equalsIgnoreCase("flatland")) {
                        settings.type(WorldType.FLAT);
                    } else if (args[na].equalsIgnoreCase("large") || args[na].replaceAll("_", "").equalsIgnoreCase("largebiomes")) {
                        settings.type(WorldType.LARGE_BIOMES);
                    } else if (args[na].equalsIgnoreCase("amplified")) {
                        settings.type(WorldType.AMPLIFIED);
                    } else if (args[na].equalsIgnoreCase("normal")) {
                    } else if (args[na].equalsIgnoreCase("nether")) {
                        settings.environment(Environment.NETHER);
                    } else if (args[na].equalsIgnoreCase("end")) {
                        settings.environment(Environment.THE_END);
                    } else if (args[na].equalsIgnoreCase("nostructures")) {
                        settings.generateStructures(false);
                    } else if (r.isInt(args[na])) {
                        settings.seed(Long.parseLong(args[na]));
                    } else if (args[na].startsWith("s:")) {
                        if (StringUtil.isAlphaNumeric(args[na].replaceFirst("s:", "").replace("-", ""))) {
                            if (r.isLong(args[na].replaceFirst("s:", ""))) {
                                String seed1 = args[na].replaceFirst("s:", "");
                                settings.seed(Long.parseLong(seed1));
                            } else {
                                String seed1 = args[na].replaceFirst("s:", "");
                                settings.seed(seed1.hashCode());
                            }
                        }
                    } else if (args[na].startsWith("g:")) {
                        String generator = args[na].replaceFirst("g:", "");
                        gen = generator;
                        settings.generator(generator);
                    }
                    na++;
                }

            }
            r.sendMes(cs, "worldCreateCreating", "%World", settings.name());

            World world = Bukkit.createWorld(settings);
            UC.getWorld(world).register(gen);

            r.sendMes(cs, "worldCreateCreated", "%World", settings.name());
        } else {
            r.sendMes(cs, "worldUsage2");
            r.sendMes(cs, "worldUsage3");
        }

    }

    public static void importw(CommandSender cs, String[] args) {
        if (!r.perm(cs, "uc.world", false, false) && !r.perm(cs, "uc.world.import", false, false)) {
            r.sendMes(cs, "noPermissions");
            return;
        }
        if (r.checkArgs(args, 0) == false) {
            usage(cs);
            return;
        }
        //
        if (r.checkArgs(args, 1) == true) {
            if (!new File(args[1]).exists()) {
                r.sendMes(cs, "worldNotFound", "%World", args[1]);
                return;
            }
            WorldCreator settings = new WorldCreator(args[1]);
            String gen = null;
            Integer na = 2;
            for (int i = 0;
                 i < args.length + 3;
                 i++) {

                if (r.checkArgs(args, na) == true) {
                    if (args[na].equalsIgnoreCase("flat") || args[na].equalsIgnoreCase("flatland")) {
                        settings.type(WorldType.FLAT);
                    } else if (args[na].equalsIgnoreCase("large") || args[na].replaceAll("_", "").equalsIgnoreCase("largebiomes")) {
                        settings.type(WorldType.LARGE_BIOMES);
                    } else if (args[na].equalsIgnoreCase("amplified")) {
                        settings.type(WorldType.AMPLIFIED);
                    } else if (args[na].equalsIgnoreCase("normal")) {
                    } else if (args[na].equalsIgnoreCase("nether")) {
                        settings.environment(Environment.NETHER);
                    } else if (args[na].equalsIgnoreCase("end")) {
                        settings.environment(Environment.THE_END);
                    } else if (args[na].equalsIgnoreCase("nostructures")) {
                        settings.generateStructures(false);
                    } else if (r.isInt(args[na])) {
                        settings.seed(Long.parseLong(args[na]));
                    } else if (args[na].startsWith("s:")) {
                        if (StringUtil.isAlphaNumeric(args[na].replaceFirst("s:", "").replace("-", ""))) {
                            if (r.isLong(args[na].replaceFirst("s:", ""))) {
                                String seed1 = args[na].replaceFirst("s:", "");
                                settings.seed(Long.parseLong(seed1));
                            } else {
                                String seed1 = args[na].replaceFirst("s:", "");
                                settings.seed(seed1.hashCode());
                            }
                        }
                    } else if (args[na].startsWith("g:")) {
                        String generator = args[na].replaceFirst("g:", "");
                        gen = generator;
                        settings.generator(generator);
                    }
                    na++;
                }

            }
            r.sendMes(cs, "worldImportImporting", "%World", settings.name());

            World world = Bukkit.createWorld(settings);
            UC.getWorld(world).register(gen);

            r.sendMes(cs, "worldImportImported", "%World", settings.name());
        } else {
            r.sendMes(cs, "worldUsage4");
        }
    }

    public static void list(CommandSender cs, String[] args) {
        if (!r.perm(cs, "uc.world", false, false) && !r.perm(cs, "uc.world.list", false, false)) {
            r.sendMes(cs, "noPermissions");
            return;
        }
        if (r.checkArgs(args, 0) == false) {
            usage(cs);
            return;
        }
        //
        List<World> worlds1 = Bukkit.getWorlds();
        ArrayList<String> worlds = new ArrayList<>();
        for (World w : worlds1) {
            worlds.add(w.getName());
        }
        StringBuilder list = new StringBuilder();
        String result;
        try {
            Integer cur = 0;
            for (int i = 0;
                 i < worlds.toArray().length;
                 i++) {
                list.append(worlds.get(cur) + ", ");
                cur++;
            }
        } catch (IndexOutOfBoundsException ex) {
        }
        result = list.substring(0, list.length() - 2);
        r.sendMes(cs, "worldList", "%Worlds", result);
    }

    public static void remove(CommandSender cs, String[] args) {
        if (!r.perm(cs, "uc.world", false, false) && !r.perm(cs, "uc.world.remove", false, false)) {
            r.sendMes(cs, "noPermissions");
            return;
        }
        if (r.checkArgs(args, 0) == false) {
            usage(cs);
            return;
        }
        //
        if (r.checkArgs(args, 1) == true) {
            World world = Bukkit.getWorld(args[1]);
            if (world == null) {
                r.sendMes(cs, "worldNotFound", "%World", args[1]);
                return;
            }
            for (Player pl : r.getOnlinePlayers()) {
                if (pl.getWorld().equals(world)) {
                    World w2 = Bukkit.getWorlds().get(0);
                    LocationUtil.teleport(pl, w2.getSpawnLocation(), TeleportCause.PLUGIN, true, false);
                }
            }
            Bukkit.getServer().unloadWorld(world, true);
            UC.getWorld(world).unregister();
            r.sendMes(cs, "worldRemove", "%World", world.getName());
        } else {
            r.sendMes(cs, "worldUsage5");
        }
    }

    private static void clear(File dir) {
        for (File file : dir.listFiles()) {
            if (file.getName().toLowerCase().contains("player")) {
                continue;
            }
            if (file.isDirectory()) {
                clear(file);
            }
            file.delete();
        }
    }

    private static void resetAll(World world) {
        world.save();
        Bukkit.unloadWorld(world, true);
        File dir = world.getWorldFolder();
        clear(dir);
        WorldCreator creator = new WorldCreator(world.getName());
        creator.seed(world.getSeed());
        creator.environment(world.getEnvironment());
        creator.generator(world.getGenerator());
        creator.generateStructures(world.canGenerateStructures());
        creator.type(world.getWorldType());
        World world2 = Bukkit.createWorld(creator);
        world2.save();

    }

    public static void tp(CommandSender cs, String[] args) {
        if (!r.perm(cs, "uc.world", false, false) && !r.perm(cs, "uc.world.tp", false, false)) {
            r.sendMes(cs, "noPermissions");
            return;
        }
        if (r.checkArgs(args, 1) == false) {
            usage(cs);
            return;
        }
        //
        if (r.checkArgs(args, 1) == true) {
            World world = Bukkit.getWorld(args[1]);
            if (world == null) {
                r.sendMes(cs, "worldNotFound", "%World", args[1]);
                return;
            }
            if (!r.isPlayer(cs)) {
                return;
            }
            final Player p = (Player) cs;
            final Location loc = world.getSpawnLocation();
            LocationUtil.teleport(p, loc, TeleportCause.COMMAND, true, true);
        }
    }

    public static void flag(CommandSender cs, String[] args) {
        if (!r.perm(cs, "uc.world", false, false) && !r.perm(cs, "uc.world.flag", false, false)) {
            r.sendMes(cs, "noPermissions");
            return;
        }
        if (r.checkArgs(args, 3) == false) {
            usage(cs);
            return;
        }
        //world flag [world] [flag] [value]
        //true = allow, false = deny
        //monster, animal, pvp

        if (r.checkArgs(args, 3) == true) {
            UWorld world = new UWorld(Bukkit.getWorld(args[1]));
            String flag = args[2];
            String value = args[3];
            if (flag.equalsIgnoreCase("monster") || flag.equalsIgnoreCase("monsterspawn")) {
                if (value.equalsIgnoreCase("deny")) {
                    for (Entity en : world.getWorld().getEntities()) {
                        if (en instanceof Monster || (MobType.fromBukkitType(en.getType()) != null && MobType.fromBukkitType(en.getType()).type.equals(MobType.Enemies.ENEMY))) {
                            en.remove();
                        }
                    }
                    world.setFlagDenied(WorldFlag.MONSTER);
                    r.sendMes(cs, "worldFlagSetMonster", "%World", world.getWorld().getName(), "%Value", r.mes("worldFlagValueDeny"));
                } else if (value.equalsIgnoreCase("allow")) {
                    world.setFlagAllowed(WorldFlag.MONSTER);
                    r.sendMes(cs, "worldFlagSetMonster", "%World", world.getWorld().getName(), "%Value", r.mes("worldFlagValueAllow"));
                } else {
                    r.sendMes(cs, "worldUsage8");
                    r.sendMes(cs, "worldUsage9", "%Flags", StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
                }
            } else if (flag.equalsIgnoreCase("animal") || flag.equalsIgnoreCase("animalspawn")) {
                if (value.equalsIgnoreCase("deny")) {
                    for (Entity en : world.getWorld().getEntities()) {
                        if (en instanceof Animals || (MobType.fromBukkitType(en.getType()) != null && !MobType.fromBukkitType(en.getType()).type.equals(MobType.Enemies.ENEMY))) {
                            en.remove();
                        }
                    }
                    world.setFlagDenied(WorldFlag.ANIMAL);
                    r.sendMes(cs, "worldFlagSetAnimal", "%World", world.getWorld().getName(), "%Value", r.mes("worldFlagValueDeny"));
                } else if (value.equalsIgnoreCase("allow")) {
                    world.setFlagAllowed(WorldFlag.ANIMAL);
                    r.sendMes(cs, "worldFlagSetAnimal", "%World", world.getWorld().getName(), "%Value", r.mes("worldFlagValueAllow"));
                } else {
                    r.sendMes(cs, "worldUsage8");
                    r.sendMes(cs, "worldUsage9", "%Flags", StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
                }
            } else if (flag.equalsIgnoreCase("pvp")) {
                if (value.equalsIgnoreCase("deny")) {
                    world.setFlagDenied(WorldFlag.PVP);
                    r.sendMes(cs, "worldFlagSetPvp", "%World", world.getWorld().getName(), "%Value", r.mes("worldFlagValueDeny"));
                } else if (value.equalsIgnoreCase("allow")) {
                    world.setFlagAllowed(WorldFlag.PVP);
                    r.sendMes(cs, "worldFlagSetPvp", "%World", world.getWorld().getName(), "%Value", r.mes("worldFlagValueAllow"));
                } else {
                    r.sendMes(cs, "worldUsage8");
                    r.sendMes(cs, "worldUsage9", "%Flags", StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
                }
            } else if (flag.equalsIgnoreCase("gamemode")) {
                GameMode mode = null;
                switch (value.toLowerCase()) {
                    case "survival":
                    case "s":
                    case "surv":
                    case "0":
                        mode = GameMode.SURVIVAL;
                        break;
                    case "creative":
                    case "c":
                    case "crea":
                    case "1":
                        mode = GameMode.CREATIVE;
                        break;
                    case "adventure:":
                    case "a":
                    case "adven":
                    case "2":
                        mode = GameMode.ADVENTURE;
                        break;
                    case "sp":
                    case "spec":
                    case "spectate":
                    case "spectator":
                    case "3":
                        mode = GameMode.SPECTATOR;
                        break;
                }
                world.setDefaultGamemode(mode);
                r.sendMes(cs, "worldFlagGamemode", "%World", world.getWorld().getName(), "%Value", mode.toString().toLowerCase());
            } else {
                r.sendMes(cs, "worldUsage8");
                r.sendMes(cs, "worldUsage9", "%Flags", StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
            }
        } else {
            r.sendMes(cs, "worldUsage8");
            r.sendMes(cs, "worldUsage9", "%Flags", StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
        }
    }

    @Override
    public String getName() {
        return "world";
    }

    @Override
    public String getPermission() {
        return "uc.world";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (r.checkArgs(args, 0) == false) {
            usage(cs);
        } else if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("add")) {
            create(cs, args);
        } else if (args[0].equalsIgnoreCase("import") || args[0].equalsIgnoreCase("imp")) {
            importw(cs, args);
        } else if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("list")) {
            list(cs, args);
        } else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
            remove(cs, args);
        } else if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
            tp(cs, args);
        } else if (args[0].equalsIgnoreCase("flag")) {
            flag(cs, args);
        } else {
            usage(cs);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        if (curn == 0) {
            return Arrays.asList("create", "import", "list", "remove", "tp", "flag");
        }
        if (curn == 1) {
            if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("import") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("flag")) {
                ArrayList<String> rtrn = new ArrayList<>();
                for (World w : Bukkit.getWorlds()) {
                    rtrn.add(w.getName());
                }
                return rtrn;
            }
        }
        return new ArrayList<>();
    }
}

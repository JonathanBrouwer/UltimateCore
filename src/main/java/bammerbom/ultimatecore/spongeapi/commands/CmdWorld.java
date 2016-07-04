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
package bammerbom.ultimatecore.spongeapi.commands;

import bammerbom.ultimatecore.spongeapi.UltimateCommand;
import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.api.UWorld;
import bammerbom.ultimatecore.spongeapi.api.UWorld.WorldFlag;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.classes.MobType;
import bammerbom.ultimatecore.spongeapi.resources.utils.LocationUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.StringUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.animal.Animal;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.GeneratorTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldArchetypes;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CmdWorld implements UltimateCommand {

    public static CommandResult usage(CommandSource cs) {
        r.sendMes(cs, "worldUsage1");
        r.sendMes(cs, "worldUsage2");
        r.sendMes(cs, "worldUsage3");
        r.sendMes(cs, "worldUsage4");
        r.sendMes(cs, "worldUsage5");
        r.sendMes(cs, "worldUsage6");
        r.sendMes(cs, "worldUsage7");
        r.sendMes(cs, "worldUsage8");
        r.sendMes(cs, "worldUsage9", "%Flags", StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
        return CommandResult.success();
    }

    public static CommandResult create(CommandSource cs, String[] args) {
        if (!r.perm(cs, "uc.world", false) && !r.perm(cs, "uc.world.create", false)) {
            r.sendMes(cs, "noPermissions");
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            usage(cs);
            return CommandResult.empty();
        }
        if (r.checkArgs(args, 1)) {
            if (Sponge.getServer().getWorld(args[1]).isPresent()) {
                r.sendMes(cs, "worldAlreadyExist");
                return CommandResult.empty();
            }
            if (!args[1].replaceAll("[_a-zA-Z0-9]", "").isEmpty()) {
                r.sendMes(cs, "worldNonAlpha");
                return CommandResult.empty();
            }
            WorldProperties settings = null;
            String gen = null;
            Integer na = 2;
            try {
                for (int i = 0; i < args.length + 3; i++) {
                    settings.setGeneratorType(GeneratorTypes.OVERWORLD);
                    if (r.checkArgs(args, na)) {
                        if (args[na].equalsIgnoreCase("normal")) {
                            settings = Sponge.getServer().createWorldProperties(args[1], WorldArchetypes.OVERWORLD);
                        } else if (args[na].equalsIgnoreCase("nether")) {
                            settings = Sponge.getServer().createWorldProperties(args[1], WorldArchetypes.THE_NETHER);
                        } else if (args[na].equalsIgnoreCase("end")) {
                            settings = Sponge.getServer().createWorldProperties(args[1], WorldArchetypes.THE_END);
                        } else if (args[na].equalsIgnoreCase("void")) {
                            settings = Sponge.getServer().createWorldProperties(args[1], WorldArchetypes.THE_VOID);
                        } else if (args[na].equalsIgnoreCase("skylands")) {
                            settings = Sponge.getServer().createWorldProperties(args[1], WorldArchetypes.THE_SKYLANDS);
                        }
                        na++;
                    }
                }
                if (settings == null) {
                    settings = Sponge.getServer().createWorldProperties(args[1], WorldArchetypes.OVERWORLD);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < args.length + 3; i++) {
                settings.setGeneratorType(GeneratorTypes.OVERWORLD);
                if (r.checkArgs(args, na)) {
                    if (args[na].equalsIgnoreCase("flat") || args[na].equalsIgnoreCase("flatland")) {
                        settings.setGeneratorType(GeneratorTypes.FLAT);
                    } else if (args[na].equalsIgnoreCase("large") || args[na].replaceAll("_", "").equalsIgnoreCase("largebiomes")) {
                        settings.setGeneratorType(GeneratorTypes.LARGE_BIOMES);
                    } else if (args[na].equalsIgnoreCase("amplified")) {
                        settings.setGeneratorType(GeneratorTypes.AMPLIFIED);
                    } else if (args[na].equalsIgnoreCase("nostructures")) {
                        //TODO generate structures?
                        settings.getGeneratorSettings().structures;
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
                        settings.setGenerator(generator);
                    }
                    na++;
                }

            }
            r.sendMes(cs, "worldCreateCreating", "%World", settings.getWorldName());

            World world = Sponge.getServer().loadWorld(settings).orElse(null);
            UC.getWorld(world).register(gen);

            r.sendMes(cs, "worldCreateCreated", "%World", settings.getWorldName());
            return CommandResult.success();
        } else {
            r.sendMes(cs, "worldUsage2");
            r.sendMes(cs, "worldUsage3");
            return CommandResult.empty();
        }
    }

    public static CommandResult importw(CommandSource cs, String[] args) {
        if (!r.perm(cs, "uc.world", false) && !r.perm(cs, "uc.world.import", false)) {
            r.sendMes(cs, "noPermissions");
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            usage(cs);
            return CommandResult.empty();
        }
        //
        if (r.checkArgs(args, 1)) {
            if (!new File(args[1]).exists()) {
                r.sendMes(cs, "worldNotFound", "%World", args[1]);
                return CommandResult.empty();
            }
            r.sendMes(cs, "worldImportImporting", "%World", args[1]);

            World world = Sponge.getServer().loadWorld(args[1]).orElse(null);
            UC.getWorld(world).register();

            r.sendMes(cs, "worldImportImported", "%World", args[1]);
            return CommandResult.success();
        } else {
            r.sendMes(cs, "worldUsage4");
            return CommandResult.empty();
        }
    }

    public static CommandResult list(CommandSource cs, String[] args) {
        if (!r.perm(cs, "uc.world", false) && !r.perm(cs, "uc.world.list", false)) {
            r.sendMes(cs, "noPermissions");
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            usage(cs);
            return CommandResult.empty();
        }
        //
        Collection<World> worlds1 = Sponge.getServer().getWorlds();
        ArrayList<String> worlds = new ArrayList<>();
        for (World w : worlds1) {
            worlds.add(w.getName());
        }
        StringBuilder list = new StringBuilder();
        String result;
        try {
            Integer cur = 0;
            for (int i = 0; i < worlds.toArray().length; i++) {
                list.append(worlds.get(cur) + ", ");
                cur++;
            }
        } catch (IndexOutOfBoundsException ex) {
        }
        result = list.substring(0, list.length() - 2);
        r.sendMes(cs, "worldList", "%Worlds", result);
        return CommandResult.success();
    }

    public static CommandResult remove(CommandSource cs, String[] args) {
        if (!r.perm(cs, "uc.world", false) && !r.perm(cs, "uc.world.remove", false)) {
            r.sendMes(cs, "noPermissions");
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            usage(cs);
            return CommandResult.empty();
        }
        //
        if (r.checkArgs(args, 1)) {
            World world = Sponge.getServer().getWorld(args[1]).orElse(null);
            if (world == null) {
                r.sendMes(cs, "worldNotFound", "%World", args[1]);
                return CommandResult.empty();
            }
            for (Player pl : r.getOnlinePlayers()) {
                if (pl.getWorld().equals(world)) {
                    World w2 = (World) Sponge.getServer().getWorlds().toArray()[0];
                    LocationUtil.teleport(pl, w2.getSpawnLocation(), Cause.builder().build(), true, false);
                }
            }
            Sponge.getServer().unloadWorld(world);
            UC.getWorld(world).unregister();
            r.sendMes(cs, "worldRemove", "%World", world.getName());
            return CommandResult.success();
        } else {
            r.sendMes(cs, "worldUsage5");
            return CommandResult.empty();
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

    public static CommandResult tp(CommandSource cs, String[] args) {
        if (!r.perm(cs, "uc.world", false) && !r.perm(cs, "uc.world.tp", false)) {
            r.sendMes(cs, "noPermissions");
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 1)) {
            usage(cs);
            return CommandResult.empty();
        } else {
            World world = Sponge.getServer().getWorld(args[1]).orElse(null);
            if (world == null) {
                r.sendMes(cs, "worldNotFound", "%World", args[1]);
                return CommandResult.empty();
            }
            if (!r.isPlayer(cs)) {
                return CommandResult.empty();
            }
            final Player p = (Player) cs;
            final Location loc = world.getSpawnLocation();
            LocationUtil.teleport(p, loc, Cause.builder().build(), true, true);
            return CommandResult.success();
        }
    }

    public static CommandResult flag(CommandSource cs, String[] args) {
        if (!r.perm(cs, "uc.world", false) && !r.perm(cs, "uc.world.flag", false)) {
            r.sendMes(cs, "noPermissions");
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 3)) {
            usage(cs);
            return CommandResult.empty();
        }
        //world flag [world] [flag] [value]
        //true = allow, false = deny
        //monster, animal, pvp

        if (r.checkArgs(args, 3)) {
            World sworld = Sponge.getServer().getWorld(args[1]).orElse(null);
            if (sworld == null) {
                r.sendMes(cs, "worldNotFound", "%World", args[1]);
                return CommandResult.empty();
            }
            UWorld world = new UWorld(sworld);
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
                    return CommandResult.success();
                } else if (value.equalsIgnoreCase("allow")) {
                    world.setFlagAllowed(WorldFlag.MONSTER);
                    r.sendMes(cs, "worldFlagSetMonster", "%World", world.getWorld().getName(), "%Value", r.mes("worldFlagValueAllow"));
                    return CommandResult.success();
                } else {
                    r.sendMes(cs, "worldUsage8");
                    r.sendMes(cs, "worldUsage9", "%Flags", StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
                    return CommandResult.empty();
                }
            } else if (flag.equalsIgnoreCase("animal") || flag.equalsIgnoreCase("animalspawn")) {
                if (value.equalsIgnoreCase("deny")) {
                    for (Entity en : world.getWorld().getEntities()) {
                        if (en instanceof Animal || (MobType.fromBukkitType(en.getType()) != null && !MobType.fromBukkitType(en.getType()).type.equals(MobType.Enemies.ENEMY))) {
                            en.remove();
                        }
                    }
                    world.setFlagDenied(WorldFlag.ANIMAL);
                    r.sendMes(cs, "worldFlagSetAnimal", "%World", world.getWorld().getName(), "%Value", r.mes("worldFlagValueDeny"));
                    return CommandResult.success();
                } else if (value.equalsIgnoreCase("allow")) {
                    world.setFlagAllowed(WorldFlag.ANIMAL);
                    r.sendMes(cs, "worldFlagSetAnimal", "%World", world.getWorld().getName(), "%Value", r.mes("worldFlagValueAllow"));
                    return CommandResult.success();
                } else {
                    r.sendMes(cs, "worldUsage8");
                    r.sendMes(cs, "worldUsage9", "%Flags", StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
                    return CommandResult.empty();
                }
            } else if (flag.equalsIgnoreCase("pvp")) {
                if (value.equalsIgnoreCase("deny")) {
                    world.setFlagDenied(WorldFlag.PVP);
                    r.sendMes(cs, "worldFlagSetPvp", "%World", world.getWorld().getName(), "%Value", r.mes("worldFlagValueDeny"));
                    return CommandResult.success();
                } else if (value.equalsIgnoreCase("allow")) {
                    world.setFlagAllowed(WorldFlag.PVP);
                    r.sendMes(cs, "worldFlagSetPvp", "%World", world.getWorld().getName(), "%Value", r.mes("worldFlagValueAllow"));
                    return CommandResult.success();
                } else {
                    r.sendMes(cs, "worldUsage8");
                    r.sendMes(cs, "worldUsage9", "%Flags", StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
                    return CommandResult.empty();
                }
            } else if (flag.equalsIgnoreCase("gamemode")) {
                GameMode mode = null;
                switch (value.toLowerCase()) {
                    case "survival":
                    case "s":
                    case "surv":
                    case "0":
                        mode = GameModes.SURVIVAL;
                        break;
                    case "creative":
                    case "c":
                    case "crea":
                    case "1":
                        mode = GameModes.CREATIVE;
                        break;
                    case "adventure:":
                    case "a":
                    case "adven":
                    case "2":
                        mode = GameModes.ADVENTURE;
                        break;
                    case "sp":
                    case "spec":
                    case "spectate":
                    case "spectator":
                    case "3":
                        mode = GameModes.SPECTATOR;
                        break;
                }
                world.setDefaultGamemode(mode);
                r.sendMes(cs, "worldFlagGamemode", "%World", world.getWorld().getName(), "%Value", mode.toString().toLowerCase());
                return CommandResult.success();
            } else {
                r.sendMes(cs, "worldUsage8");
                r.sendMes(cs, "worldUsage9", "%Flags", StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
                return CommandResult.empty();
            }
        } else {
            r.sendMes(cs, "worldUsage8");
            r.sendMes(cs, "worldUsage9", "%Flags", StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
            return CommandResult.empty();
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
    public String getUsage() {
        return "/<command> create/import/remove/list/tp/flag";
    }

    @Override
    public Text getDescription() {
        return Text.of("World management command.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.checkArgs(args, 0)) {
            return usage(cs);
        } else if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("add")) {
            return create(cs, args);
        } else if (args[0].equalsIgnoreCase("import") || args[0].equalsIgnoreCase("imp")) {
            return importw(cs, args);
        } else if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("list")) {
            return list(cs, args);
        } else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
            return remove(cs, args);
        } else if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
            return tp(cs, args);
        } else if (args[0].equalsIgnoreCase("flag")) {
            return flag(cs, args);
        } else {
            return usage(cs);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        if (curn == 0) {
            return Arrays.asList("create", "import", "list", "remove", "tp", "flag");
        }
        if (curn == 1) {
            if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("import") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("tp") || args[0]
                    .equalsIgnoreCase("flag")) {
                ArrayList<String> rtrn = new ArrayList<>();
                for (World w : Sponge.getServer().getWorlds()) {
                    rtrn.add(w.getName());
                }
                return rtrn;
            }
        }
        return new ArrayList<>();
    }
}

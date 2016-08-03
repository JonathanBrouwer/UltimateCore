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
package bammerbom.ultimatecore.sponge.api.module;

import bammerbom.ultimatecore.sponge.UltimateCore;

import java.util.Optional;

/**
 * This is a enum containing all official modules of UltimateCore
 */
public class Modules {

    private static ModuleService service = UltimateCore.get().getModuleService();

    //TODO create javadocs for a description of every module
    public static Optional<Module> AFK = service.getModule("afk");
    public static Optional<Module> AUTOMESSAGE = service.getModule("automessage");
    public static Optional<Module> AUTOSAVE = service.getModule("autosave");
    public static Optional<Module> BACK = service.getModule("back");
    public static Optional<Module> BACKUP = service.getModule("backup");
    public static Optional<Module> BAN = service.getModule("ban");
    public static Optional<Module> BLACKLIST = service.getModule("blacklist");
    public static Optional<Module> BLOOD = service.getModule("blood");
    public static Optional<Module> BURN = service.getModule("burn");
    public static Optional<Module> CHAT = service.getModule("chat");
    //Allows for warmup & cooldown for commands
    public static Optional<Module> COMMANDTIMER = service.getModule("commandtimer");
    //Logs all commands to the console, should be filterable
    public static Optional<Module> COMMANDLOG = service.getModule("commandlog");
    public static Optional<Module> COMMANDSIGN = service.getModule("commandsigns");
    //Custom join & leave messages
    //First join commands
    public static Optional<Module> CONNECTIONMESSAGES = service.getModule("connectionmessages");
    //Create custom commands which print specific text or execute other commands
    public static Optional<Module> CUSTOMCOMMANDS = service.getModule("customcommands");
    public static Optional<Module> DEAF = service.getModule("deaf");
    public static Optional<Module> DEATHMESSAGE = service.getModule("deathmessage");
    public static Optional<Module> ECONOMY = service.getModule("economy");
    public static Optional<Module> EXPERIENCE = service.getModule("experience");
    public static Optional<Module> EXPLOSION = service.getModule("explosion");
    public static Optional<Module> FLY = service.getModule("fly");
    public static Optional<Module> FREEZE = service.getModule("freeze");
    public static Optional<Module> GAMEMODE = service.getModule("gamemode");
    public static Optional<Module> HOLOGRAM = service.getModule("holograms");
    public static Optional<Module> HOME = service.getModule("home");
    //Exempt perm
    public static Optional<Module> IGNORE = service.getModule("ignore");
    public static Optional<Module> INSTANTRESPAWN = service.getModule("instantrespawn");
    public static Optional<Module> INVSEE = service.getModule("invsee");
    public static Optional<Module> ITEM = service.getModule("item");
    public static Optional<Module> JAIL = service.getModule("jail");
    public static Optional<Module> KICK = service.getModule("kick");
    public static Optional<Module> KIT = service.getModule("kit");
    public static Optional<Module> MAIL = service.getModule("mail");
    public static Optional<Module> MOBTP = service.getModule("mobtp");
    //Commands like /accountstatus, /mcservers, etc
    public static Optional<Module> MOJANGSERVICE = service.getModule("mojangservice");
    public static Optional<Module> MOTD = service.getModule("motd");
    public static Optional<Module> MUTE = service.getModule("mute");
    //Change player's nametag
    public static Optional<Module> NAMETAG = service.getModule("nametag");
    public static Optional<Module> NICK = service.getModule("nick");
    public static Optional<Module> NOCLIP = service.getModule("noclip");
    public static Optional<Module> PARTICLE = service.getModule("particle");
    public static Optional<Module> PERFORMANCE = service.getModule("performance");
    //The /playerinfo command which displays a lot of info of a player, clickable to change
    public static Optional<Module> PLAYERINFO = service.getModule("playerinfo");
    public static Optional<Module> PLUGIN = service.getModule("plugin");
    public static Optional<Module> PERSONALMESSAGE = service.getModule("personalmessage");
    public static Optional<Module> POKE = service.getModule("poke");
    //Create portals
    public static Optional<Module> PORTAL = service.getModule("portal");
    //Global and per person
    public static Optional<Module> POWERTOOL = service.getModule("powertool");
    public static Optional<Module> PREGENERATOR = service.getModule("pregenerator");
    //Protect stuff like chests, itemframes, etc (Customizable, obviously)
    public static Optional<Module> PROTECT = service.getModule("protect");
    //Generate random numbers, booleans, strings, etc
    public static Optional<Module> RANDOM = service.getModule("random");
    public static Optional<Module> REPAIR = service.getModule("repair");
    public static Optional<Module> REPORT = service.getModule("report");
    //Schedule commands at specific times of a day
    public static Optional<Module> SCHEDULER = service.getModule("scheduler");
    public static Optional<Module> SCOREBOARD = service.getModule("scoreboard");
    public static Optional<Module> SERVERLIST = service.getModule("serverlist");
    public static Optional<Module> SIGN = service.getModule("sign");
    public static Optional<Module> SOUND = service.getModule("sound");
    //Seperate /firstspawn & /setfirstspawn
    public static Optional<Module> SPAWN = service.getModule("spawn");
    public static Optional<Module> SPAWNMOB = service.getModule("spawnmob");
    public static Optional<Module> SPY = service.getModule("spy");
    //Mogelijkheid om meerdere commands te maken
    public static Optional<Module> STAFFCHAT = service.getModule("staffchat");
    //Better /stop and /restart commands (Time?)
    public static Optional<Module> STOPRESTART = service.getModule("stoprestart");
    public static Optional<Module> SUDO = service.getModule("sudo");
    //Animated, refresh every x seconds
    public static Optional<Module> TABLIST = service.getModule("tablist");
    //Split the /teleport command better
    public static Optional<Module> TELEPORT = service.getModule("teleport");
    //Teleport to a random location, include /biometp
    public static Optional<Module> TELEPORTRANDOM = service.getModule("teleportrandom");
    public static Optional<Module> TIME = service.getModule("time");
    //Timber
    public static Optional<Module> TREE = service.getModule("tree");
    //Change the unknown command message
    public static Optional<Module> UNKNOWNCOMMAND = service.getModule("unknowncommand");
    public static Optional<Module> UPDATE = service.getModule("update");
    public static Optional<Module> VANISH = service.getModule("vanish");
    public static Optional<Module> VILLAGER = service.getModule("villager");
    //Votifier module
    public static Optional<Module> VOTE = service.getModule("vote");
    public static Optional<Module> WARP = service.getModule("warp");
    public static Optional<Module> WEATHER = service.getModule("weather");
    //Stop using flags, use seperate commands & clickable chat interface
    public static Optional<Module> WORLD = service.getModule("world");
    public static Optional<Module> WORLDBORDER = service.getModule("worldborder");
    public static Optional<Module> WORLDINVENTORIES = service.getModule("worldinventories");
    //TODO /smelt command?
}

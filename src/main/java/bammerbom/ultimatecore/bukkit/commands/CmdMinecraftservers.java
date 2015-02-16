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

import bammerbom.ultimatecore.bukkit.commands.ServerCheck.MinecraftServer;
import bammerbom.ultimatecore.bukkit.commands.ServerCheck.Status;
import bammerbom.ultimatecore.bukkit.r;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CmdMinecraftservers implements UltimateCommand {

    static Boolean on = false;
    static ArrayList<MinecraftServer> offline = new ArrayList<>();
    static ArrayList<MinecraftServer> unknown = new ArrayList<>();
    static ArrayList<MinecraftServer> problems = new ArrayList<>();
    static ArrayList<MinecraftServer> online = new ArrayList<>();

    private static void runcheck() {
        //
        on = true;
        Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {
            @Override
            public void run() {
                on = false;
            }
        }, 20 * 10L);
        //
        offline.clear();
        unknown.clear();
        problems.clear();
        online.clear();
        for (MinecraftServer serv : MinecraftServer.values()) {
            Status status = ServerCheck.getStatus(serv);
            if (status.equals(Status.ONLINE)) {
                online.add(serv);
            } else if (status.equals(Status.EXPERIENCE)) {
                problems.add(serv);
            } else if (status.equals(Status.OFFLINE)) {
                offline.add(serv);
            } else if (status.equals(Status.UNKNOWN)) {
                unknown.add(serv);
            }
        }
    }

    @Override
    public String getName() {
        return "minecraftservers";
    }

    @Override
    public String getPermission() {
        return "uc.minecraftservers";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("mcservers");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.minecraftservers", true, true)) {
            return;
        }
        if (!r.getCnfg().getBoolean("Metrics")) {
            r.sendMes(cs, "minecraftserversDisabled");
            return;
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!on) {
                    runcheck();
                }
                String os = "";
                for (MinecraftServer str : online) {
                    if (!os.equals("")) {
                        os = os + ", " + ChatColor.GREEN + str.toString().toLowerCase() + r.positive + "";
                    } else {
                        os = os + ChatColor.GREEN + str.toString().toLowerCase() + r.positive + "";
                    }
                }
                for (MinecraftServer str : problems) {
                    if (!os.equals("")) {
                        os = os + ", " + ChatColor.GOLD + str.toString().toLowerCase() + r.positive + "";
                    } else {
                        os = os + ChatColor.GOLD + str.toString().toLowerCase() + r.positive + "";
                    }
                }
                for (MinecraftServer str : offline) {
                    if (!os.equals("")) {
                        os = os + ", " + ChatColor.DARK_RED + str.toString().toLowerCase() + r.positive + "";
                    } else {
                        os = os + ChatColor.DARK_RED + str.toString().toLowerCase() + r.positive + "";
                    }
                }
                for (MinecraftServer str : unknown) {
                    if (!os.equals("")) {
                        os = os + ", " + ChatColor.GRAY + str.toString().toLowerCase() + r.positive + "";
                    } else {
                        os = os + ChatColor.GRAY + str.toString().toLowerCase() + r.positive + "";
                    }
                }
                r.sendMes(cs, "minecraftserversMessage", "%Servers", ChatColor.RESET + os);

            }
        });
        thread.setName("UltimateCore: Server Check Thread");
        thread.start();
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}

class ServerCheck {

    private static final JSONParser parser = new JSONParser();

    public static Status getStatus(MinecraftServer service) {
        String status;

        try {
            URL url = new URL("http://status.mojang.com/check?service=" + service.getURL());
            BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
            Object object = parser.parse(input);
            JSONObject jsonObject = (JSONObject) object;

            status = (String) jsonObject.get(service.getURL());
        } catch (Exception e) {
            return Status.UNKNOWN;
        }

        return status(status);
    }

    private static Status status(String status) {
        switch (status.toLowerCase()) {
            case "green":
                return Status.ONLINE;

            case "yellow":
                return Status.EXPERIENCE;

            case "red":
                return Status.OFFLINE;

            default:
                return Status.UNKNOWN;
        }
    }

    public enum MinecraftServer {

        //Minecraft
        WEBSITE("minecraft.net"),
        SKIN("skins.minecraft.net"),
        SESSION("session.minecraft.net"),
        //Mojang
        ACCOUNT("account.mojang.com"),
        AUTH("auth.mojang.com"),
        AUTHSERVER("authserver.mojang.com"),
        MOJANGSESSION("sessionserver.mojang.com");

        private final String url;

        MinecraftServer(String url) {
            this.url = url;
        }

        private String getURL() {
            return url;
        }
    }

    public enum Status {

        ONLINE("No problems detected!"),
        EXPERIENCE("May be experiencing issues"),
        OFFLINE("Experiencing problems!"),
        UNKNOWN("Couldn't connect to Mojang!");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

    }

}

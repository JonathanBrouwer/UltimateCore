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
package bammerbom.ultimatecore.spongeapi.listeners;

import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.MinecraftServerUtil;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class MinecraftServerListener {
    public static void start() {
        if (!r.getCnfg().getBoolean("Mojang")) {
            return;
        }
        if (!r.getCnfg().getBoolean("MojangServersBroadcasting")) {
            return;
        }
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(r.getUC(), new Runnable() {
            public void run() {
                final ArrayList<MinecraftServerUtil.MinecraftServer> offline = (ArrayList<MinecraftServerUtil.MinecraftServer>) MinecraftServerUtil.offline.clone();
                final ArrayList<MinecraftServerUtil.MinecraftServer> unknown = (ArrayList<MinecraftServerUtil.MinecraftServer>) MinecraftServerUtil.unknown.clone();
                final ArrayList<MinecraftServerUtil.MinecraftServer> problems = (ArrayList<MinecraftServerUtil.MinecraftServer>) MinecraftServerUtil.problems.clone();
                final ArrayList<MinecraftServerUtil.MinecraftServer> online = (ArrayList<MinecraftServerUtil.MinecraftServer>) MinecraftServerUtil.online.clone();
                MinecraftServerUtil.runcheck();
                final ArrayList<MinecraftServerUtil.MinecraftServer> noffline = MinecraftServerUtil.offline;
                final ArrayList<MinecraftServerUtil.MinecraftServer> nunknown = MinecraftServerUtil.unknown;
                final ArrayList<MinecraftServerUtil.MinecraftServer> nproblems = MinecraftServerUtil.problems;
                final ArrayList<MinecraftServerUtil.MinecraftServer> nonline = MinecraftServerUtil.online;
                for (MinecraftServerUtil.MinecraftServer server : MinecraftServerUtil.MinecraftServer.values()) {
                    if (server.equals(MinecraftServerUtil.MinecraftServer.WEBSITE) || server.equals(MinecraftServerUtil.MinecraftServer.MOJANG) || server.equals(MinecraftServerUtil.MinecraftServer.API)) {
                        continue;
                    }
                    if (server.equals(MinecraftServerUtil.MinecraftServer.AUTHSERVER) && MinecraftServerUtil.getStatus(MinecraftServerUtil.MinecraftServer.AUTH) == MinecraftServerUtil.Status.OFFLINE) {
                        continue;
                    }
                    if (server.equals(MinecraftServerUtil.MinecraftServer.SESSIONSERVER) && MinecraftServerUtil.getStatus(MinecraftServerUtil.MinecraftServer.SESSION) == MinecraftServerUtil.Status.OFFLINE) {
                        continue;
                    }
                    MinecraftServerUtil.Status old = getStatus(offline, unknown, problems, online, server);
                    MinecraftServerUtil.Status new_ = getStatus(noffline, nunknown, nproblems, nonline, server);
                    if (!old.equals(new_)) {
                        if (old.equals(MinecraftServerUtil.Status.UNKNOWN)) {
                            continue;
                        }
                        if (new_.equals(MinecraftServerUtil.Status.OFFLINE) || new_.equals(MinecraftServerUtil.Status.UNKNOWN)) {
                            Bukkit.broadcastMessage(r.mes("minecraftserversOffline", "%Service", server.toString().toLowerCase(), "%Tip", MinecraftServerUtil
                                    .getTip(MinecraftServerUtil.Status.OFFLINE, server) != null ? r.mes(MinecraftServerUtil.getTip(MinecraftServerUtil.Status.OFFLINE, server)) : ""));
                        } else if (new_.equals(MinecraftServerUtil.Status.EXPERIENCE)) {
                            Bukkit.broadcastMessage(r.mes("minecraftserversUnstable", "%Service", server.toString().toLowerCase(), "%Tip", MinecraftServerUtil
                                    .getTip(MinecraftServerUtil.Status.EXPERIENCE, server) != null ? r.mes(MinecraftServerUtil.getTip(MinecraftServerUtil.Status.EXPERIENCE, server)) : ""));
                        } else if (new_.equals(MinecraftServerUtil.Status.ONLINE)) {
                            Bukkit.broadcastMessage(r.mes("minecraftserversOnline", "%Service", server.toString().toLowerCase(), "%Tip", MinecraftServerUtil
                                    .getTip(MinecraftServerUtil.Status.ONLINE, server) != null ? r.mes(MinecraftServerUtil.getTip(MinecraftServerUtil.Status.ONLINE, server)) : ""));
                        }
                    }
                }
            }
        }, 20L * r.getCnfg().getLong("MojangServersDelay"), 20L * r.getCnfg().getLong("MojangServersDelay"));
    }

    private static MinecraftServerUtil.Status getStatus(ArrayList<MinecraftServerUtil.MinecraftServer> offline, ArrayList<MinecraftServerUtil.MinecraftServer> unknown, ArrayList<MinecraftServerUtil
            .MinecraftServer> problems, ArrayList<MinecraftServerUtil.MinecraftServer> online, MinecraftServerUtil.MinecraftServer server) {
        if (offline.contains(server)) {
            return MinecraftServerUtil.Status.OFFLINE;
        } else if (unknown.contains(server)) {
            return MinecraftServerUtil.Status.UNKNOWN;
        } else if (problems.contains(server)) {
            return MinecraftServerUtil.Status.EXPERIENCE;
        } else if (online.contains(server)) {
            return MinecraftServerUtil.Status.ONLINE;
        } else {
            return MinecraftServerUtil.Status.ONLINE; //This will make sure the message is only send when the server is offline
        }
    }
}

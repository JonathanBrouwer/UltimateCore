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

import bammerbom.ultimatecore.spongeapi.r;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CmdAccountstatus implements UltimateCommand {

    @Override
    public String getName() {
        return "accountstatus";
    }

    @Override
    public String getPermission() {
        return "uc.accountstatus";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("acstatus");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.accountstatus", false, true)) {
            return;
        }
        if (!r.getCnfg().getBoolean("Mojang")) {
            r.sendMes(cs, "accountstatusDisabled");
            return;
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "accountstatusUsage");
            return;
        }
        final OfflinePlayer pl = r.searchOfflinePlayer(args[0]);
        Thread t = new Thread(new Runnable() {
            public void run() {
                URL u;
                try {
                    u = new URL("https://minecraft.net/haspaid.jsp?user=" + URLEncoder.encode(pl.getName(), "UTF-8"));
                } catch (MalformedURLException | UnsupportedEncodingException ex) {
                    r.sendMes(cs, "accountstatusFailedSupport");
                    return;
                }
                boolean premium;
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
                    premium = br.readLine().equalsIgnoreCase("true");
                } catch (IOException ex) {
                    r.sendMes(cs, "accountstatusFailedConnect");
                    return;
                }
                String status = premium ? r.mes("accountstatusPremium") : r.mes("accountstatusNotPremium");
                r.sendMes(cs, "accountstatusMessage", "%Player", pl.getName(), "%Status", status);
            }
        });
        t.setName("UltimateCore: AccountStatus thread");
        t.start();
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        if (curn == 0) {
            return null;
        }
        return new ArrayList<>();
    }
}

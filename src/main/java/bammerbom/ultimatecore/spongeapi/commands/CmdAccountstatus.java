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

import bammerbom.ultimatecore.spongeapi.UltimateCommandExecutor;
import bammerbom.ultimatecore.spongeapi.r;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.util.command.CommandSource;

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

public class CmdAccountstatus implements UltimateCommandExecutor {

    @Override
    public String getName() {
        return "accountstatus";
    }

    @Override
    public String getPermission() {
        return "uc.accountstatus";
    }

    @Override
    public String getUsage() {
        return "/accountstatus <User>";
    }

    @Override
    public String getDescription() {
        return "Checks if an account is premium or not.";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("acstatus");
    }

    @Override
    public void run(final CommandSource cs, String label, final String[] args) {
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
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                URL u;
                try {
                    u = new URL("https://minecraft.net/haspaid.jsp?user=" + URLEncoder.encode(args[0], "UTF-8"));
                } catch (MalformedURLException|UnsupportedEncodingException ex) {
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
                String status = premium ? r.mes("accountstatusPremium").toString() : r.mes("accountstatusNotPremium").toString();
                r.sendMes(cs, "accountstatusMessage", "%Player", args[0], "%Status", status);
            }
        });
        t.setName("UltimateCore: AccountStatus thread");
        t.start();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String[] args, String label, String curs, Integer curn) {
        List<String> rtrn = null;
        if (rtrn == null) {
            rtrn = new ArrayList<>();
            for (Player p : r.getOnlinePlayers()) {
                rtrn.add(p.getName());
            }
        }
        return rtrn;
    }

}
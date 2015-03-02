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

import bammerbom.ultimatecore.spongeapi.UltimateCommands;
import bammerbom.ultimatecore.spongeapi.r;
import com.google.common.base.Optional;
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
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

public class CmdAccountstatus implements CommandCallable {

    public String getName() {
        return "accountstatus";
    }

    public String getPermission() {
        return "uc.accountstatus";
    }

    @Override
    public String getUsage() {
        return "/<command> <User>";
    }

    @Override
    public Optional<String> getShortDescription() {
        return Optional.of("Check if an user is premium.");
    }

    public List<String> getAliases() {
        return Arrays.asList("acstatus");
    }

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
                r.sendMes(cs, "accountstatusMessage", "%Player", args[0], "%Status", status);
            }
        });
        t.setName("UltimateCore: AccountStatus thread");
        t.start();
    }

    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }

    //Ignore
    @Override
    public boolean call(CommandSource cs, String arg, List<String> parents) throws CommandException {
        run(cs, parents.get(0), UltimateCommands.convertsArgs(arg));
        return true;
    }

    @Override
    public List<String> getSuggestions(CommandSource cs, String arg) throws CommandException {
        String[] args = UltimateCommands.convertsArgs(arg);
        List<String> tabs = onTabComplete(cs, "x", args, args[args.length - 1], args.length - 1); //TODO
        return tabs == null ? new ArrayList<String>() : tabs;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission(getPermission());
    }

    @Override
    public Optional<String> getHelp() {
        return getShortDescription();
    }

}

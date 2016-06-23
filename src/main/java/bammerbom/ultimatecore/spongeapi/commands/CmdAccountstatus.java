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
import bammerbom.ultimatecore.spongeapi.r;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

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
    public String getUsage() {
        return "/<command> <Player>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Checks if an account is premium or not.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("acstatus");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.accountstatus", true)) {
            return CommandResult.empty();
        }
        if (!r.getCnfg().getBoolean("Mojang")) {
            r.sendMes(cs, "accountstatusDisabled");
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "accountstatusUsage");
            return CommandResult.empty();
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                URL u;
                try {
                    u = new URL("https://api.mojang.com/users/profiles/minecraft/" + URLEncoder.encode(args[0], "UTF-8"));
                } catch (MalformedURLException | UnsupportedEncodingException ex) {
                    r.sendMes(cs, "accountstatusFailedSupport");
                    return;
                }
                JSONObject json;
                boolean premium;
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
                    String jsonstring = br.readLine();
                    if (jsonstring == null || jsonstring.isEmpty()) {
                        premium = false;
                    } else {
                        JSONParser parser = new JSONParser();
                        try {
                            json = (JSONObject) parser.parse(jsonstring);
                            premium = json.get("id").toString().length() == 32;
                        } catch (ParseException e) {
                            e.printStackTrace();
                            r.sendMes(cs, "accountstatusFailedConnect");
                            return;
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    r.sendMes(cs, "accountstatusFailedConnect");
                    return;
                }
                Text status = premium ? r.mes("accountstatusPremium") : r.mes("accountstatusNotPremium");
                r.sendMes(cs, "accountstatusMessage", "%Player", args[0], "%Status", status);
            }
        });
        t.setName("UltimateCore: AccountStatus thread");
        t.start();
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
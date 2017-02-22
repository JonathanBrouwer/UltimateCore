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
package bammerbom.ultimatecore.sponge.modules.commandtimer.listeners;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.command.event.CommandExecuteEvent;
import bammerbom.ultimatecore.sponge.api.command.event.CommandPostExecuteEvent;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.variable.utils.TimeUtil;
import bammerbom.ultimatecore.sponge.modules.commandtimer.api.CommandtimerKeys;
import bammerbom.ultimatecore.sponge.modules.commandtimer.api.Warmup;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;

import java.util.HashMap;
import java.util.Optional;

public class CommandtimerListener {

    @Listener
    public void onPreCommand(CommandExecuteEvent event, @First Player src) {
        HashMap<Command, Long> warmups = GlobalData.get(CommandtimerKeys.WARMUPS).get();
        HashMap<Command, Long> cooldowns = GlobalData.get(CommandtimerKeys.COOLDOWNS).get();
        Long warmuptime = Optional.ofNullable(warmups.get(event.getCommand())).orElse(0L);
        Long cooldown = Optional.ofNullable(cooldowns.get(event.getCommand())).orElse(0L);

        UltimateUser user = UltimateCore.get().getUserService().getUser(src);
        HashMap<String, Long> userlastexecuteds = user.get(CommandtimerKeys.USER_LASTEXECUTED).get();
        HashMap<String, Warmup> userwarmups = user.get(CommandtimerKeys.USER_WARMUPS).get();
        Long userlastexecuted = Optional.ofNullable(userlastexecuteds.get(event.getCommand().getFullIdentifier())).orElse(0L);
        Optional<Warmup> userwarmup = Optional.ofNullable(userwarmups.get(event.getCommand().getFullIdentifier()));

        //Check cooldown
        if (userlastexecuted + cooldown > System.currentTimeMillis() && !src.hasPermission("uc.commandtimer.bypass.cooldown." + event.getCommand().getFullIdentifier())) {
            Messages.send(src, "commandtimer.cooldown", "%time%", TimeUtil.format(userlastexecuted + cooldown - System.currentTimeMillis()));
            event.setCancelled(true);
            return;
        }

        if (warmuptime > 0 && !src.hasPermission("uc.commandtimer.bypass.warmup." + event.getCommand().getFullIdentifier())) {
            //Create warmup
            Warmup warmup = new Warmup(event.getCommand(), event.getContext(), src, System.currentTimeMillis(), System.currentTimeMillis() + warmuptime);
            warmup.startTimer();
            userwarmups.put(event.getCommand().getFullIdentifier(), warmup);
            user.offer(CommandtimerKeys.USER_WARMUPS, userwarmups);
            event.setCancelled(true);
            Messages.send(src, "commandtimer.warmup", "%time%", TimeUtil.format(warmuptime));
        }
    }

    @Listener
    public void onPostCommand(CommandPostExecuteEvent event, @First Player src) {
        HashMap<Command, Long> warmups = GlobalData.get(CommandtimerKeys.WARMUPS).get();
        HashMap<Command, Long> cooldowns = GlobalData.get(CommandtimerKeys.COOLDOWNS).get();
        Long warmuptime = Optional.ofNullable(warmups.get(event.getCommand())).orElse(0L);
        Long cooldown = Optional.ofNullable(cooldowns.get(event.getCommand())).orElse(0L);

        UltimateUser user = UltimateCore.get().getUserService().getUser(src);
        HashMap<String, Long> userlastexecuteds = user.get(CommandtimerKeys.USER_LASTEXECUTED).get();
        HashMap<String, Warmup> userwarmups = user.get(CommandtimerKeys.USER_WARMUPS).get();
        Long userlastexecuted = Optional.ofNullable(userlastexecuteds.get(event.getCommand().getFullIdentifier())).orElse(0L);
        Optional<Warmup> userwarmup = Optional.ofNullable(userwarmups.get(event.getCommand().getFullIdentifier()));

        //Remove warmup
        if (userwarmup.isPresent()) {
            userwarmups.remove(event.getCommand().getFullIdentifier());
            user.offer(CommandtimerKeys.USER_WARMUPS, userwarmups);
        }

        //If successful, add cooldown to user
        if (event.getResult() != null && event.getResult().getSuccessCount().orElse(0) > 0) {
            userlastexecuteds.put(event.getCommand().getFullIdentifier(), System.currentTimeMillis());
            user.offer(CommandtimerKeys.USER_LASTEXECUTED, userlastexecuteds);
        }
    }
}

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
package bammerbom.ultimatecore.sponge.modules.commandtimer.api;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.event.CommandPostExecuteEvent;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandPermissionException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static org.spongepowered.api.command.CommandMessageFormatting.error;

public class Warmup {
    private HighCommand command;
    private CommandContext context;
    private Player source;
    private Long starttime;
    private Long endtime;

    private boolean cancelled = false;
    private boolean hasStarted = false;

    public Warmup(HighCommand command, CommandContext context, Player source, Long starttime, Long endtime) {
        this.command = command;
        this.context = context;
        this.source = source;
        this.starttime = starttime;
        this.endtime = endtime;
    }

    public HighCommand getCommand() {
        return command;
    }

    public CommandContext getContext() {
        return context;
    }

    public CommandSource getSource() {
        return source;
    }

    public Long getStarttime() {
        return starttime;
    }

    public Long getEndtime() {
        return endtime;
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public void startTimer() {
        if (hasStarted()) throw new IllegalStateException();
        hasStarted = true;
        Sponge.getScheduler().createTaskBuilder().delay(endtime - starttime, TimeUnit.MILLISECONDS).execute(this::finishWarmup).submit(UltimateCore.get());
    }

    private CommandResult finishWarmup() {
        if (cancelled) return CommandResult.empty();
        try {
            CommandResult result = command.execute(source, context);
            CommandPostExecuteEvent pEvent = new CommandPostExecuteEvent(command, context, result, Cause.builder().notifier(UltimateCore.get()).named(NamedCause.simulated(source)).build());
            Sponge.getEventManager().post(pEvent);
            return pEvent.getResult();
        } catch (CommandPermissionException ex) {
            Text text = ex.getText();
            if (text != null) {
                source.sendMessage(error(text));
            }
            return CommandResult.empty();
        } catch (CommandException ex) {
            Text text = ex.getText();
            if (text != null) {
                source.sendMessage(error(text));
            }

            if (ex.shouldIncludeUsage()) {
                source.sendMessage(error(Text.of("Usage: " + command.getUsage(source))));
            }
            return CommandResult.empty();
        }
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        cancelled = true;
        UltimateUser user = UltimateCore.get().getUserService().getUser(source);
        HashMap<String, Warmup> warmups = user.get(CommandtimerKeys.USER_WARMUPS).get();
        warmups.remove(command.getFullIdentifier());
        user.offer(CommandtimerKeys.USER_WARMUPS, warmups);
    }
}

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
        return this.command;
    }

    public CommandContext getContext() {
        return this.context;
    }

    public CommandSource getSource() {
        return this.source;
    }

    public Long getStarttime() {
        return this.starttime;
    }

    public Long getEndtime() {
        return this.endtime;
    }

    public boolean hasStarted() {
        return this.hasStarted;
    }

    public void startTimer() {
        if (hasStarted()) throw new IllegalStateException();
        this.hasStarted = true;
        Sponge.getScheduler().createTaskBuilder().delay(this.endtime - this.starttime, TimeUnit.MILLISECONDS).execute(this::finishWarmup).submit(UltimateCore.get());
    }

    private CommandResult finishWarmup() {
        if (this.cancelled) return CommandResult.empty();
        try {
            CommandResult result = this.command.execute(this.source, this.context);
            CommandPostExecuteEvent pEvent = new CommandPostExecuteEvent(this.command, this.context, result, Cause.builder().owner(UltimateCore.getContainer()).named(NamedCause.simulated(this.source)).build());
            Sponge.getEventManager().post(pEvent);
            return pEvent.getResult();
        } catch (CommandPermissionException ex) {
            Text text = ex.getText();
            if (text != null) {
                this.source.sendMessage(error(text));
            }
            return CommandResult.empty();
        } catch (CommandException ex) {
            Text text = ex.getText();
            if (text != null) {
                this.source.sendMessage(error(text));
            }

            if (ex.shouldIncludeUsage()) {
                this.source.sendMessage(error(Text.of("Usage: " + this.command.getUsage(this.source))));
            }
            return CommandResult.empty();
        }
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void cancel() {
        this.cancelled = true;
        UltimateUser user = UltimateCore.get().getUserService().getUser(this.source);
        HashMap<String, Warmup> warmups = user.get(CommandtimerKeys.USER_WARMUPS).get();
        warmups.remove(this.command.getFullIdentifier());
        user.offer(CommandtimerKeys.USER_WARMUPS, warmups);
    }
}

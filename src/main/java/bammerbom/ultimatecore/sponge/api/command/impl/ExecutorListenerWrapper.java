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
package bammerbom.ultimatecore.sponge.api.command.impl;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.event.CommandExecuteEvent;
import bammerbom.ultimatecore.sponge.api.command.event.CommandPostExecuteEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;

public class ExecutorListenerWrapper implements CommandExecutor {
    private HighCommand command;
    private CommandExecutor executor;

    public ExecutorListenerWrapper(HighCommand cmd, CommandExecutor exe) {
        this.command = cmd;
        this.executor = exe;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (Sponge.getEventManager().post(new CommandExecuteEvent(this.command, args, Cause.builder().append(UltimateCore.getContainer()).append(src).build(EventContext.builder().build())))) {
            //Event canceller is expected to send message
            return CommandResult.empty();
        }
        CommandResult result = this.executor.execute(src, args);
        CommandPostExecuteEvent pEvent = new CommandPostExecuteEvent(this.command, args, result, Cause.builder().append(UltimateCore.getContainer()).append(src).build(EventContext.builder().build()));
        Sponge.getEventManager().post(pEvent);
        return pEvent.getResult();
    }
}

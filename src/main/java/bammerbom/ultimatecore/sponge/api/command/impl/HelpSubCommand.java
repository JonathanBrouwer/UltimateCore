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

import bammerbom.ultimatecore.sponge.api.command.HighPermCommand;
import bammerbom.ultimatecore.sponge.api.command.HighSubCommand;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.module.Module;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpSubCommand implements HighSubCommand {
    private HighPermCommand cmd;

    public HelpSubCommand(HighPermCommand cmd) {
        this.cmd = cmd;
    }

    @Override
    public String getIdentifier() {
        return "?";
    }

    @Override
    public Module getModule() {
        return cmd.getModule();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("?", "help");
    }

    @Override
    public HighPermCommand getParent() {
        return cmd;
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[0];
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        List<Text> texts = new ArrayList<>();
        //Add entries to texts
        texts.add(Messages.getFormatted(src, "core.help.module", "%module%", cmd.getModule().getIdentifier()));
        texts.add(Text.of());
        texts.add(Messages.getFormatted(src, "core.help.description", "%description%", cmd.getLongDescription(src)));
        texts.add(Text.of());
        texts.add(Messages.getFormatted(src, "core.help.usage", "%usages%", getUsages(src)));
        //Send page
        PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
        PaginationList paginationList = paginationService.builder().contents(texts).title(Messages.getFormatted(src, "core.help.header", "%command%", cmd.getFullIdentifier()).toBuilder().color(TextColors.DARK_GREEN).build()).build();
        paginationList.sendTo(src);
        return CommandResult.success();
    }

    private Text getUsages(CommandSource src) {
        //TODO per child
        Text.Builder text = Text.builder();
        text.append(cmd.getUsage(src));
        return text.build();
    }
}

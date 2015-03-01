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
import com.google.common.base.Optional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

public class Cmd implements CommandCallable {

    public String getName() {
        return "";
    }

    public String getPermission() {
        return "uc.";
    }

    @Override
    public String getUsage() {
        return "/<command> ";
    }

    @Override
    public Optional<String> getShortDescription() {
        return Optional.of("");
    }

    public List<String> getAliases() {
        return Arrays.asList();
    }

    public void run(final CommandSource cs, String label, String[] args) {

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

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
package bammerbom.ultimatecore.sponge.impl.command;

import bammerbom.ultimatecore.sponge.api.command.Command;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UCCommandCallable implements CommandCallable {

    Command command;

    public UCCommandCallable(Command command) {
        this.command = command;
    }

    /**
     * Execute the command based on input arguments.
     * <p>
     * The implementing class must perform the necessary permission
     * checks.</p>
     *
     * @param source    The caller of the command
     * @param arguments The raw arguments for this command
     * @return The result of a command being processed
     * @throws CommandException Thrown on a command error
     */
    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        CommandResult preResult = command.runPre(source, arguments.split(" "));
        if (preResult.equals(CommandResult.success())) {
            return command.run(source, arguments.split(" "));
        }
        return preResult;
    }

    /**
     * Get a list of suggestions based on input.
     * <p>
     * If a suggestion is chosen by the user, it will replace the last
     * word.</p>
     *
     * @param source    The command source
     * @param arguments The arguments entered up to this point
     * @param loc       The position the source is looking at when performing tab completion
     * @return A list of suggestions
     * @throws CommandException Thrown if there was a parsing error
     */
    @Override
    public List<String> getSuggestions(CommandSource source, String arguments, Location<World> loc) throws CommandException {
        String[] args = arguments.split(" ");
        List<String> rtrn = command.onTabComplete(source, args, args[args.length - 1], args.length - 1);
        if (rtrn == null) {
            rtrn = new ArrayList<>();
            for (Player p : Sponge.getServer().getOnlinePlayers()) {
                rtrn.add(p.getName());
            }
        }
        return rtrn;
    }

    /**
     * Test whether this command can probably be executed by the given source.
     * <p>
     * If implementations are unsure if the command can be executed by
     * the source, {@code true} should be returned. Return values of this method
     * may be used to determine whether this command is listed in command
     * listings.</p>
     *
     * @param source The caller of the command
     * @return Whether permission is (probably) granted
     */
    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission(command.getPermission().get());
    }

    /**
     * Get a short one-line description of this command.
     * <p>
     * The help system may display the description in the command list.</p>
     *
     * @param source The source of the help request
     * @return A description
     */
    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(command.getShortDescription());
    }

    /**
     * Get a longer formatted help message about this command.
     * <p>
     * It is recommended to use the default text color and style. Sections
     * with text actions (e.g. hyperlinks) should be underlined.</p>
     * <p>
     * Multi-line messages can be created by separating the lines with
     * {@code \n}.</p>
     * <p>
     * The help system may display this message when a source requests
     * detailed information about a command.</p>
     *
     * @param source The source of the help request
     * @return A help text
     */
    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(command.getLongDescription());
    }

    /**
     * Get the usage string of this command.
     * <p>
     * A usage string may look like
     * {@code [-w &lt;world&gt;] &lt;var1&gt; &lt;var2&gt;}.</p>
     * <p>
     * The string must not contain the command alias.</p>
     *
     * @param source The source of the help request
     * @return A usage string
     */
    @Override
    public Text getUsage(CommandSource source) {
        return command.getUsage();
    }
}

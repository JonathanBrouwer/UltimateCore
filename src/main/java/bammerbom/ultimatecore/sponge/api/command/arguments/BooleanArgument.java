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
package bammerbom.ultimatecore.sponge.api.command.arguments;

import bammerbom.ultimatecore.sponge.api.command.UCommandElement;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class BooleanArgument extends UCommandElement {
    public BooleanArgument(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    public Boolean parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String var = args.next();
        switch (var.toLowerCase()) {
            case "true":
            case "t":
            case "on":
            case "yes":
            case "y":
            case "verymuchso":
            case "enable":
            case "enabled":
                return true;
            case "false":
            case "f":
            case "off":
            case "no":
            case "n":
            case "notatall":
            case "disable":
            case "disabled":
                return false;
            default:
                throw args.createError(Messages.getFormatted("core.booleaninvalid", "%argument%", var));
        }
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return Arrays.asList("true", "false", "on", "off", "yes", "no");
    }
}

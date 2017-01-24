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
package bammerbom.ultimatecore.sponge.api.command.argument.wrappers;

import bammerbom.ultimatecore.sponge.api.command.argument.UCommandElement;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.spongepowered.api.command.CommandMessageFormatting;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class FirstParsingWrapper extends Wrapper {
    List<UCommandElement> elements;

    public FirstParsingWrapper(List<UCommandElement> elements) {
        super(elements.get(0));
        this.elements = elements;
    }

    @Nullable
    public Text getKey() {
        return element.getKey();
    }

    @Nullable
    public String getUntranslatedKey() {
        return element.getUntranslatedKey();
    }

    public void parse(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
        ArgumentParseException lastException = null;
        for (CommandElement element : this.elements) {
            Object startState = args.getState();
            try {
                element.parse(source, args, context);
                return;
            } catch (ArgumentParseException ex) {
                lastException = ex;
                args.setState(startState);
            }
        }
        if (lastException != null) {
            throw lastException;
        }
    }

    @Nullable
    public Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        ArgumentParseException lastException = null;
        for (UCommandElement element : this.elements) {
            Object startState = args.getState();
            try {
                Object rtrn = element.parseValue(source, args);
                return rtrn;
            } catch (ArgumentParseException ex) {
                lastException = ex;
                args.setState(startState);
            }
        }
        throw lastException;
    }

    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return ImmutableList.copyOf(Iterables.concat(Iterables.transform(this.elements, input -> {
            if (input == null) {
                return ImmutableList.of();
            }

            Object startState = args.getState();
            List<String> ret = input.complete(src, args, context);
            args.setState(startState);
            return ret;
        })));
    }

    public Text getUsageKey(CommandSource src) {
        final Text.Builder ret = Text.builder();
        for (Iterator<UCommandElement> it = this.elements.iterator(); it.hasNext(); ) {
            ret.append(it.next().getUsageKey(src));
            if (it.hasNext()) {
                ret.append(CommandMessageFormatting.PIPE_TEXT);
            }
        }
        return ret.build();
    }
}
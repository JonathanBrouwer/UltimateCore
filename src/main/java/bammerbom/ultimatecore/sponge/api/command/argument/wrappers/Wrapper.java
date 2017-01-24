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
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.List;

public class Wrapper extends UCommandElement {
    protected final UCommandElement element;

    public Wrapper(UCommandElement element) {
        super(element.getKey());
        this.element = element;
    }

    public UCommandElement getWrappingElement() {
        return element;
    }

    @Nullable
    @Override
    public Text getKey() {
        return element.getKey();
    }

    @Nullable
    @Override
    public String getUntranslatedKey() {
        return element.getUntranslatedKey();
    }

    @Override
    public void parse(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
        element.parse(source, args, context);
    }

    @Nullable
    public Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        return element.parseValue(source, args);
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return element.complete(src, args, context);
    }

    @Override
    public Text getUsage(CommandSource src) {
        return element.getUsage(src);
    }

    @Override
    public Text getUsageKey(CommandSource src) {
        return element.getUsageKey(src);
    }
}

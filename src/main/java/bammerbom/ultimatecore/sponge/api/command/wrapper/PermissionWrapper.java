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
package bammerbom.ultimatecore.sponge.api.command.wrapper;

import bammerbom.ultimatecore.sponge.api.command.UCommandElement;
import com.google.common.collect.ImmutableList;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.List;

import static org.spongepowered.api.util.SpongeApiTranslationHelper.t;

public class PermissionWrapper extends Wrapper {
    private final String permission;

    public PermissionWrapper(UCommandElement element, String permission) {
        super(element);
        this.permission = permission;
    }

    @Nullable
    @Override
    public Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        checkPermission(source, args);
        return this.element.parseValue(source, args);
    }

    private void checkPermission(CommandSource source, CommandArgs args) throws ArgumentParseException {
        if (!source.hasPermission(this.permission)) {
            Text key = getKey();
            throw args.createError(t("You do not have permission to use the %s argument", key != null ? key : t("unknown")));
        }
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        if (!src.hasPermission(this.permission)) {
            return ImmutableList.of();
        }
        return this.element.complete(src, args, context);
    }

    @Override
    public void parse(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
        checkPermission(source, args);
        this.element.parse(source, args, context);
    }

    @Override
    public Text getUsage(CommandSource src) {
        return this.element.getUsage(src);
    }
}
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
package bammerbom.ultimatecore.sponge.api.command.argument.arguments;

import bammerbom.ultimatecore.sponge.api.command.argument.UCommandElement;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CatalogedArgument extends UCommandElement {
    private Class<? extends CatalogType> clas;
    private String message;

    public CatalogedArgument(@Nullable Text key, Class<? extends CatalogType> type, String message) {
        super(key);
        this.clas = type;
        this.message = message;
    }

    @Nullable
    @Override
    public Object parseValue(CommandSource sender, CommandArgs args) throws ArgumentParseException {
        String value = args.next();
        Optional type = Sponge.getRegistry().getType(this.clas, value);
        if (!type.isPresent()) {
            throw args.createError(Messages.getFormatted(sender, this.message, "%type%", value));
        }
        return type.get();
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return Sponge.getRegistry().getAllOf(this.clas).stream().map(CatalogType::getId).map(id -> id.startsWith("minecraft:") ? id.replaceFirst("minecraft:", "") : id).collect(Collectors.toList());
    }
}

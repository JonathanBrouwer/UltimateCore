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
package bammerbom.ultimatecore.sponge.api.command;

import bammerbom.ultimatecore.sponge.api.command.impl.LowCommandCallable;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.UsageGenerator;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.List;

public interface LowCommand extends Command {

    Module getModule();

    default String getFullIdentifier() {
        return getIdentifier();
    }

    String getIdentifier();

    Permission getPermission();

    List<Permission> getPermissions();

    List<String> getAliases();

    default CommandCallable getCallable() {
        return new LowCommandCallable(this);
    }

    default Text getUsage(@Nullable CommandSource source) {
        return UsageGenerator.usage(this, Messages.getFormatted(getModule().getIdentifier() + ".command." + getIdentifier() + ".usage"));
    }

    default Text getShortDescription(@Nullable CommandSource source) {
        return UsageGenerator.shortDescription(this, Messages.getFormatted(getModule().getIdentifier() + ".command." + getIdentifier() + ".shortdescription"));
    }

    default Text getLongDescription(@Nullable CommandSource source) {
        //TODO automatically generate longdescription?
        return UsageGenerator.longDescription(this, Messages.getFormatted(getModule().getIdentifier() + ".command." + getIdentifier() + ".longdescription"));
    }

    CommandResult run(CommandSource sender, String[] args);

    List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn);
}

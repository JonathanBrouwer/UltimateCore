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
package bammerbom.ultimatecore.sponge.utils;

import bammerbom.ultimatecore.sponge.api.command.Command;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Command message generator
 */
public class CMGenerator {
    public static Text usage(Command cmd, Text usage) {
        try {
            return usage.toBuilder().onHover(TextActions.showText(Messages.getFormatted("core.usage.hover"))).onClick(TextActions.openUrl(new URL("http://ultimatecore.org/features/cmd/" +
                    cmd.getIdentifier()))).color(TextColors.RED).build();
        } catch (MalformedURLException e) {
            //Idk what happened here
            //TODO ErrorLogger
            e.printStackTrace();
            return usage.toBuilder().onHover(TextActions.showText(Messages.getFormatted("core.usage.hover"))).color(TextColors.RED).build();
        }
    }

    public static Text shortDescription(Command cmd, Text desc) {
        try {
            return desc.toBuilder().onHover(TextActions.showText(Messages.getFormatted("core.shortdescription.hover"))).onClick(TextActions.openUrl(new URL("http://ultimatecore.org" +
                    "/features/cmd/" + cmd.getIdentifier()))).build();
        } catch (MalformedURLException e) {
            //Idk what happened here
            //TODO ErrorLogger
            e.printStackTrace();
            return desc.toBuilder().onHover(TextActions.showText(Messages.getFormatted("core.shortdescription.hover"))).build();
        }
    }

    public static Text longDescription(Command cmd, Text desc) {
        return shortDescription(cmd, desc);
    }
}

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

import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;

import javax.annotation.Nullable;

public class Arguments {

    CommandElement element;
    boolean onlyOne = false;
    ArgOptional optional = ArgOptional.REQUIRED;
    String permission = null;
    int repeat = 0;

    //Builder
    public static Arguments builder(CommandElement element) {
        return new Arguments(element);
    }

    public CommandElement build() {
        CommandElement ce = element;
        if (onlyOne) {
            ce = GenericArguments.onlyOne(ce);
        }
        if (optional.equals(ArgOptional.OPTIONAL)) {
            ce = GenericArguments.optional(element);
        } else if (optional.equals(ArgOptional.WEAK_OPTIONAL)) {
            ce = GenericArguments.optionalWeak(element);
        }
        if (permission != null) {
            ce = GenericArguments.requiringPermission(element, permission);
        }
        if (repeat > 0) {
            ce = GenericArguments.repeated(element, repeat);
        }
        return ce;
    }

    protected Arguments(CommandElement element) {
        this.element = element;
    }

    //Only one
    public boolean isOnlyOne() {
        return onlyOne;
    }

    public Arguments setOnlyOne(boolean onlyOne) {
        this.onlyOne = onlyOne;
        return this;
    }

    public Arguments onlyOne() {
        onlyOne = true;
        return this;
    }

    public Arguments multiple() {
        onlyOne = false;
        return this;
    }

    //Optional
    public ArgOptional getOptional() {
        return optional;
    }

    public Arguments setOptional(ArgOptional opt) {
        optional = opt;
        return this;
    }

    public Arguments required() {
        optional = ArgOptional.REQUIRED;
        return this;
    }

    public Arguments optional() {
        optional = ArgOptional.OPTIONAL;
        return this;
    }

    public Arguments optionalWeak() {
        optional = ArgOptional.WEAK_OPTIONAL;
        return this;
    }

    public enum ArgOptional {
        REQUIRED, OPTIONAL, WEAK_OPTIONAL
    }

    //Permission
    @Nullable
    public String getPermission() {
        return permission;
    }

    public Arguments permission(@Nullable String permission) {
        this.permission = permission;
        return this;
    }

    //Repeat
    public int getRepeat() {
        return repeat;
    }

    public Arguments repeat(int repeat) {
        this.repeat = repeat;
        return this;
    }
}

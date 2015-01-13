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
package bammerbom.ultimatecore.bukkit.configuration;

/**
 * Various settings for controlling the input and output of a {@link
 * MemoryConfiguration}
 */
class MemoryConfigurationOptions {

    private final MemoryConfiguration configuration;
    private char pathSeparator = '.';
    private boolean copyDefaults = false;

    protected MemoryConfigurationOptions(MemoryConfiguration configuration) {
        this.configuration = configuration;
    }

    public MemoryConfiguration configuration() {
        return (MemoryConfiguration) configuration;
    }

    public char pathSeparator() {
        return this.pathSeparator;
    }

    public MemoryConfigurationOptions pathSeparator(char value) {
        this.pathSeparator = value;
        return this;
    }

    public boolean copyDefaults() {
        return this.copyDefaults;
    }

    public MemoryConfigurationOptions copyDefaults(boolean value) {
        this.copyDefaults = value;
        return this;
    }
}

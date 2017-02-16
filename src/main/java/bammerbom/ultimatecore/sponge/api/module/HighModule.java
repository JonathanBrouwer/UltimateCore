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
package bammerbom.ultimatecore.sponge.api.module;

import bammerbom.ultimatecore.sponge.api.module.annotations.ModuleInfo;
import org.spongepowered.api.text.Text;

public interface HighModule extends Module {
    /**
     * This should return name of the module
     * For example: afk, ban, etc
     */
    default String getIdentifier(){
        if (getClass().isAnnotationPresent(ModuleInfo.class)) {
            ModuleInfo moduleInfo = getClass().getAnnotation(ModuleInfo.class);
            return moduleInfo.name();
        }else{
            return null;
        }
    }

    /**
     * Returns a (short) description of the module.
     * Should be only one sentence.
     *
     * @return The description
     */
    default Text getDescription(){
        if (getClass().isAnnotationPresent(ModuleInfo.class)) {
            ModuleInfo moduleInfo = getClass().getAnnotation(ModuleInfo.class);
            return Text.of(moduleInfo.description());
        }else{
            return Text.of();
        }
    }
}

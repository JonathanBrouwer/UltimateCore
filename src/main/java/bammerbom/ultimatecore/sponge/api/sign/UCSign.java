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
package bammerbom.ultimatecore.sponge.api.sign;

import bammerbom.ultimatecore.sponge.api.module.Module;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;

public interface UCSign {

    /**
     * Get the module that registered this sign.
     *
     * @return The module that registered this sign
     */
    Module getModule();

    /**
     * An unique lower-case string, normally the sign's name.
     *
     * @return the identifier
     */
    String getIdentifier();

    /**
     * The base permission for this sign.
     * This should be equal to the permission needed to execute this sign.
     *
     * @return the permission for this sign
     */
    String getPermission();

    /**
     * Called when a player executes the sign, normally by clicking it.
     *
     * @param p    The player who executed the sign
     * @param sign The sign which has been executed
     */
    void onExecute(Player p, Sign sign);

    /**
     * Called when a player creates a new sign, normally by clicking the confirm button after typing a sign.
     *
     * @param p     The player who created the sign
     * @param event The event which was thrown after creating the sign
     */
    void onCreate(Player p, ChangeSignEvent event);

    /**
     * Called when a player destroys a sign, normally by breaking it.
     *
     * @param p     The player who destroyed the sign
     * @param event The event which was thrown after destroying the sign
     */
    void onDestroy(Player p, ChangeBlockEvent event);
}

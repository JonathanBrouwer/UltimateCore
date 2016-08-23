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
package bammerbom.ultimatecore.sponge.modules.sign.listeners;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.sign.UCSign;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;

public class SignListener {

    @Listener
    public void onSignCreate(ChangeSignEvent event, @Root Player p) {
        for (UCSign sign : UltimateCore.get().getSignService().get().getRegisteredSigns()) {
            if (event.getText().get(0).orElse(Text.of()).toPlain().equalsIgnoreCase("[" + sign.getIdentifier() + "]")) {
                sign.onCreate(p, event);
            }
        }
    }

    @Listener
    public void onSignClick(InteractBlockEvent.Secondary event, @Root Player p) {
        if (!event.getTargetBlock().getLocation().isPresent() || !event.getTargetBlock().getLocation().get().getTileEntity().isPresent()) {
            return;
        }
        if (!(event.getTargetBlock().getLocation().get().getTileEntity().get() instanceof Sign)) {
            return;
        }
        Sign sign = (Sign) event.getTargetBlock().getLocation().get().getTileEntity().get();
        for (UCSign usign : UltimateCore.get().getSignService().get().getRegisteredSigns()) {
            if (sign.getSignData().get(0).orElse(Text.of()).toPlain().equalsIgnoreCase("[" + usign.getIdentifier() + "]")) {
                usign.onExecute(p, sign);
            }
        }
    }

    @Listener
    public void onSignDestroy(ChangeBlockEvent.Break event, @Root Player p) {
        for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
            Messages.log(transaction.getOriginal().getLocation().get().getTileEntity().orElse(null));
            if (!transaction.getOriginal().getLocation().isPresent() || !transaction.getOriginal().getLocation().get().getTileEntity().isPresent()) {
                return;
            }
            if (!(transaction.getOriginal().getLocation().get().getTileEntity().get() instanceof Sign)) {
                return;
            }
            Sign sign = (Sign) transaction.getOriginal().getLocation().get().getTileEntity().get();
            for (UCSign usign : UltimateCore.get().getSignService().get().getRegisteredSigns()) {
                if (sign.getSignData().get(0).orElse(Text.of()).toPlain().equalsIgnoreCase("[" + usign.getIdentifier() + "]")) {
                    usign.onDestroy(p, event, sign);
                }
            }
        }
    }
}

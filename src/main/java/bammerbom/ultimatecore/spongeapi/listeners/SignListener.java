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
package bammerbom.ultimatecore.spongeapi.listeners;

import bammerbom.ultimatecore.spongeapi.UltimateSign;
import bammerbom.ultimatecore.spongeapi.UltimateSigns;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.TextColorUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class SignListener {

    public static void start() {
        Sponge.getEventManager().registerListeners(r.getUC(), new SignListener());
    }

    @Listener(order = Order.DEFAULT)
    public void onSignChange(ChangeSignEvent e) {
        //Signs
        Optional<Player> p = e.getCause().first(Player.class);
        if (!p.isPresent()) {
            return;
        }
        for (UltimateSign s : UltimateSigns.signs) {
            if (TextColorUtil.strip(e.getText().lines().get(0).toPlain()).equalsIgnoreCase("[" + s.getName() + "]")) {
                s.onCreate(e, p.get());
            }
        }
        //Color signs
        if (!r.perm(p.get(), "uc.sign.colored", false)) {
            return;
        }
        e.getText().set(Keys.SIGN_LINES, e.getText().lines().set(0, Text.of(TextColorUtil.translateAlternate(e.getText().lines().get(0).toPlain()))).get());
        e.getText().set(Keys.SIGN_LINES, e.getText().lines().set(1, Text.of(TextColorUtil.translateAlternate(e.getText().lines().get(1).toPlain()))).get());
        e.getText().set(Keys.SIGN_LINES, e.getText().lines().set(2, Text.of(TextColorUtil.translateAlternate(e.getText().lines().get(2).toPlain()))).get());
        e.getText().set(Keys.SIGN_LINES, e.getText().lines().set(3, Text.of(TextColorUtil.translateAlternate(e.getText().lines().get(3).toPlain()))).get());
    }

    @Listener(order = Order.DEFAULT)
    public void onSignInteract(InteractBlockEvent.Secondary e) {
        //Signs
        if (e.getTargetBlock().equals(BlockSnapshot.NONE)) {
            return;
        }
        if (!e.getCause().first(Player.class).isPresent()) {
            return;
        }
        if (!e.getTargetBlock().getLocation().isPresent() || !e.getTargetBlock().getLocation().get().getTileEntity().isPresent()) {
            return;
        }
        if (!(e.getTargetBlock().getLocation().get().getTileEntity().get() instanceof Sign)) {
            return;
        }
        Sign sign = (Sign) e.getTargetBlock().getLocation().get().getTileEntity().get();
        for (UltimateSign s : UltimateSigns.signs) {
            if (TextColorUtil.strip(sign.lines().get(0).toPlain()).equalsIgnoreCase("[" + s.getName() + "]")) {
                s.onClick(e.getCause().first(Player.class).get(), sign);
            }
        }
    }

    @Listener(order = Order.DEFAULT)
    public void onSignDestroy(ChangeBlockEvent e) {
        //Signs
        for (Transaction<BlockSnapshot> tr : e.getTransactions()) {
            if (tr.getOriginal().equals(BlockSnapshot.NONE)) {
                return;
            }
            if (!e.getCause().first(Player.class).isPresent()) {
                return;
            }
            if (!tr.getOriginal().getLocation().isPresent() || !tr.getOriginal().getLocation().get().getTileEntity().isPresent()) {
                return;
            }
            if (!(tr.getOriginal().getLocation().get().getTileEntity().get() instanceof Sign)) {
                return;
            }
            Sign sign = (Sign) tr.getOriginal().getLocation().get().getTileEntity().get();
            for (UltimateSign s : UltimateSigns.signs) {
                if (TextColorUtil.strip(sign.lines().get(0).toPlain()).equalsIgnoreCase("[" + s.getName() + "]")) {
                    s.onDestroy(e, e.getCause().first(Player.class).get());
                }
            }
        }
    }

}

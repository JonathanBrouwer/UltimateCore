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
package bammerbom.ultimatecore.spongeapi.signs;

import bammerbom.ultimatecore.spongeapi.UltimateSign;
import bammerbom.ultimatecore.spongeapi.r;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;

public class SignTime implements UltimateSign {

    @Override
    public String getName() {
        return "time";
    }

    @Override
    public String getPermission() {
        return "uc.sign.time";
    }

    @Override
    public void onClick(Player p, Sign sign) {
        if (!r.perm(p, "uc.sign.time", true, true) && !r.perm(p, "uc.sign", true, true)) {
        }
        long time = p.getWorld().getProperties().getWorldTime();
        time -= time % 24000L;
        if (((Text.Literal) sign.get(Keys.SIGN_LINES).get().get(1)).getContent().equalsIgnoreCase("day")) {
            p.getWorld().getProperties().setWorldTime(time + 24000L);
            r.sendMes(p, "timeMessage", "%Time", r.mes("timeDay"));
        } else if (((Text.Literal) sign.get(Keys.SIGN_LINES).get().get(1)).getContent().equalsIgnoreCase("night")) {
            p.getWorld().getProperties().setWorldTime(time + 37700L);
            r.sendMes(p, "timeMessage", "%Time", r.mes("timeNight"));
        } else if (r.isInt(((Text.Literal) sign.get(Keys.SIGN_LINES).get().get(1)).getContent())) {
            p.getWorld().getProperties().setWorldTime(time + Integer.parseInt(((Text.Literal) sign.get(Keys.SIGN_LINES).get().get(1)).getContent()));
            r.sendMes(p, "timeMessage", "%Time", ((Text.Literal) sign.get(Keys.SIGN_LINES).get().get(1)).getContent());
        } else {
            r.sendMes(p, "signTimeNotFound");
            List<Text> lines = sign.get(Keys.SIGN_LINES).get();
            lines.set(0, Texts.of(TextColors.RED + "[Time]"));
            sign.offer(Keys.SIGN_LINES, lines);
        }
    }

    @Override
    public void onCreate(ChangeSignEvent event, Player p) {
        if (!r.perm(p, "uc.sign.time.create", false, true)) {
            event.setCancelled(true);
            event.getTargetTile().getLocation().removeBlock();
            return;
        }
        if (!((Text.Literal) event.getText().lines().get(1)).getContent().equalsIgnoreCase("day") && !((Text.Literal) event.getText().lines().get(1)).getContent().equalsIgnoreCase("night") && !r
                .isInt(((Text.Literal) event.getText().lines().get(1)).getContent())) {
            r.sendMes(p, "signTimeNotFound");
            event.getText().set(Keys.SIGN_LINES, event.getText().lines().set(0, Texts.of(TextColors.RED + "[Time]")).get());
            return;
        }
        event.getText().set(Keys.SIGN_LINES, event.getText().lines().set(0, Texts.of(TextColors.DARK_BLUE + "[Time]")).get());
        r.sendMes(p, "signCreated");
    }

    @Override
    public void onDestroy(ChangeBlockEvent.Break event, Player p) {
        if (!r.perm(p, "uc.sign.time.destroy", false, true)) {
            event.setCancelled(true);
            return;
        }
        r.sendMes(p, "signDestroyed");
    }
}

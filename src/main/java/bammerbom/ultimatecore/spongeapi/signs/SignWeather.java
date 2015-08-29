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
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.block.tileentity.SignChangeEvent;
import org.spongepowered.api.event.entity.player.PlayerBreakBlockEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.weather.Weathers;

public class SignWeather implements UltimateSign {

    @Override
    public String getName() {
        return "weather";
    }

    @Override
    public String getPermission() {
        return "uc.sign.weather";
    }

    @Override
    public void onClick(Player p, Sign sign) {
        if (!r.perm(p, "uc.sign.weather", true, true) && !r.perm(p, "uc.sign", true, true)) {
        }
        if (((Text.Literal) sign.getData().get().lines().get(1)).getContent().equalsIgnoreCase("sun")) {
            p.getWorld().forecast(Weathers.CLEAR);
            r.sendMes(p, "weatherSet", "%Weather", r.mes("weatherSun"));
        } else if (((Text.Literal) sign.getData().get().lines().get(1)).getContent().equalsIgnoreCase("rain")) {
            p.getWorld().forecast(Weathers.RAIN);
            r.sendMes(p, "weatherSet", "%Weather", r.mes("weatherRain"));
        } else if (((Text.Literal) sign.getData().get().lines().get(1)).getContent().equalsIgnoreCase("thunderstorm")) {
            p.getWorld().forecast(Weathers.THUNDER_STORM);
            r.sendMes(p, "weatherSet", "%Weather", r.mes("weatherThunder"));
        } else {
            r.sendMes(p, "signWeatherNotFound");
            sign.offer(sign.getData().get().lines().set(0, Texts.of(TextColors.RED + "[Weather]")));
        }
    }

    @Override
    public void onCreate(SignChangeEvent event, Player p) {
        if (!r.perm(p, "uc.sign.weather.create", false, true)) {
            event.setCancelled(true);
            event.getTile().getLocation().digBlock();
            return;
        }
        if (!((Text.Literal) event.getNewData().lines().get(1)).getContent().equalsIgnoreCase("sun") && !((Text.Literal) event.getNewData().lines().get(1)).getContent()
                .equalsIgnoreCase("rain") && !((Text.Literal) event.getNewData().lines().get(1)).getContent().equalsIgnoreCase("thunderstorm")) {
            r.sendMes(p, "signWeatherNotFound");
            event.setNewData(event.getNewData().set(Keys.SIGN_LINES, event.getNewData().lines().set(0, Texts.of(TextColors.RED + "[Weather]")).get()));
            return;
        }
        event.setNewData(event.getNewData().set(Keys.SIGN_LINES, event.getNewData().lines().set(0, Texts.of(TextColors.DARK_BLUE + "[Weather]")).get()));
        r.sendMes(p, "signCreated");
    }

    @Override
    public void onDestroy(PlayerBreakBlockEvent event) {
        if (!r.perm(event.getUser(), "uc.sign.weather.destroy", false, true)) {
            event.setCancelled(true);
            return;
        }
        r.sendMes(event.getUser(), "signDestroyed");
    }
}

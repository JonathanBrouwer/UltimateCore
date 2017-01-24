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
package bammerbom.ultimatecore.sponge.api.variable.variables;

import bammerbom.ultimatecore.sponge.api.variable.Variable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Optional;

public class MoneyVariable implements Variable {
    @Override
    public String getKey() {
        return "%money%";
    }

    @Override
    public Optional<Text> getValue(@Nullable Object player) {
        if (player == null) return Optional.empty();
        if (player instanceof Player) {
            Player p = (Player) player;
            if (Sponge.getServiceManager().provide(EconomyService.class).isPresent()) {
                EconomyService es = Sponge.getServiceManager().provide(EconomyService.class).get();
                if (es.getOrCreateAccount(p.getUniqueId()).isPresent()) {
                    BigDecimal balance = es.getOrCreateAccount(p.getUniqueId()).get().getBalance(es.getDefaultCurrency());
                    return Optional.of(Text.of(balance.toString()));
                }
            }
            return Optional.empty();
        }
        return Optional.empty();
    }
}

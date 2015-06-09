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

import bammerbom.ultimatecore.spongeapi.UltimateFileLoader;
import bammerbom.ultimatecore.spongeapi.UltimateSign;
import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.api.UKit;
import bammerbom.ultimatecore.spongeapi.configuration.Config;
import bammerbom.ultimatecore.spongeapi.configuration.ConfigSection;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.DateUtil;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class SignKit implements UltimateSign {

    @Override
    public String getName() {
        return "kit";
    }

    @Override
    public String getPermission() {
        return "uc.sign.kit";
    }

    @Override
    public void onClick(Player p, Sign sign) {
        if (!r.perm(p, "uc.sign.kit", true, true) && !r.perm(p, "uc.sign", true, true)) {
            return;
        }
        final Config config = new Config(UltimateFileLoader.Dkits);
        final ConfigSection kitNode = config.getConfigurationSection(sign.getLine(1));
        if (kitNode == null) {
            r.sendMes(p, "kitNotFound", "%Kit", sign.getLine(1));
            sign.setLine(0, ChatColor.RED + "[Kit]");
            return;
        }
        final UKit kit = UC.getServer().getKit(sign.getLine(1));
        if (!kit.hasCooldownPassedFor(p)) {
            if (kit.getCooldown() == -1L) {
                r.sendMes(p, "kitOnlyOnce");
            } else {
                r.sendMes(p, "kitTime", "%Time", DateUtil.formatDateDiff(kit.getCooldownFor(p)));
            }
            return;
        }
        final List<ItemStack> items = kit.getItems();
        final Map<Integer, ItemStack> leftOver = p.getInventory().addItem(items.toArray(new ItemStack[items.size()]));
        for (final ItemStack is : leftOver.values()) {
            p.getWorld().dropItemNaturally(p.getLocation(), is);
        }
        kit.setLastUsed(p, System.currentTimeMillis());
        r.sendMes(p, "kitGive", "%Kit", sign.getLine(1));
    }

    @Override
    public void onCreate(SignChangeEvent event) {
        if (!r.perm(event.getPlayer(), "uc.sign.kit", false, true)) {
            event.setCancelled(true);
            event.getBlock().breakNaturally();
            return;
        }
        final Config config = new Config(UltimateFileLoader.Dkits);
        final ConfigSection kitNode = config.getConfigurationSection(event.getLine(1));
        if (kitNode == null) {
            r.sendMes(event.getPlayer(), "kitNotFound", "%Kit", event.getLine(1));
            event.setLine(0, ChatColor.RED + "[Kit]");
            return;
        }
        event.setLine(0, ChatColor.DARK_BLUE + "[Kit]");
        r.sendMes(event.getPlayer(), "signCreated");
    }

    @Override
    public void onDestroy(BlockBreakEvent event) {
        if (!r.perm(event.getPlayer(), "uc.sign.kit.destroy", false, true)) {
            event.setCancelled(true);
            return;
        }
        r.sendMes(event.getPlayer(), "signDestroyed");
    }
}

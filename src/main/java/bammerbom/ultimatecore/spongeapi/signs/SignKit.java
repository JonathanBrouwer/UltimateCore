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
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.block.tileentity.SignChangeEvent;
import org.spongepowered.api.event.entity.player.PlayerBreakBlockEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;

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
        final ConfigSection kitNode = config.getConfigurationSection(((Text.Literal) sign.getData().get().lines().get(1)).getContent());
        if (kitNode == null) {
            r.sendMes(p, "kitNotFound", "%Kit", ((Text.Literal) sign.getData().get().lines().get(1)).getContent());
            sign.offer(sign.getData().get().lines().set(0, Texts.of(TextColors.RED + "[Kit]")));
            return;
        }
        final UKit kit = UC.getServer().getKit(((Text.Literal) sign.getData().get().lines().get(1)).getContent());
        if (!kit.hasCooldownPassedFor(p)) {
            if (kit.getCooldown() == -1L) {
                r.sendMes(p, "kitOnlyOnce");
            } else {
                r.sendMes(p, "kitTime", "%Time", DateUtil.formatDateDiff(kit.getCooldownFor(p)));
            }
            return;
        }
        final List<ItemStack> items = kit.getItems();
        for (ItemStack item : items) {
            if (!p.getInventory().offer(item)) {
                Item en = (Item) p.getWorld().createEntity(EntityTypes.DROPPED_ITEM, p.getLocation().getPosition()).get();
                en.offer(en.getItemData().set(Keys.REPRESENTED_ITEM, item));
                p.getWorld().spawnEntity(en);
            }
        }
        kit.setLastUsed(p, System.currentTimeMillis());
        r.sendMes(p, "kitGive", "%Kit", ((Text.Literal) sign.getData().get().lines().get(1)).getContent());
    }

    @Override
    public void onCreate(SignChangeEvent event, Player p) {
        if (!r.perm(p, "uc.sign.kit", false, true)) {
            event.setCancelled(true);
            event.getTile().getLocation().digBlock();
            return;
        }
        final Config config = new Config(UltimateFileLoader.Dkits);
        final ConfigSection kitNode = config.getConfigurationSection(((Text.Literal) event.getNewData().lines().get(1)).getContent());
        if (kitNode == null) {
            r.sendMes(p, "kitNotFound", "%Kit", event.getNewData().lines().get(1));
            event.setNewData(event.getNewData().set(Keys.SIGN_LINES, event.getNewData().lines().set(0, Texts.of(TextColors.RED + "[Kit]")).get()));
            return;
        }
        event.setNewData(event.getNewData().set(Keys.SIGN_LINES, event.getNewData().lines().set(0, Texts.of(TextColors.DARK_BLUE + "[Kit]")).get()));
        r.sendMes(p, "signCreated");
    }

    @Override
    public void onDestroy(PlayerBreakBlockEvent event) {
        if (!r.perm(event.getUser(), "uc.sign.kit.destroy", false, true)) {
            event.setCancelled(true);
            return;
        }
        r.sendMes(event.getUser(), "signDestroyed");
    }

}

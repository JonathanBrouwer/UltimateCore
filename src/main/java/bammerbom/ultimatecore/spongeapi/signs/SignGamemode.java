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
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class SignGamemode implements UltimateSign {

    @Override
    public String getName() {
        return "gamemode";
    }

    @Override
    public String getPermission() {
        return "uc.sign.gamemode";
    }

    @Override
    public void onClick(Player p, Sign sign) {
        if (!r.perm(p, "uc.sign.gamemode", false) && !r.perm(p, "uc.sign", false)) {
            r.sendMes(p, "noPermissions");
            return;
        }
        GameMode mode;
        switch (sign.get(Keys.SIGN_LINES).get().get(1).toPlain().toLowerCase()) {
            case "survival":
            case "s":
            case "surv":
            case "0":
                mode = GameModes.SURVIVAL;
                break;
            case "creative":
            case "c":
            case "crea":
            case "1":
                mode = GameModes.CREATIVE;
                break;
            case "adventure:":
            case "a":
            case "adven":
            case "2":
                mode = GameModes.ADVENTURE;
                break;
            case "sp":
            case "spec":
            case "spectate":
            case "spectator":
            case "3":
                mode = GameModes.SPECTATOR;
                break;
            default:
                r.sendMes(p, "signGamemodeNotFound");
                sign.offer(Keys.SIGN_LINES, sign.lines().set(0, Text.of(TextColors.RED + "[Gamemode]")).get());
                return;
        }
        p.offer(Keys.GAME_MODE, mode);
        r.sendMes(p, "gamemodeSelf", "%Gamemode", mode.toString().toLowerCase());
    }

    @Override
    public void onCreate(ChangeSignEvent event, Player p) {
        if (!r.perm(p, "uc.sign.gamemode.create", true)) {
            event.setCancelled(true);
            event.getTargetTile().getLocation().setBlockType(BlockTypes.AIR);
            Optional<Entity> eno = event.getTargetTile().getLocation().getExtent().createEntity(EntityTypes.ITEM, event.getTargetTile().getLocation().getPosition());
            if (eno.isPresent()) {
                Item en = (Item) eno.get();
                en.offer(Keys.REPRESENTED_ITEM, ItemStack.builder().itemType(ItemTypes.SIGN).build().createSnapshot());
                p.getWorld().spawnEntity(en, Cause.builder().named(NamedCause.simulated(p)).build());
            }
            return;
        }
        try {
            GameMode mode;
            switch (event.getTargetTile().get(Keys.SIGN_LINES).get().get(1).toPlain().toLowerCase()) {
                case "survival":
                case "s":
                case "surv":
                case "0":
                    break;
                case "creative":
                case "c":
                case "crea":
                case "1":
                    break;
                case "adventure:":
                case "a":
                case "adven":
                case "2":
                    break;
                case "sp":
                case "spec":
                case "spectate":
                case "spectator":
                case "3":
                    break;
                default:
                    r.sendMes(p, "signGamemodeNotFound");
                    event.getText().set(Keys.SIGN_LINES, event.getText().lines().set(0, Text.of(TextColors.RED + "[Gamemode]")).get());
                    return;
            }
        } catch (Exception ex) {
            r.sendMes(p, "signGamemodeNotFound");
            event.getText().set(Keys.SIGN_LINES, event.getText().lines().set(0, Text.of(TextColors.RED + "[Gamemode]")).get());
            return;
        }
        event.getText().set(Keys.SIGN_LINES, event.getText().lines().set(0, Text.of(TextColors.DARK_BLUE + "[Gamemode]")).get());
        r.sendMes(p, "signCreated");
    }

    @Override
    public void onDestroy(ChangeBlockEvent.Break event, Player p) {
        if (!r.perm(p, "uc.sign.gamemode.destroy", true)) {
            event.setCancelled(true);
            return;
        }
        r.sendMes(p, "signDestroyed");
    }
}

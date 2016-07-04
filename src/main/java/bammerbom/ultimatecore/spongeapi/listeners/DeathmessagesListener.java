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

import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TranslatableText;

import java.util.HashMap;

public class DeathmessagesListener {

    static HashMap<String, Text> msg = new HashMap<>();

    public static void start() {
        if (!r.getCnfg().getBoolean("Chat.EnableCustomDeathmessages")) {
            return;
        }
        Sponge.getEventManager().registerListeners(r.getUC(), new DeathmessagesListener());
        //Set deathmessages
        try {
            msg.put("death.fell.accident.ladder", r.mes("deathFellAccidentLadder", "%Player", "%1$s"));
            msg.put("death.fell.accident.vines", r.mes("deathFellAccidentVines", "%Player", "%1$s"));
            msg.put("death.fell.accident.water", r.mes("deathFellAccidentWater", "%Player", "%1$s"));
            msg.put("death.fell.accident.generic", r.mes("deathFellAccidentGeneric", "%Player", "%1$s"));
            msg.put("death.fell.killer", r.mes("deathFellKiller", "%Player", "%1$s"));
            msg.put("death.fell.assist", r.mes("deathFellAssist", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.fell.assist.item", r.mes("deathFellAssistItem", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.fell.finish", r.mes("deathFellFinish", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.fell.finish.item", r.mes("deathFellFinishItem", "%Player", "%1$s", "%Killer", "%2$s"));

            msg.put("death.attack.lightningBolt", r.mes("deathAttackLightningBolt", "%Player", "%1$s"));
            msg.put("death.attack.inFire", r.mes("deathAttackInFire", "%Player", "%1$s"));
            msg.put("death.attack.inFire.player", r.mes("deathAttackInFirePlayer", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.onFire", r.mes("deathAttackOnFire", "%Player", "%1$s"));
            msg.put("death.attack.onFire.player", r.mes("deathAttackOnFirePlayer", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.lava", r.mes("deathAttackLava", "%Player", "%1$s"));
            msg.put("death.attack.lava.player", r.mes("deathAttackLavaPlayer", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.inWall", r.mes("deathAttackInWall", "%Player", "%1$s"));
            msg.put("death.attack.drown", r.mes("deathAttackDrown", "%Player", "%1$s"));
            msg.put("death.attack.drown.player", r.mes("deathAttackDrownPlayer", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.starve", r.mes("deathAttackStarve", "%Player", "%1$s"));
            msg.put("death.attack.cactus", r.mes("deathAttackCactus", "%Player", "%1$s"));
            msg.put("death.attack.cactus.player", r.mes("deathAttackCactusPlayer", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.generic", r.mes("deathAttackGeneric", "%Player", "%1$s"));
            msg.put("death.attack.explosion", r.mes("deathAttackExplosion", "%Player", "%1$s"));
            msg.put("death.attack.explosion.player", r.mes("deathAttackExplosionPlayer", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.magic", r.mes("deathAttackMagic", "%Player", "%1$s"));
            msg.put("death.attack.wither", r.mes("deathAttackWither", "%Player", "%1$s"));
            msg.put("death.attack.anvil", r.mes("deathAttackAnvil", "%Player", "%1$s"));
            msg.put("death.attack.fallingBlock", r.mes("deathAttackFallingBlock", "%Player", "%1$s"));
            msg.put("death.attack.mob", r.mes("deathAttackMob", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.player", r.mes("deathAttackPlayer", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.player.item", r.mes("deathAttackPlayerItem", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.arrow", r.mes("deathAttackArrow", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.arrow.item", r.mes("deathAttackArrowItem", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.fireball", r.mes("deathAttackFireball", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.fireball.item", r.mes("deathAttackFireballItem", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.thrown", r.mes("deathAttackThrown", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.thrown.item", r.mes("deathAttackThrownItem", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.indirectMagic", r.mes("deathAttackIndirectMagic", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.indirectMagic.item", r.mes("deathAttackIndirectMagicItem", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.thorns", r.mes("deathAttackThorns", "%Player", "%1$s", "%Killer", "%2$s"));
            msg.put("death.attack.fall", r.mes("deathAttackFall", "%Player", "%1$s"));
            msg.put("death.attack.outOfWorld", r.mes("deathAttackOutOfWorld", "%Player", "%1$s"));
            msg.put("death.attack.dragonBreath", r.mes("deathAttackDragonBreath", "%Player", "%1$s"));
            msg.put("death.attack.flyIntoWall", r.mes("deathAttackFlyIntoWall", "%Player", "%1$s"));
            msg.put("death.attack.hotFloor", r.mes("deathAttackHotFloor", "%Player", "%1$s"));
            msg.put("death.attack.hotFloor.player", r.mes("deathAttackHotFloorPlayer", "%Player", "%1$s", "%Killer", "%2$s"));
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to write deathmessages.");
        }
    }

    @Listener(order = Order.DEFAULT)
    public void death(DestructEntityEvent.Chat e) {
        if (e.getRawMessage() instanceof TranslatableText) {
            String key = ((TranslatableText) e.getRawMessage()).getTranslation().getId();
            if (msg.containsKey(key)) {
                e.setMessage(msg.get(key));
                if (e.getCause().first(Player.class).isPresent()) {
                    Player k = e.getCause().first(Player.class).get();
                    ItemStack stack = k.getItemInHand(HandTypes.MAIN_HAND).orElse(ItemStack.builder().itemType(ItemTypes.NONE).build());
                    Text name = stack.get(Keys.DISPLAY_NAME).orElse(Text.of(stack.getItem().getName()));
                    if (stack.get(Keys.DISPLAY_NAME).isPresent()) {
                        name = stack.get(Keys.DISPLAY_NAME).get();
                    }
                    e.setMessage(Text.of(e.getMessage().toString().replace("%Weapon", name.toPlain())));
                }
            }
        }

    }
}
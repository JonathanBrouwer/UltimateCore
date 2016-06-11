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
package bammerbom.ultimatecore.bukkit.listeners;

import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.bukkit.resources.utils.ItemUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class DeathmessagesListener implements Listener {

    public static void start() {
        if (!r.getCnfg().getBoolean("Chat.EnableCustomDeathmessages")) {
            return;
        }
        Bukkit.getPluginManager().registerEvents(new DeathmessagesListener(), r.getUC());
        //Set deathmessages
        try {
            ReflectionUtil.ReflectionObject localeI18n = ReflectionUtil.executeStatic("a", ReflectionUtil.ReflectionStatic.fromNMS("LocaleI18n"));
            Map<String, String> keys = (Map<String, String>) localeI18n.get("d");
            //
            keys.put("death.fell.accident.ladder", r.mes("deathFellAccidentLadder", "%Player", "%1$s"));
            keys.put("death.fell.accident.vines", r.mes("deathFellAccidentVines", "%Player", "%1$s"));
            keys.put("death.fell.accident.water", r.mes("deathFellAccidentWater", "%Player", "%1$s"));
            keys.put("death.fell.accident.generic", r.mes("deathFellAccidentGeneric", "%Player", "%1$s"));
            keys.put("death.fell.killer", r.mes("deathFellKiller", "%Player", "%1$s"));
            keys.put("death.fell.assist", r.mes("deathFellAssist", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.fell.assist.item", r.mes("deathFellAssistItem", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.fell.finish", r.mes("deathFellFinish", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.fell.finish.item", r.mes("deathFellFinishItem", "%Player", "%1$s", "%Killer", "%2$s"));

            keys.put("death.attack.lightningBolt", r.mes("deathAttackLightningBolt", "%Player", "%1$s"));
            keys.put("death.attack.inFire", r.mes("deathAttackInFire", "%Player", "%1$s"));
            keys.put("death.attack.inFire.player", r.mes("deathAttackInFirePlayer", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.onFire", r.mes("deathAttackOnFire", "%Player", "%1$s"));
            keys.put("death.attack.onFire.player", r.mes("deathAttackOnFirePlayer", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.lava", r.mes("deathAttackLava", "%Player", "%1$s"));
            keys.put("death.attack.lava.player", r.mes("deathAttackLavaPlayer", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.inWall", r.mes("deathAttackInWall", "%Player", "%1$s"));
            keys.put("death.attack.drown", r.mes("deathAttackDrown", "%Player", "%1$s"));
            keys.put("death.attack.drown.player", r.mes("deathAttackDrownPlayer", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.starve", r.mes("deathAttackStarve", "%Player", "%1$s"));
            keys.put("death.attack.cactus", r.mes("deathAttackCactus", "%Player", "%1$s"));
            keys.put("death.attack.cactus.player", r.mes("deathAttackCactusPlayer", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.generic", r.mes("deathAttackGeneric", "%Player", "%1$s"));
            keys.put("death.attack.explosion", r.mes("deathAttackExplosion", "%Player", "%1$s"));
            keys.put("death.attack.explosion.player", r.mes("deathAttackExplosionPlayer", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.magic", r.mes("deathAttackMagic", "%Player", "%1$s"));
            keys.put("death.attack.wither", r.mes("deathAttackWither", "%Player", "%1$s"));
            keys.put("death.attack.anvil", r.mes("deathAttackAnvil", "%Player", "%1$s"));
            keys.put("death.attack.fallingBlock", r.mes("deathAttackFallingBlock", "%Player", "%1$s"));
            keys.put("death.attack.mob", r.mes("deathAttackMob", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.player", r.mes("deathAttackPlayer", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.player.item", r.mes("deathAttackPlayerItem", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.arrow", r.mes("deathAttackArrow", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.arrow.item", r.mes("deathAttackArrowItem", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.fireball", r.mes("deathAttackFireball", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.fireball.item", r.mes("deathAttackFireballItem", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.thrown", r.mes("deathAttackThrown", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.thrown.item", r.mes("deathAttackThrownItem", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.indirectMagic", r.mes("deathAttackIndirectMagic", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.indirectMagic.item", r.mes("deathAttackIndirectMagicItem", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.thorns", r.mes("deathAttackThorns", "%Player", "%1$s", "%Killer", "%2$s"));
            keys.put("death.attack.fall", r.mes("deathAttackFall", "%Player", "%1$s"));
            keys.put("death.attack.outOfWorld", r.mes("deathAttackOutOfWorld", "%Player", "%1$s"));
            keys.put("death.attack.dragonBreath", r.mes("deathAttackDragonBreath", "%Player", "%1$s"));
            keys.put("death.attack.flyIntoWall", r.mes("deathAttackFlyIntoWall", "%Player", "%1$s"));
            keys.put("death.attack.hotFloor", r.mes("deathAttackHotFloor", "%Player", "%1$s"));
            keys.put("death.attack.hotFloor.player", r.mes("deathAttackHotFloorPlayer", "%Player", "%1$s", "%Killer", "%2$s"));

            localeI18n.set("d", keys);
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to write deathmessages.");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void death(PlayerDeathEvent e) {
        //TODO find a better way to do this
        //This is needed so minecraft doenst take the old deathmessage
        e.setDeathMessage(e.getDeathMessage() + " ");
        if (e.getEntity().getKiller() != null) {
            Player k = e.getEntity().getKiller();
            ItemStack stack = k.getInventory().getItemInMainHand();
            String name = ItemUtil.getName(stack);
            if (stack.getItemMeta().getDisplayName() != null) {
                name = stack.getItemMeta().getDisplayName();
            }
            e.setDeathMessage(e.getDeathMessage().replace("%Weapon", name));
        }

    }
}
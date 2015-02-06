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

import bammerbom.ultimatecore.bukkit.api.UC;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.utils.ParticleUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.ParticleUtil.BlockData;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class BloodListener implements Listener {

    public static void start() {
        if (r.getCnfg().getBoolean("Blood.Enabled")) {
            Bukkit.getPluginManager().registerEvents(new BloodListener(), r.getUC());
        }
    }

    ArrayList<Integer> iCD = new ArrayList<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void bleed(final EntityDamageEvent e) {
        if (e.isCancelled() || e.getEntity().isDead()) {
            return;
        }
        if (!(e.getEntity() instanceof LivingEntity)) {
            return;
        }
        if (r.getCnfg().getBoolean("Blood.Enabled") == false) {
            return;
        }
        if (e.getEntity() instanceof Player || r.getCnfg().getBoolean("Blood.PlayersOnly") == false) {
            if (e.getEntity() instanceof Player) {
                final Player p = (Player) e.getEntity();
                if (UC.getPlayer(p).isGod()) {
                    return;
                }
                if (iCD.contains(p.getEntityId())) {
                    return;
                }
                if (((HumanEntity) e.getEntity()).getGameMode().equals(GameMode.CREATIVE)) {
                    return;
                }
                iCD.add(p.getEntityId());
                Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {
                    @Override
                    public void run() {
                        iCD.remove((Object) p.getEntityId());
                    }
                }, 5L);
                ParticleUtil.BLOCK_CRACK.display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
            } else {
                if (iCD.contains(e.getEntity().getEntityId())) {
                    return;
                }
                iCD.add(e.getEntity().getEntityId());
                ParticleUtil.BLOCK_CRACK.display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());

                Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {

                    @Override
                    public void run() {
                        iCD.remove((Object) e.getEntity().getEntityId());
                    }
                }, 10L);
            }

        }
    }

}

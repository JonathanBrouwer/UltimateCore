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
import bammerbom.ultimatecore.bukkit.api.UPlayer;
import bammerbom.ultimatecore.bukkit.commands.CmdMobtp;
import bammerbom.ultimatecore.bukkit.configuration.Config;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.bukkit.resources.utils.DateUtil;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.EventExecutor;

public class GlobalPlayerListener implements Listener {

    public static void start() {
        final GlobalPlayerListener gpl = new GlobalPlayerListener();
        Bukkit.getPluginManager().registerEvents(gpl, r.getUC());
        EventPriority p;
        String s = r.getCnfg().getString("Command.Spawn.Priority");
        if (s.equalsIgnoreCase("lowest")) {
            p = EventPriority.LOWEST;
        } else if (s.equalsIgnoreCase("high")) {
            p = EventPriority.HIGH;
        } else if (s.equalsIgnoreCase("highest")) {
            p = EventPriority.HIGHEST;
        } else {
            r.log("Spawn priority is invalid.");
            return;
        }
        Bukkit.getPluginManager().registerEvent(PlayerRespawnEvent.class, gpl, p, new EventExecutor() {
            @Override
            public void execute(Listener l, Event e) throws EventException {
                gpl.onRespawn((PlayerRespawnEvent) e);
            }
        }, r.getUC());
    }

    Boolean jailChat = r.getCnfg().getBoolean("Command.Jail.talk");
    Boolean jailedmove = r.getCnfg().getBoolean("Command.Jail.move");

    @EventHandler(priority = EventPriority.LOW)
    public void onTeleport(PlayerTeleportEvent e) {
        try {
            //Back
            if (e.getCause().equals(TeleportCause.COMMAND)) {
                UC.getPlayer(e.getPlayer()).setLastLocation(e.getFrom());
            }
            //Jail
            if (!jailedmove && UC.getPlayer(e.getPlayer()).isJailed()) {
                if (!e.getCause().equals(TeleportCause.UNKNOWN)) {
                    Location loc = e.getFrom().getBlock().getLocation().add(0.5, 0.1, 0.5);
                    loc.setPitch(e.getFrom().getPitch());
                    loc.setYaw(e.getFrom().getYaw());
                    e.setTo(loc);
                }
            }
            //

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerTeleportEvent");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e) {
        try {
            //Inventory
            UC.getPlayer(e.getPlayer()).updateLastInventory();
            //Jail
            if (UC.getPlayer(e.getPlayer()).isJailed()) {
                e.getPlayer().teleport(UC.getServer().getJail(UC.getPlayer(e.getPlayer()).getJail()));
            }
            //Lastconnect
            UC.getPlayer(e.getPlayer()).updateLastConnectMillis();
            //Lastip
            UC.getPlayer(e.getPlayer()).setLastIp(e.getPlayer().getAddress().toString().split("/")[1].split(":")[0]);
            UC.getPlayer(e.getPlayer()).setLastHostname(e.getPlayer().getAddress().getHostName());
            //Vanish
            for (Player p : UC.getServer().getVanishOnlinePlayers()) {
                e.getPlayer().hidePlayer(p);
            }
            //Name changes
            if (UC.getPlayer(e.getPlayer()).getPlayerConfig().contains("oldname")) {
                Config conf = UC.getPlayer(e.getPlayer()).getPlayerConfig();
                r.sendMes(e.getPlayer(), "nameChanged", "%Oldname", conf.getString("oldname"), "%Newname", e.getPlayer().getName());
                conf.set("oldname", null);
                conf.save();
            }
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerJoinEvent");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent e) {
        try {
            //Inventory
            UC.getPlayer(e.getPlayer()).updateLastInventory();
            //Last connect
            UC.getPlayer(e.getPlayer()).updateLastConnectMillis();
            //Vanish
            for (Player p : UC.getServer().getVanishOnlinePlayers()) {
                e.getPlayer().showPlayer(p);
            }
            if (UC.getPlayer(e.getPlayer()).isVanish()) {
                for (Player p : r.getOnlinePlayers()) {
                    p.showPlayer(e.getPlayer());
                }
            }
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerQuitEvent");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLogin(PlayerLoginEvent e) {
        try {
            //Ban
            if (UC.getPlayer(e.getPlayer()).isBanned()) {
                if (UC.getPlayer(e.getPlayer()).getBanType().equals(BanList.Type.NAME)) {
                    UPlayer pl = UC.getPlayer(e.getPlayer());
                    String time = pl.getBanTime() <= 0 ? r.mes("banForever") : (DateUtil.format(pl.getBanTimeLeft()));
                    String reason = pl.getBanReason();
                    if (reason == null || reason.isEmpty()) {
                        reason = r.mes("banDefaultReason");
                    }
                    String msg = r.mes("banFormat", "%Time", time, "%Reason", reason);
                    e.disallow(Result.KICK_BANNED, msg);
                } else {
                    UPlayer pl = UC.getPlayer(e.getPlayer());
                    String time = pl.getBanTime() == null ? r.mes("banipForever") : (pl.getBanTime() <= 0 ? r.mes("banipForever") : (DateUtil.format(pl.getBanTimeLeft())));
                    String reason = pl.getBanReason();
                    if (reason == null || reason.isEmpty()) {
                        reason = r.mes("banipDefaultReason");
                    }
                    String msg = r.mes("banipFormat", "%Time", time, "%Reason", reason);
                    e.disallow(Result.KICK_BANNED, msg);
                }
            }
            //

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerLoginEvent");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent e) {
        try {
            //Deaf
            e.getRecipients().removeAll(UC.getServer().getDeafOnlinePlayers());
            e.getRecipients().add(e.getPlayer());
//            if (UC.getPlayer(e.getPlayer()).isDeaf()) {
//                r.sendMes(e.getPlayer(), "deafTalk");
//                e.setCancelled(true);
//            }
            //Jail
            if (!jailChat && UC.getPlayer(e.getPlayer()).isJailed()) {
                r.sendMes(e.getPlayer(), "jailNotAllowedTalk");
                e.setCancelled(true);
            }
            //Mute
            if (UC.getPlayer(e.getPlayer()).isMuted()) {
                e.setCancelled(true);
                if (UC.getPlayer(e.getPlayer()).getMuteTime() == 0 || UC.getPlayer(e.getPlayer()).getMuteTime() == -1) {
                    r.sendMes(e.getPlayer(), "muteChat");
                } else {
                    r.sendMes(e.getPlayer(), "muteChatTime", "%Time", DateUtil.format(UC.getPlayer(e.getPlayer()).getMuteTimeLeft()));
                }
            }
            //

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: AsyncPlayerChatEvent");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPrepareItemEnchant(PrepareItemEnchantEvent e) {
        try {
            //EnchantingTable
            if (UC.getPlayer(e.getEnchanter()).isInCommandEnchantingtable()) {
                e.getExpLevelCostsOffered()[0] = 1;
                e.getExpLevelCostsOffered()[1] = 15;
                e.getExpLevelCostsOffered()[2] = 30;
            }
            //

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PrepareItemEnchantEvent");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClose(InventoryCloseEvent e) {
        try {
            //EnchantingTable
            if (UC.getPlayer((OfflinePlayer) e.getPlayer()).isInCommandEnchantingtable()) {
                UC.getPlayer((OfflinePlayer) e.getPlayer()).setInCommandEnchantingtable(false);
            }
            //Inventory
            if (UC.getPlayer(e.getPlayer().getUniqueId()).isInOfflineInventory()) {
                UC.getPlayer(e.getPlayer().getUniqueId()).setInOfflineInventory(false);
            }
            if (UC.getPlayer(e.getPlayer().getUniqueId()).isInOnlineInventory()) {
                UC.getPlayer(e.getPlayer().getUniqueId()).setInOnlineInventory(false);
            }
            //Recipe
            if (UC.getPlayer((OfflinePlayer) e.getPlayer()).isInRecipeView()) {
                UC.getPlayer((OfflinePlayer) e.getPlayer()).isInRecipeView();
                e.getInventory().clear();
            }
            //Teleportmenu
            if (UC.getPlayer(e.getPlayer().getUniqueId()).isInTeleportMenu()) {
                UC.getPlayer(e.getPlayer().getUniqueId()).setInTeleportMenu(false);
            }

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: InventoryCloseEvent");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClick(final InventoryClickEvent e) {
        try {
            //Inventory
            if (UC.getPlayer(e.getWhoClicked().getUniqueId()).isInOfflineInventory()) {
                e.setCancelled(true);
            }
            if (UC.getPlayer(e.getWhoClicked().getUniqueId()).isInOnlineInventory()) {
                if (!r.perm(e.getWhoClicked(), "uc.inventory.edit", false, true)) {
                    e.setCancelled(true);
                }
            }
            //Recipe
            if (UC.getPlayer((OfflinePlayer) e.getWhoClicked()).isInRecipeView() && e.getInventory().getType() == InventoryType.WORKBENCH) {
                e.setCancelled(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {
                    @Override
                    public void run() {
                        ((Player) e.getWhoClicked()).updateInventory();
                    }
                }, 1L);
            }
            //Teleportmenu
            if (UC.getPlayer(e.getWhoClicked().getUniqueId()).isInTeleportMenu()) {
                UC.getPlayer(e.getWhoClicked().getUniqueId()).setInTeleportMenu(false);
                Bukkit.getServer().dispatchCommand(e.getWhoClicked(), "tp " + ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                e.setCancelled(true);
                e.getWhoClicked().closeInventory();
            }
            //

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: InventoryClickEvent");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onMove(PlayerMoveEvent e) {
        if (e.getFrom().getBlock().getLocation().equals(e.getTo().getBlock().getLocation())) {
            return;
        }
        try {
            //Freeze
            if (UC.getPlayer(e.getPlayer()).isFrozen()) {
                Location loc = e.getFrom().getBlock().getLocation().add(0.5, 0.1, 0.5);
                loc.setPitch(e.getFrom().getPitch());
                loc.setYaw(e.getFrom().getYaw());
                e.setTo(loc);
            }

            //Jail
            if (!jailedmove && UC.getPlayer(e.getPlayer()).isJailed()) {
                Location loc = e.getFrom().getBlock().getLocation().add(0.5, 0.1, 0.5);
                loc.setPitch(e.getFrom().getPitch());
                loc.setYaw(e.getFrom().getYaw());
                e.setTo(loc);
                r.sendMes(e.getPlayer(), "jailNotAllowedMove");
            }
            //

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerMoveEvent");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDeath(PlayerDeathEvent e) {
        try {
            //Back
            if (r.perm(e.getEntity(), "uc.back.death", true, false)) {
                UC.getPlayer(e.getEntity()).setLastLocation();
            } else {
                UC.getPlayer(e.getEntity()).setLastLocation(null);
            }

            //
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerMoveEvent");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageEvent e) {
        try {
            //God
            if (e.getEntity().getType().equals(EntityType.PLAYER)) {
                Player p = (Player) e.getEntity();
                if (UC.getPlayer(p).isGod()) {
                    p.setFireTicks(0);
                    p.setHealth(p.getMaxHealth());
                    p.setRemainingAir(p.getMaximumAir());
                    e.setCancelled(true);
                }
            }
            //
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: EntityDamageEvent");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        try {
            //God
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                if (UC.getPlayer(p).isGod()) {
                    e.setCancelled(true);
                    p.setFoodLevel(20);
                }
            }
            //
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: FoodLevelChangeEvent");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        try {
            //Jail
            if (UC.getPlayer(e.getPlayer()).isJailed() && !e.getMessage().startsWith("/unjail")) {
                e.setCancelled(true);
                r.sendMes(e.getPlayer(), "jailNotAllowedCommand");
            }
            //
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerCommandPreprocessEvent");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent e) {
        try {
            //Mobtp
            if (!e.isCancelled() && !e.getAction().equals(Action.PHYSICAL)) {
                e.setCancelled(CmdMobtp.place(e.getPlayer()));
            }
            //Powertool
            if (!e.getAction().equals(Action.PHYSICAL)
                    && e.getPlayer() != null
                    && e.getPlayer().getItemInHand() != null
                    && e.getPlayer().getItemInHand().getType() != null
                    && !e.getPlayer().getItemInHand().getType().equals(Material.AIR)
                    && UC.getPlayer(e.getPlayer()).hasPowertool(e.getPlayer().getItemInHand().getType())) {
                for (String s : UC.getPlayer(e.getPlayer()).getPowertools(e.getPlayer().getItemInHand().getType())) {
                    e.getPlayer().performCommand(s);
                }
            }

            //
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerInteractEvent");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        try {
            //Mobtp
            if (!e.isCancelled()) {
                e.setCancelled(CmdMobtp.pickup(e.getPlayer(), e.getRightClicked()));
            }
            //
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerInteractEntityEvent");
        }
    }

    public void onRespawn(PlayerRespawnEvent e) {
        try {
            if (UC.getServer().getSpawn() != null) {
                e.setRespawnLocation(UC.getServer().getSpawn());
            }
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerRespawnEvent");
        }
    }

}

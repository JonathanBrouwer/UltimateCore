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

import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.api.UPlayer;
import bammerbom.ultimatecore.spongeapi.commands.CmdMobtp;
import bammerbom.ultimatecore.spongeapi.commands.CmdVillager;
import bammerbom.ultimatecore.spongeapi.jsonconfiguration.JsonConfig;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.spongeapi.resources.utils.DateUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.LocationUtil;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.world.Location;

public class GlobalPlayerListener {

    static boolean spawnOnJoin = r.getCnfg().getBoolean("SpawnOnJoin", false);
    Boolean jailChat = r.getCnfg().getBoolean("Command.Jail.talk");
    Boolean jailedmove = r.getCnfg().getBoolean("Command.Jail.move");

    public static void start() {
        final GlobalPlayerListener gpl = new GlobalPlayerListener();
        Sponge.getEventManager().registerListeners(r.getUC(), gpl);
        Order p;
        String s = r.getCnfg().getString("Command.Spawn.Priority");
        if (s.equalsIgnoreCase("lowest")) {
            p = Order.EARLY;
        } else if (s.equalsIgnoreCase("high")) {
            p = Order.DEFAULT;
        } else if (s.equalsIgnoreCase("highest")) {
            p = Order.LATE;
        } else {
            r.log("Spawn priority is invalid.");
            return;
        }
        Sponge.getEventManager().registerListener(r.getUC(), RespawnPlayerEvent.class, p, new EventListener<RespawnPlayerEvent>() {
            @Override
            public void handle(RespawnPlayerEvent event) throws Exception {
                gpl.onRespawn(event);
            }
        });
    }

    @Listener
    public void onTeleport(MoveEntityEvent.Teleport e) {
        if (!(e.getTargetEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getTargetEntity();
        try {
            //Back
            if (e.getCause().equals(Cause.builder().build())) {
                UC.getPlayer(p).setLastLocation(e.getFromTransform().getLocation(), e.getFromTransform().getRotation());
            }
            //Jail
            if (!jailedmove && UC.getPlayer(p).isJailed()) {
                if (!e.getCause().equals(Cause.builder().build())) {
                    Location loc = e.getFromTransform().getLocation().add(0, 0.1, 0);
                    e.setToTransform(new Transform<>(loc));
                }
            }
            //

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerTeleportEvent");
        }
    }

    @Listener
    public void onJoin(ClientConnectionEvent.Join e) {
        Player p = e.getTargetEntity();
        try {
            //Spawn on join
            if (spawnOnJoin) {
                LocationUtil.teleportUnsafe(p, (Location) UC.getPlayer(p).getSpawn(false)[0], Cause.builder().build(), false);
                p.setRotation((Vector3d) UC.getPlayer(p).getSpawn(false)[1]);
            }
            //Inventory
            UC.getPlayer(p).updateLastInventory();
            //Jail
            if (UC.getPlayer(p).isJailed()) {
                p.setLocation(UC.getServer().getJail(UC.getPlayer(p).getJail()));
            }
            //Lastconnect
            UC.getPlayer(p).updateLastConnectMillis();
            //Lastip
            UC.getPlayer(p).setLastIp(p.getConnection().getAddress().getAddress().toString().split("/")[1].split(":")[0]);
            UC.getPlayer(p).setLastHostname(p.getConnection().getAddress().getHostName());
            //Name changes
            if (UC.getPlayer(p).getPlayerConfig().contains("oldname")) {
                JsonConfig conf = UC.getPlayer(p).getPlayerConfig();
                r.sendMes(p, "nameChanged", "%Oldname", conf.getString("oldname"), "%Newname", p.getName());
                conf.set("oldname", null);
                conf.save();
            }
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerJoinEvent");
        }
    }

    @Listener
    public void onQuit(ClientConnectionEvent.Disconnect e) {
        Player p = e.getTargetEntity();
        try {
            //Inventory
            if (UC.getPlayer(p).isInOfflineInventory()) {
                UC.getPlayer(p).setInOfflineInventory(null);
            }
            if (UC.getPlayer(p).isInOnlineInventory()) {
                UC.getPlayer(p).setInOnlineInventory(null);
            }
            for (Player pl : Sponge.getServer().getOnlinePlayers()) {
                if (UC.getPlayer(pl).getInOnlineInventory().isPresent() && UC.getPlayer(pl).getInOnlineInventory().get().equals(p)) {
                    p.closeInventory(Cause.builder().build());
                    UC.getPlayer(pl).setInOnlineInventory(null);
                }
            }
            UC.getPlayer(p).updateLastInventory();
            //Last connect
            UC.getPlayer(p).updateLastConnectMillis();
            //Vanish
            if (UC.getPlayer(p).isVanish()) {
                p.offer(Keys.INVISIBLE, false);
            }
            //Fly
            if (p.get(Keys.IS_FLYING).get()) {
                LocationUtil.teleport(p, p.getLocation(), Cause.builder().build(), true, false);
                p.offer(Keys.FALL_DISTANCE, 0F);
            }
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerQuitEvent");
        }
    }

    @Listener
    public void onLogin(ClientConnectionEvent.Login e) {
        try {
            User p = e.getTargetUser();
            //Ban
            if (UC.getPlayer(p).isBanned()) {
                if (Sponge.getServiceManager().provide(BanService.class).get().isBanned(p.getProfile())) { //Is account ban, not ip
                    UPlayer pl = UC.getPlayer(p);
                    String time = pl.getBanTime() <= 0 ? r.mes("banForever").toPlain() : (DateUtil.format(pl.getBanTimeLeft()));
                    Text reason = pl.getBanReason();
                    if (reason == null || reason.toPlain().isEmpty()) {
                        reason = r.mes("banDefaultReason");
                    }
                    Text msg = r.mes("banFormat", "%Time", time, "%Reason", reason);
                    e.setMessage(msg);
                } else {
                    UPlayer pl = UC.getPlayer(p);
                    String time = pl.getBanTime() == null ? r.mes("banipForever").toPlain() : (pl.getBanTime() <= 0 ? r.mes("banipForever").toPlain() : (DateUtil.format(pl.getBanTimeLeft
                            ())));
                    Text reason = pl.getBanReason();
                    if (reason == null || reason.isEmpty()) {
                        reason = r.mes("banipDefaultReason");
                    }
                    Text msg = r.mes("banipFormat", "%Time", time, "%Reason", reason);
                    e.setMessage(msg);
                }
            }
            //

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerLoginEvent");
        }
    }

    @Listener
    public void onChat(MessageChannelEvent.Chat e) {
        try {
            Player send = e.getCause().first(Player.class).orElse(null);
            if (send == null) {
                return;
            }
            //Deaf
            MessageChannel channel = e.getChannel().orElse(e.getOriginalChannel());
            channel.getMembers().removeAll(UC.getServer().getDeafOnlinePlayers());
            e.setChannel(channel);
            //Jail
            if (!jailChat && UC.getPlayer(send).isJailed()) {
                r.sendMes(send, "jailNotAllowedTalk");
                e.setCancelled(true);
            }
            //Mute
            if (UC.getPlayer(send).isMuted()) {
                e.setCancelled(true);
                if (UC.getPlayer(send).getMuteTime() == 0 || UC.getPlayer(send).getMuteTime() == -1) {
                    r.sendMes(send, "muteChat");
                } else {
                    r.sendMes(send, "muteChatTime", "%Time", DateUtil.format(UC.getPlayer(send).getMuteTimeLeft()));
                }
                r.sendMes(send, "muteReason", "%Reason", UC.getPlayer(send).getMuteReason());
            }
            //Silence
            if (UC.getServer().isSilenced() && !r.perm(send, "uc.silence.bypass", false)) {
                if (UC.getServer().getSilenceTime() <= 0) {
                    r.sendMes(send, "silenceChat");
                } else {
                    r.sendMes(send, "silenceChatTime", "%Time", DateUtil.format(UC.getServer().getSilenceTimeLeft()));
                }
                e.setCancelled(true);
            }

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: AsyncPlayerChatEvent");
        }
    }

    @Listener
    public void onInventoryClose(InteractInventoryEvent.Close e) {
        try {
            Player p = e.getCause().first(Player.class).orElse(null);
            if (p == null) return;
            //EnchantingTable
            if (UC.getPlayer(p).isInCommandEnchantingtable()) {
                UC.getPlayer(p).setInCommandEnchantingtable(false);
            }
            //Inventory
            if (UC.getPlayer(p.getUniqueId()).isInOfflineInventory()) {
                UC.getPlayer(p.getUniqueId()).setInOfflineInventory(null);
            }
            if (UC.getPlayer(p.getUniqueId()).isInOnlineInventory()) {
                UC.getPlayer(p.getUniqueId()).setInOnlineInventory(null);
            }
            //Recipe
            if (UC.getPlayer(p).isInRecipeView()) {
                UC.getPlayer(p).setInRecipeView(false);
                e.getTargetInventory().clear();
            }
            //Teleportmenu
            if (UC.getPlayer(p.getUniqueId()).isInTeleportMenu()) {
                UC.getPlayer(p.getUniqueId()).setInTeleportMenu(false);
            }
            //Villager
            if (e.getTargetInventory().getName().get().startsWith("Villager ")) {
                CmdVillager.closeInv(p, e.getTargetInventory()); //TODO get top inv?
            }

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: InventoryCloseEvent");
        }
    }

    @Listener
    public void onInventoryClick(final ClickInventoryEvent e) {
        try {
            Player p = e.getCause().first(Player.class).orElse(null);
            if (p == null) {
                return;
            }
            //Inventory
            if (UC.getPlayer(p.getUniqueId()).isInOfflineInventory()) {
                e.setCancelled(true);
            }
            if (UC.getPlayer(p.getUniqueId()).isInOnlineInventory()) {
                if (!r.perm(p, "uc.inventory.edit", true)) {
                    e.setCancelled(true);
                }
            }
            //Recipe
            if (UC.getPlayer(p).isInRecipeView() && e.getTargetInventory().getType()) { //TODO is workbench
                e.setCancelled(true);
            }
            //Teleportmenu
            if (UC.getPlayer(p.getUniqueId()).isInTeleportMenu() && e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta()
                    .hasDisplayName()) {
                UC.getPlayer(p.getUniqueId()).setInTeleportMenu(false);
                Bukkit.getServer().dispatchCommand(p, "tp " + TextColorUtil.strip(e.getCurrentItem().getItemMeta().getDisplayName()));
                e.setCancelled(true);
                p.closeInventory();
            }
            if (!e.isCancelled() && e.getInventory().getTitle().startsWith("Villager ")) {
                e.setCancelled(CmdVillager.clickButton(e));
            }
            //

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: InventoryClickEvent");
        }
    }

    @Listener
    public void onMove(MoveEntityEvent e) {
        if (!(e.getTargetEntity() instanceof Player)) {
            return;
        }
        if (e.getFromTransform().getPosition().toInt().equals(e.getToTransform().getPosition().toInt())) {
            return;
        }
        Player p = (Player) e.getTargetEntity();
        try {
            //Freeze
            if (UC.getPlayer(p).isFrozen()) {
                Location loc = e.getFrom().getBlock().getLocation().add(0.5, 0.1, 0.5);
                loc.setPitch(e.getFrom().getPitch());
                loc.setYaw(e.getFrom().getYaw());
                e.setTo(loc);
            }

            //Jail
            if (!jailedmove && UC.getPlayer(p).isJailed()) {
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

    @Listener
    public void onDeath(PlayerDeathEvent e) {
        try {
            //Back
            if (r.perm(e.getEntity(), "uc.back.death", true, false)) {
                UC.getPlayer(e.getEntity()).setLastLocation();
                r.sendMes(e.getEntity(), "backDeathMessage");
            } else {
                UC.getPlayer(e.getEntity()).setLastLocation(null);
            }

            //
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerMoveEvent");
        }
    }

    @Listener
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

    @Listener
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        try {
            //Jailed
            if (e.getEntity().getType().equals(EntityType.PLAYER)) {
                Player p = (Player) e.getEntity();
                if (UC.getPlayer(p).isJailed()) {
                    p.setFireTicks(0);
                    e.setCancelled(true);
                }
            }
            if (e.getDamager().getType().equals(EntityType.PLAYER)) {
                Player p = (Player) e.getDamager();
                if (UC.getPlayer(p).isJailed()) {
                    p.setFireTicks(0);
                    e.setCancelled(true);
                }
            }
            //
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: EntityDamageByEntityEvent");
        }
    }

    @Listener
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

    @Listener
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

    @Listener
    public void onInteract(PlayerInteractEvent e) {
        try {
            //Mobtp
            if (!e.isCancelled() && !e.getAction().equals(Action.PHYSICAL)) {
                e.setCancelled(CmdMobtp.place(e.getPlayer()));
            }
            //Powertool
            if (!e.getAction().equals(Action.PHYSICAL) && e.getPlayer() != null && e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getType() != null && !e.getPlayer
                    ().getItemInHand().getType().equals(Material.AIR) && UC.getPlayer(e.getPlayer()).hasPowertool(e.getPlayer().getItemInHand().getType())) {
                for (String s : UC.getPlayer(e.getPlayer()).getPowertools(e.getPlayer().getItemInHand().getType())) {
                    e.getPlayer().performCommand(s);
                }
            }

            //
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerInteractEvent");
        }
    }

    @Listener
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        try {
            //Mobtp
            if (!e.isCancelled()) {
                e.setCancelled(CmdMobtp.pickup(e.getPlayer(), e.getRightClicked()));
            }
            //Villager
            if (!e.isCancelled() && e.getRightClicked() instanceof Villager) {
                e.setCancelled(CmdVillager.confirm(e.getPlayer(), (Villager) e.getRightClicked()));
            }
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerInteractEntityEvent");
        }
    }

    @Listener
    public void onWorldChange(PlayerChangedWorldEvent e) {
        try {
            //Gamemode
            if (UC.getWorld(e.getPlayer().getWorld()).getDefaultGamemode() != null && !r.perm(e.getPlayer(), "uc.world.flag.override", false, false)) {
                e.getPlayer().setGameMode(UC.getWorld(e.getPlayer().getWorld()).getDefaultGamemode());
            }
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerChangedWorldEvent");
        }
    }

    public void onRespawn(RespawnPlayerEvent e) {
        try {
            if (e.getPlayer().getBedSpawnLocation() != null) {
                e.setRespawnLocation(e.getPlayer().getBedSpawnLocation());
            } else if (UC.getPlayer(e.getPlayer()).getSpawn(false) != null) {
                e.setRespawnLocation(UC.getPlayer(e.getPlayer()).getSpawn(false));
            }
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: PlayerRespawnEvent");
        }
    }

}

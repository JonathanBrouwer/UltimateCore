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
package bammerbom.ultimatecore.spongeapi.commands;

import bammerbom.ultimatecore.spongeapi.UltimateCommand;
import bammerbom.ultimatecore.spongeapi.r;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSource;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class CmdMobtp implements UltimateCommand {

    //
    static Integer entitynumber = 0;
    static HashMap<Integer, Entity> sticks = new HashMap<>();
    static ArrayList<UUID> cantdrop = new ArrayList<>();

    public static boolean pickup(final Player p, Entity en) {
        if (cantdrop.contains(p.getUniqueId())) {
            return false;
        }
        if (!(en instanceof LivingEntity)) {
            return false;
        }
        if (en instanceof Player) {
            return false;
        }
        if (!r.perm(p, "uc.mobtp", false, false)) {
            return false;
        }
        if (p.getItemInHand() == null || p.getItemInHand().getType() == null) {
            return false;
        }
        if (p.getItemInHand().getItemMeta() == null || p.getItemInHand().getItemMeta().getDisplayName() == null) {
            return false;
        }
        if (!p.getItemInHand().getType().equals(Material.STICK)) {
            return false;
        }
        if (!p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "MobTP: select an " +
                "entity (right click)")) {
            return false;
        }
        ItemStack stick = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = stick.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "MobTP: place entity (right click)");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Select an entity to teleport, then right click to to pick it up.");
        lore.add(ChatColor.GRAY + "" + ChatColor.UNDERLINE + "Then right click again to place the entity");
        lore.add(ChatColor.BLACK + "ID: " + entitynumber);
        sticks.put(entitynumber, en);
        meta.setLore(lore);
        stick.setItemMeta(meta);
        p.setItemInHand(stick);
        entitynumber++;
        en.remove();
        cantdrop.add(p.getUniqueId());
        Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {
            @Override
            public void run() {
                cantdrop.remove(p.getUniqueId());
            }
        }, 20L);
        return true;
    }

    public static boolean place(Player p) {
        try { //I dont see another solution for this
            if (cantdrop.contains(p.getUniqueId())) {
                return false;
            }
            if (p.getItemInHand() == null || p.getItemInHand().getType() == null) {
                return false;
            }
            if (p.getItemInHand().getItemMeta() == null || p.getItemInHand().getItemMeta().getDisplayName() == null) {
                return false;
            }
            if (!p.getItemInHand().getType().equals(Material.BLAZE_ROD)) {
                return false;
            }
            if (!p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "MobTP: place " +
                    "entity (right click)")) {
                return false;
            }
            if (!r.perm(p, "uc.mobtp", false, false)) {
                return false;
            }
            ItemStack old = p.getItemInHand();
            ItemStack stick = new ItemStack(Material.STICK);
            ItemMeta meta = stick.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "MobTP: select an entity (right click)");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "" + ChatColor.UNDERLINE + "Find an entity to teleport, then right click to to " +
                    "pick it up.");
            lore.add(ChatColor.GRAY + "Then right click again to place the entity");
            meta.setLore(lore);
            stick.setItemMeta(meta);
            p.getInventory().setItemInHand(stick);
            String id2 = old.getItemMeta().getLore().get(2);
            Integer id = Integer.parseInt(ChatColor.stripColor(id2.replaceAll("ID: ", "")));
            LivingEntity eo = (LivingEntity) sticks.get(id);
            if (eo == null) {
                return false;
            }
            LivingEntity en = (LivingEntity) p.getLocation().getWorld().spawnEntity(p.getLocation(), eo.getType());
            en.setCanPickupItems(eo.getCanPickupItems());
            en.setCustomName(eo.getCustomName());
            en.setCustomNameVisible(eo.isCustomNameVisible());
            en.setFireTicks(eo.getFireTicks());
            en.setMaxHealth(eo.getMaxHealth());
            en.setHealth(eo.getHealth());
            en.setLastDamage(eo.getLastDamage());
            en.setLastDamageCause(eo.getLastDamageCause());
            en.setMaximumAir(eo.getMaximumAir());
            en.setMaximumNoDamageTicks(eo.getMaximumNoDamageTicks());
            en.setNoDamageTicks(eo.getNoDamageTicks());
            en.setPassenger(eo.getPassenger());
            en.setRemainingAir(eo.getRemainingAir());
            en.setRemoveWhenFarAway(eo.getRemoveWhenFarAway());
            en.setTicksLived(eo.getTicksLived());
            en.setVelocity(eo.getVelocity());
            en.getEquipment().setArmorContents(eo.getEquipment().getArmorContents());
            en.getEquipment().setItemInHandDropChance(eo.getEquipment().getItemInHandDropChance());
            for (PotionEffect ef : eo.getActivePotionEffects()) {
                en.addPotionEffect(ef);
            }
            if (eo instanceof Ageable) {
                Ageable eo2 = (Ageable) eo;
                Ageable en2 = (Ageable) en;
                en2.setAgeLock(eo2.getAgeLock());
                en2.setAge(eo2.getAge());
                en2.setBreed(eo2.canBreed());
                if (!eo2.isAdult()) {
                    en2.setBaby();
                }
            }
            if (eo instanceof Tameable) {
                Tameable eo2 = (Tameable) eo;
                Tameable en2 = (Tameable) en;
                en2.setOwner(eo2.getOwner());
                en2.setTamed(eo2.isTamed());
            }
            if (eo instanceof Skeleton) {
                Skeleton eo2 = (Skeleton) eo;
                Skeleton en2 = (Skeleton) en;
                en2.setSkeletonType(eo2.getSkeletonType());
            }
            if (eo instanceof Zombie) {
                Zombie eo2 = (Zombie) eo;
                Zombie en2 = (Zombie) en;
                en2.setBaby(eo2.isBaby());
                en2.setVillager(eo2.isVillager());
            }
            if (eo instanceof Horse) {
                Horse eo2 = (Horse) eo;
                Horse en2 = (Horse) en;
                en2.setColor(eo2.getColor());
                en2.setCarryingChest(eo2.isCarryingChest());
                en2.setDomestication(eo2.getDomestication());
                en2.setJumpStrength(eo2.getJumpStrength());
                en2.setMaxDomestication(eo2.getMaxDomestication());
                en2.setStyle(eo2.getStyle());
                en2.setVariant(eo2.getVariant());
                en2.getInventory().setSaddle(eo2.getInventory().getSaddle());
                en2.getInventory().setArmor(eo2.getInventory().getArmor());
            }
            if (eo instanceof Pig) {
                Pig eo2 = (Pig) eo;
                Pig en2 = (Pig) en;
                en2.setSaddle(eo2.hasSaddle());
            }
            if (eo instanceof Sheep) {
                Sheep eo2 = (Sheep) eo;
                Sheep en2 = (Sheep) en;
                en2.setColor(eo2.getColor());
                en2.setSheared(eo2.isSheared());
            }
            if (eo instanceof Wolf) {
                Wolf eo2 = (Wolf) eo;
                Wolf en2 = (Wolf) en;
                en2.setCollarColor(eo2.getCollarColor());
            }
            if (eo instanceof Slime) {
                Slime eo2 = (Slime) eo;
                Slime en2 = (Slime) en;
                en2.setSize(eo2.getSize());
            }
            if (eo instanceof MagmaCube) {
                MagmaCube eo2 = (MagmaCube) eo;
                MagmaCube en2 = (MagmaCube) en;
                en2.setSize(eo2.getSize());
            }
            if (eo instanceof Creeper) {
                Creeper eo2 = (Creeper) eo;
                Creeper en2 = (Creeper) en;
                en2.setPowered(eo2.isPowered());
            }
            if (eo instanceof Ocelot) {
                Ocelot eo2 = (Ocelot) eo;
                Ocelot en2 = (Ocelot) en;
                en2.setCatType(eo2.getCatType());
            }
            if (eo instanceof Villager) {
                Villager eo2 = (Villager) eo;
                Villager en2 = (Villager) en;
                en2.setProfession(eo2.getProfession());
            }
            return false;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public String getName() {
        return "mobtp";
    }

    @Override
    public String getPermission() {
        return "uc.mobtp";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSource cs, String label, String[] args) {
        if (!r.isPlayer(cs)) {
            return;
        }
        if (!r.perm(cs, "uc.mobtp", false, true)) {
            return;
        }
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "MobTP: select an entity (right click)");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.UNDERLINE + "Find an entity to teleport, then right click to to pick" +
                " it up.");
        lore.add(ChatColor.GRAY + "Then right click again to place the entity");
        meta.setLore(lore);
        stick.setItemMeta(meta);
        Player p = (Player) cs;
        p.getInventory().addItem(stick);
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}

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
import bammerbom.ultimatecore.spongeapi.resources.utils.TextColorUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.*;

public class CmdMobtp implements UltimateCommand {

    //
    static Integer entitynumber = 0;
    static HashMap<Integer, EntitySnapshot> sticks = new HashMap<>();
    static ArrayList<UUID> cantdrop = new ArrayList<>();

    public static boolean pickup(final Player p, Entity en) {
        if (cantdrop.contains(p.getUniqueId())) {
            return false;
        }
        if (!(en instanceof Living)) {
            return false;
        }
        if (en instanceof Player) {
            return false;
        }
        if (!r.perm(p, "uc.mobtp", false)) {
            return false;
        }
        if (!p.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
            return false;
        }
        if (!p.getItemInHand(HandTypes.MAIN_HAND).get().getItem().equals(ItemTypes.STICK)) {
            return false;
        }
        if (!p.getItemInHand(HandTypes.MAIN_HAND).get().get(Keys.DISPLAY_NAME).get().equals(Text.of(TextColors.AQUA + "MobTP: select an " +
                "entity (right click)"))) {
            return false;
        }
        ItemStack stick = ItemStack.builder().itemType(ItemTypes.BLAZE_ROD).build();
        stick.offer(Keys.DISPLAY_NAME, Text.of(TextColors.AQUA + "MobTP: place entity (right click)"));
        ArrayList<Text> lore = new ArrayList<>();
        lore.add(Text.of(TextColors.GRAY + "Select an entity to teleport, then right click to to pick it up."));
        lore.add(Text.of(TextColors.GRAY + "" + TextStyles.UNDERLINE + "Then right click again to place the entity"));
        lore.add(Text.of(TextColors.BLACK + "ID: " + entitynumber));
        sticks.put(entitynumber, en.createSnapshot());
        stick.offer(Keys.ITEM_LORE, lore);
        p.setItemInHand(HandTypes.MAIN_HAND, stick);
        entitynumber++;
        en.remove();
        cantdrop.add(p.getUniqueId());
        Sponge.getScheduler().createTaskBuilder().delayTicks(20L).execute(new Runnable() {
            @Override
            public void run() {
                cantdrop.remove(p.getUniqueId());
            }
        }).submit(r.getUC());
        return true;
    }

    public static boolean place(Player p) {
        try { //I dont see another solution for this
            if (cantdrop.contains(p.getUniqueId())) {
                return false;
            }
            if (!p.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
                return false;
            }
            if (!p.getItemInHand(HandTypes.MAIN_HAND).get().getItem().equals(ItemTypes.BLAZE_ROD)) {
                return false;
            }
            if (!p.getItemInHand(HandTypes.MAIN_HAND).get().get(Keys.DISPLAY_NAME).get().equals(Text.of(TextColors.AQUA + "MobTP: place " +
                    "entity (right click)"))) {
                return false;
            }
            if (!r.perm(p, "uc.mobtp", false)) {
                return false;
            }
            ItemStack old = p.getItemInHand(HandTypes.MAIN_HAND).get();
            ItemStack stick = ItemStack.builder().itemType(ItemTypes.STICK).build();
            stick.offer(Keys.DISPLAY_NAME, Text.of(TextColors.AQUA + "MobTP: select an entity (right click)"));
            ArrayList<Text> lore = new ArrayList<>();
            lore.add(Text.of(TextColors.GRAY + "" + TextStyles.UNDERLINE + "Find an entity to teleport, then right click to to " +
                    "pick it up."));
            lore.add(Text.of(TextColors.GRAY + "Then right click again to place the entity"));
            stick.offer(Keys.ITEM_LORE, lore);
            p.setItemInHand(HandTypes.MAIN_HAND, stick);
            String id2 = old.get(Keys.ITEM_LORE).get().get(2).toPlain();
            Integer id = Integer.parseInt(TextColorUtil.strip(id2.replaceAll("ID: ", "")));
            Entity en = sticks.get(id).restore().orElse(null);
            if (en == null) {
                return false;
            }
            en.setLocation(p.getLocation());
            p.getWorld().spawnEntity(en, Cause.builder().build());
            return true;
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
    public String getUsage() {
        return "/<command>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Teleport mobs using a stick.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.isPlayer(cs)) {
            return CommandResult.empty();
        }
        if (!r.perm(cs, "uc.mobtp", true)) {
            return CommandResult.empty();
        }
        ItemStack stick = ItemStack.builder().itemType(ItemTypes.STICK).build();
        stick.offer(Keys.DISPLAY_NAME, Text.of(TextColors.AQUA + "MobTP: select an entity (right click)"));
        ArrayList<Text> lore = new ArrayList<>();
        lore.add(Text.of(TextColors.GRAY + "" + TextStyles.UNDERLINE + "Find an entity to teleport, then right click to to " +
                "pick it up."));
        lore.add(Text.of(TextColors.GRAY + "Then right click again to place the entity"));
        stick.offer(Keys.ITEM_LORE, lore);
        Player p = (Player) cs;
        p.getInventory().offer(stick);
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}

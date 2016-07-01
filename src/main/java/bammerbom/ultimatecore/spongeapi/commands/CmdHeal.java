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
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CmdHeal implements UltimateCommand {

    static Boolean healPositiveEffects = false;
    static HashMap<PotionEffectType, State> effectStates = new HashMap<>();

    static {
        effectStates.put(PotionEffectTypes.ABSORPTION, State.POSITIVE);
        effectStates.put(PotionEffectTypes.BLINDNESS, State.NEGATIVE);
        effectStates.put(PotionEffectTypes.FIRE_RESISTANCE, State.POSITIVE);
        effectStates.put(PotionEffectTypes.GLOWING, State.NEGATIVE);
        effectStates.put(PotionEffectTypes.HASTE, State.POSITIVE);
        effectStates.put(PotionEffectTypes.HEALTH_BOOST, State.POSITIVE);
        effectStates.put(PotionEffectTypes.HUNGER, State.NEGATIVE);
        effectStates.put(PotionEffectTypes.INSTANT_DAMAGE, State.NEGATIVE);
        effectStates.put(PotionEffectTypes.INSTANT_HEALTH, State.POSITIVE);
        effectStates.put(PotionEffectTypes.INVISIBILITY, State.NEGATIVE);
        effectStates.put(PotionEffectTypes.JUMP_BOOST, State.POSITIVE);
        effectStates.put(PotionEffectTypes.LEVITATION, State.NEGATIVE);
        effectStates.put(PotionEffectTypes.LUCK, State.POSITIVE);
        effectStates.put(PotionEffectTypes.MINING_FATIGUE, State.NEGATIVE);
        effectStates.put(PotionEffectTypes.NAUSEA, State.NEGATIVE);
        effectStates.put(PotionEffectTypes.NIGHT_VISION, State.POSITIVE);
        effectStates.put(PotionEffectTypes.POISON, State.NEGATIVE);
        effectStates.put(PotionEffectTypes.REGENERATION, State.POSITIVE);
        effectStates.put(PotionEffectTypes.RESISTANCE, State.POSITIVE);
        effectStates.put(PotionEffectTypes.SATURATION, State.POSITIVE);
        effectStates.put(PotionEffectTypes.SLOWNESS, State.NEGATIVE);
        effectStates.put(PotionEffectTypes.SPEED, State.POSITIVE);
        effectStates.put(PotionEffectTypes.STRENGTH, State.POSITIVE);
        effectStates.put(PotionEffectTypes.UNLUCK, State.NEGATIVE);
        effectStates.put(PotionEffectTypes.WATER_BREATHING, State.POSITIVE);
        effectStates.put(PotionEffectTypes.WEAKNESS, State.NEGATIVE);
        effectStates.put(PotionEffectTypes.WITHER, State.NEGATIVE);
    }

    public static void start() {
        if (r.getCnfg().getBoolean("HealPositive")) {
            healPositiveEffects = true;
        }
    }

    @Override
    public String getName() {
        return "heal";
    }

    @Override
    public String getPermission() {
        return "uc.heal";
    }

    @Override
    public String getUsage() {
        return "/<command> [Player]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Fills the target's health bar.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.heal", true)) {
            return CommandResult.empty();
        }
        if (r.checkArgs(args, 0) == false) {
            if (!(r.isPlayer(cs))) {
                return CommandResult.empty();
            }
            Player p = (Player) cs;
            p.offer(Keys.HEALTH, p.get(Keys.MAX_HEALTH).orElse(20.0));
            List<PotionEffect> effects = p.get(Keys.POTION_EFFECTS).orElse(new ArrayList<>());
            List<PotionEffect> remove = new ArrayList<>();
            for (PotionEffect effect : effects) {
                if (!healPositiveEffects) {
                    if (!effectStates.containsKey(effect.getType()) || effectStates.get(effect).equals(State.NEGATIVE)) {
                        remove.add(effect);
                    }
                } else {
                    remove.add(effect);
                }
            }
            effects.removeAll(remove);
            p.offer(Keys.POTION_EFFECTS, effects);
            p.offer(Keys.HEALTH, p.get(Keys.MAX_HEALTH).orElse(20.0));
            p.offer(Keys.FOOD_LEVEL, 20);
            p.offer(Keys.SATURATION, 10.0);
            p.offer(Keys.FIRE_TICKS, 0);
            p.offer(Keys.REMAINING_AIR, p.get(Keys.MAX_AIR).orElse(10));
            r.sendMes(cs, "healSelf");
        } else {
            if (!r.perm(cs, "uc.heal.others", true)) {
                return CommandResult.empty();
            }
            Player t = r.searchPlayer(args[0]).orElse(null);
            if (t == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            } else {
                r.sendMes(cs, "healOthersSelf", "%Player", t.getName());
                r.sendMes(t, "healOthersOther", "%Player", r.getDisplayName(cs));
                List<PotionEffect> effects = t.get(Keys.POTION_EFFECTS).orElse(new ArrayList<>());
                List<PotionEffect> remove = new ArrayList<>();
                for (PotionEffect effect : effects) {
                    if (!healPositiveEffects) {
                        if (!effectStates.containsKey(effect.getType()) || effectStates.get(effect).equals(State.NEGATIVE)) {
                            remove.add(effect);
                        }
                    } else {
                        remove.add(effect);
                    }
                }
                effects.removeAll(remove);
                t.offer(Keys.POTION_EFFECTS, effects);
                t.offer(Keys.HEALTH, t.get(Keys.MAX_HEALTH).orElse(20.0));
                t.offer(Keys.FOOD_LEVEL, 20);
                t.offer(Keys.SATURATION, 10.0);
                t.offer(Keys.FIRE_TICKS, 0);
                t.offer(Keys.REMAINING_AIR, t.get(Keys.MAX_AIR).orElse(10));
            }
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }

    enum State {
        POSITIVE,
        NEGATIVE
    }
}

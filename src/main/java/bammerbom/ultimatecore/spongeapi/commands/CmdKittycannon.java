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
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.OcelotType;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.animal.Ocelot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;

public class CmdKittycannon implements UltimateCommand {

    Random ra = new Random();

    @Override
    public String getName() {
        return "kittycannon";
    }

    @Override
    public String getPermission() {
        return "uc.kittycannon";
    }

    @Override
    public String getUsage() {
        return "/<command>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Summon an exploding ocelot.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("kittyboom");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.isPlayer(cs)) {
            return CommandResult.empty();
        }
        if (!r.perm(cs, "uc.kittycannon", true)) {
            return CommandResult.empty();
        }
        Player p = (Player) cs;
        final Ocelot ocelot = (Ocelot) p.getWorld().createEntity(EntityTypes.OCELOT, p.getLocation().getPosition()).orElse(null);
        if (ocelot == null) {
            return CommandResult.empty();
        }
        int i = ra.nextInt(Sponge.getRegistry().getAllOf(CatalogTypes.OCELOT_TYPE).size());
        OcelotType type = (OcelotType) Sponge.getRegistry().getAllOf(CatalogTypes.OCELOT_TYPE).toArray()[i];
        ocelot.offer(Keys.OCELOT_TYPE, type);
        ocelot.offer(Keys.TAMED_OWNER, Optional.of(p.getUniqueId()));
        i = ra.nextInt(2);
        if (i == 1) {
            ocelot.offer(Keys.AGE, 0); //TODO baby?
        }
        ocelot.setVelocity(p.getLocation().add(0, 1, 0).getPosition());
        Sponge.getScheduler().createTaskBuilder().name("Kittycannon task").delayTicks(20L).execute(new Runnable() {
            @Override
            public void run() {
                Location<World> loc = ocelot.getLocation();
                ocelot.remove();
                loc.getExtent().spawnParticles(ParticleEffect.builder().type(ParticleTypes.EXPLOSION_NORMAL).build(), loc.getPosition());
            }
        }).submit(r.getUC());
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}

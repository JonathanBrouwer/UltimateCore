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
package bammerbom.ultimatecore.sponge.modules.migrator.migrators;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.teleport.serializabletransform.SerializableTransform;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.back.api.BackKeys;
import bammerbom.ultimatecore.sponge.modules.home.api.Home;
import bammerbom.ultimatecore.sponge.modules.home.api.HomeKeys;
import bammerbom.ultimatecore.sponge.modules.jail.api.Jail;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailKeys;
import bammerbom.ultimatecore.sponge.modules.kit.api.Kit;
import bammerbom.ultimatecore.sponge.modules.kit.api.KitKeys;
import bammerbom.ultimatecore.sponge.modules.mail.api.Mail;
import bammerbom.ultimatecore.sponge.modules.mail.api.MailKeys;
import bammerbom.ultimatecore.sponge.modules.mute.api.Mute;
import bammerbom.ultimatecore.sponge.modules.mute.api.MuteKeys;
import bammerbom.ultimatecore.sponge.modules.warp.api.Warp;
import io.github.nucleuspowered.nucleus.api.nucleusdata.MuteInfo;
import io.github.nucleuspowered.nucleus.api.nucleusdata.NamedLocation;
import io.github.nucleuspowered.nucleus.api.service.*;
import io.github.nucleuspowered.nucleus.iapi.service.NucleusJailService;
import io.github.nucleuspowered.nucleus.modules.jail.data.JailData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Identifiable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NucleusMigrator implements Migrator {
    @Override
    public boolean canMigrate() {
        return Sponge.getPluginManager().isLoaded("nucleus");
    }

    @Override
    public boolean migrateFrom() {
        if (!canMigrate()) return false;

        //GLOBAL DATA

        //Jail
        NucleusJailService jailService = Sponge.getServiceManager().provide(NucleusJailService.class).orElse(null);
        if (Modules.JAIL.isPresent() && jailService != null) {
            List<Jail> jails = new ArrayList<>();
            for (NamedLocation jail : jailService.getJails().values()) {
                if (!jail.getTransform().isPresent()) continue;
                Jail ujail = new Jail(jail.getName(), Messages.getFormatted("jail.command.setjail.defaultdescription").toPlain(), jail.getTransform().get());
                jails.add(ujail);
            }
            GlobalData.offer(JailKeys.JAILS, jails);
        }

        //Kits
        NucleusKitService kitService = Sponge.getServiceManager().provide(NucleusKitService.class).orElse(null);
        if (Modules.KIT.isPresent() && kitService != null) {
            List<Kit> kits = new ArrayList<>();
            for (String name : kitService.getKitNames()) {
                io.github.nucleuspowered.nucleus.api.nucleusdata.Kit kit = kitService.getKit(name).get();
                Kit ukit = new Kit(
                        name,
                        Messages.getFormatted("kit.defaultdescription").toPlain(),
                        kit.getStacks(),
                        kit.getCommands(),
                        kit.getInterval().toMillis()
                );
                kits.add(ukit);
            }
            GlobalData.offer(KitKeys.KITS, kits);
        }

        //Warp
        NucleusWarpService warpService = Sponge.getServiceManager().provide(NucleusWarpService.class).orElse(null);
        if (Modules.WARP.isPresent() && warpService != null) {
            List<Warp> warps = new ArrayList<>();
            for (io.github.nucleuspowered.nucleus.api.nucleusdata.Warp warp : warpService.getAllWarps()) {
                if (!warp.getTransform().isPresent()) continue;
                warps.add(new Warp(
                        warp.getName(),
                        warp.getDescription().orElse(Messages.getFormatted("warp.command.setwarp.defaultdescription")).toPlain(),
                        warp.getTransform().get()
                ));
            }
        }

        //USER DATA
        UserStorageService service = Sponge.getServiceManager().provide(UserStorageService.class).get();
        for (GameProfile profile : Sponge.getServiceManager().provide(UserStorageService.class).get().getAll()) {
            //Get user
            User user = service.get(profile).orElse(null);
            if (user == null) continue;
            UltimateUser uu = UltimateCore.get().getUserService().getUser(user);

            //Back
            NucleusBackService backService = Sponge.getServiceManager().provide(NucleusBackService.class).orElse(null);
            if (Modules.BACK.isPresent() && backService != null && backService.getLastLocation(user).isPresent()) {
                uu.offer(BackKeys.BACK, new SerializableTransform(backService.getLastLocation(user).get()));
            }

            //TODO god (Waiting for nucleus)

            //Home
            NucleusHomeService homeService = Sponge.getServiceManager().provide(NucleusHomeService.class).orElse(null);
            if (Modules.HOME.isPresent() && homeService != null) {
                List<Home> homes = new ArrayList<>();
                for (io.github.nucleuspowered.nucleus.api.nucleusdata.Home home : homeService.getHomes(user)) {
                    if (home.getTransform().isPresent()) {
                        homes.add(new Home(home.getName(), home.getTransform().get()));
                    }
                }
                uu.offer(HomeKeys.HOMES, homes);
            }

            //Jail
            if (Modules.JAIL.isPresent() && jailService != null) {
                if (jailService.isPlayerJailed(user)) {
                    JailData data = jailService.getPlayerJailData(user).get();
                    uu.offer(JailKeys.JAIL, new bammerbom.ultimatecore.sponge.modules.jail.api.JailData(
                            user.getUniqueId(),
                            data.getJailer(),
                            data.getEndTimestamp().map(Instant::toEpochMilli).orElse(-1L),
                            System.currentTimeMillis(),
                            Text.of(data.getReason()),
                            data.getJailName()
                    ));
                }
            }

            //TODO kits (Waiting for nucleus)

            //Mail
            NucleusMailService mailService = Sponge.getServiceManager().provide(NucleusMailService.class).orElse(null);
            if (Modules.MAIL.isPresent() && mailService != null) {
                List<Mail> mails = new ArrayList<>();
                mailService.getMail(user).forEach(mailMessage -> {
                    mails.add(new Mail(
                            mailMessage.getSender().map(Identifiable::getUniqueId).orElse(user.getUniqueId()),
                            Arrays.asList(user.getUniqueId()),
                            mailMessage.getDate().toEpochMilli(),
                            mailMessage.getMessage()
                    ));
                });
                uu.offer(MailKeys.MAILS_RECEIVED, mails);
                uu.offer(MailKeys.UNREAD_MAIL, mails.size());
            }

            //Mute
            NucleusMuteService muteService = Sponge.getServiceManager().provide(NucleusMuteService.class).orElse(null);
            if (Modules.MUTE.isPresent() && muteService != null) {
                if (muteService.isMuted(user)) {
                    MuteInfo data = muteService.getPlayerMuteInfo(user).get();
                    uu.offer(MuteKeys.MUTE, new Mute(
                            user.getUniqueId(),
                            data.getMuter().orElse(user.getUniqueId()),
                            data.getRemainingTime().map(time -> time.toMillis() + System.currentTimeMillis()).orElse(-1L),
                            System.currentTimeMillis(),
                            Text.of(data.getReason())
                    ));
                }
            }

            //TODO nick (Waiting for nucleus)

            //TODO spy (Waiting for me to not be lazy)
        }

        return true;
    }
}

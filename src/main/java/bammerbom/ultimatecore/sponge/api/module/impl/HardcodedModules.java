package bammerbom.ultimatecore.sponge.api.module.impl;

import bammerbom.ultimatecore.sponge.modules.afk.AfkModule;
import bammerbom.ultimatecore.sponge.modules.automessage.AutomessageModule;
import bammerbom.ultimatecore.sponge.modules.back.BackModule;
import bammerbom.ultimatecore.sponge.modules.ban.BanModule;
import bammerbom.ultimatecore.sponge.modules.blacklist.BlacklistModule;
import bammerbom.ultimatecore.sponge.modules.blockinfo.BlockinfoModule;
import bammerbom.ultimatecore.sponge.modules.blockprotection.BlockprotectionModule;
import bammerbom.ultimatecore.sponge.modules.blood.BloodModule;
import bammerbom.ultimatecore.sponge.modules.broadcast.BroadcastModule;
import bammerbom.ultimatecore.sponge.modules.burn.BurnModule;
import bammerbom.ultimatecore.sponge.modules.chat.ChatModule;
import bammerbom.ultimatecore.sponge.modules.commandtimer.CommandtimerModule;
import bammerbom.ultimatecore.sponge.modules.connectionmessages.ConnectionmessagesModule;
import bammerbom.ultimatecore.sponge.modules.core.CoreModule;
import bammerbom.ultimatecore.sponge.modules.deaf.DeafModule;
import bammerbom.ultimatecore.sponge.modules.deathmessage.DeathmessageModule;
import bammerbom.ultimatecore.sponge.modules.fly.FlyModule;
import bammerbom.ultimatecore.sponge.modules.food.FoodModule;
import bammerbom.ultimatecore.sponge.modules.gamemode.GamemodeModule;
import bammerbom.ultimatecore.sponge.modules.geoip.GeoipModule;
import bammerbom.ultimatecore.sponge.modules.god.GodModule;
import bammerbom.ultimatecore.sponge.modules.heal.HealModule;
import bammerbom.ultimatecore.sponge.modules.home.HomeModule;
import bammerbom.ultimatecore.sponge.modules.inventory.InventoryModule;
import bammerbom.ultimatecore.sponge.modules.item.ItemModule;
import bammerbom.ultimatecore.sponge.modules.jail.JailModule;
import bammerbom.ultimatecore.sponge.modules.kick.KickModule;
import bammerbom.ultimatecore.sponge.modules.kit.KitModule;
import bammerbom.ultimatecore.sponge.modules.mail.MailModule;
import bammerbom.ultimatecore.sponge.modules.migrator.MigratorModule;
import bammerbom.ultimatecore.sponge.modules.mute.MuteModule;
import bammerbom.ultimatecore.sponge.modules.nick.NickModule;
import bammerbom.ultimatecore.sponge.modules.personalmessage.PersonalmessageModule;
import bammerbom.ultimatecore.sponge.modules.playerinfo.PlayerinfoModule;
import bammerbom.ultimatecore.sponge.modules.poke.PokeModule;
import bammerbom.ultimatecore.sponge.modules.random.RandomModule;
import bammerbom.ultimatecore.sponge.modules.serverlist.ServerlistModule;
import bammerbom.ultimatecore.sponge.modules.sign.SignModule;
import bammerbom.ultimatecore.sponge.modules.spawn.SpawnModule;
import bammerbom.ultimatecore.sponge.modules.spy.SpyModule;
import bammerbom.ultimatecore.sponge.modules.sudo.SudoModule;
import bammerbom.ultimatecore.sponge.modules.tablist.TablistModule;
import bammerbom.ultimatecore.sponge.modules.teleport.TeleportModule;
import bammerbom.ultimatecore.sponge.modules.time.TimeModule;
import bammerbom.ultimatecore.sponge.modules.unknowncommand.UnknowncommandModule;
import bammerbom.ultimatecore.sponge.modules.vanish.VanishModule;
import bammerbom.ultimatecore.sponge.modules.votifier.VotifierModule;
import bammerbom.ultimatecore.sponge.modules.warp.WarpModule;
import bammerbom.ultimatecore.sponge.modules.weather.WeatherModule;
import bammerbom.ultimatecore.sponge.modules.world.WorldModule;

public class HardcodedModules {
    public static Class<?>[] modules = new Class[] {
            AfkModule.class,
            AutomessageModule.class,
            BackModule.class,
            BanModule.class,
            BlacklistModule.class,
            BlockinfoModule.class,
            BlockprotectionModule.class,
            BloodModule.class,
            BroadcastModule.class,
            BurnModule.class,
            ChatModule.class,
            CommandtimerModule.class,
            ConnectionmessagesModule.class,
            CoreModule.class,
            DeafModule.class,
            DeathmessageModule.class,
            FlyModule.class,
            FoodModule.class,
            GamemodeModule.class,
            GeoipModule.class,
            GodModule.class,
            HealModule.class,
            HomeModule.class,
            InventoryModule.class,
            ItemModule.class,
            JailModule.class,
            KickModule.class,
            KitModule.class,
            MailModule.class,
            MigratorModule.class,
            MuteModule.class,
            NickModule.class,
            PersonalmessageModule.class,
            PlayerinfoModule.class,
            PokeModule.class,
            RandomModule.class,
            ServerlistModule.class,
            SignModule.class,
            SpawnModule.class,
            SpyModule.class,
            SudoModule.class,
            TablistModule.class,
            TeleportModule.class,
            TimeModule.class,
            UnknowncommandModule.class,
            VanishModule.class,
            VotifierModule.class,
            WarpModule.class,
            WeatherModule.class,
            WorldModule.class
    };
}

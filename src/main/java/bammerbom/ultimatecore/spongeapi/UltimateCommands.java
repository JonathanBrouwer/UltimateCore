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
package bammerbom.ultimatecore.spongeapi;

import bammerbom.ultimatecore.spongeapi.commands.CmdAccountstatus;
import bammerbom.ultimatecore.spongeapi.commands.CmdAfk;
import bammerbom.ultimatecore.spongeapi.commands.CmdAlert;
import bammerbom.ultimatecore.spongeapi.commands.CmdAnswer;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.util.ArrayList;
import java.util.List;

public class UltimateCommands {

    public static List<UltimateCommandExecutor> cmds = new ArrayList<>();
    public static List<String> disabled;
    public static UltimateCommands ucmds;

    public static void load() {
        disabled = r.getCnfg().getStringList("Command.DisabledCommands");
        cmds.add(new CmdAccountstatus());
        cmds.add(new CmdAfk());
        cmds.add(new CmdAlert());
        cmds.add(new CmdAnswer());
        cmds.add(new CmdAsk());
        cmds.add(new CmdBack());
        cmds.add(new CmdBan());
        cmds.add(new CmdBanip());
        cmds.add(new CmdBanlist());
        cmds.add(new CmdBroadcast());
        cmds.add(new CmdBurn());
        cmds.add(new CmdButcher());
        cmds.add(new CmdClean());
        cmds.add(new CmdClear());
        cmds.add(new CmdClearchat());
        cmds.add(new CmdCompact());
        cmds.add(new CmdCompass());
        cmds.add(new CmdCoordinates());
        cmds.add(new CmdDamage());
        cmds.add(new CmdDeaf());
        cmds.add(new CmdDeaflist());
        cmds.add(new CmdDelhome());
        cmds.add(new CmdDeljail());
        cmds.add(new CmdDelspawn());
        cmds.add(new CmdDelwarp());
        cmds.add(new CmdEditsign());
        cmds.add(new CmdEffect());
        cmds.add(new CmdEnchant());
        //cmds.add(new CmdEnchantingtable());
        cmds.add(new CmdEnderchest());
        cmds.add(new CmdExperience());
        cmds.add(new CmdExtinguish());
        cmds.add(new CmdFeed());
        cmds.add(new CmdFireball());
        cmds.add(new CmdFirework());
        cmds.add(new CmdFly());
        cmds.add(new CmdFreeze());
        cmds.add(new CmdFreezelist());
        cmds.add(new CmdGamemode());
        cmds.add(new CmdGarbagecollector());
        cmds.add(new CmdGive());
        cmds.add(new CmdGod());
        cmds.add(new CmdHat());
        cmds.add(new CmdHeal());
        cmds.add(new CmdHelp());
        cmds.add(new CmdHome());
        cmds.add(new CmdHunger());
        cmds.add(new CmdInventory());
        cmds.add(new CmdIp());
        cmds.add(new CmdItem());
        cmds.add(new CmdJail());
        cmds.add(new CmdJaillist());
        cmds.add(new CmdJump());
        cmds.add(new CmdKick());
        cmds.add(new CmdKickall());
        cmds.add(new CmdKill());
        cmds.add(new CmdKillall());
        cmds.add(new CmdKit());
        cmds.add(new CmdKittycannon());
        cmds.add(new CmdLag());
        cmds.add(new CmdList());
        cmds.add(new CmdSetmaxhealth());
        cmds.add(new CmdMe());
        cmds.add(new CmdMegasmite());
        cmds.add(new CmdMinecraftservers());
        cmds.add(new CmdMobtp());
        cmds.add(new CmdModify());
        cmds.add(new CmdMoney());
        cmds.add(new CmdMore());
        cmds.add(new CmdMotd());
        cmds.add(new CmdMsg());
        cmds.add(new CmdMute());
        cmds.add(new CmdMutelist());
        cmds.add(new CmdNames());
        cmds.add(new CmdNear());
        cmds.add(new CmdNick());
        cmds.add(new CmdPay());
        cmds.add(new CmdPing());
        cmds.add(new CmdPlugin());
        cmds.add(new CmdPotion());
        cmds.add(new CmdPowertool());
        cmds.add(new CmdRealname());
        cmds.add(new CmdRecipe());
        cmds.add(new CmdRemoveall());
        cmds.add(new CmdRepair());
        cmds.add(new CmdReply());
        cmds.add(new CmdRules());
        cmds.add(new CmdSave());
        cmds.add(new CmdSay());
        cmds.add(new CmdSeen());
        cmds.add(new CmdSetarmor());
        cmds.add(new CmdSetexperience());
        cmds.add(new CmdSethealth());
        cmds.add(new CmdSethome());
        cmds.add(new CmdSethunger());
        cmds.add(new CmdSetjail());
        cmds.add(new CmdSetlevel());
        cmds.add(new CmdSetspawn());
        cmds.add(new CmdSetwarp());
        cmds.add(new CmdSkull());
        cmds.add(new CmdSilence());
        cmds.add(new CmdSmite());
        cmds.add(new CmdSpawn());
        cmds.add(new CmdSpawner());
        cmds.add(new CmdSpawnmob());
        cmds.add(new CmdSpeed());
        cmds.add(new CmdSpy());
        cmds.add(new CmdSudo());
        cmds.add(new CmdTeleport());
        cmds.add(new CmdTeleportaccept());
        cmds.add(new CmdTeleportall());
        cmds.add(new CmdTeleportask());
        cmds.add(new CmdTeleportaskall());
        cmds.add(new CmdTeleportdeny());
        cmds.add(new CmdTeleporthere());
        cmds.add(new CmdTeleporttoggle());
        cmds.add(new CmdTime());
        cmds.add(new CmdTop());
        cmds.add(new CmdUltimatecore());
        cmds.add(new CmdUnban());
        cmds.add(new CmdUndeaf());
        cmds.add(new CmdUnfreeze());
        cmds.add(new CmdUnjail());
        cmds.add(new CmdUnmute());
        cmds.add(new CmdUnsilence());
        cmds.add(new CmdUptime());
        cmds.add(new CmdUuid());
        cmds.add(new CmdVanish());
        cmds.add(new CmdVillager());
        cmds.add(new CmdWarp());
        cmds.add(new CmdWeather());
        cmds.add(new CmdWorkbench());
        cmds.add(new CmdWorld());
        //
        ucmds = new UltimateCommands();
        //
        for (UltimateCommandExecutor cmd : cmds) {
            if (disabled.contains(cmd.getName())) {
                continue;
            }
            for (String a : cmd.getAliases()) {
                if (disabled.contains(a)) {
                    continue;
                }
            }
            List<String> aliases = new ArrayList<>();
            aliases.add(cmd.getName());
            aliases.addAll(cmd.getAliases());

            for (String label : aliases) {
                r.getGame().getCommandDispatcher().register(r.getUC(), CommandSpec.builder().setDescription(Texts.of(cmd.getDescription())).setExecutor(new UltimateCommandExecutor.DefaultExecutor(cmd, label)).setArguments(new UltimateCommandElement(null, cmd, label)).build(), label);
            }

        }
    }

    public static String[] convertsArgs(CommandContext args) {
        return (String[]) args.getAll("string").toArray();
    }



}

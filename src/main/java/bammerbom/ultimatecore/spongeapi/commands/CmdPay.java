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
import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.api.UEconomy;
import bammerbom.ultimatecore.spongeapi.r;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CmdPay implements UltimateCommand {

    @Override
    public String getName() {
        return "pay";
    }

    @Override
    public String getPermission() {
        return "uc.pay";
    }

    @Override
    public String getUsage() {
        return "/<command> <Player> <Amount>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Give someone a certain amount of money");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.pay", true)) {
            return CommandResult.empty();
        }
        if (!Sponge.getServiceManager().provide(EconomyService.class).isPresent()) {
            r.sendMes(cs, "moneyNoEconomy");
            return CommandResult.empty();
        }
        EconomyService service = Sponge.getServiceManager().provide(EconomyService.class).get();
        if (!r.isPlayer(cs)) {
            return CommandResult.empty();
        }
        Player p = (Player) cs;
        //pay player amount
        if (!r.checkArgs(args, 1)) {
            r.sendMes(cs, "payUsage");
            return CommandResult.empty();
        }
        Player t = r.searchPlayer(args[0]).orElse(null);
        if (t == null) {
            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            return CommandResult.empty();
        }
        if (!r.isDouble(args[1])) {
            r.sendMes(cs, "numberFormat", "%Number", args[1]);
            return CommandResult.empty();
        }
        Double d = Double.parseDouble(args[1]);
        if (d < 0.1) {
            d = 0.1;
        }
        Double balance = Double.parseDouble(service.getOrCreateAccount(p.getUniqueId()).get().getBalance(service.getDefaultCurrency()).toString());
        if (service instanceof UEconomy) {
            UEconomy ue = (UEconomy) service;
            if (-d < 0) {
                r.sendMes(cs, "payTooLessMoney", "%Money", ue.getDefaultCurrency().format(BigDecimal.valueOf(d)));
                return CommandResult.empty();
            }
            if (ue.getMaximumMoney() != null && d + balance > Double.parseDouble(ue.getMaximumMoney().toString())) {
                r.sendMes(cs, "moneyMaxBalance");
                return CommandResult.empty();
            }
        } else {
            if (balance - d < 0) {
                r.sendMes(cs, "payTooLessMoney", "%Money", service.getDefaultCurrency().format(BigDecimal.valueOf(d)));
                return CommandResult.empty();
            }
        }
        TransactionResult res = service.getOrCreateAccount(p.getUniqueId()).get().withdraw(service.getDefaultCurrency(), BigDecimal.valueOf(d), Cause.builder().build());
        if (res.getResult().equals(ResultType.SUCCESS)) {
            service.getOrCreateAccount(t.getUniqueId()).get().deposit(service.getDefaultCurrency(), BigDecimal.valueOf(d), Cause.builder().build());
        }
        r.sendMes(cs, "payMessage", "%Player", UC.getPlayer(t).getDisplayName(), "%Amount", service.getDefaultCurrency().format(BigDecimal.valueOf(d)));
        r.sendMes(t, "payTarget", "%Player", UC.getPlayer(p).getDisplayName(), "%Amount", service.getDefaultCurrency().format(BigDecimal.valueOf(d)));
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}

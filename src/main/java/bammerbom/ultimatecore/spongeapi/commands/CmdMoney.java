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
import bammerbom.ultimatecore.spongeapi.api.UEconomy;
import bammerbom.ultimatecore.spongeapi.r;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.*;

public class CmdMoney implements UltimateCommand {

    @Override
    public String getName() {
        return "money";
    }

    @Override
    public String getPermission() {
        return "uc.money";
    }

    @Override
    public String getUsage() {
        return "/<command> /money [Player] or /<command> set/add/take [Player] <Amount>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Economy-related commands");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("balance");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.money", true)) {
            return CommandResult.empty();
        }
        if (!Sponge.getServiceManager().provide(EconomyService.class).isPresent()) {
            r.sendMes(cs, "moneyNoEconomy");
            return CommandResult.empty();
        }
        EconomyService service = Sponge.getServiceManager().provide(EconomyService.class).get();
        //money
        if (!r.checkArgs(args, 0)) {
            if (!r.isPlayer(cs)) {
                return CommandResult.empty();
            }
            Player p = (Player) cs;
            if (!r.perm(cs, "uc.money.status", true)) {
                return CommandResult.empty();
            }
            r.sendMes(cs, "moneyStatusSelf", "%Balance", service.getDefaultCurrency().format(service.getOrCreateAccount(p.getUniqueId()).get().getBalance(service.getDefaultCurrency())));
        } else if (args[0].equalsIgnoreCase("set")) {
            if (!r.perm(cs, "uc.money.set", true)) {
                return CommandResult.empty();
            }
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "moneyUsage");
            } else if (r.isDouble(args[1])) {
                if (!r.isPlayer(cs)) {
                    return CommandResult.empty();
                }
                Player p = (Player) cs;
                if (service instanceof UEconomy) {
                    UEconomy ue = (UEconomy) service;
                    if (ue.getMaximumMoney() != null && ue.getMaximumMoney().compareTo(BigDecimal.valueOf(Double.parseDouble(args[1]))) == -1) {
                        r.sendMes(cs, "moneyTooHigh");
                        return CommandResult.empty();
                    }
                    if (ue.getMinimumMoney() != null && ue.getMinimumMoney().compareTo(BigDecimal.valueOf(Double.parseDouble(args[1]))) == 1) {
                        r.sendMes(cs, "moneyTooLow");
                        return CommandResult.empty();
                    }
                }
                TransactionResult res = service.getOrCreateAccount(p.getUniqueId()).get().setBalance(service.getDefaultCurrency(), BigDecimal.valueOf(Double.parseDouble(args[1])), Cause
                        .builder().build());
                if (res.getResult().equals(ResultType.SUCCESS)) {
                    r.sendMes(cs, "moneySetSelf", "%Balance", service.getDefaultCurrency().format(BigDecimal.valueOf(Double.parseDouble(args[1]))));
                } else {
                    r.sendMes(cs, "moneyFailed", "%Error", res.getResult().toString());
                }
            } else if (!r.checkArgs(args, 2)) {
                r.sendMes(cs, "moneyUsage");
            } else if (!r.isDouble(args[1]) && r.isDouble(args[2])) {
                if (!r.perm(cs, "uc.money.set.others", true)) {
                    return CommandResult.empty();
                }
                GameProfile t = r.searchGameProfile(args[1]).orElse(null);
                if (service instanceof UEconomy) {
                    UEconomy ue = (UEconomy) service;
                    if (ue.getMaximumMoney() != null && ue.getMaximumMoney().compareTo(BigDecimal.valueOf(Double.parseDouble(args[1]))) == -1) {
                        r.sendMes(cs, "moneyTooHigh");
                        return CommandResult.empty();
                    }
                    if (ue.getMinimumMoney() != null && ue.getMinimumMoney().compareTo(BigDecimal.valueOf(Double.parseDouble(args[1]))) == 1) {
                        r.sendMes(cs, "moneyTooLow");
                        return CommandResult.empty();
                    }
                }
                TransactionResult res = service.getOrCreateAccount(t.getUniqueId()).get().setBalance(service.getDefaultCurrency(), BigDecimal.valueOf(Double.parseDouble(args[2])), Cause
                        .builder().build());
                if (res.getResult().equals(ResultType.SUCCESS)) {
                    r.sendMes(cs, "moneySetOthers", "%Balance", service.getDefaultCurrency().format(BigDecimal.valueOf(Double.parseDouble(args[2]))), "%Player", t.getName());
                } else {
                    r.sendMes(cs, "moneyFailed", "%Error", res.getResult().toString());
                }
            } else {
                r.sendMes(cs, "moneyUsage");
            }
        } else if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("give")) {
            if (!r.perm(cs, "uc.money.add", true)) {
                return CommandResult.empty();
            }
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "moneyUsage");

            } else if (r.isDouble(args[1])) {
                if (!r.isPlayer(cs)) {
                    return CommandResult.empty();
                }
                Player p = (Player) cs;
                if (service instanceof UEconomy) {
                    UEconomy ue = (UEconomy) service;
                    if (ue.getMaximumMoney() != null && BigDecimal.valueOf(Double.parseDouble(args[1])).add(service.getOrCreateAccount(p.getUniqueId()).get().getBalance(service
                            .getDefaultCurrency())).compareTo(ue.getMaximumMoney()) == 1) {
                        double add = ue.getMaximumMoney() - ue.getBalance((Player) cs);
                        EconomyResponse er = r.getVault().getEconomy().depositPlayer((Player) cs, add);
                        if (er.transactionSuccess()) {
                            r.sendMes(cs, "moneyAddSelf", "%Amount", r.getVault().getEconomy().format(add));
                            r.sendMes(cs, "moneyMaxBalance");
                        } else {
                            r.sendMes(cs, "moneyFailed", "%Error", er.errorMessage);
                        }
                        return CommandResult.empty();
                    }
                }
                EconomyResponse er = r.getVault().getEconomy().depositPlayer((Player) cs, Double.parseDouble(args[1]));
                if (er.transactionSuccess()) {
                    r.sendMes(cs, "moneyAddSelf", "%Amount", r.getVault().getEconomy().format(Double.parseDouble(args[1])));
                } else {
                    r.sendMes(cs, "moneyFailed", "%Error", er.errorMessage);
                }
            } else if (!r.checkArgs(args, 2)) {
                r.sendMes(cs, "moneyUsage");
            } else if (!r.isDouble(args[1]) && r.isDouble(args[2])) {
                if (!r.perm(cs, "uc.money.add.others", false, true)) {
                    return CommandResult.empty();
                }
                OfflinePlayer t = r.searchGameProfile(args[1]);
                if (!r.getVault().getEconomy().hasAccount(t)) {
                    r.getVault().getEconomy().createPlayerAccount(t);
                }
                if (r.getVault().getEconomy() instanceof UEconomy) {
                    UEconomy ue = (UEconomy) r.getVault().getEconomy();
                    if (ue.getMaximumMoney() != null && Double.parseDouble(args[2]) + ue.getBalance(t) > ue.getMaximumMoney()) {
                        if (ue.getMaximumMoney() != null && Double.parseDouble(args[1]) + ue.getBalance(t) > ue.getMaximumMoney()) {
                            double add = ue.getMaximumMoney() - ue.getBalance(t);
                            EconomyResponse er = r.getVault().getEconomy().depositPlayer(t, add);
                            if (er.transactionSuccess()) {
                                r.sendMes(cs, "moneyAddOthers", "%Amount", r.getVault().getEconomy().format(Double.parseDouble(args[2])), "%Player", t.getName());
                                r.sendMes(cs, "moneyMaxBalance");
                            } else {
                                r.sendMes(cs, "moneyFailed", "%Error", er.errorMessage);
                            }
                            return CommandResult.empty();
                        }
                        return CommandResult.empty();
                    }
                }
                EconomyResponse er = r.getVault().getEconomy().depositPlayer(t, Double.parseDouble(args[2]));
                if (er.transactionSuccess()) {
                    r.sendMes(cs, "moneyAddOthers", "%Amount", r.getVault().getEconomy().format(Double.parseDouble(args[2])), "%Player", t.getName());
                } else {
                    r.sendMes(cs, "moneyFailed", "%Error", er.errorMessage);
                }
            } else {
                r.sendMes(cs, "moneyUsage");
            }
        } else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("take")) {
            if (!r.perm(cs, "uc.money.remove", false, true)) {
                return CommandResult.empty();
            }
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "moneyUsage");
            } else if (r.isDouble(args[1])) {
                if (!r.isPlayer(cs)) {
                    return CommandResult.empty();
                }
                if (!r.getVault().getEconomy().hasAccount((Player) cs)) {
                    r.getVault().getEconomy().createPlayerAccount((Player) cs);
                }
                if (r.getVault().getEconomy() instanceof UEconomy) {
                    UEconomy ue = (UEconomy) r.getVault().getEconomy();
                    if (ue.getMaximumMoney() != null && ue.getBalance((Player) cs) - Double.parseDouble(args[1]) < ue.getMinimumMoney()) {
                        double remove = ue.getBalance((Player) cs) - ue.getMinimumMoney();
                        EconomyResponse er = ue.withdrawPlayer((Player) cs, remove, true);
                        if (er.transactionSuccess()) {
                            r.sendMes(cs, "moneyRemoveSelf", "%Amount", r.getVault().getEconomy().format(remove));
                            r.sendMes(cs, "moneyMinBalance");
                        } else {
                            r.sendMes(cs, "moneyFailed", "%Error", er.errorMessage);
                        }
                        return CommandResult.empty();
                    }
                }
                EconomyResponse er = r.getVault().getEconomy().withdrawPlayer((Player) cs, Double.parseDouble(args[1]));
                if (er.transactionSuccess()) {
                    r.sendMes(cs, "moneyRemoveSelf", "%Amount", r.getVault().getEconomy().format(Double.parseDouble(args[1])));
                } else {
                    r.sendMes(cs, "moneyFailed", "%Error", er.errorMessage);
                }
            } else if (!r.checkArgs(args, 2)) {
                r.sendMes(cs, "moneyUsage");
            } else if (!r.isDouble(args[1]) && r.isDouble(args[2])) {
                if (!r.perm(cs, "uc.money.remove.others", false, true)) {
                    return CommandResult.empty();
                }
                OfflinePlayer t = r.searchGameProfile(args[1]);
                if (!r.getVault().getEconomy().hasAccount(t)) {
                    r.getVault().getEconomy().createPlayerAccount(t);
                }
                if (r.getVault().getEconomy() instanceof UEconomy) {
                    UEconomy ue = (UEconomy) r.getVault().getEconomy();
                    if (ue.getMaximumMoney() != null && ue.getBalance(t) - Double.parseDouble(args[2]) < ue.getMinimumMoney()) {
                        double remove = ue.getBalance(t) - ue.getMinimumMoney();
                        EconomyResponse er = ue.withdrawPlayer(t, remove, true);
                        if (er.transactionSuccess()) {
                            r.sendMes(cs, "moneyRemoveOthers", "%Amount", r.getVault().getEconomy().format(Double.parseDouble(args[2])), "%Player", t.getName());
                            r.sendMes(cs, "moneyMinBalance");
                        } else {
                            r.sendMes(cs, "moneyFailed", "%Error", er.errorMessage);
                        }
                        return CommandResult.empty();
                    }
                }
                EconomyResponse er = r.getVault().getEconomy().withdrawPlayer(t, Double.parseDouble(args[2]));
                if (er.transactionSuccess()) {
                    r.sendMes(cs, "moneyRemoveOthers", "%Amount", r.getVault().getEconomy().format(Double.parseDouble(args[2])), "%Player", t.getName());
                } else {
                    r.sendMes(cs, "moneyFailed", "%Error", er.errorMessage);
                }
            } else {
                r.sendMes(cs, "moneyUsage");
            }
        } else if (args[0].equalsIgnoreCase("top")) {
            if (!r.perm(cs, "uc.money.top", true, true)) {
                return CommandResult.empty();
            }
            HashMap<String, Double> mapO = new HashMap<>();
            JsonConfig c = new JsonConfig(UltimateFileLoader.Deconomy);
            for (String s : c.listKeys(false)) {
                mapO.put(s, c.getDouble(s));
            }
            LinkedHashMap<String, Double> map = sortHashMapByValuesD(mapO);
            Integer cur = 0;
            while ((map.size() - 1) >= cur && cur < 10) {
                String player;
                try {
                    player = r.searchGameProfile(UUID.fromString(map.keySet().toArray(new String[map.size()])[cur])).getName();
                } catch (IllegalArgumentException | NullPointerException ex) {
                    player = map.keySet().toArray(new String[map.size()])[cur];
                }
                if (player == null) {
                    player = map.keySet().toArray(new String[map.size()])[cur];
                }
                r.sendMes(cs, "moneyTopEntry", "%Rank", cur + 1, "%Player", player, "%Balance", r.getVault().getEconomy().format(new ArrayList<>(map.values()).get(cur)));
                cur++;
            }

        } else {
            if (!r.perm(cs, "uc.money.status.others", true, true)) {
                return CommandResult.empty();
            }
            OfflinePlayer t = r.searchGameProfile(args[0]);
            if (!r.getVault().getEconomy().hasAccount(t)) {
                r.getVault().getEconomy().createPlayerAccount(t);
            }
            if (t == null || (!t.hasPlayedBefore() && !t.isOnline())) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return CommandResult.empty();
            }
            r.sendMes(cs, "moneyStatusOthers", "%Player", t.getName(), "%Balance", r.getVault().getEconomy().format(r.getVault().getEconomy().getBalance(t)));
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }

    public LinkedHashMap sortHashMapByValuesD(HashMap<String, Double> passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues, Collections.reverseOrder());
        Collections.sort(mapKeys, Collections.reverseOrder());

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)) {
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put(key, val);
                    break;
                }

            }

        }

        return sortedMap;
    }

}

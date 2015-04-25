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
package bammerbom.ultimatecore.bukkit.commands;

import bammerbom.ultimatecore.bukkit.UltimateFileLoader;
import bammerbom.ultimatecore.bukkit.jsonconfiguration.JsonConfig;
import bammerbom.ultimatecore.bukkit.r;
import java.util.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
    public List<String> getAliases() {
        return Arrays.asList("balance");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.money", true, true)) {
            return;
        }
        if (r.getVault() == null) {
            r.sendMes(cs, "moneyNoVault");
            return;
        }
        if (r.getVault().getEconomy() == null) {
            r.sendMes(cs, "moneyNoEconomy");
            return;
        }
        //money
        if (!r.checkArgs(args, 0)) {
            if (!r.isPlayer(cs)) {
                return;
            }
            if (!r.perm(cs, "uc.money.status", true, true)) {
                return;
            }
            if (!r.getVault().getEconomy().hasAccount((Player) cs)) {
                r.getVault().getEconomy().createPlayerAccount((Player) cs);
            }
            r.sendMes(cs, "moneyStatusSelf", "%Balance", r.getVault().getEconomy().format(r.getVault().getEconomy().getBalance((Player) cs)));
        } else if (args[0].equalsIgnoreCase("set")) {
            if (!r.perm(cs, "uc.money.set", false, true)) {
                return;
            }
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "moneyUsage");
            } else if (r.isDouble(args[1])) {
                if (!r.isPlayer(cs)) {
                    return;
                }
                if (!r.getVault().getEconomy().hasAccount((Player) cs)) {
                    r.getVault().getEconomy().createPlayerAccount((Player) cs);
                }
                r.getVault().getEconomy().withdrawPlayer((Player) cs, r.getVault().getEconomy().getBalance((Player) cs));
                r.getVault().getEconomy().depositPlayer((Player) cs, Double.parseDouble(args[1]));
                r.sendMes(cs, "moneySetSelf", "%Balance", r.getVault().getEconomy().format(Double.parseDouble(args[1])));
            } else if (!r.checkArgs(args, 2)) {
                r.sendMes(cs, "moneyUsage");
            } else if (!r.isDouble(args[1]) && r.isDouble(args[2])) {
                if (!r.perm(cs, "uc.money.set.others", false, true)) {
                    return;
                }
                OfflinePlayer t = r.searchOfflinePlayer(args[1]);
                if (!r.getVault().getEconomy().hasAccount(t)) {
                    r.getVault().getEconomy().createPlayerAccount(t);
                }
                r.getVault().getEconomy().withdrawPlayer(t, r.getVault().getEconomy().getBalance(t));
                r.getVault().getEconomy().depositPlayer(t, Double.parseDouble(args[2]));
                r.sendMes(cs, "moneySetOthers", "%Balance", r.getVault().getEconomy().format(Double.parseDouble(args[2])), "%Player", t.getName());
            } else {
                r.sendMes(cs, "moneyUsage");
            }
        } else if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("give")) {
            if (!r.perm(cs, "uc.money.add", false, true)) {
                return;
            }
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "moneyUsage");

            } else if (r.isDouble(args[1])) {
                if (!r.isPlayer(cs)) {
                    return;
                }
                if (!r.getVault().getEconomy().hasAccount((Player) cs)) {
                    r.getVault().getEconomy().createPlayerAccount((Player) cs);
                }
                r.getVault().getEconomy().depositPlayer((Player) cs, Double.parseDouble(args[1]));
                r.sendMes(cs, "moneyAddSelf", "%Amount", r.getVault().getEconomy().format(Double.parseDouble(args[1])));
            } else if (!r.checkArgs(args, 2)) {
                r.sendMes(cs, "moneyUsage");
            } else if (!r.isDouble(args[1]) && r.isDouble(args[2])) {
                if (!r.perm(cs, "uc.money.add.others", false, true)) {
                    return;
                }
                OfflinePlayer t = r.searchOfflinePlayer(args[1]);
                if (!r.getVault().getEconomy().hasAccount(t)) {
                    r.getVault().getEconomy().createPlayerAccount(t);
                }
                r.getVault().getEconomy().depositPlayer(t, Double.parseDouble(args[2]));
                r.sendMes(cs, "moneyAddOthers", "%Amount", r.getVault().getEconomy().format(Double.parseDouble(args[2])), "%Player", t.getName());
            } else {
                r.sendMes(cs, "moneyUsage");
            }
        } else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("take")) {
            if (!r.perm(cs, "uc.money.remove", false, true)) {
                return;
            }
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "moneyUsage");
            } else if (r.isDouble(args[1])) {
                if (!r.isPlayer(cs)) {
                    return;
                }
                if (!r.getVault().getEconomy().hasAccount((Player) cs)) {
                    r.getVault().getEconomy().createPlayerAccount((Player) cs);
                }
                r.getVault().getEconomy().withdrawPlayer((Player) cs, Double.parseDouble(args[1]));
                r.sendMes(cs, "moneyRemoveSelf", "%Amount", r.getVault().getEconomy().format(Double.parseDouble(args[1])));
            } else if (!r.checkArgs(args, 2)) {
                r.sendMes(cs, "moneyUsage");
            } else if (!r.isDouble(args[1]) && r.isDouble(args[2])) {
                if (!r.perm(cs, "uc.money.remove.others", false, true)) {
                    return;
                }
                OfflinePlayer t = r.searchOfflinePlayer(args[1]);
                if (!r.getVault().getEconomy().hasAccount(t)) {
                    r.getVault().getEconomy().createPlayerAccount(t);
                }
                r.getVault().getEconomy().withdrawPlayer(t, Double.parseDouble(args[2]));
                r.sendMes(cs, "moneyRemoveOthers", "%Amount", r.getVault().getEconomy().format(Double.parseDouble(args[2])), "%Player", t.getName());
            } else {
                r.sendMes(cs, "moneyUsage");
            }
        } else if (args[0].equalsIgnoreCase("top")) {
            if (!r.perm(cs, "uc.money.top", true, true)) {
                return;
            }
            HashMap<String, Double> mapO = new HashMap<>();
            JsonConfig c = new JsonConfig(UltimateFileLoader.Deconomy);
            for (String s : c.listKeys(false)) {
                mapO.put(s, c.getDouble(s));
            }
            LinkedHashMap<String, Double> map = sortHashMapByValuesD(mapO);
            Integer cur = 0;
            while ((map.size() - 1) >= cur && cur <= 10) {
                String player;
                try {
                    player = r.searchOfflinePlayer(UUID.fromString(map.keySet().toArray(new String[map.size()])[cur])).getName();
                } catch (IllegalArgumentException | NullPointerException ex) {
                    player = map.keySet().toArray(new String[map.size()])[cur];
                }
                r.sendMes(cs, "moneyTopEntry", "%Rank", cur + 1, "%Player", player, "%Balance", r.getVault().getEconomy().format(new ArrayList<>(map.values()).get(cur)));
                cur++;
            }

        } else {
            if (!r.perm(cs, "uc.money.status.others", true, true)) {
                return;
            }
            OfflinePlayer t = r.searchOfflinePlayer(args[0]);
            if (!r.getVault().getEconomy().hasAccount(t)) {
                r.getVault().getEconomy().createPlayerAccount(t);
            }
            if (t == null || (!t.hasPlayedBefore() && !t.isOnline())) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return;
            }
            r.sendMes(cs, "moneyStatusOthers", "%Player", t.getName(), "%Balance", r.getVault().getEconomy().format(r.getVault().getEconomy().getBalance(t)));
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
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
                    sortedMap.put((String) key, (Double) val);
                    break;
                }

            }

        }

        return sortedMap;
    }

}

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
import bammerbom.ultimatecore.bukkit.api.UC;
import bammerbom.ultimatecore.bukkit.api.UKit;
import bammerbom.ultimatecore.bukkit.configuration.Config;
import bammerbom.ultimatecore.bukkit.configuration.ConfigSection;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.utils.DateUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.ItemUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdKit implements UltimateCommand {

    @Override
    public String getName() {
        return "kit";
    }

    @Override
    public String getPermission() {
        return "uc.kit";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("kits", "kitlist");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.checkArgs(args, 0)) {
            if (!r.perm(cs, "uc.kit", true, true)) {
                return;
            }
            if (UC.getServer().getKits().isEmpty()) {
                r.sendMes(cs, "kitNoFound");
                return;
            }
            r.sendMes(cs, "kitList1");
            for (UKit kit : UC.getServer().getKits()) {
                r.sendMes(cs, "kitList2", "%Kit", kit.getName(), "%Description", kit.getDescription());
                if (kit.getCooldown() == 0) {
                    r.sendMes(cs, "kitList3", "%Cooldown", r.mes("kitNoCooldown"));
                } else if (kit.getCooldown() == -1) {
                    r.sendMes(cs, "kitList3", "%Cooldown", r.mes("kitOnlyOnce"));
                } else {
                    r.sendMes(cs, "kitList3", "%Cooldown", DateUtil.format(kit.getCooldown()));
                }
                List<String> items = new ArrayList<>();
                for (ItemStack item : kit.getItems()) {
                    items.add(ItemUtil.getName(item));
                }
                r.sendMes(cs, "kitList4", "%Items", StringUtil.joinList(items));
            }
            return;
        }
        if (!r.perm(cs, "uc.kit", true, false) && !r.perm(cs, "uc.kit." + args[0], true, false)) {
            r.sendMes(cs, "noPermissions");
            return;
        }
        if (!r.isPlayer(cs)) {
            return;
        }
        final Player p = (Player) cs;
        final Config config = new Config(UltimateFileLoader.DFkits);
        final ConfigSection kitNode = config.getConfigurationSection(args[0]);
        if (kitNode == null) {
            r.sendMes(cs, "kitNotFound", "%Kit", args[0]);
            return;
        }
        final UKit kit = UC.getServer().getKit(args[0]);
        if (!kit.hasCooldownPassedFor(p)) {
            if (kit.getCooldown() == -1L) {
                r.sendMes(cs, "kitOnlyOnce");
            } else {
                r.sendMes(cs, "kitTime", "%Time", DateUtil.formatDateDiff(kit.getCooldownFor(p)));
            }
            return;
        }
        final List<ItemStack> items = kit.getItems();
        final Map<Integer, ItemStack> leftOver = p.getInventory().addItem(items.toArray(new ItemStack[items.size()]));
        for (final ItemStack is : leftOver.values()) {
            p.getWorld().dropItemNaturally(p.getLocation(), is);
        }
        kit.setLastUsed(p, System.currentTimeMillis());
        r.sendMes(cs, "kitGive", "%Kit", args[0]);
        return;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return UC.getServer().getKitNames();
    }
}

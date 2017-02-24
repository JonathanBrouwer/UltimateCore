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
package bammerbom.ultimatecore.sponge.modules.playerinfo.commands;

import bammerbom.ultimatecore.sponge.api.command.HighPermCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandPermissions;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.PermissionInfo;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.modules.playerinfo.PlayerinfoModule;
import bammerbom.ultimatecore.sponge.modules.playerinfo.api.PlayerinfoPermissions;
import bammerbom.ultimatecore.sponge.utils.StringUtil;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.*;
import java.util.stream.Collectors;

@CommandInfo(module = PlayerinfoModule.class, aliases = {"list", "players", "ls", "who", "online", "plist"})
@CommandPermissions(level = PermissionLevel.EVERYONE)
public class ListCommand implements HighPermCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[0];
    }

    @Override
    public Map<String, PermissionInfo> registerPermissionSuffixes() {
        HashMap<String, PermissionInfo> map = new HashMap<>();
        map.put("seevanish", new PermissionInfo(Text.of("Allows you to see vanished players in the /list command"), PermissionLevel.MOD));
        return map;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        ModuleConfig config = Modules.PLAYERINFO.get().getConfig().get();
        CommentedConfigurationNode node = config.get();

        //Get players and remove vanished players
        List<Player> players = new ArrayList<>(Sponge.getServer().getOnlinePlayers());
        if (!hasPermSuffix(src, "seevanish")) players = players.stream().filter(p -> !p.get(Keys.VANISH).orElse(false) && !p.getIdentifier().equals(src.getIdentifier())).collect(Collectors.toList());

        //If empty, send empty message
        if (players.isEmpty()) {
            Messages.send(src, "playerinfo.command.list.nobody");
            return CommandResult.success();
        }

        //Get player's group
        HashMap<String, List<Player>> groups = new HashMap<>();
        for (Player p : players) {
            //Get group
            String group = PlayerinfoPermissions.UC_PLAYERINFO_LIST_GROUP.getFor(p);
            if (group == null) group = node.getNode("list", "default-name").getString();

            //Add to map
            List<Player> groupPlayers = groups.get(group);
            if (groupPlayers == null) groupPlayers = new ArrayList<>();
            groupPlayers.add(p);
            groups.put(group, groupPlayers);
        }

        //Sort the map
        groups = groups.entrySet().stream().sorted((a, b) -> {
            Integer pa = node.getNode("list", "priority", a.getKey()).getInt(0);
            Integer pb = node.getNode("list", "priority", b.getKey()).getInt(0);
            return pa.compareTo(pb);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        //Convert map to Text
        List<Text> texts = new ArrayList<>();
        groups.forEach((group, pls) -> {
            List<String> names = pls.stream().map(User::getName).collect(Collectors.toList());
            texts.add(Messages.getFormatted(src, "playerinfo.command.list.entry", "%group%", group, "%players%", StringUtil.join(", ", names)));
        });

        //Send messages with PaginatedList
        PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
        PaginationList paginationList = paginationService.builder().contents(texts).title(Messages.getFormatted("playerinfo.command.list.header").toBuilder().color(TextColors.DARK_GREEN).build()).build();
        paginationList.sendTo(src);
        return CommandResult.success();
    }
}

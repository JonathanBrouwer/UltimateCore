## Default
### Commands<br>
* /ultimatecore modules/resetuser/clearcache: The UltimateCore base command.<br>
## Warp
### Commands<br>
* /warp <Warp>: Teleport yourself to the specified warp.<br>
* /setwarp <Name> [Description]: Create a new warp with the specified name and description.<br>
* /delwarp <Warp>: Remove the specified warp.<br>
* /warplist: Get a paginated list of all warps.<br>
### Permissions<br>
* uc.warp.warp.base: Allows you to use the warp command. (Recommended for EVERYONE)<br>
* uc.warp.warp.[WARP]: Allows you to use a certain warp. (Recommended for EVERYONE)<br>
* uc.warp.setwarp: Allows you to set a new warp. (Recommended for ADMIN)<br>
* uc.warp.delwarp: Allows you to remove a warp. (Recommended for ADMIN)<br>
* uc.warp.warplist: Allows you to see a list of all warps. (Recommended for EVERYONE)<br>
* uc.sign.warp.use: Permission to use warpsigns. (Recommended for EVERYONE)<br>
* uc.sign.warp.create: Permission to create warpsigns. (Recommended for ADMIN)<br>
* uc.sign.warp.destroy: Permission to destroy warpsigns. (Recommended for ADMIN)<br>
## Kit
### Commands<br>
* /kit <Kit>: Use a certain kit.<br>
* /kitlist: Get a paginated list of all kits.<br>
### Permissions<br>
* uc.kit.kit.base: Allows you to use the kit command for all kits. (Recommended for EVERYONE)<br>
* uc.kit.kit.[KIT]: Allows you to use the kit command for a specific kit. (Recommended for EVERYONE)<br>
* uc.kitlist: Allows you to see a list of all kits. (Recommended for EVERYONE)<br>
## Back
### Commands<br>
* /back: Teleport back to the location you were before you teleported.<br>
### Permissions<br>
* uc.back.back.base: Allows you to use the back command. (Recommended for EVERYONE)<br>
* uc.back.back.ondeath: Allows you to use the back command after you died. (Recommended for EVERYONE)<br>
## Weather
### Commands<br>
* /weather <Sun/Rain/Thunder> [World]: Change the weather to sun, rain or thunder.<br>
* /sun [World]: Change the weather to sun.<br>
* /rain [World]: Change the weather to rain.<br>
* /thunder [World]: Change the weather to thunder.<br>
### Permissions<br>
* uc.weather.weather.base: Allows you to use the base weather command. (Recommended for ADMIN)<br>
* uc.weather.weather.sun: Allows you to change the weather to sun. (Recommended for ADMIN)<br>
* uc.weather.weather.rain: Allows you to change the weather to rain. (Recommended for ADMIN)<br>
* uc.weather.weather.thunder: Allows you to change the weather to thunder. (Recommended for ADMIN)<br>
## Sudo
### Commands<br>
* /sudo <Player> [/]<Command/Message>: Force a player to chat or to use a command.<br>
### Permissions<br>
* uc.sudo.sudo.base: Allows you to use the sudo command for both chatting and commands. (Recommended for ADMIN)<br>
* uc.sudo.sudo.command: Allows you to use the sudo command to force a command. (Recommended for ADMIN)<br>
* uc.sudo.sudo.chat: Allows you to use the sudo command to force chatting. (Recommended for ADMIN)<br>
## Deathmessage
This module has no commands or permissions.<br>
## Personalmessage
### Commands<br>
* /personalmessage <Player> <Message>: Send a private message to an other player.<br>
* /reply <Message>: Send a private message to the person who last private messaged you.<br>
### Permissions<br>
* uc.personalmessage.personalmessage: Allows you to send private messages to people. (Recommended for EVERYONE)<br>
* uc.personalmessage.reply: Allows you to reply to people's personal message using /reply. (Recommended for EVERYONE)<br>
## Sign
This module has no commands or permissions.<br>
## Automessage
This module has no commands or permissions.<br>
## Teleport
### Commands<br>
* /teleportask <Player>: Ask to teleport to a player.<br>
* /teleportaskhere <Player>: Ask to teleport a player to you.<br>
* /tpaccept [TPID/Player]: Accept the specified request, or the most recent one.<br>
* /tpdeny [TPID/Player]: Deny the specified request, or the most recent one.<br>
### Permissions<br>
* uc.teleport.teleport.base: Allows you to use the teleport command to teleport yourself to someone else. (Recommended for ADMIN)<br>
* uc.teleport.teleport.others: Allows you to use the teleport command to teleport someone else to someone else. (Recommended for ADMIN)<br>
* uc.teleport.teleport.coordinates: Allows you to use the teleport command to teleport yourself to certain coordinates. (Recommended for ADMIN)<br>
* uc.teleport.teleport.coordinates.others: Allows you to use the teleport command to teleport someone else to certain coordinates. (Recommended for ADMIN)<br>
* uc.teleport.teleportask: Allows you to use the teleportask command (Recommended for EVERYONE)<br>
* uc.teleport.teleportaskhere: Allows you to use the teleportaskhere command (Recommended for EVERYONE)<br>
* uc.teleport.teleportaccept: Allows you to use the teleportaccept command (Recommended for EVERYONE)<br>
* uc.teleport.teleportdeny: Allows you to use the teleportdeny command (Recommended for EVERYONE)<br>
## Afk
### Commands<br>
* /afk [Player] [Message]: Toggle whether a player is marked as away-from-keyboard.
/afk [Player] [Message]<br>
### Permissions<br>
* uc.afk.afk.base: Allows you to toggle your own afk status. (Recommended for EVERYONE)<br>
* uc.afk.afk.base.message: Allows you to provide a reason for being afk. (Recommended for EVERYONE)<br>
* uc.afk.afk.others: Allows you to toggle another player's afk status. (Recommended for MOD)<br>
* uc.afk.afk.others.message: Allows you to provide an message while toggling another player's afk status. (Recommended for MOD)<br>
* uc.afk.exempt: When you have this permission you can't be kicked for being afk. (Recommended for VIP)<br>
## Time
### Commands<br>
* /time (set/add) day/night/<ticks>/enable/disable/query: /time (set/add) day/night/<ticks> - Change the time.
/time enable/disable - Enable or disable time.
/time query - View the time.<br>
* /day: Set the time in a certain world to day.<br>
* /night: Set the time in a certain world to night.<br>
### Permissions<br>
* uc.time.time.base: Allows you to use the time command. (Recommended for ADMIN)<br>
* uc.time.time.day: Allows you to set the time to day. (Recommended for ADMIN)<br>
* uc.time.time.night: Allows you to set the time to night. (Recommended for ADMIN)<br>
* uc.time.time.ticks: Allows you to set the time to a certain amount of ticks. (Recommended for ADMIN)<br>
* uc.time.time.add: Allows you to add a certain amount of ticks to the time (Recommended for ADMIN)<br>
* uc.time.time.query: Allows you to use the base query command. (Recommended for ADMIN)<br>
* uc.time.time.query.days: Allows you to query the amount of days that have passes in the server. (Recommended for ADMIN)<br>
* uc.time.time.query.daytime: Allows you to query how many ticks have passed since the last day change. (Recommended for ADMIN)<br>
* uc.time.time.query.gametime: Allows you to use query how many ticks have passed in the server. (Recommended for ADMIN)<br>
* uc.time.time.query.formatted: Allows you to see the formatted time. (Recommended for EVERYONE)<br>
* uc.time.time.enable: Allows you to enable the daylight cycle. (Recommended for ADMIN)<br>
* uc.time.time.disable: Allows you to disable the daylight cycle. (Recommended for ADMIN)<br>
## Spy
### Commands<br>
* /commandspy [Player]: Toggle a player's or your own commandspy status. When commandspy is enabled you can see when other people use commands.<br>
* /commandspy [Player]: Toggle a player's or your own commandspy status. When commandspy is enabled you can see when other people whisper.<br>
### Permissions<br>
* uc.spy.commandspy.base: Allows you to toggle whether your commandspy is enabled. (Recommended for MOD)<br>
* uc.spy.messagespy.base: Allows you to toggle whether your messagespy is enabled. (Recommended for MOD)<br>
* uc.spy.commandspy.others: Allows you to toggle whether someones commandspy is enabled. (Recommended for MOD)<br>
* uc.spy.messagespy.others: Allows you to toggle whether someones messagespy is enabled. (Recommended for MOD)<br>
* uc.spy.commandspy.see: Allows you to see commandspy messages if your commandspy is toggled on. (Recommended for EVERYONE)<br>
* uc.spy.messagespy.see: Allows you to see messagespy messages if your messagespy is toggled on. (Recommended for EVERYONE)<br>
## Heal
### Commands<br>
* /heal [Player]: Heal a player.<br>
* /sethealth <Amount> [Player]: Set the health of a player.<br>
* /setmaxhealth <Amount> [Player]: Set the max health of a player.<br>
### Permissions<br>
* uc.heal.heal.base: Allows you to use the heal command. (Recommended for ADMIN)<br>
* uc.heal.heal.others: Allows you to use the heal command on other players. (Recommended for ADMIN)<br>
* uc.heal.sethealth.base: Allows you to use the sethealth command. (Recommended for ADMIN)<br>
* uc.heal.sethealth.others: Allows you to use the sethealth command on other players. (Recommended for ADMIN)<br>
* uc.heal.setmaxhealth.base: Allows you to use the setmaxhealth command on other players. (Recommended for ADMIN)<br>
* uc.heal.setmaxhealth.others: Allows you to use the setmaxhealth command on other players. (Recommended for ADMIN)<br>
## Serverlist
This module has no commands or permissions.<br>
## Fly
### Commands<br>
* /fly [Player]: Toggle someone's fly status.<br>
### Permissions<br>
* uc.fly.fly.base: Allows you to change your own fly state. (Recommended for ADMIN)<br>
* uc.fly.fly.others: Allows you to change other player's fly state. (Recommended for ADMIN)<br>
## Item
### Commands<br>
* /more: Set the quantity of the item in your hand to the maximum.<br>
* /repair [All/Hand]: Repair the item in your hand, or all items in your inventory.<br>
### Permissions<br>
* uc.item.more: Allows you to use the more command. (Recommended for ADMIN)<br>
* uc.item.repair.base: Allows you to use the repair command for both all and one item. (Recommended for ADMIN)<br>
* uc.item.repair.all: Allows you to use the repair command for all items. (Recommended for ADMIN)<br>
* uc.item.repair.one: Allows you to use the repair command for one item. (Recommended for ADMIN)<br>
## Random
### Commands<br>
* /random [Min] <Max>: Generate a random number between two values. If the minimum is not given it is set to 1.<br>
### Permissions<br>
* uc.random.random: Allows you to use the random command. (Recommended for EVERYONE)<br>
## Unknowncommand
This module has no commands or permissions.<br>
## Home
### Commands<br>
* /homelist: View a list of your homes.<br>
* /home <Name>: Teleport to your home.<br>
* /delhome <Name>: Remove a home.<br>
* /sethome <Name>: Set a new home or move the location of a home.<br>
### Permissions<br>
* uc.home.home: Allows you to teleport to your own home. (Recommended for EVERYONE)<br>
* uc.home.sethome: Allows you to set your own home. (Recommended for EVERYONE)<br>
* uc.home.sethome.unlimited: Allows you to set an unlimited amount of homes. (Recommended for VIP)<br>
* uc.home.delhome: Allows you to remove one of your homes. (Recommended for EVERYONE)<br>
### Permissions options<br>
* uc.home.homecount: The amount of homes the player can have.<br>
## Blood
This module has no commands or permissions.<br>
## Chat
This module has no commands or permissions.<br>
## Burn
### Commands<br>
* /burn <Player> <Time>: Set people on fire.<br>
### Permissions<br>
* uc.burn.burn: Allows you to use the burn command. (Recommended for ADMIN)<br>
## Poke
This module has no commands or permissions.<br>
## Food
### Commands<br>
* /food [Player]: Fill a player's food bar.<br>
### Permissions<br>
* uc.food.food.base: Allows you to use the food command. (Recommended for ADMIN)<br>
* uc.food.food.others: Allows you to use the food command on other players. (Recommended for ADMIN)<br>
## Mute
### Commands<br>
* /mute <Player> [Time] [Reason]: Mute a player, for a certain time and reason.<br>
* /mute <Player> [Time] [Reason]: Mute a player, for a certain time and reason.<br>
### Permissions<br>
* uc.mute.mute: Allows you to mute someone. (Recommended for MOD)<br>
* uc.mute.unmute: Allows you to unmute someone. (Recommended for MOD)<br>
## Kick
### Commands<br>
* /kick <Player> [Reason]: Kick a player from the server.<br>
* /kickall [Reason]: Kick all players from the server.<br>
### Permissions<br>
* uc.kick.kick: Allows you to use the kick command. (Recommended for MOD)<br>
* uc.kick.kickall: Allows you to use the kickall command. (Recommended for ADMIN)<br>
## Deaf
### Commands<br>
* /deaf <Player> [Time] [Reason]: Make a player deaf, for a certain time and reason.<br>
* /deaf <Player> [Time] [Reason]: Make a player deaf, for a certain time and reason.<br>
### Permissions<br>
* uc.deaf.deaf: Allows you to deaf someone. (Recommended for MOD)<br>
* uc.deaf.undeaf: Allows you to undeaf someone. (Recommended for MOD)<br>
## Tablist
This module has no commands or permissions.<br>
## Spawn
### Commands<br>
* /delfirstspawn: Delete the new player spawn.<br>
* /delglobalspawn: Delete the global spawn.<br>
* /delgroupspawn <Group>: Delete a group spawn.<br>
* /setfirstspawn: Set the new player spawn.<br>
* /setglobalspawn: Set the global spawn.<br>
* /setgroupspawn <Group>: Set a group spawn.<br>
* /globalspawn [Player]: Teleport someone to the global spawn.<br>
* /firstspawn [Player]: Teleport someone to the new player spawn.<br>
* /groupspawn [Player] [Group]: Teleport someone to a group spawn.<br>
* /spawn [Player]: Teleport someone to the spawn.<br>
### Permissions<br>
* uc.spawn.spawn.base: Allows you to use the spawn command. (Recommended for EVERYONE)<br>
* uc.spawn.globalspawn.base: Allows you to use the globalspawn command. (Recommended for ADMIN)<br>
* uc.spawn.groupspawn.base: Allows you to use the groupspawn command. (Recommended for ADMIN)<br>
* uc.spawn.groupspawn.group.<Group>: Allows you to use the groupspawn command for a certain group. (Recommended for ADMIN)<br>
* uc.spawn.firstspawn.base: Allows you to use the firstspawn command. (Recommended for ADMIN)<br>
* uc.spawn.spawn.others: Allows you to use the spawn command for other players. (Recommended for ADMIN)<br>
* uc.spawn.globalspawn.others: Allows you to use the globalspawn command for other players. (Recommended for ADMIN)<br>
* uc.spawn.groupspawn.others.base: Allows you to use the groupspawn command for other players. (Recommended for ADMIN)<br>
* uc.spawn.groupspawn.others.group.<Group>: Allows you to use the groupspawn command for a certain group for other players. (Recommended for ADMIN)<br>
* uc.spawn.firstspawn.others: Allows you to use the firstspawn command for other players. (Recommended for ADMIN)<br>
* uc.spawn.setglobalspawn.base: Allows you to set the globalspawn command. (Recommended for ADMIN)<br>
* uc.spawn.setgroupspawn.base: Allows you to set the groupspawn command. (Recommended for ADMIN)<br>
* uc.spawn.setfirstspawn.base: Allows you to set the firstspawn command. (Recommended for ADMIN)<br>
* uc.spawn.delglobalspawn.base: Allows you to delete the globalspawn command. (Recommended for ADMIN)<br>
* uc.spawn.delgroupspawn.base: Allows you to delete the groupspawn command. (Recommended for ADMIN)<br>
* uc.spawn.delfirstspawn.base: Allows you to delete the firstspawn command. (Recommended for ADMIN)<br>
### Permissions options<br>
* uc.spawn.groupspawn: The name of the group spawn the player should spawn at.<br>
## Connectionmessages
This module has no commands or permissions.<br>
## God
### Commands<br>
* /god [Player]: Toggle your or someone else's god mode.<br>
### Permissions<br>
* uc.god.god.base: Allows you to toggle your own god mode. (Recommended for ADMIN)<br>
* uc.god.god.others: Allows you to toggle other people's god mode. (Recommended for ADMIN)<br>
## Gamemode
### Commands<br>
* /gamemode <Survival/Creative/Adventure/Spectator> [Player]: Change a player's gamemode.<br>
* /survival [Player]: Change someone's gamemode to survival.<br>
* /creative [Player]: Change someone's gamemode to creative.<br>
* /adventure [Player]: Change someone's gamemode to adventure.<br>
* /spectator [Player]: Change someone's gamemode to spectator.<br>
### Permissions<br>
* uc.gamemode.gamemode.base: Allows you to change your own gamemode (Recommended for ADMIN)<br>
* uc.gamemode.gamemode.survival: Allows you to change your own gamemode to survival (Recommended for ADMIN)<br>
* uc.gamemode.gamemode.creative: Allows you to change your own gamemode to creative (Recommended for ADMIN)<br>
* uc.gamemode.gamemode.adventure: Allows you to change your own gamemode to adventure (Recommended for ADMIN)<br>
* uc.gamemode.gamemode.spectator: Allows you to change your own gamemode to spectator (Recommended for ADMIN)<br>
* uc.gamemode.gamemode.others.base: Allows you to change a player's gamemode (Recommended for ADMIN)<br>
* uc.gamemode.gamemode.others.survival: Allows you to change a player's gamemode to survival (Recommended for ADMIN)<br>
* uc.gamemode.gamemode.others.creative: Allows you to change a player's gamemode to creative (Recommended for ADMIN)<br>
* uc.gamemode.gamemode.others.adventure: Allows you to change a player's gamemode to adventure (Recommended for ADMIN)<br>
* uc.gamemode.gamemode.others.spectator: Allows you to change a player's gamemode to spectator (Recommended for ADMIN)<br>

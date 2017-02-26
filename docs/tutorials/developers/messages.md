Messages
====
This tutorial will cover the UltimateCore language api.
All relevant classes are located in `bammerbom/ultimatecore/sponge/api/language`.

## Getting messages
This uses the utility method:<br>
`Messages.getFormatted(CommandSource src, String key, Object... arguments)`<br>
The CommandSource is the player it will be sent to. This can be null, if it is not sent to a player.<br>
The arguments can be used to replace variables like %player%. Replacing such a variable will look like this:<br>
`Messages.getFormatted(src, "heal.command.heal.success.self", "%player%", src.getName())`<br>
In this example, `%player%` will be replaced with `src.getName()`.
Multiple arguments can be put after eachother, for example:<br>
`Messages.getFormatted(src, "world.command.world.gamerule.viewsingle", "%gamerule%", gamerule, "%value%", value.get());`<br>
This method returns a Text.

## Other methods
There are also a few other useful methods in the Messages class:

`Messages.send(CommandSource src, String key, Object... arguments)`<br>
This method automatically send the message to the provided CommandSource.<br>

`throw Messages.error(CommandSource src, String key, Object... arguments)`<br>
This method throws an ErrorMessageException, which extends CommandException. This is useful for returning an error message to a player in one line.<br>

`Messages.get(CommandSource src, String key, Object... arguments)`<br>
This method returns the raw entry from the language file. If you ever need the raw value, use this method.<br>

`Messages.log(Text)`<br>
This method logs the provided Text to the console, in color.<br>

## Creating new messages
This is incredibly easy.<br>
Go to the english language file:<br>
`src/main/resources/assets/bammerbom/ultimatecore/language/EN_US.properties`<br>

Choose a key for your message. <br>
The general format for commands is `<module>.command.<command>.<suffixes>`.<br>
The general format for other module messages is `<module>.<suffixes>`.<br>
The general format for a non-module specific message is `core.<suffixes>`.<br>
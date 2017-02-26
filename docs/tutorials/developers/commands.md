Commands
====
In this tutorial I will cover how to create commands.
Make sure you understand the [Sponge command api](https://docs.spongepowered.org/stable/en/plugin/commands/index.html) first.
Just like with modules, I will show you in steps how to create commands, so let's go.

## Planning
Make sure you have an idea about what your command will do, the usage, the aliases, and the permissions.

## Creating a command
* First of all, create a new package called `commands`. All your commands will be located in here.
* Create a new class called `TestCommand` (Replace Test with your command identifier. Make sure the command identifier is unique)
* Make the class implement `HighPermCommand`. You might see other commands implement `HighCommand` or even `Command`, but these are outdated and still have to be updated by me. Also make sure to click `implement methods` by interacting with the class name. This should generate the `getArguments()`  and `execute()` methods.
* You have to add the `@CommandInfo` and `@CommandPermissions` annotations to your command.<br>
* The `@CommandInfo` annotation has two parameters, the module and the aliases. An example is:<br> 
`@CommandInfo(module = PlayerinfoModule.class, aliases = {"uuid", "uuidtoname", "uuidtoplayer"})`<br>
**Make sure that the first alias of your command is equal to your command identifier!**
* The `@CommandPermissions` annotation contains one required parameter, the `level`. This should be equal to a value of the enum `PermissionLevel`. There is also an optional field called `supportsOthers`. If you set this to true an `uc.<module>.<command>.others` permission is automatically generated. The base permission `uc.<module>.<command>.base` is always automatically generated.
* The arguments are made using the `Arguments` class. First, choose the type of argument you want. All types are available in the `bammerbom/ultimatecore/sponge/api/command/argument/arguments` package. I prefer using my own arguments instead of sponge's `GenericArguments` class because it allows me to customize the error message. A tutorial for creating new argument types is below. 
The next step is to wrap the argument in the `Arguments` class.<br>
`Arguments.builder(new StringArgument(Text.of("name")))`<br>
This will give you an argument builder. You can call methods like `onlyOne()`, `optional()`, `optionalWeak()`, `repeat(int)`, `permission()` and `usage(Text)`. Make sure to build your argument using the `build()` method at the end.
An example of a list of arguments is: (For the /mute command)<br>
```
@Override
public CommandElement[] getArguments() {
    return new CommandElement[]{
            Arguments.builder(new PlayerArgument(Text.of("player"))).onlyOne().build(),
            Arguments.builder(new TimeArgument(Text.of("time"))).optionalWeak().onlyOne().build(),
            Arguments.builder(new RemainingStringsArgument(Text.of("reason"))).optional().onlyOne().build()
    };
}
```
* Next, you have to write your execute() method. This is pretty much the same as with normal sponge commands. The command automatically checks it's base permission `uc.<module>.<command>.base`. Any other permissions, including the others permission, must be checked using the `checkPermSuffix(CommandSource, String)` method. The CommandSource is the commandsource to check the permission on, the string is the suffix of the permission. For example, if you enter `"all"` the permission checked is `uc.<module>.<command>.all`. The method returns a void, but will throw a `CommandPermissionException`. You don't have to handle this, just call the method.<br>
Note: A tutorial for sending messages is available [here](messages.md).
* Once you finished writing your command, you have to register it. Go to your module class and use this code to register the command: (Replace TestCommand with your command class)<br>
`UltimateCore.get().getCommandService().register(new TestCommand());`
* Finally, you have to give the command a description. Add the `<module>.command.<command>.description` key to the EN_US language file. More information about creating messages is available [here](messages.md).
* An example of a fully completed command class: (Excluding the license & imports)<br>
```
@CommandInfo(module = PlayerinfoModule.class, aliases = {"uuid", "uuidtoname", "uuidtoplayer"})
@CommandPermissions(level = PermissionLevel.MOD)
public class UuidCommand implements HighPermCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new GameprofileArgument(Text.of("player"))).onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        GameProfile t = args.<GameProfile>getOne("player").get();
        Messages.send(src, "playerinfo.command.uuid.success", "%player%", t.getName().orElse("???"), "%uuid%", t.getUniqueId().toString());
        return CommandResult.success();
    }
}

```

## Creating new argument types
These are the steps for creating a new argument type
* First of all, determine whether this argument is only useful for this module (for example a HomeArgument), or is useful for other modules too.<br>
If the argument is useful for other modules, choose the package `bammerbom/ultimatecore/sponge/api/command/agument/arguments`.<br>
If the argument is not useful for other modules, choose the package `bammerbom/ultimatecore/sponge/modules/<module>/commands/arguments`
* In the chosen location, create a new Java class which extends UCommandElement. Interact with the class name and select `Implement methods`. Next, create a constructor which matches the UCommandElement constructor.
* The rest of the steps should be simular to [the sponge tutorial](https://docs.spongepowered.org/stable/en/plugin/commands/arguments.html#custom-command-elements).
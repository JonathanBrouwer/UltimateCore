Modules
----
If you are not familiar with modules, all features (commands & non-commands) in UltimateCore are seperated in modules. The is an afk module, an home module, etc.<br>
All modules are located in the `src/main/java/bammerbom/ultimatecore/sponge/modules/` package.
In this tutorial I will create a new module.

## Getting ideas
Before actually developing the module, you first need to have in mind what you want to make. I (Bammerbom) have a huge todo list, so if you can't think of anything on your own, you can always ask me.<br>
Next, think which commands you want to make, which listeners you need, etc. Once you got this planned out you can start developing.

## Creating the module class
* First of all, you have to create a new package for the module in the `bammerbom/ultimatecore/sponge/modules/` package.
This package will be the unique id of your module, and should be fully lowercase and should be alphanumeric. I will call my module `test`
* Next create the module class. This class should be in your newly created package, and it should be called `TestModule.java`, keep in mind that the first characters of your identifier and Module **must** be uppercase.
* Next make the class implement the `HighModule` class. You might see some other modules just implement the `Module` class, but these modules still have to be updated by me to the new system.
* Give the module the annotation `@ModuleInfo`, and fill in the `name` and the `description` fields. The name should be equal to your module id.
* Next, override the `onInit` method of the `Module` class, like this:<br>
Note: There are also simular `onRegister`, `onPostInit`, `onReload` and `onStop` method.
```
@Override
public void onInit(GameInitializationEvent event) {
    //Code here
}
```
* Done! You just created your first module. 
UltimateCore will automatically register your module, you don't need to register it.
Now, let's add some functionality to your module.

## The module package
If you look inside any module, you will find some of the following packages:
* `api` - All api classes will be located here. This includes data classes, permission registry classes, specific api classes, etc.
* `commands` - All your commands classes are located here. I will talk more about commands in an other tutorial.
* `config` - Any config related classes are located here. I will also talk more about configs in an other tutorial.
* `listeners` - You can create listeners here. Listeners are exactly the same as in sponge by default. You can register listeners in the `onInit()` method in your module class, using the default sponge registry:<br>
`Sponge.getEventManager().registerListeners(UltimateCore.get(), new AfkSwitchListener());`
* `utils` - All utility classes which do not fit in the api can be placed here.
* `tasks` - Any class send to the scheduler should be located here. 
* `handlers` - Classes which should really be located in the `utils` package, but I have been to lazy to move them so far. (And also because I don't want to break stuff)
* `signs` - Any registered signs (like warp signs) should be located here. This is still Work in Progress and signs don't work yet.
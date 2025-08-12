package fr.nkri.japi.cmds;

import fr.nkri.japi.cmds.bukkit.BukkitCommand;
import fr.nkri.japi.cmds.bukkit.BukkitCompleter;
import fr.nkri.japi.cmds.interfaces.Command;
import fr.nkri.japi.cmds.interfaces.Completer;
import fr.nkri.japi.logs.Logs;
import fr.nkri.japi.logs.enums.LogsType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandFramework implements CommandExecutor {
    private final Map<String, Map.Entry<Method, Object>> commandMap = new HashMap<>();
    private final CommandMap map;
    private final Plugin plugin;

    public CommandFramework(Plugin plugin) {
        this.plugin = plugin;
        if(plugin.getServer().getPluginManager() instanceof SimplePluginManager) {
            final SimplePluginManager manager = (SimplePluginManager) plugin.getServer().getPluginManager();

            try {
                final Field field = SimplePluginManager.class.getDeclaredField("commandMap");

                field.setAccessible(true);
                map = (CommandMap) field.get(manager);
            }
            catch (IllegalAccessException | NoSuchFieldException e) {
                Logs.sendLog("[WizAPI]", "Unable to access commandMap.", LogsType.ERROR);
                throw new RuntimeException("Unable to access commandMap", e);
            }
        }
        else {
            Logs.sendLog("[WizAPI]", "Plugin manager not supported.", LogsType.ERROR);
            throw new RuntimeException("Plugin manager not supported.");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        return handleCommand(sender, cmd, label, args);
    }

    public boolean handleCommand(final CommandSender sender, final org.bukkit.command.Command cmd, final String label, final String[] args) {
        for(int i = args.length; i >= 0; i--) {
            final StringBuilder buffer = new StringBuilder(label.toLowerCase());

            for(int x = 0; x < i; x++) {
                buffer.append(".").append(args[x].toLowerCase());
            }

            final String cmdLabel = buffer.toString();
            if(commandMap.containsKey(cmdLabel)) {
                final Method method = commandMap.get(cmdLabel).getKey();
                final Object methodObject = commandMap.get(cmdLabel).getValue();
                final Command command = method.getAnnotation(Command.class);

                boolean hasPerm = true;

                if((!command.isConsole()) && (!(sender instanceof Player))) {
                    sender.sendMessage("Cette commande est uniquement disponible pour les joueurs.");
                    return true;
                }

                if(!command.permissionNode().isEmpty() && ((command.permissionNode().equalsIgnoreCase("op") && !sender.isOp()) || (!sender.hasPermission(command.permissionNode())))) {
                    hasPerm = false;
                }

                if(!hasPerm) {
                    sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'exécuter cette commande.");
                    return true;
                }

                final CommandArguments commandArgs = new CommandArguments(sender, cmd, label, args, cmdLabel.split("\\.").length - 1);
                try {
                    method.invoke(methodObject, commandArgs);
                }
                catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        defaultCommand(new CommandArguments(sender, cmd, label, args, 0));
        return true;
    }

    public void registerCommands(final Object obj) {
        for(Method m : obj.getClass().getMethods()) {
            if(m.getAnnotation(Command.class) != null) {
                final Command command = m.getAnnotation(Command.class);
                if(m.getParameterCount() == 1 && m.getParameterTypes()[0] == CommandArguments.class) {
                    for(String alias : command.name()) {
                        registerCommand(command, alias, m, obj);
                    }
                }
                else {
                    System.out.println("Impossible to register " + m.getName() + ".");
                }
            }
            else if(m.getAnnotation(Completer.class) != null) {
                final Completer comp = m.getAnnotation(Completer.class);

                if(m.getParameterCount() == 1 && m.getParameterTypes()[0] == CommandArguments.class && m.getReturnType() == List.class) {
                    for(String alias : comp.name()) {
                        registerCompleter(alias, m, obj);
                    }
                }
                else {
                    System.out.println("Impossible to register completer " + m.getName() + ".");
                }
            }
        }
    }

    public void registerCommand(final Command command, final String label, final Method m, final Object obj) {
        final String cmdLabel = label.toLowerCase();
        commandMap.put(cmdLabel, new AbstractMap.SimpleEntry<>(m, obj));
        commandMap.put(plugin.getName() + ':' + cmdLabel, new AbstractMap.SimpleEntry<>(m, obj));

        if(map.getCommand(cmdLabel) == null) {
            org.bukkit.command.Command cmd = new BukkitCommand(cmdLabel, this, plugin);
            map.register(plugin.getName(), cmd);
        }
    }

    public void registerCompleter(final String label, final Method m, final Object obj) {
        final String cmdLabel = label.split("\\.")[0].toLowerCase();

        if(map.getCommand(cmdLabel) == null) {
            org.bukkit.command.Command command = new BukkitCommand(cmdLabel, this, plugin);
            map.register(plugin.getName(), command);
        }

        if(map.getCommand(cmdLabel) instanceof BukkitCommand) {
            BukkitCommand command = (BukkitCommand) map.getCommand(cmdLabel);
            if (command.completer == null) {
                command.completer = new BukkitCompleter();
            }
            command.completer.addCompleter(label, m, obj);
        }
        else if (map.getCommand(cmdLabel) instanceof PluginCommand) {
            try {
                final Object command = map.getCommand(cmdLabel);
                final Field field = command.getClass().getDeclaredField("completer");

                field.setAccessible(true);
                if(field.get(command) == null) {
                    field.set(command, new BukkitCompleter());
                }

                if(field.get(command) instanceof BukkitCompleter) {
                    ((BukkitCompleter) field.get(command)).addCompleter(label, m, obj);
                }
                else {
                    System.out.println("Impossible to register completer " + m.getName() + ", already exist.");
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void defaultCommand(CommandArguments args) {
        args.getSender().sendMessage(args.getLabel() + " is not a existing command.");
    }
}
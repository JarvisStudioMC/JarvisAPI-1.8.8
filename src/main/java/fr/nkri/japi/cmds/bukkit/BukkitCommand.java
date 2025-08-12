package fr.nkri.japi.cmds.bukkit;

import org.apache.commons.lang.Validate;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class BukkitCommand extends Command {

    private final Plugin plugin;
    private final CommandExecutor executor;
    public BukkitCompleter completer;

    public BukkitCommand(final String label, final CommandExecutor executor, final Plugin plugin) {
        super(label);
        Validate.notNull(plugin, "Plugin cannot be null");
        this.executor = executor;
        this.plugin = plugin;
        this.usageMessage = "";
    }

    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if(!plugin.isEnabled() || !testPermission(sender)) {
            return false;
        }

        try {
            final boolean success = executor.onCommand(sender, this, commandLabel, args);
            if(!success && !usageMessage.isEmpty()) {
                for (String line : usageMessage.replace("<command>", commandLabel).split("\n")) {
                    sender.sendMessage(line);
                }
            }

            return success;
        }
        catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + plugin.getDescription().getFullName(), ex);
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws CommandException, IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        List<String> completions = (completer != null) ? completer.onTabComplete(sender, this, alias, args) : null;

        if(completions == null && executor instanceof TabCompleter) {
            completions = ((TabCompleter) executor).onTabComplete(sender, this, alias, args);
        }

        if(completions == null) {
            return super.tabComplete(sender, alias, args);
        }

        return completions;
    }
}
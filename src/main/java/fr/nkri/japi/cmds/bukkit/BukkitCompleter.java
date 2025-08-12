package fr.nkri.japi.cmds.bukkit;

import fr.nkri.japi.cmds.CommandArguments;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BukkitCompleter implements TabCompleter {

    private final Map<String, Entry<Method, Object>> completers = new HashMap<>();

    public void addCompleter(String label, Method m, Object obj) {
        completers.put(label, new AbstractMap.SimpleEntry<>(m, obj));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        for(int i = args.length; i >= 0; i--) {
            final StringBuilder buffer = new StringBuilder(label.toLowerCase());

            for(int x = 0; x < i; x++) {
                if(!args[x].isEmpty() && !args[x].equals(" ")) {
                    buffer.append('.').append(args[x].toLowerCase());
                }
            }

            final String cmdLabel = buffer.toString();
            if(completers.containsKey(cmdLabel)) {
                final Entry<Method, Object> entry = completers.get(cmdLabel);

                try {
                    return (List<String>) entry.getKey().invoke(entry.getValue(), new CommandArguments(sender, command, label, args, cmdLabel.split("\\.").length - 1));
                }
                catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
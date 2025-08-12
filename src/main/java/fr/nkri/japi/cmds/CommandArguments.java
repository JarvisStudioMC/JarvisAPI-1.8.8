package fr.nkri.japi.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandArguments {
    private final CommandSender sender;
    private final Command command;
    private final String label;
    private final String[] args;

    public CommandArguments(final CommandSender sender, final Command command, final String label, final String[] args, final int subCommand) {
        this.sender = sender;
        this.command = command;
        this.args = subCommand < args.length ? Arrays.copyOfRange(args, subCommand, args.length) : new String[0];

        final StringBuilder buffer = new StringBuilder(label);

        for (int x = 0; x < subCommand && x < args.length; x++) {
            buffer.append(".").append(args[x]);
        }
        this.label = buffer.toString();
    }

    public CommandSender getSender() {
        return sender;
    }

    public Command getCommand() {
        return command;
    }

    public String getLabel() {
        return label;
    }

    public String[] getArgs() {
        return args;
    }

    public String getArgs(int index) {
        return args[index];
    }

    public int length() {
        return args.length;
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public Player getPlayer() {
        return sender instanceof Player ? (Player) sender : null;
    }
}
package fr.nkri.japi.cmds;

public abstract class ICommand {

    public ICommand() {}
    public abstract boolean onCommand(final CommandArguments commandArguments);
}
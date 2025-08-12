package fr.nkri.japi.modules;

import fr.nkri.japi.JarvisAPI;
import fr.nkri.japi.packets.JarvisPacket;
import net.minecraft.server.v1_8_R3.ICommand;
import org.bukkit.event.Listener;

public abstract class Module {

    private boolean isEnable;
    private final String name;

    /**
     * @param name module name
     */
    public Module(final String name) {
        this.name = name;
        this.isEnable = true;
    }

    //register an cmd
    public void registerCommand(final ICommand iCommand){
        JarvisAPI.getInstance().registerCommand(iCommand);
    }

    //register a listener
    public void registerListener(final Listener listener){
        JarvisAPI.getInstance().registerListeners(listener);
    }

    //register packets
    public void registerPacket(final JarvisPacket packet, final int packetId){
        JarvisAPI.getInstance().registerPacket(packet, packetId);
    }

    //save data
    public abstract void load();
    public abstract void save();
    //save data

    //Getter, Setter
    public String getName() {
        return name;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }
}

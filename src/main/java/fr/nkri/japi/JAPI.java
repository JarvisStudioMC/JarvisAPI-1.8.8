package fr.nkri.japi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.nkri.japi.cmds.CommandFramework;
import fr.nkri.japi.guis.JInvManager;
import fr.nkri.japi.logs.Logs;
import fr.nkri.japi.logs.enums.LogsType;
import fr.nkri.japi.modules.Module;
import fr.nkri.japi.modules.ModuleManager;
import fr.nkri.japi.modules.cmds.CommandModule;
import fr.nkri.japi.packets.JPacket;
import fr.nkri.japi.packets.PacketManager;
import fr.nkri.japi.utils.json.adapter.ItemStackAdpater;
import fr.nkri.japi.utils.json.adapter.LocationAdapter;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 *
 * @author NKRI
 * @version 1.0
 * @date 10/03/2024
 */

public class JAPI extends JavaPlugin {

    private static JAPI INSTANCE;
    private CommandFramework commandFramework;
    private Gson gson;
    private PacketManager packetManager;

    //module management
    private ModuleManager moduleManager;

    public JAPI(){}

    @Override
    public void onEnable() {
        INSTANCE = this;

        Logs.sendLog("", "════════════════════════════════════════════", LogsType.INFO);
        Logs.sendLog("", "           JAPI - Jarvis API v1.0           ", LogsType.INFO);
        Logs.sendLog("", "     Developed by Jarvis Studio by NKRI    ", LogsType.INFO);
        Logs.sendLog("", "════════════════════════════════════════════", LogsType.INFO);
        Logs.sendLog("[JAPI]", "Initializing components...", LogsType.INFO);

        //Command framework
        Logs.sendLog("[JAPI]", "Loading Command Framework...", LogsType.INFO);
        this.commandFramework = new CommandFramework(this);
        Logs.sendLog("[JAPI]", "Command Framework loaded successfully", LogsType.SUCCESS);

        //Gson
        Logs.sendLog("[JAPI]", "Initializing JSON serializer...", LogsType.INFO);
        this.gson = getGsonBuilder().create();
        Logs.sendLog("[JAPI]", "Gson initialized successfully", LogsType.SUCCESS);

        //Packet manager
        Logs.sendLog("[JAPI]", "Initializing Packet Manager...", LogsType.INFO);
        this.packetManager = new PacketManager();
        Logs.sendLog("[JAPI]", "Packet Manager ready", LogsType.SUCCESS);

        //Inventory manager
        Logs.sendLog("[JAPI]", "Registering Inventory Manager...", LogsType.INFO);
        JInvManager.register(this);
        Logs.sendLog("[JAPI]", "Inventory Manager registered", LogsType.SUCCESS);

        //Bungeecord channel
        Logs.sendLog("[JAPI]", "Opening BungeeCord channel...", LogsType.INFO);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Logs.sendLog("[JAPI]", "BungeeCord channel opened", LogsType.SUCCESS);

        super.onEnable();

        Logs.sendLog("", "════════════════════════════════════════════", LogsType.INFO);
        Logs.sendLog("[JAPI]", "Plugin enabled successfully!", LogsType.SUCCESS);
        Logs.sendLog("[JAPI]", "All systems operational", LogsType.INFO);
        Logs.sendLog("", "════════════════════════════════════════════", LogsType.INFO);
    }

    @Override
    public void onDisable() {
        Logs.sendLog("", "════════════════════════════════════════════", LogsType.INFO);
        Logs.sendLog("[JAPI]", "Shutting down JAPI...", LogsType.INFO);
        Logs.sendLog("", "════════════════════════════════════════════", LogsType.INFO);

        //Save modules
        if (this.moduleManager != null) {
            Logs.sendLog("[JAPI]", "Saving all modules...", LogsType.INFO);
            int savedModules = this.moduleManager.getModules().size();
            this.moduleManager.saveAll();
            Logs.sendLog("[JAPI]", savedModules + " module(s) saved successfully", LogsType.SUCCESS);
        }

        super.onDisable();

        Logs.sendLog("", "════════════════════════════════════════════", LogsType.INFO);
        Logs.sendLog("[JAPI]", "Plugin disabled successfully", LogsType.SUCCESS);
        Logs.sendLog("[JAPI]", "Thank you for using JAPI!", LogsType.INFO);
        Logs.sendLog("[JAPI]", "Developed by Jarvis Studio by NKRI", LogsType.INFO);
        Logs.sendLog("", "════════════════════════════════════════════", LogsType.INFO);
    }

    //module
    public void registerModules(final List<Module> modules){
        this.moduleManager = new ModuleManager(this);

        for(Module module : modules){
            this.moduleManager.registerModule(module);
        }

        this.moduleManager.loadAll();
        registerCommand(new CommandModule(moduleManager));
    }
    //module

    public void registerPacket(final JPacket jPacket, final int id){
        packetManager.registerPacket(jPacket, id);
    }

    public void registerPackets(){
        packetManager.registerPackets();
    }

    public void registerCommand(final Object object){
        this.commandFramework.registerCommands(object);
    }

    public void registerListeners(final Listener listener){
        final PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(listener, this);
    }

    //Json
    public GsonBuilder getGsonBuilder(){
        return new GsonBuilder().setPrettyPrinting().serializeNulls()
                .disableHtmlEscaping()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(ItemStack.class, new ItemStackAdpater())
                .registerTypeAdapter(Location.class, new LocationAdapter());
    }

    public String serialize(final Object obj){
        return this.gson.toJson(obj);
    }

    public <T> T deserialize(final String json, final Class<T> type){
        return this.gson.fromJson(json, type);
    }

    public Gson getGson() {
        return gson;
    }

    public static JAPI getInstance() {
        return INSTANCE;
    }
}
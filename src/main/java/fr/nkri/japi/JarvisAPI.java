package fr.nkri.japi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.nkri.japi.cmds.CommandFramework;
import fr.nkri.japi.guis.JarvisInvManager;
import fr.nkri.japi.logs.Logs;
import fr.nkri.japi.logs.enums.LogsType;
import fr.nkri.japi.modules.Module;
import fr.nkri.japi.modules.ModuleManager;
import fr.nkri.japi.modules.cmds.CommandModule;
import fr.nkri.japi.packets.PacketManager;
import fr.nkri.japi.packets.JarvisPacket;
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

public class JarvisAPI extends JavaPlugin {

    private static JarvisAPI INSTANCE;
    private CommandFramework commandFramework;
    private Gson gson;
    private PacketManager packetManager;

    //module management
    private ModuleManager moduleManager;

    public JarvisAPI(){}

    @Override
    public void onEnable() {
        INSTANCE = this;
        Logs.sendLog("", "-------------------[JARVIS API]-------------------", LogsType.INFO);
        Logs.sendLog("[JarvisAPI]", "Starting... Please wait for the initialization process to complete.", LogsType.INFO);
        this.commandFramework = new CommandFramework(this);
        this.gson = getGsonBuilder().create();
        JarvisInvManager.register(this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        super.onEnable();

        Logs.sendLog("[JarvisAPI]", "Plugin successfully loaded, developed by Jarvis Studio by NKRI", LogsType.INFO);
        Logs.sendLog("", "-------------------[JARVIS API]-------------------", LogsType.INFO);
    }

    @Override
    public void onDisable() {
        Logs.sendLog("", "-------------------[JARVIS API]-------------------", LogsType.INFO);
        Logs.sendLog("[JarvisAPI]", "Plugin successfully unloaded, thank you for using Jarvis.", LogsType.INFO);

        if(this.moduleManager != null){
            this.moduleManager.saveAll();
        }

        super.onDisable();
        Logs.sendLog("[JarvisAPI]", "Good Bye ! Thanks for using JarvisAPI, plugin being unloaded.", LogsType.INFO);
        Logs.sendLog("[JarvisAPI]", "Developed by Jarvis Studio by NKRI.", LogsType.INFO);
        Logs.sendLog("", "-------------------[JARVIS API]-------------------", LogsType.INFO);
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

    public void registerPacket(final JarvisPacket wizPacket, final int id){
        packetManager.registerPacket(wizPacket, id);
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

    public static JarvisAPI getInstance() {
        return INSTANCE;
    }
}
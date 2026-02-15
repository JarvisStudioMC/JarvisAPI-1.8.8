package fr.nkri.japi.modules;

import fr.nkri.japi.JAPI;
import fr.nkri.japi.logs.Logs;
import fr.nkri.japi.logs.enums.LogsType;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private final JAPI main;
    private static ModuleManager INSTANCE;
    private final List<Module> modules;

    /**
     * allows you to manage modules
     */
    public ModuleManager(final JAPI main){
        this.main = main;
        INSTANCE = this;
        this.modules = new ArrayList<>();
    }

    /**
     * @param name
     * @return a module thanks to its name
     */
    public Module getModuleByName(final String name){
        for(Module module : modules){
            if(module.getName().equalsIgnoreCase(name)){
                return module;
            }
        }

        return null;
    }

    //add a module in list
    public void registerModule(final Module module){
        this.modules.add(module);
        Logs.sendLog("[JModule]", "Module '" + module.getName() + "' registered successfully", LogsType.SUCCESS);
    }

    //save data
    public void loadAll() {
        Logs.sendLog("[JModule]", "Loading " + this.modules.size() + " module(s)...", LogsType.INFO);

        this.modules.forEach(module -> {
            try {
                module.load();
                Logs.sendLog("[JModule]", "'" + module.getName() + "' loaded", LogsType.SUCCESS);
            }
            catch (final Exception e) {
                Logs.sendLog("[JModule]", "Failed to load '" + module.getName() + "'", LogsType.ERROR);
                e.printStackTrace();
            }
        });

        Logs.sendLog("[JModule]", "All modules loaded successfully!", LogsType.SUCCESS);
    }

    public void saveAll() {
        Logs.sendLog("[JModule]", "Saving " + this.modules.size() + " module(s)...", LogsType.INFO);

        this.modules.forEach(module -> {
            try {
                module.save();
                Logs.sendLog("[JModule]", "'" + module.getName() + "' saved", LogsType.SUCCESS);
            }
            catch (final Exception e) {
                Logs.sendLog("[JModule]", "Failed to save '" + module.getName() + "'", LogsType.ERROR);
                e.printStackTrace();
            }
        });

        Logs.sendLog("[JModule]", "All modules saved successfully!", LogsType.SUCCESS);
    }
    //save data

    //Getter
    public static ModuleManager getInstance() {
        return INSTANCE;
    }

    public List<Module> getModules() {
        return modules;
    }
}

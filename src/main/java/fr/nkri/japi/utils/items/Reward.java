package fr.nkri.japi.utils.items;

import com.google.gson.annotations.Expose;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Reward {

    @Expose(serialize = true)
    private List<String> cmdList;

    /**
     * List of commands that will be executed
     * @param command cmd without the /
     */
    public Reward(final String command){
        this.cmdList = new ArrayList<>();
        cmdList.add(command);
    }

    /**
     * List of commands that will be executed
     * @param commands array of cmd without the /
     */
    public Reward(final List<String> commands){
        this.cmdList = commands;
    }

    /**
     * Gives the commands in the list to a player
     * Note: to retrieve letter %player% to retrieve players in the config
     *
     * @param player who will receive the orders
     */
    public void giveReward(final Player player) {
        for(String cmd : cmdList) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                    cmd.replaceAll("%player%", player.getName()));
        }
    }

    //Getter
    public List<String> getCmdList() {
        return cmdList;
    }
}

package fr.nkri.japi.logs.enums;

import org.bukkit.ChatColor;

public enum LogsType {

    ERROR(ChatColor.RED.toString()),
    WARNING(ChatColor.YELLOW.toString()),
    SUCCES(ChatColor.GREEN.toString()),
    INFO(ChatColor.AQUA.toString()),
    ;

    private String color;

    private LogsType(final String color){
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}

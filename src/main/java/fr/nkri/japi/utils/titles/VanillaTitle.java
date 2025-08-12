package fr.nkri.japi.utils.titles;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class VanillaTitle {

    private static VanillaTitle INSTANCE;

    private VanillaTitle(){}

    public void sendTitle(final Player player, final Integer fadeIn, final Integer stay, final Integer fadeOut, final String title, final String subtitle) {
        final PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        final PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);

        connection.sendPacket(packetPlayOutTimes);
        sendTitleComponent(player, connection, subtitle, PacketPlayOutTitle.EnumTitleAction.SUBTITLE);
        sendTitleComponent(player, connection, title, PacketPlayOutTitle.EnumTitleAction.TITLE);
    }

    public void sendActionBar(final Player player, final String message) {
        final CraftPlayer craftPlayer = (CraftPlayer) player;
        final IChatBaseComponent chatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        final PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(chatBaseComponent, (byte) 2);

        craftPlayer.getHandle().playerConnection.sendPacket(packetPlayOutChat);
    }

    public void sendTabTitle(final Player player, String header, String footer) {
        final PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        final IChatBaseComponent tabTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
        final IChatBaseComponent tabFoot = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");
        final PacketPlayOutPlayerListHeaderFooter headerPacket = new PacketPlayOutPlayerListHeaderFooter(tabTitle);

        header = formatTabText(player, header);
        footer = formatTabText(player, footer);

        try {
            final Field field = headerPacket.getClass().getDeclaredField("b");

            field.setAccessible(true);
            field.set(headerPacket, tabFoot);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            connection.sendPacket(headerPacket);
        }
    }

    private String formatTabText(final Player player, String text) {
        if (text == null) {
            return "";
        }
        text = ChatColor.translateAlternateColorCodes('&', text);
        return text.replaceAll("%player%", player.getDisplayName());
    }

    private void sendTitleComponent(final Player player, final PlayerConnection connection, String text, final PacketPlayOutTitle.EnumTitleAction action) {
        if(text != null) {
            text = text.replaceAll("%player%", player.getDisplayName());
            text = ChatColor.translateAlternateColorCodes('&', text);

            final IChatBaseComponent titleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\"}");
            final PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(action, titleComponent);

            connection.sendPacket(packetPlayOutTitle);
        }
    }

    public static VanillaTitle getInstance(){
        if(INSTANCE == null){
            INSTANCE = new VanillaTitle();
        }

        return INSTANCE;
    }
}

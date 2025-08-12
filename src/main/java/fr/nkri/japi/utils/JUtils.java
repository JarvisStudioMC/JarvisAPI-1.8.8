package fr.nkri.japi.utils;

import fr.nkri.japi.JAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class JUtils {

    public static String LINE = "§7—————————————————————————————————————";

    //Retourne vrai si l'inventaire du joueur est plein
    public static boolean isInventoryFull(final Player player) {
        final Inventory inventory = player.getInventory();

        return inventory.firstEmpty() == -1;
    }

    //Retourne vrai si le joueur à un material dans sa main
    public static boolean haveItemInHand(final Player player, final Material material){
        final ItemStack stack = player.getItemInHand();

        if(stack != null && stack.getType() != Material.AIR) {
            if(stack.getType() == material){
                return true;
            }
        }
        return false;
    }

    //Retourne vrai si le joueur à un objet dans sa main
    public static boolean haveItemInHand(final Player player) {
        final ItemStack stack = player.getItemInHand();

        if(stack != null && stack.getType() != Material.AIR) {
            return true;
        }
        return false;
    }

    //Envoi un message à tout les joueurs ayant la permission
    public static void sendBroadcastByPermission(final String permission, final List<String> messages){
        for(Player pls : Bukkit.getServer().getOnlinePlayers()){
            if(!pls.hasPermission(permission)){
                return;
            }

            for(String message : messages){
                pls.sendMessage(message);
            }
        }
    }

    //Sérialisation/Désérialisation d'itemstacl (util pour les stocker en base de donnée)
    public static List<ItemStack> deserializeItemStack(final String itemInDataBase){
        final List<ItemStack> itemList = new ArrayList<>();
        final List<String> stringList = new ArrayList<>(Arrays.asList(itemInDataBase.split(";")));

        for(String s : stringList) {
            final String materialString = s.split(":")[0];
            final String amountString = s.split(":")[1];

            final Material material = Material.getMaterial(materialString);
            final int amount = Integer.parseInt(amountString);
            final ItemStack stack = new ItemStack(material, amount);

            itemList.add(stack);
        }

        return itemList;
    }

    public static String serializeItemStack(final List<ItemStack> stackList) {
        String item = "";

        for(ItemStack stack : stackList) {
            item = item + stack.getType() + ":" + stack.getAmount() + ";";
        }

        return item;
    }

    //Envoi un joueur sur un serveur du bungeecord: joueur, nom du serveur (dans la config du proxy)
    public static void sendServer(final Player player, final String serverName) {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(serverName);
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        player.sendPluginMessage(JAPI.getInstance(), "BungeeCord", b.toByteArray());
    }

    public static String transformColor(final String color){
        return color.replace("&", "§");
    }

    //Retourne vrai si le joueur à bien cette item dans sont inventaire
    public static boolean haveItem(final Player player, final ItemStack stack) {
        return haveItem(player, stack, 1);
    }

    public static boolean haveItem(final Player player, final ItemStack stack, final int quantity) {
        return getItemAmount(player, stack) >= quantity;
    }

    public static int getItemAmount(final Player player, final ItemStack stack) {
        int amount = 0;

        for (ItemStack stackInventory : player.getInventory().getContents()) {
            if (stackInventory != null && stackInventory.getType() != Material.AIR && stackInventory.isSimilar(stack) && stackInventory.getData().getData() == stack.getData().getData()) {
                amount += stackInventory.getAmount();
            }
        }

        return amount;
    }

    public static String color(final String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static boolean isPluginEnabled(final String name) {
        return Bukkit.getPluginManager().isPluginEnabled(name);
    }

    public static void callEvent(final org.bukkit.event.Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    public static boolean compareLocations(final Location first, final Location second) {
        return (first.getBlockX() == second.getBlockX() && first.getBlockY() == second.getBlockY() && first.getBlockZ() == second.getBlockZ());
    }

    public static int randomInt(final int min, final int max) {
        final Random random = new Random();
        int range = max - min + 1;
        int randomNum = random.nextInt(range) + min;
        return randomNum;
    }

    public static void broadcastMessage(final String... messages) {
        Bukkit.broadcastMessage(LINE);
        int count = 0;
        while (count < messages.length) {
            String message = messages[count];
            Bukkit.broadcastMessage(color(message));
            count++;
        }
        Bukkit.broadcastMessage(LINE);
    }

    public static String formatLocation(final Location location) {
        return color("&6X&7: &e" + location.getBlockX() + " &6Y&7: &e" + location.getBlockY() + " &6Z&7: &e" + location.getBlockZ());
    }

    public static boolean isInteger(final String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(final String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isFloat(final String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLong(final String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNumeric(final String str) {
        return isDouble(str);
    }

    public static boolean isPositiveInteger(final String str) {
        if (!isInteger(str)) {
            return false;
        }

        int value = Integer.parseInt(str);
        return value > 0;
    }

    public static boolean isPositiveNumber(final String str) {
        if (!isDouble(str)) {
            return false;
        }

        double value = Double.parseDouble(str);
        return value > 0;
    }
}

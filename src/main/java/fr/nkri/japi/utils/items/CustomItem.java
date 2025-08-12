package fr.nkri.japi.utils.items;

import com.google.gson.annotations.Expose;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CustomItem {

    @Expose(serialize = true)
    private ItemStack stack;
    @Expose(serialize = true)
    private String uuidString;

    /**
     * Assigns a UUID to an itemstack,
     * for securing packet exchanges
     *
     * @param stack itemstack to which we associate a UUID
     */
    public CustomItem(final ItemStack stack) {
        this.stack = stack;
        this.uuidString = UUID.randomUUID().toString().substring(0, 16);
    }

    //Getter
    public ItemStack getStack() {
        return stack;
    }

    public String getUuidString() {
        return uuidString;
    }
}

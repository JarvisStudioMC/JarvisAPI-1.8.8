package fr.nkri.japi.utils.json.adapter;

import com.google.gson.*;
import fr.nkri.japi.utils.items.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemStackAdpater implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {
    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final String serializedItem = jsonObject.get("serialized").getAsString();
        final ItemBuilder itemBuilder = new ItemBuilder(serializedItem);

        return itemBuilder.toItemStack();
    }

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {
        final ItemBuilder itemBuilder = new ItemBuilder(itemStack);

        return itemBuilder.toJSONObjectCustom();
    }
}

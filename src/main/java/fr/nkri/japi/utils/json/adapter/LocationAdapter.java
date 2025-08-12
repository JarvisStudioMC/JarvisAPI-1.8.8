package fr.nkri.japi.utils.json.adapter;

import com.google.gson.*;
import fr.nkri.japi.logs.Logs;
import fr.nkri.japi.logs.enums.LogsType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;

public class LocationAdapter implements JsonDeserializer<Location>, JsonSerializer<Location> {

    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        if(!jsonElement.isJsonObject()){
            Logs.sendLog("JsonLocation", "An error occurred while deserializing the Location object: the json does not have a valid json format.", LogsType.ERROR);
            return null;
        }

        final JsonObject obj = (JsonObject) jsonElement;
        final JsonElement world = obj.get("world");
        final JsonElement x = obj.get("x");
        final JsonElement y = obj.get("y");
        final JsonElement z = obj.get("z");
        final JsonElement yaw = obj.get("yaw");
        final JsonElement pitch = obj.get("pitch");

        if(world == null || x == null || y == null || z == null){
            Logs.sendLog("JsonLocation", "Incorrect json string format.", LogsType.ERROR);
            return null;
        }

        final World worldMc = Bukkit.getWorld(world.getAsString());
        if(worldMc == null){
            Logs.sendLog("JsonLocation", "Two worlds suck or are in different dimensions.", LogsType.ERROR);
            return null;
        }

        final Location location = new Location(worldMc, x.getAsDouble(), y.getAsDouble(), z.getAsDouble(), yaw != null ? yaw.getAsFloat() : 0.0F, pitch != null ? pitch.getAsFloat() : 0.0F);
        return location;
    }

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("world", location.getWorld().getName());
        jsonObject.addProperty("x", location.getX());
        jsonObject.addProperty("y", location.getY());
        jsonObject.addProperty("z", location.getZ());
        jsonObject.addProperty("yaw", location.getYaw());
        jsonObject.addProperty("pitch", location.getPitch());
        return jsonObject;
    }
}

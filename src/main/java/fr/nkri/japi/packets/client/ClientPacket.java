package fr.nkri.japi.packets.client;

import fr.nkri.japi.packets.JPacket;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public abstract class ClientPacket extends JPacket {

    @Override
    public void writePacket(final PacketDataSerializer packetDataSerializer) throws IOException {}

    public void writeDouble(final PacketDataSerializer packetDataSerializer) {
        packetDataSerializer.readDouble();
    }

    public int readInt(final PacketDataSerializer packetDataSerializer) throws IOException{
        return packetDataSerializer.readInt();
    }

    public double readDouble(final PacketDataSerializer packetDataSerializer) throws IOException{
        return packetDataSerializer.readDouble();
    }

    public String readString(final PacketDataSerializer packetDataSerializer) throws IOException{
        return packetDataSerializer.c(191919);
    }

    public boolean readBoolean(final PacketDataSerializer packetDataSerializer) throws IOException{
        return packetDataSerializer.readBoolean();
    }

    public ItemStack readStack(final PacketDataSerializer packetDataSerializer) throws IOException{
        return CraftItemStack.asBukkitCopy(packetDataSerializer.i());
    }

    public float readFloat(final PacketDataSerializer packetDataSerializer) throws IOException{
        return packetDataSerializer.readFloat();
    }
}
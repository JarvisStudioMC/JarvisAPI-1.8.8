package fr.nkri.japi.packets;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketListener;

import java.io.IOException;

public abstract class JPacket implements Packet {

    @Override
    public void a(final PacketDataSerializer packet) throws IOException {
        readPacket(packet);
    }

    @Override
    public void b(PacketDataSerializer packet) throws IOException {
        writePacket(packet);
    }

    @Override
    public void a(PacketListener packetListener) {
        handle(packetListener);
    }

    public abstract void readPacket(final PacketDataSerializer packetDataSerializer) throws IOException;
    public abstract void writePacket(final PacketDataSerializer packetDataSerializer) throws IOException;
    public abstract void handle(final PacketListener packetListener);
}

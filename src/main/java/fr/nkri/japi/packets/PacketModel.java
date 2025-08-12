package fr.nkri.japi.packets;


import net.minecraft.server.v1_8_R3.Packet;

public class PacketModel {

    private final Packet packet;
    private final int id;

    public PacketModel(final Packet packet, final int id){
        this.packet = packet;
        this.id = id;
    }

    public Packet getPacket() {
        return packet;
    }

    public int getId() {
        return id;
    }
}

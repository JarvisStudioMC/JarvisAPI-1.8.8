package fr.nkri.japi.packets;

import com.google.common.collect.BiMap;
import fr.nkri.japi.logs.Logs;
import fr.nkri.japi.logs.enums.LogsType;
import net.minecraft.server.v1_8_R3.EnumProtocol;
import net.minecraft.server.v1_8_R3.EnumProtocolDirection;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PacketManager {

    public List<PacketModel> packetList = new ArrayList<PacketModel>();

    //Envoyer un packet à un joueur
    public static void sendPacket(final Packet packet, final Player player) {
        final PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection;
        connection.sendPacket(packet);
    }

    //Enregistrer un packet(param: le packet, id)
    public void registerPacket(final Packet packet, final int packetID) {
        final PacketModel packetModel = new PacketModel(packet, packetID);
        packetList.add(packetModel);
    }

    //Enregistrer les packets
    @SuppressWarnings("unchecked")
    public void registerPackets() {
        try {
            for(PacketModel packet : packetList) {
                final Packet packetMinecraft = packet.getPacket();
                final int packetId = packet.getId();

                final EnumProtocol protocol = EnumProtocol.PLAY;
                final EnumProtocolDirection direction = packetMinecraft.getClass().getName().contains("ClientPacket") ? EnumProtocolDirection.SERVERBOUND : EnumProtocolDirection.CLIENTBOUND;

                final Field j = EnumProtocol.class.getDeclaredField("j"); j.setAccessible(true);
                final Map<EnumProtocolDirection, BiMap<Integer, Class<? extends Packet>>> map = (Map<EnumProtocolDirection, BiMap<Integer, Class<? extends Packet>>>) j.get(protocol);

                final BiMap<Integer, Class<? extends Packet>> biMap = (BiMap)map.get(direction);
                biMap.put(packetId, packetMinecraft.getClass());
                map.put(direction, biMap);

                final Field h = EnumProtocol.class.getDeclaredField("h"); h.setAccessible(true);
                final Map<Class<?>, EnumProtocol> protocols = (Map) h.get(protocol);
                protocols.put(packetMinecraft.getClass(), protocol);

                Logs.sendLog("(JarvisPacket)", "Register packet " + packet.getId(), LogsType.INFO);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
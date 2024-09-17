package me.justahuman.slimefun_essentials.client.payloads;

import com.google.common.io.ByteStreams;
import me.justahuman.slimefun_essentials.utils.Channels;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record SlimefunAddonPayload(String addon) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, SlimefunAddonPayload> CODEC = PacketCodec.of((value, buf) -> {}, buf -> {
        return new SlimefunAddonPayload(ByteStreams.newDataInput(buf.array()).readUTF());
    });

    @Override
    public Id<? extends CustomPayload> getId() {
        return Channels.ADDON_CHANNEL;
    }
}

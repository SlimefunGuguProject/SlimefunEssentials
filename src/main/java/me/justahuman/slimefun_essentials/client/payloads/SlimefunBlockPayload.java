package me.justahuman.slimefun_essentials.client.payloads;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.justahuman.slimefun_essentials.utils.Channels;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record SlimefunBlockPayload(BlockPos pos, String id) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, SlimefunBlockPayload> CODEC = PacketCodec.of((value, buf) -> {}, buf -> {
        ByteArrayDataInput input = ByteStreams.newDataInput(buf.array());
        return new SlimefunBlockPayload(new BlockPos(input.readInt(), input.readInt(), input.readInt()), input.readUTF());
    });

    @Override
    public Id<? extends CustomPayload> getId() {
        return Channels.BLOCK_CHANNEL;
    }
}

package me.justahuman.slimefun_essentials.utils;

import me.justahuman.slimefun_essentials.client.payloads.DisabledItemPayload;
import me.justahuman.slimefun_essentials.client.payloads.SlimefunAddonPayload;
import me.justahuman.slimefun_essentials.client.payloads.SlimefunBlockPayload;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class Channels {
    public static final CustomPayload.Id<SlimefunAddonPayload> ADDON_CHANNEL = newChannel("addon");
    public static final CustomPayload.Id<SlimefunBlockPayload> BLOCK_CHANNEL = newChannel("block");
    public static final CustomPayload.Id<DisabledItemPayload> ITEM_CHANNEL = newChannel("item");

    public static <T extends CustomPayload> CustomPayload.Id<T> newChannel(String channel) {
        return new CustomPayload.Id<>(new Identifier("slimefun_server_essentials", channel));
    }
}

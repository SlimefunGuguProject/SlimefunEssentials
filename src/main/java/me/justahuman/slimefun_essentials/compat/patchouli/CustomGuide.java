package me.justahuman.slimefun_essentials.compat.patchouli;

import me.justahuman.slimefun_essentials.utils.Utils;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.PatchouliAPI;

public class CustomGuide {
    private static final Identifier BOOK_IDENTIFIER = Utils.newIdentifier("slimefun");

    public static void openGuide() {
        PatchouliAPI.get().openBookGUI(BOOK_IDENTIFIER);
    }
}

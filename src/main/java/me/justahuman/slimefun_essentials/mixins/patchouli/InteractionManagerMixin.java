package me.justahuman.slimefun_essentials.mixins.patchouli;

import me.justahuman.slimefun_essentials.compat.patchouli.PatchouliIntegration;
import me.justahuman.slimefun_essentials.config.ModConfig;
import me.justahuman.slimefun_essentials.utils.CompatUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class InteractionManagerMixin {
    @Unique
    private static final String GUIDE_KEY = "slimefun:slimefun_guide_mode";

    @Inject(at = @At("HEAD"), method = "interactBlock", cancellable = true)
    public void openBookBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        handleGuide(player, hand, cir);
    }

    @Inject(at = @At("HEAD"), method = "interactItem", cancellable = true)
    public void openBookItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        handleGuide(player, hand, cir);
    }

    @Unique
    private static void handleGuide(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (!ModConfig.customGuide() || !CompatUtils.isPatchouliLoaded()) {
            return;
        }

        final ItemStack itemStack = player.getStackInHand(hand);
        if (!(itemStack.getItem() instanceof EnchantedBookItem)) {
            return;
        }

        final NbtCompound nbt = itemStack.getNbt();
        if (nbt == null || nbt.isEmpty() || !nbt.contains("PublicBukkitValues")) {
            return;
        }

        final NbtCompound bukkitValues = nbt.getCompound("PublicBukkitValues");
        if (bukkitValues.contains(GUIDE_KEY)) {
            PatchouliIntegration.openGuide();
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}

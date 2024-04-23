package me.justahuman.slimefun_essentials.mixins.custom_guide;

import me.justahuman.slimefun_essentials.client.SlimefunItemGroup;
import me.justahuman.slimefun_essentials.compat.patchouli.CustomGuide;
import me.justahuman.slimefun_essentials.utils.Utils;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.patchouli.client.book.BookCategory;
import vazkii.patchouli.client.book.BookContents;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.common.book.Book;

import java.util.Map;
import java.util.stream.Collectors;

@Mixin(BookContentsBuilder.class)
public abstract class ContentsBuilderMixin {
    @Shadow(remap = false)
    @Final
    private Book book;

    @Shadow(remap = false)
    @Final
    private Map<Identifier, BookCategory> categories;

    @Shadow(remap = false)
    @Final
    private Map<Identifier, BookEntry> entries;

    @Inject(method = "loadFiles", at = @At(value = "INVOKE", target = "Lvazkii/patchouli/client/book/BookContentsBuilder;load(Ljava/lang/String;Lvazkii/patchouli/client/book/BookContentsBuilder$LoadFunc;Ljava/util/Map;)V", ordinal = 1), remap = false)
    private void logBecauseFuckEverything(CallbackInfo ci) {
        Utils.warn(this.categories.keySet().stream().map(Identifier::toString).collect(Collectors.joining(", ")));
    }

    @Inject(method = "build", at = @At("RETURN"), remap = false)
    private void buildSlimefun(World level, CallbackInfoReturnable<BookContents> cir) {
        if (!CustomGuide.BOOK_IDENTIFIER.equals(this.book.id)) {
            return;
        }

        final Identifier itemGroupsIdentifier = Utils.newIdentifier("item_groups");
        final BookCategory itemGroups = this.categories.get(itemGroupsIdentifier);

        int i = 0;
        for (SlimefunItemGroup itemGroup : SlimefunItemGroup.getItemGroups().values()) {
            final Identifier identifier = CustomGuide.bookIdentifier(itemGroup.identifier().toString().replace(":", "_"));
            final BookEntry entry = new BookEntry(CustomGuide.getItemGroupEntry(itemGroup, itemGroups, i), identifier, this.book, "JustAHuman");
            entry.initCategory(identifier, ignored -> itemGroups);
            this.entries.put(identifier, entry);
            i++;
        }
    }
}

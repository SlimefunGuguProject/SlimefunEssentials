package me.justahuman.slimefun_essentials.mixins.patchouli;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import vazkii.patchouli.client.book.BookContents;
import vazkii.patchouli.common.book.Book;

@Mixin(Book.class)
public interface BookMixin {
    @Mutable @Accessor(remap = false)
    void setContents(BookContents contents);
}

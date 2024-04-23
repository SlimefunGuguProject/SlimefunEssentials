package me.justahuman.slimefun_essentials.compat.patchouli;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.justahuman.slimefun_essentials.client.ResourceLoader;
import me.justahuman.slimefun_essentials.client.SlimefunItemGroup;
import me.justahuman.slimefun_essentials.client.SlimefunItemStack;
import me.justahuman.slimefun_essentials.utils.JsonUtils;
import me.justahuman.slimefun_essentials.utils.Utils;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.BookCategory;

import java.util.List;
import java.util.Locale;

public class CustomGuide {
    public static final Identifier BOOK_IDENTIFIER = Utils.newIdentifier("slimefun");

    public static Identifier bookIdentifier(String path) {
        return new Identifier("slimefun", path.toLowerCase(Locale.ROOT));
    }

    public static JsonObject getItemGroupEntry(SlimefunItemGroup itemGroup, BookCategory category, int entryIndex) {
        final JsonObject entry = new JsonObject();
        entry.addProperty("name", itemGroup.itemStack().getName().getString());
        entry.addProperty("icon", JsonUtils.serializeItem(itemGroup.itemStack()));
        entry.addProperty("category", category.getId().toString());
        entry.addProperty("sortnum", entryIndex);

        int contentSize = itemGroup.content().size();
        int pageCount = (int) Math.ceil(contentSize / 20D);
        final JsonArray pages = new JsonArray();
        for (int pageNum = 0; pageNum < pageCount; pageNum++) {
            final JsonObject page = new JsonObject();
            final List<String> subContent = itemGroup.content().subList(pageNum * 20, Math.min(contentSize, (pageNum + 1) * 20));

            int i = 1;
            for (String content : subContent) {
                final SlimefunItemStack itemStack = ResourceLoader.getSlimefunItem(content);
                if (itemStack != null) {
                    page.addProperty("item" + i, JsonUtils.serializeItem(itemStack.itemStack()));
                } else {
                    page.addProperty("class" + i, "me.justahuman.slimefun_essentials.compat.patchouli.EntryComponent");
                    page.addProperty("entry" + i, bookIdentifier(content.replace(":", "_")).toString());
                }
                i++;
            }

            for (int r = i; r <= 20; r++) {
                page.addProperty("class" + r, "me.justahuman.slimefun_essentials.compat.patchouli.EmptyComponent");
            }

            page.addProperty("type", "slimefun_essentials:item_group");
            pages.add(page);
        }
        entry.add("pages", pages);

        return entry;
    }

    public static void openGuide() {
        PatchouliAPI.get().openBookGUI(BOOK_IDENTIFIER);
    }
}

package me.justahuman.slimefun_essentials.compat.patchouli;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.justahuman.slimefun_essentials.client.ResourceLoader;
import me.justahuman.slimefun_essentials.client.SlimefunItemGroup;
import me.justahuman.slimefun_essentials.client.SlimefunItemStack;
import me.justahuman.slimefun_essentials.mixins.custom_guide.BookMixin;
import me.justahuman.slimefun_essentials.utils.JsonUtils;
import me.justahuman.slimefun_essentials.utils.Utils;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.BookCategory;
import vazkii.patchouli.client.book.BookContents;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CustomGuide {
    private static final Identifier BOOK_IDENTIFIER = Utils.newIdentifier("slimefun");
    private static final Map<String, BookCategory> CATEGORIES = new HashMap<>();
    private static final Map<String, BookEntry> ENTRIES = new HashMap<>();

    public static void load() {
        final Book book = BookRegistry.INSTANCE.books.get(BOOK_IDENTIFIER);
        if (book == null) {
            // TODO: throw something?
            return;
        }

        final Map<Identifier, BookCategory> categories = new HashMap<>();
        final Map<Identifier, BookEntry> entries = new HashMap<>();

        final Identifier itemGroupsIdentifier = bookIdentifier("item_groups");
        final BookCategory itemGroups = new BookCategory(getItemGroupsCategory(), itemGroupsIdentifier, book);
        categories.put(itemGroupsIdentifier, itemGroups);
        CATEGORIES.put(itemGroupsIdentifier.toString(), itemGroups);

        int i = -1;
        for (SlimefunItemGroup itemGroup : SlimefunItemGroup.getItemGroups().values()) {
            i++;
            final Identifier identifier = bookIdentifier(itemGroup.identifier().toString().replace(":", "_"));
            final BookEntry entry = new BookEntry(getItemGroupEntry(itemGroup, itemGroups, i), identifier, book, "JustAHuman");
            entry.initCategory(null, ignored -> itemGroups);
            entries.put(identifier, entry);
            ENTRIES.put(identifier.toString(), entry);
        }

        ((BookMixin) book).setContents(new BookContents(book, ImmutableMap.copyOf(categories), ImmutableMap.copyOf(entries), ImmutableMap.of(), null));
    }

    public static Identifier bookIdentifier(String path) {
        return new Identifier("slimefun", path.toLowerCase(Locale.ROOT));
    }

    public static Map<String, BookCategory> getCategories() {
        return Collections.unmodifiableMap(CATEGORIES);
    }

    public static Map<String, BookEntry> getEntries() {
        return Collections.unmodifiableMap(ENTRIES);
    }

    public static JsonObject getItemGroupsCategory() {
        final JsonObject category = new JsonObject();
        category.addProperty("name", "Item Groups");
        category.addProperty("description", "All the Item Groups for Slimefun!");
        category.addProperty("icon", "minecraft:book");
        return category;
    }

    public static JsonObject getItemGroupEntry(SlimefunItemGroup itemGroup, BookCategory category, int entryIndex) {
        final JsonObject entry = new JsonObject();
        entry.addProperty("name", itemGroup.itemStack().getName().getString());
        entry.addProperty("icon", JsonUtils.serializeItem(itemGroup.itemStack()));
        entry.addProperty("category", category.getId().toString());
        entry.addProperty("sortnum", entryIndex);

        int pageNumber = 0;
        final JsonArray pages = new JsonArray();
        for (String content : itemGroup.content()) {
            final SlimefunItemStack itemStack = ResourceLoader.getSlimefunItem(content);
            final JsonObject page = new JsonObject();
            page.addProperty("x", pageNumber * 16);
            page.addProperty("y", pageNumber * 16);

            if (itemStack != null) {
                page.addProperty("type", "patchouli:item");
                page.addProperty("item", JsonUtils.serializeItem(itemStack.itemStack()));
            } else {
                page.addProperty("type", "patchouli:custom");
                page.addProperty("class", "me.justahuman.slimefun_essentials.compat.patchouli.EntryComponent");
                page.addProperty("entry", bookIdentifier(content.replace(":", "_")).toString());
            }

            pages.add(page);
            pageNumber++;
        }
        entry.add("pages", pages);

        return entry;
    }

    public static void openGuide() {
        PatchouliAPI.get().openBookGUI(BOOK_IDENTIFIER);
    }
}

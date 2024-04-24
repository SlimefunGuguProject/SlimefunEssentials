package me.justahuman.slimefun_essentials.compat.patchouli;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.justahuman.slimefun_essentials.client.SlimefunItemGroup;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeComponent;
import me.justahuman.slimefun_essentials.utils.JsonUtils;
import me.justahuman.slimefun_essentials.utils.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.BookCategory;

public class CustomGuide {
    public static final Identifier BOOK_IDENTIFIER = Utils.newIdentifier("slimefun");

    public static JsonObject getItemGroupCategory(SlimefunItemGroup itemGroup, int sortnum) {
        final JsonObject category = new JsonObject();
        final String itemName = itemGroup.itemStack().getName().getString();
        category.addProperty("name", itemName);
        category.addProperty("description", "The " + itemName + " Item Group!");
        category.addProperty("icon", JsonUtils.serializeItem(itemGroup.itemStack()));
        category.addProperty("sortnum", sortnum);

        for (String requirement : itemGroup.requirements()) {
            if (requirement.startsWith("parent: ")) {
                category.addProperty("parent", requirement.substring(8));
                break;
            }
        }

        if (!category.has("parent")) {
            category.addProperty("parent", "slimefun_essentials:item_groups");
        }

        return category;
    }

    public static JsonObject getRecipeEntry(BookCategory category, SlimefunRecipeCategory recipeCategory, int sortnum) {
        final JsonObject entry = new JsonObject();
        final ItemStack itemStack = recipeCategory.getItemFromId();
        entry.addProperty("category", category.getId().toString());
        entry.addProperty("name", itemStack.getName().getString());
        entry.addProperty("icon", JsonUtils.serializeItem(itemStack));
        entry.addProperty("sortnum", sortnum);

        final JsonArray pages = new JsonArray();
        for (SlimefunRecipe recipe : recipeCategory.recipesFor()) {
            pages.add(getPage(recipeCategory, recipe));
        }
        entry.add("pages", pages);

        return entry;
    }

    public static JsonObject getPage(SlimefunRecipeCategory category, SlimefunRecipe recipe) {
        final String type = category.type();
        final JsonObject page = new JsonObject();
        if (type.contains("grid")) {
            page.addProperty("type", "slimefun_essentials:grid_recipe");
        } else if (type.equals("ancient_altar")) {
            page.addProperty("type", "slimefun_essentials:ancient_altar_recipe");
            page.addProperty("item1", "minecraft:barrier");
            page.addProperty("item2", "minecraft:barrier");
            page.addProperty("item3", "minecraft:barrier");
            page.addProperty("item4", "minecraft:barrier");
            page.addProperty("item5", "minecraft:barrier");
            page.addProperty("item6", "minecraft:barrier");
            page.addProperty("item7", "minecraft:barrier");
            page.addProperty("item8", "minecraft:barrier");
            page.addProperty("item9", "minecraft:barrier");
        } else if (type.equals("smeltery")) {
            page.addProperty("type", "slimefun_essentials:smeltery_recipe");
        } else if (type.equals("reactor")) {
            page.addProperty("type", "slimefun_essentials:reactor_recipe");
        } else {
            page.addProperty("type", "slimefun_essentials:process_recipe");
        }
        return page;
    }

    public static void openGuide() {
        PatchouliAPI.get().openBookGUI(BOOK_IDENTIFIER);
    }
}

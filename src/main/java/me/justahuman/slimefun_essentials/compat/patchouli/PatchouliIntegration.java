package me.justahuman.slimefun_essentials.compat.patchouli;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.justahuman.slimefun_essentials.client.SlimefunItemGroup;
import me.justahuman.slimefun_essentials.client.SlimefunLabel;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeComponent;
import me.justahuman.slimefun_essentials.compat.patchouli.pages.AncientAltarPage;
import me.justahuman.slimefun_essentials.compat.patchouli.pages.GridPage;
import me.justahuman.slimefun_essentials.compat.patchouli.pages.ProcessPage;
import me.justahuman.slimefun_essentials.compat.patchouli.pages.ReactorPage;
import me.justahuman.slimefun_essentials.compat.patchouli.pages.SmelteryPage;
import me.justahuman.slimefun_essentials.utils.JsonUtils;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import me.justahuman.slimefun_essentials.utils.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.BookCategory;
import vazkii.patchouli.client.book.ClientBookRegistry;

import java.util.List;

public class PatchouliIntegration {
    public static final Identifier BOOK_IDENTIFIER = Utils.newIdentifier("slimefun");
    public static final PatchouliIdInterpreter INTERPRETER = new PatchouliIdInterpreter();

    public static void init() {
        ClientBookRegistry.INSTANCE.pageTypes.put(Utils.newIdentifier("ancient_altar"), AncientAltarPage.class);
        ClientBookRegistry.INSTANCE.pageTypes.put(Utils.newIdentifier("grid"), GridPage.class);
        ClientBookRegistry.INSTANCE.pageTypes.put(Utils.newIdentifier("process"), ProcessPage.class);
        ClientBookRegistry.INSTANCE.pageTypes.put(Utils.newIdentifier("reactor"), ReactorPage.class);
        ClientBookRegistry.INSTANCE.pageTypes.put(Utils.newIdentifier("smeltery"), SmelteryPage.class);
    }

    public static JsonObject getItemGroupCategory(SlimefunItemGroup itemGroup, int sortnum) {
        final JsonObject category = new JsonObject();
        final String itemName = itemGroup.itemStack().getName().getString();
        category.addProperty("name", itemName);
        category.addProperty("description", "The " + itemName + " Item Group!");
        category.addProperty("icon", JsonUtils.serializeItem(itemGroup.itemStack()));
        category.addProperty("sortnum", sortnum);

        for (String requirement : itemGroup.requirements()) {
            if (requirement.startsWith("slimefun_essentials:")) {
                category.addProperty("parent", requirement);
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
        List<SlimefunRecipe> recipesFor = recipeCategory.recipesFor();
        for (int i = 0; i < recipesFor.size(); i++) {
            SlimefunRecipe recipe = recipesFor.get(i);
            pages.add(getPage(recipeCategory, recipe, i));
        }
        entry.add("pages", pages);

        return entry;
    }

    public static JsonObject getPage(SlimefunRecipeCategory category, SlimefunRecipe recipe, int recipeIndex) {
        final String type = recipe.parent().type();
        final JsonObject page = new JsonObject();

        page.addProperty("id", category.id());
        page.addProperty("recipe_index", recipeIndex);

        if (type.contains("grid")) {
            page.addProperty("type", "slimefun_essentials:grid_recipe");
            page.addProperty("side", TextureUtils.getSideSafe(type));
        } else {
            page.addProperty("type", "slimefun_essentials:" + type);
        }

        return page;
    }

    public static void openGuide() {
        PatchouliAPI.get().openBookGUI(BOOK_IDENTIFIER);
    }
}

package me.justahuman.slimefun_essentials.compat.rei;

import me.justahuman.slimefun_essentials.client.ResourceLoader;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.client.SlimefunItemStack;
import me.justahuman.slimefun_essentials.client.SlimefunLabel;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.compat.rei.categorys.AncientAltarCategory;
import me.justahuman.slimefun_essentials.compat.rei.categorys.GridCategory;
import me.justahuman.slimefun_essentials.compat.rei.categorys.ProcessCategory;
import me.justahuman.slimefun_essentials.compat.rei.categorys.ReactorCategory;
import me.justahuman.slimefun_essentials.compat.rei.categorys.SlimefunReiCategory;
import me.justahuman.slimefun_essentials.compat.rei.categorys.SmelteryCategory;
import me.justahuman.slimefun_essentials.compat.rei.displays.AncientAltarDisplay;
import me.justahuman.slimefun_essentials.compat.rei.displays.GridDisplay;
import me.justahuman.slimefun_essentials.compat.rei.displays.ProcessDisplay;
import me.justahuman.slimefun_essentials.compat.rei.displays.ReactorDisplay;
import me.justahuman.slimefun_essentials.compat.rei.displays.SlimefunDisplay;
import me.justahuman.slimefun_essentials.compat.rei.displays.SmelteryDisplay;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import me.justahuman.slimefun_essentials.utils.Utils;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;

import java.util.HashMap;
import java.util.Map;

public class ReiIntegration implements REIClientPlugin {
    public static final ReiRecipeInterpreter RECIPE_INTERPRETER = new ReiRecipeInterpreter();
    private static final Map<SlimefunRecipeCategory, DisplayCategory<?>> CATEGORIES = new HashMap<>();
    private static EntryRegistry entryRegistry = null;
    private static CategoryRegistry categoryRegistry = null;
    private static DisplayRegistry displayRegistry = null;

    @Override
    public double getPriority() {
        return 10;
    }

    @Override
    public void registerItemComparators(ItemComparatorRegistry registry) {
        if (!Utils.shouldFunction()) {
            return;
        }

        registry.registerGlobal(new SlimefunIdComparator());
    }

    @Override
    public void registerEntries(EntryRegistry registry) {
        if (!Utils.shouldFunction()) {
            return;
        }

        entryRegistry = registry;
    }
    
    @Override
    public void registerCategories(CategoryRegistry registry) {
        if (!Utils.shouldFunction()) {
            return;
        }

        categoryRegistry = registry;
    }
    
    @Override
    public void registerDisplays(DisplayRegistry registry) {
        if (!Utils.shouldFunction()) {
            return;
        }

        displayRegistry = registry;
    }

    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {
        for (SlimefunRecipeCategory category : CATEGORIES.keySet()) {
            if (category.type().contains("grid")) {
                registry.register(SimpleTransferHandler.create(
                        Generic3x3ContainerScreenHandler.class,
                        CATEGORIES.get(category).getCategoryIdentifier(),
                        new SimpleTransferHandler.IntRange(0, 8)
                ));
            }
        }
    }

    public static void load() {
        if (entryRegistry == null || categoryRegistry == null || displayRegistry == null) {
            return;
        }

        for (SlimefunItemStack slimefunItemStack : ResourceLoader.getSlimefunItems().values()) {
            entryRegistry.addEntry(EntryStacks.of(slimefunItemStack.itemStack()));
        }

        CATEGORIES.clear();
        for (SlimefunRecipeCategory slimefunRecipeCategory : SlimefunRecipeCategory.getRecipeCategories().values()) {
            final ItemStack icon = slimefunRecipeCategory.getItemFromId();
            final DisplayCategory<?> displayCategory = getReiCategory(slimefunRecipeCategory, icon);
            categoryRegistry.add(displayCategory);
            categoryRegistry.addWorkstations(displayCategory.getCategoryIdentifier(), EntryStacks.of(icon));
            CATEGORIES.put(slimefunRecipeCategory, displayCategory);
        }

        for (SlimefunRecipeCategory slimefunRecipeCategory : SlimefunRecipeCategory.getRecipeCategories().values()) {
            for (SlimefunRecipe slimefunRecipe : slimefunRecipeCategory.recipes()) {
                displayRegistry.add(getDisplay(slimefunRecipeCategory, slimefunRecipe));
            }
        }
    }

    public static SlimefunReiCategory<? extends SlimefunDisplay> getReiCategory(SlimefunRecipeCategory slimefunRecipeCategory, ItemStack icon) {
        final String type = slimefunRecipeCategory.type();
        if (type.equals("ancient_altar")) {
            return new AncientAltarCategory(slimefunRecipeCategory, icon);
        } else if (type.equals("smeltery")) {
            return new SmelteryCategory(slimefunRecipeCategory, icon);
        } else if (type.equals("reactor")) {
            return new ReactorCategory(slimefunRecipeCategory, icon);
        } else if (type.contains("grid")) {
            return new GridCategory(slimefunRecipeCategory, icon, TextureUtils.getSideSafe(type));
        } else {
            return new ProcessCategory(slimefunRecipeCategory, icon);
        }
    }

    public static SlimefunDisplay getDisplay(SlimefunRecipeCategory slimefunRecipeCategory, SlimefunRecipe slimefunRecipe) {
        final String type = slimefunRecipeCategory.type();
        if (type.equals("ancient_altar")) {
            return new AncientAltarDisplay(slimefunRecipeCategory, slimefunRecipe);
        } else if (type.equals("smeltery")) {
            return new SmelteryDisplay(slimefunRecipeCategory, slimefunRecipe);
        } else if (type.equals("reactor")) {
            return new ReactorDisplay(slimefunRecipeCategory, slimefunRecipe);
        } else if (type.contains("grid")) {
            return new GridDisplay(slimefunRecipeCategory, slimefunRecipe, TextureUtils.getSideSafe(type));
        } else {
            return new ProcessDisplay(slimefunRecipeCategory, slimefunRecipe);
        }
    }

    public static Widget widgetFromSlimefunLabel(SlimefunLabel slimefunLabel, int x, int y) {
        return Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> slimefunLabel.draw(graphics, x, y, REIRuntime.getInstance().isDarkThemeEnabled()));
    }

    public static Widget toolTipForSlimefunLabel(SlimefunLabel slimefunLabel, int x, int y) {
        return Widgets.createTooltip(new Rectangle(x, y, slimefunLabel.width(), slimefunLabel.height()), slimefunLabel.text());
    }

    /**
     * I would like to note that a lot of the logic for this method came from EMI: AnimatedTextureWidget.java
     */
    public static Widget widgetFromSlimefunLabel(SlimefunLabel slimefunLabel, int x, int y, int time, boolean horizontal, boolean endToStart, boolean fullToEmpty) {
        return Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
            int subTime = (int) (System.currentTimeMillis() % time);
            if (endToStart ^ fullToEmpty) {
                subTime = time - subTime;
            }

            int mx = x;
            int my = y;
            int w = slimefunLabel.width();
            int mw = slimefunLabel.width();
            int h = slimefunLabel.height();
            int mh = slimefunLabel.height();
            int u = slimefunLabel.u();
            int mu = slimefunLabel.u();
            int v = slimefunLabel.v();
            int mv = slimefunLabel.v();
            int rw = slimefunLabel.width();
            int mrw = slimefunLabel.width();
            int rh = slimefunLabel.height();
            int mrh = slimefunLabel.height();

            if (horizontal) {
                if (endToStart) {
                    mx = x + w * subTime / time;
                    mu = u + rw * subTime / time;
                    mw = w - (mx - x);
                    mrw = rw - (mu - u);
                } else {
                    mw = w * subTime / time;
                    mrw = rw * subTime / time;
                }
            } else {
                if (endToStart) {
                    my = y + h * subTime / time;
                    mv = v + rh * subTime / time;
                    mh = h - (my - y);
                    mrh = rh - (mv - v);
                } else {
                    mh = h * subTime / time;
                    mrh = rh * subTime / time;
                }
            }

            slimefunLabel.draw(graphics, mx, my, mw, mh, mu, mv, mrw, mrh, REIRuntime.getInstance().isDarkThemeEnabled());
        });
    }
}

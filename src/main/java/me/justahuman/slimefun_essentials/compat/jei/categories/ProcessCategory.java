package me.justahuman.slimefun_essentials.compat.jei.categories;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import me.justahuman.slimefun_essentials.api.OffsetBuilder;
import me.justahuman.slimefun_essentials.api.SimpleRecipeRenderer;
import me.justahuman.slimefun_essentials.client.SlimefunItemStack;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.client.SlimefunLabel;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeComponent;
import me.justahuman.slimefun_essentials.compat.jei.JeiIntegration;
import me.justahuman.slimefun_essentials.api.ManualRecipeRenderer;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import me.justahuman.slimefun_essentials.utils.Utils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProcessCategory extends SimpleRecipeRenderer implements IRecipeCategory<SlimefunRecipe>, ManualRecipeRenderer {
    protected final IGuiHelper guiHelper;
    protected final SlimefunRecipeCategory slimefunRecipeCategory;
    protected final SlimefunItemStack catalyst;
    protected IDrawable icon;
    protected final IDrawable background;
    protected final IDrawableAnimated positiveEnergy;
    protected final IDrawableAnimated negativeEnergy;
    protected final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
    protected final LoadingCache<Integer, IDrawableAnimated> cachedBackwardsArrows;

    public ProcessCategory(IGuiHelper guiHelper, SlimefunRecipeCategory slimefunRecipeCategory, SlimefunItemStack catalyst) {
        this(Type.PROCESS, guiHelper, slimefunRecipeCategory, catalyst);
    }
    
    public ProcessCategory(Type type, IGuiHelper guiHelper, SlimefunRecipeCategory slimefunRecipeCategory, SlimefunItemStack catalyst) {
        super(type);

        this.guiHelper = guiHelper;
        this.slimefunRecipeCategory = slimefunRecipeCategory;
        this.catalyst = catalyst;
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, catalyst.itemStack());
        this.background = guiHelper.drawableBuilder(TextureUtils.WIDGETS, 0, 0, 0, 0).addPadding(yPadding(), yPadding(), xPadding(), xPadding()).build();
        this.positiveEnergy = guiHelper.drawableBuilder(TextureUtils.WIDGETS, TextureUtils.ENERGY_POSITIVE.u(), TextureUtils.ENERGY.v(), TextureUtils.ENERGY_WIDTH, TextureUtils.ENERGY_HEIGHT).buildAnimated(20, IDrawableAnimated.StartDirection.TOP, false);
        this.negativeEnergy = guiHelper.drawableBuilder(TextureUtils.WIDGETS, TextureUtils.ENERGY_NEGATIVE.u(), TextureUtils.ENERGY.v(), TextureUtils.ENERGY_WIDTH, TextureUtils.ENERGY_HEIGHT).buildAnimated(20, IDrawableAnimated.StartDirection.BOTTOM, true);
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    @NotNull
                    public IDrawableAnimated load(@NotNull Integer sfTicks) {
                        return guiHelper.drawableBuilder(TextureUtils.WIDGETS, TextureUtils.ARROW.u(), TextureUtils.ARROW.v() + TextureUtils.ARROW_HEIGHT, TextureUtils.ARROW_WIDTH, TextureUtils.ARROW_HEIGHT).buildAnimated(sfTicks * 10, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
        this.cachedBackwardsArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    @NotNull
                    public IDrawableAnimated load(@NotNull Integer sfTicks) {
                        return guiHelper.drawableBuilder(TextureUtils.WIDGETS, TextureUtils.BACKWARDS_ARROW.u(), TextureUtils.BACKWARDS_ARROW.v() + TextureUtils.ARROW_HEIGHT, TextureUtils.ARROW_WIDTH, TextureUtils.ARROW_HEIGHT).buildAnimated(sfTicks * 10, IDrawableAnimated.StartDirection.RIGHT, false);
                    }
                });
    }
    
    public int xPadding() {
        return getDisplayWidth(this.slimefunRecipeCategory) / 2;
    }
    
    public int yPadding() {
        return getDisplayHeight(this.slimefunRecipeCategory) / 2;
    }

    @Override
    @NotNull
    public RecipeType<SlimefunRecipe> getRecipeType() {
        return RecipeType.create(Utils.ID, this.slimefunRecipeCategory.id().toLowerCase(), SlimefunRecipe.class);
    }
    
    @Override
    @NotNull
    public Text getTitle() {
        return Text.translatable("slimefun_essentials.recipes.category.slimefun", this.catalyst.itemStack().getName());
    }
    
    @Override
    @NotNull
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    @NotNull
    public IDrawable getIcon() {
        return this.icon;
    }

    public void updateIcon() {
        this.icon = this.guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, this.catalyst.itemStack());;
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SlimefunRecipe recipe, IFocusGroup focuses) {
        final OffsetBuilder offsets = new OffsetBuilder(this, recipe, calculateXOffset(this.slimefunRecipeCategory, recipe));

        if (recipe.hasLabels()) {
            offsets.x().add((TextureUtils.LABEL_SIZE + TextureUtils.PADDING) * recipe.labels().size());
        }

        if (recipe.hasEnergy() && recipe.hasOutputs()) {
            offsets.x().addEnergy();
        }

        if (recipe.hasInputs()) {
            for (SlimefunRecipeComponent input : recipe.inputs()) {
                JeiIntegration.RECIPE_INTERPRETER.addIngredients(builder.addSlot(RecipeIngredientRole.INPUT, offsets.getX() + 1, offsets.slot() + 1), input);
                offsets.x().addSlot();
            }
        } else {
            JeiIntegration.RECIPE_INTERPRETER.addIngredient(builder.addSlot(RecipeIngredientRole.INPUT, offsets.getX() + 1, offsets.slot() + 1), this.catalyst.itemStack());
            offsets.x().addSlot();
        }

        offsets.x().addArrow();

        if (recipe.hasOutputs()) {
            for (SlimefunRecipeComponent output : recipe.outputs()) {
                JeiIntegration.RECIPE_INTERPRETER.addIngredients(builder.addSlot(RecipeIngredientRole.OUTPUT, offsets.getX() + 5, offsets.output() + 5), output);
                offsets.x().addOutput();
            }
        }
    }

    @Override
    public void draw(SlimefunRecipe recipe, IRecipeSlotsView recipeSlotsView, DrawContext graphics, double mouseX, double mouseY) {
        final OffsetBuilder offsets = new OffsetBuilder(this, recipe, calculateXOffset(this.slimefunRecipeCategory, recipe));

        // Display Labels
        if (recipe.hasLabels()) {
            for (SlimefunLabel slimefunLabel : recipe.labels()) {
                slimefunLabel.draw(graphics, offsets.getX(), offsets.label());
                offsets.x().addLabel();
            }
        }

        // Display Energy
        addEnergyWithCheck(graphics, offsets, recipe);

        // Display Inputs, only the slot icon
        if (recipe.hasInputs()) {
            for (int i = 0; i < recipe.inputs().size(); i++) {
                TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.slot());
                offsets.x().addSlot();
            }
        } else {
            TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.slot());
            offsets.x().addSlot();
        }

        // Display Arrow
        addArrow(graphics, offsets, recipe);

        // Display Outputs
        addOutputsOrEnergy(graphics, offsets, recipe);
    }

    @NotNull
    @Override
    public List<Text> getTooltipStrings(SlimefunRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        final List<Text> tooltips = new ArrayList<>();
        final OffsetBuilder offsets = new OffsetBuilder(this, recipe, calculateXOffset(this.slimefunRecipeCategory, recipe));

        // Label Tooltips
        if (recipe.hasLabels()) {
            for (SlimefunLabel slimefunLabel : recipe.labels()) {
                if (tooltipActive(mouseX, mouseY, offsets.getX(), offsets.label(), slimefunLabel)) {
                    tooltips.add(labelTooltip(slimefunLabel));
                }
                offsets.x().addLabel();
            }
        }

        // Energy Tooltip Option 1
        if (recipe.hasEnergy() && recipe.hasOutputs()) {
            if (tooltipActive(mouseX, mouseY, offsets.getX(), offsets.energy(), TextureUtils.ENERGY)) {
                tooltips.add(energyTooltip(recipe));
            }
            offsets.x().addEnergy();
        }

        offsets.x().add((TextureUtils.SLOT_SIZE + TextureUtils.PADDING) * (recipe.hasInputs() ? recipe.inputs().size() : 1));

        // Arrow Tooltip
        if (recipe.hasTime() && tooltipActive(mouseX, mouseY, offsets.getX(), offsets.arrow(), TextureUtils.ARROW)) {
            tooltips.add(timeTooltip(recipe));
        }
        offsets.x().addArrow();

        // Energy Tooltip Option 2
        if (!recipe.hasOutputs() && tooltipActive(mouseX, mouseY, offsets.getX(), offsets.energy(), TextureUtils.ENERGY)) {
            tooltips.add(energyTooltip(recipe));
        }

        return tooltips;
    }

    public void drawEnergyFill(DrawContext graphics, int x, int y, boolean negative) {
        (negative ? this.negativeEnergy : this.positiveEnergy).draw(graphics, x, y);
    }

    @Override
    public void drawArrowFill(DrawContext graphics, int x, int y, int sfTicks, boolean backwards) {
        (backwards ? this.cachedBackwardsArrows.getUnchecked(sfTicks) : this.cachedArrows.getUnchecked(sfTicks)).draw(graphics, x, y);
    }
}

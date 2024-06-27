package me.justahuman.slimefun_essentials.compat.emi.recipes;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ListEmiIngredient;
import dev.emi.emi.api.widget.FillingArrowWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import me.justahuman.slimefun_essentials.api.OffsetBuilder;
import me.justahuman.slimefun_essentials.api.SimpleRecipeRenderer;
import me.justahuman.slimefun_essentials.client.DrawMode;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.client.SlimefunLabel;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.compat.emi.EmiIntegration;
import me.justahuman.slimefun_essentials.compat.emi.EmiUtils;
import me.justahuman.slimefun_essentials.compat.emi.EntityEmiStack;
import me.justahuman.slimefun_essentials.compat.emi.ReverseFillingArrowWidget;
import me.justahuman.slimefun_essentials.compat.emi.SlimefunEmiCategory;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProcessRecipe extends SimpleRecipeRenderer implements EmiRecipe {
    protected final SlimefunRecipeCategory slimefunRecipeCategory;
    protected final SlimefunRecipe slimefunRecipe;
    protected final SlimefunEmiCategory emiRecipeCategory;
    protected final List<EmiIngredient> inputs = new ArrayList<>();
    protected final List<EmiStack> outputs = new ArrayList<>();

    public ProcessRecipe(SlimefunRecipeCategory slimefunRecipeCategory, SlimefunRecipe slimefunRecipe, SlimefunEmiCategory emiRecipeCategory) {
        this(Type.PROCESS, slimefunRecipeCategory, slimefunRecipe, emiRecipeCategory);
    }

    public ProcessRecipe(Type type, SlimefunRecipeCategory slimefunRecipeCategory, SlimefunRecipe slimefunRecipe, SlimefunEmiCategory emiRecipeCategory) {
        super(type);

        this.slimefunRecipeCategory = slimefunRecipeCategory;
        this.slimefunRecipe = slimefunRecipe;
        this.emiRecipeCategory = emiRecipeCategory;
        this.inputs.addAll(EmiIntegration.RECIPE_INTERPRETER.getInputIngredients(this.slimefunRecipe));
        this.outputs.addAll(EmiIntegration.RECIPE_INTERPRETER.getOutputStacks(this.slimefunRecipe));
    }
    
    @Override
    public SlimefunEmiCategory getCategory() {
        return this.emiRecipeCategory;
    }
    
    @Override
    @Nullable
    public Identifier getId() {
        return null;
    }
    
    @Override
    public List<EmiIngredient> getInputs() {
        return this.inputs;
    }
    
    @Override
    public List<EmiStack> getOutputs() {
        return this.outputs;
    }

    @Override
    public int getDisplayWidth() {
        return getDisplayWidth(this.slimefunRecipe);
    }

    @Override
    public int getDisplayHeight() {
        return getDisplayHeight(this.slimefunRecipe);
    }
    
    @Override
    public boolean supportsRecipeTree() {
        return true;
    }
    
    @Override
    public void addWidgets(WidgetHolder widgets) {
        final OffsetBuilder offsets = new OffsetBuilder(this, this.slimefunRecipe);

        // Display Labels
        if (this.slimefunRecipe.hasLabels()) {
            for (SlimefunLabel slimefunLabel : this.slimefunRecipe.labels()) {
                widgets.add(EmiUtils.wrap(slimefunLabel, offsets.getX(), offsets.label(), true));
                offsets.x().addLabel();
            }
        }
    
        // Display Energy
        addEnergyWithCheck(widgets, offsets);
    
        // Display Inputs
        if (this.slimefunRecipe.hasInputs()) {
            for (EmiIngredient input : this.inputs) {
                final boolean large = isLarge(input);
                widgets.addSlot(input, offsets.getX(), large ? offsets.largeSlot() : offsets.slot()).large(large);
                offsets.x().add((large ? TextureUtils.LARGE_SLOT.width() : TextureUtils.SLOT.width()));
            }
            offsets.x().addPadding();
        } else {
            widgets.addSlot((EmiIngredient) this.emiRecipeCategory.icon, offsets.getX(), offsets.slot());
            offsets.x().addSlot();
        }

        if (this.slimefunRecipe.hasEnergy() || this.slimefunRecipe.hasOutputs()) {
            // Display Arrow
            addArrowWithCheck(widgets, offsets);

            // Display Outputs
            addOutputsOrEnergy(widgets, offsets);
        }
    }

    protected boolean isLarge(EmiIngredient ingredient) {
        if (ingredient instanceof ListEmiIngredient list) {
            return list.getEmiStacks().stream().anyMatch(this::isLarge);
        }
        return ingredient instanceof EntityEmiStack stack && stack.isLarge();
    }

    protected void addEnergyWithCheck(WidgetHolder widgets, OffsetBuilder offsets) {
        if (this.slimefunRecipe.hasEnergy() && this.slimefunRecipe.hasOutputs()) {
            addEnergy(widgets, offsets);
        }
    }

    protected void addEnergy(WidgetHolder widgets, OffsetBuilder offsets) {
        addEnergy(widgets, offsets.getX(), offsets.energy());
        offsets.x().addEnergy();
    }

    protected void addEnergy(WidgetHolder widgets, int x, int y) {
        final int totalEnergy = this.slimefunRecipe.totalEnergy();
        widgets.add(EmiUtils.wrap(TextureUtils.ENERGY, x, y, false));
        widgets.addAnimatedTexture(EmiUtils.wrap(totalEnergy >= 0 ? TextureUtils.ENERGY_POSITIVE : TextureUtils.ENERGY_NEGATIVE), x, y, 1000, false, totalEnergy < 0, totalEnergy < 0).tooltip(tooltip("slimefun_essentials.recipes.energy." + (totalEnergy >= 0 ? "generate" : "use"), TextureUtils.numberFormat.format(Math.abs(totalEnergy))));
    }

    protected void addArrowWithCheck(WidgetHolder widgets, OffsetBuilder offsets) {
        addArrowWithCheck(widgets, offsets.getX(), offsets.arrow(), false);
        offsets.x().addArrow();
    }

    protected void addArrowWithCheck(WidgetHolder widgets, int x, int y, boolean backwards) {
        if (this.slimefunRecipe.hasTime()) {
            final int sfTicks = this.slimefunRecipe.sfTicks();
            final int millis =  sfTicks * 500;
            addFillingArrow(widgets, x, y, backwards, sfTicks, millis);
        } else {
            addArrow(widgets, x, y, backwards);
        }
    }

    protected void addArrow(WidgetHolder widgets, int x, int y, boolean backwards) {
        widgets.addTexture(EmiUtils.wrap(backwards ? TextureUtils.BACKWARDS_ARROW : TextureUtils.ARROW), x, y);
    }

    protected void addFillingArrow(WidgetHolder widgets, int x, int y, boolean backwards, int sfTicks, int millis) {
        widgets.add(backwards ? new ReverseFillingArrowWidget(x, y, millis) : new FillingArrowWidget(x, y, millis)).tooltip(tooltip("slimefun_essentials.recipes.time", TextureUtils.numberFormat.format(sfTicks / 2f), TextureUtils.numberFormat.format(sfTicks * 10L)));
    }

    protected void addOutputsOrEnergy(WidgetHolder widgets, OffsetBuilder offsets) {
        if (this.slimefunRecipe.hasOutputs()) {
            addOutputs(widgets, offsets);
        } else if (this.slimefunRecipe.hasEnergy()) {
            addEnergy(widgets, offsets);
        }
    }

    protected void addOutputs(WidgetHolder widgets, OffsetBuilder offsets) {
        for (EmiStack output : this.outputs) {
            widgets.addSlot(output, offsets.getX(), offsets.largeSlot()).recipeContext(this).large(true);
            offsets.x().addLargeSlot(false);
        }
        offsets.x().addPadding();
    }

    protected List<TooltipComponent> tooltip(String key, Object... args) {
        return List.of(TooltipComponent.of(Text.translatable(key, args).asOrderedText()));
    }

    @Override
    public DrawMode getDrawMode() {
        return DrawMode.LIGHT;
    }
}
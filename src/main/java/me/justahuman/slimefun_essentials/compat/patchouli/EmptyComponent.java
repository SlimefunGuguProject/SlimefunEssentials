package me.justahuman.slimefun_essentials.compat.patchouli;

import net.minecraft.client.gui.DrawContext;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.function.UnaryOperator;

public class EmptyComponent implements ICustomComponent {
    @Override
    public void build(int ignored1, int ignored2, int ignored3) {}

    @Override
    public void render(DrawContext ignored1, IComponentRenderContext ignored2, float ignored3, int ignored4, int ignored5) {}

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> ignored1) {}
}

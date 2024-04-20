package me.justahuman.slimefun_essentials.api;

import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.utils.TextureUtils;

public abstract class RecipeRenderer {
    private final Type type;

    protected RecipeRenderer(Type type) {
        this.type = type;
    }

    public int getContentsWidth(SlimefunRecipeCategory slimefunRecipeCategory) {
        return this.type.getContentsWidth(slimefunRecipeCategory);
    }

    public int getContentsWidth(SlimefunRecipe slimefunRecipe) {
        return this.type.getContentsWidth(slimefunRecipe);
    }

    public int getContentsHeight(SlimefunRecipeCategory slimefunRecipeCategory) {
        return this.type.getContentsHeight(slimefunRecipeCategory);
    }

    public int getContentsHeight(SlimefunRecipe slimefunRecipe) {
        return this.type.getContentsHeight(slimefunRecipe);
    }

    public int getDisplayWidth(SlimefunRecipeCategory slimefunRecipeCategory) {
        return getContentsWidth(slimefunRecipeCategory) + TextureUtils.PADDING * 2;
    }

    public int getDisplayWidth(SlimefunRecipe slimefunRecipe) {
        return getContentsWidth(slimefunRecipe) + TextureUtils.PADDING * 2;
    }

    public int getDisplayHeight(SlimefunRecipeCategory slimefunRecipeCategory) {
        return getContentsHeight(slimefunRecipeCategory) + TextureUtils.PADDING * 2;
    }

    public int getDisplayHeight(SlimefunRecipe slimefunRecipe) {
        return getContentsHeight(slimefunRecipe) + TextureUtils.PADDING * 2;
    }

    protected int calculateXOffset(SlimefunRecipeCategory slimefunRecipeCategory, SlimefunRecipe slimefunRecipe) {
        return (getDisplayWidth(slimefunRecipeCategory) - getContentsWidth(slimefunRecipe)) / 2;
    }

    protected int calculateXOffset(SlimefunRecipe slimefunRecipe) {
        return (getDisplayWidth(slimefunRecipe) - getContentsWidth(slimefunRecipe)) / 2;
    }

    protected int calculateYOffset(SlimefunRecipeCategory slimefunRecipeCategory, SlimefunRecipe slimefunRecipe) {
        return (getDisplayHeight(slimefunRecipeCategory) - getContentsHeight(slimefunRecipe)) / 2;
    }

    protected int calculateYOffset(SlimefunRecipe slimefunRecipe, int height) {
        return (getDisplayHeight(slimefunRecipe) - height) / 2;
    }

    public abstract static class Type {
        public static final Type ANCIENT_ALTAR = new Type() {
            @Override
            public int getContentsWidth(SlimefunRecipeCategory slimefunRecipeCategory) {
                return 140;
            }

            @Override
            public int getContentsWidth(SlimefunRecipe slimefunRecipe) {
                return 140;
            }

            @Override
            public int getContentsHeight(SlimefunRecipeCategory slimefunRecipeCategory) {
                return 90;
            }

            @Override
            public int getContentsHeight(SlimefunRecipe slimefunRecipe) {
                return 90;
            }
        };

        public static final Type PROCESS = new Type() {
            @Override
            public int getContentsWidth(SlimefunRecipeCategory slimefunRecipeCategory) {
                return TextureUtils.getProcessWidth(slimefunRecipeCategory);
            }

            @Override
            public int getContentsWidth(SlimefunRecipe slimefunRecipe) {
                return TextureUtils.getProcessWidth(slimefunRecipe);
            }

            @Override
            public int getContentsHeight(SlimefunRecipeCategory slimefunRecipeCategory) {
                return TextureUtils.getProcessHeight(slimefunRecipeCategory);
            }

            @Override
            public int getContentsHeight(SlimefunRecipe slimefunRecipe) {
                return TextureUtils.getProcessHeight(slimefunRecipe);
            }
        };

        public static final Type REACTOR = new Type() {
            @Override
            public int getContentsWidth(SlimefunRecipeCategory slimefunRecipeCategory) {
                return TextureUtils.getReactorWidth(slimefunRecipeCategory);
            }

            @Override
            public int getContentsWidth(SlimefunRecipe slimefunRecipe) {
                return TextureUtils.getReactorWidth(slimefunRecipe);
            }

            @Override
            public int getContentsHeight(SlimefunRecipeCategory slimefunRecipeCategory) {
                return TextureUtils.getReactorHeight(slimefunRecipeCategory);
            }

            @Override
            public int getContentsHeight(SlimefunRecipe slimefunRecipe) {
                return TextureUtils.getReactorHeight(slimefunRecipe);
            }
        };

        public static final Type SMELTERY = new Type() {
            @Override
            public int getContentsWidth(SlimefunRecipeCategory slimefunRecipeCategory) {
                return TextureUtils.getSmelteryWidth(slimefunRecipeCategory);
            }

            @Override
            public int getContentsWidth(SlimefunRecipe slimefunRecipe) {
                return TextureUtils.getSmelteryWidth(slimefunRecipe);
            }

            @Override
            public int getContentsHeight(SlimefunRecipeCategory slimefunRecipeCategory) {
                return TextureUtils.SLOT_SIZE * 3;
            }

            @Override
            public int getContentsHeight(SlimefunRecipe slimefunRecipe) {
                return TextureUtils.SLOT_SIZE * 3;
            }
        };

        public static Type grid(int side) {
            return new Type() {
                @Override
                public int getContentsWidth(SlimefunRecipeCategory slimefunRecipeCategory) {
                    return TextureUtils.getGridWidth(slimefunRecipeCategory, side);
                }

                @Override
                public int getContentsWidth(SlimefunRecipe slimefunRecipe) {
                    return TextureUtils.getGridWidth(slimefunRecipe, side);
                }

                @Override
                public int getContentsHeight(SlimefunRecipeCategory slimefunRecipeCategory) {
                    return TextureUtils.getGridHeight(side);
                }

                @Override
                public int getContentsHeight(SlimefunRecipe slimefunRecipe) {
                    return TextureUtils.getGridHeight(side);
                }
            };
        }

        public abstract int getContentsWidth(SlimefunRecipeCategory slimefunRecipeCategory);
        public abstract int getContentsWidth(SlimefunRecipe slimefunRecipe);
        public abstract int getContentsHeight(SlimefunRecipeCategory slimefunRecipeCategory);
        public abstract int getContentsHeight(SlimefunRecipe slimefunRecipe);
    }
}
